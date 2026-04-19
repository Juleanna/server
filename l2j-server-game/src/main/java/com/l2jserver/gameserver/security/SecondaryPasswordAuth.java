/*
 * Copyright © 2004-2026 L2J Server
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
package com.l2jserver.gameserver.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.LoginServerThread;
import com.l2jserver.gameserver.data.xml.impl.SecondaryAuthData;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.serverpackets.Ex2ndPasswordAck;
import com.l2jserver.gameserver.network.serverpackets.Ex2ndPasswordCheck;
import com.l2jserver.gameserver.network.serverpackets.Ex2ndPasswordVerify;
import com.l2jserver.gameserver.util.Util;

/**
 * Secondary Password Auth.
 * <p>
 * Пароли хранятся как {@code $pbkdf2-sha256$<iter>$<base64 salt>$<base64 hash>}.
 * Старый формат {@code Base64(SHA-1(pwd))} без соли продолжает проверяться и
 * автоматически мигрируется в PBKDF2 при первой успешной верификации.
 * @author mrTJO
 */
public class SecondaryPasswordAuth {

	private static final Logger LOG = LoggerFactory.getLogger(SecondaryPasswordAuth.class);

	// --- KDF ---
	private static final String PBKDF2_PREFIX = "$pbkdf2-sha256$";
	private static final int PBKDF2_ITERATIONS = 120_000; // второй пароль — 6-8 цифр, нет смысла в 600k
	private static final int PBKDF2_SALT_BYTES = 16;
	private static final int PBKDF2_HASH_BITS = 256;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private final L2GameClient _activeClient;

	private String _password;
	private int _wrongAttempts;
	private boolean _authed;

	private static final String VAR_PWD = "secauth_pwd";
	private static final String VAR_WTE = "secauth_wte";

	private static final String SELECT_PASSWORD = "SELECT var, value FROM account_gsdata WHERE account_name=? AND var LIKE 'secauth_%'";
	private static final String INSERT_PASSWORD = "INSERT INTO account_gsdata VALUES (?, ?, ?)";
	private static final String UPDATE_PASSWORD = "UPDATE account_gsdata SET value=? WHERE account_name=? AND var=?";

	private static final String INSERT_ATTEMPT = "INSERT INTO account_gsdata VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value=?";

	public SecondaryPasswordAuth(L2GameClient activeClient) {
		_activeClient = activeClient;
		_password = null;
		_wrongAttempts = 0;
		_authed = false;
		loadPassword();
	}

	private void loadPassword() {
		String var, value;
		try (var con = ConnectionFactory.getInstance().getConnection();
			var statement = con.prepareStatement(SELECT_PASSWORD)) {
			statement.setString(1, _activeClient.getAccountName());
			try (var rs = statement.executeQuery()) {
				while (rs.next()) {
					var = rs.getString("var");
					value = rs.getString("value");

					if (var.equals(VAR_PWD)) {
						_password = value;
					} else if (var.equals(VAR_WTE)) {
						_wrongAttempts = Integer.parseInt(value);
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("Error while reading password.", ex);
		}
	}

	public boolean savePassword(String password) {
		if (passwordExist()) {
			LOG.warn(_activeClient.getAccountName() + " forced save password!");
			_activeClient.closeNow();
			return false;
		}

		if (!validatePassword(password)) {
			_activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
			return false;
		}

		final String stored = hashPbkdf2(password);

		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_PASSWORD)) {
			ps.setString(1, _activeClient.getAccountName());
			ps.setString(2, VAR_PWD);
			ps.setString(3, stored);
			ps.execute();
		} catch (Exception ex) {
			LOG.error("Error while writing password!", ex);
			return false;
		}
		_password = stored;
		return true;
	}

	public boolean insertWrongAttempt(int attempts) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_ATTEMPT)) {
			ps.setString(1, _activeClient.getAccountName());
			ps.setString(2, VAR_WTE);
			ps.setString(3, Integer.toString(attempts));
			ps.setString(4, Integer.toString(attempts));
			ps.execute();
		} catch (Exception ex) {
			LOG.error("Error while writing wrong attempts!", ex);
			return false;
		}
		return true;
	}

	public boolean changePassword(String oldPassword, String newPassword) {
		if (!passwordExist()) {
			LOG.warn(_activeClient.getAccountName() + " forced change password");
			_activeClient.closeNow();
			return false;
		}

		if (!checkPassword(oldPassword, true)) {
			return false;
		}

		if (!validatePassword(newPassword)) {
			_activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
			return false;
		}

		final String stored = hashPbkdf2(newPassword);

		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(UPDATE_PASSWORD)) {
			ps.setString(1, stored);
			ps.setString(2, _activeClient.getAccountName());
			ps.setString(3, VAR_PWD);
			ps.execute();
		} catch (Exception ex) {
			LOG.error("Error while writing password!", ex);
			return false;
		}

		_password = stored;
		_authed = false;
		return true;
	}

	public boolean checkPassword(String password, boolean skipAuth) {
		if (!verifyStored(password, _password)) {
			_wrongAttempts++;
			if (_wrongAttempts < SecondaryAuthData.getInstance().getMaxAttempts()) {
				_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_WRONG, _wrongAttempts));
				insertWrongAttempt(_wrongAttempts);
			} else {
				final var accountName = _activeClient.getAccountName();
				final var hostAddress = _activeClient.getConnectionAddress().getHostAddress();
				final var banTime = SecondaryAuthData.getInstance().getBanTime();
				LoginServerThread.getInstance().sendTempBan(accountName, hostAddress, banTime);
				final var recoveryLink = SecondaryAuthData.getInstance().getRecoveryLink();
				LoginServerThread.getInstance().sendMail(accountName, "SATempBan", hostAddress, Integer.toString(_wrongAttempts), Long.toString(banTime), recoveryLink);
				LOG.warn(_activeClient.getAccountName() + " - ({}) has inputted the wrong password {} times in row.", hostAddress, _wrongAttempts);
				insertWrongAttempt(0);
				_activeClient.close(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_BAN, SecondaryAuthData.getInstance().getMaxAttempts()));
			}
			return false;
		}

		// Lazy-миграция: если в БД всё ещё старый SHA-1 формат — пересохраняем в PBKDF2.
		if (_password != null && !_password.startsWith(PBKDF2_PREFIX)) {
			migrateToPbkdf2(password);
		}

		if (!skipAuth) {
			_authed = true;
			_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_OK, _wrongAttempts));
		}
		insertWrongAttempt(0);
		return true;
	}

	public boolean passwordExist() {
		return _password != null;
	}

	public void openDialog() {
		if (passwordExist()) {
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_PROMPT));
		} else {
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_NEW));
		}
	}

	public boolean isAuthed() {
		return _authed;
	}

	// ---- Crypto ----

	/** Сравнивает plaintext с сохранённым значением: понимает и PBKDF2, и legacy SHA-1. */
	private static boolean verifyStored(String plaintext, String stored) {
		if (plaintext == null || stored == null) {
			return false;
		}
		if (stored.startsWith(PBKDF2_PREFIX)) {
			return verifyPbkdf2(plaintext, stored);
		}
		// Legacy: Base64(SHA-1(pwd)) без соли, сравниваем constant-time.
		final String legacy = sha1Base64(plaintext);
		if (legacy == null) {
			return false;
		}
		return MessageDigest.isEqual(legacy.getBytes(StandardCharsets.UTF_8), stored.getBytes(StandardCharsets.UTF_8));
	}

	private static String hashPbkdf2(String plaintext) {
		try {
			final byte[] salt = new byte[PBKDF2_SALT_BYTES];
			SECURE_RANDOM.nextBytes(salt);
			final byte[] hash = pbkdf2(plaintext.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_HASH_BITS);
			final Base64.Encoder enc = Base64.getEncoder().withoutPadding();
			return PBKDF2_PREFIX + PBKDF2_ITERATIONS + "$" + enc.encodeToString(salt) + "$" + enc.encodeToString(hash);
		} catch (Exception ex) {
			throw new IllegalStateException("PBKDF2 is required but unavailable", ex);
		}
	}

	private static boolean verifyPbkdf2(String plaintext, String stored) {
		try {
			final String tail = stored.substring(PBKDF2_PREFIX.length());
			final String[] parts = tail.split("\\$");
			if (parts.length != 3) {
				return false;
			}
			final int iter = Integer.parseInt(parts[0]);
			final byte[] salt = Base64.getDecoder().decode(parts[1]);
			final byte[] expected = Base64.getDecoder().decode(parts[2]);
			final byte[] actual = pbkdf2(plaintext.toCharArray(), salt, iter, expected.length * 8);
			return MessageDigest.isEqual(expected, actual);
		} catch (Exception ex) {
			LOG.warn("PBKDF2 verify failed.", ex);
			return false;
		}
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bits) throws Exception {
		final KeySpec spec = new PBEKeySpec(password, salt, iterations, bits);
		return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
	}

	/** Перегенерирует сохранённый хэш в новый формат. Ошибки не роняют основной flow. */
	private void migrateToPbkdf2(String plaintext) {
		try {
			final String upgraded = hashPbkdf2(plaintext);
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(UPDATE_PASSWORD)) {
				ps.setString(1, upgraded);
				ps.setString(2, _activeClient.getAccountName());
				ps.setString(3, VAR_PWD);
				ps.execute();
			}
			_password = upgraded;
		} catch (Exception ex) {
			LOG.warn("Failed to migrate secondary password for {} to PBKDF2; will retry next login.",
				_activeClient.getAccountName(), ex);
		}
	}

	private static String sha1Base64(String password) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA");
			return Base64.getEncoder().encodeToString(md.digest(password.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException ex) {
			LOG.error("Unsupported Algorithm!", ex);
			return null;
		}
	}

	private boolean validatePassword(String password) {
		if (!Util.isDigit(password)) {
			return false;
		}

		if ((password.length() < 6) || (password.length() > 8)) {
			return false;
		}
		return !SecondaryAuthData.getInstance().isForbiddenPassword(password);
	}
}
