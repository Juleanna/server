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
package com.l2jserver.datapack.ai.npc.Merchant.PetManager.Mickey;

import com.l2jserver.datapack.ai.npc.Merchant.PetManager.PetManager;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Mickey extends PetManager {
	
	private static final int npcId = 36478;
	
	public Mickey() {
		super(npcId);
		
		super.fnHi = "pet_trader_mickey001.htm";
		super.fnYouAreChaotic = "pet_trader_mickey006.htm";
		super.fnEvolutionSuccess = "pet_devolution_success.htm";
		super.fnEvolveManyPet = "pet_devolution_many_pet.htm";
		super.fnEvolveNoPetPet = "pet_devolution_no_pet.htm";
		super.fnNoPetPet = "pet_devolution_farpet.htm";
		super.fnTooFarPet = "pet_devolution_farpet.htm";
		super.fnNoProperPetPet = "pet_devolution_farpet.htm";
		super.fnNotEnoughLevelPet = "pet_devolution_level.htm";
		super.fnNotEnoughMinLvPet = "pet_devolution_level.htm";
		super.fnNoItemPet = "pet_devolution_no_pet.htm";
	}
}