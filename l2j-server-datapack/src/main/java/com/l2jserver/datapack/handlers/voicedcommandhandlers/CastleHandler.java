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
package com.l2jserver.datapack.handlers.voicedcommandhandlers;

import static com.l2jserver.gameserver.config.Configuration.customs;
import static com.l2jserver.gameserver.network.SystemMessageId.GATES_NOT_OPENED_CLOSED_DURING_SIEGE;
import static com.l2jserver.gameserver.network.SystemMessageId.GATE_IS_OPENING;
import static com.l2jserver.gameserver.network.SystemMessageId.INCORRECT_TARGET;
import static com.l2jserver.gameserver.network.SystemMessageId.ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Castle voiced handler implementation.
 * @author Zoey76
 */
public class CastleHandler implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"opendoors",
		"closedoors",
		"ridewyvern"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance player, String params) {
		if (!customs().allowCastleHandler()) {
			return false;
		}
		
		switch (command) {
			case "opendoors":
				if (!params.equals("castle")) {
					player.sendMessage("Only Castle doors can be open.");
					return false;
				}
				
				if (!player.isClanLeader()) {
					player.sendPacket(ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS);
					return false;
				}
				
				final var door = (L2DoorInstance) player.getTarget();
				if (door == null) {
					player.sendPacket(INCORRECT_TARGET);
					return false;
				}
				
				final var castle = CastleManager.getInstance().getCastleById(player.getClan().getCastleId());
				if (castle == null) {
					player.sendMessage("Your clan does not own a castle.");
					return false;
				}
				
				if (castle.getSiege().isInProgress()) {
					player.sendPacket(GATES_NOT_OPENED_CLOSED_DURING_SIEGE);
					return false;
				}
				
				if (castle.checkIfInZone(door.getX(), door.getY(), door.getZ())) {
					player.sendPacket(GATE_IS_OPENING);
					door.openMe();
				}
				break;
			case "closedoors":
				if (!params.equals("castle")) {
					player.sendMessage("Only Castle doors can be closed.");
					return false;
				}
				if (!player.isClanLeader()) {
					player.sendPacket(ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS);
					return false;
				}
				final var door2 = (L2DoorInstance) player.getTarget();
				if (door2 == null) {
					player.sendPacket(INCORRECT_TARGET);
					return false;
				}
				final var castle2 = CastleManager.getInstance().getCastleById(player.getClan().getCastleId());
				if (castle2 == null) {
					player.sendMessage("Your clan does not own a castle.");
					return false;
				}
				
				if (castle2.getSiege().isInProgress()) {
					player.sendPacket(GATES_NOT_OPENED_CLOSED_DURING_SIEGE);
					return false;
				}
				
				if (castle2.checkIfInZone(door2.getX(), door2.getY(), door2.getZ())) {
					player.sendMessage("The gate is being closed.");
					door2.closeMe();
				}
				break;
			case "ridewyvern":
				if (player.isClanLeader() && (player.getClan().getCastleId() > 0)) {
					player.mount(12621, 0, true);
				}
				break;
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}
