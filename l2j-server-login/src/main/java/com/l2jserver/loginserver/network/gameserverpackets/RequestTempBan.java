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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.commons.network.BaseRecievePacket;
import com.l2jserver.loginserver.GameServerThread;
import com.l2jserver.loginserver.LoginController;

/**
 * Request Temp Ban packet.
 * @author mrTJO
 * @version 2.6.1.0
 */
public class RequestTempBan extends BaseRecievePacket {

	private static final Logger LOG = LoggerFactory.getLogger(RequestTempBan.class);

	/** Абсолютный верхний предел на длительность temp-ban'а от GS (30 дней). */
	private static final long MAX_BAN_DURATION_MS = 30L * 24 * 60 * 60 * 1000;

	private final GameServerThread _server;

	private final String _accountName;

	@SuppressWarnings("unused")
	private String _banReason;

	private final String _ip;

	private final long _banTime;

	public RequestTempBan(byte[] decrypt, GameServerThread server) {
		super(decrypt);
		_server = server;
		_accountName = readS();
		_ip = readS();
		_banTime = readQ();
		boolean haveReason = readC() != 0;
		if (haveReason) {
			_banReason = readS();
		}
		banUser();
	}

	private void banUser() {
		// Trust boundary: GS может банить только аккаунты, залогиненные на нём.
		if (_accountName == null || !_server.hasAccountOnGameServer(_accountName)) {
			LOG.warn("GS {} tried to temp-ban {} which is not on this server — denied.",
				_server.getServerId(), _accountName);
			return;
		}
		// Валидация длительности: иначе rogue GS может выставить Long.MAX_VALUE.
		if (_banTime <= 0 || _banTime > MAX_BAN_DURATION_MS) {
			LOG.warn("GS {} tried to temp-ban {} for invalid duration {} — denied.",
				_server.getServerId(), _accountName, _banTime);
			return;
		}

		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("INSERT INTO account_data VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value=?")) {
			ps.setString(1, _accountName);
			ps.setString(2, "ban_temp");
			ps.setString(3, Long.toString(_banTime));
			ps.setString(4, Long.toString(_banTime));
			ps.execute();
		} catch (Exception ex) {
			LOG.warn("There has been an error inserting ban for account {}!", _accountName, ex);
		}

		// IP-бан применяется только если адрес соответствует lastIP аккаунта —
		// rogue GS иначе мог бы забанить IP админа или всю подсеть провайдера.
		// Для надёжной проверки оставляем только аккаунт-бан через БД; IP-бан
		// админ пусть делает отдельной процедурой (Status telnet / runtime command).
		if (_ip != null && !_ip.isBlank()) {
			LOG.info("GS {} requested IP ban for {} (account {}); IP part is ignored (use admin tools).",
				_server.getServerId(), _ip, _accountName);
		}
		// Оставлено для совместимости: если очень нужно, добавьте сюда проверку
		// принадлежности _ip к lastIP аккаунта и вызов LoginController.addBanForAddress.
		try {
			// без вызова — см. комментарий выше
		} catch (Exception e) {
			LOG.warn("Error while applying IP ban for {}.", _ip, e);
		}
	}

	/** Старая сигнатура без GS — оставлена, чтобы handler старого GS не падал NPE, но отклоняет запрос. */
	public RequestTempBan(byte[] decrypt) {
		super(decrypt);
		_server = null;
		_accountName = null;
		_ip = null;
		_banTime = 0;
		LOG.warn("RequestTempBan received without GS context — ignored.");
	}
}
