/*
 * Copyright © 2004-2020 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.loginserver;

import static com.l2jserver.loginserver.config.Configuration.server;
import static com.l2jserver.loginserver.network.loginserverpackets.LoginServerFail.REASON_IP_BANNED;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.dao.ServerNameDAO;
import com.l2jserver.commons.network.BaseSendablePacket;
import com.l2jserver.commons.security.crypt.NewCrypt;
import com.l2jserver.commons.util.Util;
import com.l2jserver.loginserver.GameServerTable.GameServerInfo;
import com.l2jserver.loginserver.network.L2JGameServerPacketHandler;
import com.l2jserver.loginserver.network.L2JGameServerPacketHandler.GameServerState;
import com.l2jserver.loginserver.network.loginserverpackets.ChangePasswordResponse;
import com.l2jserver.loginserver.network.loginserverpackets.InitLS;
import com.l2jserver.loginserver.network.loginserverpackets.KickPlayer;
import com.l2jserver.loginserver.network.loginserverpackets.LoginServerFail;
import com.l2jserver.loginserver.network.loginserverpackets.RequestCharacters;

/**
 * Game Server thread.
 * @author -Wooden-
 * @author KenM
 * @version 2.6.1.0
 */
public class GameServerThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(GameServerThread.class);

	/** Верхняя граница размера одного пакета от GS (включая 2-байтовый заголовок). */
	private static final int MAX_PACKET_SIZE = 65536;

	/** Таймаут чтения для защиты от зависших GS-клиентов. */
	private static final int READ_TIMEOUT_MS = 5 * 60 * 1000;

	/**
	 * Константа начального Blowfish-ключа L2J-протокола. Используется в handshake
	 * с GameServer до того, как LS и GS обменяются реальным ключом через RSA.
	 * Не изменять без синхронного обновления game-сервера — это часть протокола.
	 */
	private static final String INITIAL_BLOWFISH_KEY = "_;v.]05-31!|+-%xT!^[$\00";

	private final Socket _connection;
	
	private InputStream _in;
	
	private OutputStream _out;
	
	private final RSAPublicKey _publicKey;
	
	private final RSAPrivateKey _privateKey;
	
	private NewCrypt _blowfish;
	
	private GameServerState _loginConnectionState = GameServerState.CONNECTED;
	
	private final String _connectionIp;
	
	private GameServerInfo _gsi;
	
	/** Authed Clients on a GameServer */
	private final Set<String> _accountsOnGameServer = ConcurrentHashMap.newKeySet();
	
	private String _connectionIPAddress;
	
	@Override
	public void run() {
		_connectionIPAddress = _connection.getInetAddress().getHostAddress();
		if (isBannedGameServerIP(_connectionIPAddress)) {
			LOG.warn("IP Address {} is on banned IP list.", _connectionIPAddress);
			forceClose(REASON_IP_BANNED);
			return;
		}

		if (_in == null || _out == null) {
			// Конструктор не смог инициализировать streams — не стартуем вовсе.
			LOG.warn("Aborting GS thread for {}: no I/O streams.", _connectionIPAddress);
			return;
		}

		try {
			sendPacket(new InitLS(_publicKey.getModulus().toByteArray()));

			for (;;) {
				final int lengthLo = _in.read();
				final int lengthHi = _in.read();
				if ((lengthLo < 0) || (lengthHi < 0) || _connection.isClosed()) {
					// EOF / соединение закрыто — выходим тихо.
					break;
				}
				final int length = (lengthHi * 256) + lengthLo;

				// Защита от вырожденных и гигантских пакетов: злонамеренный GS
				// иначе мог бы запросить new byte[-1] (NASE) или 65534 байт на
				// соединение и держать поток до таймаута.
				if (length < 2 || length > MAX_PACKET_SIZE) {
					LOG.warn("Invalid packet length {} from GS {}, closing.", length, _connectionIPAddress);
					break;
				}

				final int payloadLen = length - 2;
				final byte[] data = new byte[payloadLen];

				int receivedBytes = 0;
				while (receivedBytes < payloadLen) {
					final int newBytes = _in.read(data, receivedBytes, payloadLen - receivedBytes);
					if (newBytes < 0) {
						// EOF в середине пакета.
						break;
					}
					receivedBytes += newBytes;
				}

				if (receivedBytes != payloadLen) {
					LOG.warn("Incomplete Packet is sent to the server, closing connection. (LS)");
					break;
				}

				// decrypt if we have a key
				_blowfish.decrypt(data, 0, data.length);
				if (!NewCrypt.verifyChecksum(data)) {
					LOG.warn("Incorrect packet checksum, closing connection. (LS)");
					return;
				}

				if (server().isDebug()) {
					LOG.warn("[C]" + System.lineSeparator() + Util.printData(data));
				}

				L2JGameServerPacketHandler.handlePacket(data, this);
			}
		} catch (IOException ex) {
			final var serverName = (getServerId() != -1 ? "[" + getServerId() + "] " + ServerNameDAO.getServer(getServerId()) : "(" + _connectionIPAddress + ")");
			LOG.warn("Game Server {} lost connection!", serverName);
			broadcastToTelnet("Game Server " + serverName + " lost connection!");
		} finally {
			if (isAuthed()) {
				_gsi.setDown();

				LOG.info("Server {}[{}] is now disconnected.", ServerNameDAO.getServer(getServerId()), getServerId());
			}
			// Чистим кэш account→GS от всех игроков этого GS.
			for (String acct : _accountsOnGameServer) {
				LoginController.getInstance().onAccountLeftGameServer(acct, this);
			}
			_accountsOnGameServer.clear();
			try {
				if (!_connection.isClosed()) {
					_connection.close();
				}
			} catch (IOException ignore) {
				// уже закрыт
			}
			LoginServer.getInstance().getGameServerListener().removeGameServer(this);
			LoginServer.getInstance().getGameServerListener().removeFloodProtection(_connectionIp);
		}
	}
	
	public boolean hasAccountOnGameServer(String account) {
		return _accountsOnGameServer.contains(account);
	}
	
	public int getPlayerCount() {
		return _accountsOnGameServer.size();
	}
	
	/**
	 * Attaches a GameServerInfo to this Thread<br>
	 * <ul>
	 * <li>Updates the GameServerInfo values based on GameServerAuth packet</li>
	 * <li><b>Sets the GameServerInfo as Authed</b></li>
	 * </ul>
	 * @param gsi The GameServerInfo to be attached.
	 * @param port the port
	 * @param hosts the hosts
	 * @param maxPlayers the maximum amount of players
	 */
	public void attachGameServerInfo(GameServerInfo gsi, int port, String[] hosts, int maxPlayers) {
		setGameServerInfo(gsi);
		gsi.setGameServerThread(this);
		gsi.setPort(port);
		setGameHosts(hosts);
		gsi.setMaxPlayers(maxPlayers);
		gsi.setAuthed(true);
	}
	
	public void forceClose(int reason) {
		sendPacket(new LoginServerFail(reason));
		
		try {
			_connection.close();
		} catch (IOException ex) {
			LOG.debug("Failed disconnecting banned server, server already disconnected.");
		}
	}
	
	/**
	 * Проверка GS-IP через общий ban-список LS (раньше всегда возвращало false).
	 */
	public static boolean isBannedGameServerIP(String ipAddress) {
		try {
			return LoginController.getInstance().isBannedAddress(java.net.InetAddress.getByName(ipAddress));
		} catch (Exception ex) {
			LOG.warn("Error resolving GS address {} against ban list.", ipAddress, ex);
			return false;
		}
	}

	public GameServerThread(Socket con) {
		_connection = con;
		_connectionIp = con.getInetAddress().getHostAddress();
		InputStream in = null;
		OutputStream out = null;
		try {
			// Таймаут чтения: зависший GS иначе держит поток навсегда.
			con.setSoTimeout(READ_TIMEOUT_MS);
			con.setKeepAlive(true);
			in = _connection.getInputStream();
			out = new BufferedOutputStream(_connection.getOutputStream());
		} catch (IOException ex) {
			LOG.warn("There has been an error creating a connection!", ex);
			try {
				con.close();
			} catch (IOException ignore) {
			}
		}
		_in = in;
		_out = out;

		KeyPair pair = GameServerTable.getInstance().getKeyPair();
		_privateKey = (RSAPrivateKey) pair.getPrivate();
		_publicKey = (RSAPublicKey) pair.getPublic();
		_blowfish = new NewCrypt(INITIAL_BLOWFISH_KEY);
		setName(getClass().getSimpleName() + "-" + threadId() + "@" + _connectionIp);
		start();
	}
	
	public void sendPacket(BaseSendablePacket sl) {
		try {
			byte[] data = sl.getContent();
			NewCrypt.appendChecksum(data);
			if (server().isDebug()) {
				LOG.info("[S] {}:{}{}", sl.getClass().getSimpleName(), System.lineSeparator(), Util.printData(data));
			}
			_blowfish.crypt(data, 0, data.length);
			
			int len = data.length + 2;
			synchronized (_out) {
				_out.write(len & 0xff);
				_out.write((len >> 8) & 0xff);
				_out.write(data);
				_out.flush();
			}
		} catch (IOException ex) {
			LOG.error("There has been an error while sending packet {}!", sl.getClass().getSimpleName(), ex);
		}
	}
	
	public void broadcastToTelnet(String msg) {
		if (LoginServer.getInstance().getStatusServer() != null) {
			LoginServer.getInstance().getStatusServer().sendMessageToTelnets(msg);
		}
	}
	
	public void kickPlayer(String account) {
		sendPacket(new KickPlayer(account));
	}
	
	public void requestCharacters(String account) {
		sendPacket(new RequestCharacters(account));
	}
	
	public void ChangePasswordResponse(byte successful, String characterName, String msgToSend) {
		sendPacket(new ChangePasswordResponse(successful, characterName, msgToSend));
	}
	
	public void setGameHosts(String[] hosts) {
		LOG.info("Updated game server {}[{}] IPs.", ServerNameDAO.getServer(getServerId()), getServerId());
		
		_gsi.clearServerAddresses();
		for (int i = 0; i < hosts.length; i += 2) {
			try {
				_gsi.addServerAddress(hosts[i], hosts[i + 1]);
			} catch (Exception ex) {
				LOG.warn("There has been an error resolving host name {}!", hosts[i], ex);
			}
		}
		
		for (String s : _gsi.getServerAddresses()) {
			LOG.info(s);
		}
	}
	
	public boolean isAuthed() {
		if (getGameServerInfo() == null) {
			return false;
		}
		return getGameServerInfo().isAuthed();
	}
	
	public void setGameServerInfo(GameServerInfo gsi) {
		_gsi = gsi;
	}
	
	public GameServerInfo getGameServerInfo() {
		return _gsi;
	}
	
	public String getConnectionIpAddress() {
		return _connectionIPAddress;
	}
	
	public int getServerId() {
		if (getGameServerInfo() != null) {
			return getGameServerInfo().getId();
		}
		return -1;
	}
	
	public RSAPrivateKey getPrivateKey() {
		return _privateKey;
	}
	
	public void SetBlowFish(NewCrypt blowfish) {
		_blowfish = blowfish;
	}
	
	public void addAccountOnGameServer(String account) {
		_accountsOnGameServer.add(account);
		// Обновляем кэш в LoginController, чтобы isAccountInAnyGameServer работал O(1).
		LoginController.getInstance().onAccountJoinedGameServer(account, this);
	}

	public void removeAccountOnGameServer(String account) {
		_accountsOnGameServer.remove(account);
		LoginController.getInstance().onAccountLeftGameServer(account, this);
	}
	
	public GameServerState getLoginConnectionState() {
		return _loginConnectionState;
	}
	
	public void setLoginConnectionState(GameServerState state) {
		_loginConnectionState = state;
	}
}
