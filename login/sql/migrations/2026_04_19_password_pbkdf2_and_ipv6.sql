-- ============================================================================
-- Миграция для существующих инсталляций login-server.
-- Применить ОДИН РАЗ к базе login-сервера.
--
-- Что делает:
--  1) Расширяет accounts.password до VARCHAR(256) — под PBKDF2-SHA256 формат.
--  2) Расширяет все IP-колонки до VARCHAR(45) — под IPv6 (и оставляет место для zone id).
--  3) Добавляет индекс на accounts_ipauth.login, чтобы canCheckIn не делал full-scan.
--
-- Старые SHA-1-Base64 пароли продолжат работать и будут автоматически
-- пересохранены в PBKDF2 при первом успешном логине каждого пользователя.
-- ============================================================================

ALTER TABLE `accounts` MODIFY COLUMN `password` VARCHAR(256);
ALTER TABLE `accounts` MODIFY COLUMN `lastIP` VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE `accounts` MODIFY COLUMN `pcIp`   VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE `accounts` MODIFY COLUMN `hop1`   VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE `accounts` MODIFY COLUMN `hop2`   VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE `accounts` MODIFY COLUMN `hop3`   VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE `accounts` MODIFY COLUMN `hop4`   VARCHAR(45) NULL DEFAULT NULL;

ALTER TABLE `accounts_ipauth` MODIFY COLUMN `ip` VARCHAR(45) NOT NULL;

-- Индекс добавляем только если его ещё нет (MySQL 8+).
-- Если движок падает из-за дубликата — закомментируйте строку или выполните отдельно.
CREATE INDEX `idx_login` ON `accounts_ipauth` (`login`);
