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

public final class RequestDeleteMacro extends L2GameClientPacket {
	private static final String _C__CE_REQUESTDELETEMACRO = "[C] CE RequestDeleteMacro";
	
	private int _id;
	
	@Override
	protected void readImpl() {
		_id = readD();
	}
	
	@Override
	protected void runImpl() {
		if (getClient().getActiveChar() == null) {
			return;
		}
		getClient().getActiveChar().deleteMacro(_id);
	}
	
	@Override
	public String getType() {
		return _C__CE_REQUESTDELETEMACRO;
	}
}
