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
package com.l2jserver.gameserver.model.stats.functions.formulas;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.stats.BaseStats;
import com.l2jserver.gameserver.model.stats.Stats;
import com.l2jserver.gameserver.model.stats.functions.AbstractFunction;

/**
 * @author UnAfraid
 */
public class FuncAtkCritical extends AbstractFunction {
	private static final FuncAtkCritical _fac_instance = new FuncAtkCritical();
	
	public static AbstractFunction getInstance() {
		return _fac_instance;
	}
	
	private FuncAtkCritical() {
		super(Stats.CRITICAL_RATE, 1, null, 0, null);
	}
	
	@Override
	public double calc(L2Character effector, L2Character effected, Skill skill, double initVal) {
		return initVal * BaseStats.DEX.calcBonus(effector) * 10;
	}
}