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
package com.l2jserver.gameserver.model.items.type;

/**
 * Item Type 2 enumerated.
 * @author Adry_85
 * @since 2.6.0.0
 */
public enum ItemType2 {
	WEAPON(0),
	SHIELD_ARMOR(1),
	ACCESSORY(2),
	QUEST(3),
	MONEY(4),
	OTHER(5);
	
	private final int _id;
	
	ItemType2(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
}
