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
package com.l2jserver.datapack.ai.npc.Merchant.Katrine;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForNewbie;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Katrine extends MerchantForNewbie {
	
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
	
	private static final int npcId = 30004;
	
	public Katrine() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "katrine001.htm";
		super.fnYouAreChaotic = "katrine006.htm";
	}
}