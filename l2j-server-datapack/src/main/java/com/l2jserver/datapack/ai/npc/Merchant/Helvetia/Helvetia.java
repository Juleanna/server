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
package com.l2jserver.datapack.ai.npc.Merchant.Helvetia;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Helvetia extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1835, 10, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 10, 0.000000, 0), // spiritshot_none
		new BuySellList(1463, 10, 0.000000, 0), // soulshot_d
		new BuySellList(1464, 10, 0.000000, 0), // soulshot_c
		new BuySellList(3947, 10, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(3948, 10, 0.000000, 0), // blessed_spiritshot_d
		new BuySellList(3949, 10, 0.000000, 0), // blessed_spiritshot_c
		new BuySellList(5146, 10, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 10, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 10, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 10, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 10, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 10, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 10, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 10, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 10, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1343, 10, 0.000000, 0), // silver_arrow
		new BuySellList(1344, 10, 0.000000, 0), // mithril_arrow
		new BuySellList(1060, 10, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1061, 10, 0.000000, 0), // healing_potion
		new BuySellList(1831, 10, 0.000000, 0), // antidote
		new BuySellList(1832, 10, 0.000000, 0), // advanced_antidote
		new BuySellList(1833, 10, 0.000000, 0), // bandage
		new BuySellList(1834, 10, 0.000000, 0), // emergency_dressing
		new BuySellList(734, 10, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 10, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 10, 0.000000, 0), // potion_of_acumen2
		new BuySellList(6037, 10, 0.000000, 0), // scroll_of_awake
		new BuySellList(736, 10, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 10, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(1829, 10, 0.000000, 0), // scroll_of_escape_to_agit
		new BuySellList(1830, 10, 0.000000, 0), // scroll_of_escape_to_castle
		new BuySellList(3031, 10, 0.000000, 0), // spirit_ore
		new BuySellList(1785, 10, 0.000000, 0), // soul_ore
		new BuySellList(5589, 10, 0.000000, 0), // energy_stone
		new BuySellList(1661, 10, 0.000000, 0), // key_of_thief
		new BuySellList(5192, 10, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 10, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 10, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 10, 0.000000, 0), // rope_of_magic_a
		new BuySellList(5196, 10, 0.000000, 0), // rope_of_magic_s
		new BuySellList(2130, 10, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 10, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 10, 0.000000, 0), // gemstone_b
		new BuySellList(4679, 10, 0.000000, 0), // bless_of_eva
		new BuySellList(9633, 10, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 10, 0.000000, 0), // fine_steel_bolt
		new BuySellList(9635, 10, 0.000000, 0), // silver_bolt
		new BuySellList(9636, 10, 0.000000, 0), // mithril_bolt
		new BuySellList(8594, 10, 0.000000, 0), // recovery_scroll_none
		new BuySellList(8595, 10, 0.000000, 0), // recovery_scroll_d
		new BuySellList(8596, 10, 0.000000, 0), // recovery_scroll_c
		new BuySellList(8597, 10, 0.000000, 0), // recovery_scroll_b
		new BuySellList(8598, 10, 0.000000, 0), // recovery_scroll_a
		new BuySellList(8599, 10, 0.000000, 0), // recovery_scroll_s
		new BuySellList(8622, 10, 0.000000, 0), // elixir_of_life_none
		new BuySellList(8623, 10, 0.000000, 0), // elixir_of_life_d
		new BuySellList(8624, 10, 0.000000, 0), // elixir_of_life_c
		new BuySellList(8634, 10, 0.000000, 0), // elixir_of_combative_none
		new BuySellList(8635, 10, 0.000000, 0), // elixir_of_combative_d
		new BuySellList(8636, 10, 0.000000, 0), // elixir_of_combative_c
		new BuySellList(8637, 10, 0.000000, 0), // elixir_of_combative_b
		new BuySellList(8638, 10, 0.000000, 0), // elixir_of_combative_a
		new BuySellList(8639, 10, 0.000000, 0), // elixir_of_combative_s
		new BuySellList(8615, 10, 0.000000, 0), // crystal_of_summon
		new BuySellList(8658, 10, 0.000000, 0), // mystery_solvent
		new BuySellList(8871, 10, 0.000000, 0), // union's_directions
		new BuySellList(10409, 10, 0.000000, 0), // blank_soul_bottle_5
		new BuySellList(4625, 10, 0.000000, 0), // dice_heart
		new BuySellList(4626, 10, 0.000000, 0), // dice_spade
		new BuySellList(4627, 10, 0.000000, 0), // dice_clover
		new BuySellList(4628, 10, 0.000000, 0), // dice_diamond
		new BuySellList(8872, 10, 0.000000, 0), // smokeless_powder
		new BuySellList(8629, 10, 0.000000, 0), // elixir_of_mental_d
		new BuySellList(21746, 10, 0.000000, 0) // g_lucky_key
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(4445, 10, 0.000000, 0), // dye_s1c3_d
		new BuySellList(4446, 10, 0.000000, 0), // dye_s1d3_d
		new BuySellList(4447, 10, 0.000000, 0), // dye_c1s3_d
		new BuySellList(4448, 10, 0.000000, 0), // dye_c1d3_d
		new BuySellList(4449, 10, 0.000000, 0), // dye_d1s3_d
		new BuySellList(4450, 10, 0.000000, 0), // dye_d1c3_d
		new BuySellList(4451, 10, 0.000000, 0), // dye_i1m3_d
		new BuySellList(4452, 10, 0.000000, 0), // dye_i1w3_d
		new BuySellList(4453, 10, 0.000000, 0), // dye_m1i3_d
		new BuySellList(4454, 10, 0.000000, 0), // dye_m1w3_d
		new BuySellList(4455, 10, 0.000000, 0), // dye_w1i3_d
		new BuySellList(4456, 10, 0.000000, 0), // dye_w1m3_d
		new BuySellList(4457, 10, 0.000000, 0), // dye_s1c2_d
		new BuySellList(4458, 10, 0.000000, 0), // dye_s1d2_d
		new BuySellList(4459, 10, 0.000000, 0), // dye_c1s2_d
		new BuySellList(4460, 10, 0.000000, 0), // dye_c1d2_d
		new BuySellList(4461, 10, 0.000000, 0), // dye_d1s2_d
		new BuySellList(4462, 10, 0.000000, 0), // dye_d1c2_d
		new BuySellList(4463, 10, 0.000000, 0), // dye_i1m2_d
		new BuySellList(4464, 10, 0.000000, 0), // dye_i1w2_d
		new BuySellList(4465, 10, 0.000000, 0), // dye_m1i2_d
		new BuySellList(4466, 10, 0.000000, 0), // dye_m1w2_d
		new BuySellList(4467, 10, 0.000000, 0), // dye_w1i2_d
		new BuySellList(4468, 10, 0.000000, 0), // dye_w1m2_d
		new BuySellList(4481, 10, 0.000000, 0), // dye_s1c3_c
		new BuySellList(4482, 10, 0.000000, 0), // dye_s1d3_c
		new BuySellList(4483, 10, 0.000000, 0), // dye_c1s3_c
		new BuySellList(4484, 10, 0.000000, 0), // dye_c1c3_c
		new BuySellList(4485, 10, 0.000000, 0), // dye_d1s3_c
		new BuySellList(4486, 10, 0.000000, 0), // dye_d1c3_c
		new BuySellList(4487, 10, 0.000000, 0), // dye_i1m3_c
		new BuySellList(4488, 10, 0.000000, 0), // dye_i1w3_c
		new BuySellList(4489, 10, 0.000000, 0), // dye_m1i3_c
		new BuySellList(4490, 10, 0.000000, 0), // dye_m1w3_c
		new BuySellList(4491, 10, 0.000000, 0), // dye_w1i3_c
		new BuySellList(4492, 10, 0.000000, 0), // dye_w1m3_c
		new BuySellList(4493, 10, 0.000000, 0), // dye_s1c2_c
		new BuySellList(4494, 10, 0.000000, 0), // dye_s1d2_c
		new BuySellList(4495, 10, 0.000000, 0), // dye_c1s2_c
		new BuySellList(4496, 10, 0.000000, 0), // dye_c1c2_c
		new BuySellList(4497, 10, 0.000000, 0), // dye_d1s2_c
		new BuySellList(4498, 10, 0.000000, 0), // dye_d1c2_c
		new BuySellList(4499, 10, 0.000000, 0), // dye_i1m2_c
		new BuySellList(4500, 10, 0.000000, 0), // dye_i1w2_c
		new BuySellList(4501, 10, 0.000000, 0), // dye_m1i2_c
		new BuySellList(4502, 10, 0.000000, 0), // dye_m1w2_c
		new BuySellList(4503, 10, 0.000000, 0), // dye_w1i2_c
		new BuySellList(4504, 10, 0.000000, 0), // dye_w1m2_c
		new BuySellList(4505, 10, 0.000000, 0), // dye_s2c4_c
		new BuySellList(4506, 10, 0.000000, 0), // dye_s2d4_c
		new BuySellList(4507, 10, 0.000000, 0), // dye_c2s4_c
		new BuySellList(4508, 10, 0.000000, 0), // dye_c2c4_c
		new BuySellList(4509, 10, 0.000000, 0), // dye_d2s4_c
		new BuySellList(4510, 10, 0.000000, 0), // dye_d2c4_c
		new BuySellList(4511, 10, 0.000000, 0), // dye_i2m4_c
		new BuySellList(4512, 10, 0.000000, 0), // dye_i2w4_c
		new BuySellList(4513, 10, 0.000000, 0), // dye_m2i4_c
		new BuySellList(4514, 10, 0.000000, 0), // dye_m2w4_c
		new BuySellList(4515, 10, 0.000000, 0), // dye_w2i4_c
		new BuySellList(4516, 10, 0.000000, 0), // dye_w2m4_c
		new BuySellList(4517, 10, 0.000000, 0), // dye_s2c3_c
		new BuySellList(4518, 10, 0.000000, 0), // dye_s2d3_c
		new BuySellList(4519, 10, 0.000000, 0), // dye_c2s3_c
		new BuySellList(4520, 10, 0.000000, 0), // dye_c2c3_c
		new BuySellList(4521, 10, 0.000000, 0), // dye_d2s3_c
		new BuySellList(4522, 10, 0.000000, 0), // dye_d2c3_c
		new BuySellList(4523, 10, 0.000000, 0), // dye_i2m3_c
		new BuySellList(4524, 10, 0.000000, 0), // dye_i2w3_c
		new BuySellList(4525, 10, 0.000000, 0), // dye_m2i3_c
		new BuySellList(4526, 10, 0.000000, 0), // dye_m2w3_c
		new BuySellList(4527, 10, 0.000000, 0), // dye_w2i3_c
		new BuySellList(4528, 10, 0.000000, 0), // dye_w2m3_c
		new BuySellList(4529, 10, 0.000000, 0), // dye_s3c5_c
		new BuySellList(4530, 10, 0.000000, 0), // dye_s3d5_c
		new BuySellList(4531, 10, 0.000000, 0), // dye_c3s5_c
		new BuySellList(4532, 10, 0.000000, 0), // dye_c3c5_c
		new BuySellList(4533, 10, 0.000000, 0), // dye_d3s5_c
		new BuySellList(4534, 10, 0.000000, 0), // dye_d3c5_c
		new BuySellList(4535, 10, 0.000000, 0), // dye_i3m5_c
		new BuySellList(4536, 10, 0.000000, 0), // dye_i3w5_c
		new BuySellList(4537, 10, 0.000000, 0), // dye_m3i5_c
		new BuySellList(4538, 10, 0.000000, 0), // dye_m3w5_c
		new BuySellList(4539, 10, 0.000000, 0), // dye_w3i5_c
		new BuySellList(4540, 10, 0.000000, 0), // dye_w3m5_c
		new BuySellList(4541, 10, 0.000000, 0), // dye_s3c4_c
		new BuySellList(4542, 10, 0.000000, 0), // dye_s3d4_c
		new BuySellList(4543, 10, 0.000000, 0), // dye_c3s4_c
		new BuySellList(4544, 10, 0.000000, 0), // dye_c3c4_c
		new BuySellList(4545, 10, 0.000000, 0), // dye_d3s4_c
		new BuySellList(4546, 10, 0.000000, 0), // dye_d3c4_c
		new BuySellList(4547, 10, 0.000000, 0), // dye_i3m4_c
		new BuySellList(4548, 10, 0.000000, 0), // dye_i3w4_c
		new BuySellList(4549, 10, 0.000000, 0), // dye_m3i4_c
		new BuySellList(4550, 10, 0.000000, 0), // dye_m3w4_c
		new BuySellList(4551, 10, 0.000000, 0), // dye_w3i4_c
		new BuySellList(4552, 10, 0.000000, 0), // dye_w3m4_c
		new BuySellList(4565, 10, 0.000000, 0), // dye_s4c6_c
		new BuySellList(4566, 10, 0.000000, 0), // dye_s4d6_c
		new BuySellList(4567, 10, 0.000000, 0), // dye_c4s6_c
		new BuySellList(4568, 10, 0.000000, 0), // dye_c4c6_c
		new BuySellList(4569, 10, 0.000000, 0), // dye_d4s6_c
		new BuySellList(4570, 10, 0.000000, 0), // dye_d4c6_c
		new BuySellList(4571, 10, 0.000000, 0), // dye_i4m6_c
		new BuySellList(4572, 10, 0.000000, 0), // dye_i4w6_c
		new BuySellList(4573, 10, 0.000000, 0), // dye_m4i6_c
		new BuySellList(4574, 10, 0.000000, 0), // dye_m4w6_c
		new BuySellList(4575, 10, 0.000000, 0), // dye_w4i6_c
		new BuySellList(4576, 10, 0.000000, 0), // dye_w4m6_c
		new BuySellList(4577, 10, 0.000000, 0), // dye_s4c5_c
		new BuySellList(4578, 10, 0.000000, 0), // dye_s4d5_c
		new BuySellList(4579, 10, 0.000000, 0), // dye_c4s5_c
		new BuySellList(4580, 10, 0.000000, 0), // dye_c4c5_c
		new BuySellList(4581, 10, 0.000000, 0), // dye_d4s5_c
		new BuySellList(4582, 10, 0.000000, 0), // dye_d4c5_c
		new BuySellList(4583, 10, 0.000000, 0), // dye_i4m5_c
		new BuySellList(4584, 10, 0.000000, 0), // dye_i4w5_c
		new BuySellList(4585, 10, 0.000000, 0), // dye_m4i5_c
		new BuySellList(4586, 10, 0.000000, 0), // dye_m4w5_c
		new BuySellList(4587, 10, 0.000000, 0), // dye_w4i5_c
		new BuySellList(4588, 10, 0.000000, 0) // dye_w4m5_c
	};
	
	private static final int npcId = 30081;
	
	public Helvetia() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "helvetia001.htm";
		super.fnYouAreChaotic = "helvetia006.htm";
	}
}