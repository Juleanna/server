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

/**
 * @author Maktakien
 */
public class GetOffVehicle extends L2GameServerPacket {
	private final int _charObjId, _boatObjId, _x, _y, _z;
	
	public GetOffVehicle(int charObjId, int boatObjId, int x, int y, int z) {
		_charObjId = charObjId;
		_boatObjId = boatObjId;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	protected void writeImpl() {
		writeC(0x6f);
		writeD(_charObjId);
		writeD(_boatObjId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
