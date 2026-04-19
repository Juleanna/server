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
package com.l2jserver.datapack.ai.npc.FortressSteward.DragonspineFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class DragonspineFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010658, new Location(12468, 93196, -3424), 0),
		new TelPosList(1010659, new Location(10494, 96881, -3424), 0),
		new TelPosList(1010669, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010681, new Location(82323, 55466, -1480), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010658, new Location(12468, 93196, -3424), 0),
		new TelPosList(1010659, new Location(10494, 96881, -3424), 0),
		new TelPosList(1010669, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010681, new Location(82323, 55466, -1480), 0),
		new TelPosList(1010566, new Location(-9959, 176184, -4160), 500),
		new TelPosList(1010102, new Location(-28327, 155125, -3496), 500),
		new TelPosList(1010121, new Location(64328, 26803, -3768), 500),
		new TelPosList(1010127, new Location(118509, -4779, -4000), 500)
	};
	
	private static final int npcId = 36141;
	
	public DragonspineFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}