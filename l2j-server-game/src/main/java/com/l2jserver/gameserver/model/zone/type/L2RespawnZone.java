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
package com.l2jserver.gameserver.model.zone.type;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;

/**
 * Respawn zone implementation.
 * @author Nyaran
 */
public class L2RespawnZone extends L2ZoneType {
	private final Map<Race, String> _raceRespawnPoint = new HashMap<>();
	
	public L2RespawnZone(int id) {
		super(id);
	}
	
	@Override
	protected void onEnter(L2Character character) {
	}
	
	@Override
	protected void onExit(L2Character character) {
	}
	
	public void addRaceRespawnPoint(String race, String point) {
		_raceRespawnPoint.put(Race.valueOf(race), point);
	}
	
	public Map<Race, String> getAllRespawnPoints() {
		return _raceRespawnPoint;
	}
	
	public String getRespawnPoint(L2PcInstance activeChar) {
		return _raceRespawnPoint.get(activeChar.getRace());
	}
}
