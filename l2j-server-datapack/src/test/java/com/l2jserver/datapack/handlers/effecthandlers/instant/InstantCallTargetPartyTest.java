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
package com.l2jserver.datapack.handlers.effecthandlers.instant;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * Instant Call Target Party effect test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
public class InstantCallTargetPartyTest {
	
	@Mock
	private BuffInfo buffInfo;
	@Mock
	private L2Character effected;
	@Mock
	private L2Party party;
	@Mock
	private L2PcInstance effectedPlayer;
	@Mock
	private L2PcInstance partyMember;
	
	private static InstantCallTargetParty effect;
	
	@BeforeAll
	static void init() {
		final var set = new StatsSet(Map.of("name", "InstantBetray"));
		final var params = new StatsSet(Map.of("chance", "80", "time", "30"));
		effect = new InstantCallTargetParty(null, null, set, params);
	}
	
	@Test
	void test_target_not_in_party() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isInParty()).thenReturn(false);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	void test_target_in_party_cannot_summon_party_member() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isInParty()).thenReturn(true);
		when(effected.getParty()).thenReturn(party);
		when(party.getMembers()).thenReturn(List.of(effectedPlayer, partyMember));
		when(effected.getActingPlayer()).thenReturn(effectedPlayer);
		when(effectedPlayer.canSummonTarget(partyMember)).thenReturn(false);
		when(effectedPlayer.canSummonTarget(effectedPlayer)).thenReturn(false);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	void test_target_in_party_can_summon_party_member() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isInParty()).thenReturn(true);
		when(effected.getParty()).thenReturn(party);
		when(party.getMembers()).thenReturn(List.of(effectedPlayer, partyMember));
		when(effected.getActingPlayer()).thenReturn(effectedPlayer);
		when(effectedPlayer.canSummonTarget(partyMember)).thenReturn(true);
		when(effectedPlayer.canSummonTarget(effectedPlayer)).thenReturn(false);
		
		effect.onStart(buffInfo);
	}
}
