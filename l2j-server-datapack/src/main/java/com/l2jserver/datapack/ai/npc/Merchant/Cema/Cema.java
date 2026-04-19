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
package com.l2jserver.datapack.ai.npc.Merchant.Cema;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForChaotic;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Cema extends MerchantForChaotic {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(176, 50, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 50, 0.000000, 0), // relic_of_saints
		new BuySellList(177, 50, 0.000000, 0), // mage_staff
		new BuySellList(311, 50, 0.000000, 0), // crucifix_of_blessing
		new BuySellList(100, 50, 0.000000, 0), // voodoo_doll
		new BuySellList(178, 50, 0.000000, 0), // bone_staff
		new BuySellList(101, 50, 0.000000, 0), // scroll_of_wisdom
		new BuySellList(312, 50, 0.000000, 0), // branch_of_life
		new BuySellList(314, 50, 0.000000, 0), // proof_of_revenge
		new BuySellList(179, 50, 0.000000, 0), // mace_of_prayer
		new BuySellList(182, 50, 0.000000, 0), // doom_hammer
		new BuySellList(183, 50, 0.000000, 0), // mystic_staff
		new BuySellList(185, 50, 0.000000, 0), // staff_of_mana
		new BuySellList(315, 50, 0.000000, 0), // divine_tome
		new BuySellList(83, 50, 0.000000, 0), // sword_of_magic
		new BuySellList(143, 50, 0.000000, 0), // sword_of_mystic
		new BuySellList(144, 50, 0.000000, 0), // sword_of_occult
		new BuySellList(238, 50, 0.000000, 0), // dagger_of_mana
		new BuySellList(239, 50, 0.000000, 0), // mystic_knife
		new BuySellList(240, 50, 0.000000, 0), // conjure_knife
		new BuySellList(241, 50, 0.000000, 0), // knife_o'_silenus
		new BuySellList(186, 50, 0.000000, 0), // staff_of_magicpower
		new BuySellList(316, 50, 0.000000, 0), // blood_of_saints
		new BuySellList(317, 50, 0.000000, 0), // tome_of_blood
		new BuySellList(90, 50, 0.000000, 0), // goathead_staff
		new BuySellList(318, 50, 0.000000, 0), // crucifix_of_blood
		new BuySellList(321, 50, 0.000000, 0), // demon_fangs
		new BuySellList(187, 50, 0.000000, 0), // atuba_hammer
		new BuySellList(188, 50, 0.000000, 0), // ghost_staff
		new BuySellList(189, 50, 0.000000, 0), // life_stick
		new BuySellList(190, 50, 0.000000, 0), // atuba_mace
		new BuySellList(1104, 50, 0.000000, 0), // hose_of_devotion
		new BuySellList(1101, 50, 0.000000, 0), // tunic_of_devotion
		new BuySellList(1105, 50, 0.000000, 0), // hose_of_magicpower
		new BuySellList(1102, 50, 0.000000, 0), // tunic_of_magicpower
		new BuySellList(465, 50, 0.000000, 0), // cursed_hose
		new BuySellList(432, 50, 0.000000, 0), // cursed_tunic
		new BuySellList(467, 50, 0.000000, 0), // dark_hose
		new BuySellList(434, 50, 0.000000, 0), // white_tunic
		new BuySellList(468, 50, 0.000000, 0), // mage's_hose
		new BuySellList(435, 50, 0.000000, 0), // mage's_tunic
		new BuySellList(469, 50, 0.000000, 0), // hose_of_knowledge
		new BuySellList(436, 50, 0.000000, 0), // tunic_of_knowledge
		new BuySellList(470, 50, 0.000000, 0), // mithril_hose
		new BuySellList(437, 50, 0.000000, 0), // mithril_tunic
		new BuySellList(438, 50, 0.000000, 0), // sage's_rag
		new BuySellList(102, 50, 0.000000, 0), // round_shield
		new BuySellList(625, 50, 0.000000, 0), // bone_shield
		new BuySellList(626, 50, 0.000000, 0), // bronze_shield
		new BuySellList(627, 50, 0.000000, 0), // aspis
		new BuySellList(628, 50, 0.000000, 0), // hoplon
		new BuySellList(629, 50, 0.000000, 0), // kite_shield
		new BuySellList(2493, 50, 0.000000, 0), // brigandine_shield
		new BuySellList(630, 50, 0.000000, 0), // square_shield
		new BuySellList(2494, 50, 0.000000, 0), // plate_shield
		new BuySellList(50, 50, 0.000000, 0), // leather_gloves
		new BuySellList(51, 50, 0.000000, 0), // bracer
		new BuySellList(604, 50, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 50, 0.000000, 0), // leather_gauntlet
		new BuySellList(63, 50, 0.000000, 0), // gauntlet
		new BuySellList(606, 50, 0.000000, 0), // gauntlet_of_repose_of_the_soul
		new BuySellList(2447, 50, 0.000000, 0), // gloves_of_knowledge
		new BuySellList(2450, 50, 0.000000, 0), // elven_mithril_gloves
		new BuySellList(61, 50, 0.000000, 0), // mithril_glove
		new BuySellList(607, 50, 0.000000, 0), // ogre_power_gauntlet
		new BuySellList(2451, 50, 0.000000, 0), // sage's_worn_gloves
		new BuySellList(38, 50, 0.000000, 0), // low_boots
		new BuySellList(39, 50, 0.000000, 0), // boots
		new BuySellList(40, 50, 0.000000, 0), // leather_boots
		new BuySellList(1123, 50, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(553, 50, 0.000000, 0), // iron_boots
		new BuySellList(1124, 50, 0.000000, 0), // boots_of_power
		new BuySellList(2423, 50, 0.000000, 0), // boots_of_knowledge
		new BuySellList(2426, 50, 0.000000, 0), // elven_mithril_boots
		new BuySellList(1125, 50, 0.000000, 0), // assault_boots
		new BuySellList(2427, 50, 0.000000, 0), // slamander_skin_boots
		new BuySellList(2428, 50, 0.000000, 0), // plate_boots
		new BuySellList(44, 50, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 50, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 50, 0.000000, 0), // bone_helmet
		new BuySellList(46, 50, 0.000000, 0), // bronze_helmet
		new BuySellList(47, 50, 0.000000, 0), // helmet
		new BuySellList(2411, 50, 0.000000, 0), // brigandine_helmet
		new BuySellList(2412, 50, 0.000000, 0), // plate_helmet
		new BuySellList(9577, 50, 0.000000, 0), // tshirt
		new BuySellList(9578, 50, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 50, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 50, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1835, 50, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 50, 0.000000, 0), // spiritshot_none
		new BuySellList(3947, 50, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(5146, 50, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 50, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 50, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 50, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 50, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 50, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 50, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 50, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 50, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1060, 50, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1061, 50, 0.000000, 0), // healing_potion
		new BuySellList(1831, 50, 0.000000, 0), // antidote
		new BuySellList(1832, 50, 0.000000, 0), // advanced_antidote
		new BuySellList(1833, 50, 0.000000, 0), // bandage
		new BuySellList(1834, 50, 0.000000, 0), // emergency_dressing
		new BuySellList(734, 50, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 50, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 50, 0.000000, 0), // potion_of_acumen2
		new BuySellList(6037, 50, 0.000000, 0), // scroll_of_awake
		new BuySellList(736, 50, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 50, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(1829, 50, 0.000000, 0), // scroll_of_escape_to_agit
		new BuySellList(1830, 50, 0.000000, 0), // scroll_of_escape_to_castle
		new BuySellList(5589, 50, 0.000000, 0), // energy_stone
		new BuySellList(1661, 50, 0.000000, 0), // key_of_thief
		new BuySellList(2508, 50, 0.000000, 0), // cursed_bone
		new BuySellList(9633, 50, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 50, 0.000000, 0), // fine_steel_bolt
		new BuySellList(10409, 50, 0.000000, 0), // blank_soul_bottle_5
		new BuySellList(4625, 50, 0.000000, 0), // dice_heart
		new BuySellList(4626, 50, 0.000000, 0), // dice_spade
		new BuySellList(4627, 50, 0.000000, 0), // dice_clover
		new BuySellList(4628, 50, 0.000000, 0), // dice_diamond
		new BuySellList(21746, 50, 0.000000, 0) // g_lucky_key
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(11610, 50, 0.000000, 0), // bone_staff_low
		new BuySellList(11619, 50, 0.000000, 0), // scroll_of_wisdom_low
		new BuySellList(11612, 50, 0.000000, 0), // branch_of_life_low
		new BuySellList(11609, 50, 0.000000, 0), // proof_of_revenge_low
		new BuySellList(11627, 50, 0.000000, 0), // mace_of_prayer_low
		new BuySellList(11630, 50, 0.000000, 0), // doom_hammer_low
		new BuySellList(11634, 50, 0.000000, 0), // mystic_staff_low
		new BuySellList(11633, 50, 0.000000, 0), // staff_of_mana_low
		new BuySellList(11637, 50, 0.000000, 0), // divine_tome_low
		new BuySellList(11655, 50, 0.000000, 0), // sword_of_magic_low
		new BuySellList(11656, 50, 0.000000, 0), // sword_of_mystic_low
		new BuySellList(11657, 50, 0.000000, 0), // sword_of_occult_low
		new BuySellList(11662, 50, 0.000000, 0), // dagger_of_mana_low
		new BuySellList(11664, 50, 0.000000, 0), // mystic_knife_low
		new BuySellList(11672, 50, 0.000000, 0), // conjure_knife_low
		new BuySellList(11660, 50, 0.000000, 0), // knife_o'_silenus_low
		new BuySellList(11663, 50, 0.000000, 0), // staff_of_magicpower_low
		new BuySellList(11678, 50, 0.000000, 0), // blood_of_saints_low
		new BuySellList(11676, 50, 0.000000, 0), // tome_of_blood_low
		new BuySellList(11693, 50, 0.000000, 0), // goathead_staff_low
		new BuySellList(11709, 50, 0.000000, 0), // crucifix_of_blood_low
		new BuySellList(11715, 50, 0.000000, 0), // demon_fangs_low
		new BuySellList(11732, 50, 0.000000, 0), // atuba_hammer_low
		new BuySellList(11737, 50, 0.000000, 0), // ghost_staff_low
		new BuySellList(11729, 50, 0.000000, 0), // life_stick_low
		new BuySellList(11731, 50, 0.000000, 0), // atuba_mace_low
		new BuySellList(12011, 50, 0.000000, 0), // cursed_hose_low
		new BuySellList(12010, 50, 0.000000, 0), // cursed_tunic_low
		new BuySellList(12017, 50, 0.000000, 0), // dark_hose_low
		new BuySellList(12022, 50, 0.000000, 0), // white_tunic_low
		new BuySellList(12019, 50, 0.000000, 0), // mage's_hose_low
		new BuySellList(12018, 50, 0.000000, 0), // mage's_tunic_low
		new BuySellList(12048, 50, 0.000000, 0), // hose_of_knowledge_low
		new BuySellList(12047, 50, 0.000000, 0), // tunic_of_knowledge_low
		new BuySellList(12058, 50, 0.000000, 0), // mithril_hose_low
		new BuySellList(12057, 50, 0.000000, 0), // mithril_tunic_low
		new BuySellList(12083, 50, 0.000000, 0), // sage's_rag_low
		new BuySellList(12013, 50, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 50, 0.000000, 0), // aspis_low
		new BuySellList(12051, 50, 0.000000, 0), // hoplon_low
		new BuySellList(12069, 50, 0.000000, 0), // kite_shield_low
		new BuySellList(12063, 50, 0.000000, 0), // brigandine_shield_low
		new BuySellList(12073, 50, 0.000000, 0), // square_shield_low
		new BuySellList(12078, 50, 0.000000, 0), // plate_shield_low
		new BuySellList(12007, 50, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 50, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12038, 50, 0.000000, 0), // gauntlet_low
		new BuySellList(12068, 50, 0.000000, 0), // gauntlet_of_repose_of_the_soul_low
		new BuySellList(12046, 50, 0.000000, 0), // gloves_of_knowledge_low
		new BuySellList(12037, 50, 0.000000, 0), // reinforce_leather_gloves_low
		new BuySellList(12055, 50, 0.000000, 0), // manticor_skin_gloves_low
		new BuySellList(12061, 50, 0.000000, 0), // brigandine_gauntlet_low
		new BuySellList(12072, 50, 0.000000, 0), // mithril_glove_low
		new BuySellList(12076, 50, 0.000000, 0), // ogre_power_gauntlet_low
		new BuySellList(12082, 50, 0.000000, 0), // sage's_worn_gloves_low
		new BuySellList(12006, 50, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 50, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12033, 50, 0.000000, 0), // iron_boots_low
		new BuySellList(12070, 50, 0.000000, 0), // boots_of_power_low
		new BuySellList(12035, 50, 0.000000, 0), // reinforce_leather_boots_low
		new BuySellList(12045, 50, 0.000000, 0), // boots_of_knowledge_low
		new BuySellList(12053, 50, 0.000000, 0), // manticor_skin_boots_low
		new BuySellList(12062, 50, 0.000000, 0), // brigandine_boots_low
		new BuySellList(12071, 50, 0.000000, 0), // assault_boots_low
		new BuySellList(12075, 50, 0.000000, 0), // slamander_skin_boots_low
		new BuySellList(12077, 50, 0.000000, 0), // plate_boots_low
		new BuySellList(12009, 50, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 50, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12050, 50, 0.000000, 0), // helmet_low
		new BuySellList(12064, 50, 0.000000, 0), // brigandine_helmet_low
		new BuySellList(12079, 50, 0.000000, 0) // plate_helmet_low
	};
	
	private static final int npcId = 30834;
	
	public Cema() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "magic_trader_cema001.htm";
	}
}