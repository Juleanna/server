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
package com.l2jserver.datapack.ai.npc.Merchant.Sara;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Sara extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1835, 15, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 15, 0.000000, 0), // spiritshot_none
		new BuySellList(1463, 15, 0.000000, 0), // soulshot_d
		new BuySellList(1464, 15, 0.000000, 0), // soulshot_c
		new BuySellList(3947, 15, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(3948, 15, 0.000000, 0), // blessed_spiritshot_d
		new BuySellList(3949, 15, 0.000000, 0), // blessed_spiritshot_c
		new BuySellList(5146, 15, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 15, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 15, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 15, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 15, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 15, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 15, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 15, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 15, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1343, 15, 0.000000, 0), // silver_arrow
		new BuySellList(1344, 15, 0.000000, 0), // mithril_arrow
		new BuySellList(1060, 15, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1061, 15, 0.000000, 0), // healing_potion
		new BuySellList(1831, 15, 0.000000, 0), // antidote
		new BuySellList(1832, 15, 0.000000, 0), // advanced_antidote
		new BuySellList(1833, 15, 0.000000, 0), // bandage
		new BuySellList(1834, 15, 0.000000, 0), // emergency_dressing
		new BuySellList(734, 15, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 15, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 15, 0.000000, 0), // potion_of_acumen2
		new BuySellList(6037, 15, 0.000000, 0), // scroll_of_awake
		new BuySellList(736, 15, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 15, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(1829, 15, 0.000000, 0), // scroll_of_escape_to_agit
		new BuySellList(1830, 15, 0.000000, 0), // scroll_of_escape_to_castle
		new BuySellList(3031, 15, 0.000000, 0), // spirit_ore
		new BuySellList(1785, 15, 0.000000, 0), // soul_ore
		new BuySellList(5589, 15, 0.000000, 0), // energy_stone
		new BuySellList(1661, 15, 0.000000, 0), // key_of_thief
		new BuySellList(5192, 15, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 15, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 15, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 15, 0.000000, 0), // rope_of_magic_a
		new BuySellList(5196, 15, 0.000000, 0), // rope_of_magic_s
		new BuySellList(2130, 15, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 15, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 15, 0.000000, 0), // gemstone_b
		new BuySellList(4679, 15, 0.000000, 0), // bless_of_eva
		new BuySellList(9633, 15, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 15, 0.000000, 0), // fine_steel_bolt
		new BuySellList(9635, 15, 0.000000, 0), // silver_bolt
		new BuySellList(9636, 15, 0.000000, 0), // mithril_bolt
		new BuySellList(8594, 15, 0.000000, 0), // recovery_scroll_none
		new BuySellList(8595, 15, 0.000000, 0), // recovery_scroll_d
		new BuySellList(8596, 15, 0.000000, 0), // recovery_scroll_c
		new BuySellList(8597, 15, 0.000000, 0), // recovery_scroll_b
		new BuySellList(8598, 15, 0.000000, 0), // recovery_scroll_a
		new BuySellList(8599, 15, 0.000000, 0), // recovery_scroll_s
		new BuySellList(8622, 15, 0.000000, 0), // elixir_of_life_none
		new BuySellList(8623, 15, 0.000000, 0), // elixir_of_life_d
		new BuySellList(8624, 15, 0.000000, 0), // elixir_of_life_c
		new BuySellList(8634, 15, 0.000000, 0), // elixir_of_combative_none
		new BuySellList(8635, 15, 0.000000, 0), // elixir_of_combative_d
		new BuySellList(8636, 15, 0.000000, 0), // elixir_of_combative_c
		new BuySellList(8637, 15, 0.000000, 0), // elixir_of_combative_b
		new BuySellList(8638, 15, 0.000000, 0), // elixir_of_combative_a
		new BuySellList(8639, 15, 0.000000, 0), // elixir_of_combative_s
		new BuySellList(8615, 15, 0.000000, 0), // crystal_of_summon
		new BuySellList(8658, 15, 0.000000, 0), // mystery_solvent
		new BuySellList(8871, 15, 0.000000, 0), // union's_directions
		new BuySellList(10409, 15, 0.000000, 0), // blank_soul_bottle_5
		new BuySellList(4625, 15, 0.000000, 0), // dice_heart
		new BuySellList(4626, 15, 0.000000, 0), // dice_spade
		new BuySellList(4627, 15, 0.000000, 0), // dice_clover
		new BuySellList(4628, 15, 0.000000, 0), // dice_diamond
		new BuySellList(8872, 15, 0.000000, 0), // smokeless_powder
		new BuySellList(8629, 15, 0.000000, 0), // elixir_of_mental_d
		new BuySellList(21746, 15, 0.000000, 0) // g_lucky_key
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(4445, 15, 0.000000, 0), // dye_s1c3_d
		new BuySellList(4446, 15, 0.000000, 0), // dye_s1d3_d
		new BuySellList(4447, 15, 0.000000, 0), // dye_c1s3_d
		new BuySellList(4448, 15, 0.000000, 0), // dye_c1d3_d
		new BuySellList(4449, 15, 0.000000, 0), // dye_d1s3_d
		new BuySellList(4450, 15, 0.000000, 0), // dye_d1c3_d
		new BuySellList(4451, 15, 0.000000, 0), // dye_i1m3_d
		new BuySellList(4452, 15, 0.000000, 0), // dye_i1w3_d
		new BuySellList(4453, 15, 0.000000, 0), // dye_m1i3_d
		new BuySellList(4454, 15, 0.000000, 0), // dye_m1w3_d
		new BuySellList(4455, 15, 0.000000, 0), // dye_w1i3_d
		new BuySellList(4456, 15, 0.000000, 0), // dye_w1m3_d
		new BuySellList(4457, 15, 0.000000, 0), // dye_s1c2_d
		new BuySellList(4458, 15, 0.000000, 0), // dye_s1d2_d
		new BuySellList(4459, 15, 0.000000, 0), // dye_c1s2_d
		new BuySellList(4460, 15, 0.000000, 0), // dye_c1d2_d
		new BuySellList(4461, 15, 0.000000, 0), // dye_d1s2_d
		new BuySellList(4462, 15, 0.000000, 0), // dye_d1c2_d
		new BuySellList(4463, 15, 0.000000, 0), // dye_i1m2_d
		new BuySellList(4464, 15, 0.000000, 0), // dye_i1w2_d
		new BuySellList(4465, 15, 0.000000, 0), // dye_m1i2_d
		new BuySellList(4466, 15, 0.000000, 0), // dye_m1w2_d
		new BuySellList(4467, 15, 0.000000, 0), // dye_w1i2_d
		new BuySellList(4468, 15, 0.000000, 0), // dye_w1m2_d
		new BuySellList(4481, 15, 0.000000, 0), // dye_s1c3_c
		new BuySellList(4482, 15, 0.000000, 0), // dye_s1d3_c
		new BuySellList(4483, 15, 0.000000, 0), // dye_c1s3_c
		new BuySellList(4484, 15, 0.000000, 0), // dye_c1c3_c
		new BuySellList(4485, 15, 0.000000, 0), // dye_d1s3_c
		new BuySellList(4486, 15, 0.000000, 0), // dye_d1c3_c
		new BuySellList(4487, 15, 0.000000, 0), // dye_i1m3_c
		new BuySellList(4488, 15, 0.000000, 0), // dye_i1w3_c
		new BuySellList(4489, 15, 0.000000, 0), // dye_m1i3_c
		new BuySellList(4490, 15, 0.000000, 0), // dye_m1w3_c
		new BuySellList(4491, 15, 0.000000, 0), // dye_w1i3_c
		new BuySellList(4492, 15, 0.000000, 0) // dye_w1m3_c
	};
	
	private static final int npcId = 30180;
	
	public Sara() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "sara001.htm";
		super.fnYouAreChaotic = "sara006.htm";
	}
}