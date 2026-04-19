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
package com.l2jserver.datapack.ai.npc.FortressSteward.HiveFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class HiveFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010658, new Location(15383, 186025, -2920), 0),
		new TelPosList(1010659, new Location(18046, 190017, -2920), 0),
		new TelPosList(1010669, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010670, new Location(19888, 153395, -3144), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010658, new Location(15383, 186025, -2920), 0),
		new TelPosList(1010659, new Location(18046, 190017, -2920), 0),
		new TelPosList(1010669, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010670, new Location(19888, 153395, -3144), 0),
		new TelPosList(1010614, new Location(5106, 126916, -3664), 500),
		new TelPosList(1010113, new Location(47382, 111278, -2104), 500),
		new TelPosList(1010111, new Location(630, 179184, -3720), 500),
		new TelPosList(1010115, new Location(60374, 164301, -2856), 500)
	};
	
	private static final int npcId = 35727;
	
	public HiveFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}