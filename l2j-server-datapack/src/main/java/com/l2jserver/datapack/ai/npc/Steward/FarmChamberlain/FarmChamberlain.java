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
package com.l2jserver.datapack.ai.npc.Steward.FarmChamberlain;

import com.l2jserver.datapack.ai.npc.Steward.Steward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class FarmChamberlain extends Steward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010592, new Location(52107, -54328, -3158), 500),
		new TelPosList(1010537, new Location(43805, -88010, -2780), 500),
		new TelPosList(1010539, new Location(62084, -40935, -2802), 500),
		new TelPosList(1010200, new Location(43835, -47749, -792), 500)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010592, new Location(52107, -54328, -3158), 500),
		new TelPosList(1010537, new Location(43805, -88010, -2780), 500),
		new TelPosList(1010539, new Location(62084, -40935, -2802), 500),
		new TelPosList(1010600, new Location(65307, -71445, -3696), 500),
		new TelPosList(1010565, new Location(69340, -50203, -3314), 500),
		new TelPosList(1010575, new Location(106414, -87799, -2949), 500),
		new TelPosList(1010199, new Location(148024, -55281, -2728), 500),
		new TelPosList(1010200, new Location(43835, -47749, -792), 500),
		new TelPosList(1010706, new Location(76911, -55295, -5824), 500)
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(6643, 10, 0.000000, 0), // golden_spice
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 5) // deluxe_food_for_strider
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(6643, 10, 0.000000, 0), // golden_spice
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 2), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(952, 10, 0.500000, 1), // scrl_of_ench_am_c
		new BuySellList(7133, 10, 1.000000, 1), // escape_scroll_godard
		new BuySellList(7132, 10, 1.000000, 1), // escape_scroll_aden
		new BuySellList(8645, 10, 0.500000, 1), // rp_elixir_of_life_s
		new BuySellList(8651, 10, 0.500000, 1), // rp_elixir_of_mental_s
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(948, 10, 0.250000, 1) // scrl_of_ench_am_b
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(6643, 10, 0.000000, 0), // golden_spice
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 2), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(952, 10, 0.500000, 1), // scrl_of_ench_am_c
		new BuySellList(7133, 10, 1.000000, 1), // escape_scroll_godard
		new BuySellList(7132, 10, 1.000000, 1), // escape_scroll_aden
		new BuySellList(7583, 10, 0.500000, 1), // ticket_buffalo_panpipe
		new BuySellList(6927, 10, 1.000000, 1), // rp_healing_potion
		new BuySellList(6933, 10, 1.000000, 1), // rp_quick_step_potion
		new BuySellList(6934, 10, 1.000000, 1), // rp_swift_attack_potion
		new BuySellList(6930, 10, 1.000000, 1), // rp_advanced_antidote
		new BuySellList(6932, 10, 1.000000, 1), // rp_emergency_dressing
		new BuySellList(6937, 10, 1.000000, 1), // rp_potion_of_acumen2
		new BuySellList(6920, 10, 1.000000, 1), // rp_fish_oil_average
		new BuySellList(8368, 10, 1.000000, 1), // rp_goggle_of_artisan
		new BuySellList(6924, 10, 1.000000, 1), // rp_eye_bandage_of_pirate
		new BuySellList(6943, 10, 1.000000, 1), // rp_haircolor_a_potion
		new BuySellList(6944, 10, 1.000000, 1), // rp_haircolor_b_potion
		new BuySellList(6945, 10, 1.000000, 1), // rp_haircolor_c_potion
		new BuySellList(6946, 10, 1.000000, 1), // rp_haircolor_d_potion
		new BuySellList(6990, 10, 1.000000, 1), // rp_dye_s3c3_c
		new BuySellList(6991, 10, 1.000000, 1), // rp_dye_s3d3_c
		new BuySellList(6992, 10, 1.000000, 1), // rp_dye_c3s3_c
		new BuySellList(6993, 10, 1.000000, 1), // rp_dye_c3c3_c
		new BuySellList(6994, 10, 1.000000, 1), // rp_dye_d3s3_c
		new BuySellList(6995, 10, 1.000000, 1), // rp_dye_d3c3_c
		new BuySellList(6996, 10, 1.000000, 1), // rp_dye_i3m3_c
		new BuySellList(6997, 10, 1.000000, 1), // rp_dye_i3w3_c
		new BuySellList(6998, 10, 1.000000, 1), // rp_dye_m3i3_c
		new BuySellList(6999, 10, 1.000000, 1), // rp_dye_m3w3_c
		new BuySellList(7000, 10, 1.000000, 1), // rp_dye_w3i3_c
		new BuySellList(7001, 10, 1.000000, 1), // rp_dye_w3m3_c
		new BuySellList(8645, 10, 0.500000, 1), // rp_elixir_of_life_s
		new BuySellList(8651, 10, 0.500000, 1), // rp_elixir_of_mental_s
		new BuySellList(9606, 10, 0.000000, 0), // lbracelet_agit2
		new BuySellList(10142, 10, 0.500000, 1), // gray_talisman_transform_buffalo
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(8752, 10, 0.250000, 1), // rare_75_s
		new BuySellList(8761, 10, 0.166000, 1), // unique_70_a
		new BuySellList(948, 10, 0.250000, 1), // scrl_of_ench_am_b
		new BuySellList(947, 10, 0.166000, 1), // scrl_of_ench_wp_b
		new BuySellList(730, 10, 0.166000, 1), // scrl_of_ench_am_a
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(16121, 10, 0.042000, 1), // rp_cow_of_cap_i
		new BuySellList(8762, 10, 0.083000, 1), // unique_75_s
		new BuySellList(16133, 10, 1.000000, 10) // cow_of_cap_piece
	};
	
	private static final int npcId = 35628;
	
	public FarmChamberlain() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		
		super.fnHi = "data/html/clanHallManager/farm_chamberlain001.htm";
		super.fnNotMyLord = "data/html/clanHallManager/farm_chamberlain002.htm";
		super.fnBanish = "data/html/clanHallManager/farm_chamberlain004.htm";
		super.fnAfterOpenGate = "data/html/clanHallManager/farm_chamberlain006.htm";
		super.fnAfterCloseGate = "data/html/clanHallManager/farm_chamberlain007.htm";
		super.fnAfterBanish = "data/html/clanHallManager/farm_chamberlain008.htm";
		super.fnNotEnoughAdena = "data/html/clanHallManager/farm_chamberlain010.htm";
		super.fnNoAuthority = "data/html/clanHallManager/farm_chamberlain017.htm";
		super.fnIsUnderSiege = "data/html/clanHallManager/farm_chamberlain018.htm";
		
		super.fnManageRegen = "data/html/clanHallManager/ol_mahum_agitdeco_ar01.htm";
		super.fnManageEtc = "data/html/clanHallManager/ol_mahum_agitdeco_ae01.htm";
	}
}