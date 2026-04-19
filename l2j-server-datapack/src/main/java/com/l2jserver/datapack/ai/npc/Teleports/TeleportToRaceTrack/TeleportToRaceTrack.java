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
package com.l2jserver.datapack.ai.npc.Teleports.TeleportToRaceTrack;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * Monster Derby Track teleport AI.
 * @author Plim
 */
public final class TeleportToRaceTrack extends AbstractNpcAI {
	// NPC
	private static final int RACE_MANAGER = 30995;
	
	private static final int Q00255_TUTORIAL = 255;
	
	public TeleportToRaceTrack() {
		bindMenuSelected(RACE_MANAGER);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		
		if (ask == Q00255_TUTORIAL) {
			if (reply == 1) {
				final var q255 = QuestManager.getInstance().getQuest(Q00255_TUTORIAL);
				final var qs255 = q255.getQuestState(talker, true);
				
				int i0 = qs255.getMemoStateEx(1);
				int i1 = i0 % 10000;
				if (i1 >= 95 && i1 < 195) {
					talker.teleToLocation(-12782, 122862, -3114);
					return;
				}
				if (i1 >= 195 && i1 < 295) {
					talker.teleToLocation(-80684, 149770, -3043);
					return;
				}
				if (i1 >= 295 && i1 < 395) {
					talker.teleToLocation(15744, 142928, -2704);
					return;
				}
				if (i1 >= 395 && i1 < 495) {
					talker.teleToLocation(83475, 147966, -3404);
					return;
				}
				if (i1 >= 495 && i1 < 595) {
					talker.teleToLocation(82971, 53207, -1470);
					return;
				}
				if (i1 >= 595 && i1 < 695) {
					talker.teleToLocation(117110, 76883, -2670);
					return;
				}
				if (i1 >= 695 && i1 < 795) {
					talker.teleToLocation(146705, 25840, -2000);
					return;
				}
				if (i1 >= 795 && i1 < 895) {
					talker.teleToLocation(111333, 219345, -3546);
					return;
				}
				if (i1 >= 895 && i1 < 995) {
					talker.teleToLocation(12919, 181038, -3560);
					return;
				}
				if (i1 >= 995 && i1 < 1095) {
					talker.teleToLocation(147870, -55380, -2728);
					return;
				}
				if (i1 >= 1095 && i1 < 1195) {
					talker.teleToLocation(43845, -47820, -792);
					return;
				}
				if (i1 >= 1195 && i1 < 1295) {
					talker.teleToLocation(87099, -143426, -1288);
					return;
				}
			}
		}
	}
}
