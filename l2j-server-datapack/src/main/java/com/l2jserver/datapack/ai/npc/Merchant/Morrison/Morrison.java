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
package com.l2jserver.datapack.ai.npc.Merchant.Morrison;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Morrison extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(3999, 20, 0.000000, 0), // mticket_oren_sword_fix
		new BuySellList(4000, 20, 0.000000, 0), // mticket_oren_pole_fix
		new BuySellList(4001, 20, 0.000000, 0), // mticket_oren_bow_fix
		new BuySellList(4002, 20, 0.000000, 0), // mticket_oren_cleric_fix
		new BuySellList(4003, 20, 0.000000, 0), // mticket_oren_wizard_fix
		new BuySellList(4004, 20, 0.000000, 0), // mticket_oren_sword_move
		new BuySellList(4005, 20, 0.000000, 0), // mticket_oren_pole_move
		new BuySellList(4006, 20, 0.000000, 0), // mticket_oren_bow_move
		new BuySellList(4007, 20, 0.000000, 0), // mticket_oren_cleric_move
		new BuySellList(4008, 20, 0.000000, 0) // mticket_oren_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6077, 20, 0.000000, 0), // adv_mticket_oren_sword_fix
		new BuySellList(6078, 20, 0.000000, 0), // adv_mticket_oren_pole_fix
		new BuySellList(6079, 20, 0.000000, 0), // adv_mticket_oren_bow_fix
		new BuySellList(6080, 20, 0.000000, 0), // adv_mticket_oren_cleric_fix
		new BuySellList(6081, 20, 0.000000, 0), // adv_mticket_oren_wizard_fix
		new BuySellList(6082, 20, 0.000000, 0), // adv_mticket_oren_sword_move
		new BuySellList(6083, 20, 0.000000, 0), // adv_mticket_oren_pole_move
		new BuySellList(6084, 20, 0.000000, 0), // adv_mticket_oren_bow_move
		new BuySellList(6085, 20, 0.000000, 0), // adv_mticket_oren_cleric_move
		new BuySellList(6086, 20, 0.000000, 0) // adv_mticket_oren_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(4009, 20, 0.000000, 0), // mticket_oren_teleporter1
		new BuySellList(4010, 20, 0.000000, 0), // mticket_oren_teleporter2
		new BuySellList(4011, 20, 0.000000, 0) // mticket_oren_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6145, 20, 0.000000, 0), // dawn_mticket_oren_sword_fix
		new BuySellList(6146, 20, 0.000000, 0), // dawn_mticket_oren_pole_fix
		new BuySellList(6147, 20, 0.000000, 0), // dawn_mticket_oren_bow_fix
		new BuySellList(6148, 20, 0.000000, 0), // dawn_mticket_oren_cleric_fix
		new BuySellList(6149, 20, 0.000000, 0), // dawn_mticket_oren_wizard_fix
		new BuySellList(6150, 20, 0.000000, 0), // dawn_mticket_oren_sword_move
		new BuySellList(6151, 20, 0.000000, 0), // dawn_mticket_oren_pole_move
		new BuySellList(6152, 20, 0.000000, 0), // dawn_mticket_oren_bow_move
		new BuySellList(6153, 20, 0.000000, 0), // dawn_mticket_oren_cleric_move
		new BuySellList(6154, 20, 0.000000, 0) // dawn_mticket_oren_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6301, 20, 0.000000, 0), // nephilim_mticket_oren_sword_move
		new BuySellList(6302, 20, 0.000000, 0) // nephilim_mticket_oren_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6205, 20, 0.000000, 0), // twilight_mticket_oren_sword_fix
		new BuySellList(6206, 20, 0.000000, 0), // twilight_mticket_oren_pole_fix
		new BuySellList(6207, 20, 0.000000, 0), // twilight_mticket_oren_bow_fix
		new BuySellList(6208, 20, 0.000000, 0), // twilight_mticket_oren_cleric_fix
		new BuySellList(6209, 20, 0.000000, 0), // twilight_mticket_oren_wizard_fix
		new BuySellList(6210, 20, 0.000000, 0), // twilight_mticket_oren_sword_move
		new BuySellList(6211, 20, 0.000000, 0), // twilight_mticket_oren_pole_move
		new BuySellList(6212, 20, 0.000000, 0), // twilight_mticket_oren_bow_move
		new BuySellList(6213, 20, 0.000000, 0), // twilight_mticket_oren_cleric_move
		new BuySellList(6214, 20, 0.000000, 0), // twilight_mticket_oren_wizard_move
		new BuySellList(6265, 20, 0.000000, 0), // gloom_mticket_oren_sword_fix
		new BuySellList(6266, 20, 0.000000, 0), // gloom_mticket_oren_pole_fix
		new BuySellList(6267, 20, 0.000000, 0), // gloom_mticket_oren_bow_fix
		new BuySellList(6268, 20, 0.000000, 0), // gloom_mticket_oren_cleric_fix
		new BuySellList(6269, 20, 0.000000, 0), // gloom_mticket_oren_wizard_fix
		new BuySellList(6270, 20, 0.000000, 0), // gloom_mticket_oren_sword_move
		new BuySellList(6271, 20, 0.000000, 0), // gloom_mticket_oren_pole_move
		new BuySellList(6272, 20, 0.000000, 0), // gloom_mticket_oren_bow_move
		new BuySellList(6273, 20, 0.000000, 0), // gloom_mticket_oren_cleric_move
		new BuySellList(6274, 20, 0.000000, 0) // gloom_mticket_oren_wizard_move
	};
	
	private static final int npcId = 35228;
	
	public Morrison() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}