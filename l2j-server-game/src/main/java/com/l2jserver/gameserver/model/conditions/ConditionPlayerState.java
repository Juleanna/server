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
package com.l2jserver.gameserver.model.conditions;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.PlayerState;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * State condition.
 * @author mkizub
 */
public class ConditionPlayerState extends Condition {
	private final PlayerState _check;
	private final boolean _required;
	
	public ConditionPlayerState(PlayerState check, boolean required) {
		_check = check;
		_required = required;
	}
	
	@Override
	public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item) {
		final L2PcInstance player = effector.getActingPlayer();
		switch (_check) {
			case RESTING:
				if (player != null) {
					return (player.isSitting() == _required);
				}
				return !_required;
			case MOVING:
				return effector.isMoving() == _required;
			case RUNNING:
				return effector.isRunning() == _required;
			case STANDING:
				if (player != null) {
					return (_required != (player.isSitting() || player.isMoving()));
				}
				return (_required != effector.isMoving());
			case FLYING:
				return (effector.isFlying() == _required);
			case BEHIND:
				return (effector.isBehindTarget() == _required);
			case FRONT:
				return (effector.isInFrontOfTarget() == _required);
			case CHAOTIC:
				if (player != null) {
					return ((player.getKarma() > 0) == _required);
				}
				return !_required;
			case OLYMPIAD:
				if (player != null) {
					return (player.isInOlympiadMode() == _required);
				}
				return !_required;
		}
		return !_required;
	}
}
