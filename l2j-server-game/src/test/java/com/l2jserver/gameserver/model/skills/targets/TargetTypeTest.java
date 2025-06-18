/*
 * Copyright © 2004-2023 L2J Server
 *
 * This file is part of L2J Server.
 *
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.skills.targets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2ChestInstance;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Target Type test.
 * @author Noé Caratini aka Kita
 */
@ExtendWith(MockitoExtension.class)
public class TargetTypeTest {
	@Mock
	private Skill skill;

	@Test
	public void doorTreasureShouldReturnNullIfTargetIsNull() {
		final var caster = mock(L2Character.class);

		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, null);

		assertNull(result);
	}
	
	@Test
    public void doorTreasureShouldReturnNullIfTargetIsNotADoorOrChest() {
		final var target = mock(L2Object.class);
        when(target.isDoor()).thenReturn(false);

		final var caster = mock(L2Character.class);

		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, target);

		assertNull(result);
    }
	
	@Test
    public void doorTreasureShouldReturnTargetIfDoor() {
		final var target = mock(L2Object.class);
        when(target.isDoor()).thenReturn(true);

		final var caster = mock(L2Character.class);

		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, target);

		assertEquals(target, result);
    }
	
	@Test
	public void doorTreasureShouldReturnTargetIfChest() {
		final var target = mock(L2ChestInstance.class);
		final var caster = mock(L2Character.class);

		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, target);

		assertEquals(target, result);
	}

	@Test
	public void testMonstersCanUseEnemyOnlySkillsOnPc() {
		final var caster = mock(L2MonsterInstance.class);
		when(caster.getObjectId()).thenReturn(1);
		when(caster.isPlayable()).thenReturn(false);

		final var target = mock(L2PcInstance.class);
		when(target.isCharacter()).thenReturn(true);
		when(target.isDead()).thenReturn(false);
		when(target.getObjectId()).thenReturn(2);
		when(target.isAutoAttackable(any())).thenReturn(true);

		final var result = TargetType.ENEMY_ONLY.getTarget(skill, caster, target);

		assertEquals(target, result);
	}

	@Test
	public void testPvpChecksReachedForEnemyOnlySkills() {
		final var caster = mock(L2PcInstance.class);
		when(caster.getObjectId()).thenReturn(1);
		when(caster.isPlayable()).thenReturn(true);
		when(caster.getActingPlayer()).thenReturn(caster);
		when(caster.isInOlympiadMode()).thenReturn(true);

		final var target = mock(L2PcInstance.class);
		when(target.isCharacter()).thenReturn(true);
		when(target.isDead()).thenReturn(false);
		when(target.getObjectId()).thenReturn(2);
		when(target.isAutoAttackable(any())).thenReturn(true);

		final var result = TargetType.ENEMY_ONLY.getTarget(skill, caster, target);

		assertNull(result);
	}
}
