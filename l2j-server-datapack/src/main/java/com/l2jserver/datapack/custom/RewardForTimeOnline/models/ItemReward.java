package com.l2jserver.datapack.custom.RewardForTimeOnline.models;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Награда с расширенными возможностями
 * @author Dafna
 */
public class ItemReward {
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
    
    /**
     * Проверяет доступна ли награда в текущее время
     */
    public boolean isAvailableNow() {
        LocalDateTime now = LocalDateTime.now();
        
        // Проверяем день недели
        if (allowedDays != null && allowedDays.length > 0) {
            boolean dayAllowed = Arrays.asList(allowedDays).contains(now.getDayOfWeek());
            if (!dayAllowed) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Проверяет может ли игрок получить эту награду
     */
    public boolean canPlayerReceive(L2PcInstance player) {
        if (!isAvailableNow()) {
            return false;
        }
        
        // Проверяем уровень игрока
        int playerLevel = player.getLevel();
        if (minLevel > 0 && playerLevel < minLevel) {
            return false;
        }
        if (maxLevel > 0 && playerLevel > maxLevel) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Создает builder для удобного создания наград
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private int itemId;
        private long baseCount = 1;
        private long timeInterval = TimeUnit.HOURS.toMillis(1);
        private boolean saveToDatabase = false;
        private boolean onceOnly = false;
        private boolean progressive = false;
        private String requiredEvent = null;
        private DayOfWeek[] allowedDays = null;
        private int minLevel = 0;
        private int maxLevel = 0;
        
        public Builder itemId(int itemId) {
            this.itemId = itemId;
            return this;
        }
        
        public Builder count(long count) {
            this.baseCount = count;
            return this;
        }
        
        public Builder interval(long intervalMs) {
            this.timeInterval = intervalMs;
            return this;
        }
        
        public Builder intervalHours(int hours) {
            this.timeInterval = TimeUnit.HOURS.toMillis(hours);
            return this;
        }
        
        public Builder intervalMinutes(int minutes) {
            this.timeInterval = TimeUnit.MINUTES.toMillis(minutes);
            return this;
        }
        
        public Builder saveToDatabase(boolean save) {
            this.saveToDatabase = save;
            return this;
        }
        
        public Builder onceOnly(boolean once) {
            this.onceOnly = once;
            return this;
        }
        
        public Builder progressive(boolean progressive) {
            this.progressive = progressive;
            return this;
        }
        
        public Builder requiredEvent(String event) {
            this.requiredEvent = event;
            return this;
        }
        
        public Builder allowedDays(DayOfWeek... days) {
            this.allowedDays = days;
            return this;
        }
        
        public Builder levelRange(int min, int max) {
            this.minLevel = min;
            this.maxLevel = max;
            return this;
        }
        
        public Builder minLevel(int level) {
            this.minLevel = level;
            return this;
        }
        
        public Builder maxLevel(int level) {
            this.maxLevel = level;
            return this;
        }
        
        public ItemReward build() {
            if (itemId <= 0) {
                throw new IllegalArgumentException("Item ID must be positive");
            }
            if (baseCount <= 0) {
                throw new IllegalArgumentException("Base count must be positive");
            }
            if (timeInterval <= 0) {
                throw new IllegalArgumentException("Time interval must be positive");
            }
            
            return new ItemReward(itemId, baseCount, timeInterval, saveToDatabase,
                onceOnly, progressive, requiredEvent, allowedDays, minLevel, maxLevel);
        }
    }
    
    /**
     * Создает простую награду для быстрого использования
     */
    public static ItemReward simple(int itemId, long count, int intervalHours) {
        return builder()
            .itemId(itemId)
            .count(count)
            .intervalHours(intervalHours)
            .build();
    }
    
    /**
     * Создает прогрессивную награду
     */
    public static ItemReward progressive(int itemId, long baseCount, int intervalHours) {
        return builder()
            .itemId(itemId)
            .count(baseCount)
            .intervalHours(intervalHours)
            .progressive(true)
            .saveToDatabase(true)
            .build();
    }
    
    /**
     * Создает одноразовую награду
     */
    public static ItemReward onceOnly(int itemId, long count) {
        return builder()
            .itemId(itemId)
            .count(count)
            .onceOnly(true)
            .saveToDatabase(true)
            .build();
    }
    
    // Геттеры
    public int getItemId() { 
        return itemId; 
    }
    
    public long getBaseCount() { 
        return baseCount; 
    }
    
    public long getTimeInterval() { 
        return timeInterval; 
    }
    
    public boolean isSaveToDatabase() { 
        return saveToDatabase; 
    }
    
    public boolean isOnceOnly() { 
        return onceOnly; 
    }
    
    public boolean isProgressive() { 
        return progressive; 
    }
    
    public String getRequiredEvent() { 
        return requiredEvent; 
    }
    
    public DayOfWeek[] getAllowedDays() {
        return allowedDays != null ? allowedDays.clone() : null;
    }
    
    public int getMinLevel() {
        return minLevel;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * Получает интервал в удобочитаемом формате
     */
    public String getFormattedInterval() {
        long hours = TimeUnit.MILLISECONDS.toHours(timeInterval);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInterval) % 60;
        
        if (hours > 0) {
            return minutes > 0 ? String.format("%dh %dm", hours, minutes) : String.format("%dh", hours);
        } else {
            return String.format("%dm", minutes);
        }
    }
    
    @Override
    public String toString() {
        return String.format("ItemReward{itemId=%d, count=%d, interval=%s, progressive=%s, onceOnly=%s}", 
            itemId, baseCount, getFormattedInterval(), progressive, onceOnly);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemReward that = (ItemReward) obj;
        return itemId == that.itemId && 
               baseCount == that.baseCount &&
               timeInterval == that.timeInterval;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(itemId, baseCount, timeInterval);
    }
}