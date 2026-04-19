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
package com.l2jserver.datapack.ai.npc.Custodian.Aida;

import com.l2jserver.datapack.ai.npc.Custodian.Custodian;
import com.l2jserver.gameserver.model.Location;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Aida extends Custodian {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010024, new Location(147728, -56331, -2776), 0),
		new TelPosList(1010025, new Location(147731, -58930, -2976), 0),
		new TelPosList(1010026, new Location(150561, -57489, -2976), 0),
		new TelPosList(1010027, new Location(144866, -57464, -2976), 0)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010024, new Location(147728, -56331, -2776), 0),
		new TelPosList(1010025, new Location(147731, -58930, -2976), 0),
		new TelPosList(1010026, new Location(150561, -57489, -2976), 0),
		new TelPosList(1010027, new Location(144866, -57464, -2976), 0),
		new TelPosList(1010491, new Location(125740, -40864, -3736), 500),
		new TelPosList(1010492, new Location(146990, -67128, -3640), 500),
		new TelPosList(1010530, new Location(169018, -116303, -2432), 500),
		new TelPosList(1010568, new Location(165054, -47861, -3560), 500),
		new TelPosList(1010609, new Location(144880, -113468, -2560), 500)
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
		new BuySellList(7129, 10, 1.000000, 1), // escape_scroll_oren
		new BuySellList(7126, 10, 1.000000, 1), // escape_scroll_giran
		new BuySellList(8643, 10, 0.500000, 1), // rp_elixir_of_life_b
		new BuySellList(8649, 10, 0.500000, 1) // rp_elixir_of_mental_b
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6902, 10, 0.000000, 0), // pledge_shield_agit
		new BuySellList(1829, 10, 1.000000, 5), // scroll_of_escape_to_agit
		new BuySellList(5858, 10, 1.000000, 3), // blessed_scroll_of_escape_to_agit
		new BuySellList(5169, 10, 1.000000, 10), // deluxe_food_for_strider
		new BuySellList(7129, 10, 1.000000, 1), // escape_scroll_oren
		new BuySellList(7126, 10, 1.000000, 1), // escape_scroll_giran
		new BuySellList(7584, 10, 0.500000, 1), // ticket_cougar_chime
		new BuySellList(6928, 10, 1.000000, 1), // rp_greater_healing_potion
		new BuySellList(6935, 10, 1.000000, 1), // rp_adv_quick_step_potion
		new BuySellList(6936, 10, 1.000000, 1), // rp_adv_swift_attack_potion
		new BuySellList(6938, 10, 1.000000, 1), // rp_potion_of_acumen3
		new BuySellList(6921, 10, 1.000000, 1), // rp_fish_oil_high
		new BuySellList(8372, 10, 1.000000, 1), // rp_feeler_of_fairy
		new BuySellList(7700, 10, 1.000000, 1), // rp_daisy_hairpin
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
		new BuySellList(8643, 10, 0.500000, 1), // rp_elixir_of_life_b
		new BuySellList(8649, 10, 0.500000, 1), // rp_elixir_of_mental_b
		new BuySellList(9898, 10, 0.125000, 1), // scroll_of_high_sp
		new BuySellList(952, 10, 0.500000, 1), // scrl_of_ench_am_c
		new BuySellList(948, 10, 0.250000, 1) // scrl_of_ench_am_b
	};
	
	private static final int npcId = 35461;
	
	public Aida() {
		super(npcId);
		
		super.position1 = _position1;
		super.position2 = _position2;
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
	}
}