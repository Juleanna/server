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
package com.l2jserver.gameserver.network;

import com.l2jserver.commons.util.Rnd;

/**
 * Blowfish keygen for GameServer client connections.
 * @author KenM
 */
public class BlowFishKeygen {
	private static final int CRYPT_KEYS_SIZE = 20;
	private static final byte[][] CRYPT_KEYS = new byte[CRYPT_KEYS_SIZE][16];
	
	static {
		// init the GS encryption keys on class load
		
		for (int i = 0; i < CRYPT_KEYS_SIZE; i++) {
			// randomize the 8 first bytes
			for (int j = 0; j < CRYPT_KEYS[i].length; j++) {
				CRYPT_KEYS[i][j] = (byte) Rnd.get(255);
			}
			
			// the last 8 bytes are static
			CRYPT_KEYS[i][8] = (byte) 0xc8;
			CRYPT_KEYS[i][9] = (byte) 0x27;
			CRYPT_KEYS[i][10] = (byte) 0x93;
			CRYPT_KEYS[i][11] = (byte) 0x01;
			CRYPT_KEYS[i][12] = (byte) 0xa1;
			CRYPT_KEYS[i][13] = (byte) 0x6c;
			CRYPT_KEYS[i][14] = (byte) 0x31;
			CRYPT_KEYS[i][15] = (byte) 0x97;
		}
	}
	
	// block instantiation
	private BlowFishKeygen() {
		
	}
	
	/**
	 * Returns a key from this keygen pool, the logical ownership is retained by this keygen.<BR>
	 * Thus when getting a key with interests other then read-only a copy must be performed.<BR>
	 * @return A key from this keygen pool.
	 */
	public static byte[] getRandomKey() {
		return CRYPT_KEYS[Rnd.get(CRYPT_KEYS_SIZE)];
	}
}
