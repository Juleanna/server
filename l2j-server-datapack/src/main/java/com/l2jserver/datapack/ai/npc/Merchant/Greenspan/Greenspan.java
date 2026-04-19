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
package com.l2jserver.datapack.ai.npc.Merchant.Greenspan;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Greenspan extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(3960, 20, 0.000000, 0), // mticket_gludio_sword_fix
		new BuySellList(3961, 20, 0.000000, 0), // mticket_gludio_pole_fix
		new BuySellList(3962, 20, 0.000000, 0), // mticket_gludio_bow_fix
		new BuySellList(3963, 20, 0.000000, 0), // mticket_gludio_cleric_fix
		new BuySellList(3964, 20, 0.000000, 0), // mticket_gludio_wizard_fix
		new BuySellList(3965, 20, 0.000000, 0), // mticket_gludio_sword_move
		new BuySellList(3966, 20, 0.000000, 0), // mticket_gludio_pole_move
		new BuySellList(3967, 20, 0.000000, 0), // mticket_gludio_bow_move
		new BuySellList(3968, 20, 0.000000, 0), // mticket_gludio_cleric_move
		new BuySellList(3969, 20, 0.000000, 0) // mticket_gludio_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6038, 20, 0.000000, 0), // adv_mticket_gludio_sword_fix
		new BuySellList(6039, 20, 0.000000, 0), // adv_mticket_gludio_pole_fix
		new BuySellList(6040, 20, 0.000000, 0), // adv_mticket_gludio_bow_fix
		new BuySellList(6041, 20, 0.000000, 0), // adv_mticket_gludio_cleric_fix
		new BuySellList(6042, 20, 0.000000, 0), // adv_mticket_gludio_wizard_fix
		new BuySellList(6043, 20, 0.000000, 0), // adv_mticket_gludio_sword_move
		new BuySellList(6044, 20, 0.000000, 0), // adv_mticket_gludio_pole_move
		new BuySellList(6045, 20, 0.000000, 0), // adv_mticket_gludio_bow_move
		new BuySellList(6046, 20, 0.000000, 0), // adv_mticket_gludio_cleric_move
		new BuySellList(6047, 20, 0.000000, 0) // adv_mticket_gludio_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(3970, 20, 0.000000, 0), // mticket_gludio_teleporter1
		new BuySellList(3971, 20, 0.000000, 0), // mticket_gludio_teleporter2
		new BuySellList(3972, 20, 0.000000, 0) // mticket_gludio_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6115, 20, 0.000000, 0), // dawn_mticket_gludio_sword_fix
		new BuySellList(6116, 20, 0.000000, 0), // dawn_mticket_gludio_pole_fix
		new BuySellList(6117, 20, 0.000000, 0), // dawn_mticket_gludio_bow_fix
		new BuySellList(6118, 20, 0.000000, 0), // dawn_mticket_gludio_cleric_fix
		new BuySellList(6119, 20, 0.000000, 0), // dawn_mticket_gludio_wizard_fix
		new BuySellList(6120, 20, 0.000000, 0), // dawn_mticket_gludio_sword_move
		new BuySellList(6121, 20, 0.000000, 0), // dawn_mticket_gludio_pole_move
		new BuySellList(6122, 20, 0.000000, 0), // dawn_mticket_gludio_bow_move
		new BuySellList(6123, 20, 0.000000, 0), // dawn_mticket_gludio_cleric_move
		new BuySellList(6124, 20, 0.000000, 0) // dawn_mticket_gludio_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6295, 20, 0.000000, 0), // nephilim_mticket_gludio_sword_move
		new BuySellList(6296, 20, 0.000000, 0) // nephilim_mticket_gludio_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6175, 20, 0.000000, 0), // twilight_mticket_gludio_sword_fix
		new BuySellList(6176, 20, 0.000000, 0), // twilight_mticket_gludio_pole_fix
		new BuySellList(6177, 20, 0.000000, 0), // twilight_mticket_gludio_bow_fix
		new BuySellList(6178, 20, 0.000000, 0), // twilight_mticket_gludio_cleric_fix
		new BuySellList(6179, 20, 0.000000, 0), // twilight_mticket_gludio_wizard_fix
		new BuySellList(6180, 20, 0.000000, 0), // twilight_mticket_gludio_sword_move
		new BuySellList(6181, 20, 0.000000, 0), // twilight_mticket_gludio_pole_move
		new BuySellList(6182, 20, 0.000000, 0), // twilight_mticket_gludio_bow_move
		new BuySellList(6183, 20, 0.000000, 0), // twilight_mticket_gludio_cleric_move
		new BuySellList(6184, 20, 0.000000, 0), // twilight_mticket_gludio_wizard_move
		new BuySellList(6235, 20, 0.000000, 0), // gloom_mticket_gludio_sword_fix
		new BuySellList(6236, 20, 0.000000, 0), // gloom_mticket_gludio_pole_fix
		new BuySellList(6237, 20, 0.000000, 0), // gloom_mticket_gludio_bow_fix
		new BuySellList(6238, 20, 0.000000, 0), // gloom_mticket_gludio_cleric_fix
		new BuySellList(6239, 20, 0.000000, 0), // gloom_mticket_gludio_wizard_fix
		new BuySellList(6240, 20, 0.000000, 0), // gloom_mticket_gludio_sword_move
		new BuySellList(6241, 20, 0.000000, 0), // gloom_mticket_gludio_pole_move
		new BuySellList(6242, 20, 0.000000, 0), // gloom_mticket_gludio_bow_move
		new BuySellList(6243, 20, 0.000000, 0), // gloom_mticket_gludio_cleric_move
		new BuySellList(6244, 20, 0.000000, 0) // gloom_mticket_gludio_wizard_move
	};
	
	private static final int npcId = 35102;
	
	public Greenspan() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}