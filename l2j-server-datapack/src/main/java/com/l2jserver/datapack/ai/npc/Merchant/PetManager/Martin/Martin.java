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
package com.l2jserver.datapack.ai.npc.Merchant.PetManager.Martin;

import com.l2jserver.datapack.ai.npc.Merchant.PetManager.PetManager;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Martin extends PetManager {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(2505, 20, 0.000000, 0), // iron_canine
		new BuySellList(3439, 20, 0.000000, 0), // shining_canine
		new BuySellList(3902, 20, 0.000000, 0), // ghost_canine
		new BuySellList(3903, 20, 0.000000, 0), // mithril_canine
		new BuySellList(3904, 20, 0.000000, 0), // sylvan_canine
		new BuySellList(3905, 20, 0.000000, 0), // orikarukon_canine
		new BuySellList(3906, 20, 0.000000, 0), // fang_of_saltydog
		new BuySellList(2506, 20, 0.000000, 0), // wolve's_leather_mail
		new BuySellList(3891, 20, 0.000000, 0), // wolves_hide_armor
		new BuySellList(3892, 20, 0.000000, 0), // wolves_hard_leather_mail
		new BuySellList(3893, 20, 0.000000, 0), // wolves_wooden_armor
		new BuySellList(3894, 20, 0.000000, 0), // wolves_ring_mail
		new BuySellList(3895, 20, 0.000000, 0), // wolves_bone_armor
		new BuySellList(3896, 20, 0.000000, 0), // wolves_scale_mail
		new BuySellList(2515, 20, 0.000000, 0), // food_for_wolves
		new BuySellList(14818, 20, 0.000000, 0), // greater_food_of_pet
		new BuySellList(3920, 20, 0.000000, 0), // viperbite
		new BuySellList(3921, 20, 0.000000, 0), // shadow_fang
		new BuySellList(3922, 20, 0.000000, 0), // alya_fang
		new BuySellList(3923, 20, 0.000000, 0), // torturer
		new BuySellList(5187, 20, 0.000000, 0), // serpentine_grinder
		new BuySellList(5188, 20, 0.000000, 0), // fang_of_dahak
		new BuySellList(3913, 20, 0.000000, 0), // hatchlings_scale_mail
		new BuySellList(3914, 20, 0.000000, 0), // hatchlings_brigandine
		new BuySellList(3915, 20, 0.000000, 0), // hatchlings_bronze_coat
		new BuySellList(3916, 20, 0.000000, 0), // hatchlings_steel_coat
		new BuySellList(5182, 20, 0.000000, 0), // hatchlings_gorgon_coat
		new BuySellList(5183, 20, 0.000000, 0), // hatchlings_ophidian_plate
		new BuySellList(4038, 20, 0.000000, 0), // food_for_hatchling
		new BuySellList(5176, 20, 0.000000, 0), // serpentine_spike
		new BuySellList(5177, 20, 0.000000, 0), // drake_horn
		new BuySellList(5178, 20, 0.000000, 0), // assault_alicorn
		new BuySellList(5170, 20, 0.000000, 0), // mithril_panzer_coat
		new BuySellList(5171, 20, 0.000000, 0), // brigandine_panzer_coat
		new BuySellList(5172, 20, 0.000000, 0), // draconic_panzer_coat
		new BuySellList(5168, 20, 0.000000, 0), // food_for_strider
		new BuySellList(6645, 20, 0.000000, 0), // beast_soul_shot
		new BuySellList(6646, 20, 0.000000, 0), // beast_spirit_shot
		new BuySellList(6647, 20, 0.000000, 0), // blessed_beast_spirit_shot
		new BuySellList(7582, 20, 0.000000, 0), // pet_food_baby_spice
		new BuySellList(8541, 20, 0.000000, 0), // pet_armor_little_harness
		new BuySellList(9656, 20, 0.000000, 0), // enchanted_wolf_canine
		new BuySellList(9657, 20, 0.000000, 0), // enchanted_coyote_canine
		new BuySellList(9658, 20, 0.000000, 0), // revolution_salty_dog_canine
		new BuySellList(9662, 20, 0.000000, 0), // black_mane_wolf_scale_armor
		new BuySellList(9663, 20, 0.000000, 0), // black_mane_wolf_bronze_armor
		new BuySellList(9664, 20, 0.000000, 0), // black_mane_wolf_plate_armor
		new BuySellList(9668, 20, 0.000000, 0), // revolution_wolf_food
		new BuySellList(10515, 20, 0.000000, 0), // beast_soul_shot_capsule
		new BuySellList(10516, 20, 0.000000, 0), // beast_spirit_shot_capsule
		new BuySellList(10517, 20, 0.000000, 0), // blessed_beast_spirit_shot_capsule
		new BuySellList(10425, 20, 0.000000, 0), // food_for_upgrade_baby_pet
		new BuySellList(12740, 20, 0.000000, 0), // baby_pet_scale_armor
		new BuySellList(12741, 20, 0.000000, 0), // baby_pet_bronze_armor
		new BuySellList(12742, 20, 0.000000, 0), // baby_pet_plate_armor
		new BuySellList(12743, 20, 0.000000, 0), // baby_pet_mithril_armor
		new BuySellList(12744, 20, 0.000000, 0), // baby_pet_oriharukon_armor
		new BuySellList(12745, 20, 0.000000, 0), // baby_pet_oricalcum_armor
		new BuySellList(12746, 20, 0.000000, 0), // pet_crystal_pandent
		new BuySellList(12747, 20, 0.000000, 0), // pet_rubi_pendant
		new BuySellList(12748, 20, 0.000000, 0), // pet_sapphire_pendant
		new BuySellList(12749, 20, 0.000000, 0), // pet_diamond_pendant
		new BuySellList(12750, 20, 0.000000, 0), // pet_enria_pendant
		new BuySellList(12751, 20, 0.000000, 0), // pet_thons_pendant
		new BuySellList(12752, 20, 0.000000, 0) // pet_asofe_pendant
	};
	
	private static final int npcId = 30731;
	
	public Martin() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "pet_manager_martin001.htm";
		super.fnYouAreChaotic = "pet_manager_martin006.htm";
	}
}