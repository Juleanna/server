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
package com.l2jserver.datapack.quests.Q00293_TheHiddenVeins;

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
 * The Hidden Veins (293)
 * @author xban1x
 */
public final class Q00293_TheHiddenVeins extends Quest {
	// NPCs
	private static final int FILAUR = 30535;
	private static final int CHICHIRIN = 30539;
	// Items
	private static final int CHRYSOLITE_ORE = 1488;
	private static final int TORN_MAP_FRAGMENT = 1489;
	private static final int HIDDEN_ORE_MAP = 1490;
	// Monsters
	private static final int[] MONSTERS = new int[] {
		20446,
		20447,
		20448,
	};
	// Misc
	private static final int MIN_LVL = 6;
	private static final int REQUIRED_TORN_MAP_FRAGMENT = 4;
	
	private static final int GUIDE_MISSION = 41;
	
	private static final ItemHolder SOULSHOTS_NO_GRADE_FOR_ROOKIES = new ItemHolder(5789, 6000);
	
	public Q00293_TheHiddenVeins() {
		super(293);
		bindStartNpc(FILAUR);
		bindTalk(FILAUR, CHICHIRIN);
		bindKill(MONSTERS);
		registerQuestItems(CHRYSOLITE_ORE, TORN_MAP_FRAGMENT, HIDDEN_ORE_MAP);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st == null) {
			return htmltext;
		}
		
		switch (event) {
			case "30535-04.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30535-07.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "30535-08.html": {
				htmltext = event;
				break;
			}
			case "30539-03.html": {
				if (st.getQuestItemsCount(TORN_MAP_FRAGMENT) >= REQUIRED_TORN_MAP_FRAGMENT) {
					st.giveItems(HIDDEN_ORE_MAP, 1);
					st.takeItems(TORN_MAP_FRAGMENT, REQUIRED_TORN_MAP_FRAGMENT);
					htmltext = event;
				} else {
					htmltext = "30539-02.html";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if (st != null) {
			final int chance = getRandom(100);
			if (chance > 50) {
				st.giveItems(CHRYSOLITE_ORE, 1);
				st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
			} else if (chance < 5) {
				st.giveItems(TORN_MAP_FRAGMENT, 1);
				st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (npc.getId()) {
			case FILAUR: {
				switch (st.getState()) {
					case State.CREATED: {
						htmltext = (player.getRace() == Race.DWARF) ? (player.getLevel() >= MIN_LVL) ? "30535-03.htm" : "30535-02.htm" : "30535-01.htm";
						break;
					}
					case State.STARTED: {
						if (hasAtLeastOneQuestItem(player, CHRYSOLITE_ORE, HIDDEN_ORE_MAP)) {
							final long ores = st.getQuestItemsCount(CHRYSOLITE_ORE);
							final long maps = st.getQuestItemsCount(HIDDEN_ORE_MAP);
							st.giveAdena((ores * 5) + (maps * 500) + (((ores + maps) >= 10) ? 2000 : 0), true);
							takeItems(player, -1, CHRYSOLITE_ORE, HIDDEN_ORE_MAP);
							
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
							
							htmltext = (ores > 0) ? (maps > 0) ? "30535-10.html" : "30535-06.html" : "30535-09.html";
						} else {
							htmltext = "30535-05.html";
						}
						break;
					}
				}
				break;
			}
			case CHICHIRIN: {
				htmltext = "30539-01.html";
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
			if (!player.isMageClass()) {
				giveItems(player, SOULSHOTS_NO_GRADE_FOR_ROOKIES);
				playSound(player, Voice.TUTORIAL_VOICE_026_1000);
			}
			
			setOneTimeQuestFlag(player, 57, 1);
		}
	}
}
