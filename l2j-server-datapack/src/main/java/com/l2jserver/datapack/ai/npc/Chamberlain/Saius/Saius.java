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
package com.l2jserver.datapack.ai.npc.Chamberlain.Saius;

import com.l2jserver.datapack.ai.npc.Chamberlain.Chamberlain;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Saius extends Chamberlain {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010689, new Location(-15043, 116596, -3208), 0),
		new TelPosList(1010663, new Location(-14393, 123671, -3144), 0),
		new TelPosList(1010680, new Location(-58761, 154663, -2701), 0),
		new TelPosList(1010667, new Location(-29856, 214275, -3680), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010689, new Location(-15043, 116596, -3208), 0),
		new TelPosList(1010663, new Location(-14393, 123671, -3144), 0),
		new TelPosList(1010680, new Location(-58761, 154663, -2701), 0),
		new TelPosList(1010667, new Location(-29856, 214275, -3680), 0),
		new TelPosList(1010097, new Location(-41248, 122848, -2912), 500),
		new TelPosList(1010101, new Location(-19120, 136816, -3762), 500),
		new TelPosList(1010566, new Location(-9959, 176184, -4160), 500),
		new TelPosList(1010102, new Location(-28327, 155125, -3496), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6316, 10, 0.000000, 0), // advdeluxe_food_for_strider
		new BuySellList(7015, 10, 0.000000, 0), // pledge_shield_castle
		new BuySellList(6838, 10, 0.000000, 0), // circlet_of_gludio
		new BuySellList(9607, 10, 0.000000, 0), // lbracelet_castle_gludio
		new BuySellList(13686, 10, 0.000000, 0), // sealed_knight_cloak_of_feud
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(8752, 10, 0.250000, 1), // rare_75_s
		new BuySellList(8761, 10, 0.166000, 1), // unique_70_a
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(16115, 10, 0.042000, 1), // rp_gold_horn_cap_i
		new BuySellList(8762, 10, 0.083000, 1), // unique_75_s
		new BuySellList(16127, 10, 1.000000, 10) // gold_horn_cap_piece
	};
	
	private static final int npcId = 35100;
	
	public Saius() {
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