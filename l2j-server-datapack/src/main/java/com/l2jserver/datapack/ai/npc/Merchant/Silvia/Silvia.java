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
package com.l2jserver.datapack.ai.npc.Merchant.Silvia;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Silvia extends MerchantForNewbie {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(118, 15, 0.000000, 0), // necklace_of_magic
		new BuySellList(906, 15, 0.000000, 0), // necklace_of_knowledge
		new BuySellList(907, 15, 0.000000, 0), // necklace_of_anguish
		new BuySellList(908, 15, 0.000000, 0), // necklace_of_wisdom
		new BuySellList(112, 15, 0.000000, 0), // apprentice's_earing
		new BuySellList(113, 15, 0.000000, 0), // mage_earing
		new BuySellList(114, 15, 0.000000, 0), // earing_of_strength
		new BuySellList(115, 15, 0.000000, 0), // earing_of_wisdom
		new BuySellList(845, 15, 0.000000, 0), // cat'seye_earing
		new BuySellList(116, 15, 0.000000, 0), // magic_ring
		new BuySellList(875, 15, 0.000000, 0), // ring_of_knowledge
		new BuySellList(876, 15, 0.000000, 0), // ring_of_anguish
		new BuySellList(877, 15, 0.000000, 0) // ring_of_wisdom
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1294, 15, 0.000000, 0), // sb_adv_defence_power1
		new BuySellList(1095, 15, 0.000000, 0), // sb_advanced_attack_power1
		new BuySellList(1048, 15, 0.000000, 0), // sb_might1
		new BuySellList(1050, 15, 0.000000, 0), // sb_battle_heal1
		new BuySellList(1051, 15, 0.000000, 0), // sb_vampiric_touch1
		new BuySellList(1049, 15, 0.000000, 0), // sb_ice_bolt1
		new BuySellList(1152, 15, 0.000000, 0), // sb_heal1
		new BuySellList(1054, 15, 0.000000, 0), // sb_group_heal1
		new BuySellList(1058, 15, 0.000000, 0), // sb_shield1
		new BuySellList(1099, 15, 0.000000, 0), // sb_breeze1
		new BuySellList(1098, 15, 0.000000, 0), // sb_wind_walk1
		new BuySellList(1056, 15, 0.000000, 0), // sb_curse:weakness
		new BuySellList(1055, 15, 0.000000, 0), // sb_curse:poison1
		new BuySellList(1053, 15, 0.000000, 0), // sb_cure_poison1
		new BuySellList(1052, 15, 0.000000, 0), // sb_flame_strike1
		new BuySellList(1097, 15, 0.000000, 0), // sb_drain_energy1
		new BuySellList(1096, 15, 0.000000, 0), // sb_elemental_heal1
		new BuySellList(1386, 15, 0.000000, 0), // sb_disrupt_undead1
		new BuySellList(1514, 15, 0.000000, 0), // sb_resurrection1
		new BuySellList(1372, 15, 0.000000, 0), // sb_blaze1
		new BuySellList(1667, 15, 0.000000, 0), // sb_summon_shadow1
		new BuySellList(1671, 15, 0.000000, 0), // sb_summon_silhouette1
		new BuySellList(1669, 15, 0.000000, 0), // sb_summon_unicorn_boxer1
		new BuySellList(1403, 15, 0.000000, 0), // sb_summon_blackcat1
		new BuySellList(1405, 15, 0.000000, 0), // sb_servitor_heal1
		new BuySellList(1370, 15, 0.000000, 0), // sb_aqua_swirl1
		new BuySellList(1401, 15, 0.000000, 0), // sb_arcane_acumen1
		new BuySellList(4916, 15, 0.000000, 0), // sb_energy_bolt1
		new BuySellList(1411, 15, 0.000000, 0), // sb_aura_burn1
		new BuySellList(1513, 15, 0.000000, 0), // sb_charm11
		new BuySellList(1399, 15, 0.000000, 0), // sb_concentration1
		new BuySellList(1515, 15, 0.000000, 0), // sb_water_breathing
		new BuySellList(1371, 15, 0.000000, 0), // sb_twister1
		new BuySellList(1383, 15, 0.000000, 0), // sb_poison1
		new BuySellList(1377, 15, 0.000000, 0), // sb_poison_recovery1
		new BuySellList(3038, 15, 0.000000, 0), // sb_summon_mechanic_golem1
		new BuySellList(10025, 15, 0.000000, 0), // sb_fallen_arrow1
		new BuySellList(10026, 15, 0.000000, 0), // sb_fallen_attack1
		new BuySellList(10073, 15, 0.000000, 0), // sb_rapid_attack1
		new BuySellList(10072, 15, 0.000000, 0), // sb_increase_power1
		new BuySellList(10027, 15, 0.000000, 0), // sb_detect_trap1
		new BuySellList(10028, 15, 0.000000, 0), // sb_defuse_trap1
		new BuySellList(3100, 15, 0.000000, 0), // sb_guidance1
		new BuySellList(3431, 15, 0.000000, 0), // sb_greater_group_heal11
		new BuySellList(3430, 15, 0.000000, 0), // sb_greater_battle_heal11
		new BuySellList(3429, 15, 0.000000, 0), // sb_greater_heal11
		new BuySellList(3101, 15, 0.000000, 0), // sb_death_whisper1
		new BuySellList(3042, 15, 0.000000, 0), // sb_life_scavenge1
		new BuySellList(3071, 15, 0.000000, 0), // sb_requiem1
		new BuySellList(3432, 15, 0.000000, 0), // sb_remedy1
		new BuySellList(3098, 15, 0.000000, 0), // sb_return1
		new BuySellList(3048, 15, 0.000000, 0), // sb_reflect_damage1
		new BuySellList(3073, 15, 0.000000, 0), // sb_might_of_heaven11
		new BuySellList(3941, 15, 0.000000, 0), // sb_mass_ressurection1
		new BuySellList(7638, 15, 0.000000, 0), // sb_mass_summon_storm_cubic
		new BuySellList(3067, 15, 0.000000, 0), // sb_vampiric_claw11
		new BuySellList(3102, 15, 0.000000, 0), // sb_bless_shield1
		new BuySellList(3075, 15, 0.000000, 0), // sb_blazing_circle1
		new BuySellList(3077, 15, 0.000000, 0), // sb_blazing_skin1
		new BuySellList(3064, 15, 0.000000, 0), // sb_silence1
		new BuySellList(3074, 15, 0.000000, 0), // sb_surrender_to_wind1
		new BuySellList(3056, 15, 0.000000, 0), // sb_summon_dark_panther1
		new BuySellList(4922, 15, 0.000000, 0), // sb_summon_binding_cubic1
		new BuySellList(3039, 15, 0.000000, 0), // sb_summon_storm_cubic1
		new BuySellList(4918, 15, 0.000000, 0), // sb_summon_kai_the_cat1
		new BuySellList(3058, 15, 0.000000, 0), // sb_summon_zombi1
		new BuySellList(5813, 15, 0.000000, 0), // sb_servitor_cure1
		new BuySellList(3092, 15, 0.000000, 0), // sb_servitor_physical_shield1
		new BuySellList(4917, 15, 0.000000, 0), // sb_aura_bolt1
		new BuySellList(3081, 15, 0.000000, 0), // sb_aura_flare11
		new BuySellList(3094, 15, 0.000000, 0), // sb_invigor1
		new BuySellList(3061, 15, 0.000000, 0), // sb_curse_discord1
		new BuySellList(3062, 15, 0.000000, 0), // sb_curse_fear1
		new BuySellList(4206, 15, 0.000000, 0), // sb_transfer_pain
		new BuySellList(3076, 15, 0.000000, 0), // sb_prominence11
		new BuySellList(3053, 15, 0.000000, 0), // sb_holy_blessing11
		new BuySellList(3047, 15, 0.000000, 0), // sb_iron_will1
		new BuySellList(3052, 15, 0.000000, 0), // sb_hamstring1
		new BuySellList(3065, 15, 0.000000, 0), // sb_death_spike1
		new BuySellList(4200, 15, 0.000000, 0), // sb_restore_life1
		new BuySellList(3069, 15, 0.000000, 0), // sb_repose1
		new BuySellList(3095, 15, 0.000000, 0), // sb_magic_barrier1
		new BuySellList(3096, 15, 0.000000, 0), // sb_bless_the_body1
		new BuySellList(3097, 15, 0.000000, 0), // sb_bless_the_soul1
		new BuySellList(3057, 15, 0.000000, 0), // sb_summon_skeletonwarrior1
		new BuySellList(3091, 15, 0.000000, 0), // sb_servitor_magic_shield1
		new BuySellList(3093, 15, 0.000000, 0), // sb_servitor_haste1
		new BuySellList(3080, 15, 0.000000, 0), // sb_sleeping_cloud1
		new BuySellList(3063, 15, 0.000000, 0), // sb_anchor1
		new BuySellList(4913, 15, 0.000000, 0), // sb_word_of_fear1
		new BuySellList(4208, 15, 0.000000, 0), // sb_curse_gloom
		new BuySellList(3060, 15, 0.000000, 0), // sb_forget
		new BuySellList(3072, 15, 0.000000, 0), // sb_purify1
		new BuySellList(3099, 15, 0.000000, 0), // sb_haste1
		new BuySellList(3049, 15, 0.000000, 0), // sb_corpse_plague1
		new BuySellList(3044, 15, 0.000000, 0), // sb_horror1
		new BuySellList(3043, 15, 0.000000, 0), // sb_holy_strike1
		new BuySellList(3078, 15, 0.000000, 0), // sb_decay1
		new BuySellList(6398, 15, 0.000000, 0), // sb_body_of_avatar1-6
		new BuySellList(3068, 15, 0.000000, 0), // sb_vitalize1
		new BuySellList(3079, 15, 0.000000, 0), // sb_cancel1
		new BuySellList(3059, 15, 0.000000, 0), // sb_corpse_burst1
		new BuySellList(3070, 15, 0.000000, 0), // sb_hold_undead1
		new BuySellList(3046, 15, 0.000000, 0), // sb_sacrifice1
		new BuySellList(5812, 15, 0.000000, 0), // sb_servitor_empower1
		new BuySellList(3066, 15, 0.000000, 0), // sb_curse_death_link1
		new BuySellList(8398, 15, 0.000000, 0), // sb_mana_burn
		new BuySellList(8396, 15, 0.000000, 0), // sb_magical_backfire
		new BuySellList(8401, 15, 0.000000, 0), // sb_major_heal
		new BuySellList(8380, 15, 0.000000, 0), // sb_betray
		new BuySellList(7644, 15, 0.000000, 0), // sb_summon_cursed_man
		new BuySellList(7641, 15, 0.000000, 0), // sb_summon_queen_of_cat
		new BuySellList(8616, 15, 0.000000, 0), // sb_summon_friend
		new BuySellList(8386, 15, 0.000000, 0), // sb_arcane_disruption
		new BuySellList(8395, 15, 0.000000, 0), // sb_erase
		new BuySellList(8946, 15, 0.000000, 0), // sb_invocation1
		new BuySellList(8400, 15, 0.000000, 0), // sb_turn_undead
		new BuySellList(8394, 15, 0.000000, 0), // sb_trance
		new BuySellList(8388, 15, 0.000000, 0), // sb_greater_might
		new BuySellList(8389, 15, 0.000000, 0), // sb_greater_shield
		new BuySellList(1412, 15, 0.000000, 0), // sb_auqa_resist1
		new BuySellList(8393, 15, 0.000000, 0), // sb_resist_unholy
		new BuySellList(1413, 15, 0.000000, 0), // sb_wind_resist1
		new BuySellList(8392, 15, 0.000000, 0), // sb_resist_holy
		new BuySellList(8399, 15, 0.000000, 0), // sb_mana_storm
		new BuySellList(8383, 15, 0.000000, 0), // sb_mass_surrender_to_fire
		new BuySellList(8382, 15, 0.000000, 0), // sb_mass_curse_gloom
		new BuySellList(8381, 15, 0.000000, 0), // sb_mass_curse_fear
		new BuySellList(8402, 15, 0.000000, 0), // sb_major_group_heal
		new BuySellList(8387, 15, 0.000000, 0), // sb_summon_cursed_bone
		new BuySellList(8891, 15, 0.000000, 0), // sb_aura_flash1
		new BuySellList(5810, 15, 0.000000, 0), // sb_rain_of_fire1
		new BuySellList(4911, 15, 0.000000, 0), // sb_curse_disease1
		new BuySellList(5811, 15, 0.000000, 0), // sb_mass_slow1
		new BuySellList(5814, 15, 0.000000, 0), // sb_servitor_blessing
		new BuySellList(8945, 15, 0.000000, 0), // sb_celestial_shield1
		new BuySellList(4912, 15, 0.000000, 0), // sb_benediction
		new BuySellList(4928, 15, 0.000000, 0), // sb_seed_of_fire
		new BuySellList(6352, 15, 0.000000, 0), // sb_prayer1
		new BuySellList(4931, 15, 0.000000, 0), // sb_aura_symphony1
		new BuySellList(4932, 15, 0.000000, 0), // sb_inferno1
		new BuySellList(5013, 15, 0.000000, 0), // sb_elemental_symphony_h1
		new BuySellList(12772, 15, 0.000000, 0), // sb_inquisitor
	};
	
	private static final int npcId = 30003;
	
	public Silvia() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "silvia001.htm";
		super.fnYouAreChaotic = "silvia006.htm";
	}
}