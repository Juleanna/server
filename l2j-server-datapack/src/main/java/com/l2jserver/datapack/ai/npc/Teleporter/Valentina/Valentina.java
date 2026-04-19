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
package com.l2jserver.datapack.ai.npc.Teleporter.Valentina;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Valentina extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010023, new Location(146783, 25808, -2000), 6900),
		new TelPosList(1010007, new Location(83551, 147945, -3400), 9400),
		new TelPosList(1010200, new Location(43835, -47749, -792), 10000),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 37000),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 50000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 33000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 59000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 35000),
		new TelPosList(1010014, new Location(85391, 16228, -3640), 3700),
		new TelPosList(1010020, new Location(117088, 76931, -2670), 4100),
		new TelPosList(1010022, new Location(105918, 109759, -3170), 6100),
		new TelPosList(1010505, new Location(76839, 63851, -3648), 2400),
		new TelPosList(1010707, new Location(79414, 71496, -3448), 3700),
		new TelPosList(1010708, new Location(87448, 61460, -3664), 1800),
		new TelPosList(1010179, new Location(87252, 85514, -3103), 3900),
		new TelPosList(1010122, new Location(91539, -12204, -2440), 5200),
		new TelPosList(1010121, new Location(64328, 26803, -3768), 2500)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010180, new Location(93218, 16969, -3904), 1),
		new TelPosList(1010616, new Location(67097, 68815, -3648), 1),
		new TelPosList(1010160, new Location(-44566, 77508, -3736), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010180, new Location(93218, 16969, -3904), 1000),
		new TelPosList(1010616, new Location(67097, 68815, -3648), 1000),
		new TelPosList(1010160, new Location(-44566, 77508, -3736), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30177;
	
	public Valentina() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}