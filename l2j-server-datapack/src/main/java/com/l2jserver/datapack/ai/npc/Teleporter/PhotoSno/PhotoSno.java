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
package com.l2jserver.datapack.ai.npc.Teleporter.PhotoSno;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class PhotoSno extends Teleporter {
	
	private static final int[] NPCs = { 4316, 4317, 4318, 4319, 4320, 4321, 4322, 4323 };
	
	private static final int MS_ASK_TELE_CHAT = -2222;
	
	private static final int REPLY_TELE_WATER_FOUNTAIN = 1;
	private static final int REPLY_TELE_HOUSE = 2;
	private static final int REPLY_TELE_PARADE_TUNNEL = 3;
	private static final int REPLY_TELE_TUBA_THIRD_BUTTON = 4;
	private static final int REPLY_TELE_TUBA_SECOND_BUTTON = 5;
	private static final int REPLY_TELE_TUBA_FIRST_BUTTON = 6;
	private static final int REPLY_TELE_TUBA_MOUTH_PIECE = 7;
	private static final int REPLY_TELE_TOP_PARADE_TUNNEL = 8;
	private static final int REPLY_TELE_BOOK_HOUSE = 9;
	private static final int REPLY_TELE_BOOK_CASTLE = 10;
	private static final int REPLY_TELE_CLOCK_TOWER = 11;
	
	public PhotoSno() {
		super(NPCs);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, fnPath + getFnHi(npc.getId()));
		
		return null;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_TELE_CHAT -> {
				switch (reply) {
					case REPLY_TELE_WATER_FOUNTAIN -> {
						talker.teleToLocation(-59637, -56839, -1816);
					}
					case REPLY_TELE_HOUSE -> {
						talker.teleToLocation(-57525, -54523, -1576);
					}
					case REPLY_TELE_PARADE_TUNNEL -> {
						talker.teleToLocation(-58151, -53110, -1688);
					}
					case REPLY_TELE_TUBA_THIRD_BUTTON -> {
						talker.teleToLocation(-55748, -56327, -1336);
					}
					case REPLY_TELE_TUBA_SECOND_BUTTON -> {
						talker.teleToLocation(-55646, -56314, -1296);
					}
					case REPLY_TELE_TUBA_FIRST_BUTTON -> {
						talker.teleToLocation(-55545, -56310, -1256);
					}
					case REPLY_TELE_TUBA_MOUTH_PIECE -> {
						talker.teleToLocation(-55355, -56305, -1112);
					}
					case REPLY_TELE_TOP_PARADE_TUNNEL -> {
						talker.teleToLocation(-55223, -58832, -1680);
					}
					case REPLY_TELE_BOOK_HOUSE -> {
						talker.teleToLocation(-59075, -59464, -1464);
					}
					case REPLY_TELE_BOOK_CASTLE -> {
						talker.teleToLocation(-61926, -59504, -1728);
					}
					case REPLY_TELE_CLOCK_TOWER -> {
						talker.teleToLocation(-61288, -57736, -1600);
					}
				}
			}
		}
	}
	
	private static final String getFnHi(int npcId) {
		return switch (npcId) {
			case 4316 -> "g_photonpc001.htm";
			case 4317 -> "g_photonpc002.htm";
			case 4318 -> "g_photonpc003.htm";
			case 4319 -> "g_photonpc004.htm";
			case 4320 -> "g_photonpc005.htm";
			case 4321 -> "g_photonpc006.htm";
			case 4322 -> "g_photonpc007.htm";
			case 4323 -> "g_photonpc008.htm";
			default -> null;
		};
	}
}