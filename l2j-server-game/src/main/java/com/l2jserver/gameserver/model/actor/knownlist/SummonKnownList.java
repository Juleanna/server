/*
 * Copyright © 2004-2023 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.actor.knownlist;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Summon;

public class SummonKnownList extends PlayableKnownList {
	public SummonKnownList(L2Summon activeChar) {
		super(activeChar);
	}
	
	@Override
	public final L2Summon getActiveChar() {
		return (L2Summon) super.getActiveChar();
	}
	
	@Override
	public int getDistanceToForgetObject(L2Object object) {
		if ((object == getActiveChar().getOwner()) || (object == getActiveChar().getTarget())) {
			return 6000;
		}
		return 3000;
	}
	
	@Override
	public int getDistanceToWatchObject(L2Object object) {
		return 1500;
	}
}
