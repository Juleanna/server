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
package com.l2jserver.loginserver.network.clientpackets;

import static com.l2jserver.loginserver.config.Configuration.server;

import com.l2jserver.loginserver.LoginController;
import com.l2jserver.loginserver.SessionKey;
import com.l2jserver.loginserver.network.serverpackets.LoginFail.LoginFailReason;
import com.l2jserver.loginserver.network.serverpackets.PlayFail.PlayFailReason;
import com.l2jserver.loginserver.network.serverpackets.PlayOk;

/**
 * <pre>
 * Format is ddc
 * d: first part of session id
 * d: second part of session id
 * c: server ID
 * </pre>
 * 
 * @version 2.6.1.0
 */
public class RequestServerLogin extends L2LoginClientPacket {
	private int _skey1;
	private int _skey2;
	private int _serverId;
	
	public int getSessionKey1() {
		return _skey1;
	}
	
	public int getSessionKey2() {
		return _skey2;
	}
	
	public int getServerID() {
		return _serverId;
	}
	
	@Override
	public boolean readImpl() {
		if (super._buf.remaining() >= 9) {
			_skey1 = readD();
			_skey2 = readD();
			_serverId = readC();
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		SessionKey sk = getClient().getSessionKey();
		// if we didn't show the license we can't check these values
		if (!server().showLicense() || sk.checkLoginPair(_skey1, _skey2)) {
			if (LoginController.getInstance().isLoginPossible(getClient(), _serverId)) {
				getClient().setJoinedGS(true);
				getClient().sendPacket(new PlayOk(sk));
			} else {
				getClient().close(PlayFailReason.REASON_SERVER_OVERLOADED);
			}
		} else {
			getClient().close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
