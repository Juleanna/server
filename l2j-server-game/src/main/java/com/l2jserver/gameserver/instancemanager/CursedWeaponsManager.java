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

import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.server;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.model.CursedWeapon;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2DefenderInstance;
import com.l2jserver.gameserver.model.actor.instance.L2FeedableBeastInstance;
import com.l2jserver.gameserver.model.actor.instance.L2FestivalMonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2FortCommanderInstance;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2GuardInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2RiftInvaderInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * UnAfraid: TODO: Rewrite with DocumentParser
 * @author Micht
 */
public final class CursedWeaponsManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(CursedWeaponsManager.class);
	
	private Map<Integer, CursedWeapon> _cursedWeapons;
	
	protected CursedWeaponsManager() {
		init();
	}
	
	private void init() {
		_cursedWeapons = new HashMap<>();
		
		if (!general().allowCursedWeapons()) {
			return;
		}
		
		load();
		restore();
		controlPlayers();
		LOG.info("Loaded {} cursed weapon(s).", _cursedWeapons.size());
	}
	
	public void reload() {
		init();
	}
	
	private void load() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			
			File file = new File(server().getDatapackRoot(), "data/cursedWeapons.xml");
			if (!file.exists()) {
				LOG.warn("Couldn't find {}!", file.getAbsoluteFile());
				return;
			}
			
			Document doc = factory.newDocumentBuilder().parse(file);
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
				if ("list".equalsIgnoreCase(n.getNodeName())) {
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
						if ("item".equalsIgnoreCase(d.getNodeName())) {
							NamedNodeMap attrs = d.getAttributes();
							int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							int skillId = Integer.parseInt(attrs.getNamedItem("skillId").getNodeValue());
							String name = attrs.getNamedItem("name").getNodeValue();
							
							CursedWeapon cw = new CursedWeapon(id, skillId, name);
							
							int val;
							for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
								if ("dropRate".equalsIgnoreCase(cd.getNodeName())) {
									attrs = cd.getAttributes();
									val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
									cw.setDropRate(val);
								} else if ("duration".equalsIgnoreCase(cd.getNodeName())) {
									attrs = cd.getAttributes();
									val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
									cw.setDuration(val);
								} else if ("durationLost".equalsIgnoreCase(cd.getNodeName())) {
									attrs = cd.getAttributes();
									val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
									cw.setDurationLost(val);
								} else if ("disappearChance".equalsIgnoreCase(cd.getNodeName())) {
									attrs = cd.getAttributes();
									val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
									cw.setDisappearChance(val);
								} else if ("stageKills".equalsIgnoreCase(cd.getNodeName())) {
									attrs = cd.getAttributes();
									val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
									cw.setStageKills(val);
								}
							}
							
							// Store cursed weapon
							_cursedWeapons.put(id, cw);
						}
					}
				}
			}
		} catch (Exception ex) {
			LOG.warn("There has been an error parsing cursed weapons file!", ex);
		}
	}
	
	private void restore() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s = con.createStatement();
			var rs = s.executeQuery("SELECT itemId, charId, playerKarma, playerPkKills, nbKills, endTime FROM cursed_weapons")) {
			// Retrieve the L2PcInstance from the characters table of the database
			CursedWeapon cw;
			while (rs.next()) {
				cw = _cursedWeapons.get(rs.getInt("itemId"));
				cw.setPlayerId(rs.getInt("charId"));
				cw.setPlayerKarma(rs.getInt("playerKarma"));
				cw.setPlayerPkKills(rs.getInt("playerPkKills"));
				cw.setNbKills(rs.getInt("nbKills"));
				cw.setEndTime(rs.getLong("endTime"));
				cw.reActivate();
			}
		} catch (Exception ex) {
			LOG.warn("Could not restore cursed weapons data!", ex);
		}
	}
	
	private void controlPlayers() {
		// TODO: See comments below...
		// This entire for loop should NOT be necessary, since it is already handled by
		// CursedWeapon.endOfLife(). However, if we indeed *need* to duplicate it for safety,
		// then we'd better make sure that it FULLY cleans up inactive cursed weapons!
		// Undesired effects result otherwise, such as player with no zariche but with karma
		// or a lost-child entry in the cursed weapons table, without a corresponding one in items...
		
		// Retrieve the L2PcInstance from the characters table of the database
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT owner_id FROM items WHERE item_id=?")) {
			for (CursedWeapon cw : _cursedWeapons.values()) {
				if (cw.isActivated()) {
					continue;
				}
				
				// Do an item check to be sure that the cursed weapon isn't hold by someone
				int itemId = cw.getItemId();
				ps.setInt(1, itemId);
				try (var rset = ps.executeQuery()) {
					if (rset.next()) {
						// A player has the cursed weapon in his inventory ...
						int playerId = rset.getInt("owner_id");
						LOG.warn("Player {} owns the cursed weapon {} but he shouldn't!", playerId, itemId);
						
						// Delete the item
						try (var delete = con.prepareStatement("DELETE FROM items WHERE owner_id=? AND item_id=?")) {
							delete.setInt(1, playerId);
							delete.setInt(2, itemId);
							if (delete.executeUpdate() != 1) {
								LOG.warn("There has been an error while deleting cursed weapon {} from player Id {}!", itemId, playerId);
							}
						}
						
						// Restore the player's old karma and pk count
						try (var update = con.prepareStatement("UPDATE characters SET karma=?, pkkills=? WHERE charId=?")) {
							update.setInt(1, cw.getPlayerKarma());
							update.setInt(2, cw.getPlayerPkKills());
							update.setInt(3, playerId);
							if (update.executeUpdate() != 1) {
								LOG.warn("There has been an error while updating karma & pkkills for player Id {}!", cw.getPlayerId());
							}
						}
						// clean up the cursed weapons table.
						removeFromDb(itemId);
					}
				}
				ps.clearParameters();
			}
		} catch (Exception ex) {
			LOG.warn("Could not check cursed weapons data!", ex);
		}
	}
	
	public synchronized void checkDrop(L2Attackable attackable, L2PcInstance player) {
		// Cursed weapons cannot drop in instance
		if (attackable.getInstanceId() != 0) {
			return;
		}
		if ((attackable instanceof L2DefenderInstance) || (attackable instanceof L2RiftInvaderInstance) || (attackable instanceof L2FestivalMonsterInstance) || (attackable instanceof L2GuardInstance) || (attackable instanceof L2GrandBossInstance) || (attackable instanceof L2FeedableBeastInstance)
			|| (attackable instanceof L2FortCommanderInstance)) {
			return;
		}
		
		for (CursedWeapon cw : _cursedWeapons.values()) {
			if (cw.isActive()) {
				continue;
			}
			
			if (cw.checkDrop(attackable, player)) {
				break;
			}
		}
	}
	
	public void activate(L2PcInstance player, L2ItemInstance item) {
		CursedWeapon cw = _cursedWeapons.get(item.getId());
		if (player.isCursedWeaponEquipped()) // cannot own 2 cursed swords
		{
			CursedWeapon cw2 = _cursedWeapons.get(player.getCursedWeaponEquippedId());
			// TODO: give the bonus level in a more appropriate manner.
			// The following code adds "_stageKills" levels. This will also show in the char status.
			// I do not have enough info to know if the bonus should be shown in the pk count, or if it
			// should be a full "_stageKills" bonus or just the remaining from the current count till the of the current stage...
			// This code is a TEMP fix, so that the cursed weapon's bonus level can be observed with as little change in the code as possible, until proper info arises.
			cw2.setNbKills(cw2.getStageKills() - 1);
			cw2.increaseKills();
			
			// erase the newly obtained cursed weapon
			cw.setPlayer(player); // NECESSARY in order to find which inventory the weapon is in!
			cw.endOfLife(); // expire the weapon and clean up.
		} else {
			cw.activate(player, item);
		}
	}
	
	public void drop(int itemId, L2Character killer) {
		CursedWeapon cw = _cursedWeapons.get(itemId);
		
		cw.dropIt(killer);
	}
	
	public void increaseKills(int itemId) {
		CursedWeapon cw = _cursedWeapons.get(itemId);
		
		cw.increaseKills();
	}
	
	public int getLevel(int itemId) {
		CursedWeapon cw = _cursedWeapons.get(itemId);
		
		return cw.getLevel();
	}
	
	public static void announce(SystemMessage sm) {
		Broadcast.toAllOnlinePlayers(sm);
	}
	
	public void checkPlayer(L2PcInstance player) {
		if (player == null) {
			return;
		}
		
		for (CursedWeapon cw : _cursedWeapons.values()) {
			if (cw.isActivated() && (player.getObjectId() == cw.getPlayerId())) {
				cw.setPlayer(player);
				cw.setItem(player.getInventory().getItemByItemId(cw.getItemId()));
				cw.giveSkill();
				player.setCursedWeaponEquippedId(cw.getItemId());
				
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
				sm.addString(cw.getName());
				// sm.addItemName(cw.getItemId());
				sm.addInt((int) ((cw.getEndTime() - System.currentTimeMillis()) / 60000));
				player.sendPacket(sm);
			}
		}
	}
	
	public int checkOwnsWeaponId(int ownerId) {
		for (CursedWeapon cw : _cursedWeapons.values()) {
			if (cw.isActivated() && (ownerId == cw.getPlayerId())) {
				return cw.getItemId();
			}
		}
		return -1;
	}
	
	public static void removeFromDb(int itemId) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("DELETE FROM cursed_weapons WHERE itemId = ?")) {
			ps.setInt(1, itemId);
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("Failed to remove cursed weapon Id {}!", itemId, ex);
		}
	}
	
	public void saveData() {
		for (CursedWeapon cw : _cursedWeapons.values()) {
			cw.saveData();
		}
	}
	
	public boolean isCursed(int itemId) {
		return _cursedWeapons.containsKey(itemId);
	}
	
	public Collection<CursedWeapon> getCursedWeapons() {
		return _cursedWeapons.values();
	}
	
	public Set<Integer> getCursedWeaponsIds() {
		return _cursedWeapons.keySet();
	}
	
	public CursedWeapon getCursedWeapon(int itemId) {
		return _cursedWeapons.get(itemId);
	}
	
	public void givePassive(int itemId) {
		try {
			_cursedWeapons.get(itemId).giveSkill();
		} catch (Exception e) {
			/***/
		}
	}
	
	public static CursedWeaponsManager getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final CursedWeaponsManager _instance = new CursedWeaponsManager();
	}
}
