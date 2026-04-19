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

import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class MSeller extends Merchant {
	
	private static final String fnHi = "mseller001.htm";
	private static final String fnHiDawn = "mseller001_dawn.htm";
	private static final String fnHiDusk = "mseller001_dusk.htm";
	private static final String fnNotMyLord = "mseller002.htm";
	private static final String fnSiegeMyLord = "mseller003.htm";
	private static final String fnMercLimit = "msellerlimit.htm";
	private static final String fnSSDenial = "msellerdenial.htm";
	
	private static final int MS_ASK_BACK = 0;
	private static final int MS_ASK_HIRE = -201;
	private static final int MS_ASK_VIEW_MERCENERY_POSTING = -202;
	
	protected L2BuyList sellList0;
	protected L2BuyList sellList1;
	protected L2BuyList sellList2;
	protected L2BuyList sellList3;
	protected L2BuyList sellList4;
	protected L2BuyList sellList5;
	
	public MSeller(int npcId) {
		super(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var castle = npc.getCastle();
		
		if (npc.isMyLord(talker) || (talker.hasClanPrivilege(ClanPrivilege.CS_MERCENARIES) && talker.getClan() != null && talker.getClanId() == npc.getCastle().getOwnerId())) {
			if (castle.getSiege().isInProgress()) {
				showPage(talker, fnPath + fnSiegeMyLord);
			} else {
				if (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DUSK) {
					showPage(talker, fnPath + fnHiDusk);
				} else {
					if (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DAWN) {
						showPage(talker, fnPath + fnHiDawn);
					} else {
						showPage(talker, fnPath + fnHi);
					}
				}
			}
		} else {
			showPage(talker, fnPath + fnNotMyLord);
		}
		
		return null;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var castle = npc.getCastle();
		
		switch (ask) {
			case MS_ASK_BACK -> {
				if (npc.isMyLord(talker) || (talker.hasClanPrivilege(ClanPrivilege.CS_MERCENARIES) && talker.getClan() != null && talker.getClanId() == npc.getCastle().getOwnerId())) {
					if (castle.getSiege().isInProgress()) {
						showPage(talker, fnPath + fnSiegeMyLord);
					} else {
						if (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DUSK) {
							showPage(talker, fnPath + fnHiDusk);
						} else {
							if (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DAWN) {
								showPage(talker, fnPath + fnHiDawn);
							} else {
								showPage(talker, fnPath + fnHi);
							}
						}
					}
				} else {
					showPage(talker, fnPath + fnNotMyLord);
				}
			}
			case MS_ASK_HIRE -> {
				if (npc.isMyLord(talker) || (talker.hasClanPrivilege(ClanPrivilege.CS_MERCENARIES) && talker.getClan() != null && talker.getClanId() == npc.getCastle().getOwnerId())) {
					if (SevenSigns.getInstance().isCompetitionPeriod() || SevenSigns.getInstance().isCompResultsPeriod()) {
						showPage(talker, fnPath + fnSSDenial);
					} else {
						switch (reply) {
							case 1 -> {
								showBuySell(talker, npc, sellList0);
							}
							case 2 -> {
								showBuySell(talker, npc, sellList1);
							}
							case 3 -> {
								showBuySell(talker, npc, sellList2);
							}
							case 4 -> {
								showBuySell(talker, npc, sellList3);
							}
							case 5 -> {
								showBuySell(talker, npc, sellList4);
							}
							case 6 -> {
								showBuySell(talker, npc, sellList5);
							}
						}
					}
				}
			}
			case MS_ASK_VIEW_MERCENERY_POSTING -> {
				var s0 = "";
				
				switch (castle.getResidenceId()) {
					case 5 -> {
						s0 = "aden_";
					}
					case 8 -> {
						s0 = "rune_";
					}
					default -> {
						s0 = "";
					}
				}
				
				var html = getHtm(talker.getHtmlPrefix(), fnPath + s0 + fnMercLimit);
				html = html.replace("<?feud_name?>", "<fstring>" + Integer.valueOf(1001000 + castle.getResidenceId()) + "</fstring>");
				talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
			}
		}
	}
}