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
package com.l2jserver.datapack.ai.npc.Teleporter.Penne;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Penne extends Teleporter {
	
	private static final int npcId = 30487;
	
	public Penne() {
		super(npcId);
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var posX1 = npc.getTemplate().getParameters().getInt("PosX1", 0);
		final var posY1 = npc.getTemplate().getParameters().getInt("PosY1", 0);
		final var posZ1 = npc.getTemplate().getParameters().getInt("PosZ1", 0);
		
		final var posX2 = npc.getTemplate().getParameters().getInt("PosX2", 0);
		final var posY2 = npc.getTemplate().getParameters().getInt("PosY2", 0);
		final var posZ2 = npc.getTemplate().getParameters().getInt("PosZ2", 0);
		
		if (getRandom(100) < 50) {
			talker.teleToLocation(posX1, posY1, posZ1);
		} else {
			talker.teleToLocation(posX2, posY2, posZ2);
		}
	}
}