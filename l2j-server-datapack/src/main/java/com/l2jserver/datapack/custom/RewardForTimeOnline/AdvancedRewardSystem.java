package com.l2jserver.datapack.custom.RewardForTimeOnline;

import com.l2jserver.datapack.custom.RewardForTimeOnline.models.*;
import com.l2jserver.datapack.custom.RewardForTimeOnline.systems.*;
import com.l2jserver.datapack.custom.RewardForTimeOnline.database.RewardDatabase;
import com.l2jserver.datapack.custom.RewardForTimeOnline.web.RewardWebAPI;
import com.l2jserver.datapack.custom.RewardForTimeOnline.utils.*;
import com.l2jserver.datapack.custom.RewardForTimeOnline.tasks.*;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.Containers;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogin;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogout;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.quest.Quest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Dafna
 * Продвинутая система награждения игроков за время в сети
 * 
 * Версия 2.0 - Модульная архитектура
 * Основной координатор системы - делегирует работу специализированным модулям
 */
public final class AdvancedRewardSystem extends Quest {
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedRewardSystem.class);
    
    // Константы
    private static final String SCRIPT_NAME = "AdvancedRewardSystem";
    private static final String VERSION = "2.0.0";
    private static final boolean LOAD = true;
    private static final long CLEANUP_INTERVAL = TimeUnit.MINUTES.toMillis(30);
    private static final long CONFIG_RELOAD_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    
    // Основные компоненты системы
    private final Map<Integer, PlayerHolder> activePlayers;
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
    private final long startTime;
    
    /**
     * Конструктор - инициализирует все компоненты системы
     */
    public AdvancedRewardSystem() {
        super(-1, SCRIPT_NAME, "custom");
        
        LOG.info("Initializing Advanced Reward System v{}...", VERSION);
        this.startTime = System.currentTimeMillis();
        
        // Основные коллекции
        this.activePlayers = new ConcurrentHashMap<>();
        this.isShuttingDown = new AtomicBoolean(false);
        this.rewardGroups = new ArrayList<>();
        
        // Инициализируем компоненты в правильном порядке
        this.itemDataWrapper = ItemDataWrapper.getInstance();
        this.database = new RewardDatabase();
        this.statistics = new RewardStatistics();
        this.progressiveManager = new ProgressiveRewardManager(database);
        this.calendarManager = new CalendarEventManager(database);
        this.antiAFK = new AntiAFKSystem();
        this.webAPI = new RewardWebAPI(this);
        
        // Инициализируем систему
        if (!initialize()) {
            LOG.error("Failed to initialize advanced reward system");
            return;
        }
        
        LOG.info("Advanced Reward System v{} initialized successfully", VERSION);
    }
    
    /**
     * Полная инициализация системы
     */
    private boolean initialize() {
        try {
            LOG.info("Starting system initialization...");
            
            // 1. Проверяем совместимость ItemData
            if (!initializeItemDataSystem()) {
                LOG.warn("ItemData system initialized in fallback mode");
            }
            
            // 2. Инициализируем базу данных
            if (!database.initialize()) {
                LOG.error("Failed to initialize database");
                return false;
            }
            
            // 3. Загружаем конфигурацию
            loadConfiguration();
            
            // 4. Инициализируем компоненты
            initializeSubSystems();
            
            // 5. Регистрируем обработчики событий
            registerEventListeners();
            
            // 6. Запускаем периодические задачи
            setupPeriodicTasks();
            
            // 7. Регистрируем shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            
            LOG.info("System initialization completed successfully");
            return true;
            
        } catch (Exception e) {
            LOG.error("Critical error during initialization", e);
            return false;
        }
    }
    
    /**
     * Инициализация системы ItemData
     */
    private boolean initializeItemDataSystem() {
        try {
            itemDataWrapper.printDebugInfo();
            
            boolean isCompatible = itemDataWrapper.itemExists(57) && 
                                 itemDataWrapper.getItemName(57) != null;
            
            if (!isCompatible) {
                LOG.warn("ItemData compatibility issues detected, adding fallback items");
                addFallbackItems();
            }
            
            LOG.info("ItemData system initialized: {}", 
                isCompatible ? "Full compatibility" : "Fallback mode");
            return isCompatible;
            
        } catch (Exception e) {
            LOG.error("Failed to initialize ItemData system", e);
            addFallbackItems();
            return false;
        }
    }
    
    /**
     * Добавляет базовые предметы в fallback режим
     */
    private void addFallbackItems() {
        Map<Integer, String> essentialItems = Map.of(
            57, "Adena",
            1538, "Blessed Scroll of Escape", 
            1374, "Greater Heal Potion",
            6577, "Blessed Enchant Weapon S",
            6578, "Blessed Enchant Armor S",
            4037, "Coin of Luck",
            8762, "Top-grade Life Stone",
            1147, "Scroll of Resurrection"
        );
        
        essentialItems.forEach(itemDataWrapper::addKnownItem);
        LOG.info("Added {} fallback items to system", essentialItems.size());
    }
    
    /**
     * Инициализация подсистем
     */
    private void initializeSubSystems() {
        LOG.info("Initializing subsystems...");
        
        // Инициализируем в правильном порядке зависимостей
        statistics.initialize();
        progressiveManager.initialize();
        calendarManager.initialize();
        antiAFK.initialize();
        webAPI.initialize();
        
        LOG.info("All subsystems initialized successfully");
    }
    
    /**
     * Загрузка конфигурации
     */
    private void loadConfiguration() {
        try {
            LOG.info("Loading configuration...");
            
            // Пробуем загрузить из базы данных
            List<RewardGroup> groups = database.loadRewardGroups();
            
            if (groups.isEmpty()) {
                LOG.warn("No reward groups found in database, loading from configuration files");
                groups = ConfigurationLoader.loadFromFiles();
                
                // Сохраняем в БД для будущего использования
                if (!groups.isEmpty()) {
                    database.saveRewardGroups(groups);
                }
            }
            
            // Валидируем конфигурацию
            if (ConfigurationLoader.validateConfiguration(groups)) {
                rewardGroups = groups;
                LOG.info("Successfully loaded {} reward groups", groups.size());
                
                // Выводим информацию о группах
                for (RewardGroup group : groups) {
                    LOG.debug("Loaded group: {} (priority: {}, rewards: {}, active: {})", 
                        group.getName(), group.getPriority(), 
                        group.getRewards().size(), group.isActive());
                }
            } else {
                LOG.error("Configuration validation failed, using minimal fallback");
                rewardGroups = createMinimalConfiguration();
            }
            
        } catch (Exception e) {
            LOG.error("Failed to load configuration", e);
            rewardGroups = createMinimalConfiguration();
        }
    }
    
    /**
     * Создает минимальную конфигурацию в случае ошибок
     */
    private List<RewardGroup> createMinimalConfiguration() {
        LOG.warn("Creating minimal emergency configuration");
        
        RewardGroup emergencyGroup = RewardGroup.builder()
            .name("Emergency Rewards")
            .description("Minimal rewards created due to configuration error")
            .addReward(ItemReward.simple(57, 1000, 1)) // 1K Adena каждый час
            .allowAccessLevels("0", "1", "2", "3", "4", "5")
            .active(true)
            .priority(0)
            .build();
        
        return List.of(emergencyGroup);
    }
    
    /**
     * Горячая перезагрузка конфигурации
     */
    public void reloadConfiguration() {
        try {
            LOG.info("Reloading configuration...");
            
            List<RewardGroup> newGroups = database.loadRewardGroups();
            if (newGroups.isEmpty()) {
                newGroups = ConfigurationLoader.loadFromFiles();
            }
            
            if (ConfigurationLoader.validateConfiguration(newGroups)) {
                // Останавливаем текущие задачи для всех игроков
                for (PlayerHolder holder : activePlayers.values()) {
                    holder.pauseRewards();
                }
                
                // Обновляем конфигурацию
                rewardGroups = newGroups;
                calendarManager.reloadEvents();
                
                // Перезапускаем задачи с новой конфигурацией
                for (PlayerHolder holder : activePlayers.values()) {
                    restartPlayerTasks(holder);
                }
                
                LOG.info("Configuration reloaded successfully: {} groups, {} active events", 
                    newGroups.size(), calendarManager.getActiveEvents().size());
            } else {
                LOG.warn("New configuration failed validation, keeping current configuration");
            }
                
        } catch (Exception e) {
            LOG.error("Failed to reload configuration", e);
        }
    }
    
    /**
     * Перезапускает задачи для игрока с новой конфигурацией
     */
    private void restartPlayerTasks(PlayerHolder holder) {
        try {
            // Останавливаем старые задачи
            for (EnhancedRewardTask task : holder.getRewardTasks()) {
                task.stopTask();
            }
            
            // Создаем новые задачи
            L2PcInstance player = holder.getPlayer();
            if (player != null && player.isOnline()) {
                PlayerHolder newHolder = createPlayerSession(player);
                activePlayers.put(player.getObjectId(), newHolder);
            }
            
        } catch (Exception e) {
            LOG.warn("Failed to restart tasks for player: {}", e.getMessage());
        }
    }
    
    /**
     * Регистрация обработчиков событий
     */
    private void registerEventListeners() {
        LOG.debug("Registering event listeners...");
        
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
        
        LOG.debug("Event listeners registered successfully");
    }
    
    /**
     * Настройка периодических задач
     */
    private void setupPeriodicTasks() {
        LOG.debug("Setting up periodic tasks...");
        
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
                // Проверяем изменения в БД и перезагружаем при необходимости
                if (database.hasConfigurationChanged()) {
                    reloadConfiguration();
                }
            } catch (Exception e) {
                LOG.warn("Error during config reload check: {}", e.getMessage());
            }
        }, CONFIG_RELOAD_INTERVAL, CONFIG_RELOAD_INTERVAL);
        
        LOG.debug("Periodic tasks setup completed");
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
        
        try {
            // Проверяем существующую сессию
            PlayerHolder existingHolder = activePlayers.get(objectId);
            if (existingHolder != null && existingHolder.isActive()) {
                LOG.debug("Player {} already has active reward session", player.getName());
                return;
            }
            
            // Создаем новую сессию
            PlayerHolder newHolder = createPlayerSession(player);
            activePlayers.put(objectId, newHolder);
            
            // Регистрируем в anti-AFK системе
            antiAFK.registerPlayer(player, newHolder);
            
            // Обновляем статистику
            statistics.incrementPlayers();
            
            // Определяем доступные группы
            long availableGroups = rewardGroups.stream()
                .filter(g -> g.canPlayerReceive(player))
                .count();
            
            LOG.info("Started reward session for player: {} (Level: {}, Groups: {}, Tasks: {})", 
                player.getName(), player.getLevel(), availableGroups, newHolder.getActiveTasksCount());
                
        } catch (Exception e) {
            LOG.error("Failed to create reward session for player {}: {}", 
                player.getName(), e.getMessage());
        }
    }
    
    /**
     * Создает сессию игрока с наградами
     */
    private PlayerHolder createPlayerSession(L2PcInstance player) {
        PlayerHolder holder = new PlayerHolder(player);
        
        // Получаем доступные группы наград (отсортированные по приоритету)
        List<RewardGroup> applicableGroups = rewardGroups.stream()
            .filter(group -> group.canPlayerReceive(player))
            .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
            .collect(Collectors.toList());
        
        // Получаем специальные награды от событий
        List<ItemReward> eventRewards = calendarManager.getSpecialEventRewards();
        
        int createdTasks = 0;
        
        // Создаем задачи наград для каждой группы
        for (RewardGroup group : applicableGroups) {
            for (ItemReward reward : group.getAvailableRewards(player)) {
                if (createRewardTask(holder, reward, group.getName())) {
                    createdTasks++;
                }
            }
        }
        
        // Создаем задачи для событийных наград
        for (ItemReward eventReward : eventRewards) {
            if (eventReward.canPlayerReceive(player)) {
                if (createRewardTask(holder, eventReward, "EVENT")) {
                    createdTasks++;
                }
            }
        }
        
        LOG.debug("Created {} reward tasks for player {} from {} groups", 
            createdTasks, player.getName(), applicableGroups.size());
        
        return holder;
    }
    
    /**
     * Создает задачу награждения
     */
    private boolean createRewardTask(PlayerHolder holder, ItemReward reward, String groupName) {
        try {
            // Проверяем существование предмета
            if (!itemDataWrapper.itemExists(reward.getItemId())) {
                LOG.warn("Reward item {} does not exist, skipping task creation", reward.getItemId());
                return false;
            }
            
            EnhancedRewardTask task = new EnhancedRewardTask(holder, reward, groupName, this);
            if (task.isValid()) {
                holder.addRewardTask(task);
                return true;
            }
            
        } catch (Exception e) {
            LOG.warn("Failed to create reward task for item {}: {}", 
                reward.getItemId(), e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Обработка выхода игрока
     */
    private void onPlayerLogout(OnPlayerLogout event) {
        int objectId = event.getActiveChar().getObjectId();
        PlayerHolder holder = activePlayers.remove(objectId);
        
        if (holder != null) {
            try {
                // Завершаем сессию игрока
                holder.onPlayerLogout();
                
                // Обновляем статистику
                statistics.addSessionTime(holder.getOnlineTime());
                
                // Убираем из anti-AFK системы
                antiAFK.removePlayer(objectId);
                
                PlayerHolder.PlayerSessionStats stats = holder.getSessionStats();
                LOG.info("Ended reward session for player: {} (Duration: {}, Rewards: {})", 
                    event.getActiveChar().getName(), 
                    stats.getFormattedSessionDuration(),
                    stats.getRewardsReceived());
                    
            } catch (Exception e) {
                LOG.warn("Error during player logout processing: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Принудительная очистка системы
     */
    public int performCleanup() {
        int removedPlayers = 0;
        int totalTasks = 0;
        
        try {
            // Удаляем неактивных игроков
            Iterator<Map.Entry<Integer, PlayerHolder>> iterator = activePlayers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, PlayerHolder> entry = iterator.next();
                PlayerHolder holder = entry.getValue();
                totalTasks += holder.getActiveTasksCount();
                
                if (!holder.isActive()) {
                    iterator.remove();
                    removedPlayers++;
                }
            }
            
            // Сохраняем данные компонентов
            progressiveManager.saveMultipliers();
            statistics.performMaintenance();
            
            // Очищаем кэши
            itemDataWrapper.clearCache();
            
            if (removedPlayers > 0 || LOG.isDebugEnabled()) {
                LOG.info("Cleanup completed: removed {} inactive players, {} active players, {} total tasks", 
                    removedPlayers, activePlayers.size(), totalTasks);
            }
            
        } catch (Exception e) {
            LOG.error("Error during system cleanup", e);
        }
        
        return removedPlayers;
    }
    
    /**
     * Graceful shutdown всех систем
     */
    public void shutdown() {
        if (!isShuttingDown.compareAndSet(false, true)) {
            return;
        }
        
        LOG.info("Shutting down Advanced Reward System v{}...", VERSION);
        
        try {
            // Останавливаем периодические задачи
            if (cleanupTask != null) {
                cleanupTask.cancel(false);
                cleanupTask = null;
            }
            if (configReloadTask != null) {
                configReloadTask.cancel(false);
                configReloadTask = null;
            }
            
            // Корректно завершаем сессии игроков
            int activeSessions = activePlayers.size();
            for (PlayerHolder holder : activePlayers.values()) {
                try {
                    holder.onPlayerLogout();
                } catch (Exception e) {
                    LOG.warn("Error during player session shutdown: {}", e.getMessage());
                }
            }
            activePlayers.clear();
            
            // Завершаем компоненты в обратном порядке
            webAPI.shutdown();
            antiAFK.shutdown();
            calendarManager.shutdown();
            progressiveManager.shutdown();
            statistics.shutdown();
            database.shutdown();
            
            long shutdownTime = System.currentTimeMillis() - startTime;
            LOG.info("Advanced Reward System v{} shutdown completed. " +
                    "Sessions terminated: {}, Uptime: {} minutes, Final stats: {}", 
                VERSION, activeSessions, TimeUnit.MILLISECONDS.toMinutes(shutdownTime),
                statistics.getDetailedReport());
                
        } catch (Exception e) {
            LOG.error("Error during system shutdown", e);
        }
    }
    
    // ===============================
    // Публичные API методы
    // ===============================
    
    /**
     * Обработка Web API запросов
     */
    public Map<String, Object> handleWebAPIRequest(String method, String path, 
                                                  Map<String, String> params, String body) {
        try {
            return webAPI.handleRequest(method, path, params, body);
        } catch (Exception e) {
            LOG.error("Error handling web API request {} {}: {}", method, path, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Internal server error: " + e.getMessage());
            return errorResponse;
        }
    }
    
    /**
     * Принудительная перезагрузка конфигурации
     */
    public void forceReloadConfiguration() {
        LOG.info("Force reloading configuration by admin request");
        reloadConfiguration();
    }
    
    /**
     * Добавление кастомного предмета в систему
     */
    public void addCustomItem(int itemId, String itemName) {
        itemDataWrapper.addKnownItem(itemId, itemName);
        LOG.info("Added custom item to system: {} - {}", itemId, itemName);
    }
    
    /**
     * Получение детальной информации о системе
     */
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("version", VERSION);
        info.put("uptime", System.currentTimeMillis() - startTime);
        info.put("activePlayers", activePlayers.size());
        info.put("activeGroups", rewardGroups.size());
        info.put("activeEvents", calendarManager.getActiveEvents().size());
        info.put("itemDataMode", itemDataWrapper.getSystemInfo().isUsingFallback() ? "Fallback" : "Normal");
        info.put("totalRewards", statistics.getTotalRewards());
        info.put("totalSessions", statistics.getTotalSessions());
        
        // Добавляем статистику компонентов
        info.put("afkStats", antiAFK.getStatistics());
        info.put("progressiveMultipliers", progressiveManager.getActiveMultipliersCount());
        info.put("databaseStatus", database.getStatus());
        
        return info;
    }
    
    /**
     * Получение списка активных игроков с детальной информацией
     */
    public List<Map<String, Object>> getActivePlayersInfo() {
        return activePlayers.values().stream()
            .filter(PlayerHolder::isActive)
            .map(holder -> {
                PlayerHolder.PlayerSessionStats stats = holder.getSessionStats();
                Map<String, Object> playerInfo = new HashMap<>();
                playerInfo.put("name", stats.getPlayerName());
                playerInfo.put("level", stats.getPlayerLevel());
                playerInfo.put("sessionDuration", stats.getFormattedSessionDuration());
                playerInfo.put("rewardsReceived", stats.getRewardsReceived());
                playerInfo.put("activeTasks", stats.getActiveRewardTasks());
                playerInfo.put("rewardsPaused", stats.isRewardsPaused());
                return playerInfo;
            })
            .collect(Collectors.toList());
    }
    
    // ===============================
    // Геттеры для компонентов
    // ===============================
    
    public RewardDatabase getDatabase() { return database; }
    public AntiAFKSystem getAntiAFK() { return antiAFK; }
    public ProgressiveRewardManager getProgressiveManager() { return progressiveManager; }
    public CalendarEventManager getCalendarManager() { return calendarManager; }
    public RewardStatistics getStatistics() { return statistics; }
    public ItemDataWrapper getItemDataWrapper() { return itemDataWrapper; }
    public List<RewardGroup> getRewardGroups() { return new ArrayList<>(rewardGroups); }
    public boolean isShuttingDown() { return isShuttingDown.get(); }
    public String getVersion() { return VERSION; }
    public long getUptime() { return System.currentTimeMillis() - startTime; }
    
    /**
     * Точка входа
     */
    public static void main(String[] args) {
        if (LOAD) {
            try {
                new AdvancedRewardSystem();
                LOG.info("{} v{}: loaded successfully with modular architecture", SCRIPT_NAME, VERSION);
            } catch (Exception e) {
                LOG.error("Failed to load {} v{}: {}", SCRIPT_NAME, VERSION, e.getMessage(), e);
            }
        } else {
            LOG.info("{} v{}: loading disabled by configuration", SCRIPT_NAME, VERSION);
        }
    }
}