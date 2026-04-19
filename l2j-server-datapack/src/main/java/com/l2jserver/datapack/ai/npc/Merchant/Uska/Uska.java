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
package com.l2jserver.datapack.ai.npc.Merchant.Uska;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Uska extends MerchantForNewbie {
	
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
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
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
		new BuySellList(3104, 15, 0.000000, 0), // sb_glory_of_paagrio1
		new BuySellList(3114, 15, 0.000000, 0), // sb_steal_essence1
		new BuySellList(3110, 15, 0.000000, 0), // sb_seal_of_scourge1
		new BuySellList(3105, 15, 0.000000, 0), // sb_seal_of_winter1
		new BuySellList(3113, 15, 0.000000, 0), // sb_shield_of_paagrio1
		new BuySellList(3103, 15, 0.000000, 0), // sb_wisdom_of_paagrio1
		new BuySellList(3117, 15, 0.000000, 0), // sb_chant_of_evasion1
		new BuySellList(4204, 15, 0.000000, 0), // sb_tact_of_paagrio
		new BuySellList(3115, 15, 0.000000, 0), // sb_freezing_flame1
		new BuySellList(4205, 15, 0.000000, 0), // sb_rage_of_paagrio
		new BuySellList(3112, 15, 0.000000, 0), // sb_sight_of_paagrio1
		new BuySellList(4926, 15, 0.000000, 0), // sb_soul_guard1
		new BuySellList(3107, 15, 0.000000, 0), // sb_seal_of_gloom1
		new BuySellList(3108, 15, 0.000000, 0), // sb_seal_of_mirage1
		new BuySellList(3118, 15, 0.000000, 0), // sb_chant_of_rage1
		new BuySellList(6397, 15, 0.000000, 0), // sb_chant_of_vampire1-4
		new BuySellList(3943, 15, 0.000000, 0), // sb_heart_of_paagrio1
		new BuySellList(3109, 15, 0.000000, 0), // sb_seal_of_silence1
		new BuySellList(3111, 15, 0.000000, 0), // sb_seal_of_suspension1
		new BuySellList(3106, 15, 0.000000, 0), // sb_seal_of_flame1
		new BuySellList(6396, 15, 0.000000, 0), // sb_chant_of_eagle1-3
		new BuySellList(3116, 15, 0.000000, 0), // sb_chant_of_fury1
		new BuySellList(6395, 15, 0.000000, 0), // sb_chant_of_predator1-3
		new BuySellList(4925, 15, 0.000000, 0), // sb_speed_of_paagrio1
		new BuySellList(8391, 15, 0.000000, 0), // sb_chant_of_earth
		new BuySellList(8390, 15, 0.000000, 0), // sb_chant_of_war
		new BuySellList(4927, 15, 0.000000, 0), // sb_chant_of_revenge1
		new BuySellList(6351, 15, 0.000000, 0), // sb_ritual_of_life1
		new BuySellList(6350, 15, 0.000000, 0) // sb_honor_of_paagrio1
	};
	
	private static final int npcId = 30560;
	
	public Uska() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "trader_uska001.htm";
		super.fnYouAreChaotic = "trader_uska006.htm";
	}
}