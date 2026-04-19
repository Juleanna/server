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
package com.l2jserver.datapack.ai.npc.FortressSteward.WesternFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class WesternFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010658, new Location(112356, -17243, -992), 0),
		new TelPosList(1010659, new Location(110349, -13288, -922), 0),
		new TelPosList(1010678, new Location(147450, 28081, -2294), 0),
		new TelPosList(1010681, new Location(82323, 55466, -1480), 0),
		new TelPosList(1010661, new Location(147728, -56331, -2776), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010658, new Location(112356, -17243, -992), 0),
		new TelPosList(1010659, new Location(110349, -13288, -922), 0),
		new TelPosList(1010678, new Location(147450, 28081, -2294), 0),
		new TelPosList(1010681, new Location(82323, 55466, -1480), 0),
		new TelPosList(1010661, new Location(147728, -56331, -2776), 0),
		new TelPosList(1010619, new Location(104426, 33746, -3825), 500),
		new TelPosList(1010060, new Location(155310, -16339, -3320), 500),
		new TelPosList(1010127, new Location(118509, -4779, -4000), 500),
		new TelPosList(1010530, new Location(169018, -116303, -2432), 500),
		new TelPosList(1010568, new Location(165054, -47861, -3560), 500),
		new TelPosList(1010702, new Location(183985, 61424, -3992), 500),
		new TelPosList(1010703, new Location(191754, 56760, -7624), 500)
	};
	
	private static final int npcId = 36210;
	
	public WesternFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}