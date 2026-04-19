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
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
* @author Charus
* @version 2.6.3.0
*/
public abstract class MerchantForFriendSpecial extends Merchant {
	
	protected String fnNoFriend;
	protected String fnSpecialProduct1;
	protected String fnSpecialProduct2;
	
	private static final int MS_ASK_MULTISELL = -303;
	
	public MerchantForFriendSpecial(int npcId) {
		super(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var friendShip1 = npc.getTemplate().getParameters().getInt("FriendShip1", 0);
		final var friendShip2 = npc.getTemplate().getParameters().getInt("FriendShip2", 0);
		final var friendShip3 = npc.getTemplate().getParameters().getInt("FriendShip3", 0);
		final var friendShip4 = npc.getTemplate().getParameters().getInt("FriendShip4", 0);
		final var friendShip5 = npc.getTemplate().getParameters().getInt("FriendShip5", 0);
		
		if (friendShip1 == 0) {
			showPage(talker, fnPath + fnHi);
		} else {
			if (getQuestItemsCount(talker, friendShip5) > 0) {
				showPage(talker, fnPath + fnSpecialProduct2);
			} else {
				if (getQuestItemsCount(talker, friendShip3) > 0 || getQuestItemsCount(talker, friendShip4) > 0) {
					showPage(talker, fnPath + fnSpecialProduct1);
				} else {
					if (getQuestItemsCount(talker, friendShip1) > 0 || getQuestItemsCount(talker, friendShip2) > 0) {
						showPage(talker, fnPath + fnHi);
					} else {
						showPage(talker, fnPath + fnNoFriend);
					}
				}
			}
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
			case MS_ASK_MULTISELL -> {
				MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
			}
		}
	}
}