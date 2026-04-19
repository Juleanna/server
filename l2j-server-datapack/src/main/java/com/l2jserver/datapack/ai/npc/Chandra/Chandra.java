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
package com.l2jserver.datapack.ai.npc.Chandra;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Chandra extends AbstractNpcAI {
	
	private static final int npcId = 32645;
	
	private static final int MS_ASK_REQUEST_RIDE_WITH_FREIGHT = -7801;
	
	public Chandra() {
		bindFirstTalk(npcId);
		bindStartNpc(npcId);
		bindMenuSelected(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, "chandra001.htm");
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.endsWith(".htm")) {
			showPage(player, event);
		}
		
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_REQUEST_RIDE_WITH_FREIGHT -> {
				switch (reply) {
					case 1 -> {
						final var party0 = talker.getParty();
						if (party0 != null) {
							if (npc.calculateDistance(party0.getLeader(), false, false) <= 1000 && party0.getLeaderObjectId() == talker.getObjectId()) {
								teleportParty(npc, party0, 173492, -112272, -5200, 3000);
							} else {
								showPage(talker, "chandra003.htm");
							}
						} else {
							talker.teleToLocation(173492, -112272, -5200);
						}
					}
				}
			}
		}
	}
}