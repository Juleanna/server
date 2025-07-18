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
package com.l2jserver.gameserver.model.events;

/**
 * @author UnAfraid
 */
public class Containers {
	private static final ListenersContainer _globalContainer = new ListenersContainer();
	private static final ListenersContainer _globalNpcsContainer = new ListenersContainer();
	private static final ListenersContainer _globalMonstersContainer = new ListenersContainer();
	private static final ListenersContainer _globalPlayersContainer = new ListenersContainer();
	
	protected Containers() {
		
	}
	
	public static ListenersContainer Global() {
		return _globalContainer;
	}
	
	public static ListenersContainer Npcs() {
		return _globalNpcsContainer;
	}
	
	public static ListenersContainer Monsters() {
		return _globalMonstersContainer;
	}
	
	public static ListenersContainer Players() {
		return _globalPlayersContainer;
	}
}
