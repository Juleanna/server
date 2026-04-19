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
package com.l2jserver.datapack.quests.Q00283_TheFewTheProudTheBrave;

import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;

/**
 * The Few, The Proud, The Brave (283)
 * @author xban1x
 */
public final class Q00283_TheFewTheProudTheBrave extends Quest {
	// NPC
	private static final int PERWAN = 32133;
	// Item
	private static final QuestItemChanceHolder CRIMSON_SPIDER_CLAW = new QuestItemChanceHolder(9747, 60.0);
	// Monster
	private static final int CRIMSON_SPIDER = 22244;
	// Misc
	private static final int CLAW_PRICE = 45;
	private static final int BONUS = 2187;
	private static final int MIN_LVL = 15;
	
	private static final int GUIDE_MISSION = 41;
	
	public Q00283_TheFewTheProudTheBrave() {
		super(283);
		bindKill(CRIMSON_SPIDER);
		bindStartNpc(PERWAN);
		bindTalk(PERWAN);
		registerQuestItems(CRIMSON_SPIDER_CLAW.getId());
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st == null) {
			return htmltext;
		}
		
		switch (event) {
			case "32133-03.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "32133-06.html": {
				htmltext = event;
				break;
			}
			case "32133-08.html": {
				if (st.hasQuestItems(CRIMSON_SPIDER_CLAW.getId())) {
					final long claws = st.getQuestItemsCount(CRIMSON_SPIDER_CLAW.getId());
					st.giveAdena((claws * CLAW_PRICE) + ((claws >= 10) ? BONUS : 0), true);
					st.takeItems(CRIMSON_SPIDER_CLAW.getId(), -1);
					
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
					
					htmltext = event;
				} else {
					htmltext = "32133-07.html";
				}
				break;
			}
			case "32133-09.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(killer, -1, 3, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, CRIMSON_SPIDER_CLAW, true);
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState st = getQuestState(talker, true);
		String htmltext = getNoQuestMsg(talker);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = (talker.getLevel() >= MIN_LVL) ? "32133-01.htm" : "32133-02.htm";
				break;
			}
			case State.STARTED: {
				htmltext = st.hasQuestItems(CRIMSON_SPIDER_CLAW.getId()) ? "32133-04.html" : "32133-05.html";
				break;
			}
		}
		return htmltext;
	}
}
