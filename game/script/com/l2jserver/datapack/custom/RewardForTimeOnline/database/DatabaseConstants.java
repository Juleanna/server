package com.l2jserver.datapack.custom.RewardForTimeOnline.database;

/**
 * Константы и SQL запросы для базы данных системы наград
 * Централизует все SQL запросы для лучшей поддержки и управления
 * @author Dafna
 */
public final class DatabaseConstants {
    
    // Предотвращаем создание экземпляров
    private DatabaseConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // ===============================
    // ТАБЛИЦЫ - DDL
    // ===============================
    
    /**
     * Таблица групп наград
     */
    public static final String CREATE_REWARD_GROUPS_TABLE = """
        CREATE TABLE IF NOT EXISTS reward_groups (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(100) NOT NULL UNIQUE,
            description TEXT,
            access_levels TEXT,
            is_active BOOLEAN DEFAULT TRUE,
            priority INT DEFAULT 0,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_name (name),
            INDEX idx_active_priority (is_active, priority),
            INDEX idx_updated (updated_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    /**
     * Таблица предметов наград
     */
    public static final String CREATE_REWARD_ITEMS_TABLE = """
        CREATE TABLE IF NOT EXISTS reward_items (
            id INT AUTO_INCREMENT PRIMARY KEY,
            group_id INT NOT NULL,
            item_id INT NOT NULL,
            base_count BIGINT NOT NULL,
            time_interval BIGINT NOT NULL,
            save_to_database BOOLEAN DEFAULT FALSE,
            once_only BOOLEAN DEFAULT FALSE,
            progressive BOOLEAN DEFAULT FALSE,
            required_event VARCHAR(100),
            allowed_days VARCHAR(100),
            min_level INT DEFAULT 0,
            max_level INT DEFAULT 0,
            is_active BOOLEAN DEFAULT TRUE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (group_id) REFERENCES reward_groups(id) ON DELETE CASCADE,
            INDEX idx_group_item (group_id, item_id),
            INDEX idx_item_active (item_id, is_active),
            INDEX idx_progressive (progressive),
            INDEX idx_once_only (once_only)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    /**
     * Таблица календарных событий
     */
    public static final String CREATE_CALENDAR_EVENTS_TABLE = """
        CREATE TABLE IF NOT EXISTS calendar_events (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(100) NOT NULL UNIQUE,
            description TEXT,
            start_date DATETIME NOT NULL,
            end_date DATETIME NOT NULL,
            reward_multiplier DOUBLE DEFAULT 1.0,
            special_item_id INT,
            special_item_count BIGINT,
            is_active BOOLEAN DEFAULT TRUE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_name (name),
            INDEX idx_dates (start_date, end_date),
            INDEX idx_active_dates (is_active, start_date, end_date),
            INDEX idx_updated (updated_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    /**
     * Таблица статистики наград
     */
    public static final String CREATE_STATISTICS_TABLE = """
        CREATE TABLE IF NOT EXISTS reward_statistics (
            id INT AUTO_INCREMENT PRIMARY KEY,
            date DATE NOT NULL,
            player_id INT NOT NULL,
            player_name VARCHAR(100),
            reward_group VARCHAR(100),
            item_id INT,
            item_count BIGINT,
            progressive_multiplier DOUBLE DEFAULT 1.0,
            event_name VARCHAR(100),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            INDEX idx_date_player (date, player_id),
            INDEX idx_reward_group (reward_group),
            INDEX idx_item_stats (item_id, date),
            INDEX idx_player_stats (player_id, date),
            INDEX idx_date_group (date, reward_group),
            INDEX idx_created (created_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    /**
     * Таблица прогрессивных множителей
     */
    public static final String CREATE_PROGRESSIVE_MULTIPLIERS_TABLE = """
        CREATE TABLE IF NOT EXISTS progressive_multipliers (
            id INT AUTO_INCREMENT PRIMARY KEY,
            player_id INT NOT NULL,
            item_id INT NOT NULL,
            multiplier_value DOUBLE NOT NULL,
            last_used TIMESTAMP NOT NULL,
            increment_count INT DEFAULT 0,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            UNIQUE KEY unique_player_item (player_id, item_id),
            INDEX idx_player (player_id),
            INDEX idx_last_used (last_used),
            INDEX idx_value (multiplier_value),
            INDEX idx_updated (updated_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    /**
     * Таблица системной конфигурации
     */
    public static final String CREATE_SYSTEM_CONFIG_TABLE = """
        CREATE TABLE IF NOT EXISTS system_config (
            id INT AUTO_INCREMENT PRIMARY KEY,
            config_key VARCHAR(100) NOT NULL UNIQUE,
            config_value TEXT,
            config_type VARCHAR(50) DEFAULT 'STRING',
            description TEXT,
            last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_key (config_key),
            INDEX idx_type (config_type),
            INDEX idx_modified (last_modified)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    /**
     * Таблица логов системы (опционально)
     */
    public static final String CREATE_SYSTEM_LOGS_TABLE = """
        CREATE TABLE IF NOT EXISTS system_logs (
            id INT AUTO_INCREMENT PRIMARY KEY,
            log_level VARCHAR(20) NOT NULL,
            component VARCHAR(100) NOT NULL,
            message TEXT NOT NULL,
            player_id INT,
            item_id INT,
            additional_data JSON,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            INDEX idx_level_component (log_level, component),
            INDEX idx_player (player_id),
            INDEX idx_created (created_at),
            INDEX idx_component_date (component, created_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    """;
    
    // ===============================
    // ГРУППЫ НАГРАД - CRUD
    // ===============================
    
    public static final String SELECT_ALL_REWARD_GROUPS = """
        SELECT rg.*, GROUP_CONCAT(CONCAT(ri.item_id, ':', ri.base_count, ':', ri.time_interval, 
               ':', ri.save_to_database, ':', ri.once_only, ':', ri.progressive,
               ':', IFNULL(ri.required_event, ''), ':', IFNULL(ri.allowed_days, ''),
               ':', ri.min_level, ':', ri.max_level) SEPARATOR '|') as items
        FROM reward_groups rg
        LEFT JOIN reward_items ri ON rg.id = ri.group_id AND ri.is_active = TRUE
        WHERE rg.is_active = TRUE
        GROUP BY rg.id
        ORDER BY rg.priority DESC, rg.name
    """;
    
    public static final String SELECT_REWARD_GROUP_BY_NAME = """
        SELECT * FROM reward_groups WHERE name = ? AND is_active = TRUE
    """;
    
    public static final String INSERT_REWARD_GROUP = """
        INSERT INTO reward_groups (name, description, access_levels, is_active, priority)
        VALUES (?, ?, ?, ?, ?)
    """;
    
    public static final String UPDATE_REWARD_GROUP = """
        UPDATE reward_groups 
        SET description = ?, access_levels = ?, is_active = ?, priority = ?
        WHERE name = ?
    """;
    
    public static final String DELETE_REWARD_GROUP = """
        UPDATE reward_groups SET is_active = FALSE WHERE name = ?
    """;
    
    public static final String CHECK_GROUPS_CHANGES = """
        SELECT MAX(updated_at) as max_update 
        FROM reward_groups 
        WHERE updated_at > FROM_UNIXTIME(?)
    """;
    
    // ===============================
    // ПРЕДМЕТЫ НАГРАД - CRUD
    // ===============================
    
    public static final String INSERT_REWARD_ITEM = """
        INSERT INTO reward_items (group_id, item_id, base_count, time_interval, 
                                save_to_database, once_only, progressive, required_event,
                                allowed_days, min_level, max_level, is_active)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
    
    public static final String SELECT_ITEMS_BY_GROUP = """
        SELECT * FROM reward_items WHERE group_id = ? AND is_active = TRUE
    """;
    
    public static final String DELETE_ITEMS_BY_GROUP = """
        DELETE FROM reward_items WHERE group_id = ?
    """;
    
    public static final String CLEAR_ALL_REWARD_ITEMS = """
        DELETE FROM reward_items
    """;
    
    public static final String CLEAR_ALL_REWARD_GROUPS = """
        DELETE FROM reward_groups
    """;
    
    // ===============================
    // КАЛЕНДАРНЫЕ СОБЫТИЯ - CRUD
    // ===============================
    
    public static final String SELECT_ACTIVE_CALENDAR_EVENTS = """
        SELECT name, description, start_date, end_date, reward_multiplier,
               special_item_id, special_item_count, is_active
        FROM calendar_events
        WHERE is_active = TRUE AND end_date > NOW()
        ORDER BY start_date
    """;
    
    public static final String SELECT_CALENDAR_EVENT_BY_NAME = """
        SELECT * FROM calendar_events WHERE name = ?
    """;
    
    public static final String INSERT_CALENDAR_EVENT = """
        INSERT INTO calendar_events (name, description, start_date, end_date, 
                                   reward_multiplier, special_item_id, special_item_count, is_active)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            description = VALUES(description),
            start_date = VALUES(start_date),
            end_date = VALUES(end_date),
            reward_multiplier = VALUES(reward_multiplier),
            special_item_id = VALUES(special_item_id),
            special_item_count = VALUES(special_item_count),
            is_active = VALUES(is_active)
    """;
    
    public static final String UPDATE_CALENDAR_EVENT = """
        UPDATE calendar_events 
        SET description = ?, start_date = ?, end_date = ?, reward_multiplier = ?,
            special_item_id = ?, special_item_count = ?, is_active = ?
        WHERE name = ?
    """;
    
    public static final String DELETE_CALENDAR_EVENT = """
        DELETE FROM calendar_events WHERE name = ?
    """;
    
    public static final String CLEANUP_EXPIRED_EVENTS = """
        DELETE FROM calendar_events WHERE end_date < ? AND is_active = FALSE
    """;
    
    public static final String CHECK_EVENTS_CHANGES = """
        SELECT COUNT(*) as count
        FROM calendar_events 
        WHERE updated_at > FROM_UNIXTIME(?)
    """;
    
    // ===============================
    // СТАТИСТИКА - CRUD
    // ===============================
    
    public static final String INSERT_REWARD_STATISTICS = """
        INSERT INTO reward_statistics (date, player_id, player_name, reward_group, 
                                     item_id, item_count, progressive_multiplier, event_name)
        VALUES (CURDATE(), ?, ?, ?, ?, ?, ?, ?)
    """;
    
    public static final String SELECT_STATISTICS_SUMMARY = """
        SELECT COUNT(*) as total, COUNT(DISTINCT player_id) as unique_players 
        FROM reward_statistics WHERE %s
    """;
    
    public static final String SELECT_TOP_ITEMS = """
        SELECT item_id, SUM(item_count) as total_count, COUNT(*) as times_given
        FROM reward_statistics 
        WHERE %s
        GROUP BY item_id 
        ORDER BY total_count DESC 
        LIMIT ?
    """;
    
    public static final String SELECT_TOP_PLAYERS = """
        SELECT player_id, player_name, COUNT(*) as total_rewards, SUM(item_count) as total_items
        FROM reward_statistics 
        WHERE %s AND player_name IS NOT NULL
        GROUP BY player_id, player_name
        ORDER BY total_rewards DESC 
        LIMIT ?
    """;
    
    public static final String SELECT_DAILY_STATISTICS = """
        SELECT date, COUNT(*) as rewards_count, COUNT(DISTINCT player_id) as unique_players
        FROM reward_statistics 
        WHERE date >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
        GROUP BY date
        ORDER BY date DESC
    """;
    
    public static final String SELECT_HOURLY_STATISTICS = """
        SELECT HOUR(created_at) as hour, COUNT(*) as rewards_count
        FROM reward_statistics 
        WHERE date = CURDATE()
        GROUP BY HOUR(created_at)
        ORDER BY hour
    """;
    
    public static final String CLEANUP_OLD_STATISTICS = """
        DELETE FROM reward_statistics WHERE date < DATE_SUB(CURDATE(), INTERVAL ? DAY)
    """;
    
    // ===============================
    // ПРОГРЕССИВНЫЕ МНОЖИТЕЛИ - CRUD
    // ===============================
    
    public static final String SELECT_PROGRESSIVE_MULTIPLIERS = """
        SELECT player_id, item_id, multiplier_value, last_used, increment_count
        FROM progressive_multipliers
        WHERE last_used > DATE_SUB(NOW(), INTERVAL ? DAY)
    """;
    
    public static final String INSERT_PROGRESSIVE_MULTIPLIER = """
        INSERT INTO progressive_multipliers (player_id, item_id, multiplier_value, last_used, increment_count)
        VALUES (?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            multiplier_value = VALUES(multiplier_value),
            last_used = VALUES(last_used),
            increment_count = VALUES(increment_count)
    """;
    
    public static final String SELECT_PLAYER_MULTIPLIERS = """
        SELECT item_id, multiplier_value, increment_count, last_used
        FROM progressive_multipliers
        WHERE player_id = ?
    """;
    
    public static final String DELETE_PLAYER_MULTIPLIERS = """
        DELETE FROM progressive_multipliers WHERE player_id = ?
    """;
    
    public static final String DELETE_PLAYER_ITEM_MULTIPLIER = """
        DELETE FROM progressive_multipliers WHERE player_id = ? AND item_id = ?
    """;
    
    public static final String CLEANUP_OLD_MULTIPLIERS = """
        DELETE FROM progressive_multipliers WHERE last_used < DATE_SUB(NOW(), INTERVAL ? DAY)
    """;
    
    public static final String SELECT_MULTIPLIERS_STATISTICS = """
        SELECT 
            COUNT(*) as total_count,
            COUNT(CASE WHEN last_used > DATE_SUB(NOW(), INTERVAL 1 DAY) THEN 1 END) as active_count,
            AVG(multiplier_value) as avg_multiplier,
            MAX(multiplier_value) as max_multiplier,
            SUM(increment_count) as total_increments
        FROM progressive_multipliers
    """;
    
    // ===============================
    // СИСТЕМНАЯ КОНФИГУРАЦИЯ - CRUD
    // ===============================
    
    public static final String SELECT_SYSTEM_CONFIG = """
        SELECT config_value FROM system_config WHERE config_key = ?
    """;
    
    public static final String INSERT_SYSTEM_CONFIG = """
        INSERT INTO system_config (config_key, config_value, config_type, description)
        VALUES (?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            config_value = VALUES(config_value),
            config_type = VALUES(config_type),
            description = VALUES(description)
    """;
    
    public static final String SELECT_ALL_SYSTEM_CONFIG = """
        SELECT config_key, config_value, config_type, description, last_modified
        FROM system_config
        ORDER BY config_key
    """;
    
    public static final String DELETE_SYSTEM_CONFIG = """
        DELETE FROM system_config WHERE config_key = ?
    """;
    
    // ===============================
    // СИСТЕМНЫЕ ЛОГИ (опционально)
    // ===============================
    
    public static final String INSERT_SYSTEM_LOG = """
        INSERT INTO system_logs (log_level, component, message, player_id, item_id, additional_data)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
    
    public static final String SELECT_RECENT_LOGS = """
        SELECT log_level, component, message, player_id, item_id, created_at
        FROM system_logs
        WHERE created_at > DATE_SUB(NOW(), INTERVAL ? HOUR)
        ORDER BY created_at DESC
        LIMIT ?
    """;
    
    public static final String CLEANUP_OLD_LOGS = """
        DELETE FROM system_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY)
    """;
    
    // ===============================
    // УТИЛИТАРНЫЕ ЗАПРОСЫ
    // ===============================
    
    public static final String HEALTH_CHECK_QUERY = """
        SELECT 1 as health
    """;
    
    public static final String DATABASE_SIZE_QUERY = """
        SELECT 
            table_name,
            ROUND(((data_length + index_length) / 1024 / 1024), 2) AS table_size_mb,
            table_rows
        FROM information_schema.tables 
        WHERE table_schema = DATABASE()
        AND table_name LIKE 'reward_%' OR table_name LIKE 'progressive_%' OR table_name LIKE 'calendar_%'
        ORDER BY (data_length + index_length) DESC
    """;
    
    public static final String TABLE_COUNTS_QUERY = """
        SELECT 
            (SELECT COUNT(*) FROM reward_groups WHERE is_active = TRUE) as active_groups,
            (SELECT COUNT(*) FROM reward_items WHERE is_active = TRUE) as active_items,
            (SELECT COUNT(*) FROM calendar_events WHERE is_active = TRUE) as active_events,
            (SELECT COUNT(*) FROM progressive_multipliers) as total_multipliers,
            (SELECT COUNT(*) FROM reward_statistics WHERE date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) as recent_statistics
    """;
    
    public static final String OPTIMIZE_TABLES_QUERY = """
        OPTIMIZE TABLE reward_groups, reward_items, calendar_events, 
                      reward_statistics, progressive_multipliers, system_config
    """;
    
    // ===============================
    // ФИЛЬТРЫ ДЛЯ СТАТИСТИКИ
    // ===============================
    
    public static final class StatisticsFilters {
        public static final String TODAY = "DATE(created_at) = CURDATE()";
        public static final String YESTERDAY = "DATE(created_at) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
        public static final String WEEK = "created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";
        public static final String MONTH = "created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)";
        public static final String YEAR = "created_at >= DATE_SUB(NOW(), INTERVAL 365 DAY)";
        public static final String ALL_TIME = "1=1";
        
        public static String getFilterByPeriod(String period) {
            return switch (period.toLowerCase()) {
                case "today" -> TODAY;
                case "yesterday" -> YESTERDAY;
                case "week" -> WEEK;
                case "month" -> MONTH;
                case "year" -> YEAR;
                default -> TODAY;
            };
        }
    }
    
    // ===============================
    // КОНСТАНТЫ КОНФИГУРАЦИИ
    // ===============================
    
    public static final class ConfigKeys {
        public static final String DATABASE_VERSION = "database_version";
        public static final String LAST_CLEANUP = "last_cleanup";
        public static final String LAST_OPTIMIZATION = "last_optimization";
        public static final String SYSTEM_INSTALL_DATE = "system_install_date";
        public static final String AUTO_CLEANUP_ENABLED = "auto_cleanup_enabled";
        public static final String STATISTICS_RETENTION_DAYS = "statistics_retention_days";
        public static final String MULTIPLIERS_RETENTION_DAYS = "multipliers_retention_days";
        public static final String LOG_RETENTION_DAYS = "log_retention_days";
    }
    
    // ===============================
    // КОНСТАНТЫ РАЗМЕРОВ И ЛИМИТОВ
    // ===============================
    
    public static final class Limits {
        public static final int MAX_GROUP_NAME_LENGTH = 100;
        public static final int MAX_DESCRIPTION_LENGTH = 1000;
        public static final int MAX_EVENT_NAME_LENGTH = 100;
        public static final int MAX_PLAYER_NAME_LENGTH = 100;
        public static final int MAX_CONFIG_KEY_LENGTH = 100;
        public static final int MAX_LOG_MESSAGE_LENGTH = 5000;
        
        public static final int DEFAULT_STATISTICS_RETENTION_DAYS = 90;
        public static final int DEFAULT_MULTIPLIERS_RETENTION_DAYS = 30;
        public static final int DEFAULT_LOG_RETENTION_DAYS = 7;
        
        public static final int BATCH_SIZE = 1000;
        public static final int MAX_TOP_ITEMS = 100;
        public static final int MAX_TOP_PLAYERS = 100;
    }
    
    // ===============================
    // ВЕРСИИ СХЕМЫ БД
    // ===============================
    
    public static final class SchemaVersions {
        public static final String V1_0 = "1.0";
        public static final String V2_0 = "2.0";
        public static final String CURRENT = V2_0;
        
        public static final String[] MIGRATION_SCRIPTS = {
            // Будущие скрипты миграции
        };
    }
}