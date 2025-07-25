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

import java.util.List;

import com.l2jserver.gameserver.instancemanager.FortSiegeManager;
import com.l2jserver.gameserver.model.FortSiegeSpawn;
import com.l2jserver.gameserver.model.entity.Fort;

/**
 * @author KenM
 */
public class ExShowFortressSiegeInfo extends L2GameServerPacket {
	private final int _fortId;
	private final int _size;
	private final int _csize;
	private final int _csize2;
	
	public ExShowFortressSiegeInfo(Fort fort) {
		_fortId = fort.getResidenceId();
		_size = fort.getFortSize();
		List<FortSiegeSpawn> commanders = FortSiegeManager.getInstance().getCommanderSpawnList(_fortId);
		_csize = ((commanders == null) ? 0 : commanders.size());
		_csize2 = fort.getSiege().getCommanders().size();
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0x17);
		
		writeD(_fortId); // Fortress Id
		writeD(_size); // Total Barracks Count
		if (_csize > 0) {
			switch (_csize) {
				case 3:
					switch (_csize2) {
						case 0 -> writeD(0x03);
						case 1 -> writeD(0x02);
						case 2 -> writeD(0x01);
						case 3 -> writeD(0x00);
					}
					break;
				case 4:
					// TODO: change 4 to 5 once control room supported
					// TODO: once control room supported, update writeD(0x0x) to support 5th room
					switch (_csize2) {
						case 0 -> writeD(0x05);
						case 1 -> writeD(0x04);
						case 2 -> writeD(0x03);
						case 3 -> writeD(0x02);
						case 4 -> writeD(0x01);
					}
					break;
			}
		} else {
			for (int i = 0; i < _size; i++) {
				writeD(0x00);
			}
		}
	}
}
