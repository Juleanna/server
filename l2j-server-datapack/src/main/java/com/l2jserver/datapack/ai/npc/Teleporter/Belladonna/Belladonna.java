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
package com.l2jserver.datapack.ai.npc.Teleporter.Belladonna;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Belladonna extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010574, new Location(87126, -143520, -1288), 85000),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 47000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 56000),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 35000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 3400),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 71000),
		new TelPosList(1010007, new Location(83551, 147945, -3400), 29000),
		new TelPosList(1010200, new Location(43835, -47749, -792), 53000),
		new TelPosList(1010004, new Location(-80684, 149770, -3043), 7300),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 9200),
		new TelPosList(1010156, new Location(9709, 15566, -4500), 10000),
		new TelPosList(1010158, new Location(115120, -178224, -917), 32000),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 23000),
		new TelPosList(1010648, new Location(-117251, 46771, 380), 12000),
		new TelPosList(1010652, new Location(-73983, 51956, -3680), 9300),
		new TelPosList(1010097, new Location(-41248, 122848, -2912), 790),
		new TelPosList(1010101, new Location(-19120, 136816, -3762), 610),
		new TelPosList(1010566, new Location(-9959, 176184, -4160), 2100),
		new TelPosList(1010102, new Location(-28327, 155125, -3496), 1400)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010171, new Location(-6989, 109503, -3040), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010171, new Location(-6989, 109503, -3040), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30256;
	
	public Belladonna() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}