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
import static com.l2jserver.gameserver.model.events.EventType.PLAYABLE_EXP_CHANGED;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.listeners.FunctionEventListener;
import com.l2jserver.gameserver.model.events.returns.TerminateReturn;

/**
 * Xp on/off voiced command implementation.
 * @author Zoey76
 * @version 2.6.3.0
 */
public class XpHandler implements IVoicedCommandHandler {
	private static final String[] COMMANDS = {
		"xp"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance player, String params) {
		if (!customs().allowXpHandler()) {
			return false;
		}
		
		if (player == null) {
			return false;
		}
		
		if ((params == null) || !(params.equals("on") || params.equals("off"))) {
			player.sendMessage("Invalid parameter. Please specify 'on' or 'off' to toggle experience gain.");
			return false;
		}
		
		final var xpGain = player.hasListener(PLAYABLE_EXP_CHANGED);
		switch (params) {
			case "off" -> {
				if (!xpGain) {
					player.addListener(new FunctionEventListener(player, PLAYABLE_EXP_CHANGED, _ -> new TerminateReturn(true, true, true), this));
					player.getVariables().set("xp_gain", true);
					player.sendMessage("Experience gain has been disabled. You will no longer earn XP until it is re-enabled.");
				} else {
					player.sendMessage("Experience gain is already disabled.");
				}
			}
			case "on" -> {
				if (xpGain) {
					player.removeListenerIf(PLAYABLE_EXP_CHANGED, listener -> listener.getOwner() == this);
					player.getVariables().set("xp_gain", false);
					player.sendMessage("Experience gain has been enabled. You will now earn XP as usual.");
				} else {
					player.sendMessage("Experience gain is already enabled.");
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return COMMANDS;
	}
}
