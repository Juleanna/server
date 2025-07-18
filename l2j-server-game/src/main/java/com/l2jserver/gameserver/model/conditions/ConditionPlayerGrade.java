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

import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Grade condition.
 * @author Gigiikun
 */
public final class ConditionPlayerGrade extends Condition {
	protected static final Logger _log = Logger.getLogger(ConditionPlayerGrade.class.getName());
	// conditional values
	public static final int COND_NO_GRADE = 0x0001;
	public static final int COND_D_GRADE = 0x0002;
	public static final int COND_C_GRADE = 0x0004;
	public static final int COND_B_GRADE = 0x0008;
	public static final int COND_A_GRADE = 0x0010;
	public static final int COND_S_GRADE = 0x0020;
	public static final int COND_S80_GRADE = 0x0040;
	public static final int COND_S84_GRADE = 0x0080;
	
	private final int _value;
	
	public ConditionPlayerGrade(int value) {
		_value = value;
	}
	
	@Override
	public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item) {
		return (effector.getActingPlayer() != null) && (_value == (byte) effector.getActingPlayer().getExpertiseLevel());
	}
}
