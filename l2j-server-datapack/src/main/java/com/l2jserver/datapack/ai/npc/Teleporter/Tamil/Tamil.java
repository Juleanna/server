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
package com.l2jserver.datapack.ai.npc.Teleporter.Tamil;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Tamil extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 23000),
		new TelPosList(1010004, new Location(-80684, 149770, -3043), 18000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 13000),
		new TelPosList(1010156, new Location(9709, 15566, -4500), 13000),
		new TelPosList(1010158, new Location(115120, -178224, -917), 17000),
		new TelPosList(1010001, new Location(-84141, 244623, -3729), 35000),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 18000),
		new TelPosList(1010648, new Location(-117251, 46771, 380), 17000),
		new TelPosList(1010046, new Location(-4190, -80040, -2696), 2000),
		new TelPosList(1010647, new Location(-10983, -117484, -2464), 960),
		new TelPosList(1010603, new Location(9340, -112509, -2536), 1500),
		new TelPosList(1010048, new Location(8652, -139941, -1144), 1600)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010007, new Location(83396, 147904, -3404), 1),
		new TelPosList(1010556, new Location(-25309, -131569, -680), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010007, new Location(83396, 147904, -3404), 1000),
		new TelPosList(1010556, new Location(-25309, -131569, -680), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000),
	};
	
	private static final TelPosList[] _positionNewbie = new TelPosList[] {
		new TelPosList(1010156, new Location(9709, 15566, -4500), 1),
		new TelPosList(1010158, new Location(115120, -178224, -917), 1),
		new TelPosList(1010001, new Location(-84141, 244623, -3729), 1),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 1)
	};
	
	private static final int MS_ASK_TELE_WITH_AMULET = -6;
	
	private static final int npcId = 30576;
	
	public Tamil() {
		super(npcId);
		
		super.position = _position;
		super.positionNoblessNeedItemField = _positionNoblessNeedItemField;
		super.positionNoblessNoItemField = _positionNoblessNoItemField;
		super.positionNewbie = _positionNewbie;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		
		final var itemNeeded = npc.getTemplate().getParameters().getInt("ItemNeeded", 0);
		
		switch (ask) {
			case MS_ASK_TELE_WITH_AMULET -> {
				if (getQuestItemsCount(talker, itemNeeded) != 0) {
					takeItems(talker, itemNeeded, 1);
					talker.teleToLocation(-80684, 149770, -3043);
				} else {
					showPage(talker, fnPath + "tamil005.htm");
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}