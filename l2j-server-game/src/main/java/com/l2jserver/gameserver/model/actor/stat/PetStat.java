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
package com.l2jserver.gameserver.model.actor.stat;

import static com.l2jserver.gameserver.config.Configuration.character;

import com.l2jserver.gameserver.data.xml.impl.PetDataTable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.stats.Stats;

public class PetStat extends SummonStat {
	public PetStat(L2PetInstance activeChar) {
		super(activeChar);
	}
	
	public boolean addExp(int value) {
		if (getActiveChar().isUncontrollable() || !super.addExp(value)) {
			return false;
		}
		
		getActiveChar().updateAndBroadcastStatus(1);
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		getActiveChar().updateEffectIcons(true);
		
		return true;
	}
	
	public boolean addExpAndSp(long addToExp, int addToSp) {
		if (getActiveChar().isUncontrollable() || !addExp(addToExp)) {
			return false;
		}
		
		// Not used in H5
		// SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PET_EARNED_S1_EXP).addLong(addToExp);
		getActiveChar().updateAndBroadcastStatus(1);
		return true;
	}
	
	@Override
	public final long getExpForLevel(int level) {
		try {
			return PetDataTable.getInstance().getPetLevelData(getActiveChar().getId(), Math.min(level, getMaxExpLevel())).getPetMaxExp();
		} catch (NullPointerException e) {
			if (getActiveChar() != null) {
				_log.warning("Pet objectId:" + getActiveChar().getObjectId() + ", NpcId:" + getActiveChar().getId() + ", level:" + level + " is missing data from pets_stats table!");
			}
			throw e;
		}
	}
	
	@Override
	public void setLevel(int value) {
		getActiveChar().setPetData(PetDataTable.getInstance().getPetLevelData(getActiveChar().getTemplate().getId(), value));
		if (getActiveChar().getPetLevelData() == null) {
			throw new IllegalArgumentException("No pet data for npc: " + getActiveChar().getTemplate().getId() + " level: " + value);
		}
		getActiveChar().stopFeed();
		super.setLevel(value);
		
		getActiveChar().startFeed();
		
		if (getActiveChar().getControlItem() != null) {
			getActiveChar().getControlItem().setEnchantLevel(getLevel());
		}
	}
	
	@Override
	public int getMaxLevel() {
		return character().getMaxPetLevel();
	}
	
	@Override
	public int getMaxExpLevel() {
		return character().getMaxPetLevel() + 1;
	}
	
	@Override
	public L2PetInstance getActiveChar() {
		return (L2PetInstance) super.getActiveChar();
	}
	
	public final int getFeedBattle() {
		return getActiveChar().getPetLevelData().getPetFeedBattle();
	}
	
	public final int getFeedNormal() {
		return getActiveChar().getPetLevelData().getPetFeedNormal();
	}
	
	public final int getMaxFeed() {
		return getActiveChar().getPetLevelData().getPetMaxFeed();
	}
	
	@Override
	public int getMaxHp() {
		return (int) calcStat(Stats.MAX_HP, getActiveChar().getPetLevelData().getPetMaxHP(), null, null);
	}
	
	@Override
	public int getMaxMp() {
		return (int) calcStat(Stats.MAX_MP, getActiveChar().getPetLevelData().getPetMaxMP(), null, null);
	}
	
	@Override
	public double getMAtk(L2Character target, Skill skill) {
		return calcStat(Stats.MAGIC_ATTACK, getActiveChar().getPetLevelData().getPetMAtk(), target, skill);
	}
	
	@Override
	public double getMDef(L2Character target, Skill skill) {
		return calcStat(Stats.MAGIC_DEFENCE, getActiveChar().getPetLevelData().getPetMDef(), target, skill);
	}
	
	@Override
	public double getPAtk(L2Character target) {
		return calcStat(Stats.POWER_ATTACK, getActiveChar().getPetLevelData().getPetPAtk(), target, null);
	}
	
	@Override
	public double getPDef(L2Character target) {
		return calcStat(Stats.POWER_DEFENCE, getActiveChar().getPetLevelData().getPetPDef(), target, null);
	}
	
	@Override
	public double getPAtkSpd() {
		double val = super.getPAtkSpd();
		if (getActiveChar().isHungry()) {
			val = val / 2;
		}
		return val;
	}
	
	@Override
	public int getMAtkSpd() {
		int val = super.getMAtkSpd();
		if (getActiveChar().isHungry()) {
			val = val / 2;
		}
		return val;
	}
}
