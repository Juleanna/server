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

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.Util;

/**
 * Clan voiced handler implementation.
 * @author Zoey76
 */
public class ClanHandler implements IVoicedCommandHandler {
	private static final String[] COMMANDS = {
		"clan"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance player, String params) {
		if (!customs().allowClanHandler()) {
			return false;
		}
		
		if (!command.equals("clan")) {
			return false;
		}
		
		final var target = player.getTarget();
		if ((target == null) || !target.isPlayer()) {
			return false;
		}
		
		final var targetPlayer = target.getActingPlayer();
		if ((player.getClanId() <= 0) || (player.getClanId() != targetPlayer.getClanId())) {
			return false;
		}
		
		if (params.startsWith("privileges")) {
			final var val = params.substring(16);
			if (!Util.isDigit(val)) {
				return false;
			}
			
			final var n = Integer.parseInt(val);
			if ((player.getClanPrivileges().getBitmask() <= n) || !player.isClanLeader()) {
				return false;
			}
			
			targetPlayer.getClanPrivileges().setBitmask(n);
			targetPlayer.sendMessage("Your clan privileges have been set to " + n + " by " + player.getName() + ".");
		} else if (params.startsWith("title")) {
			if (!player.isClanLeader()) {
				return false;
			}
			final var title = params.substring(11);
			targetPlayer.setTitle(title);
			targetPlayer.sendMessage("Your title has been set to " + title + " by " + player.getName() + ".");
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return COMMANDS;
	}
}
