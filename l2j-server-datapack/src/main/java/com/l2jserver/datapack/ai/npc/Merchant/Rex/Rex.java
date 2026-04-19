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
package com.l2jserver.datapack.ai.npc.Merchant.Rex;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Rex extends Merchant {
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(176, 15, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 15, 0.000000, 0), // relic_of_saints
		new BuySellList(177, 15, 0.000000, 0), // mage_staff
		new BuySellList(311, 15, 0.000000, 0), // crucifix_of_blessing
		new BuySellList(100, 15, 0.000000, 0), // voodoo_doll
		new BuySellList(178, 15, 0.000000, 0), // bone_staff
		new BuySellList(101, 15, 0.000000, 0), // scroll_of_wisdom
		new BuySellList(7885, 15, 0.000000, 0), // sword_of_priest
		new BuySellList(312, 15, 0.000000, 0), // branch_of_life
		new BuySellList(314, 15, 0.000000, 0), // proof_of_revenge
		new BuySellList(179, 15, 0.000000, 0), // mace_of_prayer
		new BuySellList(182, 15, 0.000000, 0), // doom_hammer
		new BuySellList(183, 15, 0.000000, 0), // mystic_staff
		new BuySellList(185, 15, 0.000000, 0), // staff_of_mana
		new BuySellList(315, 15, 0.000000, 0), // divine_tome
		new BuySellList(83, 15, 0.000000, 0), // sword_of_magic
		new BuySellList(143, 15, 0.000000, 0), // sword_of_mystic
		new BuySellList(144, 15, 0.000000, 0), // sword_of_occult
		new BuySellList(238, 15, 0.000000, 0), // dagger_of_mana
		new BuySellList(239, 15, 0.000000, 0), // mystic_knife
		new BuySellList(240, 15, 0.000000, 0), // conjure_knife
		new BuySellList(241, 15, 0.000000, 0), // knife_o'_silenus
		new BuySellList(186, 15, 0.000000, 0), // staff_of_magicpower
		new BuySellList(316, 15, 0.000000, 0), // blood_of_saints
		new BuySellList(317, 15, 0.000000, 0), // tome_of_blood
		new BuySellList(90, 15, 0.000000, 0), // goathead_staff
		new BuySellList(7886, 15, 0.000000, 0), // sword_of_magic_fog
		new BuySellList(7890, 15, 0.000000, 0), // mace_of_priest
		new BuySellList(318, 15, 0.000000, 0), // crucifix_of_blood
		new BuySellList(321, 15, 0.000000, 0), // demon_fangs
		new BuySellList(187, 15, 0.000000, 0), // atuba_hammer
		new BuySellList(188, 15, 0.000000, 0), // ghost_staff
		new BuySellList(189, 15, 0.000000, 0), // life_stick
		new BuySellList(190, 15, 0.000000, 0) // atuba_mace
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(11610, 15, 0.000000, 0), // bone_staff_low
		new BuySellList(11619, 15, 0.000000, 0), // scroll_of_wisdom_low
		new BuySellList(11611, 15, 0.000000, 0), // sword_of_priest_low
		new BuySellList(11612, 15, 0.000000, 0), // branch_of_life_low
		new BuySellList(11609, 15, 0.000000, 0), // proof_of_revenge_low
		new BuySellList(11627, 15, 0.000000, 0), // mace_of_prayer_low
		new BuySellList(11630, 15, 0.000000, 0), // doom_hammer_low
		new BuySellList(11634, 15, 0.000000, 0), // mystic_staff_low
		new BuySellList(11633, 15, 0.000000, 0), // staff_of_mana_low
		new BuySellList(11637, 15, 0.000000, 0), // divine_tome_low
		new BuySellList(11655, 15, 0.000000, 0), // sword_of_magic_low
		new BuySellList(11656, 15, 0.000000, 0), // sword_of_mystic_low
		new BuySellList(11657, 15, 0.000000, 0), // sword_of_occult_low
		new BuySellList(11662, 15, 0.000000, 0), // dagger_of_mana_low
		new BuySellList(11664, 15, 0.000000, 0), // mystic_knife_low
		new BuySellList(11672, 15, 0.000000, 0), // conjure_knife_low
		new BuySellList(11660, 15, 0.000000, 0), // knife_o'_silenus_low
		new BuySellList(11663, 15, 0.000000, 0), // staff_of_magicpower_low
		new BuySellList(11678, 15, 0.000000, 0), // blood_of_saints_low
		new BuySellList(11676, 15, 0.000000, 0), // tome_of_blood_low
		new BuySellList(11693, 15, 0.000000, 0), // goathead_staff_low
		new BuySellList(11696, 15, 0.000000, 0), // sword_of_magic_fog_low
		new BuySellList(11692, 15, 0.000000, 0), // mace_of_priest_low
		new BuySellList(11709, 15, 0.000000, 0), // crucifix_of_blood_low
		new BuySellList(11715, 15, 0.000000, 0), // demon_fangs_low
		new BuySellList(11732, 15, 0.000000, 0), // atuba_hammer_low
		new BuySellList(11737, 15, 0.000000, 0), // ghost_staff_low
		new BuySellList(11729, 15, 0.000000, 0), // life_stick_low
		new BuySellList(11731, 15, 0.000000, 0) // atuba_mace_low
	};
	
	private static final int npcId = 30163;
	
	public Rex() {
		super(npcId);
		
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "rex001.htm";
		super.fnYouAreChaotic = "rex006.htm";
	}
}