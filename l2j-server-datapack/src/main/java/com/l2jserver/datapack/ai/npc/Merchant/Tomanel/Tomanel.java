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
package com.l2jserver.datapack.ai.npc.Merchant.Tomanel;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Tomanel extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1294, 10, 0.000000, 0), // sb_adv_defence_power1
		new BuySellList(1095, 10, 0.000000, 0), // sb_advanced_attack_power1
		new BuySellList(1048, 10, 0.000000, 0), // sb_might1
		new BuySellList(1050, 10, 0.000000, 0), // sb_battle_heal1
		new BuySellList(1051, 10, 0.000000, 0), // sb_vampiric_touch1
		new BuySellList(1049, 10, 0.000000, 0), // sb_ice_bolt1
		new BuySellList(1152, 10, 0.000000, 0), // sb_heal1
		new BuySellList(1054, 10, 0.000000, 0), // sb_group_heal1
		new BuySellList(1058, 10, 0.000000, 0), // sb_shield1
		new BuySellList(1099, 10, 0.000000, 0), // sb_breeze1
		new BuySellList(1098, 10, 0.000000, 0), // sb_wind_walk1
		new BuySellList(1056, 10, 0.000000, 0), // sb_curse:weakness
		new BuySellList(1055, 10, 0.000000, 0), // sb_curse:poison1
		new BuySellList(1053, 10, 0.000000, 0), // sb_cure_poison1
		new BuySellList(1052, 10, 0.000000, 0), // sb_flame_strike1
		new BuySellList(1097, 10, 0.000000, 0), // sb_drain_energy1
		new BuySellList(1096, 10, 0.000000, 0), // sb_elemental_heal1
		new BuySellList(1386, 10, 0.000000, 0), // sb_disrupt_undead1
		new BuySellList(1514, 10, 0.000000, 0), // sb_resurrection1
		new BuySellList(1372, 10, 0.000000, 0), // sb_blaze1
		new BuySellList(1667, 10, 0.000000, 0), // sb_summon_shadow1
		new BuySellList(1671, 10, 0.000000, 0), // sb_summon_silhouette1
		new BuySellList(1669, 10, 0.000000, 0), // sb_summon_unicorn_boxer1
		new BuySellList(1403, 10, 0.000000, 0), // sb_summon_blackcat1
		new BuySellList(1405, 10, 0.000000, 0), // sb_servitor_heal1
		new BuySellList(1370, 10, 0.000000, 0), // sb_aqua_swirl1
		new BuySellList(1401, 10, 0.000000, 0), // sb_arcane_acumen1
		new BuySellList(4916, 10, 0.000000, 0), // sb_energy_bolt1
		new BuySellList(1411, 10, 0.000000, 0), // sb_aura_burn1
		new BuySellList(1513, 10, 0.000000, 0), // sb_charm11
		new BuySellList(1399, 10, 0.000000, 0), // sb_concentration1
		new BuySellList(1515, 10, 0.000000, 0), // sb_water_breathing
		new BuySellList(1371, 10, 0.000000, 0), // sb_twister1
		new BuySellList(1383, 10, 0.000000, 0), // sb_poison1
		new BuySellList(1377, 10, 0.000000, 0), // sb_poison_recovery1
		new BuySellList(1512, 10, 0.000000, 0), // sb_confusion1
		new BuySellList(1379, 10, 0.000000, 0), // sb_cure_bleeding1
		new BuySellList(1415, 10, 0.000000, 0), // sb_dryad_root1
		new BuySellList(1388, 10, 0.000000, 0), // sb_mental_shield1
		new BuySellList(1517, 10, 0.000000, 0), // sb_body_to_mind1
		new BuySellList(4908, 10, 0.000000, 0), // sb_shadow_spark1
		new BuySellList(1417, 10, 0.000000, 0), // sb_surrender_to_earth1
		new BuySellList(1400, 10, 0.000000, 0), // sb_surrender_to_fire1
		new BuySellList(1418, 10, 0.000000, 0), // sb_surrender_to_poison1
		new BuySellList(1668, 10, 0.000000, 0), // sb_summon_cuti_cat1
		new BuySellList(1670, 10, 0.000000, 0), // sb_summon_unicorn_mirage1
		new BuySellList(1404, 10, 0.000000, 0), // sb_servitor_mana_charge1
		new BuySellList(4906, 10, 0.000000, 0), // sb_solar_spark1
		new BuySellList(1402, 10, 0.000000, 0), // sb_agility1
		new BuySellList(1391, 10, 0.000000, 0), // sb_empower1
		new BuySellList(1410, 10, 0.000000, 0), // sb_poison_cloud1
		new BuySellList(1398, 10, 0.000000, 0), // sb_focus1
		new BuySellList(1389, 10, 0.000000, 0), // sb_holy_weapon1
		new BuySellList(1378, 10, 0.000000, 0), // sb_touch_of_god1
		new BuySellList(1414, 10, 0.000000, 0), // sb_fire_resist1
		new BuySellList(1385, 10, 0.000000, 0), // sb_recharge1
		new BuySellList(4910, 10, 0.000000, 0), // sb_vampiric_rage1
		new BuySellList(1394, 10, 0.000000, 0), // sb_sleep1
		new BuySellList(1516, 10, 0.000000, 0), // sb_corpse_life_drain1
		new BuySellList(3944, 10, 0.000000, 0), // sb_decrease_weight1
		new BuySellList(1412, 10, 0.000000, 0), // sb_auqa_resist1
		new BuySellList(1413, 10, 0.000000, 0), // sb_wind_resist1
		new BuySellList(1387, 10, 0.000000, 0), // sb_resist_poison1
		new BuySellList(1390, 10, 0.000000, 0), // sb_regeneration1
		new BuySellList(1408, 10, 0.000000, 0), // sb_mighty_servitor1
		new BuySellList(1392, 10, 0.000000, 0), // sb_berserker_spirit1
		new BuySellList(1407, 10, 0.000000, 0), // sb_bright_servitor1
		new BuySellList(1409, 10, 0.000000, 0), // sb_slow1
		new BuySellList(1416, 10, 0.000000, 0), // sb_curse_bleary1
		new BuySellList(1406, 10, 0.000000, 0), // sb_fast_servitor1
		new BuySellList(1397, 10, 0.000000, 0), // sb_erase_hostility1
		new BuySellList(1384, 10, 0.000000, 0), // sb_speed_walk1
		new BuySellList(1380, 10, 0.000000, 0), // sb_zero_g1
		new BuySellList(1382, 10, 0.000000, 0), // sb_power_break1
		new BuySellList(1381, 10, 0.000000, 0), // sb_freezing_strike1
		new BuySellList(10025, 10, 0.000000, 0), // sb_fallen_arrow1
		new BuySellList(10026, 10, 0.000000, 0), // sb_fallen_attack1
		new BuySellList(10073, 10, 0.000000, 0), // sb_rapid_attack1
		new BuySellList(10072, 10, 0.000000, 0), // sb_increase_power1
		new BuySellList(10027, 10, 0.000000, 0), // sb_detect_trap1
		new BuySellList(10028, 10, 0.000000, 0), // sb_defuse_trap1
		new BuySellList(10029, 10, 0.000000, 0), // sb_dark_strike1
		new BuySellList(10031, 10, 0.000000, 0), // sb_double_thrust1
		new BuySellList(10032, 10, 0.000000, 0), // sb_abyssal_blaze1
		new BuySellList(10180, 10, 0.000000, 0), // sb_penetrate_short1
		new BuySellList(10181, 10, 0.000000, 0), // sb_erase_mark1
		new BuySellList(10070, 10, 0.000000, 0), // sb_change_weapon1
		new BuySellList(10074, 10, 0.000000, 0), // sb_furious_soul1
		new BuySellList(10033, 10, 0.000000, 0), // sb_dark_explosion1
		new BuySellList(10078, 10, 0.000000, 0), // sb_death_mark1
		new BuySellList(10058, 10, 0.000000, 0), // sb_fire_trap1
		new BuySellList(10077, 10, 0.000000, 0), // sb_fast_shot1
		new BuySellList(10085, 10, 0.000000, 0), // sb_surrender_to_unholy1
		new BuySellList(10094, 10, 0.000000, 0), // sb_rush1
		new BuySellList(10275, 10, 0.000000, 0), // sb_warf1
		new BuySellList(10276, 10, 0.000000, 0), // sb_soul_shock1
		new BuySellList(10075, 10, 0.000000, 0), // sb_sword_shield1
		new BuySellList(10079, 10, 0.000000, 0), // sb_courage1
		new BuySellList(10076, 10, 0.000000, 0) // sb_disarm1
	};
	
	private static final int npcId = 31420;
	
	public Tomanel() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "scroll_seller_tomanel001.htm";
		super.fnYouAreChaotic = "scroll_seller_tomanel006.htm";
	}
}