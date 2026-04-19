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
package com.l2jserver.datapack.ai.npc.Researchers;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Researchers extends AbstractNpcAI {
	
	private static final int[] NPCs = { 30949, 30950, 30951 };
	
	private static final int GREEN_DIMENSION_STONE = 4401;
	private static final int BLUE_DIMENSION_STONE = 4402;
	private static final int RED_DIMENSION_STONE = 4403;
	
	private static final double MAX_CARRY_WEIGHT = 0.9;
	
	private static final int MS_ASK_BUY_STONE = -6;
	
	private static final int REPLY_BUY_GREEN_STONE = 1;
	private static final int REPLY_BUY_BLUE_STONE = 2;
	private static final int REPLY_BUY_RED_STONE = 3;
	
	public Researchers() {
		bindStartNpc(NPCs);
		bindFirstTalk(NPCs);
		bindMenuSelected(NPCs);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.endsWith(".htm")) {
			showPage(player, event);
		}
		
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, getFn(npc.getId()) + ".htm");
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		final var npc = event.npc();
		
		switch (ask) {
			case MS_ASK_BUY_STONE -> {
				switch (reply) {
					case REPLY_BUY_GREEN_STONE -> {
						if ((talker.getInventoryLimit() - talker.getInventory().getSize()) >= 1 && talker.getCurrentLoad() < (talker.getMaxLoad() * MAX_CARRY_WEIGHT)) {
							if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= 10000) {
								takeItems(talker, Inventory.ADENA_ID, 10000);
								giveItems(talker, GREEN_DIMENSION_STONE, 1);
							} else {
								showPage(talker, getFn(npc.getId()) + "b.htm");
							}
						} else {
							showPage(talker, getFn(npc.getId()) + "c.htm");
						}
					}
					case REPLY_BUY_BLUE_STONE -> {
						if ((talker.getInventoryLimit() - talker.getInventory().getSize()) >= 1 && talker.getCurrentLoad() < (talker.getMaxLoad() * MAX_CARRY_WEIGHT)) {
							if ((talker.getInventoryLimit() - talker.getInventory().getSize()) >= 1 && talker.getCurrentLoad() < (talker.getMaxLoad() * MAX_CARRY_WEIGHT)) {
								if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= 10000) {
									takeItems(talker, Inventory.ADENA_ID, 10000);
									giveItems(talker, BLUE_DIMENSION_STONE, 1);
								} else {
									showPage(talker, getFn(npc.getId()) + "b.htm");
								}
							} else {
								showPage(talker, getFn(npc.getId()) + "c.htm");
							}
						}
					}
					case REPLY_BUY_RED_STONE -> {
						if ((talker.getInventoryLimit() - talker.getInventory().getSize()) >= 1 && talker.getCurrentLoad() < (talker.getMaxLoad() * MAX_CARRY_WEIGHT)) {
							if ((talker.getInventoryLimit() - talker.getInventory().getSize()) >= 1 && talker.getCurrentLoad() < (talker.getMaxLoad() * MAX_CARRY_WEIGHT)) {
								if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= 10000) {
									takeItems(talker, Inventory.ADENA_ID, 10000);
									giveItems(talker, RED_DIMENSION_STONE, 1);
								} else {
									showPage(talker, getFn(npc.getId()) + "b.htm");
								}
							} else {
								showPage(talker, getFn(npc.getId()) + "c.htm");
							}
						}
					}
				}
			}
		}
	}
	
	private static String getFn(int npcId) {
		return switch (npcId) {
			case 30949 -> "researcher_keplon001";
			case 30950 -> "researcher_euclie001";
			case 30951 -> "researcher_pithgon001";
			default -> null;
		};
	}
}