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

/**
 * @author ShanSoft
 */
public final class RequestTeleportBookMark extends L2GameClientPacket {
	private static final String _C__D0_51_04_REQUESTTELEPORTBOOKMARK = "[C] D0:51:04 RequestTeleportBookMark";
	
	private int id;
	
	@Override
	protected void readImpl() {
		id = readD();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		activeChar.teleportBookmarkGo(id);
	}
	
	@Override
	public String getType() {
		return _C__D0_51_04_REQUESTTELEPORTBOOKMARK;
	}
}
