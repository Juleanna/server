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
 * A DTO for items; contains item ID, count and chance.<br>
 * Complemented by {@link QuestItemChanceHolder}.
 * @author xban1x
 */
public class ItemChanceHolder extends ItemHolder {
	private final double _chance;
	
	public ItemChanceHolder(int id, double chance) {
		this(id, chance, 1);
	}
	
	public ItemChanceHolder(int id, double chance, long count) {
		super(id, count);
		_chance = chance;
	}
	
	/**
	 * Gets the chance.
	 * @return the drop chance of the item contained in this object
	 */
	public double getChance() {
		return _chance;
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] ID: " + getId() + ", count: " + getCount() + ", chance: " + _chance;
	}
}
