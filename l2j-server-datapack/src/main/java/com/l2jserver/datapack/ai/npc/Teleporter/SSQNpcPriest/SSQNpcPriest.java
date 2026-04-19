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
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest;

import static com.l2jserver.gameserver.config.Configuration.sevenSigns;
import static com.l2jserver.gameserver.network.SystemMessageId.CONTRIB_SCORE_EXCEEDED;
import static com.l2jserver.gameserver.network.SystemMessageId.CONTRIB_SCORE_INCREASED_S1;
import static com.l2jserver.gameserver.network.SystemMessageId.FIGHT_FOR_AVARICE;
import static com.l2jserver.gameserver.network.SystemMessageId.FIGHT_FOR_GNOSIS;
import static com.l2jserver.gameserver.network.SystemMessageId.FIGHT_FOR_STRIFE;
import static com.l2jserver.gameserver.network.SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT;
import static com.l2jserver.gameserver.network.SystemMessageId.SEAL_STONES_ONLY_WHILE_QUEST;
import static com.l2jserver.gameserver.network.SystemMessageId.SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION;
import static com.l2jserver.gameserver.network.SystemMessageId.SEVENSIGNS_PARTECIPATION_DAWN;
import static com.l2jserver.gameserver.network.SystemMessageId.SEVENSIGNS_PARTECIPATION_DUSK;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.enums.CategoryType;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class SSQNpcPriest extends AbstractNpcAI {
	
	public static final String fnPath = "data/html/seven_signs/";
	
	private static final String szName = "ssq_npc_priest";
	private static final String QUEST_ID = "q0506";
	
	public static final int Q00255_TUTORIAL = 255;
	public static final int Q00505_BLOODOFFERING = 505;
	
	public static final int BLOOD_OF_OFFERING = 5901;
	private static final int THE_LORD_OF_MANORS_AGREEMENT = 5708;
	private static final int THE_LORD_OF_MANORS_AGREEMENT_NEW = 6388;
	
	public static final double WEIGHT_LIMIT = 0.80;
	public static final double QUEST_LIMIT = 0.90;
	public static final int MAX_QUEST_COUNT = 40;
	
	private static final int MS_ASK_SSQ_CHAT = 506;
	private static final int MS_ASK_CONTRIBUTE_BLUE_STONES_DAWN = -201;
	private static final int MS_ASK_CONTRIBUTE_GREEN_STONES_DAWN = -202;
	private static final int MS_ASK_CONTRIBUTE_RED_STONES_DAWN = -203;
	private static final int MS_ASK_CONTRIBUTE_BLUE_STONES_DUSK = -204;
	private static final int MS_ASK_CONTRIBUTE_GREEN_STONES_DUSK = -205;
	private static final int MS_ASK_CONTRIBUTE_RED_STONES_DUSK = -206;
	private static final int MS_ASK_EXCHANGE_BLUE_STONES_DAWN = -207;
	private static final int MS_ASK_EXCHANGE_GREEN_STONES_DAWN = -208;
	private static final int MS_ASK_EXCHANGE_RED_STONES_DAWN = -209;
	private static final int MS_ASK_EXCHANGE_BLUE_STONES_DUSK = -210;
	private static final int MS_ASK_EXCHANGE_GREEN_STONES_DUSK = -211;
	private static final int MS_ASK_EXCHANGE_RED_STONES_DUSK = -212;
	private static final int MS_ASK_BUY_CONSUMABLE = -303;
	
	private static final int MS_REPLY_PARTICIPATION_REQUEST_DAWN = 0;
	private static final int MS_REPLY_PARTICIPATION_REQUEST_DUSK = 38;
	private static final int MS_REPLY_PURCHASE_RECORD_DAWN = 1;
	private static final int MS_REPLY_PURCHASE_RECORD_DUSK = 39;
	private static final int MS_REPLY_CONTRIBUTION_REQUEST_DAWN = 2;
	private static final int MS_REPLY_CONTRIBUTION_REQUEST_DUSK = 40;
	private static final int MS_REPLY_SEAL_CHOOSE_DAWN = 3;
	private static final int MS_REPLY_SEAL_CHOOSE_DUSK = 41;
	private static final int MS_REPLY_FIGHT_AVARICE_DAWN = 23;
	private static final int MS_REPLY_FIGHT_AVARICE_DUSK = 57;
	private static final int MS_REPLY_FIGHT_GNOSIS_DAWN = 24;
	private static final int MS_REPLY_FIGHT_GNOSIS_DUSK = 58;
	private static final int MS_REPLY_FIGHT_STRIFE_DAWN = 25;
	private static final int MS_REPLY_FIGHT_STRIFE_DUSK = 59;
	private static final int MS_REPLY_BUY_RECORD_DAWN = 26;
	private static final int MS_REPLY_BUY_RECORD_DUSK = 60;
	private static final int MS_REPLY_BLUE_STONES_CONTRIBUTE_REQUEST_DAWN = 27;
	private static final int MS_REPLY_GREEN_STONES_CONTRIBUTE_REQUEST_DAWN = 28;
	private static final int MS_REPLY_RED_STONES_CONTRIBUTE_REQUEST_DAWN = 29;
	private static final int MS_REPLY_BLUE_STONES_CONTRIBUTE_REQUEST_DUSK = 61;
	private static final int MS_REPLY_GREEN_STONES_CONTRIBUTE_REQUEST_DUSK = 62;
	private static final int MS_REPLY_RED_STONES_CONTRIBUTE_REQUEST_DUSK = 63;
	private static final int MS_REPLY_RECEIVE_REWARD_DAWN1 = 32;
	private static final int MS_REPLY_RECEIVE_REWARD_DAWN2 = 36;
	private static final int MS_REPLY_RECEIVE_REWARD_DUSK1 = 66;
	private static final int MS_REPLY_RECEIVE_REWARD_DUSK2 = 70;
	private static final int MS_REPLY_CONTRIBUTE_SEAL_STONES_REQUEST_DAWN = 71;
	private static final int MS_REPLY_CONTRIBUTE_SEAL_STONES_REQUEST_DUSK = 73;
	private static final int MS_REPLY_CONTRIBUTE_ALL_STONES_DAWN = 74;
	private static final int MS_REPLY_CONTRIBUTE_ALL_STONES_DUSK = 72;
	private static final int MS_REPLY_EXCHANGE_SEAL_STONES_REQUEST_DAWN = 80;
	private static final int MS_REPLY_EXCHANGE_BLUE_STONES_REQUEST_DAWN = 82;
	private static final int MS_REPLY_EXCHANGE_GREEN_STONES_REQUEST_DAWN = 83;
	private static final int MS_REPLY_EXCHANGE_RED_STONES_REQUEST_DAWN = 84;
	private static final int MS_REPLY_EXCHANGE_ALL_STONES_REQUEST_DAWN = 85;
	private static final int MS_REPLY_EXCHANGE_SEAL_STONES_REQUEST_DUSK = 86;
	private static final int MS_REPLY_EXCHANGE_BLUE_STONES_REQUEST_DUSK = 87;
	private static final int MS_REPLY_EXCHANGE_GREEN_STONES_REQUEST_DUSK = 88;
	private static final int MS_REPLY_EXCHANGE_RED_STONES_REQUEST_DUSK = 89;
	private static final int MS_REPLY_EXCHANGE_ALL_STONES_REQUEST_DUSK = 90;
	private static final int MS_REPLY_PAY_PARTITIPATION_FEE = 97;
	
	protected TelPosList[] position;
	protected TelPosList[] positionCompetition;
	
	public SSQNpcPriest(int npcId) {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
		bindTeleportRequest(npcId);
	}
	
	public record TelPosList(int locId, Location loc, long ammount, int type) {
		public TelPosList(int locId, Location loc, long ammount) {
			this(locId, loc, ammount, 0);
		}
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var ss = SevenSigns.getInstance();
		final var ssqPart = ss.getPlayerCabal(talker.getObjectId());
		final var ssqWinner = ss.getCabalHighestScore();
		
		final var ssqPriestType = npc.getTemplate().getParameters().getInt("ssq_priest_type", 0);
		
		switch (ask) {
			case MS_ASK_SSQ_CHAT -> {
				switch (reply) {
					case MS_REPLY_PARTICIPATION_REQUEST_DAWN, MS_REPLY_PARTICIPATION_REQUEST_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_02.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_36.htm");
						}
					}
					case MS_REPLY_PURCHASE_RECORD_DAWN, MS_REPLY_PURCHASE_RECORD_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_16.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_49.htm");
						}
					}
					case MS_REPLY_CONTRIBUTION_REQUEST_DAWN, MS_REPLY_CONTRIBUTION_REQUEST_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							if (ssqPart == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_19.htm");
							} else {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_20.htm");
							}
						} else {
							if (ssqPart == SevenSigns.CABAL_DUSK) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_52.htm");
							} else {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_53.htm");
							}
						}
					}
					case MS_REPLY_SEAL_CHOOSE_DAWN, MS_REPLY_SEAL_CHOOSE_DUSK -> {
						switch (ssqPart) {
							case SevenSigns.CABAL_DAWN -> {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
								return;
							}
							case SevenSigns.CABAL_DUSK -> {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
								return;
							}
						}
						if (talker.isInCategory(CategoryType.FIRST_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FIRST_CLASS_GROUP)) {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_03.htm");
							} else {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_37.htm");
							}
							return;
						}
						if (talker.isInCategory(CategoryType.SECOND_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_SECOND_CLASS_GROUP)) {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_04.htm");
							} else {
								if (ss.isJoinableToDawn(talker)) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_39a.htm");
								} else {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_38.htm");
								}
							}
							return;
						}
						if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								if (ss.isJoinableToDawn(talker)) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_05.htm");
									return;
								}
								if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07.htm");
									return;
								}
								
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07a.htm");
								return;
							}
							
							if (ss.isJoinableToDawn(talker)) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_39.htm");
							} else {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_40.htm");
							}
						}
					}
					case 4, 5, 9, 14, 15, 20, 21, 22, 30, 31, 33, 35, 42, 43, 48, 53, 54, 55, 56, 64, 67, 69 -> { // back
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							if (ss.isCompetitionPeriod() && ((ssqPart == SevenSigns.CABAL_DAWN) || (ssqPart == SevenSigns.CABAL_NULL))) {
								showPage(talker, fnPath + szName + "001.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DAWN) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) == SevenSigns.CABAL_DAWN) && (ssqPart == SevenSigns.CABAL_DAWN)) {
								showPage(talker, fnPath + szName + "026.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DAWN) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) != SevenSigns.CABAL_DAWN) && (ssqPart == SevenSigns.CABAL_DAWN)) {
								showPage(talker, fnPath + szName + "029.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DUSK) && ((ssqPart == SevenSigns.CABAL_DAWN) || (ssqPart == SevenSigns.CABAL_NULL))) {
								showPage(talker, fnPath + szName + "032.htm");
								return;
							}
							
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_NULL) && ((ssqPart == SevenSigns.CABAL_DAWN) || (ssqPart == SevenSigns.CABAL_NULL))) {
								showPage(talker, fnPath + szName + "033.htm");
								return;
							}
							if (ssqPart == SevenSigns.CABAL_DUSK) {
								showPage(talker, fnPath + szName + "034.htm");
								return;
							}
							if (ss.isInitializationPeriod()) {
								showPage(talker, fnPath + szName + "072.htm");
								return;
							}
							if (ss.isCompResultsPeriod()) {
								showPage(talker, fnPath + szName + "085.htm");
							} else {
								if (ss.isSealValidationPeriod() && (ssqPart != SevenSigns.CABAL_DAWN)) {
									showPage(talker, fnPath + szName + "083.htm");
								}
							}
						} else {
							if (ss.isCompetitionPeriod() && ((ssqPart == SevenSigns.CABAL_DUSK) || (ssqPart == SevenSigns.CABAL_NULL))) {
								showPage(talker, fnPath + szName + "035.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DUSK) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) == SevenSigns.CABAL_DUSK) && (ssqPart == SevenSigns.CABAL_DUSK)) {
								showPage(talker, fnPath + szName + "059.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DUSK) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) != SevenSigns.CABAL_DUSK) && (ssqPart == SevenSigns.CABAL_DUSK)) {
								showPage(talker, fnPath + szName + "062.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DAWN) && ((ssqPart == SevenSigns.CABAL_DUSK) || (ssqPart == SevenSigns.CABAL_NULL))) {
								showPage(talker, fnPath + szName + "065.htm");
								return;
							}
							if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_NULL) && ((ssqPart == SevenSigns.CABAL_DUSK) || (ssqPart == SevenSigns.CABAL_NULL))) {
								showPage(talker, fnPath + szName + "066.htm");
								return;
							}
							if (ssqPart == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "067.htm");
								return;
							}
							if (ss.isInitializationPeriod()) {
								showPage(talker, fnPath + szName + "076.htm");
								return;
							}
							if (ss.isCompResultsPeriod()) {
								showPage(talker, fnPath + szName + "086.htm");
							} else {
								if (ss.isSealValidationPeriod() && (ssqPart != SevenSigns.CABAL_DUSK)) {
									showPage(talker, fnPath + szName + "084.htm");
								}
							}
						}
					}
					case 6, 10, 16, 44, 49 -> { // seal of avarice
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_10.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_43.htm");
						}
					}
					case 7, 11, 17, 45, 50 -> { // seal of gnosis
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_11.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_44.htm");
						}
					}
					case 8, 12, 18, 46, 51 -> { // seal of strife
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_12.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_45.htm");
						}
					}
					case 13, 19, 47, 52 -> { // status of seals
						String html;
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_09.htm");
						} else {
							html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_42.htm");
						}
						
						if (!ss.isSealValidationPeriod()) {
							html = html.replace("<?seal_avarice?>", "<fstring>" + 1000302 + "</fstring>");
						} else {
							switch (ss.getSealOwner(SevenSigns.SEAL_AVARICE)) {
								case SevenSigns.CABAL_NULL -> {
									html = html.replace("<?seal_avarice?>", "<fstring>" + 1000302 + "</fstring>");
								}
								case SevenSigns.CABAL_DAWN -> {
									html = html.replace("<?seal_avarice?>", "<fstring>" + 1000300 + "</fstring>");
								}
								case SevenSigns.CABAL_DUSK -> {
									html = html.replace("<?seal_avarice?>", "<fstring>" + 1000301 + "</fstring>");
								}
							}
						}
						
						if (!ss.isSealValidationPeriod()) {
							html = html.replace("<?seal_revelation?>", "<fstring>" + 1000302 + "</fstring>");
						} else {
							switch (ss.getSealOwner(SevenSigns.SEAL_GNOSIS)) {
								case SevenSigns.CABAL_NULL -> {
									html = html.replace("<?seal_revelation?>", "<fstring>" + 1000302 + "</fstring>");
								}
								case SevenSigns.CABAL_DAWN -> {
									html = html.replace("<?seal_revelation?>", "<fstring>" + 1000300 + "</fstring>");
								}
								case SevenSigns.CABAL_DUSK -> {
									html = html.replace("<?seal_revelation?>", "<fstring>" + 1000301 + "</fstring>");
								}
							}
						}
						
						if (!ss.isSealValidationPeriod()) {
							html = html.replace("<?seal_strife?>", "<fstring>" + 1000302 + "</fstring>");
						} else {
							switch (ss.getSealOwner(SevenSigns.SEAL_STRIFE)) {
								case SevenSigns.CABAL_NULL -> {
									html = html.replace("<?seal_strife?>", "<fstring>" + 1000302 + "</fstring>");
								}
								case SevenSigns.CABAL_DAWN -> {
									html = html.replace("<?seal_strife?>", "<fstring>" + 1000300 + "</fstring>");
								}
								case SevenSigns.CABAL_DUSK -> {
									html = html.replace("<?seal_strife?>", "<fstring>" + 1000301 + "</fstring>");
								}
							}
						}
						
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_FIGHT_AVARICE_DAWN, MS_REPLY_FIGHT_AVARICE_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (ss.getSSQPrevWinner() == SevenSigns.CABAL_DAWN) {
								if (!ss.isCompetitionPeriod()) {
									talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
									return;
								}
								
								var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
								var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
								var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
								if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DAWN) {
											showPage(talker, fnPath + "ssq_npc_priest091.htm");
										} else {
											showPage(talker, fnPath + "ssq_npc_priest092.htm");
										}
										return;
									}
								}
							}
							
							var i4 = 0;
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								switch (ssqPart) {
									case SevenSigns.CABAL_NULL -> {
										if (ssqPart == SevenSigns.CABAL_NULL) {
											if (ssqPriestType == SevenSigns.CABAL_DAWN) {
												if (!ss.isJoinableToDawn(talker)) {
													if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
														takeItems(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW, 1);
														i4 = 2;
													} else {
														if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
															takeItems(talker, Inventory.ADENA_ID, sevenSigns().getSevenSignsJoinDawnFee());
															i4 = 3;
														} else {
															showPage(talker, fnPath + szName + "_" + QUEST_ID + "_06.htm");
															return;
														}
													}
												} else {
													i4 = 1;
												}
											} else {
												if (ssqPriestType == SevenSigns.CABAL_DUSK) {
													i4 = 0;
												}
											}
										}
									}
									case SevenSigns.CABAL_DAWN -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
										return;
									}
									case SevenSigns.CABAL_DUSK -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
										return;
									}
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DAWN) {
									int i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									int i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									int i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									int i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_AVARICE, 2, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_AVARICE, 2, i3, i4) == 0) {
										return;
									}
								}
							} else {
								i4 = 0;
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DAWN) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_AVARICE, 1, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_AVARICE, 1, i3, i4) == 0) {
										return;
									}
								}
							}
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								talker.sendPacket(SEVENSIGNS_PARTECIPATION_DAWN);
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									talker.sendPacket(SEVENSIGNS_PARTECIPATION_DUSK);
								}
							}
							
							talker.sendPacket(FIGHT_FOR_AVARICE);
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_13.htm");
						} else {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (ss.getSSQPrevWinner() == SevenSigns.CABAL_DUSK) {
								if (!ss.isCompetitionPeriod()) {
									talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
									return;
								}
								
								var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
								var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
								var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
								if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DAWN) {
											showPage(talker, fnPath + "ssq_npc_priest091.htm");
										} else {
											showPage(talker, fnPath + "ssq_npc_priest092.htm");
										}
										return;
									}
								}
							}
							
							var i4 = 0;
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								switch (ssqPart) {
									case SevenSigns.CABAL_NULL -> {
										if (ssqPart == SevenSigns.CABAL_NULL) {
											if (ssqPriestType == SevenSigns.CABAL_DAWN) {
												if (!ss.isJoinableToDawn(talker)) {
													if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
														takeItems(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW, 1);
														i4 = 2;
													} else {
														if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
															takeItems(talker, Inventory.ADENA_ID, sevenSigns().getSevenSignsJoinDawnFee());
															i4 = 3;
														} else {
															showPage(talker, fnPath + szName + "_" + QUEST_ID + "_06.htm");
															return;
														}
													}
												} else {
													i4 = 1;
												}
											} else {
												if (ssqPriestType == SevenSigns.CABAL_DUSK) {
													i4 = 0;
												}
											}
										}
									}
									case SevenSigns.CABAL_DAWN -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
										return;
									}
									case SevenSigns.CABAL_DUSK -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
										return;
									}
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DUSK) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 1) + (i1 * 1)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_AVARICE, 2, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_AVARICE, 2, i3, i4) == 0) {
										return;
									}
								}
							} else {
								i4 = 0;
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DUSK) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 1) + (i1 * 1)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_AVARICE, 1, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_AVARICE, 1, i3, i4) == 0) {
										return;
									}
								}
							}
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								talker.sendPacket(SEVENSIGNS_PARTECIPATION_DAWN);
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									talker.sendPacket(SEVENSIGNS_PARTECIPATION_DUSK);
								}
							}
							
							talker.sendPacket(FIGHT_FOR_AVARICE);
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_46.htm");
						}
					}
					case MS_REPLY_FIGHT_GNOSIS_DAWN, MS_REPLY_FIGHT_GNOSIS_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (ss.getSSQPrevWinner() == SevenSigns.CABAL_DAWN) {
								if (!ss.isCompetitionPeriod()) {
									talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
									return;
								}
								
								var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
								var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
								var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
								if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DAWN) {
											showPage(talker, fnPath + "ssq_npc_priest091.htm");
										} else {
											showPage(talker, fnPath + "ssq_npc_priest092.htm");
										}
										return;
									}
								}
							}
							
							var i4 = 0;
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								switch (ssqPart) {
									case SevenSigns.CABAL_NULL -> {
										if (ssqPart == SevenSigns.CABAL_NULL) {
											if (ssqPriestType == SevenSigns.CABAL_DAWN) {
												if (!ss.isJoinableToDawn(talker)) {
													if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
														takeItems(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW, 1);
														i4 = 2;
													} else {
														if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
															takeItems(talker, Inventory.ADENA_ID, sevenSigns().getSevenSignsJoinDawnFee());
															i4 = 3;
														} else {
															showPage(talker, fnPath + szName + "_" + QUEST_ID + "_06.htm");
															return;
														}
													}
												} else {
													i4 = 1;
												}
											} else {
												if (ssqPriestType == SevenSigns.CABAL_DUSK) {
													i4 = 0;
												}
											}
										}
									}
									case SevenSigns.CABAL_DAWN -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
										return;
									}
									case SevenSigns.CABAL_DUSK -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
										return;
									}
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DAWN) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_GNOSIS, 2, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_GNOSIS, 2, i3, i4) == 0) {
										return;
									}
								}
							} else {
								i4 = 0;
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DAWN) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_GNOSIS, 1, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_GNOSIS, 1, i3, i4) == 0) {
										return;
									}
								}
							}
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								talker.sendPacket(SEVENSIGNS_PARTECIPATION_DAWN);
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									talker.sendPacket(SEVENSIGNS_PARTECIPATION_DUSK);
								}
							}
							
							talker.sendPacket(FIGHT_FOR_GNOSIS);
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_14.htm");
						} else {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (ss.getSSQPrevWinner() == SevenSigns.CABAL_DUSK) {
								if (!ss.isCompetitionPeriod()) {
									talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
									return;
								}
								
								var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
								var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
								var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
								if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DAWN) {
											showPage(talker, fnPath + "ssq_npc_priest091.htm");
										} else {
											showPage(talker, fnPath + "ssq_npc_priest092.htm");
										}
										return;
									}
								}
							}
							
							var i4 = 0;
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								switch (ssqPart) {
									case SevenSigns.CABAL_NULL -> {
										if (ssqPart == SevenSigns.CABAL_NULL) {
											if (ssqPriestType == SevenSigns.CABAL_DAWN) {
												if (!ss.isJoinableToDawn(talker)) {
													if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
														takeItems(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW, 1);
														i4 = 2;
													} else {
														if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
															takeItems(talker, Inventory.ADENA_ID, sevenSigns().getSevenSignsJoinDawnFee());
															i4 = 3;
														} else {
															showPage(talker, fnPath + szName + "_" + QUEST_ID + "_06.htm");
															return;
														}
													}
												} else {
													i4 = 1;
												}
											} else {
												if (ssqPriestType == SevenSigns.CABAL_DUSK) {
													i4 = 0;
												}
											}
										}
									}
									case SevenSigns.CABAL_DAWN -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
										return;
									}
									case SevenSigns.CABAL_DUSK -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
										return;
									}
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DUSK) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 1) + (i1 * 1)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_GNOSIS, 2, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_GNOSIS, 2, i3, i4) == 0) {
										return;
									}
								}
							} else {
								i4 = 0;
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DUSK) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 1) + (i1 * 1)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_GNOSIS, 1, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_GNOSIS, 1, i3, i4) == 0) {
										return;
									}
								}
							}
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								talker.sendPacket(SEVENSIGNS_PARTECIPATION_DAWN);
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									talker.sendPacket(SEVENSIGNS_PARTECIPATION_DUSK);
								}
							}
							
							talker.sendPacket(FIGHT_FOR_GNOSIS);
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_47.htm");
						}
					}
					case MS_REPLY_FIGHT_STRIFE_DAWN, MS_REPLY_FIGHT_STRIFE_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (ss.getSSQPrevWinner() == SevenSigns.CABAL_DAWN) {
								if (!ss.isCompetitionPeriod()) {
									talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
									return;
								}
								
								var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
								var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
								var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
								if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DAWN) {
											showPage(talker, fnPath + "ssq_npc_priest091.htm");
										} else {
											showPage(talker, fnPath + "ssq_npc_priest092.htm");
										}
										return;
									}
								}
							}
							
							var i4 = 0;
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								switch (ssqPart) {
									case SevenSigns.CABAL_NULL -> {
										if (ssqPart == SevenSigns.CABAL_NULL) {
											if (ssqPriestType == SevenSigns.CABAL_DAWN) {
												if (!ss.isJoinableToDawn(talker)) {
													if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
														takeItems(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW, 1);
														i4 = 2;
													} else {
														if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
															takeItems(talker, Inventory.ADENA_ID, sevenSigns().getSevenSignsJoinDawnFee());
															i4 = 3;
														} else {
															showPage(talker, fnPath + szName + "_" + QUEST_ID + "_06.htm");
															return;
														}
													}
												} else {
													i4 = 1;
												}
											} else {
												if (ssqPriestType == SevenSigns.CABAL_DUSK) {
													i4 = 0;
												}
											}
										}
									}
									case SevenSigns.CABAL_DAWN -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
										return;
									}
									case SevenSigns.CABAL_DUSK -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
										return;
									}
								}
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DAWN) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_STRIFE, 2, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_STRIFE, 2, i3, i4) == 0) {
										return;
									}
								}
							} else {
								i4 = 0;
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DAWN) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_STRIFE, 1, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DAWN, SevenSigns.SEAL_STRIFE, 1, i3, i4) == 0) {
										return;
									}
								}
							}
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								talker.sendPacket(SEVENSIGNS_PARTECIPATION_DAWN);
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									talker.sendPacket(SEVENSIGNS_PARTECIPATION_DUSK);
								}
							}
							
							talker.sendPacket(FIGHT_FOR_STRIFE);
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_15.htm");
						} else {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (ss.getSSQPrevWinner() == SevenSigns.CABAL_DUSK) {
								if (!ss.isCompetitionPeriod()) {
									talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
									return;
								}
								
								var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
								var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
								var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
								if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
									var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
									if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
										ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DAWN) {
											showPage(talker, fnPath + "ssq_npc_priest091.htm");
										} else {
											showPage(talker, fnPath + "ssq_npc_priest092.htm");
										}
										return;
									}
								}
							}
							
							var i4 = 0;
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.KAMAEL_FOURTH_CLASS_GROUP)) {
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								switch (ssqPart) {
									case SevenSigns.CABAL_NULL -> {
										if (ssqPart == SevenSigns.CABAL_NULL) {
											if (ssqPriestType == SevenSigns.CABAL_DAWN) {
												if (!ss.isJoinableToDawn(talker)) {
													if (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW) > 0) {
														takeItems(talker, THE_LORD_OF_MANORS_AGREEMENT_NEW, 1);
														i4 = 2;
													} else {
														if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
															takeItems(talker, Inventory.ADENA_ID, sevenSigns().getSevenSignsJoinDawnFee());
															i4 = 3;
														} else {
															showPage(talker, fnPath + szName + "_" + QUEST_ID + "_06.htm");
															return;
														}
													}
												} else {
													i4 = 1;
												}
											} else {
												if (ssqPriestType == SevenSigns.CABAL_DUSK) {
													i4 = 0;
												}
											}
										}
									}
									case SevenSigns.CABAL_DAWN -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_08.htm");
										return;
									}
									case SevenSigns.CABAL_DUSK -> {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_41.htm");
										return;
									}
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DUSK) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 1) + (i1 * 1)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_STRIFE, 2, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_STRIFE, 2, i3, i4) == 0) {
										return;
									}
								}
							} else {
								i4 = 0;
								if (!ss.isCompetitionPeriod()) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_73.htm");
									return;
								}
								
								if (ss.getSSQPrevWinner() != SevenSigns.CABAL_DUSK) {
									var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
									var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
									var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
									var i3 = ((i0 * 1) + (i1 * 1)) + (i2 * 10);
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_STRIFE, 1, i3, i4) == 0) {
										return;
									}
								} else {
									var i3 = 0;
									if (ss.addSSQMember(talker, SevenSigns.CABAL_DUSK, SevenSigns.SEAL_STRIFE, 1, i3, i4) == 0) {
										return;
									}
								}
							}
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								talker.sendPacket(SEVENSIGNS_PARTECIPATION_DAWN);
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									talker.sendPacket(SEVENSIGNS_PARTECIPATION_DUSK);
								}
							}
							
							talker.sendPacket(FIGHT_FOR_STRIFE);
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_48.htm");
						}
					}
					case MS_REPLY_BUY_RECORD_DAWN, MS_REPLY_BUY_RECORD_DUSK -> {
						if (getQuestItemsCount(talker, Inventory.ADENA_ID) > SevenSigns.RECORD_SEVEN_SIGNS_COST) {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_17.htm");
							} else {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_50.htm");
							}
							
							giveItems(talker, SevenSigns.RECORD_SEVEN_SIGNS_ID, 1);
							takeItems(talker, Inventory.ADENA_ID, SevenSigns.RECORD_SEVEN_SIGNS_COST);
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_18.htm");
							} else {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_51.htm");
							}
						}
					}
					case MS_REPLY_BLUE_STONES_CONTRIBUTE_REQUEST_DAWN -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_21.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_GREEN_STONES_CONTRIBUTE_REQUEST_DAWN -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_22.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_RED_STONES_CONTRIBUTE_REQUEST_DAWN -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_23.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_BLUE_STONES_CONTRIBUTE_REQUEST_DUSK -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_54.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_GREEN_STONES_CONTRIBUTE_REQUEST_DUSK -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_55.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_RED_STONES_CONTRIBUTE_REQUEST_DUSK -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_56.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_RECEIVE_REWARD_DAWN1, MS_REPLY_RECEIVE_REWARD_DAWN2 -> {
						if ((ssqPart == SevenSigns.CABAL_DAWN) && (ssqWinner == SevenSigns.CABAL_DAWN)) {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (!ss.isSealValidationPeriod()) {
								talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
								return;
							}
							
							if (ssqPart != ssqWinner) {
								return;
							}
							
							var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
							var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
							var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
							if (i0 > 0 || i1 > 0 || i2 > 0) {
								var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
								if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
									ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
									ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
									ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									
									if (ssqPriestType == SevenSigns.CABAL_DAWN) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_30.htm");
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DUSK) {
											showPage(talker, fnPath + szName + "_" + QUEST_ID + "_60.htm");
										}
									}
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DAWN) {
										showPage(talker, fnPath + "ssq_npc_priest089.htm");
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DUSK) {
											showPage(talker, fnPath + "ssq_npc_priest090.htm");
										}
									}
									return;
								}
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DAWN) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_31.htm");
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DUSK) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_61.htm");
									}
								}
							}
						}
					}
					case MS_REPLY_RECEIVE_REWARD_DUSK1, MS_REPLY_RECEIVE_REWARD_DUSK2 -> {
						if ((ssqPart == SevenSigns.CABAL_DUSK) && (ssqWinner == SevenSigns.CABAL_DUSK)) {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							if (!ss.isSealValidationPeriod()) {
								talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
								return;
							}
							
							if (ssqPart != ssqWinner) {
								return;
							}
							
							var i0 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
							var i1 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
							var i2 = ss.getDepositedSSQItemCount(talker, SevenSigns.SEAL_STONE_RED_ID);
							if ((i0 > 0) || (i1 > 0) || (i2 > 0)) {
								var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
								if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
									ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
									ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
									ss.deleteDepositedSSQItemAndGiveRewards(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
									
									if (ssqPriestType == SevenSigns.CABAL_DAWN) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_30.htm");
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DUSK) {
											showPage(talker, fnPath + szName + "_" + QUEST_ID + "_60.htm");
										}
									}
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DAWN) {
										showPage(talker, fnPath + "ssq_npc_priest089.htm");
									} else {
										if (ssqPriestType == SevenSigns.CABAL_DUSK) {
											showPage(talker, fnPath + "ssq_npc_priest090.htm");
										}
									}
									return;
								}
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DAWN) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_31.htm");
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DUSK) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_61.htm");
									}
								}
							}
						}
					}
					case MS_REPLY_CONTRIBUTE_SEAL_STONES_REQUEST_DAWN, MS_REPLY_CONTRIBUTE_SEAL_STONES_REQUEST_DUSK -> {
						if (ssqPriestType == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_19.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_52.htm");
						}
					}
					case MS_REPLY_CONTRIBUTE_ALL_STONES_DAWN -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
						var i1 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
						var i2 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID);
						if ((i0 > 0) || (i1 > 0) || ((i2 > 0) && (ssqPart == SevenSigns.CABAL_DAWN))) {
							final var contribution = ss.depositSSQItem(talker, i0, i1, i2);
							if (contribution > -1) {
								if (i0 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
								}
								if (i1 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
								}
								if (i2 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
								}
								
								var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
								sm.addLong(contribution);
								talker.sendPacket(sm);
								
								if (ssqPriestType == SevenSigns.CABAL_DAWN) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_70.htm");
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DUSK) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_74.htm");
									}
								}
							} else {
								talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
							}
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_71.htm");
						}
					}
					case MS_REPLY_CONTRIBUTE_ALL_STONES_DUSK -> {
						if (!ss.isCompetitionPeriod()) {
							talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
							return;
						}
						
						var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
						var i1 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
						var i2 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID);
						if ((i0 > 0) || (i1 > 0) || ((i2 > 0) && (ssqPart == SevenSigns.CABAL_DUSK))) {
							final var contribution = ss.depositSSQItem(talker, i0, i1, i2);
							if (contribution > -1) {
								if (i0 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
								}
								if (i1 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
								}
								if (i2 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
								}
								
								var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
								sm.addLong(contribution);
								talker.sendPacket(sm);
								
								if (ssqPriestType == SevenSigns.CABAL_DAWN) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_70.htm");
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DUSK) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_74.htm");
									}
								}
							} else {
								talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
							}
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_75.htm");
						}
					}
					case MS_REPLY_EXCHANGE_SEAL_STONES_REQUEST_DAWN -> {
						if (ssqPart == SevenSigns.CABAL_DAWN) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80a.htm");
						}
					}
					case MS_REPLY_EXCHANGE_BLUE_STONES_REQUEST_DAWN -> {
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_80b.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_EXCHANGE_GREEN_STONES_REQUEST_DAWN -> {
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_80c.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_EXCHANGE_RED_STONES_REQUEST_DAWN -> {
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_80d.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_EXCHANGE_ALL_STONES_REQUEST_DAWN -> {
						if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
							talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
							return;
						}
						
						var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
						var i1 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
						var i2 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID);
						if (((i0 > 0) || (i1 > 0) || (i2 > 0)) && (ssqPart == SevenSigns.CABAL_DAWN)) {
							var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
							if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
								if (i0 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
								}
								if (i1 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
								}
								if (i2 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
								}
								
								if (i3 > 0) {
									giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
								}
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DAWN) {
									showPage(talker, fnPath + "ssq_npc_priest089.htm");
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DUSK) {
										showPage(talker, fnPath + "ssq_npc_priest090.htm");
									}
								}
								return;
							}
							
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80e.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80f.htm");
						}
					}
					case MS_REPLY_EXCHANGE_SEAL_STONES_REQUEST_DUSK -> {
						if (ssqPart == SevenSigns.CABAL_DUSK) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81a.htm");
						}
					}
					case MS_REPLY_EXCHANGE_BLUE_STONES_REQUEST_DUSK -> {
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_81b.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_EXCHANGE_GREEN_STONES_REQUEST_DUSK -> {
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_81c.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_EXCHANGE_RED_STONES_REQUEST_DUSK -> {
						var html = getHtm(talker.getHtmlPrefix(), fnPath + szName + "_" + QUEST_ID + "_81d.htm");
						html = html.replace("<?cnt?>", String.valueOf(getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID)));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case MS_REPLY_EXCHANGE_ALL_STONES_REQUEST_DUSK -> {
						if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
							talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
							return;
						}
						
						var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
						var i1 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
						var i2 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID);
						if (((i0 > 0) || (i1 > 0) || (i2 > 0)) && (ssqPart == SevenSigns.CABAL_DUSK)) {
							var i3 = ((i0 * 3) + (i1 * 5)) + (i2 * 10);
							if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
								if (i0 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, i0);
								}
								if (i1 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, i1);
								}
								if (i2 > 0) {
									takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, i2);
								}
								
								if (i3 > 0) {
									giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
								}
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DAWN) {
									showPage(talker, fnPath + "ssq_npc_priest089.htm");
								} else {
									if (ssqPriestType == SevenSigns.CABAL_DUSK) {
										showPage(talker, fnPath + "ssq_npc_priest090.htm");
									}
								}
								return;
							}
							
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81e.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81f.htm");
						}
					}
					case 91, 92, 93 -> { // dawn participation request
						if (talker.isInCategory(CategoryType.SECOND_CLASS_GROUP)) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_04.htm");
						} else {
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) && ss.isJoinableToDawn(talker)) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_05.htm");
							} else {
								if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) && (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT, ss.getCurrentCycle()) > 0)) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07.htm");
								} else {
									if (talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) && ss.isJoinableToDawn(talker)) {
										showPage(talker, fnPath + szName + "_" + QUEST_ID + "_05.htm");
									} else {
										if (talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP) && (getQuestItemsCount(talker, THE_LORD_OF_MANORS_AGREEMENT, ss.getCurrentCycle()) > 0)) {
											showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07.htm");
										} else {
											showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07a.htm");
										}
									}
								}
							}
						}
					}
					case 94, 95, 96 -> { // dusk participation request
						if (talker.isInCategory(CategoryType.SECOND_CLASS_GROUP)) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_38.htm");
						} else {
							if (talker.isInCategory(CategoryType.THIRD_CLASS_GROUP)) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_40.htm");
							} else {
								if (talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP)) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_40.htm");
								}
							}
						}
					}
					case MS_REPLY_PAY_PARTITIPATION_FEE -> {
						if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= sevenSigns().getSevenSignsJoinDawnFee()) {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07.htm");
						} else {
							showPage(talker, fnPath + szName + "_" + QUEST_ID + "_07b.htm");
						}
					}
				}
			}
			case MS_ASK_CONTRIBUTE_BLUE_STONES_DAWN -> {
				if (!ss.isCompetitionPeriod()) {
					talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
					return;
				}
				
				if (reply > 0) {
					if (reply > getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID)) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_24.htm");
					} else {
						final var contribution = ss.depositSSQItem(talker, reply, 0, 0);
						if (contribution != -1) {
							takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, reply);
							
							var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
							sm.addLong(contribution);
							talker.sendPacket(sm);
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_25.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_58.htm");
								}
							}
						} else {
							talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
						}
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_79.htm");
				}
			}
			case MS_ASK_CONTRIBUTE_GREEN_STONES_DAWN -> {
				if (!ss.isCompetitionPeriod()) {
					talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
					return;
				}
				
				if (reply > 0) {
					if (reply > getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID)) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_24.htm");
					} else {
						final var contribution = ss.depositSSQItem(talker, 0, reply, 0);
						if (contribution != -1) {
							takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, reply);
							
							var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
							sm.addLong(contribution);
							talker.sendPacket(sm);
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_25.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_58.htm");
								}
							}
						} else {
							talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
						}
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_79.htm");
				}
			}
			case MS_ASK_CONTRIBUTE_RED_STONES_DAWN -> {
				if (!ss.isCompetitionPeriod()) {
					talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
					return;
				}
				
				if (reply > 0) {
					if (reply > getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID)) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_24.htm");
					} else {
						final var contribution = ss.depositSSQItem(talker, 0, 0, reply);
						if (contribution != -1) {
							takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, reply);
							
							var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
							sm.addLong(contribution);
							talker.sendPacket(sm);
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_25.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_58.htm");
								}
							}
						} else {
							talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
						}
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_79.htm");
				}
			}
			case MS_ASK_CONTRIBUTE_BLUE_STONES_DUSK -> {
				if (!ss.isCompetitionPeriod()) {
					talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
					return;
				}
				
				if (reply > 0) {
					if (reply > getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID)) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_24.htm");
					} else {
						final var contribution = ss.depositSSQItem(talker, reply, 0, 0);
						if (contribution != -1) {
							takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, reply);
							
							var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
							sm.addLong(contribution);
							talker.sendPacket(sm);
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_25.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_58.htm");
								}
							}
						} else {
							talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
						}
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_79.htm");
				}
			}
			case MS_ASK_CONTRIBUTE_GREEN_STONES_DUSK -> {
				if (!ss.isCompetitionPeriod()) {
					talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
					return;
				}
				
				if (reply > 0) {
					if (reply > getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID)) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_24.htm");
					} else {
						final var contribution = ss.depositSSQItem(talker, 0, reply, 0);
						if (contribution != -1) {
							takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, reply);
							
							var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
							sm.addLong(contribution);
							talker.sendPacket(sm);
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_25.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_58.htm");
								}
							}
						} else {
							talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
						}
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_79.htm");
				}
			}
			case MS_ASK_CONTRIBUTE_RED_STONES_DUSK -> {
				if (!ss.isCompetitionPeriod()) {
					talker.sendPacket(SEAL_STONES_ONLY_WHILE_QUEST);
					return;
				}
				
				if (reply > 0) {
					if (reply > getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID)) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_24.htm");
					} else {
						final var contribution = ss.depositSSQItem(talker, 0, 0, reply);
						if (contribution != -1) {
							takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, reply);
							
							var sm = SystemMessage.getSystemMessage(CONTRIB_SCORE_INCREASED_S1);
							sm.addLong(contribution);
							talker.sendPacket(sm);
							
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + szName + "_" + QUEST_ID + "_25.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + szName + "_" + QUEST_ID + "_58.htm");
								}
							}
						} else {
							talker.sendPacket(CONTRIB_SCORE_EXCEEDED);
						}
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_79.htm");
				}
			}
			case MS_ASK_EXCHANGE_BLUE_STONES_DAWN -> {
				if (!ss.isSealValidationPeriod()) {
					talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
					return;
				}
				
				if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
					talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
					return;
				}
				
				final var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
				if (reply > 0) {
					if (reply > i0) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80g.htm");
					} else {
						var i3 = reply * 3;
						if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
							if (reply > 0) {
								takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, reply);
							}
							
							if (i3 > 0) {
								giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
							}
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + "ssq_npc_priest089.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + "ssq_npc_priest090.htm");
								}
							}
							return;
						}
						
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80h.htm");
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80i.htm");
				}
			}
			case MS_ASK_EXCHANGE_GREEN_STONES_DAWN -> {
				if (!ss.isSealValidationPeriod()) {
					talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
					return;
				}
				
				if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
					talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
					return;
				}
				
				final var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
				if (reply > 0) {
					if (reply > i0) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80g.htm");
					} else {
						var i3 = reply * 5;
						if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
							if (reply > 0) {
								takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, reply);
							}
							
							if (i3 > 0) {
								giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
							}
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + "ssq_npc_priest089.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + "ssq_npc_priest090.htm");
								}
							}
							return;
						}
						
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80h.htm");
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80i.htm");
				}
			}
			case MS_ASK_EXCHANGE_RED_STONES_DAWN -> {
				if (!ss.isSealValidationPeriod()) {
					talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
					return;
				}
				
				if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
					talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
					return;
				}
				
				final var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID);
				if (reply > 0) {
					if (reply > i0) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80g.htm");
					} else {
						var i3 = reply * 10;
						if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
							if (reply > 0) {
								takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, reply);
							}
							
							if (i3 > 0) {
								giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
							}
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + "ssq_npc_priest089.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + "ssq_npc_priest090.htm");
								}
							}
							return;
						}
						
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80h.htm");
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_80i.htm");
				}
			}
			case MS_ASK_EXCHANGE_BLUE_STONES_DUSK -> {
				if (!ss.isSealValidationPeriod()) {
					talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
					return;
				}
				
				if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
					talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
					return;
				}
				
				final var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_BLUE_ID);
				if (reply > 0) {
					if (reply > i0) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81g.htm");
					} else {
						var i3 = reply * 3;
						if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
							if (reply > 0) {
								takeItems(talker, SevenSigns.SEAL_STONE_BLUE_ID, reply);
							}
							if (i3 > 0) {
								giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
							}
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + "ssq_npc_priest089.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + "ssq_npc_priest090.htm");
								}
							}
							return;
						}
						
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81h.htm");
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81i.htm");
				}
			}
			case MS_ASK_EXCHANGE_GREEN_STONES_DUSK -> {
				if (!ss.isSealValidationPeriod()) {
					talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
					return;
				}
				
				if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
					talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
					return;
				}
				
				final var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_GREEN_ID);
				if (reply > 0) {
					if (reply > i0) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81g.htm");
					} else {
						var i3 = reply * 5;
						if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
							if (reply > 0) {
								takeItems(talker, SevenSigns.SEAL_STONE_GREEN_ID, reply);
							}
							
							if (i3 > 0) {
								giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
							}
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + "ssq_npc_priest089.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + "ssq_npc_priest090.htm");
								}
							}
							return;
						}
						
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81h.htm");
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81i.htm");
				}
			}
			case MS_ASK_EXCHANGE_RED_STONES_DUSK -> {
				if (!ss.isSealValidationPeriod()) {
					talker.sendPacket(SETTLE_ACCOUNT_ONLY_IN_SEAL_VALIDATION);
					return;
				}
				
				if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
					talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
					return;
				}
				
				final var i0 = getQuestItemsCount(talker, SevenSigns.SEAL_STONE_RED_ID);
				if (reply > 0) {
					if (reply > i0) {
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81g.htm");
					} else {
						var i3 = reply * 10;
						if ((getQuestItemsCount(talker, Inventory.ANCIENT_ADENA_ID) + i3) < Integer.MAX_VALUE) {
							if (reply > 0) {
								takeItems(talker, SevenSigns.SEAL_STONE_RED_ID, reply);
							}
							
							if (i3 > 0) {
								giveItems(talker, Inventory.ANCIENT_ADENA_ID, i3);
							}
						} else {
							if (ssqPriestType == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + "ssq_npc_priest089.htm");
							} else {
								if (ssqPriestType == SevenSigns.CABAL_DUSK) {
									showPage(talker, fnPath + "ssq_npc_priest090.htm");
								}
							}
							return;
						}
						
						showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81h.htm");
					}
				} else {
					showPage(talker, fnPath + szName + "_" + QUEST_ID + "_81i.htm");
				}
			}
			case MS_ASK_BUY_CONSUMABLE -> {
				MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
			}
		}
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ss = SevenSigns.getInstance();
		final var ssqPart = ss.getPlayerCabal(talker.getObjectId());
		final var ssqWinner = ss.getCabalHighestScore();
		
		final var ssqPriestType = npc.getTemplate().getParameters().getInt("ssq_priest_type", 0);
		
		if (!ss.isSealValidationPeriod()) {
			if (ssqPart == ssqPriestType) {
				if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
					showPage(talker, fnPath + "q194_noteleport.htm");
				} else {
					teleportFStr(talker, npc, positionCompetition, Inventory.ANCIENT_ADENA_ID);
				}
			} else {
				if (ssqPriestType == SevenSigns.CABAL_DAWN) {
					showPage(talker, fnPath + "ssq_npc_priest087.htm");
				} else {
					showPage(talker, fnPath + "ssq_npc_priest088.htm");
				}
			}
		} else {
			if (ss.isSealValidationPeriod() && (ssqPart == ssqWinner)) {
				if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
					showPage(talker, fnPath + "q194_noteleport.htm");
				} else {
					teleportFStr(talker, npc, position, Inventory.ANCIENT_ADENA_ID);
				}
			}
		}
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var ss = SevenSigns.getInstance();
		final var ssqPart = ss.getPlayerCabal(talker.getObjectId());
		final var ssqWinner = ss.getCabalHighestScore();
		
		final var ssqPriestType = npc.getTemplate().getParameters().getInt("ssq_priest_type", 0);
		
		if (ssqPriestType == SevenSigns.CABAL_DAWN) {
			if (ss.isCompetitionPeriod() && ((ssqPart == SevenSigns.CABAL_DAWN) || (ssqPart == SevenSigns.CABAL_NULL))) {
				showPage(talker, fnPath + szName + "001.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DAWN) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) == SevenSigns.CABAL_DAWN) && (ssqPart == SevenSigns.CABAL_DAWN)) {
				showPage(talker, fnPath + szName + "026.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DAWN) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) != SevenSigns.CABAL_DAWN) && (ssqPart == SevenSigns.CABAL_DAWN)) {
				showPage(talker, fnPath + szName + "029.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DUSK) && ((ssqPart == SevenSigns.CABAL_DAWN) || (ssqPart == SevenSigns.CABAL_NULL))) {
				showPage(talker, fnPath + szName + "032.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_NULL) && ((ssqPart == SevenSigns.CABAL_DAWN) || (ssqPart == SevenSigns.CABAL_NULL))) {
				showPage(talker, fnPath + szName + "033.htm");
				return null;
			}
			if (ssqPart == SevenSigns.CABAL_DUSK) {
				showPage(talker, fnPath + szName + "034.htm");
				return null;
			}
			if (ss.isInitializationPeriod()) {
				showPage(talker, fnPath + szName + "072.htm");
				return null;
			}
			if (ss.isCompResultsPeriod()) {
				showPage(talker, fnPath + szName + "085.htm");
			} else {
				if (ss.isSealValidationPeriod() && (ssqPart != SevenSigns.CABAL_DAWN)) {
					showPage(talker, fnPath + szName + "083.htm");
				}
			}
			
		} else {
			if (ss.isCompetitionPeriod() && ((ssqPart == SevenSigns.CABAL_DUSK) || (ssqPart == SevenSigns.CABAL_NULL))) {
				showPage(talker, fnPath + szName + "035.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DUSK) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) == SevenSigns.CABAL_DUSK) && (ssqPart == SevenSigns.CABAL_DUSK)) {
				showPage(talker, fnPath + szName + "059.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DUSK) && (ss.getSealOwner(SevenSigns.SEAL_GNOSIS) != SevenSigns.CABAL_DUSK) && (ssqPart == SevenSigns.CABAL_DUSK)) {
				showPage(talker, fnPath + szName + "062.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_DAWN) && ((ssqPart == SevenSigns.CABAL_DUSK) || (ssqPart == SevenSigns.CABAL_NULL))) {
				showPage(talker, fnPath + szName + "065.htm");
				return null;
			}
			if (ss.isSealValidationPeriod() && (ssqWinner == SevenSigns.CABAL_NULL) && ((ssqPart == SevenSigns.CABAL_DUSK) || (ssqPart == SevenSigns.CABAL_NULL))) {
				showPage(talker, fnPath + szName + "066.htm");
				return null;
			}
			if (ssqPart == SevenSigns.CABAL_DAWN) {
				showPage(talker, fnPath + szName + "067.htm");
				return null;
			}
			if (ss.isInitializationPeriod()) {
				showPage(talker, fnPath + szName + "076.htm");
				return null;
			}
			if (ss.isCompResultsPeriod()) {
				showPage(talker, fnPath + szName + "086.htm");
			} else {
				if (ss.isSealValidationPeriod() && (ssqPart != SevenSigns.CABAL_DUSK)) {
					showPage(talker, fnPath + szName + "084.htm");
				}
			}
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	protected void teleportFStr(L2PcInstance player, L2Character npc, TelPosList[] teleList, int itemId) {
		final var html = new StringBuilder("<html><body>&$556;<br><br>");
		for (TelPosList tele : teleList) {
			final var loc = tele.loc();
			var ammount = tele.ammount();
			
			html.append("<a action=\"bypass -h teleport " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + itemId + " " + ammount + "\" msg=\"811;" + getNpcString(tele.locId()) + "\">" + getNpcString(tele.locId()) + " - " + ammount + " Ancient Adena</a><br1>");
		}
		html.append("</body></html>");
		
		player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html.toString()));
	}
	
	private static final String getNpcString(int id) {
		return "<fstring>" + id + "</fstring>";
	}
}