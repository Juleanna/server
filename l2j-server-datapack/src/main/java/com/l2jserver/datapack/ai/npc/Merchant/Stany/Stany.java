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
package com.l2jserver.datapack.ai.npc.Merchant.Stany;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Stany extends Merchant {
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(176, 10, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 10, 0.000000, 0), // relic_of_saints
		new BuySellList(177, 10, 0.000000, 0), // mage_staff
		new BuySellList(311, 10, 0.000000, 0), // crucifix_of_blessing
		new BuySellList(100, 10, 0.000000, 0), // voodoo_doll
		new BuySellList(178, 10, 0.000000, 0), // bone_staff
		new BuySellList(101, 10, 0.000000, 0), // scroll_of_wisdom
		new BuySellList(7885, 10, 0.000000, 0), // sword_of_priest
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
		new BuySellList(7886, 10, 0.000000, 0), // sword_of_magic_fog
		new BuySellList(7890, 10, 0.000000, 0), // mace_of_priest
		new BuySellList(318, 10, 0.000000, 0), // crucifix_of_blood
		new BuySellList(321, 10, 0.000000, 0), // demon_fangs
		new BuySellList(187, 10, 0.000000, 0), // atuba_hammer
		new BuySellList(188, 10, 0.000000, 0), // ghost_staff
		new BuySellList(189, 10, 0.000000, 0), // life_stick
		new BuySellList(190, 10, 0.000000, 0), // atuba_mace
		new BuySellList(192, 10, 0.000000, 0), // crystal_staff
		new BuySellList(193, 10, 0.000000, 0), // stick_of_faith
		new BuySellList(7887, 10, 0.000000, 0), // mystery_sword
		new BuySellList(242, 10, 0.000000, 0), // dagger_of_magicflame
		new BuySellList(325, 10, 0.000000, 0), // horn_of_glory
		new BuySellList(195, 10, 0.000000, 0), // cursed_staff
		new BuySellList(84, 10, 0.000000, 0), // homunkulus's_sword
		new BuySellList(145, 10, 0.000000, 0), // deathbreath_sword
		new BuySellList(174, 10, 0.000000, 0), // nirvana_axe
		new BuySellList(196, 10, 0.000000, 0), // stick_of_eternity
		new BuySellList(201, 10, 0.000000, 0), // club_of_nature
		new BuySellList(202, 10, 0.000000, 0), // mace_of_underworld
		new BuySellList(326, 10, 0.000000, 0), // heathen's_book
		new BuySellList(197, 10, 0.000000, 0), // paradia_staff
		new BuySellList(198, 10, 0.000000, 0), // inferno_staff
		new BuySellList(199, 10, 0.000000, 0), // paagrio_hammer
		new BuySellList(200, 10, 0.000000, 0), // sage's_staff
		new BuySellList(203, 10, 0.000000, 0), // paagrio_axe
		new BuySellList(7888, 10, 0.000000, 0), // sword_of_eclipse
		new BuySellList(7891, 10, 0.000000, 0), // eclipse_axe
		new BuySellList(204, 10, 0.000000, 0), // deadman's_staff
		new BuySellList(205, 10, 0.000000, 0), // ghoul's_staff
		new BuySellList(206, 10, 0.000000, 0) // demon's_staff
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(11610, 10, 0.000000, 0), // bone_staff_low
		new BuySellList(11619, 10, 0.000000, 0), // scroll_of_wisdom_low
		new BuySellList(11611, 10, 0.000000, 0), // sword_of_priest_low
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
		new BuySellList(11696, 10, 0.000000, 0), // sword_of_magic_fog_low
		new BuySellList(11692, 10, 0.000000, 0), // mace_of_priest_low
		new BuySellList(11709, 10, 0.000000, 0), // crucifix_of_blood_low
		new BuySellList(11715, 10, 0.000000, 0), // demon_fangs_low
		new BuySellList(11732, 10, 0.000000, 0), // atuba_hammer_low
		new BuySellList(11737, 10, 0.000000, 0), // ghost_staff_low
		new BuySellList(11729, 10, 0.000000, 0), // life_stick_low
		new BuySellList(11731, 10, 0.000000, 0), // atuba_mace_low
		new BuySellList(11764, 10, 0.000000, 0), // crystal_staff_low
		new BuySellList(11757, 10, 0.000000, 0), // stick_of_faith_low
		new BuySellList(11748, 10, 0.000000, 0), // mystery_sword_low
		new BuySellList(11779, 10, 0.000000, 0), // dagger_of_magicflame_low
		new BuySellList(11792, 10, 0.000000, 0), // horn_of_glory_low
		new BuySellList(11793, 10, 0.000000, 0), // cursed_staff_low
		new BuySellList(11829, 10, 0.000000, 0), // homunkulus's_sword_low
		new BuySellList(11805, 10, 0.000000, 0), // deathbreath_sword_low
		new BuySellList(11802, 10, 0.000000, 0), // nirvana_axe_low
		new BuySellList(11799, 10, 0.000000, 0), // stick_of_eternity_low
		new BuySellList(11804, 10, 0.000000, 0), // club_of_nature_low
		new BuySellList(11806, 10, 0.000000, 0), // mace_of_underworld_low
		new BuySellList(11817, 10, 0.000000, 0), // heathen's_book_low
		new BuySellList(11825, 10, 0.000000, 0), // paradia_staff_low
		new BuySellList(11819, 10, 0.000000, 0), // inferno_staff_low
		new BuySellList(11824, 10, 0.000000, 0), // paagrio_hammer_low
		new BuySellList(11828, 10, 0.000000, 0), // sage's_staff_low
		new BuySellList(11833, 10, 0.000000, 0), // paagrio_axe_low
		new BuySellList(11850, 10, 0.000000, 0), // sword_of_eclipse_low
		new BuySellList(11859, 10, 0.000000, 0), // eclipse_axe_low
		new BuySellList(11842, 10, 0.000000, 0), // deadman's_staff_low
		new BuySellList(11838, 10, 0.000000, 0), // ghoul's_staff_low
		new BuySellList(11855, 10, 0.000000, 0) // demon's_staff_low
	};
	
	private static final int npcId = 30085;
	
	public Stany() {
		super(npcId);
		
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "stany001.htm";
		super.fnYouAreChaotic = "stany006.htm";
	}
}