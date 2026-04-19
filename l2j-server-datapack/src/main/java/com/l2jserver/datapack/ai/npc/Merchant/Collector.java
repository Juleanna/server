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
package com.l2jserver.datapack.ai.npc.Merchant;

import static com.l2jserver.gameserver.model.base.AcquireSkillType.COLLECT;
import static com.l2jserver.gameserver.network.SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1;
import static com.l2jserver.gameserver.network.SystemMessageId.NO_MORE_SKILLS_TO_LEARN;

import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLearnSkillRequested;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Zoey76
 * @version 2.6.3.0
 */
public abstract class Collector extends Merchant {
	
	public Collector(int npcId) {
		super(npcId);
		bindLearnSkillRequested(npcId);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		super.onMenuSelected(event);
	}
	
	@Override
	public void onLearnSkillRequested(PlayerLearnSkillRequested event) {
		final var talker = event.player();
		
		showEtcSkillList(talker);
	}
	
	// TODO(Zoey76): Generalize this function and move it to L2Npc class.
	private static void showEtcSkillList(L2PcInstance talker) {
		final var skills = SkillTreesData.getInstance().getAvailableCollectSkills(talker);
		if (skills.size() == 0) {
			final int minLevel = SkillTreesData.getInstance().getMinLevelForNewSkill(talker, SkillTreesData.getInstance().getCollectSkillTree());
			if (minLevel > 0) {
				final var sm = SystemMessage.getSystemMessage(DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1);
				sm.addInt(minLevel);
				talker.sendPacket(sm);
			} else {
				talker.sendPacket(NO_MORE_SKILLS_TO_LEARN);
			}
		} else {
			talker.sendPacket(new AcquireSkillList(COLLECT, skills));
		}
	}
}