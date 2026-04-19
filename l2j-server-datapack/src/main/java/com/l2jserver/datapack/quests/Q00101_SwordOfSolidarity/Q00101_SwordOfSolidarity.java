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
package com.l2jserver.datapack.quests.Q00101_SwordOfSolidarity;

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
import com.l2jserver.gameserver.network.serverpackets.SocialAction;

/**
 * Sword of Solidarity (101)
 * @author xban1x
 */
public class Q00101_SwordOfSolidarity extends Quest {
	// NPCs
	private static final int ROIEN = 30008;
	private static final int ALTRAN = 30283;
	// Items
	private static final int BROKEN_SWORD_HANDLE = 739;
	private static final int BROKEN_BLADE_BOTTOM = 740;
	private static final int BROKEN_BLADE_TOP = 741;
	private static final int ALTRANS_NOTE = 742;
	private static final int ROIENS_LETTER = 796;
	private static final int DIRECTIONS_TO_RUINS = 937;
	// Monsters
	private static final int[] MONSTERS = {
		20361, // Tunath Orc Marksman
		20362, // Tunath Orc Warrior
	};
	// Rewards
	private static final ItemHolder[] REWARDS = {
		new ItemHolder(738, 1), // Sword of Solidarity
		new ItemHolder(1060, 100), // Lesser Healing Potion
		new ItemHolder(4412, 10), // Echo Crystal - Theme of Battle
		new ItemHolder(4413, 10), // Echo Crystal - Theme of Love
		new ItemHolder(4414, 10), // Echo Crystal - Theme of Solitude
		new ItemHolder(4415, 10), // Echo Crystal - Theme of Feast
		new ItemHolder(4416, 10), // Echo Crystal - Theme of Celebration
	};
	// Misc
	private static final int MIN_LVL = 9;
	
	private static final int GUIDE_MISSION = 41;
	
	private static final ItemHolder SOULSHOTS_NO_GRADE_FOR_ROOKIES = new ItemHolder(5789, 7000);
	
	public Q00101_SwordOfSolidarity() {
		super(101);
		bindStartNpc(ROIEN);
		bindKill(MONSTERS);
		bindTalk(ROIEN, ALTRAN);
		registerQuestItems(BROKEN_SWORD_HANDLE, BROKEN_BLADE_BOTTOM, BROKEN_BLADE_TOP, ALTRANS_NOTE, ROIENS_LETTER, DIRECTIONS_TO_RUINS);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st != null) {
			switch (event) {
				case "30008-03.html":
				case "30008-09.html": {
					htmltext = event;
					break;
				}
				case "30008-04.htm": {
					st.startQuest();
					st.giveItems(ROIENS_LETTER, 1);
					htmltext = event;
					break;
				}
				case "30283-02.html": {
					if (st.isCond(1) && st.hasQuestItems(ROIENS_LETTER)) {
						st.takeItems(ROIENS_LETTER, -1);
						st.giveItems(DIRECTIONS_TO_RUINS, 1);
						st.setCond(2, true);
						htmltext = event;
					}
					break;
				}
				case "30283-07.html": {
					if (st.isCond(5) && st.hasQuestItems(BROKEN_SWORD_HANDLE)) {
						giveNewbieReward(player);
						for (ItemHolder reward : REWARDS) {
							st.giveItems(reward);
						}
						st.addExpAndSp(25747, 2171);
						st.giveAdena(10981, true);
						st.exitQuest(false, true);
						
						player.sendPacket(new SocialAction(player.getObjectId(), 3));
						
						// Newbie Guide
						final var newbieGuide = QuestManager.getInstance().getQuest(NewbieGuide.class.getSimpleName());
						if (newbieGuide != null) {
							final var newbieGuideQs = newbieGuide.getQuestState(player, true);
							if (!newbieGuideQs.haveNRMemo(player, GUIDE_MISSION)) {
								newbieGuideQs.setNRMemo(player, GUIDE_MISSION);
								newbieGuideQs.setNRMemoState(player, GUIDE_MISSION, 100000);
								
								showOnScreenMsg(player, NpcStringId.ACQUISITION_OF_RACE_SPECIFIC_WEAPON_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
							} else {
								if (((newbieGuideQs.getNRMemoState(player, GUIDE_MISSION) % 1000000) / 100000) != 1) {
									newbieGuideQs.setNRMemo(player, GUIDE_MISSION);
									newbieGuideQs.setNRMemoState(player, GUIDE_MISSION, newbieGuideQs.getNRMemoState(player, GUIDE_MISSION) + 100000);
									
									showOnScreenMsg(player, NpcStringId.ACQUISITION_OF_RACE_SPECIFIC_WEAPON_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000);
								}
							}
						}
						
						htmltext = event;
					}
					break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && st.isCond(2) && (getRandom(5) == 0)) {
			if (!st.hasQuestItems(BROKEN_BLADE_TOP)) {
				st.giveItems(BROKEN_BLADE_TOP, 1);
				if (st.hasQuestItems(BROKEN_BLADE_BOTTOM)) {
					st.setCond(3, true);
				} else {
					st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
				}
			} else if (!st.hasQuestItems(BROKEN_BLADE_BOTTOM)) {
				st.giveItems(BROKEN_BLADE_BOTTOM, 1);
				if (st.hasQuestItems(BROKEN_BLADE_TOP)) {
					st.setCond(3, true);
				} else {
					st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
				}
			}
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (npc.getId()) {
			case ROIEN: {
				switch (st.getState()) {
					case State.CREATED: {
						htmltext = (player.getRace() == Race.HUMAN) ? (player.getLevel() >= MIN_LVL) ? "30008-02.htm" : "30008-08.htm" : "30008-01.htm";
						break;
					}
					case State.STARTED: {
						switch (st.getCond()) {
							case 1: {
								if (st.hasQuestItems(ROIENS_LETTER)) {
									htmltext = "30008-05.html";
								}
								break;
							}
							case 2: {
								if (hasAtLeastOneQuestItem(player, BROKEN_BLADE_BOTTOM, BROKEN_BLADE_TOP)) {
									htmltext = "30008-11.html";
								} else if (st.hasQuestItems(DIRECTIONS_TO_RUINS)) {
									htmltext = "30008-10.html";
								}
								break;
							}
							case 3: {
								if (st.hasQuestItems(BROKEN_BLADE_BOTTOM, BROKEN_BLADE_TOP)) {
									htmltext = "30008-12.html";
								}
								break;
							}
							case 4: {
								if (st.hasQuestItems(ALTRANS_NOTE)) {
									st.takeItems(ALTRANS_NOTE, -1);
									st.giveItems(BROKEN_SWORD_HANDLE, 1);
									st.setCond(5, true);
									htmltext = "30008-06.html";
								}
								break;
							}
							case 5: {
								if (st.hasQuestItems(BROKEN_SWORD_HANDLE)) {
									htmltext = "30008-07.html";
								}
								break;
							}
						}
						break;
					}
					case State.COMPLETED: {
						htmltext = getAlreadyCompletedMsg(player);
						break;
					}
				}
				break;
			}
			case ALTRAN: {
				switch (st.getCond()) {
					case 1: {
						if (st.hasQuestItems(ROIENS_LETTER)) {
							htmltext = "30283-01.html";
						}
						break;
					}
					case 2: {
						if (hasAtLeastOneQuestItem(player, BROKEN_BLADE_BOTTOM, BROKEN_BLADE_TOP)) {
							htmltext = "30283-08.html";
						} else if (st.hasQuestItems(DIRECTIONS_TO_RUINS)) {
							htmltext = "30283-03.html";
						}
						break;
					}
					case 3: {
						if (st.hasQuestItems(BROKEN_BLADE_BOTTOM, BROKEN_BLADE_TOP)) {
							takeItems(player, -1, DIRECTIONS_TO_RUINS, BROKEN_BLADE_TOP, BROKEN_BLADE_BOTTOM);
							st.giveItems(ALTRANS_NOTE, 1);
							st.setCond(4, true);
							htmltext = "30283-04.html";
						}
						break;
					}
					case 4: {
						if (st.hasQuestItems(ALTRANS_NOTE)) {
							htmltext = "30283-05.html";
						}
						break;
					}
					case 5: {
						if (st.hasQuestItems(BROKEN_SWORD_HANDLE)) {
							htmltext = "30283-06.html";
						}
						break;
					}
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
	private static void giveNewbieReward(L2PcInstance player) {
		if (player.getLevel() < 25) {
			if (!player.isMageClass()) {
				giveItems(player, SOULSHOTS_NO_GRADE_FOR_ROOKIES);
				playSound(player, Voice.TUTORIAL_VOICE_026_1000);
			}
		}
	}
}