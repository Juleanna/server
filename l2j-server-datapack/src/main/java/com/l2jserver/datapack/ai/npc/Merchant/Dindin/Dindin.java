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
package com.l2jserver.datapack.ai.npc.Merchant.Dindin;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForChaotic;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Dindin extends MerchantForChaotic {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(736, 50, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 50, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(734, 50, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 50, 0.000000, 0), // swift_attack_potion
		new BuySellList(1831, 50, 0.000000, 0), // antidote
		new BuySellList(1833, 50, 0.000000, 0), // bandage
		new BuySellList(1061, 50, 0.000000, 0), // healing_potion
		new BuySellList(8786, 50, 0.000000, 0), // potion_of_genesis
		new BuySellList(8598, 50, 0.000000, 0), // recovery_scroll_a
		new BuySellList(8599, 50, 0.000000, 0), // recovery_scroll_s
		new BuySellList(8615, 50, 0.000000, 0) // crystal_of_summon
	};
	
	private static final int npcId = 32105;
	
	public Dindin() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "dindin001.htm";
		super.fnYouAreChaotic = "dindin006.htm";
	}
}