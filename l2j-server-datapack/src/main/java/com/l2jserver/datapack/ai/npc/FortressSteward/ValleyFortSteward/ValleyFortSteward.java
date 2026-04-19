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
package com.l2jserver.datapack.ai.npc.FortressSteward.ValleyFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class ValleyFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010658, new Location(126066, 120350, -2584), 0),
		new TelPosList(1010659, new Location(126023, 125853, -2584), 0),
		new TelPosList(1010665, new Location(81749, 149171, -3464), 0),
		new TelPosList(1010666, new Location(107954, 145841, -3224), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010658, new Location(126066, 120350, -2584), 0),
		new TelPosList(1010659, new Location(126023, 125853, -2584), 0),
		new TelPosList(1010665, new Location(81749, 149171, -3464), 0),
		new TelPosList(1010666, new Location(107954, 145841, -3224), 0),
		new TelPosList(1010118, new Location(85546, 131328, -3672), 500),
		new TelPosList(1010567, new Location(43408, 206881, -3752), 500),
		new TelPosList(1010067, new Location(73024, 118485, -3720), 500),
		new TelPosList(1010115, new Location(60374, 164301, -2856), 500)
	};
	
	private static final int npcId = 35758;
	
	public ValleyFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}