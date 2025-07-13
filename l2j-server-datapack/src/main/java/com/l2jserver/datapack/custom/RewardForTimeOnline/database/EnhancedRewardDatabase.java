package com.l2jserver.datapack.custom.RewardForTimeOnline.database;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.datapack.custom.RewardForTimeOnline.cache.CacheManager;
import com.l2jserver.datapack.custom.RewardForTimeOnline.cache.PlayerCacheData;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Улучшенная система работы с базой данных с кешированием
 * @author Dafna
 */
public class EnhancedRewardDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(EnhancedRewardDatabase.class);
    
    private final CacheManager cacheManager;
    private final ExecutorService asyncExecutor;
    private volatile boolean isInitialized = false;
    private DatabaseStatus status = DatabaseStatus.OFFLINE;
    
    public EnhancedRewardDatabase(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.asyncExecutor = Executors.newFixedThreadPool(3, r -> {
            Thread t = new Thread(r, "RewardDB-Async");
            t.setDaemon(true);
            return t;
        });
    }
    
    /**
     * Инициализация базы данных с созданием всех необходимых таблиц
     */
    public boolean initialize() {
        if (isInitialized) {
            LOG.warn("Database already initialized");
            return true;
        }
        
        LOG.info("Initializing enhanced reward database...");
        
        try {
            // Создаем таблицы
            createTables();
            
            // Создаем индексы для оптимизации
            createOptimizationIndexes();
            
            // Проверяем здоровье базы данных
            if (performHealthCheck()) {
                status = DatabaseStatus.ONLINE;
                isInitialized = true;
                LOG.info("Enhanced reward database initialized successfully");
                return true;
            } else {
                LOG.error("Database health check failed");
                return false;
            }
            
        } catch (Exception e) {
            LOG.error("Failed to initialize database", e);
            status = DatabaseStatus.ERROR;
            return false;
        }
    }
    
    /**
     * Создание оптимизационных индексов
     */
    private void createOptimizationIndexes() {
        String[] indexes = {
            "CREATE INDEX IF NOT EXISTS idx_player_rewards_composite ON player_rewards (player_id, item_id, last_received)",
            "CREATE INDEX IF NOT EXISTS idx_calendar_events_time_range ON calendar_events (start_date, end_date, is_active)",
            "CREATE INDEX IF NOT EXISTS idx_reward_statistics_date ON reward_statistics (date_created)",
            "CREATE INDEX IF NOT EXISTS idx_reward_groups_priority ON reward_groups (priority DESC, is_active)",
            "CREATE INDEX IF NOT EXISTS idx_reward_items_group_active ON reward_items (group_id, is_active, item_id)"
        };
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            for (String indexSql : indexes) {
                try (PreparedStatement ps = conn.prepareStatement(indexSql)) {
                    ps.executeUpdate();
                    LOG.debug("Created optimization index");
                } catch (SQLException e) {
                    // Индекс уже существует - это нормально
                    LOG.trace("Index creation skipped (already exists): {}", e.getMessage());
                }
            }
            LOG.info("Optimization indexes created successfully");
        } catch (SQLException e) {
            LOG.warn("Failed to create some optimization indexes", e);
        }
    }
    
    /**
     * Загрузка групп наград с кешированием
     */
    public List<RewardGroup> loadRewardGroups() {
        String cacheKey = "all_reward_groups";
        
        // Попытка получить из кеша
        Optional<List<RewardGroup>> cached = cacheManager.getRewardGroups(cacheKey);
        if (cached.isPresent()) {
            LOG.trace("Loaded {} reward groups from cache", cached.get().size());
            return cached.get();
        }
        
        // Загружаем из базы данных
        List<RewardGroup> groups = loadRewardGroupsFromDatabase();
        
        // Сохраняем в кеш
        if (!groups.isEmpty()) {
            cacheManager.putRewardGroups(cacheKey, groups);
        }
        
        LOG.debug("Loaded {} reward groups from database", groups.size());
        return groups;
    }
    
    /**
     * Оптимизированная загрузка групп наград из базы данных
     */
    private List<RewardGroup> loadRewardGroupsFromDatabase() {
        List<RewardGroup> groups = new ArrayList<>();
        
        String sql = """
            SELECT rg.id, rg.name, rg.description, rg.access_levels, rg.priority,
                   ri.id as item_id, ri.item_id as game_item_id, ri.base_count,
                   ri.time_interval, ri.save_to_database, ri.once_only, ri.progressive,
                   ri.required_event, ri.allowed_days, ri.min_level, ri.max_level
            FROM reward_groups rg
            LEFT JOIN reward_items ri ON rg.id = ri.group_id AND ri.is_active = TRUE
            WHERE rg.is_active = TRUE
            ORDER BY rg.priority DESC, rg.name, ri.id
            """;
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            Map<Integer, RewardGroup> groupsMap = new HashMap<>();
            
            while (rs.next()) {
                int groupId = rs.getInt("id");
                RewardGroup group = groupsMap.get(groupId);
                
                if (group == null) {
                    // Создаем новую группу
                    group = new RewardGroup();
                    group.setId(groupId);
                    group.setName(rs.getString("name"));
                    group.setDescription(rs.getString("description"));
                    group.setPriority(rs.getInt("priority"));
                    
                    // Парсим уровни доступа
                    String accessLevelsStr = rs.getString("access_levels");
                    if (accessLevelsStr != null && !accessLevelsStr.trim().isEmpty()) {
                        String[] levels = accessLevelsStr.split(",");
                        List<Integer> accessLevels = new ArrayList<>();
                        for (String level : levels) {
                            try {
                                accessLevels.add(Integer.parseInt(level.trim()));
                            } catch (NumberFormatException e) {
                                LOG.warn("Invalid access level format: {}", level);
                            }
                        }
                        group.setAccessLevels(accessLevels);
                    }
                    
                    groupsMap.put(groupId, group);
                    groups.add(group);
                }
                
                // Добавляем предмет в группу если он существует
                if (rs.getObject("item_id") != null) {
                    ItemReward reward = new ItemReward();
                    reward.setItemId(rs.getInt("game_item_id"));
                    reward.setBaseCount(rs.getLong("base_count"));
                    reward.setTimeInterval(rs.getLong("time_interval"));
                    reward.setSaveToDatabase(rs.getBoolean("save_to_database"));
                    reward.setOnceOnly(rs.getBoolean("once_only"));
                    reward.setProgressive(rs.getBoolean("progressive"));
                    reward.setRequiredEvent(rs.getString("required_event"));
                    reward.setMinLevel(rs.getInt("min_level"));
                    reward.setMaxLevel(rs.getInt("max_level"));
                    
                    // Парсим разрешенные дни
                    String allowedDaysStr = rs.getString("allowed_days");
                    if (allowedDaysStr != null && !allowedDaysStr.trim().isEmpty()) {
                        String[] days = allowedDaysStr.split(",");
                        Set<String> allowedDays = new HashSet<>();
                        for (String day : days) {
                            allowedDays.add(day.trim().toUpperCase());
                        }
                        reward.setAllowedDays(allowedDays);
                    }
                    
                    group.addReward(reward);
                }
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to load reward groups from database", e);
        }
        
        return groups;
    }
    
    /**
     * Сохранение данных игрока с кешированием
     */
    public void savePlayerRewardData(int playerId, int itemId, long lastReceived) {
        // Обновляем в кеше
        updatePlayerCacheData(playerId, itemId, lastReceived);
        
        // Асинхронно сохраняем в базу данных
        CompletableFuture.runAsync(() -> {
            savePlayerRewardDataToDatabase(playerId, itemId, lastReceived);
        }, asyncExecutor).exceptionally(throwable -> {
            LOG.error("Failed to save player reward data asynchronously", throwable);
            return null;
        });
    }
    
    /**
     * Обновление кешированных данных игрока
     */
    private void updatePlayerCacheData(int playerId, int itemId, long lastReceived) {
        Optional<PlayerCacheData> cached = cacheManager.getPlayerData(playerId);
        
        Map<Integer, Long> rewardTimes;
        Set<Integer> onceOnlyRewards;
        double progressiveMultiplier;
        
        if (cached.isPresent()) {
            PlayerCacheData data = cached.get();
            rewardTimes = data.getLastRewardTimes();
            onceOnlyRewards = data.getReceivedOnceOnlyRewards();
            progressiveMultiplier = data.getProgressiveMultiplier();
        } else {
            rewardTimes = new HashMap<>();
            onceOnlyRewards = new HashSet<>();
            progressiveMultiplier = 1.0;
        }
        
        rewardTimes.put(itemId, lastReceived);
        
        PlayerCacheData updatedData = new PlayerCacheData(
            playerId, rewardTimes, onceOnlyRewards, progressiveMultiplier);
        cacheManager.putPlayerData(playerId, updatedData);
    }
    
    /**
     * Синхронное сохранение данных игрока в базу данных
     */
    private void savePlayerRewardDataToDatabase(int playerId, int itemId, long lastReceived) {
        String sql = """
            INSERT INTO player_rewards (player_id, item_id, last_received, times_received)
            VALUES (?, ?, ?, 1)
            ON DUPLICATE KEY UPDATE
            last_received = VALUES(last_received),
            times_received = times_received + 1
            """;
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, playerId);
            ps.setInt(2, itemId);
            ps.setLong(3, lastReceived);
            
            ps.executeUpdate();
            LOG.trace("Saved player reward data: player={}, item={}", playerId, itemId);
            
        } catch (SQLException e) {
            LOG.error("Failed to save player reward data to database", e);
        }
    }
    
    /**
     * Загрузка данных игрока с кешированием
     */
    public PlayerCacheData loadPlayerData(int playerId) {
        // Проверяем кеш
        Optional<PlayerCacheData> cached = cacheManager.getPlayerData(playerId);
        if (cached.isPresent()) {
            LOG.trace("Loaded player data from cache: {}", playerId);
            return cached.get();
        }
        
        // Загружаем из базы данных
        PlayerCacheData data = loadPlayerDataFromDatabase(playerId);
        
        // Сохраняем в кеш
        if (data != null) {
            cacheManager.putPlayerData(playerId, data);
        }
        
        LOG.debug("Loaded player data from database: {}", playerId);
        return data;
    }
    
    /**
     * Загрузка данных игрока из базы данных
     */
    private PlayerCacheData loadPlayerDataFromDatabase(int playerId) {
        Map<Integer, Long> rewardTimes = new HashMap<>();
        Set<Integer> onceOnlyRewards = new HashSet<>();
        
        String sql = """
            SELECT pr.item_id, pr.last_received, ri.once_only
            FROM player_rewards pr
            LEFT JOIN reward_items ri ON pr.item_id = ri.item_id
            WHERE pr.player_id = ?
            """;
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, playerId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    long lastReceived = rs.getLong("last_received");
                    boolean onceOnly = rs.getBoolean("once_only");
                    
                    rewardTimes.put(itemId, lastReceived);
                    
                    if (onceOnly) {
                        onceOnlyRewards.add(itemId);
                    }
                }
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to load player data from database", e);
            return null;
        }
        
        // Загружаем прогрессивный множитель
        double progressiveMultiplier = loadPlayerProgressiveMultiplier(playerId);
        
        return new PlayerCacheData(playerId, rewardTimes, onceOnlyRewards, progressiveMultiplier);
    }
    
    /**
     * Загрузка прогрессивного множителя игрока
     */
    private double loadPlayerProgressiveMultiplier(int playerId) {
        String sql = "SELECT progressive_multiplier FROM player_progressive_data WHERE player_id = ?";
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, playerId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("progressive_multiplier");
                }
            }
            
        } catch (SQLException e) {
            LOG.trace("No progressive multiplier found for player {} (this is normal for new players)", playerId);
        }
        
        return 1.0; // Значение по умолчанию
    }
    
    /**
     * Батчевое сохранение статистики
     */
    public void saveStatisticsBatch(List<StatisticEvent> events) {
        if (events.isEmpty()) return;
        
        String sql = """
            INSERT INTO reward_statistics 
            (player_id, item_id, group_name, execution_time, date_created)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (StatisticEvent event : events) {
                    ps.setInt(1, event.getPlayerId());
                    ps.setInt(2, event.getItemId());
                    ps.setString(3, event.getGroupName());
                    ps.setLong(4, event.getExecutionTime());
                    ps.setTimestamp(5, Timestamp.valueOf(event.getDateTime()));
                    ps.addBatch();
                }
                
                ps.executeBatch();
                conn.commit();
                
                LOG.debug("Saved statistics batch: {} events", events.size());
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            LOG.error("Failed to save statistics batch", e);
        }
    }
    
    /**
     * Создание всех необходимых таблиц
     */
    private void createTables() throws SQLException {
        String[] tables = {
            DatabaseConstants.CREATE_REWARD_GROUPS_TABLE,
            DatabaseConstants.CREATE_REWARD_ITEMS_TABLE,
            DatabaseConstants.CREATE_CALENDAR_EVENTS_TABLE,
            DatabaseConstants.CREATE_PLAYER_REWARDS_TABLE,
            DatabaseConstants.CREATE_PLAYER_PROGRESSIVE_DATA_TABLE,
            DatabaseConstants.CREATE_REWARD_STATISTICS_TABLE,
            DatabaseConstants.CREATE_SYSTEM_CONFIG_TABLE
        };
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            for (String tableSql : tables) {
                try (PreparedStatement ps = conn.prepareStatement(tableSql)) {
                    ps.executeUpdate();
                }
            }
            LOG.info("All database tables created successfully");
        }
    }
    
    /**
     * Проверка здоровья базы данных
     */
    private boolean performHealthCheck() {
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            
            return rs.next() && rs.getInt(1) == 1;
            
        } catch (SQLException e) {
            LOG.error("Database health check failed", e);
            return false;
        }
    }
    
    /**
     * Очистка старых статистических данных
     */
    public void cleanupOldStatistics(int retentionDays) {
        String sql = "DELETE FROM reward_statistics WHERE date_created < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        CompletableFuture.runAsync(() -> {
            try (Connection conn = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setInt(1, retentionDays);
                int deleted = ps.executeUpdate();
                
                if (deleted > 0) {
                    LOG.info("Cleaned up {} old statistics records", deleted);
                }
                
            } catch (SQLException e) {
                LOG.error("Failed to cleanup old statistics", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Получение статуса базы данных
     */
    public DatabaseStatus getStatus() {
        return status;
    }
    
    /**
     * Проверка инициализации
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * Получение менеджера кеша
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }
    
    /**
     * Отключение системы
     */
    public void shutdown() {
        LOG.info("Shutting down enhanced reward database...");
        
        asyncExecutor.shutdown();
        try {
            if (!asyncExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            asyncExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        isInitialized = false;
        status = DatabaseStatus.OFFLINE;
        
        LOG.info("Enhanced reward database shutdown completed");
    }
}

/**
 * Событие статистики для батчевого сохранения
 */
class StatisticEvent {
    private final int playerId;
    private final int itemId;
    private final String groupName;
    private final long executionTime;
    private final LocalDateTime dateTime;
    
    public StatisticEvent(int playerId, int itemId, String groupName, long executionTime) {
        this.playerId = playerId;
        this.itemId = itemId;
        this.groupName = groupName;
        this.executionTime = executionTime;
        this.dateTime = LocalDateTime.now();
    }
    
    // Геттеры
    public int getPlayerId() { return playerId; }
    public int getItemId() { return itemId; }
    public String getGroupName() { return groupName; }
    public long getExecutionTime() { return executionTime; }
    public LocalDateTime getDateTime() { return dateTime; }
}

/**
 * Статус базы данных
 */
enum DatabaseStatus {
    OFFLINE, ONLINE, ERROR, MAINTENANCE
}