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
package com.l2jserver.datapack.ai.npc.Custodian.Carey;

import com.l2jserver.datapack.ai.npc.Custodian.Custodian;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Carey extends Custodian {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010024, new Location(147450, 28081, -2294), 0),
		new TelPosList(1010026, new Location(151950, 25094, -2172), 0),
		new TelPosList(1010027, new Location(142593, 26344, -2425), 0),
		new TelPosList(1010028, new Location(147503, 32299, -2501), 0),
		new TelPosList(1010025, new Location(147465, 20737, -2130), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010024, new Location(147450, 28081, -2294), 0),
		new TelPosList(1010026, new Location(151950, 25094, -2172), 0),
		new TelPosList(1010027, new Location(142593, 26344, -2425), 0),
		new TelPosList(1010028, new Location(147503, 32299, -2501), 0),
		new TelPosList(1010025, new Location(147465, 20737, -2130), 0),
		new TelPosList(1010127, new Location(106517, -2871, -3454), 500),
		new TelPosList(1010618, new Location(124904, 61992, -3973), 500),
		new TelPosList(1010619, new Location(104426, 33746, -3825), 500),
		new TelPosList(1010060, new Location(155310, -16339, -3320), 500),
		new TelPosList(1010585, new Location(142065, 81300, -3000), 500),
		new TelPosList(1010607, new Location(166182, 91560, -3168), 500),
		new TelPosList(1010137, new Location(181726, -7524, -3464), 500),
		new TelPosList(1010192, new Location(168779, -18790, -3184), 500),
		new TelPosList(1010604, new Location(184742, 19745, -3168), 500),
		new TelPosList(1010143, new Location(168217, 37990, -4072), 500),
		new TelPosList(1010144, new Location(114649, 11115, -5120), 500),
		new TelPosList(1010702, new Location(183985, 61424, -3992), 500),
		new TelPosList(1010703, new Location(191754, 56760, -7624), 500),
	};
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 5) // deluxe_food_for_strider
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 3), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7134, 10, 1.000000, 1), // escape_scroll_rune
		new BuySellList(7126, 10, 1.000000, 1), // escape_scroll_giran
		new BuySellList(8644, 10, 0.500000, 1), // rp_elixir_of_life_a
		new BuySellList(8650, 10, 0.500000, 1) // rp_elixir_of_mental_a
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 3), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7134, 10, 1.000000, 1), // escape_scroll_rune
		new BuySellList(7126, 10, 1.000000, 1), // escape_scroll_giran
		new BuySellList(7584, 10, 0.500000, 1), // ticket_cougar_chime
		new BuySellList(6928, 10, 1.000000, 1), // rp_greater_healing_potion
		new BuySellList(6935, 10, 1.000000, 1), // rp_adv_quick_step_potion
		new BuySellList(6936, 10, 1.000000, 1), // rp_adv_swift_attack_potion
		new BuySellList(6938, 10, 1.000000, 1), // rp_potion_of_acumen3
		new BuySellList(6921, 10, 1.000000, 1), // rp_fish_oil_high
		new BuySellList(8371, 10, 1.000000, 1), // rp_wing_of_little_angel
		new BuySellList(7691, 10, 1.000000, 1), // rp_maidens_hairpin
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
		new BuySellList(8644, 10, 0.500000, 1), // rp_elixir_of_life_a
		new BuySellList(8650, 10, 0.500000, 1), // rp_elixir_of_mental_a
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(948, 10, 0.250000, 1), // scrl_of_ench_am_b
		new BuySellList(730, 10, 0.166000, 1) // scrl_of_ench_am_a
	};
	
	private static final int npcId = 35439;
	
	public Carey() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
	}
}