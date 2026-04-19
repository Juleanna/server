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
package com.l2jserver.datapack.ai.npc.Merchant.Drawin;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Drawin extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1814, 15, 0.000000, 0), // rp_leather
		new BuySellList(2135, 15, 0.000000, 0), // rp_braided_hemp
		new BuySellList(2136, 15, 0.000000, 0), // rp_cokes
		new BuySellList(2137, 15, 0.000000, 0), // rp_steel
		new BuySellList(2138, 15, 0.000000, 0), // rp_coarse_bone_powder
		new BuySellList(1817, 15, 0.000000, 0), // rp_cord
		new BuySellList(2139, 15, 0.000000, 0), // rp_steel_mold
		new BuySellList(2140, 15, 0.000000, 0), // rp_high_grade_suede
		new BuySellList(2141, 15, 0.000000, 0), // rp_silver_mold
		new BuySellList(2142, 15, 0.000000, 0), // rp_varnish_of_purity
		new BuySellList(2143, 15, 0.000000, 0), // rp_synthesis_cokes
		new BuySellList(2144, 15, 0.000000, 0), // rp_compound_braid
		new BuySellList(1825, 15, 0.000000, 0), // rp_oriharukon
		new BuySellList(2145, 15, 0.000000, 0), // rp_mithirl_alloy
		new BuySellList(2146, 15, 0.000000, 0), // rp_artisan's_frame
		new BuySellList(2147, 15, 0.000000, 0), // rp_blacksmith's_frame
		new BuySellList(2148, 15, 0.000000, 0), // rp_crafted_leather
		new BuySellList(2149, 15, 0.000000, 0), // rp_metallic_fiber
		new BuySellList(5231, 15, 0.000000, 0), // rp_reinforcing_agent
		new BuySellList(5472, 15, 0.000000, 0), // rp_iron_thread
		new BuySellList(5473, 15, 0.000000, 0), // rp_reinforcing_plate
		new BuySellList(4122, 15, 0.000000, 0), // rp_maestro_holder
		new BuySellList(4123, 15, 0.000000, 0), // rp_maestro_anvil_lock
		new BuySellList(4124, 15, 0.000000, 0), // rp_craftsman_mold
		new BuySellList(4125, 15, 0.000000, 0), // rp_maestro_mold
		new BuySellList(5474, 15, 0.000000, 0), // rp_reorins_mold
		new BuySellList(5475, 15, 0.000000, 0), // rp_warsmith_mold
		new BuySellList(5476, 15, 0.000000, 0), // rp_arcsmith_anvil
		new BuySellList(5477, 15, 0.000000, 0), // rp_warsmith_holder
		new BuySellList(1804, 15, 0.000000, 0), // rp_soulshot_d
		new BuySellList(3032, 15, 0.000000, 0), // rp_spiritshot_d
		new BuySellList(1805, 15, 0.000000, 0), // rp_soulshot_c
		new BuySellList(3033, 15, 0.000000, 0), // rp_spiritshot_c
		new BuySellList(3953, 15, 0.000000, 0), // rp_blessed_spiritshot_d
		new BuySellList(3954, 15, 0.000000, 0), // rp_blessed_spiritshot_c
		new BuySellList(5153, 15, 0.000000, 0), // rp_comp_soulshot_d
		new BuySellList(5158, 15, 0.000000, 0), // rp_comp_spiritshot_d
		new BuySellList(5163, 15, 0.000000, 0), // rp_comp_bspiritshot_d
		new BuySellList(5268, 15, 0.000000, 0), // rp_adv_comp_soulshot_d
		new BuySellList(5273, 15, 0.000000, 0), // rp_adv_comp_spiritshot_d
		new BuySellList(5278, 15, 0.000000, 0), // rp_adv_comp_bspiritshot_d
		new BuySellList(5154, 15, 0.000000, 0), // rp_comp_soulshot_c
		new BuySellList(5159, 15, 0.000000, 0), // rp_comp_spiritshot_c
		new BuySellList(5164, 15, 0.000000, 0), // rp_comp_bspiritshot_c
		new BuySellList(5269, 15, 0.000000, 0), // rp_adv_comp_soulshot_c
		new BuySellList(5274, 15, 0.000000, 0), // rp_adv_comp_spiritshot_c
		new BuySellList(5279, 15, 0.000000, 0), // rp_adv_comp_bspiritshot_c
		new BuySellList(1806, 15, 0.000000, 0), // rp_soulshot_b
		new BuySellList(3034, 15, 0.000000, 0), // rp_spiritshot_b
		new BuySellList(1807, 15, 0.000000, 0), // rp_soulshot_a
		new BuySellList(3035, 15, 0.000000, 0), // rp_spiritshot_a
		new BuySellList(3955, 15, 0.000000, 0), // rp_blessed_spiritshot_b
		new BuySellList(3956, 15, 0.000000, 0), // rp_blessed_spiritshot_a
		new BuySellList(5155, 15, 0.000000, 0), // rp_comp_soulshot_b
		new BuySellList(5160, 15, 0.000000, 0), // rp_comp_spiritshot_b
		new BuySellList(5165, 15, 0.000000, 0), // rp_comp_bspiritshot_b
		new BuySellList(5270, 15, 0.000000, 0), // rp_adv_comp_soulshot_b
		new BuySellList(5275, 15, 0.000000, 0), // rp_adv_comp_spiritshot_b
		new BuySellList(5280, 15, 0.000000, 0), // rp_adv_comp_bspiritshot_b
		new BuySellList(5156, 15, 0.000000, 0), // rp_comp_soulshot_a
		new BuySellList(5161, 15, 0.000000, 0), // rp_comp_spiritshot_a
		new BuySellList(5166, 15, 0.000000, 0), // rp_comp_bspiritshot_a
		new BuySellList(5271, 15, 0.000000, 0), // rp_adv_comp_soulshot_a
		new BuySellList(5276, 15, 0.000000, 0), // rp_adv_comp_spiritshot_a
		new BuySellList(5281, 15, 0.000000, 0) // rp_adv_comp_bspiritshot_a
	};
	
	private static final int npcId = 32879;
	
	public Drawin() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "recipe_trader_drawin001.htm";
		super.fnYouAreChaotic = "recipe_trader_drawin006.htm";
	}
}