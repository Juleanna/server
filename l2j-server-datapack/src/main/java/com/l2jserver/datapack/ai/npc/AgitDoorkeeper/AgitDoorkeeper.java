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
package com.l2jserver.datapack.ai.npc.AgitDoorkeeper;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.DoorData;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class AgitDoorkeeper extends AbstractNpcAI {
	
	protected String fnHi;
	protected String fnNotMyLord;
	protected String fnUnderSiege;
	
	private static final int MS_ASK_OPENCLOSE_DOOR = -201;
	
	private static final int REPLY_OPEN_DOOR = 1;
	private static final int REPLY_CLOSE_DOOR = 2;
	
	public AgitDoorkeeper(int npcId) {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
	}
	
	public AgitDoorkeeper(int... npcIds) {
		bindFirstTalk(npcIds);
		bindMenuSelected(npcIds);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var clanHall = npc.getConquerableHall();
		
		if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
			if (clanHall.isInSiege()) {
				showPage(talker, fnUnderSiege);
			} else {
				showPage(talker, fnHi);
			}
		} else {
			showPage(talker, fnNotMyLord);
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var clanHall = npc.getConquerableHall();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var doorName1 = npc.getTemplate().getParameters().getString("DoorName1", null);
		final var doorName2 = npc.getTemplate().getParameters().getString("DoorName2", null);
		
		switch (ask) {
			case MS_ASK_OPENCLOSE_DOOR -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					if (clanHall.isInSiege()) {
						showPage(talker, fnUnderSiege);
					} else {
						switch (reply) {
							case REPLY_OPEN_DOOR -> {
								final var door1 = DoorData.getInstance().getDoorByName(doorName1);
								final var door2 = DoorData.getInstance().getDoorByName(doorName2);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
							}
							case REPLY_CLOSE_DOOR -> {
								final var door1 = DoorData.getInstance().getDoorByName(doorName1);
								final var door2 = DoorData.getInstance().getDoorByName(doorName2);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
							}
						}
					}
				} else {
					showPage(talker, fnNotMyLord);
				}
			}
		}
	}
}