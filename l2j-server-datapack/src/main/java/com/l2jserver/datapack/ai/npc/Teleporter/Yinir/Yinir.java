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
package com.l2jserver.datapack.ai.npc.Teleporter.Yinir;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Yinir extends Teleporter {
	
	private static final TelPosList[] _positionPoint = new TelPosList[] {
		new TelPosList(1010648, new Location(-117251, 46771, 380), 0)
	};
	
	private static final int higher_lv = 20;
	
	private static final int npcId = 32184;
	
	public Yinir() {
		super(npcId);
		
		super.positionPoint = _positionPoint;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		
		if (talker.getKarma() > 0) {
			showPage(talker, fnPath + "yinir003.htm");
			return;
		}
		if (talker.getLevel() > higher_lv) {
			showPage(talker, fnPath + "yinir005.htm");
			return;
		}
		
		super.onMenuSelected(event);
	}
}