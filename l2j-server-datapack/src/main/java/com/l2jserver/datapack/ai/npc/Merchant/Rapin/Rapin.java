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
package com.l2jserver.datapack.ai.npc.Merchant.Rapin;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Rapin extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(908, 15, 0.000000, 0), // necklace_of_wisdom
		new BuySellList(909, 15, 0.000000, 0), // blue_diamond_necklace
		new BuySellList(910, 15, 0.000000, 0), // necklace_of_devotion
		new BuySellList(911, 15, 0.000000, 0), // enchanted_necklace
		new BuySellList(912, 15, 0.000000, 0), // near_forest_necklace
		new BuySellList(913, 15, 0.000000, 0), // elven_necklace
		new BuySellList(845, 15, 0.000000, 0), // cat'seye_earing
		new BuySellList(846, 15, 0.000000, 0), // coral_earing
		new BuySellList(847, 15, 0.000000, 0), // red_cresent_earing
		new BuySellList(848, 15, 0.000000, 0), // enchanted_earing
		new BuySellList(849, 15, 0.000000, 0), // tiger'seye_earing
		new BuySellList(850, 15, 0.000000, 0), // elven_earing
		new BuySellList(877, 15, 0.000000, 0), // ring_of_wisdom
		new BuySellList(878, 15, 0.000000, 0), // blue_coral_ring
		new BuySellList(890, 15, 0.000000, 0), // ring_of_devotion
		new BuySellList(879, 15, 0.000000, 0), // enchanted_ring
		new BuySellList(880, 15, 0.000000, 0), // black_pearl_ring
		new BuySellList(881, 15, 0.000000, 0), // elven_ring
		new BuySellList(851, 15, 0.000000, 0), // onyxbeast'seye_earing
		new BuySellList(882, 15, 0.000000, 0), // mithril_ring
		new BuySellList(914, 15, 0.000000, 0) // necklace_of_darkness
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1835, 15, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 15, 0.000000, 0), // spiritshot_none
		new BuySellList(3947, 15, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(5146, 15, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 15, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 15, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 15, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 15, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 15, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 15, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 15, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 15, 0.000000, 0), // fine_steel_arrow
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
		new BuySellList(5589, 15, 0.000000, 0), // energy_stone
		new BuySellList(1661, 15, 0.000000, 0), // key_of_thief
		new BuySellList(8515, 15, 0.000000, 0), // charm_of_courage_none
		new BuySellList(8516, 15, 0.000000, 0), // charm_of_courage_d
		new BuySellList(8517, 15, 0.000000, 0), // charm_of_courage_c
		new BuySellList(8518, 15, 0.000000, 0), // charm_of_courage_b
		new BuySellList(8519, 15, 0.000000, 0), // charm_of_courage_a
		new BuySellList(8520, 15, 0.000000, 0), // charm_of_courage_s
		new BuySellList(8873, 15, 0.000000, 0), // blood_of_phoenix
		new BuySellList(8874, 15, 0.000000, 0), // holywater_einhasad
		new BuySellList(8875, 15, 0.000000, 0), // battle_symbol
		new BuySellList(8876, 15, 0.000000, 0), // magic_symbol
		new BuySellList(9633, 15, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 15, 0.000000, 0), // fine_steel_bolt
		new BuySellList(4625, 15, 0.000000, 0), // dice_heart
		new BuySellList(4626, 15, 0.000000, 0), // dice_spade
		new BuySellList(4627, 15, 0.000000, 0), // dice_clover
		new BuySellList(4628, 15, 0.000000, 0), // dice_diamond
		new BuySellList(21746, 15, 0.000000, 0) // g_lucky_key
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12312, 15, 0.000000, 0), // necklace_of_devotion_low
		new BuySellList(12315, 15, 0.000000, 0), // enchanted_necklace_low
		new BuySellList(12317, 15, 0.000000, 0), // near_forest_necklace_low
		new BuySellList(12320, 15, 0.000000, 0), // elven_necklace_low
		new BuySellList(12311, 15, 0.000000, 0), // red_cresent_earing_low
		new BuySellList(12314, 15, 0.000000, 0), // enchanted_earing_low
		new BuySellList(12318, 15, 0.000000, 0), // tiger'seye_earing_low
		new BuySellList(12322, 15, 0.000000, 0), // elven_earing_low
		new BuySellList(12313, 15, 0.000000, 0), // ring_of_devotion_low
		new BuySellList(12316, 15, 0.000000, 0), // enchanted_ring_low
		new BuySellList(12319, 15, 0.000000, 0), // black_pearl_ring_low
		new BuySellList(12321, 15, 0.000000, 0), // elven_ring_low
		new BuySellList(12325, 15, 0.000000, 0), // onyxbeast'seye_earing_low
		new BuySellList(12323, 15, 0.000000, 0), // mithril_ring_low
		new BuySellList(12324, 15, 0.000000, 0) // necklace_of_darkness_low
	};
	
	private static final int npcId = 30165;
	
	public Rapin() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "rapin001.htm";
		super.fnYouAreChaotic = "rapin006.htm";
	}
}