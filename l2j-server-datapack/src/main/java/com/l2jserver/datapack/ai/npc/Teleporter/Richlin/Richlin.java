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
package com.l2jserver.datapack.ai.npc.Teleporter.Richlin;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Richlin extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 7300),
		new TelPosList(1010001, new Location(-84141, 244623, -3729), 9400),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 16000),
		new TelPosList(1010156, new Location(9709, 15566, -4500), 16000),
		new TelPosList(1010158, new Location(115120, -178224, -917), 38000),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 26000),
		new TelPosList(1010648, new Location(-117251, 46771, 380), 10000),
		new TelPosList(1010100, new Location(-44763, 203497, -3592), 1800),
		new TelPosList(1010175, new Location(-75437, 168800, -3632), 550),
		new TelPosList(1010096, new Location(-63736, 101522, -3552), 1400),
		new TelPosList(1010106, new Location(-53001, 191425, -3568), 2000),
		new TelPosList(1010095, new Location(-89763, 105359, -3576), 1800),
		new TelPosList(1010094, new Location(-88539, 83389, -2864), 2600),
		new TelPosList(1010098, new Location(-49853, 147089, -2784), 1200),
		new TelPosList(1010621, new Location(-16526, 208032, -3664), 3400),
		new TelPosList(1010176, new Location(-42256, 198333, -2800), 3700)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010499, new Location(-54026, 179504, -4650), 1),
		new TelPosList(1010622, new Location(-47506, 179572, -3669), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010499, new Location(-54026, 179504, -4650), 1000),
		new TelPosList(1010622, new Location(-47506, 179572, -3669), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30320;
	
	public Richlin() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}