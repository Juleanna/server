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
package com.l2jserver.gameserver.model.olympiad;

/**
 * @author JIV
 */
public class OlympiadInfo {
	private final String _name;
	private final String _clan;
	private final int _clanId;
	private final int _classId;
	private final int _dmg;
	private final int _curPoints;
	private final int _diffPoints;
	
	public OlympiadInfo(String name, String clan, int clanId, int classId, int dmg, int curPoints, int diffPoints) {
		_name = name;
		_clan = clan;
		_clanId = clanId;
		_classId = classId;
		_dmg = dmg;
		_curPoints = curPoints;
		_diffPoints = diffPoints;
	}
	
	/**
	 * @return the name the player's name.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * @return the name the player's clan name.
	 */
	public String getClanName() {
		return _clan;
	}
	
	/**
	 * @return the name the player's clan id.
	 */
	public int getClanId() {
		return _clanId;
	}
	
	/**
	 * @return the name the player's class id.
	 */
	public int getClassId() {
		return _classId;
	}
	
	/**
	 * @return the name the player's damage.
	 */
	public int getDamage() {
		return _dmg;
	}
	
	/**
	 * @return the name the player's current points.
	 */
	public int getCurrentPoints() {
		return _curPoints;
	}
	
	/**
	 * @return the name the player's points difference since this match.
	 */
	public int getDiffPoints() {
		return _diffPoints;
	}
}