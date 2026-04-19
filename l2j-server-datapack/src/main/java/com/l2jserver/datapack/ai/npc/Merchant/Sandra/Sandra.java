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
package com.l2jserver.datapack.ai.npc.Merchant.Sandra;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Sandra extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(908, 10, 0.000000, 0), // necklace_of_wisdom
		new BuySellList(909, 10, 0.000000, 0), // blue_diamond_necklace
		new BuySellList(910, 10, 0.000000, 0), // necklace_of_devotion
		new BuySellList(911, 10, 0.000000, 0), // enchanted_necklace
		new BuySellList(912, 10, 0.000000, 0), // near_forest_necklace
		new BuySellList(913, 10, 0.000000, 0), // elven_necklace
		new BuySellList(845, 10, 0.000000, 0), // cat'seye_earing
		new BuySellList(846, 10, 0.000000, 0), // coral_earing
		new BuySellList(847, 10, 0.000000, 0), // red_cresent_earing
		new BuySellList(848, 10, 0.000000, 0), // enchanted_earing
		new BuySellList(849, 10, 0.000000, 0), // tiger'seye_earing
		new BuySellList(850, 10, 0.000000, 0), // elven_earing
		new BuySellList(877, 10, 0.000000, 0), // ring_of_wisdom
		new BuySellList(878, 10, 0.000000, 0), // blue_coral_ring
		new BuySellList(890, 10, 0.000000, 0), // ring_of_devotion
		new BuySellList(879, 10, 0.000000, 0), // enchanted_ring
		new BuySellList(880, 10, 0.000000, 0), // black_pearl_ring
		new BuySellList(881, 10, 0.000000, 0), // elven_ring
		new BuySellList(851, 10, 0.000000, 0), // onyxbeast'seye_earing
		new BuySellList(882, 10, 0.000000, 0), // mithril_ring
		new BuySellList(914, 10, 0.000000, 0), // necklace_of_darkness
		new BuySellList(852, 10, 0.000000, 0), // moonstone_earing
		new BuySellList(883, 10, 0.000000, 0), // aquastone_ring
		new BuySellList(915, 10, 0.000000, 0), // aquastone_necklace
		new BuySellList(884, 10, 0.000000, 0), // ring_of_protection
		new BuySellList(853, 10, 0.000000, 0), // earing_of_protection
		new BuySellList(916, 10, 0.000000, 0), // necklace_of_protection
		new BuySellList(885, 10, 0.000000, 0), // ring_of_ages
		new BuySellList(854, 10, 0.000000, 0), // earing_of_binding
		new BuySellList(917, 10, 0.000000, 0), // necklace_of_mermaid
		new BuySellList(886, 10, 0.000000, 0), // ring_of_binding
		new BuySellList(855, 10, 0.000000, 0), // nassen's_earing
		new BuySellList(119, 10, 0.000000, 0) // necklace_of_binding
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12312, 10, 0.000000, 0), // necklace_of_devotion_low
		new BuySellList(12315, 10, 0.000000, 0), // enchanted_necklace_low
		new BuySellList(12317, 10, 0.000000, 0), // near_forest_necklace_low
		new BuySellList(12320, 10, 0.000000, 0), // elven_necklace_low
		new BuySellList(12311, 10, 0.000000, 0), // red_cresent_earing_low
		new BuySellList(12314, 10, 0.000000, 0), // enchanted_earing_low
		new BuySellList(12318, 10, 0.000000, 0), // tiger'seye_earing_low
		new BuySellList(12322, 10, 0.000000, 0), // elven_earing_low
		new BuySellList(12313, 10, 0.000000, 0), // ring_of_devotion_low
		new BuySellList(12316, 10, 0.000000, 0), // enchanted_ring_low
		new BuySellList(12319, 10, 0.000000, 0), // black_pearl_ring_low
		new BuySellList(12321, 10, 0.000000, 0), // elven_ring_low
		new BuySellList(12325, 10, 0.000000, 0), // onyxbeast'seye_earing_low
		new BuySellList(12323, 10, 0.000000, 0), // mithril_ring_low
		new BuySellList(12324, 10, 0.000000, 0), // necklace_of_darkness_low
		new BuySellList(12326, 10, 0.000000, 0), // moonstone_earing_low
		new BuySellList(12328, 10, 0.000000, 0), // aquastone_ring_low
		new BuySellList(12327, 10, 0.000000, 0), // aquastone_necklace_low
		new BuySellList(12331, 10, 0.000000, 0), // ring_of_protection_low
		new BuySellList(12329, 10, 0.000000, 0), // earing_of_protection_low
		new BuySellList(12330, 10, 0.000000, 0), // necklace_of_protection_low
		new BuySellList(12333, 10, 0.000000, 0), // ring_of_ages_low
		new BuySellList(12332, 10, 0.000000, 0), // earing_of_binding_low
		new BuySellList(12334, 10, 0.000000, 0), // necklace_of_mermaid_low
		new BuySellList(12337, 10, 0.000000, 0), // ring_of_binding_low
		new BuySellList(12335, 10, 0.000000, 0), // nassen's_earing_low
		new BuySellList(12336, 10, 0.000000, 0) // necklace_of_binding_low
	};
	
	private static final int npcId = 30090;
	
	public Sandra() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "sandra001.htm";
		super.fnYouAreChaotic = "sandra006.htm";
	}
}