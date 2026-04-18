CREATE TABLE IF NOT EXISTS `accounts` (
  `login` VARCHAR(45) NOT NULL default '',
  -- 256 символов хватает для PBKDF2-SHA256 формата "$pbkdf2-sha256$<iter>$<b64 salt>$<b64 hash>".
  -- Старый SHA-1-Base64 был 28 символов; legacy-значения продолжат работать до первого
  -- успешного логина, после чего пользователь автоматически мигрируется в PBKDF2.
  `password` VARCHAR(256),
  `email` varchar(255) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastactive` bigint(13) unsigned NOT NULL DEFAULT '0',
  `accessLevel` TINYINT NOT NULL DEFAULT 0,
  -- VARCHAR(45) — хватает на полный IPv6 (39 символов + zone id). CHAR(15) обрезал IPv6.
  `lastIP` VARCHAR(45) NULL DEFAULT NULL,
  `lastServer` TINYINT DEFAULT 1,
  `pcIp` VARCHAR(45) DEFAULT NULL,
  `hop1` VARCHAR(45) DEFAULT NULL,
  `hop2` VARCHAR(45) DEFAULT NULL,
  `hop3` VARCHAR(45) DEFAULT NULL,
  `hop4` VARCHAR(45) DEFAULT NULL,
  -- Base32 TOTP secret для 2FA (RFC 6238). NULL = 2FA отключена.
  `totp_secret` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
