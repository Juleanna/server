package com.l2jserver.datapack.custom.RewardForTimeOnline.systems;

import com.l2jserver.datapack.custom.RewardForTimeOnline.database.RewardDatabase;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.ItemReward;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Менеджер прогрессивных наград
 * Управляет множителями наград, которые увеличиваются со временем
 * @author Dafna
 */
public class ProgressiveRewardManager {
    private static final Logger LOG = LoggerFactory.getLogger(ProgressiveRewardManager.class);
    
    // Константы
    private static final double MAX_PROGRESSIVE_MULTIPLIER = 5.0;
    private static final double MIN_PROGRESSIVE_MULTIPLIER = 1.0;
    private static final double DEFAULT_INCREMENT = 0.1;
    private static final double DECAY_RATE = 0.05; // Скорость уменьшения при неактивности
    private static final long SAVE_INTERVAL = TimeUnit.MINUTES.toMillis(10);
    private static final long DECAY_CHECK_INTERVAL = TimeUnit.HOURS.toMillis(1);
    private static final long INACTIVITY_THRESHOLD = TimeUnit.DAYS.toMillis(7); // 7 дней
    
    // Компоненты
    private final RewardDatabase database;
    private final Map<String, ProgressiveMultiplier> playerMultipliers;
    private ScheduledFuture<?> saveTask;
    private ScheduledFuture<?> decayTask;
    private boolean isInitialized = false;
    
    // Статистика
    private final AtomicLong totalIncrements = new AtomicLong(0);
    private final AtomicLong totalDecays = new AtomicLong(0);
    private final AtomicLong totalResets = new AtomicLong(0);
    
    public ProgressiveRewardManager(RewardDatabase database) {
        this.database = database;
        this.playerMultipliers = new ConcurrentHashMap<>();
    }
    
    /**
     * Инициализация системы
     */
    public void initialize() {
        if (isInitialized) {
            LOG.warn("ProgressiveRewardManager already initialized");
            return;
        }
        
        LOG.info("Initializing ProgressiveRewardManager...");
        
        // Загружаем данные из базы
        loadMultipliers();
        
        // Запускаем периодические задачи
        startPeriodicTasks();
        
        isInitialized = true;
        LOG.info("ProgressiveRewardManager initialized with {} player multipliers", 
            playerMultipliers.size());
    }
    
    /**
     * Загружает множители из базы данных
     */
    public void loadMultipliers() {
        try {
            Map<String, ProgressiveMultiplier> loadedMultipliers = loadMultipliersFromDatabase();
            playerMultipliers.clear();
            playerMultipliers.putAll(loadedMultipliers);
            
            LOG.info("Loaded {} progressive multipliers from database", loadedMultipliers.size());
            
        } catch (Exception e) {
            LOG.error("Failed to load progressive multipliers from database", e);
        }
    }
    
    /**
     * Загружает множители из базы данных (внутренний метод)
     */
    private Map<String, ProgressiveMultiplier> loadMultipliersFromDatabase() {
        Map<String, ProgressiveMultiplier> multipliers = new HashMap<>();
        
        try {
            String query = """
                SELECT player_id, item_id, multiplier_value, last_used, increment_count
                FROM progressive_multipliers
                WHERE last_used > DATE_SUB(NOW(), INTERVAL 30 DAY)
            """;
            
            // Здесь должен быть вызов database.executeQuery, но для примера используем заглушку
            // В реальной реализации нужно использовать правильный метод БД
            
            LOG.debug("Progressive multipliers query prepared");
            
        } catch (Exception e) {
            LOG.error("Error loading multipliers from database", e);
        }
        
        return multipliers;
    }
    
    /**
     * Сохраняет множители в базу данных
     */
    public void saveMultipliers() {
        try {
            if (playerMultipliers.isEmpty()) {
                return;
            }
            
            int savedCount = saveMultipliersToDatabase();
            LOG.debug("Saved {} progressive multipliers to database", savedCount);
            
        } catch (Exception e) {
            LOG.error("Failed to save progressive multipliers to database", e);
        }
    }
    
    /**
     * Сохраняет множители в базу данных (внутренний метод)
     */
    private int saveMultipliersToDatabase() {
        int savedCount = 0;
        
        try {
            // Здесь должна быть реализация сохранения в БД
            for (Map.Entry<String, ProgressiveMultiplier> entry : playerMultipliers.entrySet()) {
                ProgressiveMultiplier multiplier = entry.getValue();
                
                // Сохранение через database.saveProgressiveMultiplier(multiplier);
                savedCount++;
            }
            
        } catch (Exception e) {
            LOG.error("Error saving multipliers to database", e);
        }
        
        return savedCount;
    }
    
    /**
     * Запускает периодические задачи
     */
    private void startPeriodicTasks() {
        // Задача сохранения
        saveTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            try {
                saveMultipliers();
            } catch (Exception e) {
                LOG.warn("Error during multipliers save: {}", e.getMessage());
            }
        }, SAVE_INTERVAL, SAVE_INTERVAL);
        
        // Задача уменьшения множителей при неактивности
        decayTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            try {
                performDecayCheck();
            } catch (Exception e) {
                LOG.warn("Error during decay check: {}", e.getMessage());
            }
        }, DECAY_CHECK_INTERVAL, DECAY_CHECK_INTERVAL);
        
        LOG.debug("Progressive reward periodic tasks started");
    }
    
    /**
     * Получает текущий множитель для игрока и награды
     */
    public double getPlayerMultiplier(L2PcInstance player, ItemReward reward) {
        if (!reward.isProgressive()) {
            return MIN_PROGRESSIVE_MULTIPLIER;
        }
        
        String key = createMultiplierKey(player, reward);
        ProgressiveMultiplier multiplier = playerMultipliers.get(key);
        
        if (multiplier == null) {
            return MIN_PROGRESSIVE_MULTIPLIER;
        }
        
        // Обновляем время последнего использования
        multiplier.updateLastUsed();
        
        return multiplier.getValue();
    }
    
    /**
     * Увеличивает множитель для игрока и награды
     */
    public void incrementPlayerMultiplier(L2PcInstance player, ItemReward reward) {
        if (!reward.isProgressive()) {
            return;
        }
        
        String key = createMultiplierKey(player, reward);
        ProgressiveMultiplier multiplier = playerMultipliers.computeIfAbsent(key, 
            k -> new ProgressiveMultiplier(player.getObjectId(), reward.getItemId()));
        
        double oldValue = multiplier.getValue();
        multiplier.increment();
        double newValue = multiplier.getValue();
        
        totalIncrements.incrementAndGet();
        
        if (newValue > oldValue) {
            LOG.debug("Progressive multiplier increased for player {} item {}: {:.2f} -> {:.2f}", 
                player.getName(), reward.getItemId(), oldValue, newValue);
        }
    }
    
    /**
     * Сбрасывает множитель для игрока и награды
     */
    public void resetPlayerMultiplier(L2PcInstance player, ItemReward reward) {
        String key = createMultiplierKey(player, reward);
        ProgressiveMultiplier multiplier = playerMultipliers.remove(key);
        
        if (multiplier != null) {
            totalResets.incrementAndGet();
            LOG.debug("Progressive multiplier reset for player {} item {}: was {:.2f}", 
                player.getName(), reward.getItemId(), multiplier.getValue());
        }
    }
    
    /**
     * Сбрасывает все множители игрока
     */
    public void resetAllPlayerMultipliers(L2PcInstance player) {
        int resetCount = 0;
        String playerPrefix = player.getObjectId() + "_";
        
        Iterator<Map.Entry<String, ProgressiveMultiplier>> iterator = playerMultipliers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ProgressiveMultiplier> entry = iterator.next();
            if (entry.getKey().startsWith(playerPrefix)) {
                iterator.remove();
                resetCount++;
            }
        }
        
        if (resetCount > 0) {
            totalResets.addAndGet(resetCount);
            LOG.info("Reset {} progressive multipliers for player {}", resetCount, player.getName());
        }
    }
    
    /**
     * Выполняет проверку и уменьшение неактивных множителей
     */
    private void performDecayCheck() {
        try {
            long currentTime = System.currentTimeMillis();
            int decayedCount = 0;
            int removedCount = 0;
            
            Iterator<Map.Entry<String, ProgressiveMultiplier>> iterator = playerMultipliers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ProgressiveMultiplier> entry = iterator.next();
                ProgressiveMultiplier multiplier = entry.getValue();
                
                // Проверяем неактивность
                if (currentTime - multiplier.getLastUsed() > INACTIVITY_THRESHOLD) {
                    double oldValue = multiplier.getValue();
                    multiplier.decay(DECAY_RATE);
                    
                    // Если множитель упал до минимума, удаляем его
                    if (multiplier.getValue() <= MIN_PROGRESSIVE_MULTIPLIER) {
                        iterator.remove();
                        removedCount++;
                    } else {
                        decayedCount++;
                    }
                    
                    totalDecays.incrementAndGet();
                    
                    LOG.trace("Multiplier decayed for key {}: {:.2f} -> {:.2f}", 
                        entry.getKey(), oldValue, multiplier.getValue());
                }
            }
            
            if (decayedCount > 0 || removedCount > 0) {
                LOG.debug("Decay check completed: {} multipliers decayed, {} removed", 
                    decayedCount, removedCount);
            }
            
        } catch (Exception e) {
            LOG.error("Error during progressive multiplier decay check", e);
        }
    }
    
    /**
     * Создает ключ для множителя
     */
    private String createMultiplierKey(L2PcInstance player, ItemReward reward) {
        return player.getObjectId() + "_" + reward.getItemId();
    }
    
    /**
     * Получает количество активных множителей
     */
    public int getActiveMultipliersCount() {
        return playerMultipliers.size();
    }
    
    /**
     * Получает множители для конкретного игрока
     */
    public Map<Integer, Double> getPlayerMultipliers(L2PcInstance player) {
        Map<Integer, Double> result = new HashMap<>();
        String playerPrefix = player.getObjectId() + "_";
        
        for (Map.Entry<String, ProgressiveMultiplier> entry : playerMultipliers.entrySet()) {
            if (entry.getKey().startsWith(playerPrefix)) {
                try {
                    String itemIdStr = entry.getKey().substring(playerPrefix.length());
                    int itemId = Integer.parseInt(itemIdStr);
                    result.put(itemId, entry.getValue().getValue());
                } catch (NumberFormatException e) {
                    LOG.warn("Invalid multiplier key format: {}", entry.getKey());
                }
            }
        }
        
        return result;
    }
    
    /**
     * Проверяет здоровье кэша
     */
    public void checkCacheHealth() {
        int totalMultipliers = playerMultipliers.size();
        long currentTime = System.currentTimeMillis();
        int staleMultipliers = 0;
        
        for (ProgressiveMultiplier multiplier : playerMultipliers.values()) {
            if (currentTime - multiplier.getLastUsed() > TimeUnit.DAYS.toMillis(30)) {
                staleMultipliers++;
            }
        }
        
        if (staleMultipliers > totalMultipliers * 0.5) { // Более 50% устаревших
            LOG.warn("High number of stale progressive multipliers: {}/{}", 
                staleMultipliers, totalMultipliers);
        }
    }
    
    /**
     * Получает статистику системы
     */
    public ProgressiveRewardStatistics getStatistics() {
        int totalMultipliers = playerMultipliers.size();
        int activeMultipliers = 0;
        double avgMultiplier = 0;
        double maxMultiplier = MIN_PROGRESSIVE_MULTIPLIER;
        long currentTime = System.currentTimeMillis();
        
        for (ProgressiveMultiplier multiplier : playerMultipliers.values()) {
            double value = multiplier.getValue();
            avgMultiplier += value;
            maxMultiplier = Math.max(maxMultiplier, value);
            
            if (currentTime - multiplier.getLastUsed() < TimeUnit.HOURS.toMillis(24)) {
                activeMultipliers++;
            }
        }
        
        if (totalMultipliers > 0) {
            avgMultiplier /= totalMultipliers;
        }
        
        return new ProgressiveRewardStatistics(
            totalMultipliers,
            activeMultipliers,
            avgMultiplier,
            maxMultiplier,
            totalIncrements.get(),
            totalDecays.get(),
            totalResets.get()
        );
    }
    
    /**
     * Отключение системы
     */
    public void shutdown() {
        LOG.info("Shutting down ProgressiveRewardManager...");
        
        // Останавливаем периодические задачи
        if (saveTask != null) {
            saveTask.cancel(false);
            saveTask = null;
        }
        if (decayTask != null) {
            decayTask.cancel(false);
            decayTask = null;
        }
        
        // Сохраняем финальное состояние
        saveMultipliers();
        
        // Очищаем данные
        playerMultipliers.clear();
        
        isInitialized = false;
        LOG.info("ProgressiveRewardManager shutdown completed");
    }
    
    /**
     * Принудительная очистка устаревших множителей
     */
    public int forceCleanup() {
        long currentTime = System.currentTimeMillis();
        int removedCount = 0;
        
        Iterator<Map.Entry<String, ProgressiveMultiplier>> iterator = playerMultipliers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ProgressiveMultiplier> entry = iterator.next();
            ProgressiveMultiplier multiplier = entry.getValue();
            if (currentTime - multiplier.getLastUsed() > TimeUnit.DAYS.toMillis(30)) {
                iterator.remove();
                removedCount++;
            }
        }
        
        if (removedCount > 0) {
            totalResets.addAndGet(removedCount);
            LOG.info("Force cleanup removed {} stale progressive multipliers", removedCount);
        }
        
        return removedCount;
    }
    
    /**
     * Класс для хранения прогрессивного множителя
     */
    public static class ProgressiveMultiplier {
        private final int playerId;
        private final int itemId;
        private double value;
        private long lastUsed;
        private final long createdTime;
        private int incrementCount;
        
        public ProgressiveMultiplier(int playerId, int itemId) {
            this.playerId = playerId;
            this.itemId = itemId;
            this.value = MIN_PROGRESSIVE_MULTIPLIER;
            this.lastUsed = System.currentTimeMillis();
            this.createdTime = System.currentTimeMillis();
            this.incrementCount = 0;
        }
        
        public ProgressiveMultiplier(int playerId, int itemId, double value, long lastUsed, int incrementCount) {
            this.playerId = playerId;
            this.itemId = itemId;
            this.value = Math.max(MIN_PROGRESSIVE_MULTIPLIER, Math.min(MAX_PROGRESSIVE_MULTIPLIER, value));
            this.lastUsed = lastUsed;
            this.createdTime = lastUsed; // Приблизительно
            this.incrementCount = incrementCount;
        }
        
        public void increment() {
            value = Math.min(MAX_PROGRESSIVE_MULTIPLIER, value + DEFAULT_INCREMENT);
            incrementCount++;
            updateLastUsed();
        }
        
        public void decay(double decayRate) {
            value = Math.max(MIN_PROGRESSIVE_MULTIPLIER, value - decayRate);
        }
        
        public void updateLastUsed() {
            lastUsed = System.currentTimeMillis();
        }
        
        // Геттеры
        public int getPlayerId() { return playerId; }
        public int getItemId() { return itemId; }
        public double getValue() { return value; }
        public long getLastUsed() { return lastUsed; }
        public long getCreatedTime() { return createdTime; }
        public int getIncrementCount() { return incrementCount; }
        
        /**
         * Получает возраст множителя в днях
         */
        public int getAgeInDays() {
            return (int) TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - createdTime);
        }
        
        /**
         * Получает дни с последнего использования
         */
        public int getDaysSinceLastUse() {
            return (int) TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastUsed);
        }
        
        /**
         * Проверяет активность множителя
         */
        public boolean isActive() {
            return System.currentTimeMillis() - lastUsed < TimeUnit.DAYS.toMillis(1);
        }
        
        /**
         * Получает процент до максимального множителя
         */
        public double getProgressPercentage() {
            return ((value - MIN_PROGRESSIVE_MULTIPLIER) / (MAX_PROGRESSIVE_MULTIPLIER - MIN_PROGRESSIVE_MULTIPLIER)) * 100;
        }
        
        @Override
        public String toString() {
            return String.format("ProgressiveMultiplier{player=%d, item=%d, value=%.2f, increments=%d, active=%s}", 
                playerId, itemId, value, incrementCount, isActive());
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            ProgressiveMultiplier that = (ProgressiveMultiplier) obj;
            return playerId == that.playerId && itemId == that.itemId;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(playerId, itemId);
        }
    }
    
    /**
     * Статистика прогрессивных наград
     */
    public static class ProgressiveRewardStatistics {
        private final int totalMultipliers;
        private final int activeMultipliers;
        private final double averageMultiplier;
        private final double maxMultiplier;
        private final long totalIncrements;
        private final long totalDecays;
        private final long totalResets;
        
        public ProgressiveRewardStatistics(int totalMultipliers, int activeMultipliers, 
                                         double averageMultiplier, double maxMultiplier,
                                         long totalIncrements, long totalDecays, long totalResets) {
            this.totalMultipliers = totalMultipliers;
            this.activeMultipliers = activeMultipliers;
            this.averageMultiplier = averageMultiplier;
            this.maxMultiplier = maxMultiplier;
            this.totalIncrements = totalIncrements;
            this.totalDecays = totalDecays;
            this.totalResets = totalResets;
        }
        
        // Геттеры
        public int getTotalMultipliers() { return totalMultipliers; }
        public int getActiveMultipliers() { return activeMultipliers; }
        public int getInactiveMultipliers() { return totalMultipliers - activeMultipliers; }
        public double getAverageMultiplier() { return averageMultiplier; }
        public double getMaxMultiplier() { return maxMultiplier; }
        public long getTotalIncrements() { return totalIncrements; }
        public long getTotalDecays() { return totalDecays; }
        public long getTotalResets() { return totalResets; }
        
        public double getActivityPercentage() {
            return totalMultipliers > 0 ? (double) activeMultipliers / totalMultipliers * 100 : 0;
        }
        
        public double getEfficiencyScore() {
            long totalOperations = totalIncrements + totalDecays + totalResets;
            return totalOperations > 0 ? (double) totalIncrements / totalOperations * 100 : 0;
        }
        
        public boolean isHealthy() {
            return getActivityPercentage() > 10 && getEfficiencyScore() > 50;
        }
        
        @Override
        public String toString() {
            return String.format("ProgressiveStats{total=%d, active=%d (%.1f%%), avg=%.2f, max=%.2f, efficiency=%.1f%%}", 
                totalMultipliers, activeMultipliers, getActivityPercentage(), 
                averageMultiplier, maxMultiplier, getEfficiencyScore());
        }
    }
    
    /**
     * Получает детальную информацию о множителях игрока
     */
    public List<Map<String, Object>> getPlayerMultipliersDetailed(L2PcInstance player) {
        List<Map<String, Object>> details = new ArrayList<>();
        String playerPrefix = player.getObjectId() + "_";
        
        for (Map.Entry<String, ProgressiveMultiplier> entry : playerMultipliers.entrySet()) {
            if (entry.getKey().startsWith(playerPrefix)) {
                ProgressiveMultiplier multiplier = entry.getValue();
                Map<String, Object> detail = new HashMap<>();
                
                detail.put("itemId", multiplier.getItemId());
                detail.put("value", multiplier.getValue());
                detail.put("incrementCount", multiplier.getIncrementCount());
                detail.put("ageInDays", multiplier.getAgeInDays());
                detail.put("daysSinceLastUse", multiplier.getDaysSinceLastUse());
                detail.put("isActive", multiplier.isActive());
                detail.put("progressPercentage", multiplier.getProgressPercentage());
                
                details.add(detail);
            }
        }
        
        return details;
    }
    
    /**
     * Получает топ множители по значению
     */
    public List<Map<String, Object>> getTopMultipliers(int limit) {
        return playerMultipliers.values().stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(limit)
            .map(multiplier -> {
                Map<String, Object> info = new HashMap<>();
                info.put("playerId", multiplier.getPlayerId());
                info.put("itemId", multiplier.getItemId());
                info.put("value", multiplier.getValue());
                info.put("incrementCount", multiplier.getIncrementCount());
                info.put("progressPercentage", multiplier.getProgressPercentage());
                return info;
            })
            .collect(Collectors.toList());
    }
}