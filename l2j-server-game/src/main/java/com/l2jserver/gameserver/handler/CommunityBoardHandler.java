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
package com.l2jserver.gameserver.handler;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.util.Util;

/**
 * Community Board handler.
 * @author Zoey76
 */
public final class CommunityBoardHandler implements IHandler<IParseBoardHandler, String> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommunityBoardHandler.class);
	
	/** The registered handlers. */
	private final Map<String, IParseBoardHandler> _datatable = new HashMap<>();
	/** The bypasses used by the players. */
	private final Map<Integer, String> _bypasses = new ConcurrentHashMap<>();
	
	protected CommunityBoardHandler() {
		// Prevent external initialization.
	}
	
	@Override
	public void registerHandler(IParseBoardHandler handler) {
		for (String cmd : handler.getCommunityBoardCommands()) {
			_datatable.put(cmd.toLowerCase(), handler);
		}
	}
	
	@Override
	public synchronized void removeHandler(IParseBoardHandler handler) {
		for (String cmd : handler.getCommunityBoardCommands()) {
			_datatable.remove(cmd.toLowerCase());
		}
	}
	
	@Override
	public IParseBoardHandler getHandler(String cmd) {
		for (IParseBoardHandler cb : _datatable.values()) {
			for (String command : cb.getCommunityBoardCommands()) {
				if (cmd.toLowerCase().startsWith(command.toLowerCase())) {
					return cb;
				}
			}
		}
		return null;
	}
	
	@Override
	public int size() {
		return _datatable.size();
	}
	
	/**
	 * Verifies if the string is a registered community board command.
	 * @param cmd the command to verify
	 * @return {@code true} if the command has been registered, {@code false} otherwise
	 */
	public boolean isCommunityBoardCommand(String cmd) {
		return getHandler(cmd) != null;
	}
	
	/**
	 * Parses a community board command.
	 * @param command the command
	 * @param player the player
	 */
	public void handleParseCommand(String command, L2PcInstance player) {
		if (player == null) {
			return;
		}
		
		if (!general().enableCommunityBoard()) {
			player.sendPacket(SystemMessageId.CB_OFFLINE);
			return;
		}
		
		final IParseBoardHandler cb = getHandler(command);
		if (cb == null) {
			LOG.warn("Couldn't find parse handler for command {}!", command);
			return;
		}
		
		cb.parseCommunityBoardCommand(command, player);
	}
	
	/**
	 * Writes a command into the client.
	 * @param player the player
	 * @param url the command URL
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 * @param arg3 the third argument
	 * @param arg4 the fourth argument
	 * @param arg5 the fifth argument
	 */
	public void handleWriteCommand(L2PcInstance player, String url, String arg1, String arg2, String arg3, String arg4, String arg5) {
		if (player == null) {
			return;
		}
		
		if (!general().enableCommunityBoard()) {
			player.sendPacket(SystemMessageId.CB_OFFLINE);
			return;
		}
		
		String cmd;
		switch (url) {
			case "Topic" -> cmd = "_bbstop";
			case "Post" -> cmd = "_bbspos"; // TODO: Implement.
			case "Region" -> cmd = "_bbsloc";
			case "Notice" -> cmd = "_bbsclan";
			default -> {
				separateAndSend("<html><body><br><br><center>The command: " + url + " is not implemented yet.</center><br><br></body></html>", player);
				return;
			}
		}
		
		final IParseBoardHandler cb = getHandler(cmd);
		if (cb == null) {
			LOG.warn("Couldn't find write handler for command {}!", cmd);
			return;
		}
		
		if (!(cb instanceof IWriteBoardHandler)) {
			LOG.warn("{} doesn't implement write!", cb.getClass().getSimpleName());
			return;
		}
		((IWriteBoardHandler) cb).writeCommunityBoardCommand(player, arg1, arg2, arg3, arg4, arg5);
	}
	
	/**
	 * Sets the last bypass used by the player.
	 * @param player the player
	 * @param title the title
	 * @param bypass the bypass
	 */
	public void addBypass(L2PcInstance player, String title, String bypass) {
		_bypasses.put(player.getObjectId(), title + "&" + bypass);
	}
	
	/**
	 * Removes the last bypass used by the player.
	 * @param player the player
	 * @return the last bypass used
	 */
	public String removeBypass(L2PcInstance player) {
		return _bypasses.remove(player.getObjectId());
	}
	
	/**
	 * Separates and send an HTML into multiple packets, to display into the community board.<br>
	 * The limit is 16383 characters.
	 * @param html the HTML to send
	 * @param player the player
	 */
	public static void separateAndSend(String html, L2PcInstance player) {
		Util.sendCBHtml(player, html);
	}
	
	public static CommunityBoardHandler getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final CommunityBoardHandler _instance = new CommunityBoardHandler();
	}
}
