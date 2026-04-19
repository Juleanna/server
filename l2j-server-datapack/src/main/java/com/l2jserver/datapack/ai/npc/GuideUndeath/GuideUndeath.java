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
package com.l2jserver.datapack.ai.npc.GuideUndeath;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class GuideUndeath extends AbstractNpcAI {
	
	private static final int npcId = 32534;
	
	private static final int MS_ASK_ENTER_SEED_OF_INFINITY = -7801;
	
	public GuideUndeath() {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, "guide_undeath001.htm");
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		
		switch (ask) {
			case MS_ASK_ENTER_SEED_OF_INFINITY -> {
				switch (reply) {
					case 1 -> {
						if (talker.getTransformationId() != 260 && talker.getTransformationId() != 8 && talker.getTransformationId() != 9) {
							talker.teleToLocation((-183296 + getRandom(100)) - getRandom(100), (206038 + getRandom(100)) - getRandom(100), -12896);
						} else {
							talker.sendPacket(SystemMessageId.YOU_CANNOT_ENTER_SEED_IN_FLYING_TRANSFORM);
						}
					}
				}
			}
		}
	}
}