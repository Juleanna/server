/*
 * Copyright © 2004-2023 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.actor.instance;

import static com.l2jserver.gameserver.config.Configuration.character;

import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.base.PlayerClass;

public final class L2VillageMasterKamaelInstance extends L2VillageMasterInstance {
	
	public L2VillageMasterKamaelInstance(L2NpcTemplate template) {
		super(template);
	}
	
	@Override
	protected String getSubClassMenu(Race race) {
		if (character().subclassEverywhere() || (race == Race.KAMAEL)) {
			return "data/html/villagemaster/SubClass.htm";
		}
		
		return "data/html/villagemaster/SubClass_NoKamael.htm";
	}
	
	@Override
	protected String getSubClassFail() {
		return "data/html/villagemaster/SubClass_Fail_Kamael.htm";
	}
	
	@Override
	protected boolean checkQuests(L2PcInstance player) {
		return player.isNoble() || player.hasQuestCompleted("Q00234_FatesWhisper") || player.hasQuestCompleted("Q00236_SeedsOfChaos");
	}
	
	@Override
	protected boolean checkVillageMasterRace(PlayerClass pclass) {
		if (pclass == null) {
			return false;
		}
		return pclass.isOfRace(Race.KAMAEL);
	}
}
