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
package com.l2jserver.datapack.ai.npc.Steward.OlMahumTamutak;

import com.l2jserver.datapack.ai.npc.Steward.Steward;
import com.l2jserver.gameserver.model.Location;

/**
* @author Charus
* @version 2.6.3.0
*/
public class OlMahumTamutak extends Steward {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010035, new Location(46467, 126885, -3720), 500),
		new TelPosList(1010066, new Location(43534, 164260, -2984), 500),
		new TelPosList(1010033, new Location(5941, 125455, -3400), 500),
		new TelPosList(1010034, new Location(37566, 148224, -3699), 500),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 500),
		new TelPosList(1010036, new Location(17430, 170103, -3506), 500)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010035, new Location(46467, 126885, -3720), 500),
		new TelPosList(1010066, new Location(43534, 164260, -2984), 500),
		new TelPosList(1010033, new Location(5941, 125455, -3400), 500),
		new TelPosList(1010034, new Location(37566, 148224, -3699), 500),
		new TelPosList(1010006, new Location(15472, 142880, -2699), 500),
		new TelPosList(1010036, new Location(17430, 170103, -3506), 500),
		new TelPosList(1010041, new Location(16050, 114176, -3576), 500),
		new TelPosList(1010042, new Location(69770, 126371, -3800), 500),
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 500),
		new TelPosList(1010007, new Location(83336, 147972, -3404), 500)
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
		new BuySellList(7126, 10, 1.000000, 1), // escape_scroll_giran
		new BuySellList(7132, 10, 1.000000, 1), // escape_scroll_aden
		new BuySellList(8644, 10, 0.500000, 1), // rp_elixir_of_life_a
		new BuySellList(8650, 10, 0.500000, 1), // rp_elixir_of_mental_a
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(948, 10, 0.250000, 1) // scrl_of_ench_am_b
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 1), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7126, 10, 1.000000, 1), // escape_scroll_giran
		new BuySellList(7132, 10, 1.000000, 1), // escape_scroll_aden
		new BuySellList(7585, 10, 0.500000, 1), // ticket_kukaburo_ocarina
		new BuySellList(6926, 10, 1.000000, 1), // rp_lesser_healing_potion
		new BuySellList(6929, 10, 1.000000, 1), // rp_antidote
		new BuySellList(6931, 10, 1.000000, 1), // rp_bandage
		new BuySellList(6920, 10, 1.000000, 1), // rp_fish_oil_average
		new BuySellList(8364, 10, 1.000000, 1), // rp_party_hat
		new BuySellList(7690, 10, 1.000000, 1), // rp_outlaw_eyepatch
		new BuySellList(6940, 10, 1.000000, 1), // rp_masktype_a_potion
		new BuySellList(6941, 10, 1.000000, 1), // rp_masktype_b_potion
		new BuySellList(6942, 10, 1.000000, 1), // rp_masktype_c_potion
		new BuySellList(6978, 10, 1.000000, 1), // rp_dye_s2c2_c
		new BuySellList(6979, 10, 1.000000, 1), // rp_dye_s2d2_c
		new BuySellList(6980, 10, 1.000000, 1), // rp_dye_c2s2_c
		new BuySellList(6981, 10, 1.000000, 1), // rp_dye_c2c2_c
		new BuySellList(6982, 10, 1.000000, 1), // rp_dye_d2s2_c
		new BuySellList(6983, 10, 1.000000, 1), // rp_dye_d2c2_c
		new BuySellList(6984, 10, 1.000000, 1), // rp_dye_i2m2_c
		new BuySellList(6985, 10, 1.000000, 1), // rp_dye_i2w2_c
		new BuySellList(6986, 10, 1.000000, 1), // rp_dye_m2i2_c
		new BuySellList(6987, 10, 1.000000, 1), // rp_dye_m2w2_c
		new BuySellList(6988, 10, 1.000000, 1), // rp_dye_w2i2_c
		new BuySellList(6989, 10, 1.000000, 1), // rp_dye_w2m2_c
		new BuySellList(8644, 10, 0.500000, 1), // rp_elixir_of_life_a
		new BuySellList(8650, 10, 0.500000, 1), // rp_elixir_of_mental_a
		new BuySellList(8751, 10, 0.500000, 1), // rare_70_a
		new BuySellList(8752, 10, 0.250000, 1), // rare_75_s
		new BuySellList(8761, 10, 0.166000, 1), // unique_70_a
		new BuySellList(948, 10, 0.250000, 1), // scrl_of_ench_am_b
		new BuySellList(947, 10, 0.166000, 1), // scrl_of_ench_wp_b
		new BuySellList(730, 10, 0.166000, 1), // scrl_of_ench_am_a
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(16110, 10, 0.042000, 1), // rp_black_skeleton_circlet_i
		new BuySellList(16111, 10, 0.042000, 1), // rp_oldgold_skeleton_circlet_i
		new BuySellList(8762, 10, 0.083000, 1), // unique_75_s
		new BuySellList(16122, 10, 1.000000, 10), // black_skeleton_circlet_piece
		new BuySellList(16123, 10, 1.000000, 10) // oldgold_skeleton_circlet_piece
	};
	
	private static final int npcId = 35383;
	
	public OlMahumTamutak() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		
		super.fnHi = "data/html/clanHallManager/ol_mahum_steward_tamutak001.htm";
		super.fnNotMyLord = "data/html/clanHallManager/ol_mahum_steward_tamutak002.htm";
		super.fnBanish = "data/html/clanHallManager/ol_mahum_steward_tamutak004.htm";
		super.fnAfterOpenGate = "data/html/clanHallManager/ol_mahum_steward_tamutak006.htm";
		super.fnAfterCloseGate = "data/html/clanHallManager/ol_mahum_steward_tamutak007.htm";
		super.fnAfterBanish = "data/html/clanHallManager/ol_mahum_steward_tamutak008.htm";
		super.fnNotEnoughAdena = "data/html/clanHallManager/ol_mahum_steward_tamutak010.htm";
		super.fnNoAuthority = "data/html/clanHallManager/ol_mahum_steward_tamutak017.htm";
		super.fnIsUnderSiege = "data/html/clanHallManager/ol_mahum_steward_tamutak018.htm";
		
		super.fnManageRegen = "data/html/clanHallManager/ol_mahum_agitdeco_br01.htm";
		super.fnManageEtc = "data/html/clanHallManager/ol_mahum_agitdeco_be01.htm";
	}
}