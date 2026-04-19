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
package com.l2jserver.datapack.ai.npc.Merchant.Viktor;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Viktor extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(2, 30, 0.000000, 0), // long_sword
		new BuySellList(218, 30, 0.000000, 0), // throw_knife
		new BuySellList(272, 30, 0.000000, 0), // bow_of_forest
		new BuySellList(15, 30, 0.000000, 0), // short_spear
		new BuySellList(68, 30, 0.000000, 0), // falchion
		new BuySellList(219, 30, 0.000000, 0), // sword_breaker
		new BuySellList(273, 30, 0.000000, 0), // composition_bow
		new BuySellList(155, 30, 0.000000, 0), // buzdygan
		new BuySellList(88, 30, 0.000000, 0), // morning_star
		new BuySellList(87, 30, 0.000000, 0), // iron_hammer
		new BuySellList(16, 30, 0.000000, 0), // long_spear
		new BuySellList(123, 30, 0.000000, 0), // saber
		new BuySellList(7880, 30, 0.000000, 0), // iron_sword
		new BuySellList(220, 30, 0.000000, 0), // handiwork_dagger
		new BuySellList(221, 30, 0.000000, 0), // assassin_knife
		new BuySellList(274, 30, 0.000000, 0), // strengthening_bow
		new BuySellList(156, 30, 0.000000, 0), // hand_axe
		new BuySellList(166, 30, 0.000000, 0), // heavy_mace
		new BuySellList(168, 30, 0.000000, 0), // work_hammer
		new BuySellList(291, 30, 0.000000, 0), // trident
		new BuySellList(69, 30, 0.000000, 0), // bastard_sword
		new BuySellList(222, 30, 0.000000, 0), // poniard_dagger
		new BuySellList(275, 30, 0.000000, 0), // long_bow
		new BuySellList(277, 30, 0.000000, 0), // dark_elven_bow
		new BuySellList(292, 30, 0.000000, 0), // pike
		new BuySellList(295, 30, 0.000000, 0), // dwarven_trident
		new BuySellList(256, 30, 0.000000, 0), // cestus
		new BuySellList(257, 30, 0.000000, 0), // viper's_canine
		new BuySellList(258, 30, 0.000000, 0), // bagh-nakh
		new BuySellList(259, 30, 0.000000, 0) // single-edged_jamadhr
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(176, 30, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 30, 0.000000, 0), // relic_of_saints
		new BuySellList(177, 30, 0.000000, 0), // mage_staff
		new BuySellList(311, 30, 0.000000, 0), // crucifix_of_blessing
		new BuySellList(100, 30, 0.000000, 0), // voodoo_doll
		new BuySellList(178, 30, 0.000000, 0), // bone_staff
		new BuySellList(101, 30, 0.000000, 0), // scroll_of_wisdom
		new BuySellList(7885, 30, 0.000000, 0), // sword_of_priest
		new BuySellList(312, 30, 0.000000, 0), // branch_of_life
		new BuySellList(314, 30, 0.000000, 0), // proof_of_revenge
		new BuySellList(179, 30, 0.000000, 0), // mace_of_prayer
		new BuySellList(182, 30, 0.000000, 0), // doom_hammer
		new BuySellList(183, 30, 0.000000, 0), // mystic_staff
		new BuySellList(185, 30, 0.000000, 0), // staff_of_mana
		new BuySellList(315, 30, 0.000000, 0), // divine_tome
		new BuySellList(83, 30, 0.000000, 0), // sword_of_magic
		new BuySellList(143, 30, 0.000000, 0), // sword_of_mystic
		new BuySellList(144, 30, 0.000000, 0), // sword_of_occult
		new BuySellList(238, 30, 0.000000, 0), // dagger_of_mana
		new BuySellList(239, 30, 0.000000, 0), // mystic_knife
		new BuySellList(240, 30, 0.000000, 0) // conjure_knife
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(11614, 30, 0.000000, 0), // saber_low
		new BuySellList(11605, 30, 0.000000, 0), // iron_sword_low
		new BuySellList(11613, 30, 0.000000, 0), // handiwork_dagger_low
		new BuySellList(11617, 30, 0.000000, 0), // assassin_knife_low
		new BuySellList(11606, 30, 0.000000, 0), // strengthening_bow_low
		new BuySellList(11623, 30, 0.000000, 0), // hand_axe_low
		new BuySellList(11626, 30, 0.000000, 0), // heavy_mace_low
		new BuySellList(11618, 30, 0.000000, 0), // work_hammer_low
		new BuySellList(11622, 30, 0.000000, 0), // trident_low
		new BuySellList(11635, 30, 0.000000, 0), // bastard_sword_low
		new BuySellList(11646, 30, 0.000000, 0), // poniard_dagger_low
		new BuySellList(11632, 30, 0.000000, 0), // long_bow_low
		new BuySellList(11629, 30, 0.000000, 0), // dark_elven_bow_low
		new BuySellList(11647, 30, 0.000000, 0), // pike_low
		new BuySellList(11631, 30, 0.000000, 0), // dwarven_trident_low
		new BuySellList(11608, 30, 0.000000, 0), // bagh-nakh_low
		new BuySellList(11642, 30, 0.000000, 0) // single-edged_jamadhr_low
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(11610, 30, 0.000000, 0), // bone_staff_low
		new BuySellList(11619, 30, 0.000000, 0), // scroll_of_wisdom_low
		new BuySellList(11611, 30, 0.000000, 0), // sword_of_priest_low
		new BuySellList(11612, 30, 0.000000, 0), // branch_of_life_low
		new BuySellList(11609, 30, 0.000000, 0), // proof_of_revenge_low
		new BuySellList(11627, 30, 0.000000, 0), // mace_of_prayer_low
		new BuySellList(11630, 30, 0.000000, 0), // doom_hammer_low
		new BuySellList(11634, 30, 0.000000, 0), // mystic_staff_low
		new BuySellList(11633, 30, 0.000000, 0), // staff_of_mana_low
		new BuySellList(11637, 30, 0.000000, 0), // divine_tome_low
		new BuySellList(11655, 30, 0.000000, 0), // sword_of_magic_low
		new BuySellList(11656, 30, 0.000000, 0), // sword_of_mystic_low
		new BuySellList(11657, 30, 0.000000, 0), // sword_of_occult_low
		new BuySellList(11662, 30, 0.000000, 0), // dagger_of_mana_low
		new BuySellList(11664, 30, 0.000000, 0), // mystic_knife_low
		new BuySellList(11672, 30, 0.000000, 0) // conjure_knife_low
	};
	
	private static final int npcId = 30684;
	
	public Viktor() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "trader_viktor001.htm";
		super.fnYouAreChaotic = "trader_viktor006.htm";
	}
}