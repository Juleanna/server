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

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.ZoneId;

/**
 * The Monster Derby Track Zone
 * @author durgus
 */
public class L2DerbyTrackZone extends L2ZoneType {
	public L2DerbyTrackZone(int id) {
		super(id);
	}
	
	@Override
	protected void onEnter(L2Character character) {
		if (character.isPlayable()) {
			character.setInsideZone(ZoneId.MONSTER_TRACK, true);
		}
	}
	
	@Override
	protected void onExit(L2Character character) {
		if (character.isPlayable()) {
			character.setInsideZone(ZoneId.MONSTER_TRACK, false);
		}
	}
}
