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
package com.l2jserver.datapack.ai.npc.Merchant.Shhadai;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
* @author GKR
* @version 2.6.3.0
*/
public class Shhadai extends Merchant {
	
	private static final int posX = 9032;
	private static final int posY = 253063;
	private static final int posZ = -1928;
	
	private static final int posXTemp = 16882;
	private static final int posYTemp = 238952;
	private static final int posZTemp = 9776;
	
	private static final int MS_ASK_ABOUT_AA = -1006;
	private static final int MS_ASK_BUY_MULTISELL_ITEM = -303;
	
	private static final int MS_REPLY_ABOUT_AA = 1;
	private static final int MS_REPLY_CURSED_SEAL_STONES = 624;
	private static final int MS_REPLY_EXCHANGE_SHOULDER_ORNAMENT = 625;
	private static final int MS_REPLY_REMOVE_ARMOR_POWER = 616;
	private static final int MS_REPLY_ENHANCE_ARMOR_POWER = 617;
	
	private int _i_ai3 = 0;
	
	private static final int npcId = 32347;
	
	public Shhadai() {
		super(npcId);
		bindSpawn(npcId);
	}
	
	@Override
	public void onSpawn(L2Npc npc) {
		final var i0 = GameTimeController.getInstance().isNight();
		if (i0) {
			npc.teleToLocation(posX, posY, posZ);
			_i_ai3 = 1;
		} else {
			npc.teleToLocation(posXTemp, posYTemp, posZTemp);
			_i_ai3 = 0;
		}
		
		startQuestTimer("5003", 10000, npc, null);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, fnPath + "shhadai001.htm");
		
		return null;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_ABOUT_AA -> {
				switch (reply) {
					case MS_REPLY_ABOUT_AA -> {
						showPage(talker, fnPath + "shhadai002.htm");
					}
				}
			}
			case MS_ASK_BUY_MULTISELL_ITEM -> {
				switch (reply) {
					case MS_REPLY_CURSED_SEAL_STONES, MS_REPLY_EXCHANGE_SHOULDER_ORNAMENT -> {
						MultisellData.getInstance().separateAndSend(reply, talker, npc, false);
					}
					case MS_REPLY_REMOVE_ARMOR_POWER , MS_REPLY_ENHANCE_ARMOR_POWER -> {
						MultisellData.getInstance().separateAndSend(reply, talker, npc, true);
					}
				}
			}
		}
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.equals("5003")) {
			final var i0 = GameTimeController.getInstance().isNight();
			if (i0) {
				if (_i_ai3 == 0) {
					npc.teleToLocation(posX, posY, posZ);
					_i_ai3 = 1;
				}
			} else {
				if (_i_ai3 == 1) {
					npc.teleToLocation(posXTemp, posYTemp, posZTemp);
					_i_ai3 = 0;
				}
			}
			
			startQuestTimer("5003", 10000, npc, null);
		}
		
		return super.onEvent(event, npc, player);
	}
}