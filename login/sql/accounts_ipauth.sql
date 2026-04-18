CREATE TABLE IF NOT EXISTS `accounts_ipauth` (
  `login` varchar(45) NOT NULL,
  -- 45 символов — полный IPv6 с zone id. Char(15) обрезал IPv6.
  `ip` VARCHAR(45) NOT NULL,
  `type` enum('deny','allow') NULL DEFAULT 'allow',
  -- Индекс по login: SELECT из canCheckIn иначе full-scan на каждый логин.
  KEY `idx_login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
