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
package com.l2jserver.datapack.ai.npc.Steward.Biggerstaff;

import com.l2jserver.datapack.ai.npc.Steward.Steward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Biggerstaff extends Steward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010060, new Location(138879, -1860, -4050), 500),
		new TelPosList(1010553, new Location(181737, 46469, -4276), 500),
		new TelPosList(1010585, new Location(142065, 81300, -3000), 500),
		new TelPosList(1010023, new Location(144635, 26664, -2220), 500)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010060, new Location(138879, -1860, -4050), 500),
		new TelPosList(1010553, new Location(181737, 46469, -4276), 500),
		new TelPosList(1010585, new Location(142065, 81300, -3000), 500),
		new TelPosList(1010023, new Location(144635, 26664, -2220), 500),
		new TelPosList(1010604, new Location(184742, 19745, -3168), 500),
		new TelPosList(1010144, new Location(114649, 11115, -5120), 500),
		new TelPosList(1010020, new Location(117304, 76318, -2670), 500),
		new TelPosList(1010702, new Location(183985, 61424, -3992), 500),
		new TelPosList(1010703, new Location(191754, 56760, -7624), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(6316, 10, 0.000000, 0), // advdeluxe_food_for_strider
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 5) // deluxe_food_for_strider
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(6316, 10, 0.000000, 0), // advdeluxe_food_for_strider
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 3), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(952, 10, 0.500000, 1), // scrl_of_ench_am_c
		new BuySellList(948, 10, 0.200000, 1), // scrl_of_ench_am_b
		new BuySellList(730, 10, 0.040000, 1), // scrl_of_ench_am_a
		new BuySellList(7134, 10, 1.000000, 1), // escape_scroll_rune
		new BuySellList(7135, 10, 1.000000, 1), // escape_scroll_schtgart
		new BuySellList(8645, 10, 0.500000, 1), // rp_elixir_of_life_s
		new BuySellList(8651, 10, 0.500000, 1), // rp_elixir_of_mental_s
		new BuySellList(8751, 10, 0.500000, 1) // rare_70_a
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(6316, 10, 0.000000, 0), // advdeluxe_food_for_strider
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 3), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(952, 10, 0.500000, 1), // scrl_of_ench_am_c
		new BuySellList(948, 10, 0.200000, 1), // scrl_of_ench_am_b
		new BuySellList(730, 10, 0.040000, 1), // scrl_of_ench_am_a
		new BuySellList(7134, 10, 1.000000, 1), // escape_scroll_rune
		new BuySellList(7135, 10, 1.000000, 1), // escape_scroll_schtgart
		new BuySellList(7584, 10, 0.500000, 1), // ticket_cougar_chime
		new BuySellList(6928, 10, 1.000000, 1), // rp_greater_healing_potion
		new BuySellList(6935, 10, 1.000000, 1), // rp_adv_quick_step_potion
		new BuySellList(6936, 10, 1.000000, 1), // rp_adv_swift_attack_potion
		new BuySellList(6938, 10, 1.000000, 1), // rp_potion_of_acumen3
		new BuySellList(6921, 10, 1.000000, 1), // rp_fish_oil_high
		new BuySellList(8366, 10, 1.000000, 1), // rp_chaperon_of_dresser
		new BuySellList(7700, 10, 1.000000, 1), // rp_daisy_hairpin
		new BuySellList(6939, 10, 1.000000, 1), // rp_bighead_potion
		new BuySellList(6947, 10, 1.000000, 1), // rp_hairstyle_a_potion
		new BuySellList(6948, 10, 1.000000, 1), // rp_hairstyle_b_potion
		new BuySellList(6949, 10, 1.000000, 1), // rp_hairstyle_c_potion
		new BuySellList(6950, 10, 1.000000, 1), // rp_hairstyle_d_potion
		new BuySellList(6951, 10, 1.000000, 1), // rp_hairstyle_e_potion
		new BuySellList(6952, 10, 1.000000, 1), // rp_hairstyle_f_potion
		new BuySellList(6953, 10, 1.000000, 1), // rp_hairstyle_g_potion
		new BuySellList(7002, 10, 1.000000, 1), // rp_dye_s4c4_c
		new BuySellList(7003, 10, 1.000000, 1), // rp_dye_s4d4_c
		new BuySellList(7004, 10, 1.000000, 1), // rp_dye_c4s4_c
		new BuySellList(7005, 10, 1.000000, 1), // rp_dye_c4c4_c
		new BuySellList(7006, 10, 1.000000, 1), // rp_dye_d4s4_c
		new BuySellList(7007, 10, 1.000000, 1), // rp_dye_d4c4_c
		new BuySellList(7008, 10, 1.000000, 1), // rp_dye_i4m4_c
		new BuySellList(7009, 10, 1.000000, 1), // rp_dye_i4w4_c
		new BuySellList(7010, 10, 1.000000, 1), // rp_dye_m4i4_c
		new BuySellList(7011, 10, 1.000000, 1), // rp_dye_m4w4_c
		new BuySellList(7012, 10, 1.000000, 1), // rp_dye_w4i4_c
		new BuySellList(7013, 10, 1.000000, 1), // rp_dye_w4m4_c
		new BuySellList(8645, 10, 0.500000, 1), // rp_elixir_of_life_s
		new BuySellList(8651, 10, 0.500000, 1), // rp_elixir_of_mental_s
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(8752, 10, 0.250000, 1), // rare_75_s
		new BuySellList(8761, 10, 0.166000, 1), // unique_70_a
		new BuySellList(729, 10, 0.083000, 1), // scrl_of_ench_wp_a
		new BuySellList(960, 10, 0.042000, 1), // scrl_of_ench_am_s
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(16113, 10, 0.042000, 1), // rp_brown_skeleton_circlet_i
		new BuySellList(16112, 10, 0.042000, 1), // rp_green_skeleton_circlet_i
		new BuySellList(8762, 10, 0.083000, 1), // unique_75_s
		new BuySellList(16124, 10, 1.000000, 10), // green_skeleton_circlet_piece
		new BuySellList(16125, 10, 1.000000, 10) // brown_skeleton_circlet_piece
	};
	
	private static final int npcId = 35421;
	
	public Biggerstaff() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		
		super.fnHi = "data/html/clanHallManager/steward_biggerstaff001.htm";
		super.fnNotMyLord = "data/html/clanHallManager/steward_biggerstaff002.htm";
		super.fnBanish = "data/html/clanHallManager/steward_biggerstaff004.htm";
		super.fnAfterOpenGate = "data/html/clanHallManager/steward_biggerstaff006.htm";
		super.fnAfterCloseGate = "data/html/clanHallManager/steward_biggerstaff007.htm";
		super.fnAfterBanish = "data/html/clanHallManager/steward_biggerstaff008.htm";
		super.fnNotEnoughAdena = "data/html/clanHallManager/steward_biggerstaff010.htm";
		super.fnNoAuthority = "data/html/clanHallManager/steward_biggerstaff017.htm";
		super.fnIsUnderSiege = "data/html/clanHallManager/steward_biggerstaff018.htm";
		
		super.fnManageRegen = "data/html/clanHallManager/ol_mahum_agitdeco_ar01.htm";
		super.fnManageEtc = "data/html/clanHallManager/ol_mahum_agitdeco_ae01.htm";
	}
}