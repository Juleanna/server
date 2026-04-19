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
package com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestRune;

import static com.l2jserver.gameserver.network.SystemMessageId.YOU_CAN_PROCEED_ONLY_WHEN_THE_INVENTORY_WEIGHT_IS_BELOW_80_PERCENT_AND_THE_QUANTITY_IS_BELOW_90_PERCENT;

import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.SSQNpcPriest;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class DawnPriestRune extends SSQNpcPriest {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010075, new Location(114496, 132416, -3101), 28000),
		new TelPosList(1010081, new Location(136672, 79328, -3702), 22000),
		new TelPosList(1010073, new Location(-25472, 77728, -3446), 17000),
		new TelPosList(1010074, new Location(-56064, 78720, -3011), 23000),
		new TelPosList(1010076, new Location(168560, -17968, -3174), 21000),
		new TelPosList(1010083, new Location(110912, 84912, -4816), 25000),
		new TelPosList(1010082, new Location(-22480, 13872, -3174), 15000),
		new TelPosList(1010077, new Location(79296, 209584, -3709), 43000)
	};
	
	private static final TelPosList[] _positionCompetition = new TelPosList[] {
		new TelPosList(1010075, new Location(114496, 132416, -3101), 46000),
		new TelPosList(1010081, new Location(136672, 79328, -3702), 37000),
		new TelPosList(1010073, new Location(-25472, 77728, -3446), 29000),
		new TelPosList(1010074, new Location(-56064, 78720, -3011), 39000),
		new TelPosList(1010076, new Location(168560, -17968, -3174), 35000),
		new TelPosList(1010083, new Location(110912, 84912, -4816), 41000),
		new TelPosList(1010082, new Location(-22480, 13872, -3174), 25000),
		new TelPosList(1010077, new Location(79296, 209584, -3709), 73000)
	};
	
	private static final int MS_ASK_INTO_THE_CHAOS_Q = 507;
	
	private static final int MS_REPLY_INTO_THE_CHAOS_INFO = 1;
	private static final int MS_REPLY_INTO_THE_CHAOS_TELE = 2;
	
	private static final int npcId = 31694;
	
	public DawnPriestRune() {
		super(npcId);
		
		super.position = _position;
		super.positionCompetition = _positionCompetition;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_INTO_THE_CHAOS_Q -> {
				if (talker.getInventory().getSize(true) >= (talker.getQuestInventoryLimit() * QUEST_LIMIT) || talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT) || talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT)) {
					talker.sendPacket(YOU_CAN_PROCEED_ONLY_WHEN_THE_INVENTORY_WEIGHT_IS_BELOW_80_PERCENT_AND_THE_QUANTITY_IS_BELOW_90_PERCENT);
					return;
				}
				
				switch (reply) {
					case MS_REPLY_INTO_THE_CHAOS_INFO -> {
						showPage(talker, fnPath + "ssq_npc_priest_q507_01.htm");
					}
					case MS_REPLY_INTO_THE_CHAOS_TELE -> {
						final var q255 = QuestManager.getInstance().getQuest(Q00255_TUTORIAL);
						final var qs255 = q255.getQuestState(talker, true);
						
						var i0 = qs255.getMemoStateEx(1);
						i0 = i0 % 100;
						if (i0 >= 95 || i0 < 0) {
							i0 = 0;
						}
						
						qs255.setMemoStateEx(1, i0 + 1100);
						
						if (haveMemo(talker, Q00505_BLOODOFFERING)) {
							final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
							final var qs505 = q505.getQuestState(talker, true);
							qs505.exitQuest(true, false);
						}
						
						takeItems(talker, BLOOD_OF_OFFERING, getQuestItemsCount(talker, BLOOD_OF_OFFERING));
						
						switch (getRandom(2)) {
							case 0 -> {
								talker.teleToLocation(-80316, 111356, -4896);
							}
							case 1 -> {
								talker.teleToLocation(-80226, 111290, -4896);
							}
							case 2 -> {
								talker.teleToLocation(-80217, 111435, -4896);
							}
						}
						
						playSound(talker, Sound.ITEMSOUND_QUEST_ACCEPT);
					}
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}