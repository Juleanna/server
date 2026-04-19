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
package com.l2jserver.datapack.ai.npc.Merchant.Rupert;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Rupert extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1294, 20, 0.000000, 0), // sb_adv_defence_power1
		new BuySellList(1095, 20, 0.000000, 0), // sb_advanced_attack_power1
		new BuySellList(1048, 20, 0.000000, 0), // sb_might1
		new BuySellList(1050, 20, 0.000000, 0), // sb_battle_heal1
		new BuySellList(1051, 20, 0.000000, 0), // sb_vampiric_touch1
		new BuySellList(1049, 20, 0.000000, 0), // sb_ice_bolt1
		new BuySellList(1152, 20, 0.000000, 0), // sb_heal1
		new BuySellList(1054, 20, 0.000000, 0), // sb_group_heal1
		new BuySellList(1058, 20, 0.000000, 0), // sb_shield1
		new BuySellList(1099, 20, 0.000000, 0), // sb_breeze1
		new BuySellList(1098, 20, 0.000000, 0), // sb_wind_walk1
		new BuySellList(1056, 20, 0.000000, 0), // sb_curse:weakness
		new BuySellList(1055, 20, 0.000000, 0), // sb_curse:poison1
		new BuySellList(1053, 20, 0.000000, 0), // sb_cure_poison1
		new BuySellList(1052, 20, 0.000000, 0), // sb_flame_strike1
		new BuySellList(1097, 20, 0.000000, 0), // sb_drain_energy1
		new BuySellList(1096, 20, 0.000000, 0), // sb_elemental_heal1
		new BuySellList(1386, 20, 0.000000, 0), // sb_disrupt_undead1
		new BuySellList(1514, 20, 0.000000, 0), // sb_resurrection1
		new BuySellList(1372, 20, 0.000000, 0), // sb_blaze1
		new BuySellList(1667, 20, 0.000000, 0), // sb_summon_shadow1
		new BuySellList(1671, 20, 0.000000, 0), // sb_summon_silhouette1
		new BuySellList(1669, 20, 0.000000, 0), // sb_summon_unicorn_boxer1
		new BuySellList(1403, 20, 0.000000, 0), // sb_summon_blackcat1
		new BuySellList(1405, 20, 0.000000, 0), // sb_servitor_heal1
		new BuySellList(1370, 20, 0.000000, 0), // sb_aqua_swirl1
		new BuySellList(1401, 20, 0.000000, 0), // sb_arcane_acumen1
		new BuySellList(4916, 20, 0.000000, 0), // sb_energy_bolt1
		new BuySellList(1411, 20, 0.000000, 0), // sb_aura_burn1
		new BuySellList(1513, 20, 0.000000, 0), // sb_charm11
		new BuySellList(1399, 20, 0.000000, 0), // sb_concentration1
		new BuySellList(1515, 20, 0.000000, 0), // sb_water_breathing
		new BuySellList(1371, 20, 0.000000, 0), // sb_twister1
		new BuySellList(1383, 20, 0.000000, 0), // sb_poison1
		new BuySellList(1377, 20, 0.000000, 0), // sb_poison_recovery1
		new BuySellList(1512, 20, 0.000000, 0), // sb_confusion1
		new BuySellList(1379, 20, 0.000000, 0), // sb_cure_bleeding1
		new BuySellList(1415, 20, 0.000000, 0), // sb_dryad_root1
		new BuySellList(1388, 20, 0.000000, 0), // sb_mental_shield1
		new BuySellList(1517, 20, 0.000000, 0), // sb_body_to_mind1
		new BuySellList(4908, 20, 0.000000, 0), // sb_shadow_spark1
		new BuySellList(1417, 20, 0.000000, 0), // sb_surrender_to_earth1
		new BuySellList(1400, 20, 0.000000, 0), // sb_surrender_to_fire1
		new BuySellList(1418, 20, 0.000000, 0), // sb_surrender_to_poison1
		new BuySellList(1668, 20, 0.000000, 0), // sb_summon_cuti_cat1
		new BuySellList(1670, 20, 0.000000, 0), // sb_summon_unicorn_mirage1
		new BuySellList(1404, 20, 0.000000, 0), // sb_servitor_mana_charge1
		new BuySellList(4906, 20, 0.000000, 0), // sb_solar_spark1
		new BuySellList(1402, 20, 0.000000, 0), // sb_agility1
		new BuySellList(1391, 20, 0.000000, 0), // sb_empower1
		new BuySellList(1410, 20, 0.000000, 0), // sb_poison_cloud1
		new BuySellList(1398, 20, 0.000000, 0), // sb_focus1
		new BuySellList(1389, 20, 0.000000, 0), // sb_holy_weapon1
		new BuySellList(1378, 20, 0.000000, 0), // sb_touch_of_god1
		new BuySellList(1414, 20, 0.000000, 0), // sb_fire_resist1
		new BuySellList(1385, 20, 0.000000, 0), // sb_recharge1
		new BuySellList(4910, 20, 0.000000, 0), // sb_vampiric_rage1
		new BuySellList(1394, 20, 0.000000, 0), // sb_sleep1
		new BuySellList(1516, 20, 0.000000, 0), // sb_corpse_life_drain1
		new BuySellList(3944, 20, 0.000000, 0), // sb_decrease_weight1
		new BuySellList(1412, 20, 0.000000, 0), // sb_auqa_resist1
		new BuySellList(1413, 20, 0.000000, 0), // sb_wind_resist1
		new BuySellList(1387, 20, 0.000000, 0), // sb_resist_poison1
		new BuySellList(1390, 20, 0.000000, 0), // sb_regeneration1
		new BuySellList(1408, 20, 0.000000, 0), // sb_mighty_servitor1
		new BuySellList(1392, 20, 0.000000, 0), // sb_berserker_spirit1
		new BuySellList(1407, 20, 0.000000, 0), // sb_bright_servitor1
		new BuySellList(1409, 20, 0.000000, 0), // sb_slow1
		new BuySellList(1416, 20, 0.000000, 0), // sb_curse_bleary1
		new BuySellList(1406, 20, 0.000000, 0), // sb_fast_servitor1
		new BuySellList(1397, 20, 0.000000, 0), // sb_erase_hostility1
		new BuySellList(1384, 20, 0.000000, 0), // sb_speed_walk1
		new BuySellList(1380, 20, 0.000000, 0), // sb_zero_g1
		new BuySellList(1382, 20, 0.000000, 0), // sb_power_break1
		new BuySellList(1381, 20, 0.000000, 0), // sb_freezing_strike1
		new BuySellList(1529, 20, 0.000000, 0), // sb_night_murmur1
		new BuySellList(1525, 20, 0.000000, 0), // sb_blood_lust1
		new BuySellList(1527, 20, 0.000000, 0), // sb_pain_thorn1
		new BuySellList(1524, 20, 0.000000, 0), // sb_devotioin_of_shine1
		new BuySellList(1531, 20, 0.000000, 0), // sb_chill_flame1
		new BuySellList(1522, 20, 0.000000, 0), // sb_mass_frenzy1
		new BuySellList(1526, 20, 0.000000, 0), // sb_external_fear1
		new BuySellList(1534, 20, 0.000000, 0), // sb_entice_madness1
		new BuySellList(1537, 20, 0.000000, 0), // sb_pain_edge1
		new BuySellList(1856, 20, 0.000000, 0), // sb_inspire_life_force1
		new BuySellList(1523, 20, 0.000000, 0), // sb_devotioin_of_soul1
		new BuySellList(1521, 20, 0.000000, 0), // sb_burning_spirit1
		new BuySellList(1535, 20, 0.000000, 0), // sb_blaze_quake1
		new BuySellList(1532, 20, 0.000000, 0), // sb_eternal_flame1
		new BuySellList(1536, 20, 0.000000, 0), // sb_bind_will1
		new BuySellList(1533, 20, 0.000000, 0), // sb_aura_sway1
		new BuySellList(1528, 20, 0.000000, 0), // sb_engrave_seal_of_timid1
		new BuySellList(1518, 20, 0.000000, 0), // sb_pure_inspiration1
		new BuySellList(1519, 20, 0.000000, 0), // sb_power_of_paagrio1
		new BuySellList(1520, 20, 0.000000, 0), // sb_blessing_of_paagrio1
		new BuySellList(1530, 20, 0.000000, 0), // sb_engrave_seal_of_lazy1
		new BuySellList(3038, 20, 0.000000, 0), // sb_summon_mechanic_golem1
		new BuySellList(10025, 20, 0.000000, 0), // sb_fallen_arrow1
		new BuySellList(10026, 20, 0.000000, 0), // sb_fallen_attack1
		new BuySellList(10073, 20, 0.000000, 0), // sb_rapid_attack1
		new BuySellList(10072, 20, 0.000000, 0), // sb_increase_power1
		new BuySellList(10027, 20, 0.000000, 0), // sb_detect_trap1
		new BuySellList(10028, 20, 0.000000, 0), // sb_defuse_trap1
		new BuySellList(10029, 20, 0.000000, 0), // sb_dark_strike1
		new BuySellList(10031, 20, 0.000000, 0), // sb_double_thrust1
		new BuySellList(10032, 20, 0.000000, 0), // sb_abyssal_blaze1
		new BuySellList(10180, 20, 0.000000, 0), // sb_penetrate_short1
		new BuySellList(10181, 20, 0.000000, 0), // sb_erase_mark1
		new BuySellList(10070, 20, 0.000000, 0), // sb_change_weapon1
		new BuySellList(10074, 20, 0.000000, 0), // sb_furious_soul1
		new BuySellList(10033, 20, 0.000000, 0), // sb_dark_explosion1
		new BuySellList(10078, 20, 0.000000, 0), // sb_death_mark1
		new BuySellList(10058, 20, 0.000000, 0), // sb_fire_trap1
		new BuySellList(10077, 20, 0.000000, 0), // sb_fast_shot1
		new BuySellList(10085, 20, 0.000000, 0), // sb_surrender_to_unholy1
		new BuySellList(10094, 20, 0.000000, 0), // sb_rush1
		new BuySellList(10275, 20, 0.000000, 0), // sb_warf1
		new BuySellList(10276, 20, 0.000000, 0), // sb_soul_shock1
		new BuySellList(10075, 20, 0.000000, 0), // sb_sword_shield1
		new BuySellList(10079, 20, 0.000000, 0), // sb_courage1
		new BuySellList(10076, 20, 0.000000, 0), // sb_disarm1
		new BuySellList(8616, 20, 1.000000, 5), // sb_summon_friend
		new BuySellList(8890, 20, 1.000000, 10), // sb_summon_attract_cubic1
		new BuySellList(8891, 20, 1.000000, 10), // sb_aura_flash1
		new BuySellList(8909, 20, 1.000000, 5), // sb_summon_swoop_cannon1
		new BuySellList(8945, 20, 1.000000, 10), // sb_celestial_shield1
		new BuySellList(8946, 20, 1.000000, 10) // sb_invocation1
	};
	
	private static final int npcId = 31262;
	
	public Rupert() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "trader_rupert001.htm";
		super.fnYouAreChaotic = "trader_rupert006.htm";
	}
}