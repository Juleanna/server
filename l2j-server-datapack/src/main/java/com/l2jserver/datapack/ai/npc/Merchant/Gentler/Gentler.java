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
package com.l2jserver.datapack.ai.npc.Merchant.Gentler;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Gentler extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1294, 10, 0.000000, 0), // sb_adv_defence_power1
		new BuySellList(1095, 10, 0.000000, 0), // sb_advanced_attack_power1
		new BuySellList(1048, 10, 0.000000, 0), // sb_might1
		new BuySellList(1050, 10, 0.000000, 0), // sb_battle_heal1
		new BuySellList(1051, 10, 0.000000, 0), // sb_vampiric_touch1
		new BuySellList(1049, 10, 0.000000, 0), // sb_ice_bolt1
		new BuySellList(1152, 10, 0.000000, 0), // sb_heal1
		new BuySellList(1054, 10, 0.000000, 0), // sb_group_heal1
		new BuySellList(1058, 10, 0.000000, 0), // sb_shield1
		new BuySellList(1099, 10, 0.000000, 0), // sb_breeze1
		new BuySellList(1098, 10, 0.000000, 0), // sb_wind_walk1
		new BuySellList(1056, 10, 0.000000, 0), // sb_curse:weakness
		new BuySellList(1055, 10, 0.000000, 0), // sb_curse:poison1
		new BuySellList(1053, 10, 0.000000, 0), // sb_cure_poison1
		new BuySellList(1052, 10, 0.000000, 0), // sb_flame_strike1
		new BuySellList(1097, 10, 0.000000, 0), // sb_drain_energy1
		new BuySellList(1096, 10, 0.000000, 0), // sb_elemental_heal1
		new BuySellList(1386, 10, 0.000000, 0), // sb_disrupt_undead1
		new BuySellList(1514, 10, 0.000000, 0), // sb_resurrection1
		new BuySellList(1372, 10, 0.000000, 0), // sb_blaze1
		new BuySellList(1667, 10, 0.000000, 0), // sb_summon_shadow1
		new BuySellList(1671, 10, 0.000000, 0), // sb_summon_silhouette1
		new BuySellList(1669, 10, 0.000000, 0), // sb_summon_unicorn_boxer1
		new BuySellList(1403, 10, 0.000000, 0), // sb_summon_blackcat1
		new BuySellList(1405, 10, 0.000000, 0), // sb_servitor_heal1
		new BuySellList(1370, 10, 0.000000, 0), // sb_aqua_swirl1
		new BuySellList(1401, 10, 0.000000, 0), // sb_arcane_acumen1
		new BuySellList(4916, 10, 0.000000, 0), // sb_energy_bolt1
		new BuySellList(1411, 10, 0.000000, 0), // sb_aura_burn1
		new BuySellList(1513, 10, 0.000000, 0), // sb_charm11
		new BuySellList(1399, 10, 0.000000, 0), // sb_concentration1
		new BuySellList(1515, 10, 0.000000, 0), // sb_water_breathing
		new BuySellList(1371, 10, 0.000000, 0), // sb_twister1
		new BuySellList(1383, 10, 0.000000, 0), // sb_poison1
		new BuySellList(1377, 10, 0.000000, 0), // sb_poison_recovery1
		new BuySellList(1512, 10, 0.000000, 0), // sb_confusion1
		new BuySellList(1379, 10, 0.000000, 0), // sb_cure_bleeding1
		new BuySellList(1415, 10, 0.000000, 0), // sb_dryad_root1
		new BuySellList(1388, 10, 0.000000, 0), // sb_mental_shield1
		new BuySellList(1517, 10, 0.000000, 0), // sb_body_to_mind1
		new BuySellList(4908, 10, 0.000000, 0), // sb_shadow_spark1
		new BuySellList(1417, 10, 0.000000, 0), // sb_surrender_to_earth1
		new BuySellList(1400, 10, 0.000000, 0), // sb_surrender_to_fire1
		new BuySellList(1418, 10, 0.000000, 0), // sb_surrender_to_poison1
		new BuySellList(1668, 10, 0.000000, 0), // sb_summon_cuti_cat1
		new BuySellList(1670, 10, 0.000000, 0), // sb_summon_unicorn_mirage1
		new BuySellList(1404, 10, 0.000000, 0), // sb_servitor_mana_charge1
		new BuySellList(4906, 10, 0.000000, 0), // sb_solar_spark1
		new BuySellList(1402, 10, 0.000000, 0), // sb_agility1
		new BuySellList(1391, 10, 0.000000, 0), // sb_empower1
		new BuySellList(1410, 10, 0.000000, 0), // sb_poison_cloud1
		new BuySellList(1398, 10, 0.000000, 0), // sb_focus1
		new BuySellList(1389, 10, 0.000000, 0), // sb_holy_weapon1
		new BuySellList(1378, 10, 0.000000, 0), // sb_touch_of_god1
		new BuySellList(1414, 10, 0.000000, 0), // sb_fire_resist1
		new BuySellList(1385, 10, 0.000000, 0), // sb_recharge1
		new BuySellList(4910, 10, 0.000000, 0), // sb_vampiric_rage1
		new BuySellList(1394, 10, 0.000000, 0), // sb_sleep1
		new BuySellList(1516, 10, 0.000000, 0), // sb_corpse_life_drain1
		new BuySellList(3944, 10, 0.000000, 0), // sb_decrease_weight1
		new BuySellList(1412, 10, 0.000000, 0), // sb_auqa_resist1
		new BuySellList(1413, 10, 0.000000, 0), // sb_wind_resist1
		new BuySellList(1387, 10, 0.000000, 0), // sb_resist_poison1
		new BuySellList(1390, 10, 0.000000, 0), // sb_regeneration1
		new BuySellList(1408, 10, 0.000000, 0), // sb_mighty_servitor1
		new BuySellList(1392, 10, 0.000000, 0), // sb_berserker_spirit1
		new BuySellList(1407, 10, 0.000000, 0), // sb_bright_servitor1
		new BuySellList(1409, 10, 0.000000, 0), // sb_slow1
		new BuySellList(1416, 10, 0.000000, 0), // sb_curse_bleary1
		new BuySellList(1406, 10, 0.000000, 0), // sb_fast_servitor1
		new BuySellList(1397, 10, 0.000000, 0), // sb_erase_hostility1
		new BuySellList(1384, 10, 0.000000, 0), // sb_speed_walk1
		new BuySellList(1380, 10, 0.000000, 0), // sb_zero_g1
		new BuySellList(1382, 10, 0.000000, 0), // sb_power_break1
		new BuySellList(1381, 10, 0.000000, 0), // sb_freezing_strike1
		new BuySellList(1529, 10, 0.000000, 0), // sb_night_murmur1
		new BuySellList(1525, 10, 0.000000, 0), // sb_blood_lust1
		new BuySellList(1527, 10, 0.000000, 0), // sb_pain_thorn1
		new BuySellList(1524, 10, 0.000000, 0), // sb_devotioin_of_shine1
		new BuySellList(1531, 10, 0.000000, 0), // sb_chill_flame1
		new BuySellList(1522, 10, 0.000000, 0), // sb_mass_frenzy1
		new BuySellList(1526, 10, 0.000000, 0), // sb_external_fear1
		new BuySellList(1534, 10, 0.000000, 0), // sb_entice_madness1
		new BuySellList(1537, 10, 0.000000, 0), // sb_pain_edge1
		new BuySellList(1856, 10, 0.000000, 0), // sb_inspire_life_force1
		new BuySellList(10025, 10, 0.000000, 0), // sb_fallen_arrow1
		new BuySellList(10026, 10, 0.000000, 0), // sb_fallen_attack1
		new BuySellList(10073, 10, 0.000000, 0), // sb_rapid_attack1
		new BuySellList(10072, 10, 0.000000, 0), // sb_increase_power1
		new BuySellList(10027, 10, 0.000000, 0), // sb_detect_trap1
		new BuySellList(10028, 10, 0.000000, 0), // sb_defuse_trap1
		new BuySellList(10029, 10, 0.000000, 0), // sb_dark_strike1
		new BuySellList(10031, 10, 0.000000, 0), // sb_double_thrust1
		new BuySellList(10032, 10, 0.000000, 0), // sb_abyssal_blaze1
		new BuySellList(10180, 10, 0.000000, 0), // sb_penetrate_short1
		new BuySellList(10181, 10, 0.000000, 0), // sb_erase_mark1
		new BuySellList(10070, 10, 0.000000, 0), // sb_change_weapon1
		new BuySellList(10074, 10, 0.000000, 0), // sb_furious_soul1
		new BuySellList(10033, 10, 0.000000, 0), // sb_dark_explosion1
		new BuySellList(10078, 10, 0.000000, 0), // sb_death_mark1
		new BuySellList(10058, 10, 0.000000, 0), // sb_fire_trap1
		new BuySellList(10077, 10, 0.000000, 0), // sb_fast_shot1
		new BuySellList(10085, 10, 0.000000, 0), // sb_surrender_to_unholy1
		new BuySellList(10094, 10, 0.000000, 0), // sb_rush1
		new BuySellList(10275, 10, 0.000000, 0), // sb_warf1
		new BuySellList(10276, 10, 0.000000, 0), // sb_soul_shock1
		new BuySellList(10075, 10, 0.000000, 0), // sb_sword_shield1
		new BuySellList(10079, 10, 0.000000, 0), // sb_courage1
		new BuySellList(10076, 10, 0.000000, 0) // sb_disarm1
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(176, 10, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 10, 0.000000, 0), // relic_of_saints
		new BuySellList(177, 10, 0.000000, 0), // mage_staff
		new BuySellList(311, 10, 0.000000, 0), // crucifix_of_blessing
		new BuySellList(100, 10, 0.000000, 0), // voodoo_doll
		new BuySellList(178, 10, 0.000000, 0), // bone_staff
		new BuySellList(101, 10, 0.000000, 0), // scroll_of_wisdom
		new BuySellList(312, 10, 0.000000, 0), // branch_of_life
		new BuySellList(314, 10, 0.000000, 0), // proof_of_revenge
		new BuySellList(179, 10, 0.000000, 0), // mace_of_prayer
		new BuySellList(182, 10, 0.000000, 0), // doom_hammer
		new BuySellList(183, 10, 0.000000, 0), // mystic_staff
		new BuySellList(185, 10, 0.000000, 0), // staff_of_mana
		new BuySellList(315, 10, 0.000000, 0), // divine_tome
		new BuySellList(83, 10, 0.000000, 0), // sword_of_magic
		new BuySellList(143, 10, 0.000000, 0), // sword_of_mystic
		new BuySellList(144, 10, 0.000000, 0), // sword_of_occult
		new BuySellList(238, 10, 0.000000, 0), // dagger_of_mana
		new BuySellList(239, 10, 0.000000, 0), // mystic_knife
		new BuySellList(240, 10, 0.000000, 0), // conjure_knife
		new BuySellList(241, 10, 0.000000, 0), // knife_o'_silenus
		new BuySellList(186, 10, 0.000000, 0), // staff_of_magicpower
		new BuySellList(316, 10, 0.000000, 0), // blood_of_saints
		new BuySellList(317, 10, 0.000000, 0), // tome_of_blood
		new BuySellList(90, 10, 0.000000, 0), // goathead_staff
		new BuySellList(318, 10, 0.000000, 0), // crucifix_of_blood
		new BuySellList(321, 10, 0.000000, 0), // demon_fangs
		new BuySellList(187, 10, 0.000000, 0), // atuba_hammer
		new BuySellList(188, 10, 0.000000, 0), // ghost_staff
		new BuySellList(189, 10, 0.000000, 0), // life_stick
		new BuySellList(190, 10, 0.000000, 0), // atuba_mace
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
		new BuySellList(44, 10, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 10, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 10, 0.000000, 0), // bone_helmet
		new BuySellList(46, 10, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 10, 0.000000, 0), // helmet
		new BuySellList(2411, 10, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 10, 0.000000, 0), // plate_helmet
		new BuySellList(9577, 10, 0.000000, 0), // tshirt
		new BuySellList(9578, 10, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 10, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 10, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(11610, 10, 0.000000, 0), // bone_staff_low
		new BuySellList(11619, 10, 0.000000, 0), // scroll_of_wisdom_low
		new BuySellList(11612, 10, 0.000000, 0), // branch_of_life_low
		new BuySellList(11609, 10, 0.000000, 0), // proof_of_revenge_low
		new BuySellList(11627, 10, 0.000000, 0), // mace_of_prayer_low
		new BuySellList(11630, 10, 0.000000, 0), // doom_hammer_low
		new BuySellList(11634, 10, 0.000000, 0), // mystic_staff_low
		new BuySellList(11633, 10, 0.000000, 0), // staff_of_mana_low
		new BuySellList(11637, 10, 0.000000, 0), // divine_tome_low
		new BuySellList(11655, 10, 0.000000, 0), // sword_of_magic_low
		new BuySellList(11656, 10, 0.000000, 0), // sword_of_mystic_low
		new BuySellList(11657, 10, 0.000000, 0), // sword_of_occult_low
		new BuySellList(11662, 10, 0.000000, 0), // dagger_of_mana_low
		new BuySellList(11664, 10, 0.000000, 0), // mystic_knife_low
		new BuySellList(11672, 10, 0.000000, 0), // conjure_knife_low
		new BuySellList(11660, 10, 0.000000, 0), // knife_o'_silenus_low
		new BuySellList(11663, 10, 0.000000, 0), // staff_of_magicpower_low
		new BuySellList(11678, 10, 0.000000, 0), // blood_of_saints_low
		new BuySellList(11676, 10, 0.000000, 0), // tome_of_blood_low
		new BuySellList(11693, 10, 0.000000, 0), // goathead_staff_low
		new BuySellList(11709, 10, 0.000000, 0), // crucifix_of_blood_low
		new BuySellList(11715, 10, 0.000000, 0), // demon_fangs_low
		new BuySellList(11732, 10, 0.000000, 0), // atuba_hammer_low
		new BuySellList(11737, 10, 0.000000, 0), // ghost_staff_low
		new BuySellList(11729, 10, 0.000000, 0), // life_stick_low
		new BuySellList(11731, 10, 0.000000, 0), // atuba_mace_low
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
		new BuySellList(12037, 10, 0.000000, 0), // reinforce_leather_gloves_low
		new BuySellList(12055, 10, 0.000000, 0), // manticor_skin_gloves_low
		new BuySellList(12061, 10, 0.000000, 0), // brigandine_gauntlet_low
		new BuySellList(12072, 10, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 10, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12082, 10, 0.000000, 0), // sage's_worn_gloves_low
		new BuySellList(12006, 10, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 10, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 10, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 10, 0.000000, 0), // boots_of_power_low
		new BuySellList(12035, 10, 0.000000, 0), // reinforce_leather_boots_low
		new BuySellList(12045, 10, 0.000000, 0), // boots_of_knowledge_low
		new BuySellList(12053, 10, 0.000000, 0), // manticor_skin_boots_low
		new BuySellList(12062, 10, 0.000000, 0), // brigandine_boots_low
		new BuySellList(12071, 10, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 10, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 10, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 10, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 10, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 10, 0.000000, 0), // helmet_low
		new BuySellList(12064, 10, 0.000000, 0), // brigandine_helmet_low
		new BuySellList(12079, 10, 0.000000, 0) // plate_helmet_low
	};
	
	private static final int npcId = 30094;
	
	public Gentler() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "gentler001.htm";
		super.fnYouAreChaotic = "gentler006.htm";
	}
}