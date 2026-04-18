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
package com.l2jserver.loginserver.metrics;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.loginserver.GameServerTable;
import com.l2jserver.loginserver.LoginController;

/**
 * JMX MBean с runtime-метриками login-server.
 * Видно в jconsole/VisualVM под доменом {@code com.l2jserver.loginserver:type=Metrics}.
 */
public final class LoginMetrics implements LoginMetricsMBean {

	private static final Logger LOG = LoggerFactory.getLogger(LoginMetrics.class);

	private static final LoginMetrics INSTANCE = new LoginMetrics();

	private final AtomicLong _loginsOk = new AtomicLong();
	private final AtomicLong _loginsFail = new AtomicLong();
	private final AtomicLong _accountLocks = new AtomicLong();
	private final AtomicLong _passwordMigrations = new AtomicLong();

	private LoginMetrics() {
	}

	public static LoginMetrics getInstance() {
		return INSTANCE;
	}

	public static void register() {
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName on = new ObjectName("com.l2jserver.loginserver:type=Metrics");
			if (!mbs.isRegistered(on)) {
				mbs.registerMBean(INSTANCE, on);
				LOG.info("Registered JMX MBean {}.", on);
			}
		} catch (Exception ex) {
			LOG.warn("Failed to register JMX MBean.", ex);
		}
	}

	public void incLoginOk() {
		_loginsOk.incrementAndGet();
	}

	public void incLoginFail() {
		_loginsFail.incrementAndGet();
	}

	public void incAccountLock() {
		_accountLocks.incrementAndGet();
	}

	public void incPasswordMigration() {
		_passwordMigrations.incrementAndGet();
	}

	// MBean accessors --------------------------------------------------------

	@Override
	public long getLoginsOk() {
		return _loginsOk.get();
	}

	@Override
	public long getLoginsFail() {
		return _loginsFail.get();
	}

	@Override
	public long getAccountLocks() {
		return _accountLocks.get();
	}

	@Override
	public long getPasswordMigrations() {
		return _passwordMigrations.get();
	}

	@Override
	public int getAuthedClients() {
		return LoginController.getInstance().getAuthedClientCount();
	}

	@Override
	public int getRegisteredGs() {
		return GameServerTable.getInstance().getRegisteredGameServers().size();
	}

	@Override
	public int getBannedIpsCount() {
		return LoginController.getInstance().getBannedIps().size();
	}

	@Override
	public int getBannedSubnetsCount() {
		return LoginController.getInstance().getBannedSubnets().size();
	}
}
