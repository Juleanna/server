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
package com.l2jserver.datapack.ai.npc.Teleporter.Wirphy;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Wirphy extends Teleporter {
	
	private static final TelPosList[] _position = new TelPosList[] {
		new TelPosList(1010005, new Location(-12787, 122779, -3114), 32000),
		new TelPosList(1010004, new Location(-80684, 149770, -3043), 18000),
		new TelPosList(1010574, new Location(87126, -143520, -1288), 4400),
		new TelPosList(1010156, new Location(9709, 15566, -4500), 22000),
		new TelPosList(1010001, new Location(-84141, 244623, -3729), 46000),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 23000),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 17000),
		new TelPosList(1010648, new Location(-117251, 46771, 380), 32000),
		new TelPosList(1010596, new Location(139714, -177456, -1536), 690),
		new TelPosList(1010189, new Location(169008, -208272, -3506), 2400),
		new TelPosList(1010188, new Location(136910, -205082, -3664), 970),
		new TelPosList(1010700, new Location(171946, -173352, 3440), 11000),
		new TelPosList(1010701, new Location(178591, -184615, -360), 12000)
	};
	
	private static final TelPosList[] _positionNoblessNeedItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1),
		new TelPosList(1010595, new Location(175499, -181586, -904), 1),
		new TelPosList(1010597, new Location(144706, -173223, -1520), 1),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1)
	};
	
	private static final TelPosList[] _positionNoblessNoItemField = new TelPosList[] {
		new TelPosList(1010506, new Location(-87328, 142266, -3640), 1000),
		new TelPosList(1010507, new Location(73579, 142709, -3768), 1000),
		new TelPosList(1010595, new Location(175499, -181586, -904), 1000),
		new TelPosList(1010597, new Location(144706, -173223, -1520), 1000),
		new TelPosList(1010053, new Location(146440, 46723, -3400), 1000)
	};
	
	private static final TelPosList[] _positionNewbie = new TelPosList[] {
		new TelPosList(1010156, new Location(9709, 15566, -4500), 1),
		new TelPosList(1010001, new Location(-84141, 244623, -3729), 1),
		new TelPosList(1010155, new Location(46951, 51550, -2976), 1),
		new TelPosList(1010157, new Location(-45158, -112583, -236), 1)
	};
	
	private static final int MS_ASK_TELE_WITH_TOKEN = -6;
	
	private static final int npcId = 30540;
	
	public Wirphy() {
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
			case MS_ASK_TELE_WITH_TOKEN -> {
				if (getQuestItemsCount(talker, itemNeeded) != 0) {
					takeItems(talker, itemNeeded, 1);
					talker.teleToLocation(-80749, 149834, -3043);
				} else {
					showPage(talker, fnPath + "wirphy005.htm");
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}