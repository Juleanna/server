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
package com.l2jserver.loginserver.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Account Info.
 * @author HorridoJoho
 * @version 2.6.1.0
 */
public final class AccountInfo {
	private final String _login;
	private final String _passHash;
	private final int _accessLevel;
	private final int _lastServer;
	
	public AccountInfo(final String login, final String passHash, final int accessLevel, final int lastServer) {
		Objects.requireNonNull(login, "login parameter is null");
		Objects.requireNonNull(passHash, "passHash parameter is null");
		
		if (login.isEmpty()) {
			throw new IllegalArgumentException("login string is empty");
		}
		if (passHash.isEmpty()) {
			throw new IllegalArgumentException("passHash string is empty");
		}
		
		_login = login.toLowerCase();
		_passHash = passHash;
		_accessLevel = accessLevel;
		_lastServer = lastServer;
	}
	
	/**
	 * Constant-time сравнение хэшей, чтобы исключить timing-атаку на
	 * определение правильной длины/префикса.
	 */
	public boolean checkPassHash(final String passHash) {
		if (passHash == null) {
			return false;
		}
		byte[] a = _passHash.getBytes(StandardCharsets.UTF_8);
		byte[] b = passHash.getBytes(StandardCharsets.UTF_8);
		return MessageDigest.isEqual(a, b);
	}

	/**
	 * Возвращает сырой хэш/формат пароля из БД (для определения нужна ли миграция).
	 */
	public String getPassHash() {
		return _passHash;
	}

	public String getLogin() {
		return _login;
	}
	
	public int getAccessLevel() {
		return _accessLevel;
	}
	
	public int getLastServer() {
		return _lastServer;
	}
}
