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
 * Dialog with input field<br>
 * type 0 = char name (Selection screen)<br>
 * type 1 = clan name
 * @author JIV
 */
public class ExNeedToChangeName extends L2GameServerPacket {
	private final int _type, _subType;
	private final String _name;
	
	public ExNeedToChangeName(int type, int subType, String name) {
		_type = type;
		_subType = subType;
		_name = name;
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0x69);
		writeD(_type);
		writeD(_subType);
		writeS(_name);
	}
}
