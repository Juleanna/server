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

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.model.L2Clan.PENALTY_TYPE_CLAN_DISMISSED;
import static com.l2jserver.gameserver.model.L2Clan.PENALTY_TYPE_DISMISS_CLAN;
import static java.util.concurrent.TimeUnit.DAYS;

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;

public final class AllyDismiss extends L2GameClientPacket {
	private static final String _C__8F_ALLYDISMISS = "[C] 8F AllyDismiss";
	
	private String _clanName;
	
	@Override
	protected void readImpl() {
		_clanName = readS();
	}
	
	@Override
	protected void runImpl() {
		if (_clanName == null) {
			return;
		}
		
		L2PcInstance player = getClient().getActiveChar();
		if (player == null) {
			return;
		}
		
		L2Clan leaderClan = player.getClan();
		if (leaderClan == null) {
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
			return;
		}
		
		if (leaderClan.getAllyId() == 0) {
			player.sendPacket(SystemMessageId.NO_CURRENT_ALLIANCES);
			return;
		}
		
		if (!player.isClanLeader() || (leaderClan.getId() != leaderClan.getAllyId())) {
			player.sendPacket(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
			return;
		}
		
		L2Clan clan = ClanTable.getInstance().getClanByName(_clanName);
		if (clan == null) {
			player.sendPacket(SystemMessageId.CLAN_DOESNT_EXISTS);
			return;
		}
		
		if (clan.getId() == leaderClan.getId()) {
			player.sendPacket(SystemMessageId.ALLIANCE_LEADER_CANT_WITHDRAW);
			return;
		}
		
		if (clan.getAllyId() != leaderClan.getAllyId()) {
			player.sendPacket(SystemMessageId.DIFFERENT_ALLIANCE);
			return;
		}
		
		long currentTime = System.currentTimeMillis();
		leaderClan.setAllyPenaltyExpiryTime(currentTime + DAYS.toMillis(character().getDaysBeforeAcceptNewClanWhenDismissed()), PENALTY_TYPE_DISMISS_CLAN);
		leaderClan.updateClanInDB();
		
		clan.setAllyId(0);
		clan.setAllyName(null);
		clan.changeAllyCrest(0, true);
		clan.setAllyPenaltyExpiryTime(currentTime + DAYS.toMillis(character().getDaysBeforeJoinAllyWhenDismissed()), PENALTY_TYPE_CLAN_DISMISSED);
		clan.updateClanInDB();
		player.sendPacket(SystemMessageId.YOU_HAVE_EXPELED_A_CLAN);
	}
	
	@Override
	public String getType() {
		return _C__8F_ALLYDISMISS;
	}
}
