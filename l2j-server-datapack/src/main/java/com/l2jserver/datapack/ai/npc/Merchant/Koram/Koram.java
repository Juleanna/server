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
package com.l2jserver.datapack.ai.npc.Merchant.Koram;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Koram extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1529, 20, 0.000000, 0), // sb_night_murmur1
		new BuySellList(1525, 20, 0.000000, 0), // sb_blood_lust1
		new BuySellList(1527, 20, 0.000000, 0), // sb_pain_thorn1
		new BuySellList(1524, 20, 0.000000, 0), // sb_devotioin_of_shine1
		new BuySellList(1531, 20, 0.000000, 0), // sb_chill_flame1
		new BuySellList(1522, 20, 0.000000, 0), // sb_mass_frenzy1
		new BuySellList(1526, 20, 0.000000, 0), // sb_external_fear1
		new BuySellList(1534, 20, 0.000000, 0), // sb_entice_madness1
		new BuySellList(1537, 20, 0.000000, 0), // sb_pain_edge1
		new BuySellList(1856, 20, 0.000000, 0), // sb_inspire_life_force1
		new BuySellList(1523, 20, 0.000000, 0), // sb_devotioin_of_soul1
		new BuySellList(1521, 20, 0.000000, 0), // sb_burning_spirit1
		new BuySellList(1535, 20, 0.000000, 0), // sb_blaze_quake1
		new BuySellList(1532, 20, 0.000000, 0), // sb_eternal_flame1
		new BuySellList(1536, 20, 0.000000, 0), // sb_bind_will1
		new BuySellList(1533, 20, 0.000000, 0), // sb_aura_sway1
		new BuySellList(1528, 20, 0.000000, 0), // sb_engrave_seal_of_timid1
		new BuySellList(1518, 20, 0.000000, 0), // sb_pure_inspiration1
		new BuySellList(1519, 20, 0.000000, 0) // sb_power_of_paagrio1
	};
	
	private static final int npcId = 31425;
	
	public Koram() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "amulet_seller_koram001.htm";
		super.fnYouAreChaotic = "amulet_seller_koram006.htm";
	}
}