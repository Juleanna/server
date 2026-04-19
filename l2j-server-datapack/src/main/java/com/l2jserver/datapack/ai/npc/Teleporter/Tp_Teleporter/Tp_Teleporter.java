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
package com.l2jserver.datapack.ai.npc.Teleporter.Tp_Teleporter;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Tp_Teleporter extends Teleporter {
	
	private static final int Q00255_TUTORIAL = 255;
	
	private static final int MS_ASK_TELE_CHAT = -1816;
	
	private static final int REPLY_TELE_BACK = 1;
	private static final int REPLY_TELE_UNDERGROUND_COLISEUM = 2;
	private static final int REPLY_TELE_KRATEI_CUBE = 3;
	private static final int REPLY_ITEM_LIST = 4;
	private static final int REPLY_TELE_HANDYS_ARENA = 5;
	
	private static final int npcId = 32378;
	
	public Tp_Teleporter() {
		super(npcId);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		if (ask == MS_ASK_TELE_CHAT) {
			switch (reply) {
				case REPLY_TELE_BACK -> {
					final var q255 = QuestManager.getInstance().getQuest(Q00255_TUTORIAL);
					final var qs255 = q255.getQuestState(talker, true);
					
					int i0 = qs255.getMemoStateEx(1);
					int i1 = i0 / 1000000;
					if (i1 >= 100) {
						i1 = i1 % 100;
					}
					if (i1 < 0) {
						i1 = 0;
					}
					switch (i1) {
						case 0 -> {
							talker.teleToLocation(43835, -47749, -792);
						}
						case 1 -> {
							talker.teleToLocation(-12787, 122779, -3114);
						}
						case 2 -> {
							talker.teleToLocation(-80684, 149770, -3043);
						}
						case 3 -> {
							talker.teleToLocation(15472, 142880, -2699);
						}
						case 4 -> {
							talker.teleToLocation(83551, 147945, -3400);
						}
						case 5 -> {
							talker.teleToLocation(82971, 53207, -1470);
						}
						case 6 -> {
							talker.teleToLocation(117088, 76931, -2670);
						}
						case 7 -> {
							talker.teleToLocation(146783, 25808, -2000);
						}
						case 8 -> {
							talker.teleToLocation(111455, 219400, -3546);
						}
						case 9 -> {
							talker.teleToLocation(46911, 49441, -3056);
						}
						case 10 -> {
							talker.teleToLocation(148024, -55281, -2728);
						}
						case 11 -> {
							talker.teleToLocation(43835, -47749, -792);
						}
						case 12 -> {
							talker.teleToLocation(87126, -143520, -1288);
						}
						case 13 -> {
							talker.teleToLocation(-84752, 243122, -3728);
						}
						case 14 -> {
							talker.teleToLocation(11179, 15848, -4584);
						}
						case 15 -> {
							talker.teleToLocation(17441, 170434, -3504);
						}
						case 16 -> {
							talker.teleToLocation(-44132, -113766, -240);
						}
						case 17 -> {
							talker.teleToLocation(114976, -178774, -856);
						}
						case 18 -> {
							talker.teleToLocation(-119377, 47000, 360);
						}
						default -> {
							broadcastNpcSay((L2Npc) npc, Say2.ALL, NpcStringId.IF_YOUR_MEANS_OF_ARRIVAL_WAS_A_BIT_UNCONVENTIONAL_THEN_ILL_BE_SENDING_YOU_BACK_TO_RUNE_TOWNSHIP_WHICH_IS_THE_NEAREST_TOWN);
						}
					}
					
					i1 = i1 * 1000000;
					qs255.setMemoStateEx(1, i0 - i1);
				}
				case REPLY_TELE_UNDERGROUND_COLISEUM -> {
					if (getRandom(4) < 1) {
						talker.teleToLocation(-81896, -49589, -10352);
					} else {
						if (getRandom(3) < 1) {
							talker.teleToLocation(-82271, -49196, -10352);
						} else {
							if (getRandom(2) < 1) {
								talker.teleToLocation(-81886, -48784, -10352);
							} else {
								talker.teleToLocation(-81490, -49167, -10352);
							}
						}
					}
				}
				case REPLY_TELE_KRATEI_CUBE -> {
					int i3 = getRandom(3) + 1;
					if (i3 == 1) {
						talker.teleToLocation(-70411, -70958, -1416);
					} else {
						if (i3 == 2) {
							talker.teleToLocation(-70522, -71026, -1416);
						} else {
							talker.teleToLocation(-70293, -71029, -1416);
						}
					}
				}
				case REPLY_ITEM_LIST -> {
					MultisellData.getInstance().separateAndSend(643, talker, (L2Npc) npc, false);
				}
				case REPLY_TELE_HANDYS_ARENA -> {
					talker.teleToLocation(-57328, -60566, -2320);
				}
			}
		}
	}
}