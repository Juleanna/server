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
package com.l2jserver.datapack.ai.npc.ForgeOfTheGods;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSkillFinished;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.AttackableAggroRangeEnter;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Tar Beetle AI
 * @author nonom, malyelfik
 */
public final class TarBeetle extends AbstractNpcAI {
	// NPC
	private static final int TAR_BEETLE = 18804;
	// Skills
	private static final int TAR_SPITE = 6142;
	private static SkillHolder[] SKILLS = {
		new SkillHolder(TAR_SPITE, 1),
		new SkillHolder(TAR_SPITE, 2),
		new SkillHolder(TAR_SPITE, 3)
	};
	
	private static final TarBeetleSpawn spawn = new TarBeetleSpawn();
	
	public TarBeetle() {
		bindAggroRangeEnter(TAR_BEETLE);
		bindSpellFinished(TAR_BEETLE);
	}
	
	@Override
	public void onAggroRangeEnter(AttackableAggroRangeEnter event) {
		final var npc = event.npc();
		if (npc.getScriptValue() > 0) {
			final var info = event.player().getEffectList().getBuffInfoBySkillId(TAR_SPITE);
			final int level = (info != null) ? info.getSkill().getAbnormalLvl() : 0;
			if (level < 3) {
				final Skill skill = SKILLS[level].getSkill();
				if (!npc.isSkillDisabled(skill)) {
					npc.setTarget(event.player());
					npc.doCast(skill);
				}
			}
		}
	}
	
	@Override
	public void onSpellFinished(NpcSkillFinished event) {
		if ((event.skill() != null) && (event.skill().getId() == TAR_SPITE)) {
			final int val = event.npc().getScriptValue() - 1;
			if ((val <= 0) || (SKILLS[0].getSkill().getMpConsume2() > event.npc().getCurrentMp())) {
				spawn.removeBeetle(event.npc());
			} else {
				event.npc().setScriptValue(val);
			}
		}
	}
	
	@Override
	public boolean unload() {
		spawn.unload();
		return super.unload();
	}
}