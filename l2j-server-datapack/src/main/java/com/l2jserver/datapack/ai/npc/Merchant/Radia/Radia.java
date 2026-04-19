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
package com.l2jserver.datapack.ai.npc.Merchant.Radia;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Radia extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(412, 10, 0.000000, 0), // tights_pants
		new BuySellList(390, 10, 0.000000, 0), // tights_shirt
		new BuySellList(24, 10, 0.000000, 0), // bone_breastplate
		new BuySellList(31, 10, 0.000000, 0), // bone_gaiters
		new BuySellList(25, 10, 0.000000, 0), // piece_bone_breastplate
		new BuySellList(26, 10, 0.000000, 0), // bronze_breastplate
		new BuySellList(33, 10, 0.000000, 0), // hard_leather_gaiters
		new BuySellList(32, 10, 0.000000, 0), // piece_bone_gaiters
		new BuySellList(27, 10, 0.000000, 0), // hard_leather_shirt
		new BuySellList(34, 10, 0.000000, 0), // bronze_gaiters
		new BuySellList(413, 10, 0.000000, 0), // puma_skin_gaiters
		new BuySellList(347, 10, 0.000000, 0), // ring_breastplate
		new BuySellList(391, 10, 0.000000, 0), // puma_skin_shirt
		new BuySellList(376, 10, 0.000000, 0), // iron_plate_gaiters
		new BuySellList(414, 10, 0.000000, 0), // lion_skin_gaiters
		new BuySellList(392, 10, 0.000000, 0), // lion_skin_shirt
		new BuySellList(348, 10, 0.000000, 0), // scale_mail
		new BuySellList(349, 10, 0.000000, 0), // compound_scale_mail
		new BuySellList(350, 10, 0.000000, 0), // dwarven_scale_mail
		new BuySellList(58, 10, 0.000000, 0), // mithril_breastplate
		new BuySellList(59, 10, 0.000000, 0), // mithril_gaiters
		new BuySellList(416, 10, 0.000000, 0), // reinforce_leather_gaiters
		new BuySellList(377, 10, 0.000000, 0), // scale_gaiters
		new BuySellList(351, 10, 0.000000, 0), // blast_plate
		new BuySellList(378, 10, 0.000000, 0), // compound_scale_gaiters
		new BuySellList(379, 10, 0.000000, 0), // dwarven_scale_gaiters
		new BuySellList(393, 10, 0.000000, 0), // mithril_banded_mail
		new BuySellList(415, 10, 0.000000, 0), // mithril_banded_gaiters
		new BuySellList(394, 10, 0.000000, 0), // reinforce_leather_shirt
		new BuySellList(2378, 10, 0.000000, 0), // brigandine_gaiters
		new BuySellList(352, 10, 0.000000, 0), // brigandine
		new BuySellList(395, 10, 0.000000, 0), // manticor_skin_shirt
		new BuySellList(417, 10, 0.000000, 0), // manticor_skin_gaiters
		new BuySellList(2377, 10, 0.000000, 0), // mithril_scale_gaiters
		new BuySellList(353, 10, 0.000000, 0), // half_plate
		new BuySellList(380, 10, 0.000000, 0), // plate_gaiters
		new BuySellList(396, 10, 0.000000, 0), // slamander_skin_mail
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
		new BuySellList(2446, 10, 0.000000, 0), // reinforce_leather_gloves
		new BuySellList(2448, 10, 0.000000, 0), // manticor_skin_gloves
		new BuySellList(2449, 10, 0.000000, 0), // brigandine_gauntlet
		new BuySellList(61, 10, 0.000000, 0), // mithril_glove
		new BuySellList(607, 10, 0.000000, 0), // ogre_power_gauntlet
		new BuySellList(38, 10, 0.000000, 0), // low_boots
		new BuySellList(39, 10, 0.000000, 0), // boots
		new BuySellList(40, 10, 0.000000, 0), // leather_boots
		new BuySellList(1123, 10, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(553, 10, 0.000000, 0), // iron_boots
		new BuySellList(1124, 10, 0.000000, 0), // boots_of_power
		new BuySellList(2422, 10, 0.000000, 0), // reinforce_leather_boots
		new BuySellList(2424, 10, 0.000000, 0), // manticor_skin_boots
		new BuySellList(2425, 10, 0.000000, 0), // brigandine_boots
		new BuySellList(1125, 10, 0.000000, 0), // assault_boots
		new BuySellList(2427, 10, 0.000000, 0), // slamander_skin_boots
		new BuySellList(2428, 10, 0.000000, 0), // plate_boots
		new BuySellList(44, 10, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 10, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 10, 0.000000, 0), // bone_helmet
		new BuySellList(46, 10, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 10, 0.000000, 0), // helmet
		new BuySellList(2411, 10, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 10, 0.000000, 0), // plate_helmet
		new BuySellList(62, 10, 0.000000, 0), // mithril_boots
		new BuySellList(354, 10, 0.000000, 0), // chain_mail_shirt
		new BuySellList(381, 10, 0.000000, 0), // chain_gaiters
		new BuySellList(397, 10, 0.000000, 0), // tempered_mithril_shirt
		new BuySellList(631, 10, 0.000000, 0), // eldarake
		new BuySellList(2387, 10, 0.000000, 0), // tempered_mithril_gaiters
		new BuySellList(2429, 10, 0.000000, 0), // chain_boots
		new BuySellList(2431, 10, 0.000000, 0), // plate_leather_boots
		new BuySellList(2432, 10, 0.000000, 0), // dwarven_chain_boots
		new BuySellList(2433, 10, 0.000000, 0), // boots_of_seal
		new BuySellList(2452, 10, 0.000000, 0), // reinforce_mithril_gloves
		new BuySellList(2453, 10, 0.000000, 0), // chain_gloves
		new BuySellList(2495, 10, 0.000000, 0), // chain_shield
		new BuySellList(398, 10, 0.000000, 0), // plate_leather
		new BuySellList(418, 10, 0.000000, 0), // plate_leather_gaiters
		new BuySellList(2455, 10, 0.000000, 0), // plate_leather_gloves
		new BuySellList(2496, 10, 0.000000, 0), // dwarven_chain_shield
		new BuySellList(60, 10, 0.000000, 0), // composite_armor
		new BuySellList(356, 10, 0.000000, 0), // full_plate_armor
		new BuySellList(401, 10, 0.000000, 0), // drake_leather_mail
		new BuySellList(632, 10, 0.000000, 0), // knight_shield
		new BuySellList(103, 10, 0.000000, 0), // tower_shield
		new BuySellList(107, 10, 0.000000, 0), // composite_shield
		new BuySellList(2497, 10, 0.000000, 0), // full_plate_shield
		new BuySellList(382, 10, 0.000000, 0), // dwarven_chain_gaiters
		new BuySellList(419, 10, 0.000000, 0), // rind_leather_gaiters
		new BuySellList(420, 10, 0.000000, 0), // theca_leather_gaiters
		new BuySellList(500, 10, 0.000000, 0), // great_helmet
		new BuySellList(517, 10, 0.000000, 0), // composite_helmet
		new BuySellList(1149, 10, 0.000000, 0), // shining_circlet
		new BuySellList(2414, 10, 0.000000, 0), // full_plate_helmet
		new BuySellList(2456, 10, 0.000000, 0), // dwarven_chain_gloves
		new BuySellList(2457, 10, 0.000000, 0), // gloves_of_seal
		new BuySellList(2458, 10, 0.000000, 0), // rind_leather_gloves
		new BuySellList(608, 10, 0.000000, 0), // mithril_gauntlet
		new BuySellList(2460, 10, 0.000000, 0), // theca_leather_gloves
		new BuySellList(2461, 10, 0.000000, 0), // drake_leather_gloves
		new BuySellList(2462, 10, 0.000000, 0), // full_plate_gauntlet
		new BuySellList(1126, 10, 0.000000, 0), // crimson_boots
		new BuySellList(2434, 10, 0.000000, 0), // rind_leather_boots
		new BuySellList(64, 10, 0.000000, 0), // composite_boots
		new BuySellList(2436, 10, 0.000000, 0), // theca_leather_boots
		new BuySellList(2437, 10, 0.000000, 0), // drake_leather_boots
		new BuySellList(2438, 10, 0.000000, 0), // full_plate_boots
		new BuySellList(355, 10, 0.000000, 0), // dwarven_chain_mail_shirt
		new BuySellList(399, 10, 0.000000, 0), // rind_leather_mail
		new BuySellList(400, 10, 0.000000, 0), // theca_leather_mail
		new BuySellList(9577, 10, 0.000000, 0), // tshirt
		new BuySellList(9578, 10, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 10, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 10, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12014, 10, 0.000000, 0), // puma_skin_gaiters_low
		new BuySellList(12008, 10, 0.000000, 0), // ring_breastplate_low
		new BuySellList(12015, 10, 0.000000, 0), // puma_skin_shirt_low
		new BuySellList(12012, 10, 0.000000, 0), // iron_plate_gaiters_low
		new BuySellList(12020, 10, 0.000000, 0), // lion_skin_gaiters_low
		new BuySellList(12021, 10, 0.000000, 0), // lion_skin_shirt_low
		new BuySellList(12024, 10, 0.000000, 0), // scale_mail_low
		new BuySellList(12032, 10, 0.000000, 0), // compound_scale_mail_low
		new BuySellList(12030, 10, 0.000000, 0), // dwarven_scale_mail_low
		new BuySellList(12031, 10, 0.000000, 0), // mithril_breastplate_low
		new BuySellList(12041, 10, 0.000000, 0), // mithril_gaiters_low
		new BuySellList(12034, 10, 0.000000, 0), // reinforce_leather_gaiters_low
		new BuySellList(12023, 10, 0.000000, 0), // scale_gaiters_low
		new BuySellList(12044, 10, 0.000000, 0), // blast_plate_low
		new BuySellList(12049, 10, 0.000000, 0), // compound_scale_gaiters_low
		new BuySellList(12039, 10, 0.000000, 0), // dwarven_scale_gaiters_low
		new BuySellList(12043, 10, 0.000000, 0), // mithril_banded_mail_low
		new BuySellList(12042, 10, 0.000000, 0), // mithril_banded_gaiters_low
		new BuySellList(12036, 10, 0.000000, 0), // reinforce_leather_shirt_low
		new BuySellList(12060, 10, 0.000000, 0), // brigandine_gaiters_low
		new BuySellList(12059, 10, 0.000000, 0), // brigandine_low
		new BuySellList(12056, 10, 0.000000, 0), // mithril_scale_gaiters_low
		new BuySellList(12054, 10, 0.000000, 0), // manticor_skin_shirt_low
		new BuySellList(12052, 10, 0.000000, 0), // manticor_skin_gaiters_low
		new BuySellList(12081, 10, 0.000000, 0), // half_plate_low
		new BuySellList(12080, 10, 0.000000, 0), // plate_gaiters_low
		new BuySellList(12074, 10, 0.000000, 0), // slamander_skin_mail_low
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
		new BuySellList(12037, 10, 0.000000, 0), // reinforce_leather_gloves_low
		new BuySellList(12055, 10, 0.000000, 0), // manticor_skin_gloves_low
		new BuySellList(12061, 10, 0.000000, 0), // brigandine_gauntlet_low
		new BuySellList(12072, 10, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 10, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12006, 10, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 10, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 10, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 10, 0.000000, 0), // boots_of_power_low
		new BuySellList(12035, 10, 0.000000, 0), // reinforce_leather_boots_low
		new BuySellList(12053, 10, 0.000000, 0), // manticor_skin_boots_low
		new BuySellList(12062, 10, 0.000000, 0), // brigandine_boots_low
		new BuySellList(12071, 10, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 10, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 10, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 10, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 10, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 10, 0.000000, 0), // helmet_low
		new BuySellList(12064, 10, 0.000000, 0), // brigandine_helmet_low
		new BuySellList(12079, 10, 0.000000, 0), // plate_helmet_low
		new BuySellList(12085, 10, 0.000000, 0), // mithril_boots_low
		new BuySellList(12095, 10, 0.000000, 0), // chain_mail_shirt_low
		new BuySellList(12094, 10, 0.000000, 0), // chain_gaiters_low
		new BuySellList(12086, 10, 0.000000, 0), // tempered_mithril_shirt_low
		new BuySellList(12091, 10, 0.000000, 0), // eldarake_low
		new BuySellList(12084, 10, 0.000000, 0), // tempered_mithril_gaiters_low
		new BuySellList(12093, 10, 0.000000, 0), // chain_boots_low
		new BuySellList(12101, 10, 0.000000, 0), // plate_leather_boots_low
		new BuySellList(12088, 10, 0.000000, 0), // dwarven_chain_boots_low
		new BuySellList(12089, 10, 0.000000, 0), // boots_of_seal_low
		new BuySellList(12087, 10, 0.000000, 0), // reinforce_mithril_gloves_low
		new BuySellList(12092, 10, 0.000000, 0), // chain_gloves_low
		new BuySellList(12096, 10, 0.000000, 0), // chain_shield_low
		new BuySellList(12128, 10, 0.000000, 0), // composite_armor_low
		new BuySellList(12144, 10, 0.000000, 0), // full_plate_armor_low
		new BuySellList(12135, 10, 0.000000, 0), // drake_leather_mail_low
		new BuySellList(12107, 10, 0.000000, 0), // knight_shield_low
		new BuySellList(12129, 10, 0.000000, 0), // tower_shield_low
		new BuySellList(12126, 10, 0.000000, 0), // composite_shield_low
		new BuySellList(12143, 10, 0.000000, 0), // full_plate_shield_low
		new BuySellList(12109, 10, 0.000000, 0), // dwarven_chain_gaiters_low
		new BuySellList(12111, 10, 0.000000, 0), // rind_leather_gaiters_low
		new BuySellList(12130, 10, 0.000000, 0), // theca_leather_gaiters_low
		new BuySellList(12106, 10, 0.000000, 0), // great_helmet_low
		new BuySellList(12127, 10, 0.000000, 0), // composite_helmet_low
		new BuySellList(12120, 10, 0.000000, 0), // shining_circlet_low
		new BuySellList(12145, 10, 0.000000, 0), // full_plate_helmet_low
		new BuySellList(12108, 10, 0.000000, 0), // dwarven_chain_gloves_low
		new BuySellList(12116, 10, 0.000000, 0), // gloves_of_seal_low
		new BuySellList(12112, 10, 0.000000, 0), // rind_leather_gloves_low
		new BuySellList(12119, 10, 0.000000, 0), // mithril_gauntlet_low
		new BuySellList(12131, 10, 0.000000, 0), // theca_leather_gloves_low
		new BuySellList(12134, 10, 0.000000, 0), // drake_leather_gloves_low
		new BuySellList(12141, 10, 0.000000, 0), // full_plate_gauntlet_low
		new BuySellList(12118, 10, 0.000000, 0), // crimson_boots_low
		new BuySellList(12114, 10, 0.000000, 0), // rind_leather_boots_low
		new BuySellList(12125, 10, 0.000000, 0), // composite_boots_low
		new BuySellList(12132, 10, 0.000000, 0), // theca_leather_boots_low
		new BuySellList(12136, 10, 0.000000, 0), // drake_leather_boots_low
		new BuySellList(12142, 10, 0.000000, 0), // full_plate_boots_low
		new BuySellList(12110, 10, 0.000000, 0), // dwarven_chain_mail_shirt_low
		new BuySellList(12113, 10, 0.000000, 0), // rind_leather_mail_low
		new BuySellList(12133, 10, 0.000000, 0) // theca_leather_mail_low
	};
	
	private static final int npcId = 30088;
	
	public Radia() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "radia001.htm";
		super.fnYouAreChaotic = "radia006.htm";
	}
}