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

import com.l2jserver.gameserver.model.L2CommandChannel;
import com.l2jserver.gameserver.model.L2Party;

/**
 * @author chris_00
 */
public class ExMultiPartyCommandChannelInfo extends L2GameServerPacket {
	private final L2CommandChannel _channel;
	
	public ExMultiPartyCommandChannelInfo(L2CommandChannel channel) {
		_channel = channel;
	}
	
	@Override
	protected void writeImpl() {
		if (_channel == null) {
			return;
		}
		
		writeC(0xFE);
		writeH(0x31);
		
		writeS(_channel.getLeader().getName());
		writeD(0x00); // Channel loot 0 or 1
		writeD(_channel.getMemberCount());
		
		writeD(_channel.getParties().size());
		for (L2Party p : _channel.getParties()) {
			writeS(p.getLeader().getName());
			writeD(p.getLeaderObjectId());
			writeD(p.getMemberCount());
		}
	}
}
