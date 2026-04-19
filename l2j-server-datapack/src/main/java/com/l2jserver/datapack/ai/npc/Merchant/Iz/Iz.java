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
package com.l2jserver.datapack.ai.npc.Merchant.Iz;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Iz extends Merchant {
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1104, 15, 0.000000, 0), // hose_of_devotion
		new BuySellList(1101, 15, 0.000000, 0), // tunic_of_devotion
		new BuySellList(1105, 15, 0.000000, 0), // hose_of_magicpower
		new BuySellList(1102, 15, 0.000000, 0), // tunic_of_magicpower
		new BuySellList(465, 15, 0.000000, 0), // cursed_hose
		new BuySellList(432, 15, 0.000000, 0), // cursed_tunic
		new BuySellList(467, 15, 0.000000, 0), // dark_hose
		new BuySellList(434, 15, 0.000000, 0), // white_tunic
		new BuySellList(468, 15, 0.000000, 0), // mage's_hose
		new BuySellList(435, 15, 0.000000, 0), // mage's_tunic
		new BuySellList(469, 15, 0.000000, 0), // hose_of_knowledge
		new BuySellList(436, 15, 0.000000, 0), // tunic_of_knowledge
		new BuySellList(470, 15, 0.000000, 0), // mithril_hose
		new BuySellList(437, 15, 0.000000, 0), // mithril_tunic
		new BuySellList(438, 15, 0.000000, 0), // sage's_rag
		new BuySellList(102, 15, 0.000000, 0), // round_shield
		new BuySellList(625, 15, 0.000000, 0), // bone_shield
		new BuySellList(626, 15, 0.000000, 0), // bronze_shield
		new BuySellList(627, 15, 0.000000, 0), // aspis
		new BuySellList(628, 15, 0.000000, 0), // hoplon
		new BuySellList(629, 15, 0.000000, 0), // kite_shield
		new BuySellList(2493, 15, 0.000000, 0), // brigandine_shield
		new BuySellList(630, 15, 0.000000, 0), // square_shield
		new BuySellList(2494, 15, 0.000000, 0), // plate_shield
		new BuySellList(50, 15, 0.000000, 0), // leather_gloves
		new BuySellList(51, 15, 0.000000, 0), // bracer
		new BuySellList(604, 15, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 15, 0.000000, 0), // leather_gauntlet
		new BuySellList(63, 15, 0.000000, 0), // gauntlet
		new BuySellList(606, 15, 0.000000, 0), // gauntlet_of_repose_of_the_soul
		new BuySellList(2447, 15, 0.000000, 0), // gloves_of_knowledge
		new BuySellList(2450, 15, 0.000000, 0), // elven_mithril_gloves
		new BuySellList(61, 15, 0.000000, 0), // mithril_glove
		new BuySellList(607, 15, 0.000000, 0), // ogre_power_gauntlet
		new BuySellList(2451, 15, 0.000000, 0), // sage's_worn_gloves
		new BuySellList(38, 15, 0.000000, 0), // low_boots
		new BuySellList(39, 15, 0.000000, 0), // boots
		new BuySellList(40, 15, 0.000000, 0), // leather_boots
		new BuySellList(1123, 15, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(553, 15, 0.000000, 0), // iron_boots
		new BuySellList(1124, 15, 0.000000, 0), // boots_of_power
		new BuySellList(2423, 15, 0.000000, 0), // boots_of_knowledge
		new BuySellList(2426, 15, 0.000000, 0), // elven_mithril_boots
		new BuySellList(1125, 15, 0.000000, 0), // assault_boots
		new BuySellList(2427, 15, 0.000000, 0), // slamander_skin_boots
		new BuySellList(2428, 15, 0.000000, 0), // plate_boots
		new BuySellList(44, 15, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 15, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 15, 0.000000, 0), // bone_helmet
		new BuySellList(46, 15, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 15, 0.000000, 0), // helmet
		new BuySellList(2411, 15, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 15, 0.000000, 0), // plate_helmet
		new BuySellList(9577, 15, 0.000000, 0), // tshirt
		new BuySellList(9578, 15, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 15, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 15, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(12011, 15, 0.000000, 0), // cursed_hose_low
		new BuySellList(12010, 15, 0.000000, 0), // cursed_tunic_low
		new BuySellList(12017, 15, 0.000000, 0), // dark_hose_low
		new BuySellList(12022, 15, 0.000000, 0), // white_tunic_low
		new BuySellList(12019, 15, 0.000000, 0), // mage's_hose_low
		new BuySellList(12018, 15, 0.000000, 0), // mage's_tunic_low
		new BuySellList(12048, 15, 0.000000, 0), // hose_of_knowledge_low
		new BuySellList(12047, 15, 0.000000, 0), // tunic_of_knowledge_low
		new BuySellList(12058, 15, 0.000000, 0), // mithril_hose_low
		new BuySellList(12057, 15, 0.000000, 0), // mithril_tunic_low
		new BuySellList(12083, 15, 0.000000, 0), // sage's_rag_low
		new BuySellList(12013, 15, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 15, 0.000000, 0), // aspis_low
		new BuySellList(12051, 15, 0.000000, 0), // hoplon_low
		new BuySellList(12069, 15, 0.000000, 0), // kite_shield_low
		new BuySellList(12063, 15, 0.000000, 0), // brigandine_shield_low
		new BuySellList(12073, 15, 0.000000, 0), // square_shield_low
		new BuySellList(12078, 15, 0.000000, 0), // plate_shield_low
		new BuySellList(12007, 15, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 15, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12038, 15, 0.000000, 0), // gauntlet_low
		new BuySellList(12068, 15, 0.000000, 0), // gauntlet_of_repose_of_the_soul_low
		new BuySellList(12046, 15, 0.000000, 0), // gloves_of_knowledge_low
		new BuySellList(12065, 15, 0.000000, 0), // elven_mithril_gloves_low
		new BuySellList(12072, 15, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 15, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12082, 15, 0.000000, 0), // sage's_worn_gloves_low
		new BuySellList(12006, 15, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 15, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 15, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 15, 0.000000, 0), // boots_of_power_low
		new BuySellList(12045, 15, 0.000000, 0), // boots_of_knowledge_low
		new BuySellList(12066, 15, 0.000000, 0), // elven_mithril_boots_low
		new BuySellList(12071, 15, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 15, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 15, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 15, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 15, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 15, 0.000000, 0), // helmet_low
		new BuySellList(12079, 15, 0.000000, 0) // plate_helmet_low
	};
	
	private static final int npcId = 30164;
	
	public Iz() {
		super(npcId);
		
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "iz001.htm";
		super.fnYouAreChaotic = "iz006.htm";
	}
}