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

import com.l2jserver.gameserver.network.serverpackets.PledgeCrest;

/**
 * @since 2005/03/27 15:29:30
 */
public final class RequestPledgeCrest extends L2GameClientPacket {
	private static final String _C__68_REQUESTPLEDGECREST = "[C] 68 RequestPledgeCrest";
	
	private int _crestId;
	
	@Override
	protected void readImpl() {
		_crestId = readD();
	}
	
	@Override
	protected void runImpl() {
		sendPacket(new PledgeCrest(_crestId));
	}
	
	@Override
	public String getType() {
		return _C__68_REQUESTPLEDGECREST;
	}
	
	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
