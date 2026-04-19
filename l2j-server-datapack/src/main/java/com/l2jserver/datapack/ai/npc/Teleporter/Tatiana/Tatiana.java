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
package com.l2jserver.datapack.ai.npc.Teleporter.Tatiana;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Tatiana extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 71000),
		new TelPosList(1010007, new Location(83551, 147945, -3400), 63000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 71000),
		new TelPosList(1010200, new Location(43835, -47749, -792), 10000),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 83000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 10000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 8100),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 37000),
		new TelPosList(1010491, new Location(125740, -40864, -3736), 4200),
		new TelPosList(1010492, new Location(146990, -67128, -3640), 1800),
		new TelPosList(1010609, new Location(144880, -113468, -2560), 9300),
		new TelPosList(1010568, new Location(165054, -47861, -3560), 2200),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 10000),
		new TelPosList(1010530, new Location(169018, -116303, -2432), 10000)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010534, new Location(107929, -52248, -2408), 1),
		new TelPosList(1010535, new Location(149817, -80053, -5576), 1),
		new TelPosList(1010550, new Location(106349, -61870, -2904), 1),
		new TelPosList(1010549, new Location(132997, -60608, -2960), 1),
		new TelPosList(1010610, new Location(144625, -101291, -3384), 1),
		new TelPosList(1010605, new Location(183140, -53307, -1896), 1),
		new TelPosList(1010529, new Location(191257, -59388, -2898), 1),
		new TelPosList(1010527, new Location(178127, -84435, -7215), 1),
		new TelPosList(1010528, new Location(186699, -75915, -2826), 1),
		new TelPosList(1010531, new Location(173436, -112725, -3680), 1),
		new TelPosList(1010532, new Location(180260, -111913, -5851), 1),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010534, new Location(107929, -52248, -2408), 1000),
		new TelPosList(1010535, new Location(149817, -80053, -5576), 1000),
		new TelPosList(1010550, new Location(106349, -61870, -2904), 1000),
		new TelPosList(1010549, new Location(132997, -60608, -2960), 1000),
		new TelPosList(1010610, new Location(144625, -101291, -3384), 1000),
		new TelPosList(1010605, new Location(183140, -53307, -1896), 1000),
		new TelPosList(1010529, new Location(191257, -59388, -2898), 1000),
		new TelPosList(1010527, new Location(178127, -84435, -7215), 1000),
		new TelPosList(1010528, new Location(186699, -75915, -2826), 1000),
		new TelPosList(1010531, new Location(173436, -112725, -3680), 1000),
		new TelPosList(1010532, new Location(180260, -111913, -5851), 1000),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 31275;
	
	public Tatiana() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}