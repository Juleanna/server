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
package com.l2jserver.datapack.ai.npc.FortressDoorkeeper;

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
public class FortressDoorkeeper extends AbstractNpcAI {
	
	private static final int[] npcIds = {
		35667, 35668, 35669,
		35699, 35700, 35701,
		35736, 35737, 35738,
		35768, 35769, 35770,
		35805, 35806, 35807,
		35836, 35837, 35838,
		35868, 35869, 35870,
		35905, 35906, 35907,
		35937, 35938, 35939,
		35975, 35976, 35977,
		36012, 36013, 36014,
		36044, 36045, 36046,
		36082, 36083, 36084,
		36119, 36120, 36121,
		36150, 36151, 36152,
		36182, 36183, 36184,
		36220, 36221, 36222,
		36258, 36259, 36260,
		36295, 36296, 36297,
		36327, 36328, 36329,
		36365, 36366, 36367
	};
	
	private static final String fnHi = "data/html/doormen/fortress_doorkeeper001.htm";
	private static final String fnNotMyLord = "data/html/doormen/fortress_doorkeeper002.htm";
	private static final String fnUnderSiege = "data/html/doormen/fortress_doorkeeper003.htm";
	
	private static final int MS_ASK_OPENCLOSE_DOOR = -201;
	private static final int MS_ASK_TELE = -202;
	
	private static final int REPLY_OPEN_DOOR = 1;
	private static final int REPLY_CLOSE_DOOR = 2;
	private static final int REPLY_TELE_FLAGPOLE = 1;
	
	public FortressDoorkeeper() {
		bindFirstTalk(npcIds);
		bindMenuSelected(npcIds);
	}
	
	public FortressDoorkeeper(int... npcIds) {
		bindFirstTalk(npcIds);
		bindMenuSelected(npcIds);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var fort = npc.getFort();
		
		if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (fort.getOwnerId() == talker.getClanId())) {
			if (npc.getCastle().getZone().isActive() || fort.getZone().isActive()) {
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
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var fort = npc.getFort();
		
		final var doorName1 = npc.getTemplate().getParameters().getString("DoorName1", null);
		final var doorName2 = npc.getTemplate().getParameters().getString("DoorName2", null);
		final var doorName3 = npc.getTemplate().getParameters().getString("DoorName3", null);
		final var doorName4 = npc.getTemplate().getParameters().getString("DoorName4", null);
		
		final var posx01 = npc.getTemplate().getParameters().getInt("pos_x01", 0);
		final var posy01 = npc.getTemplate().getParameters().getInt("pos_y01", 0);
		final var posz01 = npc.getTemplate().getParameters().getInt("pos_z01", 0);
		
		switch (ask) {
			case MS_ASK_OPENCLOSE_DOOR -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (fort.getOwnerId() == talker.getClanId())) {
					if (npc.getCastle().getZone().isActive() || fort.getZone().isActive()) {
						showPage(talker, fnUnderSiege);
					} else {
						switch (reply) {
							case REPLY_OPEN_DOOR -> {
								final var door1 = DoorData.getInstance().getDoorByName(doorName1);
								final var door2 = DoorData.getInstance().getDoorByName(doorName2);
								final var door3 = DoorData.getInstance().getDoorByName(doorName3);
								final var door4 = DoorData.getInstance().getDoorByName(doorName4);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								if (door3 != null) {
									door3.openMe();
								}
								if (door4 != null) {
									door4.openMe();
								}
							}
							case REPLY_CLOSE_DOOR -> {
								final var door1 = DoorData.getInstance().getDoorByName(doorName1);
								final var door2 = DoorData.getInstance().getDoorByName(doorName2);
								final var door3 = DoorData.getInstance().getDoorByName(doorName3);
								final var door4 = DoorData.getInstance().getDoorByName(doorName4);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								if (door3 != null) {
									door3.closeMe();
								}
								if (door4 != null) {
									door4.closeMe();
								}
							}
						}
					}
				} else {
					showPage(talker, fnNotMyLord);
				}
			}
			case MS_ASK_TELE -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (fort.getOwnerId() == talker.getClanId())) {
					switch (reply) {
						case REPLY_TELE_FLAGPOLE -> {
							if (!talker.isAlikeDead()) {
								talker.teleToLocation(posx01, posy01, posz01);
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