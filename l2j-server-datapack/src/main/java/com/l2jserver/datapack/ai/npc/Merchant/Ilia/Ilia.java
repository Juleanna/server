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
package com.l2jserver.datapack.ai.npc.Merchant.Ilia;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForClan;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Ilia extends MerchantForClan {
	
	private static final int npcId = 32025;
	
	public Ilia() {
		super(npcId);
		
		super.fnHi = "clan_merchant_ilia001.htm";
		super.fnHi1 = "clan_merchant_ilia003.htm";
		super.fnNoLeader = "clan_merchant_ilia002.htm";
		super.fnNoPledge = "clan_merchant_ilia004.htm";
		super.fnPledgeFameValue = "clan_merchant_ilia005.htm";
		super.fnNotEnoughItem = "clan_merchant_ilia006.htm";
		super.fnUpdateFameSuccess = "clan_merchant_ilia007.htm";
		super.fnLowerPledgeLvReq = "clan_merchant_ilia008.htm";
	}
}