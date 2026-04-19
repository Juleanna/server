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
package com.l2jserver.datapack.ai.npc.Teleports.Survivor;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Gracia Survivor teleport AI.<br>
 * Original Jython script by Kerberos.
 * @author Plim
 */
public final class Survivor extends AbstractNpcAI {
	// NPC
	private static final int SURVIVOR = 32632;
	
	private static final int MS_ASK_GRACIA = -1425;
	
	private static final int REPLY_TELE_GLUDIO_AIRSHIP_WHARF = 1;
	private static final int REPLY_SEEDS_STATUS = 2;
	
	// Misc
	private static final int MIN_LEVEL = 75;
	
	public Survivor() {
		bindFirstTalk(SURVIVOR);
		bindStartNpc(SURVIVOR);
		bindMenuSelected(SURVIVOR);
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
		showPage(talker, "looser_of_gracia001.htm");
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		final var npc = event.npc();
		
		switch (ask) {
			case MS_ASK_GRACIA -> {
				switch (reply) {
					case REPLY_TELE_GLUDIO_AIRSHIP_WHARF -> {
						if (talker.getLevel() < MIN_LEVEL) {
							showPage(talker, "looser_of_gracia005.htm");
							return;
						}
						if (getQuestItemsCount(talker, Inventory.ADENA_ID) >= 150000) {
							takeItems(talker, Inventory.ADENA_ID, 150000);
							talker.teleToLocation(-149406, 255247, -85);
						} else {
							showPage(talker, "looser_of_gracia004.htm");
						}
					}
					case REPLY_SEEDS_STATUS -> {
						int i1 = 0;
						
						String html = getHtm(talker.getHtmlPrefix(), "looser_of_gracia003.htm");
						var i0 = 1; // GraciaSeedsManager.getInstance().getSoIState();
						switch (i0) {
							case 1 -> {
								i1 = 1800711;
							}
							case 2 -> {
								i1 = 1800712;
							}
							case 3 -> {
								i1 = 1800713;
							}
							case 4 -> {
								i1 = 1800714;
							}
							case 5 -> {
								i1 = 1800715;
							}
							case 6 -> {
								i1 = 1800716;
							}
						}
						html = html.replace("<?stat_unde?>", "<fstring>" + i1 + "</fstring>");
						
						i0 = GraciaSeedsManager.getInstance().getSoDState();
						switch (i0) {
							case 1 -> {
								i1 = 1800708;
							}
							case 2 -> {
								i1 = 1800709;
							}
							case 3 -> {
								i1 = 1800710;
							}
						}
						html = html.replace("<?stat_dest?>", "<fstring>" + i1 + "</fstring>");
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
				}
			}
		}
	}
}
