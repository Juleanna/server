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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.InstanceListManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.entity.Fort;

public final class FortManager implements InstanceListManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(FortManager.class);
	
	private final List<Fort> _forts = new CopyOnWriteArrayList<>();
	
	public int findNearestFortIndex(L2Object obj) {
		return findNearestFortIndex(obj, Long.MAX_VALUE);
	}
	
	public int findNearestFortIndex(L2Object obj, long maxDistance) {
		int index = getFortIndex(obj);
		if (index < 0) {
			double distance;
			Fort fort;
			for (int i = 0; i < _forts.size(); i++) {
				fort = _forts.get(i);
				if (fort == null) {
					continue;
				}
				distance = fort.getDistance(obj);
				if (maxDistance > distance) {
					maxDistance = (long) distance;
					index = i;
				}
			}
		}
		return index;
	}
	
	public Fort getFortById(int fortId) {
		for (Fort f : _forts) {
			if (f.getResidenceId() == fortId) {
				return f;
			}
		}
		return null;
	}
	
	public Fort getFortByOwner(L2Clan clan) {
		for (Fort f : _forts) {
			if (f.getOwnerClan() == clan) {
				return f;
			}
		}
		return null;
	}
	
	public Fort getFort(String name) {
		for (Fort f : _forts) {
			if (f.getName().equalsIgnoreCase(name.trim())) {
				return f;
			}
		}
		return null;
	}
	
	public Fort getFort(int x, int y, int z) {
		for (Fort f : _forts) {
			if (f.checkIfInZone(x, y, z)) {
				return f;
			}
		}
		return null;
	}
	
	public Fort getFort(L2Object activeObject) {
		return getFort(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public int getFortIndex(int fortId) {
		Fort fort;
		for (int i = 0; i < _forts.size(); i++) {
			fort = _forts.get(i);
			if ((fort != null) && (fort.getResidenceId() == fortId)) {
				return i;
			}
		}
		return -1;
	}
	
	public int getFortIndex(L2Object activeObject) {
		return getFortIndex(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public int getFortIndex(int x, int y, int z) {
		Fort fort;
		for (int i = 0; i < _forts.size(); i++) {
			fort = _forts.get(i);
			if ((fort != null) && fort.checkIfInZone(x, y, z)) {
				return i;
			}
		}
		return -1;
	}
	
	public List<Fort> getForts() {
		return _forts;
	}
	
	@Override
	public void loadInstances() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s = con.createStatement();
			var rs = s.executeQuery("SELECT id FROM fort ORDER BY id")) {
			while (rs.next()) {
				_forts.add(new Fort(rs.getInt("id")));
			}
			
			LOG.info("Loaded {} fortress.", _forts.size());
			
			for (Fort fort : _forts) {
				fort.getSiege().getSiegeGuardManager().loadSiegeGuard();
			}
		} catch (Exception ex) {
			LOG.warn("There has been an error loading fort instances!", ex);
		}
	}
	
	@Override
	public void updateReferences() {
	}
	
	@Override
	public void activateInstances() {
		for (final Fort fort : _forts) {
			fort.activateInstance();
		}
	}
	
	public static FortManager getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final FortManager _instance = new FortManager();
	}
}
