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
package com.l2jserver.datapack.ai.npc.Merchant.Raudia;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Raudia extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(2, 15, 0.000000, 0), // long_sword
		new BuySellList(218, 15, 0.000000, 0), // throw_knife
		new BuySellList(272, 15, 0.000000, 0), // bow_of_forest
		new BuySellList(15, 15, 0.000000, 0), // short_spear
		new BuySellList(68, 15, 0.000000, 0), // falchion
		new BuySellList(219, 15, 0.000000, 0), // sword_breaker
		new BuySellList(273, 15, 0.000000, 0), // composition_bow
		new BuySellList(155, 15, 0.000000, 0), // buzdygan
		new BuySellList(88, 15, 0.000000, 0), // morning_star
		new BuySellList(87, 15, 0.000000, 0), // iron_hammer
		new BuySellList(16, 15, 0.000000, 0), // long_spear
		new BuySellList(123, 15, 0.000000, 0), // saber
		new BuySellList(7880, 15, 0.000000, 0), // iron_sword
		new BuySellList(220, 15, 0.000000, 0), // handiwork_dagger
		new BuySellList(221, 15, 0.000000, 0), // assassin_knife
		new BuySellList(274, 15, 0.000000, 0), // strengthening_bow
		new BuySellList(156, 15, 0.000000, 0), // hand_axe
		new BuySellList(166, 15, 0.000000, 0), // heavy_mace
		new BuySellList(168, 15, 0.000000, 0), // work_hammer
		new BuySellList(291, 15, 0.000000, 0), // trident
		new BuySellList(69, 15, 0.000000, 0), // bastard_sword
		new BuySellList(222, 15, 0.000000, 0), // poniard_dagger
		new BuySellList(275, 15, 0.000000, 0), // long_bow
		new BuySellList(277, 15, 0.000000, 0), // dark_elven_bow
		new BuySellList(292, 15, 0.000000, 0), // pike
		new BuySellList(295, 15, 0.000000, 0), // dwarven_trident
		new BuySellList(256, 15, 0.000000, 0), // cestus
		new BuySellList(257, 15, 0.000000, 0), // viper's_canine
		new BuySellList(258, 15, 0.000000, 0), // bagh-nakh
		new BuySellList(259, 15, 0.000000, 0) // single-edged_jamadhr
	};
	
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
		new BuySellList(240, 15, 0.000000, 0) // conjure_knife
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(11614, 15, 0.000000, 0), // saber_low
		new BuySellList(11605, 15, 0.000000, 0), // iron_sword_low
		new BuySellList(11613, 15, 0.000000, 0), // handiwork_dagger_low
		new BuySellList(11617, 15, 0.000000, 0), // assassin_knife_low
		new BuySellList(11606, 15, 0.000000, 0), // strengthening_bow_low
		new BuySellList(11623, 15, 0.000000, 0), // hand_axe_low
		new BuySellList(11626, 15, 0.000000, 0), // heavy_mace_low
		new BuySellList(11618, 15, 0.000000, 0), // work_hammer_low
		new BuySellList(11622, 15, 0.000000, 0), // trident_low
		new BuySellList(11635, 15, 0.000000, 0), // bastard_sword_low
		new BuySellList(11646, 15, 0.000000, 0), // poniard_dagger_low
		new BuySellList(11632, 15, 0.000000, 0), // long_bow_low
		new BuySellList(11629, 15, 0.000000, 0), // dark_elven_bow_low
		new BuySellList(11647, 15, 0.000000, 0), // pike_low
		new BuySellList(11631, 15, 0.000000, 0), // dwarven_trident_low
		new BuySellList(11608, 15, 0.000000, 0), // bagh-nakh_low
		new BuySellList(11642, 15, 0.000000, 0) // single-edged_jamadhr_low
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
		new BuySellList(11672, 15, 0.000000, 0) // conjure_knife_low
	};
	
	private static final int npcId = 30179;
	
	public Raudia() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "raudia001.htm";
		super.fnYouAreChaotic = "raudia006.htm";
	}
}