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
package com.l2jserver.datapack.ai.npc.Merchant.Abercrombie;

import com.l2jserver.datapack.ai.npc.Merchant.MerchantForFriendSpecial;

/**
* @author Zoey76
* @version 2.6.3.0
*/
public class Abercrombie extends MerchantForFriendSpecial {
	
	private static final int npcId = 31555;
	
	public Abercrombie() {
		super(npcId);
		
		super.fnHi = "supplier_abercrombie001.htm";
		super.fnNoFriend = "supplier_abercrombie009.htm";
		super.fnSpecialProduct1 = "supplier_abercrombie008.htm";
		super.fnSpecialProduct2 = "supplier_abercrombie007.htm";
	}
}