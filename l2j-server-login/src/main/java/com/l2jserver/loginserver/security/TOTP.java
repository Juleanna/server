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
package com.l2jserver.loginserver.security;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * RFC 6238 TOTP (HMAC-SHA1, 30s шаг, 6 цифр) — совместимо с Google Authenticator / Authy.
 * Секрет хранится в БД в Base32 (поле accounts.totp_secret).
 */
public final class TOTP {

	private static final int STEP_SECONDS = 30;
	private static final int DIGITS = 6;
	private static final String ALGO = "HmacSHA1";

	private TOTP() {
	}

	/** Генерирует новый 20-байтовый секрет и возвращает его в Base32. */
	public static String generateSecret() {
		final byte[] bytes = new byte[20];
		new SecureRandom().nextBytes(bytes);
		return base32Encode(bytes);
	}

	/** Проверяет код с допуском ±1 шаг (погрешность часов клиента). */
	public static boolean verify(String base32Secret, int code) {
		try {
			final byte[] key = base32Decode(base32Secret);
			final long step = System.currentTimeMillis() / 1000L / STEP_SECONDS;
			for (int offset = -1; offset <= 1; offset++) {
				if (generate(key, step + offset) == code) {
					return true;
				}
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	private static int generate(byte[] key, long counter) throws Exception {
		final ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(counter);
		final Mac mac = Mac.getInstance(ALGO);
		mac.init(new SecretKeySpec(key, ALGO));
		final byte[] hash = mac.doFinal(buf.array());
		final int offset = hash[hash.length - 1] & 0x0F;
		final int bin = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16)
			| ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
		int mod = 1;
		for (int i = 0; i < DIGITS; i++) {
			mod *= 10;
		}
		return bin % mod;
	}

	// --- Base32 (RFC 4648) без внешних зависимостей ---
	private static final char[] B32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();

	public static String base32Encode(byte[] data) {
		if (data == null || data.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int buf = 0;
		int bits = 0;
		for (byte b : data) {
			buf = (buf << 8) | (b & 0xFF);
			bits += 8;
			while (bits >= 5) {
				bits -= 5;
				sb.append(B32[(buf >> bits) & 0x1F]);
			}
		}
		if (bits > 0) {
			sb.append(B32[(buf << (5 - bits)) & 0x1F]);
		}
		return sb.toString();
	}

	public static byte[] base32Decode(String s) {
		s = s.replaceAll("\\s+", "").replaceAll("=", "").toUpperCase();
		byte[] out = new byte[s.length() * 5 / 8];
		int buf = 0;
		int bits = 0;
		int idx = 0;
		for (int i = 0; i < s.length(); i++) {
			int v = indexOf(B32, s.charAt(i));
			if (v < 0) {
				throw new IllegalArgumentException("Invalid base32 char: " + s.charAt(i));
			}
			buf = (buf << 5) | v;
			bits += 5;
			if (bits >= 8) {
				bits -= 8;
				out[idx++] = (byte) ((buf >> bits) & 0xFF);
			}
		}
		return out;
	}

	private static int indexOf(char[] arr, char c) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == c) {
				return i;
			}
		}
		return -1;
	}

	/** Constant-time сравнение двух string-секретов (для тестов). */
	public static boolean secretsEqual(String a, String b) {
		return a != null && b != null
			&& MessageDigest.isEqual(a.getBytes(), b.getBytes());
	}
}
