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

import static com.l2jserver.loginserver.config.Configuration.server;
import static com.l2jserver.loginserver.config.Configuration.telnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.loginserver.GameServerTable;
import com.l2jserver.loginserver.LoginController;
import com.l2jserver.loginserver.LoginServer;

public final class LoginStatusThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(LoginStatusThread.class);

	/** Rate-limit попыток аутентификации по IP. После N неверных — временный lockout. */
	private static final int MAX_AUTH_FAILURES = 5;
	private static final long AUTH_LOCKOUT_MS = 15L * 60_000L;

	private static final Map<String, AuthAttemptInfo> AUTH_ATTEMPTS = new ConcurrentHashMap<>();

	private static final class AuthAttemptInfo {
		final AtomicInteger failures = new AtomicInteger();
		volatile long lockoutUntil;
	}

	private final Socket _cSocket;
	
	private final PrintWriter _print;
	
	private final BufferedReader _read;
	
	private boolean _redirectLogger;
	
	private void telnetOutput(int type, String text) {
		if (type == 1) {
			System.out.println("TELNET | " + text);
		} else if (type == 2) {
			System.out.print("TELNET | " + text);
		} else if (type == 3) {
			System.out.print(text);
		} else if (type == 4) {
			System.out.println(text);
		} else {
			System.out.println("TELNET | " + text);
		}
	}
	
	private boolean isValidIP(Socket client) {
		boolean result = false;
		InetAddress clientIP = client.getInetAddress();
		
		// convert IP to String, and compare with list
		String clientStringIP = clientIP.getHostAddress();
		
		telnetOutput(1, "Connection from: " + clientStringIP);
		
		// read and loop through a list of IPs, compare with them to the newIP
		if (server().isDebug()) {
			telnetOutput(2, "");
		}
		
		if (server().isDebug()) {
			telnetOutput(3, "Comparing ip to list...");
		}
		
		for (String host : telnet().getHosts()) {
			try {
				String ipToCompare = InetAddress.getByName(host).getHostAddress();
				if (clientStringIP.equals(ipToCompare)) {
					result = true;
				}
				
				if (server().isDebug()) {
					telnetOutput(3, clientStringIP + " = " + ipToCompare + "(" + host + ") = " + result);
				}
			} catch (Exception ex) {
				LOG.warn("There has been an error parsing host {}!", host, ex);
			}
		}
		
		if (server().isDebug()) {
			telnetOutput(4, "Allow IP: " + result);
		}
		return result;
	}
	
	public LoginStatusThread(Socket client, int uptime, String StatusPW) throws Exception {
		_cSocket = client;
		final String clientIp = client.getInetAddress().getHostAddress();

		_print = new PrintWriter(_cSocket.getOutputStream());
		_read = new BufferedReader(new InputStreamReader(_cSocket.getInputStream()));

		if (!isValidIP(client)) {
			telnetOutput(5, "Connection attempt from " + clientIp + " rejected.");
			_cSocket.close();
			return;
		}

		// Rate-limit по IP. После MAX_AUTH_FAILURES неудач — lockout на AUTH_LOCKOUT_MS.
		AuthAttemptInfo info = AUTH_ATTEMPTS.computeIfAbsent(clientIp, k -> new AuthAttemptInfo());
		if (info.lockoutUntil > System.currentTimeMillis()) {
			telnetOutput(5, "Rate-limited telnet auth from " + clientIp + ".");
			_print.println("Too many attempts, try again later.");
			_print.flush();
			_cSocket.close();
			return;
		}

		telnetOutput(1, clientIp + " accepted.");
		_print.println("Welcome To The L2J Telnet Session.");
		_print.println("Please Insert Your Password!");
		_print.print("Password: ");
		_print.flush();
		String tmpLine = null;
		try {
			tmpLine = _read.readLine();
		} catch (Exception ex) {
			// SocketTimeoutException: клиент не прислал пароль вовремя (Status выставил soTimeout).
			telnetOutput(5, "Timed-out telnet auth from " + clientIp + ".");
		}
		if (tmpLine == null) {
			_print.println("Error.");
			_print.println("Disconnected...");
			_print.flush();
			_cSocket.close();
			return;
		}

		// Constant-time сравнение паролей, чтобы не утекала длина правильного префикса.
		byte[] got = tmpLine.getBytes();
		byte[] exp = StatusPW == null ? new byte[0] : StatusPW.getBytes();
		if (!java.security.MessageDigest.isEqual(got, exp)) {
			int f = info.failures.incrementAndGet();
			if (f >= MAX_AUTH_FAILURES) {
				info.lockoutUntil = System.currentTimeMillis() + AUTH_LOCKOUT_MS;
				info.failures.set(0);
				LOG.warn("Telnet IP {} locked out after {} failures.", clientIp, MAX_AUTH_FAILURES);
			}
			_print.println("Incorrect Password!");
			_print.println("Disconnected...");
			_print.flush();
			_cSocket.close();
			return;
		}

		// Success: снимаем все ограничения для этого IP, снимаем soTimeout (был выставлен на время auth).
		AUTH_ATTEMPTS.remove(clientIp);
		try {
			_cSocket.setSoTimeout(0);
		} catch (Exception ignore) {
		}
		_print.println("Password Correct!");
		_print.println("[L2J Login Server]");
		_print.print("");
		_print.flush();
		start();
	}
	
	@Override
	public void run() {
		String _usrCommand = "";
		try {
			while ((_usrCommand.compareTo("quit") != 0) && (_usrCommand.compareTo("exit") != 0)) {
				_usrCommand = _read.readLine();
				if (_usrCommand == null) {
					_cSocket.close();
					break;
				}
				if (_usrCommand.equals("help")) {
					_print.println("The following is a list of all available commands: ");
					_print.println("help                - shows this help.");
					_print.println("status              - displays basic server statistics.");
					_print.println("unblock <ip>        - removes <ip> from the ban list.");
					_print.println("shutdown            - shuts down server.");
					_print.println("restart             - restarts the server.");
					_print.println("RedirectLogger      - Telnet will give you some info about server in real time.");
					_print.println("quit                - closes telnet session.");
					_print.println("");
				} else if (_usrCommand.equals("status")) {
					// TODO enhance the output
					_print.println("Registered Server Count: " + GameServerTable.getInstance().getRegisteredGameServers().size());
				} else if (_usrCommand.startsWith("unblock")) {
					try {
						// substring(8).trim(): раньше substring(8) захватывал ведущий
						// пробел ("unblock 1.2.3.4" → " 1.2.3.4"), и lookup по IP не срабатывал.
						_usrCommand = _usrCommand.substring(8).trim();
						if (LoginController.getInstance().removeBanForAddress(_usrCommand)) {
							LOG.warn("IP removed via TELNET by host {}!", _cSocket.getInetAddress().getHostAddress());
							_print.println("The IP " + _usrCommand + " has been removed from the hack protection list!");
						} else {
							_print.println("IP not found in hack protection list...");
						}
					} catch (StringIndexOutOfBoundsException e) {
						_print.println("Please Enter the IP to Unblock!");
					}
				} else if (_usrCommand.startsWith("shutdown")) {
					LoginServer.getInstance().shutdown(false);
					_print.println("Bye Bye!");
					_print.flush();
					_cSocket.close();
				} else if (_usrCommand.startsWith("restart")) {
					LoginServer.getInstance().shutdown(true);
					_print.println("Bye Bye!");
					_print.flush();
					_cSocket.close();
				} else if (_usrCommand.equals("RedirectLogger")) {
					_redirectLogger = true;
				} else if (_usrCommand.equals("quit")) { /* Do Nothing :p - Just here to save us from the "Command Not Understood" Text */
				} else if (_usrCommand.isEmpty()) { /* Do Nothing Again - Same reason as the quit part */
				} else {
					_print.println("Invalid Command");
				}
				_print.print("");
				_print.flush();
			}
			if (!_cSocket.isClosed()) {
				_print.println("Bye Bye!");
				_print.flush();
				_cSocket.close();
			}
			telnetOutput(1, "Connection from " + _cSocket.getInetAddress().getHostAddress() + " was closed by client.");
		} catch (Exception ex) {
			LOG.warn("There has been an error executing login status task!", ex);
		}
	}
	
	public void printToTelnet(String msg) {
		synchronized (_print) {
			_print.println(msg);
			_print.flush();
		}
	}
	
	public boolean isRedirectLogger() {
		return _redirectLogger;
	}
}
