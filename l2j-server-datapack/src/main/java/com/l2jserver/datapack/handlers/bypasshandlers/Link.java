/*
 * Copyright © 2004-2023 L2J DataPack
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

public class Link implements IBypassHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(Link.class);
	
	private static final String[] COMMANDS = {
		"Link"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target) {
		String htmlPath = command.substring(4).trim();
		if (htmlPath.isEmpty()) {
			LOG.warn("Player {} sent empty link html!", activeChar);
			return false;
		}
		
		if (htmlPath.contains("..")) {
			LOG.warn("Player {} sent invalid link html: {}!", activeChar, htmlPath);
			return false;
		}
		
		String filename = "data/html/" + htmlPath;
		final NpcHtmlMessage html = new NpcHtmlMessage(target != null ? target.getObjectId() : 0);
		html.setFile(activeChar.getHtmlPrefix(), filename);
		html.replace("%objectId%", String.valueOf(target != null ? target.getObjectId() : 0));
		activeChar.sendPacket(html);
		return true;
	}
	
	@Override
	public String[] getBypassList() {
		return COMMANDS;
	}
}
