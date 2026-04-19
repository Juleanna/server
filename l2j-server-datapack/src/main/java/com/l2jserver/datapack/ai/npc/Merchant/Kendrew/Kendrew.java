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
package com.l2jserver.datapack.ai.npc.Merchant.Kendrew;

import com.l2jserver.datapack.ai.npc.Merchant.MSeller;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Kendrew extends MSeller {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(7918, 20, 0.000000, 0), // mticket_schutt_sword_fix
		new BuySellList(7919, 20, 0.000000, 0), // mticket_schutt_pole_fix
		new BuySellList(7920, 20, 0.000000, 0), // mticket_schutt_bow_fix
		new BuySellList(7921, 20, 0.000000, 0), // mticket_schutt_cleric_fix
		new BuySellList(7922, 20, 0.000000, 0), // mticket_schutt_wizard_fix
		new BuySellList(7923, 20, 0.000000, 0), // mticket_schutt_sword_move
		new BuySellList(7924, 20, 0.000000, 0), // mticket_schutt_pole_move
		new BuySellList(7925, 20, 0.000000, 0), // mticket_schutt_bow_move
		new BuySellList(7926, 20, 0.000000, 0), // mticket_schutt_cleric_move
		new BuySellList(7927, 20, 0.000000, 0) // mticket_schutt_wizard_move
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(7931, 20, 0.000000, 0), // adv_mticket_schutt_sword_fix
		new BuySellList(7932, 20, 0.000000, 0), // adv_mticket_schutt_pole_fix
		new BuySellList(7933, 20, 0.000000, 0), // adv_mticket_schutt_bow_fix
		new BuySellList(7934, 20, 0.000000, 0), // adv_mticket_schutt_cleric_fix
		new BuySellList(7935, 20, 0.000000, 0), // adv_mticket_schutt_wizard_fix
		new BuySellList(7936, 20, 0.000000, 0), // adv_mticket_schutt_sword_move
		new BuySellList(7937, 20, 0.000000, 0), // adv_mticket_schutt_pole_move
		new BuySellList(7938, 20, 0.000000, 0), // adv_mticket_schutt_bow_move
		new BuySellList(7939, 20, 0.000000, 0), // adv_mticket_schutt_cleric_move
		new BuySellList(7940, 20, 0.000000, 0) // adv_mticket_schutt_wizard_move
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(7928, 20, 0.000000, 0), // mticket_schutt_teleporter1
		new BuySellList(7929, 20, 0.000000, 0), // mticket_schutt_teleporter2
		new BuySellList(7930, 20, 0.000000, 0) // mticket_schutt_teleporter3
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(7941, 20, 0.000000, 0), // dawn_mticket_schutt_sword_fix
		new BuySellList(7942, 20, 0.000000, 0), // dawn_mticket_schutt_pole_fix
		new BuySellList(7943, 20, 0.000000, 0), // dawn_mticket_schutt_bow_fix
		new BuySellList(7944, 20, 0.000000, 0), // dawn_mticket_schutt_cleric_fix
		new BuySellList(7945, 20, 0.000000, 0), // dawn_mticket_schutt_wizard_fix
		new BuySellList(7946, 20, 0.000000, 0), // dawn_mticket_schutt_sword_move
		new BuySellList(7947, 20, 0.000000, 0), // dawn_mticket_schutt_pole_move
		new BuySellList(7948, 20, 0.000000, 0), // dawn_mticket_schutt_bow_move
		new BuySellList(7949, 20, 0.000000, 0), // dawn_mticket_schutt_cleric_move
		new BuySellList(7950, 20, 0.000000, 0) // dawn_mticket_schutt_wizard_move
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(7971, 20, 0.000000, 0), // nephilim_mticket_schutt_sword_move
		new BuySellList(7972, 20, 0.000000, 0) // nephilim_mticket_schutt_wizard_move
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(7951, 20, 0.000000, 0), // twilight_mticket_schutt_sword_fix
		new BuySellList(7952, 20, 0.000000, 0), // twilight_mticket_schutt_pole_fix
		new BuySellList(7953, 20, 0.000000, 0), // twilight_mticket_schutt_bow_fix
		new BuySellList(7954, 20, 0.000000, 0), // twilight_mticket_schutt_cleric_fix
		new BuySellList(7955, 20, 0.000000, 0), // twilight_mticket_schutt_wizard_fix
		new BuySellList(7956, 20, 0.000000, 0), // twilight_mticket_schutt_sword_move
		new BuySellList(7957, 20, 0.000000, 0), // twilight_mticket_schutt_pole_move
		new BuySellList(7958, 20, 0.000000, 0), // twilight_mticket_schutt_bow_move
		new BuySellList(7959, 20, 0.000000, 0), // twilight_mticket_schutt_cleric_move
		new BuySellList(7960, 20, 0.000000, 0), // twilight_mticket_schutt_wizard_move
		new BuySellList(7961, 20, 0.000000, 0), // gloom_mticket_schutt_sword_fix
		new BuySellList(7962, 20, 0.000000, 0), // gloom_mticket_schutt_pole_fix
		new BuySellList(7963, 20, 0.000000, 0), // gloom_mticket_schutt_bow_fix
		new BuySellList(7964, 20, 0.000000, 0), // gloom_mticket_schutt_cleric_fix
		new BuySellList(7965, 20, 0.000000, 0), // gloom_mticket_schutt_wizard_fix
		new BuySellList(7966, 20, 0.000000, 0), // gloom_mticket_schutt_sword_move
		new BuySellList(7967, 20, 0.000000, 0), // gloom_mticket_schutt_pole_move
		new BuySellList(7968, 20, 0.000000, 0), // gloom_mticket_schutt_bow_move
		new BuySellList(7969, 20, 0.000000, 0), // gloom_mticket_schutt_cleric_move
		new BuySellList(7970, 20, 0.000000, 0) // gloom_mticket_schutt_wizard_move
	};
	
	private static final int npcId = 35557;
	
	public Kendrew() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		super.sellList1 = buildBuySellList(_sellList1, npcId, 1);
		super.sellList2 = buildBuySellList(_sellList2, npcId, 2);
		super.sellList3 = buildBuySellList(_sellList3, npcId, 3);
		super.sellList4 = buildBuySellList(_sellList4, npcId, 4);
		super.sellList5 = buildBuySellList(_sellList5, npcId, 5);
	}
}