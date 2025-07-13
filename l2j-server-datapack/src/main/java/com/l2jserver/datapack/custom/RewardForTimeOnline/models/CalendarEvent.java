package com.l2jserver.datapack.custom.RewardForTimeOnline.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Календарное событие с особыми наградами
 * @author Dafna
 */
public class CalendarEvent {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    
    private final String name;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final double rewardMultiplier;
    private final Integer specialItemId;
    private final Long specialItemCount;
    private final boolean isActive;
    
    public CalendarEvent(String name, String description, LocalDateTime startDate,
                        LocalDateTime endDate, double rewardMultiplier,
                        Integer specialItemId, Long specialItemCount, boolean isActive) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardMultiplier = rewardMultiplier;
        this.specialItemId = specialItemId;
        this.specialItemCount = specialItemCount;
        this.isActive = isActive;
    }
    
    /**
     * Проверяет активно ли событие в данный момент
     */
    public boolean isActiveNow() {
        if (!isActive) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }
    
    /**
     * Проверяет будет ли событие активно в будущем
     */
    public boolean isUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && now.isBefore(startDate);
    }
    
    /**
     * Проверяет закончилось ли событие
     */
    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(endDate);
    }
    
    /**
     * Получает оставшееся время до начала события (в минутах)
     */
    public long getMinutesUntilStart() {
        if (!isUpcoming()) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        return java.time.Duration.between(now, startDate).toMinutes();
    }
    
    /**
     * Получает оставшееся время события (в минутах)
     */
    public long getMinutesRemaining() {
        if (!isActiveNow()) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        return java.time.Duration.between(now, endDate).toMinutes();
    }
    
    /**
     * Проверяет имеет ли событие специальную награду
     */
    public boolean hasSpecialReward() {
        return specialItemId != null && specialItemCount != null && specialItemCount > 0;
    }
    
    /**
     * Создает специальную награду для этого события
     */
    public ItemReward createSpecialReward() {
        if (!hasSpecialReward()) {
            return null;
        }
        
        return ItemReward.builder()
            .itemId(specialItemId)
            .count(specialItemCount)
            .intervalHours(1)
            .requiredEvent(name)
            .build();
    }
    
    /**
     * Получает статус события в виде строки
     */
    public String getStatus() {
        if (isActiveNow()) {
            return "ACTIVE";
        } else if (isUpcoming()) {
            return "UPCOMING";
        } else if (isExpired()) {
            return "EXPIRED";
        } else {
            return "INACTIVE";
        }
    }
    
    /**
     * Создает builder для удобного создания событий
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String description = "";
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private double rewardMultiplier = 1.0;
        private Integer specialItemId = null;
        private Long specialItemCount = null;
        private boolean isActive = true;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }
        
        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }
        
        public Builder duration(LocalDateTime start, int durationHours) {
            this.startDate = start;
            this.endDate = start.plusHours(durationHours);
            return this;
        }
        
        public Builder rewardMultiplier(double multiplier) {
            this.rewardMultiplier = multiplier;
            return this;
        }
        
        public Builder specialReward(int itemId, long count) {
            this.specialItemId = itemId;
            this.specialItemCount = count;
            return this;
        }
        
        public Builder active(boolean active) {
            this.isActive = active;
            return this;
        }
        
        public CalendarEvent build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Event name cannot be null or empty");
            }
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start and end dates must be specified");
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            if (rewardMultiplier < 0) {
                throw new IllegalArgumentException("Reward multiplier cannot be negative");
            }
            
            return new CalendarEvent(name, description, startDate, endDate,
                rewardMultiplier, specialItemId, specialItemCount, isActive);
        }
    }
    
    /**
     * Создает простое событие с множителем наград
     */
    public static CalendarEvent createMultiplierEvent(String name, LocalDateTime start, 
                                                     LocalDateTime end, double multiplier) {
        return builder()
            .name(name)
            .startDate(start)
            .endDate(end)
            .rewardMultiplier(multiplier)
            .build();
    }
    
    /**
     * Создает событие со специальной наградой
     */
    public static CalendarEvent createSpecialRewardEvent(String name, LocalDateTime start, 
                                                        LocalDateTime end, int itemId, long count) {
        return builder()
            .name(name)
            .startDate(start)
            .endDate(end)
            .specialReward(itemId, count)
            .build();
    }
    
    // Геттеры
    public String getName() { 
        return name; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public LocalDateTime getStartDate() { 
        return startDate; 
    }
    
    public LocalDateTime getEndDate() { 
        return endDate; 
    }
    
    public double getRewardMultiplier() { 
        return rewardMultiplier; 
    }
    
    public Integer getSpecialItemId() { 
        return specialItemId; 
    }
    
    public Long getSpecialItemCount() { 
        return specialItemCount; 
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public String getFormattedStartDate() {
        return startDate.format(FORMATTER);
    }
    
    public String getFormattedEndDate() {
        return endDate.format(FORMATTER);
    }
    
    public String getFormattedPeriod() {
        return String.format("%s - %s", getFormattedStartDate(), getFormattedEndDate());
    }
    
    @Override
    public String toString() {
        return String.format("CalendarEvent{name='%s', period='%s', multiplier=%.1f, status='%s'}", 
            name, getFormattedPeriod(), rewardMultiplier, getStatus());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CalendarEvent that = (CalendarEvent) obj;
        return Objects.equals(name, that.name) && 
               Objects.equals(startDate, that.startDate) &&
               Objects.equals(endDate, that.endDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate);
    }
}