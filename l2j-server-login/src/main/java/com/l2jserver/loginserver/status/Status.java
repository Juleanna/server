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
package com.l2jserver.loginserver.status;

import static com.l2jserver.loginserver.config.Configuration.telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.util.Util;

public class Status extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(Status.class);

	/** Таймаут на чтение пароля telnet-клиентом: один тихий клиент иначе DoS'ит весь telnet. */
	private static final int TELNET_AUTH_TIMEOUT_MS = 30_000;

	private final ServerSocket statusServerSocket;

	private final int _uptime;

	private String _statusPw;

	// CopyOnWriteArrayList: пишется из accept-потока, читается из sendMessageToTelnets.
	// Раньше был unsynchronized LinkedList → гонка.
	private final List<LoginStatusThread> _loginStatus;

	public Status() throws IOException {
		super("Status");
		_statusPw = telnet().getPassword();

		if (_statusPw == null) {
			_statusPw = Util.randomPassword(10);
			LOG.info("Server's Telnet function has no password defined!");
			LOG.info("A password has been automatically created!");
			// Пароль НЕ пишем в лог — логи часто смотрят третьи лица / админы других ролей.
			// Пользователь сгенерированный пароль может взять из конфига (telnet.password).
			LOG.warn("A random telnet password was generated. Set telnet.password in config to a known value; the generated one is not persisted.");
		}

		statusServerSocket = new ServerSocket(telnet().getPort());
		_uptime = (int) System.currentTimeMillis();
		_loginStatus = new CopyOnWriteArrayList<>();
		LOG.info("Telnet server started successfully, listening on port {}.", telnet().getPort());
	}

	@Override
	public void run() {
		setPriority(Thread.MAX_PRIORITY);

		while (!isInterrupted()) {
			try {
				Socket connection = statusServerSocket.accept();
				try {
					// Ограничиваем время на отправку пароля клиентом: без этого
					// конструктор LoginStatusThread делает блокирующий readLine()
					// в accept-потоке — тихий клиент замораживает listener.
					connection.setSoTimeout(TELNET_AUTH_TIMEOUT_MS);
				} catch (Exception ignore) {
				}

				try {
					LoginStatusThread lst = new LoginStatusThread(connection, _uptime, _statusPw);
					if (lst.isAlive()) {
						_loginStatus.add(lst);
					}
				} catch (Exception ex) {
					LOG.warn("Failed to accept telnet client from {}.", connection.getInetAddress(), ex);
					try {
						connection.close();
					} catch (Exception ignore) {
					}
				}

				if (isInterrupted()) {
					try {
						statusServerSocket.close();
					} catch (Exception ex) {
						LOG.warn("There has been an error closing status server socket!", ex);
					}
					break;
				}
			} catch (Exception ex1) {
				if (isInterrupted()) {
					try {
						statusServerSocket.close();
					} catch (Exception ex2) {
						LOG.warn("There has been an error closing status server socket!", ex2);
					}
					break;
				}
				LOG.warn("Telnet accept loop error.", ex1);
			}
		}
	}

	public void sendMessageToTelnets(String msg) {
		for (LoginStatusThread ls : _loginStatus) {
			if (!ls.isInterrupted()) {
				ls.printToTelnet(msg);
			}
		}
	}
}
