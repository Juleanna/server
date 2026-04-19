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
package com.l2jserver.datapack.ai.npc.Teleporter.Kurfa;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Kurfa extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010199, new Location(148024, -55281, -2728), 2400),
		new TelPosList(1010200, new Location(43835, -47749, -792), 11000),
		new TelPosList(1010568, new Location(165054, -47861, -3560), 4200),
		new TelPosList(1010609, new Location(144880, -113468, -2560), 5400)
	};
	
	private static final TelPosList[] _positionForFriend = new TelPosList[] {
		new TelPosList(1010199, new Location(148024, -55281, -2728), 2400),
		new TelPosList(1010200, new Location(43835, -47749, -792), 11000),
		new TelPosList(1010023, new Location(146783, 25808, -2000), 10000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 8900),
		new TelPosList(1010568, new Location(165054, -47861, -3560), 4200),
		new TelPosList(1010609, new Location(144880, -113468, -2560), 5400)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010605, new Location(183140, -53307, -1896), 1),
		new TelPosList(1010610, new Location(144625, -101291, -3384), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1),
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000), 
		new TelPosList(1010605, new Location(183140, -53307, -1896), 1000),
		new TelPosList(1010610, new Location(144625, -101291, -3384), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final int friendShip1 = 7211;
	private static final int friendShip2 = 7212;
	private static final int friendShip3 = 7213;
	private static final int friendShip4 = 7214;
	private static final int friendShip5 = 7215;
	
	private static final String fnHi = fnPath + "kurfa001.htm";
	private static final String fnNoFriend = fnPath + "kurfa009.htm";
	private static final String fnForFriendNormal = fnPath + "kurfa008.htm";
	private static final String fnForFriendSpecial = fnPath + "kurfa007.htm";
	
	private static final int ADENA = 57;
	
	private static final int MS_ASK_TELE_CHAT = -30;
	
	private static final int REPLY_TELE_POSITION = 1;
	private static final int REPLY_TELE_FRIEND = 2;
	
	private static final int npcId = 31376;
	
	public Kurfa() {
		super(npcId);
		
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (getQuestItemsCount(talker, friendShip5) > 0) {
			showPage(talker, fnForFriendSpecial);
		} else {
			if (getQuestItemsCount(talker, friendShip4) > 0) {
				showPage(talker, fnForFriendNormal);
			} else {
				if (getQuestItemsCount(talker, friendShip1) > 0 || getQuestItemsCount(talker, friendShip2) > 0 || getQuestItemsCount(talker, friendShip3) > 0) {
					showPage(talker, fnHi);
				} else {
					showPage(talker, fnNoFriend);
				}
			}
		}
		
		return null;
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
							teleportFStr(talker, npc, _position, ADENA, TeleType.NORMAL);
						}
					}
					case REPLY_TELE_FRIEND -> {
						if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
							showPage(talker, _fnQ194NoTeleport);
						} else {
							teleportFStr(talker, npc, _positionForFriend, ADENA, TeleType.NORMAL);
						}
					}
				}
			}
		}
	}
}