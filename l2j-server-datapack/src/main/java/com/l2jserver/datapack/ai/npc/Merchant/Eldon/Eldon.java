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
package com.l2jserver.datapack.ai.npc.Merchant.Eldon;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Eldon extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(4012, 20, 0.000000, 0), // mticket_aden_sword_fix
		new BuySellList(4013, 20, 0.000000, 0), // mticket_aden_pole_fix
		new BuySellList(4014, 20, 0.000000, 0), // mticket_aden_bow_fix
		new BuySellList(4015, 20, 0.000000, 0), // mticket_aden_cleric_fix
		new BuySellList(4016, 20, 0.000000, 0), // mticket_aden_wizard_fix
		new BuySellList(4017, 20, 0.000000, 0), // mticket_aden_sword_move
		new BuySellList(4018, 20, 0.000000, 0), // mticket_aden_pole_move
		new BuySellList(4019, 20, 0.000000, 0), // mticket_aden_bow_move
		new BuySellList(4020, 20, 0.000000, 0), // mticket_aden_cleric_move
		new BuySellList(4021, 20, 0.000000, 0) // mticket_aden_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6090, 20, 0.000000, 0), // adv_mticket_aden_sword_fix
		new BuySellList(6091, 20, 0.000000, 0), // adv_mticket_aden_pole_fix
		new BuySellList(6092, 20, 0.000000, 0), // adv_mticket_aden_bow_fix
		new BuySellList(6093, 20, 0.000000, 0), // adv_mticket_aden_cleric_fix
		new BuySellList(6094, 20, 0.000000, 0), // adv_mticket_aden_wizard_fix
		new BuySellList(6095, 20, 0.000000, 0), // adv_mticket_aden_sword_move
		new BuySellList(6096, 20, 0.000000, 0), // adv_mticket_aden_pole_move
		new BuySellList(6097, 20, 0.000000, 0), // adv_mticket_aden_bow_move
		new BuySellList(6098, 20, 0.000000, 0), // adv_mticket_aden_cleric_move
		new BuySellList(6099, 20, 0.000000, 0) // adv_mticket_aden_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(4022, 20, 0.000000, 0), // mticket_aden_teleporter1
		new BuySellList(4023, 20, 0.000000, 0), // mticket_aden_teleporter2
		new BuySellList(4024, 20, 0.000000, 0), // mticket_aden_teleporter3
		new BuySellList(4025, 20, 0.000000, 0), // mticket_aden_teleporter4
		new BuySellList(4026, 20, 0.000000, 0) // mticket_aden_teleporter5
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6155, 20, 0.000000, 0), // dawn_mticket_aden_sword_fix
		new BuySellList(6156, 20, 0.000000, 0), // dawn_mticket_aden_pole_fix
		new BuySellList(6157, 20, 0.000000, 0), // dawn_mticket_aden_bow_fix
		new BuySellList(6158, 20, 0.000000, 0), // dawn_mticket_aden_cleric_fix
		new BuySellList(6159, 20, 0.000000, 0), // dawn_mticket_aden_wizard_fix
		new BuySellList(6160, 20, 0.000000, 0), // dawn_mticket_aden_sword_move
		new BuySellList(6161, 20, 0.000000, 0), // dawn_mticket_aden_pole_move
		new BuySellList(6162, 20, 0.000000, 0), // dawn_mticket_aden_bow_move
		new BuySellList(6163, 20, 0.000000, 0), // dawn_mticket_aden_cleric_move
		new BuySellList(6164, 20, 0.000000, 0) // dawn_mticket_aden_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6303, 20, 0.000000, 0), // nephilim_mticket_aden_sword_move
		new BuySellList(6304, 20, 0.000000, 0) // nephilim_mticket_aden_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6215, 20, 0.000000, 0), // twilight_mticket_aden_sword_fix
		new BuySellList(6216, 20, 0.000000, 0), // twilight_mticket_aden_pole_fix
		new BuySellList(6217, 20, 0.000000, 0), // twilight_mticket_aden_bow_fix
		new BuySellList(6218, 20, 0.000000, 0), // twilight_mticket_aden_cleric_fix
		new BuySellList(6219, 20, 0.000000, 0), // twilight_mticket_aden_wizard_fix
		new BuySellList(6220, 20, 0.000000, 0), // twilight_mticket_aden_sword_move
		new BuySellList(6221, 20, 0.000000, 0), // twilight_mticket_aden_pole_move
		new BuySellList(6222, 20, 0.000000, 0), // twilight_mticket_aden_bow_move
		new BuySellList(6223, 20, 0.000000, 0), // twilight_mticket_aden_cleric_move
		new BuySellList(6224, 20, 0.000000, 0), // twilight_mticket_aden_wizard_move
		new BuySellList(6275, 20, 0.000000, 0), // gloom_mticket_aden_sword_fix
		new BuySellList(6276, 20, 0.000000, 0), // gloom_mticket_aden_pole_fix
		new BuySellList(6277, 20, 0.000000, 0), // gloom_mticket_aden_bow_fix
		new BuySellList(6278, 20, 0.000000, 0), // gloom_mticket_aden_cleric_fix
		new BuySellList(6279, 20, 0.000000, 0), // gloom_mticket_aden_wizard_fix
		new BuySellList(6280, 20, 0.000000, 0), // gloom_mticket_aden_sword_move
		new BuySellList(6281, 20, 0.000000, 0), // gloom_mticket_aden_pole_move
		new BuySellList(6282, 20, 0.000000, 0), // gloom_mticket_aden_bow_move
		new BuySellList(6283, 20, 0.000000, 0), // gloom_mticket_aden_cleric_move
		new BuySellList(6284, 20, 0.000000, 0) // gloom_mticket_aden_wizard_move
	};
	
	private static final int npcId = 35276;
	
	public Eldon() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}