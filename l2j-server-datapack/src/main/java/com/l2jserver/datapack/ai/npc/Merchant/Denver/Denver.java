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
package com.l2jserver.datapack.ai.npc.Merchant.Denver;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Denver extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1814, 20, 0.000000, 0), // rp_leather
		new BuySellList(2135, 20, 0.000000, 0), // rp_braided_hemp
		new BuySellList(2136, 20, 0.000000, 0), // rp_cokes
		new BuySellList(2137, 20, 0.000000, 0), // rp_steel
		new BuySellList(2138, 20, 0.000000, 0), // rp_coarse_bone_powder
		new BuySellList(1817, 20, 0.000000, 0), // rp_cord
		new BuySellList(2139, 20, 0.000000, 0), // rp_steel_mold
		new BuySellList(2140, 20, 0.000000, 0), // rp_high_grade_suede
		new BuySellList(2141, 20, 0.000000, 0), // rp_silver_mold
		new BuySellList(2142, 20, 0.000000, 0), // rp_varnish_of_purity
		new BuySellList(2143, 20, 0.000000, 0), // rp_synthesis_cokes
		new BuySellList(2144, 20, 0.000000, 0), // rp_compound_braid
		new BuySellList(1825, 20, 0.000000, 0), // rp_oriharukon
		new BuySellList(2145, 20, 0.000000, 0), // rp_mithirl_alloy
		new BuySellList(2146, 20, 0.000000, 0), // rp_artisan's_frame
		new BuySellList(2147, 20, 0.000000, 0), // rp_blacksmith's_frame
		new BuySellList(2148, 20, 0.000000, 0), // rp_crafted_leather
		new BuySellList(2149, 20, 0.000000, 0), // rp_metallic_fiber
		new BuySellList(5231, 20, 0.000000, 0), // rp_reinforcing_agent
		new BuySellList(5472, 20, 0.000000, 0), // rp_iron_thread
		new BuySellList(5473, 20, 0.000000, 0), // rp_reinforcing_plate
		new BuySellList(4122, 20, 0.000000, 0), // rp_maestro_holder
		new BuySellList(4123, 20, 0.000000, 0), // rp_maestro_anvil_lock
		new BuySellList(4124, 20, 0.000000, 0), // rp_craftsman_mold
		new BuySellList(4125, 20, 0.000000, 0), // rp_maestro_mold
		new BuySellList(5474, 20, 0.000000, 0), // rp_reorins_mold
		new BuySellList(5475, 20, 0.000000, 0), // rp_warsmith_mold
		new BuySellList(5476, 20, 0.000000, 0), // rp_arcsmith_anvil
		new BuySellList(5477, 20, 0.000000, 0), // rp_warsmith_holder
		new BuySellList(1804, 20, 0.000000, 0), // rp_soulshot_d
		new BuySellList(3032, 20, 0.000000, 0), // rp_spiritshot_d
		new BuySellList(1805, 20, 0.000000, 0), // rp_soulshot_c
		new BuySellList(3033, 20, 0.000000, 0), // rp_spiritshot_c
		new BuySellList(3953, 20, 0.000000, 0), // rp_blessed_spiritshot_d
		new BuySellList(3954, 20, 0.000000, 0), // rp_blessed_spiritshot_c
		new BuySellList(5153, 20, 0.000000, 0), // rp_comp_soulshot_d
		new BuySellList(5158, 20, 0.000000, 0), // rp_comp_spiritshot_d
		new BuySellList(5163, 20, 0.000000, 0), // rp_comp_bspiritshot_d
		new BuySellList(5268, 20, 0.000000, 0), // rp_adv_comp_soulshot_d
		new BuySellList(5273, 20, 0.000000, 0), // rp_adv_comp_spiritshot_d
		new BuySellList(5278, 20, 0.000000, 0), // rp_adv_comp_bspiritshot_d
		new BuySellList(5154, 20, 0.000000, 0), // rp_comp_soulshot_c
		new BuySellList(5159, 20, 0.000000, 0), // rp_comp_spiritshot_c
		new BuySellList(5164, 20, 0.000000, 0), // rp_comp_bspiritshot_c
		new BuySellList(5269, 20, 0.000000, 0), // rp_adv_comp_soulshot_c
		new BuySellList(5274, 20, 0.000000, 0), // rp_adv_comp_spiritshot_c
		new BuySellList(5279, 20, 0.000000, 0), // rp_adv_comp_bspiritshot_c
		new BuySellList(1806, 20, 0.000000, 0), // rp_soulshot_b
		new BuySellList(3034, 20, 0.000000, 0), // rp_spiritshot_b
		new BuySellList(1807, 20, 0.000000, 0), // rp_soulshot_a
		new BuySellList(3035, 20, 0.000000, 0), // rp_spiritshot_a
		new BuySellList(3955, 20, 0.000000, 0), // rp_blessed_spiritshot_b
		new BuySellList(3956, 20, 0.000000, 0), // rp_blessed_spiritshot_a
		new BuySellList(5155, 20, 0.000000, 0), // rp_comp_soulshot_b
		new BuySellList(5160, 20, 0.000000, 0), // rp_comp_spiritshot_b
		new BuySellList(5165, 20, 0.000000, 0), // rp_comp_bspiritshot_b
		new BuySellList(5270, 20, 0.000000, 0), // rp_adv_comp_soulshot_b
		new BuySellList(5275, 20, 0.000000, 0), // rp_adv_comp_spiritshot_b
		new BuySellList(5280, 20, 0.000000, 0), // rp_adv_comp_bspiritshot_b
		new BuySellList(5156, 20, 0.000000, 0), // rp_comp_soulshot_a
		new BuySellList(5161, 20, 0.000000, 0), // rp_comp_spiritshot_a
		new BuySellList(5166, 20, 0.000000, 0), // rp_comp_bspiritshot_a
		new BuySellList(5271, 20, 0.000000, 0), // rp_adv_comp_soulshot_a
		new BuySellList(5276, 20, 0.000000, 0), // rp_adv_comp_spiritshot_a
		new BuySellList(5281, 20, 0.000000, 0) // rp_adv_comp_bspiritshot_a
	};
	
	private static final int npcId = 32881;
	
	public Denver() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "recipe_trader_denver001.htm";
		super.fnYouAreChaotic = "recipe_trader_denver006.htm";
	}
}