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
package com.l2jserver.datapack.ai.npc.Merchant;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Charus
 * @version 2.6.3.0
 */
public abstract class MerchantPrimeval extends Merchant {
	
	private static final int QElrokianHuntersProof = 111;
	
	public MerchantPrimeval(int npcId) {
		super(npcId);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance talker) {
		if (getOneTimeQuestFlag(talker, QElrokianHuntersProof) == 0) {
			showPage(talker, fnPath + "asama001.htm");
		} else {
			showPage(talker, fnPath + "asama002.htm");
		}
		
		return null;
	}
}