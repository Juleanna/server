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

import static com.l2jserver.loginserver.config.Configuration.database;
import static com.l2jserver.loginserver.config.Configuration.email;
import static com.l2jserver.loginserver.config.Configuration.mmo;
import static com.l2jserver.loginserver.config.Configuration.server;
import static com.l2jserver.loginserver.config.Configuration.telnet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.UPnPService;
import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.loginserver.health.HealthCheckServer;
import com.l2jserver.loginserver.mail.MailSystem;
import com.l2jserver.loginserver.metrics.LoginMetrics;
import com.l2jserver.loginserver.network.L2LoginClient;
import com.l2jserver.loginserver.network.L2LoginPacketHandler;
import com.l2jserver.loginserver.status.Status;
import com.l2jserver.mmocore.SelectorConfig;
import com.l2jserver.mmocore.SelectorThread;

/**
 * Login Server.
 * @author KenM
 * @author Zoey76
 * @version 2.6.1.0
 */
public final class LoginServer {

	private static final Logger LOG = LoggerFactory.getLogger(LoginServer.class);

	private static final String BANNED_IPS = "config/banned_ip.cfg";

	private static LoginServer _instance;

	private GameServerListener _gameServerListener;

	private SelectorThread<L2LoginClient> _selectorThread;

	private Status _statusServer;

	private SelectorHelper _selectorHelper;

	private HealthCheckServer _healthCheckServer;

	private volatile boolean _shuttingDown = false;
	
	public static void main(String[] args) {
		new LoginServer();
	}
	
	public static LoginServer getInstance() {
		return _instance;
	}
	
	private LoginServer() {
		_instance = this;
		
		// Prepare Database
		ConnectionFactory.builder() //
			.withUrl(database().getURL()) //
			.withUser(database().getUser()) //
			.withPassword(database().getPassword()) //
			.withMaxIdleTime(database().getMaxIdleTime()) //
			.withMaxPoolSize(database().getMaxConnections()) //
			.build();
		
		LoginController.getInstance();
		
		GameServerTable.getInstance();
		
		loadBanFile();
		
		if (email().isEnabled()) {
			MailSystem.getInstance();
		}
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = mmo().getMaxReadPerPass();
		sc.MAX_SEND_PER_PASS = mmo().getMaxSendPerPass();
		sc.SLEEP_TIME = mmo().getSleepTime();
		sc.HELPER_BUFFER_COUNT = mmo().getHelperBufferCount();
		
		final L2LoginPacketHandler loginPacketHandler = new L2LoginPacketHandler();
		_selectorHelper = new SelectorHelper();
		try {
			_selectorThread = new SelectorThread<>(sc, _selectorHelper, loginPacketHandler, _selectorHelper, _selectorHelper);
		} catch (Exception ex) {
			LOG.error("Failed to open Selector!", ex);
			System.exit(1);
		}
		
		try {
			_gameServerListener = new GameServerListener();
			_gameServerListener.start();
			LOG.info("Listening for game servers on {}:{}.", server().getGameServerHost(), server().getGameServerPort());
		} catch (Exception ex) {
			LOG.error("Failed to start the Game Server Listener!", ex);
			System.exit(1);
		}
		
		if (telnet().isEnabled()) {
			try {
				_statusServer = new Status();
				_statusServer.start();
			} catch (Exception ex) {
				LOG.warn("Failed to start the Telnet Server!", ex);
			}
		} else {
			LOG.info("Telnet server is currently disabled.");
		}
		
		InetAddress bindAddress = null;
		if (!server().getHost().equals("*")) {
			try {
				bindAddress = InetAddress.getByName(server().getGameServerHost());
			} catch (Exception ex) {
				LOG.warn("The Login Server bind address is invalid, using all available IPs!", ex);
			}
		}
		try {
			_selectorThread.openServerSocket(bindAddress, server().getPort());
			_selectorThread.start();
			LOG.info("Login Server is now listening on {}:{}.", server().getHost(), server().getPort());
		} catch (Exception ex) {
			LOG.error("Failed to open server socket!", ex);
			System.exit(1);
		}
		
		if (server().isUPnPEnabled()) {
			UPnPService.getInstance().load(server().getPort(), "L2J Login Server");
		}

		// JMX-метрики.
		LoginMetrics.register();

		// HTTP health-check (опционально).
		if (server().isHealthCheckEnabled()) {
			try {
				_healthCheckServer = new HealthCheckServer();
				_healthCheckServer.start(server().getHealthCheckHost(), server().getHealthCheckPort());
			} catch (Exception ex) {
				LOG.warn("Failed to start health-check HTTP server.", ex);
			}
		}

		// Graceful shutdown при SIGTERM/Ctrl+C: иначе selector / telnet / purge
		// потоки могут оставлять висячие соединения и коннекты к БД.
		Runtime.getRuntime().addShutdownHook(new Thread(this::gracefulShutdown, "LS-Shutdown-Hook"));
	}
	
	public Status getStatusServer() {
		return _statusServer;
	}
	
	public GameServerListener getGameServerListener() {
		return _gameServerListener;
	}
	
	private void loadBanFile() {
		// Загружаем из файловой системы (путь относительно рабочей директории
		// сервера). Прежняя реализация через getResourceAsStream пыталась взять
		// файл из classpath/jar — там его нет, так что ban-list молча терялся.
		final File banFile = new File(BANNED_IPS);
		if (!banFile.exists() || !banFile.isFile()) {
			LOG.info("Ban file {} not found; skipping.", banFile.getAbsolutePath());
			return;
		}

		try (var fis = new FileInputStream(banFile);
			var is = new InputStreamReader(fis);
			var lnr = new LineNumberReader(is)) {
			lnr.lines() //
				.map(String::trim) //
				.filter(l -> !l.isEmpty() && (l.charAt(0) != '#')) //
				.forEach(line -> {
					String[] parts = line.split("#", 2); // address[ duration][ # comments]
					line = parts[0];
					parts = line.split("\\s+"); // durations might be aligned via multiple spaces
					String address = parts[0];
					long duration = 0;

					if (parts.length > 1) {
						try {
							duration = Long.parseLong(parts[1]);
						} catch (Exception ex) {
							LOG.warn("Incorrect ban duration {} on line {} on file {}!", parts[1], lnr.getLineNumber(), BANNED_IPS, ex);
							return;
						}
					}

					try {
						// CIDR-нотация (a.b.c.0/24) попадает в ban-subnets, остальное — точный адрес.
						if (address.contains("/")) {
							LoginController.getInstance().addBannedSubnet(address);
						} else {
							LoginController.getInstance().addBanForAddress(address, duration);
						}
					} catch (Exception ex) {
						LOG.warn("Invalid address {} on line {} on file {}!", address, lnr.getLineNumber(), BANNED_IPS, ex);
					}
				});
		} catch (Exception ex) {
			LOG.warn("Error while reading the bans file {}!", BANNED_IPS, ex);
		}
		LOG.info("Loaded {} banned IPs and {} banned subnets.",
			LoginController.getInstance().getBannedIps().size(),
			LoginController.getInstance().getBannedSubnets().size());
		
		if (server().isLoginRestartEnabled()) {
			final var restartLoginServer = new LoginServerRestart();
			restartLoginServer.setDaemon(true);
			restartLoginServer.start();
			LOG.info("Scheduled restart after {} hours.", server().getLoginRestartTime());
		}
	}
	
	class LoginServerRestart extends Thread {
		public LoginServerRestart() {
			setName("LoginServerRestart");
		}
		
		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					Thread.sleep(server().getLoginRestartTime() * 3600000);
				} catch (InterruptedException e) {
					return;
				}
				shutdown(true);
			}
		}
	}
	
	public void shutdown(boolean restart) {
		gracefulShutdown();
		Runtime.getRuntime().exit(restart ? 2 : 0);
	}

	private synchronized void gracefulShutdown() {
		if (_shuttingDown) {
			return;
		}
		_shuttingDown = true;
		LOG.info("Graceful shutdown starting...");

		// Останавливаем приём новых клиентов / GS.
		try {
			if (_selectorThread != null) {
				_selectorThread.shutdown();
			}
		} catch (Exception ex) {
			LOG.warn("Failed to shutdown selector.", ex);
		}
		try {
			if (_gameServerListener != null) {
				_gameServerListener.interrupt();
				_gameServerListener.close();
			}
		} catch (Exception ex) {
			LOG.warn("Failed to close GS listener.", ex);
		}
		try {
			if (_statusServer != null) {
				_statusServer.interrupt();
			}
		} catch (Exception ex) {
			LOG.warn("Failed to stop telnet.", ex);
		}
		try {
			if (_healthCheckServer != null) {
				_healthCheckServer.stop();
			}
		} catch (Exception ex) {
			LOG.warn("Failed to stop health-check HTTP.", ex);
		}
		com.l2jserver.loginserver.audit.AuditLogger.shutdown();
		// Drain пула пакетов.
		try {
			if (_selectorHelper != null) {
				_selectorHelper.getPacketsThreadPool().shutdown();
				_selectorHelper.getPacketsThreadPool().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
			}
		} catch (Exception ignore) {
		}
		// Закрываем пул БД.
		try {
			ConnectionFactory.getInstance().close();
		} catch (Exception ex) {
			LOG.warn("Failed to close DB pool.", ex);
		}
		LOG.info("Graceful shutdown completed.");
	}

	public boolean isShuttingDown() {
		return _shuttingDown;
	}
}
