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
package com.l2jserver.datapack.ai.npc.Chamberlain.Brasseur;

import com.l2jserver.datapack.ai.npc.Chamberlain.Chamberlain;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Brasseur extends Chamberlain {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010692, new Location(75648, 39380, -2952), 0),
		new TelPosList(1010681, new Location(82323, 55466, -1480), 0),
		new TelPosList(1010675, new Location(77023, 1591, -3608), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010692, new Location(75648, 39380, -2952), 0),
		new TelPosList(1010681, new Location(82323, 55466, -1480), 0),
		new TelPosList(1010675, new Location(77023, 1591, -3608), 0),
		new TelPosList(1010014, new Location(85391, 16228, -3640), 500),
		new TelPosList(1010055, new Location(109721, -7394, -2800), 500),
		new TelPosList(1010121, new Location(64328, 26803, -3768), 500),
		new TelPosList(1010618, new Location(124904, 61992, -3973), 500),
		new TelPosList(1010127, new Location(118509, -4779, -4000), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6316, 10, 0.000000, 0), // advdeluxe_food_for_strider
		new BuySellList(7015, 10, 0.000000, 0), // pledge_shield_castle
		new BuySellList(6837, 10, 0.000000, 0), // circlet_of_oren
		new BuySellList(9610, 10, 0.000000, 0), // lbracelet_castle_oren
		new BuySellList(13686, 10, 0.000000, 0), // sealed_knight_cloak_of_feud
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(8752, 10, 0.250000, 1), // rare_75_s
		new BuySellList(8761, 10, 0.166000, 1), // unique_70_a
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(16116, 10, 0.042000, 1), // rp_silver_horn_cap_i
		new BuySellList(8762, 10, 0.083000, 1), // unique_75_s
		new BuySellList(16128, 10, 1.000000, 10) // silver_horn_cap_piece
	};
	
	private static final int npcId = 35226;
	
	public Brasseur() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnSetGate = "data/html/chamberlain/chamberlain_saul005.htm";
		super.fnMyFortressStatus = "data/html/chamberlain/chamberlain_saul070.htm";
		super.fnDoorStrengthen = "data/html/chamberlain/chamberlain_saul053.htm";
		super.fnSetSlowZone = "data/html/chamberlain/chamberlain_saul058.htm";
	}
}