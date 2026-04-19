/*
 * Copyright © 2004-2026 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.handlers.bypasshandlers;

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.StringTokenizer;

import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.instancemanager.TownManager;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Teleport implements IBypassHandler {
	
	private static final String[] COMMANDS = {
		"teleport"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance player, L2Character target) {
		String portInfo = command.substring(9).trim();
		StringTokenizer st = new StringTokenizer(portInfo);
		
		var x = Integer.parseInt(st.nextToken());
		var y = Integer.parseInt(st.nextToken());
		var z = Integer.parseInt(st.nextToken());
		
		var itemId = Integer.parseInt(st.nextToken());
		var ammount = Long.parseLong(st.nextToken());
		
		if (SiegeManager.getInstance().getSiege(x, y, z) != null) {
			player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
			return false;
		}
		if (TownManager.townHasCastleInSiege(x, y) && target.isInsideZone(ZoneId.TOWN)) {
			player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
			return false;
		}
		if (player.isCombatFlagEquipped()) {
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
			return false;
		}
		if (player.isAlikeDead()) {
			return false;
		}
		
		if (character().freeTeleporting() || player.destroyItemByItemId("teleport", itemId, ammount, target, true)) {
			player.teleToLocation(x, y, z, player.getHeading(), -1);
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		return true;
	}

	@Override
	public String[] getBypassList() {
		return COMMANDS;
	}
}