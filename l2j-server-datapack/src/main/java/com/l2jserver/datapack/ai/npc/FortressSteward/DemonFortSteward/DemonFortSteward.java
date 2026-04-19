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
package com.l2jserver.datapack.ai.npc.FortressSteward.DemonFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class DemonFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010656, new Location(98920, -56423, -624), 0),
		new TelPosList(1010657, new Location(102390, -54320, -632), 0),
		new TelPosList(1010671, new Location(43889, -49101, -792), 0),
		new TelPosList(1010661, new Location(147728, -56331, -2776), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010656, new Location(98920, -56423, -624), 0),
		new TelPosList(1010657, new Location(102390, -54320, -632), 0),
		new TelPosList(1010671, new Location(43889, -49101, -792), 0),
		new TelPosList(1010661, new Location(147728, -56331, -2776), 0),
		new TelPosList(1010565, new Location(69340, -50203, -3314), 500),
		new TelPosList(1010600, new Location(65307, -71445, -3696), 500),
		new TelPosList(1010530, new Location(169018, -116303, -2432), 500),
		new TelPosList(1010568, new Location(165054, -47861, -3560), 500),
		new TelPosList(1010706, new Location(76911, -55295, -5824), 500)
	};
	
	private static final int npcId = 36317;
	
	public DemonFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}