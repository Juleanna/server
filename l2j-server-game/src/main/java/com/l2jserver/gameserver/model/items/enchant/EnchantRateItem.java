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
package com.l2jserver.gameserver.model.items.enchant;

import com.l2jserver.gameserver.model.items.L2Item;

/**
 * @author UnAfraid
 */
public final class EnchantRateItem {
	private final String _name;
	private int _itemId;
	private int _slot;
	private Boolean _isMagicWeapon = null;
	
	public EnchantRateItem(String name) {
		_name = name;
	}
	
	/**
	 * @return name of enchant group.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Adds item id verification.
	 * @param id
	 */
	public void setItemId(int id) {
		_itemId = id;
	}
	
	/**
	 * Adds body slot verification.
	 * @param slot
	 */
	public void addSlot(int slot) {
		_slot |= slot;
	}
	
	/**
	 * Adds magic weapon verification.
	 * @param magicWeapon
	 */
	public void setMagicWeapon(boolean magicWeapon) {
		_isMagicWeapon = magicWeapon;
	}
	
	/**
	 * @param item
	 * @return {@code true} if item can be used with this rate group, {@code false} otherwise.
	 */
	public boolean validate(L2Item item) {
		if ((_itemId != 0) && (_itemId != item.getId())) {
			return false;
		} else if ((_slot != 0) && ((item.getBodyPart() & _slot) == 0)) {
			return false;
		}
		return (_isMagicWeapon == null) || (item.isMagicWeapon() == _isMagicWeapon);
	}
}
