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
package com.l2jserver.datapack.ai.npc.Merchant.Ariel;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Ariel extends MerchantForNewbie {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(28, 15, 0.000000, 0), // pants
		new BuySellList(21, 15, 0.000000, 0), // shirt
		new BuySellList(29, 15, 0.000000, 0), // leather_pants
		new BuySellList(22, 15, 0.000000, 0), // leather_shirt
		new BuySellList(30, 15, 0.000000, 0), // hard_leather_pants
		new BuySellList(2386, 15, 0.000000, 0), // wooden_gaiters
		new BuySellList(23, 15, 0.000000, 0), // wooden_breastplate
		new BuySellList(412, 15, 0.000000, 0), // tights_pants
		new BuySellList(390, 15, 0.000000, 0), // tights_shirt
		new BuySellList(24, 15, 0.000000, 0), // bone_breastplate
		new BuySellList(31, 15, 0.000000, 0), // bone_gaiters
		new BuySellList(18, 15, 0.000000, 0), // leather_shield
		new BuySellList(19, 15, 0.000000, 0), // small_shield
		new BuySellList(20, 15, 0.000000, 0), // buckler
		new BuySellList(102, 15, 0.000000, 0), // round_shield
		new BuySellList(48, 15, 0.000000, 0), // short_gloves
		new BuySellList(1119, 15, 0.000000, 0), // short_leather_gloves
		new BuySellList(49, 15, 0.000000, 0), // gloves
		new BuySellList(50, 15, 0.000000, 0), // leather_gloves
		new BuySellList(1121, 15, 0.000000, 0), // apprentice's_shoes
		new BuySellList(35, 15, 0.000000, 0), // cloth_shoes
		new BuySellList(36, 15, 0.000000, 0), // leather_sandals
		new BuySellList(1129, 15, 0.000000, 0), // crude_leather_shoes
		new BuySellList(1122, 15, 0.000000, 0), // cotton_shoes
		new BuySellList(37, 15, 0.000000, 0), // leather_shoes
		new BuySellList(38, 15, 0.000000, 0), // low_boots
		new BuySellList(41, 15, 0.000000, 0), // cloth_cap
		new BuySellList(42, 15, 0.000000, 0), // leather_cap
		new BuySellList(43, 15, 0.000000, 0), // wooden_helmet
		new BuySellList(44, 15, 0.000000, 0), // leather_helmet
		new BuySellList(9577, 15, 0.000000, 0), // tshirt
		new BuySellList(9583, 15, 0.000000, 0) // pattern_tshirt
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(462, 15, 0.000000, 0), // hose
		new BuySellList(426, 15, 0.000000, 0), // tunic
		new BuySellList(1103, 15, 0.000000, 0), // cotton_hose
		new BuySellList(1100, 15, 0.000000, 0), // cotton_tunic
		new BuySellList(463, 15, 0.000000, 0), // feriotic_hose
		new BuySellList(428, 15, 0.000000, 0), // feriotic_tunic
		new BuySellList(464, 15, 0.000000, 0), // leather_hose
		new BuySellList(429, 15, 0.000000, 0), // leather_tunic
		new BuySellList(1104, 15, 0.000000, 0), // hose_of_devotion
		new BuySellList(1101, 15, 0.000000, 0), // tunic_of_devotion
		new BuySellList(18, 15, 0.000000, 0), // leather_shield
		new BuySellList(19, 15, 0.000000, 0), // small_shield
		new BuySellList(20, 15, 0.000000, 0), // buckler
		new BuySellList(102, 15, 0.000000, 0), // round_shield
		new BuySellList(48, 15, 0.000000, 0), // short_gloves
		new BuySellList(1119, 15, 0.000000, 0), // short_leather_gloves
		new BuySellList(49, 15, 0.000000, 0), // gloves
		new BuySellList(50, 15, 0.000000, 0), // leather_gloves
		new BuySellList(1121, 15, 0.000000, 0), // apprentice's_shoes
		new BuySellList(35, 15, 0.000000, 0), // cloth_shoes
		new BuySellList(36, 15, 0.000000, 0), // leather_sandals
		new BuySellList(1129, 15, 0.000000, 0), // crude_leather_shoes
		new BuySellList(1122, 15, 0.000000, 0), // cotton_shoes
		new BuySellList(37, 15, 0.000000, 0), // leather_shoes
		new BuySellList(38, 15, 0.000000, 0), // low_boots
		new BuySellList(41, 15, 0.000000, 0), // cloth_cap
		new BuySellList(42, 15, 0.000000, 0), // leather_cap
		new BuySellList(43, 15, 0.000000, 0), // wooden_helmet
		new BuySellList(44, 15, 0.000000, 0), // leather_helmet
		new BuySellList(9577, 15, 0.000000, 0), // tshirt
		new BuySellList(9583, 15, 0.000000, 0) // pattern_tshirt
	};
	
	private static final int npcId = 30148;
	
	public Ariel() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "ariel001.htm";
		super.fnYouAreChaotic = "ariel006.htm";
	}
}