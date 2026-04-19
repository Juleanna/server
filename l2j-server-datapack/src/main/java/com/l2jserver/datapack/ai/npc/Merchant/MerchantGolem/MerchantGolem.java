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
package com.l2jserver.datapack.ai.npc.Merchant.MerchantGolem;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantGolemBasic;

/**
* @author Charus
* @version 2.6.3.0
*/
public class MerchantGolem extends MerchantGolemBasic {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1835, 60, 0.000000, 0), // soulshot_none
		new BuySellList(2509, 60, 0.000000, 0), // spiritshot_none
		new BuySellList(3947, 60, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(5146, 60, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5140, 60, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5134, 60, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5262, 60, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5256, 60, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5250, 60, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(17, 60, 0.000000, 0), // wooden_arrow
		new BuySellList(1341, 60, 0.000000, 0), // bone_arrow
		new BuySellList(1060, 60, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1831, 60, 0.000000, 0), // antidote
		new BuySellList(1833, 60, 0.000000, 0), // bandage
		new BuySellList(734, 60, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 60, 0.000000, 0), // swift_attack_potion
		new BuySellList(6035, 60, 0.000000, 0), // potion_of_acumen2
		new BuySellList(736, 60, 0.000000, 0), // scroll_of_escape
		new BuySellList(737, 60, 0.000000, 0), // scroll_of_resurrection
		new BuySellList(5589, 60, 0.000000, 0), // energy_stone
		new BuySellList(1661, 60, 0.000000, 0), // key_of_thief
		new BuySellList(9633, 60, 0.000000, 0), // bone_bolt
		new BuySellList(4625, 60, 0.000000, 0), // dice_heart
		new BuySellList(4626, 60, 0.000000, 0), // dice_spade
		new BuySellList(4627, 60, 0.000000, 0), // dice_clover
		new BuySellList(4628, 60, 0.000000, 0), // dice_diamond
		new BuySellList(21746, 60, 0.000000, 0) // g_lucky_key
	};
	
	private static final int npcId = 13128;
	
	public MerchantGolem() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "merchant_golem001.htm";
	}
}