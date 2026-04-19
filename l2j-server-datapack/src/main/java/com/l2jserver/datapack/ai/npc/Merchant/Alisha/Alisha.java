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
package com.l2jserver.datapack.ai.npc.Merchant.Alisha;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Alisha extends Merchant {
	
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
		new BuySellList(2449, 20, 0.000000, 0), // brigandine_gauntlet
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
		new BuySellList(62, 20, 0.000000, 0), // mithril_boots
		new BuySellList(354, 20, 0.000000, 0), // chain_mail_shirt
		new BuySellList(381, 20, 0.000000, 0), // chain_gaiters
		new BuySellList(397, 20, 0.000000, 0), // tempered_mithril_shirt
		new BuySellList(631, 20, 0.000000, 0), // eldarake
		new BuySellList(2387, 20, 0.000000, 0), // tempered_mithril_gaiters
		new BuySellList(2429, 20, 0.000000, 0), // chain_boots
		new BuySellList(2431, 20, 0.000000, 0), // plate_leather_boots
		new BuySellList(2432, 20, 0.000000, 0), // dwarven_chain_boots
		new BuySellList(2433, 20, 0.000000, 0), // boots_of_seal
		new BuySellList(2452, 20, 0.000000, 0), // reinforce_mithril_gloves
		new BuySellList(2453, 20, 0.000000, 0), // chain_gloves
		new BuySellList(2495, 20, 0.000000, 0), // chain_shield
		new BuySellList(398, 20, 0.000000, 0), // plate_leather
		new BuySellList(418, 20, 0.000000, 0), // plate_leather_gaiters
		new BuySellList(2455, 20, 0.000000, 0), // plate_leather_gloves
		new BuySellList(2496, 20, 0.000000, 0), // dwarven_chain_shield
		new BuySellList(60, 20, 0.000000, 0), // composite_armor
		new BuySellList(356, 20, 0.000000, 0), // full_plate_armor
		new BuySellList(401, 20, 0.000000, 0), // drake_leather_mail
		new BuySellList(632, 20, 0.000000, 0), // knight_shield
		new BuySellList(103, 20, 0.000000, 0), // tower_shield
		new BuySellList(107, 20, 0.000000, 0), // composite_shield
		new BuySellList(2497, 20, 0.000000, 0), // full_plate_shield
		new BuySellList(382, 20, 0.000000, 0), // dwarven_chain_gaiters
		new BuySellList(419, 20, 0.000000, 0), // rind_leather_gaiters
		new BuySellList(420, 20, 0.000000, 0), // theca_leather_gaiters
		new BuySellList(500, 20, 0.000000, 0), // great_helmet
		new BuySellList(517, 20, 0.000000, 0), // composite_helmet
		new BuySellList(1149, 20, 0.000000, 0), // shining_circlet
		new BuySellList(2414, 20, 0.000000, 0), // full_plate_helmet
		new BuySellList(2456, 20, 0.000000, 0), // dwarven_chain_gloves
		new BuySellList(2457, 20, 0.000000, 0), // gloves_of_seal
		new BuySellList(2458, 20, 0.000000, 0), // rind_leather_gloves
		new BuySellList(608, 20, 0.000000, 0), // mithril_gauntlet
		new BuySellList(2460, 20, 0.000000, 0), // theca_leather_gloves
		new BuySellList(2461, 20, 0.000000, 0), // drake_leather_gloves
		new BuySellList(2462, 20, 0.000000, 0), // full_plate_gauntlet
		new BuySellList(1126, 20, 0.000000, 0), // crimson_boots
		new BuySellList(2434, 20, 0.000000, 0), // rind_leather_boots
		new BuySellList(64, 20, 0.000000, 0), // composite_boots
		new BuySellList(2436, 20, 0.000000, 0), // theca_leather_boots
		new BuySellList(2437, 20, 0.000000, 0), // drake_leather_boots
		new BuySellList(2438, 20, 0.000000, 0), // full_plate_boots
		new BuySellList(355, 20, 0.000000, 0), // dwarven_chain_mail_shirt
		new BuySellList(399, 20, 0.000000, 0), // rind_leather_mail
		new BuySellList(400, 20, 0.000000, 0), // theca_leather_mail
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
		new BuySellList(2459, 20, 0.000000, 0), // demon's_gloves
		new BuySellList(2463, 20, 0.000000, 0), // blessed_gloves
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
		new BuySellList(2435, 20, 0.000000, 0), // demon's_boots
		new BuySellList(44, 20, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 20, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 20, 0.000000, 0), // bone_helmet
		new BuySellList(46, 20, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 20, 0.000000, 0), // helmet
		new BuySellList(2411, 20, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 20, 0.000000, 0), // plate_helmet
		new BuySellList(439, 20, 0.000000, 0), // karmian_tunic
		new BuySellList(471, 20, 0.000000, 0), // karmian_hose
		new BuySellList(2430, 20, 0.000000, 0), // karmian_boots
		new BuySellList(2454, 20, 0.000000, 0), // karmian_gloves
		new BuySellList(440, 20, 0.000000, 0), // robe_of_seal
		new BuySellList(472, 20, 0.000000, 0), // demon's_hose
		new BuySellList(473, 20, 0.000000, 0), // divine_hose
		new BuySellList(441, 20, 0.000000, 0), // demon's_tunic
		new BuySellList(442, 20, 0.000000, 0), // divine_tunic
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
		new BuySellList(12063, 20, 0.000000, 0), // brigandine_shield_low
		new BuySellList(12073, 20, 0.000000, 0), // square_shield_low
		new BuySellList(12078, 20, 0.000000, 0), // plate_shield_low
		new BuySellList(12007, 20, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 20, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12038, 20, 0.000000, 0), // gauntlet_low
		new BuySellList(12068, 20, 0.000000, 0), // gauntlet_of_repose_of_the_soul_low
		new BuySellList(12037, 20, 0.000000, 0), // reinforce_leather_gloves_low
		new BuySellList(12055, 20, 0.000000, 0), // manticor_skin_gloves_low
		new BuySellList(12061, 20, 0.000000, 0), // brigandine_gauntlet_low
		new BuySellList(12072, 20, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 20, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12006, 20, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 20, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 20, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 20, 0.000000, 0), // boots_of_power_low
		new BuySellList(12035, 20, 0.000000, 0), // reinforce_leather_boots_low
		new BuySellList(12053, 20, 0.000000, 0), // manticor_skin_boots_low
		new BuySellList(12062, 20, 0.000000, 0), // brigandine_boots_low
		new BuySellList(12071, 20, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 20, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 20, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 20, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 20, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 20, 0.000000, 0), // helmet_low
		new BuySellList(12064, 20, 0.000000, 0), // brigandine_helmet_low
		new BuySellList(12079, 20, 0.000000, 0), // plate_helmet_low
		new BuySellList(12085, 20, 0.000000, 0), // mithril_boots_low
		new BuySellList(12095, 20, 0.000000, 0), // chain_mail_shirt_low
		new BuySellList(12094, 20, 0.000000, 0), // chain_gaiters_low
		new BuySellList(12086, 20, 0.000000, 0), // tempered_mithril_shirt_low
		new BuySellList(12091, 20, 0.000000, 0), // eldarake_low
		new BuySellList(12084, 20, 0.000000, 0), // tempered_mithril_gaiters_low
		new BuySellList(12093, 20, 0.000000, 0), // chain_boots_low
		new BuySellList(12101, 20, 0.000000, 0), // plate_leather_boots_low
		new BuySellList(12088, 20, 0.000000, 0), // dwarven_chain_boots_low
		new BuySellList(12089, 20, 0.000000, 0), // boots_of_seal_low
		new BuySellList(12087, 20, 0.000000, 0), // reinforce_mithril_gloves_low
		new BuySellList(12092, 20, 0.000000, 0), // chain_gloves_low
		new BuySellList(12096, 20, 0.000000, 0), // chain_shield_low
		new BuySellList(12128, 20, 0.000000, 0), // composite_armor_low
		new BuySellList(12144, 20, 0.000000, 0), // full_plate_armor_low
		new BuySellList(12135, 20, 0.000000, 0), // drake_leather_mail_low
		new BuySellList(12107, 20, 0.000000, 0), // knight_shield_low
		new BuySellList(12129, 20, 0.000000, 0), // tower_shield_low
		new BuySellList(12126, 20, 0.000000, 0), // composite_shield_low
		new BuySellList(12143, 20, 0.000000, 0), // full_plate_shield_low
		new BuySellList(12109, 20, 0.000000, 0), // dwarven_chain_gaiters_low
		new BuySellList(12111, 20, 0.000000, 0), // rind_leather_gaiters_low
		new BuySellList(12130, 20, 0.000000, 0), // theca_leather_gaiters_low
		new BuySellList(12106, 20, 0.000000, 0), // great_helmet_low
		new BuySellList(12127, 20, 0.000000, 0), // composite_helmet_low
		new BuySellList(12120, 20, 0.000000, 0), // shining_circlet_low
		new BuySellList(12145, 20, 0.000000, 0), // full_plate_helmet_low
		new BuySellList(12108, 20, 0.000000, 0), // dwarven_chain_gloves_low
		new BuySellList(12116, 20, 0.000000, 0), // gloves_of_seal_low
		new BuySellList(12112, 20, 0.000000, 0), // rind_leather_gloves_low
		new BuySellList(12119, 20, 0.000000, 0), // mithril_gauntlet_low
		new BuySellList(12131, 20, 0.000000, 0), // theca_leather_gloves_low
		new BuySellList(12134, 20, 0.000000, 0), // drake_leather_gloves_low
		new BuySellList(12141, 20, 0.000000, 0), // full_plate_gauntlet_low
		new BuySellList(12118, 20, 0.000000, 0), // crimson_boots_low
		new BuySellList(12114, 20, 0.000000, 0), // rind_leather_boots_low
		new BuySellList(12125, 20, 0.000000, 0), // composite_boots_low
		new BuySellList(12132, 20, 0.000000, 0), // theca_leather_boots_low
		new BuySellList(12136, 20, 0.000000, 0), // drake_leather_boots_low
		new BuySellList(12142, 20, 0.000000, 0), // full_plate_boots_low
		new BuySellList(12110, 20, 0.000000, 0), // dwarven_chain_mail_shirt_low
		new BuySellList(12113, 20, 0.000000, 0), // rind_leather_mail_low
		new BuySellList(12133, 20, 0.000000, 0) // theca_leather_mail_low
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
		new BuySellList(12122, 20, 0.000000, 0), // demon's_gloves_low
		new BuySellList(12137, 20, 0.000000, 0), // blessed_gloves_low
		new BuySellList(12071, 20, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 20, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 20, 0.000000, 0), // plate_boots_low
		new BuySellList(12121, 20, 0.000000, 0), // demon's_boots_low
		new BuySellList(12009, 20, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 20, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 20, 0.000000, 0), // helmet_low
		new BuySellList(12064, 20, 0.000000, 0), // brigandine_helmet_low
		new BuySellList(12079, 20, 0.000000, 0), // plate_helmet_low
		new BuySellList(12099, 20, 0.000000, 0), // karmian_tunic_low
		new BuySellList(12100, 20, 0.000000, 0), // karmian_hose_low
		new BuySellList(12098, 20, 0.000000, 0), // karmian_boots_low
		new BuySellList(12097, 20, 0.000000, 0), // karmian_gloves_low
		new BuySellList(12115, 20, 0.000000, 0), // robe_of_seal_low
		new BuySellList(12124, 20, 0.000000, 0), // demon's_hose_low
		new BuySellList(12139, 20, 0.000000, 0), // divine_hose_low
		new BuySellList(12123, 20, 0.000000, 0), // demon's_tunic_low
		new BuySellList(12138, 20, 0.000000, 0) // divine_tunic_low
	};
	
	private static final int npcId = 31303;
	
	public Alisha() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "trader_alisha001.htm";
		super.fnYouAreChaotic = "trader_alisha006.htm";
	}
}