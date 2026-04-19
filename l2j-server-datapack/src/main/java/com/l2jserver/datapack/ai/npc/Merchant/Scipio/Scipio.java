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
package com.l2jserver.datapack.ai.npc.Merchant.Scipio;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForPvp;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Scipio extends MerchantForPvp {
	
	private static final int npcId = 36480;
	
	public Scipio() {
		super(npcId);
		
		super.fnHi = "pvp_merchant_scipio001.htm";
		super.fnNoPvpPoint = "pvp_merchant_scipio002.htm";
		super.fnNoPledge = "pvp_merchant_scipio004.htm";
		super.fnFameUpSuccess = "pvp_merchant_scipio005.htm";
		super.fnNoPkCount = "pvp_merchant_scipio006.htm";
		super.fnPkDownSuccess = "pvp_merchant_scipio007.htm";
	}
}