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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2TrapInstance;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * TrapRemove effect unit tests
 * @author Kita (Noé Caratini)
 */
@ExtendWith(MockitoExtension.class)
class TrapRemoveTest {
	
	@Mock
	private StatsSet statsSet;
	@Mock
	private StatsSet params;
	
	@Mock
	private BuffInfo buffInfo;
	@Mock
	private L2TrapInstance trap;
	@Mock
	private L2Character character;
	
	private TrapRemove effect;
	
	@BeforeEach
	void setUp() {
		when(statsSet.getString("name")).thenReturn("TestTrapRemove");
		when(params.getInt("power")).thenReturn(78);
		
		effect = new TrapRemove(null, null, statsSet, params);
	}
	
	@Test
	void testCreatingEffectWithEmptyParamsShouldThrowException() {
		assertThatThrownBy(() -> new TrapRemove(null, null, statsSet, StatsSet.EMPTY_STATSET))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void testTrapRemoveEffectShouldBeInstant() {
		assertThat(effect.isInstant()).isTrue();
	}
	
	@Test
	void testTrapRemoveEffectShouldNotApplyToNonTrapCharacter() {
		when(buffInfo.getEffected()).thenReturn(character);
		
		effect.onStart(buffInfo);
		
		verifyNoInteractions(trap);
	}
	
	@Test
	void testTrapRemoveEffectShouldNotApplyToDeadTrap() {
		when(buffInfo.getEffected()).thenReturn(trap);
		when(trap.isAlikeDead()).thenReturn(true);
		
		effect.onStart(buffInfo);
		
		verifyNoMoreInteractions(trap);
	}
	
	@Test
	void testTrapRemoveEffectShouldNotApplyToTrapNotVisibleToEffector() {
		when(buffInfo.getEffector()).thenReturn(character);
		when(buffInfo.getEffected()).thenReturn(trap);
		when(trap.isAlikeDead()).thenReturn(false);
		when(trap.isVisibleFor(character)).thenReturn(false);
		
		effect.onStart(buffInfo);
		
		verifyNoMoreInteractions(trap);
	}
	
	@Test
	void testTrapRemoveEffectShouldNotApplyToTrapWithPowerHigherThanEffect() {
		when(buffInfo.getEffector()).thenReturn(character);
		when(buffInfo.getEffected()).thenReturn(trap);
		when(trap.isAlikeDead()).thenReturn(false);
		when(trap.isVisibleFor(character)).thenReturn(true);
		when(trap.getLevel()).thenReturn(85);
		
		effect.onStart(buffInfo);
		
		verifyNoMoreInteractions(trap);
	}
	
	@Test
	void testTrapRemoveEffectShouldUnsummonTrap() {
		when(buffInfo.getEffector()).thenReturn(character);
		when(buffInfo.getEffected()).thenReturn(trap);
		when(trap.isAlikeDead()).thenReturn(false);
		when(trap.isVisibleFor(character)).thenReturn(true);
		when(trap.getLevel()).thenReturn(40);
		
		effect.onStart(buffInfo);
		
		verify(trap).unSummon();
	}
}
