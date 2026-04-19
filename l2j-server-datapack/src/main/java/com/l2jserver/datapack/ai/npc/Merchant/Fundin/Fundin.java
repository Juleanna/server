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
package com.l2jserver.datapack.ai.npc.Merchant.Fundin;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Fundin extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(3031, 20, 0.000000, 0), // spirit_ore
		new BuySellList(1785, 20, 0.000000, 0), // soul_ore
		new BuySellList(5192, 20, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 20, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 20, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 20, 0.000000, 0), // rope_of_magic_a
		new BuySellList(2130, 20, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 20, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 20, 0.000000, 0) // gemstone_b
	};
	
	private static final int npcId = 31274;
	
	public Fundin() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "mineral_trader_fundin001.htm";
		super.fnYouAreChaotic = "mineral_trader_fundin006.htm";
	}
}