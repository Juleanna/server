package com.l2jserver.datapack.custom.RewardForTimeOnline.systems;

import com.l2jserver.datapack.custom.RewardForTimeOnline.models.PlayerHolder;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Anti-AFK система для определения неактивных игроков
 * @author Dafna
 */
public class AntiAFKSystem {
    private static final Logger LOG = LoggerFactory.getLogger(AntiAFKSystem.class);
    
    private static final long CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(2);
    private static final int AFK_TIMEOUT_MINUTES = 10;
    private static final int MOVEMENT_THRESHOLD = 100; // Минимальное расстояние для определения движения
    
    private final Map<Integer, AFKData> playerAFKData;
    private final Map<Integer, PlayerHolder> playerHolders;
    private ScheduledFuture<?> checkTask;
    private boolean isInitialized = false;
    
    public AntiAFKSystem() {
        this.playerAFKData = new ConcurrentHashMap<>();
        this.playerHolders = new ConcurrentHashMap<>();
    }
    
    /**
     * Инициализация системы
     */
    public void initialize() {
        if (isInitialized) {
            LOG.warn("AntiAFKSystem already initialized");
            return;
        }
        
        startAFKChecking();
        isInitialized = true;
        LOG.info("AntiAFKSystem initialized successfully");
    }
    
    /**
     * Запуск периодической проверки AFK
     */
    private void startAFKChecking() {
        checkTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            try {
                checkPlayersAFK();
            } catch (Exception e) {
                LOG.warn("Error during AFK check: {}", e.getMessage());
            }
        }, CHECK_INTERVAL, CHECK_INTERVAL);
        
        LOG.debug("AFK checking started with interval {} minutes", TimeUnit.MILLISECONDS.toMinutes(CHECK_INTERVAL));
    }
    
    /**
     * Проверка всех игроков на AFK
     */
    private void checkPlayersAFK() {
        int checkedPlayers = 0;
        int afkPlayers = 0;
        
        for (Map.Entry<Integer, PlayerHolder> entry : playerHolders.entrySet()) {
            int playerId = entry.getKey();
            PlayerHolder holder = entry.getValue();
            
            if (!holder.isActive()) {
                continue;
            }
            
            L2PcInstance player = holder.getPlayer();
            AFKData afkData = playerAFKData.get(playerId);
            
            if (afkData == null) {
                afkData = new AFKData(player);
                playerAFKData.put(playerId, afkData);
                continue;
            }
            
            checkedPlayers++;
            
            if (afkData.hasMovedFrom(player)) {
                // Игрок двигался - обновляем позицию и время
                afkData.updatePosition(player);
                if (afkData.isAFK()) {
                    afkData.setAFK(false);
                    holder.resumeRewards();
                    LOG.debug("Player {} is no longer AFK", player.getName());
                }
            } else if (afkData.isAFKByTime()) {
                // Игрок не двигался долгое время
                if (!afkData.isAFK()) {
                    afkData.setAFK(true);
                    holder.pauseRewards();
                    afkPlayers++;
                    LOG.debug("Player {} detected as AFK", player.getName());
                }
            }
        }
        
        if (checkedPlayers > 0) {
            LOG.trace("AFK check completed: {}/{} players are AFK", afkPlayers, checkedPlayers);
        }
    }
    
    /**
     * Регистрирует игрока в системе
     */
    public void registerPlayer(L2PcInstance player, PlayerHolder holder) {
        int playerId = player.getObjectId();
        playerHolders.put(playerId, holder);
        playerAFKData.put(playerId, new AFKData(player));
        
        LOG.debug("Registered player {} in AntiAFK system", player.getName());
    }
    
    /**
     * Убирает игрока из системы
     */
    public void removePlayer(int objectId) {
        playerHolders.remove(objectId);
        playerAFKData.remove(objectId);
        
        LOG.debug("Removed player {} from AntiAFK system", objectId);
    }
    
    /**
     * Обработка действия игрока (движение, атака, использование скилов)
     */
    public void onPlayerAction(L2PcInstance player) {
        AFKData afkData = playerAFKData.get(player.getObjectId());
        if (afkData != null) {
            afkData.updatePosition(player);
            
            // Если игрок был AFK, возобновляем награды
            if (afkData.isAFK()) {
                afkData.setAFK(false);
                PlayerHolder holder = playerHolders.get(player.getObjectId());
                if (holder != null) {
                    holder.resumeRewards();
                    LOG.debug("Player {} action detected, rewards resumed", player.getName());
                }
            }
        }
    }
    
    /**
     * Проверяет AFK статус игрока
     */
    public boolean isPlayerAFK(L2PcInstance player) {
        AFKData afkData = playerAFKData.get(player.getObjectId());
        return afkData != null && afkData.isAFK();
    }
    
    /**
     * Получает время с последнего действия игрока
     */
    public long getTimeSinceLastAction(L2PcInstance player) {
        AFKData afkData = playerAFKData.get(player.getObjectId());
        return afkData != null ? afkData.getTimeSinceLastAction() : 0;
    }
    
    /**
     * Получает статистику AFK системы
     */
    public AFKStatistics getStatistics() {
        int totalPlayers = playerAFKData.size();
        int afkPlayers = 0;
        long totalAfkTime = 0;
        
        for (AFKData data : playerAFKData.values()) {
            if (data.isAFK()) {
                afkPlayers++;
                totalAfkTime += data.getAfkTime();
            }
        }
        
        return new AFKStatistics(totalPlayers, afkPlayers, totalAfkTime);
    }
    
    /**
     * Принудительно помечает игрока как AFK
     */
    public void forceAFK(L2PcInstance player) {
        AFKData afkData = playerAFKData.get(player.getObjectId());
        if (afkData != null) {
            afkData.setAFK(true);
            PlayerHolder holder = playerHolders.get(player.getObjectId());
            if (holder != null) {
                holder.pauseRewards();
            }
            LOG.info("Player {} manually marked as AFK", player.getName());
        }
    }
    
    /**
     * Принудительно снимает AFK статус с игрока
     */
    public void clearAFK(L2PcInstance player) {
        AFKData afkData = playerAFKData.get(player.getObjectId());
        if (afkData != null) {
            afkData.setAFK(false);
            afkData.updatePosition(player);
            PlayerHolder holder = playerHolders.get(player.getObjectId());
            if (holder != null) {
                holder.resumeRewards();
            }
            LOG.info("Player {} manually cleared from AFK", player.getName());
        }
    }
    
    /**
     * Отключение системы
     */
    public void shutdown() {
        if (checkTask != null) {
            checkTask.cancel(false);
            checkTask = null;
        }
        
        playerAFKData.clear();
        playerHolders.clear();
        isInitialized = false;
        
        LOG.info("AntiAFKSystem shutdown completed");
    }
    
    /**
     * Данные о AFK состоянии игрока
     */
    private static class AFKData {
        private volatile long lastActionTime;
        private volatile boolean isAFK;
        private volatile int x, y, z;
        private volatile long afkStartTime;
        
        public AFKData(L2PcInstance player) {
            this.lastActionTime = System.currentTimeMillis();
            this.isAFK = false;
            this.afkStartTime = 0;
            updatePosition(player);
        }
        
        public void updatePosition(L2PcInstance player) {
            int newX = player.getX();
            int newY = player.getY();
            int newZ = player.getZ();
            
            // Проверяем значительное ли это движение
            double distance = Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2));
            
            if (distance >= MOVEMENT_THRESHOLD) {
                this.x = newX;
                this.y = newY;
                this.z = newZ;
                this.lastActionTime = System.currentTimeMillis();
                
                if (isAFK) {
                    setAFK(false);
                }
            }
        }
        
        public boolean hasMovedFrom(L2PcInstance player) {
            double distance = Math.sqrt(
                Math.pow(player.getX() - x, 2) + 
                Math.pow(player.getY() - y, 2)
            );
            return distance >= MOVEMENT_THRESHOLD;
        }
        
        public boolean isAFKByTime() {
            return (System.currentTimeMillis() - lastActionTime) > TimeUnit.MINUTES.toMillis(AFK_TIMEOUT_MINUTES);
        }
        
        public boolean isAFK() {
            return isAFK;
        }
        
        public void setAFK(boolean afk) {
            if (afk && !this.isAFK) {
                this.afkStartTime = System.currentTimeMillis();
            } else if (!afk && this.isAFK) {
                this.afkStartTime = 0;
            }
            this.isAFK = afk;
        }
        
        public long getTimeSinceLastAction() {
            return System.currentTimeMillis() - lastActionTime;
        }
        
        public long getAfkTime() {
            return isAFK && afkStartTime > 0 ? System.currentTimeMillis() - afkStartTime : 0;
        }
    }
    
    /**
     * Статистика AFK системы
     */
    public static class AFKStatistics {
        private final int totalPlayers;
        private final int afkPlayers;
        private final long totalAfkTime;
        
        public AFKStatistics(int totalPlayers, int afkPlayers, long totalAfkTime) {
            this.totalPlayers = totalPlayers;
            this.afkPlayers = afkPlayers;
            this.totalAfkTime = totalAfkTime;
        }
        
        public int getTotalPlayers() { return totalPlayers; }
        public int getAfkPlayers() { return afkPlayers; }
        public int getActivePlayers() { return totalPlayers - afkPlayers; }
        public long getTotalAfkTime() { return totalAfkTime; }
        
        public double getAfkPercentage() {
            return totalPlayers > 0 ? (double) afkPlayers / totalPlayers * 100 : 0;
        }
        
        public String getFormattedAfkTime() {
            long hours = TimeUnit.MILLISECONDS.toHours(totalAfkTime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(totalAfkTime) % 60;
            return String.format("%dh %dm", hours, minutes);
        }
        
        @Override
        public String toString() {
            return String.format("AFKStats{total=%d, afk=%d (%.1f%%), afkTime=%s}", 
                totalPlayers, afkPlayers, getAfkPercentage(), getFormattedAfkTime());
        }
    }
}