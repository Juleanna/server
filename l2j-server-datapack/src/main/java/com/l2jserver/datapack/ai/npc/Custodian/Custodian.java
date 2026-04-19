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
package com.l2jserver.datapack.ai.npc.Custodian;

import static com.l2jserver.gameserver.config.Configuration.clanhall;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.ClanHallManager;
import com.l2jserver.gameserver.instancemanager.ClanHallSiegeManager;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.entity.ClanHall;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.network.serverpackets.AgitDecoInfo;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class Custodian extends AbstractNpcAI {
	
	private static final String fnHi = "data/html/clanHallManager/black001.htm";
	private static final String fnNotMyLord = "data/html/clanHallManager/black002.htm";
	private static final String fnWarehouse = "data/html/clanHallManager/agitwarehouse.htm";
	private static final String fnWarehouse_b = "data/html/clanHallManager/agitwarehouse_b.htm";
	private static final String fnManage = "data/html/clanHallManager/agitdecomanage.htm";
	
	private static String fnManageRegen = "agitdeco_ar01.htm";
	private static String fnManageEtc = "agitdeco_ae01.htm";
	private static String fnManageDeco = "agitdeco_ad01.htm";
	
	private static final String fnManageAgitDeco = "data/html/clanHallManager/agitdeco__";
	private static final String fnBanish = "data/html/clanHallManager/agitbanish.htm";
	private static final String fnAfterBanish = "data/html/clanHallManager/agitafterbanish.htm";
	private static final String fnDoor = "data/html/clanHallManager/agitdoor.htm";
	private static final String fnAfterDoorOpen = "data/html/clanHallManager/agitafterdooropen.htm";
	private static final String fnAfterDoorClose = "data/html/clanHallManager/agitafterdoorclose.htm";
	private static final String fnDecoFunction = "data/html/clanHallManager/agitdecofunction.htm";
	private static final String fnAfterSetDeco = "data/html/clanHallManager/agitaftersetdeco.htm";
	private static final String fnAfterResetDeco = "data/html/clanHallManager/agitafterresetdeco.htm";
	private static final String fnDecoAlreadySet = "data/html/clanHallManager/agitdecoalreadyset.htm";
	private static final String fnDecoReset = "data/html/clanHallManager/agitresetdeco.htm";
	private static final String fnAgitBuff = "data/html/clanHallManager/agitbuff";
	private static final String fnAfterBuff = "data/html/clanHallManager/agitafterbuff.htm";
	private static final String fnNoAuthority = "data/html/clanHallManager/noauthority.htm";
	private static final String fnNotEnoughAdena = "data/html/clanHallManager/agitnotenoughadena.htm";
	private static final String fnFuncDisabled = "data/html/clanHallManager/agitfuncdisabled.htm";
	private static final String fnNotEnoughMP = "data/html/clanHallManager/agitnotenoughmp.htm";
	private static final String fnCostFail = "data/html/clanHallManager/agitcostfail.htm";
	private static final String fnFlagMan = "data/html/clanHallManager/flagman.htm";
	
	private static final int ADENA = 57;
	
	private static final int MS_ASK_BACK = 0;
	private static final int MS_ASK_MAIN_CHAT = -201;
	private static final int MS_ASK_OPENCLOSE_DOORS = -203;
	private static final int MS_ASK_DISMISS = -219;
	private static final int MS_ASK_SUPPORT_MAGIC = -208;
	private static final int MS_ASK_RESETUPDATE_FUNCTION = -270;
	private static final int MS_ASK_RESETUPDATE_CONFIRM = -271;
	
	private static final int REPLY_BACK = 0;
	private static final int REPLY_DOOR_CHAT = 1;
	private static final int REPLY_DISMISS_CHAT = 2;
	private static final int REPLY_FUNCTION_CHAT = 3;
	private static final int REPLY_AGIT_COST_INFO_CHAT = 4;
	private static final int REPLY_SET_FUNCTIONS_CHAT = 5;
	private static final int REPLY_WAREHOUSE_CHAT = 6;
	private static final int REPLY_SUPPORT_CHAT = 7;
	private static final int REPLY_ITEM_CREATION = 12;
	private static final int REPLY_MANAGE_REGEN_CHAT = 51;
	private static final int REPLY_MANAGE_ETC_CHAT = 52;
	private static final int REPLY_MANAGE_DECO_CHAT = 53;
	private static final int REPLY_DOOR_OPEN = 1;
	private static final int REPLY_DOOR_CLOSE = 2;
	private static final int REPLY_DISMISS = 1;
	
	private int _clanHallId = -1;
	
	protected TelPosList[] position1;
	protected TelPosList[] position2;
	
	protected L2BuyList sellList0;
	protected L2BuyList sellList1;
	protected L2BuyList sellList2;
	protected L2BuyList sellList3;
	protected L2BuyList sellList4;
	protected L2BuyList sellList5;
	
	private static Map<Integer, SkillHolder> _supportList = new HashMap<>();
	
	static {
		_supportList.put(284557313, new SkillHolder(4342, 1));
		_supportList.put(284622849, new SkillHolder(4343, 1));
		_supportList.put(284688385, new SkillHolder(4344, 1));
		_supportList.put(284819457, new SkillHolder(4346, 1));
		_supportList.put(284753921, new SkillHolder(4345, 1));
		_supportList.put(284557314, new SkillHolder(4342, 2));
		_supportList.put(284622851, new SkillHolder(4343, 3));
		_supportList.put(284688387, new SkillHolder(4344, 3));
		_supportList.put(284819460, new SkillHolder(4346, 4));
		_supportList.put(284753923, new SkillHolder(4345, 3));
		_supportList.put(284884994, new SkillHolder(4347, 2));
		_supportList.put(285016065, new SkillHolder(4349, 1));
		_supportList.put(285081601, new SkillHolder(4350, 1));
		_supportList.put(284950530, new SkillHolder(4348, 2));
		_supportList.put(285147138, new SkillHolder(4351, 2));
		_supportList.put(285212673, new SkillHolder(4352, 1));
		_supportList.put(285278210, new SkillHolder(4353, 2));
		_supportList.put(285605889, new SkillHolder(4358, 1));
		_supportList.put(285343745, new SkillHolder(4354, 1));
		_supportList.put(285409281, new SkillHolder(4355, 1));
		_supportList.put(285474817, new SkillHolder(4356, 1));
		_supportList.put(285540353, new SkillHolder(4357, 1));
		_supportList.put(285671425, new SkillHolder(4359, 1));
		_supportList.put(285736961, new SkillHolder(4360, 1));
		_supportList.put(284884998, new SkillHolder(4347, 6));
		_supportList.put(285016066, new SkillHolder(4349, 2));
		_supportList.put(285081604, new SkillHolder(4350, 4));
		_supportList.put(284950534, new SkillHolder(4348, 6));
		_supportList.put(285147142, new SkillHolder(4351, 6));
		_supportList.put(285212674, new SkillHolder(4352, 2));
		_supportList.put(285278214, new SkillHolder(4353, 6));
		_supportList.put(285605891, new SkillHolder(4358, 3));
		_supportList.put(285343748, new SkillHolder(4354, 4));
		_supportList.put(285409283, new SkillHolder(4355, 3));
		_supportList.put(285474819, new SkillHolder(4356, 3));
		_supportList.put(285540354, new SkillHolder(4357, 2));
		_supportList.put(285671427, new SkillHolder(4359, 3));
		_supportList.put(285736963, new SkillHolder(4360, 3));
		
		_supportList.put(286195713, new SkillHolder(4367, 1));
		_supportList.put(286261249, new SkillHolder(4368, 1));
		_supportList.put(286326785, new SkillHolder(4369, 1));
		_supportList.put(286392321, new SkillHolder(4370, 1));
		_supportList.put(286457857, new SkillHolder(4371, 1));
		_supportList.put(286588929, new SkillHolder(4373, 1));
		_supportList.put(286654465, new SkillHolder(4374, 1));
	}
	
	public Custodian(int npcId) {
		bindStartNpc(npcId);
		bindFirstTalk(npcId);
		bindTeleportRequest(npcId);
		bindMenuSelected(npcId);
		bindSpawn(npcId);
	}
	
	public record TelPosList(int locId, Location loc, long ammount, int type) {
		public TelPosList(int locId, Location loc, long ammount) {
			this(locId, loc, ammount, 0);
		}
	}
	
	@Override
	public void onSpawn(L2Npc npc) {
		startQuestTimer("1077", 1000, npc, null);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance talker) {
		if (event.equalsIgnoreCase("1077")) {
			final var clanHall = getClanHall(npc);
			
			if (clanHall.getFunction(ClanHall.FUNC_SUPPORT) != null && clanHall.getFunction(ClanHall.FUNC_SUPPORT).getLvl() > 0) {
				final var i0 = 286130177 + ((clanHall.getFunction(ClanHall.FUNC_SUPPORT).getLvl() * 256) * 256);
				final var skill = _supportList.get(i0).getSkill();
				if ((skill.getMpConsume1() + skill.getMpConsume2()) < npc.getCurrentMp()) {
					npc.setTarget(npc);
					npc.doCast(skill);
				}
			} else {
				final var skill = _supportList.get(286195713).getSkill();
				if ((skill.getMpConsume1() + skill.getMpConsume2()) < npc.getCurrentMp()) {
					npc.setTarget(npc);
					npc.doCast(skill);
				}
			}
			
			startQuestTimer("1077", 300000, npc, null);
		}
		
		return super.onEvent(event, npc, talker);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var clanHall = getClanHall(npc);
		
		setManageFn(clanHall);
		
		if (talker.isCombatFlagEquipped()) {
			showPage(talker, fnFlagMan);
		} else {
			if (talker.getClan() != null && clanHall.getOwnerId() == talker.getClanId()) {
				if (clanHall.getPaidUntil() > System.currentTimeMillis()) {
					showPage(talker, fnHi);
				} else {
					String html = getHtm(talker.getHtmlPrefix(), fnCostFail);
					html = html.replace("<?CostFailDayLeft?>", String.valueOf(8 - ((System.currentTimeMillis() - clanHall.getPaidUntil()) / 1000 / 60 / 60 / 24)));
					talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
				}
			} else {
				showPage(talker, fnNotMyLord);
			}
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var format = new SimpleDateFormat("dd/MM HH");
		final var clanHall = getClanHall(npc);
		
		switch (ask) {
			case MS_ASK_BACK -> {
				if ((talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					showPage(talker, fnHi);
				} else {
					showPage(talker, fnNotMyLord);
				}
			}
			case MS_ASK_MAIN_CHAT -> {
				switch (reply) {
					case REPLY_BACK -> {
						if ((talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							showPage(talker, fnHi);
						} else {
							showPage(talker, fnNotMyLord);
						}
					}
					case REPLY_DOOR_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							showPage(talker, fnDoor);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_DISMISS_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_DISMISS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							showPage(talker, fnBanish);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_FUNCTION_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							String html = getHtm(talker.getHtmlPrefix(), fnDecoFunction);
							if (clanHall.getFunction(ClanHall.FUNC_RESTORE_HP) == null) {
								html = html.replace("<?HPDepth?>", "0");
							} else {
								html = html.replace("<?HPDepth?>", String.valueOf(clanHall.getFunction(ClanHall.FUNC_RESTORE_HP).getLvl() * 20));
							}
							if (clanHall.getFunction(ClanHall.FUNC_RESTORE_MP) == null) {
								html = html.replace("<?MPDepth?>", "0");
							} else {
								html = html.replace("<?MPDepth?>", String.valueOf(clanHall.getFunction(ClanHall.FUNC_RESTORE_MP).getLvl() * 5));
							}
							if (clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP) == null) {
								html = html.replace("<?XPDepth?>", "0");
							} else {
								html = html.replace("<?XPDepth?>", String.valueOf(clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl() * 5));
							}
							if (clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS) == null) {
								html = html.replace("<?VPDepth?>", "0");
							} else {
								html = html.replace("<?VPDepth?>", String.valueOf(clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl() * 25));
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_AGIT_COST_INFO_CHAT -> {
						if ((talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							String html;
							if (clanHall.getLease() <= 0) {
								html = getHtm(talker.getHtmlPrefix(), fnWarehouse_b);
							} else {
								html = getHtm(talker.getHtmlPrefix(), fnWarehouse);
								html = html.replace("<?agit_lease?>", String.valueOf(clanHall.getLease()));
								html = html.replace("<?pay_time?>", format.format(clanHall.getPaidUntil()));
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_SET_FUNCTIONS_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_SET_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							showPage(talker, fnManage);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_WAREHOUSE_CHAT -> {
						if ((talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							String html = getHtm(talker.getHtmlPrefix(), fnWarehouse);
							html = html.replace("<?agit_lease?>", String.valueOf(clanHall.getLease()));
							html = html.replace("<?pay_time?>", format.format(clanHall.getPaidUntil()));
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_SUPPORT_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							if (clanHall.getFunction(ClanHall.FUNC_SUPPORT) == null) {
								showPage(talker, fnFuncDisabled);
							} else {
								String html = getHtm(talker.getHtmlPrefix(), fnAgitBuff + "_" + clanHall.getFunction(ClanHall.FUNC_SUPPORT).getLvl() + ".htm");
								html = html.replace("<?MPLeft?>", String.valueOf((int) npc.getCurrentMp()));
								talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_ITEM_CREATION -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							if (clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE) == null) {
								showPage(talker, fnFuncDisabled);
							} else {
								switch (clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE).getLvl()) {
									case 1 -> {
										showBuySell(talker, npc, sellList0);
									}
									case 2 -> {
										showBuySell(talker, npc, sellList1);
									}
									case 3 -> {
										showBuySell(talker, npc, sellList2);
									}
									case 4 -> {
										showBuySell(talker, npc, sellList3);
									}
									case 5 -> {
										showBuySell(talker, npc, sellList4);
									}
									case 6 -> {
										showBuySell(talker, npc, sellList5);
									}
								}
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_REGEN_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_SET_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							String html = getHtm(talker.getHtmlPrefix(), fnManageRegen);
							if (clanHall.getFunction(ClanHall.FUNC_RESTORE_HP) == null) {
								html = html.replace("<?HPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?HPCost?>", "");
								html = html.replace("<?HPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?HPReset?>", "");
							} else {
								html = html.replace("<?HPDepth?>", (clanHall.getFunction(ClanHall.FUNC_RESTORE_HP).getLvl() * 20) + "%");
								html = html.replace("<?HPCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_RESTORE_HP).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_RESTORE_HP, clanHall.getFunction(ClanHall.FUNC_RESTORE_HP).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?HPExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_RESTORE_HP).getEndTime()));
								html = html.replace("<?HPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_RESTORE_HP + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_RESTORE_MP) == null) {
								html = html.replace("<?MPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?MPCost?>", "");
								html = html.replace("<?MPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?MPReset?>", "");
							} else {
								html = html.replace("<?MPDepth?>", (clanHall.getFunction(ClanHall.FUNC_RESTORE_MP).getLvl() * 5) + "%");
								html = html.replace("<?MPCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_RESTORE_MP).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_RESTORE_MP, clanHall.getFunction(ClanHall.FUNC_RESTORE_MP).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?MPExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_RESTORE_MP).getEndTime()));
								html = html.replace("<?MPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_RESTORE_MP + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP) == null) {
								html = html.replace("<?XPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?XPCost?>", "");
								html = html.replace("<?XPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?XPReset?>", "");
							} else {
								html = html.replace("<?XPDepth?>", (clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl() * 5) + "%");
								html = html.replace("<?XPCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_RESTORE_EXP, clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?XPExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_RESTORE_EXP).getEndTime()));
								html = html.replace("<?XPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_RESTORE_EXP + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS) == null) {
								html = html.replace("<?VPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?VPCost?>", "");
								html = html.replace("<?VPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?VPReset?>", "");
							} else {
								html = html.replace("<?VPDepth?>", (clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl() * 25) + "%");
								html = html.replace("<?VPCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_DECO_CURTAINS, clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?VPExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getEndTime()));
								html = html.replace("<?VPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_DECO_CURTAINS + "000\">Deactivate</a>]");
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_ETC_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_SET_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							String html = getHtm(talker.getHtmlPrefix(), fnManageEtc);
							if (clanHall.getFunction(ClanHall.FUNC_TELEPORT) == null) {
								html = html.replace("<?TPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?TPCost?>", "");
								html = html.replace("<?TPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?TPReset?>", "");
							} else {
								html = html.replace("<?TPDepth?>", "Stage " + clanHall.getFunction(ClanHall.FUNC_TELEPORT).getLvl() * 1);
								html = html.replace("<?TPCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_TELEPORT).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_TELEPORT, clanHall.getFunction(ClanHall.FUNC_TELEPORT).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?TPExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_TELEPORT).getEndTime()));
								html = html.replace("<?TPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_TELEPORT + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_BROADCAST) == null) { // TODO: implement Agit_GetDecoLevel(decotype_broadcast)
								html = html.replace("<?BCDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?BCCost?>", "");
								html = html.replace("<?BCExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?BCReset?>", "");
							} else {
								html = html.replace("<?BCDepth?>", "Stage " + clanHall.getFunction(ClanHall.FUNC_BROADCAST).getLvl() * 1);
								html = html.replace("<?BCCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_BROADCAST).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_BROADCAST, clanHall.getFunction(ClanHall.FUNC_BROADCAST).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?BCExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_BROADCAST).getEndTime()));
								html = html.replace("<?BCReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_BROADCAST + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE) == null) {
								html = html.replace("<?ICDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?ICCost?>", "");
								html = html.replace("<?ICExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?ICReset?>", "");
							} else {
								html = html.replace("<?ICDepth?>", "Stage " + clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE).getLvl() * 1);
								html = html.replace("<?ICCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_ITEM_CREATE, clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?ICExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_ITEM_CREATE).getEndTime()));
								html = html.replace("<?ICReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_ITEM_CREATE + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_SUPPORT) == null) {
								html = html.replace("<?BFDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?BFCost?>", "");
								html = html.replace("<?BFExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?BFReset?>", "");
							} else {
								html = html.replace("<?BFDepth?>", "Stage " + clanHall.getFunction(ClanHall.FUNC_SUPPORT).getLvl() * 1);
								html = html.replace("<?BFCost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_SUPPORT).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_SUPPORT, clanHall.getFunction(ClanHall.FUNC_SUPPORT).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?BFExpire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_SUPPORT).getEndTime()));
								html = html.replace("<?BFReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_SUPPORT + "000\">Deactivate</a>]");
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_DECO_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_SET_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							String html = getHtm(talker.getHtmlPrefix(), fnManageDeco);
							if (clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS) == null) {
								html = html.replace("<?7_Depth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?7_Cost?>", "");
								html = html.replace("<?7_Expire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?7_Reset?>", "");
							} else {
								html = html.replace("<?7_Depth?>", "Stage " + clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl() * 1);
								html = html.replace("<?7_Cost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_DECO_CURTAINS, clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?7_Expire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_DECO_CURTAINS).getEndTime()));
								html = html.replace("<?7_Reset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_DECO_CURTAINS + "000\">Deactivate</a>]");
							}
							if (clanHall.getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM) == null) {
								html = html.replace("<?11_Depth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?11_Cost?>", "");
								html = html.replace("<?11_Expire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?11_Reset?>", "");
							} else {
								html = html.replace("<?11_Depth?>", "Stage " + clanHall.getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getLvl() * 1);
								html = html.replace("<?11_Cost?>", "(<font color=\"FFAABB\">" + clanHall.getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getLease() + "</font> Adena/ " + (getDecoDay(ClanHall.FUNC_DECO_FRONTPLATEFORM, clanHall.getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?11_Expire?>", "Withdraw the fee for the next time at " +  format.format(clanHall.getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getEndTime()));
								html = html.replace("<?11_Reset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + ClanHall.FUNC_DECO_FRONTPLATEFORM + "000\">Deactivate</a>]");
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
				}
			}
			case MS_ASK_OPENCLOSE_DOORS -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CH_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					switch (reply) {
						case REPLY_DOOR_OPEN -> {
							clanHall.openCloseDoors(true);
							showPage(talker, fnAfterDoorOpen);
						}
						case REPLY_DOOR_CLOSE -> {
							clanHall.openCloseDoors(false);
							showPage(talker, fnAfterDoorClose);
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_DISMISS -> {
				switch (reply) {
					case REPLY_DISMISS -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CH_DISMISS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							clanHall.banishForeigners();
							showPage(talker, fnAfterBanish);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
				}
			}
			case MS_ASK_RESETUPDATE_FUNCTION -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CH_SET_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					String html;
					
					final var i0 = reply / 1000;
					final var i1 = reply - (i0 * 1000);
					
					if (i1 == 0) {
						html = getHtm(talker.getHtmlPrefix(), fnDecoReset);
						html = html.replace("<?AgitDecoSubmit?>", String.valueOf(reply));
					} else {
						if ((clanHall.getFunction(i0) != null) && (clanHall.getFunction(i0).getLvl() == i1)) {
							html = getHtm(talker.getHtmlPrefix(), fnDecoAlreadySet);
						} else {
							html = getHtm(talker.getHtmlPrefix(), fnManageAgitDeco + i0 + ".htm");
							html = html.replace("<?AgitDecoCost?>", "(<font color=\"FFAABB\">" + getDecoFee(i0, i1) + "</font> Adena/" + (getDecoDay(i0, i1)  / 1000 / 60 / 60 / 24) + " Day(s))");
							html = html.replace("<?AgitDecoSubmit?>", String.valueOf(reply));
						}
						switch (i0) {
							case ClanHall.FUNC_RESTORE_HP -> {
								html = html.replace("<?AgitDecoEffect?>", i1 * 20 + "%");
							}
							case ClanHall.FUNC_RESTORE_MP -> {
								html = html.replace("<?AgitDecoEffect?>", i1 * 5 + "%");
							}
							case ClanHall.FUNC_RESTORE_EXP -> {
								html = html.replace("<?AgitDecoEffect?>", i1 * 5 + "%");
							}
							case ClanHall.FUNC_DECO_CURTAINS -> {
								html = html.replace("<?AgitDecoEffect?>", i1 * 25 + "%");
							}
							default -> {
								html = html.replace("<?AgitDecoEffect?>", "Stage " + i1);
							}
						}
					}
					talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_RESETUPDATE_CONFIRM -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CH_SET_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					final var i0 = reply / 1000;
					final var i1 = reply - (i0 * 1000);
					
					if (i1 == 0) {
						clanHall.removeFunction(i0);
						showPage(talker, fnAfterResetDeco);
						revalidateDeco(talker);
					} else {
						if (getQuestItemsCount(talker, ADENA) >= getDecoFee(i0, i1)) {
							if (i0 == ClanHall.FUNC_SUPPORT) {
								if (i1 > 0) {
									final var i2 = 286130177 + ((i1 * 256) * 256);
									final var skill = _supportList.get(i2).getSkill();
									if ((skill.getMpConsume1() + skill.getMpConsume2()) < npc.getCurrentMp()) {
										npc.setTarget(npc);
										npc.doCast(skill);
									}
								} else {
									final var skill = _supportList.get(286195713).getSkill();
									if ((skill.getMpConsume1() + skill.getMpConsume2()) < npc.getCurrentMp()) {
										npc.setTarget(npc);
										npc.doCast(skill);
									}
								}
							}
							clanHall.updateFunctions(talker, i0, i1, getDecoFee(i0, i1), getDecoDay(i0, i1), (clanHall.getFunction(i0) == null));
							showPage(talker, fnAfterSetDeco);
							revalidateDeco(talker);
						} else {
							showPage(talker, fnNotEnoughAdena);
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_SUPPORT_MAGIC -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					npc.setTarget(talker);
					
					if (_supportList.containsKey(reply)) {
						String html;
						
						final var skill = _supportList.get(reply).getSkill();
						if (((skill.getMpConsume1() + skill.getMpConsume2()) < npc.getCurrentMp()) || clanhall().mpBuffFree()) {
							npc.doCast(skill);
							html = getHtm(talker.getHtmlPrefix(), fnAfterBuff);
						} else {
							html = getHtm(talker.getHtmlPrefix(), fnNotEnoughMP);
						}
						
						html = html.replace("<?MPLeft?>", String.valueOf((int) npc.getCurrentMp()));
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
		}
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var clanHall = getClanHall(npc);
		
		if (talker.hasClanPrivilege(ClanPrivilege.CH_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
			if (clanHall.getFunction(ClanHall.FUNC_TELEPORT) == null) {
				showPage(talker, fnFuncDisabled);
			} else {
				if (clanHall.getFunction(ClanHall.FUNC_TELEPORT).getLvl() == 1) {
					teleportFStr(talker, npc, position1, ADENA);
				} else {
					if (clanHall.getFunction(ClanHall.FUNC_TELEPORT).getLvl() == 2) {
						teleportFStr(talker, npc, position2, ADENA);
					}
				}
			}
		} else {
			showPage(talker, fnNoAuthority);
		}
	}
	
	private void teleportFStr(L2PcInstance player, L2Character npc, TelPosList[] teleList, int itemId) {
		final var html = new StringBuilder("<html><body>&$556;<br><br>");
		for (TelPosList tele : teleList) {
			final var loc = tele.loc();
			var ammount = tele.ammount();
			
			if (ammount == 0) {
				html.append("<a action=\"bypass -h teleport " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + itemId + " " + ammount + "\" msg=\"811;" + getNpcString(tele.locId()) + "\">" + getNpcString(tele.locId()) + "</a><br1>");
			} else {
				html.append("<a action=\"bypass -h teleport " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + itemId + " " + ammount + "\" msg=\"811;" + getNpcString(tele.locId()) + "\">" + getNpcString(tele.locId()) + " - " + ammount + " Adena</a><br1>");
			}
		}
		html.append("</body></html>");
		
		player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html.toString()));
	}
	
	private static final String getNpcString(int id) {
		return "<fstring>" + id + "</fstring>";
	}
	
	/**
	 * @return the L2ClanHall this L2NpcInstance belongs to.
	 */
	public final ClanHall getClanHall(L2Npc npc) {
		if (_clanHallId < 0) {
			ClanHall temp = ClanHallManager.getInstance().getNearbyClanHall(npc.getX(), npc.getY(), 500);
			if (temp == null) {
				temp = ClanHallSiegeManager.getInstance().getNearbyClanHall(npc);
			}
			
			if (temp != null) {
				_clanHallId = temp.getId();
			}
			
			if (_clanHallId < 0) {
				return null;
			}
		}
		return ClanHallManager.getInstance().getClanHallById(_clanHallId);
	}
	
	private static final long getDecoDay(int function, int level) {
		switch (function) {
			case ClanHall.FUNC_RESTORE_HP -> {
				switch (level) {
					case 2, 4, 6, 7, 8, 9, 12, 13 -> {
						return clanhall().getFunctionFeeDay1();
					}
					case 5, 10, 15 -> {
						return clanhall().getFunctionFeeDay3();
					}
				}
			}
			case ClanHall.FUNC_RESTORE_MP -> {
				switch (level) {
					case 1, 3, 5 -> {
						return clanhall().getFunctionFeeDay1();
					}
					case 6, 8 -> {
						return clanhall().getFunctionFeeDay2();
					}
				}
			}
			case ClanHall.FUNC_RESTORE_EXP -> {
				switch (level) {
					case 1, 3, 5, 6, 7, 10 -> {
						return clanhall().getFunctionFeeDay1();
					}
					case 8 -> {
						return clanhall().getFunctionFeeDay3();
					}
				}
			}
			case ClanHall.FUNC_TELEPORT -> {
				switch (level) {
					case 1 -> {
						return clanhall().getFunctionFeeDay7();
					}
					case 2 -> {
						return clanhall().getFunctionFeeDay3();
					}
				}
			}
			case ClanHall.FUNC_DECO_CURTAINS -> {
				switch (level) {
					case 1, 2 -> {
						return clanhall().getFunctionFeeDay7();
					}
				}
			}
			case ClanHall.FUNC_SUPPORT -> {
				switch (level) {
					case 1, 2, 3, 4, 5, 7, 8 -> {
						return clanhall().getFunctionFeeDay1();
					}
				}
			}
			case ClanHall.FUNC_DECO_FRONTPLATEFORM -> {
				switch (level) {
					case 1, 2 -> {
						return clanhall().getFunctionFeeDay3();
					}
				}
			}
			case ClanHall.FUNC_ITEM_CREATE -> {
				switch (level) {
					case 1, 2, 3 -> {
						return clanhall().getFunctionFeeDay1();
					}
				}
			}
		}
		return 0;
	}
	
	private static final int getDecoFee(int function, int level) {
		switch (function) {
			case ClanHall.FUNC_RESTORE_HP -> {
				switch (level) {
					case 2 -> {
						return clanhall().getHpRegenerationFeeLvl2();
					}
					case 4 -> {
						return clanhall().getHpRegenerationFeeLvl4();
					}
					case 5 -> {
						return clanhall().getHpRegenerationFeeLvl5();
					}
					case 6 -> {
						return clanhall().getHpRegenerationFeeLvl6();
					}
					case 7 -> {
						return clanhall().getHpRegenerationFeeLvl7();
					}
					case 8 -> {
						return clanhall().getHpRegenerationFeeLvl8();
					}
					case 9 -> {
						return clanhall().getHpRegenerationFeeLvl9();
					}
					case 10 -> {
						return clanhall().getHpRegenerationFeeLvl10();
					}
					case 12 -> {
						return clanhall().getHpRegenerationFeeLvl12();
					}
					case 13 -> {
						return clanhall().getHpRegenerationFeeLvl13();
					}
					case 15 -> {
						return clanhall().getHpRegenerationFeeLvl15();
					}
				}
			}
			case ClanHall.FUNC_RESTORE_MP -> {
				switch (level) {
					case 1 -> {
						return clanhall().getMpRegenerationFeeLvl1();
					}
					case 3 -> {
						return clanhall().getMpRegenerationFeeLvl3();
					}
					case 5 -> {
						return clanhall().getMpRegenerationFeeLvl5();
					}
					case 6 -> {
						return clanhall().getMpRegenerationFeeLvl6();
					}
					case 8 -> {
						return clanhall().getMpRegenerationFeeLvl8();
					}
				}
			}
			case ClanHall.FUNC_RESTORE_EXP -> {
				switch (level) {
					case 1 -> {
						return clanhall().getExpRegenerationFeeLvl1();
					}
					case 3 -> {
						return clanhall().getExpRegenerationFeeLvl3();
					}
					case 5 -> {
						return clanhall().getExpRegenerationFeeLvl5();
					}
					case 6 -> {
						return clanhall().getExpRegenerationFeeLvl6();
					}
					case 7 -> {
						return clanhall().getExpRegenerationFeeLvl7();
					}
					case 8 -> {
						return clanhall().getExpRegenerationFeeLvl8();
					}
					case 10 -> {
						return clanhall().getExpRegenerationFeeLvl10();
					}
				}
			}
			case ClanHall.FUNC_TELEPORT -> {
				switch (level) {
					case 1 -> {
						return clanhall().getTeleportFunctionFeeLvl1();
					}
					case 2 -> {
						return clanhall().getTeleportFunctionFeeLvl2();
					}
				}
			}
			case ClanHall.FUNC_DECO_CURTAINS -> {
				switch (level) {
					case 1 -> {
						return clanhall().getCurtainFunctionFeeLvl1();
					}
					case 2 -> {
						return clanhall().getCurtainFunctionFeeLvl2();
					}
				}
			}
			case ClanHall.FUNC_SUPPORT -> {
				switch (level) {
					case 1 -> {
						return clanhall().getSupportFeeLvl1();
					}
					case 2 -> {
						return clanhall().getSupportFeeLvl2();
					}
					case 3 -> {
						return clanhall().getSupportFeeLvl3();
					}
					case 4 -> {
						return clanhall().getSupportFeeLvl4();
					}
					case 5 -> {
						return clanhall().getSupportFeeLvl5();
					}
					case 7 -> {
						return clanhall().getSupportFeeLvl7();
					}
					case 8 -> {
						return clanhall().getSupportFeeLvl8();
					}
				}
			}
			case ClanHall.FUNC_DECO_FRONTPLATEFORM -> {
				switch (level) {
					case 1 -> {
						return clanhall().getFrontPlatformFunctionFeeLvl1();
					}
					case 2 -> {
						return clanhall().getFrontPlatformFunctionFeeLvl2();
					}
				}
			}
			case ClanHall.FUNC_ITEM_CREATE -> {
				switch (level) {
					case 1 -> {
						return clanhall().getItemCreationFunctionFeeLvl1();
					}
					case 2 -> {
						return clanhall().getItemCreationFunctionFeeLvl2();
					}
					case 3 -> {
						return clanhall().getItemCreationFunctionFeeLvl3();
					}
				}
			}
		}
		return 0;
	}
	
	private static void setManageFn(ClanHall clanHall) {
		switch (clanHall.getGrade()) {
			case 1 -> {
				fnManageRegen = "data/html/clanHallManager/agitdeco_cr01.htm";
				fnManageEtc = "data/html/clanHallManager/agitdeco_ce01.htm";
				fnManageDeco = "data/html/clanHallManager/agitdeco_cd01.htm";
			}
			case 2 -> {
				fnManageRegen = "data/html/clanHallManager/agitdeco_br01.htm";
				fnManageEtc = "data/html/clanHallManager/agitdeco_be01.htm";
				fnManageDeco = "data/html/clanHallManager/agitdeco_bd01.htm";
			}
			case 3 -> {
				fnManageRegen = "data/html/clanHallManager/agitdeco_ar01.htm";
				fnManageEtc = "data/html/clanHallManager/agitdeco_ae01.htm";
				fnManageDeco = "data/html/clanHallManager/agitdeco_ad01.htm";
			}
		}
	}
	
	private void revalidateDeco(L2PcInstance player) {
		final var ch = ClanHallManager.getInstance().getClanHallByOwner(player.getClan());
		if (ch == null) {
			return;
		}
		
		player.sendPacket(new AgitDecoInfo(ch));
	}
}