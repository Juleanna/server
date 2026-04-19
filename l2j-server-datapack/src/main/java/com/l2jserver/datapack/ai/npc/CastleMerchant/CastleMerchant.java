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
package com.l2jserver.datapack.ai.npc.CastleMerchant;

import static com.l2jserver.gameserver.config.Configuration.general;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.CastleManorManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcManorBypass;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.BuyListSeed;
import com.l2jserver.gameserver.network.serverpackets.ExShowCropInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowProcureCropDetail;
import com.l2jserver.gameserver.network.serverpackets.ExShowSeedInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowSellCropList;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
* @author malyelfik
* @version 2.6.3.0
*/
public abstract class CastleMerchant extends AbstractNpcAI {
	
	private static final String fnPath = "data/html/castleMerchant/";
	
	private static final String fnHi = "castle_merchant001.htm";
	private static final String fnMyLord = "castle_merchant002.htm";
	
	private static final String fnFeudInfo = "defaultfeudinfo.htm";
	private static final String fnNoFeudInfo = "nofeudinfo.htm";
	
	private static final int MS_ASK_TERRITORY_STATUS = -1000;
	
	private static final int MS_REPLY_TERRITORY_STATUS_BACK = 0;
	private static final int MS_REPLY_TERRITORY_STATUS_INFO = 1;
	
	private L2BuyList sellList0;
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(5125, 0, 0.000000, 0) // reaping_machine
	};
	
	public CastleMerchant(int npcId) {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
		bindManorMenuSelected(npcId);
		
		sellList0 = buildBuySellList(_sellList0, npcId, 0);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (general().allowManor()) {
			if (npc.isMyLord(talker)) {
				showPage(talker, fnPath + fnMyLord);
			} else {
				showPage(talker, fnPath + fnHi);
			}
		} else {
			showPage(talker, "data/html/npcdefault.htm");
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
	
	@Override
	public void onManorMenuSelected(NpcManorBypass event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.target();
		
		final var request = event.request();
		
		final var manorId = event.manorId();
		final var nextPeriod = event.nextPeriod();
		
		if (CastleManorManager.getInstance().isUnderMaintenance()) {
			talker.sendPacket(SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
			return;
		}
		
		final int templateId = npc.getTemplate().getParameters().getInt("manor_id", -1);
		final int castleId = (manorId == -1) ? templateId : manorId;
		
		switch (request) {
			case 1 -> { // Seed purchase
				if (templateId != castleId) {
					talker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR).addCastleId(templateId));
					return;
				}
				talker.sendPacket(new BuyListSeed(talker.getAdena(), castleId));
			}
			case 2 -> { // Crop sales
				talker.sendPacket(new ExShowSellCropList(talker.getInventory(), castleId));
			}
			case 3 -> { // Seed info
				talker.sendPacket(new ExShowSeedInfo(castleId, nextPeriod, false));
			}
			case 4 -> { // Crop info
				talker.sendPacket(new ExShowCropInfo(castleId, nextPeriod, false));
			}
			case 5 -> { // Basic info
				talker.sendPacket(new ExShowManorDefaultInfo(false));
			}
			case 6 -> { // Buy harvester
				showBuySell(talker, npc, sellList0);
			}
			case 9 -> { // Edit sales (Crop sales)
				talker.sendPacket(new ExShowProcureCropDetail(manorId));
			}
		}
	}
}