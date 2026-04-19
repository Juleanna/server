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
package com.l2jserver.datapack.ai.npc.Janitor;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.instancemanager.ClanHallManager;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.ClanHall;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Evolve;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public class Janitor extends AbstractNpcAI {
	
	private static final int[] npcIds = { 35385, 35387, 35389, 35391, 35393, 35395, 35397, 35399, 35401, 35402, 35404, 35406, 35440, 35442, 35444, 35446, 35448, 35450, 35452, 35454, 35456, 35458, 35460, 35462, 35464, 35466, 35468, 35567, 35569, 35571, 35573, 35575, 35577, 35579, 35581, 35583, 35585, 35587 };
	
	private static final String fnFeudInfo = "data/html/clanHallDoormen/defaultAgitInfo.htm";
	private static final String fnNoFeudInfo = "data/html/clanHallDoormen/noAgitInfo.htm";
	private static final String fnOwner = "data/html/clanHallDoormen/AgitJanitorHi.htm";
	private static final String fnWyvernOwner = "data/html/clanHallDoormen/WyvernAgitJanitorHi.htm";
	private static final String fnNoAuthority = "data/html/clanHallDoormen/noAuthority.htm";
	private static final String fnAfterDoorOpen = "data/html/clanHallDoormen/AgitJanitorAfterDoorOpen.htm";
	private static final String fnAfterDoorClose = "data/html/clanHallDoormen/AgitJanitorAfterDoorClose.htm";
	
	private static final String fnEvolutionSuccess = "data/html/clanHallDoormen/pet_evolution_success.htm";
	private static final String fnEvolutionStopped = "data/html/clanHallDoormen/pet_evolution_stopped.htm";
	private static final String fnEvolveManyPet = "data/html/clanHallDoormen/pet_evolution_many_pet.htm";
	private static final String fnEvolveNoPetPet = "data/html/clanHallDoormen/pet_evolution_no_pet.htm";
	private static final String fnNoPetPet = "data/html/clanHallDoormen/pet_evolution_farpet.htm";
	private static final String fnTooFarPet = "data/html/clanHallDoormen/pet_evolution_farpet.htm";
	private static final String fnNoProperPetPet = "data/html/clanHallDoormen/pet_evolution_farpet.htm";
	private static final String fnNotEnoughMinLvPet = "data/html/clanHallDoormen/pet_evolution_level.htm";
	private static final String fnNotEnoughLevelPet = "data/html/clanHallDoormen/pet_evolution_level.htm";
	private static final String fnNoItemPet = "data/html/clanHallDoormen/pet_evolution_no_pet.htm";
	
	private static final int MS_ASK_BACK = 0;
	private static final int MS_ASK_OPENCLOSE_DOOR = -203;
	private static final int MS_ASK_EVOLVE_PET = -1002;
	
	private static final int REPLY_BACK = 0;
	private static final int REPLY_OPEN_DOOR = 1;
	private static final int REPLY_CLOSE_DOOR = 2;
	private static final int REPLY_EVOLVE_GREAT_WOLF = 1;
	private static final int REPLY_EVOLVE_WIND_STRIDER = 2;
	private static final int REPLY_EVOLVE_STAR_STRIDER = 3;
	private static final int REPLY_EVOLVE_TWILIGHT_STRIDER = 4;
	private static final int REPLY_EVOLVE_FENRIR = 5;
	
	private volatile boolean _init = false;
	private ClanHall _clanHall = null;
	
	public Janitor() {
		bindFirstTalk(npcIds);
		bindMenuSelected(npcIds);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var clanHall = getClanHall(npc);
		
		final var isWyvern = npc.getTemplate().getParameters().getInt("Is_Wyvern", 0);
		
		String html;
		
		if (talker.hasClanPrivilege(ClanPrivilege.CH_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
			if (isWyvern == 1) {
				html = getHtm(talker.getHtmlPrefix(), fnWyvernOwner);
			} else {
				html = getHtm(talker.getHtmlPrefix(), fnOwner);
			}
			html = html.replace("<?my_pledge_name?>", talker.getClan().getName());
			talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
		} else {
			final var clan = ClanTable.getInstance().getClan(clanHall.getOwnerId());
			if (clan != null) {
				html = getHtm(talker.getHtmlPrefix(), fnFeudInfo);
				html = html.replace("<?my_pledge_name?>", clan.getName());
				html = html.replace("<?my_owner_name?>", clan.getLeaderName());
				talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
			} else {
				html = getHtm(talker.getHtmlPrefix(), fnNoFeudInfo);
			}
			talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
		}
		
		return super.onFirstTalk(npc, talker);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var clanHall = getClanHall(npc);
		
		switch (ask) {
			case MS_ASK_BACK -> {
				switch (reply) {
					case REPLY_BACK -> {
						final var isWyvern = npc.getTemplate().getParameters().getInt("Is_Wyvern", 0);
						
						String html;
						
						if (talker.hasClanPrivilege(ClanPrivilege.CH_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
							if (isWyvern == 1) {
								html = getHtm(talker.getHtmlPrefix(), fnWyvernOwner);
							} else {
								html = getHtm(talker.getHtmlPrefix(), fnOwner);
							}
							html = html.replace("<?my_pledge_name?>", talker.getClan().getName());
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						} else {
							final var clan = ClanTable.getInstance().getClan(clanHall.getOwnerId());
							if (clan != null) {
								html = getHtm(talker.getHtmlPrefix(), fnFeudInfo);
								html = html.replace("<?my_pledge_name?>", clan.getName());
								html = html.replace("<?my_owner_name?>", clan.getLeaderName());
								talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							} else {
								html = getHtm(talker.getHtmlPrefix(), fnNoFeudInfo);
							}
							talker.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						}
					}
				}
			}
			case MS_ASK_OPENCLOSE_DOOR -> {
				if (talker.hasClanPrivilege(ClanPrivilege.CH_OPEN_DOOR) && (talker.getClan() != null) && (clanHall.getOwnerId() == talker.getClanId())) {
					switch (reply) {
						case REPLY_OPEN_DOOR -> {
							clanHall.openCloseDoors(true);
							showPage(talker, fnAfterDoorOpen);
						}
						case REPLY_CLOSE_DOOR -> {
							clanHall.openCloseDoors(false);
							showPage(talker, fnAfterDoorClose);
						}
					}
				} else {
					showPage(talker, fnNoAuthority);
				}
			}
			case MS_ASK_EVOLVE_PET -> {
				switch (reply) {
					case REPLY_EVOLVE_GREAT_WOLF, REPLY_EVOLVE_WIND_STRIDER, REPLY_EVOLVE_STAR_STRIDER, REPLY_EVOLVE_TWILIGHT_STRIDER, REPLY_EVOLVE_FENRIR -> {
						final var itemBabyPet = npc.getTemplate().getParameters().getInt("item_baby_pet" + reply, -1);
						final var itemGrownPet = npc.getTemplate().getParameters().getInt("item_grown_pet" + reply, -1);
						final var classIdBabyPet = npc.getTemplate().getParameters().getInt("class_id_baby_pet" + reply, -1);
						final var minLvPet = npc.getTemplate().getParameters().getInt("min_lv_pet" + reply, -1);
						
						final var checkMinLvPet = npc.getTemplate().getParameters().getInt("check_min_lv_pet" + reply, 0);
						final var skipCheckSummonPet = npc.getTemplate().getParameters().getInt("skip_chk_summon_pet" + reply, 0);
						
						if (itemBabyPet == -1) {
							return;
						}
						
						if (getQuestItemsCount(talker, itemBabyPet) >= 2) {
							showPage(talker, fnEvolveManyPet);
							return;
						}
						
						if ((getQuestItemsCount(talker, itemBabyPet) <= 0) && (getQuestItemsCount(talker, itemGrownPet) > 0)) {
							showPage(talker, fnEvolveNoPetPet);
							return;
						}
						
						if (skipCheckSummonPet == 0) {
							final var c0 = talker.getSummon();
							if (c0 == null && !talker.hasPet()) {
								showPage(talker, fnNoPetPet);
								return;
							}
							if (npc.calculateDistance(c0, true, false) >= 200) {
								showPage(talker, fnTooFarPet);
								return;
							}
							if (c0.getId() != classIdBabyPet) {
								showPage(talker, fnNoProperPetPet);
								return;
							}
							if ((c0.getLevel() < minLvPet) && (checkMinLvPet == 1)) {
								showPage(talker, fnNotEnoughMinLvPet);
								return;
							}
						}
						
						final var item0 = talker.getInventory().getItemByItemId(itemBabyPet);
						if (item0 != null) {
							if (item0.getEnchantLevel() < minLvPet) {
								showPage(talker, fnNotEnoughLevelPet);
								return;
							}
							
							if (skipCheckSummonPet == 0) {
								Evolve.doEvolve(talker, npc, itemBabyPet, itemGrownPet, item0.getEnchantLevel());
							} else {
								final var c0 = talker.getSummon();
								if (c0 != null || talker.hasPet() && skipCheckSummonPet == 1) {
									showPage(talker, fnEvolutionStopped);
									return;
								}
								
								Evolve.doRestore(talker, npc, itemBabyPet, itemGrownPet, minLvPet);
							}
							
							showPage(talker, fnEvolutionSuccess);
						} else {
							showPage(talker, fnNoItemPet);
						}
					}
				}
			}
		}
	}
	
	private ClanHall getClanHall(L2Npc npc) {
		if (!_init) {
			synchronized (npc) {
				if (!_init) {
					_clanHall = ClanHallManager.getInstance().getNearbyClanHall(npc.getX(), npc.getY(), 500);
					_init = true;
				}
			}
		}
		return _clanHall;
	}
}