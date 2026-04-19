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
package com.l2jserver.datapack.ai.npc.Merchant.Lowell;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Lowell extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(6779, 20, 0.000000, 0), // mticket_godad_sword_fix
		new BuySellList(6780, 20, 0.000000, 0), // mticket_godad_pole_fix
		new BuySellList(6781, 20, 0.000000, 0), // mticket_godad_bow_fix
		new BuySellList(6782, 20, 0.000000, 0), // mticket_godad_cleric_fix
		new BuySellList(6783, 20, 0.000000, 0), // mticket_godad_wizard_fix
		new BuySellList(6784, 20, 0.000000, 0), // mticket_godad_sword_move
		new BuySellList(6785, 20, 0.000000, 0), // mticket_godad_pole_move
		new BuySellList(6786, 20, 0.000000, 0), // mticket_godad_bow_move
		new BuySellList(6787, 20, 0.000000, 0), // mticket_godad_cleric_move
		new BuySellList(6788, 20, 0.000000, 0) // mticket_godad_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(6792, 20, 0.000000, 0), // adv_mticket_godad_sword_fix
		new BuySellList(6793, 20, 0.000000, 0), // adv_mticket_godad_pole_fix
		new BuySellList(6794, 20, 0.000000, 0), // adv_mticket_godad_bow_fix
		new BuySellList(6795, 20, 0.000000, 0), // adv_mticket_godad_cleric_fix
		new BuySellList(6796, 20, 0.000000, 0), // adv_mticket_godad_wizard_fix
		new BuySellList(6797, 20, 0.000000, 0), // adv_mticket_godad_sword_move
		new BuySellList(6798, 20, 0.000000, 0), // adv_mticket_godad_pole_move
		new BuySellList(6799, 20, 0.000000, 0), // adv_mticket_godad_bow_move
		new BuySellList(6800, 20, 0.000000, 0), // adv_mticket_godad_cleric_move
		new BuySellList(6801, 20, 0.000000, 0) // adv_mticket_godad_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(6789, 20, 0.000000, 0), // mticket_godad_teleporter1
		new BuySellList(6790, 20, 0.000000, 0), // mticket_godad_teleporter2
		new BuySellList(6791, 20, 0.000000, 0) // mticket_godad_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(6802, 20, 0.000000, 0), // dawn_mticket_godad_sword_fix
		new BuySellList(6803, 20, 0.000000, 0), // dawn_mticket_godad_pole_fix
		new BuySellList(6804, 20, 0.000000, 0), // dawn_mticket_godad_bow_fix
		new BuySellList(6805, 20, 0.000000, 0), // dawn_mticket_godad_cleric_fix
		new BuySellList(6806, 20, 0.000000, 0), // dawn_mticket_godad_wizard_fix
		new BuySellList(6807, 20, 0.000000, 0), // dawn_mticket_godad_sword_move
		new BuySellList(6808, 20, 0.000000, 0), // dawn_mticket_godad_pole_move
		new BuySellList(6809, 20, 0.000000, 0), // dawn_mticket_godad_bow_move
		new BuySellList(6810, 20, 0.000000, 0), // dawn_mticket_godad_cleric_move
		new BuySellList(6811, 20, 0.000000, 0) // dawn_mticket_godad_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(6832, 20, 0.000000, 0), // nephilim_mticket_godad_sword_move
		new BuySellList(6833, 20, 0.000000, 0) // nephilim_mticket_godad_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(6812, 20, 0.000000, 0), // twilight_mticket_godad_sword_fix
		new BuySellList(6813, 20, 0.000000, 0), // twilight_mticket_godad_pole_fix
		new BuySellList(6814, 20, 0.000000, 0), // twilight_mticket_godad_bow_fix
		new BuySellList(6815, 20, 0.000000, 0), // twilight_mticket_godad_cleric_fix
		new BuySellList(6816, 20, 0.000000, 0), // twilight_mticket_godad_wizard_fix
		new BuySellList(6817, 20, 0.000000, 0), // twilight_mticket_godad_sword_move
		new BuySellList(6818, 20, 0.000000, 0), // twilight_mticket_godad_pole_move
		new BuySellList(6819, 20, 0.000000, 0), // twilight_mticket_godad_bow_move
		new BuySellList(6820, 20, 0.000000, 0), // twilight_mticket_godad_cleric_move
		new BuySellList(6821, 20, 0.000000, 0), // twilight_mticket_godad_wizard_move
		new BuySellList(6822, 20, 0.000000, 0), // gloom_mticket_godad_sword_fix
		new BuySellList(6823, 20, 0.000000, 0), // gloom_mticket_godad_pole_fix
		new BuySellList(6824, 20, 0.000000, 0), // gloom_mticket_godad_bow_fix
		new BuySellList(6825, 20, 0.000000, 0), // gloom_mticket_godad_cleric_fix
		new BuySellList(6826, 20, 0.000000, 0), // gloom_mticket_godad_wizard_fix
		new BuySellList(6827, 20, 0.000000, 0), // gloom_mticket_godad_sword_move
		new BuySellList(6828, 20, 0.000000, 0), // gloom_mticket_godad_pole_move
		new BuySellList(6829, 20, 0.000000, 0), // gloom_mticket_godad_bow_move
		new BuySellList(6830, 20, 0.000000, 0), // gloom_mticket_godad_cleric_move
		new BuySellList(6831, 20, 0.000000, 0) // gloom_mticket_godad_wizard_move
	};
	
	private static final int npcId = 35365;
	
	public Lowell() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}