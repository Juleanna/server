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

import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * Class explanation:<br>
 * For item counting or checking purposes. When you don't want to modify inventory<br>
 * class contains itemId, quantity, ownerId, referencePrice, but not objectId<br>
 * is stored, this will be only "list" of items with it's owner
 */
public final class TempItem {
	private final int _itemId;
	private int _quantity;
	private final int _referencePrice;
	private final String _itemName;
	
	public TempItem(L2ItemInstance item, int quantity) {
		_itemId = item.getId();
		_quantity = quantity;
		_itemName = item.getItem().getName();
		_referencePrice = item.getReferencePrice();
	}
	
	public int getQuantity() {
		return _quantity;
	}
	
	public void setQuantity(int quantity) {
		_quantity = quantity;
	}
	
	public int getReferencePrice() {
		return _referencePrice;
	}
	
	public int getItemId() {
		return _itemId;
	}
	
	public String getItemName() {
		return _itemName;
	}
}
