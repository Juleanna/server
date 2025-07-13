package com.l2jserver.datapack.custom.RewardForTimeOnline.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Система сбора и анализа статистики наград
 * Отслеживает все метрики системы наградов
 * @author Dafna
 */
public class RewardStatistics {
    private static final Logger LOG = LoggerFactory.getLogger(RewardStatistics.class);
    
    // Основные счетчики
    private final AtomicLong totalRewardsGiven = new AtomicLong(0);
    private final AtomicLong totalPlayersProcessed = new AtomicLong(0);
    private final AtomicLong totalOnlineTime = new AtomicLong(0);
    private final AtomicLong totalSessions = new AtomicLong(0);
    private final long startTime;
    
    // Детальная статистика
    private final Map<String, AtomicLong> rewardsByGroup = new ConcurrentHashMap<>();
    private final Map<Integer, AtomicLong> rewardsByItem = new ConcurrentHashMap<>();
    private final Map<Integer, AtomicLong> rewardsByPlayer = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> dailyRewards = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> hourlyDistribution = new ConcurrentHashMap<>();
    
    // Производительность
    private final AtomicLong totalRewardTime = new AtomicLong(0);
    private final AtomicLong slowRewardsCount = new AtomicLong(0);
    private final AtomicLong failedRewardsCount = new AtomicLong(0);
    
    // Топ списки (ограниченные по размеру)
    private final Map<Integer, PlayerStats> topPlayers = new ConcurrentHashMap<>();
    private final Map<Integer, ItemStats> topItems = new ConcurrentHashMap<>();
    
    private boolean isInitialized = false;
    
    public RewardStatistics() {
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Инициализация системы
     */
    public void initialize() {
        if (isInitialized) {
            LOG.warn("RewardStatistics already initialized");
            return;
        }
        
        LOG.info("Initializing RewardStatistics...");
        
        // Инициализируем часовую статистику
        for (int hour = 0; hour < 24; hour++) {
            hourlyDistribution.put(String.valueOf(hour), new AtomicLong(0));
        }
        
        isInitialized = true;
        LOG.info("RewardStatistics initialized successfully");
    }
    
    /**
     * Регистрирует выдачу награды
     */
    public void incrementRewards(String groupName, int itemId) {
        incrementRewards(groupName, itemId, 1, 0);
    }
    
    /**
     * Регистрирует выдачу награды с дополнительными параметрами
     */
    public void incrementRewards(String groupName, int itemId, long count, long executionTime) {
        // Основные счетчики
        totalRewardsGiven.incrementAndGet();
        
        // Статистика по группам
        rewardsByGroup.computeIfAbsent(groupName, k -> new AtomicLong(0)).incrementAndGet();
        
        // Статистика по предметам
        rewardsByItem.computeIfAbsent(itemId, k -> new AtomicLong(0)).addAndGet(count);
        
        // Обновляем топ предметы
        updateTopItems(itemId, count);
        
        // Дневная статистика
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dailyRewards.computeIfAbsent(today, k -> new AtomicLong(0)).incrementAndGet();
        
        // Часовая статистика
        String currentHour = String.valueOf(LocalDateTime.now().getHour());
        hourlyDistribution.computeIfAbsent(currentHour, k -> new AtomicLong(0)).incrementAndGet();
        
        // Производительность
        if (executionTime > 0) {
            totalRewardTime.addAndGet(executionTime);
            if (executionTime > 1000) { // Медленнее 1 секунды
                slowRewardsCount.incrementAndGet();
            }
        }
        
        LOG.trace("Reward statistics updated: group={}, item={}, count={}", groupName, itemId, count);
    }
    
    /**
     * Регистрирует нового игрока
     */
    public void incrementPlayers() {
        totalPlayersProcessed.incrementAndGet();
    }
    
    /**
     * Добавляет время сессии
     */
    public void addSessionTime(long timeMs) {
        totalOnlineTime.addAndGet(timeMs);
        totalSessions.incrementAndGet();
    }
    
    /**
     * Регистрирует награду для конкретного игрока
     */
    public void recordPlayerReward(int playerId, String playerName, int itemId, long count) {
        // Статистика по игрокам
        rewardsByPlayer.computeIfAbsent(playerId, k -> new AtomicLong(0)).incrementAndGet();
        
        // Обновляем топ игроков
        updateTopPlayers(playerId, playerName, count);
    }
    
    /**
     * Регистрирует неудачную выдачу награды
     */
    public void incrementFailedRewards() {
        failedRewardsCount.incrementAndGet();
    }
    
    /**
     * Обновляет топ предметов
     */
    private void updateTopItems(int itemId, long count) {
        topItems.compute(itemId, (id, stats) -> {
            if (stats == null) {
                return new ItemStats(itemId, count, 1);
            } else {
                return new ItemStats(itemId, stats.totalCount + count, stats.timesGiven + 1);
            }
        });
        
        // Ограничиваем размер топа
        if (topItems.size() > 100) {
            cleanupTopItems();
        }
    }
    
    /**
     * Обновляет топ игроков
     */
    private void updateTopPlayers(int playerId, String playerName, long rewardCount) {
        topPlayers.compute(playerId, (id, stats) -> {
            if (stats == null) {
                return new PlayerStats(playerId, playerName, 1, rewardCount);
            } else {
                return new PlayerStats(playerId, playerName, stats.totalRewards + 1, stats.totalItems + rewardCount);
            }
        });
        
        // Ограничиваем размер топа
        if (topPlayers.size() > 100) {
            cleanupTopPlayers();
        }
    }
    
    /**
     * Очищает топ предметов от наименее активных
     */
    private void cleanupTopItems() {
        if (topItems.size() <= 50) return;
        
        List<Map.Entry<Integer, ItemStats>> sorted = topItems.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().totalCount, a.getValue().totalCount))
            .collect(Collectors.toList());
        
        topItems.clear();
        sorted.stream().limit(50).forEach(entry -> topItems.put(entry.getKey(), entry.getValue()));
    }
    
    /**
     * Очищает топ игроков от наименее активных
     */
    private void cleanupTopPlayers() {
        if (topPlayers.size() <= 50) return;
        
        List<Map.Entry<Integer, PlayerStats>> sorted = topPlayers.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().totalRewards, a.getValue().totalRewards))
            .collect(Collectors.toList());
        
        topPlayers.clear();
        sorted.stream().limit(50).forEach(entry -> topPlayers.put(entry.getKey(), entry.getValue()));
    }
    
    /**
     * Получает системную статистику
     */
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long uptime = getUptime();
        long totalRewards = totalRewardsGiven.get();
        long totalPlayers = totalPlayersProcessed.get();
        
        // Основные метрики
        stats.put("uptime", TimeUnit.MILLISECONDS.toMinutes(uptime));
        stats.put("totalRewards", totalRewards);
        stats.put("totalPlayers", totalPlayers);
        stats.put("totalSessions", totalSessions.get());
        stats.put("totalOnlineTime", TimeUnit.MILLISECONDS.toHours(totalOnlineTime.get()));
        stats.put("failedRewards", failedRewardsCount.get());
        
        // Производительность
        stats.put("averageRewardsPerMinute", uptime > 0 ? (double) totalRewards / TimeUnit.MILLISECONDS.toMinutes(uptime) : 0);
        stats.put("averageRewardsPerPlayer", totalPlayers > 0 ? (double) totalRewards / totalPlayers : 0);
        stats.put("averageSessionTime", totalSessions.get() > 0 ? TimeUnit.MILLISECONDS.toMinutes(totalOnlineTime.get() / totalSessions.get()) : 0);
        
        if (totalRewardTime.get() > 0 && totalRewards > 0) {
            stats.put("averageRewardTime", (double) totalRewardTime.get() / totalRewards);
            stats.put("slowRewardsPercentage", (double) slowRewardsCount.get() / totalRewards * 100);
        }
        
        // Топ группы
        Map<String, Long> topGroups = rewardsByGroup.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get(),
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        stats.put("rewardsByGroup", topGroups);
        
        return stats;
    }
    
    /**
     * Получает детальный отчет
     */
    public String getDetailedReport() {
        long uptime = getUptime();
        long totalRewards = totalRewardsGiven.get();
        long totalPlayers = totalPlayersProcessed.get();
        
        StringBuilder report = new StringBuilder();
        report.append("=== Advanced Reward System Statistics ===\n");
        report.append(String.format("Uptime: %d minutes (%.1f hours)\n", 
            TimeUnit.MILLISECONDS.toMinutes(uptime),
            TimeUnit.MILLISECONDS.toHours(uptime) / 1.0));
        report.append(String.format("Total rewards given: %,d\n", totalRewards));
        report.append(String.format("Total players processed: %,d\n", totalPlayers));
        report.append(String.format("Total sessions: %,d\n", totalSessions.get()));
        report.append(String.format("Total online time: %,d hours\n", 
            TimeUnit.MILLISECONDS.toHours(totalOnlineTime.get())));
        
        if (totalPlayers > 0) {
            report.append(String.format("Average rewards per player: %.2f\n", 
                (double) totalRewards / totalPlayers));
        }
        
        if (uptime > 0) {
            report.append(String.format("Rewards per minute: %.2f\n", 
                (double) totalRewards / TimeUnit.MILLISECONDS.toMinutes(uptime)));
        }
        
        if (totalSessions.get() > 0) {
            report.append(String.format("Average session time: %.1f minutes\n",
                (double) TimeUnit.MILLISECONDS.toMinutes(totalOnlineTime.get()) / totalSessions.get()));
        }
        
        // Производительность
        if (totalRewardTime.get() > 0 && totalRewards > 0) {
            report.append(String.format("Average reward processing time: %.2fms\n",
                (double) totalRewardTime.get() / totalRewards));
            report.append(String.format("Slow rewards: %,d (%.2f%%)\n",
                slowRewardsCount.get(), (double) slowRewardsCount.get() / totalRewards * 100));
        }
        
        if (failedRewardsCount.get() > 0) {
            report.append(String.format("Failed rewards: %,d (%.2f%%)\n",
                failedRewardsCount.get(), (double) failedRewardsCount.get() / totalRewards * 100));
        }
        
        report.append("==========================================");
        
        return report.toString();
    }
    
    /**
     * Получает топ предметов
     */
    public List<Map<String, Object>> getTopItems(int limit) {
        return topItems.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().totalCount, a.getValue().totalCount))
            .limit(limit)
            .map(entry -> {
                Map<String, Object> item = new HashMap<>();
                item.put("itemId", entry.getKey());
                item.put("totalCount", entry.getValue().totalCount);
                item.put("timesGiven", entry.getValue().timesGiven);
                return item;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Получает топ игроков
     */
    public List<Map<String, Object>> getTopPlayers(int limit) {
        return topPlayers.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().totalRewards, a.getValue().totalRewards))
            .limit(limit)
            .map(entry -> {
                Map<String, Object> player = new HashMap<>();
                player.put("playerId", entry.getKey());
                player.put("playerName", entry.getValue().playerName);
                player.put("totalRewards", entry.getValue().totalRewards);
                player.put("totalItems", entry.getValue().totalItems);
                return player;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Получает распределение по часам
     */
    public Map<Integer, Long> getHourlyDistribution() {
        Map<Integer, Long> distribution = new HashMap<>();
        
        for (int hour = 0; hour < 24; hour++) {
            AtomicLong count = hourlyDistribution.get(String.valueOf(hour));
            distribution.put(hour, count != null ? count.get() : 0L);
        }
        
        return distribution;
    }
    
    /**
     * Получает дневную статистику за последние дни
     */
    public Map<String, Long> getDailyStatistics(int days) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Long> dailyStats = new LinkedHashMap<>();
        
        for (int i = days - 1; i >= 0; i--) {
            String date = now.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            AtomicLong count = dailyRewards.get(date);
            dailyStats.put(date, count != null ? count.get() : 0L);
        }
        
        return dailyStats;
    }
    
    /**
     * Выполняет обслуживание статистики
     */
    public void performMaintenance() {
        try {
            // Очищаем старую дневную статистику (старше 30 дней)
            LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
            String cutoffDate = cutoff.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            int removedDays = 0;
            Iterator<String> iterator = dailyRewards.keySet().iterator();
            while (iterator.hasNext()) {
                String date = iterator.next();
                if (date.compareTo(cutoffDate) < 0) {
                    iterator.remove();
                    removedDays++;
                }
            }
            
            if (removedDays > 0) {
                LOG.debug("Cleaned up {} old daily statistics entries", removedDays);
            }
            
            // Очищаем топы если они слишком большие
            cleanupTopItems();
            cleanupTopPlayers();
            
        } catch (Exception e) {
            LOG.error("Error during statistics maintenance", e);
        }
    }
    
    /**
     * Сбрасывает всю статистику
     */
    public void resetAllStatistics() {
        LOG.warn("Resetting all reward statistics");
        
        totalRewardsGiven.set(0);
        totalPlayersProcessed.set(0);
        totalOnlineTime.set(0);
        totalSessions.set(0);
        totalRewardTime.set(0);
        slowRewardsCount.set(0);
        failedRewardsCount.set(0);
        
        rewardsByGroup.clear();
        rewardsByItem.clear();
        rewardsByPlayer.clear();
        dailyRewards.clear();
        topPlayers.clear();
        topItems.clear();
        
        // Пересоздаем часовую статистику
        hourlyDistribution.clear();
        for (int hour = 0; hour < 24; hour++) {
            hourlyDistribution.put(String.valueOf(hour), new AtomicLong(0));
        }
    }
    
    /**
     * Отключение системы
     */
    public void shutdown() {
        LOG.info("Shutting down RewardStatistics...");
        LOG.info("Final statistics: {}", getDetailedReport());
        isInitialized = false;
    }
    
    // Геттеры для основных метрик
    public long getUptime() { 
        return System.currentTimeMillis() - startTime; 
    }
    
    public long getTotalRewards() { 
        return totalRewardsGiven.get(); 
    }
    
    public long getTotalSessions() { 
        return totalSessions.get(); 
    }
    
    public long getTotalPlayers() { 
        return totalPlayersProcessed.get(); 
    }
    
    public double getSuccessRate() {
        long total = totalRewardsGiven.get() + failedRewardsCount.get();
        return total > 0 ? (double) totalRewardsGiven.get() / total * 100 : 100.0;
    }
    
    /**
     * Статистика предмета
     */
    private static class ItemStats {
        final int itemId;
        final long totalCount;
        final long timesGiven;
        
        ItemStats(int itemId, long totalCount, long timesGiven) {
            this.itemId = itemId;
            this.totalCount = totalCount;
            this.timesGiven = timesGiven;
        }
    }
    
    /**
     * Статистика игрока
     */
    private static class PlayerStats {
        final int playerId;
        final String playerName;
        final long totalRewards;
        final long totalItems;
        
        PlayerStats(int playerId, String playerName, long totalRewards, long totalItems) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.totalRewards = totalRewards;
            this.totalItems = totalItems;
        }
    }
}