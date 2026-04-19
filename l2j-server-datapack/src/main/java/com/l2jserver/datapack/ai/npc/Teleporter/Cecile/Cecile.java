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
package com.l2jserver.datapack.ai.npc.Teleporter.Cecile;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Cecile extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010016, new Location(84856, 15912, -4270), 0),
		new TelPosList(1010015, new Location(85336, 16137, -3640), 0),
		new TelPosList(1010018, new Location(85391, 16228, -2270), 0),
		new TelPosList(1010019, new Location(85391, 16228, -1750), 0)
	};
	
	private static final int npcId = 30716;
	
	public Cecile() {
		super(npcId);
		
		super.position = _position;
	}
}