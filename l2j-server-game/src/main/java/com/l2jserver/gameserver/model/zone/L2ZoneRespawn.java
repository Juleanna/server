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
package com.l2jserver.gameserver.model.zone;

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.model.Location;

/**
 * Abstract respawn zone.
 * @author DS
 * @author Nyaran
 * @since 2011/07/10
 */
public abstract class L2ZoneRespawn extends L2ZoneType {
	private List<Location> _spawnLocs = null;
	private List<Location> _otherSpawnLocs = null;
	private List<Location> _chaoticSpawnLocs = null;
	private List<Location> _banishSpawnLocs = null;
	
	protected L2ZoneRespawn(int id) {
		super(id);
	}
	
	public void parseLoc(int x, int y, int z, String type) {
		if ((type == null) || type.isEmpty()) {
			addSpawn(x, y, z);
		} else {
			switch (type) {
				case "other" -> addOtherSpawn(x, y, z);
				case "chaotic" -> addChaoticSpawn(x, y, z);
				case "banish" -> addBanishSpawn(x, y, z);
				default -> _log.warning(getClass().getSimpleName() + ": Unknown location type: " + type);
			}
		}
	}
	
	public final void addSpawn(int x, int y, int z) {
		if (_spawnLocs == null) {
			_spawnLocs = new ArrayList<>();
		}
		
		_spawnLocs.add(new Location(x, y, z));
	}
	
	public final void addOtherSpawn(int x, int y, int z) {
		if (_otherSpawnLocs == null) {
			_otherSpawnLocs = new ArrayList<>();
		}
		
		_otherSpawnLocs.add(new Location(x, y, z));
	}
	
	public final void addChaoticSpawn(int x, int y, int z) {
		if (_chaoticSpawnLocs == null) {
			_chaoticSpawnLocs = new ArrayList<>();
		}
		
		_chaoticSpawnLocs.add(new Location(x, y, z));
	}
	
	public final void addBanishSpawn(int x, int y, int z) {
		if (_banishSpawnLocs == null) {
			_banishSpawnLocs = new ArrayList<>();
		}
		
		_banishSpawnLocs.add(new Location(x, y, z));
	}
	
	public final List<Location> getSpawns() {
		return _spawnLocs;
	}
	
	public final Location getSpawnLoc() {
		if (character().randomRespawnInTown()) {
			return _spawnLocs.get(Rnd.get(_spawnLocs.size()));
		}
		return _spawnLocs.get(0);
	}
	
	public final Location getOtherSpawnLoc() {
		if (_otherSpawnLocs != null) {
			if (character().randomRespawnInTown()) {
				return _otherSpawnLocs.get(Rnd.get(_otherSpawnLocs.size()));
			}
			return _otherSpawnLocs.get(0);
		}
		return getSpawnLoc();
	}
	
	public final Location getChaoticSpawnLoc() {
		if (_chaoticSpawnLocs != null) {
			if (character().randomRespawnInTown()) {
				return _chaoticSpawnLocs.get(Rnd.get(_chaoticSpawnLocs.size()));
			}
			return _chaoticSpawnLocs.get(0);
		}
		return getSpawnLoc();
	}
	
	public final Location getBanishSpawnLoc() {
		if (_banishSpawnLocs != null) {
			if (character().randomRespawnInTown()) {
				return _banishSpawnLocs.get(Rnd.get(_banishSpawnLocs.size()));
			}
			return _banishSpawnLocs.get(0);
		}
		return getSpawnLoc();
	}
}
