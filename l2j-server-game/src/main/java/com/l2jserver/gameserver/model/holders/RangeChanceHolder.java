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
package com.l2jserver.gameserver.model.holders;

/**
 * @author UnAfraid
 */
public class RangeChanceHolder {
	private final int _min;
	private final int _max;
	private final double _chance;
	
	public RangeChanceHolder(int min, int max, double chance) {
		_min = min;
		_max = max;
		_chance = chance;
	}
	
	public int getMin() {
		return _min;
	}
	
	public int getMax() {
		return _max;
	}
	
	public double getChance() {
		return _chance;
	}
}
