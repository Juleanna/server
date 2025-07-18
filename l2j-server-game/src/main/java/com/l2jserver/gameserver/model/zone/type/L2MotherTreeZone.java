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
package com.l2jserver.gameserver.model.zone.type;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * A mother-trees zone Basic type zone for Hp, MP regen
 * @author durgus
 */
public class L2MotherTreeZone extends L2ZoneType {
	private int _enterMsg;
	private int _leaveMsg;
	private int _mpRegen;
	private int _hpRegen;
	
	public L2MotherTreeZone(int id) {
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value) {
		switch (name) {
			case "enterMsgId" -> _enterMsg = Integer.parseInt(value);
			case "leaveMsgId" -> _leaveMsg = Integer.parseInt(value);
			case "MpRegenBonus" -> _mpRegen = Integer.parseInt(value);
			case "HpRegenBonus" -> _hpRegen = Integer.parseInt(value);
			default -> super.setParameter(name, value);
		}
	}
	
	@Override
	protected void onEnter(L2Character character) {
		if (character.isPlayer()) {
			L2PcInstance player = character.getActingPlayer();
			character.setInsideZone(ZoneId.MOTHER_TREE, true);
			if (_enterMsg != 0) {
				player.sendPacket(SystemMessage.getSystemMessage(_enterMsg));
			}
		}
	}
	
	@Override
	protected void onExit(L2Character character) {
		if (character.isPlayer()) {
			L2PcInstance player = character.getActingPlayer();
			player.setInsideZone(ZoneId.MOTHER_TREE, false);
			if (_leaveMsg != 0) {
				player.sendPacket(SystemMessage.getSystemMessage(_leaveMsg));
			}
		}
	}
	
	/**
	 * @return the _mpRegen
	 */
	public int getMpRegenBonus() {
		return _mpRegen;
	}
	
	/**
	 * @return the _hpRegen
	 */
	public int getHpRegenBonus() {
		return _hpRegen;
	}
}
