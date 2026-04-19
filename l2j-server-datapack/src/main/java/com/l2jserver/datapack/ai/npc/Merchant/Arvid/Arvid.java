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
package com.l2jserver.datapack.ai.npc.Merchant.Arvid;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Arvid extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(3986, 20, 0.000000, 0), // mticket_giran_sword_fix
		new BuySellList(3987, 20, 0.000000, 0), // mticket_giran_pole_fix
		new BuySellList(3988, 20, 0.000000, 0), // mticket_giran_bow_fix
		new BuySellList(3989, 20, 0.000000, 0), // mticket_giran_cleric_fix
		new BuySellList(3990, 20, 0.000000, 0), // mticket_giran_wizard_fix
		new BuySellList(3991, 20, 0.000000, 0), // mticket_giran_sword_move
		new BuySellList(3992, 20, 0.000000, 0), // mticket_giran_pole_move
		new BuySellList(3993, 20, 0.000000, 0), // mticket_giran_bow_move
		new BuySellList(3994, 20, 0.000000, 0), // mticket_giran_cleric_move
		new BuySellList(3995, 20, 0.000000, 0) // mticket_giran_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6064, 20, 0.000000, 0), // adv_mticket_giran_sword_fix
		new BuySellList(6065, 20, 0.000000, 0), // adv_mticket_giran_pole_fix
		new BuySellList(6066, 20, 0.000000, 0), // adv_mticket_giran_bow_fix
		new BuySellList(6067, 20, 0.000000, 0), // adv_mticket_giran_cleric_fix
		new BuySellList(6068, 20, 0.000000, 0), // adv_mticket_giran_wizard_fix
		new BuySellList(6069, 20, 0.000000, 0), // adv_mticket_giran_sword_move
		new BuySellList(6070, 20, 0.000000, 0), // adv_mticket_giran_pole_move
		new BuySellList(6071, 20, 0.000000, 0), // adv_mticket_giran_bow_move
		new BuySellList(6072, 20, 0.000000, 0), // adv_mticket_giran_cleric_move
		new BuySellList(6073, 20, 0.000000, 0) // adv_mticket_giran_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(3996, 20, 0.000000, 0), // mticket_giran_teleporter1
		new BuySellList(3997, 20, 0.000000, 0), // mticket_giran_teleporter2
		new BuySellList(3998, 20, 0.000000, 0) // mticket_giran_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6135, 20, 0.000000, 0), // dawn_mticket_giran_sword_fix
		new BuySellList(6136, 20, 0.000000, 0), // dawn_mticket_giran_pole_fix
		new BuySellList(6137, 20, 0.000000, 0), // dawn_mticket_giran_bow_fix
		new BuySellList(6138, 20, 0.000000, 0), // dawn_mticket_giran_cleric_fix
		new BuySellList(6139, 20, 0.000000, 0), // dawn_mticket_giran_wizard_fix
		new BuySellList(6140, 20, 0.000000, 0), // dawn_mticket_giran_sword_move
		new BuySellList(6141, 20, 0.000000, 0), // dawn_mticket_giran_pole_move
		new BuySellList(6142, 20, 0.000000, 0), // dawn_mticket_giran_bow_move
		new BuySellList(6143, 20, 0.000000, 0), // dawn_mticket_giran_cleric_move
		new BuySellList(6144, 20, 0.000000, 0) // dawn_mticket_giran_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6299, 20, 0.000000, 0), // nephilim_mticket_giran_sword_move
		new BuySellList(6300, 20, 0.000000, 0) // nephilim_mticket_giran_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6195, 20, 0.000000, 0), // twilight_mticket_giran_sword_fix
		new BuySellList(6196, 20, 0.000000, 0), // twilight_mticket_giran_pole_fix
		new BuySellList(6197, 20, 0.000000, 0), // twilight_mticket_giran_bow_fix
		new BuySellList(6198, 20, 0.000000, 0), // twilight_mticket_giran_cleric_fix
		new BuySellList(6199, 20, 0.000000, 0), // twilight_mticket_giran_wizard_fix
		new BuySellList(6200, 20, 0.000000, 0), // twilight_mticket_giran_sword_move
		new BuySellList(6201, 20, 0.000000, 0), // twilight_mticket_giran_pole_move
		new BuySellList(6202, 20, 0.000000, 0), // twilight_mticket_giran_bow_move
		new BuySellList(6203, 20, 0.000000, 0), // twilight_mticket_giran_cleric_move
		new BuySellList(6204, 20, 0.000000, 0), // twilight_mticket_giran_wizard_move
		new BuySellList(6255, 20, 0.000000, 0), // gloom_mticket_giran_sword_fix
		new BuySellList(6256, 20, 0.000000, 0), // gloom_mticket_giran_pole_fix
		new BuySellList(6257, 20, 0.000000, 0), // gloom_mticket_giran_bow_fix
		new BuySellList(6258, 20, 0.000000, 0), // gloom_mticket_giran_cleric_fix
		new BuySellList(6259, 20, 0.000000, 0), // gloom_mticket_giran_wizard_fix
		new BuySellList(6260, 20, 0.000000, 0), // gloom_mticket_giran_sword_move
		new BuySellList(6261, 20, 0.000000, 0), // gloom_mticket_giran_pole_move
		new BuySellList(6262, 20, 0.000000, 0), // gloom_mticket_giran_bow_move
		new BuySellList(6263, 20, 0.000000, 0), // gloom_mticket_giran_cleric_move
		new BuySellList(6264, 20, 0.000000, 0) // gloom_mticket_giran_wizard_move
	};
	
	private static final int npcId = 35186;
	
	public Arvid() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}