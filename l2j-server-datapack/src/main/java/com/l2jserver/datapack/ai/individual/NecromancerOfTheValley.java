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
package com.l2jserver.datapack.ai.individual;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSkillFinished;
import com.l2jserver.gameserver.model.holders.SkillHolder;

/**
 * Necromancer of the Valley AI.
 * @author Adry_85
 * @author Maneco2
 * @since 2.6.2.0
 */
public class NecromancerOfTheValley extends AbstractNpcAI {
	// NPCs
	private static final int EXPLODING_ORC_GHOST = 22818;
	private static final int WRATHFUL_ORC_GHOST = 22819;
	private static final int NECROMANCER_OF_THE_VALLEY = 22858;
	// Skill
	private static final SkillHolder SELF_DESTRUCTION = new SkillHolder(6850);
	// Variable
	private static final String MID_HP_FLAG = "MID_HP_FLAG";
	// Misc
	private static final double HP_PERCENTAGE = 0.60;
	
	public NecromancerOfTheValley() {
		bindAttack(NECROMANCER_OF_THE_VALLEY);
		bindSpawn(EXPLODING_ORC_GHOST);
		bindSpellFinished(EXPLODING_ORC_GHOST);
	}
	
	@Override
	public void onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
		if (npc.getCurrentHp() < (npc.getMaxHp() * HP_PERCENTAGE)) {
			if ((getRandom(100) < 10) && !npc.getVariables().getBoolean(MID_HP_FLAG, false)) {
				npc.getVariables().set(MID_HP_FLAG, true);
				addAttackDesire(addSpawn((getRandomBoolean() ? EXPLODING_ORC_GHOST : WRATHFUL_ORC_GHOST), npc, true), attacker);
			}
		}
	}
	
	@Override
	public void onSpawn(L2Npc npc) {
		for (L2Character obj : npc.getKnownList().getKnownCharactersInRadius(200)) {
			if (obj.isPlayer() && !obj.isDead()) {
				addSkillCastDesire(npc, obj, SELF_DESTRUCTION, 1000000L);
			}
		}
	}
	
	@Override
	public void onSpellFinished(NpcSkillFinished event) {
		if ((event.skill() == SELF_DESTRUCTION.getSkill()) && (event.npc() != null) && !event.npc().isDead()) {
			event.npc().doDie(event.player());
		}
	}
}
