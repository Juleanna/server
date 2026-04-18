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
package com.l2jserver.loginserver.network.gameserverpackets;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.loginserver.GameServerThread;
import com.l2jserver.loginserver.LoginController;

/**
 * Change Password packet.
 * @author Nik
 * @version 2.6.1.0
 */
public class ChangePassword {

	private static final Logger LOG = LoggerFactory.getLogger(ChangePassword.class);

	private static final String PBKDF2_PREFIX = "$pbkdf2-sha256$";

	// Без super(decrypt) — читаем руками, чтобы не плодить статические поля.
	public ChangePassword(byte[] decrypt, GameServerThread server) {
		final Reader r = new Reader(decrypt);
		final String accountName = r.readS();
		final String characterName = r.readS();
		final String currentPassword = r.readS();
		final String newPassword = r.readS();

		if (server == null) {
			LOG.warn("ChangePassword received without GS context — ignored.");
			return;
		}

		// Trust boundary: менять пароль может только GS, где залогинен этот аккаунт.
		// Иначе rogue GS мог бы менять пароли всем игрокам сервера.
		if (accountName == null || !server.hasAccountOnGameServer(accountName)) {
			LOG.warn("GS {} tried to change password for {} which is not on this server — denied.",
				server.getServerId(), accountName);
			return;
		}

		if ((currentPassword == null) || (newPassword == null)) {
			server.ChangePasswordResponse((byte) 0, characterName, "Invalid password data! Try again.");
			return;
		}
		if (newPassword.length() < 6) {
			server.ChangePasswordResponse((byte) 0, characterName, "New password is too short (min 6 chars).");
			return;
		}

		try {
			String stored = null;
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement("SELECT password FROM accounts WHERE login=?")) {
				ps.setString(1, accountName);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						stored = rs.getString("password");
					}
				}
			}

			if (stored == null) {
				server.ChangePasswordResponse((byte) 0, characterName, "Account not found.");
				return;
			}

			if (!verify(currentPassword, stored)) {
				server.ChangePasswordResponse((byte) 0, characterName,
					"The typed current password doesn't match with your current one.");
				return;
			}

			// Новый пароль всегда сохраняем в PBKDF2 (через LoginController.hashPassword).
			final String newStored = LoginController.hashPassword(newPassword);
			int updated;
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?")) {
				ps.setString(1, newStored);
				ps.setString(2, accountName);
				updated = ps.executeUpdate();
			}

			// Логируем факт смены, но БЕЗ хэшей: прежняя версия писала в info старый/новый
			// хэш, из логов восстанавливался пароль через rainbow-table.
			LOG.info("Password changed for account {} (by GS {}).", accountName, server.getServerId());

			if (updated > 0) {
				server.ChangePasswordResponse((byte) 1, characterName, "You have successfully changed your password!");
			} else {
				server.ChangePasswordResponse((byte) 0, characterName, "The password change was unsuccessful!");
			}
		} catch (Exception ex) {
			LOG.warn("Error while changing password for account {} requested by player {}!", accountName, characterName, ex);
		}
	}

	/** Старая сигнатура — оставлена, чтобы не падать при случайном вызове без GS. */
	public ChangePassword(byte[] decrypt) {
		this(decrypt, null);
	}

	private static boolean verify(String plaintext, String stored) {
		if (stored.startsWith(PBKDF2_PREFIX)) {
			return LoginController.verifyPbkdf2Public(plaintext, stored);
		}
		try {
			final var md = MessageDigest.getInstance("SHA");
			final var legacy = Base64.getEncoder().encodeToString(md.digest(plaintext.getBytes(UTF_8)));
			return MessageDigest.isEqual(legacy.getBytes(UTF_8), stored.getBytes(UTF_8));
		} catch (Exception ex) {
			LOG.warn("Password verify failed.", ex);
			return false;
		}
	}

	/** Мини-ридер, чтобы не тащить весь BaseRecievePacket только ради S-строк. */
	private static final class Reader {
		private final byte[] _buf;
		private int _pos = 1; // opcode уже съеден хэндлером

		Reader(byte[] buf) {
			_buf = buf;
		}

		String readS() {
			final StringBuilder sb = new StringBuilder();
			int hi;
			int lo;
			while ((_pos + 1) < _buf.length) {
				lo = _buf[_pos] & 0xff;
				hi = _buf[_pos + 1] & 0xff;
				_pos += 2;
				final char c = (char) ((hi << 8) | lo);
				if (c == 0) {
					return sb.toString();
				}
				sb.append(c);
			}
			return sb.toString();
		}
	}
}
