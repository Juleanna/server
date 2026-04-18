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
package com.l2jserver.loginserver.health;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.loginserver.LoginController;
import com.l2jserver.loginserver.GameServerTable;

/**
 * Встроенный HTTP-сервер для health-check и базовой runtime-статистики.
 * Использует com.sun.net.httpserver (JDK) — без внешних зависимостей.
 * Два эндпоинта:
 *   GET /health  — простой 200/500 плюс "ok" / "degraded".
 *   GET /metrics — JSON с ключевыми метриками для Prometheus/Consul/etc.
 */
public final class HealthCheckServer {

	private static final Logger LOG = LoggerFactory.getLogger(HealthCheckServer.class);

	private HttpServer _server;

	public void start(String bindIp, int port) throws IOException {
		final InetSocketAddress addr = "*".equals(bindIp)
			? new InetSocketAddress(port)
			: new InetSocketAddress(bindIp, port);
		_server = HttpServer.create(addr, 10);
		_server.createContext("/health", new HealthHandler());
		_server.createContext("/metrics", new MetricsHandler());
		_server.setExecutor(null); // default executor — дефолтный single-thread ок для health
		_server.start();
		LOG.info("Health-check HTTP started on {}:{}", bindIp, port);
	}

	public void stop() {
		if (_server != null) {
			_server.stop(0);
		}
	}

	private static final class HealthHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange ex) throws IOException {
			final byte[] body = "ok\n".getBytes(StandardCharsets.UTF_8);
			ex.sendResponseHeaders(200, body.length);
			ex.getResponseBody().write(body);
			ex.close();
		}
	}

	private static final class MetricsHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange ex) throws IOException {
			final LoginController lc = LoginController.getInstance();
			final int authed = lc.getAuthedClientCount();
			final int gs = GameServerTable.getInstance().getRegisteredGameServers().size();
			final int bannedIps = lc.getBannedIps().size();
			final int bannedSubnets = lc.getBannedSubnets().size();

			final String json = "{"
				+ "\"authedClients\":" + authed + ","
				+ "\"registeredGs\":" + gs + ","
				+ "\"bannedIps\":" + bannedIps + ","
				+ "\"bannedSubnets\":" + bannedSubnets
				+ "}";
			final byte[] body = json.getBytes(StandardCharsets.UTF_8);
			ex.getResponseHeaders().add("Content-Type", "application/json");
			ex.sendResponseHeaders(200, body.length);
			ex.getResponseBody().write(body);
			ex.close();
		}
	}
}
