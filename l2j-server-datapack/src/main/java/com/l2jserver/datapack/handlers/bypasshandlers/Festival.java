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
package com.l2jserver.datapack.handlers.bypasshandlers;

import static com.l2jserver.gameserver.config.Configuration.sevenSigns;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.SevenSignsFestival;
import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Party.messageType;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2FestivalGuideInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.StringUtil;

public class Festival implements IBypassHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(Festival.class);
	
	private static final String[] COMMANDS = {
		"festival",
		"festivaldesc"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target) {
		if (!(target instanceof L2FestivalGuideInstance npc)) {
			return false;
		}
		
		try {
			final int val;
			if (command.toLowerCase().startsWith(COMMANDS[1])) {
				val = Integer.parseInt(command.substring(13));
				npc.showChatWindow(activeChar, val, null, true);
				return true;
			}
			
			final L2Party party;
			val = Integer.parseInt(command.substring(9));
			switch (val) {
				case 1: // Become a Participant
					// Check if the festival period is active, if not then don't allow registration.
					if (SevenSigns.getInstance().isSealValidationPeriod()) {
						npc.showChatWindow(activeChar, 2, "a", false);
						return true;
					}
					
					// Check if a festival is in progress, then don't allow registration yet.
					if (SevenSignsFestival.getInstance().isFestivalInitialized()) {
						activeChar.sendMessage("You cannot sign up while a festival is in progress.");
						return true;
					}
					
					// Check if the player is in a formed party already.
					if (!activeChar.isInParty()) {
						npc.showChatWindow(activeChar, 2, "b", false);
						return true;
					}
					
					party = activeChar.getParty();
					
					// Check if the player is the party leader.
					if (!party.isLeader(activeChar)) {
						npc.showChatWindow(activeChar, 2, "c", false);
						return true;
					}
					
					// Check to see if the party has at least 5 members.
					if (party.getMemberCount() < sevenSigns().getFestivalMinPlayer()) {
						npc.showChatWindow(activeChar, 2, "b", false);
						return true;
					}
					
					// Check if all the party members are in the required level range.
					if (party.getLevel() > SevenSignsFestival.getMaxLevelForFestival(npc.getFestivalType())) {
						npc.showChatWindow(activeChar, 2, "d", false);
						return true;
					}
					
					// Check to see if the player has already signed up
					if (activeChar.isFestivalParticipant()) {
						SevenSignsFestival.getInstance().setParticipants(npc.getFestivalOracle(), npc.getFestivalType(), party);
						npc.showChatWindow(activeChar, 2, "f", false);
						return true;
					}
					
					npc.showChatWindow(activeChar, 1, null, false);
					break;
				case 2: // Seal Stones
					final int stoneType = Integer.parseInt(command.substring(11));
					final int stoneCount = npc.getStoneCount(stoneType);
					if (stoneCount <= 0) {
						return false;
					}
					
					if (!activeChar.destroyItemByItemId("SevenSigns", stoneType, stoneCount, npc, true)) {
						return false;
					}
					
					SevenSignsFestival.getInstance().setParticipants(npc.getFestivalOracle(), npc.getFestivalType(), activeChar.getParty());
					SevenSignsFestival.getInstance().addAccumulatedBonus(npc.getFestivalType(), stoneType, stoneCount);
					
					npc.showChatWindow(activeChar, 2, "e", false);
					break;
				case 3: // Score Registration
					// Check if the festival period is active, if not then don't register the score.
					if (SevenSigns.getInstance().isSealValidationPeriod()) {
						npc.showChatWindow(activeChar, 3, "a", false);
						return true;
					}
					
					// Check if a festival is in progress, if it is don't register the score.
					if (SevenSignsFestival.getInstance().isFestivalInProgress()) {
						activeChar.sendMessage("You cannot register a score while a festival is in progress.");
						return true;
					}
					
					// Check if the player is in a party.
					if (!activeChar.isInParty()) {
						npc.showChatWindow(activeChar, 3, "b", false);
						return true;
					}
					
					final List<Integer> prevParticipants = SevenSignsFestival.getInstance().getPreviousParticipants(npc.getFestivalOracle(), npc.getFestivalType());
					
					// Check if there are any past participants.
					if ((prevParticipants == null) || prevParticipants.isEmpty() || !prevParticipants.contains(activeChar.getObjectId())) {
						npc.showChatWindow(activeChar, 3, "b", false);
						return true;
					}
					
					// Check if this player was the party leader in the festival.
					if (activeChar.getObjectId() != prevParticipants.get(0)) {
						npc.showChatWindow(activeChar, 3, "b", false);
						return true;
					}
					
					final L2ItemInstance bloodOfferings = activeChar.getInventory().getItemByItemId(SevenSignsFestival.FESTIVAL_OFFERING_ID);
					
					// Check if the player collected any blood offerings during the festival.
					if (bloodOfferings == null) {
						activeChar.sendMessage("You do not have any blood offerings to contribute.");
						return true;
					}
					
					final long offeringScore = bloodOfferings.getCount() * SevenSignsFestival.FESTIVAL_OFFERING_VALUE;
					if (!activeChar.destroyItem("SevenSigns", bloodOfferings, npc, false)) {
						return true;
					}
					
					final boolean isHighestScore = SevenSignsFestival.getInstance().setFinalScore(activeChar, npc.getFestivalOracle(), npc.getFestivalType(), offeringScore);
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CONTRIB_SCORE_INCREASED_S1);
					sm.addLong(offeringScore);
					activeChar.sendPacket(sm);
					
					if (isHighestScore) {
						npc.showChatWindow(activeChar, 3, "c", false);
					} else {
						npc.showChatWindow(activeChar, 3, "d", false);
					}
					break;
				case 4: // Current High Scores
					final StringBuilder strBuffer = StringUtil.startAppend(500, "<html><body>Festival Guide:<br>These are the top scores of the week, for the ");
					
					final StatsSet dawnData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DAWN, npc.getFestivalType());
					final StatsSet duskData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DUSK, npc.getFestivalType());
					final StatsSet overallData = SevenSignsFestival.getInstance().getOverallHighestScoreData(npc.getFestivalType());
					
					final int dawnScore = dawnData.getInt("score");
					final int duskScore = duskData.getInt("score");
					int overallScore = 0;
					
					// If no data is returned, assume there is no record, or all scores are 0.
					if (overallData != null) {
						overallScore = overallData.getInt("score");
					}
					
					StringUtil.append(strBuffer, SevenSignsFestival.getFestivalName(npc.getFestivalType()), " festival.<br>");
					
					if (dawnScore > 0) {
						StringUtil.append(strBuffer, "Dawn: ", calculateDate(dawnData.getString("date")), ". Score ", String.valueOf(dawnScore), "<br>", dawnData.getString("members"), "<br>");
					} else {
						strBuffer.append("Dawn: No record exists. Score 0<br>");
					}
					
					if (duskScore > 0) {
						StringUtil.append(strBuffer, "Dusk: ", calculateDate(duskData.getString("date")), ". Score ", String.valueOf(duskScore), "<br>", duskData.getString("members"), "<br>");
					} else {
						strBuffer.append("Dusk: No record exists. Score 0<br>");
					}
					
					if ((overallScore > 0) && (overallData != null)) {
						final String cabalStr;
						if (overallData.getString("cabal").equals("dawn")) {
							cabalStr = "Children of Dawn";
						} else {
							cabalStr = "Children of Dusk";
						}
						
						StringUtil.append(strBuffer, "Consecutive top scores: ", calculateDate(overallData.getString("date")), ". Score ", String.valueOf(overallScore), "<br>Affilated side: ", cabalStr, "<br>", overallData.getString("members"), "<br>");
					} else {
						strBuffer.append("Consecutive top scores: No record exists. Score 0<br>");
					}
					
					StringUtil.append(strBuffer, "<a action=\"bypass -h npc_", String.valueOf(npc.getObjectId()), "_Chat 0\">Go back.</a></body></html>");
					
					final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
					html.setHtml(strBuffer.toString());
					activeChar.sendPacket(html);
					break;
				case 8: // Increase the Festival Challenge
					if (!activeChar.isInParty()) {
						return true;
					}
					
					if (!SevenSignsFestival.getInstance().isFestivalInProgress()) {
						return true;
					}
					
					party = activeChar.getParty();
					if (!party.isLeader(activeChar)) {
						npc.showChatWindow(activeChar, 8, "a", false);
						return true;
					}
					
					if (SevenSignsFestival.getInstance().increaseChallenge(npc.getFestivalOracle(), npc.getFestivalType())) {
						npc.showChatWindow(activeChar, 8, "b", false);
					} else {
						npc.showChatWindow(activeChar, 8, "c", false);
					}
					break;
				case 9: // Leave the Festival
					if (!activeChar.isInParty()) {
						return true;
					}
					
					party = activeChar.getParty();
					if (party.isLeader(activeChar)) {
						SevenSignsFestival.getInstance().updateParticipants(activeChar, null);
					} else {
						if (party.getMemberCount() > sevenSigns().getFestivalMinPlayer()) {
							party.removePartyMember(activeChar, messageType.Expelled);
						} else {
							activeChar.sendMessage("Only the party leader can leave a festival when a party has minimum number of members.");
						}
					}
					break;
				case 10:
					activeChar.teleToLocation(-114796, -179334, -6752);
					npc.showChatWindow(activeChar, 10, "a", false);
					break;
				case 11:
					final var q505 = QuestManager.getInstance().getQuest(505);
					if (q505 != null) {
						if (activeChar.hasQuestState(q505.getName())) {
							final var qs505 = q505.getQuestState(activeChar, true);
							qs505.exitQuest(true, false);
						}
					}
					
					final L2ItemInstance bloodOffering = activeChar.getInventory().getItemByItemId(SevenSignsFestival.FESTIVAL_OFFERING_ID);
					if (bloodOffering != null) {
						activeChar.destroyItem("SevenSigns", bloodOffering, npc, false);
					}
					
					final var q255 = QuestManager.getInstance().getQuest(255);
					final var qs255 = q255.getQuestState(activeChar, true);
					
					int i0 = qs255.getMemoStateEx(1);
					int i1 = i0 % 10000;
					int i2 = ((i0 - i1) + 5) / 10000;
					
					final var partType = npc.getTemplate().getParameters().getString("part_type", null);
					
					if (i1 >= 95 && i1 < 195) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(-80542, 150315, -3040);
								}
								case 1 -> {
									activeChar.teleToLocation(-80602, 150352, -3040);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(-82340, 151575, -3120);
								}
								case 1 -> {
									activeChar.teleToLocation(-82392, 151584, -3120);
								}
							}
						}
						return true;
					}
					if (i1 >= 295 && i1 < 395) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(16320, 142915, -2696);
								}
								case 1 -> {
									activeChar.teleToLocation(16383, 142899, -2696);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(18501, 144673, -3056);
								}
								case 1 -> {
									activeChar.teleToLocation(18523, 144624, -3056);
								}
							}
						}
						return true;
					}
					if (i1 >= 395 && i1 < 495) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(83312, 149236, -3400);
								}
								case 1 -> {
									activeChar.teleToLocation(83313, 149304, -3400);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(81572, 148580, -3464);
								}
								case 1 -> {
									activeChar.teleToLocation(81571, 148641, -3464);
								}
							}
						}
						return true;
					}
					if (i1 >= 495 && i1 < 595) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(111359, 220959, -3544);
								}
								case 1 -> {
									activeChar.teleToLocation(111411, 220955, -3544);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(112441, 220149, -3544);
								}
								case 1 -> {
									activeChar.teleToLocation(112452, 220204, -3592);
								}
							}
						}
						return true;
					}
					if (i1 >= 595 && i1 < 695) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(83057, 53983, -1488);
								}
								case 1 -> {
									activeChar.teleToLocation(83069, 54043, -1488);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(82842, 54613, -1520);
								}
								case 1 -> {
									activeChar.teleToLocation(82791, 54616, -1520);
								}
							}
						}
						return true;
					}
					if (i1 >= 695 && i1 < 795) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(146955, 26690, -2200);
								}
								case 1 -> {
									activeChar.teleToLocation(147015, 26689, -2200);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(147528, 28899, -2264);
								}
								case 1 -> {
									activeChar.teleToLocation(147528, 28962, -2264);
								}
							}
						}
						return true;
					}
					if (i1 >= 795 && i1 < 895) {
						if (partType.equalsIgnoreCase("DAWN")) {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(115206, 74775, -2600);
								}
								case 1 -> {
									activeChar.teleToLocation(115174, 74722, -2608);
								}
							}
						} else {
							switch (Rnd.get(1)) {
								case 0 -> {
									activeChar.teleToLocation(116651, 77512, -2688);
								}
								case 1 -> {
									activeChar.teleToLocation(116597, 77539, -2688);
								}
							}
						}
						return true;
					}
					if (i1 >= 995 && i1 < 1095) {
						if (partType.equalsIgnoreCase("DAWN")) {
							activeChar.teleToLocation(148326, -55533, -2776);
						} else {
							activeChar.teleToLocation(149968, -56645, -2976);
						}
						return true;
					}
					if (i1 >= 1095 && i1 < 1195) {
						if (partType.equalsIgnoreCase("DAWN")) {
							activeChar.teleToLocation(45605, -50360, -792);
						} else {
							activeChar.teleToLocation(44505, -48331, -792);
						}
						return true;
					}
					if (i1 >= 1195 && i1 < 1295) {
						if (partType.equalsIgnoreCase("DAWN")) {
							activeChar.teleToLocation(86730, -143148, -1336);
						} else {
							activeChar.teleToLocation(85048, -142046, -1536);
						}
						return true;
					}
					
					if (i2 == 1) {
						activeChar.teleToLocation(-41443, 210030, -5080);
					} else {
						if (i2 == 2) {
							activeChar.teleToLocation(-53034, -250421, -7935);
						} else {
							if (i2 == 3) {
								activeChar.teleToLocation(45160, 123605, -5408);
							} else {
								if (i2 == 4) {
									activeChar.teleToLocation(46488, 170184, -4976);
								} else {
									if (i2 == 5) {
										activeChar.teleToLocation(111521, 173905, -5432);
									} else {
										if (i2 == 6) {
											activeChar.teleToLocation(-20395, -250930, -8191);
										} else {
											if (i2 == 7) {
												activeChar.teleToLocation(-21482, 77253, -5168);
											} else {
												if (i2 == 8) {
													activeChar.teleToLocation(140688, 79565, -5424);
												} else {
													if (i2 == 9) {
														activeChar.teleToLocation(-52007, 78986, -4736);
													} else {
														if (i2 == 10) {
															activeChar.teleToLocation(118547, 132669, -4824);
														} else {
															if (i2 == 11) {
																activeChar.teleToLocation(172562, -17730, -4896);
															} else {
																if (i2 == 12) {
																	activeChar.teleToLocation(83344, 209110, -5432);
																} else {
																	if (i2 == 13) {
																		activeChar.teleToLocation(-19154, 13415, -4896);
																	} else {
																		if (i2 == 14) {
																			activeChar.teleToLocation(12747, -248614, -9607);
																		} else {
																			if (i1 < 95) {
																				npc.showChatWindow(activeChar, 11, "a", false);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					
					break;
				case 0: // Distribute Accumulated Bonus
					if (!SevenSigns.getInstance().isSealValidationPeriod()) {
						activeChar.sendMessage("Bonuses cannot be paid during the competition period.");
						return true;
					}
					
					if (SevenSignsFestival.getInstance().distribAccumulatedBonus(activeChar) > 0) {
						npc.showChatWindow(activeChar, 0, "a", false);
					} else {
						npc.showChatWindow(activeChar, 0, "b", false);
					}
					break;
				default:
					npc.showChatWindow(activeChar, val, null, false);
			}
			return true;
		} catch (Exception ex) {
			LOG.warn("Exception using bypass!", ex);
		}
		return false;
	}
	
	private final String calculateDate(String milliFromEpoch) {
		long numMillis = Long.parseLong(milliFromEpoch);
		Calendar calCalc = Calendar.getInstance();
		calCalc.setTimeInMillis(numMillis);
		return calCalc.get(Calendar.YEAR) + "/" + calCalc.get(Calendar.MONTH) + "/" + calCalc.get(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	public String[] getBypassList() {
		return COMMANDS;
	}
}
