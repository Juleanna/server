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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.InstanceListManager;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

public final class CastleManager implements InstanceListManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(CastleManager.class);
	
	private final List<Castle> _castles = new ArrayList<>();
	
	private final Map<Integer, Long> _castleSiegeDate = new ConcurrentHashMap<>();
	
	private static final int[] _castleCirclets = {
		0,
		6838,
		6835,
		6839,
		6837,
		6840,
		6834,
		6836,
		8182,
		8183
	};
	
	public int findNearestCastleIndex(L2Object obj) {
		return findNearestCastleIndex(obj, Long.MAX_VALUE);
	}
	
	public int findNearestCastleIndex(L2Object obj, long maxDistance) {
		int index = getCastleIndex(obj);
		if (index < 0) {
			double distance;
			Castle castle;
			for (int i = 0; i < _castles.size(); i++) {
				castle = _castles.get(i);
				if (castle == null) {
					continue;
				}
				distance = castle.getDistance(obj);
				if (maxDistance > distance) {
					maxDistance = (long) distance;
					index = i;
				}
			}
		}
		return index;
	}
	
	public Castle getCastleById(int castleId) {
		for (Castle temp : _castles) {
			if (temp.getResidenceId() == castleId) {
				return temp;
			}
		}
		return null;
	}
	
	public Castle getCastleByOwner(L2Clan clan) {
		for (Castle temp : _castles) {
			if (temp.getOwnerId() == clan.getId()) {
				return temp;
			}
		}
		return null;
	}
	
	public Castle getCastle(String name) {
		for (Castle temp : _castles) {
			if (temp.getName().equalsIgnoreCase(name.trim())) {
				return temp;
			}
		}
		return null;
	}
	
	public Castle getCastle(int x, int y, int z) {
		for (Castle temp : _castles) {
			if (temp.checkIfInZone(x, y, z)) {
				return temp;
			}
		}
		return null;
	}
	
	public Castle getCastle(L2Object activeObject) {
		return getCastle(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public int getCastleIndex(int castleId) {
		Castle castle;
		for (int i = 0; i < _castles.size(); i++) {
			castle = _castles.get(i);
			if ((castle != null) && (castle.getResidenceId() == castleId)) {
				return i;
			}
		}
		return -1;
	}
	
	public int getCastleIndex(L2Object activeObject) {
		return getCastleIndex(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public int getCastleIndex(int x, int y, int z) {
		Castle castle;
		for (int i = 0; i < _castles.size(); i++) {
			castle = _castles.get(i);
			if ((castle != null) && castle.checkIfInZone(x, y, z)) {
				return i;
			}
		}
		return -1;
	}
	
	public List<Castle> getCastles() {
		return _castles;
	}
	
	public boolean hasOwnedCastle() {
		boolean hasOwnedCastle = false;
		for (Castle castle : _castles) {
			if (castle.getOwnerId() > 0) {
				hasOwnedCastle = true;
				break;
			}
		}
		return hasOwnedCastle;
	}
	
	public void validateTaxes(int sealStrifeOwner) {
		int maxTax = switch (sealStrifeOwner) {
			case SevenSigns.CABAL_DUSK -> 5;
			case SevenSigns.CABAL_DAWN -> 25;
			default -> 15; // no owner
		};
		for (Castle castle : _castles) {
			if (castle.getTaxPercent() > maxTax) {
				castle.setTaxPercent(maxTax);
			}
		}
	}
	
	public int getCirclet() {
		return getCircletByCastleId(1);
	}
	
	public int getCircletByCastleId(int castleId) {
		if ((castleId > 0) && (castleId < 10)) {
			return _castleCirclets[castleId];
		}
		
		return 0;
	}
	
	// remove this castle's circlets from the clan
	public void removeCirclet(L2Clan clan, int castleId) {
		for (L2ClanMember member : clan.getMembers()) {
			removeCirclet(member, castleId);
		}
	}
	
	public void removeCirclet(L2ClanMember member, int castleId) {
		if (member == null) {
			return;
		}
		L2PcInstance player = member.getPlayerInstance();
		int circletId = getCircletByCastleId(castleId);
		
		if (circletId != 0) {
			// online-player circlet removal
			if (player != null) {
				try {
					L2ItemInstance circlet = player.getInventory().getItemByItemId(circletId);
					if (circlet != null) {
						if (circlet.isEquipped()) {
							player.getInventory().unEquipItemInSlot(circlet.getLocationSlot());
						}
						player.destroyItemByItemId("CastleCircletRemoval", circletId, 1, player, true);
					}
					return;
				} catch (NullPointerException e) {
					// continue removing offline
				}
			}
			// else offline-player circlet removal
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement("DELETE FROM items WHERE owner_id = ? and item_id = ?")) {
				ps.setInt(1, member.getObjectId());
				ps.setInt(2, circletId);
				ps.execute();
			} catch (Exception ex) {
				LOG.warn("Failed to remove castle circlets offline for player {}!", player, ex);
			}
		}
	}
	
	@Override
	public void loadInstances() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s = con.createStatement();
			var rs = s.executeQuery("SELECT id FROM castle ORDER BY id")) {
			while (rs.next()) {
				_castles.add(new Castle(rs.getInt("id")));
			}
			LOG.info("Loaded {} castles.", _castles.size());
		} catch (Exception ex) {
			LOG.warn("There has been an error loading castles from database!", ex);
		}
	}
	
	@Override
	public void updateReferences() {
	}
	
	@Override
	public void activateInstances() {
		for (final Castle castle : _castles) {
			castle.activateInstance();
		}
	}
	
	public void registerSiegeDate(int castleId, long siegeDate) {
		_castleSiegeDate.put(castleId, siegeDate);
	}
	
	public int getSiegeDates(long siegeDate) {
		int count = 0;
		for (long date : _castleSiegeDate.values()) {
			if (Math.abs(date - siegeDate) < 1000) {
				count++;
			}
		}
		return count;
	}
	
	public static CastleManager getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final CastleManager _instance = new CastleManager();
	}
}