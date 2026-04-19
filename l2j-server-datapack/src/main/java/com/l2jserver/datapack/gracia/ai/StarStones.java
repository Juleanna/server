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
package com.l2jserver.datapack.gracia.ai;

import java.util.List;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * Star Stones AI.
 * @author Gigiikun
 */
public class StarStones extends AbstractNpcAI {
	// @formatter:off
	private static final int[] MOBS =
	{
		18684, 18685, 18686, 18687, 18688, 18689, 18690, 18691, 18692
	};
	// @formatter:on
	
	private static final int COLLECTION_RATE = 1;
	
	public StarStones() {
		bindSkillSee(MOBS);
	}
	
	@Override
	public void onSkillSee(L2Npc npc, L2PcInstance caster, Skill skill, List<L2Object> targets, boolean isSummon) {
		if (skill.getId() == 932) {
			final int itemId = switch (npc.getId()) {
				case 18684, 18685, 18686 -> 14009; // Red item
				case 18687, 18688, 18689 -> 14010; // Blue item
				case 18690, 18691, 18692 -> 14011; // Green item
				default -> throw new IllegalArgumentException("Unexpected value: " + npc.getId());
			};
			if (getRandom(100) < 33) {
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
				caster.addItem("StarStone", itemId, getRandom(COLLECTION_RATE + 1, 2 * COLLECTION_RATE), null, true);
			} else if (((skill.getLevel() == 1) && (getRandom(100) < 15)) || ((skill.getLevel() == 2) && (getRandom(100) < 50)) || ((skill.getLevel() == 3) && (getRandom(100) < 75))) {
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
				caster.addItem("StarStone", itemId, getRandom(1, COLLECTION_RATE), null, true);
			} else {
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_FAILED);
			}
			npc.deleteMe();
		}
	}
}
