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
 * @author KenM
 */
public class ExSetCompassZoneCode extends L2GameServerPacket {
	
	public static final int ALTERED_ZONE = 0x08;
	public static final int SIEGE_WAR_ZONE_1 = 0x0A;
	public static final int SIEGE_WAR_ZONE_2 = 0x0B;
	public static final int PEACE_ZONE = 0x0C;
	public static final int SEVEN_SIGNS_ZONE = 0x0D;
	public static final int PVP_ZONE = 0x0E;
	public static final int GENERAL_ZONE = 0x0F;
	
	private final int _zoneType;
	
	public ExSetCompassZoneCode(int val) {
		_zoneType = val;
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0x33);
		writeD(_zoneType);
	}
}
