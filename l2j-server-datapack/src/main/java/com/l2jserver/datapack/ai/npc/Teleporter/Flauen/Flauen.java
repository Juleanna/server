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
package com.l2jserver.datapack.ai.npc.Teleporter.Flauen;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Flauen extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010007, new Location(83551, 147945, -3400), 7600),
		new TelPosList(1010013, new Location(82971, 53207, -1470), 50000),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 12000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 59000),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 83000),
		new TelPosList(1010200, new Location(43835, -47749, -792), 82000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 100000),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 47000),
		new TelPosList(1010021, new Location(47938, 186864, -3420), 7100),
		new TelPosList(1010089, new Location(87691, 162835, -3563), 12000),
		new TelPosList(1010090, new Location(82192, 226128, -3664), 5900),
		new TelPosList(1010150, new Location(115583, 192261, -3488), 2100),
		new TelPosList(1010153, new Location(84413, 234334, -3656), 2400),
		new TelPosList(1010699, new Location(149518, 195280, -3736), 7200),
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010704, new Location(80987, 182423, -3504), 1),
		new TelPosList(1010705, new Location(86147, 218268, -3592), 1),
		new TelPosList(1010587, new Location(75387, 195263, -3000), 1),
		new TelPosList(1010602, new Location(97786, 209303, -3040), 1),
		new TelPosList(1010606, new Location(113708, 178387, -3232), 1),
		new TelPosList(1010526, new Location(82693, 242220, -6712), 1),
		new TelPosList(1010522, new Location(79248, 247390, -8816), 1),
		new TelPosList(1010523, new Location(77868, 250400, -9328), 1),
		new TelPosList(1010524, new Location(78721, 253309, -9840), 1),
		new TelPosList(1010525, new Location(82951, 252354, -10592), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010704, new Location(80987, 182423, -3504), 1000),
		new TelPosList(1010705, new Location(86147, 218268, -3592), 1000),
		new TelPosList(1010587, new Location(75387, 195263, -3000), 1000),
		new TelPosList(1010602, new Location(97786, 209303, -3040), 1000),
		new TelPosList(1010606, new Location(113708, 178387, -3232), 1000),
		new TelPosList(1010526, new Location(82693, 242220, -6712), 1000),
		new TelPosList(1010522, new Location(79248, 247390, -8816), 1000),
		new TelPosList(1010523, new Location(77868, 250400, -9328), 1000),
		new TelPosList(1010524, new Location(78721, 253309, -9840), 1000),
		new TelPosList(1010525, new Location(82951, 252354, -10592), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int npcId = 30899;
	
	public Flauen() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
}