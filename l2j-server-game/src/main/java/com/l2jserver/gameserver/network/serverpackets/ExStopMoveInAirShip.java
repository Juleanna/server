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

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author kerberos
 * @author JIV
 * @since 08/27/2010
 */
public class ExStopMoveInAirShip extends L2GameServerPacket {
	private final L2PcInstance _activeChar;
	private final int _shipObjId;
	private final int x, y, z, h;
	
	public ExStopMoveInAirShip(L2PcInstance player, int shipObjId) {
		_activeChar = player;
		_shipObjId = shipObjId;
		x = player.getInVehiclePosition().getX();
		y = player.getInVehiclePosition().getY();
		z = player.getInVehiclePosition().getZ();
		h = player.getHeading();
	}
	
	@Override
	protected final void writeImpl() {
		writeC(0xfe);
		writeH(0x6e);
		writeD(_activeChar.getObjectId());
		writeD(_shipObjId);
		writeD(x);
		writeD(y);
		writeD(z);
		writeD(h);
	}
}
