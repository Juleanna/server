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

import com.l2jserver.gameserver.data.sql.impl.PetNameTable;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * @since 2005/04/06 16:13:48
 */
public final class RequestChangePetName extends L2GameClientPacket {
	private static final String _C__93_REQUESTCHANGEPETNAME = "[C] 93 RequestChangePetName";
	private static final int PET_NAME_MAX_LENGTH = 16;
	
	private String _name;
	
	@Override
	protected void readImpl() {
		_name = readS();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		final L2Summon pet = activeChar.getSummon();
		if (pet == null) {
			return;
		}
		
		if (!pet.isPet()) {
			activeChar.sendPacket(SystemMessageId.DONT_HAVE_PET);
			return;
		}
		
		if (pet.getName() != null) {
			activeChar.sendPacket(SystemMessageId.NAMING_YOU_CANNOT_SET_NAME_OF_THE_PET);
			return;
		}
		
		if (PetNameTable.getInstance().doesPetNameExist(_name)) {
			activeChar.sendPacket(SystemMessageId.NAMING_ALREADY_IN_USE_BY_ANOTHER_PET);
			return;
		}
		
		if (_name.isEmpty() || (_name.length() > PET_NAME_MAX_LENGTH)) {
			activeChar.sendPacket(SystemMessageId.NAMING_CHARNAME_UP_TO_16CHARS);
			return;
		}
		
		if (!PetNameTable.getInstance().isValidPetName(_name)) {
			activeChar.sendPacket(SystemMessageId.NAMING_PETNAME_CONTAINS_INVALID_CHARS);
			return;
		}
		
		pet.setName(_name);
		pet.updateAndBroadcastStatus(1);
	}
	
	@Override
	public String getType() {
		return _C__93_REQUESTCHANGEPETNAME;
	}
}
