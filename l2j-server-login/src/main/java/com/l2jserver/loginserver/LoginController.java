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
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.spec.RSAKeyGenParameterSpec.F4;

import java.net.InetAddress;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.loginserver.audit.AuditLogger;
import com.l2jserver.loginserver.GameServerTable.GameServerInfo;
import com.l2jserver.loginserver.metrics.LoginMetrics;
import com.l2jserver.loginserver.model.AccountInfo;
import com.l2jserver.loginserver.network.L2LoginClient;
import com.l2jserver.loginserver.network.gameserverpackets.ServerStatus;
import com.l2jserver.loginserver.network.serverpackets.LoginFail.LoginFailReason;
import com.l2jserver.loginserver.security.ScrambledKeyPair;
import com.l2jserver.loginserver.util.IPSubnet;
import com.l2jserver.loginserver.util.PasswordPolicy;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Login Controller.
 * @author Zoey76
 * @version 2.6.1.0
 */
public class LoginController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	/** Time before kicking the client if he didn't log yet */
	public static final int LOGIN_TIMEOUT = 60 * 1000;

	// ---- Криптография паролей ------------------------------------------------
	// PBKDF2-SHA256. 600k итераций — OWASP recommendation 2023 для SHA-256.
	private static final String PBKDF2_PREFIX = "$pbkdf2-sha256$";
	private static final int PBKDF2_ITERATIONS = 600_000;
	private static final int PBKDF2_SALT_BYTES = 16;
	private static final int PBKDF2_HASH_BITS = 256;

	/** Единый источник криптостойкого рандома для login-server. */
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	// ---- Per-account brute-force lockout ------------------------------------
	/** Попытки неудачных логинов на аккаунт (включая «аккаунт не существует»). */
	private final Map<String, AtomicInteger> _failedAttemptsPerAccount = new ConcurrentHashMap<>();
	/** Время, до которого аккаунт заблокирован после серии неудач. */
	private final Map<String, Long> _accountLockUntil = new ConcurrentHashMap<>();
	/** Порог срабатывания per-account локаута (независимо от LoginTryBeforeBan для IP). */
	private static final int ACCOUNT_LOCK_THRESHOLD = 8;
	/** Базовое окно блокировки в мс (экспоненциальный backoff от количества попыток). */
	private static final long ACCOUNT_LOCK_BASE_MS = 60_000L; // 1 минута

	/** Authed Clients on LoginServer */
	protected final Map<String, L2LoginClient> _loginServerClients = new ConcurrentHashMap<>();

	/** Публичный count для метрик. */
	public int getAuthedClientCount() {
		return _loginServerClients.size();
	}

	private final Map<InetAddress, AtomicInteger> _failedLoginAttempts = new ConcurrentHashMap<>();
	private final Map<InetAddress, Long> _bannedIps = new ConcurrentHashMap<>();

	/** CIDR-бан подсетей. Пример: 10.0.0.0/8. Проверяется в isBannedAddress. */
	private final List<IPSubnet> _bannedSubnets = new CopyOnWriteArrayList<>();

	/**
	 * Кэш account→GameServerThread: обновляется при register/unregister игрока на GS.
	 * Позволяет O(1) отвечать на isAccountInAnyGameServer вместо O(N) скана всех GS.
	 */
	private final Map<String, GameServerThread> _accountToGs = new ConcurrentHashMap<>();

	/**
	 * In-flight lock для сериализации параллельных логинов одного аккаунта.
	 * Без него два клиента одного логина, стартующие одновременно, могут оба
	 * проскочить check + putIfAbsent разнесённые во времени.
	 */
	private final Set<String> _loginsInProgress = ConcurrentHashMap.newKeySet();

	protected final ScrambledKeyPair[] _keyPairs;

	protected byte[][] _blowfishKeys;
	private static final int BLOWFISH_KEYS = 20;

	// SQL Queries
	private static final String USER_INFO_SELECT = "SELECT login, password, IF(? > value OR value IS NULL, accessLevel, -1) AS accessLevel, lastServer FROM accounts LEFT JOIN (account_data) ON (account_data.account_name=accounts.login AND account_data.var=\"ban_temp\") WHERE login=?";
	private static final String TOTP_SECRET_SELECT = "SELECT totp_secret FROM accounts WHERE login=?";
	private static final String AUTO_CREATE_ACCOUNTS_INSERT = "INSERT INTO accounts (login, password, lastactive, accessLevel, lastIP) values (?, ?, ?, ?, ?)";
	private static final String ACCOUNT_INFO_UPDATE = "UPDATE accounts SET lastactive = ?, lastIP = ? WHERE login = ?";
	private static final String ACCOUNT_LAST_SERVER_UPDATE = "UPDATE accounts SET lastServer = ? WHERE login = ?";
	private static final String ACCOUNT_ACCESS_LEVEL_UPDATE = "UPDATE accounts SET accessLevel = ? WHERE login = ?";
	private static final String ACCOUNT_IPS_UPDATE = "UPDATE accounts SET pcIp = ?, hop1 = ?, hop2 = ?, hop3 = ?, hop4 = ? WHERE login = ?";
	private static final String ACCOUNT_PASSWORD_UPDATE = "UPDATE accounts SET password = ? WHERE login = ?";
	private static final String ACCOUNT_IPAUTH_SELECT = "SELECT * FROM accounts_ipauth WHERE login = ?";

	private LoginController() {
		LOG.info("Loading Login Controller...");

		_keyPairs = new ScrambledKeyPair[10];

		try {
			final var keygen = KeyPairGenerator.getInstance("RSA");
			final var spec = new RSAKeyGenParameterSpec(1024, F4);
			keygen.initialize(spec);
			for (int i = 0; i < 10; i++) {
				_keyPairs[i] = new ScrambledKeyPair(keygen.generateKeyPair());
			}

			testCipher((RSAPrivateKey) _keyPairs[0].getPair().getPrivate());

			LOG.info("Cached 10 KeyPairs for RSA communication.");
		} catch (Exception ex) {
			LOG.error("There has been an error loading the key pairs!", ex);
		}

		// Store keys for blowfish communication
		generateBlowFishKeys();

		final var purge = new PurgeThread();
		purge.setDaemon(true);
		purge.start();
	}

	/**
	 * This is mostly to force the initialization of the Crypto Implementation, avoiding it being done on runtime when its first needed.<BR>
	 * In short, it avoids the worst-case execution time on runtime by doing it on loading.
	 * @param key Any private RSA Key just for testing purposes.
	 */
	private void testCipher(RSAPrivateKey key) throws Exception {
		// avoid worst-case execution, KenM
		Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
		rsaCipher.init(Cipher.DECRYPT_MODE, key);
	}

	private void generateBlowFishKeys() {
		_blowfishKeys = new byte[BLOWFISH_KEYS][16];
		for (int i = 0; i < BLOWFISH_KEYS; i++) {
			// SecureRandom: полный диапазон байт (включая 0). Старый код
			// использовал Rnd.nextInt(255)+1 → никогда не давал нулевой байт.
			SECURE_RANDOM.nextBytes(_blowfishKeys[i]);
		}
		LOG.info("Stored {} keys for Blowfish communication.", _blowfishKeys.length);
	}

	public byte[] getBlowfishKey() {
		return _blowfishKeys[SECURE_RANDOM.nextInt(BLOWFISH_KEYS)];
	}

	public SessionKey assignSessionKeyToClient(String account, L2LoginClient client) {
		// Клиент уже был зарегистрирован через tryCheckinAccount.putIfAbsent —
		// повторный put здесь только затирал бы ту же ссылку. Не делаем.
		return new SessionKey(
			SECURE_RANDOM.nextInt(),
			SECURE_RANDOM.nextInt(),
			SECURE_RANDOM.nextInt(),
			SECURE_RANDOM.nextInt());
	}

	public void removeAuthedLoginClient(String account) {
		if (account == null) {
			return;
		}
		_loginServerClients.remove(account);
	}

	public L2LoginClient getAuthedClient(String account) {
		return _loginServerClients.get(account);
	}

	public AccountInfo retrieveAccountInfo(InetAddress clientAddr, String login, String password) {
		return retrieveAccountInfo(clientAddr, login, password, true);
	}

	/**
	 * Инкремент per-IP счётчика. Атомарный: merge исключает lost-update при
	 * параллельных неудачных попытках с одного IP.
	 */
	private void recordFailedLoginAttempt(InetAddress addr) {
		int attempts = _failedLoginAttempts
			.computeIfAbsent(addr, k -> new AtomicInteger())
			.incrementAndGet();

		if (attempts >= server().getLoginTryBeforeBan()) {
			addBanForAddress(addr, server().getLoginBlockAfterBan() * 1000L);
			clearFailedLoginAttempts(addr);
			LOG.warn("Added banned address {}, too many login attempts!", addr.getHostAddress());
		}
	}

	private void clearFailedLoginAttempts(InetAddress addr) {
		_failedLoginAttempts.remove(addr);
	}

	// ---- Per-account lockout -------------------------------------------------

	/**
	 * Проверяет, заблокирован ли логин-аккаунт на данный момент.
	 * Per-account лимит нужен потому что per-IP обходится распределённым брутом
	 * (множество ботов на разных IP против одного популярного логина).
	 */
	public boolean isAccountLocked(String login) {
		if (login == null) {
			return false;
		}
		Long lockedUntil = _accountLockUntil.get(login);
		if (lockedUntil == null) {
			return false;
		}
		if (System.currentTimeMillis() >= lockedUntil) {
			_accountLockUntil.remove(login);
			return false;
		}
		return true;
	}

	private void recordFailedAccountAttempt(String login) {
		if (login == null) {
			return;
		}
		final String key = login.toLowerCase();
		int attempts = _failedAttemptsPerAccount
			.computeIfAbsent(key, k -> new AtomicInteger())
			.incrementAndGet();

		if (attempts >= ACCOUNT_LOCK_THRESHOLD) {
			// Экспоненциальный backoff: 1 мин, 2, 4, 8 ... до 1 часа.
			int over = attempts - ACCOUNT_LOCK_THRESHOLD;
			long lockMs = Math.min(ACCOUNT_LOCK_BASE_MS << Math.min(over, 6), 3_600_000L);
			_accountLockUntil.put(key, System.currentTimeMillis() + lockMs);
			LOG.warn("Account {} locked for {} ms after {} failed attempts.", key, lockMs, attempts);
			AuditLogger.accountLocked(key, lockMs);
			LoginMetrics.getInstance().incAccountLock();
		}
	}

	private void clearFailedAccountAttempts(String login) {
		if (login == null) {
			return;
		}
		final String key = login.toLowerCase();
		_failedAttemptsPerAccount.remove(key);
		_accountLockUntil.remove(key);
	}

	// ---- Password hashing ----------------------------------------------------

	/**
	 * Генерирует PBKDF2-SHA256 хэш нового пароля.
	 * Формат: {@code $pbkdf2-sha256$<iter>$<base64 salt>$<base64 hash>}.
	 */
	public static String hashPassword(String plaintext) {
		try {
			byte[] salt = new byte[PBKDF2_SALT_BYTES];
			SECURE_RANDOM.nextBytes(salt);
			byte[] hash = pbkdf2(plaintext.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_HASH_BITS);
			Base64.Encoder enc = Base64.getEncoder().withoutPadding();
			return PBKDF2_PREFIX + PBKDF2_ITERATIONS + "$" + enc.encodeToString(salt) + "$" + enc.encodeToString(hash);
		} catch (Exception ex) {
			throw new IllegalStateException("PBKDF2 is required but unavailable", ex);
		}
	}

	/**
	 * Проверяет пароль против сохранённого значения.
	 * Поддерживает PBKDF2 (новый формат) и Base64(SHA-1) — legacy формат.
	 * При успешной проверке legacy-хэша мигрирует запись в PBKDF2 (lazy migration).
	 */
	private boolean verifyPassword(String login, String plaintext, String stored) {
		if (stored == null || plaintext == null) {
			return false;
		}
		if (stored.startsWith(PBKDF2_PREFIX)) {
			return verifyPbkdf2(plaintext, stored);
		}
		// Legacy: Base64(SHA-1(plaintext)).
		try {
			final var md = MessageDigest.getInstance("SHA");
			final var legacyHash = Base64.getEncoder().encodeToString(md.digest(plaintext.getBytes(UTF_8)));
			byte[] a = legacyHash.getBytes(UTF_8);
			byte[] b = stored.getBytes(UTF_8);
			if (!MessageDigest.isEqual(a, b)) {
				return false;
			}
			// Миграция прошла успешно — апгрейдим формат в БД.
			try {
				updateStoredPassword(login, hashPassword(plaintext));
				LoginMetrics.getInstance().incPasswordMigration();
			} catch (Exception ex) {
				LOG.warn("Lazy password migration failed for {}; will retry next login.", login, ex);
			}
			return true;
		} catch (Exception ex) {
			LOG.warn("SHA-1 legacy verify failed for {}.", login, ex);
			return false;
		}
	}

	/** Public accessor — используется внешними модулями (ChangePassword). */
	public static boolean verifyPbkdf2Public(String plaintext, String stored) {
		return verifyPbkdf2(plaintext, stored);
	}

	private static boolean verifyPbkdf2(String plaintext, String stored) {
		try {
			// $pbkdf2-sha256$<iter>$<salt>$<hash>
			String tail = stored.substring(PBKDF2_PREFIX.length());
			String[] parts = tail.split("\\$");
			if (parts.length != 3) {
				return false;
			}
			int iter = Integer.parseInt(parts[0]);
			byte[] salt = Base64.getDecoder().decode(parts[1]);
			byte[] expected = Base64.getDecoder().decode(parts[2]);
			byte[] actual = pbkdf2(plaintext.toCharArray(), salt, iter, expected.length * 8);
			return MessageDigest.isEqual(expected, actual);
		} catch (Exception ex) {
			LOG.warn("PBKDF2 verify failed.", ex);
			return false;
		}
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bits) throws Exception {
		KeySpec spec = new PBEKeySpec(password, salt, iterations, bits);
		return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
	}

	/**
	 * Получить TOTP-секрет для аккаунта или null, если 2FA не настроена.
	 * Поле accounts.totp_secret появляется при ALTER TABLE-миграции;
	 * если колонки нет — метод тихо возвращает null.
	 */
	public String getTotpSecret(String login) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(TOTP_SECRET_SELECT)) {
			ps.setString(1, login);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					String s = rs.getString(1);
					if (s != null && !s.isBlank()) {
						return s;
					}
				}
			}
		} catch (Exception ex) {
			// Колонки нет или др. ошибка — просто считаем, что 2FA отключена.
		}
		return null;
	}

	/** Обновить хранимый хэш пароля (используется lazy-миграцией). */
	public void updateStoredPassword(String login, String newStored) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(ACCOUNT_PASSWORD_UPDATE)) {
			ps.setString(1, newStored);
			ps.setString(2, login);
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("There has been an error updating password for account {}!", login, ex);
		}
	}

	// --------------------------------------------------------------------------

	private AccountInfo retrieveAccountInfo(InetAddress addr, String login, String password, boolean autoCreateIfEnabled) {
		// Per-account блокировка: до БД не идём, если аккаунт в lockout-окне.
		if (isAccountLocked(login)) {
			return null;
		}

		try {
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(USER_INFO_SELECT)) {
				ps.setString(1, Long.toString(System.currentTimeMillis()));
				ps.setString(2, login);
				try (var rs = ps.executeQuery()) {
					if (rs.next()) {
						if (server().isDebug()) {
							LOG.info("Account {} exists.", login);
						}

						final var info = new AccountInfo(rs.getString("login"), rs.getString("password"), rs.getInt("accessLevel"), rs.getInt("lastServer"));
						if (!verifyPassword(info.getLogin(), password, info.getPassHash())) {
							recordFailedLoginAttempt(addr);
							recordFailedAccountAttempt(info.getLogin());
							AuditLogger.loginFailed(info.getLogin(), addr.getHostAddress(), "wrong_password");
							LoginMetrics.getInstance().incLoginFail();
							return null;
						}

						clearFailedLoginAttempts(addr);
						clearFailedAccountAttempts(info.getLogin());
						AuditLogger.loginSuccess(info.getLogin(), addr.getHostAddress());
						LoginMetrics.getInstance().incLoginOk();
						return info;
					}
				}
			}

			if (!autoCreateIfEnabled || !server().autoCreateAccounts()) {
				// Аккаунт не существует + auto-create выключен.
				// Регистрируем неудачу по обеим осям (IP и логин), чтобы
				// спам несуществующими логинами тоже триггерил блокировку.
				recordFailedLoginAttempt(addr);
				recordFailedAccountAttempt(login);
				return null;
			}

			// Policy: отклоняем auto-create со слабым паролем, чтобы не плодить
			// моментально-брутящиеся учётки. Пользователь получит REASON_USER_OR_PASS_WRONG.
			final PasswordPolicy.Result policy = PasswordPolicy.validate(password);
			if (!policy.ok) {
				LOG.info("Rejecting auto-create of {}: {}", login, policy.message);
				return null;
			}

			// Auto-create: сохраняем уже PBKDF2, а не SHA-1.
			final String stored = hashPassword(password);
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(AUTO_CREATE_ACCOUNTS_INSERT)) {
				ps.setString(1, login);
				ps.setString(2, stored);
				ps.setLong(3, System.currentTimeMillis());
				ps.setInt(4, server().autoCreateAccountsAccessLevel());
				ps.setString(5, addr.getHostAddress());
				ps.execute();
			} catch (Exception ex) {
				LOG.warn("There has been an error auto-creating the account {}!", login, ex);
				return null;
			}

			LOG.info("Auto-created account {}.", login);
			return retrieveAccountInfo(addr, login, password, false);
		} catch (Exception ex) {
			LOG.warn("There has been an error getting account info for {}!", login, ex);
			return null;
		}
	}

	public AuthLoginResult tryCheckinAccount(L2LoginClient client, InetAddress address, AccountInfo info) {
		if (info.getAccessLevel() < 0) {
			if (info.getAccessLevel() == server().autoCreateAccountsAccessLevel()) {
				return AuthLoginResult.ACCOUNT_INACTIVE;
			}
			return AuthLoginResult.ACCOUNT_BANNED;
		}

		final String login = info.getLogin();

		// In-flight lock: один параллельный tryCheckinAccount на логин.
		// Это сужает окно гонки между isAccountInAnyGameServer и putIfAbsent
		// до работы одного потока; параллельные попытки корректно получают ALREADY_ON_LS.
		if (!_loginsInProgress.add(login)) {
			return AuthLoginResult.ALREADY_ON_LS;
		}
		try {
			AuthLoginResult ret = AuthLoginResult.INVALID_PASSWORD;
			if (canCheckIn(client, address, info)) {
				ret = AuthLoginResult.ALREADY_ON_GS;
				if (!isAccountInAnyGameServer(login)) {
					ret = AuthLoginResult.ALREADY_ON_LS;

					// Если на LS уже висит клиент с таким логином и он уже отключён
					// (zombie после abrupt disconnect до onDisconnection),
					// закрываем его и пускаем нового.
					L2LoginClient existing = _loginServerClients.get(login);
					if (existing != null && (existing.getConnection() == null
						|| existing.getConnection().isClosed())) {
						_loginServerClients.remove(login, existing);
					}

					if (_loginServerClients.putIfAbsent(login, client) == null) {
						ret = AuthLoginResult.AUTH_SUCCESS;
					}
				}
			}
			return ret;
		} finally {
			_loginsInProgress.remove(login);
		}
	}

	/**
	 * Adds the address to the ban list of the login server, with the given end time in milliseconds.
	 * @param address The Address to be banned.
	 * @param expiration Timestamp in milliseconds when this ban expires
	 * @throws Exception if the address is invalid.
	 */
	public void addBanForAddress(String address, long expiration) throws Exception {
		_bannedIps.putIfAbsent(InetAddress.getByName(address), expiration);
	}

	/**
	 * Adds the address to the ban list of the login server, with the given duration.
	 * @param address The Address to be banned.
	 * @param duration is milliseconds
	 */
	public void addBanForAddress(InetAddress address, long duration) {
		_bannedIps.putIfAbsent(address, System.currentTimeMillis() + duration);
	}

	/**
	 * IPv4/IPv6-safe ban check: сначала точное совпадение, затем CIDR-подсети.
	 */
	public boolean isBannedAddress(InetAddress address) {
		if (address == null) {
			return false;
		}
		Long bi = _bannedIps.get(address);
		if (bi != null) {
			if ((bi > 0) && (bi < System.currentTimeMillis())) {
				_bannedIps.remove(address);
				LOG.info("Removed expired IP address ban {}.", address.getHostAddress());
			} else {
				return true;
			}
		}
		// CIDR-подсети (bulk-бан подсети / прокси-сервиса).
		for (IPSubnet sn : _bannedSubnets) {
			if (sn.applyMask(address.getAddress())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Добавляет CIDR-подсеть в ban-list (формат "a.b.c.0/24" или "2001:db8::/32").
	 */
	public void addBannedSubnet(String cidr) throws Exception {
		_bannedSubnets.add(new IPSubnet(cidr));
	}

	public boolean removeBannedSubnet(String cidr) {
		try {
			final IPSubnet target = new IPSubnet(cidr);
			return _bannedSubnets.removeIf(s -> s.toString().equals(target.toString()));
		} catch (Exception ex) {
			return false;
		}
	}

	public List<IPSubnet> getBannedSubnets() {
		return _bannedSubnets;
	}

	public Map<InetAddress, Long> getBannedIps() {
		return _bannedIps;
	}

	/**
	 * Remove the specified address from the ban list
	 * @param address The address to be removed from the ban list
	 * @return true if the ban was removed, false if there was no ban for this ip
	 */
	public boolean removeBanForAddress(InetAddress address) {
		return _bannedIps.remove(address) != null;
	}

	/**
	 * Remove the specified address from the ban list
	 * @param address The address to be removed from the ban list
	 * @return true if the ban was removed, false if there was no ban for this ip or the address was invalid.
	 */
	public boolean removeBanForAddress(String address) {
		try {
			return this.removeBanForAddress(InetAddress.getByName(address));
		} catch (Exception e) {
			return false;
		}
	}

	public SessionKey getKeyForAccount(String account) {
		L2LoginClient client = _loginServerClients.get(account);
		if (client != null) {
			return client.getSessionKey();
		}
		return null;
	}

	public boolean isAccountInAnyGameServer(String account) {
		// Быстрая проверка по кэшу. Fallback к линейному скану только если
		// кэш не сформирован (например, сразу после рестарта LS).
		GameServerThread cached = _accountToGs.get(account);
		if (cached != null && cached.hasAccountOnGameServer(account)) {
			return true;
		}
		Collection<GameServerInfo> serverList = GameServerTable.getInstance().getRegisteredGameServers().values();
		for (GameServerInfo gsi : serverList) {
			GameServerThread gst = gsi.getGameServerThread();
			if ((gst != null) && gst.hasAccountOnGameServer(account)) {
				_accountToGs.putIfAbsent(account, gst);
				return true;
			}
		}
		return false;
	}

	public GameServerInfo getAccountOnGameServer(String account) {
		GameServerThread cached = _accountToGs.get(account);
		if (cached != null && cached.hasAccountOnGameServer(account)) {
			return cached.getGameServerInfo();
		}
		Collection<GameServerInfo> serverList = GameServerTable.getInstance().getRegisteredGameServers().values();
		for (GameServerInfo gsi : serverList) {
			GameServerThread gst = gsi.getGameServerThread();
			if ((gst != null) && gst.hasAccountOnGameServer(account)) {
				_accountToGs.putIfAbsent(account, gst);
				return gsi;
			}
		}
		return null;
	}

	/** Вызывается из GameServerThread при регистрации аккаунта на GS. */
	public void onAccountJoinedGameServer(String account, GameServerThread gs) {
		if (account != null && gs != null) {
			_accountToGs.put(account, gs);
		}
	}

	/** Вызывается из GameServerThread при выходе аккаунта с GS. */
	public void onAccountLeftGameServer(String account, GameServerThread gs) {
		if (account == null) {
			return;
		}
		// Удаляем только если наш GS всё ещё owner — иначе можем потереть свежего.
		_accountToGs.remove(account, gs);
	}

	public void getCharactersOnAccount(String account) {
		Collection<GameServerInfo> serverList = GameServerTable.getInstance().getRegisteredGameServers().values();
		for (GameServerInfo gsi : serverList) {
			if (gsi.isAuthed()) {
				gsi.getGameServerThread().requestCharacters(account);
			}
		}
	}

	public boolean isLoginPossible(L2LoginClient client, int serverId) {
		GameServerInfo gsi = GameServerTable.getInstance().getRegisteredGameServerById(serverId);
		int access = client.getAccessLevel();
		if ((gsi != null) && gsi.isAuthed()) {
			boolean loginOk = ((gsi.getCurrentPlayerCount() < gsi.getMaxPlayers()) && (gsi.getStatus() != ServerStatus.STATUS_GM_ONLY)) || (access > 0);

			if (loginOk && (client.getLastServer() != serverId)) {
				try (var con = ConnectionFactory.getInstance().getConnection();
					var ps = con.prepareStatement(ACCOUNT_LAST_SERVER_UPDATE)) {
					ps.setInt(1, serverId);
					ps.setString(2, client.getAccount());
					ps.executeUpdate();
				} catch (Exception ex) {
					LOG.warn("There has been an error setting last server for account {}!", client.getAccount(), ex);
				}
			}
			return loginOk;
		}
		return false;
	}

	public void setAccountAccessLevel(String account, int banLevel) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(ACCOUNT_ACCESS_LEVEL_UPDATE)) {
			ps.setInt(1, banLevel);
			ps.setString(2, account);
			ps.executeUpdate();
			AuditLogger.accessLevelChanged(account, banLevel, null);
		} catch (Exception ex) {
			LOG.warn("There has been an error setting account level for account {}!", account, ex);
		}
	}

	public void setAccountLastTracert(String account, String pcIp, String hop1, String hop2, String hop3, String hop4) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(ACCOUNT_IPS_UPDATE)) {
			ps.setString(1, pcIp);
			ps.setString(2, hop1);
			ps.setString(3, hop2);
			ps.setString(4, hop3);
			ps.setString(5, hop4);
			ps.setString(6, account);
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("There has been an error setting last tracert for account {}!", account, ex);
		}
	}

	public void setCharactersOnServer(String account, int charsNum, long[] timeToDel, int serverId) {
		final var client = _loginServerClients.get(account);
		if (client == null) {
			return;
		}

		if (charsNum > 0) {
			client.setCharsOnServ(serverId, charsNum);
		}

		if (timeToDel.length > 0) {
			client.serCharsWaitingDelOnServ(serverId, timeToDel);
		}
	}

	/**
	 * This method returns one of the cached {@link ScrambledKeyPair ScrambledKeyPairs} for communication with Login Clients.
	 * @return a scrambled keypair
	 */
	public ScrambledKeyPair getScrambledRSAKeyPair() {
		return _keyPairs[SECURE_RANDOM.nextInt(_keyPairs.length)];
	}

	/**
	 * @param client the client
	 * @param address client host address
	 * @param info the account info to check in
	 * @return true when ok to check in, false otherwise
	 */
	public boolean canCheckIn(L2LoginClient client, InetAddress address, AccountInfo info) {
		try {
			List<InetAddress> ipWhiteList = new ArrayList<>();
			List<InetAddress> ipBlackList = new ArrayList<>();
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(ACCOUNT_IPAUTH_SELECT)) {
				ps.setString(1, info.getLogin());
				try (var rs = ps.executeQuery()) {
					while (rs.next()) {
						final var ip = rs.getString("ip");
						if (!isValidIPAddress(ip)) {
							continue;
						}

						final var type = rs.getString("type");
						if (type.equals("allow")) {
							ipWhiteList.add(InetAddress.getByName(ip));
						} else if (type.equals("deny")) {
							ipBlackList.add(InetAddress.getByName(ip));
						}
					}
				}
			}

			// Check IP
			if (!ipWhiteList.isEmpty() || !ipBlackList.isEmpty()) {
				if (!ipWhiteList.isEmpty() && !ipWhiteList.contains(address)) {
					LOG.warn("Account checkin attempt from address {} not present on whitelist for account {}!", address.getHostAddress(), info.getLogin());
					return false;
				}

				if (!ipBlackList.isEmpty() && ipBlackList.contains(address)) {
					LOG.warn("Account checkin attempt from address {} on blacklist for account {}!", address.getHostAddress(), info.getLogin());
					return false;
				}
			}

			client.setAccessLevel(info.getAccessLevel());
			client.setLastServer(info.getLastServer());
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(ACCOUNT_INFO_UPDATE)) {
				ps.setLong(1, System.currentTimeMillis());
				ps.setString(2, address.getHostAddress());
				ps.setString(3, info.getLogin());
				ps.execute();
			}
			return true;
		} catch (Exception ex) {
			LOG.warn("There has been an error logging in!", ex);
			return false;
		}
	}

	/**
	 * IPv4/IPv6-safe проверка через InetAddress.getByName.
	 * Старый парсер IPv4 падал NFE на IPv6 и нечисловых октетах.
	 */
	public boolean isValidIPAddress(String ipAddress) {
		if (ipAddress == null || ipAddress.isBlank()) {
			return false;
		}
		try {
			InetAddress.getByName(ipAddress);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	class PurgeThread extends Thread {
		public PurgeThread() {
			setName("PurgeThread");
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				for (L2LoginClient client : _loginServerClients.values()) {
					if (client == null) {
						continue;
					}
					if ((client.getConnectionStartTime() + LOGIN_TIMEOUT) < System.currentTimeMillis()) {
						client.close(LoginFailReason.REASON_ACCESS_FAILED);
					}
				}

				try {
					Thread.sleep(LOGIN_TIMEOUT / 2);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	public enum AuthLoginResult {
		INVALID_PASSWORD,
		ACCOUNT_INACTIVE,
		ACCOUNT_BANNED,
		ALREADY_ON_LS,
		ALREADY_ON_GS,
		AUTH_SUCCESS
	}

	public static LoginController getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		protected static final LoginController INSTANCE = new LoginController();
	}
}
