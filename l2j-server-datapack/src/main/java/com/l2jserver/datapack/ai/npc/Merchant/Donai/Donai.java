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
package com.l2jserver.datapack.ai.npc.Merchant.Donai;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Donai extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1529, 15, 0.000000, 0), // sb_night_murmur1
		new BuySellList(1525, 15, 0.000000, 0), // sb_blood_lust1
		new BuySellList(1527, 15, 0.000000, 0), // sb_pain_thorn1
		new BuySellList(1524, 15, 0.000000, 0), // sb_devotioin_of_shine1
		new BuySellList(1531, 15, 0.000000, 0), // sb_chill_flame1
		new BuySellList(1522, 15, 0.000000, 0), // sb_mass_frenzy1
		new BuySellList(1526, 15, 0.000000, 0), // sb_external_fear1
		new BuySellList(1534, 15, 0.000000, 0), // sb_entice_madness1
		new BuySellList(1537, 15, 0.000000, 0), // sb_pain_edge1
		new BuySellList(1856, 15, 0.000000, 0), // sb_inspire_life_force1
		new BuySellList(1523, 15, 0.000000, 0), // sb_devotioin_of_soul1
		new BuySellList(1521, 15, 0.000000, 0), // sb_burning_spirit1
		new BuySellList(1535, 15, 0.000000, 0), // sb_blaze_quake1
		new BuySellList(1532, 15, 0.000000, 0), // sb_eternal_flame1
		new BuySellList(1536, 15, 0.000000, 0), // sb_bind_will1
		new BuySellList(1533, 15, 0.000000, 0), // sb_aura_sway1
		new BuySellList(1528, 15, 0.000000, 0), // sb_engrave_seal_of_timid1
		new BuySellList(1518, 15, 0.000000, 0), // sb_pure_inspiration1
		new BuySellList(1519, 15, 0.000000, 0) // sb_power_of_paagrio1
	};
	
	private static final int npcId = 31430;
	
	public Donai() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "amulet_seller_donai001.htm";
		super.fnYouAreChaotic = "amulet_seller_donai006.htm";
	}
}