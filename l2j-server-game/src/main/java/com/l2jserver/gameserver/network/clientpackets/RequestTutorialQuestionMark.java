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
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerTutorialQuestionMark;

public class RequestTutorialQuestionMark extends L2GameClientPacket {
	private static final String _C__87_REQUESTTUTORIALQUESTIONMARK = "[C] 87 RequestTutorialQuestionMark";
	
	private int _number = 0;
	
	@Override
	protected void readImpl() {
		_number = readD();
	}
	
	@Override
	protected void runImpl() {
		final L2PcInstance player = getActiveChar();
		if (player == null) {
			return;
		}
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerTutorialQuestionMark(player, _number), player);
	}
	
	@Override
	public String getType() {
		return _C__87_REQUESTTUTORIALQUESTIONMARK;
	}
}
