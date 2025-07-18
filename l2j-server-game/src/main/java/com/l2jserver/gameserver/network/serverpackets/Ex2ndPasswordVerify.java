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
 * @author mrTJO
 */
public class Ex2ndPasswordVerify extends L2GameServerPacket {
	public static final int PASSWORD_OK = 0x00;
	public static final int PASSWORD_WRONG = 0x01;
	public static final int PASSWORD_BAN = 0x02;
	
	private final int _wrongTentatives, _mode;
	
	public Ex2ndPasswordVerify(int mode, int wrongTentatives) {
		_mode = mode;
		_wrongTentatives = wrongTentatives;
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		// writeH(0x109); GOD
		writeH(0xe6);
		writeD(_mode);
		writeD(_wrongTentatives);
	}
}
