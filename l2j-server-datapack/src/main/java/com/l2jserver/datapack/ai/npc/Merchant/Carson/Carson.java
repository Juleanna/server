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
package com.l2jserver.datapack.ai.npc.Merchant.Carson;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Carson extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(908, 20, 0.000000, 0), // necklace_of_wisdom
		new BuySellList(909, 20, 0.000000, 0), // blue_diamond_necklace
		new BuySellList(910, 20, 0.000000, 0), // necklace_of_devotion
		new BuySellList(845, 20, 0.000000, 0), // cat'seye_earing
		new BuySellList(846, 20, 0.000000, 0), // coral_earing
		new BuySellList(847, 20, 0.000000, 0), // red_cresent_earing
		new BuySellList(877, 20, 0.000000, 0), // ring_of_wisdom
		new BuySellList(878, 20, 0.000000, 0), // blue_coral_ring
		new BuySellList(890, 20, 0.000000, 0), // ring_of_devotion
		new BuySellList(852, 20, 0.000000, 0), // moonstone_earing
		new BuySellList(883, 20, 0.000000, 0), // aquastone_ring
		new BuySellList(915, 20, 0.000000, 0), // aquastone_necklace
		new BuySellList(884, 20, 0.000000, 0), // ring_of_protection
		new BuySellList(853, 20, 0.000000, 0), // earing_of_protection
		new BuySellList(916, 20, 0.000000, 0), // necklace_of_protection
		new BuySellList(885, 20, 0.000000, 0), // ring_of_ages
		new BuySellList(854, 20, 0.000000, 0), // earing_of_binding
		new BuySellList(917, 20, 0.000000, 0), // necklace_of_mermaid
		new BuySellList(886, 20, 0.000000, 0), // ring_of_binding
		new BuySellList(855, 20, 0.000000, 0), // nassen's_earing
		new BuySellList(119, 20, 0.000000, 0) // necklace_of_binding
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(4445, 20, 0.000000, 0), // dye_s1c3_d
		new BuySellList(4446, 20, 0.000000, 0), // dye_s1d3_d
		new BuySellList(4447, 20, 0.000000, 0), // dye_c1s3_d
		new BuySellList(4448, 20, 0.000000, 0), // dye_c1d3_d
		new BuySellList(4449, 20, 0.000000, 0), // dye_d1s3_d
		new BuySellList(4450, 20, 0.000000, 0), // dye_d1c3_d
		new BuySellList(4451, 20, 0.000000, 0), // dye_i1m3_d
		new BuySellList(4452, 20, 0.000000, 0), // dye_i1w3_d
		new BuySellList(4453, 20, 0.000000, 0), // dye_m1i3_d
		new BuySellList(4454, 20, 0.000000, 0), // dye_m1w3_d
		new BuySellList(4455, 20, 0.000000, 0), // dye_w1i3_d
		new BuySellList(4456, 20, 0.000000, 0), // dye_w1m3_d
		new BuySellList(4457, 20, 0.000000, 0), // dye_s1c2_d
		new BuySellList(4458, 20, 0.000000, 0), // dye_s1d2_d
		new BuySellList(4459, 20, 0.000000, 0), // dye_c1s2_d
		new BuySellList(4460, 20, 0.000000, 0), // dye_c1d2_d
		new BuySellList(4461, 20, 0.000000, 0), // dye_d1s2_d
		new BuySellList(4462, 20, 0.000000, 0), // dye_d1c2_d
		new BuySellList(4463, 20, 0.000000, 0), // dye_i1m2_d
		new BuySellList(4464, 20, 0.000000, 0), // dye_i1w2_d
		new BuySellList(4465, 20, 0.000000, 0), // dye_m1i2_d
		new BuySellList(4466, 20, 0.000000, 0), // dye_m1w2_d
		new BuySellList(4467, 20, 0.000000, 0), // dye_w1i2_d
		new BuySellList(4468, 20, 0.000000, 0), // dye_w1m2_d
		new BuySellList(4481, 20, 0.000000, 0), // dye_s1c3_c
		new BuySellList(4482, 20, 0.000000, 0), // dye_s1d3_c
		new BuySellList(4483, 20, 0.000000, 0), // dye_c1s3_c
		new BuySellList(4484, 20, 0.000000, 0), // dye_c1c3_c
		new BuySellList(4485, 20, 0.000000, 0), // dye_d1s3_c
		new BuySellList(4486, 20, 0.000000, 0), // dye_d1c3_c
		new BuySellList(4487, 20, 0.000000, 0), // dye_i1m3_c
		new BuySellList(4488, 20, 0.000000, 0), // dye_i1w3_c
		new BuySellList(4489, 20, 0.000000, 0), // dye_m1i3_c
		new BuySellList(4490, 20, 0.000000, 0), // dye_m1w3_c
		new BuySellList(4491, 20, 0.000000, 0), // dye_w1i3_c
		new BuySellList(4492, 20, 0.000000, 0) // dye_w1m3_c
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12312, 20, 0.000000, 0), // necklace_of_devotion_low
		new BuySellList(12311, 20, 0.000000, 0), // red_cresent_earing_low
		new BuySellList(12313, 20, 0.000000, 0), // ring_of_devotion_low
		new BuySellList(12326, 20, 0.000000, 0), // moonstone_earing_low
		new BuySellList(12328, 20, 0.000000, 0), // aquastone_ring_low
		new BuySellList(12327, 20, 0.000000, 0), // aquastone_necklace_low
		new BuySellList(12331, 20, 0.000000, 0), // ring_of_protection_low
		new BuySellList(12329, 20, 0.000000, 0), // earing_of_protection_low
		new BuySellList(12330, 20, 0.000000, 0), // necklace_of_protection_low
		new BuySellList(12333, 20, 0.000000, 0), // ring_of_ages_low
		new BuySellList(12332, 20, 0.000000, 0), // earing_of_binding_low
		new BuySellList(12334, 20, 0.000000, 0), // necklace_of_mermaid_low
		new BuySellList(12337, 20, 0.000000, 0), // ring_of_binding_low
		new BuySellList(12335, 20, 0.000000, 0), // nassen's_earing_low
		new BuySellList(12336, 20, 0.000000, 0) // necklace_of_binding_low
	};
	
	private static final int npcId = 30841;
	
	public Carson() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "trader_carson001.htm";
		super.fnYouAreChaotic = "trader_carson006.htm";
	}
}