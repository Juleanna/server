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
package com.l2jserver.datapack.quests.Q00276_TotemOfTheHestui;

import java.util.List;

import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.util.Util;

/**
 * Totem of the Hestui (276)
 * @author xban1x
 */
public final class Q00276_TotemOfTheHestui extends Quest {
	// Npc
	private static final int TANAPI = 30571;
	// Items
	private static final int KASHA_PARASITE = 1480;
	private static final QuestItemChanceHolder KASHA_CRYSTAL = new QuestItemChanceHolder(1481, 1L);
	// Monsters
	private static final int KASHA_BEAR = 20479;
	private static final int KASHA_BEAR_TOTEM = 27044;
	// Rewards
	private static final int[] REWARDS = new int[] {
		29,
		1500,
	};
	// Misc
	private static final List<ItemHolder> SPAWN_CHANCES = List.of(new ItemHolder(79, 100), new ItemHolder(69, 20), new ItemHolder(59, 15), new ItemHolder(49, 10), new ItemHolder(39, 2));
	private static final int MIN_LVL = 15;
	
	private static final int GUIDE_MISSION = 41;
	
	public Q00276_TotemOfTheHestui() {
		super(276);
		bindStartNpc(TANAPI);
		bindTalk(TANAPI);
		bindKill(KASHA_BEAR, KASHA_BEAR_TOTEM);
		registerQuestItems(KASHA_PARASITE, KASHA_CRYSTAL.getId());
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if ((st != null) && event.equals("30571-03.htm")) {
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && st.isCond(1) && Util.checkIfInRange(1500, killer, npc, true)) {
			switch (npc.getId()) {
				case KASHA_BEAR -> {
					final long chance1 = st.getQuestItemsCount(KASHA_PARASITE);
					final int chance2 = getRandom(100);
					boolean chance3 = true;
					for (ItemHolder spawnChance : SPAWN_CHANCES) {
						if ((chance1 >= spawnChance.getId()) && (chance2 <= spawnChance.getCount())) {
							st.addSpawn(KASHA_BEAR_TOTEM);
							st.takeItems(KASHA_PARASITE, -1);
							chance3 = false;
							break;
						}
					}
					if (chance3) {
						giveItemRandomly(st.getPlayer(), npc, KASHA_PARASITE, true);
					}
				}
				case KASHA_BEAR_TOTEM -> {
					if (giveItemRandomly(st.getPlayer(), npc, KASHA_CRYSTAL, true)) {
						st.setCond(2);
					}
				}
			}
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = (player.getRace() == Race.ORC) ? (player.getLevel() >= MIN_LVL) ? "30571-02.htm" : "30571-01.htm" : "30571-00.htm";
				break;
			}
			case State.STARTED: {
				switch (st.getCond()) {
					case 1: {
						htmltext = "30571-04.html";
						break;
					}
					case 2: {
						if (st.hasQuestItems(KASHA_CRYSTAL.getId())) {
							for (int reward : REWARDS) {
								st.rewardItems(reward, 1);
							}
							st.exitQuest(true, true);
							
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
							
							htmltext = "30571-05.html";
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
