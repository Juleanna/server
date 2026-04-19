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
package com.l2jserver.datapack.ai.npc.Merchant.Solinus;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Solinus extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(5205, 20, 0.000000, 0), // mticket_innadrile_sword_fix
		new BuySellList(5206, 20, 0.000000, 0), // mticket_innadrile_pole_fix
		new BuySellList(5207, 20, 0.000000, 0), // mticket_innadrile_bow_fix
		new BuySellList(5208, 20, 0.000000, 0), // mticket_innadrile_cleric_fix
		new BuySellList(5209, 20, 0.000000, 0), // mticket_innadrile_wizard_fix
		new BuySellList(5210, 20, 0.000000, 0), // mticket_innadrile_sword_move
		new BuySellList(5211, 20, 0.000000, 0), // mticket_innadrile_pole_move
		new BuySellList(5212, 20, 0.000000, 0), // mticket_innadrile_bow_move
		new BuySellList(5213, 20, 0.000000, 0), // mticket_innadrile_cleric_move
		new BuySellList(5214, 20, 0.000000, 0) // mticket_innadrile_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6105, 20, 0.000000, 0), // adv_mticket_innadrile_sword_fix
		new BuySellList(6106, 20, 0.000000, 0), // adv_mticket_innadrile_pole_fix
		new BuySellList(6107, 20, 0.000000, 0), // adv_mticket_innadrile_bow_fix
		new BuySellList(6108, 20, 0.000000, 0), // adv_mticket_innadrile_cleric_fix
		new BuySellList(6109, 20, 0.000000, 0), // adv_mticket_innadrile_wizard_fix
		new BuySellList(6110, 20, 0.000000, 0), // adv_mticket_innadrile_sword_move
		new BuySellList(6111, 20, 0.000000, 0), // adv_mticket_innadrile_pole_move
		new BuySellList(6112, 20, 0.000000, 0), // adv_mticket_innadrile_bow_move
		new BuySellList(6113, 20, 0.000000, 0), // adv_mticket_innadrile_cleric_move
		new BuySellList(6114, 20, 0.000000, 0) // adv_mticket_innadrile_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(5215, 20, 0.000000, 0), // mticket_innadrile_teleporter1
		new BuySellList(5218, 20, 0.000000, 0), // mticket_innadrile_teleporter2
		new BuySellList(5219, 20, 0.000000, 0) // mticket_innadrile_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6165, 20, 0.000000, 0), // dawn_mticket_innadrile_sword_fix
		new BuySellList(6166, 20, 0.000000, 0), // dawn_mticket_innadrile_pole_fix
		new BuySellList(6167, 20, 0.000000, 0), // dawn_mticket_innadrile_bow_fix
		new BuySellList(6168, 20, 0.000000, 0), // dawn_mticket_innadrile_cleric_fix
		new BuySellList(6169, 20, 0.000000, 0), // dawn_mticket_innadrile_wizard_fix
		new BuySellList(6170, 20, 0.000000, 0), // dawn_mticket_innadrile_sword_move
		new BuySellList(6171, 20, 0.000000, 0), // dawn_mticket_innadrile_pole_move
		new BuySellList(6172, 20, 0.000000, 0), // dawn_mticket_innadrile_bow_move
		new BuySellList(6173, 20, 0.000000, 0), // dawn_mticket_innadrile_cleric_move
		new BuySellList(6174, 20, 0.000000, 0) // dawn_mticket_innadrile_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6305, 20, 0.000000, 0), // nephilim_mticket_innadrile_sword_move
		new BuySellList(6306, 20, 0.000000, 0) // nephilim_mticket_innadrile_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6225, 20, 0.000000, 0), // twilight_mticket_innadrile_sword_fix
		new BuySellList(6226, 20, 0.000000, 0), // twilight_mticket_innadrile_pole_fix
		new BuySellList(6227, 20, 0.000000, 0), // twilight_mticket_innadrile_bow_fix
		new BuySellList(6228, 20, 0.000000, 0), // twilight_mticket_innadrile_cleric_fix
		new BuySellList(6229, 20, 0.000000, 0), // twilight_mticket_innadrile_wizard_fix
		new BuySellList(6230, 20, 0.000000, 0), // twilight_mticket_innadrile_sword_move
		new BuySellList(6231, 20, 0.000000, 0), // twilight_mticket_innadrile_pole_move
		new BuySellList(6232, 20, 0.000000, 0), // twilight_mticket_innadrile_bow_move
		new BuySellList(6233, 20, 0.000000, 0), // twilight_mticket_innadrile_cleric_move
		new BuySellList(6234, 20, 0.000000, 0), // twilight_mticket_innadrile_wizard_move
		new BuySellList(6285, 20, 0.000000, 0), // gloom_mticket_innadrile_sword_fix
		new BuySellList(6286, 20, 0.000000, 0), // gloom_mticket_innadrile_pole_fix
		new BuySellList(6287, 20, 0.000000, 0), // gloom_mticket_innadrile_bow_fix
		new BuySellList(6288, 20, 0.000000, 0), // gloom_mticket_innadrile_cleric_fix
		new BuySellList(6289, 20, 0.000000, 0), // gloom_mticket_innadrile_wizard_fix
		new BuySellList(6290, 20, 0.000000, 0), // gloom_mticket_innadrile_sword_move
		new BuySellList(6291, 20, 0.000000, 0), // gloom_mticket_innadrile_pole_move
		new BuySellList(6292, 20, 0.000000, 0), // gloom_mticket_innadrile_bow_move
		new BuySellList(6293, 20, 0.000000, 0), // gloom_mticket_innadrile_cleric_move
		new BuySellList(6294, 20, 0.000000, 0) // gloom_mticket_innadrile_wizard_move
	};
	
	private static final int npcId = 35318;
	
	public Solinus() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}