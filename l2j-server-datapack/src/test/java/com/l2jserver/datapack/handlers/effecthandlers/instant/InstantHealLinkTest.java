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
package com.l2jserver.datapack.handlers.effecthandlers.instant;

import static com.l2jserver.gameserver.enums.EffectCalculationType.DIFF;
import static com.l2jserver.gameserver.enums.EffectCalculationType.PER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * Instant Heal Link test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
class InstantHealLinkTest {
	
	private static final int SKILL_ID = 1553;
	private static final int AMOUNT_PER = 30;
	private static final int STEP_PER = 3;
	private static final int AMOUNT_DIFF = 100;
	private static final int STEP_DIFF = 10;
	
	private static final int INDEX_FIRST = 0;
	private static final int INDEX_THIRD = 2;
	private static final int OBJECT_ID_EFFECTOR = 1;
	private static final int OBJECT_ID_EFFECTED = 2;
	private static final int MAXIMUM_HP = 1000;
	private static final double CURRENT_HP = 500.0;
	private static final double TARGET_PER_HP_FIRST = 800.0;
	private static final double TARGET_PER_HP_THIRD = 740.0;
	private static final double TARGET_DIFF_HP_FIRST = 600.0;
	private static final double TARGET_DIFF_HP_THIRD = 580.0;
	
	@Mock
	private L2Character effector;
	@Mock
	private L2Character effected;
	@Mock
	private BuffInfo buffInfo;
	
	private static InstantHealLink effectPer;
	private static InstantHealLink effectDiff;
	
	@BeforeAll
	static void init() {
		final var set = new StatsSet(Map.of("name", "InstantHealLink"));
		final var paramsPer = new StatsSet(Map.of("id", SKILL_ID, "amount", AMOUNT_PER, "mode", PER, "step", STEP_PER));
		effectPer = new InstantHealLink(null, null, set, paramsPer);
		
		final var paramsDiff = new StatsSet(Map.of("id", SKILL_ID, "amount", AMOUNT_DIFF, "mode", DIFF, "step", STEP_DIFF));
		effectDiff = new InstantHealLink(null, null, set, paramsDiff);
	}
	
	@Test
	void test_is_instant() {
		assertTrue(effectPer.isInstant());
	}
	
	@Test
	void test_null_effected() {
		effectPer.onStart(buffInfo);
	}
	
	@Test
	void test_effected_is_dead() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isDead()).thenReturn(true);
		
		effectPer.onStart(buffInfo);
		
		verify(effected, never()).setCurrentHp(anyDouble());
	}
	
	@Test
	void test_effected_is_door() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isDoor()).thenReturn(true);
		
		effectPer.onStart(buffInfo);
		
		verify(effected, never()).setCurrentHp(anyDouble());
	}
	
	@Test
	void test_effected_is_invulnerable() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isInvul()).thenReturn(true);
		
		effectPer.onStart(buffInfo);
		
		verify(effected, never()).setCurrentHp(anyDouble());
	}
	
	@Test
	void test_effected_is_hp_blocked() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isHpBlocked()).thenReturn(true);
		
		effectPer.onStart(buffInfo);
		
		verify(effected, never()).setCurrentHp(anyDouble());
	}
	
	@Test
	void test_full_percentage_healed_first_target() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getIndex()).thenReturn(INDEX_FIRST);
		when(effected.getMaxHp()).thenReturn(MAXIMUM_HP);
		when(effected.getMaxRecoverableHp()).thenReturn(MAXIMUM_HP);
		when(effected.getCurrentHp()).thenReturn(CURRENT_HP);
		when(effected.getObjectId()).thenReturn(OBJECT_ID_EFFECTED);
		when(effector.getObjectId()).thenReturn(OBJECT_ID_EFFECTOR);
		
		effectPer.onStart(buffInfo);
		
		verify(effected, times(1)).setCurrentHp(TARGET_PER_HP_FIRST);
	}
	
	@Test
	void test_full_percentage_healed_first_target_self() {
		when(buffInfo.getEffected()).thenReturn(effector);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getIndex()).thenReturn(INDEX_FIRST);
		when(effector.getMaxHp()).thenReturn(MAXIMUM_HP);
		when(effector.getMaxRecoverableHp()).thenReturn(MAXIMUM_HP);
		when(effector.getCurrentHp()).thenReturn(CURRENT_HP);
		when(effector.getObjectId()).thenReturn(OBJECT_ID_EFFECTOR);
		
		effectPer.onStart(buffInfo);
		
		verify(effector, times(1)).setCurrentHp(TARGET_PER_HP_FIRST);
	}
	
	@Test
	void test_full_percentage_healed_third_target() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getIndex()).thenReturn(INDEX_THIRD);
		when(effected.getMaxHp()).thenReturn(MAXIMUM_HP);
		when(effected.getMaxRecoverableHp()).thenReturn(MAXIMUM_HP);
		when(effected.getCurrentHp()).thenReturn(CURRENT_HP);
		when(effected.getObjectId()).thenReturn(OBJECT_ID_EFFECTED);
		when(effector.getObjectId()).thenReturn(OBJECT_ID_EFFECTOR);
		
		effectPer.onStart(buffInfo);
		
		verify(effected, times(1)).setCurrentHp(TARGET_PER_HP_THIRD);
	}
	
	@Test
	void test_full_difference_healed_first_target() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getIndex()).thenReturn(INDEX_FIRST);
		when(effected.getMaxRecoverableHp()).thenReturn(MAXIMUM_HP);
		when(effected.getCurrentHp()).thenReturn(CURRENT_HP);
		when(effected.getObjectId()).thenReturn(OBJECT_ID_EFFECTED);
		when(effector.getObjectId()).thenReturn(OBJECT_ID_EFFECTOR);
		
		effectDiff.onStart(buffInfo);
		
		verify(effected, times(1)).setCurrentHp(TARGET_DIFF_HP_FIRST);
	}
	
	@Test
	void test_full_difference_healed_third_target() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getIndex()).thenReturn(INDEX_THIRD);
		when(effected.getMaxRecoverableHp()).thenReturn(MAXIMUM_HP);
		when(effected.getCurrentHp()).thenReturn(CURRENT_HP);
		when(effected.getObjectId()).thenReturn(OBJECT_ID_EFFECTED);
		when(effector.getObjectId()).thenReturn(OBJECT_ID_EFFECTOR);
		
		effectDiff.onStart(buffInfo);
		
		verify(effected, times(1)).setCurrentHp(TARGET_DIFF_HP_THIRD);
	}
}
