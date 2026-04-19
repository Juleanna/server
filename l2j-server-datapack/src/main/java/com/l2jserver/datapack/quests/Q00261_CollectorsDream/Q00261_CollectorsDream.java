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
package com.l2jserver.datapack.quests.Q00261_CollectorsDream;

import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.util.Util;

/**
 * Collector's Dream (261)
 * @author xban1x
 */
public final class Q00261_CollectorsDream extends Quest {
	// Npc
	private static final int ALSHUPES = 30222;
	// Monsters
	private static final int[] MONSTERS = new int[] {
		20308, // Hook Spider
		20460, // Crimson Spider
		20466, // Pincer Spider
	};
	// Item
	private static final QuestItemChanceHolder SPIDER_LEG = new QuestItemChanceHolder(1087, 8L);
	// Misc
	private static final int MIN_LVL = 15;
	
	private static final int GUIDE_MISSION = 41;
	
	public Q00261_CollectorsDream() {
		super(261);
		bindStartNpc(ALSHUPES);
		bindTalk(ALSHUPES);
		bindKill(MONSTERS);
		registerQuestItems(SPIDER_LEG.getId());
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if ((st != null) && event.equals("30222-03.htm")) {
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && st.isCond(1) && Util.checkIfInRange(1500, npc, killer, true)) {
			if (giveItemRandomly(st.getPlayer(), npc, SPIDER_LEG, true)) {
				st.setCond(2);
			}
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = (player.getLevel() >= MIN_LVL) ? "30222-02.htm" : "30222-01.htm";
				break;
			}
			case State.STARTED: {
				switch (st.getCond()) {
					case 1: {
						htmltext = "30222-04.html";
						break;
					}
					case 2: {
						if (hasItemsAtLimit(player, SPIDER_LEG)) {
							// Newbie Guide
							final var newbieGuide = QuestManager.getInstance().getQuest(NewbieGuide.class.getSimpleName());
							if (newbieGuide != null) {
								final var newbieGuideQs = newbieGuide.getQuestState(player, true);
								if (!newbieGuideQs.haveNRMemo(player, GUIDE_MISSION)) {
									newbieGuideQs.setNRMemo(player, GUIDE_MISSION);
									newbieGuideQs.setNRMemoState(player, GUIDE_MISSION, 100000);
									
									showOnScreenMsg(player, NpcStringId.LAST_DUTY_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
								} else {
									if (((newbieGuideQs.getNRMemoState(player, GUIDE_MISSION) % 100000000) / 10000000) != 1) {
										newbieGuideQs.setNRMemo(player, GUIDE_MISSION);
										newbieGuideQs.setNRMemoState(player, GUIDE_MISSION, newbieGuideQs.getNRMemoState(player, GUIDE_MISSION) + 10000000);
										
										showOnScreenMsg(player, NpcStringId.LAST_DUTY_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
									}
								}
							}
							
							st.giveAdena(1000, true);
							st.addExpAndSp(2000, 0);
							st.exitQuest(true, true);
							htmltext = "30222-05.html";
						}
						break;
					}
				}
				break;
			}
		}
		return htmltext;
	}
}
