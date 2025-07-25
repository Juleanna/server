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
package com.l2jserver.gameserver.model.itemauction;

/**
 * @author Forsaiken
 */
public enum ItemAuctionState {
	CREATED((byte) 0),
	STARTED((byte) 1),
	FINISHED((byte) 2);
	
	private final byte _stateId;
	
	ItemAuctionState(final byte stateId) {
		_stateId = stateId;
	}
	
	public byte getStateId() {
		return _stateId;
	}
	
	public static ItemAuctionState stateForStateId(final byte stateId) {
		for (final ItemAuctionState state : ItemAuctionState.values()) {
			if (state.getStateId() == stateId) {
				return state;
			}
		}
		return null;
	}
}