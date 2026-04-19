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
package com.l2jserver.datapack.ai.npc.Merchant.PetManager;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.util.Evolve;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class PetManager extends Merchant {
	
	protected String fnEvolutionSuccess = "pet_evolution_success.htm";
	private static final String fnEvolutionStopped = "pet_evolution_stopped.htm";
	
	protected String fnEvolveManyPet = "pet_evolution_many_pet.htm";
	protected String fnEvolveNoPetPet = "pet_evolution_no_pet.htm";
	protected String fnNoPetPet = "pet_evolution_farpet.htm";
	protected String fnTooFarPet = "pet_evolution_farpet.htm";
	protected String fnNoProperPetPet = "pet_evolution_farpet.htm";
	protected String fnNotEnoughLevelPet = "pet_evolution_level.htm";
	protected String fnNotEnoughMinLvPet = "pet_evolution_level.htm";
	protected String fnNoItemPet = "pet_evolution_no_pet.htm";
	
	private static final int ticketKukaburoOcarina = 7585;
	private static final int babyKukaburoOcarina = 6650;
	private static final int ticketBuffaloPanpipe = 7583;
	private static final int babyBuffaloPanpipe = 6648;
	private static final int ticketCougarChime = 7584;
	private static final int babyCougarChime = 6649;
	
	private static final int MS_ASK_EXCHANGE_PET = -1001;
	private static final int MS_ASK_EVOLVE_PET = -1002;
	
	private static final int MS_REPLY_EXCHANGE_KOOKABURRA = 0;
	private static final int MS_REPLY_EXCHANGE_BUFFALO = 1;
	private static final int MS_REPLY_EXCHANGE_COUGAR = 2;
	private static final int REPLY_EVOLVE_GREAT_WOLF = 1;
	private static final int REPLY_EVOLVE_WIND_STRIDER = 2;
	private static final int REPLY_EVOLVE_STAR_STRIDER = 3;
	private static final int REPLY_EVOLVE_TWILIGHT_STRIDER = 4;
	private static final int REPLY_EVOLVE_FENRIR = 5;
	
	public PetManager(int npcId) {
		super(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		final var skipCheckSummonPet = npc.getTemplate().getParameters().getInt("skip_chk_summon_pet1", 0);
		
		final var c0 = talker.getSummon();
		if (c0 != null && skipCheckSummonPet == 1) {
			showPage(talker, fnPath + fnEvolutionStopped);
		} else {
			super.onFirstTalk(npc, talker);
		}
		
		return null;
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_EXCHANGE_PET -> {
				switch (reply) {
					case MS_REPLY_EXCHANGE_KOOKABURRA -> {
						if (getQuestItemsCount(talker, ticketKukaburoOcarina) > 0) {
							takeItems(talker, ticketKukaburoOcarina, 1);
							giveItems(talker, babyKukaburoOcarina, 1);
							showPage(talker, fnPath + "pet_manager_trade_pet.htm");
						} else {
							showPage(talker, fnPath + "pet_manager_no_ticket.htm");
						}
					}
					case MS_REPLY_EXCHANGE_BUFFALO -> {
						if (getQuestItemsCount(talker, ticketBuffaloPanpipe) > 0) {
							takeItems(talker, ticketBuffaloPanpipe, 1);
							giveItems(talker, babyBuffaloPanpipe, 1);
							showPage(talker, fnPath + "pet_manager_trade_pet.htm");
						} else {
							showPage(talker, fnPath + "pet_manager_no_ticket.htm");
						}
					}
					case MS_REPLY_EXCHANGE_COUGAR -> {
						if (getQuestItemsCount(talker, ticketCougarChime) > 0) {
							takeItems(talker, ticketCougarChime, 1);
							giveItems(talker, babyCougarChime, 1);
							showPage(talker, fnPath + "pet_manager_trade_pet.htm");
						} else {
							showPage(talker, fnPath + "pet_manager_no_ticket.htm");
						}
					}
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
							showPage(talker, fnPath + fnEvolveManyPet);
							return;
						}
						
						if ((getQuestItemsCount(talker, itemBabyPet) <= 0) && (getQuestItemsCount(talker, itemGrownPet) > 0)) {
							showPage(talker, fnPath + fnEvolveNoPetPet);
							return;
						}
						
						if (skipCheckSummonPet == 0) {
							final var c0 = talker.getSummon();
							if (c0 == null && !talker.hasPet()) {
								showPage(talker, fnPath + fnNoPetPet);
								return;
							}
							if (npc.calculateDistance(c0, true, false) >= 200) {
								showPage(talker, fnPath + fnTooFarPet);
								return;
							}
							if (c0.getId() != classIdBabyPet) {
								showPage(talker, fnPath + fnNoProperPetPet);
								return;
							}
							if ((c0.getLevel() < minLvPet) && (checkMinLvPet == 1)) {
								showPage(talker, fnPath + fnNotEnoughMinLvPet);
								return;
							}
						}
						
						final var item0 = talker.getInventory().getItemByItemId(itemBabyPet);
						if (item0 != null) {
							if (item0.getEnchantLevel() < minLvPet) {
								showPage(talker, fnPath + fnNotEnoughLevelPet);
								return;
							}
							
							if (skipCheckSummonPet == 0) {
								Evolve.doEvolve(talker, npc, itemBabyPet, itemGrownPet, item0.getEnchantLevel());
							} else {
								final var c0 = talker.getSummon();
								if (c0 != null || talker.hasPet() && skipCheckSummonPet == 1) {
									showPage(talker, fnPath + fnEvolutionStopped);
									return;
								}
								
								Evolve.doRestore(talker, npc, itemBabyPet, itemGrownPet, minLvPet);
							}
							
							showPage(talker, fnPath + fnEvolutionSuccess);
						} else {
							showPage(talker, fnPath + fnNoItemPet);
						}
					}
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
}