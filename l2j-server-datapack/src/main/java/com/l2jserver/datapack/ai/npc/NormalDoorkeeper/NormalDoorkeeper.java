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
package com.l2jserver.datapack.ai.npc.NormalDoorkeeper;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.DoorData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class NormalDoorkeeper extends AbstractNpcAI {
	
	public static final String fnPath = "data/html/doormen/";
	
	protected String fnHi;
	
	private static final int MS_ASK_OPENCLOSE_DOOR = -201;
	
	private static final int REPLY_OPEN_DOOR = 1;
	private static final int REPLY_CLOSE_DOOR = 2;
	
	public NormalDoorkeeper(int npcId) {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, fnHi);
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var doorName1 = npc.getTemplate().getParameters().getString("DoorName1", null);
		
		switch (ask) {
			case MS_ASK_OPENCLOSE_DOOR -> {
				switch (reply) {
					case REPLY_OPEN_DOOR -> {
						final var door = DoorData.getInstance().getDoorByName(doorName1);
						if (door != null) {
							door.openMe();
						}
					}
					case REPLY_CLOSE_DOOR -> {
						final var door = DoorData.getInstance().getDoorByName(doorName1);
						if (door != null) {
							door.closeMe();
						}
					}
				}
			}
		}
	}
}