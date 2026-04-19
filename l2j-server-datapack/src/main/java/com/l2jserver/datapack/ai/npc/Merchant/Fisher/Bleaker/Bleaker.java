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
package com.l2jserver.datapack.ai.npc.Merchant.Fisher.Bleaker;

import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Fisher;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Bleaker extends Fisher {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(7807, 20, 0.000000, 0), // green_lure_easy
		new BuySellList(7808, 20, 0.000000, 0), // violet_lure_easy
		new BuySellList(7809, 20, 0.000000, 0), // yellow_lure_easy
		new BuySellList(6520, 20, 0.000000, 0), // green_lure_average
		new BuySellList(6523, 20, 0.000000, 0), // violet_lure_average
		new BuySellList(6526, 20, 0.000000, 0), // yellow_lure_average
		new BuySellList(8506, 20, 0.000000, 0), // green_night_lure_average
		new BuySellList(8509, 20, 0.000000, 0), // violet_night_lure_average
		new BuySellList(8512, 20, 0.000000, 0), // yellow_night_lure_average
		new BuySellList(8484, 20, 0.000000, 0), // big_fish_lure_normal
		new BuySellList(8485, 20, 0.000000, 0), // big_fish_lure_night
		new BuySellList(8486, 20, 0.000000, 0), // big_fish_lure_easy
		new BuySellList(6529, 20, 0.000000, 0), // fp_babyduck_rod
		new BuySellList(6530, 20, 0.000000, 0), // fp_albatros_rod
		new BuySellList(6531, 20, 0.000000, 0), // fp_pelican_rod
		new BuySellList(6532, 20, 0.000000, 0), // fp_kingfisher_rod
		new BuySellList(6533, 20, 0.000000, 0), // fp_cygnus_pole
		new BuySellList(6534, 20, 0.000000, 0), // fp_triton_pole
		new BuySellList(7561, 20, 0.000000, 0), // fishing_manual
		new BuySellList(8193, 20, 0.000000, 0), // oblivion_green
		new BuySellList(8194, 20, 0.000000, 0), // oblivion_jade
		new BuySellList(8195, 20, 0.000000, 0), // oblivion_blue
		new BuySellList(8196, 20, 0.000000, 0), // oblivion_yellow
		new BuySellList(8197, 20, 0.000000, 0), // oblivion_orange
		new BuySellList(8198, 20, 0.000000, 0), // oblivion_violet
		new BuySellList(8199, 20, 0.000000, 0), // oblivion_red
		new BuySellList(8200, 20, 0.000000, 0), // oblivion_white
		new BuySellList(8202, 20, 0.000000, 0) // oblivion_recovery
	};
	
	private static final int npcId = 31567;
	
	public Bleaker() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "fisher_bleaker001.htm";
		super.fnYouAreChaotic = "fisher_bleaker006.htm";
	}
}