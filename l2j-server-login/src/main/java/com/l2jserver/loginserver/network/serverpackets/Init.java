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
package com.l2jserver.loginserver.network.serverpackets;

import com.l2jserver.loginserver.network.L2LoginClient;

/**
 * <pre>
 * Format: dd b dddd s
 * d: session id
 * d: protocol revision
 * b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key
 *                 0x10 bytes at 0x00
 * d: unknown
 * d: unknown
 * d: unknown
 * d: unknown
 * s: blowfish key
 * </pre>
 * 
 * @version 2.6.1.0
 */
public final class Init extends L2LoginServerPacket {
	private final int _sessionId;
	
	private final byte[] _publicKey;
	private final byte[] _blowfishKey;
	
	public Init(L2LoginClient client) {
		this(client.getScrambledModulus(), client.getBlowfishKey(), client.getSessionId());
	}
	
	public Init(byte[] publicKey, byte[] blowfishKey, int sessionId) {
		_sessionId = sessionId;
		_publicKey = publicKey;
		_blowfishKey = blowfishKey;
	}
	
	@Override
	protected void write() {
		writeC(0x00); // init packet id
		
		writeD(_sessionId); // session id
		writeD(0x0000c621); // protocol revision
		
		writeB(_publicKey); // RSA Public Key
		
		// unk GG related?
		writeD(0x29DD954E);
		writeD(0x77C39CFC);
		writeD(0x97ADB620);
		writeD(0x07BDE0F7);
		
		writeB(_blowfishKey); // BlowFish key
		writeC(0x00); // null termination ;)
	}
}
