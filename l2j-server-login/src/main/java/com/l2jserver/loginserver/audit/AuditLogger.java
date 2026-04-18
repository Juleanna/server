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
package com.l2jserver.loginserver.audit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;

/**
 * Audit-log важных событий login-server: логины (успех/отказ), смена пароля,
 * смена access level, баны, регистрация GS. Write асинхронный — не блокирует
 * игровой поток.
 */
public final class AuditLogger {

	private static final Logger LOG = LoggerFactory.getLogger(AuditLogger.class);

	private static final String INSERT = "INSERT INTO login_audit (ts, event, account, ip, actor, details) VALUES (?, ?, ?, ?, ?, ?)";

	private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) Executors.newSingleThreadExecutor(new NamedDaemon("LoginAudit"));

	private AuditLogger() {
	}

	public static void log(String event, String account, String ip, String actor, String details) {
		final long ts = System.currentTimeMillis();
		EXECUTOR.execute(() -> doLog(ts, event, account, ip, actor, details));
	}

	public static void loginSuccess(String account, String ip) {
		log("LOGIN_OK", account, ip, null, null);
	}

	public static void loginFailed(String account, String ip, String reason) {
		log("LOGIN_FAIL", account, ip, null, reason);
	}

	public static void accountLocked(String account, long lockoutMs) {
		log("ACCOUNT_LOCK", account, null, null, "lockoutMs=" + lockoutMs);
	}

	public static void passwordChanged(String account, String actor) {
		log("PASSWORD_CHANGE", account, null, actor, null);
	}

	public static void accessLevelChanged(String account, int level, String actor) {
		log("ACCESS_LEVEL_CHANGE", account, null, actor, "level=" + level);
	}

	public static void banned(String target, String actor, long durationMs) {
		log("BAN", null, target, actor, "durationMs=" + durationMs);
	}

	public static void gsRegistered(int id, String ip) {
		log("GS_REGISTER", null, ip, "gs=" + id, null);
	}

	private static void doLog(long ts, String event, String account, String ip, String actor, String details) {
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT)) {
			ps.setLong(1, ts);
			ps.setString(2, event);
			ps.setString(3, account);
			ps.setString(4, ip);
			ps.setString(5, actor);
			ps.setString(6, details);
			ps.executeUpdate();
		} catch (Exception ex) {
			// Falling back to plain logger: обычно таблица не создана при старте,
			// не ронять из-за этого основной поток.
			LOG.warn("Audit insert failed for {} ({}): {}", event, account, ex.getMessage());
		}
	}

	public static void shutdown() {
		EXECUTOR.shutdown();
		try {
			EXECUTOR.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException ignore) {
			Thread.currentThread().interrupt();
		}
	}

	private static final class NamedDaemon implements ThreadFactory {
		private final String _name;

		NamedDaemon(String name) {
			_name = name;
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, _name);
			t.setDaemon(true);
			return t;
		}
	}
}
