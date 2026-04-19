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
package com.l2jserver.datapack.ai.npc.Merchant.Phojett;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Phojett extends Merchant {
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1104, 10, 0.000000, 0), // hose_of_devotion
		new BuySellList(1101, 10, 0.000000, 0), // tunic_of_devotion
		new BuySellList(1105, 10, 0.000000, 0), // hose_of_magicpower
		new BuySellList(1102, 10, 0.000000, 0), // tunic_of_magicpower
		new BuySellList(465, 10, 0.000000, 0), // cursed_hose
		new BuySellList(432, 10, 0.000000, 0), // cursed_tunic
		new BuySellList(467, 10, 0.000000, 0), // dark_hose
		new BuySellList(434, 10, 0.000000, 0), // white_tunic
		new BuySellList(468, 10, 0.000000, 0), // mage's_hose
		new BuySellList(435, 10, 0.000000, 0), // mage's_tunic
		new BuySellList(469, 10, 0.000000, 0), // hose_of_knowledge
		new BuySellList(436, 10, 0.000000, 0), // tunic_of_knowledge
		new BuySellList(470, 10, 0.000000, 0), // mithril_hose
		new BuySellList(437, 10, 0.000000, 0), // mithril_tunic
		new BuySellList(438, 10, 0.000000, 0), // sage's_rag
		new BuySellList(102, 10, 0.000000, 0), // round_shield
		new BuySellList(625, 10, 0.000000, 0), // bone_shield
		new BuySellList(626, 10, 0.000000, 0), // bronze_shield
		new BuySellList(627, 10, 0.000000, 0), // aspis
		new BuySellList(628, 10, 0.000000, 0), // hoplon
		new BuySellList(629, 10, 0.000000, 0), // kite_shield
		new BuySellList(2493, 10, 0.000000, 0), // brigandine_shield
		new BuySellList(630, 10, 0.000000, 0), // square_shield
		new BuySellList(2494, 10, 0.000000, 0), // plate_shield
		new BuySellList(50, 10, 0.000000, 0), // leather_gloves
		new BuySellList(51, 10, 0.000000, 0), // bracer
		new BuySellList(604, 10, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 10, 0.000000, 0), // leather_gauntlet
		new BuySellList(63, 10, 0.000000, 0), // gauntlet
		new BuySellList(606, 10, 0.000000, 0), // gauntlet_of_repose_of_the_soul
		new BuySellList(2447, 10, 0.000000, 0), // gloves_of_knowledge
		new BuySellList(2450, 10, 0.000000, 0), // elven_mithril_gloves
		new BuySellList(61, 10, 0.000000, 0), // mithril_glove
		new BuySellList(607, 10, 0.000000, 0), // ogre_power_gauntlet
		new BuySellList(2451, 10, 0.000000, 0), // sage's_worn_gloves
		new BuySellList(2459, 10, 0.000000, 0), // demon's_gloves
		new BuySellList(2463, 10, 0.000000, 0), // blessed_gloves
		new BuySellList(38, 10, 0.000000, 0), // low_boots
		new BuySellList(39, 10, 0.000000, 0), // boots
		new BuySellList(40, 10, 0.000000, 0), // leather_boots
		new BuySellList(1123, 10, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(553, 10, 0.000000, 0), // iron_boots
		new BuySellList(1124, 10, 0.000000, 0), // boots_of_power
		new BuySellList(2423, 10, 0.000000, 0), // boots_of_knowledge
		new BuySellList(2426, 10, 0.000000, 0), // elven_mithril_boots
		new BuySellList(1125, 10, 0.000000, 0), // assault_boots
		new BuySellList(2427, 10, 0.000000, 0), // slamander_skin_boots
		new BuySellList(2428, 10, 0.000000, 0), // plate_boots
		new BuySellList(2435, 10, 0.000000, 0), // demon's_boots
		new BuySellList(44, 10, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 10, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 10, 0.000000, 0), // bone_helmet
		new BuySellList(46, 10, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 10, 0.000000, 0), // helmet
		new BuySellList(2411, 10, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 10, 0.000000, 0), // plate_helmet
		new BuySellList(439, 10, 0.000000, 0), // karmian_tunic
		new BuySellList(471, 10, 0.000000, 0), // karmian_hose
		new BuySellList(2430, 10, 0.000000, 0), // karmian_boots
		new BuySellList(2454, 10, 0.000000, 0), // karmian_gloves
		new BuySellList(440, 10, 0.000000, 0), // robe_of_seal
		new BuySellList(472, 10, 0.000000, 0), // demon's_hose
		new BuySellList(473, 10, 0.000000, 0), // divine_hose
		new BuySellList(441, 10, 0.000000, 0), // demon's_tunic
		new BuySellList(442, 10, 0.000000, 0), // divine_tunic
		new BuySellList(9577, 10, 0.000000, 0), // tshirt
		new BuySellList(9578, 10, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 10, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 10, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(12011, 10, 0.000000, 0), // cursed_hose_low
		new BuySellList(12010, 10, 0.000000, 0), // cursed_tunic_low
		new BuySellList(12017, 10, 0.000000, 0), // dark_hose_low
		new BuySellList(12022, 10, 0.000000, 0), // white_tunic_low
		new BuySellList(12019, 10, 0.000000, 0), // mage's_hose_low
		new BuySellList(12018, 10, 0.000000, 0), // mage's_tunic_low
		new BuySellList(12048, 10, 0.000000, 0), // hose_of_knowledge_low
		new BuySellList(12047, 10, 0.000000, 0), // tunic_of_knowledge_low
		new BuySellList(12058, 10, 0.000000, 0), // mithril_hose_low
		new BuySellList(12057, 10, 0.000000, 0), // mithril_tunic_low
		new BuySellList(12083, 10, 0.000000, 0), // sage's_rag_low
		new BuySellList(12013, 10, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 10, 0.000000, 0), // aspis_low
		new BuySellList(12051, 10, 0.000000, 0), // hoplon_low
		new BuySellList(12069, 10, 0.000000, 0), // kite_shield_low
		new BuySellList(12063, 10, 0.000000, 0), // brigandine_shield_low
		new BuySellList(12073, 10, 0.000000, 0), // square_shield_low
		new BuySellList(12078, 10, 0.000000, 0), // plate_shield_low
		new BuySellList(12007, 10, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 10, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12038, 10, 0.000000, 0), // gauntlet_low
		new BuySellList(12068, 10, 0.000000, 0), // gauntlet_of_repose_of_the_soul_low
		new BuySellList(12046, 10, 0.000000, 0), // gloves_of_knowledge_low
		new BuySellList(12065, 10, 0.000000, 0), // elven_mithril_gloves_low
		new BuySellList(12072, 10, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 10, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12082, 10, 0.000000, 0), // sage's_worn_gloves_low
		new BuySellList(12006, 10, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 10, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 10, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 10, 0.000000, 0), // boots_of_power_low
		new BuySellList(12045, 10, 0.000000, 0), // boots_of_knowledge_low
		new BuySellList(12066, 10, 0.000000, 0), // elven_mithril_boots_low
		new BuySellList(12122, 10, 0.000000, 0), // demon's_gloves_low
		new BuySellList(12137, 10, 0.000000, 0), // blessed_gloves_low
		new BuySellList(12071, 10, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 10, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 10, 0.000000, 0), // plate_boots_low
		new BuySellList(12121, 10, 0.000000, 0), // demon's_boots_low
		new BuySellList(12009, 10, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 10, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 10, 0.000000, 0), // helmet_low
		new BuySellList(12064, 10, 0.000000, 0), // brigandine_helmet_low
		new BuySellList(12079, 10, 0.000000, 0), // plate_helmet_low
		new BuySellList(12099, 10, 0.000000, 0), // karmian_tunic_low
		new BuySellList(12100, 10, 0.000000, 0), // karmian_hose_low
		new BuySellList(12098, 10, 0.000000, 0), // karmian_boots_low
		new BuySellList(12097, 10, 0.000000, 0), // karmian_gloves_low
		new BuySellList(12115, 10, 0.000000, 0), // robe_of_seal_low
		new BuySellList(12124, 10, 0.000000, 0), // demon's_hose_low
		new BuySellList(12139, 10, 0.000000, 0), // divine_hose_low
		new BuySellList(12123, 10, 0.000000, 0), // demon's_tunic_low
		new BuySellList(12138, 10, 0.000000, 0) // divine_tunic_low
	};
	
	private static final int npcId = 30087;
	
	public Phojett() {
		super(npcId);
		
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "phojett001.htm";
		super.fnYouAreChaotic = "phojett006.htm";
	}
}