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
package com.l2jserver.datapack.ai.npc.SeparatedSoul;

import static com.l2jserver.gameserver.network.SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * Separated Soul teleport AI.
 * @author UnAfraid, Adry_85, Zealar
 */
public class SeparatedSoul extends AbstractNpcAI {
	
	private static final int[] NPCs = {
		32864,
		32865,
		32866,
		32867,
		32868,
		32869,
		32870,
		32891
	};
	
	private static final Location telPos01 = new Location(117046, 76798, -2696);
	private static final Location telPos02 = new Location(99218, 110283, -3688);
	private static final Location telPos03 = new Location(116992, 113716, -3056);
	private static final Location telPos04 = new Location(113203, 121063, -3712);
	private static final Location telPos05 = new Location(146129, 111232, -3568);
	private static final Location telPos06 = new Location(148447, 110582, -3944);
	private static final Location telPos07 = new Location(73122, 118351, -3784);
	private static final Location telPos08 = new Location(131116, 114333, -3704);
	
	private static final String antaras_items = "separated_soul_01002.htm";
	private static final String fnNo_items = "separated_soul_01003.htm";
	private static final String fnNotEnoughLevel = "separated_soul_09001.htm";
	
	private static final int WILL_OF_ANTHARAS = 17266;
	private static final int SEALED_BLOOD_CRYSTAL = 17267;
	private static final int ANTHARAS_BLOOD_CRYSTAL = 17268;
	
	private static final int MIN_LEVEL = 80;
	
	private static final double WEIGHT_LIMIT = 0.8;
	
	private static final int MS_ASK_TELE = -1;
	private static final int MS_ASK_ITEM = -2324;
	
	private static final int REPLY_TELE_HUNTERS_VILLAGE = 1;
	private static final int REPLY_TELE_DRAGON_VALLEY_CENTER = 2;
	private static final int REPLY_TELE_DRAGON_VALLEY_NORTH = 3;
	private static final int REPLY_TELE_DRAGON_VALLEY_SOUTH = 4;
	private static final int REPLY_TELE_ANTHARAS_LAIR_BRIDGE = 5;
	private static final int REPLY_TELE_ANTHARAS_LAIR_DEEP = 6;
	private static final int REPLY_TELE_DRAGON_VALLEY_ENTRANCE = 7;
	private static final int REPLY_TELE_ATHARAS_LAIR_ENTRANCE = 8;
	
	private static final int REPLY_ITEM_REQUEST_SYNTHESIS = 1;
	private static final int REPLY_ITEM_BLOOD_CRYSTAL_ANTHARAS = 2;
	
	public SeparatedSoul() {
		bindFirstTalk(NPCs);
		bindMenuSelected(NPCs);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		showPage(player, getFnHi(npc.getId()));
		
		return super.onFirstTalk(npc, player);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		
		switch (ask) {
			case MS_ASK_TELE -> {
				if (talker.getLevel() < MIN_LEVEL) {
					showPage(talker, fnNotEnoughLevel);
				} else {
					switch (reply) {
						case REPLY_TELE_HUNTERS_VILLAGE -> {
							talker.teleToLocation(telPos01);
						}
						case REPLY_TELE_DRAGON_VALLEY_CENTER -> {
							talker.teleToLocation(telPos02);
						}
						case REPLY_TELE_DRAGON_VALLEY_NORTH -> {
							talker.teleToLocation(telPos03);
						}
						case REPLY_TELE_DRAGON_VALLEY_SOUTH -> {
							talker.teleToLocation(telPos04);
						}
						case REPLY_TELE_ANTHARAS_LAIR_BRIDGE -> {
							talker.teleToLocation(telPos05);
						}
						case REPLY_TELE_ANTHARAS_LAIR_DEEP -> {
							talker.teleToLocation(telPos06);
						}
						case REPLY_TELE_DRAGON_VALLEY_ENTRANCE -> {
							talker.teleToLocation(telPos07);
						}
						case REPLY_TELE_ATHARAS_LAIR_ENTRANCE -> {
							talker.teleToLocation(telPos08);
						}
					}
				}
			}
			case MS_ASK_ITEM -> {
				switch (reply) {
					case REPLY_ITEM_REQUEST_SYNTHESIS -> {
						if ((getQuestItemsCount(talker, WILL_OF_ANTHARAS) > 0) && (getQuestItemsCount(talker, SEALED_BLOOD_CRYSTAL) > 0)) {
							if ((talker.getInventory().getSize(false) >= (talker.getInventoryLimit() * WEIGHT_LIMIT)) || (talker.getCurrentLoad() >= (talker.getMaxLoad() * WEIGHT_LIMIT))) {
								talker.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
								return;
							}
							
							takeItems(talker, WILL_OF_ANTHARAS, 1);
							takeItems(talker, SEALED_BLOOD_CRYSTAL, 1);
							giveItems(talker, ANTHARAS_BLOOD_CRYSTAL, 1);
						} else {
							showPage(talker, fnNo_items);
						}
					}
					case REPLY_ITEM_BLOOD_CRYSTAL_ANTHARAS -> {
						showPage(talker, antaras_items);
					}
				}
			}
		}
	}
	
	private static String getFnHi(int npcId) {
		return switch (npcId) {
			case 32864 -> "separated_soul_01001.htm";
			case 32865 -> "separated_soul_02001.htm";
			case 32866 -> "separated_soul_03001.htm";
			case 32867 -> "separated_soul_04001.htm";
			case 32868 -> "separated_soul_05001.htm";
			case 32869 -> "separated_soul_06001.htm";
			case 32870 -> "separated_soul_07001.htm";
			case 32891 -> "separated_soul_08001.htm";
			default -> null;
		};
	}
}