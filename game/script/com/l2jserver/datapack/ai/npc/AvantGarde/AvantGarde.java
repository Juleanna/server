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
package com.l2jserver.datapack.ai.npc.AvantGarde;

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.datapack.custom.Validators.SubClassSkills;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.RequestAcquireSkill;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Avant-Garde AI.<br>
 * Sub-Class Certification system, skill learning and certification canceling.<br>
 * Transformation skill learning and transformation scroll sell.
 * @author Zoey76
 */
public class AvantGarde extends AbstractNpcAI {
	
	private static final Logger LOG = LoggerFactory.getLogger(AvantGarde.class);
	
	// NPC
	private static final int AVANT_GARDE = 32323;
	// Items
	// @formatter:off
	private static final int[] ITEMS =
	{
		10280, 10281, 10282, 10283, 10284, 10285, 10286, 10287, 10288, 10289, 10290, 10291, 10292, 10293, 10294, 10612
	};
	// @formatter:on
	// Misc
	private static final String[] QUEST_VAR_NAMES = {
		"EmergentAbility65-",
		"EmergentAbility70-",
		"ClassAbility75-",
		"ClassAbility80-"
	};
	
	public AvantGarde() {
		super(AvantGarde.class.getSimpleName(), "ai/npc");
		addStartNpc(AVANT_GARDE);
		addTalkId(AVANT_GARDE);
		addFirstTalkId(AVANT_GARDE);
		addAcquireSkillId(AVANT_GARDE);
	}
	
	@Override
	public String onAcquireSkill(L2Npc npc, L2PcInstance player, Skill skill, AcquireSkillType type) {
		switch (type) {
			case TRANSFORM: {
				showTransformSkillList(player);
				break;
			}
			case SUBCLASS: {
				showSubClassSkillList(player);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		String htmltext = null;
		switch (event) {
			case "32323-02.html":
			case "32323-02a.html":
			case "32323-02b.html":
			case "32323-02c.html":
			case "32323-05.html":
			case "32323-05a.html":
			case "32323-05no.html":
			case "32323-06.html":
			case "32323-06no.html": {
				htmltext = event;
				break;
			}
			case "LearnTransformationSkill": {
				if (RequestAcquireSkill.canTransform(player)) {
					showTransformSkillList(player);
				} else {
					htmltext = "32323-03.html";
				}
				break;
			}
			case "BuyTransformationItems": {
				if (RequestAcquireSkill.canTransform(player)) {
					MultisellData.getInstance().separateAndSend(32323001, player, npc, false);
				} else {
					htmltext = "32323-04.html";
				}
				break;
			}
			case "LearnSubClassSkill": {
				if (!RequestAcquireSkill.canTransform(player)) {
					htmltext = "32323-04.html";
				}
				if (player.isSubClassActive()) {
					htmltext = "32323-08.html";
				} else {
					boolean hasItems = false;
					for (int id : ITEMS) {
						if (player.getInventory().getItemByItemId(id) != null) {
							hasItems = true;
							break;
						}
					}
					if (hasItems) {
						showSubClassSkillList(player);
					} else {
						htmltext = "32323-08.html";
					}
				}
				break;
			}
			case "CancelCertification": {
				if (player.getSubClasses().size() == 0) {
					htmltext = "32323-07.html";
				} else if (player.isSubClassActive()) {
					htmltext = "32323-08.html";
				} else if (player.getAdena() < character().getFeeDeleteSubClassSkills()) {
					htmltext = "32323-08no.html";
				} else {
					QuestState st = player.getQuestState(SubClassSkills.class.getSimpleName());
					if (st == null) {
						st = QuestManager.getInstance().getQuest(SubClassSkills.class.getSimpleName()).newQuestState(player);
					}
					
					int activeCertifications = 0;
					for (String varName : QUEST_VAR_NAMES) {
						for (int i = 1; i <= character().getMaxSubclass(); i++) {
							String qvar = st.getGlobalQuestVar(varName + i);
							if (!qvar.isEmpty() && (qvar.endsWith(";") || !qvar.equals("0"))) {
								activeCertifications++;
							}
						}
					}
					if (activeCertifications == 0) {
						htmltext = "32323-10no.html";
					} else {
						for (String varName : QUEST_VAR_NAMES) {
							for (int i = 1; i <= character().getMaxSubclass(); i++) {
								final String qvarName = varName + i;
								final String qvar = st.getGlobalQuestVar(qvarName);
								if (qvar.endsWith(";")) {
									final String skillIdVar = qvar.replace(";", "");
									if (Util.isDigit(skillIdVar)) {
										int skillId = Integer.parseInt(skillIdVar);
										final Skill sk = SkillData.getInstance().getSkill(skillId, 1);
										if (sk != null) {
											player.removeSkill(sk);
											st.saveGlobalQuestVar(qvarName, "0");
										}
									} else {
										LOG.warn("Invalid Sub-Class Skill Id {} for player {}!", skillIdVar, player);
									}
								} else if (!qvar.isEmpty() && !qvar.equals("0")) {
									if (Util.isDigit(qvar)) {
										final int itemObjId = Integer.parseInt(qvar);
										var itemInstance = player.getInventory().getItemByObjectId(itemObjId);
										if (itemInstance != null) {
											player.destroyItem("CancelCertification", itemObjId, 1, player, false);
										} else {
											itemInstance = player.getWarehouse().getItemByObjectId(itemObjId);
											if (itemInstance != null) {
												LOG.warn("Somehow {} put a certification book into warehouse!", player);
												player.getWarehouse().destroyItem("CancelCertification", itemInstance, 1, player, false);
											} else {
												LOG.warn("Somehow {} deleted a certification book!", player);
											}
										}
										st.saveGlobalQuestVar(qvarName, "0");
									} else {
										LOG.warn("Invalid item object Id {} for player {}!", qvar, player);
									}
								}
							}
						}
						
						player.reduceAdena("Cleanse", character().getFeeDeleteSubClassSkills(), npc, true);
						htmltext = "32323-09no.html";
						player.sendSkillList();
					}
					
					// Let's consume all certification books, even those not present in database.
					for (int itemId : ITEMS) {
						final var item = player.getInventory().getItemByItemId(itemId);
						if (item != null) {
							LOG.warn("Player {} had extra certification skill books while cancelling sub-class certifications!", player);
							player.destroyItem("CancelCertificationExtraBooks", item, npc, false);
						}
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		return "32323-01.html";
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		return "32323-01.html";
	}
	
	/**
	 * Display the Sub-Class Skill list to the player.
	 * @param player the player
	 */
	public static void showSubClassSkillList(L2PcInstance player) {
		final List<L2SkillLearn> subClassSkills = SkillTreesData.getInstance().getAvailableSubClassSkills(player);
		final AcquireSkillList asl = new AcquireSkillList(AcquireSkillType.SUBCLASS);
		int count = 0;
		
		for (L2SkillLearn s : subClassSkills) {
			if (SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel()) != null) {
				count++;
				asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), 0, 0);
			}
		}
		if (count > 0) {
			player.sendPacket(asl);
		} else {
			player.sendPacket(SystemMessageId.NO_MORE_SKILLS_TO_LEARN);
		}
	}
	
	/**
	 * This displays Transformation Skill List to the player.
	 * @param player the active character.
	 */
	public static void showTransformSkillList(L2PcInstance player) {
		final List<L2SkillLearn> skills = SkillTreesData.getInstance().getAvailableTransformSkills(player);
		final AcquireSkillList asl = new AcquireSkillList(AcquireSkillType.TRANSFORM);
		int counts = 0;
		
		for (L2SkillLearn s : skills) {
			if (SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel()) != null) {
				counts++;
				asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), s.getLevelUpSp(), 0);
			}
		}
		
		if (counts == 0) {
			final int minlevel = SkillTreesData.getInstance().getMinLevelForNewSkill(player, SkillTreesData.getInstance().getTransformSkillTree());
			if (minlevel > 0) {
				// No more skills to learn, come back when you level.
				final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1);
				sm.addInt(minlevel);
				player.sendPacket(sm);
			} else {
				player.sendPacket(SystemMessageId.NO_MORE_SKILLS_TO_LEARN);
			}
		} else {
			player.sendPacket(asl);
		}
	}
}
