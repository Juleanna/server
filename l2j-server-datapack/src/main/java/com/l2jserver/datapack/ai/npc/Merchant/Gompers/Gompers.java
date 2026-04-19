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
package com.l2jserver.datapack.ai.npc.Merchant.Gompers;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Gompers extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(7973, 20, 0.000000, 0), // mticket_rune_sword_fix
		new BuySellList(7974, 20, 0.000000, 0), // mticket_rune_pole_fix
		new BuySellList(7975, 20, 0.000000, 0), // mticket_rune_bow_fix
		new BuySellList(7976, 20, 0.000000, 0), // mticket_rune_cleric_fix
		new BuySellList(7977, 20, 0.000000, 0), // mticket_rune_wizard_fix
		new BuySellList(7978, 20, 0.000000, 0), // mticket_rune_sword_move
		new BuySellList(7979, 20, 0.000000, 0), // mticket_rune_pole_move
		new BuySellList(7980, 20, 0.000000, 0), // mticket_rune_bow_move
		new BuySellList(7981, 20, 0.000000, 0), // mticket_rune_cleric_move
		new BuySellList(7982, 20, 0.000000, 0) // mticket_rune_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(7988, 20, 0.000000, 0), // adv_mticket_rune_sword_fix
		new BuySellList(7989, 20, 0.000000, 0), // adv_mticket_rune_pole_fix
		new BuySellList(7990, 20, 0.000000, 0), // adv_mticket_rune_bow_fix
		new BuySellList(7991, 20, 0.000000, 0), // adv_mticket_rune_cleric_fix
		new BuySellList(7992, 20, 0.000000, 0), // adv_mticket_rune_wizard_fix
		new BuySellList(7993, 20, 0.000000, 0), // adv_mticket_rune_sword_move
		new BuySellList(7994, 20, 0.000000, 0), // adv_mticket_rune_pole_move
		new BuySellList(7995, 20, 0.000000, 0), // adv_mticket_rune_bow_move
		new BuySellList(7996, 20, 0.000000, 0), // adv_mticket_rune_cleric_move
		new BuySellList(7997, 20, 0.000000, 0) // adv_mticket_rune_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(7983, 20, 0.000000, 0), // mticket_rune_teleporter1
		new BuySellList(7984, 20, 0.000000, 0), // mticket_rune_teleporter2
		new BuySellList(7985, 20, 0.000000, 0), // mticket_rune_teleporter3
		new BuySellList(7986, 20, 0.000000, 0), // mticket_rune_teleporter4
		new BuySellList(7987, 20, 0.000000, 0) // mticket_rune_teleporter5
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(7998, 20, 0.000000, 0), // dawn_mticket_rune_sword_fix
		new BuySellList(7999, 20, 0.000000, 0), // dawn_mticket_rune_pole_fix
		new BuySellList(8000, 20, 0.000000, 0), // dawn_mticket_rune_bow_fix
		new BuySellList(8001, 20, 0.000000, 0), // dawn_mticket_rune_cleric_fix
		new BuySellList(8002, 20, 0.000000, 0), // dawn_mticket_rune_wizard_fix
		new BuySellList(8003, 20, 0.000000, 0), // dawn_mticket_rune_sword_move
		new BuySellList(8004, 20, 0.000000, 0), // dawn_mticket_rune_pole_move
		new BuySellList(8005, 20, 0.000000, 0), // dawn_mticket_rune_bow_move
		new BuySellList(8006, 20, 0.000000, 0), // dawn_mticket_rune_cleric_move
		new BuySellList(8007, 20, 0.000000, 0) // dawn_mticket_rune_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(8028, 20, 0.000000, 0), // nephilim_mticket_rune_sword_move
		new BuySellList(8029, 20, 0.000000, 0) // nephilim_mticket_rune_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(8008, 20, 0.000000, 0), // twilight_mticket_rune_sword_fix
		new BuySellList(8009, 20, 0.000000, 0), // twilight_mticket_rune_pole_fix
		new BuySellList(8010, 20, 0.000000, 0), // twilight_mticket_rune_bow_fix
		new BuySellList(8011, 20, 0.000000, 0), // twilight_mticket_rune_cleric_fix
		new BuySellList(8012, 20, 0.000000, 0), // twilight_mticket_rune_wizard_fix
		new BuySellList(8013, 20, 0.000000, 0), // twilight_mticket_rune_sword_move
		new BuySellList(8014, 20, 0.000000, 0), // twilight_mticket_rune_pole_move
		new BuySellList(8015, 20, 0.000000, 0), // twilight_mticket_rune_bow_move
		new BuySellList(8016, 20, 0.000000, 0), // twilight_mticket_rune_cleric_move
		new BuySellList(8017, 20, 0.000000, 0), // twilight_mticket_rune_wizard_move
		new BuySellList(8018, 20, 0.000000, 0), // gloom_mticket_rune_sword_fix
		new BuySellList(8019, 20, 0.000000, 0), // gloom_mticket_rune_pole_fix
		new BuySellList(8020, 20, 0.000000, 0), // gloom_mticket_rune_bow_fix
		new BuySellList(8021, 20, 0.000000, 0), // gloom_mticket_rune_cleric_fix
		new BuySellList(8022, 20, 0.000000, 0), // gloom_mticket_rune_wizard_fix
		new BuySellList(8023, 20, 0.000000, 0), // gloom_mticket_rune_sword_move
		new BuySellList(8024, 20, 0.000000, 0), // gloom_mticket_rune_pole_move
		new BuySellList(8025, 20, 0.000000, 0), // gloom_mticket_rune_bow_move
		new BuySellList(8026, 20, 0.000000, 0), // gloom_mticket_rune_cleric_move
		new BuySellList(8027, 20, 0.000000, 0) // gloom_mticket_rune_wizard_move
	};
	
	private static final int npcId = 35511;
	
	public Gompers() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}