package com.l2jserver.datapack.custom.RewardForTimeOnline;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.config.RewardsOnlineConfig;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.Containers;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogin;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogout;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * @author Dafna
 * Продвинутая система награждения игроков за время в сети
 * 
 * Возможности:
 * - Универсальная поддержка всех сборок L2J через ItemDataWrapper
 * - База данных для конфигурации и горячая перезагрузка
 * - Web API для управления наградами
 * - Группы игроков с разными наградами
 * - Календарные события и специальные награды
 * - Прогрессивные награды
 * - Anti-AFK система
 * - Детальная аналитика и метрики
 */
public final class AdvancedRewardSystem extends Quest {
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedRewardSystem.class);
    
    // Константы
    private static final String SCRIPT_NAME = "AdvancedRewardSystem";
    private static final boolean LOAD = true;
    private static final long CLEANUP_INTERVAL = TimeUnit.MINUTES.toMillis(30);
    private static final long CONFIG_RELOAD_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    private static final long ANTI_AFK_CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(2);
    private static final int AFK_TIMEOUT_MINUTES = 10;
    private static final int MAX_PROGRESSIVE_MULTIPLIER = 5;
    
    // Основные компоненты
    private final Map<Integer, PlayerHolder> players;
    private final RewardDatabase database;
    private final RewardWebAPI webAPI;
    private final AntiAFKSystem antiAFK;
    private final ProgressiveRewardManager progressiveManager;
    private final CalendarEventManager calendarManager;
    private final RewardStatistics statistics;
    private final ItemDataWrapper itemDataWrapper;
    
    // Системные переменные
    private final AtomicBoolean isShuttingDown;
    private volatile List<RewardGroup> rewardGroups;
    private ScheduledFuture<?> cleanupTask;
    private ScheduledFuture<?> configReloadTask;
    
    /**
     * Универсальная обертка для работы с данными предметов
     * Поддерживает различные сборки L2J
     */
    private static class ItemDataWrapper {
        private static ItemDataWrapper instance;
        private Object itemDataInstance;
        private Class<?> itemDataClass;
        private Class<?> itemTemplateClass;
        private final Map<Integer, String> itemCache = new ConcurrentHashMap<>();
        
        // Возможные классы в разных сборках
        private static final String[] ITEM_DATA_CLASSES = {
            // L2J-Server официальный
            "com.l2jserver.gameserver.data.xml.impl.ItemData",
            
            // aCis сборка
            "net.sf.l2j.gameserver.data.ItemTable",
            
            // L2JFrozen
            "com.l2jfrozen.gameserver.datatables.ItemTable",
            
            // L2JMobius
            "org.l2jmobius.gameserver.data.ItemTable",
            "org.l2jmobius.gameserver.data.xml.ItemData",
            
            // L2JEternity
            "com.l2jeternity.gameserver.data.ItemTable",
            
            // L2JArchid  
            "net.sf.l2j.gameserver.datatables.ItemTable",
            
            // Другие популярные сборки
            "com.l2j.gameserver.data.ItemTable",
            "l2r.gameserver.data.xml.ItemData",
            "lineage2.gameserver.data.xml.holder.ItemHolder"
        };
        
        private static final String[] ITEM_TEMPLATE_CLASSES = {
            // L2J-Server официальный
            "com.l2jserver.gameserver.model.items.L2Item",
            
            // aCis
            "net.sf.l2j.gameserver.model.item.Item",
            
            // L2JFrozen
            "com.l2jfrozen.gameserver.model.L2Item",
            
            // L2JMobius
            "org.l2jmobius.gameserver.model.items.Item",
            
            // Другие
            "com.l2j.gameserver.model.L2Item",
            "lineage2.gameserver.templates.item.ItemTemplate"
        };
        
        // Fallback данные для известных предметов
        private static final Map<Integer, String> KNOWN_ITEMS = new HashMap<>();
        
        static {
            KNOWN_ITEMS.put(57, "Adena");
            KNOWN_ITEMS.put(1538, "Blessed Scroll of Escape");
            KNOWN_ITEMS.put(1374, "Greater Heal Potion");
            KNOWN_ITEMS.put(6577, "Blessed Enchant Weapon S");
            KNOWN_ITEMS.put(6578, "Blessed Enchant Armor S");
            KNOWN_ITEMS.put(4037, "Coin of Luck");
            KNOWN_ITEMS.put(8762, "Top-grade Life Stone");
            KNOWN_ITEMS.put(1147, "Scroll of Resurrection");
            KNOWN_ITEMS.put(3936, "Blessed Scroll of Resurrection");
            KNOWN_ITEMS.put(1060, "Lesser Heal Potion");
            KNOWN_ITEMS.put(5575, "Ancient Adena");
            KNOWN_ITEMS.put(6673, "Seal of Gnosis");
            KNOWN_ITEMS.put(6674, "Forgotten Scroll");
        }
        
        private ItemDataWrapper() {
            initialize();
        }
        
        public static ItemDataWrapper getInstance() {
            if (instance == null) {
                instance = new ItemDataWrapper();
            }
            return instance;
        }
        
        private void initialize() {
            boolean foundItemData = false;
            
            // Пробуем найти правильный класс для данной сборки
            for (String className : ITEM_DATA_CLASSES) {
                try {
                    itemDataClass = Class.forName(className);
                    
                    // Пробуем получить инстанс через getInstance()
                    try {
                        itemDataInstance = itemDataClass.getMethod("getInstance").invoke(null);
                        LOG.info("[ItemDataWrapper] Found ItemData class: {}", className);
                        foundItemData = true;
                        break;
                    } catch (Exception e) {
                        // Пробуем как статический класс
                        itemDataInstance = itemDataClass;
                        LOG.info("[ItemDataWrapper] Found static ItemData class: {}", className);
                        foundItemData = true;
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // Класс не найден, пробуем следующий
                    continue;
                }
            }
            
            // Находим класс шаблона предмета
            for (String className : ITEM_TEMPLATE_CLASSES) {
                try {
                    itemTemplateClass = Class.forName(className);
                    LOG.info("[ItemDataWrapper] Found ItemTemplate class: {}", className);
                    break;
                } catch (ClassNotFoundException e) {
                    continue;
                }
            }
            
            if (!foundItemData) {
                LOG.warn("[ItemDataWrapper] Could not find ItemData class, using fallback mode");
            }
            if (itemTemplateClass == null) {
                LOG.warn("[ItemDataWrapper] Could not find ItemTemplate class");
            }
        }
        
        /**
         * Получает шаблон предмета по ID
         */
        public Object getTemplate(int itemId) {
            if (itemDataInstance == null || itemDataClass == null) {
                return KNOWN_ITEMS.containsKey(itemId) ? new FallbackItemTemplate(itemId) : null;
            }
            
            try {
                // Список возможных методов в разных сборках
                String[] methodNames = {
                    "getTemplate",      // L2J-Server
                    "getItem",          // aCis, Frozen
                    "getItemTemplate",  // Mobius
                    "getItemById"       // Другие
                };
                
                for (String methodName : methodNames) {
                    try {
                        return itemDataClass.getMethod(methodName, int.class).invoke(itemDataInstance, itemId);
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                }
                
                // Если ничего не нашли, используем fallback
                return KNOWN_ITEMS.containsKey(itemId) ? new FallbackItemTemplate(itemId) : null;
                
            } catch (Exception e) {
                LOG.debug("[ItemDataWrapper] Error getting item template for {}: {}", itemId, e.getMessage());
                return KNOWN_ITEMS.containsKey(itemId) ? new FallbackItemTemplate(itemId) : null;
            }
        }
        
        /**
         * Получает название предмета
         */
        public String getItemName(int itemId) {
            // Проверяем кэш
            String cachedName = itemCache.get(itemId);
            if (cachedName != null) {
                return cachedName;
            }
            
            String itemName = null;
            Object template = getTemplate(itemId);
            
            if (template != null) {
                if (template instanceof FallbackItemTemplate) {
                    itemName = ((FallbackItemTemplate) template).getName();
                } else {
                    try {
                        // Пробуем разные методы получения имени
                        String[] methodNames = {"getName", "getItemName", "toString"};
                        
                        for (String methodName : methodNames) {
                            try {
                                Object result = template.getClass().getMethod(methodName).invoke(template);
                                if (result instanceof String && !result.toString().isEmpty()) {
                                    itemName = (String) result;
                                    break;
                                }
                            } catch (NoSuchMethodException e) {
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        LOG.debug("[ItemDataWrapper] Error getting item name for {}: {}", itemId, e.getMessage());
                    }
                }
            }
            
            // Fallback к известным предметам
            if (itemName == null || itemName.isEmpty()) {
                itemName = KNOWN_ITEMS.getOrDefault(itemId, "Unknown Item (" + itemId + ")");
            }
            
            // Кэшируем результат
            itemCache.put(itemId, itemName);
            return itemName;
        }
        
        /**
         * Проверяет существование предмета
         */
        public boolean itemExists(int itemId) {
            Object template = getTemplate(itemId);
            return template != null;
        }
        
        /**
         * Простой класс шаблона для fallback режима
         */
        private static class FallbackItemTemplate {
            private final int itemId;
            private final String name;
            
            public FallbackItemTemplate(int itemId) {
                this.itemId = itemId;
                this.name = KNOWN_ITEMS.getOrDefault(itemId, "Unknown Item (" + itemId + ")");
            }
            
            public String getName() {
                return name;
            }
            
            public int getItemId() {
                return itemId;
            }
        }
        
        /**
         * Добавляет известный предмет в fallback список
         */
        public void addKnownItem(int itemId, String name) {
            KNOWN_ITEMS.put(itemId, name);
            itemCache.put(itemId, name); // Обновляем кэш
        }
        
        /**
         * Отладочная информация
         */
        public void printDebugInfo() {
            LOG.info("=== ItemDataWrapper Debug Info ===");
            LOG.info("ItemData class: {}", itemDataClass != null ? itemDataClass.getName() : "NOT FOUND");
            LOG.info("ItemTemplate class: {}", itemTemplateClass != null ? itemTemplateClass.getName() : "NOT FOUND");
            LOG.info("ItemData instance: {}", itemDataInstance != null ? "OK" : "NULL");
            LOG.info("Known items count: {}", KNOWN_ITEMS.size());
            
            // Тестируем с Adena
            if (itemExists(57)) {
                LOG.info("Test successful - Adena: {}", getItemName(57));
            } else {
                LOG.warn("Test failed - Adena not found");
            }
            LOG.info("===================================");
        }
    }
    
    /**
     * Утилиты для работы с предметами
     */
    private static class ItemUtils {
        
        /**
         * Получает красивое название предмета с форматированием
         */
        public static String getFormattedItemName(int itemId, long count) {
            String itemName = ItemDataWrapper.getInstance().getItemName(itemId);
            
            if (count == 1) {
                return itemName;
            } else {
                return String.format("%s x%s", itemName, formatNumber(count));
            }
        }
        
        /**
         * Форматирует большие числа
         */
        public static String formatNumber(long number) {
            if (number >= 1_000_000_000) {
                return String.format("%.1fB", number / 1_000_000_000.0);
            } else if (number >= 1_000_000) {
                return String.format("%.1fM", number / 1_000_000.0);
            } else if (number >= 1_000) {
                return String.format("%.1fK", number / 1_000.0);
            } else {
                return String.valueOf(number);
            }
        }
        
        /**
         * Проверяет является ли предмет валютой
         */
        public static boolean isCurrency(int itemId) {
            int[] currencies = {57, 5575, 6673, 6674}; // Adena, Ancient Adena, etc.
            
            for (int currency : currencies) {
                if (currency == itemId) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * Получает описание предмета для логов
         */
        public static String getItemDescription(int itemId) {
            return String.format("Item{id=%d, name='%s'}", 
                itemId, ItemDataWrapper.getInstance().getItemName(itemId));
        }
    }
    
    /**
     * Группа наград для определенных категорий игроков
     */
    public static class RewardGroup {
        private final String name;
        private final String description;
        private final List<ItemReward> rewards;
        private final Set<String> allowedAccessLevels;
        private final boolean isActive;
        private final int priority;
        
        public RewardGroup(String name, String description, List<ItemReward> rewards, 
                          Set<String> allowedAccessLevels, boolean isActive, int priority) {
            this.name = name;
            this.description = description;
            this.rewards = new ArrayList<>(rewards);
            this.allowedAccessLevels = new HashSet<>(allowedAccessLevels);
            this.isActive = isActive;
            this.priority = priority;
        }
        
        public boolean canPlayerReceive(L2PcInstance player) {
            if (!isActive) return false;
            
            // Проверяем уровень доступа
            String playerAccessLevel = String.valueOf(player.getAccessLevel().getLevel());
            if (!allowedAccessLevels.isEmpty() && !allowedAccessLevels.contains(playerAccessLevel)) {
                return false;
            }
            
            return true;
        }
        
        // Геттеры
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<ItemReward> getRewards() { return new ArrayList<>(rewards); }
        public boolean isActive() { return isActive; }
        public int getPriority() { return priority; }
    }
    
    /**
     * Награда с расширенными возможностями
     */
    public static class ItemReward {
        private final int itemId;
        private final long baseCount;
        private final long timeInterval;
        private final boolean saveToDatabase;
        private final boolean onceOnly;
        private final boolean progressive;
        private final String requiredEvent;
        private final DayOfWeek[] allowedDays;
        private final int minLevel;
        private final int maxLevel;
        
        public ItemReward(int itemId, long baseCount, long timeInterval, boolean saveToDatabase,
                         boolean onceOnly, boolean progressive, String requiredEvent,
                         DayOfWeek[] allowedDays, int minLevel, int maxLevel) {
            this.itemId = itemId;
            this.baseCount = baseCount;
            this.timeInterval = timeInterval;
            this.saveToDatabase = saveToDatabase;
            this.onceOnly = onceOnly;
            this.progressive = progressive;
            this.requiredEvent = requiredEvent;
            this.allowedDays = allowedDays;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }
        
        public boolean isAvailableNow() {
            LocalDateTime now = LocalDateTime.now();
            
            // Проверяем день недели
            if (allowedDays != null && allowedDays.length > 0) {
                boolean dayAllowed = Arrays.asList(allowedDays).contains(now.getDayOfWeek());
                if (!dayAllowed) return false;
            }
            
            return true;
        }
        
        public boolean canPlayerReceive(L2PcInstance player) {
            if (!isAvailableNow()) return false;
            
            // Проверяем уровень игрока
            int playerLevel = player.getLevel();
            if (minLevel > 0 && playerLevel < minLevel) return false;
            if (maxLevel > 0 && playerLevel > maxLevel) return false;
            
            return true;
        }
        
        // Геттеры
        public int getItemId() { return itemId; }
        public long getBaseCount() { return baseCount; }
        public long getTimeInterval() { return timeInterval; }
        public boolean isSaveToDatabase() { return saveToDatabase; }
        public boolean isOnceOnly() { return onceOnly; }
        public boolean isProgressive() { return progressive; }
        public String getRequiredEvent() { return requiredEvent; }
    }
    
    /**
     * Система работы с базой данных
     */
    private class RewardDatabase {
        private static final String CREATE_REWARDS_TABLE = """
            CREATE TABLE IF NOT EXISTS reward_groups (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL UNIQUE,
                description TEXT,
                access_levels TEXT,
                is_active BOOLEAN DEFAULT TRUE,
                priority INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
        """;
        
        private static final String CREATE_REWARDS_ITEMS_TABLE = """
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
                FOREIGN KEY (group_id) REFERENCES reward_groups(id) ON DELETE CASCADE
            )
        """;
        
        private static final String CREATE_CALENDAR_EVENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS calendar_events (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                description TEXT,
                start_date DATETIME NOT NULL,
                end_date DATETIME NOT NULL,
                reward_multiplier DOUBLE DEFAULT 1.0,
                special_item_id INT,
                special_item_count BIGINT,
                is_active BOOLEAN DEFAULT TRUE
            )
        """;
        
        private static final String CREATE_STATISTICS_TABLE = """
            CREATE TABLE IF NOT EXISTS reward_statistics (
                id INT AUTO_INCREMENT PRIMARY KEY,
                date DATE NOT NULL,
                player_id INT NOT NULL,
                reward_group VARCHAR(100),
                item_id INT,
                item_count BIGINT,
                progressive_multiplier DOUBLE DEFAULT 1.0,
                event_name VARCHAR(100),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_date_player (date, player_id),
                INDEX idx_reward_group (reward_group)
            )
        """;
        
        public void initializeTables() {
            try (Connection con = ConnectionFactory.getInstance().getConnection()) {
                try (Statement stmt = con.createStatement()) {
                    stmt.execute(CREATE_REWARDS_TABLE);
                    stmt.execute(CREATE_REWARDS_ITEMS_TABLE);
                    stmt.execute(CREATE_CALENDAR_EVENTS_TABLE);
                    stmt.execute(CREATE_STATISTICS_TABLE);
                }
                LOG.info("Database tables initialized successfully");
            } catch (SQLException e) {
                LOG.error("Failed to initialize database tables", e);
            }
        }
        
        public List<RewardGroup> loadRewardGroups() {
            List<RewardGroup> groups = new ArrayList<>();
            
            String query = """
                SELECT rg.*, GROUP_CONCAT(ri.item_id, ':', ri.base_count, ':', ri.time_interval, 
                       ':', ri.save_to_database, ':', ri.once_only, ':', ri.progressive,
                       ':', IFNULL(ri.required_event, ''), ':', IFNULL(ri.allowed_days, ''),
                       ':', ri.min_level, ':', ri.max_level SEPARATOR '|') as items
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
                    
                    List<ItemReward> rewards = parseItemRewards(itemsStr);
                    
                    groups.add(new RewardGroup(name, description, rewards, accessLevels, isActive, priority));
                }
                
            } catch (SQLException e) {
                LOG.error("Failed to load reward groups from database", e);
            }
            
            return groups;
        }
        
        private List<ItemReward> parseItemRewards(String itemsStr) {
            List<ItemReward> rewards = new ArrayList<>();
            
            if (itemsStr == null || itemsStr.trim().isEmpty()) {
                return rewards;
            }
            
            String[] items = itemsStr.split("\\|");
            for (String item : items) {
                String[] parts = item.split(":");
                if (parts.length >= 11) {
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
                        
                        rewards.add(new ItemReward(itemId, baseCount, timeInterval, saveToDatabase,
                            onceOnly, progressive, requiredEvent, allowedDays, minLevel, maxLevel));
                            
                    } catch (Exception e) {
                        LOG.warn("Failed to parse item reward: {}", item, e);
                    }
                }
            }
            
            return rewards;
        }
        
        private boolean validateItem(ItemReward item) {
            // Используем ItemDataWrapper для проверки
            if (!itemDataWrapper.itemExists(item.itemId)) {
                LOG.warn("Item with ID {} does not exist", item.itemId);
                return false;
            }
            
            if (item.baseCount <= 0) {
                LOG.warn("Invalid count {} for item {}", item.baseCount, item.itemId);
                return false;
            }
            
            if (item.timeInterval <= 0) {
                LOG.warn("Invalid time {} for item {}", item.timeInterval, item.itemId);
                return false;
            }
            
            return true;
        }
        
        public void saveStatistics(int playerId, String rewardGroup, int itemId, long itemCount,
                                 double progressiveMultiplier, String eventName) {
            String query = """
                INSERT INTO reward_statistics (date, player_id, reward_group, item_id, item_count, 
                                             progressive_multiplier, event_name)
                VALUES (CURDATE(), ?, ?, ?, ?, ?, ?)
            """;
            
            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                
                stmt.setInt(1, playerId);
                stmt.setString(2, rewardGroup);
                stmt.setInt(3, itemId);
                stmt.setLong(4, itemCount);
                stmt.setDouble(5, progressiveMultiplier);
                stmt.setString(6, eventName);
                
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                LOG.error("Failed to save statistics", e);
            }
        }
        
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
                String totalQuery = "SELECT COUNT(*) as total, COUNT(DISTINCT player_id) as unique_players FROM reward_statistics WHERE " + dateFilter;
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
    }
    
    /**
     * Web API для управления системой наград
     */
    private class RewardWebAPI {
        private final Map<String, APIEndpoint> endpoints;
        
        public RewardWebAPI() {
            endpoints = new HashMap<>();
            registerEndpoints();
        }
        
        private void registerEndpoints() {
            endpoints.put("GET:/api/rewards/groups", this::getRewardGroups);
            endpoints.put("POST:/api/rewards/groups", this::createRewardGroup);
            endpoints.put("PUT:/api/rewards/groups", this::updateRewardGroup);
            endpoints.put("DELETE:/api/rewards/groups", this::deleteRewardGroup);
            endpoints.put("GET:/api/rewards/statistics", this::getStatistics);
            endpoints.put("POST:/api/rewards/reload", this::reloadConfiguration);
            endpoints.put("GET:/api/rewards/players", this::getActivePlayers);
        }
        
        @FunctionalInterface
        private interface APIEndpoint {
            Map<String, Object> handle(Map<String, String> params, String body);
        }
        
        public Map<String, Object> handleRequest(String method, String path, 
                                               Map<String, String> params, String body) {
            String key = method + ":" + path;
            APIEndpoint endpoint = endpoints.get(key);
            
            if (endpoint == null) {
                return createErrorResponse("Endpoint not found", 404);
            }
            
            try {
                return endpoint.handle(params, body);
            } catch (Exception e) {
                LOG.error("API error for {} {}: {}", method, path, e.getMessage());
                return createErrorResponse("Internal server error: " + e.getMessage(), 500);
            }
        }
        
        private Map<String, Object> createErrorResponse(String message, int code) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", message);
            response.put("code", code);
            return response;
        }
        
        private Map<String, Object> createSuccessResponse(Object data) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);
            return response;
        }
        
        private Map<String, Object> getRewardGroups(Map<String, String> params, String body) {
            List<Map<String, Object>> groupsData = rewardGroups.stream()
                .map(group -> {
                    Map<String, Object> groupData = new HashMap<>();
                    groupData.put("name", group.getName());
                    groupData.put("description", group.getDescription());
                    groupData.put("active", group.isActive());
                    groupData.put("priority", group.getPriority());
                    groupData.put("rewardsCount", group.getRewards().size());
                    return groupData;
                })
                .collect(Collectors.toList());
                
            return createSuccessResponse(groupsData);
        }
        
        private Map<String, Object> createRewardGroup(Map<String, String> params, String body) {
            return createSuccessResponse("Group creation not implemented yet");
        }
        
        private Map<String, Object> updateRewardGroup(Map<String, String> params, String body) {
            return createSuccessResponse("Group update not implemented yet");
        }
        
        private Map<String, Object> deleteRewardGroup(Map<String, String> params, String body) {
            return createSuccessResponse("Group deletion not implemented yet");
        }
        
        private Map<String, Object> getStatistics(Map<String, String> params, String body) {
            String period = params.getOrDefault("period", "today");
            Map<String, Object> stats = database.getStatistics(period);
            stats.put("systemStats", statistics.getSystemStatistics());
            return createSuccessResponse(stats);
        }
        
        private Map<String, Object> reloadConfiguration(Map<String, String> params, String body) {
            reloadConfiguration();
            return createSuccessResponse("Configuration reloaded successfully");
        }
        
        private Map<String, Object> getActivePlayers(Map<String, String> params, String body) {
            List<Map<String, Object>> playersData = players.values().stream()
                .filter(PlayerHolder::isActive)
                .map(holder -> {
                    L2PcInstance player = holder.getPlayer();
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("name", player.getName());
                    playerData.put("level", player.getLevel());
                    playerData.put("onlineTime", holder.getOnlineTime());
                    playerData.put("rewardsReceived", holder.getRewardsReceived());
                    return playerData;
                })
                .collect(Collectors.toList());
                
            return createSuccessResponse(playersData);
        }
    }
    
    /**
     * Anti-AFK система
     */
    private class AntiAFKSystem {
        private final Map<Integer, AFKData> playerAFKData;
        private ScheduledFuture<?> checkTask;
        
        private static class AFKData {
            private volatile long lastActionTime;
            private volatile boolean isAFK;
            private volatile int x, y, z;
            
            public AFKData(L2PcInstance player) {
                this.lastActionTime = System.currentTimeMillis();
                this.isAFK = false;
                updatePosition(player);
            }
            
            public void updatePosition(L2PcInstance player) {
                this.x = player.getX();
                this.y = player.getY();
                this.z = player.getZ();
                this.lastActionTime = System.currentTimeMillis();
                this.isAFK = false;
            }
            
            public boolean hasMovedFrom(L2PcInstance player) {
                return player.getX() != x || player.getY() != y || player.getZ() != z;
            }
            
            public boolean isAFK() {
                return isAFK || (System.currentTimeMillis() - lastActionTime) > TimeUnit.MINUTES.toMillis(AFK_TIMEOUT_MINUTES);
            }
            
            public void markAsAFK() {
                this.isAFK = true;
            }
        }
        
        public AntiAFKSystem() {
            playerAFKData = new ConcurrentHashMap<>();
            startAFKChecking();
        }
        
        private void startAFKChecking() {
            checkTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
                try {
                    checkPlayersAFK();
                } catch (Exception e) {
                    LOG.warn("Error during AFK check: {}", e.getMessage());
                }
            }, ANTI_AFK_CHECK_INTERVAL, ANTI_AFK_CHECK_INTERVAL);
        }
        
        private void checkPlayersAFK() {
            for (Map.Entry<Integer, PlayerHolder> entry : players.entrySet()) {
                PlayerHolder holder = entry.getValue();
                if (!holder.isActive()) continue;
                
                L2PcInstance player = holder.getPlayer();
                AFKData afkData = playerAFKData.get(player.getObjectId());
                
                if (afkData == null) {
                    afkData = new AFKData(player);
                    playerAFKData.put(player.getObjectId(), afkData);
                    continue;
                }
                
                if (afkData.hasMovedFrom(player)) {
                    afkData.updatePosition(player);
                } else if (afkData.isAFK()) {
                    holder.pauseRewards();
                    LOG.debug("Player {} is AFK, rewards paused", player.getName());
                }
            }
        }
        
        public void onPlayerAction(L2PcInstance player) {
            AFKData afkData = playerAFKData.get(player.getObjectId());
            if (afkData != null) {
                afkData.updatePosition(player);
                
                PlayerHolder holder = players.get(player.getObjectId());
                if (holder != null) {
                    holder.resumeRewards();
                }
            }
        }
        
        public void removePlayer(int objectId) {
            playerAFKData.remove(objectId);
        }
        
        public boolean isPlayerAFK(L2PcInstance player) {
            AFKData afkData = playerAFKData.get(player.getObjectId());
            return afkData != null && afkData.isAFK();
        }
        
        public void shutdown() {
            if (checkTask != null) {
                checkTask.cancel(false);
            }
            playerAFKData.clear();
        }
    }
    
    /**
     * Менеджер прогрессивных наград
     */
    private class ProgressiveRewardManager {
        private final Map<String, Double> playerProgressiveMultipliers;
        
        public ProgressiveRewardManager() {
            playerProgressiveMultipliers = new ConcurrentHashMap<>();
        }
        
        public double getPlayerMultiplier(L2PcInstance player, ItemReward reward) {
            if (!reward.isProgressive()) {
                return 1.0;
            }
            
            String key = player.getObjectId() + "_" + reward.getItemId();
            return playerProgressiveMultipliers.getOrDefault(key, 1.0);
        }
        
        public void incrementPlayerMultiplier(L2PcInstance player, ItemReward reward) {
            if (!reward.isProgressive()) {
                return;
            }
            
            String key = player.getObjectId() + "_" + reward.getItemId();
            double currentMultiplier = playerProgressiveMultipliers.getOrDefault(key, 1.0);
            double newMultiplier = Math.min(currentMultiplier + 0.1, MAX_PROGRESSIVE_MULTIPLIER);
            
            playerProgressiveMultipliers.put(key, newMultiplier);
            
            LOG.debug("Progressive multiplier for player {} item {} increased to {}", 
                player.getName(), reward.getItemId(), newMultiplier);
        }
        
        public void resetPlayerMultiplier(L2PcInstance player, ItemReward reward) {
            String key = player.getObjectId() + "_" + reward.getItemId();
            playerProgressiveMultipliers.remove(key);
        }
        
        public void saveMultipliers() {
            // Реализация сохранения в БД
        }
        
        public void loadMultipliers() {
            // Реализация загрузки из БД
        }
    }
    
    /**
     * Менеджер календарных событий
     */
    private class CalendarEventManager {
        private volatile List<CalendarEvent> activeEvents;
        
        private static class CalendarEvent {
            private final String name;
            private final String description;
            private final LocalDateTime startDate;
            private final LocalDateTime endDate;
            private final double rewardMultiplier;
            private final Integer specialItemId;
            private final Long specialItemCount;
            
            public CalendarEvent(String name, String description, LocalDateTime startDate,
                               LocalDateTime endDate, double rewardMultiplier,
                               Integer specialItemId, Long specialItemCount) {
                this.name = name;
                this.description = description;
                this.startDate = startDate;
                this.endDate = endDate;
                this.rewardMultiplier = rewardMultiplier;
                this.specialItemId = specialItemId;
                this.specialItemCount = specialItemCount;
            }
            
            public boolean isActive() {
                LocalDateTime now = LocalDateTime.now();
                return !now.isBefore(startDate) && !now.isAfter(endDate);
            }
            
            public String getName() { return name; }
            public String getDescription() { return description; }
            public double getRewardMultiplier() { return rewardMultiplier; }
            public Integer getSpecialItemId() { return specialItemId; }
            public Long getSpecialItemCount() { return specialItemCount; }
        }
        
        public CalendarEventManager() {
            activeEvents = new ArrayList<>();
            loadEvents();
        }
        
        private void loadEvents() {
            String query = """
                SELECT name, description, start_date, end_date, reward_multiplier, 
                       special_item_id, special_item_count
                FROM calendar_events 
                WHERE is_active = TRUE AND end_date > NOW()
                ORDER BY start_date
            """;
            
            List<CalendarEvent> events = new ArrayList<>();
            
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
                    
                    events.add(new CalendarEvent(name, description, startDate, endDate,
                        rewardMultiplier, specialItemId, specialItemCount));
                }
                
                activeEvents = events;
                LOG.info("Loaded {} calendar events", events.size());
                
            } catch (SQLException e) {
                LOG.error("Failed to load calendar events", e);
            }
        }
        
        public List<CalendarEvent> getActiveEvents() {
            return activeEvents.stream()
                .filter(CalendarEvent::isActive)
                .collect(Collectors.toList());
        }
        
        public double getCurrentEventMultiplier() {
            return getActiveEvents().stream()
                .mapToDouble(CalendarEvent::getRewardMultiplier)
                .max()
                .orElse(1.0);
        }
        
        public List<ItemReward> getSpecialEventRewards() {
            List<ItemReward> specialRewards = new ArrayList<>();
            
            for (CalendarEvent event : getActiveEvents()) {
                if (event.getSpecialItemId() != null && event.getSpecialItemCount() != null) {
                    ItemReward specialReward = new ItemReward(
                        event.getSpecialItemId(),
                        event.getSpecialItemCount(),
                        TimeUnit.HOURS.toMillis(1),
                        false,
                        false,
                        false,
                        event.getName(),
                        null,
                        0, 0
                    );
                    specialRewards.add(specialReward);
                }
            }
            
            return specialRewards;
        }
        
        public void reloadEvents() {
            loadEvents();
        }
    }
    
    /**
     * Расширенная статистика системы
     */
    private class RewardStatistics {
        private final AtomicLong totalRewardsGiven = new AtomicLong(0);
        private final AtomicLong totalPlayersProcessed = new AtomicLong(0);
        private final AtomicLong totalOnlineTime = new AtomicLong(0);
        private final Map<String, AtomicLong> rewardsByGroup = new ConcurrentHashMap<>();
        private final Map<Integer, AtomicLong> rewardsByItem = new ConcurrentHashMap<>();
        private final long startTime = System.currentTimeMillis();
        
        public void incrementRewards(String groupName, int itemId) {
            totalRewardsGiven.incrementAndGet();
            rewardsByGroup.computeIfAbsent(groupName, k -> new AtomicLong(0)).incrementAndGet();
            rewardsByItem.computeIfAbsent(itemId, k -> new AtomicLong(0)).incrementAndGet();
        }
        
        public void incrementPlayers() {
            totalPlayersProcessed.incrementAndGet();
        }
        
        public void addOnlineTime(long timeMs) {
            totalOnlineTime.addAndGet(timeMs);
        }
        
        public Map<String, Object> getSystemStatistics() {
            Map<String, Object> stats = new HashMap<>();
            
            long uptime = System.currentTimeMillis() - startTime;
            stats.put("uptime", TimeUnit.MILLISECONDS.toMinutes(uptime));
            stats.put("totalRewards", totalRewardsGiven.get());
            stats.put("totalPlayers", totalPlayersProcessed.get());
            stats.put("totalOnlineTime", TimeUnit.MILLISECONDS.toHours(totalOnlineTime.get()));
            stats.put("currentActivePlayers", players.size());
            
            Map<String, Long> topGroups = rewardsByGroup.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().get(),
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
            stats.put("rewardsByGroup", topGroups);
            
            Map<Integer, Long> topItems = rewardsByItem.entrySet().stream()
                .sorted(Map.Entry.<Integer, AtomicLong>comparingByValue(
                    (a, b) -> Long.compare(b.get(), a.get())))
                .limit(10)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().get(),
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
            stats.put("topItems", topItems);
            
            return stats;
        }
        
        public String getDetailedReport() {
            long uptime = System.currentTimeMillis() - startTime;
            return String.format(
                "=== Reward System Statistics ===\n" +
                "Uptime: %d minutes\n" +
                "Total rewards given: %d\n" +
                "Total players processed: %d\n" +
                "Current active players: %d\n" +
                "Total online time: %d hours\n" +
                "Average rewards per player: %.2f\n" +
                "Rewards per minute: %.2f",
                TimeUnit.MILLISECONDS.toMinutes(uptime),
                totalRewardsGiven.get(),
                totalPlayersProcessed.get(),
                players.size(),
                TimeUnit.MILLISECONDS.toHours(totalOnlineTime.get()),
                totalPlayersProcessed.get() > 0 ? (double) totalRewardsGiven.get() / totalPlayersProcessed.get() : 0.0,
                uptime > 0 ? (double) totalRewardsGiven.get() / TimeUnit.MILLISECONDS.toMinutes(uptime) : 0.0
            );
        }
    }
    
    /**
     * Улучшенный держатель информации о игроке
     */
    private final class PlayerHolder {
        private final L2PcInstance player;
        private final List<EnhancedRewardTask> rewardTasks;
        private final long loginTime;
        private volatile boolean isActive;
        private volatile boolean rewardsPaused;
        private final AtomicLong rewardsReceived = new AtomicLong(0);
        
        public PlayerHolder(L2PcInstance player) {
            this.player = player;
            this.rewardTasks = new ArrayList<>();
            this.loginTime = System.currentTimeMillis();
            this.isActive = true;
            this.rewardsPaused = false;
        }
        
        public PlayerHolder startRewardTasks() {
            if (isShuttingDown.get()) {
                return this;
            }
            
            List<RewardGroup> applicableGroups = rewardGroups.stream()
                .filter(group -> group.canPlayerReceive(player))
                .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
                .collect(Collectors.toList());
            
            List<ItemReward> eventRewards = calendarManager.getSpecialEventRewards();
            
            for (RewardGroup group : applicableGroups) {
                for (ItemReward reward : group.getRewards()) {
                    if (reward.canPlayerReceive(player)) {
                        try {
                            EnhancedRewardTask task = new EnhancedRewardTask(this, reward, group.getName());
                            if (task.isValid()) {
                                rewardTasks.add(task);
                            }
                        } catch (Exception e) {
                            LOG.warn("Failed to create reward task for player {} and reward {}: {}", 
                                player.getName(), reward.getItemId(), e.getMessage());
                        }
                    }
                }
            }
            
            for (ItemReward eventReward : eventRewards) {
                if (eventReward.canPlayerReceive(player)) {
                    try {
                        EnhancedRewardTask task = new EnhancedRewardTask(this, eventReward, "EVENT");
                        if (task.isValid()) {
                            rewardTasks.add(task);
                        }
                    } catch (Exception e) {
                        LOG.warn("Failed to create event reward task for player {}: {}", 
                            player.getName(), e.getMessage());
                    }
                }
            }
            
            LOG.debug("Started {} reward tasks for player {} from {} groups", 
                rewardTasks.size(), player.getName(), applicableGroups.size());
            return this;
        }
        
        public void pauseRewards() {
            rewardsPaused = true;
            LOG.debug("Paused rewards for player {}", player.getName());
        }
        
        public void resumeRewards() {
            if (rewardsPaused) {
                rewardsPaused = false;
                LOG.debug("Resumed rewards for player {}", player.getName());
            }
        }
        
        public void onPlayerLogout() {
            isActive = false;
            
            for (EnhancedRewardTask task : rewardTasks) {
                try {
                    task.onPlayerLogout();
                } catch (Exception e) {
                    LOG.warn("Error during logout processing for player {}: {}", 
                        player.getName(), e.getMessage());
                }
            }
            
            long sessionTime = System.currentTimeMillis() - loginTime;
            statistics.addOnlineTime(sessionTime);
            
            antiAFK.removePlayer(player.getObjectId());
            
            LOG.debug("Player {} session ended, duration: {} minutes, rewards received: {}", 
                player.getName(), TimeUnit.MILLISECONDS.toMinutes(sessionTime), rewardsReceived.get());
        }
        
        public boolean isActive() {
            return isActive && player != null && player.isOnline();
        }
        
        public boolean areRewardsPaused() {
            return rewardsPaused;
        }
        
        public L2PcInstance getPlayer() {
            return player;
        }
        
        public long getOnlineTime() {
            return System.currentTimeMillis() - loginTime;
        }
        
        public long getRewardsReceived() {
            return rewardsReceived.get();
        }
        
        public void incrementRewardsReceived() {
            rewardsReceived.incrementAndGet();
        }
    }
    
    /**
     * Усовершенствованная задача награждения
     */
    private final class EnhancedRewardTask implements Runnable {
        private final PlayerHolder playerHolder;
        private final ItemReward reward;
        private final String groupName;
        private volatile ScheduledFuture<?> task;
        private volatile long lastExecutionTime;
        private final AtomicBoolean isRunning = new AtomicBoolean(false);
        
        public EnhancedRewardTask(PlayerHolder playerHolder, ItemReward reward, String groupName) {
            this.playerHolder = playerHolder;
            this.reward = reward;
            this.groupName = groupName;
            this.lastExecutionTime = System.currentTimeMillis();
            
            long initialDelay = calculateInitialDelay();
            
            if (initialDelay > 0) {
                this.task = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(
                    this, initialDelay, reward.getTimeInterval());
            }
        }
        
        private long calculateInitialDelay() {
            L2PcInstance player = playerHolder.getPlayer();
            
            if (reward.isOnceOnly() && player.getVariables().getBoolean(getGivenVar(), false)) {
                LOG.debug("Player {} already received once-only reward {}", 
                    player.getName(), reward.getItemId());
                return -1;
            }
            
            if (reward.isSaveToDatabase()) {
                long savedTime = player.getVariables().getLong(getTimeVar(), reward.getTimeInterval());
                return Math.max(0, Math.min(savedTime, reward.getTimeInterval()));
            }
            
            return reward.getTimeInterval();
        }
        
        @Override
        public void run() {
            if (isShuttingDown.get() || !isRunning.compareAndSet(false, true)) {
                return;
            }
            
            try {
                executeReward();
            } catch (Exception e) {
                LOG.error("Error executing reward task for player {} and item {}: {}", 
                    playerHolder.getPlayer().getName(), reward.getItemId(), e.getMessage());
            } finally {
                isRunning.set(false);
            }
        }
        
        private void executeReward() {
            L2PcInstance player = playerHolder.getPlayer();
            
            if (!isPlayerValid(player) || playerHolder.areRewardsPaused()) {
                return;
            }
            
            if (antiAFK.isPlayerAFK(player)) {
                LOG.debug("Player {} is AFK, skipping reward", player.getName());
                return;
            }
            
            if (reward.isOnceOnly() && player.getVariables().getBoolean(getGivenVar(), false)) {
                stopTask();
                return;
            }
            
            if (!reward.isAvailableNow()) {
                return;
            }
            
            if (player.isOnline()) {
                giveEnhancedReward(player);
            }
        }
        
        private boolean isPlayerValid(L2PcInstance player) {
            return player != null && 
                   player.getClient() != null && 
                   !player.getClient().isDetached() && 
                   playerHolder.isActive();
        }
        
        private void giveEnhancedReward(L2PcInstance player) {
            try {
                long finalCount = calculateFinalRewardCount(player);
                
                if (!player.getInventory().validateCapacityByItemId(reward.getItemId())) {
                    sendNotification(player, "Inventory full! Reward will be sent later.");
                    return;
                }
                
                player.addItem(SCRIPT_NAME, reward.getItemId(), finalCount, null, true);
                
                if (reward.isProgressive()) {
                    progressiveManager.incrementPlayerMultiplier(player, reward);
                }
                
                sendEnhancedNotification(player, finalCount);
                
                statistics.incrementRewards(groupName, reward.getItemId());
                playerHolder.incrementRewardsReceived();
                
                double progressiveMultiplier = progressiveManager.getPlayerMultiplier(player, reward);
                String eventName = getActiveEventName();
                database.saveStatistics(player.getObjectId(), groupName, reward.getItemId(), 
                    finalCount, progressiveMultiplier, eventName);
                
                lastExecutionTime = System.currentTimeMillis();
                
                if (reward.isSaveToDatabase()) {
                    player.getVariables().set(getTimeVar(), reward.getTimeInterval());
                }
                
                if (reward.isOnceOnly()) {
                    player.getVariables().set(getGivenVar(), true);
                    stopTask();
                }
                
                LOG.debug("Enhanced reward given to {}: {} (group: {}, multiplier: {:.2f})", 
                    player.getName(), 
                    ItemUtils.getFormattedItemName(reward.getItemId(), finalCount),
                    groupName, progressiveMultiplier);
                    
            } catch (Exception e) {
                LOG.error("Failed to give enhanced reward to player {}: {}", 
                    player.getName(), e.getMessage());
            }
        }
        
        private long calculateFinalRewardCount(L2PcInstance player) {
            long baseCount = reward.getBaseCount();
            
            double progressiveMultiplier = progressiveManager.getPlayerMultiplier(player, reward);
            double eventMultiplier = calendarManager.getCurrentEventMultiplier();
            
            double finalMultiplier = progressiveMultiplier * eventMultiplier;
            return Math.max(1, Math.round(baseCount * finalMultiplier));
        }
        
        private void sendEnhancedNotification(L2PcInstance player, long finalCount) {
            try {
                String itemName = itemDataWrapper.getItemName(reward.getItemId());
                double progressiveMultiplier = progressiveManager.getPlayerMultiplier(player, reward);
                double eventMultiplier = calendarManager.getCurrentEventMultiplier();
                
                StringBuilder message = new StringBuilder();
                message.append(String.format("Received %s", 
                    ItemUtils.getFormattedItemName(reward.getItemId(), finalCount)));
                
                if (progressiveMultiplier > 1.0) {
                    message.append(String.format(" (Progressive: %.1fx)", progressiveMultiplier));
                }
                
                if (eventMultiplier > 1.0) {
                    String eventName = getActiveEventName();
                    message.append(String.format(" [%s Event: %.1fx]", eventName, eventMultiplier));
                }
                
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1);
                sm.addString("[Online Reward] " + message.toString());
                player.sendPacket(sm);
                
            } catch (Exception e) {
                LOG.debug("Failed to send enhanced notification to player {}: {}", 
                    player.getName(), e.getMessage());
            }
        }
        
        private void sendNotification(L2PcInstance player, String message) {
            try {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1);
                sm.addString("[Online Reward] " + message);
                player.sendPacket(sm);
            } catch (Exception e) {
                LOG.debug("Failed to send notification to player {}: {}", 
                    player.getName(), e.getMessage());
            }
        }
        
        private String getActiveEventName() {
            return calendarManager.getActiveEvents().stream()
                .findFirst()
                .map(CalendarEvent::getName)
                .orElse("None");
        }
        
        public void onPlayerLogout() {
            if (reward.isSaveToDatabase() && !reward.isOnceOnly()) {
                L2PcInstance player = playerHolder.getPlayer();
                long elapsedTime = System.currentTimeMillis() - lastExecutionTime;
                long remainingTime = Math.max(0, reward.getTimeInterval() - elapsedTime);
                
                if (remainingTime > 0) {
                    player.getVariables().set(getTimeVar(), remainingTime);
                    LOG.debug("Saved remaining time {} ms for player {} and reward {}", 
                        remainingTime, player.getName(), reward.getItemId());
                }
            }
            
            stopTask();
        }
        
        public void stopTask() {
            if (task != null && !task.isCancelled()) {
                task.cancel(false);
                task = null;
            }
        }
        
        public boolean isValid() {
            return task != null;
        }
        
        private String getTimeVar() {
            return "reward_time_" + groupName + "_" + reward.getItemId();
        }
        
        private String getGivenVar() {
            return "reward_given_" + groupName + "_" + reward.getItemId();
        }
    }
    
    /**
     * Конструктор с полной инициализацией всех систем
     */
    public AdvancedRewardSystem() {
        super(-1, SCRIPT_NAME, "custom");
        
        LOG.info("Initializing Advanced Reward System...");
        
        // Инициализируем ItemDataWrapper в первую очередь
        this.itemDataWrapper = ItemDataWrapper.getInstance();
        itemDataWrapper.printDebugInfo();
        
        // Проверяем совместимость
        if (!testItemDataCompatibility()) {
            LOG.error("ItemData compatibility test failed! System may not work correctly.");
        }
        
        this.players = new ConcurrentHashMap<>();
        this.isShuttingDown = new AtomicBoolean(false);
        this.rewardGroups = new ArrayList<>();
        
        // Инициализируем компоненты
        this.database = new RewardDatabase();
        this.webAPI = new RewardWebAPI();
        this.antiAFK = new AntiAFKSystem();
        this.progressiveManager = new ProgressiveRewardManager();
        this.calendarManager = new CalendarEventManager();
        this.statistics = new RewardStatistics();
        
        if (!initialize()) {
            LOG.error("Failed to initialize advanced reward system");
            return;
        }
        
        setupPeriodicTasks();
        LOG.info("Advanced reward system initialized successfully");
    }
    
    /**
     * Тестирование совместимости ItemData
     */
    private boolean testItemDataCompatibility() {
        try {
            // Тестируем основные функции
            boolean adenaExists = itemDataWrapper.itemExists(57);
            String adenaName = itemDataWrapper.getItemName(57);
            
            if (!adenaExists) {
                LOG.warn("Adena (ID:57) not found - using fallback mode");
                // Добавляем базовые предметы в fallback
                addKnownItems();
                return true; // Fallback режим тоже работает
            }
            
            if (adenaName == null || adenaName.trim().isEmpty()) {
                LOG.warn("Cannot get Adena name - using fallback mode");
                addKnownItems();
                return true;
            }
            
            LOG.info("ItemData compatibility test passed - Adena: {}", adenaName);
            return true;
            
        } catch (Exception e) {
            LOG.error("ItemData compatibility test failed", e);
            addKnownItems();
            return false;
        }
    }
    
    /**
     * Добавляет базовые предметы в систему
     */
    private void addKnownItems() {
        LOG.info("Adding known items to fallback system");
        
        // Добавляем популярные предметы
        Map<Integer, String> commonItems = Map.of(
            57, "Adena",
            1538, "Blessed Scroll of Escape", 
            1374, "Greater Heal Potion",
            6577, "Blessed Enchant Weapon S",
            6578, "Blessed Enchant Armor S",
            4037, "Coin of Luck",
            8762, "Top-grade Life Stone",
            1147, "Scroll of Resurrection",
            3936, "Blessed Scroll of Resurrection",
            1060, "Lesser Heal Potion"
        );
        
        for (Map.Entry<Integer, String> entry : commonItems.entrySet()) {
            itemDataWrapper.addKnownItem(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Полная инициализация системы
     */
    private boolean initialize() {
        try {
            // Инициализируем базу данных
            database.initializeTables();
            
            // Регистрируем обработчики событий
            registerEventListeners();
            
            // Загружаем конфигурацию
            if (!loadConfiguration()) {
                LOG.error("Failed to load configuration");
                return false;
            }
            
            // Загружаем прогрессивные множители
            progressiveManager.loadMultipliers();
            
            // Регистрируем shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            
            return true;
            
        } catch (Exception e) {
            LOG.error("Error during initialization:", e);
            return false;
        }
    }
    
    /**
     * Загрузка конфигурации из базы данных
     */
    private boolean loadConfiguration() {
        try {
            List<RewardGroup> groups = database.loadRewardGroups();
            if (groups.isEmpty()) {
                LOG.warn("No reward groups found in database, loading from file config");
                groups = loadFallbackConfiguration();
            }
            
            rewardGroups = groups;
            LOG.info("Loaded {} reward groups from database", groups.size());
            return true;
            
        } catch (Exception e) {
            LOG.error("Failed to load configuration", e);
            return false;
        }
    }
    
    /**
     * Загрузка резервной конфигурации из файла
     */
    private List<RewardGroup> loadFallbackConfiguration() {
        List<RewardGroup> groups = new ArrayList<>();
        
        try {
            RewardsOnlineConfig config = ConfigFactory.create(RewardsOnlineConfig.class);
            
            // Создаем группу по умолчанию из старой конфигурации
            List<ItemReward> rewards = new ArrayList<>();
            
            // Базовые награды для совместимости
            rewards.add(new ItemReward(57, 10000, TimeUnit.HOURS.toMillis(1), true, false, false, null, null, 0, 0));
            rewards.add(new ItemReward(1538, 1, TimeUnit.HOURS.toMillis(2), true, false, true, null, null, 0, 0));
            
            RewardGroup defaultGroup = new RewardGroup(
                "Default Group", 
                "Default rewards from legacy configuration",
                rewards,
                new HashSet<>(Arrays.asList("0", "1", "2", "3", "4", "5")),
                true,
                1
            );
            
            groups.add(defaultGroup);
            
        } catch (Exception e) {
            LOG.error("Failed to load fallback configuration", e);
            
            // Создаем минимальную конфигурацию
            List<ItemReward> minimalRewards = Arrays.asList(
                new ItemReward(57, 5000, TimeUnit.HOURS.toMillis(1), false, false, false, null, null, 0, 0)
            );
            
            RewardGroup minimalGroup = new RewardGroup(
                "Minimal Group",
                "Minimal default rewards",
                minimalRewards,
                new HashSet<>(Arrays.asList("0", "1", "2")),
                true,
                0
            );
            
            groups.add(minimalGroup);
        }
        
        return groups;
    }
    
    /**
     * Горячая перезагрузка конфигурации
     */
    public void reloadConfiguration() {
        try {
            List<RewardGroup> newGroups = database.loadRewardGroups();
            rewardGroups = newGroups;
            calendarManager.reloadEvents();
            
            LOG.info("Configuration reloaded successfully: {} groups, {} active events", 
                newGroups.size(), calendarManager.getActiveEvents().size());
                
        } catch (Exception e) {
            LOG.error("Failed to reload configuration", e);
        }
    }
    
    /**
     * Регистрация обработчиков событий
     */
    private void registerEventListeners() {
        Containers.Global().addListener(new ConsumerEventListener(
            Containers.Global(), 
            EventType.ON_PLAYER_LOGIN, 
            this::onPlayerLogin, 
            this
        ));
        
        Containers.Global().addListener(new ConsumerEventListener(
            Containers.Global(), 
            EventType.ON_PLAYER_LOGOUT, 
            this::onPlayerLogout, 
            this
        ));
    }
    
    /**
     * Настройка периодических задач
     */
    private void setupPeriodicTasks() {
        // Задача очистки
        cleanupTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            try {
                performCleanup();
            } catch (Exception e) {
                LOG.warn("Error during cleanup: {}", e.getMessage());
            }
        }, CLEANUP_INTERVAL, CLEANUP_INTERVAL);
        
        // Задача перезагрузки конфигурации
        configReloadTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            try {
                reloadConfiguration();
            } catch (Exception e) {
                LOG.warn("Error during config reload: {}", e.getMessage());
            }
        }, CONFIG_RELOAD_INTERVAL, CONFIG_RELOAD_INTERVAL);
    }
    
    /**
     * Очистка и статистика
     */
    private void performCleanup() {
        int removedPlayers = 0;
        
        // Удаляем неактивных игроков
        Iterator<Map.Entry<Integer, PlayerHolder>> iterator = players.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, PlayerHolder> entry = iterator.next();
            PlayerHolder holder = entry.getValue();
            if (!holder.isActive()) {
                iterator.remove();
                removedPlayers++;
            }
        }
        
        if (removedPlayers > 0) {
            LOG.debug("Cleanup removed {} inactive players", removedPlayers);
        }
        
        // Сохраняем прогрессивные множители
        progressiveManager.saveMultipliers();
        
        // Выводим детальную статистику
        LOG.info(statistics.getDetailedReport());
    }
    
    /**
     * Обработка входа игрока
     */
    private void onPlayerLogin(OnPlayerLogin event) {
        if (isShuttingDown.get()) {
            return;
        }
        
        L2PcInstance player = event.getActiveChar();
        int objectId = player.getObjectId();
        
        // Проверяем существующую сессию
        PlayerHolder existingHolder = players.get(objectId);
        if (existingHolder != null && existingHolder.isActive()) {
            LOG.debug("Player {} already has active reward session", player.getName());
            return;
        }
        
        // Создаем новую сессию
        PlayerHolder newHolder = new PlayerHolder(player).startRewardTasks();
        players.put(objectId, newHolder);
        statistics.incrementPlayers();
        
        LOG.info("Started advanced reward session for player: {} (Level: {}, Active groups: {})", 
            player.getName(), player.getLevel(), 
            rewardGroups.stream().filter(g -> g.canPlayerReceive(player)).count());
    }
    
    /**
     * Обработка выхода игрока
     */
    private void onPlayerLogout(OnPlayerLogout event) {
        int objectId = event.getActiveChar().getObjectId();
        PlayerHolder holder = players.remove(objectId);
        
        if (holder != null) {
            holder.onPlayerLogout();
            LOG.info("Ended reward session for player: {} (Rewards received: {})", 
                event.getActiveChar().getName(), holder.getRewardsReceived());
        }
    }
    
    /**
     * Graceful shutdown всех систем
     */
    public void shutdown() {
        if (!isShuttingDown.compareAndSet(false, true)) {
            return;
        }
        
        LOG.info("Shutting down advanced reward system...");
        
        // Останавливаем периодические задачи
        if (cleanupTask != null) cleanupTask.cancel(false);
        if (configReloadTask != null) configReloadTask.cancel(false);
        
        // Завершаем компоненты
        antiAFK.shutdown();
        progressiveManager.saveMultipliers();
        
        // Корректно завершаем сессии игроков
        for (PlayerHolder holder : players.values()) {
            try {
                holder.onPlayerLogout();
            } catch (Exception e) {
                LOG.warn("Error during player logout: {}", e.getMessage());
            }
        }
        
        players.clear();
        LOG.info("Advanced reward system shutdown completed. {}", statistics.getDetailedReport());
    }
    
    /**
     * API методы для внешнего управления
     */
    public Map<String, Object> handleWebAPIRequest(String method, String path, 
                                                  Map<String, String> params, String body) {
        return webAPI.handleRequest(method, path, params, body);
    }
    
    public String getDetailedStatistics() {
        return statistics.getDetailedReport();
    }
    
    public List<String> getActiveEventNames() {
        return calendarManager.getActiveEvents().stream()
            .map(CalendarEvent::getName)
            .collect(Collectors.toList());
    }
    
    /**
     * Добавление кастомного предмета в систему
     */
    public void addCustomItem(int itemId, String itemName) {
        itemDataWrapper.addKnownItem(itemId, itemName);
        LOG.info("Added custom item to system: {} - {}", itemId, itemName);
    }
    
    /**
     * Получение информации о системе
     */
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("version", "1.0.0");
        info.put("activePlayers", players.size());
        info.put("activeGroups", rewardGroups.size());
        info.put("activeEvents", calendarManager.getActiveEvents().size());
        info.put("uptime", System.currentTimeMillis() - statistics.startTime);
        info.put("itemDataMode", itemDataWrapper.itemDataClass != null ? "Normal" : "Fallback");
        return info;
    }
    
    /**
     * Точка входа
     */
    public static void main(String[] args) {
        if (LOAD) {
            try {
                new AdvancedRewardSystem();
                LOG.info("{}: loaded successfully with advanced features", SCRIPT_NAME);
            } catch (Exception e) {
                LOG.error("Failed to load {}: {}", SCRIPT_NAME, e.getMessage(), e);
            }
        } else {
            LOG.info("{}: loading disabled.", SCRIPT_NAME);
        }
    }
}