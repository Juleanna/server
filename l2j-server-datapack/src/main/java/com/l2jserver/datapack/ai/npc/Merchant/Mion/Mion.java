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
package com.l2jserver.datapack.ai.npc.Merchant.Mion;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Mion extends MerchantForNewbie {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1835, 15, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 15, 0.000000, 0), // spiritshot_none
		new BuySellList(3947, 15, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(5146, 15, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 15, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 15, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 15, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 15, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 15, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 15, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 15, 0.000000, 0), // bone_arrow
		new BuySellList(1060, 15, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1831, 15, 0.000000, 0), // antidote
		new BuySellList(1833, 15, 0.000000, 0), // bandage
		new BuySellList(734, 15, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 15, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 15, 0.000000, 0), // potion_of_acumen2
		new BuySellList(736, 15, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 15, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(3031, 15, 0.000000, 0), // spirit_ore
		new BuySellList(1785, 15, 0.000000, 0), // soul_ore
		new BuySellList(5589, 15, 0.000000, 0), // energy_stone
		new BuySellList(1661, 15, 0.000000, 0), // key_of_thief
		new BuySellList(5192, 15, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 15, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 15, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 15, 0.000000, 0), // rope_of_magic_a
		new BuySellList(2130, 15, 0.000000, 0), // gemstone_d
		new BuySellList(9633, 15, 0.000000, 0), // bone_bolt
		new BuySellList(8594, 15, 0.000000, 0), // recovery_scroll_none
		new BuySellList(8595, 15, 0.000000, 0), // recovery_scroll_d
		new BuySellList(8622, 15, 0.000000, 0), // elixir_of_life_none
		new BuySellList(8623, 15, 0.000000, 0), // elixir_of_life_d
		new BuySellList(8628, 15, 0.000000, 0), // elixir_of_mental_none
		new BuySellList(8629, 15, 0.000000, 0), // elixir_of_mental_d
		new BuySellList(8634, 15, 0.000000, 0), // elixir_of_combative_none
		new BuySellList(8635, 15, 0.000000, 0), // elixir_of_combative_d
		new BuySellList(8615, 15, 0.000000, 0), // crystal_of_summon
		new BuySellList(4625, 15, 0.000000, 0), // dice_heart
		new BuySellList(4626, 15, 0.000000, 0), // dice_spade
		new BuySellList(4627, 15, 0.000000, 0), // dice_clover
		new BuySellList(4628, 15, 0.000000, 0), // dice_diamond
		new BuySellList(21746, 15, 0.000000, 0) // g_lucky_key
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
		new BuySellList(3940, 15, 0.000000, 0), // sb_summon_siege_golem
		new BuySellList(4921, 15, 0.000000, 0), // sb_summon_bigboom1
		new BuySellList(4915, 15, 0.000000, 0), // sb_summon_wild_hog_cannon
		new BuySellList(8909, 15, 0.000000, 0), // sb_summon_swoop_cannon1
		new BuySellList(12820, 15, 0.000000, 0) // sb_summon_merchant_golem1
	};
	
	private static final int npcId = 30519;
	
	public Mion() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		
		super.fnHi = "trader_mion001.htm";
		super.fnYouAreChaotic = "trader_mion006.htm";
	}
}