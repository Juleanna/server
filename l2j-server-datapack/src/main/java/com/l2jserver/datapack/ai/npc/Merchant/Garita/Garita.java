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
package com.l2jserver.datapack.ai.npc.Merchant.Garita;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Garita extends MerchantForNewbie {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(118, 15, 0.000000, 0), // necklace_of_magic
		new BuySellList(906, 15, 0.000000, 0), // necklace_of_knowledge
		new BuySellList(907, 15, 0.000000, 0), // necklace_of_anguish
		new BuySellList(908, 15, 0.000000, 0), // necklace_of_wisdom
		new BuySellList(112, 15, 0.000000, 0), // apprentice's_earing
		new BuySellList(113, 15, 0.000000, 0), // mage_earing
		new BuySellList(114, 15, 0.000000, 0), // earing_of_strength
		new BuySellList(115, 15, 0.000000, 0), // earing_of_wisdom
		new BuySellList(845, 15, 0.000000, 0), // cat'seye_earing
		new BuySellList(116, 15, 0.000000, 0), // magic_ring
		new BuySellList(875, 15, 0.000000, 0), // ring_of_knowledge
		new BuySellList(876, 15, 0.000000, 0), // ring_of_anguish
		new BuySellList(877, 15, 0.000000, 0) // ring_of_wisdom
	};
	
	private static final int npcId = 30518;
	
	public Garita() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "trader_garita001.htm";
		super.fnYouAreChaotic = "trader_garita006.htm";
	}
}