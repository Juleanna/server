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
package com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.datapack.quests.Q00255_Tutorial.Q00255_Tutorial;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.enums.audio.Voice;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.NpcStringId;

/**
 * Class handle all newbie guide tasks
 * @author Zealar
 * @since 2.6.0.0
 */
public final class NewbieGuide extends AbstractNpcAI {
	// Suffix
	private static final String SUFFIX_FIGHTER_5_LEVEL = "-f05.htm";
	private static final String SUFFIX_FIGHTER_10_LEVEL = "-f10.htm";
	private static final String SUFFIX_FIGHTER_15_LEVEL = "-f15.htm";
	private static final String SUFFIX_FIGHTER_20_LEVEL = "-f20.htm";
	private static final String SUFFIX_MAGE_7_LEVEL = "-m07.htm";
	private static final String SUFFIX_MAGE_14_LEVEL = "-m14.htm";
	private static final String SUFFIX_MAGE_20_LEVEL = "-m20.htm";
	
	// Vars
	private static final int FIRST_COUPON_SIZE = 5;
	private static final int SECOND_COUPON_SIZE = 1;
	
	// Newbie helpers
	private static final int NEWBIE_GUIDE_HUMAN = 30598;
	private static final int NEWBIE_GUIDE_ELF = 30599;
	private static final int NEWBIE_GUIDE_DARK_ELF = 30600;
	private static final int NEWBIE_GUIDE_DWARF = 30601;
	private static final int NEWBIE_GUIDE_ORC = 30602;
	private static final int NEWBIE_GUIDE_KAMAEL = 32135;
	private static final int NEWBIE_GUIDE_GLUDIN = 31076;
	private static final int NEWBIE_GUIDE_GLUDIO = 31077;
	private static final int ADVENTURERS_GUIDE = 32327;
	
	private static final int GUIDE_MISSION = 41;
	
	// Item
	private static final int SOULSHOT_NO_GRADE_FOR_BEGINNERS = 5789;
	private static final int SPIRITSHOT_NO_GRADE_FOR_BEGINNERS = 5790;
	private static final int SCROLL_RECOVERY_NO_GRADE = 8594;
	
	private static final int APPRENTICE_ADVENTURERS_WEAPON_EXCHANGE_COUPON = 7832;
	private static final int ADVENTURERS_MAGIC_ACCESSORY_EXCHANGE_COUPON = 7833;
	
	// Buffs
	private static final SkillHolder WIND_WALK_FOR_BEGINNERS = new SkillHolder(4322);
	private static final SkillHolder SHIELD_FOR_BEGINNERS = new SkillHolder(4323);
	private static final SkillHolder BLESS_THE_BODY_FOR_BEGINNERS = new SkillHolder(4324);
	private static final SkillHolder VAMPIRIC_RAGE_FOR_BEGINNERS = new SkillHolder(4325);
	private static final SkillHolder REGENERATION_FOR_BEGINNERS = new SkillHolder(4326);
	private static final SkillHolder HASTE_FOR_BEGINNERS = new SkillHolder(4327);
	private static final SkillHolder BLESS_THE_SOUL_FOR_BEGINNERS = new SkillHolder(4328);
	private static final SkillHolder ACUMEN_FOR_BEGINNERS = new SkillHolder(4329);
	private static final SkillHolder CONCENTRATION_FOR_BEGINNERS = new SkillHolder(4330);
	private static final SkillHolder EMPOWER_FOR_BEGINNERS = new SkillHolder(4331);
	private static final SkillHolder LIFE_CUBIC_FOR_BEGINNERS = new SkillHolder(4338);
	private static final SkillHolder BLESSING_OF_PROTECTION = new SkillHolder(5182);
	private static final SkillHolder ADVENTURERS_HASTE = new SkillHolder(5632);
	private static final SkillHolder ADVENTURERS_MAGIC_BARRIER = new SkillHolder(5637);
	
	// Buylist
	private static final int WEAPON_MULTISELL = 305986001;
	private static final int ACCESORIES_MULTISELL = 305986002;
	
	private static final Map<Integer, List<Location>> TELEPORT_MAP = new HashMap<>();
	
	static {
		Location TALKING_ISLAND_VILLAGE = new Location(-84081, 243227, -3723);
		Location DARK_ELF_VILLAGE = new Location(12111, 16686, -4582);
		Location DWARVEN_VILLAGE = new Location(115632, -177996, -905);
		Location ELVEN_VILLAGE = new Location(45475, 48359, -3060);
		Location ORC_VILLAGE = new Location(-45032, -113598, -192);
		Location KAMAEL_VILLAGE = new Location(-119697, 44532, 380);
		
		TELEPORT_MAP.put(NEWBIE_GUIDE_HUMAN, Arrays.asList(DARK_ELF_VILLAGE, DWARVEN_VILLAGE, ELVEN_VILLAGE, ORC_VILLAGE, KAMAEL_VILLAGE));
		TELEPORT_MAP.put(NEWBIE_GUIDE_ELF, Arrays.asList(DARK_ELF_VILLAGE, DWARVEN_VILLAGE, TALKING_ISLAND_VILLAGE, ORC_VILLAGE, KAMAEL_VILLAGE));
		TELEPORT_MAP.put(NEWBIE_GUIDE_DARK_ELF, Arrays.asList(DWARVEN_VILLAGE, TALKING_ISLAND_VILLAGE, ELVEN_VILLAGE, ORC_VILLAGE, KAMAEL_VILLAGE));
		TELEPORT_MAP.put(NEWBIE_GUIDE_DWARF, Arrays.asList(DARK_ELF_VILLAGE, TALKING_ISLAND_VILLAGE, ELVEN_VILLAGE, ORC_VILLAGE, KAMAEL_VILLAGE));
		TELEPORT_MAP.put(NEWBIE_GUIDE_ORC, Arrays.asList(DARK_ELF_VILLAGE, DWARVEN_VILLAGE, TALKING_ISLAND_VILLAGE, ELVEN_VILLAGE, KAMAEL_VILLAGE));
		TELEPORT_MAP.put(NEWBIE_GUIDE_KAMAEL, Arrays.asList(TALKING_ISLAND_VILLAGE, DARK_ELF_VILLAGE, ELVEN_VILLAGE, DWARVEN_VILLAGE, ORC_VILLAGE));
	}
	
	public NewbieGuide() {
		bindStartNpc(NEWBIE_GUIDE_HUMAN, NEWBIE_GUIDE_ELF, NEWBIE_GUIDE_DARK_ELF, NEWBIE_GUIDE_DWARF, NEWBIE_GUIDE_ORC, NEWBIE_GUIDE_KAMAEL, NEWBIE_GUIDE_GLUDIN, NEWBIE_GUIDE_GLUDIO, ADVENTURERS_GUIDE);
		bindFirstTalk(NEWBIE_GUIDE_HUMAN, NEWBIE_GUIDE_ELF, NEWBIE_GUIDE_DARK_ELF, NEWBIE_GUIDE_DWARF, NEWBIE_GUIDE_ORC, NEWBIE_GUIDE_KAMAEL, NEWBIE_GUIDE_GLUDIN, NEWBIE_GUIDE_GLUDIO, ADVENTURERS_GUIDE);
		bindTalk(NEWBIE_GUIDE_HUMAN, NEWBIE_GUIDE_ELF, NEWBIE_GUIDE_DARK_ELF, NEWBIE_GUIDE_DWARF, NEWBIE_GUIDE_ORC, NEWBIE_GUIDE_KAMAEL, NEWBIE_GUIDE_GLUDIN, NEWBIE_GUIDE_GLUDIO, ADVENTURERS_GUIDE);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		QuestState qs = player.getQuestState(Q00255_Tutorial.class.getSimpleName());
		if (qs != null) {
			switch (npc.getId()) {
				case ADVENTURERS_GUIDE -> {
					return "32327.htm";
				}
				case NEWBIE_GUIDE_GLUDIN -> {
					return "31076.htm";
				}
				case NEWBIE_GUIDE_GLUDIO -> {
					return "31077.htm";
				}
			}
			
			return talkGuide(player, npc, qs);
		}
		return super.onFirstTalk(npc, player);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance talker) {
		if (event.endsWith(".htm")) {
			return event;
		}
		if (event.startsWith("teleport")) {
			String[] tel = event.split("_");
			if (tel.length != 2) {
				teleportRequest(talker, npc, -1);
			} else {
				teleportRequest(talker, npc, Integer.parseInt(tel[1]));
			}
			return super.onEvent(event, npc, talker);
		}
		
		final QuestState qs = getQuestState(talker, true);
		int ask = Integer.parseInt(event.split(";")[0]);
		int reply = Integer.parseInt(event.split(";")[1]);
		switch (ask) {
			case -7: {
				switch (reply) {
					case 1: {
						if (npc.getRace() == Race.KAMAEL) {
							if (talker.getRace() != npc.getRace()) {
								showPage(talker, "32135-003.htm");
							} else if ((talker.getLevel() > 20) || ((talker.getRace() != Race.KAMAEL) || (talker.getClassId().level() != 0))) {
								showPage(talker, "32135-002.htm");
							} else if (talker.getClassId() == ClassId.maleSoldier) {
								if (talker.getLevel() <= 5) {
									showPage(talker, "32135-kmf05.htm");
								} else if (talker.getLevel() <= 10) {
									showPage(talker, "32135-kmf10.htm");
								} else if (talker.getLevel() <= 15) {
									showPage(talker, "32135-kmf15.htm");
								} else {
									showPage(talker, "32135-kmf20.htm");
								}
							} else if (talker.getClassId() == ClassId.femaleSoldier) {
								if (talker.getLevel() <= 5) {
									showPage(talker, "32135-kff05.htm");
								} else if (talker.getLevel() <= 10) {
									showPage(talker, "32135-kff10.htm");
								} else if (talker.getLevel() <= 15) {
									showPage(talker, "32135-kff15.htm");
								} else {
									showPage(talker, "32135-kff20.htm");
								}
							}
						} else if (talker.getRace() != npc.getRace()) {
							showPage(talker, npc.getId() + "-003.htm");
						} else if ((talker.getLevel() > 20) || (talker.getClassId().level() != 0)) {
							showPage(talker, "");
						} else if (!talker.isMageClass()) {
							if (talker.getLevel() <= 5) {
								showPage(talker, npc.getId() + SUFFIX_FIGHTER_5_LEVEL);
							} else if (talker.getLevel() <= 10) {
								showPage(talker, npc.getId() + SUFFIX_FIGHTER_10_LEVEL);
							} else if (talker.getLevel() <= 15) {
								showPage(talker, npc.getId() + SUFFIX_FIGHTER_15_LEVEL);
							} else {
								showPage(talker, npc.getId() + SUFFIX_FIGHTER_20_LEVEL);
							}
						} else if (talker.getLevel() <= 7) {
							showPage(talker, npc.getId() + SUFFIX_MAGE_7_LEVEL);
						} else if (talker.getLevel() <= 14) {
							showPage(talker, npc.getId() + SUFFIX_MAGE_14_LEVEL);
						} else {
							showPage(talker, npc.getId() + SUFFIX_MAGE_20_LEVEL);
						}
						break;
					}
					case 2: {
						if (talker.getLevel() <= 75) {
							if (talker.getLevel() < 6) {
								showPage(talker, "buffs-low-level.htm");
							} else if (!talker.isMageClass() && (talker.getClassId().level() < 3)) {
								npc.setTarget(talker);
								npc.doCast(WIND_WALK_FOR_BEGINNERS);
								npc.doCast(WIND_WALK_FOR_BEGINNERS);
								npc.doCast(SHIELD_FOR_BEGINNERS);
								npc.doCast(ADVENTURERS_MAGIC_BARRIER);
								npc.doCast(BLESS_THE_BODY_FOR_BEGINNERS);
								npc.doCast(VAMPIRIC_RAGE_FOR_BEGINNERS);
								npc.doCast(REGENERATION_FOR_BEGINNERS);
								if ((talker.getLevel() >= 6) && (talker.getLevel() <= 39)) {
									npc.doCast(HASTE_FOR_BEGINNERS);
								}
								if ((talker.getLevel() >= 40) && (talker.getLevel() <= 75)) {
									npc.doCast(ADVENTURERS_HASTE);
								}
								if ((talker.getLevel() >= 16) && (talker.getLevel() <= 34)) {
									npc.doCast(LIFE_CUBIC_FOR_BEGINNERS);
								}
							} else if (talker.isMageClass() && (talker.getClassId().level() < 3)) {
								npc.setTarget(talker);
								npc.doCast(WIND_WALK_FOR_BEGINNERS);
								npc.doCast(SHIELD_FOR_BEGINNERS);
								npc.doCast(ADVENTURERS_MAGIC_BARRIER);
								npc.doCast(BLESS_THE_SOUL_FOR_BEGINNERS);
								npc.doCast(ACUMEN_FOR_BEGINNERS);
								npc.doCast(CONCENTRATION_FOR_BEGINNERS);
								npc.doCast(EMPOWER_FOR_BEGINNERS);
								if ((talker.getLevel() >= 16) && (talker.getLevel() <= 34)) {
									npc.doCast(LIFE_CUBIC_FOR_BEGINNERS);
								}
							}
						} else {
							showPage(talker, "buffs-big-level.htm");
						}
						break;
					}
					case 3: {
						if ((talker.getLevel() <= 39) && (talker.getClassId().level() < 3)) {
							npc.setTarget(talker);
							npc.doCast(BLESSING_OF_PROTECTION);
						} else {
							showPage(talker, "pk-protection-002.htm");
						}
						break;
					}
					case 4: {
						L2Summon summon = talker.getSummon();
						if ((summon != null) && !summon.isPet()) {
							if ((talker.getLevel() < 6) || (talker.getLevel() > 75)) {
								showPage(talker, "buffs-big-level.htm");
							} else {
								npc.setTarget(talker);
								npc.doCast(WIND_WALK_FOR_BEGINNERS);
								npc.doCast(SHIELD_FOR_BEGINNERS);
								npc.doCast(ADVENTURERS_MAGIC_BARRIER);
								npc.doCast(BLESS_THE_BODY_FOR_BEGINNERS);
								npc.doCast(VAMPIRIC_RAGE_FOR_BEGINNERS);
								npc.doCast(REGENERATION_FOR_BEGINNERS);
								npc.doCast(BLESS_THE_SOUL_FOR_BEGINNERS);
								npc.doCast(ACUMEN_FOR_BEGINNERS);
								npc.doCast(CONCENTRATION_FOR_BEGINNERS);
								npc.doCast(EMPOWER_FOR_BEGINNERS);
								if ((talker.getLevel() >= 6) && (talker.getLevel() <= 39)) {
									npc.doCast(HASTE_FOR_BEGINNERS);
								}
								if ((talker.getLevel() >= 40) && (talker.getLevel() <= 75)) {
									npc.doCast(ADVENTURERS_HASTE);
								}
							}
						} else {
							showPage(talker, "buffs-no-pet.htm");
						}
						break;
					}
				}
				break;
			}
			case -1000: {
				switch (reply) {
					case 1: {
						if (talker.getLevel() > 5) {
							if ((talker.getLevel() < 20) && (talker.getClassId().level() == 0)) {
								if (getOneTimeQuestFlag(talker, 207) == 0) {
									qs.giveItems(APPRENTICE_ADVENTURERS_WEAPON_EXCHANGE_COUPON, FIRST_COUPON_SIZE);
									setOneTimeQuestFlag(talker, 207, 1);
									showPage(talker, "newbie-guide-002.htm");
									qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 100);
									showOnScreenMsg(talker, NpcStringId.ACQUISITION_OF_WEAPON_EXCHANGE_COUPON_FOR_BEGINNERS_COMPLETE_N_GO_SPEAK_WITH_THE_NEWBIE_GUIDE, 2, 5000, "");
								} else {
									showPage(talker, "newbie-guide-004.htm");
								}
							} else {
								showPage(talker, "newbie-guide-003.htm");
							}
						} else {
							showPage(talker, "newbie-guide-003.htm");
						}
						break;
					}
					case 2: {
						if (talker.getClassId().level() == 1) {
							if (talker.getLevel() < 40) {
								if (getOneTimeQuestFlag(talker, 208) == 0) {
									qs.giveItems(ADVENTURERS_MAGIC_ACCESSORY_EXCHANGE_COUPON, SECOND_COUPON_SIZE);
									setOneTimeQuestFlag(talker, 208, 1);
									showPage(talker, "newbie-guide-011.htm");
								} else {
									showPage(talker, "newbie-guide-013.htm");
								}
							} else {
								showPage(talker, "newbie-guide-012.htm");
							}
						} else {
							showPage(talker, "newbie-guide-012.htm");
						}
						break;
					}
				}
				break;
				
			}
			case -303: {
				switch (reply) {
					case 528:
						if (talker.getLevel() > 5) {
							if ((talker.getLevel() < 20) && (talker.getClassId().level() == 0)) {
								MultisellData.getInstance().separateAndSend(WEAPON_MULTISELL, talker, npc, false);
							} else {
								showPage(talker, "newbie-guide-005.htm");
							}
						} else {
							showPage(talker, "newbie-guide-005.htm");
						}
						break;
					case 529:
						if (talker.getLevel() > 5) {
							if ((talker.getLevel() < 40) && (talker.getClassId().level() == 1)) {
								MultisellData.getInstance().separateAndSend(ACCESORIES_MULTISELL, talker, npc, false);
							} else {
								showPage(talker, "newbie-guide-014.htm");
							}
						} else {
							showPage(talker, "newbie-guide-014.htm");
						}
						break;
				}
				break;
			}
		}
		switch (npc.getId()) {
			case NEWBIE_GUIDE_HUMAN: {
				String ansGuideHumanCnacelot = eventGuideHumanCnacelot(reply, talker, 2);
				if (!ansGuideHumanCnacelot.isEmpty()) {
					return ansGuideHumanCnacelot;
				}
				break;
			}
			case NEWBIE_GUIDE_ELF: {
				String ansGuideElfRoios = eventGuideElfRoios(reply, talker, 2);
				if (!ansGuideElfRoios.isEmpty()) {
					return ansGuideElfRoios;
				}
				break;
			}
			case NEWBIE_GUIDE_DARK_ELF: {
				String ansGuideDelfFrankia = eventGuideDelfFrankia(reply, talker, 2);
				if (!ansGuideDelfFrankia.isEmpty()) {
					return ansGuideDelfFrankia;
				}
				break;
			}
			case NEWBIE_GUIDE_DWARF: {
				String ansGuideDwarfGullin = eventGuideDwarfGullin(reply, talker, 2);
				if (!ansGuideDwarfGullin.isEmpty()) {
					return ansGuideDwarfGullin;
				}
				break;
			}
			case NEWBIE_GUIDE_ORC: {
				String ansGuideOrcTanai = eventGuideOrcTanai(reply, talker, 2);
				if (!ansGuideOrcTanai.isEmpty()) {
					return ansGuideOrcTanai;
				}
				break;
			}
			case NEWBIE_GUIDE_KAMAEL: {
				String ansGuideKrenisk = eventGuideKrenisk(reply, talker, 2);
				if (!ansGuideKrenisk.isEmpty()) {
					return ansGuideKrenisk;
				}
				break;
			}
		}
		return "";
	}
	
	private void teleportRequest(L2PcInstance talker, L2Npc npc, int teleportId) {
		if (talker.getLevel() >= 20) {
			showPage(talker, "teleport-big-level.htm");
		} else if ((talker.getTransformationId() == 111) || (talker.getTransformationId() == 112) || (talker.getTransformationId() == 124)) {
			showPage(talker, "frog-teleport.htm");
		} else {
			if ((teleportId < 0) || (teleportId > 5)) {
				showPage(talker, npc.getId() + "-teleport.htm");
			} else {
				if (TELEPORT_MAP.containsKey(npc.getId())) {
					if (TELEPORT_MAP.get(npc.getId()).size() > teleportId) {
						talker.teleToLocation(TELEPORT_MAP.get(npc.getId()).get(teleportId), false);
					}
				}
			}
		}
	}
	
	private String talkGuide(L2PcInstance talker, L2Npc npc, QuestState tutorialQS) {
		final QuestState qs = getQuestState(talker, true);
		if ((tutorialQS.getMemoStateEx(1) < 5) && (getOneTimeQuestFlag(talker, GUIDE_MISSION) == 0)) {
			if (!talker.isMageClass()) {
				qs.playSound(Voice.TUTORIAL_VOICE_026_1000);
				qs.giveItems(SOULSHOT_NO_GRADE_FOR_BEGINNERS, 200);
				qs.giveItems(SCROLL_RECOVERY_NO_GRADE, 2);
				tutorialQS.setMemoStateEx(1, 5);
				if (talker.getLevel() <= 1) {
					qs.addExpAndSp(68, 50);
				} else {
					qs.addExpAndSp(0, 50);
				}
			}
			if (talker.isMageClass()) {
				if (talker.getClassId() == ClassId.orcMage) {
					qs.playSound(Voice.TUTORIAL_VOICE_026_1000);
					qs.giveItems(SOULSHOT_NO_GRADE_FOR_BEGINNERS, 200);
				} else {
					qs.playSound(Voice.TUTORIAL_VOICE_027_1000);
					qs.giveItems(SPIRITSHOT_NO_GRADE_FOR_BEGINNERS, 100);
				}
				qs.giveItems(SCROLL_RECOVERY_NO_GRADE, 2);
				tutorialQS.setMemoStateEx(1, 5);
				if (talker.getLevel() <= 1) {
					qs.addExpAndSp(68, 50);
				} else {
					qs.addExpAndSp(0, 50);
				}
			}
			if (talker.getLevel() < 6) {
				if ((qs.getNRMemoState(talker, GUIDE_MISSION) % 10) == 1) {
					if (talker.getLevel() >= 5) {
						qs.giveAdena(695, true);
						qs.addExpAndSp(3154, 127);
					} else if (talker.getLevel() >= 4) {
						qs.giveAdena(1041, true);
						qs.addExpAndSp(4870, 195);
					} else if (talker.getLevel() >= 3) {
						qs.giveAdena(1186, true);
						qs.addExpAndSp(5675, 227);
					} else {
						qs.giveAdena(1240, true);
						qs.addExpAndSp(5970, 239);
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 10);
					} else {
						qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 10);
					}
					showPage(talker, "newbie-guide-02.htm");
				} else {
					switch (npc.getRace()) {
						case HUMAN -> {
							showRadar(talker, -84436, 242793, -3729, 2);
							showPage(talker, "newbie-guide-01a.htm");
						}
						case ELF -> {
							showRadar(talker, 42978, 49115, 2994, 2);
							showPage(talker, "newbie-guide-01b.htm");
						}
						case DARK_ELF -> {
							showRadar(talker, 25790, 10844, -3727, 2);
							showPage(talker, "newbie-guide-01c.htm");
						}
						case ORC -> {
							showRadar(talker, -47360, -113791, -237, 2);
							showPage(talker, "newbie-guide-01d.htm");
						}
						case DWARF -> {
							showRadar(talker, 112656, -174864, -611, 2);
							showPage(talker, "newbie-guide-01e.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -119378, 49242, 22, 2);
							showPage(talker, "newbie-guide-01f.htm");
						}
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
				}
			} else if (talker.getLevel() < 10) {
				if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000) / 100) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 10000) / 100) == 1)) {
					switch (talker.getRace()) {
						case HUMAN -> {
							if (!talker.isMageClass()) {
								showRadar(talker, -71384, 258304, -3109, 2);
								showPage(talker, "newbie-guide-05a.htm");
							} else {
								showRadar(talker, -91008, 248016, -3568, 2);
								showPage(talker, "newbie-guide-05b.htm");
							}
						}
						case ELF -> {
							showRadar(talker, 47595, 51569, -2996, 2);
							showPage(talker, "newbie-guide-05c.htm");
						}
						case DARK_ELF -> {
							if (!talker.isMageClass()) {
								showRadar(talker, 10580, 17574, -4554, 2);
								showPage(talker, "newbie-guide-05d.htm");
							} else {
								showRadar(talker, 10775, 14190, -4242, 2);
								showPage(talker, "newbie-guide-05e.htm");
							}
						}
						case ORC -> {
							showRadar(talker, -46808, -113184, -112, 2);
							showPage(talker, "newbie-guide-05f.htm");
						}
						case DWARF -> {
							showRadar(talker, 115717, -183488, -1483, 2);
							showPage(talker, "newbie-guide-05g.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -118080, 42835, 720, 2);
							showPage(talker, "newbie-guide-05h.htm");
						}
					}
					if (talker.getLevel() >= 9) {
						qs.giveAdena(5563, true);
						qs.addExpAndSp(16851, 711);
					} else if (talker.getLevel() >= 8) {
						qs.giveAdena(9290, true);
						qs.addExpAndSp(28806, 1207);
					} else if (talker.getLevel() >= 7) {
						qs.giveAdena(11567, true);
						qs.addExpAndSp(36942, 1541);
					} else {
						qs.giveAdena(12928, true);
						qs.addExpAndSp(42191, 1753);
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 10000);
					} else {
						qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 10000);
					}
				} else if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000) / 100) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 10000) / 100) != 1)) {
					switch (npc.getRace()) {
						case HUMAN -> {
							showRadar(talker, -82236, 241573, -3728, 2);
							showPage(talker, "newbie-guide-04a.htm");
						}
						case ELF -> {
							showRadar(talker, 42812, 51138, -2996, 2);
							showPage(talker, "newbie-guide-04b.htm");
						}
						case DARK_ELF -> {
							showRadar(talker, 7644, 18048, -4377, 2);
							showPage(talker, "newbie-guide-04c.htm");
						}
						case ORC -> {
							showRadar(talker, -46802, -114011, -112, 2);
							showPage(talker, "newbie-guide-04d.htm");
						}
						case DWARF -> {
							showRadar(talker, 116103, -178407, -948, 2);
							showPage(talker, "newbie-guide-04e.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -119378, 49242, 22, 2);
							showPage(talker, "newbie-guide-04f.htm");
						}
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
				} else {
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
					showPage(talker, "newbie-guide-03.htm");
				}
			} else {
				setOneTimeQuestFlag(talker, GUIDE_MISSION, 1);
				if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
					qs.setNRMemo(talker, GUIDE_MISSION);
					qs.setNRMemoState(talker, GUIDE_MISSION, 0);
				}
				showPage(talker, "newbie-guide-06.htm");
			}
		} else if ((tutorialQS.getMemoStateEx(1) >= 5) && (getOneTimeQuestFlag(talker, GUIDE_MISSION) == 0)) {
			if (talker.getLevel() < 6) {
				if ((qs.getNRMemoState(talker, GUIDE_MISSION) % 10) == 1) {
					if (talker.getLevel() >= 5) {
						qs.giveAdena(695, true);
						qs.addExpAndSp(3154, 127);
					} else if (talker.getLevel() >= 4) {
						qs.giveAdena(1041, true);
						qs.addExpAndSp(4870, 195);
					} else if (talker.getLevel() >= 3) {
						qs.giveAdena(1186, true);
						qs.addExpAndSp(5675, 227);
					} else {
						qs.giveAdena(1240, true);
						qs.addExpAndSp(5970, 239);
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 10);
					} else {
						qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 10);
					}
					showPage(talker, "newbie-guide-08.htm");
				} else {
					switch (npc.getRace()) {
						case HUMAN -> {
							showRadar(talker, -84436, 242793, -3729, 2);
							showPage(talker, "newbie-guide-07a.htm");
						}
						case ELF -> {
							showRadar(talker, 42978, 49115, 2994, 2);
							showPage(talker, "newbie-guide-07b.htm");
						}
						case DARK_ELF -> {
							showRadar(talker, 25790, 10844, -3727, 2);
							showPage(talker, "newbie-guide-07c.htm");
						}
						case ORC -> {
							showRadar(talker, -47360, -113791, -237, 2);
							showPage(talker, "newbie-guide-07d.htm");
						}
						case DWARF -> {
							showRadar(talker, 112656, -174864, -611, 2);
							showPage(talker, "newbie-guide-07e.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -119378, 49242, 22, 2);
							showPage(talker, "newbie-guide-07f.htm");
						}
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
				}
			} else if (talker.getLevel() < 10) {
				if (((qs.getNRMemoState(talker, GUIDE_MISSION) % 100000) / 10000) == 1) {
					showPage(talker, "newbie-guide-09g.htm");
				} else if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000) / 100) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 10000) / 1000) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 100000) / 10000) != 1)) {
					switch (talker.getRace()) {
						case HUMAN -> {
							if (!talker.isMageClass()) {
								showRadar(talker, -71384, 258304, -3109, 2);
								showPage(talker, "newbie-guide-10a.htm");
							} else {
								showRadar(talker, -91008, 248016, -3568, 2);
								showPage(talker, "newbie-guide-10b.htm");
							}
						}
						case ELF -> {
							showRadar(talker, 47595, 51569, -2996, 2);
							showPage(talker, "newbie-guide-10c.htm");
						}
						case DARK_ELF -> {
							if (!talker.isMageClass()) {
								showRadar(talker, 10580, 17574, -4554, 2);
								showPage(talker, "newbie-guide-10d.htm");
							} else {
								showRadar(talker, 10775, 14190, -4242, 2);
								showPage(talker, "newbie-guide-10e.htm");
							}
						}
						case ORC -> {
							showRadar(talker, -46808, -113184, -112, 2);
							showPage(talker, "newbie-guide-10f.htm");
						}
						case DWARF -> {
							showRadar(talker, 115717, -183488, -1483, 2);
							showPage(talker, "newbie-guide-10g.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -118080, 42835, 720, 2);
							showPage(talker, "newbie-guide-10h.htm");
						}
					}
					if (talker.getLevel() >= 9) {
						qs.giveAdena(5563, true);
						qs.addExpAndSp(16851, 711);
					} else if (talker.getLevel() >= 8) {
						qs.giveAdena(9290, true);
						qs.addExpAndSp(28806, 1207);
					} else if (talker.getLevel() >= 7) {
						qs.giveAdena(11567, true);
						qs.addExpAndSp(36942, 1541);
					} else {
						qs.giveAdena(12928, true);
						qs.addExpAndSp(42191, 1753);
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 10000);
					} else {
						qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 10000);
					}
				} else if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000) / 100) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 10000) / 1000) != 1)) {
					switch (npc.getRace()) {
						case HUMAN -> {
							showRadar(talker, -82236, 241573, -3728, 2);
							showPage(talker, "newbie-guide-09a.htm");
						}
						case ELF -> {
							showRadar(talker, 42812, 51138, -2996, 2);
							showPage(talker, "newbie-guide-09b.htm");
						}
						case DARK_ELF -> {
							showRadar(talker, 7644, 18048, -4377, 2);
							showPage(talker, "newbie-guide-09c.htm");
						}
						case ORC -> {
							showRadar(talker, -46802, -114011, -112, 2);
							showPage(talker, "newbie-guide-09d.htm");
						}
						case DWARF -> {
							showRadar(talker, 116103, -178407, -948, 2);
							showPage(talker, "newbie-guide-09e.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -119378, 49242, 22, 2);
							showPage(talker, "newbie-guide-09f.htm");
						}
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
				} else {
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
					showPage(talker, "newbie-guide-08.htm");
				}
			} else if (talker.getLevel() < 15) {
				if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000000) / 100000) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 10000000) / 1000000) == 1)) {
					showPage(talker, "newbie-guide-15.htm");
				} else if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000000) / 100000) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 10000000) / 1000000) != 1)) {
					switch (npc.getRace()) {
						case HUMAN -> {
							showRadar(talker, -84057, 242832, -3729, 2);
							showPage(talker, "newbie-guide-11a.htm");
						}
						case ELF -> {
							showRadar(talker, 45859, 50827, -3058, 2);
							showPage(talker, "newbie-guide-11b.htm");
						}
						case DARK_ELF -> {
							showRadar(talker, 11258, 14431, -4242, 2);
							showPage(talker, "newbie-guide-11c.htm");
						}
						case ORC -> {
							showRadar(talker, -45863, -112621, -200, 2);
							showPage(talker, "newbie-guide-11d.htm");
						}
						case DWARF -> {
							showRadar(talker, 116268, -177524, -914, 2);
							showPage(talker, "newbie-guide-11e.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -125872, 38208, 1251, 2);
							showPage(talker, "newbie-guide-11f.htm");
						}
					}
					if (talker.getLevel() >= 14) {
						qs.giveAdena(13002, true);
						qs.addExpAndSp(62876, 2891);
					} else if (talker.getLevel() >= 13) {
						qs.giveAdena(23468, true);
						qs.addExpAndSp(113137, 5161);
					} else if (talker.getLevel() >= 12) {
						qs.giveAdena(31752, true);
						qs.addExpAndSp(152653, 6914);
					} else if (talker.getLevel() >= 11) {
						qs.giveAdena(38180, true);
						qs.addExpAndSp(183128, 8242);
					} else {
						qs.giveAdena(43054, true);
						qs.addExpAndSp(206101, 9227);
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 1000000);
					} else {
						qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 1000000);
					}
				} else if (((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000000) / 100000) != 1) {
					switch (talker.getRace()) {
						case HUMAN -> {
							if (!talker.isMageClass()) {
								showRadar(talker, -71384, 258304, -3109, 2);
								showPage(talker, "newbie-guide-10a.htm");
							} else {
								showRadar(talker, -91008, 248016, -3568, 2);
								showPage(talker, "newbie-guide-10b.htm");
							}
						}
						case ELF -> {
							showRadar(talker, 47595, 51569, -2996, 2);
							showPage(talker, "newbie-guide-10c.htm");
						}
						case DARK_ELF -> {
							if (!talker.isMageClass()) {
								showRadar(talker, 10580, 17574, -4554, 2);
								showPage(talker, "newbie-guide-10d.htm");
							} else {
								showRadar(talker, 10775, 14190, -4242, 2);
								showPage(talker, "newbie-guide-10e.htm");
							}
						}
						case ORC -> {
							showRadar(talker, -46808, -113184, -112, 2);
							showPage(talker, "newbie-guide-10f.htm");
						}
						case DWARF -> {
							showRadar(talker, 115717, -183488, -1483, 2);
							showPage(talker, "newbie-guide-10g.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -118080, 42835, 720, 2);
							showPage(talker, "newbie-guide-10h.htm");
						}
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 0);
					}
				}
			} else if (talker.getLevel() < 18) {
				if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 100000000) / 10000000) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000000000) / 100000000) == 1)) {
					setOneTimeQuestFlag(talker, GUIDE_MISSION, 1);
					showPage(talker, "newbie-guide-13.htm");
				} else if ((((qs.getNRMemoState(talker, GUIDE_MISSION) % 100000000) / 10000000) == 1) && (((qs.getNRMemoState(talker, GUIDE_MISSION) % 1000000000) / 100000000) != 1)) {
					if (talker.getLevel() >= 17) {
						qs.giveAdena(22996, true);
						qs.addExpAndSp(113712, 5518);
					} else if (talker.getLevel() >= 16) {
						qs.giveAdena(10018, true);
						qs.addExpAndSp(208133, 42237);
					} else {
						qs.giveAdena(13648, true);
						qs.addExpAndSp(285670, 58155);
					}
					if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
						qs.setNRMemo(talker, GUIDE_MISSION);
						qs.setNRMemoState(talker, GUIDE_MISSION, 100000000);
					} else {
						qs.setNRMemoState(talker, GUIDE_MISSION, qs.getNRMemoState(talker, GUIDE_MISSION) + 100000000);
					}
					setOneTimeQuestFlag(talker, GUIDE_MISSION, 1);
					showPage(talker, "newbie-guide-12.htm");
				} else if (((qs.getNRMemoState(talker, GUIDE_MISSION) % 100000000) / 10000000) != 1) {
					switch (npc.getRace()) {
						case HUMAN -> {
							showRadar(talker, -84057, 242832, -3729, 2);
							showPage(talker, "newbie-guide-11a.htm");
						}
						case ELF -> {
							showRadar(talker, 45859, 50827, -3058, 2);
							showPage(talker, "newbie-guide-11b.htm");
						}
						case DARK_ELF -> {
							showRadar(talker, 11258, 14431, -4242, 2);
							showPage(talker, "newbie-guide-11c.htm");
						}
						case ORC -> {
							showRadar(talker, -45863, -112621, -200, 2);
							showPage(talker, "newbie-guide-11d.htm");
						}
						case DWARF -> {
							showRadar(talker, 116268, -177524, -914, 2);
							showPage(talker, "newbie-guide-11e.htm");
						}
						case KAMAEL -> {
							showRadar(talker, -125872, 38208, 1251, 2);
							showPage(talker, "newbie-guide-11f.htm");
						}
					}
				}
				if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
					qs.setNRMemo(talker, GUIDE_MISSION);
					qs.setNRMemoState(talker, GUIDE_MISSION, 0);
				}
			} else if (talker.getClassId().level() == 1) {
				setOneTimeQuestFlag(talker, GUIDE_MISSION, 1);
				if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
					qs.setNRMemo(talker, GUIDE_MISSION);
					qs.setNRMemoState(talker, GUIDE_MISSION, 0);
				}
				showPage(talker, "newbie-guide-13.htm");
			} else {
				setOneTimeQuestFlag(talker, GUIDE_MISSION, 1);
				if (!qs.haveNRMemo(talker, GUIDE_MISSION)) {
					qs.setNRMemo(talker, GUIDE_MISSION);
					qs.setNRMemoState(talker, GUIDE_MISSION, 0);
				}
				showPage(talker, "newbie-guide-14.htm");
			}
		}
		return "";
	}
	
	private String eventGuideHumanCnacelot(int event, L2PcInstance player, int type) {
		switch (event) {
			case 10:
				return "30598-04.htm";
			case 11:
				return "30598-04a.htm";
			case 12:
				return "30598-04b.htm";
			case 13:
				return "30598-04c.htm";
			case 14:
				return "30598-04d.htm";
			case 15:
				return "30598-04e.htm";
			case 16:
				return "30598-04f.htm";
			case 17:
				return "30598-04g.htm";
			case 18:
				return "30598-04h.htm";
			case 19:
				return "30598-04i.htm";
			case 31:
				deleteAllRadar(player, type);
				showRadar(player, -84108, 244604, -3729, 2);
				return "30598-05.htm";
			case 32:
				deleteAllRadar(player, type);
				showRadar(player, -82236, 241573, -3728, 2);
				return "30598-05.htm";
			case 33:
				deleteAllRadar(player, type);
				showRadar(player, -82515, 241221, -3728, 2);
				return "30598-05.htm";
			case 34:
				deleteAllRadar(player, type);
				showRadar(player, -82319, 244709, -3727, 2);
				return "30598-05.htm";
			case 35:
				deleteAllRadar(player, type);
				showRadar(player, -82659, 244992, -3717, 2);
				return "30598-05.htm";
			case 36:
				deleteAllRadar(player, type);
				showRadar(player, -86114, 244682, -3727, 2);
				return "30598-05.htm";
			case 37:
				deleteAllRadar(player, type);
				showRadar(player, -86328, 244448, -3724, 2);
				return "30598-05.htm";
			case 38:
				deleteAllRadar(player, type);
				showRadar(player, -86322, 241215, -3727, 2);
				return "30598-05.htm";
			case 39:
				deleteAllRadar(player, type);
				showRadar(player, -85964, 240947, -3727, 2);
				return "30598-05.htm";
			case 40:
				deleteAllRadar(player, type);
				showRadar(player, -85026, 242689, -3729, 2);
				return "30598-05.htm";
			case 41:
				deleteAllRadar(player, type);
				showRadar(player, -83789, 240799, -3717, 2);
				return "30598-05.htm";
			case 42: {
				deleteAllRadar(player, type);
				showRadar(player, -84204, 240403, -3717, 2);
				return "30598-05.htm";
			}
			case 43: {
				deleteAllRadar(player, type);
				showRadar(player, -86385, 243267, -3717, 2);
				return "30598-05.htm";
			}
			case 44: {
				deleteAllRadar(player, type);
				showRadar(player, -86733, 242918, -3717, 2);
				return "30598-05.htm";
			}
			case 45: {
				deleteAllRadar(player, type);
				showRadar(player, -84516, 245449, -3714, 2);
				return "30598-05.htm";
			}
			case 46: {
				deleteAllRadar(player, type);
				showRadar(player, -84729, 245001, -3726, 2);
				return "30598-05.htm";
			}
			case 47: {
				deleteAllRadar(player, type);
				showRadar(player, -84965, 245222, -3726, 2);
				return "30598-05.htm";
			}
			case 48: {
				deleteAllRadar(player, type);
				showRadar(player, -84981, 244764, -3726, 2);
				return "30598-05.htm";
			}
			case 49: {
				deleteAllRadar(player, type);
				showRadar(player, -85186, 245001, -3726, 2);
				return "30598-05.htm";
			}
			case 50: {
				deleteAllRadar(player, type);
				showRadar(player, -83326, 242964, -3718, 2);
				return "30598-05.htm";
			}
			case 51: {
				deleteAllRadar(player, type);
				showRadar(player, -83020, 242553, -3718, 2);
				return "30598-05.htm";
			}
			case 52: {
				deleteAllRadar(player, type);
				showRadar(player, -83175, 243065, -3718, 2);
				return "30598-05.htm";
			}
			case 53: {
				deleteAllRadar(player, type);
				showRadar(player, -82809, 242751, -3718, 2);
				return "30598-05.htm";
			}
			case 54: {
				deleteAllRadar(player, type);
				showRadar(player, -81895, 243917, -3721, 2);
				return "30598-05.htm";
			}
			case 55: {
				deleteAllRadar(player, type);
				showRadar(player, -81840, 243534, -3721, 2);
				return "30598-05.htm";
			}
			case 56: {
				deleteAllRadar(player, type);
				showRadar(player, -81512, 243424, -3720, 2);
				return "30598-05.htm";
			}
			case 57: {
				deleteAllRadar(player, type);
				showRadar(player, -84436, 242793, -3729, 2);
				return "30598-05.htm";
			}
			case 58: {
				deleteAllRadar(player, type);
				showRadar(player, -78939, 240305, -3443, 2);
				return "30598-05.htm";
			}
			case 59: {
				deleteAllRadar(player, type);
				showRadar(player, -85301, 244587, -3725, 2);
				return "30598-05.htm";
			}
			case 60: {
				deleteAllRadar(player, type);
				showRadar(player, -83163, 243560, -3728, 2);
				return "30598-05.htm";
			}
			case 61: {
				deleteAllRadar(player, type);
				showRadar(player, -97131, 258946, -3622, 2);
				return "30598-05.htm";
			}
			case 62: {
				deleteAllRadar(player, type);
				showRadar(player, -114685, 222291, -2925, 2);
				return "30598-05.htm";
			}
			case 63: {
				deleteAllRadar(player, type);
				showRadar(player, -84057, 242832, -3729, 2);
				return "30598-05.htm";
			}
			case 64: {
				deleteAllRadar(player, type);
				showRadar(player, -100332, 238019, -3573, 2);
				return "30598-05.htm";
			}
			case 65: {
				deleteAllRadar(player, type);
				showRadar(player, -82041, 242718, -3725, 2);
				return "30598-05.htm";
			}
		}
		return "";
	}
	
	private String eventGuideElfRoios(int event, L2PcInstance player, int type) {
		switch (event) {
			case 10:
				return "30599-04.htm";
			case 11:
				return "30599-04a.htm";
			case 12:
				return "30599-04b.htm";
			case 13:
				return "30599-04c.htm";
			case 14:
				return "30599-04d.htm";
			case 15:
				return "30599-04e.htm";
			case 16:
				return "30599-04f.htm";
			case 17:
				return "30599-04g.htm";
			case 18:
				return "30599-04h.htm";
			case 31:
				deleteAllRadar(player, type);
				showRadar(player, 46926, 51511, -2977, 2);
				return "30599-05.htm";
			case 32:
				deleteAllRadar(player, type);
				showRadar(player, 44995, 51706, -2803, 2);
				return "30599-05.htm";
			case 33:
				deleteAllRadar(player, type);
				showRadar(player, 45727, 51721, -2803, 2);
				return "30599-05.htm";
			case 34:
				deleteAllRadar(player, type);
				showRadar(player, 42812, 51138, -2996, 2);
				return "30599-05.htm";
			case 35:
				deleteAllRadar(player, type);
				showRadar(player, 45487, 46511, -2996, 2);
				return "30599-05.htm";
			case 36:
				deleteAllRadar(player, type);
				showRadar(player, 47401, 51764, -2996, 2);
				return "30599-05.htm";
			case 37:
				deleteAllRadar(player, type);
				showRadar(player, 42971, 51372, -2996, 2);
				return "30599-05.htm";
			case 38:
				deleteAllRadar(player, type);
				showRadar(player, 47595, 51569, -2996, 2);
				return "30599-05.htm";
			case 39:
				deleteAllRadar(player, type);
				showRadar(player, 45778, 46534, -2996, 2);
				return "30599-05.htm";
			case 40:
				deleteAllRadar(player, type);
				showRadar(player, 44476, 47153, -2984, 2);
				return "30599-05.htm";
			case 41:
				deleteAllRadar(player, type);
				showRadar(player, 42700, 50057, -2984, 2);
				return "30599-05.htm";
			case 42:
				deleteAllRadar(player, type);
				showRadar(player, 42766, 50037, -2984, 2);
				return "30599-05.htm";
			case 43:
				deleteAllRadar(player, type);
				showRadar(player, 44683, 46952, -2981, 2);
				return "30599-05.htm";
			case 44:
				deleteAllRadar(player, type);
				showRadar(player, 44667, 46896, -2982, 2);
				return "30599-05.htm";
			case 45:
				deleteAllRadar(player, type);
				showRadar(player, 45725, 52105, -2795, 2);
				return "30599-05.htm";
			case 46:
				deleteAllRadar(player, type);
				showRadar(player, 44823, 52414, -2795, 2);
				return "30599-05.htm";
			case 47:
				deleteAllRadar(player, type);
				showRadar(player, 45000, 52101, -2795, 2);
				return "30599-05.htm";
			case 48:
				deleteAllRadar(player, type);
				showRadar(player, 45919, 52414, -2795, 2);
				return "30599-05.htm";
			case 49:
				deleteAllRadar(player, type);
				showRadar(player, 44692, 52261, -2795, 2);
				return "30599-05.htm";
			case 50:
				deleteAllRadar(player, type);
				showRadar(player, 47780, 49568, -2983, 2);
				return "30599-05.htm";
			case 51:
				deleteAllRadar(player, type);
				showRadar(player, 47912, 50170, -2983, 2);
				return "30599-05.htm";
			case 52:
				deleteAllRadar(player, type);
				showRadar(player, 47868, 50167, -2983, 2);
				return "30599-05.htm";
			case 53:
				deleteAllRadar(player, type);
				showRadar(player, 28928, 74248, -3773, 2);
				return "30599-05.htm";
			case 54:
				deleteAllRadar(player, type);
				showRadar(player, 43673, 49683, -3046, 2);
				return "30599-05.htm";
			case 55:
				deleteAllRadar(player, type);
				showRadar(player, 45610, 49008, -3059, 2);
				return "30599-05.htm";
			case 56:
				deleteAllRadar(player, type);
				showRadar(player, 50592, 54986, -3376, 2);
				return "30599-05.htm";
			case 57:
				deleteAllRadar(player, type);
				showRadar(player, 42978, 49115, -2994, 2);
				return "30599-05.htm";
			case 58:
				deleteAllRadar(player, type);
				showRadar(player, 46475, 50495, -3058, 2);
				return "30599-05.htm";
			case 59:
				deleteAllRadar(player, type);
				showRadar(player, 45859, 50827, -3058, 2);
				return "30599-05.htm";
			case 60:
				deleteAllRadar(player, type);
				showRadar(player, 51210, 82474, -3283, 2);
				return "30599-05.htm";
			case 61:
				deleteAllRadar(player, type);
				showRadar(player, 49262, 53607, -3216, 2);
				return "30599-05.htm";
		}
		return "";
	}
	
	private String eventGuideDelfFrankia(int event, L2PcInstance player, int type) {
		switch (event) {
			case 10:
				return "30600-04.htm";
			case 11:
				return "30600-04a.htm";
			case 12:
				return "30600-04b.htm";
			case 13:
				return "30600-04c.htm";
			case 14:
				return "30600-04d.htm";
			case 15:
				return "30600-04e.htm";
			case 16:
				return "30600-04f.htm";
			case 17:
				return "30600-04g.htm";
			case 18:
				return "30600-04h.htm";
			case 31:
				deleteAllRadar(player, type);
				showRadar(player, 9670, 15537, -4574, 2);
				return "30600-05.htm";
			case 32:
				deleteAllRadar(player, type);
				showRadar(player, 15120, 15656, -4376, 2);
				return "30600-05.htm";
			case 33:
				deleteAllRadar(player, type);
				showRadar(player, 17306, 13592, -3724, 2);
				return "30600-05.htm";
			case 34:
				deleteAllRadar(player, type);
				showRadar(player, 15272, 16310, -4377, 2);
				return "30600-05.htm";
			case 35:
				deleteAllRadar(player, type);
				showRadar(player, 6449, 19619, -3694, 2);
				return "30600-05.htm";
			case 36:
				deleteAllRadar(player, type);
				showRadar(player, -15404, 71131, -3445, 2);
				return "30600-05.htm";
			case 37:
				deleteAllRadar(player, type);
				showRadar(player, 7496, 17388, -4377, 2);
				return "30600-05.htm";
			case 38:
				deleteAllRadar(player, type);
				showRadar(player, 17102, 13002, -3743, 2);
				return "30600-05.htm";
			case 39:
				deleteAllRadar(player, type);
				showRadar(player, 6532, 19903, -3693, 2);
				return "30600-05.htm";
			case 40:
				deleteAllRadar(player, type);
				showRadar(player, -15648, 71405, -3451, 2);
				return "30600-05.htm";
			case 41:
				deleteAllRadar(player, type);
				showRadar(player, 7644, 18048, -4377, 2);
				return "30600-05.htm";
			case 42:
				deleteAllRadar(player, type);
				showRadar(player, -1301, 75883, -3566, 2);
				return "30600-05.htm";
			case 43:
				deleteAllRadar(player, type);
				showRadar(player, -1152, 76125, -3566, 2);
				return "30600-05.htm";
			case 44:
				deleteAllRadar(player, type);
				showRadar(player, 10580, 17574, -4554, 2);
				return "30600-05.htm";
			case 45:
				deleteAllRadar(player, type);
				showRadar(player, 12009, 15704, -4554, 2);
				return "30600-05.htm";
			case 46:
				deleteAllRadar(player, type);
				showRadar(player, 11951, 15661, -4554, 2);
				return "30600-05.htm";
			case 47:
				deleteAllRadar(player, type);
				showRadar(player, 10761, 17970, -4554, 2);
				return "30600-05.htm";
			case 48:
				deleteAllRadar(player, type);
				showRadar(player, 10823, 18013, -4554, 2);
				return "30600-05.htm";
			case 49:
				deleteAllRadar(player, type);
				showRadar(player, 11283, 14226, -4242, 2);
				return "30600-05.htm";
			case 50:
				deleteAllRadar(player, type);
				showRadar(player, 10447, 14620, -4242, 2);
				return "30600-05.htm";
			case 51:
				deleteAllRadar(player, type);
				showRadar(player, 11258, 14431, -4242, 2);
				return "30600-05.htm";
			case 52:
				deleteAllRadar(player, type);
				showRadar(player, 10344, 14445, -4242, 2);
				return "30600-05.htm";
			case 53:
				deleteAllRadar(player, type);
				showRadar(player, 10315, 14293, -4242, 2);
				return "30600-05.htm";
			case 54:
				deleteAllRadar(player, type);
				showRadar(player, 10775, 14190, -4242, 2);
				return "30600-05.htm";
			case 55:
				deleteAllRadar(player, type);
				showRadar(player, 11235, 14078, -4242, 2);
				return "30600-05.htm";
			case 56:
				deleteAllRadar(player, type);
				showRadar(player, 11012, 14128, -4242, 2);
				return "30600-05.htm";
			case 57:
				deleteAllRadar(player, type);
				showRadar(player, 13380, 17430, -4542, 2);
				return "30600-05.htm";
			case 58:
				deleteAllRadar(player, type);
				showRadar(player, 13464, 17751, -4541, 2);
				return "30600-05.htm";
			case 59:
				deleteAllRadar(player, type);
				showRadar(player, 13763, 17501, -4542, 2);
				return "30600-05.htm";
			case 60:
				deleteAllRadar(player, type);
				showRadar(player, -44225, 79721, -3652, 2);
				return "30600-05.htm";
			case 61:
				deleteAllRadar(player, type);
				showRadar(player, -44015, 79683, -3652, 2);
				return "30600-05.htm";
			case 62:
				deleteAllRadar(player, type);
				showRadar(player, 25856, 10832, -3724, 2);
				return "30600-05.htm";
			case 63:
				deleteAllRadar(player, type);
				showRadar(player, 12328, 14947, -4574, 2);
				return "30600-05.htm";
			case 64:
				deleteAllRadar(player, type);
				showRadar(player, 13081, 18444, -4573, 2);
				return "30600-05.htm";
			case 65:
				deleteAllRadar(player, type);
				showRadar(player, 12311, 17470, -4574, 2);
				return "30600-05.htm";
		}
		return "";
	}
	
	private String eventGuideDwarfGullin(int event, L2PcInstance player, int type) {
		switch (event) {
			case 10:
				return "30601-04.htm";
			case 11:
				return "30601-04a.htm";
			case 12:
				return "30601-04b.htm";
			case 13:
				return "30601-04c.htm";
			case 14:
				return "30601-04d.htm";
			case 15:
				return "30601-04e.htm";
			case 16:
				return "30601-04f.htm";
			case 17:
				return "30601-04g.htm";
			case 18:
				return "30601-04h.htm";
			case 31:
				deleteAllRadar(player, type);
				showRadar(player, 115072, -178176, -906, 2);
				return "30601-05.htm";
			case 32:
				deleteAllRadar(player, type);
				showRadar(player, 117847, -182339, -1537, 2);
				return "30601-05.htm";
			case 33:
				deleteAllRadar(player, type);
				showRadar(player, 116617, -184308, -1569, 2);
				return "30601-05.htm";
			case 34:
				deleteAllRadar(player, type);
				showRadar(player, 117826, -182576, -1537, 2);
				return "30601-05.htm";
			case 35:
				deleteAllRadar(player, type);
				showRadar(player, 116378, -184308, -1571, 2);
				return "30601-05.htm";
			case 36:
				deleteAllRadar(player, type);
				showRadar(player, 115183, -176728, -791, 2);
				return "30601-05.htm";
			case 37:
				deleteAllRadar(player, type);
				showRadar(player, 114969, -176752, -790, 2);
				return "30601-05.htm";
			case 38:
				deleteAllRadar(player, type);
				showRadar(player, 117366, -178725, -1118, 2);
				return "30601-05.htm";
			case 39:
				deleteAllRadar(player, type);
				showRadar(player, 117378, -178914, -1120, 2);
				return "30601-05.htm";
			case 40:
				deleteAllRadar(player, type);
				showRadar(player, 116226, -178529, -948, 2);
				return "30601-05.htm";
			case 41:
				deleteAllRadar(player, type);
				showRadar(player, 116190, -178441, -948, 2);
				return "30601-05.htm";
			case 42:
				deleteAllRadar(player, type);
				showRadar(player, 116016, -178615, -948, 2);
				return "30601-05.htm";
			case 43:
				deleteAllRadar(player, type);
				showRadar(player, 116190, -178615, -948, 2);
				return "30601-05.htm";
			case 44:
				deleteAllRadar(player, type);
				showRadar(player, 116103, -178407, -948, 2);
				return "30601-05.htm";
			case 45:
				deleteAllRadar(player, type);
				showRadar(player, 116103, -178653, -948, 2);
				return "30601-05.htm";
			case 46:
				deleteAllRadar(player, type);
				showRadar(player, 115468, -182446, -1434, 2);
				return "30601-05.htm";
			case 47:
				deleteAllRadar(player, type);
				showRadar(player, 115315, -182155, -1444, 2);
				return "30601-05.htm";
			case 48:
				deleteAllRadar(player, type);
				showRadar(player, 115271, -182692, -1445, 2);
				return "30601-05.htm";
			case 49:
				deleteAllRadar(player, type);
				showRadar(player, 115900, -177316, -915, 2);
				return "30601-05.htm";
			case 50:
				deleteAllRadar(player, type);
				showRadar(player, 116268, -177524, -914, 2);
				return "30601-05.htm";
			case 51:
				deleteAllRadar(player, type);
				showRadar(player, 115741, -181645, -1344, 2);
				return "30601-05.htm";
			case 52:
				deleteAllRadar(player, type);
				showRadar(player, 116192, -181072, -1344, 2);
				return "30601-05.htm";
			case 53:
				deleteAllRadar(player, type);
				showRadar(player, 115205, -180024, -870, 2);
				return "30601-05.htm";
			case 54:
				deleteAllRadar(player, type);
				showRadar(player, 114716, -180018, -871, 2);
				return "30601-05.htm";
			case 55:
				deleteAllRadar(player, type);
				showRadar(player, 114832, -179520, -871, 2);
				return "30601-05.htm";
			case 56:
				deleteAllRadar(player, type);
				showRadar(player, 115717, -183488, -1483, 2);
				return "30601-05.htm";
			case 57:
				deleteAllRadar(player, type);
				showRadar(player, 115618, -183265, -1483, 2);
				return "30601-05.htm";
			case 58:
				deleteAllRadar(player, type);
				showRadar(player, 114348, -178537, -813, 2);
				return "30601-05.htm";
			case 59:
				deleteAllRadar(player, type);
				showRadar(player, 114990, -177294, -854, 2);
				return "30601-05.htm";
			case 60:
				deleteAllRadar(player, type);
				showRadar(player, 114426, -178672, -812, 2);
				return "30601-05.htm";
			case 61:
				deleteAllRadar(player, type);
				showRadar(player, 114409, -178415, -812, 2);
				return "30601-05.htm";
			case 62:
				deleteAllRadar(player, type);
				showRadar(player, 117061, -181867, -1413, 2);
				return "30601-05.htm";
			case 63:
				deleteAllRadar(player, type);
				showRadar(player, 116164, -184029, -1507, 2);
				return "30601-05.htm";
			case 64:
				deleteAllRadar(player, type);
				showRadar(player, 115563, -182923, -1448, 2);
				return "30601-05.htm";
			case 65:
				deleteAllRadar(player, type);
				showRadar(player, 112656, -174864, -611, 2);
				return "30601-05.htm";
			case 66:
				deleteAllRadar(player, type);
				showRadar(player, 116852, -183595, -1566, 2);
				return "30601-05.htm";
		}
		return "";
	}
	
	private String eventGuideOrcTanai(int event, L2PcInstance player, int type) {
		switch (event) {
			case 10:
				return "30602-04.htm";
			case 11:
				return "30602-04a.htm";
			case 12:
				return "30602-04b.htm";
			case 13:
				return "30602-04c.htm";
			case 14:
				return "30602-04d.htm";
			case 15:
				return "30602-04e.htm";
			case 16:
				return "30602-04f.htm";
			case 17:
				return "30602-04g.htm";
			case 18:
				return "30602-04h.htm";
			case 19:
				return "30602-04i.htm";
			case 31:
				deleteAllRadar(player, type);
				showRadar(player, -45264, -112512, -235, 2);
				return "30602-05.htm";
			case 32:
				deleteAllRadar(player, type);
				showRadar(player, -46576, -117311, -242, 2);
				return "30602-05.htm";
			case 33:
				deleteAllRadar(player, type);
				showRadar(player, -47360, -113791, -237, 2);
				return "30602-05.htm";
			case 34:
				deleteAllRadar(player, type);
				showRadar(player, -47360, -113424, -235, 2);
				return "30602-05.htm";
			case 35:
				deleteAllRadar(player, type);
				showRadar(player, -45744, -117165, -236, 2);
				return "30602-05.htm";
			case 36:
				deleteAllRadar(player, type);
				showRadar(player, -46528, -109968, -250, 2);
				return "30602-05.htm";
			case 37:
				deleteAllRadar(player, type);
				showRadar(player, -45808, -110055, -255, 2);
				return "30602-05.htm";
			case 38:
				deleteAllRadar(player, type);
				showRadar(player, -45731, -113844, -237, 2);
				return "30602-05.htm";
			case 39:
				deleteAllRadar(player, type);
				showRadar(player, -45728, -113360, -237, 2);
				return "30602-05.htm";
			case 40:
				deleteAllRadar(player, type);
				showRadar(player, -45952, -114784, -199, 2);
				return "30602-05.htm";
			case 41:
				deleteAllRadar(player, type);
				showRadar(player, -45952, -114496, -199, 2);
				return "30602-05.htm";
			case 42:
				deleteAllRadar(player, type);
				showRadar(player, -45863, -112621, -200, 2);
				return "30602-05.htm";
			case 43:
				deleteAllRadar(player, type);
				showRadar(player, -45864, -112540, -199, 2);
				return "30602-05.htm";
			case 44:
				deleteAllRadar(player, type);
				showRadar(player, -43264, -112532, -220, 2);
				return "30602-05.htm";
			case 45:
				deleteAllRadar(player, type);
				showRadar(player, -43910, -115518, -194, 2);
				return "30602-05.htm";
			case 46:
				deleteAllRadar(player, type);
				showRadar(player, -43950, -115457, -194, 2);
				return "30602-05.htm";
			case 47:
				deleteAllRadar(player, type);
				showRadar(player, -44416, -111486, -222, 2);
				return "30602-05.htm";
			case 48:
				deleteAllRadar(player, type);
				showRadar(player, -43926, -111794, -222, 2);
				return "30602-05.htm";
			case 49:
				deleteAllRadar(player, type);
				showRadar(player, -43109, -113770, -221, 2);
				return "30602-05.htm";
			case 50:
				deleteAllRadar(player, type);
				showRadar(player, -43114, -113404, -221, 2);
				return "30602-05.htm";
			case 51:
				deleteAllRadar(player, type);
				showRadar(player, -46768, -113610, -3, 2);
				return "30602-05.htm";
			case 52:
				deleteAllRadar(player, type);
				showRadar(player, -46802, -114011, -112, 2);
				return "30602-05.htm";
			case 53:
				deleteAllRadar(player, type);
				showRadar(player, -46247, -113866, -21, 2);
				return "30602-05.htm";
			case 54:
				deleteAllRadar(player, type);
				showRadar(player, -46808, -113184, -112, 2);
				return "30602-05.htm";
			case 55:
				deleteAllRadar(player, type);
				showRadar(player, -45328, -114736, -237, 2);
				return "30602-05.htm";
			case 56:
				deleteAllRadar(player, type);
				showRadar(player, -44624, -111873, -238, 2);
				return "30602-05.htm";
		}
		return "";
	}
	
	private String eventGuideKrenisk(int event, L2PcInstance player, int type) {
		switch (event) {
			case 10:
				return "32135-04.htm";
			case 11:
				return "32135-04a.htm";
			case 12:
				return "32135-04b.htm";
			case 13:
				return "32135-04c.htm";
			case 14:
				return "32135-04d.htm";
			case 15:
				return "32135-04e.htm";
			case 16:
				return "32135-04f.htm";
			case 17:
				return "32135-04g.htm";
			case 18:
				return "32135-04h.htm";
			case 19:
				return "32135-04i.htm";
			case 20:
				return "32135-04j.htm";
			case 21:
				return "32135-04k.htm";
			case 22:
				return "32135-04l.htm";
			case 31:
				deleteAllRadar(player, type);
				showRadar(player, -116879, 46591, 380, 2);
				return "32135-05.htm";
			case 32:
				deleteAllRadar(player, type);
				showRadar(player, -119378, 49242, 22, 2);
				return "32135-05.htm";
			case 33:
				deleteAllRadar(player, type);
				showRadar(player, -119774, 49245, 22, 2);
				return "32135-05.htm";
			case 34:
				deleteAllRadar(player, type);
				showRadar(player, -119830, 51860, -787, 2);
				return "32135-05.htm";
			case 35:
				deleteAllRadar(player, type);
				showRadar(player, -119362, 51862, -780, 2);
				return "32135-05.htm";
			case 36:
				deleteAllRadar(player, type);
				showRadar(player, -112872, 46850, 68, 2);
				return "32135-05.htm";
			case 37:
				deleteAllRadar(player, type);
				showRadar(player, -112352, 47392, 68, 2);
				return "32135-05.htm";
			case 38:
				deleteAllRadar(player, type);
				showRadar(player, -110544, 49040, -1124, 2);
				return "32135-05.htm";
			case 39:
				deleteAllRadar(player, type);
				showRadar(player, -110536, 45162, -1132, 2);
				return "32135-05.htm";
			case 40:
				deleteAllRadar(player, type);
				showRadar(player, -115888, 43568, 524, 2);
				return "32135-05.htm";
			case 41:
				deleteAllRadar(player, type);
				showRadar(player, -115486, 43567, 525, 2);
				return "32135-05.htm";
			case 42:
				deleteAllRadar(player, type);
				showRadar(player, -116920, 47792, 464, 2);
				return "32135-05.htm";
			case 43:
				deleteAllRadar(player, type);
				showRadar(player, -116749, 48077, 462, 2);
				return "32135-05.htm";
			case 44:
				deleteAllRadar(player, type);
				showRadar(player, -117153, 48075, 463, 2);
				return "32135-05.htm";
			case 45:
				deleteAllRadar(player, type);
				showRadar(player, -119104, 43280, 559, 2);
				return "32135-05.htm";
			case 46:
				deleteAllRadar(player, type);
				showRadar(player, -119104, 43152, 559, 2);
				return "32135-05.htm";
			case 47:
				deleteAllRadar(player, type);
				showRadar(player, -117056, 43168, 559, 2);
				return "32135-05.htm";
			case 48:
				deleteAllRadar(player, type);
				showRadar(player, -117060, 43296, 559, 2);
				return "32135-05.htm";
			case 49:
				deleteAllRadar(player, type);
				showRadar(player, -118192, 42384, 838, 2);
				return "32135-05.htm";
			case 50:
				deleteAllRadar(player, type);
				showRadar(player, -117968, 42384, 838, 2);
				return "32135-05.htm";
			case 51:
				deleteAllRadar(player, type);
				showRadar(player, -118132, 42788, 723, 2);
				return "32135-05.htm";
			case 52:
				deleteAllRadar(player, type);
				showRadar(player, -118028, 42788, 720, 2);
				return "32135-05.htm";
			case 53:
				deleteAllRadar(player, type);
				showRadar(player, -114802, 44821, 524, 2);
				return "32135-05.htm";
			case 54:
				deleteAllRadar(player, type);
				showRadar(player, -114975, 44658, 524, 2);
				return "32135-05.htm";
			case 55:
				deleteAllRadar(player, type);
				showRadar(player, -114801, 45031, 525, 2);
				return "32135-05.htm";
			case 56:
				deleteAllRadar(player, type);
				showRadar(player, -120432, 45296, 416, 2);
				return "32135-05.htm";
			case 57:
				deleteAllRadar(player, type);
				showRadar(player, -120706, 45079, 419, 2);
				return "32135-05.htm";
			case 58:
				deleteAllRadar(player, type);
				showRadar(player, -120356, 45293, 416, 2);
				return "32135-05.htm";
			case 59:
				deleteAllRadar(player, type);
				showRadar(player, -120604, 44960, 423, 2);
				return "32135-05.htm";
			case 60:
				deleteAllRadar(player, type);
				showRadar(player, -120294, 46013, 384, 2);
				return "32135-05.htm";
			case 61:
				deleteAllRadar(player, type);
				showRadar(player, -120157, 45813, 355, 2);
				return "32135-05.htm";
			case 62:
				deleteAllRadar(player, type);
				showRadar(player, -120158, 46221, 354, 2);
				return "32135-05.htm";
			case 63:
				deleteAllRadar(player, type);
				showRadar(player, -120400, 46921, 415, 2);
				return "32135-05.htm";
			case 64:
				deleteAllRadar(player, type);
				showRadar(player, -120407, 46755, 423, 2);
				return "32135-05.htm";
			case 65:
				deleteAllRadar(player, type);
				showRadar(player, -120442, 47125, 422, 2);
				return "32135-05.htm";
			case 66:
				deleteAllRadar(player, type);
				showRadar(player, -118720, 48062, 473, 2);
				return "32135-05.htm";
			case 67:
				deleteAllRadar(player, type);
				showRadar(player, -118918, 47956, 474, 2);
				return "32135-05.htm";
			case 68:
				deleteAllRadar(player, type);
				showRadar(player, -118527, 47955, 473, 2);
				return "32135-05.htm";
			case 69:
				deleteAllRadar(player, type);
				showRadar(player, -117605, 48079, 472, 2);
				return "32135-05.htm";
			case 70:
				deleteAllRadar(player, type);
				showRadar(player, -117824, 48080, 476, 2);
				return "32135-05.htm";
			case 71:
				deleteAllRadar(player, type);
				showRadar(player, -118030, 47930, 465, 2);
				return "32135-05.htm";
			case 72:
				deleteAllRadar(player, type);
				showRadar(player, -119221, 46981, 380, 2);
				return "32135-05.htm";
			case 73:
				deleteAllRadar(player, type);
				showRadar(player, -118080, 42835, 720, 2);
				return "32135-05.htm";
		}
		return "";
	}
}