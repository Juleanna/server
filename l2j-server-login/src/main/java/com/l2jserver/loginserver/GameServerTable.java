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

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.dao.ServerNameDAO;
import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.commons.util.Rnd;
import com.l2jserver.loginserver.network.gameserverpackets.ServerStatus;
import com.l2jserver.loginserver.util.IPSubnet;

/**
 * Loads the game server names and initialize the game server tables.
 * @author KenM
 * @author Zoey76
 * @version 2.6.1.0
 */
public final class GameServerTable {
	
	private static final Logger LOG = LoggerFactory.getLogger(GameServerTable.class);
	
	private static final Map<Integer, GameServerInfo> GAME_SERVER_TABLE = new HashMap<>();
	
	private static final int KEYS_SIZE = 10;
	
	private KeyPair[] _keyPairs;
	
	public GameServerTable() {
		loadRegisteredGameServers();
		LOG.info("{}: Loaded {} registered Game Servers.", getClass().getSimpleName(), GAME_SERVER_TABLE.size());
		
		initRSAKeys();
		LOG.info("{}: Cached {} RSA keys for Game Server communication.", getClass().getSimpleName(), _keyPairs.length);
	}
	
	private void initRSAKeys() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4));
			_keyPairs = new KeyPair[KEYS_SIZE];
			for (int i = 0; i < KEYS_SIZE; i++) {
				_keyPairs[i] = keyGen.genKeyPair();
			}
		} catch (Exception e) {
			LOG.error("{}: Error loading RSA keys for Game Server communication!", getClass().getSimpleName(), e);
		}
	}
	
	private void loadRegisteredGameServers() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.createStatement();
			var rs = ps.executeQuery("SELECT * FROM gameservers")) {
			int id;
			while (rs.next()) {
				id = rs.getInt("server_id");
				GAME_SERVER_TABLE.put(id, new GameServerInfo(id, stringToHex(rs.getString("hexid"))));
			}
		} catch (Exception e) {
			LOG.error("{}: Error loading registered game servers!", getClass().getSimpleName(), e);
		}
	}
	
	/**
	 * Gets the registered game servers.
	 * @return the registered game servers
	 */
	public Map<Integer, GameServerInfo> getRegisteredGameServers() {
		return GAME_SERVER_TABLE;
	}
	
	/**
	 * Gets the registered game server by id.
	 * @param id the game server ID
	 * @return the registered game server by id
	 */
	public GameServerInfo getRegisteredGameServerById(int id) {
		return GAME_SERVER_TABLE.get(id);
	}
	
	/**
	 * Checks for registered game server on id.
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean hasRegisteredGameServerOnId(int id) {
		return GAME_SERVER_TABLE.containsKey(id);
	}
	
	/**
	 * Register with first available id.
	 * @param gsi the game server information DTO
	 * @return true, if successful
	 */
	public boolean registerWithFirstAvailableId(GameServerInfo gsi) {
		// avoid two servers registering with the same "free" id
		synchronized (GAME_SERVER_TABLE) {
			for (Integer serverId : ServerNameDAO.getServers().keySet()) {
				if (!GAME_SERVER_TABLE.containsKey(serverId)) {
					GAME_SERVER_TABLE.put(serverId, gsi);
					gsi.setId(serverId);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Register a game server.
	 * @param id the id
	 * @param gsi the gsi
	 * @return true, if successful
	 */
	public boolean register(int id, GameServerInfo gsi) {
		// avoid two servers registering with the same id
		synchronized (GAME_SERVER_TABLE) {
			if (!GAME_SERVER_TABLE.containsKey(id)) {
				GAME_SERVER_TABLE.put(id, gsi);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Wrapper method.
	 * @param gsi the game server info DTO.
	 */
	public void registerServerOnDB(GameServerInfo gsi) {
		registerServerOnDB(gsi.getHexId(), gsi.getId(), gsi.getExternalHost());
	}
	
	/**
	 * Register server on db.
	 * @param hexId the hex id
	 * @param id the id
	 * @param externalHost the external host
	 */
	public void registerServerOnDB(byte[] hexId, int id, String externalHost) {
		register(id, new GameServerInfo(id, hexId));
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("INSERT INTO gameservers (hexid,server_id,host) values (?,?,?)")) {
			ps.setString(1, hexToString(hexId));
			ps.setInt(2, id);
			ps.setString(3, externalHost);
			ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("{}: Error while saving game server!", getClass().getSimpleName(), e);
		}
	}
	
	/**
	 * Gets the key pair.
	 * @return a random key pair.
	 */
	public KeyPair getKeyPair() {
		return _keyPairs[Rnd.nextInt(10)];
	}
	
	/**
	 * String to hex.
	 * @param string the string to convert.
	 * @return return the hex representation.
	 */
	private byte[] stringToHex(String string) {
		return new BigInteger(string, 16).toByteArray();
	}
	
	/**
	 * Hex to string.
	 * @param hex the hex value to convert.
	 * @return the string representation.
	 */
	private String hexToString(byte[] hex) {
		if (hex == null) {
			return "null";
		}
		return new BigInteger(hex).toString(16);
	}
	
	/**
	 * The Class GameServerInfo.
	 */
	public static class GameServerInfo {
		// auth
		private int _id;
		private final byte[] _hexId;
		private boolean _isAuthed;
		// status
		private GameServerThread _gst;
		private int _status;
		// network
		private final ArrayList<GameServerAddress> _addresses = new ArrayList<>(5);
		private int _port;
		// config
		private final boolean _isPvp = true;
		private int _serverType;
		private int _ageLimit;
		private boolean _isShowingBrackets;
		private int _maxPlayers;
		
		/**
		 * Instantiates a new game server info.
		 * @param id the id
		 * @param hexId the hex id
		 * @param gst the gst
		 */
		public GameServerInfo(int id, byte[] hexId, GameServerThread gst) {
			_id = id;
			_hexId = hexId;
			_gst = gst;
			_status = ServerStatus.STATUS_DOWN;
		}
		
		/**
		 * Instantiates a new game server info.
		 * @param id the id
		 * @param hexId the hex id
		 */
		public GameServerInfo(int id, byte[] hexId) {
			this(id, hexId, null);
		}
		
		/**
		 * Sets the id.
		 * @param id the new id
		 */
		public void setId(int id) {
			_id = id;
		}
		
		/**
		 * Gets the id.
		 * @return the id
		 */
		public int getId() {
			return _id;
		}
		
		/**
		 * Gets the hex id.
		 * @return the hex id
		 */
		public byte[] getHexId() {
			return _hexId;
		}
		
		public String getName() {
			return ServerNameDAO.getServer(_id);
		}
		
		/**
		 * Sets the authed.
		 * @param isAuthed the new authed
		 */
		public void setAuthed(boolean isAuthed) {
			_isAuthed = isAuthed;
		}
		
		/**
		 * Checks if is authed.
		 * @return true, if is authed
		 */
		public boolean isAuthed() {
			return _isAuthed;
		}
		
		/**
		 * Sets the game server thread.
		 * @param gst the new game server thread
		 */
		public void setGameServerThread(GameServerThread gst) {
			_gst = gst;
		}
		
		/**
		 * Gets the game server thread.
		 * @return the game server thread
		 */
		public GameServerThread getGameServerThread() {
			return _gst;
		}
		
		/**
		 * Sets the status.
		 * @param status the new status
		 */
		public void setStatus(int status) {
			_status = status;
		}
		
		/**
		 * Gets the status.
		 * @return the status
		 */
		public int getStatus() {
			return _status;
		}
		
		public String getStatusName() {
			return switch (_status) {
				case 0 -> "Auto";
				case 1 -> "Good";
				case 2 -> "Normal";
				case 3 -> "Full";
				case 4 -> "Down";
				case 5 -> "GM Only";
				default -> "Unknown";
			};
		}
		
		/**
		 * Gets the current player count.
		 * @return the current player count
		 */
		public int getCurrentPlayerCount() {
			if (_gst == null) {
				return 0;
			}
			return _gst.getPlayerCount();
		}
		
		/**
		 * Gets the external host.
		 * @return the external host
		 */
		public String getExternalHost() {
			try {
				return getServerAddress(InetAddress.getByName("0.0.0.0"));
			} catch (Exception e) {
				
			}
			return null;
		}
		
		/**
		 * Gets the port.
		 * @return the port
		 */
		public int getPort() {
			return _port;
		}
		
		/**
		 * Sets the port.
		 * @param port the new port
		 */
		public void setPort(int port) {
			_port = port;
		}
		
		/**
		 * Sets the max players.
		 * @param maxPlayers the new max players
		 */
		public void setMaxPlayers(int maxPlayers) {
			_maxPlayers = maxPlayers;
		}
		
		/**
		 * Gets the max players.
		 * @return the max players
		 */
		public int getMaxPlayers() {
			return _maxPlayers;
		}
		
		/**
		 * Checks if is pvp.
		 * @return true, if is pvp
		 */
		public boolean isPvp() {
			return _isPvp;
		}
		
		/**
		 * Sets the age limit.
		 * @param val the new age limit
		 */
		public void setAgeLimit(int val) {
			_ageLimit = val;
		}
		
		/**
		 * Gets the age limit.
		 * @return the age limit
		 */
		public int getAgeLimit() {
			return _ageLimit;
		}
		
		/**
		 * Sets the server type.
		 * @param val the new server type
		 */
		public void setServerType(int val) {
			_serverType = val;
		}
		
		/**
		 * Gets the server type.
		 * @return the server type
		 */
		public int getServerType() {
			return _serverType;
		}
		
		/**
		 * Sets the showing brackets.
		 * @param val the new showing brackets
		 */
		public void setShowingBrackets(boolean val) {
			_isShowingBrackets = val;
		}
		
		/**
		 * Checks if it is showing brackets.
		 * @return true, if it is showing brackets
		 */
		public boolean isShowingBrackets() {
			return _isShowingBrackets;
		}
		
		/**
		 * Sets the down.
		 */
		public void setDown() {
			setAuthed(false);
			setPort(0);
			setGameServerThread(null);
			setStatus(ServerStatus.STATUS_DOWN);
		}
		
		/**
		 * Adds the server address.
		 * @param subnet the subnet
		 * @param addr the addr
		 * @throws UnknownHostException the unknown host exception
		 */
		public void addServerAddress(String subnet, String addr) throws UnknownHostException {
			_addresses.add(new GameServerAddress(subnet, addr));
		}
		
		/**
		 * Gets the server address.
		 * @param addr the addr
		 * @return the server address
		 */
		public String getServerAddress(InetAddress addr) {
			for (GameServerAddress a : _addresses) {
				if (a.equals(addr)) {
					return a.getServerAddress();
				}
			}
			return null; // should not happen
		}
		
		/**
		 * Gets the server addresses.
		 * @return the server addresses
		 */
		public String[] getServerAddresses() {
			String[] result = new String[_addresses.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = _addresses.get(i).toString();
			}
			
			return result;
		}
		
		/**
		 * Clear server addresses.
		 */
		public void clearServerAddresses() {
			_addresses.clear();
		}
		
		/**
		 * The Class GameServerAddress.
		 */
		private static class GameServerAddress extends IPSubnet {
			private final String _serverAddress;
			
			/**
			 * Instantiates a new game server address.
			 * @param subnet the subnet
			 * @param address the address
			 * @throws UnknownHostException the unknown host exception
			 */
			public GameServerAddress(String subnet, String address) throws UnknownHostException {
				super(subnet);
				_serverAddress = address;
			}
			
			/**
			 * Gets the server address.
			 * @return the server address
			 */
			public String getServerAddress() {
				return _serverAddress;
			}
			
			@Override
			public String toString() {
				return _serverAddress + super.toString();
			}
		}
	}
	
	/**
	 * Gets the single instance of GameServerTable.
	 * @return single instance of GameServerTable
	 */
	public static GameServerTable getInstance() {
		return SingletonHolder._instance;
	}
	
	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder {
		protected static final GameServerTable _instance = new GameServerTable();
	}
}
