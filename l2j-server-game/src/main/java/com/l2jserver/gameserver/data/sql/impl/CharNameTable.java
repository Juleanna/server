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
package com.l2jserver.gameserver.data.sql.impl;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Loads name and access level for all players.
 * @since 2005/03/27
 */
public class CharNameTable {
	
	private static final Logger LOG = LoggerFactory.getLogger(CharNameTable.class);
	
	private final Map<Integer, String> _chars = new ConcurrentHashMap<>();
	
	private final Map<Integer, Integer> _accessLevels = new ConcurrentHashMap<>();
	
	protected CharNameTable() {
		if (general().cacheCharNames()) {
			loadAll();
		}
	}
	
	public final void addName(L2PcInstance player) {
		if (player != null) {
			addName(player.getObjectId(), player.getName());
			_accessLevels.put(player.getObjectId(), player.getAccessLevel().getLevel());
		}
	}
	
	private void addName(int objectId, String name) {
		if (name != null) {
			if (!name.equals(_chars.get(objectId))) {
				_chars.put(objectId, name);
			}
		}
	}
	
	public final void removeName(int objId) {
		_chars.remove(objId);
		_accessLevels.remove(objId);
	}
	
	public final int getIdByName(String name) {
		if ((name == null) || name.isEmpty()) {
			return -1;
		}
		
		for (Entry<Integer, String> entry : _chars.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(name)) {
				return entry.getKey();
			}
		}
		
		if (general().cacheCharNames()) {
			return -1;
		}
		
		int id = -1;
		int accessLevel = 0;
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT charId,accesslevel FROM characters WHERE char_name=?")) {
			ps.setString(1, name);
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					id = rs.getInt(1);
					accessLevel = rs.getInt(2);
				}
			}
		} catch (Exception ex) {
			LOG.warn("Could not check existing char name!", ex);
		}
		
		if (id > 0) {
			_chars.put(id, name);
			_accessLevels.put(id, accessLevel);
			return id;
		}
		
		return -1; // not found
	}
	
	public final String getNameById(int id) {
		if (id <= 0) {
			return null;
		}
		
		String name = _chars.get(id);
		if (name != null) {
			return name;
		}
		
		if (general().cacheCharNames()) {
			return null;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT char_name,accesslevel FROM characters WHERE charId=?")) {
			ps.setInt(1, id);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					name = rs.getString(1);
					_chars.put(id, name);
					_accessLevels.put(id, rs.getInt(2));
					return name;
				}
			}
		} catch (Exception ex) {
			LOG.warn("Could not check existing char id!", ex);
		}
		
		return null; // not found
	}
	
	public final int getAccessLevelById(int objectId) {
		if (getNameById(objectId) != null) {
			return _accessLevels.get(objectId);
		}
		
		return 0;
	}
	
	public boolean doesCharNameExist(String name) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?")) {
			ps.setString(1, name);
			try (var rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (Exception ex) {
			LOG.warn("Could not check existing player name!", ex);
		}
		return false;
	}
	
	public int getAccountCharacterCount(String account) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT COUNT(char_name) FROM characters WHERE account_name=?")) {
			ps.setString(1, account);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception ex) {
			LOG.warn("Could not check existing char count!", ex);
		}
		return 0;
	}
	
	private void loadAll() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s = con.createStatement();
			var rs = s.executeQuery("SELECT charId, char_name, accesslevel FROM characters")) {
			while (rs.next()) {
				final int id = rs.getInt(1);
				_chars.put(id, rs.getString(2));
				_accessLevels.put(id, rs.getInt(3));
			}
		} catch (Exception ex) {
			LOG.warn("Could not load char name!", ex);
		}
		LOG.info("Loaded {} char names.", _chars.size());
	}
	
	public static CharNameTable getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final CharNameTable INSTANCE = new CharNameTable();
	}
}
