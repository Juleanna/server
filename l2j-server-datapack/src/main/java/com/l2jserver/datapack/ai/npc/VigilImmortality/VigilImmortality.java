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
package com.l2jserver.datapack.ai.npc.VigilImmortality;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class VigilImmortality extends AbstractNpcAI {
	
	private static final int npcId = 32539;
	
	private static final int MS_ASK_WITHSTAND_GAZE = -1002;
	private static final int MS_ASKAVOID_GAZE = -1004;
	private static final int MS_ASK_ENTER_SEED = -1006;
	private static final int MS_ASK_HEART_OF_INFINITY = -1008;
	
	private static final int REPLY_WITHSTAND_GAZE = 1003;
	private static final int REPLY_AVOID_GAZE = 1005;
	private static final int REPLY_ENTER_SEED = 1007;
	private static final int REPLY_HEART_OF_INFINITY = 1009;
	
	private static final Location loc01 = new Location(-179537, 209551, -15504);
	private static final Location loc02 = new Location(-179779, 212540, -15520);
	private static final Location loc03 = new Location(-177028, 211135, -15520);
	private static final Location loc04 = new Location(-176355, 208043, -15520);
	private static final Location loc05 = new Location(-179284, 205990, -15520);
	private static final Location loc06 = new Location(-182268, 208218, -15520);
	private static final Location loc07 = new Location(-182069, 211140, -15520);
	private static final Location loc08 = new Location(-176036, 210002, -11948);
	private static final Location loc09 = new Location(-176039, 208203, -11949);
	private static final Location loc10 = new Location(-183288,	208205,	-11939);
	private static final Location loc11 = new Location(-183290,	210004,	-11939);
	private static final Location loc12 = new Location(-187776, 205696,	-9536);
	private static final Location loc13 = new Location(-186327,	208286,	-9536);
	private static final Location loc14 = new Location(-184429,	211155,	-9536);
	private static final Location loc15 = new Location(-182811,	213871,	-9504);
	private static final Location loc16 = new Location(-180921,	216789,	-9536);
	private static final Location loc17 = new Location(-177264,	217760,	-9536);
	private static final Location loc18 = new Location(-173727,	218169,	-9536);
	
	public VigilImmortality() {
		bindFirstTalk(npcId);
		bindMenuSelected(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		showPage(talker, "vigil_immortality001.htm");
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		
		switch (ask) {
			case MS_ASK_WITHSTAND_GAZE -> {
				switch (reply) {
					case REPLY_WITHSTAND_GAZE -> {
						final var i0 = 1; // GraciaSeedsManager.getInstance().getSoIState();
						switch (i0) {
							case 1 -> {
								showPage(talker, "vigil_immortality003.htm");
							}
							case 2, 5 -> {
								showPage(talker, "vigil_immortality002b.htm");
							}
							default -> {
								showPage(talker, "vigil_immortality002a.htm");
							}
						}
					}
				}
			}
			case MS_ASKAVOID_GAZE -> {
				switch (reply) {
					case REPLY_AVOID_GAZE -> {
						talker.teleToLocation(-212836, 209824, 4288);
					}
				}
			}
			case MS_ASK_ENTER_SEED -> {
				switch (reply) {
					case REPLY_ENTER_SEED -> {
						final var i0 = 1; // GraciaSeedsManager.getInstance().getSoIState();
						switch (i0) {
							case 3 -> {
								Location loc = null;
								
								final var i1 = getRandom(18) + 1;
								switch (i1) {
									case 1 -> {
										loc = loc01;
									}
									case 2 -> {
										loc = loc02;
									}
									case 3 -> {
										loc = loc03;
									}
									case 4 -> {
										loc = loc04;
									}
									case 5 -> {
										loc = loc05;
									}
									case 6 -> {
										loc = loc06;
									}
									case 7 -> {
										loc = loc07;
									}
									case 8 -> {
										loc = loc08;
									}
									case 9 -> {
										loc = loc09;
									}
									case 10 -> {
										loc = loc10;
									}
									case 11 -> {
										loc = loc11;
									}
									case 12 -> {
										loc = loc12;
									}
									case 13 -> {
										loc = loc13;
									}
									case 14 -> {
										loc = loc14;
									}
									case 15 -> {
										loc = loc15;
									}
									case 16 -> {
										loc = loc16;
									}
									case 17 -> {
										loc = loc17;
									}
									case 18 -> {
										loc = loc18;
									}
								}
								
								talker.teleToLocation(loc);
							}
							case 4 -> {
								Location loc = null;
								
								final var i1 = getRandom(7) + 1;
								switch (i1) {
									case 1 -> {
										loc = loc01;
									}
									case 2 -> {
										loc = loc02;
									}
									case 3 -> {
										loc = loc03;
									}
									case 4 -> {
										loc = loc04;
									}
									case 5 -> {
										loc = loc05;
									}
									case 6 -> {
										loc = loc06;
									}
									case 7 -> {
										loc = loc07;
									}
								}
								
								talker.teleToLocation(loc);
							}
							default -> {
								showPage(talker, "vigil_immortality003.htm");
							}
						}
					}
				}
			}
			case MS_ASK_HEART_OF_INFINITY -> {
				switch (reply) {
					case REPLY_HEART_OF_INFINITY -> {
						final var i0 = 1; // GraciaSeedsManager.getInstance().getSoIState();
						switch (i0) {
							case 2 -> {
								//myself.InstantZone_Enter(talker, inzone_id1, @eIZ_ET_COMMAND_CHANNEL);
							}
							case 5 -> {
								//myself.InstantZone_Enter(talker, inzone_id2, @eIZ_ET_COMMAND_CHANNEL);
							}
							default -> {
								showPage(talker, "vigil_immortality003.htm");
							}
						}
					}
				}
			}
		}
	}
}