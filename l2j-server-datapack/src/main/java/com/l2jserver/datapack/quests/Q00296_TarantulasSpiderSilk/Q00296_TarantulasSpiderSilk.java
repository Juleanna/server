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
package com.l2jserver.datapack.quests.Q00296_TarantulasSpiderSilk;

import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.drops.IDropItem;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.util.Util;

/**
 * Tarantula's Spider Silk (296)
 * @author xban1x
 */
public final class Q00296_TarantulasSpiderSilk extends Quest {
	// NPCs
	private static final int TRADER_MION = 30519;
	private static final int DEFENDER_NATHAN = 30548;
	// Items
	private static final QuestItemChanceHolder TARANTULA_SPIDER_SILK = new QuestItemChanceHolder(1493, 90.909);
	private static final QuestItemChanceHolder TARANTULA_SPINNERETTE = new QuestItemChanceHolder(1494, 9.091);
	private static final IDropItem DROP_ITEM = QuestDroplist.groupedDropItem(55.0, TARANTULA_SPIDER_SILK, TARANTULA_SPINNERETTE);
	// Monsters
	private static final int[] MONSTERS = new int[] {
		20394,
		20403,
		20508,
	};
	// Misc
	private static final int MIN_LVL = 15;
	
	private static final int GUIDE_MISSION = 41;
	
	public Q00296_TarantulasSpiderSilk() {
		super(296);
		bindStartNpc(TRADER_MION);
		bindTalk(TRADER_MION, DEFENDER_NATHAN);
		bindKill(MONSTERS);
		registerQuestItems(TARANTULA_SPIDER_SILK.getId(), TARANTULA_SPINNERETTE.getId());
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String html = null;
		if (qs == null) {
			return null;
		}
		
		switch (event) {
			case "30519-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					html = event;
				}
				break;
			}
			case "30519-06.html": {
				if (qs.isStarted()) {
					qs.exitQuest(true, true);
					html = event;
				}
				break;
			}
			case "30519-07.html": {
				if (qs.isStarted()) {
					html = event;
				}
				break;
			}
			case "30548-03.html": {
				if (qs.isStarted()) {
					if (hasQuestItems(player, TARANTULA_SPINNERETTE.getId())) {
						giveItems(player, TARANTULA_SPIDER_SILK, (15 + getRandom(9)) * getQuestItemsCount(player, TARANTULA_SPINNERETTE.getId()));
						takeItems(player, TARANTULA_SPINNERETTE.getId(), -1);
						html = event;
					} else {
						html = "30548-02.html";
					}
				}
				break;
			}
		}
		return html;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && Util.checkIfInRange(1500, npc, qs.getPlayer(), true)) {
			giveItemRandomly(qs.getPlayer(), npc, DROP_ITEM, 0, true);
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState qs = getQuestState(talker, true);
		String html = getNoQuestMsg(talker);
		if (qs.isCreated() && (npc.getId() == TRADER_MION)) {
			html = (talker.getLevel() >= MIN_LVL ? "30519-02.htm" : "30519-01.htm");
		} else if (qs.isStarted()) {
			if (npc.getId() == TRADER_MION) {
				final long silk = getQuestItemsCount(talker, TARANTULA_SPIDER_SILK.getId());
				if (silk >= 1) {
					giveAdena(talker, (silk * 30) + (silk >= 10 ? 2000 : 0), true);
					takeItems(talker, TARANTULA_SPIDER_SILK.getId(), -1);
					
					// Newbie Guide
					final var newbieGuide = QuestManager.getInstance().getQuest(NewbieGuide.class.getSimpleName());
					if (newbieGuide != null) {
						final var newbieGuideQs = newbieGuide.getQuestState(talker, true);
						if (!newbieGuideQs.haveNRMemo(talker, GUIDE_MISSION)) {
							newbieGuideQs.setNRMemo(talker, GUIDE_MISSION);
							newbieGuideQs.setNRMemoState(talker, GUIDE_MISSION, 100000);
							
							showOnScreenMsg(talker, NpcStringId.LAST_DUTY_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
						} else {
							if (((newbieGuideQs.getNRMemoState(talker, GUIDE_MISSION) % 100000000) / 10000000) != 1) {
								newbieGuideQs.setNRMemo(talker, GUIDE_MISSION);
								newbieGuideQs.setNRMemoState(talker, GUIDE_MISSION, newbieGuideQs.getNRMemoState(talker, GUIDE_MISSION) + 10000000);
								
								showOnScreenMsg(talker, NpcStringId.LAST_DUTY_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
							}
						}
					}
					
					html = "30519-05.html";
				} else {
					html = "30519-04.html";
				}
			} else if (npc.getId() == DEFENDER_NATHAN) {
				html = "30548-01.html";
			}
		}
		return html;
	}
}
