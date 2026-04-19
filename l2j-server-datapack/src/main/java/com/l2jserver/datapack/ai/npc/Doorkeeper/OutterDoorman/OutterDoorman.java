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
package com.l2jserver.datapack.ai.npc.Doorkeeper.OutterDoorman;

import com.l2jserver.datapack.ai.npc.Doorkeeper.Doorkeeper;

/**
* @author Charus
* @version 2.6.3.0
*/
public class OutterDoorman extends Doorkeeper {
	
	private static final int[] npcIds = { 35096, 35138, 35180, 35222, 35267, 35312, 35356, 35503, 35548 };
	
	public OutterDoorman() {
		super(npcIds);
		
		super.fnHi = "data/html/doormen/gludio_outter_doorman001.htm";
		super.fnNotMyLord = "data/html/doormen/gludio_outter_doorman002.htm";
		super.fnUnderSiege = "data/html/doormen/gludio_outter_doorman003.htm";
	}
}