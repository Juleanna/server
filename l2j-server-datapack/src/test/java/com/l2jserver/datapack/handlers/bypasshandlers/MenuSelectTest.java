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
package com.l2jserver.datapack.handlers.bypasshandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * Menu Select bypass test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
class MenuSelectTest {
	
	private static final IBypassHandler BYPASS_HANDLER = new MenuSelect();
	
	@Mock
	private L2PcInstance player;
	
	@Mock
	private L2NpcInstance npc;
	
	@Mock
	private EventDispatcher eventDispatcher;
	
	@ParameterizedTest
	@MethodSource("bypassProvider")
	void testUseBypass(String command, int ask, int reply, boolean expected) {
		try (var eventDispatcherStatic = mockStatic(EventDispatcher.class)) {
			eventDispatcherStatic.when(() -> EventDispatcher.getInstance()).thenReturn(eventDispatcher);
			
			final var result = BYPASS_HANDLER.useBypass(command, player, npc);
			
			assertEquals(expected, result);
			
			if (expected) {
				final var captor = ArgumentCaptor.forClass(PlayerMenuSelected.class);
				
				verify(eventDispatcher).notifyEventAsync(captor.capture(), eq(npc));
				
				final var event = captor.getValue();
				assertEquals(ask, event.ask());
				assertEquals(reply, event.reply());
				assertEquals(player, event.player());
				assertEquals(npc, event.npc());
			} else {
				verify(eventDispatcher, never()).notifyEventAsync(any(PlayerMenuSelected.class), any());
			}
		}
	}
	
	private static Stream<Object[]> bypassProvider() {
		// @formatter:off
		return Stream.of(
			new Object[] { "menu_select?ask=123&reply=456", 123, 456, true },
			new Object[] { "menu_select?reply=123&ask=456", 123, 456, false },
			new Object[] { "menu_select? ask=123&reply=456", 123, 456, false },
			new Object[] { "invalid_bypass", 0, 0, false },
			new Object[] { "menu_select?ask=123", 0, 0, false },
			new Object[] { "menu_select?reply=456", 0, 0, false },
			new Object[] { "menu_select?ask=abc?reply=def", 0, 0, false });
		// @formatter:on
	}
}
