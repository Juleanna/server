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
package com.l2jserver.datapack.ai.npc.Teleporter.Ragara;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Ragara extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 12000),
		new TelPosList(1010004, new Location(-80684, 149770, -3043), 18000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 26000),
		new TelPosList(1010001, new Location(-84141, 244623, -3729), 20000),
		new TelPosList(1010156, new Location(9709, 15566, -4500), 13000),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 16000),
		new TelPosList(1010158, new Location(115120, -178224, -917), 32000),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 17000),
		new TelPosList(1010653, new Location(-122410, 73205, -2859), 2600),
		new TelPosList(1010654, new Location(-95540, 52150, -2017), 2200),
		new TelPosList(1010655, new Location(-85928, 37095, -2040), 3200),
		new TelPosList(1010652, new Location(-73983, 51956, -3680), 4300)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final TelPosList[] _positionPoint = new TelPosList[] {
		new TelPosList(1010653, new Location(-122410, 73205, -2859), 0),
		new TelPosList(1010654, new Location(-95540, 52150, -2017), 0),
		new TelPosList(1010655, new Location(-85928, 37095, -2040), 0)
	};
	
	private static final int npcId = 32163;
	
	public Ragara() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
		super.positionPoint = _positionPoint;
	}
}