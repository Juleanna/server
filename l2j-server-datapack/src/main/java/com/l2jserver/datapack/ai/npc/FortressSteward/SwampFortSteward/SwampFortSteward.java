/*
 * Copyright © 2004-2026 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.FortressSteward.SwampFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class SwampFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010658, new Location(68692, -63928, -2784), 0),
		new TelPosList(1010659, new Location(70820, -58862, -2784), 0),
		new TelPosList(1010671, new Location(43889, -49101, -792), 0),
		new TelPosList(1010672, new Location(27400, -49180, -1320), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010658, new Location(68692, -63928, -2784), 0),
		new TelPosList(1010659, new Location(70820, -58862, -2784), 0),
		new TelPosList(1010671, new Location(43889, -49101, -792), 0),
		new TelPosList(1010672, new Location(27400, -49180, -1320), 0),
		new TelPosList(1010592, new Location(52107, -54328, -3158), 500),
		new TelPosList(1010537, new Location(43805, -88010, -2780), 500),
		new TelPosList(1010565, new Location(69340, -50203, -3314), 500),
		new TelPosList(1010600, new Location(65307, -71445, -3696), 500),
		new TelPosList(1010706, new Location(76911, -55295, -5824), 500)
	};
	
	private static final int npcId = 35965;
	
	public SwampFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}