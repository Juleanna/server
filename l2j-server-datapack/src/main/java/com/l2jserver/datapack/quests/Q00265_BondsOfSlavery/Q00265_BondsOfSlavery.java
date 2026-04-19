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
package com.l2jserver.datapack.quests.Q00265_BondsOfSlavery;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.enums.audio.Voice;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;

/**
 * Bonds of Slavery (265)
 * @author xban1x
 */
public final class Q00265_BondsOfSlavery extends Quest {
	// Item
	private static final int IMP_SHACKLES = 1368;
	// NPC
	private static final int KRISTIN = 30357;
	// Misc
	private static final int MIN_LVL = 6;
	
	private static final int GUIDE_MISSION = 41;
	
	private static final ItemHolder SPIRITSHOTS_NO_GRADE_FOR_ROOKIES = new ItemHolder(5790, 3000);
	private static final ItemHolder SOULSHOTS_NO_GRADE_FOR_ROOKIES = new ItemHolder(5789, 6000);
	
	// Monsters
	private static final Map<Integer, Integer> MONSTERS = new HashMap<>();
	static {
		MONSTERS.put(20004, 5); // Imp
		MONSTERS.put(20005, 6); // Imp Elder
	}
	
	public Q00265_BondsOfSlavery() {
		super(265);
		bindStartNpc(KRISTIN);
		bindTalk(KRISTIN);
		bindKill(MONSTERS.keySet());
		registerQuestItems(IMP_SHACKLES);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st == null) {
			return htmltext;
		}
		
		switch (event) {
			case "30357-04.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30357-07.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "30357-08.html": {
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && (getRandom(10) < MONSTERS.get(npc.getId()))) {
			st.giveItems(IMP_SHACKLES, 1);
			st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = (player.getRace() == Race.DARK_ELF) ? (player.getLevel() >= MIN_LVL) ? "30357-03.htm" : "30357-02.html" : "30357-01.html";
				break;
			}
			case State.STARTED: {
				if (st.hasQuestItems(IMP_SHACKLES)) {
					final long shackles = st.getQuestItemsCount(IMP_SHACKLES);
					st.giveAdena((shackles * 12) + (shackles >= 10 ? 500 : 0), true);
					st.takeItems(IMP_SHACKLES, -1);
					
					giveNewbieReward(player);
					
					// Newbie Guide
					final var newbieGuide = QuestManager.getInstance().getQuest(NewbieGuide.class.getSimpleName());
					if (newbieGuide != null) {
						final var newbieGuideQs = newbieGuide.getQuestState(player, true);
						if (!newbieGuideQs.haveNRMemo(player, GUIDE_MISSION)) {
							newbieGuideQs.setNRMemo(player, GUIDE_MISSION);
							newbieGuideQs.setNRMemoState(player, GUIDE_MISSION, 1000);
							
							showOnScreenMsg(player, NpcStringId.ACQUISITION_OF_SOULSHOT_FOR_BEGINNERS_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
						} else {
							if (((newbieGuideQs.getNRMemoState(player, GUIDE_MISSION) % 10000) / 1000) != 1) {
								newbieGuideQs.setNRMemo(player, GUIDE_MISSION);
								newbieGuideQs.setNRMemoState(player, GUIDE_MISSION, newbieGuideQs.getNRMemoState(player, GUIDE_MISSION) + 1000);
								
								showOnScreenMsg(player, NpcStringId.ACQUISITION_OF_SOULSHOT_FOR_BEGINNERS_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
							}
						}
					}
					
					htmltext = "30357-06.html";
				} else {
					htmltext = "30357-05.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	/**
	 * Give basic newbie reward.
	 * @param player the player to reward
	 */
	private void giveNewbieReward(L2PcInstance player) {
		if ((player.getLevel() < 25) && (getOneTimeQuestFlag(player, 57) == 0)) {
			if (player.isMageClass()) {
				giveItems(player, SPIRITSHOTS_NO_GRADE_FOR_ROOKIES);
				playSound(player, Voice.TUTORIAL_VOICE_027_1000);
			} else {
				giveItems(player, SOULSHOTS_NO_GRADE_FOR_ROOKIES);
				playSound(player, Voice.TUTORIAL_VOICE_026_1000);
			}
			
			setOneTimeQuestFlag(player, 57, 1);
		}
	}
}
