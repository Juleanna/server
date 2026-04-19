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
package com.l2jserver.datapack.ai.npc.Merchant.Cel;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Cel extends MerchantForNewbie {
	
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
		new BuySellList(3429, 15, 0.000000, 0), // sb_greater_heal11
		new BuySellList(3431, 15, 0.000000, 0), // sb_greater_group_heal11
		new BuySellList(4201, 15, 0.000000, 0), // sb_resist_shock1
		new BuySellList(3098, 15, 0.000000, 0), // sb_return1
		new BuySellList(4207, 15, 0.000000, 0), // sb_mana_regeneration
		new BuySellList(3073, 15, 0.000000, 0), // sb_might_of_heaven11
		new BuySellList(7639, 15, 0.000000, 0), // sb_mass_summon_aqua_cubic
		new BuySellList(3102, 15, 0.000000, 0), // sb_bless_shield1
		new BuySellList(3082, 15, 0.000000, 0), // sb_surrender_to_water1
		new BuySellList(3045, 15, 0.000000, 0), // sb_summon_life_cubic1
		new BuySellList(3039, 15, 0.000000, 0), // sb_summon_storm_cubic1
		new BuySellList(4923, 15, 0.000000, 0), // sb_summon_aqua_cubic1
		new BuySellList(4919, 15, 0.000000, 0), // sb_summon_unicorn_merrow1
		new BuySellList(5813, 15, 0.000000, 0), // sb_servitor_cure1
		new BuySellList(3092, 15, 0.000000, 0), // sb_servitor_physical_shield1
		new BuySellList(4907, 15, 0.000000, 0), // sb_solar_flare1
		new BuySellList(3051, 15, 0.000000, 0), // sb_spirit_barrier1
		new BuySellList(4917, 15, 0.000000, 0), // sb_aura_bolt1
		new BuySellList(3081, 15, 0.000000, 0), // sb_aura_flare11
		new BuySellList(3062, 15, 0.000000, 0), // sb_curse_fear1
		new BuySellList(4206, 15, 0.000000, 0), // sb_transfer_pain
		new BuySellList(3086, 15, 0.000000, 0), // sb_frost_bolt11
		new BuySellList(3088, 15, 0.000000, 0), // sb_freezing_skin1
		new BuySellList(3085, 15, 0.000000, 0), // sb_hydro_blast11
		new BuySellList(3091, 15, 0.000000, 0), // sb_servitor_magic_shield1
		new BuySellList(3093, 15, 0.000000, 0), // sb_servitor_haste1
		new BuySellList(4914, 15, 0.000000, 0), // sb_serenade_of_eva1
		new BuySellList(3080, 15, 0.000000, 0), // sb_sleeping_cloud1
		new BuySellList(3087, 15, 0.000000, 0), // sb_ice_dagger1
		new BuySellList(3084, 15, 0.000000, 0), // sb_freezing_shackle1
		new BuySellList(3068, 15, 0.000000, 0), // sb_vitalize1
		new BuySellList(3079, 15, 0.000000, 0), // sb_cancel1
		new BuySellList(3942, 15, 0.000000, 0), // sb_party_recall1
		new BuySellList(5812, 15, 0.000000, 0), // sb_servitor_empower1
		new BuySellList(3083, 15, 0.000000, 0), // sb_frost_wall1
		new BuySellList(8398, 15, 0.000000, 0), // sb_mana_burn
		new BuySellList(8401, 15, 0.000000, 0), // sb_major_heal
		new BuySellList(8380, 15, 0.000000, 0), // sb_betray
		new BuySellList(7642, 15, 0.000000, 0), // sb_summon_unicorn_seraphim
		new BuySellList(8616, 15, 0.000000, 0), // sb_summon_friend
		new BuySellList(8395, 15, 0.000000, 0), // sb_erase
		new BuySellList(8946, 15, 0.000000, 0), // sb_invocation1
		new BuySellList(8400, 15, 0.000000, 0), // sb_turn_undead
		new BuySellList(8394, 15, 0.000000, 0), // sb_trance
		new BuySellList(1412, 15, 0.000000, 0), // sb_auqa_resist1
		new BuySellList(8393, 15, 0.000000, 0), // sb_resist_unholy
		new BuySellList(8384, 15, 0.000000, 0), // sb_mass_surrender_to_water
		new BuySellList(5809, 15, 0.000000, 0), // sb_aqua_splash1
		new BuySellList(5816, 15, 0.000000, 0), // sb_advanced_block1
		new BuySellList(8891, 15, 0.000000, 0), // sb_aura_flash1
		new BuySellList(8397, 15, 0.000000, 0), // sb_clarity
		new BuySellList(8890, 15, 0.000000, 0), // sb_summon_attract_cubic1
		new BuySellList(5814, 15, 0.000000, 0), // sb_servitor_blessing
		new BuySellList(5815, 15, 0.000000, 0), // sb_wild_magic1
		new BuySellList(4929, 15, 0.000000, 0), // sb_seed_of_water
		new BuySellList(4931, 15, 0.000000, 0), // sb_aura_symphony1
		new BuySellList(4933, 15, 0.000000, 0), // sb_blizzard1
		new BuySellList(5014, 15, 0.000000, 0), // sb_elemental_symphony_e1
		new BuySellList(12772, 15, 0.000000, 0) // sb_inquisitor
	};
	
	private static final int npcId = 30149;
	
	public Cel() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "cel001.htm";
		super.fnYouAreChaotic = "cel006.htm";
	}
}