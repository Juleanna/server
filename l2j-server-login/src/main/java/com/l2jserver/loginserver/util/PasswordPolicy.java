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
package com.l2jserver.loginserver.util;

import java.util.Set;

/**
 * Минимальная политика стойкости паролей.
 * Отвергает пустые/короткие/однообразные пароли и базу самых популярных.
 */
public final class PasswordPolicy {

	private static final int MIN_LENGTH = 8;
	private static final int MIN_CHARACTER_CLASSES = 2; // lowercase / uppercase / digit / symbol

	/** Топ-20 самых популярных паролей, мгновенно подбираемых любым ботом. */
	private static final Set<String> TOP_WEAK = Set.of(
		"password", "123456", "12345678", "qwerty", "abc123",
		"monkey", "letmein", "dragon", "111111", "baseball",
		"iloveyou", "trustno1", "sunshine", "master", "welcome",
		"shadow", "ashley", "football", "jesus", "ninja");

	private PasswordPolicy() {
	}

	public static Result validate(String password) {
		if (password == null || password.isEmpty()) {
			return Result.fail("Password is empty.");
		}
		if (password.length() < MIN_LENGTH) {
			return Result.fail("Password must be at least " + MIN_LENGTH + " characters.");
		}
		if (TOP_WEAK.contains(password.toLowerCase())) {
			return Result.fail("Password is in the list of most common weak passwords.");
		}
		if (password.chars().distinct().count() < 4) {
			return Result.fail("Password is too repetitive.");
		}
		int classes = 0;
		if (password.chars().anyMatch(Character::isLowerCase)) classes++;
		if (password.chars().anyMatch(Character::isUpperCase)) classes++;
		if (password.chars().anyMatch(Character::isDigit)) classes++;
		if (password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) classes++;
		if (classes < MIN_CHARACTER_CLASSES) {
			return Result.fail("Password must use at least " + MIN_CHARACTER_CLASSES
				+ " character classes (lower/upper/digit/symbol).");
		}
		return Result.ok();
	}

	public static final class Result {
		public final boolean ok;
		public final String message;

		private Result(boolean ok, String message) {
			this.ok = ok;
			this.message = message;
		}

		static Result ok() {
			return new Result(true, null);
		}

		static Result fail(String msg) {
			return new Result(false, msg);
		}
	}
}
