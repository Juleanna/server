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
package com.l2jserver.gameserver.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.ai.L2ControllableMobAI;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2ControllableMobInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;

/**
 * @author littlecrow
 */
public final class MobGroup {
	private final L2NpcTemplate _npcTemplate;
	private final int _groupId;
	private final int _maxMobCount;
	
	private List<L2ControllableMobInstance> _mobs;
	
	public MobGroup(int groupId, L2NpcTemplate npcTemplate, int maxMobCount) {
		_groupId = groupId;
		_npcTemplate = npcTemplate;
		_maxMobCount = maxMobCount;
	}
	
	public int getActiveMobCount() {
		return getMobs().size();
	}
	
	public int getGroupId() {
		return _groupId;
	}
	
	public int getMaxMobCount() {
		return _maxMobCount;
	}
	
	public List<L2ControllableMobInstance> getMobs() {
		if (_mobs == null) {
			_mobs = new CopyOnWriteArrayList<>();
		}
		
		return _mobs;
	}
	
	public String getStatus() {
		try {
			L2ControllableMobAI mobGroupAI = (L2ControllableMobAI) getMobs().get(0).getAI();
			return switch (mobGroupAI.getAlternateAI()) {
				case L2ControllableMobAI.AI_FORCE_ATTACK -> "Force Attacking";
				case L2ControllableMobAI.AI_FOLLOW -> "Following";
				case L2ControllableMobAI.AI_CAST -> "Casting";
				case L2ControllableMobAI.AI_ATTACK_GROUP -> "Attacking Group";
				default -> "Idle";
			};
		} catch (Exception e) {
			return "Unspawned";
		}
	}
	
	public L2NpcTemplate getTemplate() {
		return _npcTemplate;
	}
	
	public boolean isGroupMember(L2ControllableMobInstance mobInst) {
		for (L2ControllableMobInstance groupMember : getMobs()) {
			if (groupMember == null) {
				continue;
			}
			
			if (groupMember.getObjectId() == mobInst.getObjectId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void spawnGroup(int x, int y, int z) {
		if (getActiveMobCount() > 0) {
			return;
		}
		
		try {
			for (int i = 0; i < getMaxMobCount(); i++) {
				L2GroupSpawn spawn = new L2GroupSpawn(getTemplate());
				
				int signX = (Rnd.nextInt(2) == 0) ? -1 : 1;
				int signY = (Rnd.nextInt(2) == 0) ? -1 : 1;
				int randX = Rnd.nextInt(MobGroupTable.RANDOM_RANGE);
				int randY = Rnd.nextInt(MobGroupTable.RANDOM_RANGE);
				
				spawn.setX(x + (signX * randX));
				spawn.setY(y + (signY * randY));
				spawn.setZ(z);
				spawn.stopRespawn();
				
				SpawnTable.getInstance().addNewSpawn(spawn, false);
				getMobs().add((L2ControllableMobInstance) spawn.doGroupSpawn());
			}
		} catch (Exception ex) {
			
		}
	}
	
	public void spawnGroup(L2PcInstance activeChar) {
		spawnGroup(activeChar.getX(), activeChar.getY(), activeChar.getZ());
	}
	
	public void teleportGroup(L2PcInstance player) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			if (!mobInst.isDead()) {
				int x = player.getX() + Rnd.nextInt(50);
				int y = player.getY() + Rnd.nextInt(50);
				
				mobInst.teleToLocation(new Location(x, y, player.getZ()), true);
				L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
				ai.follow(player);
			}
		}
	}
	
	public L2ControllableMobInstance getRandomMob() {
		removeDead();
		
		if (getActiveMobCount() == 0) {
			return null;
		}
		
		int choice = Rnd.nextInt(getActiveMobCount());
		return getMobs().get(choice);
	}
	
	public void unspawnGroup() {
		removeDead();
		
		if (getActiveMobCount() == 0) {
			return;
		}
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			if (!mobInst.isDead()) {
				mobInst.deleteMe();
			}
			
			SpawnTable.getInstance().deleteSpawn(mobInst.getSpawn(), false);
		}
		
		getMobs().clear();
	}
	
	public void killGroup(L2PcInstance activeChar) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			if (!mobInst.isDead()) {
				mobInst.reduceCurrentHp(mobInst.getMaxHp() + 1, activeChar, null);
			}
			
			SpawnTable.getInstance().deleteSpawn(mobInst.getSpawn(), false);
		}
		
		getMobs().clear();
	}
	
	public void setAttackRandom() {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.setAlternateAI(L2ControllableMobAI.AI_NORMAL);
			ai.setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}
	
	public void setAttackTarget(L2Character target) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.forceAttack(target);
		}
	}
	
	public void setIdleMode() {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.stop();
		}
	}
	
	public void returnGroup(L2Character activeChar) {
		setIdleMode();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			int signX = (Rnd.nextInt(2) == 0) ? -1 : 1;
			int signY = (Rnd.nextInt(2) == 0) ? -1 : 1;
			int randX = Rnd.nextInt(MobGroupTable.RANDOM_RANGE);
			int randY = Rnd.nextInt(MobGroupTable.RANDOM_RANGE);
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.move(activeChar.getX() + (signX * randX), activeChar.getY() + (signY * randY), activeChar.getZ());
		}
	}
	
	public void setFollowMode(L2Character character) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.follow(character);
		}
	}
	
	public void setCastMode() {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.setAlternateAI(L2ControllableMobAI.AI_CAST);
		}
	}
	
	public void setNoMoveMode(boolean enabled) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.setNotMoving(enabled);
		}
	}
	
	protected void removeDead() {
		List<L2ControllableMobInstance> deadMobs = new LinkedList<>();
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if ((mobInst != null) && mobInst.isDead()) {
				deadMobs.add(mobInst);
			}
		}
		
		getMobs().removeAll(deadMobs);
	}
	
	public void setInvul(boolean invulState) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst != null) {
				mobInst.setInvul(invulState);
			}
		}
	}
	
	public void setAttackGroup(MobGroup otherGrp) {
		removeDead();
		
		for (L2ControllableMobInstance mobInst : getMobs()) {
			if (mobInst == null) {
				continue;
			}
			
			L2ControllableMobAI ai = (L2ControllableMobAI) mobInst.getAI();
			ai.forceAttackGroup(otherGrp);
			ai.setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}
}