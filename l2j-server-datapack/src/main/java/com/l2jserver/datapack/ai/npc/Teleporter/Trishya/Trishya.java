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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.Teleporter.Trishya;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Trishya extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010007, new Location(83551, 147945, -3400), 6800),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 12000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 3400),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 71000),
		new TelPosList(1010200, new Location(43835, -47749, -792), 57000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 88000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 52000),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 33000),
		new TelPosList(1010614, new Location(5106, 126916, -3664), 760),
		new TelPosList(1010116, new Location(17225, 114173, -3440), 2300),
		new TelPosList(1010113, new Location(47382, 111278, -2104), 1700),
		new TelPosList(1010111, new Location(630, 179184, -3720), 1500),
		new TelPosList(1010599, new Location(34475, 188095, -2976), 2900),
		new TelPosList(1010115, new Location(60374, 164301, -2856), 3900)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010114, new Location(50568, 152408, -2656), 1),
		new TelPosList(1010177, new Location(26810, 172787, -3376), 1),
		new TelPosList(1010615, new Location(5941, 125455, -3400), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010114, new Location(50568, 152408, -2656), 1000),
		new TelPosList(1010177, new Location(26810, 172787, -3376), 1000),
		new TelPosList(1010615, new Location(5941, 125455, -3400), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30059;
	
	public Trishya() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}