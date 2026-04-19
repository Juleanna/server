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

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class Merchant extends AbstractNpcAI {
	
	protected static final String fnPath = "data/html/merchant/";
	
	protected String fnHi;
	protected String fnYouAreChaotic;
	
	private static final String fnFeudInfo = "defaultfeudinfo.htm";
	private static final String fnNoFeudInfo = "nofeudinfo.htm";
	
	private static final int MS_ASK_BUYSELL = -1;
	private static final int MS_ASK_EXCHANGE_PET_EQUIP = -506;
	private static final int MS_ASK_EXCHANGE_HATCHLING_EQUIP = -507;
	private static final int MS_ASK_BUY_SHADOW_CHAT = -510;
	private static final int MS_ASK_BUY_MULTISELL_ITEM = -303;
	private static final int MS_ASK_TERRITORY_STATUS = -1000;
	
	private static final int MS_REPLY_BUY_SELLLIST0 = 0;
	private static final int MS_REPLY_BUY_SELLLIST1 = 1;
	private static final int MS_REPLY_PREVIEW_SELLLIST0 = 2;
	private static final int MS_REPLY_PREVIEW_SELLLIST1 = 3;
	private static final int MS_REPLY_BUY_SELLLIST4 = 4;
	private static final int MS_REPLY_BUY_SELLLIST5 = 5;
	private static final int MS_REPLY_BUY_SELLLIST6 = 6;
	private static final int MS_REPLY_BUY_SELLLIST7 = 7;
	private static final int MS_REPLY_BUY_SHADOW_CHAT = 1;
	private static final int MS_REPLY_BUY_SHADOW_D = 579;
	private static final int MS_REPLY_BUY_SHADOW_C = 580;
	private static final int MS_REPLY_BUY_SHADOW_B = 581;
	private static final int MS_REPLY_TERRITORY_STATUS_BACK = 0;
	private static final int MS_REPLY_TERRITORY_STATUS_INFO = 1;
	
	protected L2BuyList sellList0;
	protected L2BuyList sellList1;
	protected L2BuyList sellList4;
	protected L2BuyList sellList5;
	protected L2BuyList sellList6;
	protected L2BuyList sellList7;
	
	public Merchant(int npcId) {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (talker.getKarma() > 0) {
			showPage(talker, fnPath + fnYouAreChaotic);
		} else {
			showPage(talker, fnPath + fnHi);
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_BUYSELL -> {
				switch (reply) {
					case MS_REPLY_BUY_SELLLIST0 -> {
						showBuySell(talker, npc, sellList0);
					}
					case MS_REPLY_BUY_SELLLIST1 -> {
						showBuySell(talker, npc, sellList1);
					}
					case MS_REPLY_PREVIEW_SELLLIST0 -> {
						sellPreview(talker, npc, sellList0);
					}
					case MS_REPLY_PREVIEW_SELLLIST1 -> {
						sellPreview(talker, npc, sellList1);
					}
					case MS_REPLY_BUY_SELLLIST4 -> {
						showBuySell(talker, npc, sellList4);
					}
					case MS_REPLY_BUY_SELLLIST5 -> {
						showBuySell(talker, npc, sellList5);
					}
					case MS_REPLY_BUY_SELLLIST6 -> {
						showBuySell(talker, npc, sellList6);
					}
					case MS_REPLY_BUY_SELLLIST7 -> {
						showBuySell(talker, npc, sellList7);
					}
				}
			}
			case MS_ASK_EXCHANGE_PET_EQUIP -> {
				MultisellData.getInstance().separateAndSend(212, talker, npc, true);
			}
			case MS_ASK_EXCHANGE_HATCHLING_EQUIP -> {
				MultisellData.getInstance().separateAndSend(221, talker, npc, false);
			}
			case MS_ASK_BUY_SHADOW_CHAT -> {
				switch (reply) {
					case MS_REPLY_BUY_SHADOW_CHAT -> {
						if (talker.getLevel() < 40) {
							showPage(talker, fnPath + "reflect_weapon_none.htm");
						} else {
							if (talker.getLevel() >= 40 && talker.getLevel() < 46) {
								showPage(talker, fnPath + "reflect_weapon_d.htm");
							} else {
								if (talker.getLevel() >= 46 && talker.getLevel() < 52) {
									showPage(talker, fnPath + "reflect_weapon_c.htm");
								}
							}
						}
						if (talker.getLevel() >= 52) {
							showPage(talker, fnPath + "reflect_weapon_b.htm");
						}
					}
				}
			}
			case MS_ASK_BUY_MULTISELL_ITEM -> {
				switch (reply) {
					case MS_REPLY_BUY_SHADOW_D -> {
						if (talker.getLevel() >= 40 && talker.getLevel() < 46) {
							if (talker.getRace() == Race.KAMAEL) {
								MultisellData.getInstance().separateAndSend(603, talker, npc, false);
							} else {
								MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
							}
						}
					}
					case MS_REPLY_BUY_SHADOW_C -> {
						if (talker.getLevel() >= 46 && talker.getLevel() < 52) {
							if (talker.getRace() == Race.KAMAEL) {
								MultisellData.getInstance().separateAndSend(604, talker, npc, false);
							} else {
								MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
							}
						}
					}
					case MS_REPLY_BUY_SHADOW_B -> {
						if (talker.getLevel() >= 52) {
							if (talker.getRace() == Race.KAMAEL) {
								MultisellData.getInstance().separateAndSend(605, talker, npc, false);
							} else {
								MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
							}
						}
					}
					default -> {
						MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
					}
				}
			}
			case MS_ASK_TERRITORY_STATUS -> {
				switch (reply) {
					case MS_REPLY_TERRITORY_STATUS_BACK -> {
						showPage(talker, fnPath + fnHi);
					}
					case MS_REPLY_TERRITORY_STATUS_INFO -> {
						if (npc.getCastle().getResidenceId() > 0) {
							final var castle = npc.getCastle();
							String html;
							if (castle.getOwner() != null) {
								html = getHtm(talker.getHtmlPrefix(), "data/html/" + fnFeudInfo);
								html = html.replace("<?my_pledge_name?>", castle.getOwner().getName());
								html = html.replace("<?my_owner_name?>", castle.getOwner().getLeaderName());
								html = html.replace("<?current_tax_rate?>", String.valueOf(castle.getTaxPercent()));
							} else {
								html = getHtm(talker.getHtmlPrefix(), "data/html/" + fnNoFeudInfo);
							}
							
							if (castle.getResidenceId() < 7) {
								html = html.replace("<?kingdom_name?>", "<fstring>" + 1001000 + "</fstring>");
							} else {
								html = html.replace("<?kingdom_name?>", "<fstring>" + 1001100 + "</fstring>");
							}
							
							html = html.replace("<?feud_name?>", "<fstring>" + Integer.valueOf(1001000 + castle.getResidenceId()) + "</fstring>");
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						}
					}
				}
			}
		}
	}
}