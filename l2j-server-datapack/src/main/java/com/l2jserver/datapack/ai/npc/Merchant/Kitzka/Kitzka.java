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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.Merchant.Kitzka;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForChaotic;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Kitzka extends MerchantForChaotic {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1835, 50, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 50, 0.000000, 0), // spiritshot_none
		new BuySellList(1463, 50, 0.000000, 0), // soulshot_d
		new BuySellList(1464, 50, 0.000000, 0), // soulshot_c
		new BuySellList(3947, 50, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(3948, 50, 0.000000, 0), // blessed_spiritshot_d
		new BuySellList(3949, 50, 0.000000, 0), // blessed_spiritshot_c
		new BuySellList(5146, 50, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 50, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 50, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 50, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 50, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 50, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 50, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 50, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 50, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1343, 50, 0.000000, 0), // silver_arrow
		new BuySellList(1344, 50, 0.000000, 0), // mithril_arrow
		new BuySellList(1060, 50, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1061, 50, 0.000000, 0), // healing_potion
		new BuySellList(1831, 50, 0.000000, 0), // antidote
		new BuySellList(1832, 50, 0.000000, 0), // advanced_antidote
		new BuySellList(1833, 50, 0.000000, 0), // bandage
		new BuySellList(1834, 50, 0.000000, 0), // emergency_dressing
		new BuySellList(734, 50, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 50, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 50, 0.000000, 0), // potion_of_acumen2
		new BuySellList(6037, 50, 0.000000, 0), // scroll_of_awake
		new BuySellList(736, 50, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 50, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(1829, 50, 0.000000, 0), // scroll_of_escape_to_agit
		new BuySellList(1830, 50, 0.000000, 0), // scroll_of_escape_to_castle
		new BuySellList(3031, 50, 0.000000, 0), // spirit_ore
		new BuySellList(1785, 50, 0.000000, 0), // soul_ore
		new BuySellList(5589, 50, 0.000000, 0), // energy_stone
		new BuySellList(1661, 50, 0.000000, 0), // key_of_thief
		new BuySellList(5192, 50, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 50, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 50, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 50, 0.000000, 0), // rope_of_magic_a
		new BuySellList(5196, 50, 0.000000, 0), // rope_of_magic_s
		new BuySellList(2130, 50, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 50, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 50, 0.000000, 0), // gemstone_b
		new BuySellList(4679, 50, 0.000000, 0), // bless_of_eva
		new BuySellList(9633, 50, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 50, 0.000000, 0), // fine_steel_bolt
		new BuySellList(9635, 50, 0.000000, 0), // silver_bolt
		new BuySellList(9636, 50, 0.000000, 0), // mithril_bolt
		new BuySellList(8594, 50, 0.000000, 0), // recovery_scroll_none
		new BuySellList(8595, 50, 0.000000, 0), // recovery_scroll_d
		new BuySellList(8596, 50, 0.000000, 0), // recovery_scroll_c
		new BuySellList(8597, 50, 0.000000, 0), // recovery_scroll_b
		new BuySellList(8598, 50, 0.000000, 0), // recovery_scroll_a
		new BuySellList(8599, 50, 0.000000, 0), // recovery_scroll_s
		new BuySellList(8622, 50, 0.000000, 0), // elixir_of_life_none
		new BuySellList(8623, 50, 0.000000, 0), // elixir_of_life_d
		new BuySellList(8624, 50, 0.000000, 0), // elixir_of_life_c
		new BuySellList(8634, 50, 0.000000, 0), // elixir_of_combative_none
		new BuySellList(8635, 50, 0.000000, 0), // elixir_of_combative_d
		new BuySellList(8636, 50, 0.000000, 0), // elixir_of_combative_c
		new BuySellList(8637, 50, 0.000000, 0), // elixir_of_combative_b
		new BuySellList(8638, 50, 0.000000, 0), // elixir_of_combative_a
		new BuySellList(8639, 50, 0.000000, 0), // elixir_of_combative_s
		new BuySellList(8615, 50, 0.000000, 0), // crystal_of_summon
		new BuySellList(8658, 50, 0.000000, 0), // mystery_solvent
		new BuySellList(8871, 50, 0.000000, 0), // union's_directions
		new BuySellList(10409, 50, 0.000000, 0), // blank_soul_bottle_5
		new BuySellList(4625, 50, 0.000000, 0), // dice_heart
		new BuySellList(4626, 50, 0.000000, 0), // dice_spade
		new BuySellList(4627, 50, 0.000000, 0), // dice_clover
		new BuySellList(4628, 50, 0.000000, 0), // dice_diamond
		new BuySellList(8872, 50, 0.000000, 0), // smokeless_powder
		new BuySellList(8629, 50, 0.000000, 0), // elixir_of_mental_d
		new BuySellList(21746, 50, 0.000000, 0) // g_lucky_key
	};
	
	private static final int npcId = 31045;
	
	public Kitzka() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "kitzka001.htm";
	}
}