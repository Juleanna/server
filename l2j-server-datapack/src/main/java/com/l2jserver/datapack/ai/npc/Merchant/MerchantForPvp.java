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
package com.l2jserver.datapack.ai.npc.Merchant;

import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.enums.CategoryType;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;

/**
 * @author St3eT
 * @version 2.6.3.0
 */
public abstract class MerchantForPvp extends Merchant {
	
	protected String fnNoPvpPoint = "pvp_merchant_lepidus002.htm";
	protected String fnNoPledge = "pvp_merchant_lepidus004.htm";
	protected String fnFameUpSuccess = "pvp_merchant_lepidus005.htm";
	protected String fnNoPkCount = "pvp_merchant_lepidus006.htm";
	protected String fnPkDownSuccess = "pvp_merchant_lepidus007.htm";
	
	private static final int MS_ASK_EXAMINE_ITEMS = -3001;
	private static final int MS_ASK_FAME = -4001;
	
	private static final int MS_REPLY_ENHANCE_PVP = 1;
	private static final int MS_REPLY_ENHANCE_CANCEL = 2;
	private static final int MS_REPLY_OBTAIN_ITEM = 3;
	private static final int MS_REPLY_DECREASE_PK = 1;
	private static final int MS_REPLY_INCREASE_CLAN_FAME = 2;
	
	private static final int DECREASE_COST = 5000;
	private static final int REPUTATION_COST = 1000;
	private static final int MIN_CLAN_LVL = 5;
	private static final int MIN_LVL = 40;
	
	public MerchantForPvp(int npcId) {
		super(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (talker.getFame() > 0) {
			if ((talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP)) && talker.getLevel() >= MIN_LVL) {
				showPage(talker, fnPath + fnHi);
			} else {
				showPage(talker, fnPath + fnNoPvpPoint);
			}
		} else {
			showPage(talker, fnPath + fnNoPvpPoint);
		}
		
		return null;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_EXAMINE_ITEMS -> {
				switch (reply) {
					case MS_REPLY_ENHANCE_PVP -> {
						if (talker.getFame() < 0) {
							showPage(talker, fnPath + fnNoPvpPoint);
						}
						MultisellData.getInstance().separateAndSend(638, talker, npc, true);
					}
					case MS_REPLY_ENHANCE_CANCEL -> {
						if (talker.getFame() < 0) {
							showPage(talker, fnPath + fnNoPvpPoint);
						}
						MultisellData.getInstance().separateAndSend(639, talker, npc, true);
					}
					case MS_REPLY_OBTAIN_ITEM -> {
						if (talker.getFame() < 0) {
							showPage(talker, fnPath + fnNoPvpPoint);
						}
						MultisellData.getInstance().separateAndSend(640, talker, npc, false);
					}
				}
			}
			case MS_ASK_FAME -> {
				switch (reply) {
					case MS_REPLY_DECREASE_PK -> {
						if (talker.getPkKills() > 0) {
							if (talker.getFame() >= DECREASE_COST) {
								if ((talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP)) && talker.getLevel() >= MIN_LVL) {
									talker.setFame(talker.getFame() - DECREASE_COST);
									talker.setPkKills(talker.getPkKills() - 1);
									showPage(talker, fnPath + fnPkDownSuccess);
								} else {
									showPage(talker, fnPath + fnNoPvpPoint);
								}
							} else {
								showPage(talker, fnPath + fnNoPvpPoint);
							}
						} else {
							showPage(talker, fnPath + fnNoPkCount);
						}
					}
					case MS_REPLY_INCREASE_CLAN_FAME -> {
						if (talker.getClan() != null && talker.getClan().getLevel() >= MIN_CLAN_LVL) {
							if (talker.getFame() >= REPUTATION_COST) {
								if ((talker.isInCategory(CategoryType.THIRD_CLASS_GROUP) || talker.isInCategory(CategoryType.FOURTH_CLASS_GROUP)) && talker.getLevel() >= MIN_LVL) {
									talker.setFame(talker.getFame() - REPUTATION_COST);
									talker.getClan().addReputationScore(50, true);
									talker.sendPacket(new UserInfo(talker));
									talker.sendPacket(SystemMessageId.ACQUIRED_50_CLAN_FAME_POINTS);
									showPage(talker, fnPath + fnFameUpSuccess);
								} else {
									showPage(talker, fnPath + fnNoPvpPoint);
								}
							} else {
								showPage(talker, fnPath + fnNoPvpPoint);
							}
						} else {
							showPage(talker, fnPath + fnNoPledge);
						}
					}
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}