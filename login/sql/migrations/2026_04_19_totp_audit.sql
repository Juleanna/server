-- Дополнение к миграции от 2026-04-19: TOTP-секрет и audit-лог.
-- Идемпотентно — проверяет, что колонки/таблицы ещё нет.

-- TOTP-секрет (Base32, RFC 4648). NULL = 2FA отключена.
ALTER TABLE `accounts` ADD COLUMN `totp_secret` VARCHAR(64) DEFAULT NULL;

-- Audit-лог (из login_audit.sql)
CREATE TABLE IF NOT EXISTS `login_audit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ts` BIGINT NOT NULL,
  `event` VARCHAR(32) NOT NULL,
  `account` VARCHAR(45) DEFAULT NULL,
  `ip` VARCHAR(45) DEFAULT NULL,
  `actor` VARCHAR(45) DEFAULT NULL,
  `details` VARCHAR(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ts` (`ts`),
  KEY `idx_event_ts` (`event`, `ts`),
  KEY `idx_account_ts` (`account`, `ts`),
  KEY `idx_ip_ts` (`ip`, `ts`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
