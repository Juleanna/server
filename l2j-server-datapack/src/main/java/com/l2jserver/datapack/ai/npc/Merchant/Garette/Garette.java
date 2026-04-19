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
package com.l2jserver.datapack.ai.npc.Merchant.Garette;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Garette extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(908, 30, 0.000000, 0), // necklace_of_wisdom
		new BuySellList(909, 30, 0.000000, 0), // blue_diamond_necklace
		new BuySellList(910, 30, 0.000000, 0), // necklace_of_devotion
		new BuySellList(845, 30, 0.000000, 0), // cat'seye_earing
		new BuySellList(846, 30, 0.000000, 0), // coral_earing
		new BuySellList(847, 30, 0.000000, 0), // red_cresent_earing
		new BuySellList(877, 30, 0.000000, 0), // ring_of_wisdom
		new BuySellList(878, 30, 0.000000, 0), // blue_coral_ring
		new BuySellList(890, 30, 0.000000, 0) // ring_of_devotion
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1294, 30, 0.000000, 0), // sb_adv_defence_power1
		new BuySellList(1095, 30, 0.000000, 0), // sb_advanced_attack_power1
		new BuySellList(1048, 30, 0.000000, 0), // sb_might1
		new BuySellList(1050, 30, 0.000000, 0), // sb_battle_heal1
		new BuySellList(1051, 30, 0.000000, 0), // sb_vampiric_touch1
		new BuySellList(1049, 30, 0.000000, 0), // sb_ice_bolt1
		new BuySellList(1152, 30, 0.000000, 0), // sb_heal1
		new BuySellList(1054, 30, 0.000000, 0), // sb_group_heal1
		new BuySellList(1058, 30, 0.000000, 0), // sb_shield1
		new BuySellList(1099, 30, 0.000000, 0), // sb_breeze1
		new BuySellList(1098, 30, 0.000000, 0), // sb_wind_walk1
		new BuySellList(1056, 30, 0.000000, 0), // sb_curse:weakness
		new BuySellList(1055, 30, 0.000000, 0), // sb_curse:poison1
		new BuySellList(1053, 30, 0.000000, 0), // sb_cure_poison1
		new BuySellList(1052, 30, 0.000000, 0), // sb_flame_strike1
		new BuySellList(1097, 30, 0.000000, 0), // sb_drain_energy1
		new BuySellList(1096, 30, 0.000000, 0), // sb_elemental_heal1
		new BuySellList(1386, 30, 0.000000, 0), // sb_disrupt_undead1
		new BuySellList(1514, 30, 0.000000, 0), // sb_resurrection1
		new BuySellList(1372, 30, 0.000000, 0), // sb_blaze1
		new BuySellList(1667, 30, 0.000000, 0), // sb_summon_shadow1
		new BuySellList(1671, 30, 0.000000, 0), // sb_summon_silhouette1
		new BuySellList(1669, 30, 0.000000, 0), // sb_summon_unicorn_boxer1
		new BuySellList(1403, 30, 0.000000, 0), // sb_summon_blackcat1
		new BuySellList(1405, 30, 0.000000, 0), // sb_servitor_heal1
		new BuySellList(1370, 30, 0.000000, 0), // sb_aqua_swirl1
		new BuySellList(1401, 30, 0.000000, 0), // sb_arcane_acumen1
		new BuySellList(4916, 30, 0.000000, 0), // sb_energy_bolt1
		new BuySellList(1411, 30, 0.000000, 0), // sb_aura_burn1
		new BuySellList(1513, 30, 0.000000, 0), // sb_charm11
		new BuySellList(1399, 30, 0.000000, 0), // sb_concentration1
		new BuySellList(1515, 30, 0.000000, 0), // sb_water_breathing
		new BuySellList(1371, 30, 0.000000, 0), // sb_twister1
		new BuySellList(1383, 30, 0.000000, 0), // sb_poison1
		new BuySellList(1377, 30, 0.000000, 0), // sb_poison_recovery1
		new BuySellList(1512, 30, 0.000000, 0), // sb_confusion1
		new BuySellList(1379, 30, 0.000000, 0), // sb_cure_bleeding1
		new BuySellList(1415, 30, 0.000000, 0), // sb_dryad_root1
		new BuySellList(1388, 30, 0.000000, 0), // sb_mental_shield1
		new BuySellList(1517, 30, 0.000000, 0), // sb_body_to_mind1
		new BuySellList(4908, 30, 0.000000, 0), // sb_shadow_spark1
		new BuySellList(1417, 30, 0.000000, 0), // sb_surrender_to_earth1
		new BuySellList(1400, 30, 0.000000, 0), // sb_surrender_to_fire1
		new BuySellList(1418, 30, 0.000000, 0), // sb_surrender_to_poison1
		new BuySellList(1668, 30, 0.000000, 0), // sb_summon_cuti_cat1
		new BuySellList(1670, 30, 0.000000, 0), // sb_summon_unicorn_mirage1
		new BuySellList(1404, 30, 0.000000, 0), // sb_servitor_mana_charge1
		new BuySellList(4906, 30, 0.000000, 0), // sb_solar_spark1
		new BuySellList(1402, 30, 0.000000, 0), // sb_agility1
		new BuySellList(1391, 30, 0.000000, 0), // sb_empower1
		new BuySellList(1410, 30, 0.000000, 0), // sb_poison_cloud1
		new BuySellList(1398, 30, 0.000000, 0), // sb_focus1
		new BuySellList(1389, 30, 0.000000, 0), // sb_holy_weapon1
		new BuySellList(1378, 30, 0.000000, 0), // sb_touch_of_god1
		new BuySellList(1414, 30, 0.000000, 0), // sb_fire_resist1
		new BuySellList(1385, 30, 0.000000, 0), // sb_recharge1
		new BuySellList(4910, 30, 0.000000, 0), // sb_vampiric_rage1
		new BuySellList(1394, 30, 0.000000, 0), // sb_sleep1
		new BuySellList(1516, 30, 0.000000, 0), // sb_corpse_life_drain1
		new BuySellList(1529, 30, 0.000000, 0), // sb_night_murmur1
		new BuySellList(1525, 30, 0.000000, 0), // sb_blood_lust1
		new BuySellList(1527, 30, 0.000000, 0), // sb_pain_thorn1
		new BuySellList(1524, 30, 0.000000, 0), // sb_devotioin_of_shine1
		new BuySellList(1531, 30, 0.000000, 0), // sb_chill_flame1
		new BuySellList(1522, 30, 0.000000, 0), // sb_mass_frenzy1
		new BuySellList(1526, 30, 0.000000, 0), // sb_external_fear1
		new BuySellList(1534, 30, 0.000000, 0), // sb_entice_madness1
		new BuySellList(1537, 30, 0.000000, 0), // sb_pain_edge1
		new BuySellList(1856, 30, 0.000000, 0), // sb_inspire_life_force1
		new BuySellList(1523, 30, 0.000000, 0), // sb_devotioin_of_soul1
		new BuySellList(1521, 30, 0.000000, 0), // sb_burning_spirit1
		new BuySellList(1535, 30, 0.000000, 0), // sb_blaze_quake1
		new BuySellList(1532, 30, 0.000000, 0), // sb_eternal_flame1
		new BuySellList(1536, 30, 0.000000, 0), // sb_bind_will1
		new BuySellList(1533, 30, 0.000000, 0), // sb_aura_sway1
		new BuySellList(1528, 30, 0.000000, 0), // sb_engrave_seal_of_timid1
		new BuySellList(1518, 30, 0.000000, 0), // sb_pure_inspiration1
		new BuySellList(1519, 30, 0.000000, 0), // sb_power_of_paagrio1
		new BuySellList(3038, 30, 0.000000, 0), // sb_summon_mechanic_golem1
		new BuySellList(10025, 30, 0.000000, 0), // sb_fallen_arrow1
		new BuySellList(10026, 30, 0.000000, 0), // sb_fallen_attack1
		new BuySellList(10073, 30, 0.000000, 0), // sb_rapid_attack1
		new BuySellList(10072, 30, 0.000000, 0), // sb_increase_power1
		new BuySellList(10027, 30, 0.000000, 0), // sb_detect_trap1
		new BuySellList(10028, 30, 0.000000, 0), // sb_defuse_trap1
		new BuySellList(10029, 30, 0.000000, 0), // sb_dark_strike1
		new BuySellList(10031, 30, 0.000000, 0), // sb_double_thrust1
		new BuySellList(10032, 30, 0.000000, 0), // sb_abyssal_blaze1
		new BuySellList(10180, 30, 0.000000, 0), // sb_penetrate_short1
		new BuySellList(10181, 30, 0.000000, 0), // sb_erase_mark1
		new BuySellList(10070, 30, 0.000000, 0), // sb_change_weapon1
		new BuySellList(10074, 30, 0.000000, 0), // sb_furious_soul1
		new BuySellList(10033, 30, 0.000000, 0), // sb_dark_explosion1
		new BuySellList(10078, 30, 0.000000, 0), // sb_death_mark1
		new BuySellList(10058, 30, 0.000000, 0), // sb_fire_trap1
		new BuySellList(10077, 30, 0.000000, 0), // sb_fast_shot1
		new BuySellList(10085, 30, 0.000000, 0), // sb_surrender_to_unholy1
		new BuySellList(10094, 30, 0.000000, 0), // sb_rush1
		new BuySellList(10275, 30, 0.000000, 0) // sb_warf1
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(12312, 30, 0.000000, 0), // necklace_of_devotion_low
		new BuySellList(12311, 30, 0.000000, 0), // red_cresent_earing_low
		new BuySellList(12313, 30, 0.000000, 0) // ring_of_devotion_low
	};
	
	private static final int npcId = 30231;
	
	public Garette() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		
		super.fnHi = "trader_garette001.htm";
		super.fnYouAreChaotic = "trader_garette006.htm";
	}
}