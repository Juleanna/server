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
package com.l2jserver.datapack.ai.npc.Merchant.FarmFeedSeller;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForChaotic;

/**
* @author Charus
* @version 2.6.3.0
*/
public class FarmFeedSeller extends MerchantForChaotic {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(15474, 50, 0.000000, 0), // golden_spice_new
		new BuySellList(15475, 50, 0.000000, 0), // crystal_spice_new
		new BuySellList(15482, 50, 0.000000, 0), // golden_spice_comp
		new BuySellList(15483, 50, 0.000000, 0), // crystal_spice_comp
		new BuySellList(736, 50, 0.000000, 0), // scroll_of_escape
		new BuySellList(8599, 50, 0.000000, 0) // recovery_scroll_s
	};
	
	private static final int npcId = 31366;
	
	public FarmFeedSeller() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "farm_feed_seller001.htm";
		super.fnYouAreChaotic = "farm_feed_seller006.htm";
	}
}