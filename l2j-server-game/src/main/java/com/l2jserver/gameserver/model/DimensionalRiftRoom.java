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
package com.l2jserver.gameserver.model;

import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import com.l2jserver.commons.util.Rnd;

/**
 * Dimensional Rift Room.
 * @author xban1x
 */
public final class DimensionalRiftRoom {
	private final byte _type;
	private final byte _room;
	private final int _xMin;
	private final int _xMax;
	private final int _yMin;
	private final int _yMax;
	private final int _zMin;
	private final int _zMax;
	private final Location _teleportCoords;
	private final Shape _s;
	private final boolean _isBossRoom;
	private final List<L2Spawn> _roomSpawns = new ArrayList<>();
	private L2Party _party = null;
	
	public DimensionalRiftRoom(byte type, byte room, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, int xT, int yT, int zT, boolean isBossRoom) {
		_type = type;
		_room = room;
		_xMin = (xMin + 128);
		_xMax = (xMax - 128);
		_yMin = (yMin + 128);
		_yMax = (yMax - 128);
		_zMin = zMin;
		_zMax = zMax;
		_teleportCoords = new Location(xT, yT, zT);
		_isBossRoom = isBossRoom;
		_s = new Polygon(new int[] {
			xMin,
			xMax,
			xMax,
			xMin
		}, new int[] {
			yMin,
			yMin,
			yMax,
			yMax
		}, 4);
	}
	
	public byte getType() {
		return _type;
	}
	
	public byte getRoom() {
		return _room;
	}
	
	public int getRandomX() {
		return Rnd.get(_xMin, _xMax);
	}
	
	public int getRandomY() {
		return Rnd.get(_yMin, _yMax);
	}
	
	public Location getTeleportCoordinates() {
		return _teleportCoords;
	}
	
	public boolean checkIfInZone(int x, int y, int z) {
		return _s.contains(x, y) && (z >= _zMin) && (z <= _zMax);
	}
	
	public boolean isBossRoom() {
		return _isBossRoom;
	}
	
	public List<L2Spawn> getSpawns() {
		return _roomSpawns;
	}
	
	public void spawn() {
		for (L2Spawn spawn : _roomSpawns) {
			spawn.doSpawn();
			spawn.startRespawn();
		}
	}
	
	public DimensionalRiftRoom unspawn() {
		for (L2Spawn spawn : _roomSpawns) {
			spawn.stopRespawn();
			if (spawn.getLastSpawn() != null) {
				spawn.getLastSpawn().deleteMe();
			}
		}
		return this;
	}
	
	/**
	 * Sets the party inside.
	 * @param party
	 */
	public void setParty(L2Party party) {
		_party = party;
	}
	
	public L2Party getParty() {
		return _party;
	}
}
