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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.network.BaseRecievePacket;
import com.l2jserver.loginserver.GameServerThread;
import com.l2jserver.loginserver.mail.MailSystem;

/**
 * @author mrTJO
 * @version 2.6.1.0
 */
public class RequestSendMail extends BaseRecievePacket {

	private static final Logger LOG = LoggerFactory.getLogger(RequestSendMail.class);

	/** Простейший rate-limit по аккаунту: не более N писем за окно. */
	private static final int MAX_MAILS_PER_WINDOW = 5;
	private static final long WINDOW_MS = 60L * 60_000L; // 1 час
	private static final Map<String, Window> WINDOWS = new ConcurrentHashMap<>();

	public RequestSendMail(byte[] decrypt, GameServerThread server) {
		super(decrypt);
		String accountName = readS();
		String mailId = readS();
		int argNum = readC();
		String[] args = new String[argNum];
		for (int i = 0; i < argNum; i++) {
			args[i] = readS();
		}

		if (server == null) {
			LOG.warn("RequestSendMail without GS context — ignored.");
			return;
		}

		// Trust boundary: GS может отправлять mail только для аккаунтов, залогиненных на нём.
		// Иначе rogue GS использует SMTP-реквизиты LS для массовой рассылки.
		if (accountName == null || !server.hasAccountOnGameServer(accountName)) {
			LOG.warn("GS {} tried to send mail to {} which is not on this server — denied.",
				server.getServerId(), accountName);
			return;
		}

		// Rate-limit per-account.
		if (!allow(accountName)) {
			LOG.warn("Mail rate-limit hit for account {} (GS {}).", accountName, server.getServerId());
			return;
		}

		MailSystem.getInstance().sendMail(accountName, mailId, args);
	}

	/** Старая сигнатура — чтобы старый handler не падал. */
	public RequestSendMail(byte[] decrypt) {
		this(decrypt, null);
	}

	private static boolean allow(String account) {
		final long now = System.currentTimeMillis();
		Window w = WINDOWS.computeIfAbsent(account, k -> new Window(now));
		synchronized (w) {
			if ((now - w.start) > WINDOW_MS) {
				w.start = now;
				w.count.set(0);
			}
			return w.count.incrementAndGet() <= MAX_MAILS_PER_WINDOW;
		}
	}

	private static final class Window {
		long start;
		final AtomicInteger count = new AtomicInteger();

		Window(long start) {
			this.start = start;
		}
	}
}
