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

public class NullKnownList extends ObjectKnownList {
	public NullKnownList(L2Object activeObject) {
		super(activeObject);
	}
	
	@Override
	public boolean addKnownObject(L2Object object) {
		return false;
	}
	
	@Override
	public L2Object getActiveObject() {
		return super.getActiveObject();
	}
	
	@Override
	public int getDistanceToForgetObject(L2Object object) {
		return 0;
	}
	
	@Override
	public int getDistanceToWatchObject(L2Object object) {
		return 0;
	}
	
	@Override
	public void removeAllKnownObjects() {
	}
	
	@Override
	protected boolean removeKnownObject(L2Object object, boolean forget) {
		return false;
	}
}