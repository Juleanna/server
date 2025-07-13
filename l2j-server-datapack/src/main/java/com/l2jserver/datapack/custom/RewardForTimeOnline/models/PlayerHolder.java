package com.l2jserver.datapack.custom.RewardForTimeOnline.models;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.datapack.custom.RewardForTimeOnline.tasks.EnhancedRewardTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Держатель информации о игроке и его наградах
 * @author Dafna
 */
public class PlayerHolder {
    private static final Logger LOG = LoggerFactory.getLogger(PlayerHolder.class);
    
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
        
        LOG.debug("Created PlayerHolder for {}", player.getName());
    }
    
    /**
     * Добавляет задачу награждения
     */
    public void addRewardTask(EnhancedRewardTask task) {
        if (task != null && task.isValid()) {
            rewardTasks.add(task);
            LOG.debug("Added reward task for player {} (total: {})", 
                player.getName(), rewardTasks.size());
        }
    }
    
    /**
     * Приостанавливает все награды (например, при AFK)
     */
    public void pauseRewards() {
        if (!rewardsPaused) {
            rewardsPaused = true;
            LOG.debug("Paused rewards for player {}", player.getName());
        }
    }
    
    /**
     * Возобновляет награды
     */
    public void resumeRewards() {
        if (rewardsPaused) {
            rewardsPaused = false;
            LOG.debug("Resumed rewards for player {}", player.getName());
        }
    }
    
    /**
     * Обработка выхода игрока
     */
    public void onPlayerLogout() {
        isActive = false;
        
        // Корректно завершаем все задачи
        for (EnhancedRewardTask task : rewardTasks) {
            try {
                task.onPlayerLogout();
            } catch (Exception e) {
                LOG.warn("Error during logout processing for player {}: {}", 
                    player.getName(), e.getMessage());
            }
        }
        
        long sessionTime = System.currentTimeMillis() - loginTime;
        LOG.info("Player {} session ended, duration: {} minutes, rewards received: {}", 
            player.getName(), TimeUnit.MILLISECONDS.toMinutes(sessionTime), rewardsReceived.get());
    }
    
    /**
     * Проверяет активность игрока
     */
    public boolean isActive() {
        return isActive && player != null && player.isOnline();
    }
    
    /**
     * Проверяет доступность наград
     */
    public boolean areRewardsAvailable() {
        return isActive() && !rewardsPaused;
    }
    
    /**
     * Увеличивает счетчик полученных наград
     */
    public void incrementRewardsReceived() {
        rewardsReceived.incrementAndGet();
    }
    
    /**
     * Получает статистику сессии
     */
    public PlayerSessionStats getSessionStats() {
        long currentTime = System.currentTimeMillis();
        long sessionDuration = currentTime - loginTime;
        
        return new PlayerSessionStats(
            player.getName(),
            player.getLevel(),
            loginTime,
            sessionDuration,
            rewardsReceived.get(),
            rewardTasks.size(),
            isActive,
            rewardsPaused
        );
    }
    
    /**
     * Статистика сессии игрока
     */
    public static class PlayerSessionStats {
        private final String playerName;
        private final int playerLevel;
        private final long loginTime;
        private final long sessionDuration;
        private final long rewardsReceived;
        private final int activeRewardTasks;
        private final boolean isActive;
        private final boolean rewardsPaused;
        
        public PlayerSessionStats(String playerName, int playerLevel, long loginTime, 
                                long sessionDuration, long rewardsReceived, int activeRewardTasks,
                                boolean isActive, boolean rewardsPaused) {
            this.playerName = playerName;
            this.playerLevel = playerLevel;
            this.loginTime = loginTime;
            this.sessionDuration = sessionDuration;
            this.rewardsReceived = rewardsReceived;
            this.activeRewardTasks = activeRewardTasks;
            this.isActive = isActive;
            this.rewardsPaused = rewardsPaused;
        }
        
        // Геттеры
        public String getPlayerName() { return playerName; }
        public int getPlayerLevel() { return playerLevel; }
        public long getLoginTime() { return loginTime; }
        public long getSessionDuration() { return sessionDuration; }
        public long getRewardsReceived() { return rewardsReceived; }
        public int getActiveRewardTasks() { return activeRewardTasks; }
        public boolean isActive() { return isActive; }
        public boolean isRewardsPaused() { return rewardsPaused; }
        
        public String getFormattedSessionDuration() {
            long hours = TimeUnit.MILLISECONDS.toHours(sessionDuration);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionDuration) % 60;
            return String.format("%dh %dm", hours, minutes);
        }
        
        @Override
        public String toString() {
            return String.format("PlayerSession{name='%s', level=%d, duration=%s, rewards=%d, active=%s}", 
                playerName, playerLevel, getFormattedSessionDuration(), rewardsReceived, isActive);
        }
    }
    
    // Геттеры
    public L2PcInstance getPlayer() {
        return player;
    }
    
    public long getOnlineTime() {
        return System.currentTimeMillis() - loginTime;
    }
    
    public long getRewardsReceived() {
        return rewardsReceived.get();
    }
    
    public boolean areRewardsPaused() {
        return rewardsPaused;
    }
    
    public int getActiveTasksCount() {
        return rewardTasks.size();
    }
    
    public List<EnhancedRewardTask> getRewardTasks() {
        return new ArrayList<>(rewardTasks);
    }
    
    @Override
    public String toString() {
        return String.format("PlayerHolder{player='%s', tasks=%d, active=%s, paused=%s}", 
            player.getName(), rewardTasks.size(), isActive, rewardsPaused);
    }
}