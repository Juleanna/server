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
package com.l2jserver.datapack.ai.npc.Merchant.Nils;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Nils extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(2, 20, 0.000000, 0), // long_sword
		new BuySellList(218, 20, 0.000000, 0), // throw_knife
		new BuySellList(272, 20, 0.000000, 0), // bow_of_forest
		new BuySellList(15, 20, 0.000000, 0), // short_spear
		new BuySellList(68, 20, 0.000000, 0), // falchion
		new BuySellList(219, 20, 0.000000, 0), // sword_breaker
		new BuySellList(273, 20, 0.000000, 0), // composition_bow
		new BuySellList(155, 20, 0.000000, 0), // buzdygan
		new BuySellList(87, 20, 0.000000, 0), // iron_hammer
		new BuySellList(16, 20, 0.000000, 0), // long_spear
		new BuySellList(123, 20, 0.000000, 0), // saber
		new BuySellList(7880, 20, 0.000000, 0), // iron_sword
		new BuySellList(220, 20, 0.000000, 0), // handiwork_dagger
		new BuySellList(221, 20, 0.000000, 0), // assassin_knife
		new BuySellList(274, 20, 0.000000, 0), // strengthening_bow
		new BuySellList(156, 20, 0.000000, 0), // hand_axe
		new BuySellList(166, 20, 0.000000, 0), // heavy_mace
		new BuySellList(168, 20, 0.000000, 0), // work_hammer
		new BuySellList(291, 20, 0.000000, 0), // trident
		new BuySellList(69, 20, 0.000000, 0), // bastard_sword
		new BuySellList(126, 20, 0.000000, 0), // artisan's_sword
		new BuySellList(222, 20, 0.000000, 0), // poniard_dagger
		new BuySellList(275, 20, 0.000000, 0), // long_bow
		new BuySellList(276, 20, 0.000000, 0), // elven_bow
		new BuySellList(277, 20, 0.000000, 0), // dark_elven_bow
		new BuySellList(86, 20, 0.000000, 0), // tomahawk
		new BuySellList(292, 20, 0.000000, 0), // pike
		new BuySellList(295, 20, 0.000000, 0), // dwarven_trident
		new BuySellList(124, 20, 0.000000, 0), // two-handed_sword
		new BuySellList(127, 20, 0.000000, 0), // crimson_sword
		new BuySellList(130, 20, 0.000000, 0), // elven_sword
		new BuySellList(223, 20, 0.000000, 0), // kukuri
		new BuySellList(278, 20, 0.000000, 0), // gastraphetes
		new BuySellList(157, 20, 0.000000, 0), // spike_club
		new BuySellList(293, 20, 0.000000, 0), // war_hammer
		new BuySellList(296, 20, 0.000000, 0), // dwarven_pike
		new BuySellList(129, 20, 0.000000, 0), // sword_of_revolution
		new BuySellList(224, 20, 0.000000, 0), // maingauche
		new BuySellList(1660, 20, 0.000000, 0), // cursed_maingauche
		new BuySellList(279, 20, 0.000000, 0), // strengthening_long_bow
		new BuySellList(158, 20, 0.000000, 0), // tarbar
		new BuySellList(7881, 20, 0.000000, 0), // giants_sword
		new BuySellList(7896, 20, 0.000000, 0), // giants_hammer
		new BuySellList(172, 20, 0.000000, 0), // heavy_bone_club
		new BuySellList(294, 20, 0.000000, 0), // hammer_in_flames
		new BuySellList(88, 20, 0.000000, 0), // morning_star
		new BuySellList(93, 20, 0.000000, 0), // winged_spear
		new BuySellList(256, 20, 0.000000, 0), // cestus
		new BuySellList(257, 20, 0.000000, 0), // viper's_canine
		new BuySellList(258, 20, 0.000000, 0), // bagh-nakh
		new BuySellList(259, 20, 0.000000, 0), // single-edged_jamadhr
		new BuySellList(260, 20, 0.000000, 0), // triple-edged_jamadhr
		new BuySellList(261, 20, 0.000000, 0), // bich'hwa
		new BuySellList(70, 20, 0.000000, 0), // claymore
		new BuySellList(159, 20, 0.000000, 0), // bonebreaker
		new BuySellList(225, 20, 0.000000, 0), // mithril_dagger
		new BuySellList(262, 20, 0.000000, 0), // scallop_jamadhr
		new BuySellList(280, 20, 0.000000, 0), // cyclone_bow
		new BuySellList(297, 20, 0.000000, 0), // glaive
		new BuySellList(2499, 20, 0.000000, 0), // elven_long_sword
		new BuySellList(5284, 20, 0.000000, 0), // zweihander
		new BuySellList(5285, 20, 0.000000, 0), // heavy_sword
		new BuySellList(232, 20, 0.000000, 0), // darkelven_dagger
		new BuySellList(263, 20, 0.000000, 0), // chakram
		new BuySellList(281, 20, 0.000000, 0), // crystallized_ice_bow
		new BuySellList(298, 20, 0.000000, 0), // orcish_glaive
		new BuySellList(302, 20, 0.000000, 0), // body_slasher
		new BuySellList(73, 20, 0.000000, 0), // shamshir
		new BuySellList(74, 20, 0.000000, 0), // katana
		new BuySellList(131, 20, 0.000000, 0), // spirits_sword
		new BuySellList(133, 20, 0.000000, 0), // raid_sword
		new BuySellList(227, 20, 0.000000, 0), // stiletto
		new BuySellList(2502, 20, 0.000000, 0), // dwarven_warhammer
		new BuySellList(94, 20, 0.000000, 0), // bech_de_corbin
		new BuySellList(282, 20, 0.000000, 0), // elemental_bow
		new BuySellList(285, 20, 0.000000, 0), // noble_elven_bow
		new BuySellList(4233, 20, 0.000000, 0), // knuckle_dust
		new BuySellList(75, 20, 0.000000, 0), // caliburs
		new BuySellList(76, 20, 0.000000, 0), // sword_of_delusion
		new BuySellList(77, 20, 0.000000, 0), // tsurugi
		new BuySellList(132, 20, 0.000000, 0), // sword_of_limit
		new BuySellList(134, 20, 0.000000, 0), // sword_of_nightmare
		new BuySellList(162, 20, 0.000000, 0), // war_axe
		new BuySellList(231, 20, 0.000000, 0), // grace_dagger
		new BuySellList(233, 20, 0.000000, 0), // dark_screamer
		new BuySellList(95, 20, 0.000000, 0), // poleaxe
		new BuySellList(265, 20, 0.000000, 0), // fist_blade
		new BuySellList(283, 20, 0.000000, 0), // akat_long_bow
		new BuySellList(7882, 20, 0.000000, 0), // sword_of_paagrio
		new BuySellList(7898, 20, 0.000000, 0), // horn_of_karik
		new BuySellList(301, 20, 0.000000, 0), // scorpion
		new BuySellList(303, 20, 0.000000, 0), // widow_maker
		new BuySellList(135, 20, 0.000000, 0), // samurai_longsword
		new BuySellList(228, 20, 0.000000, 0), // crystal_dagger
		new BuySellList(2503, 20, 0.000000, 0), // yaksa_mace
		new BuySellList(266, 20, 0.000000, 0), // great_pata
		new BuySellList(286, 20, 0.000000, 0), // eminence_bow
		new BuySellList(299, 20, 0.000000, 0), // orcish_poleaxe
		new BuySellList(5286, 20, 0.000000, 0), // berserker_blade
		new BuySellList(7897, 20, 0.000000, 0) // dwarven_hammer
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(176, 20, 0.000000, 0), // apprentice's_staff
		new BuySellList(310, 20, 0.000000, 0), // relic_of_saints
		new BuySellList(177, 20, 0.000000, 0), // mage_staff
		new BuySellList(311, 20, 0.000000, 0), // crucifix_of_blessing
		new BuySellList(100, 20, 0.000000, 0), // voodoo_doll
		new BuySellList(178, 20, 0.000000, 0), // bone_staff
		new BuySellList(101, 20, 0.000000, 0), // scroll_of_wisdom
		new BuySellList(7885, 20, 0.000000, 0), // sword_of_priest
		new BuySellList(312, 20, 0.000000, 0), // branch_of_life
		new BuySellList(314, 20, 0.000000, 0), // proof_of_revenge
		new BuySellList(179, 20, 0.000000, 0), // mace_of_prayer
		new BuySellList(182, 20, 0.000000, 0), // doom_hammer
		new BuySellList(183, 20, 0.000000, 0), // mystic_staff
		new BuySellList(185, 20, 0.000000, 0), // staff_of_mana
		new BuySellList(315, 20, 0.000000, 0), // divine_tome
		new BuySellList(83, 20, 0.000000, 0), // sword_of_magic
		new BuySellList(143, 20, 0.000000, 0), // sword_of_mystic
		new BuySellList(144, 20, 0.000000, 0), // sword_of_occult
		new BuySellList(238, 20, 0.000000, 0), // dagger_of_mana
		new BuySellList(239, 20, 0.000000, 0), // mystic_knife
		new BuySellList(240, 20, 0.000000, 0), // conjure_knife
		new BuySellList(241, 20, 0.000000, 0), // knife_o'_silenus
		new BuySellList(186, 20, 0.000000, 0), // staff_of_magicpower
		new BuySellList(316, 20, 0.000000, 0), // blood_of_saints
		new BuySellList(317, 20, 0.000000, 0), // tome_of_blood
		new BuySellList(90, 20, 0.000000, 0), // goathead_staff
		new BuySellList(7886, 20, 0.000000, 0), // sword_of_magic_fog
		new BuySellList(7890, 20, 0.000000, 0), // mace_of_priest
		new BuySellList(318, 20, 0.000000, 0), // crucifix_of_blood
		new BuySellList(321, 20, 0.000000, 0), // demon_fangs
		new BuySellList(187, 20, 0.000000, 0), // atuba_hammer
		new BuySellList(188, 20, 0.000000, 0), // ghost_staff
		new BuySellList(189, 20, 0.000000, 0), // life_stick
		new BuySellList(190, 20, 0.000000, 0), // atuba_mace
		new BuySellList(192, 20, 0.000000, 0), // crystal_staff
		new BuySellList(193, 20, 0.000000, 0), // stick_of_faith
		new BuySellList(7887, 20, 0.000000, 0), // mystery_sword
		new BuySellList(242, 20, 0.000000, 0), // dagger_of_magicflame
		new BuySellList(325, 20, 0.000000, 0), // horn_of_glory
		new BuySellList(195, 20, 0.000000, 0), // cursed_staff
		new BuySellList(84, 20, 0.000000, 0), // homunkulus's_sword
		new BuySellList(145, 20, 0.000000, 0), // deathbreath_sword
		new BuySellList(174, 20, 0.000000, 0), // nirvana_axe
		new BuySellList(196, 20, 0.000000, 0), // stick_of_eternity
		new BuySellList(201, 20, 0.000000, 0), // club_of_nature
		new BuySellList(202, 20, 0.000000, 0), // mace_of_underworld
		new BuySellList(326, 20, 0.000000, 0), // heathen's_book
		new BuySellList(197, 20, 0.000000, 0), // paradia_staff
		new BuySellList(198, 20, 0.000000, 0), // inferno_staff
		new BuySellList(199, 20, 0.000000, 0), // paagrio_hammer
		new BuySellList(200, 20, 0.000000, 0), // sage's_staff
		new BuySellList(203, 20, 0.000000, 0), // paagrio_axe
		new BuySellList(7888, 20, 0.000000, 0), // sword_of_eclipse
		new BuySellList(7891, 20, 0.000000, 0), // eclipse_axe
		new BuySellList(204, 20, 0.000000, 0), // deadman's_staff
		new BuySellList(205, 20, 0.000000, 0), // ghoul's_staff
		new BuySellList(206, 20, 0.000000, 0) // demon's_staff
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(11614, 20, 0.000000, 0), // saber_low
		new BuySellList(11605, 20, 0.000000, 0), // iron_sword_low
		new BuySellList(11613, 20, 0.000000, 0), // handiwork_dagger_low
		new BuySellList(11617, 20, 0.000000, 0), // assassin_knife_low
		new BuySellList(11606, 20, 0.000000, 0), // strengthening_bow_low
		new BuySellList(11623, 20, 0.000000, 0), // hand_axe_low
		new BuySellList(11626, 20, 0.000000, 0), // heavy_mace_low
		new BuySellList(11618, 20, 0.000000, 0), // work_hammer_low
		new BuySellList(11622, 20, 0.000000, 0), // trident_low
		new BuySellList(11635, 20, 0.000000, 0), // bastard_sword_low
		new BuySellList(11643, 20, 0.000000, 0), // artisan's_sword_low
		new BuySellList(11646, 20, 0.000000, 0), // poniard_dagger_low
		new BuySellList(11632, 20, 0.000000, 0), // long_bow_low
		new BuySellList(11640, 20, 0.000000, 0), // elven_bow_low
		new BuySellList(11629, 20, 0.000000, 0), // dark_elven_bow_low
		new BuySellList(11645, 20, 0.000000, 0), // tomahawk_low
		new BuySellList(11647, 20, 0.000000, 0), // pike_low
		new BuySellList(11631, 20, 0.000000, 0), // dwarven_trident_low
		new BuySellList(11667, 20, 0.000000, 0), // two-handed_sword_low
		new BuySellList(11675, 20, 0.000000, 0), // crimson_sword_low
		new BuySellList(11670, 20, 0.000000, 0), // elven_sword_low
		new BuySellList(11673, 20, 0.000000, 0), // kukuri_low
		new BuySellList(11659, 20, 0.000000, 0), // gastraphetes_low
		new BuySellList(11666, 20, 0.000000, 0), // spike_club_low
		new BuySellList(11671, 20, 0.000000, 0), // war_hammer_low
		new BuySellList(11661, 20, 0.000000, 0), // dwarven_pike_low
		new BuySellList(11714, 20, 0.000000, 0), // sword_of_revolution_low
		new BuySellList(11685, 20, 0.000000, 0), // maingauche_low
		new BuySellList(11706, 20, 0.000000, 0), // cursed_maingauche_low
		new BuySellList(11683, 20, 0.000000, 0), // strengthening_long_bow_low
		new BuySellList(11708, 20, 0.000000, 0), // tarbar_low
		new BuySellList(11710, 20, 0.000000, 0), // giants_sword_low
		new BuySellList(11711, 20, 0.000000, 0), // giants_hammer_low
		new BuySellList(11713, 20, 0.000000, 0), // heavy_bone_club_low
		new BuySellList(11712, 20, 0.000000, 0), // hammer_in_flames_low
		new BuySellList(11686, 20, 0.000000, 0), // morning_star_low
		new BuySellList(11703, 20, 0.000000, 0), // winged_spear_low
		new BuySellList(11608, 20, 0.000000, 0), // bagh-nakh_low
		new BuySellList(11642, 20, 0.000000, 0), // single-edged_jamadhr_low
		new BuySellList(11665, 20, 0.000000, 0), // triple-edged_jamadhr_low
		new BuySellList(11691, 20, 0.000000, 0), // bich'hwa_low
		new BuySellList(11625, 20, 0.000000, 0), // heavy_sword_low
		new BuySellList(11736, 20, 0.000000, 0), // claymore_low
		new BuySellList(11727, 20, 0.000000, 0), // bonebreaker_low
		new BuySellList(11726, 20, 0.000000, 0), // mithril_dagger_low
		new BuySellList(11730, 20, 0.000000, 0), // scallop_jamadhr_low
		new BuySellList(11728, 20, 0.000000, 0), // cyclone_bow_low
		new BuySellList(11725, 20, 0.000000, 0), // glaive_low
		new BuySellList(11733, 20, 0.000000, 0), // elven_long_sword_low
		new BuySellList(11747, 20, 0.000000, 0), // darkelven_dagger_low
		new BuySellList(11763, 20, 0.000000, 0), // chakram_low
		new BuySellList(11759, 20, 0.000000, 0), // crystallized_ice_bow_low
		new BuySellList(11760, 20, 0.000000, 0), // orcish_glaive_low
		new BuySellList(11749, 20, 0.000000, 0), // body_slasher_low
		new BuySellList(11783, 20, 0.000000, 0), // shamshir_low
		new BuySellList(11794, 20, 0.000000, 0), // katana_low
		new BuySellList(11789, 20, 0.000000, 0), // spirits_sword_low
		new BuySellList(11778, 20, 0.000000, 0), // raid_sword_low
		new BuySellList(11788, 20, 0.000000, 0), // stiletto_low
		new BuySellList(11777, 20, 0.000000, 0), // dwarven_warhammer_low
		new BuySellList(11782, 20, 0.000000, 0), // bech_de_corbin_low
		new BuySellList(11790, 20, 0.000000, 0), // elemental_bow_low
		new BuySellList(11775, 20, 0.000000, 0), // noble_elven_bow_low
		new BuySellList(11776, 20, 0.000000, 0), // knuckle_dust_low
		new BuySellList(11821, 20, 0.000000, 0), // caliburs_low
		new BuySellList(11807, 20, 0.000000, 0), // sword_of_delusion_low
		new BuySellList(11820, 20, 0.000000, 0), // tsurugi_low
		new BuySellList(11801, 20, 0.000000, 0), // sword_of_limit_low
		new BuySellList(11815, 20, 0.000000, 0), // sword_of_nightmare_low
		new BuySellList(11816, 20, 0.000000, 0), // war_axe_low
		new BuySellList(11800, 20, 0.000000, 0), // grace_dagger_low
		new BuySellList(11803, 20, 0.000000, 0), // dark_screamer_low
		new BuySellList(11826, 20, 0.000000, 0), // poleaxe_low
		new BuySellList(11827, 20, 0.000000, 0), // fist_blade_low
		new BuySellList(11814, 20, 0.000000, 0), // akat_long_bow_low
		new BuySellList(11813, 20, 0.000000, 0), // sword_of_paagrio_low
		new BuySellList(11822, 20, 0.000000, 0), // horn_of_karik_low
		new BuySellList(11830, 20, 0.000000, 0), // scorpion_low
		new BuySellList(11832, 20, 0.000000, 0), // widow_maker_low
		new BuySellList(11853, 20, 0.000000, 0), // samurai_longsword_low
		new BuySellList(11863, 20, 0.000000, 0), // crystal_dagger_low
		new BuySellList(11856, 20, 0.000000, 0), // yaksa_mace_low
		new BuySellList(11839, 20, 0.000000, 0), // great_pata_low
		new BuySellList(11857, 20, 0.000000, 0), // eminence_bow_low
		new BuySellList(11858, 20, 0.000000, 0), // orcish_poleaxe_low
		new BuySellList(11843, 20, 0.000000, 0), // berserker_blade_low
		new BuySellList(11840, 20, 0.000000, 0) // dwarven_hammer_low
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(11610, 20, 0.000000, 0), // bone_staff_low
		new BuySellList(11619, 20, 0.000000, 0), // scroll_of_wisdom_low
		new BuySellList(11611, 20, 0.000000, 0), // sword_of_priest_low
		new BuySellList(11612, 20, 0.000000, 0), // branch_of_life_low
		new BuySellList(11609, 20, 0.000000, 0), // proof_of_revenge_low
		new BuySellList(11627, 20, 0.000000, 0), // mace_of_prayer_low
		new BuySellList(11630, 20, 0.000000, 0), // doom_hammer_low
		new BuySellList(11634, 20, 0.000000, 0), // mystic_staff_low
		new BuySellList(11633, 20, 0.000000, 0), // staff_of_mana_low
		new BuySellList(11637, 20, 0.000000, 0), // divine_tome_low
		new BuySellList(11655, 20, 0.000000, 0), // sword_of_magic_low
		new BuySellList(11656, 20, 0.000000, 0), // sword_of_mystic_low
		new BuySellList(11657, 20, 0.000000, 0), // sword_of_occult_low
		new BuySellList(11662, 20, 0.000000, 0), // dagger_of_mana_low
		new BuySellList(11664, 20, 0.000000, 0), // mystic_knife_low
		new BuySellList(11672, 20, 0.000000, 0), // conjure_knife_low
		new BuySellList(11660, 20, 0.000000, 0), // knife_o'_silenus_low
		new BuySellList(11663, 20, 0.000000, 0), // staff_of_magicpower_low
		new BuySellList(11678, 20, 0.000000, 0), // blood_of_saints_low
		new BuySellList(11676, 20, 0.000000, 0), // tome_of_blood_low
		new BuySellList(11693, 20, 0.000000, 0), // goathead_staff_low
		new BuySellList(11696, 20, 0.000000, 0), // sword_of_magic_fog_low
		new BuySellList(11692, 20, 0.000000, 0), // mace_of_priest_low
		new BuySellList(11709, 20, 0.000000, 0), // crucifix_of_blood_low
		new BuySellList(11715, 20, 0.000000, 0), // demon_fangs_low
		new BuySellList(11732, 20, 0.000000, 0), // atuba_hammer_low
		new BuySellList(11737, 20, 0.000000, 0), // ghost_staff_low
		new BuySellList(11729, 20, 0.000000, 0), // life_stick_low
		new BuySellList(11731, 20, 0.000000, 0), // atuba_mace_low
		new BuySellList(11764, 20, 0.000000, 0), // crystal_staff_low
		new BuySellList(11757, 20, 0.000000, 0), // stick_of_faith_low
		new BuySellList(11748, 20, 0.000000, 0), // mystery_sword_low
		new BuySellList(11779, 20, 0.000000, 0), // dagger_of_magicflame_low
		new BuySellList(11792, 20, 0.000000, 0), // horn_of_glory_low
		new BuySellList(11793, 20, 0.000000, 0), // cursed_staff_low
		new BuySellList(11829, 20, 0.000000, 0), // homunkulus's_sword_low
		new BuySellList(11805, 20, 0.000000, 0), // deathbreath_sword_low
		new BuySellList(11802, 20, 0.000000, 0), // nirvana_axe_low
		new BuySellList(11799, 20, 0.000000, 0), // stick_of_eternity_low
		new BuySellList(11804, 20, 0.000000, 0), // club_of_nature_low
		new BuySellList(11806, 20, 0.000000, 0), // mace_of_underworld_low
		new BuySellList(11817, 20, 0.000000, 0), // heathen's_book_low
		new BuySellList(11825, 20, 0.000000, 0), // paradia_staff_low
		new BuySellList(11819, 20, 0.000000, 0), // inferno_staff_low
		new BuySellList(11824, 20, 0.000000, 0), // paagrio_hammer_low
		new BuySellList(11828, 20, 0.000000, 0), // sage's_staff_low
		new BuySellList(11833, 20, 0.000000, 0), // paagrio_axe_low
		new BuySellList(11850, 20, 0.000000, 0), // sword_of_eclipse_low
		new BuySellList(11859, 20, 0.000000, 0), // eclipse_axe_low
		new BuySellList(11842, 20, 0.000000, 0), // deadman's_staff_low
		new BuySellList(11838, 20, 0.000000, 0), // ghoul's_staff_low
		new BuySellList(11855, 20, 0.000000, 0) // demon's_staff_low
	};
	
	private static final int npcId = 31301;
	
	public Nils() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		
		super.fnHi = "trader_nils001.htm";
		super.fnYouAreChaotic = "trader_nils006.htm";
	}
}