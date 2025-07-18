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

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.network.serverpackets.SiegeDefenderList;

/**
 * @since 2005/03/27 15:29:30
 */
public final class RequestConfirmSiegeWaitingList extends L2GameClientPacket {
	private static final String _C__AE_RequestConfirmSiegeWaitingList = "[C] AE RequestConfirmSiegeWaitingList";
	
	private int _approved;
	private int _castleId;
	private int _clanId;
	
	@Override
	protected void readImpl() {
		_castleId = readD();
		_clanId = readD();
		_approved = readD();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		// Check if the player has a clan
		if (activeChar.getClan() == null) {
			return;
		}
		
		Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null) {
			return;
		}
		
		// Check if leader of the clan who owns the castle?
		if ((castle.getOwnerId() != activeChar.getClanId()) || (!activeChar.isClanLeader())) {
			return;
		}
		
		L2Clan clan = ClanTable.getInstance().getClan(_clanId);
		if (clan == null) {
			return;
		}
		
		if (!castle.getSiege().getIsRegistrationOver()) {
			if (_approved == 1) {
				if (castle.getSiege().checkIsDefenderWaiting(clan)) {
					castle.getSiege().approveSiegeDefenderClan(_clanId);
				} else {
					return;
				}
			} else {
				if ((castle.getSiege().checkIsDefenderWaiting(clan)) || (castle.getSiege().checkIsDefender(clan))) {
					castle.getSiege().removeSiegeClan(_clanId);
				}
			}
		}
		
		// Update the defender list
		activeChar.sendPacket(new SiegeDefenderList(castle));
		
	}
	
	@Override
	public String getType() {
		return _C__AE_RequestConfirmSiegeWaitingList;
	}
}
