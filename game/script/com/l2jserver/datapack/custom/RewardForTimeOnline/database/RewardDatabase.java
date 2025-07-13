package com.l2jserver.datapack.custom.RewardForTimeOnline.database;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.*;
import com.l2jserver.datapack.custom.RewardForTimeOnline.systems.ProgressiveRewardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Система работы с базой данных для наград
 * Обеспечивает сохранение и загрузку всех данных системы
 * @author Dafna
 */
public class RewardDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(RewardDatabase.class);
    
    // SQL запросы для создания таблиц
    private static final String CREATE_REWARD_GROUPS_TABLE = """
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
            INDEX idx_active_priority (is_active, priority)
        )
    """;
    
    private static final String CREATE_REWARD_ITEMS_TABLE = """
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
            INDEX idx_item_active (item_id, is_active)
        )
    """;
    
    private static final String CREATE_CALENDAR_EVENTS_TABLE = """
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
            INDEX idx_active_dates (is_active, start_date, end_date)
        )
    """;
    
    private static final String CREATE_STATISTICS_TABLE = """
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
            INDEX idx_player_stats (player_id, date)
        )
    """;
    
    private static final String CREATE_PROGRESSIVE_MULTIPLIERS_TABLE = """
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
            INDEX idx_last_used (last_used)
        )
    """;
    
    private static final String CREATE_SYSTEM_CONFIG_TABLE = """
        CREATE TABLE IF NOT EXISTS system_config (
            id INT AUTO_INCREMENT PRIMARY KEY,
            config_key VARCHAR(100) NOT NULL UNIQUE,
            config_value TEXT,
            config_type VARCHAR(50) DEFAULT 'STRING',
            description TEXT,
            last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_key (config_key)
        )
    """;
    
    // Кэш для отслеживания изменений
    private volatile long lastConfigCheck = 0;
    private volatile long lastEventsCheck = 0;
    private boolean isInitialized = false;
    private DatabaseStatus status = DatabaseStatus.UNKNOWN;
    
    public enum DatabaseStatus {
        UNKNOWN, HEALTHY, DEGRADED, OFFLINE
    }
    
    /**
     * Инициализация базы данных
     */
    public boolean initialize() {
        if (isInitialized) {
            LOG.warn("RewardDatabase already initialized");
            return true;
        }
        
        LOG.info("Initializing RewardDatabase...");
        
        try {
            // Создаем таблицы
            if (!createTables()) {
                status = DatabaseStatus.OFFLINE;
                return false;
            }
            
            // Проверяем здоровье БД
            if (!performHealthCheck()) {
                status = DatabaseStatus.DEGRADED;
                LOG.warn("Database health check failed, but continuing with degraded mode");
            } else {
                status = DatabaseStatus.HEALTHY;
            }
            
            // Инициализируем системную конфигурацию
            initializeSystemConfig();
            
            isInitialized = true;
            LOG.info("RewardDatabase initialized successfully with status: {}", status);
            return true;
            
        } catch (Exception e) {
            LOG.error("Failed to initialize RewardDatabase", e);
            status = DatabaseStatus.OFFLINE;
            return false;
        }
    }
    
    /**
     * Создает все необходимые таблицы
     */
    private boolean createTables() {
        String[] createQueries = {
            CREATE_REWARD_GROUPS_TABLE,
            CREATE_REWARD_ITEMS_TABLE,
            CREATE_CALENDAR_EVENTS_TABLE,
            CREATE_STATISTICS_TABLE,
            CREATE_PROGRESSIVE_MULTIPLIERS_TABLE,
            CREATE_SYSTEM_CONFIG_TABLE
        };
        
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            con.setAutoCommit(false);
            
            try (Statement stmt = con.createStatement()) {
                for (String query : createQueries) {
                    stmt.execute(query);
                }
                con.commit();
                
                LOG.info("Database tables created/verified successfully");
                return true;
                
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to create database tables", e);
            return false;
        }
    }
    
    /**
     * Выполняет проверку здоровья БД
     */
    private boolean performHealthCheck() {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            // Простой запрос для проверки соединения
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Database health check failed", e);
            return false;
        }
    }
    
    /**
     * Инициализирует системную конфигурацию
     */
    private void initializeSystemConfig() {
        try {
            setSystemConfig("database_version", "2.0", "Database schema version");
            setSystemConfig("last_cleanup", String.valueOf(System.currentTimeMillis()), "Last cleanup timestamp");
            
        } catch (Exception e) {
            LOG.warn("Failed to initialize system config: {}", e.getMessage());
        }
    }
    
    /**
     * Загружает группы наград из базы данных
     */
    public List<RewardGroup> loadRewardGroups() {
        List<RewardGroup> groups = new ArrayList<>();
        
        String query = """
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
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RewardGroup group = parseRewardGroupFromResultSet(rs);
                if (group != null) {
                    groups.add(group);
                }
            }
            
            LOG.debug("Loaded {} reward groups from database", groups.size());
            
        } catch (SQLException e) {
            LOG.error("Failed to load reward groups from database", e);
        }
        
        return groups;
    }
    
    /**
     * Парсит группу наград из ResultSet
     */
    private RewardGroup parseRewardGroupFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String description = rs.getString("description");
        String accessLevelsStr = rs.getString("access_levels");
        boolean isActive = rs.getBoolean("is_active");
        int priority = rs.getInt("priority");
        String itemsStr = rs.getString("items");
        
        Set<String> accessLevels = new HashSet<>();
        if (accessLevelsStr != null && !accessLevelsStr.trim().isEmpty()) {
            accessLevels.addAll(Arrays.asList(accessLevelsStr.split(",")));
        }
        
        List<ItemReward> rewards = parseItemRewardsFromString(itemsStr);
        
        return RewardGroup.builder()
            .name(name)
            .description(description)
            .addRewards(rewards)
            .allowAccessLevels(accessLevels.toArray(new String[0]))
            .active(isActive)
            .priority(priority)
            .build();
    }
    
    /**
     * Парсит награды из строки
     */
    private List<ItemReward> parseItemRewardsFromString(String itemsStr) {
        List<ItemReward> rewards = new ArrayList<>();
        
        if (itemsStr == null || itemsStr.trim().isEmpty()) {
            return rewards;
        }
        
        String[] items = itemsStr.split("\\|");
        for (String item : items) {
            String[] parts = item.split(":");
            if (parts.length >= 10) {
                try {
                    int itemId = Integer.parseInt(parts[0]);
                    long baseCount = Long.parseLong(parts[1]);
                    long timeInterval = Long.parseLong(parts[2]);
                    boolean saveToDatabase = Boolean.parseBoolean(parts[3]);
                    boolean onceOnly = Boolean.parseBoolean(parts[4]);
                    boolean progressive = Boolean.parseBoolean(parts[5]);
                    String requiredEvent = parts[6].isEmpty() ? null : parts[6];
                    
                    DayOfWeek[] allowedDays = null;
                    if (!parts[7].isEmpty()) {
                        String[] dayNames = parts[7].split(",");
                        allowedDays = Arrays.stream(dayNames)
                            .map(DayOfWeek::valueOf)
                            .toArray(DayOfWeek[]::new);
                    }
                    
                    int minLevel = Integer.parseInt(parts[8]);
                    int maxLevel = Integer.parseInt(parts[9]);
                    
                    ItemReward reward = new ItemReward(itemId, baseCount, timeInterval,
                        saveToDatabase, onceOnly, progressive, requiredEvent, 
                        allowedDays, minLevel, maxLevel);
                    
                    rewards.add(reward);
                    
                } catch (Exception e) {
                    LOG.warn("Failed to parse item reward: {}", item, e);
                }
            }
        }
        
        return rewards;
    }
    
    /**
     * Сохраняет группы наград в базу данных
     */
    public boolean saveRewardGroups(List<RewardGroup> groups) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            con.setAutoCommit(false);
            
            try {
                // Очищаем старые данные
                try (Statement stmt = con.createStatement()) {
                    stmt.execute("DELETE FROM reward_items");
                    stmt.execute("DELETE FROM reward_groups");
                }
                
                // Сохраняем новые группы
                for (RewardGroup group : groups) {
                    saveRewardGroup(con, group);
                }
                
                con.commit();
                LOG.info("Saved {} reward groups to database", groups.size());
                return true;
                
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to save reward groups to database", e);
            return false;
        }
    }
    
    /**
     * Сохраняет одну группу наград
     */
    private void saveRewardGroup(Connection con, RewardGroup group) throws SQLException {
        // Сохраняем группу
        String groupQuery = """
            INSERT INTO reward_groups (name, description, access_levels, is_active, priority)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        int groupId;
        try (PreparedStatement stmt = con.prepareStatement(groupQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());
            stmt.setString(3, String.join(",", group.getAllowedAccessLevels()));
            stmt.setBoolean(4, group.isActive());
            stmt.setInt(5, group.getPriority());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    groupId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated group ID");
                }
            }
        }
        
        // Сохраняем награды группы
        String itemQuery = """
            INSERT INTO reward_items (group_id, item_id, base_count, time_interval, 
                                    save_to_database, once_only, progressive, required_event,
                                    allowed_days, min_level, max_level, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = con.prepareStatement(itemQuery)) {
            for (ItemReward reward : group.getRewards()) {
                stmt.setInt(1, groupId);
                stmt.setInt(2, reward.getItemId());
                stmt.setLong(3, reward.getBaseCount());
                stmt.setLong(4, reward.getTimeInterval());
                stmt.setBoolean(5, reward.isSaveToDatabase());
                stmt.setBoolean(6, reward.isOnceOnly());
                stmt.setBoolean(7, reward.isProgressive());
                stmt.setString(8, reward.getRequiredEvent());
                
                // Конвертируем дни недели в строку
                DayOfWeek[] allowedDays = reward.getAllowedDays();
                String daysStr = null;
                if (allowedDays != null && allowedDays.length > 0) {
                    daysStr = Arrays.stream(allowedDays)
                        .map(DayOfWeek::name)
                        .reduce((a, b) -> a + "," + b)
                        .orElse(null);
                }
                stmt.setString(9, daysStr);
                
                stmt.setInt(10, reward.getMinLevel());
                stmt.setInt(11, reward.getMaxLevel());
                stmt.setBoolean(12, true);
                
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    /**
     * Загружает календарные события
     */
    public List<CalendarEvent> loadCalendarEvents() {
        List<CalendarEvent> events = new ArrayList<>();
        
        String query = """
            SELECT name, description, start_date, end_date, reward_multiplier,
                   special_item_id, special_item_count, is_active
            FROM calendar_events
            WHERE is_active = TRUE AND end_date > NOW()
            ORDER BY start_date
        """;
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
                double rewardMultiplier = rs.getDouble("reward_multiplier");
                Integer specialItemId = rs.getObject("special_item_id", Integer.class);
                Long specialItemCount = rs.getObject("special_item_count", Long.class);
                boolean isActive = rs.getBoolean("is_active");
                
                CalendarEvent.Builder builder = CalendarEvent.builder()
                    .name(name)
                    .description(description)
                    .startDate(startDate)
                    .endDate(endDate)
                    .rewardMultiplier(rewardMultiplier)
                    .active(isActive);
                
                if (specialItemId != null && specialItemCount != null) {
                    builder.specialReward(specialItemId, specialItemCount);
                }
                
                events.add(builder.build());
            }
            
            LOG.debug("Loaded {} calendar events from database", events.size());
            
        } catch (SQLException e) {
            LOG.error("Failed to load calendar events from database", e);
        }
        
        return events;
    }
    
    /**
     * Сохраняет календарные события
     */
    public boolean saveCalendarEvents(List<CalendarEvent> events) {
        String query = """
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
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            for (CalendarEvent event : events) {
                stmt.setString(1, event.getName());
                stmt.setString(2, event.getDescription());
                stmt.setTimestamp(3, Timestamp.valueOf(event.getStartDate()));
                stmt.setTimestamp(4, Timestamp.valueOf(event.getEndDate()));
                stmt.setDouble(5, event.getRewardMultiplier());
                stmt.setObject(6, event.getSpecialItemId());
                stmt.setObject(7, event.getSpecialItemCount());
                stmt.setBoolean(8, event.isActive());
                
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            int savedCount = Arrays.stream(results).sum();
            
            LOG.debug("Saved {} calendar events to database", savedCount);
            return true;
            
        } catch (SQLException e) {
            LOG.error("Failed to save calendar events to database", e);
            return false;
        }
    }
    
    /**
     * Сохраняет одно календарное событие
     */
    public boolean saveCalendarEvent(CalendarEvent event) {
        return saveCalendarEvents(List.of(event));
    }
    
    /**
     * Удаляет календарное событие
     */
    public boolean removeCalendarEvent(String eventName) {
        String query = "DELETE FROM calendar_events WHERE name = ?";
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setString(1, eventName);
            int affected = stmt.executeUpdate();
            
            LOG.debug("Removed calendar event '{}', affected rows: {}", eventName, affected);
            return affected > 0;
            
        } catch (SQLException e) {
            LOG.error("Failed to remove calendar event '{}'", eventName, e);
            return false;
        }
    }
    
    /**
     * Сохраняет статистику награды
     */
    public void saveStatistics(int playerId, String playerName, String rewardGroup, 
                             int itemId, long itemCount, double progressiveMultiplier, String eventName) {
        String query = """
            INSERT INTO reward_statistics (date, player_id, player_name, reward_group, 
                                         item_id, item_count, progressive_multiplier, event_name)
            VALUES (CURDATE(), ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setInt(1, playerId);
            stmt.setString(2, playerName);
            stmt.setString(3, rewardGroup);
            stmt.setInt(4, itemId);
            stmt.setLong(5, itemCount);
            stmt.setDouble(6, progressiveMultiplier);
            stmt.setString(7, eventName);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            LOG.debug("Failed to save statistics: {}", e.getMessage());
        }
    }
    
    /**
     * Загружает прогрессивные множители
     */
    public Map<String, ProgressiveRewardManager.ProgressiveMultiplier> loadProgressiveMultipliers() {
        Map<String, ProgressiveRewardManager.ProgressiveMultiplier> multipliers = new HashMap<>();
        
        String query = """
            SELECT player_id, item_id, multiplier_value, last_used, increment_count
            FROM progressive_multipliers
            WHERE last_used > DATE_SUB(NOW(), INTERVAL 30 DAY)
        """;
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int playerId = rs.getInt("player_id");
                int itemId = rs.getInt("item_id");
                double value = rs.getDouble("multiplier_value");
                long lastUsed = rs.getTimestamp("last_used").getTime();
                int incrementCount = rs.getInt("increment_count");
                
                String key = playerId + "_" + itemId;
                // Здесь должен быть создан объект ProgressiveMultiplier
                // multipliers.put(key, new ProgressiveMultiplier(...));
            }
            
            LOG.debug("Loaded {} progressive multipliers from database", multipliers.size());
            
        } catch (SQLException e) {
            LOG.error("Failed to load progressive multipliers from database", e);
        }
        
        return multipliers;
    }
    
    /**
     * Сохраняет прогрессивные множители
     */
    public int saveProgressiveMultipliers(Map<String, ?> multipliers) {
        if (multipliers.isEmpty()) {
            return 0;
        }
        
        String query = """
            INSERT INTO progressive_multipliers (player_id, item_id, multiplier_value, last_used, increment_count)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                multiplier_value = VALUES(multiplier_value),
                last_used = VALUES(last_used),
                increment_count = VALUES(increment_count)
        """;
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            int batchCount = 0;
            for (Map.Entry<String, ?> entry : multipliers.entrySet()) {
                String[] keyParts = entry.getKey().split("_");
                if (keyParts.length != 2) continue;
                
                try {
                    int playerId = Integer.parseInt(keyParts[0]);
                    int itemId = Integer.parseInt(keyParts[1]);
                    
                    // Здесь нужно получить данные из объекта ProgressiveMultiplier
                    // Object multiplier = entry.getValue();
                    // stmt.setInt(1, playerId);
                    // stmt.setInt(2, itemId);
                    // stmt.setDouble(3, multiplier.getValue());
                    // stmt.setTimestamp(4, new Timestamp(multiplier.getLastUsed()));
                    // stmt.setInt(5, multiplier.getIncrementCount());
                    
                    stmt.addBatch();
                    batchCount++;
                    
                } catch (NumberFormatException e) {
                    LOG.warn("Invalid multiplier key format: {}", entry.getKey());
                }
            }
            
            if (batchCount > 0) {
                stmt.executeBatch();
            }
            
            LOG.debug("Saved {} progressive multipliers to database", batchCount);
            return batchCount;
            
        } catch (SQLException e) {
            LOG.error("Failed to save progressive multipliers to database", e);
            return 0;
        }
    }
    
    /**
     * Проверяет изменения конфигурации
     */
    public boolean hasConfigurationChanged() {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            String query = """
                SELECT MAX(updated_at) as max_update 
                FROM reward_groups 
                WHERE updated_at > FROM_UNIXTIME(?)
            """;
            
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setLong(1, lastConfigCheck / 1000);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Timestamp maxUpdate = rs.getTimestamp("max_update");
                        if (maxUpdate != null) {
                            lastConfigCheck = System.currentTimeMillis();
                            return true;
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            LOG.warn("Error checking configuration changes: {}", e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Проверяет изменения календарных событий
     */
    public boolean hasCalendarEventsChanged(long sinceTimestamp) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            String query = """
                SELECT COUNT(*) as count
                FROM calendar_events 
                WHERE updated_at > FROM_UNIXTIME(?)
            """;
            
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setLong(1, sinceTimestamp / 1000);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("count") > 0;
                    }
                }
            }
            
        } catch (SQLException e) {
            LOG.warn("Error checking calendar events changes: {}", e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Очищает устаревшие события
     */
    public void cleanupExpiredEvents(LocalDateTime cutoffDate) {
        String query = "DELETE FROM calendar_events WHERE end_date < ? AND is_active = FALSE";
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(cutoffDate));
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                LOG.info("Cleaned up {} expired calendar events", affected);
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to cleanup expired events", e);
        }
    }
    
    /**
     * Выполняет обслуживание базы данных
     */
    public void performMaintenance() {
        try {
            // Очищаем старую статистику (старше 90 дней)
            cleanupOldStatistics(90);
            
            // Очищаем старые прогрессивные множители (старше 30 дней неактивности)
            cleanupOldMultipliers(30);
            
            // Обновляем системную конфигурацию
            setSystemConfig("last_cleanup", String.valueOf(System.currentTimeMillis()), "Last cleanup timestamp");
            
        } catch (Exception e) {
            LOG.error("Error during database maintenance", e);
        }
    }
    
    /**
     * Очищает старую статистику
     */
    private void cleanupOldStatistics(int daysOld) {
        String query = "DELETE FROM reward_statistics WHERE date < DATE_SUB(CURDATE(), INTERVAL ? DAY)";
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setInt(1, daysOld);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                LOG.info("Cleaned up {} old statistics records", affected);
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to cleanup old statistics", e);
        }
    }
    
    /**
     * Очищает старые множители
     */
    private void cleanupOldMultipliers(int daysInactive) {
        String query = "DELETE FROM progressive_multipliers WHERE last_used < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setInt(1, daysInactive);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                LOG.info("Cleaned up {} old progressive multipliers", affected);
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to cleanup old multipliers", e);
        }
    }
    
    /**
     * Устанавливает системную конфигурацию
     */
    private void setSystemConfig(String key, String value, String description) {
        String query = """
            INSERT INTO system_config (config_key, config_value, description)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
                config_value = VALUES(config_value),
                description = VALUES(description)
        """;
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, description);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            LOG.debug("Failed to set system config {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Получает статистику базы данных
     */
    public Map<String, Object> getStatistics(String period) {
        Map<String, Object> stats = new HashMap<>();
        
        String dateFilter = switch (period.toLowerCase()) {
            case "today" -> "DATE(created_at) = CURDATE()";
            case "week" -> "created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";
            case "month" -> "created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)";
            default -> "created_at >= DATE_SUB(NOW(), INTERVAL 1 DAY)";
        };
        
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            // Общая статистика
            String totalQuery = """
                SELECT COUNT(*) as total, COUNT(DISTINCT player_id) as unique_players 
                FROM reward_statistics WHERE %s
            """.formatted(dateFilter);
            
            try (PreparedStatement stmt = con.prepareStatement(totalQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalRewards", rs.getInt("total"));
                    stats.put("uniquePlayers", rs.getInt("unique_players"));
                }
            }
            
            // Топ предметов
            String topItemsQuery = """
                SELECT item_id, SUM(item_count) as total_count, COUNT(*) as times_given
                FROM reward_statistics 
                WHERE %s
                GROUP BY item_id 
                ORDER BY total_count DESC 
                LIMIT 10
            """.formatted(dateFilter);
            
            try (PreparedStatement stmt = con.prepareStatement(topItemsQuery);
                 ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> topItems = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", rs.getInt("item_id"));
                    item.put("totalCount", rs.getLong("total_count"));
                    item.put("timesGiven", rs.getInt("times_given"));
                    topItems.add(item);
                }
                stats.put("topItems", topItems);
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to get statistics", e);
        }
        
        return stats;
    }
    
    /**
     * Проверяет здоровье базы данных
     */
    public boolean isHealthy() {
        return performHealthCheck();
    }
    
    /**
     * Получает статус базы данных
     */
    public DatabaseStatus getStatus() {
        return status;
    }
    
    /**
     * Отключение системы
     */
    public void shutdown() {
        LOG.info("Shutting down RewardDatabase...");
        isInitialized = false;
        status = DatabaseStatus.OFFLINE;
        LOG.info("RewardDatabase shutdown completed");
    }
}