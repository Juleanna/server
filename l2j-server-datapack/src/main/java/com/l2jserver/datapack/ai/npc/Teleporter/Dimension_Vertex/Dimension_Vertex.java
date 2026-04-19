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
package com.l2jserver.datapack.ai.npc.Teleporter.Dimension_Vertex;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class Dimension_Vertex extends Teleporter {
	
	private static final int GREEN_DIMENSION_STONE = 4401;
	private static final int BLUE_DIMENSION_STONE = 4402;
	private static final int RED_DIMENSION_STONE = 4403;
	
	private static final int MS_ASK_TELE_CHAT = -6;
	
	private static final int REPLY_TELE_TOI10 = 10;
	private static final int REPLY_TELE_TOI9 = 9;
	private static final int REPLY_TELE_TOI8 = 8;
	private static final int REPLY_TELE_TOI7 = 7;
	private static final int REPLY_TELE_TOI6 = 6;
	private static final int REPLY_TELE_TOI5 = 5;
	private static final int REPLY_TELE_TOI4 = 4;
	private static final int REPLY_TELE_TOI3 = 3;
	private static final int REPLY_TELE_TOI2 = 2;
	private static final int REPLY_TELE_TOI1 = 1;
	
	private final String _npcName = getClass().getSimpleName().toLowerCase();
	
	public Dimension_Vertex(int npcId) {
		super(npcId);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_TELE_CHAT -> {
				switch (reply) {
					case REPLY_TELE_TOI10 -> {
						if (getQuestItemsCount(talker, RED_DIMENSION_STONE) != 0) {
							takeItems(talker, RED_DIMENSION_STONE, 1);
							talker.teleToLocation(118507, 16605, 5984);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI9 -> {
						if (getQuestItemsCount(talker, RED_DIMENSION_STONE) != 0) {
							takeItems(talker, RED_DIMENSION_STONE, 1);
							talker.teleToLocation(114649, 14144, 4976);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI8 -> {
						if (getQuestItemsCount(talker, RED_DIMENSION_STONE) != 0) {
							takeItems(talker, RED_DIMENSION_STONE, 1);
							talker.teleToLocation(115571, 13723, 3960);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI7 -> {
						if (getQuestItemsCount(talker, RED_DIMENSION_STONE) != 0) {
							takeItems(talker, RED_DIMENSION_STONE, 1);
							talker.teleToLocation(113026, 17687, 2952);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI6 -> {
						if (getQuestItemsCount(talker, BLUE_DIMENSION_STONE) != 0) {
							takeItems(talker, BLUE_DIMENSION_STONE, 1);
							talker.teleToLocation(117131, 16044, 1944);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI5 -> {
						if (getQuestItemsCount(talker, BLUE_DIMENSION_STONE) != 0) {
							takeItems(talker, BLUE_DIMENSION_STONE, 1);
							talker.teleToLocation(114152, 19902, 928);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI4 -> {
						if (getQuestItemsCount(talker, BLUE_DIMENSION_STONE) != 0) {
							takeItems(talker, BLUE_DIMENSION_STONE, 1);
							talker.teleToLocation(114636, 13413, -650);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI3 -> {
						if (getQuestItemsCount(talker, GREEN_DIMENSION_STONE) != 0) {
							takeItems(talker, GREEN_DIMENSION_STONE, 1);
							talker.teleToLocation(111982, 16028, -2100);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI2 -> {
						if (getQuestItemsCount(talker, GREEN_DIMENSION_STONE) != 0) {
							takeItems(talker, GREEN_DIMENSION_STONE, 1);
							talker.teleToLocation(114666, 13380, -3600);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
					case REPLY_TELE_TOI1 -> {
						if (getQuestItemsCount(talker, GREEN_DIMENSION_STONE) != 0) {
							takeItems(talker, GREEN_DIMENSION_STONE, 1);
							talker.teleToLocation(114356, 13423, -5127);
						} else {
							showPage(talker, fnPath + _npcName + "005.htm");
						}
					}
				}
			}
		}
	}
}