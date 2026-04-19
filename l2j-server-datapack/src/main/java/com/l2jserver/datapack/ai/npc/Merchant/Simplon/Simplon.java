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
package com.l2jserver.datapack.ai.npc.Merchant.Simplon;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Simplon extends MerchantForNewbie {
	
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
		new BuySellList(376, 20, 0.000000, 0), // iron_plate_gaiters
		new BuySellList(391, 20, 0.000000, 0), // puma_skin_shirt
		new BuySellList(414, 20, 0.000000, 0), // lion_skin_gaiters
		new BuySellList(392, 20, 0.000000, 0), // lion_skin_shirt
		new BuySellList(58, 20, 0.000000, 0), // mithril_breastplate
		new BuySellList(59, 20, 0.000000, 0), // mithril_gaiters
		new BuySellList(102, 20, 0.000000, 0), // round_shield
		new BuySellList(625, 20, 0.000000, 0), // bone_shield
		new BuySellList(626, 20, 0.000000, 0), // bronze_shield
		new BuySellList(627, 20, 0.000000, 0), // aspis
		new BuySellList(50, 20, 0.000000, 0), // leather_gloves
		new BuySellList(51, 20, 0.000000, 0), // bracer
		new BuySellList(604, 20, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 20, 0.000000, 0), // leather_gauntlet
		new BuySellList(38, 20, 0.000000, 0), // low_boots
		new BuySellList(39, 20, 0.000000, 0), // boots
		new BuySellList(40, 20, 0.000000, 0), // leather_boots
		new BuySellList(1123, 20, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(44, 20, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 20, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 20, 0.000000, 0), // bone_helmet
		new BuySellList(46, 20, 0.000000, 0), // bronze_helmet
		new BuySellList(415, 20, 0.000000, 0), // mithril_banded_gaiters
		new BuySellList(393, 20, 0.000000, 0), // mithril_banded_mail
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
		new BuySellList(102, 20, 0.000000, 0), // round_shield
		new BuySellList(625, 20, 0.000000, 0), // bone_shield
		new BuySellList(626, 20, 0.000000, 0), // bronze_shield
		new BuySellList(627, 20, 0.000000, 0), // aspis
		new BuySellList(50, 20, 0.000000, 0), // leather_gloves
		new BuySellList(51, 20, 0.000000, 0), // bracer
		new BuySellList(604, 20, 0.000000, 0), // excellence_leather_gloves
		new BuySellList(605, 20, 0.000000, 0), // leather_gauntlet
		new BuySellList(38, 20, 0.000000, 0), // low_boots
		new BuySellList(39, 20, 0.000000, 0), // boots
		new BuySellList(40, 20, 0.000000, 0), // leather_boots
		new BuySellList(1123, 20, 0.000000, 0), // blue_buckskin_boots
		new BuySellList(44, 20, 0.000000, 0), // leather_helmet
		new BuySellList(1148, 20, 0.000000, 0), // hard_leather_helmet
		new BuySellList(45, 20, 0.000000, 0), // bone_helmet
		new BuySellList(46, 20, 0.000000, 0), // bronze_helmet
		new BuySellList(9577, 20, 0.000000, 0), // tshirt
		new BuySellList(9578, 20, 0.000000, 0), // cotton_tshirt
		new BuySellList(9583, 20, 0.000000, 0), // pattern_tshirt
		new BuySellList(9584, 20, 0.000000, 0) // pattern_cotton_tshirt
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12014, 20, 0.000000, 0), // puma_skin_gaiters_low
		new BuySellList(12008, 20, 0.000000, 0), // ring_breastplate_low
		new BuySellList(12012, 20, 0.000000, 0), // iron_plate_gaiters_low
		new BuySellList(12015, 20, 0.000000, 0), // puma_skin_shirt_low
		new BuySellList(12020, 20, 0.000000, 0), // lion_skin_gaiters_low
		new BuySellList(12021, 20, 0.000000, 0), // lion_skin_shirt_low
		new BuySellList(12031, 20, 0.000000, 0), // mithril_breastplate_low
		new BuySellList(12041, 20, 0.000000, 0), // mithril_gaiters_low
		new BuySellList(12013, 20, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 20, 0.000000, 0), // aspis_low
		new BuySellList(12007, 20, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 20, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12006, 20, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 20, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12009, 20, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 20, 0.000000, 0), // bronze_helmet_low
		new BuySellList(12042, 20, 0.000000, 0), // mithril_banded_gaiters_low
		new BuySellList(12043, 20, 0.000000, 0) // mithril_banded_mail_low
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
		new BuySellList(12013, 20, 0.000000, 0), // bronze_shield_low
		new BuySellList(12025, 20, 0.000000, 0), // aspis_low
		new BuySellList(12007, 20, 0.000000, 0), // excellence_leather_gloves_low
		new BuySellList(12016, 20, 0.000000, 0), // leather_gauntlet_low
		new BuySellList(12006, 20, 0.000000, 0), // leather_boots_low
		new BuySellList(12029, 20, 0.000000, 0), // blue_buckskin_boots_low
		new BuySellList(12009, 20, 0.000000, 0), // bone_helmet_low
		new BuySellList(12028, 20, 0.000000, 0) // bronze_helmet_low
	};
	
	private static final int npcId = 30253;
	
	public Simplon() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "trader_simplon001.htm";
		super.fnYouAreChaotic = "trader_simplon006.htm";
	}
}