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
package com.l2jserver.datapack.ai.npc.FortressSteward.SouthernFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class SouthernFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010656, new Location(-25480, 219848, -3248), 0),
		new TelPosList(1010657, new Location(-19886, 219821, -3256), 0),
		new TelPosList(1010663, new Location(-14393, 123671, -3144), 0),
		new TelPosList(1010664, new Location(-15043, 116596, -3208), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010656, new Location(-25480, 219848, -3248), 0),
		new TelPosList(1010657, new Location(-19886, 219821, -3256), 0),
		new TelPosList(1010663, new Location(-14393, 123671, -3144), 0),
		new TelPosList(1010664, new Location(-15043, 116596, -3208), 0),
		new TelPosList(1010097, new Location(-41248, 122848, -2912), 500),
		new TelPosList(1010101, new Location(-19120, 136816, -3762), 500),
		new TelPosList(1010566, new Location(-9959, 176184, -4160), 500),
		new TelPosList(1010102, new Location(-28327, 155125, -3496), 500)
	};
	
	private static final int npcId = 35689;
	
	public SouthernFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}