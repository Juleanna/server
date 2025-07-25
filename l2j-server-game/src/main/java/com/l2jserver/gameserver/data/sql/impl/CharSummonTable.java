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

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.data.xml.impl.PetDataTable;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.L2PetData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.actor.instance.L2ServitorInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.PetItemList;

/**
 * @author Nyaran
 */
public class CharSummonTable {
	
	private static final Logger LOG = LoggerFactory.getLogger(CharSummonTable.class);
	
	private static final Map<Integer, Integer> _pets = new ConcurrentHashMap<>();
	
	private static final Map<Integer, Integer> _servitors = new ConcurrentHashMap<>();
	
	// SQL
	private static final String INIT_PET = "SELECT ownerId, item_obj_id FROM pets WHERE restore=TRUE";
	
	private static final String INIT_SUMMONS = "SELECT ownerId, summonSkillId FROM character_summons";
	
	private static final String LOAD_SUMMON = "SELECT curHp, curMp, time FROM character_summons WHERE ownerId = ? AND summonSkillId = ?";
	
	private static final String REMOVE_SUMMON = "DELETE FROM character_summons WHERE ownerId = ?";
	
	private static final String SAVE_SUMMON = "REPLACE INTO character_summons (ownerId,summonSkillId,curHp,curMp,time) VALUES (?,?,?,?,?)";
	
	public Map<Integer, Integer> getPets() {
		return _pets;
	}
	
	public Map<Integer, Integer> getServitors() {
		return _servitors;
	}
	
	public void init() {
		if (character().restoreServitorOnReconnect()) {
			try (var con = ConnectionFactory.getInstance().getConnection();
				var s = con.createStatement();
				var rs = s.executeQuery(INIT_SUMMONS)) {
				while (rs.next()) {
					_servitors.put(rs.getInt("ownerId"), rs.getInt("summonSkillId"));
				}
			} catch (Exception e) {
				LOG.warn("Error while loading saved servitor!", e);
			}
		}
		
		if (character().restorePetOnReconnect()) {
			try (var con = ConnectionFactory.getInstance().getConnection();
				var s = con.createStatement();
				var rs = s.executeQuery(INIT_PET)) {
				while (rs.next()) {
					_pets.put(rs.getInt("ownerId"), rs.getInt("item_obj_id"));
				}
			} catch (Exception e) {
				LOG.warn("Error while loading saved pet!", e);
			}
		}
	}
	
	public void removeServitor(L2PcInstance activeChar) {
		_servitors.remove(activeChar.getObjectId());
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(REMOVE_SUMMON)) {
			ps.setInt(1, activeChar.getObjectId());
			ps.execute();
		} catch (Exception e) {
			LOG.warn("Summon cannot be removed!", e);
		}
	}
	
	public void restorePet(L2PcInstance activeChar) {
		final L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_pets.get(activeChar.getObjectId()));
		if (item == null) {
			LOG.warn("Null pet summoning item for player {}!", activeChar);
			return;
		}
		final L2PetData petData = PetDataTable.getInstance().getPetDataByItemId(item.getId());
		if (petData == null) {
			LOG.warn("Null pet data for: {} and summoning item {}!", activeChar, item);
			return;
		}
		final L2NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(petData.getNpcId());
		if (npcTemplate == null) {
			LOG.warn("Null pet NPC template for player {} and pet ID {}!", activeChar, petData.getNpcId());
			return;
		}
		
		final L2PetInstance pet = L2PetInstance.spawnPet(npcTemplate, activeChar, item);
		if (pet == null) {
			LOG.warn("Null pet instance for player {} and pet NPC template {}!", activeChar, npcTemplate);
			return;
		}
		
		pet.setShowSummonAnimation(true);
		pet.setTitle(activeChar.getName());
		
		if (!pet.isRespawned()) {
			pet.setCurrentHp(pet.getMaxHp());
			pet.setCurrentMp(pet.getMaxMp());
			pet.setExp(pet.getExpForThisLevel());
			pet.setCurrentFed(pet.getMaxFed());
		}
		
		pet.setRunning();
		
		if (!pet.isRespawned()) {
			pet.storeMe();
		}
		
		item.setEnchantLevel(pet.getLevel());
		activeChar.setPet(pet);
		pet.spawnMe(activeChar.getX() + 50, activeChar.getY() + 100, activeChar.getZ());
		pet.startFeed();
		pet.setFollowStatus(true);
		pet.getOwner().sendPacket(new PetItemList(pet.getInventory().getItems()));
		pet.broadcastStatusUpdate();
	}
	
	public void restoreServitor(L2PcInstance activeChar) {
		int skillId = _servitors.get(activeChar.getObjectId());
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(LOAD_SUMMON)) {
			ps.setInt(1, activeChar.getObjectId());
			ps.setInt(2, skillId);
			try (var rs = ps.executeQuery()) {
				Skill skill;
				
				while (rs.next()) {
					int curHp = rs.getInt("curHp");
					int curMp = rs.getInt("curMp");
					int time = rs.getInt("time");
					
					skill = SkillData.getInstance().getSkill(skillId, activeChar.getSkillLevel(skillId));
					if (skill == null) {
						removeServitor(activeChar);
						return;
					}
					
					skill.applyEffects(activeChar, activeChar);
					if (activeChar.hasServitor()) {
						final L2ServitorInstance summon = (L2ServitorInstance) activeChar.getSummon();
						summon.setCurrentHp(curHp);
						summon.setCurrentMp(curMp);
						summon.setLifeTimeRemaining(time);
					}
				}
			}
		} catch (Exception e) {
			LOG.warn("Servitor cannot be restored!", e);
		}
	}
	
	public void saveSummon(L2ServitorInstance summon) {
		if ((summon == null) || (summon.getLifeTimeRemaining() <= 0)) {
			return;
		}
		
		_servitors.put(summon.getOwner().getObjectId(), summon.getReferenceSkill());
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(SAVE_SUMMON)) {
			ps.setInt(1, summon.getOwner().getObjectId());
			ps.setInt(2, summon.getReferenceSkill());
			ps.setInt(3, (int) Math.round(summon.getCurrentHp()));
			ps.setInt(4, (int) Math.round(summon.getCurrentMp()));
			ps.setInt(5, summon.getLifeTimeRemaining());
			ps.execute();
		} catch (Exception e) {
			LOG.warn("Failed to store summon {} from {}!", summon, summon.getOwner(), e);
		}
	}
	
	public static CharSummonTable getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final CharSummonTable _instance = new CharSummonTable();
	}
}
