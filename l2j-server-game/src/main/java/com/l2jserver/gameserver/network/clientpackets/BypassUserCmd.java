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

import com.l2jserver.gameserver.handler.IUserCommandHandler;
import com.l2jserver.gameserver.handler.UserCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @since 2005/03/27 15:29:30
 */
public class BypassUserCmd extends L2GameClientPacket {
	private static final String _C__B3_BYPASSUSERCMD = "[C] B3 BypassUserCmd";
	
	private int _command;
	
	@Override
	protected void readImpl() {
		_command = readD();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance player = getClient().getActiveChar();
		if (player == null) {
			return;
		}
		
		IUserCommandHandler handler = UserCommandHandler.getInstance().getHandler(_command);
		
		if (handler == null) {
			if (player.isGM()) {
				player.sendMessage("User commandID " + _command + " not implemented yet.");
			}
		} else {
			handler.useUserCommand(_command, getClient().getActiveChar());
		}
	}
	
	@Override
	public String getType() {
		return _C__B3_BYPASSUSERCMD;
	}
}
