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
package com.l2jserver.datapack.ai.npc.Merchant.Volker;

import com.l2jserver.datapack.ai.npc.Merchant.WharfKeeper;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Volker extends WharfKeeper {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(7904, 20, 0.000000, 0), // boat_ticket_rune_glu
		new BuySellList(8924, 20, 0.000000, 0), // boat_ticket_prime_rune
		new BuySellList(8925, 20, 0.000000, 0) // boat_ticket_rune_prime
	};
	
	private static final int npcId = 31351;
	
	public Volker() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "wharf_manager_volker001.htm";
		super.fnYouAreChaotic = "wharf_manager_volker006.htm";
	}
}