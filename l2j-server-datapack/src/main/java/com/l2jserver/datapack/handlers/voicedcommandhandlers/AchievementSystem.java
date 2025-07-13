/*
 * Copyright © 2004-2023 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.handlers.voicedcommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;

/**
 * Система достижений и наград
 * @author YourName
 */
public class AchievementSystem {
    
    private static final Logger LOG = LoggerFactory.getLogger(AchievementSystem.class);
    
    // Кэш прогресса игроков
    private static final Map<Integer, PlayerProgress> PLAYER_PROGRESS = new ConcurrentHashMap<>();
    
    // SQL запросы
    private static final String SELECT_PROGRESS = "SELECT * FROM player_achievements WHERE player_id = ?";
    private static final String INSERT_ACHIEVEMENT = "INSERT INTO player_achievements (player_id, achievement_id, progress, completed, completion_date) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE progress = ?, completed = ?, completion_date = ?";
    private static final String SELECT_DAILY_TASKS = "SELECT * FROM daily_tasks WHERE player_id = ? AND task_date = ?";
    private static final String INSERT_DAILY_TASK = "INSERT INTO daily_tasks (player_id, task_id, progress, completed, task_date) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE progress = ?, completed = ?";
    
    /**
     * Типы достижений
     */
    public enum AchievementType {
        PANEL_USAGE("Использование панели", "🎮"),
        ENCHANT("Заточка", "⚔️"),
        SHOPPING("Покупки", "🛒"),
        BUFFS("Бафы", "✨"),
        TELEPORT("Телепортация", "🌀"),
        BANKING("Банкинг", "🏦"),
        SOCIAL("Социальные", "👥"),
        COLLECTION("Коллекционирование", "📚"),
        SPECIAL("Особые", "⭐");
        
        private final String displayName;
        private final String icon;
        
        AchievementType(String displayName, String icon) {
            this.displayName = displayName;
            this.icon = icon;
        }
        
        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
    }
    
    /**
     * Редкость достижений
     */
    public enum AchievementRarity {
        COMMON("Обычное", "⚪", 1.0),
        UNCOMMON("Необычное", "🔵", 1.5),
        RARE("Редкое", "🟣", 2.0),
        EPIC("Эпическое", "🟠", 3.0),
        LEGENDARY("Легендарное", "🟡", 5.0),
        MYTHIC("Мифическое", "🔴", 10.0);
        
        private final String displayName;
        private final String icon;
        private final double rewardMultiplier;
        
        AchievementRarity(String displayName, String icon, double rewardMultiplier) {
            this.displayName = displayName;
            this.icon = icon;
            this.rewardMultiplier = rewardMultiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public double getRewardMultiplier() { return rewardMultiplier; }
    }
    
    /**
     * Класс достижения
     */
    public static class Achievement {
        private final int id;
        private final String name;
        private final String description;
        private final AchievementType type;
        private final AchievementRarity rarity;
        private final int maxProgress;
        private final List<ItemHolder> rewards;
        private final long experienceReward;
        private final boolean isHidden;        // Скрытое до выполнения
        private final boolean isRepeatable;    // Можно выполнять многократно
        
        public Achievement(int id, String name, String description, AchievementType type, 
                          AchievementRarity rarity, int maxProgress, List<ItemHolder> rewards,
                          long experienceReward, boolean isHidden, boolean isRepeatable) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.maxProgress = maxProgress;
            this.rewards = rewards;
            this.experienceReward = experienceReward;
            this.isHidden = isHidden;
            this.isRepeatable = isRepeatable;
        }
        
        // Геттеры
        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public AchievementType getType() { return type; }
        public AchievementRarity getRarity() { return rarity; }
        public int getMaxProgress() { return maxProgress; }
        public List<ItemHolder> getRewards() { return rewards; }
        public long getExperienceReward() { return experienceReward; }
        public boolean isHidden() { return isHidden; }
        public boolean isRepeatable() { return isRepeatable; }
    }
    
    /**
     * Ежедневное задание
     */
    public static class DailyTask {
        private final int id;
        private final String name;
        private final String description;
        private final AchievementType type;
        private final int targetProgress;
        private final List<ItemHolder> rewards;
        private final long experienceReward;
        
        public DailyTask(int id, String name, String description, AchievementType type,
                        int targetProgress, List<ItemHolder> rewards, long experienceReward) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
            this.targetProgress = targetProgress;
            this.rewards = rewards;
            this.experienceReward = experienceReward;
        }
        
        // Геттеры
        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public AchievementType getType() { return type; }
        public int getTargetProgress() { return targetProgress; }
        public List<ItemHolder> getRewards() { return rewards; }
        public long getExperienceReward() { return experienceReward; }
    }
    
    /**
     * Прогресс игрока
     */
    public static class PlayerProgress {
        private final int playerId;
        private final Map<Integer, AchievementProgress> achievements;
        private final Map<Integer, TaskProgress> dailyTasks;
        private long panelExperience;
        private int panelLevel;
        
        public PlayerProgress(int playerId) {
            this.playerId = playerId;
            this.achievements = new ConcurrentHashMap<>();
            this.dailyTasks = new ConcurrentHashMap<>();
            this.panelExperience = 0;
            this.panelLevel = 1;
        }
        
        // Геттеры и сеттеры
        public int getPlayerId() { return playerId; }
        public Map<Integer, AchievementProgress> getAchievements() { return achievements; }
        public Map<Integer, TaskProgress> getDailyTasks() { return dailyTasks; }
        public long getPanelExperience() { return panelExperience; }
        public void setPanelExperience(long panelExperience) { this.panelExperience = panelExperience; }
        public int getPanelLevel() { return panelLevel; }
        public void setPanelLevel(int panelLevel) { this.panelLevel = panelLevel; }
        
        /**
         * Опыт, необходимый для следующего уровня
         */
        public long getExperienceForNextLevel() {
            return panelLevel * 10000L; // Прогрессивное увеличение
        }
        
        /**
         * Процент до следующего уровня
         */
        public double getProgressToNextLevel() {
            long expForNext = getExperienceForNextLevel();
            long currentLevelExp = panelExperience % expForNext;
            return (double)currentLevelExp / expForNext * 100;
        }
    }
    
    /**
     * Прогресс достижения
     */
    public static class AchievementProgress {
        private int progress;
        private boolean completed;
        private long completionDate;
        
        public AchievementProgress() {
            this.progress = 0;
            this.completed = false;
            this.completionDate = 0;
        }
        
        // Геттеры и сеттеры
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
        public long getCompletionDate() { return completionDate; }
        public void setCompletionDate(long completionDate) { this.completionDate = completionDate; }
    }
    
    /**
     * Прогресс ежедневного задания
     */
    public static class TaskProgress {
        private int progress;
        private boolean completed;
        
        public TaskProgress() {
            this.progress = 0;
            this.completed = false;
        }
        
        // Геттеры и сеттеры
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }
    
    // Все достижения в системе
    private static final Map<Integer, Achievement> ALL_ACHIEVEMENTS = new HashMap<>();
    
    // Ежедневные задания
    private static final Map<Integer, DailyTask> DAILY_TASKS = new HashMap<>();
    
    static {
        initializeAchievements();
        initializeDailyTasks();
        startDailyReset();
    }
    
    /**
     * Инициализация достижений
     */
    private static void initializeAchievements() {
        List<ItemHolder> commonRewards = List.of(new ItemHolder(57, 100000)); // 100k адены
        List<ItemHolder> rareRewards = List.of(new ItemHolder(57, 500000), new ItemHolder(3470, 1)); // 500k + золотой слиток
        List<ItemHolder> epicRewards = List.of(new ItemHolder(57, 1000000), new ItemHolder(3470, 5)); // 1кк + 5 слитков
        
        // Достижения использования панели
        ALL_ACHIEVEMENTS.put(1, new Achievement(1, "Первые шаги", "Открыть панель впервые", 
            AchievementType.PANEL_USAGE, AchievementRarity.COMMON, 1, commonRewards, 1000, false, false));
        
        ALL_ACHIEVEMENTS.put(2, new Achievement(2, "Постоянный пользователь", "Использовать панель 100 раз", 
            AchievementType.PANEL_USAGE, AchievementRarity.UNCOMMON, 100, rareRewards, 5000, false, false));
        
        ALL_ACHIEVEMENTS.put(3, new Achievement(3, "Мастер панели", "Использовать панель 1000 раз", 
            AchievementType.PANEL_USAGE, AchievementRarity.RARE, 1000, epicRewards, 20000, false, false));
        
        // Достижения заточки
        ALL_ACHIEVEMENTS.put(10, new Achievement(10, "Первая заточка", "Заточить предмет впервые", 
            AchievementType.ENCHANT, AchievementRarity.COMMON, 1, commonRewards, 1000, false, false));
        
        ALL_ACHIEVEMENTS.put(11, new Achievement(11, "Мастер заточки", "Заточить 100 предметов", 
            AchievementType.ENCHANT, AchievementRarity.UNCOMMON, 100, rareRewards, 5000, false, false));
        
        ALL_ACHIEVEMENTS.put(12, new Achievement(12, "Легендарный кузнец", "Заточить предмет до +15", 
            AchievementType.ENCHANT, AchievementRarity.EPIC, 1, epicRewards, 50000, false, false));
        
        // Достижения покупок
        ALL_ACHIEVEMENTS.put(20, new Achievement(20, "Первая покупка", "Купить товар в магазине", 
            AchievementType.SHOPPING, AchievementRarity.COMMON, 1, commonRewards, 500, false, false));
        
        ALL_ACHIEVEMENTS.put(21, new Achievement(21, "Завсегдатай магазина", "Купить 500 товаров", 
            AchievementType.SHOPPING, AchievementRarity.RARE, 500, epicRewards, 15000, false, false));
        
        // Достижения бафов
        ALL_ACHIEVEMENTS.put(30, new Achievement(30, "Первый баф", "Получить баф через панель", 
            AchievementType.BUFFS, AchievementRarity.COMMON, 1, commonRewards, 500, false, false));
        
        ALL_ACHIEVEMENTS.put(31, new Achievement(31, "Коллекционер бафов", "Получить все типы бафов", 
            AchievementType.BUFFS, AchievementRarity.EPIC, 7, epicRewards, 25000, false, false));
        
        // Достижения телепортации
        ALL_ACHIEVEMENTS.put(40, new Achievement(40, "Путешественник", "Телепортироваться в 5 разных городов", 
            AchievementType.TELEPORT, AchievementRarity.UNCOMMON, 5, rareRewards, 3000, false, false));
        
        // Скрытые достижения
        ALL_ACHIEVEMENTS.put(100, new Achievement(100, "Секретное достижение", "Использовать панель в полночь", 
            AchievementType.SPECIAL, AchievementRarity.LEGENDARY, 1, 
            List.of(new ItemHolder(57, 5000000), new ItemHolder(4037, 10)), 100000, true, false));
        
        // Повторяемые достижения
        ALL_ACHIEVEMENTS.put(200, new Achievement(200, "Ежедневный игрок", "Использовать панель каждый день", 
            AchievementType.PANEL_USAGE, AchievementRarity.UNCOMMON, 1, 
            List.of(new ItemHolder(57, 50000)), 1000, false, true));
    }
    
    /**
     * Инициализация ежедневных заданий
     */
    private static void initializeDailyTasks() {
        DAILY_TASKS.put(1, new DailyTask(1, "Активный пользователь", "Использовать панель 10 раз", 
            AchievementType.PANEL_USAGE, 10, List.of(new ItemHolder(57, 50000)), 1000));
        
        DAILY_TASKS.put(2, new DailyTask(2, "Заточи предмет", "Заточить 5 предметов", 
            AchievementType.ENCHANT, 5, List.of(new ItemHolder(57, 100000)), 2000));
        
        DAILY_TASKS.put(3, new DailyTask(3, "Покупатель", "Купить 10 товаров", 
            AchievementType.SHOPPING, 10, List.of(new ItemHolder(57, 75000)), 1500));
        
        DAILY_TASKS.put(4, new DailyTask(4, "Сила бафов", "Получить 20 бафов", 
            AchievementType.BUFFS, 20, List.of(new ItemHolder(57, 80000)), 1800));
        
        DAILY_TASKS.put(5, new DailyTask(5, "Путешествия", "Телепортироваться 3 раза", 
            AchievementType.TELEPORT, 3, List.of(new ItemHolder(57, 30000)), 800));
    }
    
    /**
     * Запуск ежедневного сброса заданий
     */
    private static void startDailyReset() {
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> {
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0) {
                resetDailyTasks();
            }
        }, 60000, 60000); // Проверяем каждую минуту
    }
    
    /**
     * Сброс ежедневных заданий
     */
    private static void resetDailyTasks() {
        for (PlayerProgress progress : PLAYER_PROGRESS.values()) {
            progress.getDailyTasks().clear();
        }
        LOG.info("Daily tasks reset for all players");
    }
    
    /**
     * Получение прогресса игрока
     */
    public static PlayerProgress getPlayerProgress(L2PcInstance player) {
        return getPlayerProgress(player.getObjectId());
    }
    
    public static PlayerProgress getPlayerProgress(int playerId) {
        PlayerProgress progress = PLAYER_PROGRESS.get(playerId);
        if (progress == null) {
            progress = loadPlayerProgress(playerId);
            PLAYER_PROGRESS.put(playerId, progress);
        }
        return progress;
    }
    
    /**
     * Загрузка прогресса из базы данных
     */
    private static PlayerProgress loadPlayerProgress(int playerId) {
        PlayerProgress progress = new PlayerProgress(playerId);
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_PROGRESS)) {
            
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int achievementId = rs.getInt("achievement_id");
                    AchievementProgress achProgress = new AchievementProgress();
                    achProgress.setProgress(rs.getInt("progress"));
                    achProgress.setCompleted(rs.getBoolean("completed"));
                    achProgress.setCompletionDate(rs.getLong("completion_date"));
                    progress.getAchievements().put(achievementId, achProgress);
                }
            }
        } catch (SQLException e) {
            LOG.error("Error loading player achievements for " + playerId, e);
        }
        
        // Загружаем ежедневные задания
        loadDailyTasks(progress);
        
        return progress;
    }
    
    /**
     * Загрузка ежедневных заданий
     */
    private static void loadDailyTasks(PlayerProgress progress) {
        String today = getTodayString();
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_DAILY_TASKS)) {
            
            ps.setInt(1, progress.getPlayerId());
            ps.setString(2, today);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int taskId = rs.getInt("task_id");
                    TaskProgress taskProgress = new TaskProgress();
                    taskProgress.setProgress(rs.getInt("progress"));
                    taskProgress.setCompleted(rs.getBoolean("completed"));
                    progress.getDailyTasks().put(taskId, taskProgress);
                }
            }
        } catch (SQLException e) {
            LOG.error("Error loading daily tasks for " + progress.getPlayerId(), e);
        }
    }
    
    /**
     * Прогресс в достижении
     */
    public static void progressAchievement(L2PcInstance player, AchievementType type, int amount) {
        PlayerProgress progress = getPlayerProgress(player);
        
        for (Achievement achievement : ALL_ACHIEVEMENTS.values()) {
            if (achievement.getType() != type) {
                continue;
            }
            
            AchievementProgress achProgress = progress.getAchievements().computeIfAbsent(
                achievement.getId(), k -> new AchievementProgress());
            
            if (achProgress.isCompleted() && !achievement.isRepeatable()) {
                continue;
            }
            
            // Увеличиваем прогресс
            achProgress.setProgress(achProgress.getProgress() + amount);
            
            // Проверяем завершение
            if (achProgress.getProgress() >= achievement.getMaxProgress() && !achProgress.isCompleted()) {
                completeAchievement(player, achievement, achProgress);
            }
            
            // Сохраняем прогресс
            saveAchievementProgress(player.getObjectId(), achievement.getId(), achProgress);
        }
        
        // Прогресс в ежедневных заданиях
        progressDailyTasks(player, type, amount);
    }
    
    /**
     * Завершение достижения
     */
    private static void completeAchievement(L2PcInstance player, Achievement achievement, AchievementProgress progress) {
        progress.setCompleted(true);
        progress.setCompletionDate(System.currentTimeMillis());
        
        // Выдаем награды
        for (ItemHolder reward : achievement.getRewards()) {
            long amount = (long)(reward.getCount() * achievement.getRarity().getRewardMultiplier());
            player.addItem("Achievement", reward.getId(), amount, null, true);
        }
        
        // Выдаем опыт панели
        PlayerProgress playerProgress = getPlayerProgress(player);
        long expReward = (long)(achievement.getExperienceReward() * achievement.getRarity().getRewardMultiplier());
        addPanelExperience(player, expReward);
        
        // Уведомление
        String message = String.format("🏆 Достижение разблокировано: %s %s\n%s", 
                                     achievement.getRarity().getIcon(), 
                                     achievement.getName(), 
                                     achievement.getDescription());
        player.sendMessage(message);
        
        // Дополнительное уведомление для редких достижений
        if (achievement.getRarity().ordinal() >= AchievementRarity.RARE.ordinal()) {
            // Можно добавить глобальное объявление или специальные эффекты
            player.sendMessage("✨ Получено редкое достижение! Бонусные награды выданы!");
        }
    }
    
    /**
     * Прогресс в ежедневных заданиях
     */
    private static void progressDailyTasks(L2PcInstance player, AchievementType type, int amount) {
        PlayerProgress progress = getPlayerProgress(player);
        
        for (DailyTask task : DAILY_TASKS.values()) {
            if (task.getType() != type) {
                continue;
            }
            
            TaskProgress taskProgress = progress.getDailyTasks().computeIfAbsent(
                task.getId(), k -> new TaskProgress());
            
            if (taskProgress.isCompleted()) {
                continue;
            }
            
            // Увеличиваем прогресс
            taskProgress.setProgress(taskProgress.getProgress() + amount);
            
            // Проверяем завершение
            if (taskProgress.getProgress() >= task.getTargetProgress()) {
                completeDailyTask(player, task, taskProgress);
            }
            
            // Сохраняем прогресс
            saveDailyTaskProgress(player.getObjectId(), task.getId(), taskProgress);
        }
    }
    
    /**
     * Завершение ежедневного задания
     */
    private static void completeDailyTask(L2PcInstance player, DailyTask task, TaskProgress progress) {
        progress.setCompleted(true);
        
        // Выдаем награды
        for (ItemHolder reward : task.getRewards()) {
            player.addItem("DailyTask", reward.getId(), reward.getCount(), null, true);
        }
        
        // Выдаем опыт панели
        addPanelExperience(player, task.getExperienceReward());
        
        // Уведомление
        String message = String.format("📅 Ежедневное задание выполнено: %s", task.getName());
        player.sendMessage(message);
    }
    
    /**
     * Добавление опыта панели
     */
    private static void addPanelExperience(L2PcInstance player, long experience) {
        PlayerProgress progress = getPlayerProgress(player);
        long oldExp = progress.getPanelExperience();
        int oldLevel = progress.getPanelLevel();
        
        progress.setPanelExperience(oldExp + experience);
        
        // Проверяем повышение уровня
        while (progress.getPanelExperience() >= progress.getExperienceForNextLevel()) {
            progress.setPanelLevel(progress.getPanelLevel() + 1);
            player.sendMessage("🆙 Уровень панели повышен! Новый уровень: " + progress.getPanelLevel());
            
            // Награда за повышение уровня
            long adenaReward = progress.getPanelLevel() * 100000L;
            player.addAdena("PanelLevelUp", adenaReward, null, true);
            player.sendMessage("💰 Награда за уровень: " + String.format("%,d", adenaReward) + " адены!");
        }
    }
    
    /**
     * Сохранение прогресса достижения
     */
    private static void saveAchievementProgress(int playerId, int achievementId, AchievementProgress progress) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ACHIEVEMENT)) {
            
            ps.setInt(1, playerId);
            ps.setInt(2, achievementId);
            ps.setInt(3, progress.getProgress());
            ps.setBoolean(4, progress.isCompleted());
            ps.setLong(5, progress.getCompletionDate());
            ps.setInt(6, progress.getProgress());
            ps.setBoolean(7, progress.isCompleted());
            ps.setLong(8, progress.getCompletionDate());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            LOG.error("Error saving achievement progress", e);
        }
    }
    
    /**
     * Сохранение прогресса ежедневного задания
     */
    private static void saveDailyTaskProgress(int playerId, int taskId, TaskProgress progress) {
        String today = getTodayString();
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_DAILY_TASK)) {
            
            ps.setInt(1, playerId);
            ps.setInt(2, taskId);
            ps.setInt(3, progress.getProgress());
            ps.setBoolean(4, progress.isCompleted());
            ps.setString(5, today);
            ps.setInt(6, progress.getProgress());
            ps.setBoolean(7, progress.isCompleted());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            LOG.error("Error saving daily task progress", e);
        }
    }
    
    /**
     * Получение строки сегодняшней даты
     */
    private static String getTodayString() {
        Calendar cal = Calendar.getInstance();
        return String.format("%04d-%02d-%02d", 
                           cal.get(Calendar.YEAR), 
                           cal.get(Calendar.MONTH) + 1, 
                           cal.get(Calendar.DAY_OF_MONTH));
    }
    
    /**
     * Получение всех достижений для отображения
     */
    public static List<Achievement> getVisibleAchievements(L2PcInstance player) {
        PlayerProgress progress = getPlayerProgress(player);
        List<Achievement> visible = new ArrayList<>();
        
        for (Achievement achievement : ALL_ACHIEVEMENTS.values()) {
            if (!achievement.isHidden() || 
                progress.getAchievements().containsKey(achievement.getId())) {
                visible.add(achievement);
            }
        }
        
        return visible;
    }
    
    /**
     * Получение активных ежедневных заданий
     */
    public static List<DailyTask> getActiveDailyTasks() {
        return new ArrayList<>(DAILY_TASKS.values());
    }
    
    /**
     * Получение статистики игрока
     */
    public static Map<String, Object> getPlayerStats(L2PcInstance player) {
        PlayerProgress progress = getPlayerProgress(player);
        Map<String, Object> stats = new HashMap<>();
        
        // Общая статистика
        int completedAchievements = (int)progress.getAchievements().values().stream()
                                                 .mapToLong(p -> p.isCompleted() ? 1 : 0)
                                                 .sum();
        
        int completedDailyTasks = (int)progress.getDailyTasks().values().stream()
                                               .mapToLong(p -> p.isCompleted() ? 1 : 0)
                                               .sum();
        
        stats.put("panelLevel", progress.getPanelLevel());
        stats.put("panelExperience", progress.getPanelExperience());
        stats.put("completedAchievements", completedAchievements);
        stats.put("totalAchievements", ALL_ACHIEVEMENTS.size());
        stats.put("completedDailyTasks", completedDailyTasks);
        stats.put("totalDailyTasks", DAILY_TASKS.size());
        stats.put("progressToNextLevel", progress.getProgressToNextLevel());
        
        return stats;
    }
    
    /**
     * Специальные события (например, использование панели в полночь)
     */
    public static void checkSpecialEvents(L2PcInstance player) {
        Calendar cal = Calendar.getInstance();
        
        // Проверка полуночного использования
        if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
            progressAchievement(player, AchievementType.SPECIAL, 1);
        }
        
        // Можно добавить другие специальные события
        // Например, использование в определенные дни недели, праздники и т.д.
    }
}