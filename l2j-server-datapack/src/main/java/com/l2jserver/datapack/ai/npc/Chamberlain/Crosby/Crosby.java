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
package com.l2jserver.datapack.ai.npc.Chamberlain.Crosby;

import com.l2jserver.datapack.ai.npc.Chamberlain.Chamberlain;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Crosby extends Chamberlain {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010690, new Location(19888, 153395, -3144), 0),
		new TelPosList(1010669, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010687, new Location(20126, 188254, -3392), 0),
		new TelPosList(1010036, new Location(17430, 170103, -3506), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010690, new Location(19888, 153395, -3144), 0),
		new TelPosList(1010669, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010687, new Location(20126, 188254, -3392), 0),
		new TelPosList(1010036, new Location(17430, 170103, -3506), 0),
		new TelPosList(1010614, new Location(5106, 126916, -3664), 500),
		new TelPosList(1010113, new Location(47382, 111278, -2104), 500),
		new TelPosList(1010111, new Location(630, 179184, -3720), 500),
		new TelPosList(1010115, new Location(60374, 164301, -2856), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6316, 10, 0.000000, 0), // advdeluxe_food_for_strider
		new BuySellList(7015, 10, 0.000000, 0), // pledge_shield_castle
		new BuySellList(6835, 10, 0.000000, 0), // circlet_of_dion
		new BuySellList(9608, 10, 0.000000, 0), // lbracelet_castle_dion
		new BuySellList(13686, 10, 0.000000, 0), // sealed_knight_cloak_of_feud
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(8752, 10, 0.250000, 1), // rare_75_s
		new BuySellList(8761, 10, 0.166000, 1), // unique_70_a
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(16116, 10, 0.042000, 1), // rp_silver_horn_cap_i
		new BuySellList(8762, 10, 0.083000, 1), // unique_75_s
		new BuySellList(16128, 10, 1.000000, 10) // silver_horn_cap_piece
	};
	
	private static final int npcId = 35142;
	
	public Crosby() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnSetGate = "data/html/chamberlain/chamberlain_saul005.htm";
		super.fnMyFortressStatus = "data/html/chamberlain/chamberlain_saius070.htm";
		super.fnDoorStrengthen = "data/html/chamberlain/chamberlain_saul053.htm";
		super.fnSetSlowZone = "data/html/chamberlain/chamberlain_saul058.htm";
	}
}