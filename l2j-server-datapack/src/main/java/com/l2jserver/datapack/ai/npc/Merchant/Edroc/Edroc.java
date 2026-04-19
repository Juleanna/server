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
package com.l2jserver.datapack.ai.npc.Merchant.Edroc;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Edroc extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(412, 30, 0.000000, 0), // tights_pants
		new BuySellList(390, 30, 0.000000, 0), // tights_shirt
		new BuySellList(24, 30, 0.000000, 0), // bone_breastplate
		new BuySellList(31, 30, 0.000000, 0), // bone_gaiters
		new BuySellList(25, 30, 0.000000, 0), // piece_bone_breastplate
		new BuySellList(26, 30, 0.000000, 0), // bronze_breastplate
		new BuySellList(33, 30, 0.000000, 0), // hard_leather_gaiters
		new BuySellList(32, 30, 0.000000, 0), // piece_bone_gaiters
		new BuySellList(27, 30, 0.000000, 0), // hard_leather_shirt
		new BuySellList(34, 30, 0.000000, 0), // bronze_gaiters
		new BuySellList(413, 30, 0.000000, 0), // puma_skin_gaiters
		new BuySellList(347, 30, 0.000000, 0), // ring_breastplate
		new BuySellList(376, 30, 0.000000, 0), // iron_plate_gaiters
		new BuySellList(391, 30, 0.000000, 0), // puma_skin_shirt
		new BuySellList(414, 30, 0.000000, 0), // lion_skin_gaiters
		new BuySellList(392, 30, 0.000000, 0), // lion_skin_shirt
		new BuySellList(58, 30, 0.000000, 0), // mithril_breastplate
		new BuySellList(59, 30, 0.000000, 0), // mithril_gaiters
		new BuySellList(102, 30, 0.000000, 0), // round_shield
		new BuySellList(625, 30, 0.000000, 0), // bone_shield
		new BuySellList(626, 30, 0.000000, 0), // bronze_shield
		new BuySellList(627, 30, 0.000000, 0), // aspis
		new BuySellList(50, 30, 0.000000, 0), // leather_gloves
		new BuySellList(51, 30, 0.000000, 0), // bracer
		new BuySellList(604, 30, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 30, 0.000000, 0), // leather_gauntlet
		new BuySellList(38, 30, 0.000000, 0), // low_boots
		new BuySellList(39, 30, 0.000000, 0), // boots
		new BuySellList(40, 30, 0.000000, 0), // leather_boots
		new BuySellList(1123, 30, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(44, 30, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 30, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 30, 0.000000, 0), // bone_helmet
		new BuySellList(46, 30, 0.000000, 0), // bronze_helmet
		new BuySellList(415, 30, 0.000000, 0), // mithril_banded_gaiters
		new BuySellList(393, 30, 0.000000, 0), // mithril_banded_mail
		new BuySellList(9577, 30, 0.000000, 0), // tshirt
		new BuySellList(9578, 30, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 30, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 30, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1104, 30, 0.000000, 0), // hose_of_devotion
		new BuySellList(1101, 30, 0.000000, 0), // tunic_of_devotion
		new BuySellList(1105, 30, 0.000000, 0), // hose_of_magicpower
		new BuySellList(1102, 30, 0.000000, 0), // tunic_of_magicpower
		new BuySellList(465, 30, 0.000000, 0), // cursed_hose
		new BuySellList(432, 30, 0.000000, 0), // cursed_tunic
		new BuySellList(467, 30, 0.000000, 0), // dark_hose
		new BuySellList(434, 30, 0.000000, 0), // white_tunic
		new BuySellList(468, 30, 0.000000, 0), // mage's_hose
		new BuySellList(435, 30, 0.000000, 0), // mage's_tunic
		new BuySellList(469, 30, 0.000000, 0), // hose_of_knowledge
		new BuySellList(436, 30, 0.000000, 0), // tunic_of_knowledge
		new BuySellList(102, 30, 0.000000, 0), // round_shield
		new BuySellList(625, 30, 0.000000, 0), // bone_shield
		new BuySellList(626, 30, 0.000000, 0), // bronze_shield
		new BuySellList(627, 30, 0.000000, 0), // aspis
		new BuySellList(50, 30, 0.000000, 0), // leather_gloves
		new BuySellList(51, 30, 0.000000, 0), // bracer
		new BuySellList(604, 30, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 30, 0.000000, 0), // leather_gauntlet
		new BuySellList(38, 30, 0.000000, 0), // low_boots
		new BuySellList(39, 30, 0.000000, 0), // boots
		new BuySellList(40, 30, 0.000000, 0), // leather_boots
		new BuySellList(1123, 30, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(44, 30, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 30, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 30, 0.000000, 0), // bone_helmet
		new BuySellList(46, 30, 0.000000, 0), // bronze_helmet
		new BuySellList(9577, 30, 0.000000, 0), // tshirt
		new BuySellList(9578, 30, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 30, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 30, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12014, 30, 0.000000, 0), // puma_skin_gaiters_low
		new BuySellList(12008, 30, 0.000000, 0), // ring_breastplate_low
		new BuySellList(12012, 30, 0.000000, 0), // iron_plate_gaiters_low
		new BuySellList(12015, 30, 0.000000, 0), // puma_skin_shirt_low
		new BuySellList(12020, 30, 0.000000, 0), // lion_skin_gaiters_low
		new BuySellList(12021, 30, 0.000000, 0), // lion_skin_shirt_low
		new BuySellList(12031, 30, 0.000000, 0), // mithril_breastplate_low
		new BuySellList(12041, 30, 0.000000, 0), // mithril_gaiters_low
		new BuySellList(12013, 30, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 30, 0.000000, 0), // aspis_low
		new BuySellList(12007, 30, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 30, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12006, 30, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 30, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12009, 30, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 30, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12042, 30, 0.000000, 0), // mithril_banded_gaiters_low
		new BuySellList(12043, 30, 0.000000, 0) // mithril_banded_mail_low
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(12011, 30, 0.000000, 0), // cursed_hose_low
		new BuySellList(12010, 30, 0.000000, 0), // cursed_tunic_low
		new BuySellList(12017, 30, 0.000000, 0), // dark_hose_low
		new BuySellList(12022, 30, 0.000000, 0), // white_tunic_low
		new BuySellList(12019, 30, 0.000000, 0), // mage's_hose_low
		new BuySellList(12018, 30, 0.000000, 0), // mage's_tunic_low
		new BuySellList(12048, 30, 0.000000, 0), // hose_of_knowledge_low
		new BuySellList(12047, 30, 0.000000, 0), // tunic_of_knowledge_low
		new BuySellList(12013, 30, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 30, 0.000000, 0), // aspis_low
		new BuySellList(12007, 30, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 30, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12006, 30, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 30, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12009, 30, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 30, 0.000000, 0) // bronze_helmet_low
	};
	
	private static final int npcId = 30230;
	
	public Edroc() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "trader_edroc001.htm";
		super.fnYouAreChaotic = "trader_edroc006.htm";
	}
}