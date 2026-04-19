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
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.Teleporter.Havarti;

import com.l2jserver.datapack.ai.npc.Teleporter.Teleporter;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Havarti extends Teleporter {
	
	private static final int npcId = 30486;
	
	public Havarti() {
		super(npcId);
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		
		final var i6 = getRandom(100);
		if (i6 > 90) {
			talker.teleToLocation(17616, 115436, -6582);
		} else {
			if (i6 > 80) {
				talker.teleToLocation(17824, 115426, -6582);
			} else {
				if (i6 > 70) {
					talker.teleToLocation(17720, 115312, -6582);
				} else {
					if (i6 > 60) {
						talker.teleToLocation(17720, 115536, -6582);
					} else {
						if (i6 > 50) {
							talker.teleToLocation(17678, 115431, -6582);
						} else {
							if (i6 > 40) {
								talker.teleToLocation(17767, 115427, -6582);
							} else {
								if (i6 > 20) {
									talker.teleToLocation(17718, 115374, -6582);
								} else {
									talker.teleToLocation(17720, 115472, -6582);
								}
							}
						}
					}
				}
			}
		}
	}
}