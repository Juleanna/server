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

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * Game Server listener.
 * @author KenM
 * @version 2.6.1.0
 */
public class GameServerListener extends FloodProtectedListener {
	private static final List<GameServerThread> _gameServers = new CopyOnWriteArrayList<>();

	public GameServerListener() throws Exception {
		super(server().getGameServerHost(), server().getGameServerPort(), buildProvider());
		setName(getClass().getSimpleName());
	}

	/**
	 * Если GameServerTlsEnabled=true — поднимаем SSLServerSocket.
	 * Требует PKCS12-keystore с приватным ключом и сертификатом LS.
	 * Game server должен соединяться TLS-клиентом и доверять этому сертификату.
	 */
	private static ServerSocketProvider buildProvider() {
		if (!server().isGameServerTlsEnabled()) {
			return null;
		}
		return (listenIp, port) -> {
			final char[] pw = server().getGameServerTlsKeystorePassword().toCharArray();
			final KeyStore ks = KeyStore.getInstance("PKCS12");
			try (FileInputStream fis = new FileInputStream(server().getGameServerTlsKeystore())) {
				ks.load(fis, pw);
			}
			final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, pw);
			final SSLContext ctx = SSLContext.getInstance("TLSv1.3");
			ctx.init(kmf.getKeyManagers(), null, null);
			final SSLServerSocketFactory sf = ctx.getServerSocketFactory();
			final ServerSocket s;
			if ("*".equals(listenIp)) {
				s = sf.createServerSocket(port);
			} else {
				s = sf.createServerSocket(port, 50, InetAddress.getByName(listenIp));
			}
			((SSLServerSocket) s).setEnabledProtocols(new String[] { "TLSv1.3", "TLSv1.2" });
			return s;
		};
	}
	
	@Override
	public void addClient(Socket s) {
		_gameServers.add(new GameServerThread(s));
	}
	
	public void removeGameServer(GameServerThread gst) {
		_gameServers.remove(gst);
	}
}
