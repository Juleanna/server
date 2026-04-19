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
package com.l2jserver.datapack.ai.npc.Merchant.Zenkin;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Zenkin extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(412, 15, 0.000000, 0), // tights_pants
		new BuySellList(390, 15, 0.000000, 0), // tights_shirt
		new BuySellList(24, 15, 0.000000, 0), // bone_breastplate
		new BuySellList(31, 15, 0.000000, 0), // bone_gaiters
		new BuySellList(25, 15, 0.000000, 0), // piece_bone_breastplate
		new BuySellList(26, 15, 0.000000, 0), // bronze_breastplate
		new BuySellList(33, 15, 0.000000, 0), // hard_leather_gaiters
		new BuySellList(32, 15, 0.000000, 0), // piece_bone_gaiters
		new BuySellList(27, 15, 0.000000, 0), // hard_leather_shirt
		new BuySellList(34, 15, 0.000000, 0), // bronze_gaiters
		new BuySellList(413, 15, 0.000000, 0), // puma_skin_gaiters
		new BuySellList(347, 15, 0.000000, 0), // ring_breastplate
		new BuySellList(376, 15, 0.000000, 0), // iron_plate_gaiters
		new BuySellList(391, 15, 0.000000, 0), // puma_skin_shirt
		new BuySellList(414, 15, 0.000000, 0), // lion_skin_gaiters
		new BuySellList(392, 15, 0.000000, 0), // lion_skin_shirt
		new BuySellList(58, 15, 0.000000, 0), // mithril_breastplate
		new BuySellList(59, 15, 0.000000, 0), // mithril_gaiters
		new BuySellList(102, 15, 0.000000, 0), // round_shield
		new BuySellList(625, 15, 0.000000, 0), // bone_shield
		new BuySellList(626, 15, 0.000000, 0), // bronze_shield
		new BuySellList(627, 15, 0.000000, 0), // aspis
		new BuySellList(50, 15, 0.000000, 0), // leather_gloves
		new BuySellList(51, 15, 0.000000, 0), // bracer
		new BuySellList(604, 15, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 15, 0.000000, 0), // leather_gauntlet
		new BuySellList(38, 15, 0.000000, 0), // low_boots
		new BuySellList(39, 15, 0.000000, 0), // boots
		new BuySellList(40, 15, 0.000000, 0), // leather_boots
		new BuySellList(1123, 15, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(44, 15, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 15, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 15, 0.000000, 0), // bone_helmet
		new BuySellList(46, 15, 0.000000, 0), // bronze_helmet
		new BuySellList(415, 15, 0.000000, 0), // mithril_banded_gaiters
		new BuySellList(393, 15, 0.000000, 0), // mithril_banded_mail
		new BuySellList(9577, 15, 0.000000, 0), // tshirt
		new BuySellList(9578, 15, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 15, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 15, 0.000000, 0) // pattern_cotton_tshirt
	};
	
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
		new BuySellList(102, 15, 0.000000, 0), // round_shield
		new BuySellList(625, 15, 0.000000, 0), // bone_shield
		new BuySellList(626, 15, 0.000000, 0), // bronze_shield
		new BuySellList(627, 15, 0.000000, 0), // aspis
		new BuySellList(50, 15, 0.000000, 0), // leather_gloves
		new BuySellList(51, 15, 0.000000, 0), // bracer
		new BuySellList(604, 15, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 15, 0.000000, 0), // leather_gauntlet
		new BuySellList(38, 15, 0.000000, 0), // low_boots
		new BuySellList(39, 15, 0.000000, 0), // boots
		new BuySellList(40, 15, 0.000000, 0), // leather_boots
		new BuySellList(1123, 15, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(44, 15, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 15, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 15, 0.000000, 0), // bone_helmet
		new BuySellList(46, 15, 0.000000, 0), // bronze_helmet
		new BuySellList(9577, 15, 0.000000, 0), // tshirt
		new BuySellList(9578, 15, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 15, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 15, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12014, 15, 0.000000, 0), // puma_skin_gaiters_low
		new BuySellList(12008, 15, 0.000000, 0), // ring_breastplate_low
		new BuySellList(12012, 15, 0.000000, 0), // iron_plate_gaiters_low
		new BuySellList(12015, 15, 0.000000, 0), // puma_skin_shirt_low
		new BuySellList(12020, 15, 0.000000, 0), // lion_skin_gaiters_low
		new BuySellList(12021, 15, 0.000000, 0), // lion_skin_shirt_low
		new BuySellList(12031, 15, 0.000000, 0), // mithril_breastplate_low
		new BuySellList(12041, 15, 0.000000, 0), // mithril_gaiters_low
		new BuySellList(12013, 15, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 15, 0.000000, 0), // aspis_low
		new BuySellList(12007, 15, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 15, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12006, 15, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 15, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12009, 15, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 15, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12042, 15, 0.000000, 0), // mithril_banded_gaiters_low
		new BuySellList(12043, 15, 0.000000, 0) // mithril_banded_mail_low
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
		new BuySellList(12013, 15, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 15, 0.000000, 0), // aspis_low
		new BuySellList(12007, 15, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 15, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12006, 15, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 15, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12009, 15, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 15, 0.000000, 0) // bronze_helmet_low
	};
	
	private static final int npcId = 30178;
	
	public Zenkin() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "zenkin001.htm";
		super.fnYouAreChaotic = "zenkin006.htm";
	}
}