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

import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.scripting.ScriptEngineManager;

/**
 * @author BiggBoss
 * @author UnAfraid
 */
public final class EffectHandler implements IHandler<Class<? extends AbstractEffect>, String> {
	private final Map<String, Class<? extends AbstractEffect>> _handlers;
	
	protected EffectHandler() {
		_handlers = new HashMap<>();
	}
	
	@Override
	public void registerHandler(Class<? extends AbstractEffect> handler) {
		_handlers.put(handler.getSimpleName(), handler);
	}
	
	@Override
	public synchronized void removeHandler(Class<? extends AbstractEffect> handler) {
		_handlers.remove(handler.getSimpleName());
	}
	
	@Override
	public Class<? extends AbstractEffect> getHandler(String name) {
		return _handlers.get(name);
	}
	
	@Override
	public int size() {
		return _handlers.size();
	}
	
	public void executeScript() throws Exception {
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/handlers/EffectMasterHandler.java");
	}
	
	public static EffectHandler getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder {
		protected static final EffectHandler INSTANCE = new EffectHandler();
	}
}
