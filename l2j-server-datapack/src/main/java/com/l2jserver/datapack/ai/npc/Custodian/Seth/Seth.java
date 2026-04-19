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
package com.l2jserver.datapack.ai.npc.Custodian.Seth;

import com.l2jserver.datapack.ai.npc.Custodian.Custodian;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Seth extends Custodian {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010024, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010026, new Location(21511, 145866, -3153), 0),
		new TelPosList(1010025, new Location(18891, 142365, -3051), 0),
		new TelPosList(1010028, new Location(17394, 147593, -3129), 0),
		new TelPosList(1010027, new Location(16582, 144130, -2960), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010024, new Location(19025, 145245, -3107), 0),
		new TelPosList(1010026, new Location(21511, 145866, -3153), 0),
		new TelPosList(1010025, new Location(18891, 142365, -3051), 0),
		new TelPosList(1010028, new Location(17394, 147593, -3129), 0),
		new TelPosList(1010027, new Location(16582, 144130, -2960), 0),
		new TelPosList(1010614, new Location(5106, 126916, -3664), 500),
		new TelPosList(1010113, new Location(47382, 111278, -2104), 500),
		new TelPosList(1010111, new Location(630, 179184, -3720), 500),
		new TelPosList(1010115, new Location(60374, 164301, -2856), 500),
		new TelPosList(1010036, new Location(17430, 170103, -3506), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 5) // deluxe_food_for_strider
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 0.500000, 1), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7128, 10, 1.000000, 1), // escape_scroll_heiness
		new BuySellList(7131, 10, 1.000000, 1), // escape_scroll_hunter
		new BuySellList(8643, 10, 0.500000, 1), // rp_elixir_of_life_b
		new BuySellList(8648, 10, 0.500000, 1) // rp_elixir_of_mental_c
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 1), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7128, 10, 1.000000, 1), // escape_scroll_heiness
		new BuySellList(7131, 10, 1.000000, 1), // escape_scroll_hunter
		new BuySellList(7585, 10, 0.500000, 1), // ticket_kukaburo_ocarina
		new BuySellList(6926, 10, 1.000000, 1), // rp_lesser_healing_potion
		new BuySellList(6929, 10, 1.000000, 1), // rp_antidote
		new BuySellList(6931, 10, 1.000000, 1), // rp_bandage
		new BuySellList(6920, 10, 1.000000, 1), // rp_fish_oil_average
		new BuySellList(7691, 10, 1.000000, 1), // rp_maidens_hairpin
		new BuySellList(6925, 10, 1.000000, 1), // rp_monocle
		new BuySellList(6940, 10, 1.000000, 1), // rp_masktype_a_potion
		new BuySellList(6941, 10, 1.000000, 1), // rp_masktype_b_potion
		new BuySellList(6942, 10, 1.000000, 1), // rp_masktype_c_potion
		new BuySellList(6954, 10, 1.000000, 1), // rp_dye_s1c1_d
		new BuySellList(6955, 10, 1.000000, 1), // rp_dye_s1d1_d
		new BuySellList(6956, 10, 1.000000, 1), // rp_dye_c1s1_d
		new BuySellList(6957, 10, 1.000000, 1), // rp_dye_c1d1_d
		new BuySellList(6958, 10, 1.000000, 1), // rp_dye_d1s1_d
		new BuySellList(6959, 10, 1.000000, 1), // rp_dye_d1c1_d
		new BuySellList(6960, 10, 1.000000, 1), // rp_dye_i1m1_d
		new BuySellList(6961, 10, 1.000000, 1), // rp_dye_i1w1_d
		new BuySellList(6962, 10, 1.000000, 1), // rp_dye_m1i1_d
		new BuySellList(6963, 10, 1.000000, 1), // rp_dye_m1w1_d
		new BuySellList(6964, 10, 1.000000, 1), // rp_dye_w1i1_d
		new BuySellList(6965, 10, 1.000000, 1), // rp_dye_w1m1_d
		new BuySellList(8643, 10, 0.500000, 1), // rp_elixir_of_life_b
		new BuySellList(8648, 10, 0.500000, 1), // rp_elixir_of_mental_c
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(952, 10, 0.500000, 1) // scrl_of_ench_am_c
	};
	
	private static final int npcId = 35407;
	
	public Seth() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
	}
}