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
package com.l2jserver.datapack.ai.npc.Teleporter;

import java.util.Calendar;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class Teleporter extends AbstractNpcAI {
	
	public static final String fnPath = "data/html/teleporter/";
	
	private static final String _fnFlagMan = fnPath + "flagman.htm";
	private static final String _fnNobless = fnPath + "fornobless.htm";
	private static final String _fnNoNobless = fnPath + "fornonobless.htm";
	private static final String _fnNoNoblessItem = fnPath + "fornonoblessitem.htm";
	public static final String _fnQ194NoTeleport = fnPath + "q194_noteleport.htm";
	
	private static int i_ai4;
	
	private static final int ADENA = 57;
	private static final int OLYMPIAD_TOKEN = 13722;
	private static final int TRAVELER_TOKEN = 8542;
	private static final int ANCIENT_ADENA = 5575;
	
	private static final int Q00255_TUTORIAL = 255;
	
	private static final int MS_ASK_RACE_TRACK = 255;
	private static final int MS_ASK_NOBLE = -19;
	private static final int MS_ASK_NOBLE_ITEM = -20;
	private static final int MS_ASK_NOBLE_ADENA = -21;
	private static final int MS_ASK_BACK = -22;
	private static final int MS_ASK_NEWBIE = 30;
	private static final int MS_ASK_POINT = -31;
	private static final int MS_ASK_FANTASY_ISLE = -1816;
	private static final int MS_ASK_GLUDIO_AIRSHIP = -1055;
	private static final int MS_ASK_TALKING_ISLE = -1056;
	private static final int MS_ASK_ALEGRIA = 20003;
	private static final int MS_ASK_MULTISELL = -303;
	
	private final String _npcName = getClass().getSimpleName().toLowerCase();
	
	protected TelPosList[] position;
	protected TelPosList[] positionNoblessNeedItemField;
	protected TelPosList[] positionNoblessNoItemField;
	protected TelPosList[] positionNewbie;
	protected TelPosList[] positionPoint;
	
	public Teleporter(int npcId) {
		bindFirstTalk(npcId);
		bindTeleportRequest(npcId);
		bindMenuSelected(npcId);
	}
	
	public Teleporter(int... npcIds) {
		bindStartNpc(npcIds);
		bindFirstTalk(npcIds);
		bindTeleportRequest(npcIds);
		bindMenuSelected(npcIds);
	}
	
	public enum TeleType {
		NORMAL,
		NOBLESS,
		NEWBIE,
		SSQ
	}
	
	public record TelPosList(int locId, Location loc, long ammount, int type) {
		public TelPosList(int locId, Location loc, long ammount) {
			this(locId, loc, ammount, 0);
		}
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (talker.isCombatFlagEquipped()) {
			showPage(talker, _fnFlagMan);
		} else {
			if (talker.getKarma() > 0) {
				showPage(talker, fnPath + _npcName + "003.htm");
			} else {
				showPage(talker, fnPath + _npcName + "001.htm");
			}
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		final var npc = event.npc();
		
		if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
			showPage(talker, _fnQ194NoTeleport);
		} else {
			teleportFStr(talker, npc, position, ADENA, TeleType.NORMAL);
		}
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_RACE_TRACK -> {
				int i3 = npc.getId();
				
				final var q255 = QuestManager.getInstance().getQuest(Q00255_TUTORIAL);
				final var qs255 = q255.getQuestState(talker, true);
				
				int i0 = qs255.getMemoStateEx(1);
				i0 = i0 % 100;
				if (i0 >= 95 || i0 < 0) {
					i0 = 0;
				}
				if (reply == 1 && i3 == 30256) {
					qs255.setMemoStateEx(1, i0 + 100);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 2 && i3 == 30320) {
					qs255.setMemoStateEx(1, i0 + 200);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 3 && i3 == 30059) {
					qs255.setMemoStateEx(1, i0 + 300);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 4 && i3 == 30080) {
					qs255.setMemoStateEx(1, i0 + 400);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 5 && i3 == 30177) {
					qs255.setMemoStateEx(1, i0 + 500);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 6 && i3 == 30233) {
					qs255.setMemoStateEx(1, i0 + 600);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 7 && i3 == 30848) {
					qs255.setMemoStateEx(1, i0 + 700);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 8 && i3 == 30899) {
					qs255.setMemoStateEx(1, i0 + 800);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 9 && i3 == 31210) {
					i_ai4 = i0;
					// myself.CheckCursedUser(talker);
					if (!talker.isCursedWeaponEquipped()) {
						if (i_ai4 < 100) {
							qs255.setMemoStateEx(1, i_ai4 + 900);
							talker.teleToLocation(12661, 181687, -3540);
							i_ai4 = 0;
							return;
						}
					} else {
						showPage(talker, "race_gatekeeper1010.htm");
					}
				}
				if (reply == 10 && i3 == 31275) {
					qs255.setMemoStateEx(1, i0 + 1000);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 11 && i3 == 31320) {
					qs255.setMemoStateEx(1, i0 + 1100);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
				if (reply == 12 && i3 == 31964) {
					qs255.setMemoStateEx(1, i0 + 1200);
					talker.teleToLocation(12661, 181687, -3540);
					return;
				}
			}
			case MS_ASK_NOBLE -> {
				if (talker.isNoble()) {
					showPage(talker, _fnNobless);
				} else {
					showPage(talker, _fnNoNobless);
				}
			}
			case MS_ASK_NOBLE_ITEM -> {
				if (reply == 2) {
					if (talker.isNoble()) {
						if (getQuestItemsCount(talker, OLYMPIAD_TOKEN) != 0) {
							if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
								showPage(talker, _fnQ194NoTeleport);
							} else {
								teleportFStr(talker, npc, positionNoblessNeedItemField, OLYMPIAD_TOKEN, TeleType.NOBLESS);
							}
						} else {
							showPage(talker, _fnNoNoblessItem);
						}
					} else {
						showPage(talker, _fnNoNobless);
					}
				}
			}
			case MS_ASK_NOBLE_ADENA -> {
				if (talker.isNoble()) {
					if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
						showPage(talker, _fnQ194NoTeleport);
					} else {
						if (reply == 2 && talker.isNoble()) {
							teleportFStr(talker, npc, positionNoblessNoItemField, ADENA, TeleType.NOBLESS);
						}
					}
				} else {
					showPage(talker, _fnNoNobless);
				}
			}
			case MS_ASK_BACK -> {
				showPage(talker, fnPath + _npcName + "001.htm");
			}
			case MS_ASK_NEWBIE -> {
				if (getQuestItemsCount(talker, TRAVELER_TOKEN) == 0) {
					showPage(talker, fnPath + _npcName + "010.htm");
				} else {
					if (talker.getLevel() >= 20) {
						showPage(talker, fnPath + _npcName + "011.htm");
					} else {
						if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
							showPage(talker, _fnQ194NoTeleport);
						} else {
							teleportFStr(talker, npc, positionNewbie, TRAVELER_TOKEN, TeleType.NEWBIE);
						}
					}
				}
			}
			case MS_ASK_POINT -> {
				if (talker.getLevel() >= 20 || talker.getClassId().level() > 1) {
					showPage(talker, fnPath + _npcName + "005.htm");
				} else {
					if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
						showPage(talker, _fnQ194NoTeleport);
					} else {
						teleportFStr(talker, npc, positionPoint, ADENA, TeleType.NORMAL);
					}
				}
			}
			case MS_ASK_FANTASY_ISLE -> {
				if (reply == 3) {
					int i3 = npc.getId();
					
					final var q255 = QuestManager.getInstance().getQuest(Q00255_TUTORIAL);
					final var qs255 = q255.getQuestState(talker, true);
					
					int i0 = qs255.getMemoStateEx(1);
					int i1 = i0 / 1000000;
					if (i1 >= 99 || i1 < 0) {
						i1 = 0;
					}
					if (i3 == 30256) {
						i1 = 1000000;
					}
					if (i3 == 30320) {
						i1 = 2000000;
					}
					if (i3 == 30059) {
						i1 = 3000000;
					}
					if (i3 == 30080) {
						i1 = 4000000;
					}
					if (i3 == 30177) {
						i1 = 5000000;
					}
					if (i3 == 30233) {
						i1 = 6000000;
					}
					if (i3 == 30848) {
						i1 = 7000000;
					}
					if (i3 == 30899) {
						i1 = 8000000;
					}
					if (i3 == 31210) {
						i1 = 9000000;
					}
					if (i3 == 31275) {
						i1 = 10000000;
					}
					if (i3 == 31320) {
						i1 = 11000000;
					}
					if (i3 == 31964) {
						i1 = 12000000;
					}
					int i2 = 0;
					i2 = i0 / 1000000;
					if (i2 > 0) {
						i2 = i2 % 100;
						i2 = i2 * 1000000;
					} else {
						i2 = 0;
					}
					if (i0 < 0) {
						qs255.setMemoStateEx(1, i1);
					} else {
						qs255.setMemoStateEx(1, (i0 - i2) + i1);
					}
					if (getRandom(3) < 1) {
						talker.teleToLocation(-58752, -56898, -2032);
					} else {
						if (getRandom(2) < 1) {
							talker.teleToLocation(-59722, -57866, -2032);
						} else {
							talker.teleToLocation(-60695, -56894, -2032);
						}
					}
				}
			}
			case MS_ASK_GLUDIO_AIRSHIP -> {
				if (reply == 0) {
					talker.teleToLocation(-149406, 255247, -85);
				}
			}
			case MS_ASK_TALKING_ISLE -> {
				if (reply == 0) {
					talker.teleToLocation(-84752, 243122, -3728);
				}
			}
			case MS_ASK_ALEGRIA -> {
				if (reply == 1) {
					QuestManager.getInstance().getQuest("CharacterBirthday").onTalk((L2Npc) npc, talker);
				}
			}
			case MS_ASK_MULTISELL -> {
				MultisellData.getInstance().separateAndSend(002, talker, (L2Npc) npc, false);
			}
		}
	}
	
	protected void teleportFStr(L2PcInstance player, L2Character npc, TelPosList[] teleList, int itemId, TeleType type) {
		final var html = new StringBuilder("<html><body>&$556;<br><br>");
		for (TelPosList tele : teleList) {
			final var loc = tele.loc();
			var ammount = tele.ammount();
			
			if (type == TeleType.NORMAL) {
				final var cal = Calendar.getInstance();
				if ((cal.get(Calendar.HOUR_OF_DAY) >= 20) && ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))) {
					ammount /= 2;
				}
				
				if ((player.getLevel() < 41)) {
					ammount = 0;
				}
			}
			
			if (ammount == 0) {
				html.append("<a action=\"bypass -h teleport " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + itemId + " " + ammount + "\" msg=\"811;" + getNpcString(tele.locId()) + "\">" + getNpcString(tele.locId()) + "</a><br1>");
			} else {
				html.append("<a action=\"bypass -h teleport " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + itemId + " " + ammount + "\" msg=\"811;" + getNpcString(tele.locId()) + "\">" + getNpcString(tele.locId()) + " - " + ammount + " " + getItemName(itemId) + "</a><br1>");
			}
		}
		html.append("</body></html>");
		
		player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html.toString()));
	}
	
	private static final String getItemName(int id) {
		return switch (id) {
			case ADENA -> "Adena";
			case OLYMPIAD_TOKEN -> "Olympiad Token";
			case TRAVELER_TOKEN -> "Newbie Travel Token";
			case ANCIENT_ADENA -> "Ancient Adena";
			default -> null;
		};
	}
	
	private static final String getNpcString(int id) {
		return "<fstring>" + id + "</fstring>";
	}
}