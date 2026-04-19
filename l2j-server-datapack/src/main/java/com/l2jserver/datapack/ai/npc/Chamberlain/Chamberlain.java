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
package com.l2jserver.datapack.ai.npc.Chamberlain;

import static com.l2jserver.gameserver.config.Configuration.castle;
import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.sevenSigns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.data.xml.impl.DoorData;
import com.l2jserver.gameserver.instancemanager.CastleManorManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcManorBypass;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTeleportRequest;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExShowCropInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowCropSetting;
import com.l2jserver.gameserver.network.serverpackets.ExShowDominionRegistry;
import com.l2jserver.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowSeedInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowSeedSetting;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Castle Chamberlain AI.
 * @author malyelfik
 */
public abstract class Chamberlain extends AbstractNpcAI {
	
	private static final String fnHi = "data/html/chamberlain/chamberlain_saius001.htm";
	private static final String fnViewReport = "data/html/chamberlain/chamberlain_saius002.htm";
	private static final String fnViewTaxRate = "data/html/chamberlain/chamberlain_saius003.htm";
	protected String fnSetGate;
	private static final String fnNotMyLord = "data/html/chamberlain/chamberlain_saius009.htm";
	private static final String fnAfterOpenGate = "data/html/chamberlain/chamberlain_saius011.htm";
	private static final String fnAfterCloseGate = "data/html/chamberlain/chamberlain_saius012.htm";
	private static final String fnSiegeViewReport = "data/html/chamberlain/chamberlain_saius020.htm";
	private static final String fnSiegeStoppedFunction = "data/html/chamberlain/chamberlain_saius021.htm";
	private static final String fnNotEnoughMoney = "data/html/chamberlain/chamberlain_saius049.htm";
	private static final String fnBanish = "data/html/chamberlain/chamberlain_saius050.htm";
	private static final String fnAfterBanish = "data/html/chamberlain/chamberlain_saius051.htm";
	private static final String fnSiegeDefendList = "data/html/chamberlain/chamberlain_saul052.htm";
	protected String fnDoorStrengthen;
	private static final String fnDoorLevel = "data/html/chamberlain/chamberlain_saius054.htm";
	private static final String fnDoorStrengthenConfirm = "data/html/chamberlain/chamberlain_saius055.htm";
	private static final String fnCurrentDoorLevelHigher = "data/html/chamberlain/chamberlain_saius056.htm";
	private static final String fnDoorHpLevelUp = "data/html/chamberlain/chamberlain_saul057.htm";
	protected String fnSetSlowZone;
	private static final String fnSetDmgLevel = "data/html/chamberlain/chamberlain_saul059.htm";
	private static final String fnDmgZoneConfirm = "data/html/chamberlain/chamberlain_saius060.htm";
	private static final String fnCurrentDmgzoneLevelHigher = "data/html/chamberlain/chamberlain_saius061.htm";
	private static final String fnDmgZoneLevelUp = "data/html/chamberlain/chamberlain_saius062.htm";
	private static final String fnNoAuthority = "data/html/chamberlain/chamberlain_saius063.htm";
	private static final String fnSellPage = "data/html/chamberlain/chamberlain_saius064.htm";
	private static final String fnCastleList = "data/html/chamberlain/chamberlain_saius065.htm";
	private static final String fnCrownBefor = "data/html/chamberlain/chamberlain_saius066.htm";
	private static final String fnCrownAfter = "data/html/chamberlain/chamberlain_saius067.htm";
	private static final String fnSSDenial = "data/html/chamberlain/chamberlain_saius068.htm";
	private static final String fnSetTaxRate = "data/html/chamberlain/castlesettaxrate.htm";
	private static final String fnAfterSetTaxRate = "data/html/chamberlain/castleaftersettaxrate.htm";
	private static final String fnManageVault = "data/html/chamberlain/castlemanagevault.htm";
	private static final String fnNotEnoughBalance = "data/html/chamberlain/castlenotenoughbalance.htm";
	private static final String fnSellDawnTicket = "data/html/chamberlain/ssq_selldawnticket.htm";
	private static final String fnTooHighTaxRate = "data/html/chamberlain/castletoohightaxrate.htm";
	private static final String fnNotEnoughTicket = "data/html/chamberlain/ssq_notenoughticket.htm";
	private static final String fnNotDawnOrEvent = "data/html/chamberlain/ssq_notdawnorevent.htm";
	private static final String fnManage = "data/html/chamberlain/chamberlain_castletdecomanage.htm";
	private static final String fnManageRegen = "data/html/chamberlain/chamberlain_castledeco_ar01.htm";
	private static final String fnManageEtc = "data/html/chamberlain/chamberlain_castledeco_ae01.htm";
	private static final String fnManageAgitDeco = "data/html/chamberlain/ol_mahum_castledeco__";
	private static final String fnDecoReset = "data/html/chamberlain/castleresetdeco.htm";
	private static final String fnAfterSetDeco = "data/html/chamberlain/castleaftersetdeco.htm";
	private static final String fnAfterResetDeco = "data/html/chamberlain/castleafterresetdeco.htm";
	private static final String fnDecoFunction = "data/html/chamberlain/castledecofunction.htm";
	private static final String fnFuncDisabled = "data/html/chamberlain/castlefuncdisabled.htm";
	private static final String fnAgitBuff = "data/html/chamberlain/castlebuff";
	private static final String fnDecoAlreadySet = "data/html/chamberlain/castledecoalreadyset.htm";
	private static final String fnAfterBuff = "data/html/chamberlain/castleafterbuff.htm";
	private static final String fnNotEnoughMP = "data/html/chamberlain/castlenotenoughmp.htm";
	private static final String fnNoFortressContract = "data/html/chamberlain/chamberlain_saius069.htm";
	protected String fnMyFortressStatus;
	
	private static final String fnManor = "data/html/chamberlain/manor.htm";
	private static final String fnFlagMan = "data/html/chamberlain/flagman.htm";
	private static final String fnQ194NoTeleport = "data/html/chamberlain/q194_noteleport.htm";
	
	private static final int fortress_fstr = 1300101;
	private static final int fortress_dependancy = 1300133;
	
	private static final int SSQ_DawnFactor_tax = 25;
	private static final int SSQ_DuskFactor_tax = 5;
	private static final int SSQ_DrawFactor_tax = 15;
	
	private static final int SSQ_DawnFactor_door = 80;
	private static final int SSQ_DuskFactor_door = 300;
	private static final int SSQ_DrawFactor_door = 100;
	
	private static final int SSQ_DawnFactor_dmg = 80;
	private static final int SSQ_DuskFactor_dmg = 300;
	private static final int SSQ_DrawFactor_dmg = 100;
	
	private static final int ADENA = 57;
	private static final int CROWN_OF_LORD = 6841;
	
	private static final int MS_ASK_BACK = 0;
	private static final int MS_ASK_MAIN_CHAT = -201;
	private static final int MS_ASK_SET_TAX = -202;
	private static final int MS_ASK_OPENCLOSE_DOORS = -203;
	private static final int MS_ASK_SIEGE_DEFEND_FUNC = -204;
	private static final int MS_ASK_REINFORCE_GATES_CHAT = -205;
	private static final int MS_ASK_REINFORCE_DOOR_LEVEL = -206;
	private static final int MS_ASK_REINFORCE_DOOR_CONFIRM = -207;
	private static final int MS_ASK_REINFORCE_DMG_ZONE_CHAT = -208;
	private static final int MS_ASK_REINFORCE_DMG_ZONE_LEVEL = -209;
	private static final int MS_ASK_REINFORCE_DMG_ZONE_CONFIRM = -210;
	private static final int MS_ASK_DISMISS = -219;
	private static final int MS_ASK_WITHDRAW = -240;
	private static final int MS_ASK_DEPOSIT = -241;
	private static final int MS_ASK_SELL_TICKET = -260;
	private static final int MS_ASK_MANAGE_REGEN_CHAT = -270;
	private static final int MS_ASK_RESETUPDATE_CONFIRM = -271;
	private static final int MS_ASK_SUPPORT_MAGIC = -22208;
	
	private static final int REPLY_VIEW_REPORT = 1;
	private static final int REPLY_ADJUST_TAXES = 2;
	private static final int REPLY_MANAGE_VAULT = 3;
	private static final int REPLY_MANAGE_FUNCTIONS = 4;
	private static final int REPLY_SIEGE_INFO = 5;
	private static final int REPLY_MANAGE_MANOR = 6;
	private static final int REPLY_ITEMS_CHAT = 7;
	private static final int REPLY_DISMISS_CHAT = 8;
	private static final int REPLY_OPENCLOSE_DOORS_CHAT = 9;
	private static final int REPLY_MANAGE_SIEGE_FUNC = 10;
	private static final int REPLY_LORDS_CERTIFICATE_APPROVAL = 11;
	private static final int REPLY_ITEMS = 12;
	private static final int REPLY_CROWN_OF_LORD = 13;
	private static final int REPLY_TERRITORY_WAR_INFO = 14;
	private static final int REPLY_MANAGE_REGEN_CHAT = 151;
	private static final int REPLY_MANAGE_ETC_CHAT = 152;
	private static final int REPLY_SUPPORT_CHAT = 107;
	private static final int REPLY_SET_FUNCTIONS_CHAT = 105;
	private static final int REPLY_FUNCTION_CHAT = 103;
	private static final int REPLY_FORTRESS_STATUS = 399;
	private static final int REPLY_SIEGE_DEFEND_REINFORCE_GATES_WALLS = 1;
	private static final int REPLY_SIEGE_DEFEND_DEPLOY_TRAPS = 2;
	private static final int REPLY_REINFORCE_LEVEL_1 = 1;
	private static final int REPLY_REINFORCE_LEVEL_2 = 2;
	private static final int REPLY_REINFORCE_LEVEL_3 = 3;
	
	protected TelPosList[] position1;
	protected TelPosList[] position2;
	
	protected L2BuyList sellList0;
	
	public static List<Integer> _fortress = new ArrayList<>();
	
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
	
	public Chamberlain(int npcId) {
		bindFirstTalk(npcId);
		bindTeleportRequest(npcId);
		bindMenuSelected(npcId);
		bindManorMenuSelected(npcId);
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
			final var castle = npc.getCastle();
			
			if (castle.getFunction(Castle.FUNC_SUPPORT) != null && castle.getFunction(Castle.FUNC_SUPPORT).getLvl() > 0) {
				final var i0 = 286130177 + (((castle.getFunction(Castle.FUNC_SUPPORT).getLvl() - 10) * 256) * 256);
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
		final var castle = npc.getCastle();
		
		if (talker.isCombatFlagEquipped()) {
			showPage(talker, fnFlagMan);
		} else {
			if (npc.isMyLord(talker) || talker.getClan() != null && castle.getOwnerId() == talker.getClanId()) {
				showPage(talker, fnHi);
			} else {
				showPage(talker, fnNotMyLord);
			}
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var format = new SimpleDateFormat("dd/MM HH");
		final var castle = npc.getCastle();
		
		switch (ask) {
			case MS_ASK_BACK -> {
				if (npc.isMyLord(talker) || (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					showPage(talker, fnHi);
				} else {
					showPage(talker, fnNotMyLord);
				}
			}
			case MS_ASK_MAIN_CHAT -> {
				switch (reply) {
					case REPLY_VIEW_REPORT -> {
						if (npc.isMyLord(talker)) {
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeViewReport);
							} else {
								var html = getHtm(talker.getHtmlPrefix(), fnViewReport);
								if (castle.getOwner() != null) {
									html = html.replace("<?my_pledge_name?>", castle.getOwner().getName());
									html = html.replace("<?my_owner_name?>", castle.getOwner().getLeaderName());
									html = html.replace("<?feud_name?>", "<fstring>" + Integer.valueOf(1001000 + castle.getResidenceId()) + "</fstring>");
								}
								
								switch (SevenSigns.getInstance().getCurrentPeriod()) {
									case SevenSigns.PERIOD_COMPETITION -> {
										html = html.replace("<?ss_event?>", "<fstring>" + 1000507 + "</fstring>");
									}
									case SevenSigns.PERIOD_SEAL_VALIDATION -> {
										html = html.replace("<?ss_event?>", "<fstring>" + 1000508 + "</fstring>");
									}
									default -> {
										html = html.replace("<?ss_event?>", "<fstring>" + 1000509 + "</fstring>");
									}
								}
								
								html = html.replace("<?ss_avarice?>", "<fstring>" + getSealOwner(SevenSigns.SEAL_AVARICE) + "</fstring>");
								html = html.replace("<?ss_revelation?>", "<fstring>" + getSealOwner(SevenSigns.SEAL_GNOSIS) + "</fstring>");
								html = html.replace("<?ss_strife?>", "<fstring>" + getSealOwner(SevenSigns.SEAL_STRIFE) + "</fstring>");
								talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_ADJUST_TAXES -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_TAXES) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeStoppedFunction);
							} else {
								var html = getHtm(talker.getHtmlPrefix(), fnSetTaxRate);
								html = html.replace("<?current_tax_rate?>", String.valueOf(castle.getTaxPercent()));
								html = html.replace("<?next_tax_rate?>", String.valueOf(castle.getNextTaxPercent()));
								html = html.replace("<?TaxLimit?>", String.valueOf(getTaxLimit()));
								talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						} else {
							if ((talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
								var html = getHtm(talker.getHtmlPrefix(), fnViewTaxRate);
								html = html.replace("<?current_tax_rate?>", String.valueOf(castle.getTaxPercent()));
								html = html.replace("<?next_tax_rate?>", String.valueOf(castle.getNextTaxPercent()));
								talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							} else {
								showPage(talker, fnNoAuthority);
							}
						}
					}
					case REPLY_MANAGE_VAULT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_TAXES) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							var seedIncome = 0;
							if (general().allowManor()) {
								for (var sp : CastleManorManager.getInstance().getSeedProduction(castle.getResidenceId(), false)) {
									final var diff = sp.getStartAmount() - sp.getAmount();
									if (diff != 0) {
										seedIncome += diff * sp.getPrice();
									}
								}
							}
							
							var html = getHtm(talker.getHtmlPrefix(), fnManageVault);
							html = html.replace("<?tax_income?>", Util.formatAdena(castle.getTreasury()));
							html = html.replace("<?tax_income_reserved?>", "0"); // TODO: Implement me!
							html = html.replace("<?seed_income?>", Util.formatAdena(seedIncome));
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_FUNCTIONS -> {
						if (npc.isMyLord(talker) || (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeStoppedFunction);
							} else {
								showPage(talker, fnCastleList);
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_SIEGE_INFO -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_MANAGE_SIEGE) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							castle.getSiege().listRegisterClan(talker);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_MANOR -> {
						if (general().allowManor()) {
							if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_MANOR_ADMIN) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
								showPage(talker, fnManor);
							} else {
								showPage(talker, fnNoAuthority);
							}
						} else {
							talker.sendPacket(SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
						}
					}
					case REPLY_ITEMS_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							showPage(talker, fnSellPage);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_DISMISS_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_DISMISS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeStoppedFunction);
							} else {
								showPage(talker, fnBanish);
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_OPENCLOSE_DOORS_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeStoppedFunction);
							} else {
								showPage(talker, fnSetGate);
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_SIEGE_FUNC -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							if (!isDomainFortressInContractStatus(npc)) {
								showPage(talker, fnNoFortressContract);
								return;
							}
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeStoppedFunction);
							} else {
								if (SevenSigns.getInstance().isCompetitionPeriod() || SevenSigns.getInstance().isCompResultsPeriod()) {
									showPage(talker, fnSSDenial);
								} else {
									showPage(talker, fnSiegeDefendList);
								}
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_LORDS_CERTIFICATE_APPROVAL -> {
						if (npc.isMyLord(talker)) {
							if (castle.getZone().isActive()) {
								showPage(talker, fnSiegeStoppedFunction);
							} else {
								if ((SevenSigns.getInstance().getPlayerCabal(talker.getObjectId()) == SevenSigns.CABAL_DAWN)) {
									if (SevenSigns.getInstance().isCompetitionPeriod()) {
										final var ticketCount = castle.getTicketBuyCount();
										if (ticketCount < (sevenSigns().getSevenSignsDawnTicketQuantity() / sevenSigns().getSevenSignsDawnTicketBundle())) {
											var html = getHtm(talker.getHtmlPrefix(), fnSellDawnTicket);
											html = html.replace("<?DawnTicketLeft?>", String.valueOf(sevenSigns().getSevenSignsDawnTicketQuantity() - (ticketCount * sevenSigns().getSevenSignsDawnTicketBundle())));
											html = html.replace("<?DawnTicketBundle?>", String.valueOf(sevenSigns().getSevenSignsDawnTicketBundle()));
											html = html.replace("<?DawnTicketPrice?>", String.valueOf(sevenSigns().getSevenSignsDawnTicketPrice() * sevenSigns().getSevenSignsDawnTicketBundle()));
											talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
										} else {
											showPage(talker, fnNotEnoughTicket);
										}
									} else {
										showPage(talker, fnNotDawnOrEvent);
									}
								} else {
									showPage(talker, fnNotDawnOrEvent);
								}
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_ITEMS -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							showBuySell(talker, npc, sellList0);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_CROWN_OF_LORD -> {
						if (castle.getZone().isActive()) {
							showPage(talker, fnSiegeStoppedFunction);
						} else {
							if (npc.isMyLord(talker)) {
								if (getQuestItemsCount(talker, CROWN_OF_LORD) >= 1) {
									showPage(talker, fnCrownBefor);
								} else {
									if (castle.getOwner() != null) {
										var html = getHtm(talker.getHtmlPrefix(), fnCrownAfter);
										html = html.replace("<?my_owner_name?>", castle.getOwner().getLeaderName());
										html = html.replace("<?feud_name?>", "<fstring>" + Integer.valueOf(1001000 + castle.getResidenceId()) + "</fstring>");
										giveItems(talker, CROWN_OF_LORD, 1);
										talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									}
								}
							} else {
								showPage(talker, fnNoAuthority);
							}
						}
					}
					case REPLY_TERRITORY_WAR_INFO -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_MANAGE_SIEGE) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							talker.sendPacket(new ExShowDominionRegistry(castle.getResidenceId(), talker));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_REGEN_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							var html = getHtm(talker.getHtmlPrefix(), fnManageRegen);
							if (castle.getFunction(Castle.FUNC_RESTORE_HP) == null) {
								html = html.replace("<?HPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?HPCost?>", "");
								html = html.replace("<?HPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?HPReset?>", "");
							} else {
								final var i0 = castle.getFunction(Castle.FUNC_RESTORE_HP).getLvl() - 10;
								html = html.replace("<?HPDepth?>", (i0 * 20) + "%");
								html = html.replace("<?HPCost?>", "(<font color=\"FFAABB\">" + castle.getFunction(Castle.FUNC_RESTORE_HP).getLease() + "</font> Adena/ " + (getDecoDay(Castle.FUNC_RESTORE_HP, castle.getFunction(Castle.FUNC_RESTORE_HP).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?HPExpire?>", "Withdraw the fee for the next time at " +  format.format(castle.getFunction(Castle.FUNC_RESTORE_HP).getEndTime()));
								html = html.replace("<?HPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + Castle.FUNC_RESTORE_HP + "000\">Deactivate</a>]");
							}
							if (castle.getFunction(Castle.FUNC_RESTORE_MP) == null) {
								html = html.replace("<?MPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?MPCost?>", "");
								html = html.replace("<?MPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?MPReset?>", "");
							} else {
								final var i0 = castle.getFunction(Castle.FUNC_RESTORE_MP).getLvl() - 10;
								html = html.replace("<?MPDepth?>", (i0 * 5) + "%");
								html = html.replace("<?MPCost?>", "(<font color=\"FFAABB\">" + castle.getFunction(Castle.FUNC_RESTORE_MP).getLease() + "</font> Adena/ " + (getDecoDay(Castle.FUNC_RESTORE_MP, castle.getFunction(Castle.FUNC_RESTORE_MP).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?MPExpire?>", "Withdraw the fee for the next time at " +  format.format(castle.getFunction(Castle.FUNC_RESTORE_MP).getEndTime()));
								html = html.replace("<?MPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + Castle.FUNC_RESTORE_MP + "000\">Deactivate</a>]");
							}
							if (castle.getFunction(Castle.FUNC_RESTORE_EXP) == null) {
								html = html.replace("<?XPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?XPCost?>", "");
								html = html.replace("<?XPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?XPReset?>", "");
							} else {
								final var i0 = castle.getFunction(Castle.FUNC_RESTORE_EXP).getLvl() - 10;
								html = html.replace("<?XPDepth?>", (i0 * 5) + "%");
								html = html.replace("<?XPCost?>", "(<font color=\"FFAABB\">" + castle.getFunction(Castle.FUNC_RESTORE_EXP).getLease() + "</font> Adena/ " + (getDecoDay(Castle.FUNC_RESTORE_EXP, castle.getFunction(Castle.FUNC_RESTORE_EXP).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?XPExpire?>", "Withdraw the fee for the next time at " +  format.format(castle.getFunction(Castle.FUNC_RESTORE_EXP).getEndTime()));
								html = html.replace("<?XPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + Castle.FUNC_RESTORE_EXP + "000\">Deactivate</a>]");
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_MANAGE_ETC_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							var html = getHtm(talker.getHtmlPrefix(), fnManageEtc);
							if (castle.getFunction(Castle.FUNC_TELEPORT) == null) {
								html = html.replace("<?TPDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?TPCost?>", "");
								html = html.replace("<?TPExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?TPReset?>", "");
							} else {
								final var i0 = castle.getFunction(Castle.FUNC_TELEPORT).getLvl() - 10;
								html = html.replace("<?TPDepth?>", "Stage " + i0 * 1);
								html = html.replace("<?TPCost?>", "(<font color=\"FFAABB\">" + castle.getFunction(Castle.FUNC_TELEPORT).getLease() + "</font> Adena/ " + (getDecoDay(Castle.FUNC_TELEPORT, castle.getFunction(Castle.FUNC_TELEPORT).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?TPExpire?>", "Withdraw the fee for the next time at " +  format.format(castle.getFunction(Castle.FUNC_TELEPORT).getEndTime()));
								html = html.replace("<?TPReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + Castle.FUNC_TELEPORT + "000\">Deactivate</a>]");
							}
							if (castle.getFunction(Castle.FUNC_SUPPORT) == null) {
								html = html.replace("<?BFDepth?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?BFCost?>", "");
								html = html.replace("<?BFExpire?>", "<fstring> " + 4 + "</fstring>");
								html = html.replace("<?BFReset?>", "");
							} else {
								final var i0 = castle.getFunction(Castle.FUNC_SUPPORT).getLvl() - 10;
								html = html.replace("<?BFDepth?>", "Stage " + i0 * 1);
								html = html.replace("<?BFCost?>", "(<font color=\"FFAABB\">" + castle.getFunction(Castle.FUNC_SUPPORT).getLease() + "</font> Adena/ " + (getDecoDay(Castle.FUNC_SUPPORT, castle.getFunction(Castle.FUNC_SUPPORT).getLvl()) / 1000 / 60 / 60 / 24) + " Day(s))");
								html = html.replace("<?BFExpire?>", "Withdraw the fee for the next time at " +  format.format(castle.getFunction(Castle.FUNC_SUPPORT).getEndTime()));
								html = html.replace("<?BFReset?>", "[<a action=\"bypass -h menu_select?ask=-270&reply=" + Castle.FUNC_SUPPORT + "000\">Deactivate</a>]");
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_SUPPORT_CHAT -> {
						if (talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							if (castle.getFunction(Castle.FUNC_SUPPORT) == null) {
								showPage(talker, fnFuncDisabled);
							} else {
								String html = getHtm(talker.getHtmlPrefix(), fnAgitBuff + "_" + (castle.getFunction(Castle.FUNC_SUPPORT).getLvl() - 10) + ".htm");
								html = html.replace("<?MPLeft?>", String.valueOf((int) npc.getCurrentMp()));
								talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_SET_FUNCTIONS_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							showPage(talker, fnManage);
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_FUNCTION_CHAT -> {
						if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
							var html = getHtm(talker.getHtmlPrefix(), fnDecoFunction);
							if (castle.getFunction(Castle.FUNC_RESTORE_HP) == null) {
								html = html.replace("<?HPDepth?>", "0");
							} else {
								html = html.replace("<?HPDepth?>", String.valueOf((castle.getFunction(Castle.FUNC_RESTORE_HP).getLvl() - 10) * 20));
							}
							if (castle.getFunction(Castle.FUNC_RESTORE_MP) == null) {
								html = html.replace("<?MPDepth?>", "0");
							} else {
								html = html.replace("<?MPDepth?>", String.valueOf((castle.getFunction(Castle.FUNC_RESTORE_MP).getLvl() - 10) * 20));
							}
							if (castle.getFunction(Castle.FUNC_RESTORE_EXP) == null) {
								html = html.replace("<?XPDepth?>", "0");
							} else {
								html = html.replace("<?XPDepth?>", String.valueOf((castle.getFunction(Castle.FUNC_RESTORE_EXP).getLvl() - 10) * 20));
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							showPage(talker, fnNoAuthority);
						}
					}
					case REPLY_FORTRESS_STATUS -> {
						final var fortressId1 = npc.getTemplate().getParameters().getInt("fortress_id1", 0);
						final var fortressId2 = npc.getTemplate().getParameters().getInt("fortress_id2", 0);
						final var fortressId3 = npc.getTemplate().getParameters().getInt("fortress_id3", 0);
						final var fortressId4 = npc.getTemplate().getParameters().getInt("fortress_id4", 0);
						final var fortressId5 = npc.getTemplate().getParameters().getInt("fortress_id5", 0);
						
						var html = getHtm(talker.getHtmlPrefix(), fnMyFortressStatus);
						
						if (fortressId1 > 100) {
							final var fort = FortManager.getInstance().getFortById(fortressId1);
							final var name = (fortress_fstr + fortressId1) - 101;
							final var fortStatus = switch (fort.getFortState()) {
								case 1 -> "1300122";
								case 2 -> "1300124";
								default -> "1300123";
							};
							final var fortType = fortress_dependancy + (fort.isBorderFortress() ? 0 : 1);
							
							html = html.replace("<?name_fortress1?>", "<fstring>" + name + "</fstring>");
							html = html.replace("<?status_fortress1?>", "<fstring>" + fortStatus + "</fstring>");
							html = html.replace("<?boundary_fortress1?>", "<fstring>" + fortType  + "</fstring>");
						}
						if (fortressId2 > 100) {
							final var fort = FortManager.getInstance().getFortById(fortressId2);
							final var name = (fortress_fstr + fortressId2) - 101;
							final var fortStatus = switch (fort.getFortState()) {
								case 1 -> "1300122";
								case 2 -> "1300124";
								default -> "1300123";
							};
							final var fortType = fortress_dependancy + (fort.isBorderFortress() ? 0 : 1);
							
							html = html.replace("<?name_fortress2?>", "<fstring>" + name + "</fstring>");
							html = html.replace("<?status_fortress2?>", "<fstring>" + fortStatus + "</fstring>");
							html = html.replace("<?boundary_fortress2?>", "<fstring>" + fortType  + "</fstring>");
						}
						if (fortressId3 > 100) {
							final var fort = FortManager.getInstance().getFortById(fortressId3);
							final var name = (fortress_fstr + fortressId3) - 101;
							final var fortStatus = switch (fort.getFortState()) {
								case 1 -> "1300122";
								case 2 -> "1300124";
								default -> "1300123";
							};
							final var fortType = fortress_dependancy + (fort.isBorderFortress() ? 0 : 1);
							
							html = html.replace("<?name_fortress3?>", "<fstring>" + name + "</fstring>");
							html = html.replace("<?status_fortress3?>", "<fstring>" + fortStatus + "</fstring>");
							html = html.replace("<?boundary_fortress3?>", "<fstring>" + fortType  + "</fstring>");
						}
						if (fortressId4 > 100) {
							final var fort = FortManager.getInstance().getFortById(fortressId4);
							final var name = (fortress_fstr + fortressId4) - 101;
							final var fortStatus = switch (fort.getFortState()) {
								case 1 -> "1300122";
								case 2 -> "1300124";
								default -> "1300123";
							};
							final var fortType = fortress_dependancy + (fort.isBorderFortress() ? 0 : 1);
							
							html = html.replace("<?name_fortress4?>", "<fstring>" + name + "</fstring>");
							html = html.replace("<?status_fortress4?>", "<fstring>" + fortStatus + "</fstring>");
							html = html.replace("<?boundary_fortress4?>", "<fstring>" + fortType  + "</fstring>");
						}
						if (fortressId5 > 100) {
							final var fort = FortManager.getInstance().getFortById(fortressId5);
							final var name = (fortress_fstr + fortressId5) - 101;
							final var fortStatus = switch (fort.getFortState()) {
								case 1 -> "1300122";
								case 2 -> "1300124";
								default -> "1300123";
							};
							final var fortType = fortress_dependancy + (fort.isBorderFortress() ? 0 : 1);
							
							html = html.replace("<?name_fortress5?>", "<fstring>" + name + "</fstring>");
							html = html.replace("<?status_fortress5?>", "<fstring>" + fortStatus + "</fstring>");
							html = html.replace("<?boundary_fortress5?>", "<fstring>" + fortType  + "</fstring>");
						}
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
				}
			}
			case MS_ASK_SET_TAX -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_TAXES) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					if (castle.getZone().isActive()) {
						showPage(talker, fnSiegeStoppedFunction);
					} else {
						int i0;
						if (getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DUSK) {
							i0 = SSQ_DuskFactor_tax;
						} else {
							if (getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DAWN) {
								i0 = SSQ_DawnFactor_tax;
							} else {
								i0 = SSQ_DrawFactor_tax;
							}
						}
						String html;
						if (reply > i0) {
							html = getHtm(talker.getHtmlPrefix(), fnTooHighTaxRate);
							html = html.replace("<?TaxLimit?>", String.valueOf(i0));
						} else {
							html = getHtm(talker.getHtmlPrefix(), fnAfterSetTaxRate);
							if (reply < 0) {
								castle.setNextTaxPercent(0);
								html = html.replace("<?next_tax_rate?>", "0");
							} else {
								castle.setNextTaxPercent(reply);
								html = html.replace("<?next_tax_rate?>", String.valueOf(reply));
							}
						}
						talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_OPENCLOSE_DOORS -> {
				if (castle.getZone().isActive()) {
					showPage(talker, fnSiegeStoppedFunction);
				} else {
					if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
						switch (reply) {
							case 1 -> {
								final var dDoorName11 = npc.getTemplate().getParameters().getString("DDoorName1_1", null);
								final var dDoorName12 = npc.getTemplate().getParameters().getString("DDoorName1_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName11);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName12);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 101 -> {
								final var dDoorName11 = npc.getTemplate().getParameters().getString("DDoorName1_1", null);
								final var dDoorName12 = npc.getTemplate().getParameters().getString("DDoorName1_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName11);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName12);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 2 -> {
								final var dDoorName21 = npc.getTemplate().getParameters().getString("DDoorName2_1", null);
								final var dDoorName22 = npc.getTemplate().getParameters().getString("DDoorName2_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName21);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName22);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 102 -> {
								final var dDoorName21 = npc.getTemplate().getParameters().getString("DDoorName2_1", null);
								final var dDoorName22 = npc.getTemplate().getParameters().getString("DDoorName2_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName21);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName22);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 3 -> {
								final var dDoorName31 = npc.getTemplate().getParameters().getString("DDoorName3_1", null);
								final var dDoorName32 = npc.getTemplate().getParameters().getString("DDoorName3_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName31);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName32);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 103 -> {
								final var dDoorName31 = npc.getTemplate().getParameters().getString("DDoorName3_1", null);
								final var dDoorName32 = npc.getTemplate().getParameters().getString("DDoorName3_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName31);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName32);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 4 -> {
								final var dDoorName41 = npc.getTemplate().getParameters().getString("DDoorName4_1", null);
								final var dDoorName42 = npc.getTemplate().getParameters().getString("DDoorName4_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName41);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName42);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 104 -> {
								final var dDoorName41 = npc.getTemplate().getParameters().getString("DDoorName4_1", null);
								final var dDoorName42 = npc.getTemplate().getParameters().getString("DDoorName4_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName41);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName42);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 5 -> {
								final var dDoorName51 = npc.getTemplate().getParameters().getString("DDoorName5_1", null);
								final var dDoorName52 = npc.getTemplate().getParameters().getString("DDoorName5_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName51);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName52);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 105 -> {
								final var dDoorName51 = npc.getTemplate().getParameters().getString("DDoorName5_1", null);
								final var dDoorName52 = npc.getTemplate().getParameters().getString("DDoorName5_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName51);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName52);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 6 -> {
								final var dDoorName61 = npc.getTemplate().getParameters().getString("DDoorName6_1", null);
								final var dDoorName62 = npc.getTemplate().getParameters().getString("DDoorName6_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName61);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName62);
								
								if (door1 != null) {
									door1.openMe();
								}
								if (door2 != null) {
									door2.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 106 -> {
								final var dDoorName61 = npc.getTemplate().getParameters().getString("DDoorName6_1", null);
								final var dDoorName62 = npc.getTemplate().getParameters().getString("DDoorName6_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName61);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName62);
								
								if (door1 != null) {
									door1.closeMe();
								}
								if (door2 != null) {
									door2.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 11 -> {
								final var sDoorName1 = npc.getTemplate().getParameters().getString("SDoorName1", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName1);
								
								if (door != null) {
									door.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 111 -> {
								final var sDoorName1 = npc.getTemplate().getParameters().getString("SDoorName1", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName1);
								
								if (door != null) {
									door.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 12 -> {
								final var sDoorName2 = npc.getTemplate().getParameters().getString("SDoorName2", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName2);
								
								if (door != null) {
									door.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 112 -> {
								final var sDoorName2 = npc.getTemplate().getParameters().getString("SDoorName2", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName2);
								
								if (door != null) {
									door.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 13 -> {
								final var sDoorName3 = npc.getTemplate().getParameters().getString("SDoorName3", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName3);
								
								if (door != null) {
									door.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 113 -> {
								final var sDoorName3 = npc.getTemplate().getParameters().getString("SDoorName3", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName3);
								
								if (door != null) {
									door.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 14 -> {
								final var sDoorName4 = npc.getTemplate().getParameters().getString("SDoorName4", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName4);
								
								if (door != null) {
									door.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 114 -> {
								final var sDoorName4 = npc.getTemplate().getParameters().getString("SDoorName4", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName4);
								
								if (door != null) {
									door.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 15 -> {
								final var sDoorName5 = npc.getTemplate().getParameters().getString("SDoorName5", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName5);
								
								if (door != null) {
									door.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 115 -> {
								final var sDoorName5 = npc.getTemplate().getParameters().getString("SDoorName5", null);
								
								final var door = DoorData.getInstance().getDoorByName(sDoorName5);
								
								if (door != null) {
									door.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
							case 99 -> {
								final var doors = castle.getDoors();
								for (var door : doors) {
									door.openMe();
								}
								showPage(talker, fnAfterOpenGate);
							}
							case 199 -> {
								final var doors = castle.getDoors();
								for (var door : doors) {
									door.closeMe();
								}
								showPage(talker, fnAfterCloseGate);
							}
						}
					} else {
						showPage(talker, fnNoAuthority);
					}
				}
			}
			case MS_ASK_SIEGE_DEFEND_FUNC -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					switch (reply) {
						case REPLY_SIEGE_DEFEND_REINFORCE_GATES_WALLS -> {
							showPage(talker, fnDoorStrengthen);
						}
						case REPLY_SIEGE_DEFEND_DEPLOY_TRAPS -> {
							showPage(talker, fnSetSlowZone);
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_REINFORCE_GATES_CHAT -> {
				npc.getVariables().set("gate_num", reply);
				showPage(talker, fnDoorLevel);
			}
			case MS_ASK_REINFORCE_DOOR_LEVEL -> {
				switch (reply) {
					case REPLY_REINFORCE_LEVEL_1 -> {
						npc.getVariables().set("gate_level", 200);
					}
					case REPLY_REINFORCE_LEVEL_2 -> {
						npc.getVariables().set("gate_level", 300);
					}
					case REPLY_REINFORCE_LEVEL_3 -> {
						npc.getVariables().set("gate_level", 500);
					}
				}
				
				final var i0 = npc.getVariables().getInt("gate_num");
				final var i1 = npc.getVariables().getInt("gate_level");
				
				var i2 = 0;
				if (i0 == 1 && i1 == 200) {
					i2 = castle().getOuterDoorUpgradePriceLvl2();
				}
				if (i0 == 1 && i1 == 300) {
					i2 = castle().getOuterDoorUpgradePriceLvl3();
				}
				if (i0 == 1 && i1 == 500) {
					i2 = castle().getOuterDoorUpgradePriceLvl5();
				}
				if ((i0 == 2 || i0 == 3) && i1 == 200) {
					i2 = castle().getInnerDoorUpgradePriceLvl2();
				}
				if ((i0 == 2 || i0 == 3) && i1 == 300) {
					i2 = castle().getInnerDoorUpgradePriceLvl3();
				}
				if ((i0 == 2 || i0 == 3) && i1 == 500) {
					i2 = castle().getInnerDoorUpgradePriceLvl5();
				}
				if (i0 == 4 && i1 == 200) {
					i2 = castle().getInnerDoorUpgradePriceLvl2();
				}
				if (i0 == 4 && i1 == 300) {
					i2 = castle().getInnerDoorUpgradePriceLvl3();
				}
				if (i0 == 4 && i1 == 500) {
					i2 = castle().getInnerDoorUpgradePriceLvl5();
				}
				if (i0 == 21 && i1 == 200) {
					i2 = castle().getWallUpgradePriceLvl2();
				}
				if (i0 == 21 && i1 == 300) {
					i2 = castle().getWallUpgradePriceLvl3();
				}
				if (i0 == 21 && i1 == 500) {
					i2 = castle().getWallUpgradePriceLvl5();
				}
				if (i0 == 22 && i1 == 200) {
					i2 = castle().getWallUpgradePriceLvl2();
				}
				if (i0 == 22 && i1 == 300) {
					i2 = castle().getWallUpgradePriceLvl3();
				}
				if (i0 == 22 && i1 == 500) {
					i2 = castle().getWallUpgradePriceLvl5();
				}
				
				if (getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DUSK) {
					i2 = ((i2 * SSQ_DuskFactor_door) / 100);
				} else {
					if (getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DAWN) {
						i2 = ((i2 * SSQ_DawnFactor_door) / 100);
					} else {
						i2 = ((i2 * SSQ_DrawFactor_door) / 100);
					}
				}
				
				npc.getVariables().set("gate_price", i2);
				var html = getHtm(talker.getHtmlPrefix(), fnDoorStrengthenConfirm);
				html = html.replace("<?gate_price?>", String.valueOf(i2));
				talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
			}
			case MS_ASK_REINFORCE_DOOR_CONFIRM -> {
				if (castle.getZone().isActive()) {
					showPage(talker, fnSiegeStoppedFunction);
				} else {
					if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
						final var i0 = npc.getVariables().getInt("gate_num");
						
						switch (i0) {
							case 1 -> {
								final var dDoorName11 = npc.getTemplate().getParameters().getString("DDoorName1_1", null);
								
								final var door = DoorData.getInstance().getDoorByName(dDoorName11);
								
								if (door != null) {
									setDoorHpLevel(talker, npc, door.getId());
								}
							}
							case 2 -> {
								final var dDoorName21 = npc.getTemplate().getParameters().getString("DDoorName2_1", null);
								
								final var door = DoorData.getInstance().getDoorByName(dDoorName21);
								
								if (door != null) {
									setDoorHpLevel(talker, npc, door.getId());
								}
							}
							case 3 -> {
								final var dDoorName31 = npc.getTemplate().getParameters().getString("DDoorName3_1", null);
								
								final var door = DoorData.getInstance().getDoorByName(dDoorName31);
								
								if (door != null) {
									setDoorHpLevel(talker, npc, door.getId());
								}
							}
							case 4 -> {
								final var dDoorName41 = npc.getTemplate().getParameters().getString("DDoorName4_1", null);
								
								final var door = DoorData.getInstance().getDoorByName(dDoorName41);
								
								if (door != null) {
									setDoorHpLevel(talker, npc, door.getId());
								}
							}
							case 21 -> {
								final var wallName1 = npc.getTemplate().getParameters().getString("WallName1", null);
								
								final var wall = DoorData.getInstance().getDoorByName(wallName1);
								
								if (wall != null) {
									setDoorHpLevel(talker, npc, wall.getId());
								}
							}
							case 22 -> {
								final var wallName3 = npc.getTemplate().getParameters().getString("WallName3", null);
								
								final var wall = DoorData.getInstance().getDoorByName(wallName3);
								
								if (wall != null) {
									setDoorHpLevel(talker, npc, wall.getId());
								}
							}
						}
					} else {
						showPage(talker, fnNoAuthority);
					}
				}
			}
			case MS_ASK_REINFORCE_DMG_ZONE_CHAT -> {
				switch (reply) {
					case 1 -> {
						npc.getVariables().set("dmgzone_num", 1);
					}
					case 2 -> {
						npc.getVariables().set("dmgzone_num", 2);
					}
				}
				showPage(talker, fnSetDmgLevel);
			}
			case MS_ASK_REINFORCE_DMG_ZONE_LEVEL -> {
				npc.getVariables().set("dmgzone_level", reply);
				
				var i0 = 0;
				
				switch (reply) {
					case 1 -> {
						i0 = castle().getTrapUpgradePriceLvl1();
					}
					case 2 -> {
						i0 = castle().getTrapUpgradePriceLvl2();
					}
					case 3 -> {
						i0 = castle().getTrapUpgradePriceLvl3();
					}
					case 4 -> {
						i0 = castle().getTrapUpgradePriceLvl4();
					}
				}
				
				if (getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DUSK) {
					i0 = ((i0 * SSQ_DuskFactor_dmg) / 100);
				} else {
					if (getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DAWN) {
						i0 = ((i0 * SSQ_DawnFactor_dmg) / 100);
					} else {
						i0 = ((i0 * SSQ_DrawFactor_dmg) / 100);
					}
				}
				
				npc.getVariables().set("dmgzone_price", i0);
				
				var html = getHtm(talker.getHtmlPrefix(), fnDmgZoneConfirm);
				html = html.replace("<?dmgzone_price?>", String.valueOf(i0));
				talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
			}
			case MS_ASK_REINFORCE_DMG_ZONE_CONFIRM -> {
				switch (reply) {
					case 1 -> {
						if (castle.getZone().isActive()) {
							showPage(talker, fnSiegeStoppedFunction);
						} else {
							final var i0 = npc.getVariables().getInt("dmgzone_num");
							if (i0 == 1) {
								setControlTowerLevel(talker, npc, i0 - 1);
							} else {
								if (i0 == 2) {
									setControlTowerLevel(talker, npc, i0 - 1);
								}
							}
						}
					}
				}
			}
			case MS_ASK_DISMISS -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_DISMISS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					if (castle.getZone().isActive()) {
						showPage(talker, fnSiegeStoppedFunction);
					} else {
						castle.banishForeigners();
						showPage(talker, fnAfterBanish);
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_WITHDRAW -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_TAXES) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					if (reply == 0) {
						showPage(talker, fnHi);
					} else {
						if (reply <= castle.getTreasury()) {
							castle.addToTreasuryNoTax((-1) * reply);
							giveItems(talker, ADENA, reply);
							showPage(talker, fnHi);
						} else {
							var html = getHtm(talker.getHtmlPrefix(), fnNotEnoughBalance);
							html = html.replace("<?tax_income?>", Util.formatAdena(castle.getTreasury()));
							html = html.replace("<?withdraw_amount?>", Util.formatAdena(reply));
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_DEPOSIT -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_TAXES) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					if (getQuestItemsCount(talker, ADENA) >= reply) {
						castle.addToTreasuryNoTax(reply);
						takeItems(talker, ADENA, reply);
						showPage(talker, fnHi);
					} else {
						talker.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_SELL_TICKET -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					if (castle.getZone().isActive()) {
						showPage(talker, fnSiegeStoppedFunction);
					} else {
						if ((SevenSigns.getInstance().getPlayerCabal(talker.getObjectId()) == SevenSigns.CABAL_DAWN) && SevenSigns.getInstance().isCompetitionPeriod()) {
							if (castle.getTicketBuyCount() < (sevenSigns().getSevenSignsDawnTicketQuantity() / sevenSigns().getSevenSignsDawnTicketBundle())) {
								if (getQuestItemsCount(talker, ADENA) >= (sevenSigns().getSevenSignsDawnTicketPrice() * sevenSigns().getSevenSignsDawnTicketBundle())) {
									takeItems(talker, ADENA, sevenSigns().getSevenSignsDawnTicketPrice() * sevenSigns().getSevenSignsDawnTicketBundle());
									giveItems(talker, sevenSigns().getSevenSignsManorsAgreementId(), sevenSigns().getSevenSignsDawnTicketBundle());
									castle.setTicketBuyCount(castle.getTicketBuyCount() + 1);
								} else {
									showPage(talker, fnNotEnoughMoney);
								}
							} else {
								showPage(talker, fnNotEnoughTicket);
							}
						} else {
							showPage(talker, fnNotDawnOrEvent);
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_MANAGE_REGEN_CHAT -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					String html;
					
					final var i0 = (reply / 1000);
					final var i1 = reply - (i0 * 1000);
					
					if (i1 == 0) {
						html = getHtm(talker.getHtmlPrefix(), fnDecoReset);
						html = html.replace("<?AgitDecoSubmit?>", String.valueOf(reply));
					} else {
						if ((castle.getFunction(i0) != null) && (castle.getFunction(i0).getLvl() == i1)) {
							html = getHtm(talker.getHtmlPrefix(), fnDecoAlreadySet);
						} else {
							html = getHtm(talker.getHtmlPrefix(), fnManageAgitDeco + i0 + ".htm");
							html = html.replace("<?AgitDecoCost?>", "(<font color=\"FFAABB\">" + getDecoFee(i0, i1) + "</font> Adena/" + (getDecoDay(i0, i1)  / 1000 / 60 / 60 / 24) + " Day(s))");
							html = html.replace("<?AgitDecoSubmit?>", String.valueOf(reply));
						}
						switch (i0) {
							case Castle.FUNC_RESTORE_HP -> {
								html = html.replace("<?AgitDecoEffect?>", (i1 - 10) * 20 + "%");
							}
							case Castle.FUNC_RESTORE_MP -> {
								html = html.replace("<?AgitDecoEffect?>", (i1 - 10) * 5 + "%");
							}
							case Castle.FUNC_RESTORE_EXP -> {
								html = html.replace("<?AgitDecoEffect?>", (i1 - 10) * 5 + "%");
							}
							case Castle.FUNC_DECO_CURTAINS -> {
								html = html.replace("<?AgitDecoEffect?>", i1 * 25 + "%");
							}
							default -> {
								html = html.replace("<?AgitDecoEffect?>", "Stage " + (i1 - 10));
							}
						}
					}
					talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_RESETUPDATE_CONFIRM -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					final var i0 = (reply / 1000);
					final var i1 = reply - (i0 * 1000);
					
					if (i1 == 0) {
						castle.removeFunction(i0);
						showPage(talker, fnAfterResetDeco);
					} else {
						if (getQuestItemsCount(talker, ADENA) >= getDecoFee(i0, i1)) {
							if (i0 == Castle.FUNC_SUPPORT) {
								if (i1 > 0) {
									final var i2 = 286130177 + (((i1 - 10) * 256) * 256);
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
							castle.updateFunctions(talker, i0, i1, getDecoFee(i0, i1), getDecoDay(i0, i1), (castle.getFunction(i0) == null));
							showPage(talker, fnAfterSetDeco);
						} else {
							showPage(talker, fnNotEnoughMoney);
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_SUPPORT_MAGIC -> {
				if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_USE_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
					npc.setTarget(talker);
					
					if (_supportList.containsKey(reply)) {
						String html;
						
						final var skill = _supportList.get(reply).getSkill();
						if (((skill.getMpConsume1() + skill.getMpConsume2()) < npc.getCurrentMp()) || castle().mpBuffFree()) {
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
	public void onManorMenuSelected(NpcManorBypass event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.target();
		
		final var request = event.request();
		
		final var manorId = event.manorId();
		final var nextPeriod = event.nextPeriod();
		
		if (npc.isMyLord(talker)) {
			final var manor = CastleManorManager.getInstance();
			if (manor.isUnderMaintenance()) {
				talker.sendPacket(SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
				return;
			}
			
			final var castleId = (manorId == -1) ? npc.getCastle().getResidenceId() : manorId;
			switch (request) {
				case 3 -> { // Seed info
					talker.sendPacket(new ExShowSeedInfo(castleId, nextPeriod, true));
				}
				case 4 -> { // Crop info
					talker.sendPacket(new ExShowCropInfo(castleId, nextPeriod, true));
				}
				case 5 -> { // Basic info
					talker.sendPacket(new ExShowManorDefaultInfo(true));
				}
				case 7 -> { // Seed settings
					if (manor.isManorApproved()) {
						talker.sendPacket(SystemMessageId.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_4_30_AM_AND_8_PM);
						return;
					}
					talker.sendPacket(new ExShowSeedSetting(castleId));
				}
				case 8 -> { // Crop settings
					if (manor.isManorApproved()) {
						talker.sendPacket(SystemMessageId.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_4_30_AM_AND_8_PM);
						return;
					}
					talker.sendPacket(new ExShowCropSetting(castleId));
				}
			}
		}
	}
	
	@Override
	public void onTeleportRequest(PlayerTeleportRequest event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var castle = npc.getCastle();
		
		if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_OPEN_DOOR) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
			if (talker.getTransformationId() == 111 || talker.getTransformationId() == 112 || talker.getTransformationId() == 124) {
				showPage(talker, fnQ194NoTeleport);
			} else {
				if (castle.getFunction(Castle.FUNC_TELEPORT) == null) {
					showPage(talker, fnFuncDisabled);
				} else {
					if (castle.getFunction(Castle.FUNC_TELEPORT).getLvl() == 11) {
						teleportFStr(talker, npc, position1, ADENA);
					} else {
						if (castle.getFunction(Castle.FUNC_TELEPORT).getLvl() == 12) {
							teleportFStr(talker, npc, position2, ADENA);
						}
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
	
	private static int getSealOwner(int seal) {
		final var npcString = switch (SevenSigns.getInstance().getSealOwner(seal)) {
			case SevenSigns.CABAL_DAWN -> 1000511;
			case SevenSigns.CABAL_DUSK -> 1000510;
			default -> 1000512;
		};
		return npcString;
	}
	
	private static int getTaxLimit() {
		final int taxLimit = switch (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE)) {
			case SevenSigns.CABAL_DAWN -> 25;
			case SevenSigns.CABAL_DUSK -> 5;
			default -> 15;
		};
		return taxLimit;
	}
	
	private static boolean isDomainFortressInContractStatus(L2Npc npc) {
		_fortress.clear();
		
		final var fortressId1 = npc.getTemplate().getParameters().getInt("fortress_id1", 0);
		final var fortressId2 = npc.getTemplate().getParameters().getInt("fortress_id2", 0);
		final var fortressId3 = npc.getTemplate().getParameters().getInt("fortress_id3", 0);
		final var fortressId4 = npc.getTemplate().getParameters().getInt("fortress_id4", 0);
		final var fortressId5 = npc.getTemplate().getParameters().getInt("fortress_id5", 0);
		
		_fortress.add(fortressId1);
		_fortress.add(fortressId2);
		_fortress.add(fortressId3);
		_fortress.add(fortressId4);
		_fortress.add(fortressId5);
		
		for (var fort : _fortress) {
			final var fortress = FortManager.getInstance().getFortById(fort);
			if (fortress != null) {
				if (fortress.getFortState() == 2) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static final int getDecoFee(int function, int level) {
		switch (function) {
			case Castle.FUNC_RESTORE_HP -> {
				switch (level) {
					case 25 -> {
						return castle().getHpRegenerationFeeLvl25();
					}
					case 30 -> {
						return castle().getHpRegenerationFeeLvl30();
					}
				}
			}
			case Castle.FUNC_RESTORE_MP -> {
				switch (level) {
					case 18 -> {
						return castle().getMpRegenerationFeeLvl18();
					}
					case 20 -> {
						return castle().getMpRegenerationFeeLvl20();
					}
				}
			}
			case Castle.FUNC_RESTORE_EXP -> {
				switch (level) {
					case 19 -> {
						return castle().getExpRegenerationFeeLvl19();
					}
					case 20 -> {
						return castle().getExpRegenerationFeeLvl20();
					}
				}
			}
			case Castle.FUNC_TELEPORT -> {
				switch (level) {
					case 11 -> {
						return castle().getTeleportFunctionFeeLvl11();
					}
					case 12 -> {
						return castle().getTeleportFunctionFeeLvl12();
					}
				}
			}
			case Castle.FUNC_SUPPORT -> {
				switch (level) {
					case 15 -> {
						return castle().getSupportFeeLvl15();
					}
					case 18 -> {
						return castle().getSupportFeeLvl18();
					}
				}
			}
		}
		return 0;
	}
	
	private static final long getDecoDay(int function, int level) {
		switch (function) {
			case Castle.FUNC_RESTORE_HP -> {
				switch (level) {
					case 25, 30 -> {
						return castle().getFunctionFeeDay7();
					}
				}
			}
			case Castle.FUNC_RESTORE_MP -> {
				switch (level) {
					case 18, 20 -> {
						return castle().getFunctionFeeDay7();
					}
				}
			}
			case Castle.FUNC_RESTORE_EXP -> {
				switch (level) {
					case 19, 20 -> {
						return castle().getFunctionFeeDay7();
					}
				}
			}
			case Castle.FUNC_TELEPORT -> {
				switch (level) {
					case 11, 12 -> {
						return castle().getFunctionFeeDay7();
					}
				}
			}
			case Castle.FUNC_SUPPORT -> {
				switch (level) {
					case 15, 18 -> {
						return castle().getFunctionFeeDay7();
					}
				}
			}
		}
		return 0;
	}
	
	private void setDoorHpLevel(L2PcInstance talker, L2Npc npc, int doorId) {
		final var castle = npc.getCastle();
		
		if (castle.getZone().isActive()) {
			showPage(talker, fnSiegeStoppedFunction);
		} else {
			if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
				final var i0 = npc.getVariables().getInt("gate_price");
				var i1 = npc.getVariables().getInt("gate_level");
				final var i2 = npc.getVariables().getInt("gate_num");
				
				final var door = DoorData.getInstance().getDoor(doorId);
				final var level = door.getStat().getUpgradeHpRatio() * 100;
				
				if (level >= i1) {
					var html = getHtm(talker.getHtmlPrefix(), fnCurrentDoorLevelHigher);
					html = html.replace("<?doorlevel?>", String.valueOf(level));
					talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
				} else {
					if (getQuestItemsCount(talker, ADENA) >= i0) {
						takeItems(talker, ADENA, i0);
						
						i1 = i1 / 100;
						
						switch (i2) {
							case 1 -> {
								final var dDoorName11 = npc.getTemplate().getParameters().getString("DDoorName1_1", null);
								final var dDoorName12 = npc.getTemplate().getParameters().getString("DDoorName1_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName11);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName12);
								
								if (door1 != null) {
									castle.setDoorUpgrade(door1.getId(), i1, true);
								}
								if (door2 != null) {
									castle.setDoorUpgrade(door2.getId(), i1, true);
								}
							}
							case 2 -> {
								final var dDoorName21 = npc.getTemplate().getParameters().getString("DDoorName2_1", null);
								final var dDoorName22 = npc.getTemplate().getParameters().getString("DDoorName2_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName21);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName22);
								
								if (door1 != null) {
									castle.setDoorUpgrade(door1.getId(), i1, true);
								}
								if (door2 != null) {
									castle.setDoorUpgrade(door2.getId(), i1, true);
								}
							}
							case 3 -> {
								final var dDoorName31 = npc.getTemplate().getParameters().getString("DDoorName3_1", null);
								final var dDoorName32 = npc.getTemplate().getParameters().getString("DDoorName3_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName31);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName32);
								
								if (door1 != null) {
									castle.setDoorUpgrade(door1.getId(), i1, true);
								}
								if (door2 != null) {
									castle.setDoorUpgrade(door2.getId(), i1, true);
								}
							}
							case 4 -> {
								final var dDoorName41 = npc.getTemplate().getParameters().getString("DDoorName4_1", null);
								final var dDoorName42 = npc.getTemplate().getParameters().getString("DDoorName4_2", null);
								
								final var door1 = DoorData.getInstance().getDoorByName(dDoorName41);
								final var door2 = DoorData.getInstance().getDoorByName(dDoorName42);
								
								if (door1 != null) {
									castle.setDoorUpgrade(door1.getId(), i1, true);
								}
								if (door2 != null) {
									castle.setDoorUpgrade(door2.getId(), i1, true);
								}
							}
							case 21 -> {
								final var wallName1 = npc.getTemplate().getParameters().getString("WallName1", null);
								final var wallName2 = npc.getTemplate().getParameters().getString("WallName2", null);
								
								final var wall1 = DoorData.getInstance().getDoorByName(wallName1);
								final var wall2 = DoorData.getInstance().getDoorByName(wallName2);
								
								if (wall1 != null) {
									castle.setDoorUpgrade(wall1.getId(), i1, true);
								}
								if (wall2 != null) {
									castle.setDoorUpgrade(wall2.getId(), i1, true);
								}
							}
							case 22 -> {
								final var wallName3 = npc.getTemplate().getParameters().getString("WallName3", null);
								
								final var wall = DoorData.getInstance().getDoorByName(wallName3);
								
								if (wall != null) {
									castle.setDoorUpgrade(wall.getId(), i1, true);
								}
							}
						}
						showPage(talker, fnDoorHpLevelUp);
					} else {
						showPage(talker, fnNotEnoughMoney);
					}
				}
			} else {
				showPage(talker, fnNoAuthority);
			}
		}
	}
	
	private void setControlTowerLevel(L2PcInstance talker, L2Npc npc, int zoneIndex) {
		final var castle = npc.getCastle();
		
		if (castle.getZone().isActive()) {
			showPage(talker, fnSiegeStoppedFunction);
		} else {
			if (npc.isMyLord(talker) || talker.hasClanPrivilege(ClanPrivilege.CS_SET_FUNCTIONS) && (talker.getClan() != null) && (castle.getOwnerId() == talker.getClanId())) {
				final var i0 = npc.getVariables().getInt("dmgzone_price");
				final var i1 = npc.getVariables().getInt("dmgzone_level");
				final var i2 = npc.getVariables().getInt("dmgzone_num");
				
				final var level = castle.getTrapUpgradeLevel(zoneIndex);
				
				if (level >= i1) {
					var html = getHtm(talker.getHtmlPrefix(), fnCurrentDmgzoneLevelHigher);
					html = html.replace("<?dmglevel?>", String.valueOf(level));
					talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
				} else {
					if (getQuestItemsCount(talker, ADENA) >= i0) {
						takeItems(talker, ADENA, i0);
						
						switch (i2) {
							case 1 -> {
								castle.setTrapUpgrade(zoneIndex, i1, true);
							}
							case 2 -> {
								castle.setTrapUpgrade(zoneIndex, i1, true);
							}
						}
						showPage(talker, fnDmgZoneLevelUp);
					} else {
						showPage(talker, fnNotEnoughMoney);
					}
				}
			} else {
				showPage(talker, fnNoAuthority);
			}
		}
	}
	
	private static final String getNpcString(int id) {
		return "<fstring>" + id + "</fstring>";
	}
}