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
package com.l2jserver.datapack.ai.npc.Teleporter.Clavier;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Clavier extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010013, new Location(82971, 53207, -1470), 9400),
		new TelPosList(1010049, new Location(111455, 219400, -3546), 7600),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 6800),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 63000),
		new TelPosList(1010200, new Location(43835, -47749, -792), 59000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 87000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 29000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 13000),
		new TelPosList(1010021, new Location(47938, 186864, -3420), 5200),
		new TelPosList(1010022, new Location(105918, 109759, -3170), 4400),
		new TelPosList(1010567, new Location(43408, 206881, -3752), 5700),
		new TelPosList(1010118, new Location(85546, 131328, -3672), 1000)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010119, new Location(113553, 134813, -3540), 1),
		new TelPosList(1010022, new Location(105918, 109759, -3170), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010119, new Location(113553, 134813, -3540), 1000),
		new TelPosList(1010022, new Location(105918, 109759, -3170), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30080;
	
	public Clavier() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}