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
package com.l2jserver.datapack.ai.npc.FortressSteward.HuntersFortSteward;

import com.l2jserver.datapack.ai.npc.FortressSteward.FortressSteward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class HuntersFortSteward extends FortressSteward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010658, new Location(124180, 93249, -2144), 0),
		new TelPosList(1010659, new Location(126155, 97124, -2144), 0),
		new TelPosList(1010678, new Location(147450, 28081, -2294), 0),
		new TelPosList(1010665, new Location(81749, 149171, -3464), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010658, new Location(124180, 93249, -2144), 0),
		new TelPosList(1010659, new Location(126155, 97124, -2144), 0),
		new TelPosList(1010678, new Location(147450, 28081, -2294), 0),
		new TelPosList(1010665, new Location(81749, 149171, -3464), 0),
		new TelPosList(1010619, new Location(104426, 33746, -3825), 500),
		new TelPosList(1010060, new Location(155310, -16339, -3320), 500),
		new TelPosList(1010067, new Location(73024, 118485, -3720), 500),
		new TelPosList(1010115, new Location(60374, 164301, -2856), 500),
		new TelPosList(1010702, new Location(183985, 61424, -3992), 500),
		new TelPosList(1010703, new Location(191754, 56760, -7624), 500)
	};
	
	private static final int npcId = 36248;
	
	public HuntersFortSteward() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
	}
}