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
package com.l2jserver.gameserver.model.events.impl.character.npc.attackable;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.IBaseEvent;

/**
 * @author UnAfraid
 */
public class OnAttackableFactionCall implements IBaseEvent {
	private final L2Npc _npc;
	private final L2Npc _caller;
	private final L2PcInstance _attacker;
	private final boolean _isSummon;
	
	public OnAttackableFactionCall(L2Npc npc, L2Npc caller, L2PcInstance attacker, boolean isSummon) {
		_npc = npc;
		_caller = caller;
		_attacker = attacker;
		_isSummon = isSummon;
	}
	
	public L2Npc getNpc() {
		return _npc;
	}
	
	public L2Npc getCaller() {
		return _caller;
	}
	
	public L2PcInstance getAttacker() {
		return _attacker;
	}
	
	public boolean isSummon() {
		return _isSummon;
	}
	
	@Override
	public EventType getType() {
		return EventType.ON_ATTACKABLE_FACTION_CALL;
	}
}
