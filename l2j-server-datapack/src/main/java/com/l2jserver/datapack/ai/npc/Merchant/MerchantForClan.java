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

import static com.l2jserver.gameserver.config.Configuration.clan;

import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class MerchantForClan extends Merchant {
	
	protected String fnHi1;
	protected String fnNoLeader;
	protected String fnNoPledge;
	protected String fnPledgeFameValue;
	protected String fnNotEnoughItem;
	protected String fnUpdateFameSuccess;
	protected String fnLowerPledgeLvReq;
	
	private static final int BLOOD_ALLIANCE = 9911;
	private static final int BLOOD_ALLIANCE_COUNT = 1;
	private static final int BLOOD_OATH = 9910;
	private static final int BLOOD_OATH_COUNT = 10;
	private static final int KNIGHTS_EPAULETTE = 9912;
	private static final int KNIGHTS_EPAULETTE_COUNT = 100;
	
	private static final int pledge_lv_req = 5;
	
	private static final int MS_ASK_CLAN_REPUTATION = -302;
	private static final int MS_ASK_BUY_CLAN_ITEM = -303;
	
	private static final int MS_REPLY_CLAN_REPUTATION = 0;
	private static final int MS_REPLY_PROVIDE_BLOOD_ALLIANCE = 1;
	private static final int MS_REPLY_PROVIDE_BLOOD_OATHS = 2;
	private static final int MS_REPLY_PROVIDE_KNIGHTS_EPALUETTES = 3;
	private static final int MS_REPLY_BUY_CLAN_ITEM = 551;
	private static final int MS_REPLY_UPGRADE_CLAN_ITEM = 628;
	
	public MerchantForClan(int npcId) {
		super(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (talker.getClan() != null && (talker.isClanLeader() || talker.hasClanPrivilege(ClanPrivilege.CL_TROOPS_FAME))) {
			showPage(talker, fnPath + fnHi);
		} else {
			showPage(talker, fnPath + fnNoLeader);
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
			case MS_ASK_BUY_CLAN_ITEM -> {
				switch (reply) {
					case MS_REPLY_BUY_CLAN_ITEM -> {
						MultisellData.getInstance().separateAndSend(551, talker, npc, false);
					}
					case MS_REPLY_UPGRADE_CLAN_ITEM -> {
						MultisellData.getInstance().separateAndSend(628, talker, npc, true);
					}
				}
			}
			case MS_ASK_CLAN_REPUTATION -> {
				int i0 = 0;
				int i1 = 0;
				int i2 = 0;
				
				if (talker.getClan() != null && talker.getClan().getLevel() < pledge_lv_req) {
					showPage(talker, fnPath + fnLowerPledgeLvReq);
					return;
				}
				
				switch (reply) {
					case MS_REPLY_CLAN_REPUTATION -> {
						showPage(talker, fnPath + fnHi1);
						return;
					}
					case MS_REPLY_PROVIDE_BLOOD_ALLIANCE -> {
						i0 = BLOOD_ALLIANCE;
						i1 = BLOOD_ALLIANCE_COUNT;
						i2 = clan().getBloodAlliancePoints();
					}
					case MS_REPLY_PROVIDE_BLOOD_OATHS -> {
						i0 = BLOOD_OATH;
						i1 = BLOOD_OATH_COUNT;
						i2 = clan().getBloodOathPoints();
					}
					case MS_REPLY_PROVIDE_KNIGHTS_EPALUETTES -> {
						i0 = KNIGHTS_EPAULETTE;
						i1 = KNIGHTS_EPAULETTE_COUNT;
						i2 = clan().getKnightsEpaulettePoints();
					}
				}
				
				if (getQuestItemsCount(talker, i0) >= i1) {
					if (talker.getClan() == null) {
						showPage(talker, fnPath + fnNoPledge);
						return;
					}
					
					takeItems(talker, i0, i1);
					talker.getClan().addReputationScore(i2, true);
					
					showPage(talker, fnPath + fnUpdateFameSuccess);
					return;
				}
				
				showPage(talker, fnPath + fnNotEnoughItem);
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}