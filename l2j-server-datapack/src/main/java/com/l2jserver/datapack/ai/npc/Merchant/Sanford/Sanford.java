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
package com.l2jserver.datapack.ai.npc.Merchant.Sanford;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Sanford extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(3973, 20, 0.000000, 0), // mticket_dion_sword_fix
		new BuySellList(3974, 20, 0.000000, 0), // mticket_dion_pole_fix
		new BuySellList(3975, 20, 0.000000, 0), // mticket_dion_bow_fix
		new BuySellList(3976, 20, 0.000000, 0), // mticket_dion_cleric_fix
		new BuySellList(3977, 20, 0.000000, 0), // mticket_dion_wizard_fix
		new BuySellList(3978, 20, 0.000000, 0), // mticket_dion_sword_move
		new BuySellList(3979, 20, 0.000000, 0), // mticket_dion_pole_move
		new BuySellList(3980, 20, 0.000000, 0), // mticket_dion_bow_move
		new BuySellList(3981, 20, 0.000000, 0), // mticket_dion_cleric_move
		new BuySellList(3982, 20, 0.000000, 0) // mticket_dion_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6051, 20, 0.000000, 0), // adv_mticket_dion_sword_fix
		new BuySellList(6052, 20, 0.000000, 0), // adv_mticket_dion_pole_fix
		new BuySellList(6053, 20, 0.000000, 0), // adv_mticket_dion_bow_fix
		new BuySellList(6054, 20, 0.000000, 0), // adv_mticket_dion_cleric_fix
		new BuySellList(6055, 20, 0.000000, 0), // adv_mticket_dion_wizard_fix
		new BuySellList(6056, 20, 0.000000, 0), // adv_mticket_dion_sword_move
		new BuySellList(6057, 20, 0.000000, 0), // adv_mticket_dion_pole_move
		new BuySellList(6058, 20, 0.000000, 0), // adv_mticket_dion_bow_move
		new BuySellList(6059, 20, 0.000000, 0), // adv_mticket_dion_cleric_move
		new BuySellList(6060, 20, 0.000000, 0) // adv_mticket_dion_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(3983, 20, 0.000000, 0), // mticket_dion_teleporter1
		new BuySellList(3984, 20, 0.000000, 0), // mticket_dion_teleporter2
		new BuySellList(3985, 20, 0.000000, 0) // mticket_dion_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6125, 20, 0.000000, 0), // dawn_mticket_dion_sword_fix
		new BuySellList(6126, 20, 0.000000, 0), // dawn_mticket_dion_pole_fix
		new BuySellList(6127, 20, 0.000000, 0), // dawn_mticket_dion_bow_fix
		new BuySellList(6128, 20, 0.000000, 0), // dawn_mticket_dion_cleric_fix
		new BuySellList(6129, 20, 0.000000, 0), // dawn_mticket_dion_wizard_fix
		new BuySellList(6130, 20, 0.000000, 0), // dawn_mticket_dion_sword_move
		new BuySellList(6131, 20, 0.000000, 0), // dawn_mticket_dion_pole_move
		new BuySellList(6132, 20, 0.000000, 0), // dawn_mticket_dion_bow_move
		new BuySellList(6133, 20, 0.000000, 0), // dawn_mticket_dion_cleric_move
		new BuySellList(6134, 20, 0.000000, 0) // dawn_mticket_dion_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6297, 20, 0.000000, 0), // nephilim_mticket_dion_sword_move
		new BuySellList(6298, 20, 0.000000, 0) // nephilim_mticket_dion_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6185, 20, 0.000000, 0), // twilight_mticket_dion_sword_fix
		new BuySellList(6186, 20, 0.000000, 0), // twilight_mticket_dion_pole_fix
		new BuySellList(6187, 20, 0.000000, 0), // twilight_mticket_dion_bow_fix
		new BuySellList(6188, 20, 0.000000, 0), // twilight_mticket_dion_cleric_fix
		new BuySellList(6189, 20, 0.000000, 0), // twilight_mticket_dion_wizard_fix
		new BuySellList(6190, 20, 0.000000, 0), // twilight_mticket_dion_sword_move
		new BuySellList(6191, 20, 0.000000, 0), // twilight_mticket_dion_pole_move
		new BuySellList(6192, 20, 0.000000, 0), // twilight_mticket_dion_bow_move
		new BuySellList(6193, 20, 0.000000, 0), // twilight_mticket_dion_cleric_move
		new BuySellList(6194, 20, 0.000000, 0), // twilight_mticket_dion_wizard_move
		new BuySellList(6245, 20, 0.000000, 0), // gloom_mticket_dion_sword_fix
		new BuySellList(6246, 20, 0.000000, 0), // gloom_mticket_dion_pole_fix
		new BuySellList(6247, 20, 0.000000, 0), // gloom_mticket_dion_bow_fix
		new BuySellList(6248, 20, 0.000000, 0), // gloom_mticket_dion_cleric_fix
		new BuySellList(6249, 20, 0.000000, 0), // gloom_mticket_dion_wizard_fix
		new BuySellList(6250, 20, 0.000000, 0), // gloom_mticket_dion_sword_move
		new BuySellList(6251, 20, 0.000000, 0), // gloom_mticket_dion_pole_move
		new BuySellList(6252, 20, 0.000000, 0), // gloom_mticket_dion_bow_move
		new BuySellList(6253, 20, 0.000000, 0), // gloom_mticket_dion_cleric_move
		new BuySellList(6254, 20, 0.000000, 0) // gloom_mticket_dion_wizard_move
	};
	
	private static final int npcId = 35144;
	
	public Sanford() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}