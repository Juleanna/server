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
package com.l2jserver.datapack.ai.npc.Merchant.Bandor;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Bandor extends Merchant {
	
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
		new BuySellList(1512, 15, 0.000000, 0), // sb_confusion1
		new BuySellList(1379, 15, 0.000000, 0), // sb_cure_bleeding1
		new BuySellList(1415, 15, 0.000000, 0), // sb_dryad_root1
		new BuySellList(1388, 15, 0.000000, 0), // sb_mental_shield1
		new BuySellList(1517, 15, 0.000000, 0), // sb_body_to_mind1
		new BuySellList(4908, 15, 0.000000, 0), // sb_shadow_spark1
		new BuySellList(1417, 15, 0.000000, 0), // sb_surrender_to_earth1
		new BuySellList(1400, 15, 0.000000, 0), // sb_surrender_to_fire1
		new BuySellList(1418, 15, 0.000000, 0), // sb_surrender_to_poison1
		new BuySellList(1668, 15, 0.000000, 0), // sb_summon_cuti_cat1
		new BuySellList(1670, 15, 0.000000, 0), // sb_summon_unicorn_mirage1
		new BuySellList(1404, 15, 0.000000, 0), // sb_servitor_mana_charge1
		new BuySellList(4906, 15, 0.000000, 0), // sb_solar_spark1
		new BuySellList(1402, 15, 0.000000, 0), // sb_agility1
		new BuySellList(1391, 15, 0.000000, 0), // sb_empower1
		new BuySellList(1410, 15, 0.000000, 0), // sb_poison_cloud1
		new BuySellList(1398, 15, 0.000000, 0), // sb_focus1
		new BuySellList(1389, 15, 0.000000, 0), // sb_holy_weapon1
		new BuySellList(1378, 15, 0.000000, 0), // sb_touch_of_god1
		new BuySellList(1414, 15, 0.000000, 0), // sb_fire_resist1
		new BuySellList(1385, 15, 0.000000, 0), // sb_recharge1
		new BuySellList(4910, 15, 0.000000, 0), // sb_vampiric_rage1
		new BuySellList(1394, 15, 0.000000, 0), // sb_sleep1
		new BuySellList(1516, 15, 0.000000, 0), // sb_corpse_life_drain1
		new BuySellList(3944, 15, 0.000000, 0), // sb_decrease_weight1
		new BuySellList(1412, 15, 0.000000, 0), // sb_auqa_resist1
		new BuySellList(1413, 15, 0.000000, 0), // sb_wind_resist1
		new BuySellList(1387, 15, 0.000000, 0), // sb_resist_poison1
		new BuySellList(1390, 15, 0.000000, 0), // sb_regeneration1
		new BuySellList(1408, 15, 0.000000, 0), // sb_mighty_servitor1
		new BuySellList(1392, 15, 0.000000, 0), // sb_berserker_spirit1
		new BuySellList(1407, 15, 0.000000, 0), // sb_bright_servitor1
		new BuySellList(1409, 15, 0.000000, 0), // sb_slow1
		new BuySellList(1416, 15, 0.000000, 0), // sb_curse_bleary1
		new BuySellList(1406, 15, 0.000000, 0), // sb_fast_servitor1
		new BuySellList(1397, 15, 0.000000, 0), // sb_erase_hostility1
		new BuySellList(1384, 15, 0.000000, 0), // sb_speed_walk1
		new BuySellList(1380, 15, 0.000000, 0), // sb_zero_g1
		new BuySellList(1382, 15, 0.000000, 0), // sb_power_break1
		new BuySellList(1381, 15, 0.000000, 0), // sb_freezing_strike1
		new BuySellList(8616, 15, 1.000000, 5), // sb_summon_friend
		new BuySellList(8890, 15, 1.000000, 10), // sb_summon_attract_cubic1
		new BuySellList(8891, 15, 1.000000, 10), // sb_aura_flash1
		new BuySellList(8909, 15, 1.000000, 5), // sb_summon_swoop_cannon1
		new BuySellList(8945, 15, 1.000000, 10), // sb_celestial_shield1
		new BuySellList(8946, 15, 1.000000, 10) // sb_invocation1
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(4445, 15, 0.000000, 0), // dye_s1c3_d
		new BuySellList(4446, 15, 0.000000, 0), // dye_s1d3_d
		new BuySellList(4447, 15, 0.000000, 0), // dye_c1s3_d
		new BuySellList(4448, 15, 0.000000, 0), // dye_c1d3_d
		new BuySellList(4449, 15, 0.000000, 0), // dye_d1s3_d
		new BuySellList(4450, 15, 0.000000, 0), // dye_d1c3_d
		new BuySellList(4451, 15, 0.000000, 0), // dye_i1m3_d
		new BuySellList(4452, 15, 0.000000, 0), // dye_i1w3_d
		new BuySellList(4453, 15, 0.000000, 0), // dye_m1i3_d
		new BuySellList(4454, 15, 0.000000, 0), // dye_m1w3_d
		new BuySellList(4455, 15, 0.000000, 0), // dye_w1i3_d
		new BuySellList(4456, 15, 0.000000, 0), // dye_w1m3_d
		new BuySellList(4457, 15, 0.000000, 0), // dye_s1c2_d
		new BuySellList(4458, 15, 0.000000, 0), // dye_s1d2_d
		new BuySellList(4459, 15, 0.000000, 0), // dye_c1s2_d
		new BuySellList(4460, 15, 0.000000, 0), // dye_c1d2_d
		new BuySellList(4461, 15, 0.000000, 0), // dye_d1s2_d
		new BuySellList(4462, 15, 0.000000, 0), // dye_d1c2_d
		new BuySellList(4463, 15, 0.000000, 0), // dye_i1m2_d
		new BuySellList(4464, 15, 0.000000, 0), // dye_i1w2_d
		new BuySellList(4465, 15, 0.000000, 0), // dye_m1i2_d
		new BuySellList(4466, 15, 0.000000, 0), // dye_m1w2_d
		new BuySellList(4467, 15, 0.000000, 0), // dye_w1i2_d
		new BuySellList(4468, 15, 0.000000, 0), // dye_w1m2_d
		new BuySellList(4481, 15, 0.000000, 0), // dye_s1c3_c
		new BuySellList(4482, 15, 0.000000, 0), // dye_s1d3_c
		new BuySellList(4483, 15, 0.000000, 0), // dye_c1s3_c
		new BuySellList(4484, 15, 0.000000, 0), // dye_c1c3_c
		new BuySellList(4485, 15, 0.000000, 0), // dye_d1s3_c
		new BuySellList(4486, 15, 0.000000, 0), // dye_d1c3_c
		new BuySellList(4487, 15, 0.000000, 0), // dye_i1m3_c
		new BuySellList(4488, 15, 0.000000, 0), // dye_i1w3_c
		new BuySellList(4489, 15, 0.000000, 0), // dye_m1i3_c
		new BuySellList(4490, 15, 0.000000, 0), // dye_m1w3_c
		new BuySellList(4491, 15, 0.000000, 0), // dye_w1i3_c
		new BuySellList(4492, 15, 0.000000, 0), // dye_w1m3_c
		new BuySellList(4493, 15, 0.000000, 0), // dye_s1c2_c
		new BuySellList(4494, 15, 0.000000, 0), // dye_s1d2_c
		new BuySellList(4495, 15, 0.000000, 0), // dye_c1s2_c
		new BuySellList(4496, 15, 0.000000, 0), // dye_c1c2_c
		new BuySellList(4497, 15, 0.000000, 0), // dye_d1s2_c
		new BuySellList(4498, 15, 0.000000, 0), // dye_d1c2_c
		new BuySellList(4499, 15, 0.000000, 0), // dye_i1m2_c
		new BuySellList(4500, 15, 0.000000, 0), // dye_i1w2_c
		new BuySellList(4501, 15, 0.000000, 0), // dye_m1i2_c
		new BuySellList(4502, 15, 0.000000, 0), // dye_m1w2_c
		new BuySellList(4503, 15, 0.000000, 0), // dye_w1i2_c
		new BuySellList(4504, 15, 0.000000, 0), // dye_w1m2_c
		new BuySellList(4505, 15, 0.000000, 0), // dye_s2c4_c
		new BuySellList(4506, 15, 0.000000, 0), // dye_s2d4_c
		new BuySellList(4507, 15, 0.000000, 0), // dye_c2s4_c
		new BuySellList(4508, 15, 0.000000, 0), // dye_c2c4_c
		new BuySellList(4509, 15, 0.000000, 0), // dye_d2s4_c
		new BuySellList(4510, 15, 0.000000, 0), // dye_d2c4_c
		new BuySellList(4511, 15, 0.000000, 0), // dye_i2m4_c
		new BuySellList(4512, 15, 0.000000, 0), // dye_i2w4_c
		new BuySellList(4513, 15, 0.000000, 0), // dye_m2i4_c
		new BuySellList(4514, 15, 0.000000, 0), // dye_m2w4_c
		new BuySellList(4515, 15, 0.000000, 0), // dye_w2i4_c
		new BuySellList(4516, 15, 0.000000, 0), // dye_w2m4_c
		new BuySellList(4517, 15, 0.000000, 0), // dye_s2c3_c
		new BuySellList(4518, 15, 0.000000, 0), // dye_s2d3_c
		new BuySellList(4519, 15, 0.000000, 0), // dye_c2s3_c
		new BuySellList(4520, 15, 0.000000, 0), // dye_c2c3_c
		new BuySellList(4521, 15, 0.000000, 0), // dye_d2s3_c
		new BuySellList(4522, 15, 0.000000, 0), // dye_d2c3_c
		new BuySellList(4523, 15, 0.000000, 0), // dye_i2m3_c
		new BuySellList(4524, 15, 0.000000, 0), // dye_i2w3_c
		new BuySellList(4525, 15, 0.000000, 0), // dye_m2i3_c
		new BuySellList(4526, 15, 0.000000, 0), // dye_m2w3_c
		new BuySellList(4527, 15, 0.000000, 0), // dye_w2i3_c
		new BuySellList(4528, 15, 0.000000, 0), // dye_w2m3_c
		new BuySellList(4529, 15, 0.000000, 0), // dye_s3c5_c
		new BuySellList(4530, 15, 0.000000, 0), // dye_s3d5_c
		new BuySellList(4531, 15, 0.000000, 0), // dye_c3s5_c
		new BuySellList(4532, 15, 0.000000, 0), // dye_c3c5_c
		new BuySellList(4533, 15, 0.000000, 0), // dye_d3s5_c
		new BuySellList(4534, 15, 0.000000, 0), // dye_d3c5_c
		new BuySellList(4535, 15, 0.000000, 0), // dye_i3m5_c
		new BuySellList(4536, 15, 0.000000, 0), // dye_i3w5_c
		new BuySellList(4537, 15, 0.000000, 0), // dye_m3i5_c
		new BuySellList(4538, 15, 0.000000, 0), // dye_m3w5_c
		new BuySellList(4539, 15, 0.000000, 0), // dye_w3i5_c
		new BuySellList(4540, 15, 0.000000, 0), // dye_w3m5_c
		new BuySellList(4541, 15, 0.000000, 0), // dye_s3c4_c
		new BuySellList(4542, 15, 0.000000, 0), // dye_s3d4_c
		new BuySellList(4543, 15, 0.000000, 0), // dye_c3s4_c
		new BuySellList(4544, 15, 0.000000, 0), // dye_c3c4_c
		new BuySellList(4545, 15, 0.000000, 0), // dye_d3s4_c
		new BuySellList(4546, 15, 0.000000, 0), // dye_d3c4_c
		new BuySellList(4547, 15, 0.000000, 0), // dye_i3m4_c
		new BuySellList(4548, 15, 0.000000, 0), // dye_i3w4_c
		new BuySellList(4549, 15, 0.000000, 0), // dye_m3i4_c
		new BuySellList(4550, 15, 0.000000, 0), // dye_m3w4_c
		new BuySellList(4551, 15, 0.000000, 0), // dye_w3i4_c
		new BuySellList(4552, 15, 0.000000, 0), // dye_w3m4_c
		new BuySellList(4565, 15, 0.000000, 0), // dye_s4c6_c
		new BuySellList(4566, 15, 0.000000, 0), // dye_s4d6_c
		new BuySellList(4567, 15, 0.000000, 0), // dye_c4s6_c
		new BuySellList(4568, 15, 0.000000, 0), // dye_c4c6_c
		new BuySellList(4569, 15, 0.000000, 0), // dye_d4s6_c
		new BuySellList(4570, 15, 0.000000, 0), // dye_d4c6_c
		new BuySellList(4571, 15, 0.000000, 0), // dye_i4m6_c
		new BuySellList(4572, 15, 0.000000, 0), // dye_i4w6_c
		new BuySellList(4573, 15, 0.000000, 0), // dye_m4i6_c
		new BuySellList(4574, 15, 0.000000, 0), // dye_m4w6_c
		new BuySellList(4575, 15, 0.000000, 0), // dye_w4i6_c
		new BuySellList(4576, 15, 0.000000, 0), // dye_w4m6_c
		new BuySellList(4577, 15, 0.000000, 0), // dye_s4c5_c
		new BuySellList(4578, 15, 0.000000, 0), // dye_s4d5_c
		new BuySellList(4579, 15, 0.000000, 0), // dye_c4s5_c
		new BuySellList(4580, 15, 0.000000, 0), // dye_c4c5_c
		new BuySellList(4581, 15, 0.000000, 0), // dye_d4s5_c
		new BuySellList(4582, 15, 0.000000, 0), // dye_d4c5_c
		new BuySellList(4583, 15, 0.000000, 0), // dye_i4m5_c
		new BuySellList(4584, 15, 0.000000, 0), // dye_i4w5_c
		new BuySellList(4585, 15, 0.000000, 0), // dye_m4i5_c
		new BuySellList(4586, 15, 0.000000, 0), // dye_m4w5_c
		new BuySellList(4587, 15, 0.000000, 0), // dye_w4i5_c
		new BuySellList(4588, 15, 0.000000, 0) // dye_w4m5_c
	};
	
	private static final int npcId = 30166;
	
	public Bandor() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "bandor001.htm";
		super.fnYouAreChaotic = "bandor006.htm";
	}
}