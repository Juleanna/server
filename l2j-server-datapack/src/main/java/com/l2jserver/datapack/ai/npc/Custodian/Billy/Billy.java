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
package com.l2jserver.datapack.ai.npc.Custodian.Billy;

import com.l2jserver.datapack.ai.npc.Custodian.Custodian;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Billy extends Custodian {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010024, new Location(87379, -142322, -1336), 0),
		new TelPosList(1010027, new Location(84753, -141051, -1536), 0),
		new TelPosList(1010028, new Location(87347, -139802, -1536), 0),
		new TelPosList(1010026, new Location(89959, -141034, -1536), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010024, new Location(87379, -142322, -1336), 0),
		new TelPosList(1010027, new Location(84753, -141051, -1536), 0),
		new TelPosList(1010028, new Location(87347, -139802, -1536), 0),
		new TelPosList(1010026, new Location(89959, -141034, -1536), 0),
		new TelPosList(1010571, new Location(47692, -115745, -3744), 500),
		new TelPosList(1010572, new Location(111965, -154172, -1528), 500),
		new TelPosList(1010569, new Location(68693, -110438, -1946), 500),
		new TelPosList(1010570, new Location(113903, -108752, -860), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 5) // deluxe_food_for_strider
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 1), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7130, 10, 1.000000, 1), // escape_scroll_ivorytower
		new BuySellList(7131, 10, 1.000000, 1), // escape_scroll_hunter
		new BuySellList(8643, 10, 0.500000, 1), // rp_elixir_of_life_b
		new BuySellList(8648, 10, 0.500000, 1) // rp_elixir_of_mental_c
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 1), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7130, 10, 1.000000, 1), // escape_scroll_ivorytower
		new BuySellList(7131, 10, 1.000000, 1), // escape_scroll_hunter
		new BuySellList(7583, 10, 0.500000, 1), // ticket_buffalo_panpipe
		new BuySellList(6927, 10, 1.000000, 1), // rp_healing_potion
		new BuySellList(6933, 10, 1.000000, 1), // rp_quick_step_potion
		new BuySellList(6934, 10, 1.000000, 1), // rp_swift_attack_potion
		new BuySellList(6930, 10, 1.000000, 1), // rp_advanced_antidote
		new BuySellList(6932, 10, 1.000000, 1), // rp_emergency_dressing
		new BuySellList(6937, 10, 1.000000, 1), // rp_potion_of_acumen2
		new BuySellList(6920, 10, 1.000000, 1), // rp_fish_oil_average
		new BuySellList(6922, 10, 1.000000, 1), // rp_party_mask
		new BuySellList(7690, 10, 1.000000, 1), // rp_outlaw_eyepatch
		new BuySellList(6943, 10, 1.000000, 1), // rp_haircolor_a_potion
		new BuySellList(6944, 10, 1.000000, 1), // rp_haircolor_b_potion
		new BuySellList(6945, 10, 1.000000, 1), // rp_haircolor_c_potion
		new BuySellList(6946, 10, 1.000000, 1), // rp_haircolor_d_potion
		new BuySellList(6966, 10, 1.000000, 1), // rp_dye_s1c1_c
		new BuySellList(6967, 10, 1.000000, 1), // rp_dye_s1d1_c
		new BuySellList(6968, 10, 1.000000, 1), // rp_dye_c1s1_c
		new BuySellList(6969, 10, 1.000000, 1), // rp_dye_c1c1_c
		new BuySellList(6970, 10, 1.000000, 1), // rp_dye_d1s1_c
		new BuySellList(6971, 10, 1.000000, 1), // rp_dye_d1c1_c
		new BuySellList(6972, 10, 1.000000, 1), // rp_dye_i1m1_c
		new BuySellList(6973, 10, 1.000000, 1), // rp_dye_i1w1_c
		new BuySellList(6974, 10, 1.000000, 1), // rp_dye_m1i1_c
		new BuySellList(6975, 10, 1.000000, 1), // rp_dye_m1w1_c
		new BuySellList(6976, 10, 1.000000, 1), // rp_dye_w1i1_c
		new BuySellList(6977, 10, 1.000000, 1), // rp_dye_w1m1_c
		new BuySellList(8643, 10, 0.500000, 1), // rp_elixir_of_life_b
		new BuySellList(8648, 10, 0.500000, 1), // rp_elixir_of_mental_c
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(952, 10, 0.500000, 1), // scrl_of_ench_am_c
		new BuySellList(948, 10, 0.250000, 1) // scrl_of_ench_am_b
	};
	
	private static final int npcId = 35584;
	
	public Billy() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
	}
}