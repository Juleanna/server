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
package com.l2jserver.datapack.ai.npc.Teleporter.Rapunzel;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Rapunzel extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010004, new Location(-80684, 149770, -3043), 18000),
		new TelPosList(1010156, new Location(9709, 15566, -4500), 24000),
		new TelPosList(1010158, new Location(115120, -178224, -917), 46000),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 23000),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 35000),
		new TelPosList(1010648, new Location(-117251, 46771, 380), 20000),
		new TelPosList(1010557, new Location(-112367, 234703, -3688), 830),
		new TelPosList(1010558, new Location(-111728, 244330, -3448), 770),
		new TelPosList(1010559, new Location(-106696, 214691, -3424), 1000),
		new TelPosList(1010168, new Location(-99586, 237637, -3568), 470),
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010608, new Location(49315, 248452, -5960), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1),
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010608, new Location(49315, 248452, -5960), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000),
	};
	
	private static final TelPosList[] _positionNewbie = new TelPosList[] {
		new TelPosList(1010506, new Location(9709, 15566, -4500), 1),
		new TelPosList(1010507, new Location(115120, -178224, -917), 1),
		new TelPosList(1010608, new Location(46951, 51550, -2976), 1),
		new TelPosList(1010053, new Location(-45158, -112583, -236), 1),
	};
	
	private static final int npcId = 30006;
	
	public Rapunzel() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
		super.positionNewbie = _positionNewbie;
	}
}