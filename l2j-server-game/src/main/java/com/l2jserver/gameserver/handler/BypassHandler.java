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

import java.util.HashMap;
import java.util.Map;

/**
 * @author nBd
 * @author UnAfraid
 */
public class BypassHandler implements IHandler<IBypassHandler, String> {
	private final Map<String, IBypassHandler> _datatable;
	
	protected BypassHandler() {
		_datatable = new HashMap<>();
	}
	
	@Override
	public void registerHandler(IBypassHandler handler) {
		for (String element : handler.getBypassList()) {
			_datatable.put(element.toLowerCase(), handler);
		}
	}
	
	@Override
	public synchronized void removeHandler(IBypassHandler handler) {
		for (String element : handler.getBypassList()) {
			_datatable.remove(element.toLowerCase());
		}
	}
	
	@Override
	public IBypassHandler getHandler(String command) {
		if (command.contains(" ")) {
			command = command.substring(0, command.indexOf(" "));
		}
		return _datatable.get(command.toLowerCase());
	}
	
	@Override
	public int size() {
		return _datatable.size();
	}
	
	public static BypassHandler getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final BypassHandler _instance = new BypassHandler();
	}
}