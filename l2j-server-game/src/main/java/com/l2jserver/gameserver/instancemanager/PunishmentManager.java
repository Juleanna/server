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
package com.l2jserver.gameserver.instancemanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.model.holders.PunishmentHolder;
import com.l2jserver.gameserver.model.punishment.PunishmentAffect;
import com.l2jserver.gameserver.model.punishment.PunishmentTask;
import com.l2jserver.gameserver.model.punishment.PunishmentType;

/**
 * Punishment Manager.
 * @author UnAfraid
 */
public final class PunishmentManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(PunishmentManager.class);
	
	private final Map<PunishmentAffect, PunishmentHolder> _tasks = new ConcurrentHashMap<>();
	
	protected PunishmentManager() {
		load();
	}
	
	private void load() {
		// Initiate task holders.
		for (PunishmentAffect affect : PunishmentAffect.values()) {
			_tasks.put(affect, new PunishmentHolder());
		}
		
		int initiated = 0;
		int expired = 0;
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var st = con.createStatement();
			var rs = st.executeQuery("SELECT * FROM punishments")) {
			while (rs.next()) {
				final int id = rs.getInt("id");
				final String key = rs.getString("key");
				final PunishmentAffect affect = PunishmentAffect.getByName(rs.getString("affect"));
				final PunishmentType type = PunishmentType.getByName(rs.getString("type"));
				final long expirationTime = rs.getLong("expiration");
				final String reason = rs.getString("reason");
				final String punishedBy = rs.getString("punishedBy");
				if ((type != null) && (affect != null)) {
					if ((expirationTime > 0) && (System.currentTimeMillis() > expirationTime)) // expired task.
					{
						expired++;
					} else {
						initiated++;
						_tasks.get(affect).addPunishment(new PunishmentTask(id, key, affect, type, expirationTime, reason, punishedBy, true));
					}
				}
			}
		} catch (Exception ex) {
			LOG.warn("There has been an error while loading punishments!", ex);
		}
		
		LOG.info("Loaded {} active and {} expired punishments.", initiated, expired);
	}
	
	public void startPunishment(PunishmentTask task) {
		_tasks.get(task.getAffect()).addPunishment(task);
	}
	
	public void stopPunishment(Object key, PunishmentAffect affect, PunishmentType type) {
		final PunishmentTask task = getPunishment(key, affect, type);
		if (task != null) {
			_tasks.get(affect).stopPunishment(task);
		}
	}
	
	public boolean hasPunishment(Object key, PunishmentAffect affect, PunishmentType type) {
		final PunishmentHolder holder = _tasks.get(affect);
		return holder.hasPunishment(String.valueOf(key), type);
	}
	
	public long getPunishmentExpiration(Object key, PunishmentAffect affect, PunishmentType type) {
		final PunishmentTask p = getPunishment(key, affect, type);
		return p != null ? p.getExpirationTime() : 0;
	}
	
	private PunishmentTask getPunishment(Object key, PunishmentAffect affect, PunishmentType type) {
		return _tasks.get(affect).getPunishment(String.valueOf(key), type);
	}
	
	/**
	 * Gets the single instance of {@code PunishmentManager}.
	 * @return single instance of {@code PunishmentManager}
	 */
	public static PunishmentManager getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final PunishmentManager _instance = new PunishmentManager();
	}
}
