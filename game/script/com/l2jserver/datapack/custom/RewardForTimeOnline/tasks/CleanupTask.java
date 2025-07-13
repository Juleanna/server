package com.l2jserver.datapack.custom.RewardForTimeOnline.tasks;

import com.l2jserver.datapack.custom.RewardForTimeOnline.AdvancedRewardSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Задача периодической очистки системы
 * Выполняет обслуживание системы наград
 * @author Dafna
 */
public class CleanupTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(CleanupTask.class);
    
    private final AdvancedRewardSystem system;
    private long lastExecutionTime;
    private long executionCount;
    private long totalCleanupTime;
    
    public CleanupTask(AdvancedRewardSystem system) {
        this.system = system;
        this.lastExecutionTime = System.currentTimeMillis();
        this.executionCount = 0;
        this.totalCleanupTime = 0;
    }
    
    @Override
    public void run() {
        if (system.isShuttingDown()) {
            return;
        }
        
        long startTime = System.currentTimeMillis();
        executionCount++;
        
        try {
            LOG.debug("Starting cleanup task #{}", executionCount);
            
            // 1. Очистка неактивных игроков
            int removedPlayers = system.performCleanup();
            
            // 2. Обслуживание компонентов системы
            performSystemMaintenance();
            
            // 3. Проверка состояния системы
            performHealthCheck();
            
            // 4. Сбор статистики
            collectStatistics(startTime, removedPlayers);
            
            lastExecutionTime = System.currentTimeMillis();
            totalCleanupTime += (lastExecutionTime - startTime);
            
            if (removedPlayers > 0 || LOG.isDebugEnabled()) {
                LOG.info("Cleanup task #{} completed: removed {} players, took {}ms", 
                    executionCount, removedPlayers, (lastExecutionTime - startTime));
            }
            
        } catch (Exception e) {
            LOG.error("Error during cleanup task #{}: {}", executionCount, e.getMessage(), e);
        }
    }
    
    /**
     * Выполняет обслуживание компонентов системы
     */
    private void performSystemMaintenance() {
        try {
            // Сохранение прогрессивных множителей
            system.getProgressiveManager().saveMultipliers();
            
            // Обслуживание статистики
            system.getStatistics().performMaintenance();
            
            // Очистка кэшей ItemData
            if (executionCount % 10 == 0) { // Каждые 10 запусков
                system.getItemDataWrapper().clearCache();
                LOG.debug("ItemData cache cleared during maintenance");
            }
            
            // Обслуживание базы данных
            if (executionCount % 20 == 0) { // Каждые 20 запусков
                system.getDatabase().performMaintenance();
                LOG.debug("Database maintenance completed");
            }
            
        } catch (Exception e) {
            LOG.warn("Error during system maintenance: {}", e.getMessage());
        }
    }
    
    /**
     * Проверка состояния системы
     */
    private void performHealthCheck() {
        try {
            // Проверка доступности базы данных
            if (!system.getDatabase().isHealthy()) {
                LOG.warn("Database health check failed");
            }
            
            // Проверка ItemData системы
            if (!system.getItemDataWrapper().itemExists(57)) {
                LOG.warn("ItemData system health check failed - Adena not found");
            }
            
            // Проверка активных событий
            if (system.getCalendarManager().getActiveEvents().isEmpty() && executionCount % 50 == 0) {
                LOG.debug("No active calendar events");
            }
            
            // Проверка состояния anti-AFK системы
            var afkStats = system.getAntiAFK().getStatistics();
            if (afkStats.getAfkPercentage() > 80) {
                LOG.warn("High AFK percentage detected: {:.1f}%", afkStats.getAfkPercentage());
            }
            
        } catch (Exception e) {
            LOG.warn("Error during health check: {}", e.getMessage());
        }
    }
    
    /**
     * Сбор статистики выполнения
     */
    private void collectStatistics(long startTime, int removedPlayers) {
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Логируем детальную статистику каждые 50 выполнений
        if (executionCount % 50 == 0) {
            double avgExecutionTime = (double) totalCleanupTime / executionCount;
            long timeSinceStart = System.currentTimeMillis() - (lastExecutionTime - totalCleanupTime);
            
            LOG.info("Cleanup Statistics Summary:");
            LOG.info("- Total executions: {}", executionCount);
            LOG.info("- Average execution time: {:.2f}ms", avgExecutionTime);
            LOG.info("- Total cleanup time: {}ms", totalCleanupTime);
            LOG.info("- System uptime: {}", formatDuration(timeSinceStart));
            LOG.info("- Current execution time: {}ms", executionTime);
            LOG.info("- Players removed this run: {}", removedPlayers);
        }
        
        // Предупреждение о долгом выполнении
        if (executionTime > 5000) { // Более 5 секунд
            LOG.warn("Cleanup task #{} took unusually long: {}ms", executionCount, executionTime);
        }
    }
    
    /**
     * Форматирует продолжительность в читаемый вид
     */
    private String formatDuration(long milliseconds) {
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }
    
    /**
     * Получает статистику выполнения задачи
     */
    public CleanupStatistics getStatistics() {
        return new CleanupStatistics(
            executionCount,
            totalCleanupTime,
            executionCount > 0 ? (double) totalCleanupTime / executionCount : 0,
            lastExecutionTime
        );
    }
    
    /**
     * Статистика выполнения задачи очистки
     */
    public static class CleanupStatistics {
        private final long executionCount;
        private final long totalExecutionTime;
        private final double averageExecutionTime;
        private final long lastExecutionTime;
        
        public CleanupStatistics(long executionCount, long totalExecutionTime, 
                               double averageExecutionTime, long lastExecutionTime) {
            this.executionCount = executionCount;
            this.totalExecutionTime = totalExecutionTime;
            this.averageExecutionTime = averageExecutionTime;
            this.lastExecutionTime = lastExecutionTime;
        }
        
        public long getExecutionCount() { return executionCount; }
        public long getTotalExecutionTime() { return totalExecutionTime; }
        public double getAverageExecutionTime() { return averageExecutionTime; }
        public long getLastExecutionTime() { return lastExecutionTime; }
        
        public String getFormattedAverageTime() {
            return String.format("%.2fms", averageExecutionTime);
        }
        
        public String getTimeSinceLastExecution() {
            long timeSince = System.currentTimeMillis() - lastExecutionTime;
            return formatDuration(timeSince);
        }
        
        private String formatDuration(long milliseconds) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
            
            if (minutes > 0) {
                return String.format("%dm %ds", minutes, seconds);
            } else {
                return String.format("%ds", seconds);
            }
        }
        
        @Override
        public String toString() {
            return String.format("CleanupStats{executions=%d, avgTime=%s, lastRun=%s}", 
                executionCount, getFormattedAverageTime(), getTimeSinceLastExecution());
        }
    }
}