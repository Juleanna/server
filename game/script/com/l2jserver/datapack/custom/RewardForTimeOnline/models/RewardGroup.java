package com.l2jserver.datapack.custom.RewardForTimeOnline.models;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import java.util.*;

/**
 * Группа наград для определенных категорий игроков
 * @author Dafna
 */
public class RewardGroup {
    private final String name;
    private final String description;
    private final List<ItemReward> rewards;
    private final Set<String> allowedAccessLevels;
    private final boolean isActive;
    private final int priority;
    
    public RewardGroup(String name, String description, List<ItemReward> rewards, 
                      Set<String> allowedAccessLevels, boolean isActive, int priority) {
        this.name = name;
        this.description = description;
        this.rewards = new ArrayList<>(rewards);
        this.allowedAccessLevels = new HashSet<>(allowedAccessLevels);
        this.isActive = isActive;
        this.priority = priority;
    }
    
    /**
     * Проверяет может ли игрок получать награды из этой группы
     */
    public boolean canPlayerReceive(L2PcInstance player) {
        if (!isActive) {
            return false;
        }
        
        // Проверяем уровень доступа
        String playerAccessLevel = String.valueOf(player.getAccessLevel().getLevel());
        if (!allowedAccessLevels.isEmpty() && !allowedAccessLevels.contains(playerAccessLevel)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Получает все доступные награды для игрока
     */
    public List<ItemReward> getAvailableRewards(L2PcInstance player) {
        if (!canPlayerReceive(player)) {
            return new ArrayList<>();
        }
        
        return rewards.stream()
            .filter(reward -> reward.canPlayerReceive(player))
            .toList();
    }
    
    /**
     * Создает builder для удобного создания групп
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String description = "";
        private List<ItemReward> rewards = new ArrayList<>();
        private Set<String> allowedAccessLevels = new HashSet<>();
        private boolean isActive = true;
        private int priority = 0;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder addReward(ItemReward reward) {
            this.rewards.add(reward);
            return this;
        }
        
        public Builder addRewards(List<ItemReward> rewards) {
            this.rewards.addAll(rewards);
            return this;
        }
        
        public Builder allowAccessLevel(String accessLevel) {
            this.allowedAccessLevels.add(accessLevel);
            return this;
        }
        
        public Builder allowAccessLevels(String... accessLevels) {
            this.allowedAccessLevels.addAll(Arrays.asList(accessLevels));
            return this;
        }
        
        public Builder active(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }
        
        public RewardGroup build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Group name cannot be null or empty");
            }
            
            return new RewardGroup(name, description, rewards, allowedAccessLevels, isActive, priority);
        }
    }
    
    // Геттеры
    public String getName() { 
        return name; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public List<ItemReward> getRewards() { 
        return new ArrayList<>(rewards); 
    }
    
    public Set<String> getAllowedAccessLevels() {
        return new HashSet<>(allowedAccessLevels);
    }
    
    public boolean isActive() { 
        return isActive; 
    }
    
    public int getPriority() { 
        return priority; 
    }
    
    public int getRewardsCount() {
        return rewards.size();
    }
    
    @Override
    public String toString() {
        return String.format("RewardGroup{name='%s', rewards=%d, active=%s, priority=%d}", 
            name, rewards.size(), isActive, priority);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RewardGroup that = (RewardGroup) obj;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}