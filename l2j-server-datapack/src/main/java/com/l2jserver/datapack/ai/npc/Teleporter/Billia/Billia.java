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
package com.l2jserver.datapack.ai.npc.Teleporter.Billia;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Billia extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010200, new Location(43835, -47749, -792), 10000),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 10000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 53000),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 59000),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 100000),
		new TelPosList(1010007, new Location(83551, 147945, -3400), 87000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 88000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 85000),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 13000),
		new TelPosList(1010158, new Location(115120, -178224, -917), 4400),
		new TelPosList(1010569, new Location(68693, -110438, -1946), 7500),
		new TelPosList(1010572, new Location(111965, -154172, -1528), 1600),
		new TelPosList(1010570, new Location(113903, -108752, -856), 3500),
		new TelPosList(1010571, new Location(47692, -115745, -3744), 9600),
		new TelPosList(1010573, new Location(91280, -117152, -3952), 5300),
		new TelPosList(1010594, new Location(171946, -173352, 3440), 17000)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010620, new Location(107577, -122392, -3632), 1),
		new TelPosList(1010591, new Location(44221, -114232, -2784), 1),
		new TelPosList(1010588, new Location(121618, -141554, -1496), 1),
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010620, new Location(107577, -122392, -3632), 1000),
		new TelPosList(1010591, new Location(44221, -114232, -2784), 1000),
		new TelPosList(1010588, new Location(121618, -141554, -1496), 1000),
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 31964;
	
	public Billia() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}