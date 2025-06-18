/*
 * Copyright © 2004-2023 L2J DataPack
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
package com.l2jserver.datapack.ai.group_template;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.actor.L2Npc;

/**
 * Corpse test.
 * @author Noé Caratini aka Kita
 */
@ExtendWith(MockitoExtension.class)
public class CorpseTest {
	
	@Mock
	private L2Npc npc;
	
	@Test
	void testShouldDieOnSpawn() {
		final var corpseAi = new Corpse();
		
		corpseAi.onSpawn(npc);
		
		verify(npc).doDie(npc);
	}
}