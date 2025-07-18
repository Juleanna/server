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

import com.l2jserver.gameserver.handler.BypassHandler;
import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * format ch c: (id) 0xD0 h: (subid) 0x13
 * @author -Wooden-
 */
public final class RequestOlympiadMatchList extends L2GameClientPacket {
	private static final String _C__D0_2E_REQUESTOLYMPIADMATCHLIST = "[C] D0:2E RequestOlympiadMatchList";
	private static final String COMMAND = "arenalist";
	
	@Override
	protected void readImpl() {
		// trigger packet
	}
	
	@Override
	protected void runImpl() {
		final L2PcInstance activeChar = getClient().getActiveChar();
		if ((activeChar == null) || !activeChar.inObserverMode()) {
			return;
		}
		
		IBypassHandler handler = BypassHandler.getInstance().getHandler(COMMAND);
		if (handler != null) {
			handler.useBypass(COMMAND, activeChar, null);
		}
	}
	
	@Override
	public String getType() {
		return _C__D0_2E_REQUESTOLYMPIADMATCHLIST;
	}
}