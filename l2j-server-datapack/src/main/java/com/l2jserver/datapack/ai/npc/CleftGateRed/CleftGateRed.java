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
package com.l2jserver.datapack.ai.npc.CleftGateRed;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public final class CleftGateRed extends AbstractNpcAI {
	
	private static final int MIN_LEVEL = 75;
	
	private static final int npcId = 36570;
	
	public CleftGateRed() {
		bindFirstTalk(npcId);
		bindTeleportRequest(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, "cleft_gate_red001.htm");
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		final var npc = event.npc();
		
		if (talker.getLevel() >= MIN_LEVEL) {
			final var tel_x1 = ((L2Npc) npc).getTemplate().getParameters().getInt("tel_x1", 0);
			final var tel_y1 = ((L2Npc) npc).getTemplate().getParameters().getInt("tel_y1", 0);
			final var tel_z1 = ((L2Npc) npc).getTemplate().getParameters().getInt("tel_z1", 0);
			
			talker.teleToLocation(tel_x1, tel_y1, tel_z1);
		} else {
			showPage(talker, "cleft_gate_red002.htm");
		}
	}
}