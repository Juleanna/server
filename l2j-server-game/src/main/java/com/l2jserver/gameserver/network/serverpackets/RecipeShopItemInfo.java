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
package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class RecipeShopItemInfo extends L2GameServerPacket {
	private final L2PcInstance _player;
	private final int _recipeId;
	
	public RecipeShopItemInfo(L2PcInstance player, int recipeId) {
		_player = player;
		_recipeId = recipeId;
	}
	
	@Override
	protected final void writeImpl() {
		writeC(0xE0);
		writeD(_player.getObjectId());
		writeD(_recipeId);
		writeD((int) _player.getCurrentMp());
		writeD(_player.getMaxMp());
		writeD(0xffffffff);
	}
}
