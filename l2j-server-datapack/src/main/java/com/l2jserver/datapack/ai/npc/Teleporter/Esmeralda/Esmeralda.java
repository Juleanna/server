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
package com.l2jserver.datapack.ai.npc.Teleporter.Esmeralda;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Esmeralda extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010023, new Location(146783, 25808, -2000), 5900),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 4100),
		new TelPosList(1010022, new Location(105918, 109759, -3170), 3400),
		new TelPosList(1010618, new Location(124904, 61992, -3973), 1300),
		new TelPosList(1010619, new Location(104426, 33746, -3825), 3600),
		new TelPosList(1010585, new Location(142065, 81300, -3000), 2000),
		new TelPosList(1010067, new Location(73024, 118485, -3720), 1800),
		new TelPosList(1010120, new Location(131557, 114509, -3712), 7000),
		new TelPosList(1010014, new Location(85391, 16228, -3640), 13000)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010586, new Location(166080, 75574, -2992), 1),
		new TelPosList(1010617, new Location(114674, 44150, -3376), 1),
		new TelPosList(1010130, new Location(114306, 86573, -3112), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1),
		new TelPosList(1010512, new Location(154396, 121235, -3808), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010586, new Location(166080, 75574, -2992), 1000),
		new TelPosList(1010617, new Location(114674, 44150, -3376), 1000),
		new TelPosList(1010130, new Location(114306, 86573, -3112), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000),
		new TelPosList(1010512, new Location(154396, 121235, -3808), 1000)
	};
	
	private static final int npcId = 30233;
	
	public Esmeralda() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}