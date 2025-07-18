/*
 * Copyright © 2004-2023 L2J DataPack
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
package com.l2jserver.datapack.handlers.bypasshandlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.listeners.AbstractEventListener;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.StringUtil;

public class QuestLink implements IBypassHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(QuestLink.class);
	
	private static final int MAX_QUEST_COUNT = 40;
	private static final int TO_LEAD_AND_BE_LED = 118;
	private static final int THE_LEADER_AND_THE_FOLLOWER = 123;
	private static final String[] COMMANDS = {
		"Quest"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target) {
		String quest = "";
		try {
			quest = command.substring(5).trim();
		} catch (IndexOutOfBoundsException ioobe) {
		}
		if (quest.length() == 0) {
			showQuestWindow(activeChar, (L2Npc) target);
		} else {
			int questNameEnd = quest.indexOf(" ");
			if (questNameEnd == -1) {
				showQuestWindow(activeChar, (L2Npc) target, quest);
			} else {
				activeChar.processQuestEvent(quest.substring(0, questNameEnd), quest.substring(questNameEnd).trim());
			}
		}
		return true;
	}
	
	/**
	 * Open a choose quest window on client with all quests available of the L2NpcInstance.<br>
	 * <b><u>Actions</u>:</b><br>
	 * <li>Send a Server->Client NpcHtmlMessage containing the text of the L2NpcInstance to the L2PcInstance</li>
	 * @param player The L2PcInstance that talk with the L2NpcInstance
	 * @param npc The table containing quests of the L2NpcInstance
	 * @param quests the quest available
	 */
	private static void showQuestChooseWindow(L2PcInstance player, L2Npc npc, Quest[] quests) {
		final StringBuilder sb = StringUtil.startAppend(150, "<html><body>");
		String state = "";
		String color = "";
		int questId = -1;
		for (Quest quest : quests) {
			if (quest == null) {
				continue;
			}
			
			final QuestState qs = player.getQuestState(quest.getName());
			if ((qs == null) || qs.isCreated()) {
				state = quest.isCustomQuest() ? "" : "01";
				if (quest.canStartQuest(player)) {
					color = "bbaa88";
				} else {
					color = "a62f31";
				}
			} else if (qs.isStarted()) {
				state = quest.isCustomQuest() ? " (In Progress)" : "02";
				color = "ffdd66";
			} else if (qs.isCompleted()) {
				state = quest.isCustomQuest() ? " (Done)" : "03";
				color = "787878";
			}
			StringUtil.append(sb, "<a action=\"bypass -h npc_", String.valueOf(npc.getObjectId()), "_Quest ", quest.getName(), "\">");
			StringUtil.append(sb, "<font color=\"" + color + "\">[");
			
			if (quest.isCustomQuest()) {
				StringUtil.append(sb, quest.getDescr(), state);
			} else {
				questId = quest.getId();
				if (questId > 10000) {
					questId -= 5000;
				} else if (questId == 146) {
					questId = 640;
				}
				StringUtil.append(sb, "<fstring>", String.valueOf(questId), state, "</fstring>");
			}
			sb.append("]</font></a><br>");
			
			if ((player.getApprentice() > 0) && (L2World.getInstance().getPlayer(player.getApprentice()) != null)) {
				if (questId == TO_LEAD_AND_BE_LED) {
					sb.append("<a action=\"bypass -h Quest Q00118_ToLeadAndBeLed sponsor\"><font color=\"")
						.append(color) //
						.append("\">[<fstring>")
						.append(questId)
						.append(state)
						.append("</fstring> (Sponsor)]</font></a><br>");
				}
				
				if (questId == THE_LEADER_AND_THE_FOLLOWER) {
					sb.append("<a action=\"bypass -h Quest Q00123_TheLeaderAndTheFollower sponsor\"><font color=\"")
						.append(color) //
						.append("\">[<fstring>")
						.append(questId)
						.append(state)
						.append("</fstring> (Sponsor)]</font></a><br>");
				}
			}
		}
		sb.append("</body></html>");
		
		// Send a Server->Client packet NpcHtmlMessage to the L2PcInstance in order to display the message of the L2NpcInstance
		npc.insertObjectIdAndShowChatWindow(player, sb.toString());
	}
	
	/**
	 * Open a quest window on client with the text of the L2NpcInstance.<br>
	 * <b><u>Actions</u>:</b><br>
	 * <ul>
	 * <li>Get the text of the quest state in the folder com/l2jserver/datapack/quests/questId/stateId.htm</li>
	 * <li>Send a Server->Client NpcHtmlMessage containing the text of the L2NpcInstance to the L2PcInstance</li>
	 * <li>Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet</li>
	 * </ul>
	 * @param player the L2PcInstance that talk with the {@code npc}
	 * @param npc the L2NpcInstance that chats with the {@code player}
	 * @param questId the Id of the quest to display the message
	 */
	private static void showQuestWindow(L2PcInstance player, L2Npc npc, String questId) {
		String content = null;
		
		final Quest q = QuestManager.getInstance().getQuest(questId);
		
		// Get the state of the selected quest
		final QuestState qs = player.getQuestState(questId);
		
		if (q != null) {
			if (((q.getId() >= 1) && (q.getId() < 20000)) && ((player.getWeightPenalty() >= 3) || !player.isInventoryUnder90(true))) {
				player.sendPacket(SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT);
				return;
			}
			
			if (qs == null) {
				if ((q.getId() >= 1) && (q.getId() < 20000)) {
					// Too many ongoing quests.
					if (player.getAllActiveQuests().size() >= MAX_QUEST_COUNT) {
						final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
						html.setFile(player.getHtmlPrefix(), "data/html/fullquest.html");
						player.sendPacket(html);
						return;
					}
				}
			}
			
			q.notifyTalk(npc, player);
		} else {
			content = Quest.getNoQuestMsg(player); // no quests found
		}
		
		// Send a Server->Client packet NpcHtmlMessage to the L2PcInstance in order to display the message of the L2NpcInstance
		if (content != null) {
			npc.insertObjectIdAndShowChatWindow(player, content);
		}
		
		// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	/**
	 * @param player the player talking to the NPC
	 * @param npcId The Identifier of the NPC
	 * @return a table containing all QuestState from the table _quests in which the L2PcInstance must talk to the NPC.
	 */
	private static List<QuestState> getQuestsForTalk(L2PcInstance player, int npcId) {
		// Create a QuestState table that will contain all QuestState to modify
		final List<QuestState> states = new ArrayList<>();
		
		final L2NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
		if (template == null) {
			LOG.warn("{} requested quests for talk on non existing npc {}!", player, npcId);
			return states;
		}
		
		// Go through the QuestState of the L2PcInstance quests
		for (AbstractEventListener listener : template.getListeners(EventType.ON_NPC_TALK)) {
			if (listener.getOwner() instanceof Quest) {
				final Quest quest = (Quest) listener.getOwner();
				if (quest.isVisibleInQuestWindow()) {
					// Copy the current L2PcInstance QuestState in the QuestState table
					final QuestState st = player.getQuestState(quest.getName());
					if (st != null) {
						states.add(st);
					}
				}
			}
		}
		
		// Return a table containing all QuestState to modify
		return states;
	}
	
	/**
	 * Collect awaiting quests/start points and display a QuestChooseWindow (if several available) or QuestWindow.
	 * @param player the L2PcInstance that talk with the {@code npc}.
	 * @param npc the L2NpcInstance that chats with the {@code player}.
	 */
	public static void showQuestWindow(L2PcInstance player, L2Npc npc) {
		boolean conditionMeet = false;
		final Set<Quest> options = new HashSet<>();
		for (QuestState state : getQuestsForTalk(player, npc.getId())) {
			final Quest quest = state.getQuest();
			if (quest == null) {
				LOG.warn("{} requested incorrect quest state for non existing quest: {}", player, state.getQuestName());
				continue;
			}
			if ((quest.getId() > 0) && (quest.getId() < 20000)) {
				options.add(quest);
				if (quest.canStartQuest(player)) {
					conditionMeet = true;
				}
			}
		}
		
		for (AbstractEventListener listener : npc.getListeners(EventType.ON_NPC_QUEST_START)) {
			if (listener.getOwner() instanceof Quest) {
				final Quest quest = (Quest) listener.getOwner();
				if (quest.isVisibleInQuestWindow()) {
					if ((quest.getId() > 0) && (quest.getId() < 20000)) {
						options.add(quest);
						if (quest.canStartQuest(player)) {
							conditionMeet = true;
						}
					}
				}
			}
		}
		
		if (!conditionMeet) {
			showQuestWindow(player, npc, "");
		} else if ((options.size() > 1) || ((player.getApprentice() > 0) && (L2World.getInstance().getPlayer(player.getApprentice()) != null) && options.stream().anyMatch(q -> q.getId() == TO_LEAD_AND_BE_LED))) {
			showQuestChooseWindow(player, npc, options.toArray(new Quest[options.size()]));
		} else if (options.size() == 1) {
			showQuestWindow(player, npc, options.stream().findFirst().get().getName());
		} else {
			showQuestWindow(player, npc, "");
		}
	}
	
	@Override
	public String[] getBypassList() {
		return COMMANDS;
	}
}
