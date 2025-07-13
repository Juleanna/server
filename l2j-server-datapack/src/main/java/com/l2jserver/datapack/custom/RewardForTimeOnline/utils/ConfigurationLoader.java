package com.l2jserver.datapack.custom.RewardForTimeOnline.utils;

import com.l2jserver.datapack.custom.RewardForTimeOnline.models.ItemReward;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.RewardGroup;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Загрузчик конфигурации системы наград
 * @author Dafna
 */
public class ConfigurationLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);
    
    private static final String CONFIG_FILE = "config/RewardsOnline.properties";
    private static final String REWARDS_CONFIG_FILE = "config/AdvancedRewards.properties";
    
    /**
     * Загружает конфигурацию из файлов
     */
    public static List<RewardGroup> loadFromFiles() {
        List<RewardGroup> groups = new ArrayList<>();
        
        try {
            // Пробуем загрузить новый формат конфигурации
            groups.addAll(loadAdvancedConfiguration());
            
            // Если нет новой конфигурации, загружаем legacy
            if (groups.isEmpty()) {
                groups.addAll(loadLegacyConfiguration());
            }
            
            // Если и legacy нет, создаем базовую конфигурацию
            if (groups.isEmpty()) {
                groups.addAll(createDefaultConfiguration());
            }
            
            LOG.info("Loaded {} reward groups from configuration files", groups.size());
            
        } catch (Exception e) {
            LOG.error("Failed to load configuration from files", e);
            groups.addAll(createDefaultConfiguration());
        }
        
        return groups;
    }
    
    /**
     * Загружает расширенную конфигурацию
     */
    private static List<RewardGroup> loadAdvancedConfiguration() {
        File configFile = new File(REWARDS_CONFIG_FILE);
        if (!configFile.exists()) {
            LOG.debug("Advanced configuration file not found: {}", REWARDS_CONFIG_FILE);
            return new ArrayList<>();
        }
        
        List<RewardGroup> groups = new ArrayList<>();
        Properties props = new Properties();
        
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
            
            // Получаем список групп
            Set<String> groupNames = getGroupNames(props);
            
            for (String groupName : groupNames) {
                RewardGroup group = parseRewardGroup(props, groupName);
                if (group != null) {
                    groups.add(group);
                }
            }
            
            LOG.info("Loaded {} groups from advanced configuration", groups.size());
            
        } catch (IOException e) {
            LOG.error("Failed to load advanced configuration", e);
        }
        
        return groups;
    }
    
    /**
     * Загружает legacy конфигурацию
     */
    private static List<RewardGroup> loadLegacyConfiguration() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            LOG.debug("Legacy configuration file not found: {}", CONFIG_FILE);
            return new ArrayList<>();
        }
        
        List<RewardGroup> groups = new ArrayList<>();
        Properties props = new Properties();
        
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
            
            // Парсим старый формат конфигурации
            List<ItemReward> rewards = parseLegacyRewards(props);
            
            if (!rewards.isEmpty()) {
                RewardGroup defaultGroup = RewardGroup.builder()
                    .name("Legacy Group")
                    .description("Imported from legacy configuration")
                    .addRewards(rewards)
                    .allowAccessLevels("0", "1", "2", "3", "4", "5")
                    .active(true)
                    .priority(1)
                    .build();
                
                groups.add(defaultGroup);
            }
            
            LOG.info("Loaded legacy configuration with {} rewards", rewards.size());
            
        } catch (IOException e) {
            LOG.error("Failed to load legacy configuration", e);
        }
        
        return groups;
    }
    
    /**
     * Создает конфигурацию по умолчанию
     */
    private static List<RewardGroup> createDefaultConfiguration() {
        LOG.info("Creating default configuration");
        
        List<RewardGroup> groups = new ArrayList<>();
        
        // Группа для новичков
        RewardGroup newbieGroup = RewardGroup.builder()
            .name("Newbie Rewards")
            .description("Special rewards for new players")
            .addReward(ItemReward.simple(57, 5000, 1))  // 5K Adena каждый час
            .addReward(ItemReward.simple(1060, 10, 2))  // 10 Lesser Heal Potions каждые 2 часа
            .addReward(ItemReward.onceOnly(1538, 5))    // 5 Blessed Scrolls of Escape одноразово
            .allowAccessLevels("0")
            .active(true)
            .priority(3)
            .build();
        
        // Группа для обычных игроков
        RewardGroup regularGroup = RewardGroup.builder()
            .name("Regular Rewards")
            .description("Standard rewards for regular players")
            .addReward(ItemReward.simple(57, 15000, 1))     // 15K Adena каждый час
            .addReward(ItemReward.simple(1374, 5, 3))       // 5 Greater Heal Potions каждые 3 часа
            .addReward(ItemReward.progressive(57, 50000, 4)) // Прогрессивная Adena каждые 4 часа
            .allowAccessLevels("1", "2")
            .active(true)
            .priority(2)
            .build();
        
        // Группа для VIP игроков
        RewardGroup vipGroup = RewardGroup.builder()
            .name("VIP Rewards")
            .description("Premium rewards for VIP players")
            .addReward(ItemReward.simple(57, 100000, 1))    // 100K Adena каждый час
            .addReward(ItemReward.simple(6577, 1, 6))       // Blessed Enchant Weapon S каждые 6 часов
            .addReward(ItemReward.simple(4037, 10, 4))      // 10 Coins of Luck каждые 4 часа
            .allowAccessLevels("3", "4", "5")
            .active(true)
            .priority(1)
            .build();
        
        // Выходные бонусы
        RewardGroup weekendGroup = RewardGroup.builder()
            .name("Weekend Bonus")
            .description("Special weekend rewards")
            .addReward(ItemReward.builder()
                .itemId(57)
                .count(25000)
                .intervalHours(2)
                .allowedDays(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
                .build())
            .addReward(ItemReward.builder()
                .itemId(8762)
                .count(1)
                .intervalHours(6)
                .allowedDays(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
                .build())
            .allowAccessLevels("0", "1", "2", "3", "4", "5")
            .active(true)
            .priority(4)
            .build();
        
        groups.add(newbieGroup);
        groups.add(regularGroup);
        groups.add(vipGroup);
        groups.add(weekendGroup);
        
        // Сохраняем конфигурацию по умолчанию
        saveDefaultConfiguration(groups);
        
        return groups;
    }
    
    /**
     * Парсит группу наград из properties
     */
    private static RewardGroup parseRewardGroup(Properties props, String groupName) {
        try {
            String description = props.getProperty(groupName + ".description", "");
            String accessLevelsStr = props.getProperty(groupName + ".accessLevels", "0,1,2");
            boolean isActive = Boolean.parseBoolean(props.getProperty(groupName + ".active", "true"));
            int priority = Integer.parseInt(props.getProperty(groupName + ".priority", "0"));
            
            Set<String> accessLevels = new HashSet<>(Arrays.asList(accessLevelsStr.split(",")));
            
            // Парсим награды для группы
            List<ItemReward> rewards = parseGroupRewards(props, groupName);
            
            return RewardGroup.builder()
                .name(groupName)
                .description(description)
                .addRewards(rewards)
                .allowAccessLevels(accessLevels.toArray(new String[0]))
                .active(isActive)
                .priority(priority)
                .build();
                
        } catch (Exception e) {
            LOG.warn("Failed to parse reward group {}: {}", groupName, e.getMessage());
            return null;
        }
    }
    
    /**
     * Парсит награды для конкретной группы
     */
    private static List<ItemReward> parseGroupRewards(Properties props, String groupName) {
        List<ItemReward> rewards = new ArrayList<>();
        
        // Ищем награды в формате: groupName.reward.1.itemId, groupName.reward.1.count, etc.
        int rewardIndex = 1;
        while (props.containsKey(groupName + ".reward." + rewardIndex + ".itemId")) {
            try {
                String prefix = groupName + ".reward." + rewardIndex + ".";
                
                int itemId = Integer.parseInt(props.getProperty(prefix + "itemId"));
                long count = Long.parseLong(props.getProperty(prefix + "count", "1"));
                int intervalHours = Integer.parseInt(props.getProperty(prefix + "intervalHours", "1"));
                boolean saveToDatabase = Boolean.parseBoolean(props.getProperty(prefix + "saveToDatabase", "false"));
                boolean onceOnly = Boolean.parseBoolean(props.getProperty(prefix + "onceOnly", "false"));
                boolean progressive = Boolean.parseBoolean(props.getProperty(prefix + "progressive", "false"));
                
                String allowedDaysStr = props.getProperty(prefix + "allowedDays", "");
                DayOfWeek[] allowedDays = null;
                if (!allowedDaysStr.isEmpty()) {
                    String[] dayNames = allowedDaysStr.split(",");
                    allowedDays = Arrays.stream(dayNames)
                        .map(String::trim)
                        .map(DayOfWeek::valueOf)
                        .toArray(DayOfWeek[]::new);
                }
                
                int minLevel = Integer.parseInt(props.getProperty(prefix + "minLevel", "0"));
                int maxLevel = Integer.parseInt(props.getProperty(prefix + "maxLevel", "0"));
                
                ItemReward reward = new ItemReward(itemId, count, TimeUnit.HOURS.toMillis(intervalHours),
                    saveToDatabase, onceOnly, progressive, null, allowedDays, minLevel, maxLevel);
                
                rewards.add(reward);
                rewardIndex++;
                
            } catch (Exception e) {
                LOG.warn("Failed to parse reward {} for group {}: {}", rewardIndex, groupName, e.getMessage());
                break;
            }
        }
        
        return rewards;
    }
    
    /**
     * Парсит legacy награды
     */
    private static List<ItemReward> parseLegacyRewards(Properties props) {
        List<ItemReward> rewards = new ArrayList<>();
        
        int rewardIndex = 1;
        while (props.containsKey("reward.item." + rewardIndex + ".id")) {
            try {
                int itemId = Integer.parseInt(props.getProperty("reward.item." + rewardIndex + ".id"));
                long count = Long.parseLong(props.getProperty("reward.item." + rewardIndex + ".count", "1"));
                long timeMs = Long.parseLong(props.getProperty("reward.item." + rewardIndex + ".time", "3600000"));
                boolean saveToDatabase = Boolean.parseBoolean(props.getProperty("reward.item." + rewardIndex + ".saveToDatabase", "false"));
                
                ItemReward reward = ItemReward.builder()
                    .itemId(itemId)
                    .count(count)
                    .interval(timeMs)
                    .saveToDatabase(saveToDatabase)
                    .build();
                
                rewards.add(reward);
                rewardIndex++;
                
            } catch (Exception e) {
                LOG.warn("Failed to parse legacy reward {}: {}", rewardIndex, e.getMessage());
                break;
            }
        }
        
        return rewards;
    }
    
    /**
     * Получает имена всех групп из properties
     */
    private static Set<String> getGroupNames(Properties props) {
        Set<String> groupNames = new HashSet<>();
        
        for (String key : props.stringPropertyNames()) {
            if (key.contains(".") && !key.endsWith(".reward.1.itemId")) {
                String groupName = key.substring(0, key.indexOf('.'));
                groupNames.add(groupName);
            }
        }
        
        return groupNames;
    }
    
    /**
     * Сохраняет конфигурацию по умолчанию
     */
    private static void saveDefaultConfiguration(List<RewardGroup> groups) {
        try {
            File configFile = new File(REWARDS_CONFIG_FILE);
            configFile.getParentFile().mkdirs();
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(configFile))) {
                writer.println("# Advanced Reward System Configuration");
                writer.println("# Generated automatically");
                writer.println();
                
                for (RewardGroup group : groups) {
                    writeGroupToFile(writer, group);
                    writer.println();
                }
            }
            
            LOG.info("Saved default configuration to {}", REWARDS_CONFIG_FILE);
            
        } catch (IOException e) {
            LOG.error("Failed to save default configuration", e);
        }
    }
    
    /**
     * Записывает группу в файл
     */
    private static void writeGroupToFile(PrintWriter writer, RewardGroup group) {
        String groupName = group.getName().replaceAll("\\s+", "_");
        
        writer.println("# " + group.getName());
        writer.println(groupName + ".description=" + group.getDescription());
        writer.println(groupName + ".accessLevels=" + String.join(",", group.getAllowedAccessLevels()));
        writer.println(groupName + ".active=" + group.isActive());
        writer.println(groupName + ".priority=" + group.getPriority());
        
        List<ItemReward> rewards = group.getRewards();
        for (int i = 0; i < rewards.size(); i++) {
            ItemReward reward = rewards.get(i);
            String prefix = groupName + ".reward." + (i + 1) + ".";
            
            writer.println(prefix + "itemId=" + reward.getItemId());
            writer.println(prefix + "count=" + reward.getBaseCount());
            writer.println(prefix + "intervalHours=" + TimeUnit.MILLISECONDS.toHours(reward.getTimeInterval()));
            writer.println(prefix + "saveToDatabase=" + reward.isSaveToDatabase());
            writer.println(prefix + "onceOnly=" + reward.isOnceOnly());
            writer.println(prefix + "progressive=" + reward.isProgressive());
            
            if (reward.getAllowedDays() != null) {
                String days = Arrays.stream(reward.getAllowedDays())
                    .map(DayOfWeek::name)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
                writer.println(prefix + "allowedDays=" + days);
            }
            
            if (reward.getMinLevel() > 0) {
                writer.println(prefix + "minLevel=" + reward.getMinLevel());
            }
            if (reward.getMaxLevel() > 0) {
                writer.println(prefix + "maxLevel=" + reward.getMaxLevel());
            }
        }
    }
    
    /**
     * Проверяет валидность конфигурации
     */
    public static boolean validateConfiguration(List<RewardGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            LOG.warn("Configuration is empty");
            return false;
        }
        
        ItemDataWrapper itemWrapper = ItemDataWrapper.getInstance();
        boolean isValid = true;
        
        for (RewardGroup group : groups) {
            for (ItemReward reward : group.getRewards()) {
                if (!ItemUtils.isValidItemId(reward.getItemId())) {
                    LOG.warn("Invalid item ID {} in group {}", reward.getItemId(), group.getName());
                    isValid = false;
                }
                
                if (!itemWrapper.itemExists(reward.getItemId())) {
                    LOG.warn("Item {} does not exist in group {}", reward.getItemId(), group.getName());
                    isValid = false;
                }
                
                if (!ItemUtils.isValidCount(reward.getBaseCount())) {
                    LOG.warn("Invalid count {} for item {} in group {}", 
                        reward.getBaseCount(), reward.getItemId(), group.getName());
                    isValid = false;
                }
            }
        }
        
        LOG.info("Configuration validation completed: {}", isValid ? "PASSED" : "FAILED");
        return isValid;
    }
}