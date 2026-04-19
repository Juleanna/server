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
package com.l2jserver.datapack.ai.npc.TraderImmortality;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class TraderImmortality extends AbstractNpcAI {
	
	private static final String fnHi = "trader_immortality001.htm";
	private static final String fnHi2 = "trader_immortality002.htm";
	
	private static final int MS_ASK_LOOK_ITEMS = -7801;
	
	private static final int MS_REPLY_LOOK_ITEMS1 = 1;
	private static final int MS_REPLY_LOOK_ITEMS2 = 2;
	
	private static final int KEUSEREUS_MARK_STAGE2 = 13692;
	
	private static final int npcId = 32546;
	
	public TraderImmortality() {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (getQuestItemsCount(talker, KEUSEREUS_MARK_STAGE2) >= 1) {
			showPage(talker, fnHi2);
		} else {
			showPage(talker, fnHi);
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
			case MS_ASK_LOOK_ITEMS -> {
				switch (reply) {
					case MS_REPLY_LOOK_ITEMS1 -> {
						MultisellData.getInstance().separateAndSend(647, talker, npc, false);
					}
					case MS_REPLY_LOOK_ITEMS2 -> {
						if (getQuestItemsCount(talker, KEUSEREUS_MARK_STAGE2) >= 1) {
							MultisellData.getInstance().separateAndSend(698, talker, npc, false);
						}
					}
				}
			}
		}
	}
}