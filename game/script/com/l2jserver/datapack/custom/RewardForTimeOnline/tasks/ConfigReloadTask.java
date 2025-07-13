package com.l2jserver.datapack.custom.RewardForTimeOnline.tasks;

import com.l2jserver.datapack.custom.RewardForTimeOnline.AdvancedRewardSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Задача автоматической перезагрузки конфигурации
 * Отслеживает изменения в файлах конфигурации и базе данных
 * @author Dafna
 */
public class ConfigReloadTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigReloadTask.class);
    
    private final AdvancedRewardSystem system;
    private final Map<String, Long> fileTimestamps;
    private final Map<String, String> fileHashes;
    private long lastDatabaseCheck;
    private long executionCount;
    private long lastSuccessfulReload;
    private int consecutiveFailures;
    
    // Файлы для отслеживания
    private static final String[] CONFIG_FILES = {
        "config/RewardsOnline.properties",
        "config/AdvancedRewards.properties",
        "config/CalendarEvents.properties"
    };
    
    public ConfigReloadTask(AdvancedRewardSystem system) {
        this.system = system;
        this.fileTimestamps = new HashMap<>();
        this.fileHashes = new HashMap<>();
        this.lastDatabaseCheck = System.currentTimeMillis();
        this.executionCount = 0;
        this.lastSuccessfulReload = System.currentTimeMillis();
        this.consecutiveFailures = 0;
        
        // Инициализируем начальные состояния файлов
        initializeFileStates();
    }
    
    @Override
    public void run() {
        if (system.isShuttingDown()) {
            return;
        }
        
        executionCount++;
        
        try {
            LOG.trace("Starting config reload check #{}", executionCount);
            
            boolean needsReload = false;
            String reloadReason = "";
            
            // 1. Проверяем изменения в файлах конфигурации
            if (checkConfigurationFiles()) {
                needsReload = true;
                reloadReason = "Configuration file changes detected";
            }
            
            // 2. Проверяем изменения в базе данных
            if (checkDatabaseChanges()) {
                needsReload = true;
                reloadReason = reloadReason.isEmpty() ? "Database configuration changes detected" 
                    : reloadReason + " and database changes";
            }
            
            // 3. Проверяем календарные события
            if (checkCalendarEvents()) {
                needsReload = true;
                reloadReason = reloadReason.isEmpty() ? "Calendar events updated" 
                    : reloadReason + " and calendar events";
            }
            
            // 4. Выполняем перезагрузку если необходимо
            if (needsReload) {
                performConfigurationReload(reloadReason);
            }
            
            // 5. Проверяем устаревшие кэши
            checkStaleCache();
            
            consecutiveFailures = 0; // Сбрасываем счетчик ошибок при успешном выполнении
            
        } catch (Exception e) {
            consecutiveFailures++;
            LOG.error("Error during config reload check #{}: {}", executionCount, e.getMessage());
            
            // Если слишком много последовательных ошибок, увеличиваем интервал проверки
            if (consecutiveFailures > 5) {
                LOG.warn("Too many consecutive failures ({}), considering system health issues", 
                    consecutiveFailures);
            }
        }
    }
    
    /**
     * Инициализирует начальные состояния файлов
     */
    private void initializeFileStates() {
        for (String configFile : CONFIG_FILES) {
            updateFileState(configFile);
        }
        LOG.debug("Initialized file states for {} configuration files", CONFIG_FILES.length);
    }
    
    /**
     * Проверяет изменения в файлах конфигурации
     */
    private boolean checkConfigurationFiles() {
        boolean hasChanges = false;
        
        for (String configFile : CONFIG_FILES) {
            try {
                Path filePath = Paths.get(configFile);
                
                if (!Files.exists(filePath)) {
                    continue; // Файл может не существовать - это нормально
                }
                
                long currentTimestamp = Files.getLastModifiedTime(filePath).toMillis();
                Long previousTimestamp = fileTimestamps.get(configFile);
                
                if (previousTimestamp == null || currentTimestamp > previousTimestamp) {
                    LOG.info("Configuration file changed: {} (timestamp: {} -> {})", 
                        configFile, previousTimestamp, currentTimestamp);
                    
                    updateFileState(configFile);
                    hasChanges = true;
                }
                
            } catch (Exception e) {
                LOG.warn("Error checking file {}: {}", configFile, e.getMessage());
            }
        }
        
        return hasChanges;
    }
    
    /**
     * Обновляет состояние файла (timestamp и hash)
     */
    private void updateFileState(String configFile) {
        try {
            Path filePath = Paths.get(configFile);
            
            if (Files.exists(filePath)) {
                long timestamp = Files.getLastModifiedTime(filePath).toMillis();
                fileTimestamps.put(configFile, timestamp);
                
                // Можно добавить проверку hash для более точного определения изменений
                // String hash = calculateFileHash(filePath);
                // fileHashes.put(configFile, hash);
            } else {
                fileTimestamps.remove(configFile);
                fileHashes.remove(configFile);
            }
            
        } catch (Exception e) {
            LOG.warn("Error updating file state for {}: {}", configFile, e.getMessage());
        }
    }
    
    /**
     * Проверяет изменения в базе данных
     */
    private boolean checkDatabaseChanges() {
        try {
            // Проверяем только если прошло достаточно времени с последней проверки
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDatabaseCheck < TimeUnit.MINUTES.toMillis(2)) {
                return false;
            }
            
            lastDatabaseCheck = currentTime;
            
            // Проверяем изменения в БД через систему
            boolean hasChanges = system.getDatabase().hasConfigurationChanged();
            
            if (hasChanges) {
                LOG.info("Database configuration changes detected");
            }
            
            return hasChanges;
            
        } catch (Exception e) {
            LOG.warn("Error checking database changes: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверяет обновления календарных событий
     */
    private boolean checkCalendarEvents() {
        try {
            // Проверяем только каждые 5 выполнений
            if (executionCount % 5 != 0) {
                return false;
            }
            
            return system.getCalendarManager().hasEventsChanged();
            
        } catch (Exception e) {
            LOG.warn("Error checking calendar events: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Выполняет перезагрузку конфигурации
     */
    private void performConfigurationReload(String reason) {
        try {
            LOG.info("Performing automatic configuration reload: {}", reason);
            long startTime = System.currentTimeMillis();
            
            // Выполняем перезагрузку через основную систему
            system.reloadConfiguration();
            
            long reloadTime = System.currentTimeMillis() - startTime;
            lastSuccessfulReload = System.currentTimeMillis();
            
            LOG.info("Configuration reload completed successfully in {}ms", reloadTime);
            
            // Уведомляем администратора если перезагрузка заняла много времени
            if (reloadTime > 10000) { // Более 10 секунд
                LOG.warn("Configuration reload took unusually long: {}ms", reloadTime);
            }
            
        } catch (Exception e) {
            LOG.error("Failed to reload configuration: {}", e.getMessage(), e);
            consecutiveFailures++;
        }
    }
    
    /**
     * Проверяет устаревшие кэши
     */
    private void checkStaleCache() {
        try {
            // Проверяем только каждые 10 выполнений
            if (executionCount % 10 != 0) {
                return;
            }
            
            // Проверяем кэш ItemData
            var itemDataInfo = system.getItemDataWrapper().getSystemInfo();
            if (itemDataInfo.getCacheHitRatio() < 50 && itemDataInfo.getCacheHits() > 100) {
                LOG.info("ItemData cache hit ratio is low ({:.1f}%), consider warming up cache", 
                    itemDataInfo.getCacheHitRatio());
            }
            
            // Проверяем кэши других компонентов
            system.getProgressiveManager().checkCacheHealth();
            
        } catch (Exception e) {
            LOG.debug("Error during cache check: {}", e.getMessage());
        }
    }
    
    /**
     * Принудительная проверка всех источников конфигурации
     */
    public boolean forceCheck() {
        try {
            LOG.info("Performing forced configuration check");
            
            boolean needsReload = checkConfigurationFiles() || 
                                checkDatabaseChanges() || 
                                checkCalendarEvents();
            
            if (needsReload) {
                performConfigurationReload("Forced configuration check");
                return true;
            } else {
                LOG.info("No configuration changes detected during forced check");
                return false;
            }
            
        } catch (Exception e) {
            LOG.error("Error during forced configuration check: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Получает статистику задачи
     */
    public ConfigReloadStatistics getStatistics() {
        return new ConfigReloadStatistics(
            executionCount,
            lastSuccessfulReload,
            consecutiveFailures,
            fileTimestamps.size(),
            lastDatabaseCheck
        );
    }
    
    /**
     * Статистика задачи перезагрузки конфигурации
     */
    public static class ConfigReloadStatistics {
        private final long executionCount;
        private final long lastSuccessfulReload;
        private final int consecutiveFailures;
        private final int trackedFiles;
        private final long lastDatabaseCheck;
        
        public ConfigReloadStatistics(long executionCount, long lastSuccessfulReload, 
                                    int consecutiveFailures, int trackedFiles, long lastDatabaseCheck) {
            this.executionCount = executionCount;
            this.lastSuccessfulReload = lastSuccessfulReload;
            this.consecutiveFailures = consecutiveFailures;
            this.trackedFiles = trackedFiles;
            this.lastDatabaseCheck = lastDatabaseCheck;
        }
        
        public long getExecutionCount() { return executionCount; }
        public long getLastSuccessfulReload() { return lastSuccessfulReload; }
        public int getConsecutiveFailures() { return consecutiveFailures; }
        public int getTrackedFiles() { return trackedFiles; }
        public long getLastDatabaseCheck() { return lastDatabaseCheck; }
        
        public String getTimeSinceLastReload() {
            long timeSince = System.currentTimeMillis() - lastSuccessfulReload;
            long hours = TimeUnit.MILLISECONDS.toHours(timeSince);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeSince) % 60;
            
            if (hours > 0) {
                return String.format("%dh %dm", hours, minutes);
            } else {
                return String.format("%dm", minutes);
            }
        }
        
        public boolean isHealthy() {
            return consecutiveFailures < 3;
        }
        
        @Override
        public String toString() {
            return String.format("ConfigReloadStats{executions=%d, failures=%d, lastReload=%s, healthy=%s}", 
                executionCount, consecutiveFailures, getTimeSinceLastReload(), isHealthy());
        }
    }
}