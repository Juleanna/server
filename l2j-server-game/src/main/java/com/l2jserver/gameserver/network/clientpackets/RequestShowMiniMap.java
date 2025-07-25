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
package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ShowMiniMap;

/**
 * @since 2005/04/10 00:17:44
 */
public final class RequestShowMiniMap extends L2GameClientPacket {
	private static final String _C__6C_REQUESTSHOWMINIMAP = "[C] 6C RequestShowMiniMap";
	
	@Override
	protected void readImpl() {
		// trigger
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		activeChar.sendPacket(new ShowMiniMap(1665));
	}
	
	@Override
	public String getType() {
		return _C__6C_REQUESTSHOWMINIMAP;
	}
	
	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
