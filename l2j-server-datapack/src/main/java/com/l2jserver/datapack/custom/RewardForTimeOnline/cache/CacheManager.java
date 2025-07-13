package com.l2jserver.datapack.custom.RewardForTimeOnline.cache;

import com.l2jserver.datapack.custom.RewardForTimeOnline.models.RewardGroup;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.CalendarEvent;
import com.l2jserver.gameserver.config.RewardsOnlineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Менеджер кеширования для системы наград
 * Обеспечивает быстрый доступ к часто используемым данным
 * @author Dafna
 */
public class CacheManager {
    private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);
    
    // Основные кеши
    private final Map<String, CacheEntry<List<RewardGroup>>> groupsCache;
    private final Map<Integer, CacheEntry<PlayerCacheData>> playersCache;
    private final Map<String, CacheEntry<List<CalendarEvent>>> eventsCache;
    private final Map<String, CacheEntry<Object>> configCache;
    
    // Статистика кеширования
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicLong cacheEvictions = new AtomicLong(0);
    
    // Настройки
    private final long defaultTtl;
    private final int maxSize;
    private final boolean enabled;
    
    public CacheManager() {
        this.groupsCache = new ConcurrentHashMap<>();
        this.playersCache = new ConcurrentHashMap<>();
        this.eventsCache = new ConcurrentHashMap<>();
        this.configCache = new ConcurrentHashMap<>();
        
        // Загружаем настройки из конфигурации
        RewardsOnlineConfig config = RewardsOnlineConfig.getInstance();
        this.defaultTtl = config.getCacheTtl();
        this.maxSize = config.getCacheMaxSize();
        this.enabled = config.isCacheEnabled();
        
        if (enabled) {
            startCleanupTask();
            LOG.info("CacheManager initialized (TTL: {}ms, MaxSize: {})", defaultTtl, maxSize);
        } else {
            LOG.info("CacheManager disabled by configuration");
        }
    }
    
    /**
     * Получение групп наград из кеша
     */
    public Optional<List<RewardGroup>> getRewardGroups(String key) {
        if (!enabled) return Optional.empty();
        
        CacheEntry<List<RewardGroup>> entry = groupsCache.get(key);
        if (entry != null && !entry.isExpired()) {
            cacheHits.incrementAndGet();
            entry.updateAccessTime();
            LOG.trace("Cache hit for reward groups: {}", key);
            return Optional.of(new ArrayList<>(entry.getValue()));
        }
        
        cacheMisses.incrementAndGet();
        LOG.trace("Cache miss for reward groups: {}", key);
        return Optional.empty();
    }
    
    /**
     * Сохранение групп наград в кеш
     */
    public void putRewardGroups(String key, List<RewardGroup> groups) {
        if (!enabled || groups == null) return;
        
        // Проверяем размер кеша
        if (groupsCache.size() >= maxSize) {
            evictOldestEntry(groupsCache);
        }
        
        CacheEntry<List<RewardGroup>> entry = new CacheEntry<>(
            new ArrayList<>(groups), defaultTtl);
        groupsCache.put(key, entry);
        
        LOG.trace("Cached reward groups: {} (size: {})", key, groups.size());
    }
    
    /**
     * Получение данных игрока из кеша
     */
    public Optional<PlayerCacheData> getPlayerData(int playerId) {
        if (!enabled) return Optional.empty();
        
        CacheEntry<PlayerCacheData> entry = playersCache.get(playerId);
        if (entry != null && !entry.isExpired()) {
            cacheHits.incrementAndGet();
            entry.updateAccessTime();
            LOG.trace("Cache hit for player data: {}", playerId);
            return Optional.of(entry.getValue().copy());
        }
        
        cacheMisses.incrementAndGet();
        LOG.trace("Cache miss for player data: {}", playerId);
        return Optional.empty();
    }
    
    /**
     * Сохранение данных игрока в кеш
     */
    public void putPlayerData(int playerId, PlayerCacheData data) {
        if (!enabled || data == null) return;
        
        // Проверяем размер кеша
        if (playersCache.size() >= maxSize) {
            evictOldestEntry(playersCache);
        }
        
        CacheEntry<PlayerCacheData> entry = new CacheEntry<>(data.copy(), defaultTtl);
        playersCache.put(playerId, entry);
        
        LOG.trace("Cached player data: {}", playerId);
    }
    
    /**
     * Получение календарных событий из кеша
     */
    public Optional<List<CalendarEvent>> getCalendarEvents(String key) {
        if (!enabled) return Optional.empty();
        
        CacheEntry<List<CalendarEvent>> entry = eventsCache.get(key);
        if (entry != null && !entry.isExpired()) {
            cacheHits.incrementAndGet();
            entry.updateAccessTime();
            LOG.trace("Cache hit for calendar events: {}", key);
            return Optional.of(new ArrayList<>(entry.getValue()));
        }
        
        cacheMisses.incrementAndGet();
        LOG.trace("Cache miss for calendar events: {}", key);
        return Optional.empty();
    }
    
    /**
     * Сохранение календарных событий в кеш
     */
    public void putCalendarEvents(String key, List<CalendarEvent> events) {
        if (!enabled || events == null) return;
        
        if (eventsCache.size() >= maxSize) {
            evictOldestEntry(eventsCache);
        }
        
        CacheEntry<List<CalendarEvent>> entry = new CacheEntry<>(
            new ArrayList<>(events), defaultTtl);
        eventsCache.put(key, entry);
        
        LOG.trace("Cached calendar events: {} (size: {})", key, events.size());
    }
    
    /**
     * Получение значения конфигурации из кеша
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getConfigValue(String key, Class<T> type) {
        if (!enabled) return Optional.empty();
        
        CacheEntry<Object> entry = configCache.get(key);
        if (entry != null && !entry.isExpired()) {
            cacheHits.incrementAndGet();
            entry.updateAccessTime();
            
            try {
                T value = type.cast(entry.getValue());
                LOG.trace("Cache hit for config: {}", key);
                return Optional.of(value);
            } catch (ClassCastException e) {
                LOG.warn("Type mismatch in config cache for key: {}", key);
                configCache.remove(key);
            }
        }
        
        cacheMisses.incrementAndGet();
        LOG.trace("Cache miss for config: {}", key);
        return Optional.empty();
    }
    
    /**
     * Сохранение значения конфигурации в кеш
     */
    public void putConfigValue(String key, Object value) {
        if (!enabled || value == null) return;
        
        if (configCache.size() >= maxSize) {
            evictOldestEntry(configCache);
        }
        
        CacheEntry<Object> entry = new CacheEntry<>(value, defaultTtl * 2); // Конфиг кешируем дольше
        configCache.put(key, entry);
        
        LOG.trace("Cached config value: {}", key);
    }
    
    /**
     * Инвалидация кеша групп наград
     */
    public void invalidateRewardGroups() {
        groupsCache.clear();
        LOG.debug("Reward groups cache invalidated");
    }
    
    /**
     * Инвалидация кеша данных игрока
     */
    public void invalidatePlayerData(int playerId) {
        playersCache.remove(playerId);
        LOG.debug("Player data cache invalidated for ID: {}", playerId);
    }
    
    /**
     * Инвалидация кеша календарных событий
     */
    public void invalidateCalendarEvents() {
        eventsCache.clear();
        LOG.debug("Calendar events cache invalidated");
    }
    
    /**
     * Полная очистка всех кешей
     */
    public void invalidateAll() {
        groupsCache.clear();
        playersCache.clear();
        eventsCache.clear();
        configCache.clear();
        
        LOG.info("All caches invalidated");
    }
    
    /**
     * Удаление самой старой записи из кеша
     */
    private <T> void evictOldestEntry(Map<?, CacheEntry<T>> cache) {
        if (cache.isEmpty()) return;
        
        Object oldestKey = cache.entrySet().stream()
            .min(Map.Entry.comparingByValue(
                (e1, e2) -> Long.compare(e1.getLastAccessTime(), e2.getLastAccessTime())))
            .map(Map.Entry::getKey)
            .orElse(null);
            
        if (oldestKey != null) {
            cache.remove(oldestKey);
            cacheEvictions.incrementAndGet();
            LOG.trace("Evicted oldest entry from cache: {}", oldestKey);
        }
    }
    
    /**
     * Запуск задачи очистки устаревших записей
     */
    private void startCleanupTask() {
        Timer cleanupTimer = new Timer("CacheCleanup", true);
        cleanupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanupExpiredEntries();
            }
        }, TimeUnit.MINUTES.toMillis(5), TimeUnit.MINUTES.toMillis(5));
        
        LOG.debug("Cache cleanup task started");
    }
    
    /**
     * Очистка устаревших записей
     */
    private void cleanupExpiredEntries() {
        int removed = 0;
        
        // Очищаем группы наград
        removed += cleanupExpiredEntries(groupsCache);
        
        // Очищаем данные игроков
        removed += cleanupExpiredEntries(playersCache);
        
        // Очищаем календарные события
        removed += cleanupExpiredEntries(eventsCache);
        
        // Очищаем конфигурацию
        removed += cleanupExpiredEntries(configCache);
        
        if (removed > 0) {
            LOG.debug("Cleaned up {} expired cache entries", removed);
        }
    }
    
    /**
     * Очистка устаревших записей в конкретном кеше
     */
    private <K, T> int cleanupExpiredEntries(Map<K, CacheEntry<T>> cache) {
    List<K> expiredKeys = cache.entrySet().stream()
        .filter(entry -> entry.getValue().isExpired())
        .map(Map.Entry::getKey)
        .toList();

    expiredKeys.forEach(cache::remove);
    return expiredKeys.size();
    }

    
    /**
     * Получение статистики кеширования
     */
    public CacheStatistics getStatistics() {
        return new CacheStatistics(
            cacheHits.get(),
            cacheMisses.get(),
            cacheEvictions.get(),
            groupsCache.size(),
            playersCache.size(),
            eventsCache.size(),
            configCache.size()
        );
    }
    
    /**
     * Отключение кеша
     */
    public void shutdown() {
        invalidateAll();
        LOG.info("CacheManager shutdown completed");
    }
    
    /**
     * Запись кеша с TTL и временем последнего доступа
     */
    private static class CacheEntry<T> {
        private final T value;
        private final long creationTime;
        private final long ttl;
        private volatile long lastAccessTime;
        
        public CacheEntry(T value, long ttl) {
            this.value = value;
            this.creationTime = System.currentTimeMillis();
            this.ttl = ttl;
            this.lastAccessTime = creationTime;
        }
        
        public T getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() - creationTime > ttl;
        }
        
        public void updateAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
    }
    
    /**
     * Статистика кеширования
     */
    public static class CacheStatistics {
        private final long hits;
        private final long misses;
        private final long evictions;
        private final int groupsCacheSize;
        private final int playersCacheSize;
        private final int eventsCacheSize;
        private final int configCacheSize;
        
        public CacheStatistics(long hits, long misses, long evictions,
                             int groupsCacheSize, int playersCacheSize,
                             int eventsCacheSize, int configCacheSize) {
            this.hits = hits;
            this.misses = misses;
            this.evictions = evictions;
            this.groupsCacheSize = groupsCacheSize;
            this.playersCacheSize = playersCacheSize;
            this.eventsCacheSize = eventsCacheSize;
            this.configCacheSize = configCacheSize;
        }
        
        public double getHitRatio() {
            long total = hits + misses;
            return total > 0 ? (double) hits / total : 0.0;
        }
        
        // Геттеры
        public long getHits() { return hits; }
        public long getMisses() { return misses; }
        public long getEvictions() { return evictions; }
        public int getGroupsCacheSize() { return groupsCacheSize; }
        public int getPlayersCacheSize() { return playersCacheSize; }
        public int getEventsCacheSize() { return eventsCacheSize; }
        public int getConfigCacheSize() { return configCacheSize; }
        
        @Override
        public String toString() {
            return String.format("CacheStats{hits=%d, misses=%d, hitRatio=%.2f%%, evictions=%d, " +
                "sizes=[groups=%d, players=%d, events=%d, config=%d]}", 
                hits, misses, getHitRatio() * 100, evictions,
                groupsCacheSize, playersCacheSize, eventsCacheSize, configCacheSize);
        }
    }
}

/**
 * Данные игрока для кеширования
 */
class PlayerCacheData {
    private final int playerId;
    private final Map<Integer, Long> lastRewardTimes;
    private final Set<Integer> receivedOnceOnlyRewards;
    private final double progressiveMultiplier;
    private final long lastUpdate;
    
    public PlayerCacheData(int playerId, Map<Integer, Long> lastRewardTimes,
                          Set<Integer> receivedOnceOnlyRewards, double progressiveMultiplier) {
        this.playerId = playerId;
        this.lastRewardTimes = new HashMap<>(lastRewardTimes);
        this.receivedOnceOnlyRewards = new HashSet<>(receivedOnceOnlyRewards);
        this.progressiveMultiplier = progressiveMultiplier;
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public PlayerCacheData copy() {
        return new PlayerCacheData(playerId, lastRewardTimes, 
            receivedOnceOnlyRewards, progressiveMultiplier);
    }
    
    // Геттеры
    public int getPlayerId() { return playerId; }
    public Map<Integer, Long> getLastRewardTimes() { return new HashMap<>(lastRewardTimes); }
    public Set<Integer> getReceivedOnceOnlyRewards() { return new HashSet<>(receivedOnceOnlyRewards); }
    public double getProgressiveMultiplier() { return progressiveMultiplier; }
    public long getLastUpdate() { return lastUpdate; }
}