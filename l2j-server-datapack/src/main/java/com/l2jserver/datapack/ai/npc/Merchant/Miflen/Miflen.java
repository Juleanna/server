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
package com.l2jserver.datapack.ai.npc.Merchant.Miflen;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Miflen extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
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
		new BuySellList(10030, 15, 0.000000, 0), // sb_dark_smash1
		new BuySellList(10185, 15, 0.000000, 0), // sb_destroy_instinct1
		new BuySellList(10040, 15, 0.000000, 0), // sb_life_to_soul1
		new BuySellList(10186, 15, 0.000000, 0), // sb_life_restoration1
		new BuySellList(10088, 15, 0.000000, 0), // sb_real_target1
		new BuySellList(10062, 15, 0.000000, 0), // sb_binding_trap1
		new BuySellList(10045, 15, 0.000000, 0), // sb_soul_of_pain1
		new BuySellList(10097, 15, 0.000000, 0), // sb_shoulder_charge1
		new BuySellList(10034, 15, 0.000000, 0), // sb_shift_target1
		new BuySellList(10042, 15, 0.000000, 0), // sb_triple_thrust1
		new BuySellList(10052, 15, 0.000000, 0), // sb_twin_shot1
		new BuySellList(10066, 15, 0.000000, 0), // sb_decoy1
		new BuySellList(10086, 15, 0.000000, 0), // sb_bleed_shot1
		new BuySellList(10081, 15, 0.000000, 0), // sb_soul_to_empower1
		new BuySellList(10041, 15, 0.000000, 0), // sb_scorn1
		new BuySellList(10182, 15, 0.000000, 0), // sb_ultimate_escape1
		new BuySellList(10187, 15, 0.000000, 0), // sb_oblivion1
		new BuySellList(10183, 15, 0.000000, 0), // sb_create_special_bolt1
		new BuySellList(10083, 15, 0.000000, 0), // sb_dark_weapon1
		new BuySellList(10046, 15, 0.000000, 0), // sb_dark_flame1
		new BuySellList(10053, 15, 0.000000, 0), // sb_rise_shot1
		new BuySellList(10037, 15, 0.000000, 0), // sb_blade_slash1
		new BuySellList(10204, 15, 0.000000, 0), // sb_soul_gathering
		new BuySellList(10188, 15, 0.000000, 0), // sb_thin_skin1
		new BuySellList(10189, 15, 0.000000, 0), // sb_protection_instinct1
		new BuySellList(10184, 15, 0.000000, 0), // sb_life_restoration_impact1
		new BuySellList(10080, 15, 0.000000, 0), // sb_violent_temper1
		new BuySellList(10043, 15, 0.000000, 0), // sb_shining_edge1
		new BuySellList(10095, 15, 0.000000, 0), // sb_strom_assault1
		new BuySellList(10055, 15, 0.000000, 0), // sb_temptation1
		new BuySellList(10059, 15, 0.000000, 0), // sb_poison_trap1
		new BuySellList(10084, 15, 0.000000, 0), // sb_pride_of_kamael1
		new BuySellList(10190, 15, 0.000000, 0), // sb_vampire_impulse1
		new BuySellList(10092, 15, 0.000000, 0), // sb_shadow_bind1
		new BuySellList(10036, 15, 0.000000, 0), // sb_spread_wing1
		new BuySellList(10060, 15, 0.000000, 0), // sb_slow_trap1
		new BuySellList(10044, 15, 0.000000, 0), // sb_checkmate1
		new BuySellList(10071, 15, 0.000000, 0), // sb_quiver_of_bolts_b
		new BuySellList(10191, 15, 0.000000, 0), // sb_magic_impulse1
		new BuySellList(10098, 15, 0.000000, 0), // sb_blade_rush1
		new BuySellList(10049, 15, 0.000000, 0), // sb_steal_divinity1
		new BuySellList(10061, 15, 0.000000, 0), // sb_flish_trap1
		new BuySellList(10082, 15, 0.000000, 0), // sb_darkness_protection1
		new BuySellList(10093, 15, 0.000000, 0), // sb_voice_bind1
		new BuySellList(10600, 15, 0.000000, 0), // sb_soul_barrier
		new BuySellList(10047, 15, 0.000000, 0), // sb_annihilation_circle1
		new BuySellList(10050, 15, 0.000000, 0), // sb_blink1
		new BuySellList(10087, 15, 0.000000, 0), // sb_sharpshooting1
		new BuySellList(10192, 15, 0.000000, 0), // sb_enervation1
		new BuySellList(10597, 15, 0.000000, 0), // sb_curse_of_life_flow
		new BuySellList(10063, 15, 0.000000, 0), // sb_quiver_of_bolts_a
		new BuySellList(10038, 15, 0.000000, 0), // sb_crush_of_pain1
		new BuySellList(10193, 15, 0.000000, 0), // sb_weak_constitution1
		new BuySellList(10089, 15, 0.000000, 0), // sb_imbue_dark_seed1
		new BuySellList(10057, 15, 0.000000, 0), // sb_create_dark_seed1
		new BuySellList(10039, 15, 0.000000, 0), // sb_contagion1
		new BuySellList(10065, 15, 0.000000, 0), // sb_cure_dark_seed1
		new BuySellList(10054, 15, 0.000000, 0), // sb_deadly_roulette1
		new BuySellList(10596, 15, 0.000000, 0), // sb_soul_cleanse
		new BuySellList(10194, 15, 0.000000, 0), // sb_spite1
		new BuySellList(10048, 15, 0.000000, 0), // sb_curse_of_divinity1
		new BuySellList(10195, 15, 0.000000, 0), // sb_mental_impoverish1
		new BuySellList(10196, 15, 0.000000, 0), // sb_soul_harmony1
		new BuySellList(10064, 15, 0.000000, 0), // sb_quiver_of_bolts_s
		new BuySellList(12773, 15, 0.000000, 0), // sb_reconstruction_body1
		new BuySellList(12774, 15, 0.000000, 0), // sb_blood_constract1
		new BuySellList(12775, 15, 0.000000, 0), // sb_imbue_bomb_seed1
		new BuySellList(12776, 15, 0.000000, 0), // sb_oblivion_trap1
		new BuySellList(12777, 15, 0.000000, 0), // sb_painkiller1
		new BuySellList(12778, 15, 0.000000, 0) // sb_soul_web1
	};
	
	private static final int npcId = 32169;
	
	public Miflen() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "trader_miflen001.htm";
		super.fnYouAreChaotic = "trader_miflen006.htm";
	}
}