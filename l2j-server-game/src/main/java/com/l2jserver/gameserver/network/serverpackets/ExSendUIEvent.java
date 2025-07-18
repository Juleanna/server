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

import java.util.Arrays;
import java.util.List;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.NpcStringId;

public class ExSendUIEvent extends L2GameServerPacket {
	private final int _objectId;
	private final boolean _type;
	private final boolean _countUp;
	private final int _startTime;
	private final int _endTime;
	private final int _npcstringId;
	private final List<String> _params;
	
	public ExSendUIEvent(L2PcInstance player, boolean hide, boolean countUp, int startTime, int endTime, String text) {
		this(player, hide, countUp, startTime, endTime, -1, text);
	}
	
	public ExSendUIEvent(L2PcInstance player, boolean hide, boolean countUp, int startTime, int endTime, NpcStringId npcString, String... params) {
		this(player, hide, countUp, startTime, endTime, npcString.getId(), params);
	}
	
	public ExSendUIEvent(L2PcInstance player, boolean hide, boolean countUp, int startTime, int endTime, int npcstringId, String... params) {
		_objectId = player.getObjectId();
		_type = hide;
		_countUp = countUp;
		_startTime = startTime;
		_endTime = endTime;
		_npcstringId = npcstringId;
		_params = Arrays.asList(params);
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0x8E);
		writeD(_objectId);
		writeD(_type ? 1 : 0); // 0 = show, 1 = hide (there is 2 = pause and 3 = resume also but they don't work well you can only pause count down and you cannot resume it because resume hides the counter).
		writeD(0);// unknown
		writeD(0);// unknown
		writeS(_countUp ? "1" : "0"); // 0 = count down, 1 = count up
		// timer always disappears 10 seconds before end
		writeS(String.valueOf(_startTime / 60));
		writeS(String.valueOf(_startTime % 60));
		writeS(String.valueOf(_endTime / 60));
		writeS(String.valueOf(_endTime % 60));
		writeD(_npcstringId);
		if (_params != null) {
			for (String param : _params) {
				writeS(param);
			}
		}
	}
}