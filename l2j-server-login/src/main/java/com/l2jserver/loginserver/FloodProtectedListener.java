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

import static com.l2jserver.loginserver.config.Configuration.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flood Protected listener.
 * @author -Wooden-
 * @version 2.6.1.0
 */
public abstract class FloodProtectedListener extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(FloodProtectedListener.class);

	/**
	 * Фабрика серверного сокета — позволяет подклассу создать TLS-сокет
	 * (SSLServerSocketFactory) вместо plain TCP.
	 */
	public interface ServerSocketProvider {
		ServerSocket create(String listenIp, int port) throws Exception;
	}

	/** TTL записи в карте. Записи с ctime старше TTL и нулевым счётчиком удаляются. */
	private static final long ENTRY_TTL_MS = 10L * 60_000L;
	/** Как часто (accept-tick) запускать eviction, чтобы не считать currentTimeMillis на каждую запись. */
	private static final int EVICT_EVERY_N_ACCEPTS = 256;
	/** Пауза после ошибки accept, чтобы не крутить busy-loop при «Too many open files». */
	private static final long ACCEPT_ERROR_BACKOFF_MS = 100L;

	private final Map<String, ForeignConnection> _floodProtection = new ConcurrentHashMap<>();

	private final ServerSocket _serverSocket;

	private int _acceptCounter;

	public FloodProtectedListener(String listenIp, int port) throws Exception {
		this(listenIp, port, null);
	}

	public FloodProtectedListener(String listenIp, int port, ServerSocketProvider provider) throws Exception {
		if (provider != null) {
			_serverSocket = provider.create(listenIp, port);
		} else if (listenIp.equals("*")) {
			_serverSocket = new ServerSocket(port);
		} else {
			_serverSocket = new ServerSocket(port, 50, InetAddress.getByName(listenIp));
		}
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			Socket connection = null;
			try {
				connection = _serverSocket.accept();

				if (server().isFloodProtectionEnabled()) {
					final String ip = connection.getInetAddress().getHostAddress();
					final long now = System.currentTimeMillis();

					ForeignConnection fConnection = _floodProtection.get(ip);
					if (fConnection != null) {
						fConnection.connectionNumber += 1;
						if (((fConnection.connectionNumber > server().getFastConnectionLimit()) && //
							((now - fConnection.lastConnection) < server().getNormalConnectionTime())) || //
							((now - fConnection.lastConnection) < server().getFastConnectionTime()) || //
							(fConnection.connectionNumber > server().getMaxConnectionPerIP())) {
							fConnection.lastConnection = now;
							fConnection.connectionNumber -= 1;
							if (!fConnection.isFlooding) {
								LOG.warn("Potential Flood from {}!", ip);
							}
							fConnection.isFlooding = true;
							connection.close();
							continue;
						}
						if (fConnection.isFlooding) {
							fConnection.isFlooding = false;
							LOG.info("Connection {} is not considered as flooding anymore.", ip);
						}
						fConnection.lastConnection = now;
					} else {
						_floodProtection.put(ip, new ForeignConnection(now));
					}

					// Периодический eviction: иначе _floodProtection растёт навсегда.
					// Удаляем записи старше TTL и с нулём активных соединений.
					if ((++_acceptCounter & (EVICT_EVERY_N_ACCEPTS - 1)) == 0) {
						evictStale(now);
					}
				}

				addClient(connection);
			} catch (Exception e) {
				if (isInterrupted()) {
					close();
					break;
				}
				// Закрываем полу-принятый сокет (если accept прошёл, а затем упал addClient).
				if (connection != null) {
					try {
						connection.close();
					} catch (Exception ignore) {
					}
				}
				// Логируем — раньше исключение глоталось, маскируя "Too many open files" и т.п.
				LOG.warn("Error accepting connection.", e);
				try {
					Thread.sleep(ACCEPT_ERROR_BACKOFF_MS);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
	}

	private void evictStale(long now) {
		_floodProtection.entrySet().removeIf(e ->
			(e.getValue().connectionNumber <= 0)
				&& ((now - e.getValue().lastConnection) > ENTRY_TTL_MS));
	}

	protected static class ForeignConnection {
		public int connectionNumber;
		public long lastConnection;
		public boolean isFlooding = false;

		public ForeignConnection(long time) {
			lastConnection = time;
			connectionNumber = 1;
		}
	}

	public abstract void addClient(Socket s);

	public void removeFloodProtection(String ip) {
		if (!server().isFloodProtectionEnabled()) {
			return;
		}
		ForeignConnection fConnection = _floodProtection.get(ip);
		if (fConnection != null) {
			fConnection.connectionNumber -= 1;
			if (fConnection.connectionNumber <= 0) {
				_floodProtection.remove(ip);
			}
		}
		// Раньше было LOG.warn при отсутствии записи — это нормальная ситуация
		// для клиентских подключений (их lifecycle пока не завёл removeFloodProtection),
		// шумит лог без пользы.
	}

	public void close() {
		try {
			_serverSocket.close();
		} catch (Exception ex) {
			LOG.warn("There has been an error closing the connection!", ex);
		}
	}
}