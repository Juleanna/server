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

/**
 * Holds a list of all AirShip teleports.
 * @author xban1x
 */
public final class AirShipTeleportList {
	private final int _location;
	private final int[] _fuel;
	private final VehiclePathPoint[][] _routes;
	
	public AirShipTeleportList(int loc, int[] f, VehiclePathPoint[][] r) {
		_location = loc;
		_fuel = f;
		_routes = r;
	}
	
	public int getLocation() {
		return _location;
	}
	
	public int[] getFuel() {
		return _fuel;
	}
	
	public VehiclePathPoint[][] getRoute() {
		return _routes;
	}
}
