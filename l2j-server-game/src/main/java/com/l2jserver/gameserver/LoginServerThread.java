/*
 * Copyright © 2004-2023 L2J Server
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
package com.l2jserver.gameserver;

import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.hexId;
import static com.l2jserver.gameserver.config.Configuration.ip;
import static com.l2jserver.gameserver.config.Configuration.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.commons.network.BaseSendablePacket;
import com.l2jserver.commons.security.crypt.NewCrypt;
import com.l2jserver.commons.util.Rnd;
import com.l2jserver.commons.util.Util;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.config.HexIdConfiguration;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.L2GameClient.GameClientState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.gameserverpackets.AuthRequest;
import com.l2jserver.gameserver.network.gameserverpackets.BlowFishKey;
import com.l2jserver.gameserver.network.gameserverpackets.ChangeAccessLevel;
import com.l2jserver.gameserver.network.gameserverpackets.ChangePassword;
import com.l2jserver.gameserver.network.gameserverpackets.PlayerAuthRequest;
import com.l2jserver.gameserver.network.gameserverpackets.PlayerInGame;
import com.l2jserver.gameserver.network.gameserverpackets.PlayerLogout;
import com.l2jserver.gameserver.network.gameserverpackets.PlayerTracert;
import com.l2jserver.gameserver.network.gameserverpackets.ReplyCharacters;
import com.l2jserver.gameserver.network.gameserverpackets.SendMail;
import com.l2jserver.gameserver.network.gameserverpackets.ServerStatus;
import com.l2jserver.gameserver.network.gameserverpackets.TempBan;
import com.l2jserver.gameserver.network.loginserverpackets.AuthResponse;
import com.l2jserver.gameserver.network.loginserverpackets.ChangePasswordResponse;
import com.l2jserver.gameserver.network.loginserverpackets.InitLS;
import com.l2jserver.gameserver.network.loginserverpackets.KickPlayer;
import com.l2jserver.gameserver.network.loginserverpackets.LoginServerFail;
import com.l2jserver.gameserver.network.loginserverpackets.PlayerAuthResponse;
import com.l2jserver.gameserver.network.loginserverpackets.RequestCharacters;
import com.l2jserver.gameserver.network.serverpackets.CharSelectionInfo;
import com.l2jserver.gameserver.network.serverpackets.LoginFail;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

public class LoginServerThread extends Thread {
	
	protected static final Logger LOG = LoggerFactory.getLogger(LoginServerThread.class);
	
	protected static final Logger LOG_ACCOUNTING = LoggerFactory.getLogger("accounting");
	
	private static final int TEENAGER = 15;
	
	private static final int ADULT = 18;
	
	private final String _hostname;
	private final int _port;
	private final int _gamePort;
	private Socket _loginSocket;
	private OutputStream _out;
	
	/**
	 * The BlowFish engine used to encrypt packets<br>
	 * It is first initialized with a unified key:<br>
	 * "_;v.]05-31!|+-%xT!^[$\00"<br>
	 * <br>
	 * and then after handshake, with a new key sent by<br>
	 * login server during the handshake. This new key is stored<br>
	 * in blowfishKey
	 */
	private NewCrypt _blowfish;
	private final byte[] _hexID;
	private final boolean _acceptAlternate;
	private final int _requestID;
	private final boolean _reserveHost;
	private int _maxPlayer;
	private final List<WaitingClient> _waitingClients;
	private final Map<String, L2GameClient> _accountsInGameServer = new ConcurrentHashMap<>();
	private int _status;
	private String _serverName;
	private final List<String> _subnets;
	private final List<String> _hosts;
	
	protected LoginServerThread() {
		super("LoginServerThread");
		_port = server().getLoginPort();
		_gamePort = server().getPort();
		_hostname = server().getLoginHost();
		if (hexId().getHexID() == null) {
			_hexID = Util.generateHex(16);
			_requestID = server().getRequestServerId();
			hexId().setProperty("ServerID", String.valueOf(_requestID));
		} else {
			_hexID = hexId().getHexID().toByteArray();
			_requestID = hexId().getServerID();
		}
		_acceptAlternate = server().acceptAlternateId();
		_reserveHost = server().reserveHostOnLogin();
		_subnets = ip().getSubnets();
		_hosts = ip().getHosts();
		_waitingClients = new CopyOnWriteArrayList<>();
		_maxPlayer = server().getMaxOnlineUsers();
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			int lengthHi;
			int lengthLo;
			int length;
			boolean checksumOk;
			try {
				// Connection
				LOG.info("Connecting to login server on {}:{}", _hostname, _port);
				_loginSocket = new Socket(_hostname, _port);
				InputStream in = _loginSocket.getInputStream();
				_out = new BufferedOutputStream(_loginSocket.getOutputStream());
				
				// init Blowfish
				byte[] blowfishKey = Util.generateHex(40);
				// Protect the new blowfish key what cannot begin with zero
				if (blowfishKey[0] == 0) {
					blowfishKey[0] = (byte) Rnd.get(32, 64);
				}
				_blowfish = new NewCrypt("_;v.]05-31!|+-%xT!^[$\00");
				while (!isInterrupted()) {
					lengthLo = in.read();
					lengthHi = in.read();
					length = (lengthHi * 256) + lengthLo;
					
					if (lengthHi < 0) {
						LOG.info("LoginServerThread: Login terminated the connection.");
						break;
					}
					
					byte[] incoming = new byte[length - 2];
					
					int receivedBytes = 0;
					int newBytes = 0;
					int left = length - 2;
					while ((newBytes != -1) && (receivedBytes < (length - 2))) {
						newBytes = in.read(incoming, receivedBytes, left);
						receivedBytes = receivedBytes + newBytes;
						left -= newBytes;
					}
					
					if (receivedBytes != (length - 2)) {
						LOG.warn("Incomplete Packet is sent to the server, closing connection.(LS)");
						break;
					}
					
					// decrypt if we have a key
					_blowfish.decrypt(incoming, 0, incoming.length);
					checksumOk = NewCrypt.verifyChecksum(incoming);
					
					if (!checksumOk) {
						LOG.warn("Incorrect packet checksum, ignoring packet (LS)");
						break;
					}
					
					int packetType = incoming[0] & 0xff;
					// send the blowfish key through the rsa encryption
					// now, only accept packet with the new encryption
					// login will close the connection here
					switch (packetType) {
						case 0x00 -> {
							InitLS init = new InitLS(incoming);
							RSAPublicKey publicKey;
							try {
								KeyFactory kfac = KeyFactory.getInstance("RSA");
								BigInteger modulus = new BigInteger(init.getRSAKey());
								RSAPublicKeySpec kspec1 = new RSAPublicKeySpec(modulus, RSAKeyGenParameterSpec.F4);
								publicKey = (RSAPublicKey) kfac.generatePublic(kspec1);
							} catch (GeneralSecurityException e) {
								LOG.warn("Trouble while init the public key send by login");
								break;
							}
							sendPacket(new BlowFishKey(blowfishKey, publicKey));
							_blowfish = new NewCrypt(blowfishKey);
							sendPacket(new AuthRequest(_requestID, _acceptAlternate, _hexID, _gamePort, _reserveHost, _maxPlayer, _subnets, _hosts));
						}
						case 0x01 -> {
							LoginServerFail lsf = new LoginServerFail(incoming);
							LOG.info("Damn! Registration Failed: {}", lsf.getReasonString());
						}
						case 0x02 -> {
							AuthResponse aresp = new AuthResponse(incoming);
							int serverID = aresp.getServerId();
							_serverName = aresp.getServerName();
							saveHexid(serverID, hexToString(_hexID));
							LOG.info("Registered on login as Server {}: {}", serverID, _serverName);
							ServerStatus st = new ServerStatus();
							if (general().getServerListBrackets()) {
								st.addAttribute(ServerStatus.SERVER_LIST_SQUARE_BRACKET, ServerStatus.ON);
							} else {
								st.addAttribute(ServerStatus.SERVER_LIST_SQUARE_BRACKET, ServerStatus.OFF);
							}
							st.addAttribute(ServerStatus.SERVER_TYPE, general().getServerListType());
							if (general().serverGMOnly()) {
								st.addAttribute(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_GM_ONLY);
							} else {
								st.addAttribute(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_AUTO);
							}
							if (general().getServerListAge() == TEENAGER) {
								st.addAttribute(ServerStatus.SERVER_AGE, ServerStatus.SERVER_AGE_15);
							} else if (general().getServerListAge() == ADULT) {
								st.addAttribute(ServerStatus.SERVER_AGE, ServerStatus.SERVER_AGE_18);
							} else {
								st.addAttribute(ServerStatus.SERVER_AGE, ServerStatus.SERVER_AGE_ALL);
							}
							sendPacket(st);
							if (L2World.getInstance().getAllPlayersCount() > 0) {
								final List<String> playerList = new ArrayList<>();
								for (L2PcInstance player : L2World.getInstance().getPlayers()) {
									playerList.add(player.getAccountName());
								}
								sendPacket(new PlayerInGame(playerList));
							}
						}
						case 0x03 -> {
							PlayerAuthResponse par = new PlayerAuthResponse(incoming);
							String account = par.getAccount();
							WaitingClient wcToRemove = null;
							synchronized (_waitingClients) {
								for (WaitingClient wc : _waitingClients) {
									if (wc.account.equals(account)) {
										wcToRemove = wc;
									}
								}
							}
							if (wcToRemove != null) {
								if (par.isAuthed()) {
									PlayerInGame pig = new PlayerInGame(par.getAccount());
									sendPacket(pig);
									wcToRemove.gameClient.setState(GameClientState.AUTHED);
									wcToRemove.gameClient.setSessionId(wcToRemove.session);
									CharSelectionInfo cl = new CharSelectionInfo(wcToRemove.account, wcToRemove.gameClient.getSessionId().playOkID1);
									wcToRemove.gameClient.getConnection().sendPacket(cl);
									wcToRemove.gameClient.setCharSelection(cl.getCharInfo());
								} else {
									LOG.warn("Session key is not correct. Closing connection for account {}.", wcToRemove.account);
									// wcToRemove.gameClient.getConnection().sendPacket(new LoginFail(LoginFail.SYSTEM_ERROR_LOGIN_LATER));
									wcToRemove.gameClient.close(new LoginFail(LoginFail.SYSTEM_ERROR_LOGIN_LATER));
									_accountsInGameServer.remove(wcToRemove.account);
								}
								_waitingClients.remove(wcToRemove);
							}
						}
						case 0x04 -> {
							KickPlayer kp = new KickPlayer(incoming);
							doKickPlayer(kp.getAccount());
						}
						case 0x05 -> {
							RequestCharacters rc = new RequestCharacters(incoming);
							getCharsOnServer(rc.getAccount());
						}
						case 0x06 -> new ChangePasswordResponse(incoming);
					}
				}
			} catch (UnknownHostException e) {
				LOG.warn("Unknown host!", e);
			} catch (SocketException e) {
				LOG.warn("LoginServer not avaible, trying to reconnect...");
			} catch (IOException e) {
				LOG.warn("Disconnected from Login, Trying to reconnect!", e);
			} finally {
				try {
					_loginSocket.close();
					if (isInterrupted()) {
						return;
					}
				} catch (Exception e) {
				}
			}
			
			try {
				Thread.sleep(5000); // 5 seconds tempo.
			} catch (InterruptedException e) {
				return; // never swallow an interrupt!
			}
		}
	}
	
	/**
	 * Adds the waiting client and send request.
	 * @param acc the account
	 * @param client the game client
	 * @param key the session key
	 */
	public void addWaitingClientAndSendRequest(String acc, L2GameClient client, SessionKey key) {
		WaitingClient wc = new WaitingClient(acc, client, key);
		synchronized (_waitingClients) {
			_waitingClients.add(wc);
		}
		PlayerAuthRequest par = new PlayerAuthRequest(acc, key);
		try {
			sendPacket(par);
		} catch (IOException e) {
			LOG.warn("Error while sending player auth request!");
		}
	}
	
	/**
	 * Removes the waiting client.
	 * @param client the client
	 */
	public void removeWaitingClient(L2GameClient client) {
		WaitingClient toRemove = null;
		synchronized (_waitingClients) {
			for (WaitingClient c : _waitingClients) {
				if (c.gameClient == client) {
					toRemove = c;
				}
			}
			if (toRemove != null) {
				_waitingClients.remove(toRemove);
			}
		}
	}
	
	/**
	 * Send logout for the given account.
	 * @param account the account
	 */
	public void sendLogout(String account) {
		if (account == null) {
			return;
		}
		PlayerLogout pl = new PlayerLogout(account);
		try {
			sendPacket(pl);
		} catch (IOException e) {
			LOG.warn("Error while sending logout packet to login!");
		} finally {
			_accountsInGameServer.remove(account);
		}
	}
	
	/**
	 * Adds the game server login.
	 * @param account the account
	 * @param client the client
	 * @return {@code true} if account was not already logged in, {@code false} otherwise
	 */
	public boolean addGameServerLogin(String account, L2GameClient client) {
		return _accountsInGameServer.putIfAbsent(account, client) == null;
	}
	
	/**
	 * Send access level.
	 * @param account the account
	 * @param level the access level
	 */
	public void sendAccessLevel(String account, int level) {
		ChangeAccessLevel cal = new ChangeAccessLevel(account, level);
		try {
			sendPacket(cal);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send client tracert.
	 * @param account the account
	 * @param address the address
	 */
	public void sendClientTracert(String account, String[] address) {
		PlayerTracert ptc = new PlayerTracert(account, address[0], address[1], address[2], address[3], address[4]);
		try {
			sendPacket(ptc);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send mail.
	 * @param account the account
	 * @param mailId the mail id
	 * @param args the args
	 */
	public void sendMail(String account, String mailId, String... args) {
		SendMail sem = new SendMail(account, mailId, args);
		try {
			sendPacket(sem);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send temp ban.
	 * @param account the account
	 * @param ip the ip
	 * @param time the time
	 */
	public void sendTempBan(String account, String ip, long time) {
		TempBan tbn = new TempBan(account, ip, time);
		try {
			sendPacket(tbn);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Hex to string.
	 * @param hex the hex value
	 * @return the hex value as string
	 */
	private String hexToString(byte[] hex) {
		return new BigInteger(hex).toString(16);
	}
	
	/**
	 * Kick player for the given account.
	 * @param account the account
	 */
	public void doKickPlayer(String account) {
		L2GameClient client = _accountsInGameServer.get(account);
		if (client != null) {
			LOG_ACCOUNTING.warn("Kicked by login: {}", client);
			client.setAdditionalClosePacket(SystemMessage.getSystemMessage(SystemMessageId.ANOTHER_LOGIN_WITH_ACCOUNT));
			client.closeNow();
		}
	}
	
	/**
	 * Gets the chars on server.
	 * @param account the account
	 */
	private void getCharsOnServer(String account) {
		
		int chars = 0;
		List<Long> charToDel = new ArrayList<>();
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT deletetime FROM characters WHERE account_name=?")) {
			ps.setString(1, account);
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					chars++;
					long delTime = rs.getLong("deletetime");
					if (delTime != 0) {
						charToDel.add(delTime);
					}
				}
			}
		} catch (SQLException e) {
			LOG.warn("Exception: getCharsOnServer!", e);
		}
		
		ReplyCharacters rec = new ReplyCharacters(account, chars, charToDel);
		try {
			sendPacket(rec);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send packet.
	 * @param sl the sendable packet
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void sendPacket(BaseSendablePacket sl) throws IOException {
		byte[] data = sl.getContent();
		NewCrypt.appendChecksum(data);
		_blowfish.crypt(data, 0, data.length);
		
		int len = data.length + 2;
		synchronized (_out) // avoids tow threads writing in the mean time
		{
			_out.write(len & 0xff);
			_out.write((len >> 8) & 0xff);
			_out.write(data);
			_out.flush();
		}
	}
	
	/**
	 * Sets the max player.
	 * @param maxPlayer The maxPlayer to set.
	 */
	public void setMaxPlayer(int maxPlayer) {
		sendServerStatus(ServerStatus.MAX_PLAYERS, maxPlayer);
		_maxPlayer = maxPlayer;
	}
	
	/**
	 * Gets the max player.
	 * @return Returns the maxPlayer.
	 */
	public int getMaxPlayer() {
		return _maxPlayer;
	}
	
	/**
	 * Send server status.
	 * @param id the id
	 * @param value the value
	 */
	public void sendServerStatus(int id, int value) {
		ServerStatus ss = new ServerStatus();
		ss.addAttribute(id, value);
		try {
			sendPacket(ss);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send Server Type Config to LS.
	 */
	public void sendServerType() {
		ServerStatus ss = new ServerStatus();
		ss.addAttribute(ServerStatus.SERVER_TYPE, general().getServerListType());
		try {
			sendPacket(ss);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send change password.
	 * @param accountName the account name
	 * @param charName the char name
	 * @param oldpass the old pass
	 * @param newpass the new pass
	 */
	public void sendChangePassword(String accountName, String charName, String oldpass, String newpass) {
		ChangePassword cp = new ChangePassword(accountName, charName, oldpass, newpass);
		try {
			sendPacket(cp);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Gets the status string.
	 * @return the status string
	 */
	public String getStatusString() {
		return ServerStatus.STATUS_STRING[_status];
	}
	
	/**
	 * Gets the server name.
	 * @return the server name.
	 */
	public String getServerName() {
		return _serverName;
	}
	
	/**
	 * Sets the server status.
	 * @param status the new server status
	 */
	public void setServerStatus(int status) {
		switch (status) {
			case ServerStatus.STATUS_AUTO -> {
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_AUTO);
				_status = status;
			}
			case ServerStatus.STATUS_DOWN -> {
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_DOWN);
				_status = status;
			}
			case ServerStatus.STATUS_FULL -> {
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_FULL);
				_status = status;
			}
			case ServerStatus.STATUS_GM_ONLY -> {
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_GM_ONLY);
				_status = status;
			}
			case ServerStatus.STATUS_GOOD -> {
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_GOOD);
				_status = status;
			}
			case ServerStatus.STATUS_NORMAL -> {
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_NORMAL);
				_status = status;
			}
			default -> throw new IllegalArgumentException("Status does not exists:" + status);
		}
	}
	
	public L2GameClient getClient(String name) {
		return name != null ? _accountsInGameServer.get(name) : null;
	}
	
	public static class SessionKey {
		public int playOkID1;
		public int playOkID2;
		public int loginOkID1;
		public int loginOkID2;
		
		/**
		 * Instantiates a new session key.
		 * @param loginOK1 the login o k1
		 * @param loginOK2 the login o k2
		 * @param playOK1 the play o k1
		 * @param playOK2 the play o k2
		 */
		public SessionKey(int loginOK1, int loginOK2, int playOK1, int playOK2) {
			playOkID1 = playOK1;
			playOkID2 = playOK2;
			loginOkID1 = loginOK1;
			loginOkID2 = loginOK2;
		}
		
		@Override
		public String toString() {
			return "PlayOk: " + playOkID1 + " " + playOkID2 + " LoginOk:" + loginOkID1 + " " + loginOkID2;
		}
	}
	
	private static class WaitingClient {
		public String account;
		public L2GameClient gameClient;
		public SessionKey session;
		
		/**
		 * Instantiates a new waiting client.
		 * @param acc the acc
		 * @param client the client
		 * @param key the key
		 */
		public WaitingClient(String acc, L2GameClient client, SessionKey key) {
			account = acc;
			gameClient = client;
			session = key;
		}
	}
	
	/**
	 * Save hexadecimal ID of the server in the L2Properties file.
	 * @param serverId the ID of the server whose hexId to save
	 * @param hexId the hexadecimal ID to store
	 */
	public static void saveHexid(int serverId, String hexId) {
		Path hexIdFilePath = Configuration.getCustomOrDefaultPath(HexIdConfiguration.FILENAME);
		
		hexId().setProperty(HexIdConfiguration.SERVERID_KEY, String.valueOf(serverId));
		hexId().setProperty(HexIdConfiguration.HEXID_KEY, hexId);
		
		try (OutputStream out = Files.newOutputStream(hexIdFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			hexId().store(out, "the hexID to auth into login");
			LOG.info("Saved {}.", hexIdFilePath);
		} catch (Exception ex) {
			LOG.warn("Failed to save {}.", hexIdFilePath, ex);
		}
	}
	
	/**
	 * Gets the single instance of LoginServerThread.
	 * @return single instance of LoginServerThread
	 */
	public static LoginServerThread getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final LoginServerThread _instance = new LoginServerThread();
	}
}
