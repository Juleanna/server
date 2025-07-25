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

import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public final class PartySmallWindowAll extends L2GameServerPacket {
	private final L2Party _party;
	private final L2PcInstance _exclude;
	
	public PartySmallWindowAll(L2PcInstance exclude, L2Party party) {
		_exclude = exclude;
		_party = party;
	}
	
	@Override
	protected void writeImpl() {
		writeC(0x4e);
		writeD(_party.getLeaderObjectId());
		writeD(_party.getDistributionType().getId());
		writeD(_party.getMemberCount() - 1);
		
		for (L2PcInstance member : _party.getMembers()) {
			if ((member != null) && (member != _exclude)) {
				writeD(member.getObjectId());
				writeS(member.getName());
				
				writeD((int) member.getCurrentCp()); // c4
				writeD(member.getMaxCp()); // c4
				
				writeD((int) member.getCurrentHp());
				writeD(member.getMaxHp());
				writeD((int) member.getCurrentMp());
				writeD(member.getMaxMp());
				writeD(member.getLevel());
				writeD(member.getClassId().getId());
				writeD(0x00);// writeD(0x01); ??
				writeD(member.getRace().ordinal());
				writeD(0x00); // T2.3
				writeD(0x00); // T2.3
				if (member.hasSummon()) {
					writeD(member.getSummon().getObjectId());
					writeD(member.getSummon().getId() + 1000000);
					writeD(member.getSummon().getSummonType());
					writeS(member.getSummon().getName());
					writeD((int) member.getSummon().getCurrentHp());
					writeD(member.getSummon().getMaxHp());
					writeD((int) member.getSummon().getCurrentMp());
					writeD(member.getSummon().getMaxMp());
					writeD(member.getSummon().getLevel());
				} else {
					writeD(0x00);
				}
			}
		}
	}
}
