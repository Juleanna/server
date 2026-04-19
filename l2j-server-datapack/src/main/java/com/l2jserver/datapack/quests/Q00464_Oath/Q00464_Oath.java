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
package com.l2jserver.datapack.quests.Q00464_Oath;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.enums.QuestType;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.item.ItemTalk;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Oath (464)
 * @author malyelfik
 */
public class Q00464_Oath extends Quest {
	// NPCs @formatter:off
	private static final int[][] NPC =
	{
		// NPC id, EXP, SP, Adena
		{32596,	0, 0, 0},
		{30657,	15449, 17696, 42910},
		{30839, 189377, 21692, 52599},
		{30899,	249180, 28542, 69210},
		{31350, 249180, 28542, 69210},
		{30539,	19408, 47062, 169442},
		{30297,	24146, 58551, 210806},
		{31960,	15449, 17696, 42910},
		{31588,	15449, 17696, 42910}
	};
	// @formatter:on
	
	// Items
	private static final int STRONGBOX = 15537;
	private static final int BOOK = 15538;
	private static final int BOOK2 = 15539;
	// Misc
	private static final int MIN_LEVEL = 82;
	
	// Monsters
	private static final Map<Integer, Integer> MOBS = new HashMap<>();
	
	static {
		MOBS.put(22799, 9);
		MOBS.put(22794, 6);
		MOBS.put(22800, 10);
		MOBS.put(22796, 9);
		MOBS.put(22798, 9);
		MOBS.put(22795, 8);
		MOBS.put(22797, 7);
		MOBS.put(22789, 5);
		MOBS.put(22791, 4);
		MOBS.put(22790, 5);
		MOBS.put(22792, 4);
		MOBS.put(22793, 5);
	}
	
	public Q00464_Oath() {
		super(464);
		for (int[] npc : NPC) {
			bindTalk(npc[0]);
		}
		bindKill(MOBS.keySet());
		bindItemTalk(STRONGBOX);
		registerQuestItems(BOOK, BOOK2);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = event;
		switch (event) {
			case "32596-04.html":
				if (!st.hasQuestItems(BOOK)) {
					return getNoQuestMsg(player);
				}
				
				int cond = getRandom(2, 9);
				st.set("npc", String.valueOf(NPC[cond - 1][0]));
				st.setCond(cond, true);
				st.takeItems(BOOK, 1);
				st.giveItems(BOOK2, 1);
				switch (cond) {
					case 2:
						htmltext = "32596-04.html";
						break;
					case 3:
						htmltext = "32596-04a.html";
						break;
					case 4:
						htmltext = "32596-04b.html";
						break;
					case 5:
						htmltext = "32596-04c.html";
						break;
					case 6:
						htmltext = "32596-04d.html";
						break;
					case 7:
						htmltext = "32596-04e.html";
						break;
					case 8:
						htmltext = "32596-04f.html";
						break;
					case 9:
						htmltext = "32596-04g.html";
						break;
				}
				break;
			case "end_quest":
				if (!st.hasQuestItems(BOOK2)) {
					return getNoQuestMsg(player);
				}
				
				int i = st.getCond() - 1;
				st.addExpAndSp(NPC[i][1], NPC[i][2]);
				st.giveAdena(NPC[i][3], true);
				st.exitQuest(QuestType.DAILY, true);
				htmltext = npc.getId() + "-02.html";
				break;
			case "32596-02.html":
			case "32596-03.html":
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public void onItemTalk(ItemTalk event) {
		final var player = event.player();
		final var qs = getQuestState(player, true);
		boolean startQuest = false;
		switch (qs.getState()) {
			case State.CREATED -> startQuest = true;
			case State.STARTED -> showQuestPage(player, "strongbox-02.html", getId());
			case State.COMPLETED -> {
				if (qs.isNowAvailable()) {
					qs.setState(State.CREATED);
					startQuest = true;
				} else {
					showQuestPage(player, "strongbox-03.html", getId());
				}
			}
		}
		
		if (startQuest) {
			if (player.getLevel() >= MIN_LEVEL) {
				qs.startQuest();
				qs.takeItems(STRONGBOX, 1);
				qs.giveItems(BOOK, 1);
				showPage(player, "strongbox-01.htm");
			} else {
				showPage(player, "strongbox-00.htm");
			}
		}
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		if (getRandom(1000) < MOBS.get(npc.getId())) {
			npc.dropItem(killer, STRONGBOX, 1);
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		if (st.isStarted()) {
			int npcId = npc.getId();
			if (npcId == NPC[0][0]) {
				switch (st.getCond()) {
					case 1:
						htmltext = "32596-01.html";
						break;
					case 2:
						htmltext = "32596-05.html";
						break;
					case 3:
						htmltext = "32596-05a.html";
						break;
					case 4:
						htmltext = "32596-05b.html";
						break;
					case 5:
						htmltext = "32596-05c.html";
						break;
					case 6:
						htmltext = "32596-05d.html";
						break;
					case 7:
						htmltext = "32596-05e.html";
						break;
					case 8:
						htmltext = "32596-05f.html";
						break;
					case 9:
						htmltext = "32596-05g.html";
						break;
				}
			} else if ((st.getCond() > 1) && (st.getInt("npc") == npcId)) {
				htmltext = npcId + "-01.html";
			}
		}
		return htmltext;
	}
}