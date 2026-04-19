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
package com.l2jserver.datapack.quests.Q00508_AClansReputation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * A Clan's Reputation (508)<br>
 * Original Jython script by chris_00, @katmai and DrLecter.
 * @author Adry_85
 */
public class Q00508_AClansReputation extends Quest {
	// NPC
	private static final int SIR_ERIC_RODEMAI = 30868;
	
	private static final Map<Integer, List<Integer>> REWARD_POINTS = new HashMap<>();
	
	static {
		REWARD_POINTS.put(1, Arrays.asList(25252, 8277, 560)); // Palibati Queen Themis
		REWARD_POINTS.put(2, Arrays.asList(25478, 14883, 584)); // Shilen's Priest Hisilrome
		REWARD_POINTS.put(3, Arrays.asList(25255, 8280, 602)); // Gargoyle Lord Tiphon
		REWARD_POINTS.put(4, Arrays.asList(25245, 8281, 784)); // Last Lesser Giant Glaki
		REWARD_POINTS.put(5, Arrays.asList(25051, 8282, 558)); // Rahha
		REWARD_POINTS.put(6, Arrays.asList(25524, 8494, 768)); // Flamestone Giant
	}
	
	private static final int[] RAID_BOSS = {
		25252,
		25478,
		25255,
		25245,
		25051,
		25524
	};
	
	public Q00508_AClansReputation() {
		super(508);
		bindStartNpc(SIR_ERIC_RODEMAI);
		bindTalk(SIR_ERIC_RODEMAI);
		bindKill(RAID_BOSS);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		switch (event) {
			case "30868-0.html":
				st.startQuest();
				break;
			case "30868-1.html":
				st.set("raid", "1");
				showRadar(player, 192376, 22087, -3608, 2);
				break;
			case "30868-2.html":
				st.set("raid", "2");
				showRadar(player, 168288, 28368, -3632, 2);
				break;
			case "30868-3.html":
				st.set("raid", "3");
				showRadar(player, 170048, -24896, -3440, 2);
				break;
			case "30868-4.html":
				st.set("raid", "4");
				showRadar(player, 188809, 47780, -5968, 2);
				break;
			case "30868-5.html":
				st.set("raid", "5");
				showRadar(player, 117760, -9072, -3264, 2);
				break;
			case "30868-6.html":
				st.set("raid", "6");
				showRadar(player, 144600, -5500, -4100, 2);
				break;
			case "30868-7.html":
				st.exitQuest(true, true);
				break;
		}
		return event;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		if (player.getClan() == null) {
			return;
		}
		
		QuestState st = null;
		if (player.isClanLeader()) {
			st = player.getQuestState(getName());
		} else {
			L2PcInstance pleader = player.getClan().getLeader().getPlayerInstance();
			if ((pleader != null) && player.isInsideRadius(pleader, 1500, true, false)) {
				st = pleader.getQuestState(getName());
			}
		}
		
		if ((st != null) && st.isStarted()) {
			int raid = st.getInt("raid");
			if (REWARD_POINTS.containsKey(raid)) {
				if ((npc.getId() == REWARD_POINTS.get(raid).get(0)) && !st.hasQuestItems(REWARD_POINTS.get(raid).get(1))) {
					st.rewardItems(REWARD_POINTS.get(raid).get(1), 1);
					st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
				}
			}
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		L2Clan clan = player.getClan();
		switch (st.getState()) {
			case State.CREATED:
				htmltext = ((clan == null) || !player.isClanLeader() || (clan.getLevel() < 5)) ? "30868-0a.htm" : "30868-0b.htm";
				break;
			case State.STARTED:
				if ((clan == null) || !player.isClanLeader()) {
					st.exitQuest(true);
					return "30868-8.html";
				}
				
				int raid = st.getInt("raid");
				
				if (REWARD_POINTS.containsKey(raid)) {
					if (st.hasQuestItems(REWARD_POINTS.get(raid).get(1))) {
						htmltext = "30868-" + raid + "b.html";
						st.playSound(Sound.ITEMSOUND_QUEST_FANFARE_1);
						st.takeItems(REWARD_POINTS.get(raid).get(1), -1);
						final int rep = REWARD_POINTS.get(raid).get(2);
						clan.addReputationScore(rep, true);
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CLAN_QUEST_COMPLETED_AND_S1_POINTS_GAINED).addInt(rep));
						clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
					} else {
						htmltext = "30868-" + raid + "a.html";
					}
				} else {
					htmltext = "30868-0.html";
				}
				break;
			default:
				break;
		}
		return htmltext;
	}
}
