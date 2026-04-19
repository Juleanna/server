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
package com.l2jserver.datapack.ai.npc.Merchant.Fisher;

import static com.l2jserver.gameserver.model.base.AcquireSkillType.FISHING;
import static com.l2jserver.gameserver.network.SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1;
import static com.l2jserver.gameserver.network.SystemMessageId.NO_MORE_SKILLS_TO_LEARN;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLearnSkillRequested;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSkillLearned;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Adry_85
 * @version 2.6.3.0
 */
public abstract class Fisher extends Merchant {
	
	private static final int MS_ASK_CLAIM_PRIZE = -401;
	private static final int MS_ASK_FISHING_CONTEST = -404;
	
	private static final int MS_REPLY_CLAIM_PRIZE = 1;
	private static final int MS_REPLY_FISHING_CONTEST = 1;
	
	public Fisher(int npcId) {
		super(npcId);
		bindLearnSkillRequested(npcId);
		bindSkillLearned(npcId);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_CLAIM_PRIZE -> {
				switch (reply) {
					case MS_REPLY_CLAIM_PRIZE -> {
						if (getFishingEventRewardRemainTime() == 0) {
							showPage(talker, fnPath + "no_fish_event_reward001.htm");
						} else {
							giveFishingEventPrize(talker);
							showPage(talker, fnPath + "fish_event_reward001.htm");
						}
					}
				}
			}
			case MS_ASK_FISHING_CONTEST -> {
				switch (reply) {
					case MS_REPLY_FISHING_CONTEST -> {
						showHtmlFishingEventRanking(talker);
					}
				}
			}
			default -> {
				super.onMenuSelected(event);
			}
		}
	}
	
	@Override
	public void onLearnSkillRequested(PlayerLearnSkillRequested event) {
		final var talker = event.player();
		
		showFishSkillList(talker);
	}
	
	@Override
	public void onSkillLearned(PlayerSkillLearned event) {
		final var talker = event.player();
		
		showFishSkillList(talker);
	}
	
	private void showHtmlFishingEventRanking(L2PcInstance talker) {
		showPage(talker, fnPath + "no_fish_event001.htm"); // TODO implement fishing event
	}
	
	private int getFishingEventRewardRemainTime() {
		return 0; // TODO implement fishing event
	}
	
	private void giveFishingEventPrize(L2PcInstance talker) {
		// TODO implement fishing event
	}
	
	/**
	 * Display the Fishing Skill list to the player.
	 * @param player the player
	 */
	private void showFishSkillList(L2PcInstance talker) {
		final var skills = SkillTreesData.getInstance().getAvailableFishingSkills(talker);
		if (skills.size() > 0) {
			talker.sendPacket(new AcquireSkillList(FISHING, skills));
		} else {
			final int minlLevel = SkillTreesData.getInstance().getMinLevelForNewSkill(talker, SkillTreesData.getInstance().getFishingSkillTree());
			if (minlLevel > 0) {
				final var sm = SystemMessage.getSystemMessage(DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1);
				sm.addInt(minlLevel);
				talker.sendPacket(sm);
			} else {
				talker.sendPacket(NO_MORE_SKILLS_TO_LEARN);
			}
		}
	}
}