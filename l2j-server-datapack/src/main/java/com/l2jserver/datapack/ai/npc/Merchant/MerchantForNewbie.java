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
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class MerchantForNewbie extends Merchant {
	
	private static final int MS_ASK_EXCHANGE_EQUIPMENT = -305;
	
	private static final int MIN_LEVEL = 25;
	
	public MerchantForNewbie(int npcId) {
		super(npcId);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		
		switch (ask) {
			case MS_ASK_EXCHANGE_EQUIPMENT -> {
				if (talker.getLevel() < MIN_LEVEL) {
					MultisellData.getInstance().separateAndSend(003, talker, npc, true);
				} else {
					showPage(talker, fnPath + "merchant_for_newbie001.htm");
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}
