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
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.Doorkeeper.InnerDoorman;

import com.l2jserver.datapack.ai.npc.Doorkeeper.Doorkeeper;

/**
* @author Charus
* @version 2.6.3.0
*/
public class InnerDoorman extends Doorkeeper {
	
	private static final int[] npcIds = { 35097, 35139, 35181, 35223, 35268, 35269, 35270, 35271, 35313, 35357, 35358, 35359, 35360, 35504, 35505, 35549, 35550, 35551, 35552 };
	
	public InnerDoorman() {
		super(npcIds);
		
		super.fnHi = "data/html/doormen/gludio_inner_doorman001.htm";
		super.fnNotMyLord = "data/html/doormen/gludio_inner_doorman002.htm";
		super.fnUnderSiege = "data/html/doormen/gludio_inner_doorman003.htm";
	}
}