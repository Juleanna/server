/*
 * Copyright © 2004-2020 L2J Server
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
package com.l2jserver.loginserver;

import static com.l2jserver.loginserver.config.Configuration.server;

/**
 * This class is used to represent session keys used by the client to authenticate in the game server.<br>
 * A SessionKey is made up of two 8 bytes keys. One is sent in the {@link com.l2jserver.loginserver.network.serverpackets.LoginOk#LoginOk} packet and the other is sent in {@link com.l2jserver.loginserver.network.serverpackets.PlayOk#PlayOk}
 * @author -Wooden-
 * @version 2.6.1.0
 */
public class SessionKey {
	public final int playOkID1;
	public final int playOkID2;
	public final int loginOkID1;
	public final int loginOkID2;
	
	public SessionKey(int loginOK1, int loginOK2, int playOK1, int playOK2) {
		playOkID1 = playOK1;
		playOkID2 = playOK2;
		loginOkID1 = loginOK1;
		loginOkID2 = loginOK2;
	}
	
	@Override
	public String toString() {
		return "PlayOk: " + playOkID1 + " " + playOkID2 + " LoginOk:" + loginOkID1 + " " + loginOkID2;
	}
	
	public boolean checkLoginPair(int loginOk1, int loginOk2) {
		return (loginOkID1 == loginOk1) && (loginOkID2 == loginOk2);
	}
	
	/**
	 * Only checks the PlayOk part of the session key if server doesn't show the license when player logs in.
	 * @param other the other session key to validate
	 * @return true if keys are equal.
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SessionKey key)) {
			return false;
		}
		// when server doesn't show license it doesn't send the LoginOk packet, client doesn't have this part of the key then.
		if (server().showLicense()) {
			return ((playOkID1 == key.playOkID1) && (loginOkID1 == key.loginOkID1) && (playOkID2 == key.playOkID2) && (loginOkID2 == key.loginOkID2));
		}
		return ((playOkID1 == key.playOkID1) && (playOkID2 == key.playOkID2));
	}
}