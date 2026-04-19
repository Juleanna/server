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
package com.l2jserver.datapack.ai.npc.Teleporter.Elisabeth;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Elisabeth extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010199, new Location(148024, -55281, -2728), 8100),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 6900),
		new TelPosList(1010007, new Location(83551, 147945, -3400), 13000),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 59000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 53000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 52000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 56000),
		new TelPosList(1010200, new Location(43835, -47749, -792), 37000),
		new TelPosList(1010648, new Location(-117251, 46771, 380), 26000),
		new TelPosList(1010020, new Location(117088, 76931, -2670), 5900),
		new TelPosList(1010014, new Location(85391, 16228, -3640), 6200),
		new TelPosList(1010652, new Location(-73983, 51956, -3680), 22000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 2000),
		new TelPosList(1010143, new Location(168217, 37990, -4072), 1900),
		new TelPosList(1010604, new Location(184742, 19745, -3168), 3000),
		new TelPosList(1010585, new Location(142065, 81300, -3000), 4400),
		new TelPosList(1010060, new Location(155310, -16339, -3320), 6800),
		new TelPosList(1010181, new Location(183543, -14974, -2768), 6500),
		new TelPosList(1010127, new Location(106517, -2871, -3454), 5900),
		new TelPosList(1010182, new Location(170838, 55776, -5280), 6100),
		new TelPosList(1010144, new Location(114649, 11115, -5120), 4200),
		new TelPosList(1010553, new Location(174491, 50942, -4360), 7400)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010702, new Location(183985, 61424, -3992), 1),
		new TelPosList(1010703, new Location(191754, 56760, -7624), 1),
		new TelPosList(1010133, new Location(135580, 19467, -3424), 1),
		new TelPosList(1010134, new Location(156898, 11217, -4032), 1),
		new TelPosList(1010193, new Location(110848, 16154, -2120), 1),
		new TelPosList(1010194, new Location(118404, 15988, 832), 1),
		new TelPosList(1010195, new Location(115064, 12181, 2960), 1),
		new TelPosList(1010196, new Location(118525, 16455, 5984), 1),
		new TelPosList(1010197, new Location(115384, 16820, 9000), 1),
		new TelPosList(1010130, new Location(114306, 86573, -3112), 1),
		new TelPosList(1010607, new Location(166182, 91560, -3168), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010702, new Location(183985, 61424, -3992), 1000),
		new TelPosList(1010703, new Location(191754, 56760, -7624), 1000),
		new TelPosList(1010133, new Location(135580, 19467, -3424), 1000),
		new TelPosList(1010134, new Location(156898, 11217, -4032), 1000),
		new TelPosList(1010193, new Location(110848, 16154, -2120), 1000),
		new TelPosList(1010194, new Location(118404, 15988, 832), 1000),
		new TelPosList(1010195, new Location(115064, 12181, 2960), 1000),
		new TelPosList(1010196, new Location(118525, 16455, 5984), 1000),
		new TelPosList(1010197, new Location(115384, 16820, 9000), 1000),
		new TelPosList(1010130, new Location(114306, 86573, -3112), 1000),
		new TelPosList(1010607, new Location(166182, 91560, -3168), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30848;
	
	public Elisabeth() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}