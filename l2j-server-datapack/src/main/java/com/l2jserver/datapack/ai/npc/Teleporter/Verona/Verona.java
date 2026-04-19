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
package com.l2jserver.datapack.ai.npc.Teleporter.Verona;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Verona extends Teleporter {
	
	private static final TelPosList[] _position1 = new TelPosList[] {
		new TelPosList(1010013, new Location(82971, 53207, -1470), 3700),
		new TelPosList(1010020, new Location(117088, 76931, -2670), 6800),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 6200)
	};
	
	private static final TelPosList[] _position2 = new TelPosList[] {
		new TelPosList(1010016, new Location(84852, 15863, -4270), 0),
		new TelPosList(1010017, new Location(85289, 16225, -2780), 0),
		new TelPosList(1010018, new Location(85289, 16225, -2270), 0),
		new TelPosList(1010019, new Location(85289, 16225, -1750), 0)
	};
	
	private static final int ADENA = 57;
	
	private static final int MS_ASK_TELE_CHAT = -8;
	
	private static final int REPLY_TELE_POSITION = 1;
	private static final int REPLY_TELE_FLOOR = 2;
	
	private static final int npcId = 30727;
	
	public Verona() {
		super(npcId);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_TELE_CHAT -> {
				switch (reply) {
					case REPLY_TELE_POSITION -> {
						if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
							showPage(talker, _fnQ194NoTeleport);
						} else {
							teleportFStr(talker, npc, _position1, ADENA, TeleType.NORMAL);
						}
					}
					case REPLY_TELE_FLOOR -> {
						if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
							showPage(talker, _fnQ194NoTeleport);
						} else {
							teleportFStr(talker, npc, _position2, ADENA, TeleType.NORMAL);
						}
					}
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}