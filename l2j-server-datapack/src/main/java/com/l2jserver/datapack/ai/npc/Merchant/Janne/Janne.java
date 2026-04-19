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
package com.l2jserver.datapack.ai.npc.Merchant.Janne;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Janne extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(412, 20, 0.000000, 0), // tights_pants
		new BuySellList(390, 20, 0.000000, 0), // tights_shirt
		new BuySellList(24, 20, 0.000000, 0), // bone_breastplate
		new BuySellList(31, 20, 0.000000, 0), // bone_gaiters
		new BuySellList(25, 20, 0.000000, 0), // piece_bone_breastplate
		new BuySellList(26, 20, 0.000000, 0), // bronze_breastplate
		new BuySellList(33, 20, 0.000000, 0), // hard_leather_gaiters
		new BuySellList(32, 20, 0.000000, 0), // piece_bone_gaiters
		new BuySellList(27, 20, 0.000000, 0), // hard_leather_shirt
		new BuySellList(34, 20, 0.000000, 0), // bronze_gaiters
		new BuySellList(413, 20, 0.000000, 0), // puma_skin_gaiters
		new BuySellList(347, 20, 0.000000, 0), // ring_breastplate
		new BuySellList(391, 20, 0.000000, 0), // puma_skin_shirt
		new BuySellList(376, 20, 0.000000, 0), // iron_plate_gaiters
		new BuySellList(414, 20, 0.000000, 0), // lion_skin_gaiters
		new BuySellList(392, 20, 0.000000, 0), // lion_skin_shirt
		new BuySellList(348, 20, 0.000000, 0), // scale_mail
		new BuySellList(349, 20, 0.000000, 0), // compound_scale_mail
		new BuySellList(350, 20, 0.000000, 0), // dwarven_scale_mail
		new BuySellList(58, 20, 0.000000, 0), // mithril_breastplate
		new BuySellList(59, 20, 0.000000, 0), // mithril_gaiters
		new BuySellList(416, 20, 0.000000, 0), // reinforce_leather_gaiters
		new BuySellList(377, 20, 0.000000, 0), // scale_gaiters
		new BuySellList(351, 20, 0.000000, 0), // blast_plate
		new BuySellList(378, 20, 0.000000, 0), // compound_scale_gaiters
		new BuySellList(379, 20, 0.000000, 0), // dwarven_scale_gaiters
		new BuySellList(393, 20, 0.000000, 0), // mithril_banded_mail
		new BuySellList(415, 20, 0.000000, 0), // mithril_banded_gaiters
		new BuySellList(394, 20, 0.000000, 0), // reinforce_leather_shirt
		new BuySellList(2378, 20, 0.000000, 0), // brigandine_gaiters
		new BuySellList(352, 20, 0.000000, 0), // brigandine
		new BuySellList(395, 20, 0.000000, 0), // manticor_skin_shirt
		new BuySellList(417, 20, 0.000000, 0), // manticor_skin_gaiters
		new BuySellList(2377, 20, 0.000000, 0), // mithril_scale_gaiters
		new BuySellList(353, 20, 0.000000, 0), // half_plate
		new BuySellList(380, 20, 0.000000, 0), // plate_gaiters
		new BuySellList(396, 20, 0.000000, 0), // slamander_skin_mail
		new BuySellList(102, 20, 0.000000, 0), // round_shield
		new BuySellList(625, 20, 0.000000, 0), // bone_shield
		new BuySellList(626, 20, 0.000000, 0), // bronze_shield
		new BuySellList(627, 20, 0.000000, 0), // aspis
		new BuySellList(628, 20, 0.000000, 0), // hoplon
		new BuySellList(629, 20, 0.000000, 0), // kite_shield
		new BuySellList(2493, 20, 0.000000, 0), // brigandine_shield
		new BuySellList(630, 20, 0.000000, 0), // square_shield
		new BuySellList(2494, 20, 0.000000, 0), // plate_shield
		new BuySellList(50, 20, 0.000000, 0), // leather_gloves
		new BuySellList(51, 20, 0.000000, 0), // bracer
		new BuySellList(604, 20, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 20, 0.000000, 0), // leather_gauntlet
		new BuySellList(63, 20, 0.000000, 0), // gauntlet
		new BuySellList(606, 20, 0.000000, 0), // gauntlet_of_repose_of_the_soul
		new BuySellList(2446, 20, 0.000000, 0), // reinforce_leather_gloves
		new BuySellList(2448, 20, 0.000000, 0), // manticor_skin_gloves
		new BuySellList(61, 20, 0.000000, 0), // mithril_glove
		new BuySellList(607, 20, 0.000000, 0), // ogre_power_gauntlet
		new BuySellList(38, 20, 0.000000, 0), // low_boots
		new BuySellList(39, 20, 0.000000, 0), // boots
		new BuySellList(40, 20, 0.000000, 0), // leather_boots
		new BuySellList(1123, 20, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(553, 20, 0.000000, 0), // iron_boots
		new BuySellList(1124, 20, 0.000000, 0), // boots_of_power
		new BuySellList(2422, 20, 0.000000, 0), // reinforce_leather_boots
		new BuySellList(2424, 20, 0.000000, 0), // manticor_skin_boots
		new BuySellList(2425, 20, 0.000000, 0), // brigandine_boots
		new BuySellList(1125, 20, 0.000000, 0), // assault_boots
		new BuySellList(2427, 20, 0.000000, 0), // slamander_skin_boots
		new BuySellList(2428, 20, 0.000000, 0), // plate_boots
		new BuySellList(44, 20, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 20, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 20, 0.000000, 0), // bone_helmet
		new BuySellList(46, 20, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 20, 0.000000, 0), // helmet
		new BuySellList(2411, 20, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 20, 0.000000, 0), // plate_helmet
		new BuySellList(9577, 20, 0.000000, 0), // tshirt
		new BuySellList(9578, 20, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 20, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 20, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1104, 20, 0.000000, 0), // hose_of_devotion
		new BuySellList(1101, 20, 0.000000, 0), // tunic_of_devotion
		new BuySellList(1105, 20, 0.000000, 0), // hose_of_magicpower
		new BuySellList(1102, 20, 0.000000, 0), // tunic_of_magicpower
		new BuySellList(465, 20, 0.000000, 0), // cursed_hose
		new BuySellList(432, 20, 0.000000, 0), // cursed_tunic
		new BuySellList(467, 20, 0.000000, 0), // dark_hose
		new BuySellList(434, 20, 0.000000, 0), // white_tunic
		new BuySellList(468, 20, 0.000000, 0), // mage's_hose
		new BuySellList(435, 20, 0.000000, 0), // mage's_tunic
		new BuySellList(469, 20, 0.000000, 0), // hose_of_knowledge
		new BuySellList(436, 20, 0.000000, 0), // tunic_of_knowledge
		new BuySellList(470, 20, 0.000000, 0), // mithril_hose
		new BuySellList(437, 20, 0.000000, 0), // mithril_tunic
		new BuySellList(438, 20, 0.000000, 0), // sage's_rag
		new BuySellList(102, 20, 0.000000, 0), // round_shield
		new BuySellList(625, 20, 0.000000, 0), // bone_shield
		new BuySellList(626, 20, 0.000000, 0), // bronze_shield
		new BuySellList(627, 20, 0.000000, 0), // aspis
		new BuySellList(628, 20, 0.000000, 0), // hoplon
		new BuySellList(629, 20, 0.000000, 0), // kite_shield
		new BuySellList(2493, 20, 0.000000, 0), // brigandine_shield
		new BuySellList(630, 20, 0.000000, 0), // square_shield
		new BuySellList(2494, 20, 0.000000, 0), // plate_shield
		new BuySellList(50, 20, 0.000000, 0), // leather_gloves
		new BuySellList(51, 20, 0.000000, 0), // bracer
		new BuySellList(604, 20, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 20, 0.000000, 0), // leather_gauntlet
		new BuySellList(63, 20, 0.000000, 0), // gauntlet
		new BuySellList(606, 20, 0.000000, 0), // gauntlet_of_repose_of_the_soul
		new BuySellList(2447, 20, 0.000000, 0), // gloves_of_knowledge
		new BuySellList(2450, 20, 0.000000, 0), // elven_mithril_gloves
		new BuySellList(61, 20, 0.000000, 0), // mithril_glove
		new BuySellList(607, 20, 0.000000, 0), // ogre_power_gauntlet
		new BuySellList(2451, 20, 0.000000, 0), // sage's_worn_gloves
		new BuySellList(38, 20, 0.000000, 0), // low_boots
		new BuySellList(39, 20, 0.000000, 0), // boots
		new BuySellList(40, 20, 0.000000, 0), // leather_boots
		new BuySellList(1123, 20, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(553, 20, 0.000000, 0), // iron_boots
		new BuySellList(1124, 20, 0.000000, 0), // boots_of_power
		new BuySellList(2423, 20, 0.000000, 0), // boots_of_knowledge
		new BuySellList(2426, 20, 0.000000, 0), // elven_mithril_boots
		new BuySellList(1125, 20, 0.000000, 0), // assault_boots
		new BuySellList(2427, 20, 0.000000, 0), // slamander_skin_boots
		new BuySellList(2428, 20, 0.000000, 0), // plate_boots
		new BuySellList(44, 20, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 20, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 20, 0.000000, 0), // bone_helmet
		new BuySellList(46, 20, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 20, 0.000000, 0), // helmet
		new BuySellList(2411, 20, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 20, 0.000000, 0), // plate_helmet
		new BuySellList(9577, 20, 0.000000, 0), // tshirt
		new BuySellList(9578, 20, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 20, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 20, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12014, 20, 0.000000, 0), // puma_skin_gaiters_low
		new BuySellList(12008, 20, 0.000000, 0), // ring_breastplate_low
		new BuySellList(12015, 20, 0.000000, 0), // puma_skin_shirt_low
		new BuySellList(12012, 20, 0.000000, 0), // iron_plate_gaiters_low
		new BuySellList(12020, 20, 0.000000, 0), // lion_skin_gaiters_low
		new BuySellList(12021, 20, 0.000000, 0), // lion_skin_shirt_low
		new BuySellList(12024, 20, 0.000000, 0), // scale_mail_low
		new BuySellList(12032, 20, 0.000000, 0), // compound_scale_mail_low
		new BuySellList(12030, 20, 0.000000, 0), // dwarven_scale_mail_low
		new BuySellList(12031, 20, 0.000000, 0), // mithril_breastplate_low
		new BuySellList(12041, 20, 0.000000, 0), // mithril_gaiters_low
		new BuySellList(12034, 20, 0.000000, 0), // reinforce_leather_gaiters_low
		new BuySellList(12023, 20, 0.000000, 0), // scale_gaiters_low
		new BuySellList(12044, 20, 0.000000, 0), // blast_plate_low
		new BuySellList(12049, 20, 0.000000, 0), // compound_scale_gaiters_low
		new BuySellList(12039, 20, 0.000000, 0), // dwarven_scale_gaiters_low
		new BuySellList(12043, 20, 0.000000, 0), // mithril_banded_mail_low
		new BuySellList(12042, 20, 0.000000, 0), // mithril_banded_gaiters_low
		new BuySellList(12036, 20, 0.000000, 0), // reinforce_leather_shirt_low
		new BuySellList(12060, 20, 0.000000, 0), // brigandine_gaiters_low
		new BuySellList(12059, 20, 0.000000, 0), // brigandine_low
		new BuySellList(12056, 20, 0.000000, 0), // mithril_scale_gaiters_low
		new BuySellList(12054, 20, 0.000000, 0), // manticor_skin_shirt_low
		new BuySellList(12052, 20, 0.000000, 0), // manticor_skin_gaiters_low
		new BuySellList(12081, 20, 0.000000, 0), // half_plate_low
		new BuySellList(12080, 20, 0.000000, 0), // plate_gaiters_low
		new BuySellList(12074, 20, 0.000000, 0), // slamander_skin_mail_low
		new BuySellList(12013, 20, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 20, 0.000000, 0), // aspis_low
		new BuySellList(12051, 20, 0.000000, 0), // hoplon_low
		new BuySellList(12069, 20, 0.000000, 0), // kite_shield_low
		new BuySellList(12073, 20, 0.000000, 0), // square_shield_low
		new BuySellList(12078, 20, 0.000000, 0), // plate_shield_low
		new BuySellList(12007, 20, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 20, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12038, 20, 0.000000, 0), // gauntlet_low
		new BuySellList(12068, 20, 0.000000, 0), // gauntlet_of_repose_of_the_soul_low
		new BuySellList(12037, 20, 0.000000, 0), // reinforce_leather_gloves_low
		new BuySellList(12072, 20, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 20, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12006, 20, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 20, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 20, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 20, 0.000000, 0), // boots_of_power_low
		new BuySellList(12035, 20, 0.000000, 0), // reinforce_leather_boots_low
		new BuySellList(12071, 20, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 20, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 20, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 20, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 20, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 20, 0.000000, 0), // helmet_low
		new BuySellList(12079, 20, 0.000000, 0) // plate_helmet_low
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(12011, 20, 0.000000, 0), // cursed_hose_low
		new BuySellList(12010, 20, 0.000000, 0), // cursed_tunic_low
		new BuySellList(12017, 20, 0.000000, 0), // dark_hose_low
		new BuySellList(12022, 20, 0.000000, 0), // white_tunic_low
		new BuySellList(12019, 20, 0.000000, 0), // mage's_hose_low
		new BuySellList(12018, 20, 0.000000, 0), // mage's_tunic_low
		new BuySellList(12048, 20, 0.000000, 0), // hose_of_knowledge_low
		new BuySellList(12047, 20, 0.000000, 0), // tunic_of_knowledge_low
		new BuySellList(12058, 20, 0.000000, 0), // mithril_hose_low
		new BuySellList(12057, 20, 0.000000, 0), // mithril_tunic_low
		new BuySellList(12083, 20, 0.000000, 0), // sage's_rag_low
		new BuySellList(12013, 20, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 20, 0.000000, 0), // aspis_low
		new BuySellList(12051, 20, 0.000000, 0), // hoplon_low
		new BuySellList(12069, 20, 0.000000, 0), // kite_shield_low
		new BuySellList(12063, 20, 0.000000, 0), // brigandine_shield_low
		new BuySellList(12073, 20, 0.000000, 0), // square_shield_low
		new BuySellList(12078, 20, 0.000000, 0), // plate_shield_low
		new BuySellList(12007, 20, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 20, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12038, 20, 0.000000, 0), // gauntlet_low
		new BuySellList(12068, 20, 0.000000, 0), // gauntlet_of_repose_of_the_soul_low
		new BuySellList(12046, 20, 0.000000, 0), // gloves_of_knowledge_low
		new BuySellList(12065, 20, 0.000000, 0), // elven_mithril_gloves_low
		new BuySellList(12072, 20, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 20, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12082, 20, 0.000000, 0), // sage's_worn_gloves_low
		new BuySellList(12006, 20, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 20, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 20, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 20, 0.000000, 0), // boots_of_power_low
		new BuySellList(12045, 20, 0.000000, 0), // boots_of_knowledge_low
		new BuySellList(12066, 20, 0.000000, 0), // elven_mithril_boots_low
		new BuySellList(12071, 20, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 20, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 20, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 20, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 20, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 20, 0.000000, 0), // helmet_low
		new BuySellList(12079, 20, 0.000000, 0) // plate_helmet_low
	};
	
	private static final int npcId = 31259;
	
	public Janne() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "trader_janne001.htm";
		super.fnYouAreChaotic = "trader_janne006.htm";
	}
}