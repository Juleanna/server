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
package com.l2jserver.datapack.ai.npc.Merchant.Grabner;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Grabner extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(2, 10, 0.000000, 0), // long_sword
		new BuySellList(218, 10, 0.000000, 0), // throw_knife
		new BuySellList(272, 10, 0.000000, 0), // bow_of_forest
		new BuySellList(15, 10, 0.000000, 0), // short_spear
		new BuySellList(68, 10, 0.000000, 0), // falchion
		new BuySellList(219, 10, 0.000000, 0), // sword_breaker
		new BuySellList(273, 10, 0.000000, 0), // composition_bow
		new BuySellList(155, 10, 0.000000, 0), // buzdygan
		new BuySellList(87, 10, 0.000000, 0), // iron_hammer
		new BuySellList(16, 10, 0.000000, 0), // long_spear
		new BuySellList(123, 10, 0.000000, 0), // saber
		new BuySellList(7880, 10, 0.000000, 0), // iron_sword
		new BuySellList(220, 10, 0.000000, 0), // handiwork_dagger
		new BuySellList(221, 10, 0.000000, 0), // assassin_knife
		new BuySellList(274, 10, 0.000000, 0), // strengthening_bow
		new BuySellList(156, 10, 0.000000, 0), // hand_axe
		new BuySellList(166, 10, 0.000000, 0), // heavy_mace
		new BuySellList(168, 10, 0.000000, 0), // work_hammer
		new BuySellList(291, 10, 0.000000, 0), // trident
		new BuySellList(69, 10, 0.000000, 0), // bastard_sword
		new BuySellList(126, 10, 0.000000, 0), // artisan's_sword
		new BuySellList(222, 10, 0.000000, 0), // poniard_dagger
		new BuySellList(275, 10, 0.000000, 0), // long_bow
		new BuySellList(276, 10, 0.000000, 0), // elven_bow
		new BuySellList(277, 10, 0.000000, 0), // dark_elven_bow
		new BuySellList(86, 10, 0.000000, 0), // tomahawk
		new BuySellList(292, 10, 0.000000, 0), // pike
		new BuySellList(295, 10, 0.000000, 0), // dwarven_trident
		new BuySellList(124, 10, 0.000000, 0), // two-handed_sword
		new BuySellList(127, 10, 0.000000, 0), // crimson_sword
		new BuySellList(130, 10, 0.000000, 0), // elven_sword
		new BuySellList(223, 10, 0.000000, 0), // kukuri
		new BuySellList(278, 10, 0.000000, 0), // gastraphetes
		new BuySellList(157, 10, 0.000000, 0), // spike_club
		new BuySellList(293, 10, 0.000000, 0), // war_hammer
		new BuySellList(296, 10, 0.000000, 0), // dwarven_pike
		new BuySellList(129, 10, 0.000000, 0), // sword_of_revolution
		new BuySellList(224, 10, 0.000000, 0), // maingauche
		new BuySellList(1660, 10, 0.000000, 0), // cursed_maingauche
		new BuySellList(279, 10, 0.000000, 0), // strengthening_long_bow
		new BuySellList(158, 10, 0.000000, 0), // tarbar
		new BuySellList(7881, 10, 0.000000, 0), // giants_sword
		new BuySellList(7896, 10, 0.000000, 0), // giants_hammer
		new BuySellList(172, 10, 0.000000, 0), // heavy_bone_club
		new BuySellList(294, 10, 0.000000, 0), // hammer_in_flames
		new BuySellList(88, 10, 0.000000, 0), // morning_star
		new BuySellList(93, 10, 0.000000, 0), // winged_spear
		new BuySellList(256, 10, 0.000000, 0), // cestus
		new BuySellList(257, 10, 0.000000, 0), // viper's_canine
		new BuySellList(258, 10, 0.000000, 0), // bagh-nakh
		new BuySellList(259, 10, 0.000000, 0), // single-edged_jamadhr
		new BuySellList(260, 10, 0.000000, 0), // triple-edged_jamadhr
		new BuySellList(261, 10, 0.000000, 0), // bich'hwa
		new BuySellList(70, 10, 0.000000, 0), // claymore
		new BuySellList(159, 10, 0.000000, 0), // bonebreaker
		new BuySellList(225, 10, 0.000000, 0), // mithril_dagger
		new BuySellList(262, 10, 0.000000, 0), // scallop_jamadhr
		new BuySellList(280, 10, 0.000000, 0), // cyclone_bow
		new BuySellList(297, 10, 0.000000, 0), // glaive
		new BuySellList(2499, 10, 0.000000, 0), // elven_long_sword
		new BuySellList(5284, 10, 0.000000, 0), // zweihander
		new BuySellList(5285, 10, 0.000000, 0), // heavy_sword
		new BuySellList(232, 10, 0.000000, 0), // darkelven_dagger
		new BuySellList(263, 10, 0.000000, 0), // chakram
		new BuySellList(281, 10, 0.000000, 0), // crystallized_ice_bow
		new BuySellList(298, 10, 0.000000, 0), // orcish_glaive
		new BuySellList(302, 10, 0.000000, 0), // body_slasher
		new BuySellList(73, 10, 0.000000, 0), // shamshir
		new BuySellList(74, 10, 0.000000, 0), // katana
		new BuySellList(131, 10, 0.000000, 0), // spirits_sword
		new BuySellList(133, 10, 0.000000, 0), // raid_sword
		new BuySellList(227, 10, 0.000000, 0), // stiletto
		new BuySellList(2502, 10, 0.000000, 0), // dwarven_warhammer
		new BuySellList(94, 10, 0.000000, 0), // bech_de_corbin
		new BuySellList(282, 10, 0.000000, 0), // elemental_bow
		new BuySellList(285, 10, 0.000000, 0), // noble_elven_bow
		new BuySellList(4233, 10, 0.000000, 0), // knuckle_dust
		new BuySellList(75, 10, 0.000000, 0), // caliburs
		new BuySellList(76, 10, 0.000000, 0), // sword_of_delusion
		new BuySellList(77, 10, 0.000000, 0), // tsurugi
		new BuySellList(132, 10, 0.000000, 0), // sword_of_limit
		new BuySellList(134, 10, 0.000000, 0), // sword_of_nightmare
		new BuySellList(162, 10, 0.000000, 0), // war_axe
		new BuySellList(231, 10, 0.000000, 0), // grace_dagger
		new BuySellList(233, 10, 0.000000, 0), // dark_screamer
		new BuySellList(95, 10, 0.000000, 0), // poleaxe
		new BuySellList(265, 10, 0.000000, 0), // fist_blade
		new BuySellList(283, 10, 0.000000, 0), // akat_long_bow
		new BuySellList(7882, 10, 0.000000, 0), // sword_of_paagrio
		new BuySellList(7898, 10, 0.000000, 0), // horn_of_karik
		new BuySellList(301, 10, 0.000000, 0), // scorpion
		new BuySellList(303, 10, 0.000000, 0), // widow_maker
		new BuySellList(135, 10, 0.000000, 0), // samurai_longsword
		new BuySellList(228, 10, 0.000000, 0), // crystal_dagger
		new BuySellList(2503, 10, 0.000000, 0), // yaksa_mace
		new BuySellList(266, 10, 0.000000, 0), // great_pata
		new BuySellList(286, 10, 0.000000, 0), // eminence_bow
		new BuySellList(299, 10, 0.000000, 0), // orcish_poleaxe
		new BuySellList(5286, 10, 0.000000, 0), // berserker_blade
		new BuySellList(7897, 10, 0.000000, 0) // dwarven_hammer
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(11614, 10, 0.000000, 0), // saber_low
		new BuySellList(11605, 10, 0.000000, 0), // iron_sword_low
		new BuySellList(11613, 10, 0.000000, 0), // handiwork_dagger_low
		new BuySellList(11617, 10, 0.000000, 0), // assassin_knife_low
		new BuySellList(11606, 10, 0.000000, 0), // strengthening_bow_low
		new BuySellList(11623, 10, 0.000000, 0), // hand_axe_low
		new BuySellList(11626, 10, 0.000000, 0), // heavy_mace_low
		new BuySellList(11618, 10, 0.000000, 0), // work_hammer_low
		new BuySellList(11622, 10, 0.000000, 0), // trident_low
		new BuySellList(11635, 10, 0.000000, 0), // bastard_sword_low
		new BuySellList(11643, 10, 0.000000, 0), // artisan's_sword_low
		new BuySellList(11646, 10, 0.000000, 0), // poniard_dagger_low
		new BuySellList(11632, 10, 0.000000, 0), // long_bow_low
		new BuySellList(11640, 10, 0.000000, 0), // elven_bow_low
		new BuySellList(11629, 10, 0.000000, 0), // dark_elven_bow_low
		new BuySellList(11645, 10, 0.000000, 0), // tomahawk_low
		new BuySellList(11647, 10, 0.000000, 0), // pike_low
		new BuySellList(11631, 10, 0.000000, 0), // dwarven_trident_low
		new BuySellList(11667, 10, 0.000000, 0), // two-handed_sword_low
		new BuySellList(11675, 10, 0.000000, 0), // crimson_sword_low
		new BuySellList(11670, 10, 0.000000, 0), // elven_sword_low
		new BuySellList(11673, 10, 0.000000, 0), // kukuri_low
		new BuySellList(11659, 10, 0.000000, 0), // gastraphetes_low
		new BuySellList(11666, 10, 0.000000, 0), // spike_club_low
		new BuySellList(11671, 10, 0.000000, 0), // war_hammer_low
		new BuySellList(11661, 10, 0.000000, 0), // dwarven_pike_low
		new BuySellList(11714, 10, 0.000000, 0), // sword_of_revolution_low
		new BuySellList(11685, 10, 0.000000, 0), // maingauche_low
		new BuySellList(11706, 10, 0.000000, 0), // cursed_maingauche_low
		new BuySellList(11683, 10, 0.000000, 0), // strengthening_long_bow_low
		new BuySellList(11708, 10, 0.000000, 0), // tarbar_low
		new BuySellList(11710, 10, 0.000000, 0), // giants_sword_low
		new BuySellList(11711, 10, 0.000000, 0), // giants_hammer_low
		new BuySellList(11713, 10, 0.000000, 0), // heavy_bone_club_low
		new BuySellList(11712, 10, 0.000000, 0), // hammer_in_flames_low
		new BuySellList(11686, 10, 0.000000, 0), // morning_star_low
		new BuySellList(11703, 10, 0.000000, 0), // winged_spear_low
		new BuySellList(11608, 10, 0.000000, 0), // bagh-nakh_low
		new BuySellList(11642, 10, 0.000000, 0), // single-edged_jamadhr_low
		new BuySellList(11665, 10, 0.000000, 0), // triple-edged_jamadhr_low
		new BuySellList(11691, 10, 0.000000, 0), // bich'hwa_low
		new BuySellList(11625, 10, 0.000000, 0), // heavy_sword_low
		new BuySellList(11736, 10, 0.000000, 0), // claymore_low
		new BuySellList(11727, 10, 0.000000, 0), // bonebreaker_low
		new BuySellList(11726, 10, 0.000000, 0), // mithril_dagger_low
		new BuySellList(11730, 10, 0.000000, 0), // scallop_jamadhr_low
		new BuySellList(11728, 10, 0.000000, 0), // cyclone_bow_low
		new BuySellList(11725, 10, 0.000000, 0), // glaive_low
		new BuySellList(11733, 10, 0.000000, 0), // elven_long_sword_low
		new BuySellList(11747, 10, 0.000000, 0), // darkelven_dagger_low
		new BuySellList(11763, 10, 0.000000, 0), // chakram_low
		new BuySellList(11759, 10, 0.000000, 0), // crystallized_ice_bow_low
		new BuySellList(11760, 10, 0.000000, 0), // orcish_glaive_low
		new BuySellList(11749, 10, 0.000000, 0), // body_slasher_low
		new BuySellList(11783, 10, 0.000000, 0), // shamshir_low
		new BuySellList(11794, 10, 0.000000, 0), // katana_low
		new BuySellList(11789, 10, 0.000000, 0), // spirits_sword_low
		new BuySellList(11778, 10, 0.000000, 0), // raid_sword_low
		new BuySellList(11788, 10, 0.000000, 0), // stiletto_low
		new BuySellList(11777, 10, 0.000000, 0), // dwarven_warhammer_low
		new BuySellList(11782, 10, 0.000000, 0), // bech_de_corbin_low
		new BuySellList(11790, 10, 0.000000, 0), // elemental_bow_low
		new BuySellList(11775, 10, 0.000000, 0), // noble_elven_bow_low
		new BuySellList(11776, 10, 0.000000, 0), // knuckle_dust_low
		new BuySellList(11821, 10, 0.000000, 0), // caliburs_low
		new BuySellList(11807, 10, 0.000000, 0), // sword_of_delusion_low
		new BuySellList(11820, 10, 0.000000, 0), // tsurugi_low
		new BuySellList(11801, 10, 0.000000, 0), // sword_of_limit_low
		new BuySellList(11815, 10, 0.000000, 0), // sword_of_nightmare_low
		new BuySellList(11816, 10, 0.000000, 0), // war_axe_low
		new BuySellList(11800, 10, 0.000000, 0), // grace_dagger_low
		new BuySellList(11803, 10, 0.000000, 0), // dark_screamer_low
		new BuySellList(11826, 10, 0.000000, 0), // poleaxe_low
		new BuySellList(11827, 10, 0.000000, 0), // fist_blade_low
		new BuySellList(11814, 10, 0.000000, 0), // akat_long_bow_low
		new BuySellList(11813, 10, 0.000000, 0), // sword_of_paagrio_low
		new BuySellList(11822, 10, 0.000000, 0), // horn_of_karik_low
		new BuySellList(11830, 10, 0.000000, 0), // scorpion_low
		new BuySellList(11832, 10, 0.000000, 0), // widow_maker_low
		new BuySellList(11853, 10, 0.000000, 0), // samurai_longsword_low
		new BuySellList(11863, 10, 0.000000, 0), // crystal_dagger_low
		new BuySellList(11856, 10, 0.000000, 0), // yaksa_mace_low
		new BuySellList(11839, 10, 0.000000, 0), // great_pata_low
		new BuySellList(11857, 10, 0.000000, 0), // eminence_bow_low
		new BuySellList(11858, 10, 0.000000, 0), // orcish_poleaxe_low
		new BuySellList(11843, 10, 0.000000, 0), // berserker_blade_low
		new BuySellList(11840, 10, 0.000000, 0) // dwarven_hammer_low
	};
	
	private static final int npcId = 30084;
	
	public Grabner() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "grabner001.htm";
		super.fnYouAreChaotic = "grabner006.htm";
	}
}