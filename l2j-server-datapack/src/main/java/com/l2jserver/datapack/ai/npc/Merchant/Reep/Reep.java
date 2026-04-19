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
package com.l2jserver.datapack.ai.npc.Merchant.Reep;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Reep extends MerchantForNewbie {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1, 15, 0.000000, 0), // small_sword
		new BuySellList(4, 15, 0.000000, 0), // club
		new BuySellList(11, 15, 0.000000, 0), // bone_dagger
		new BuySellList(13, 15, 0.000000, 0), // short_bow
		new BuySellList(3, 15, 0.000000, 0), // broad_sword
		new BuySellList(152, 15, 0.000000, 0), // heavy_chisel
		new BuySellList(12, 15, 0.000000, 0), // knife
		new BuySellList(215, 15, 0.000000, 0), // doomed_dagger
		new BuySellList(14, 15, 0.000000, 0), // bow
		new BuySellList(5, 15, 0.000000, 0), // mace
		new BuySellList(153, 15, 0.000000, 0), // sickle
		new BuySellList(1333, 15, 0.000000, 0), // brandish
		new BuySellList(66, 15, 0.000000, 0), // gladius
		new BuySellList(67, 15, 0.000000, 0), // orcish_sword
		new BuySellList(122, 15, 0.000000, 0), // handmade_sword
		new BuySellList(154, 15, 0.000000, 0), // dwarven_mace
		new BuySellList(216, 15, 0.000000, 0), // dirk
		new BuySellList(271, 15, 0.000000, 0), // hunting_bow
		new BuySellList(2, 15, 0.000000, 0), // long_sword
		new BuySellList(218, 15, 0.000000, 0), // throw_knife
		new BuySellList(272, 15, 0.000000, 0), // bow_of_forest
		new BuySellList(15, 15, 0.000000, 0), // short_spear
		new BuySellList(5284, 15, 0.000000, 0) // zweihander
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(7, 15, 0.000000, 0), // apprentice's_rod
		new BuySellList(308, 15, 0.000000, 0), // buffalo_horn
		new BuySellList(8, 15, 0.000000, 0), // willow_staff
		new BuySellList(99, 15, 0.000000, 0), // apprentice's_spellbook
		new BuySellList(9, 15, 0.000000, 0), // cedar_staff
		new BuySellList(176, 15, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 15, 0.000000, 0) // relic_of_saints
	};
	
	private static final int npcId = 30516;
	
	public Reep() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "trader_reep001.htm";
		super.fnYouAreChaotic = "trader_reep006.htm";
	}
}