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
package com.l2jserver.datapack.ai.npc.Merchant.Poesia;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Poesia extends MerchantForNewbie {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1835, 20, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 20, 0.000000, 0), // spiritshot_none
		new BuySellList(1463, 20, 0.000000, 0), // soulshot_d
		new BuySellList(1464, 20, 0.000000, 0), // soulshot_c
		new BuySellList(3947, 20, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(3948, 20, 0.000000, 0), // blessed_spiritshot_d
		new BuySellList(3949, 20, 0.000000, 0), // blessed_spiritshot_c
		new BuySellList(5146, 20, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 20, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 20, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 20, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 20, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 20, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 20, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 20, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 20, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1343, 20, 0.000000, 0), // silver_arrow
		new BuySellList(1344, 20, 0.000000, 0), // mithril_arrow
		new BuySellList(1060, 20, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1061, 20, 0.000000, 0), // healing_potion
		new BuySellList(1831, 20, 0.000000, 0), // antidote
		new BuySellList(1832, 20, 0.000000, 0), // advanced_antidote
		new BuySellList(1833, 20, 0.000000, 0), // bandage
		new BuySellList(1834, 20, 0.000000, 0), // emergency_dressing
		new BuySellList(734, 20, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 20, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 20, 0.000000, 0), // potion_of_acumen2
		new BuySellList(6037, 20, 0.000000, 0), // scroll_of_awake
		new BuySellList(736, 20, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 20, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(1829, 20, 0.000000, 0), // scroll_of_escape_to_agit
		new BuySellList(1830, 20, 0.000000, 0), // scroll_of_escape_to_castle
		new BuySellList(3031, 20, 0.000000, 0), // spirit_ore
		new BuySellList(1785, 20, 0.000000, 0), // soul_ore
		new BuySellList(5589, 20, 0.000000, 0), // energy_stone
		new BuySellList(1661, 20, 0.000000, 0), // key_of_thief
		new BuySellList(5192, 20, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 20, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 20, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 20, 0.000000, 0), // rope_of_magic_a
		new BuySellList(5196, 20, 0.000000, 0), // rope_of_magic_s
		new BuySellList(2130, 20, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 20, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 20, 0.000000, 0), // gemstone_b
		new BuySellList(4679, 20, 0.000000, 0), // bless_of_eva
		new BuySellList(9633, 20, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 20, 0.000000, 0), // fine_steel_bolt
		new BuySellList(9635, 20, 0.000000, 0), // silver_bolt
		new BuySellList(9636, 20, 0.000000, 0), // mithril_bolt
		new BuySellList(8594, 20, 0.000000, 0), // recovery_scroll_none
		new BuySellList(8595, 20, 0.000000, 0), // recovery_scroll_d
		new BuySellList(8596, 20, 0.000000, 0), // recovery_scroll_c
		new BuySellList(8597, 20, 0.000000, 0), // recovery_scroll_b
		new BuySellList(8598, 20, 0.000000, 0), // recovery_scroll_a
		new BuySellList(8599, 20, 0.000000, 0), // recovery_scroll_s
		new BuySellList(8622, 20, 0.000000, 0), // elixir_of_life_none
		new BuySellList(8623, 20, 0.000000, 0), // elixir_of_life_d
		new BuySellList(8624, 20, 0.000000, 0), // elixir_of_life_c
		new BuySellList(8634, 20, 0.000000, 0), // elixir_of_combative_none
		new BuySellList(8635, 20, 0.000000, 0), // elixir_of_combative_d
		new BuySellList(8636, 20, 0.000000, 0), // elixir_of_combative_c
		new BuySellList(8637, 20, 0.000000, 0), // elixir_of_combative_b
		new BuySellList(8638, 20, 0.000000, 0), // elixir_of_combative_a
		new BuySellList(8639, 20, 0.000000, 0), // elixir_of_combative_s
		new BuySellList(8615, 20, 0.000000, 0), // crystal_of_summon
		new BuySellList(8658, 20, 0.000000, 0), // mystery_solvent
		new BuySellList(8871, 20, 0.000000, 0), // union's_directions
		new BuySellList(10409, 20, 0.000000, 0), // blank_soul_bottle_5
		new BuySellList(4625, 20, 0.000000, 0), // dice_heart
		new BuySellList(4626, 20, 0.000000, 0), // dice_spade
		new BuySellList(4627, 20, 0.000000, 0), // dice_clover
		new BuySellList(4628, 20, 0.000000, 0), // dice_diamond
		new BuySellList(8872, 20, 0.000000, 0), // smokeless_powder
		new BuySellList(8629, 20, 0.000000, 0), // elixir_of_mental_d
		new BuySellList(21746, 20, 0.000000, 0) // g_lucky_key
	};
	
	private static final int npcId = 30315;
	
	public Poesia() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "trader_poesia001.htm";
		super.fnYouAreChaotic = "trader_poesia006.htm";
	}
}