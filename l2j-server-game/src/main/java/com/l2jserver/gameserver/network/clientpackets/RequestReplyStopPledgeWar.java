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

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * @since 2005/03/27 15:29:30
 */
public final class RequestReplyStopPledgeWar extends L2GameClientPacket {
	private static final String _C__06_REQUESTREPLYSTOPPLEDGEWAR = "[C] 06 RequestReplyStopPledgeWar";
	
	private int _answer;
	
	@Override
	protected void readImpl() {
		readS();
		_answer = readD();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		L2PcInstance requestor = activeChar.getActiveRequester();
		if (requestor == null) {
			return;
		}
		
		if (_answer == 1) {
			ClanTable.getInstance().deleteclanswars(requestor.getClanId(), activeChar.getClanId());
		} else {
			requestor.sendPacket(SystemMessageId.REQUEST_TO_END_WAR_HAS_BEEN_DENIED);
		}
		
		activeChar.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
	
	@Override
	public String getType() {
		return _C__06_REQUESTREPLYSTOPPLEDGEWAR;
	}
}