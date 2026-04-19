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
package com.l2jserver.datapack.ai.npc.Merchant.SalesmanCat;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForChaotic;

/**
* @author Charus
* @version 2.6.3.0
*/
public class SalesmanCat extends MerchantForChaotic {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1463, 100, 0.000000, 0), // soulshot_d
		new BuySellList(1464, 100, 0.000000, 0), // soulshot_c
		new BuySellList(1465, 100, 0.000000, 0), // soulshot_b
		new BuySellList(1466, 100, 0.000000, 0), // soulshot_a
		new BuySellList(2510, 100, 0.000000, 0), // spiritshot_d
		new BuySellList(2511, 100, 0.000000, 0), // spiritshot_c
		new BuySellList(2512, 100, 0.000000, 0), // spiritshot_b
		new BuySellList(2513, 100, 0.000000, 0), // spiritshot_a
		new BuySellList(3947, 100, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(3948, 100, 0.000000, 0), // blessed_spiritshot_d
		new BuySellList(3949, 100, 0.000000, 0), // blessed_spiritshot_c
		new BuySellList(3950, 100, 0.000000, 0), // blessed_spiritshot_b
		new BuySellList(3951, 100, 0.000000, 0), // blessed_spiritshot_a
		new BuySellList(5134, 100, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5135, 100, 0.000000, 0), // comp_soulshot_d
		new BuySellList(5136, 100, 0.000000, 0), // comp_soulshot_c
		new BuySellList(5137, 100, 0.000000, 0), // comp_soulshot_b
		new BuySellList(5138, 100, 0.000000, 0), // comp_soulshot_a
		new BuySellList(5140, 100, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5141, 100, 0.000000, 0), // comp_spiritshot_d
		new BuySellList(5142, 100, 0.000000, 0), // comp_spiritshot_c
		new BuySellList(5143, 100, 0.000000, 0), // comp_spiritshot_b
		new BuySellList(5144, 100, 0.000000, 0), // comp_spiritshot_a
		new BuySellList(5146, 100, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5147, 100, 0.000000, 0), // comp_bspiritshot_d
		new BuySellList(5148, 100, 0.000000, 0), // comp_bspiritshot_c
		new BuySellList(5149, 100, 0.000000, 0), // comp_bspiritshot_b
		new BuySellList(5150, 100, 0.000000, 0), // comp_bspiritshot_a
		new BuySellList(5250, 100, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(5251, 100, 0.000000, 0), // adv_comp_soulshot_d
		new BuySellList(5252, 100, 0.000000, 0), // adv_comp_soulshot_c
		new BuySellList(5253, 100, 0.000000, 0), // adv_comp_soulshot_b
		new BuySellList(5254, 100, 0.000000, 0), // adv_comp_soulshot_a
		new BuySellList(5256, 100, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5257, 100, 0.000000, 0), // adv_comp_spiritshot_d
		new BuySellList(5258, 100, 0.000000, 0), // adv_comp_spiritshot_c
		new BuySellList(5259, 100, 0.000000, 0), // adv_comp_spiritshot_b
		new BuySellList(5260, 100, 0.000000, 0), // adv_comp_spiritshot_a
		new BuySellList(5262, 100, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5263, 100, 0.000000, 0), // adv_comp_bspiritshot_d
		new BuySellList(5264, 100, 0.000000, 0), // adv_comp_bspiritshot_c
		new BuySellList(5265, 100, 0.000000, 0), // adv_comp_bspiritshot_b
		new BuySellList(5266, 100, 0.000000, 0), // adv_comp_bspiritshot_a
		new BuySellList(5126, 100, 0.000000, 0), // dualsword_craft_stamp
		new BuySellList(1458, 100, 0.000000, 0), // crystal_d
		new BuySellList(1459, 100, 0.000000, 0), // crystal_c
		new BuySellList(1460, 100, 0.000000, 0), // crystal_b
		new BuySellList(1461, 100, 0.000000, 0), // crystal_a
		new BuySellList(2130, 100, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 100, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 100, 0.000000, 0), // gemstone_b
		new BuySellList(2133, 100, 0.000000, 0), // gemstone_a
		new BuySellList(1341, 100, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 100, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1343, 100, 0.000000, 0), // silver_arrow
		new BuySellList(1344, 100, 0.000000, 0), // mithril_arrow
		new BuySellList(5589, 100, 0.000000, 0), // energy_stone
		new BuySellList(1864, 100, 0.000000, 0), // stem
		new BuySellList(1865, 100, 0.000000, 0), // varnish
		new BuySellList(1866, 100, 0.000000, 0), // suede
		new BuySellList(1867, 100, 0.000000, 0), // animal_skin
		new BuySellList(1868, 100, 0.000000, 0), // thread
		new BuySellList(1869, 100, 0.000000, 0), // iron_ore
		new BuySellList(1870, 100, 0.000000, 0), // coal
		new BuySellList(1871, 100, 0.000000, 0), // charcoal
		new BuySellList(1872, 100, 0.000000, 0), // animal_bone
		new BuySellList(1873, 100, 0.000000, 0), // silver_nugget
		new BuySellList(1874, 100, 0.000000, 0), // oriharukon_ore
		new BuySellList(1875, 100, 0.000000, 0), // stone_of_purity
		new BuySellList(1876, 100, 0.000000, 0), // mithril_ore
		new BuySellList(1877, 100, 0.000000, 0), // admantite_nugget
		new BuySellList(4039, 100, 0.000000, 0), // mold_glue
		new BuySellList(4040, 100, 0.000000, 0), // mold_lubricant
		new BuySellList(4041, 100, 0.000000, 0), // mold_hardener
		new BuySellList(4042, 100, 0.000000, 0), // enria
		new BuySellList(4043, 100, 0.000000, 0), // asofe
		new BuySellList(4044, 100, 0.000000, 0), // thons
		new BuySellList(1878, 100, 0.000000, 0), // braided_hemp
		new BuySellList(1879, 100, 0.000000, 0), // cokes
		new BuySellList(1880, 100, 0.000000, 0), // steel
		new BuySellList(1881, 100, 0.000000, 0), // coarse_bone_powder
		new BuySellList(1882, 100, 0.000000, 0), // leather
		new BuySellList(5220, 100, 0.000000, 0), // reinforcing_agent
		new BuySellList(5549, 100, 0.000000, 0), // iron_thread
		new BuySellList(1883, 100, 0.000000, 0), // steel_mold
		new BuySellList(1884, 100, 0.000000, 0), // cord
		new BuySellList(1885, 100, 0.000000, 0), // high_grade_suede
		new BuySellList(1886, 100, 0.000000, 0), // silver_mold
		new BuySellList(1887, 100, 0.000000, 0), // varnish_of_purity
		new BuySellList(1888, 100, 0.000000, 0), // synthesis_cokes
		new BuySellList(1889, 100, 0.000000, 0), // compound_braid
		new BuySellList(5550, 100, 0.000000, 0), // reinforcing_plate
		new BuySellList(1890, 100, 0.000000, 0), // mithirl_alloy
		new BuySellList(1891, 100, 0.000000, 0), // artisan's_frame
		new BuySellList(1892, 100, 0.000000, 0), // blacksmith's_frame
		new BuySellList(1893, 100, 0.000000, 0), // oriharukon
		new BuySellList(1894, 100, 0.000000, 0), // crafted_leather
		new BuySellList(1895, 100, 0.000000, 0), // metallic_fiber
		new BuySellList(4045, 100, 0.000000, 0), // maestro_holder
		new BuySellList(4046, 100, 0.000000, 0), // maestro_anvil_lock
		new BuySellList(5551, 100, 0.000000, 0), // reorins_mold
		new BuySellList(4047, 100, 0.000000, 0), // craftsman_mold
		new BuySellList(4048, 100, 0.000000, 0), // maestro_mold
		new BuySellList(5552, 100, 0.000000, 0), // warsmith_mold
		new BuySellList(5553, 100, 0.000000, 0), // arcsmith_anvil
		new BuySellList(5554, 100, 0.000000, 0), // warsmith_holder
		new BuySellList(9633, 100, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 100, 0.000000, 0), // fine_steel_bolt
		new BuySellList(9635, 100, 0.000000, 0), // silver_bolt
		new BuySellList(9636, 100, 0.000000, 0), // mithril_bolt
		new BuySellList(9637, 100, 0.000000, 0) // shining_bolt
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(4177, 100, 0.000000, 0), // rp_shrnoen's_gauntlet
		new BuySellList(4960, 100, 0.000000, 0), // rp_shrnoen's_gauntlet_i
		new BuySellList(4150, 100, 0.000000, 0), // rp_avadon_gloves
		new BuySellList(4953, 100, 0.000000, 0), // rp_avadon_gloves_i
		new BuySellList(4173, 100, 0.000000, 0), // rp_blue_wolve's_gloves
		new BuySellList(4998, 100, 0.000000, 0), // rp_blue_wolve's_gloves_i
		new BuySellList(4168, 100, 0.000000, 0), // rp_doom_gloves
		new BuySellList(4993, 100, 0.000000, 0), // rp_doom_gloves_i
		new BuySellList(4174, 100, 0.000000, 0), // rp_shrnoen's_boots
		new BuySellList(4958, 100, 0.000000, 0), // rp_shrnoen's_boots_i
		new BuySellList(4175, 100, 0.000000, 0), // rp_avadon_boots
		new BuySellList(4959, 100, 0.000000, 0), // rp_avadon_boots_i
		new BuySellList(4167, 100, 0.000000, 0), // rp_blue_wolve's_boots
		new BuySellList(4992, 100, 0.000000, 0), // rp_blue_wolve's_boots_i
		new BuySellList(4176, 100, 0.000000, 0), // rp_doom_boots
		new BuySellList(4999, 100, 0.000000, 0), // rp_doom_boots_i
		new BuySellList(5008, 100, 0.000000, 0), // rp_pata_i
		new BuySellList(4181, 100, 0.000000, 0), // rp_pata
		new BuySellList(4963, 100, 0.000000, 0), // rp_great_sword_i
		new BuySellList(4182, 100, 0.000000, 0), // rp_great_sword
		new BuySellList(4964, 100, 0.000000, 0), // rp_heavy_war_axe_i
		new BuySellList(4183, 100, 0.000000, 0), // rp_heavy_war_axe
		new BuySellList(4965, 100, 0.000000, 0), // rp_sprite's_staff_i
		new BuySellList(4184, 100, 0.000000, 0), // rp_sprite's_staff
		new BuySellList(4966, 100, 0.000000, 0), // rp_kshanberk_i
		new BuySellList(4185, 100, 0.000000, 0), // rp_kshanberk
		new BuySellList(4967, 100, 0.000000, 0), // rp_sword_of_valhalla_i
		new BuySellList(4186, 100, 0.000000, 0), // rp_sword_of_valhalla
		new BuySellList(4968, 100, 0.000000, 0), // rp_kris_i
		new BuySellList(4187, 100, 0.000000, 0), // rp_kris
		new BuySellList(4969, 100, 0.000000, 0), // rp_hell_knife_i
		new BuySellList(4188, 100, 0.000000, 0), // rp_hell_knife
		new BuySellList(4970, 100, 0.000000, 0), // rp_arthro_nail_i
		new BuySellList(4189, 100, 0.000000, 0), // rp_arthro_nail
		new BuySellList(4971, 100, 0.000000, 0), // rp_dark_elven_long_bow_i
		new BuySellList(4190, 100, 0.000000, 0), // rp_dark_elven_long_bow
		new BuySellList(4972, 100, 0.000000, 0), // rp_great_axe_i
		new BuySellList(4191, 100, 0.000000, 0), // rp_great_axe
		new BuySellList(5000, 100, 0.000000, 0), // rp_sword_of_damascus_i
		new BuySellList(4192, 100, 0.000000, 0), // rp_sword_of_damascus
		new BuySellList(5001, 100, 0.000000, 0), // rp_lancia_i
		new BuySellList(4193, 100, 0.000000, 0), // rp_lancia
		new BuySellList(5002, 100, 0.000000, 0), // rp_deadman's_glory_i
		new BuySellList(4194, 100, 0.000000, 0), // rp_deadman's_glory
		new BuySellList(5003, 100, 0.000000, 0), // rp_art_of_battle_axe_i
		new BuySellList(4195, 100, 0.000000, 0), // rp_art_of_battle_axe
		new BuySellList(5004, 100, 0.000000, 0), // rp_staff_of_evil_sprit_i
		new BuySellList(4196, 100, 0.000000, 0), // rp_staff_of_evil_sprit
		new BuySellList(5005, 100, 0.000000, 0), // rp_demon's_sword_i
		new BuySellList(4197, 100, 0.000000, 0), // rp_demon's_sword
		new BuySellList(5006, 100, 0.000000, 0), // rp_bellion_cestus_i
		new BuySellList(4198, 100, 0.000000, 0), // rp_bellion_cestus
		new BuySellList(5007, 100, 0.000000, 0), // rp_hazard_bow_i
		new BuySellList(4199, 100, 0.000000, 0), // rp_hazard_bow
		new BuySellList(4940, 100, 0.000000, 0), // rp_shrnoen's_breastplate_i
		new BuySellList(4133, 100, 0.000000, 0), // rp_shrnoen's_breastplate
		new BuySellList(4941, 100, 0.000000, 0), // rp_shrnoen's_gaiters_i
		new BuySellList(4134, 100, 0.000000, 0), // rp_shrnoen's_gaiters
		new BuySellList(4946, 100, 0.000000, 0), // rp_shrnoen's_leather_shirts_i
		new BuySellList(4143, 100, 0.000000, 0), // rp_shrnoen's_leather_shirts
		new BuySellList(4947, 100, 0.000000, 0), // rp_shrnoen's_leather_gaiters_i
		new BuySellList(4144, 100, 0.000000, 0), // rp_shrnoen's_leather_gaiters
		new BuySellList(4949, 100, 0.000000, 0), // rp_tunic_of_shrnoen_i
		new BuySellList(4146, 100, 0.000000, 0), // rp_tunic_of_shrnoen
		new BuySellList(4950, 100, 0.000000, 0), // rp_hose_of_shrnoen_i
		new BuySellList(4147, 100, 0.000000, 0), // rp_hose_of_shrnoen
		new BuySellList(4961, 100, 0.000000, 0), // rp_shrnoen's_shield_i
		new BuySellList(4178, 100, 0.000000, 0), // rp_shrnoen's_shield
		new BuySellList(4962, 100, 0.000000, 0), // rp_shrnoen's_helmet_i
		new BuySellList(4179, 100, 0.000000, 0), // rp_shrnoen's_helmet
		new BuySellList(4944, 100, 0.000000, 0), // rp_avadon_breastplate_i
		new BuySellList(4141, 100, 0.000000, 0), // rp_avadon_breastplate
		new BuySellList(4945, 100, 0.000000, 0), // rp_avadon_gaiters_i
		new BuySellList(4142, 100, 0.000000, 0), // rp_avadon_gaiters
		new BuySellList(4948, 100, 0.000000, 0), // rp_avadon_leather_mail_i
		new BuySellList(4145, 100, 0.000000, 0), // rp_avadon_leather_mail
		new BuySellList(4951, 100, 0.000000, 0), // rp_avadon_robe_i
		new BuySellList(4148, 100, 0.000000, 0), // rp_avadon_robe
		new BuySellList(4952, 100, 0.000000, 0), // rp_avadon_circlet_i
		new BuySellList(4149, 100, 0.000000, 0), // rp_avadon_circlet
		new BuySellList(4936, 100, 0.000000, 0), // rp_avadon_shield_i
		new BuySellList(4441, 100, 0.000000, 0), // rp_avadon_shield
		new BuySellList(4981, 100, 0.000000, 0), // rp_blue_wolve's_breastplate_i
		new BuySellList(4155, 100, 0.000000, 0), // rp_blue_wolve's_breastplate
		new BuySellList(4982, 100, 0.000000, 0), // rp_blue_wolve's_gaiters_i
		new BuySellList(4157, 100, 0.000000, 0), // rp_blue_wolve's_gaiters
		new BuySellList(4984, 100, 0.000000, 0), // rp_blue_wolve's_leather_mail_i
		new BuySellList(4159, 100, 0.000000, 0), // rp_blue_wolve's_leather_mail
		new BuySellList(4986, 100, 0.000000, 0), // rp_blue_wolve's_tunic_i
		new BuySellList(4161, 100, 0.000000, 0), // rp_blue_wolve's_tunic
		new BuySellList(4988, 100, 0.000000, 0), // rp_blue_wolve's_hose_i
		new BuySellList(4163, 100, 0.000000, 0), // rp_blue_wolve's_hose
		new BuySellList(4990, 100, 0.000000, 0), // rp_blue_wolve's_helmet_i
		new BuySellList(4165, 100, 0.000000, 0), // rp_blue_wolve's_helmet
		new BuySellList(4983, 100, 0.000000, 0), // rp_doom_plate_armor_i
		new BuySellList(4158, 100, 0.000000, 0), // rp_doom_plate_armor
		new BuySellList(4985, 100, 0.000000, 0), // rp_leather_mail_of_doom_i
		new BuySellList(4160, 100, 0.000000, 0), // rp_leather_mail_of_doom
		new BuySellList(4987, 100, 0.000000, 0), // rp_tunic_of_doom_i
		new BuySellList(4162, 100, 0.000000, 0), // rp_tunic_of_doom
		new BuySellList(4989, 100, 0.000000, 0), // rp_hose_of_doom_i
		new BuySellList(4164, 100, 0.000000, 0), // rp_hose_of_doom
		new BuySellList(4991, 100, 0.000000, 0), // rp_doom_helmet_i
		new BuySellList(4166, 100, 0.000000, 0), // rp_doom_helmet
		new BuySellList(4980, 100, 0.000000, 0), // rp_doom_shield_i
		new BuySellList(4154, 100, 0.000000, 0), // rp_doom_shield
		new BuySellList(4937, 100, 0.000000, 0), // rp_adamantite_earing_i
		new BuySellList(4126, 100, 0.000000, 0), // rp_adamantite_earing
		new BuySellList(4938, 100, 0.000000, 0), // rp_adamantite_ring_i
		new BuySellList(4127, 100, 0.000000, 0), // rp_adamantite_ring
		new BuySellList(4939, 100, 0.000000, 0), // rp_adamantite_necklace_i
		new BuySellList(4128, 100, 0.000000, 0), // rp_adamantite_necklace
		new BuySellList(4973, 100, 0.000000, 0), // rp_earing_of_black_ore_i
		new BuySellList(4129, 100, 0.000000, 0), // rp_earing_of_black_ore
		new BuySellList(4974, 100, 0.000000, 0), // rp_ring_of_black_ore_i
		new BuySellList(4130, 100, 0.000000, 0), // rp_ring_of_black_ore
		new BuySellList(4975, 100, 0.000000, 0), // rp_necklace_of_black_ore_i
		new BuySellList(4131, 100, 0.000000, 0) // rp_necklace_of_black_ore
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(4100, 100, 0.000000, 0), // shrnoen's_gauntlet_part
		new BuySellList(4073, 100, 0.000000, 0), // avadon_gloves_part
		new BuySellList(4096, 100, 0.000000, 0), // blue_wolve's_gloves_fabric
		new BuySellList(4091, 100, 0.000000, 0), // doom_gloves_part
		new BuySellList(4097, 100, 0.000000, 0), // shrnoen's_boots_design
		new BuySellList(4098, 100, 0.000000, 0), // avadon_boots_design
		new BuySellList(4090, 100, 0.000000, 0), // blue_wolve's_boots_design
		new BuySellList(4099, 100, 0.000000, 0), // doom_boots_part
		new BuySellList(4103, 100, 0.000000, 0), // pata_blade
		new BuySellList(4104, 100, 0.000000, 0), // great_sword_blade
		new BuySellList(4105, 100, 0.000000, 0), // heavy_war_axe_head
		new BuySellList(4106, 100, 0.000000, 0), // sprite's_staff_head
		new BuySellList(4107, 100, 0.000000, 0), // kshanberk_blade
		new BuySellList(4108, 100, 0.000000, 0), // sword_of_valhalla_blade
		new BuySellList(4109, 100, 0.000000, 0), // kris_edge
		new BuySellList(4110, 100, 0.000000, 0), // hell_knife_edge
		new BuySellList(4111, 100, 0.000000, 0), // arthro_nail_blade
		new BuySellList(4112, 100, 0.000000, 0), // dark_elven_long_bow_shaft
		new BuySellList(4113, 100, 0.000000, 0), // great_axe_head
		new BuySellList(4114, 100, 0.000000, 0), // sword_of_damascus_blade
		new BuySellList(4115, 100, 0.000000, 0), // lancia_blade
		new BuySellList(4116, 100, 0.000000, 0), // deadman's_glory_stone
		new BuySellList(4117, 100, 0.000000, 0), // art_of_battle_axe_blade
		new BuySellList(4118, 100, 0.000000, 0), // staff_of_evil_sprit_head
		new BuySellList(4119, 100, 0.000000, 0), // demon's_sword_edge
		new BuySellList(4120, 100, 0.000000, 0), // bellion_cestus_edge
		new BuySellList(4121, 100, 0.000000, 0), // hazard_bow_shaft
		new BuySellList(4056, 100, 0.000000, 0), // shrnoen's_breastplate_part
		new BuySellList(4057, 100, 0.000000, 0), // shrnoen's_gaiters_material
		new BuySellList(4066, 100, 0.000000, 0), // shrnoen's_leather_shirts_fabric
		new BuySellList(4067, 100, 0.000000, 0), // shrnoen's_leather_gaiters_texture
		new BuySellList(4069, 100, 0.000000, 0), // tunic_of_shrnoen_fabric
		new BuySellList(4070, 100, 0.000000, 0), // hose_of_shrnoen_fabric
		new BuySellList(4101, 100, 0.000000, 0), // shrnoen's_shield_fragment
		new BuySellList(4102, 100, 0.000000, 0), // shrnoen's_helmet_design
		new BuySellList(4064, 100, 0.000000, 0), // avadon_breastplate_part
		new BuySellList(4065, 100, 0.000000, 0), // avadon_gaiters_material
		new BuySellList(4068, 100, 0.000000, 0), // avadon_leather_mail_lining
		new BuySellList(4071, 100, 0.000000, 0), // avadon_robe_fabric
		new BuySellList(4072, 100, 0.000000, 0), // avadon_circlet_pattern
		new BuySellList(4439, 100, 0.000000, 0), // avadon_shield_fragment
		new BuySellList(4078, 100, 0.000000, 0), // blue_wolve's_breastplate_part
		new BuySellList(4080, 100, 0.000000, 0), // blue_wolve's_gaiters_material
		new BuySellList(4082, 100, 0.000000, 0), // blue_wolve's_leather_mail_texture
		new BuySellList(4084, 100, 0.000000, 0), // blue_wolve's_tunic_fabric
		new BuySellList(4086, 100, 0.000000, 0), // blue_wolve's_hose_pattern
		new BuySellList(4088, 100, 0.000000, 0), // blue_wolve's_helmet_design
		new BuySellList(4081, 100, 0.000000, 0), // doom_plate_armor_temper
		new BuySellList(4083, 100, 0.000000, 0), // leather_mail_of_doom_design
		new BuySellList(4085, 100, 0.000000, 0), // tunic_of_doom_pattern
		new BuySellList(4087, 100, 0.000000, 0), // hose_of_doom_pattern
		new BuySellList(4089, 100, 0.000000, 0), // doom_helmet_pattern
		new BuySellList(4077, 100, 0.000000, 0), // doom_shield_fragment
		new BuySellList(4049, 100, 0.000000, 0), // adamantite_earing_gemstone
		new BuySellList(4050, 100, 0.000000, 0), // adamantite_ring_wire
		new BuySellList(4051, 100, 0.000000, 0), // adamantite_necklace_chain
		new BuySellList(4052, 100, 0.000000, 0), // earing_of_black_ore_piece
		new BuySellList(4053, 100, 0.000000, 0), // ring_of_black_ore_gemstone
		new BuySellList(4054, 100, 0.000000, 0) // necklace_of_black_ore_beads
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(5332, 100, 0.000000, 0), // rp_sealed_dark_crystal_leather_mail_i
		new BuySellList(5333, 100, 0.000000, 0), // rp_sealed_dark_crystal_leather_mail
		new BuySellList(5334, 100, 0.000000, 0), // rp_sealed_tallum_leather_mail_i
		new BuySellList(5335, 100, 0.000000, 0), // rp_sealed_tallum_leather_mail
		new BuySellList(5336, 100, 0.000000, 0), // rp_sealed_leather_mail_of_nightmare_i
		new BuySellList(5337, 100, 0.000000, 0), // rp_sealed_leather_mail_of_nightmare
		new BuySellList(5338, 100, 0.000000, 0), // rp_sealed_majestic_leather_mail_i
		new BuySellList(5339, 100, 0.000000, 0), // rp_sealed_majestic_leather_mail
		new BuySellList(5340, 100, 0.000000, 0), // rp_sealed_legging_of_dark_crystal_i
		new BuySellList(5341, 100, 0.000000, 0), // rp_sealed_legging_of_dark_crystal
		new BuySellList(6331, 100, 0.000000, 0), // rp_sealed_phoenix's_earing_i
		new BuySellList(6332, 100, 0.000000, 0), // rp_sealed_phoenix's_earing
		new BuySellList(6337, 100, 0.000000, 0), // rp_sealed_majestic_earing_i
		new BuySellList(6338, 100, 0.000000, 0), // rp_sealed_majestic_earing
		new BuySellList(5346, 100, 0.000000, 0), // rp_sealed_tallum_tunic_i
		new BuySellList(5347, 100, 0.000000, 0), // rp_sealed_tallum_tunic
		new BuySellList(5348, 100, 0.000000, 0), // rp_sealed_dark_crystal_robe_i
		new BuySellList(5349, 100, 0.000000, 0), // rp_sealed_dark_crystal_robe
		new BuySellList(5350, 100, 0.000000, 0), // rp_sealed_robe_of_nightmare_i
		new BuySellList(5351, 100, 0.000000, 0), // rp_sealed_robe_of_nightmare
		new BuySellList(5352, 100, 0.000000, 0), // rp_sealed_majestic_robe_i
		new BuySellList(5353, 100, 0.000000, 0), // rp_sealed_majestic_robe
		new BuySellList(5354, 100, 0.000000, 0), // rp_sealed_tallum_hose_i
		new BuySellList(5355, 100, 0.000000, 0), // rp_sealed_tallum_hose
		new BuySellList(6329, 100, 0.000000, 0), // rp_sealed_phoenix's_necklace_i
		new BuySellList(6330, 100, 0.000000, 0), // rp_sealed_phoenix's_necklace
		new BuySellList(6335, 100, 0.000000, 0), // rp_sealed_majestic_necklace_i
		new BuySellList(6336, 100, 0.000000, 0), // rp_sealed_majestic_necklace
		new BuySellList(6333, 100, 0.000000, 0), // rp_sealed_phoenix's_ring_i
		new BuySellList(6334, 100, 0.000000, 0), // rp_sealed_phoenix's_ring
		new BuySellList(6339, 100, 0.000000, 0), // rp_sealed_majestic_ring_i
		new BuySellList(6340, 100, 0.000000, 0), // rp_sealed_majestic_ring
		new BuySellList(5364, 100, 0.000000, 0), // rp_sealed_dark_crystal_shield_i
		new BuySellList(5365, 100, 0.000000, 0), // rp_sealed_dark_crystal_shield
		new BuySellList(5366, 100, 0.000000, 0), // rp_sealed_shield_of_nightmare_i
		new BuySellList(5367, 100, 0.000000, 0), // rp_sealed_shield_of_nightmare
		new BuySellList(5368, 100, 0.000000, 0), // rp_sealed_dark_crystal_boots_i
		new BuySellList(5369, 100, 0.000000, 0), // rp_sealed_dark_crystal_boots
		new BuySellList(5370, 100, 0.000000, 0), // rp_sealed_tallum_boots_i
		new BuySellList(5371, 100, 0.000000, 0), // rp_sealed_tallum_boots
		new BuySellList(5380, 100, 0.000000, 0), // rp_sealed_boots_of_nightmare_i
		new BuySellList(5381, 100, 0.000000, 0), // rp_sealed_boots_of_nightmare
		new BuySellList(5382, 100, 0.000000, 0), // rp_sealed_magestic_boots_i
		new BuySellList(5383, 100, 0.000000, 0), // rp_sealed_magestic_boots
		new BuySellList(5404, 100, 0.000000, 0), // rp_sealed_gloves_of_nightmare_i
		new BuySellList(5405, 100, 0.000000, 0), // rp_sealed_gloves_of_nightmare
		new BuySellList(5406, 100, 0.000000, 0), // rp_sealed_magestic_gloves_i
		new BuySellList(5407, 100, 0.000000, 0), // rp_sealed_magestic_gloves
		new BuySellList(5416, 100, 0.000000, 0), // rp_sealed_dark_crystal_breastplate_i
		new BuySellList(5417, 100, 0.000000, 0), // rp_sealed_dark_crystal_breastplate
		new BuySellList(5418, 100, 0.000000, 0), // rp_sealed_tallum_plate_armor_i
		new BuySellList(5419, 100, 0.000000, 0), // rp_sealed_tallum_plate_armor
		new BuySellList(5420, 100, 0.000000, 0), // rp_sealed_armor_of_nightmare_i
		new BuySellList(5421, 100, 0.000000, 0), // rp_sealed_armor_of_nightmare
		new BuySellList(5422, 100, 0.000000, 0), // rp_sealed_majestic_platte_armor_i
		new BuySellList(5423, 100, 0.000000, 0), // rp_sealed_majestic_platte_armor
		new BuySellList(5424, 100, 0.000000, 0), // rp_sealed_dark_crystal_gaiters_i
		new BuySellList(5425, 100, 0.000000, 0), // rp_sealed_dark_crystal_gaiters
		new BuySellList(5426, 100, 0.000000, 0), // rp_sealed_dark_crystal_helmet_i
		new BuySellList(5427, 100, 0.000000, 0), // rp_sealed_dark_crystal_helmet
		new BuySellList(5428, 100, 0.000000, 0), // rp_sealed_tallum_bonnet_i
		new BuySellList(5429, 100, 0.000000, 0), // rp_sealed_tallum_bonnet
		new BuySellList(5430, 100, 0.000000, 0), // rp_sealed_helm_of_nightmare_i
		new BuySellList(5431, 100, 0.000000, 0), // rp_sealed_helm_of_nightmare
		new BuySellList(5432, 100, 0.000000, 0), // rp_sealed_magestic_circlet_i
		new BuySellList(5433, 100, 0.000000, 0), // rp_sealed_magestic_circlet
		new BuySellList(5434, 100, 0.000000, 0), // rp_dragon_slayer_i
		new BuySellList(5435, 100, 0.000000, 0), // rp_dragon_slayer
		new BuySellList(5438, 100, 0.000000, 0), // rp_meteor_shower_i
		new BuySellList(5439, 100, 0.000000, 0), // rp_meteor_shower
		new BuySellList(5440, 100, 0.000000, 0), // rp_elysian_i
		new BuySellList(5441, 100, 0.000000, 0), // rp_elysian
		new BuySellList(5442, 100, 0.000000, 0), // rp_soul_bow_i
		new BuySellList(5443, 100, 0.000000, 0), // rp_soul_bow
		new BuySellList(5444, 100, 0.000000, 0), // rp_carnium_bow_i
		new BuySellList(5445, 100, 0.000000, 0), // rp_carnium_bow
		new BuySellList(5446, 100, 0.000000, 0), // rp_bloody_orchid_i
		new BuySellList(5447, 100, 0.000000, 0), // rp_bloody_orchid
		new BuySellList(5448, 100, 0.000000, 0), // rp_soul_separator_i
		new BuySellList(5449, 100, 0.000000, 0), // rp_soul_separator
		new BuySellList(5450, 100, 0.000000, 0), // rp_dragon_grinder_i
		new BuySellList(5451, 100, 0.000000, 0), // rp_dragon_grinder
		new BuySellList(5452, 100, 0.000000, 0), // rp_blood_tornado_i
		new BuySellList(5453, 100, 0.000000, 0), // rp_blood_tornado
		new BuySellList(5456, 100, 0.000000, 0), // rp_tallum_glaive_i
		new BuySellList(5457, 100, 0.000000, 0), // rp_tallum_glaive
		new BuySellList(5458, 100, 0.000000, 0), // rp_halbard_i
		new BuySellList(5459, 100, 0.000000, 0), // rp_halbard
		new BuySellList(5460, 100, 0.000000, 0), // rp_dasparion's_staff_i
		new BuySellList(5461, 100, 0.000000, 0), // rp_dasparion's_staff
		new BuySellList(5462, 100, 0.000000, 0), // rp_worldtree's_branch_i
		new BuySellList(5463, 100, 0.000000, 0), // rp_worldtree's_branch
		new BuySellList(5464, 100, 0.000000, 0), // rp_dark_legion's_edge_i
		new BuySellList(5465, 100, 0.000000, 0), // rp_dark_legion's_edge
		new BuySellList(5466, 100, 0.000000, 0), // rp_sword_of_miracle_i
		new BuySellList(5467, 100, 0.000000, 0), // rp_sword_of_miracle
		new BuySellList(5468, 100, 0.000000, 0), // rp_elemental_sword_i
		new BuySellList(5469, 100, 0.000000, 0), // rp_elemental_sword
		new BuySellList(5470, 100, 0.000000, 0), // rp_tallum_blade_i
		new BuySellList(5471, 100, 0.000000, 0), // rp_tallum_blade
		new BuySellList(5394, 100, 0.000000, 0), // rp_sealed_tallum_gloves_i
		new BuySellList(5395, 100, 0.000000, 0), // rp_sealed_tallum_gloves
		new BuySellList(5392, 100, 0.000000, 0), // rp_sealed_dark_crystal_gloves_i
		new BuySellList(5393, 100, 0.000000, 0) // rp_sealed_dark_crystal_gloves
	};
	
	private static final BuySellList[] _sellList6 = new BuySellList[] {
		new BuySellList(5478, 100, 0.000000, 0), // sealed_dark_crystal_leather_mail_pattern
		new BuySellList(5479, 100, 0.000000, 0), // sealed_tallum_leather_mail_pattern
		new BuySellList(5480, 100, 0.000000, 0), // sealed_leather_mail_of_nightmare_fabric
		new BuySellList(5481, 100, 0.000000, 0), // sealed_majestic_leather_mail_fabric
		new BuySellList(5482, 100, 0.000000, 0), // sealed_legging_of_dark_crystal_design
		new BuySellList(6341, 100, 0.000000, 0), // sealed_phoenix's_earing_gemstone
		new BuySellList(6346, 100, 0.000000, 0), // sealed_majestic_ring_gemstrone
		new BuySellList(5485, 100, 0.000000, 0), // sealed_tallum_tunic_texture
		new BuySellList(5486, 100, 0.000000, 0), // sealed_dark_crystal_robe_fabric
		new BuySellList(5487, 100, 0.000000, 0), // sealed_robe_of_nightmare_fabric
		new BuySellList(5488, 100, 0.000000, 0), // sealed_majestic_robe_fabric
		new BuySellList(5489, 100, 0.000000, 0), // sealed_tallum_hose_fabric
		new BuySellList(6343, 100, 0.000000, 0), // sealed_phoenix's_necklace_beads
		new BuySellList(6344, 100, 0.000000, 0), // sealed_majestic_necklace_beads
		new BuySellList(6345, 100, 0.000000, 0), // sealed_phoenix's_ring_gemstone
		new BuySellList(6342, 100, 0.000000, 0), // sealed_majestic_earing_gemstone
		new BuySellList(5494, 100, 0.000000, 0), // sealed_dark_crystal_shield_fragment
		new BuySellList(5495, 100, 0.000000, 0), // sealed_shield_of_nightmare_fragment
		new BuySellList(5496, 100, 0.000000, 0), // sealed_dark_crystal_boots_lining
		new BuySellList(5497, 100, 0.000000, 0), // sealed_tallum_boots_lining
		new BuySellList(5502, 100, 0.000000, 0), // sealed_boots_of_nightmare_lining
		new BuySellList(5503, 100, 0.000000, 0), // sealed_magestic_boots_lining
		new BuySellList(5508, 100, 0.000000, 0), // sealed_dark_crystal_gloves_design
		new BuySellList(5509, 100, 0.000000, 0), // sealed_tallum_gloves_design
		new BuySellList(5514, 100, 0.000000, 0), // sealed_gloves_of_nightmare_design
		new BuySellList(5515, 100, 0.000000, 0), // sealed_magestic_gloves_design
		new BuySellList(5520, 100, 0.000000, 0), // sealed_dark_crystal_breastplate_pattern
		new BuySellList(5521, 100, 0.000000, 0), // sealed_tallum_plate_armor_pattern
		new BuySellList(5522, 100, 0.000000, 0), // sealed_armor_of_nightmare_pattern
		new BuySellList(5523, 100, 0.000000, 0), // sealed_majestic_platte_armor_pattern
		new BuySellList(5524, 100, 0.000000, 0), // sealed_dark_crystal_gaiters_pattern
		new BuySellList(5525, 100, 0.000000, 0), // sealed_dark_crystal_helmet_design
		new BuySellList(5526, 100, 0.000000, 0), // sealed_tallum_bonnet_design
		new BuySellList(5527, 100, 0.000000, 0), // sealed_helm_of_nightmare_design
		new BuySellList(5528, 100, 0.000000, 0), // sealed_magestic_circlet_design
		new BuySellList(5529, 100, 0.000000, 0), // dragon_slayer_edge
		new BuySellList(5532, 100, 0.000000, 0), // meteor_shower_head
		new BuySellList(5533, 100, 0.000000, 0), // elysian_head
		new BuySellList(5534, 100, 0.000000, 0), // soul_bow_shaft
		new BuySellList(5535, 100, 0.000000, 0), // carnium_bow_shaft
		new BuySellList(5536, 100, 0.000000, 0), // bloody_orchid_head
		new BuySellList(5537, 100, 0.000000, 0), // soul_separator_head
		new BuySellList(5538, 100, 0.000000, 0), // dragon_grinder_edge
		new BuySellList(5539, 100, 0.000000, 0), // blood_tornado_edge
		new BuySellList(5541, 100, 0.000000, 0), // tallum_glaive_edge
		new BuySellList(5542, 100, 0.000000, 0), // halbard_edge
		new BuySellList(5543, 100, 0.000000, 0), // dasparion's_staff_head
		new BuySellList(5544, 100, 0.000000, 0), // worldtree's_branch_head
		new BuySellList(5545, 100, 0.000000, 0), // dark_legion's_edge_edge
		new BuySellList(5546, 100, 0.000000, 0), // sword_of_miracle_edge
		new BuySellList(5547, 100, 0.000000, 0), // elemental_sword_edge
		new BuySellList(5548, 100, 0.000000, 0) // tallum_blade_edge
	};
	
	private static final BuySellList[] _sellList7 = new BuySellList[] {
		new BuySellList(1524, 100, 0.000000, 0), // sb_devotioin_of_shine1
		new BuySellList(1525, 100, 0.000000, 0), // sb_blood_lust1
		new BuySellList(1527, 100, 0.000000, 0), // sb_pain_thorn1
		new BuySellList(1529, 100, 0.000000, 0), // sb_night_murmur1
		new BuySellList(1531, 100, 0.000000, 0), // sb_chill_flame1
		new BuySellList(1522, 100, 0.000000, 0), // sb_mass_frenzy1
		new BuySellList(1526, 100, 0.000000, 0), // sb_external_fear1
		new BuySellList(1521, 100, 0.000000, 0), // sb_burning_spirit1
		new BuySellList(1523, 100, 0.000000, 0), // sb_devotioin_of_soul1
		new BuySellList(1534, 100, 0.000000, 0), // sb_entice_madness1
		new BuySellList(1535, 100, 0.000000, 0), // sb_blaze_quake1
		new BuySellList(1537, 100, 0.000000, 0), // sb_pain_edge1
		new BuySellList(1856, 100, 0.000000, 0), // sb_inspire_life_force1
		new BuySellList(1532, 100, 0.000000, 0), // sb_eternal_flame1
		new BuySellList(1533, 100, 0.000000, 0), // sb_aura_sway1
		new BuySellList(1536, 100, 0.000000, 0), // sb_bind_will1
		new BuySellList(1518, 100, 0.000000, 0), // sb_pure_inspiration1
		new BuySellList(1519, 100, 0.000000, 0), // sb_power_of_paagrio1
		new BuySellList(1528, 100, 0.000000, 0), // sb_engrave_seal_of_timid1
		new BuySellList(1520, 100, 0.000000, 0), // sb_blessing_of_paagrio1
		new BuySellList(1530, 100, 0.000000, 0), // sb_engrave_seal_of_lazy1
		new BuySellList(3103, 100, 0.000000, 0), // sb_wisdom_of_paagrio1
		new BuySellList(3104, 100, 0.000000, 0), // sb_glory_of_paagrio1
		new BuySellList(3105, 100, 0.000000, 0), // sb_seal_of_winter1
		new BuySellList(3110, 100, 0.000000, 0), // sb_seal_of_scourge1
		new BuySellList(3113, 100, 0.000000, 0), // sb_shield_of_paagrio1
		new BuySellList(3114, 100, 0.000000, 0), // sb_steal_essence1
		new BuySellList(3115, 100, 0.000000, 0), // sb_freezing_flame1
		new BuySellList(3117, 100, 0.000000, 0), // sb_chant_of_evasion1
		new BuySellList(4204, 100, 0.000000, 0), // sb_tact_of_paagrio
		new BuySellList(3107, 100, 0.000000, 0), // sb_seal_of_gloom1
		new BuySellList(3108, 100, 0.000000, 0), // sb_seal_of_mirage1
		new BuySellList(3112, 100, 0.000000, 0), // sb_sight_of_paagrio1
		new BuySellList(3118, 100, 0.000000, 0), // sb_chant_of_rage1
		new BuySellList(3943, 100, 0.000000, 0), // sb_heart_of_paagrio1
		new BuySellList(4205, 100, 0.000000, 0), // sb_rage_of_paagrio
		new BuySellList(4926, 100, 0.000000, 0), // sb_soul_guard1
		new BuySellList(6397, 100, 0.000000, 0), // sb_chant_of_vampire1-4
		new BuySellList(3106, 100, 0.000000, 0), // sb_seal_of_flame1
		new BuySellList(3109, 100, 0.000000, 0), // sb_seal_of_silence1
		new BuySellList(3111, 100, 0.000000, 0), // sb_seal_of_suspension1
		new BuySellList(3116, 100, 0.000000, 0), // sb_chant_of_fury1
		new BuySellList(6395, 100, 0.000000, 0), // sb_chant_of_predator1-3
		new BuySellList(6396, 100, 0.000000, 0), // sb_chant_of_eagle1-3
		new BuySellList(4927, 100, 0.000000, 0), // sb_chant_of_revenge1
		new BuySellList(6351, 100, 0.000000, 0), // sb_ritual_of_life1
		new BuySellList(6350, 100, 0.000000, 0), // sb_honor_of_paagrio1
		new BuySellList(4925, 100, 0.000000, 0), // sb_speed_of_paagrio1
		new BuySellList(3038, 100, 0.000000, 0), // sb_summon_mechanic_golem1
		new BuySellList(3940, 100, 0.000000, 0), // sb_summon_siege_golem
		new BuySellList(4915, 100, 0.000000, 0), // sb_summon_wild_hog_cannon
		new BuySellList(4921, 100, 0.000000, 0) // sb_summon_bigboom1
	};
	
	private static final int npcId = 31075;
	
	public SalesmanCat() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
		super.sellList6 = buildBuySellList(_sellList6, npcId, 6);
		super.sellList7 = buildBuySellList(_sellList7, npcId, 7);
		
		super.fnHi = "salesman_cat001.htm";
	}
}