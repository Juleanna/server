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
package com.l2jserver.datapack.ai.npc.Doorkeeper;

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
public abstract class Doorkeeper extends AbstractNpcAI {
	
	protected String fnHi;
	protected String fnNotMyLord;
	protected String fnUnderSiege;
	
	private static final int MS_ASK_OPENCLOSE_DOOR = -201;
	private static final int MS_ASK_TELE = -202;
	
	private static final int REPLY_OPEN_DOOR = 1;
	private static final int REPLY_CLOSE_DOOR = 2;
	private static final int REPLY_TELE_OUTSIDE = 1;
	private static final int REPLY_TELE_INSIDE = 2;
	
	public Doorkeeper(int... npcIds) {
		bindFirstTalk(npcIds);
		bindMenuSelected(npcIds);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var castle = npc.getCastle();
		
		if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
			if (castle.getZone().isActive()) {
				if ((talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					showPage(talker, fnHi);
				} else {
					showPage(talker, fnUnderSiege);
				}
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
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var castle = npc.getCastle();
		
		final var doorName1 = npc.getTemplate().getParameters().getString("DoorName1", null);
		final var doorName2 = npc.getTemplate().getParameters().getString("DoorName2", null);
		
		final var posx01 = npc.getTemplate().getParameters().getInt("pos_x01", 0);
		final var posy01 = npc.getTemplate().getParameters().getInt("pos_y01", 0);
		final var posz01 = npc.getTemplate().getParameters().getInt("pos_z01", 0);
		final var posx02 = npc.getTemplate().getParameters().getInt("pos_x02", 0);
		final var posy02 = npc.getTemplate().getParameters().getInt("pos_y02", 0);
		final var posz02 = npc.getTemplate().getParameters().getInt("pos_z02", 0);
		
		switch (ask) {
			case MS_ASK_OPENCLOSE_DOOR -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					if (castle.getZone().isActive()) {
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
			case MS_ASK_TELE -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					switch (reply) {
						case REPLY_TELE_OUTSIDE -> {
							if (!talker.isAlikeDead()) {
								talker.teleToLocation(posx01, posy01, posz01);
							}
						}
						case REPLY_TELE_INSIDE -> {
							if (!talker.isAlikeDead()) {
								talker.teleToLocation(posx02, posy02, posz02);
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