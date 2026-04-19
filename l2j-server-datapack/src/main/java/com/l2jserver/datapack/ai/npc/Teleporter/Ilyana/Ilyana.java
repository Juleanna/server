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
package com.l2jserver.datapack.ai.npc.Teleporter.Ilyana;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Ilyana extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010199, new Location(148024, -55281, -2728), 10000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 53000),
		new TelPosList(1010007, new Location(83551, 147945, -3400), 59000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 57000),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 82000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 10000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 37000),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 10000),
		new TelPosList(1010537, new Location(53516, -82831, -2704), 7200),
		new TelPosList(1010600, new Location(65307, -71445, -3696), 3800),
		new TelPosList(1010592, new Location(52107, -54328, -3158), 1200),
		new TelPosList(1010565, new Location(69340, -50203, -3314), 3000),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 14000),
		new TelPosList(1010577, new Location(89513, -44800, -2136), 9100),
		new TelPosList(1010698, new Location(11235, -24026, -3640), 6400)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010593, new Location(54425, -41692, -3072), 1),
		new TelPosList(1010601, new Location(84092, -80084, -3504), 1),
		new TelPosList(1010539, new Location(62084, -40935, -2802), 1),
		new TelPosList(1010706, new Location(76911, -55295, -5824), 1),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010593, new Location(54425, -41692, -3072), 1000),
		new TelPosList(1010601, new Location(84092, -80084, -3504), 1000),
		new TelPosList(1010539, new Location(62084, -40935, -2802), 1000),
		new TelPosList(1010706, new Location(76911, -55295, -5824), 1000),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 31320;
	
	public Ilyana() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}