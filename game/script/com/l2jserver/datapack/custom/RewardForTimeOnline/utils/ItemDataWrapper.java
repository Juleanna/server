package com.l2jserver.datapack.custom.RewardForTimeOnline.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Универсальная обертка для работы с данными предметов
 * Поддерживает различные сборки L2J с автоматическим определением типа
 * 
 * Версия 2.0 - Улучшенная для модульной архитектуры
 * @author Dafna
 */
public class ItemDataWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(ItemDataWrapper.class);
    
    private static volatile ItemDataWrapper instance;
    private static final Object LOCK = new Object();
    
    // Системные данные
    private Object itemDataInstance;
    private Class<?> itemDataClass;
    private Class<?> itemTemplateClass;
    private BuildType detectedBuild;
    private boolean isInitialized = false;
    
    // Кэширование и статистика
    private final Map<Integer, String> nameCache = new ConcurrentHashMap<>();
    private final Map<Integer, Long> stackLimitCache = new ConcurrentHashMap<>();
    private final Map<Integer, String> typeCache = new ConcurrentHashMap<>();
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    
    // Возможные классы в разных сборках L2J
    private static final BuildInfo[] KNOWN_BUILDS = {
        // L2J-Server официальный
        new BuildInfo(BuildType.L2J_SERVER, 
            "com.l2jserver.gameserver.data.xml.impl.ItemData",
            "com.l2jserver.gameserver.model.items.L2Item"),
            
        // aCis сборка
        new BuildInfo(BuildType.ACIS,
            "net.sf.l2j.gameserver.data.ItemTable",
            "net.sf.l2j.gameserver.model.item.Item"),
            
        // L2JFrozen
        new BuildInfo(BuildType.L2J_FROZEN,
            "com.l2jfrozen.gameserver.datatables.ItemTable",
            "com.l2jfrozen.gameserver.model.L2Item"),
            
        // L2JMobius
        new BuildInfo(BuildType.L2J_MOBIUS,
            "org.l2jmobius.gameserver.data.xml.ItemData",
            "org.l2jmobius.gameserver.model.items.Item"),
            
        // L2JMobius альтернативный
        new BuildInfo(BuildType.L2J_MOBIUS_ALT,
            "org.l2jmobius.gameserver.data.ItemTable",
            "org.l2jmobius.gameserver.model.items.Item"),
            
        // L2JEternity
        new BuildInfo(BuildType.L2J_ETERNITY,
            "com.l2jeternity.gameserver.data.ItemTable",
            "com.l2jeternity.gameserver.model.L2Item"),
            
        // L2JArchid
        new BuildInfo(BuildType.L2J_ARCHID,
            "net.sf.l2j.gameserver.datatables.ItemTable",
            "net.sf.l2j.gameserver.model.item.Item"),
            
        // Lineage2 (русские сборки)
        new BuildInfo(BuildType.LINEAGE2_RU,
            "lineage2.gameserver.data.xml.holder.ItemHolder",
            "lineage2.gameserver.templates.item.ItemTemplate"),
            
        // L2R сборка
        new BuildInfo(BuildType.L2R,
            "l2r.gameserver.data.xml.ItemData",
            "l2r.gameserver.model.items.Item")
    };
    
    // Fallback данные для известных предметов
    private static final Map<Integer, ItemInfo> KNOWN_ITEMS = new HashMap<>();
    
    static {
        // Инициализируем базовые предметы
        initializeKnownItems();
    }
    
    /**
     * Enum для типов сборок
     */
    public enum BuildType {
        L2J_SERVER("L2J-Server Official"),
        ACIS("aCis"),
        L2J_FROZEN("L2J Frozen"),
        L2J_MOBIUS("L2J Mobius"),
        L2J_MOBIUS_ALT("L2J Mobius Alternative"),
        L2J_ETERNITY("L2J Eternity"),
        L2J_ARCHID("L2J Archid"),
        LINEAGE2_RU("Lineage2 Russian"),
        L2R("L2R"),
        FALLBACK("Fallback Mode");
        
        private final String displayName;
        
        BuildType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Информация о сборке
     */
    private static class BuildInfo {
        final BuildType type;
        final String itemDataClass;
        final String itemTemplateClass;
        
        BuildInfo(BuildType type, String itemDataClass, String itemTemplateClass) {
            this.type = type;
            this.itemDataClass = itemDataClass;
            this.itemTemplateClass = itemTemplateClass;
        }
    }
    
    /**
     * Информация о предмете
     */
    public static class ItemInfo {
        private final int itemId;
        private final String name;
        private final String type;
        private final long stackLimit;
        private final boolean isCurrency;
        private final boolean isRare;
        
        public ItemInfo(int itemId, String name, String type, long stackLimit, boolean isCurrency, boolean isRare) {
            this.itemId = itemId;
            this.name = name;
            this.type = type;
            this.stackLimit = stackLimit;
            this.isCurrency = isCurrency;
            this.isRare = isRare;
        }
        
        // Геттеры
        public int getItemId() { return itemId; }
        public String getName() { return name; }
        public String getType() { return type; }
        public long getStackLimit() { return stackLimit; }
        public boolean isCurrency() { return isCurrency; }
        public boolean isRare() { return isRare; }
    }
    
    private ItemDataWrapper() {
        // Приватный конструктор для Singleton
    }
    
    public static ItemDataWrapper getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ItemDataWrapper();
                    instance.initialize();
                }
            }
        }
        return instance;
    }
    
    /**
     * Инициализация системы с автоматическим определением сборки
     */
    private void initialize() {
        if (isInitialized) {
            return;
        }
        
        LOG.info("Initializing ItemDataWrapper v2.0...");
        
        // Пробуем определить тип сборки
        for (BuildInfo build : KNOWN_BUILDS) {
            if (tryInitializeBuild(build)) {
                detectedBuild = build.type;
                LOG.info("Successfully detected L2J build: {} ({})", 
                    detectedBuild.getDisplayName(), build.itemDataClass);
                isInitialized = true;
                return;
            }
        }
        
        // Если ничего не найдено, используем fallback режим
        detectedBuild = BuildType.FALLBACK;
        isInitialized = true;
        LOG.warn("No compatible L2J build detected, using fallback mode with {} known items", 
            KNOWN_ITEMS.size());
    }
    
    /**
     * Пробует инициализировать конкретную сборку
     */
    private boolean tryInitializeBuild(BuildInfo build) {
        try {
            // Проверяем наличие класса ItemData
            itemDataClass = Class.forName(build.itemDataClass);
            
            // Пробуем получить инстанс
            try {
                itemDataInstance = itemDataClass.getMethod("getInstance").invoke(null);
            } catch (Exception e) {
                // Если нет getInstance, пробуем как статический класс
                itemDataInstance = itemDataClass;
            }
            
            // Проверяем класс шаблона
            itemTemplateClass = Class.forName(build.itemTemplateClass);
            
            // Тестируем работоспособность с Adena
            Object testTemplate = getTemplateInternal(57);
            if (testTemplate != null) {
                String testName = getNameInternal(testTemplate);
                if (testName != null && !testName.trim().isEmpty()) {
                    LOG.debug("Build test successful: Adena = '{}'", testName);
                    return true;
                }
            }
            
            return false;
            
        } catch (ClassNotFoundException e) {
            // Класс не найден - нормальная ситуация
            return false;
        } catch (Exception e) {
            LOG.debug("Failed to initialize build {}: {}", build.type, e.getMessage());
            return false;
        }
    }
    
    /**
     * Получает шаблон предмета по ID
     */
    public Object getTemplate(int itemId) {
        if (!isInitialized) {
            initialize();
        }
        
        if (detectedBuild == BuildType.FALLBACK) {
            return createFallbackTemplate(itemId);
        }
        
        return getTemplateInternal(itemId);
    }
    
    /**
     * Внутренний метод получения шаблона
     */
    private Object getTemplateInternal(int itemId) {
        if (itemDataInstance == null || itemDataClass == null) {
            return null;
        }
        
        try {
            // Список методов в зависимости от сборки
            String[] methodNames = getMethodNamesForBuild("getTemplate");
            
            for (String methodName : methodNames) {
                try {
                    return itemDataClass.getMethod(methodName, int.class).invoke(itemDataInstance, itemId);
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            
            return null;
            
        } catch (Exception e) {
            LOG.debug("Error getting template for item {}: {}", itemId, e.getMessage());
            return null;
        }
    }
    
    /**
     * Получает название предмета с кэшированием
     */
    public String getItemName(int itemId) {
        if (!isInitialized) {
            initialize();
        }
        
        // Проверяем кэш
        String cachedName = nameCache.get(itemId);
        if (cachedName != null) {
            cacheHits.incrementAndGet();
            return cachedName;
        }
        
        cacheMisses.incrementAndGet();
        String itemName = getItemNameInternal(itemId);
        
        // Кэшируем результат
        if (itemName != null && !itemName.trim().isEmpty()) {
            nameCache.put(itemId, itemName);
        }
        
        return itemName;
    }
    
    /**
     * Внутренний метод получения имени предмета
     */
    private String getItemNameInternal(int itemId) {
        // Проверяем fallback данные
        ItemInfo knownItem = KNOWN_ITEMS.get(itemId);
        if (knownItem != null) {
            return knownItem.getName();
        }
        
        if (detectedBuild == BuildType.FALLBACK) {
            return "Unknown Item (" + itemId + ")";
        }
        
        Object template = getTemplateInternal(itemId);
        if (template != null) {
            String name = getNameInternal(template);
            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
        }
        
        return "Unknown Item (" + itemId + ")";
    }
    
    /**
     * Получает имя из шаблона предмета
     */
    private String getNameInternal(Object template) {
        if (template == null) {
            return null;
        }
        
        try {
            String[] methodNames = getMethodNamesForBuild("getName");
            
            for (String methodName : methodNames) {
                try {
                    Object result = template.getClass().getMethod(methodName).invoke(template);
                    if (result instanceof String && !result.toString().trim().isEmpty()) {
                        return (String) result;
                    }
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            
        } catch (Exception e) {
            LOG.debug("Error getting name from template: {}", e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Проверяет существование предмета
     */
    public boolean itemExists(int itemId) {
        if (!isInitialized) {
            initialize();
        }
        
        // Проверяем известные предметы
        if (KNOWN_ITEMS.containsKey(itemId)) {
            return true;
        }
        
        if (detectedBuild == BuildType.FALLBACK) {
            return false;
        }
        
        Object template = getTemplateInternal(itemId);
        return template != null;
    }
    
    /**
     * Получает максимальное количество в стаке
     */
    public long getStackLimit(int itemId) {
        if (!isInitialized) {
            initialize();
        }
        
        // Проверяем кэш
        Long cachedLimit = stackLimitCache.get(itemId);
        if (cachedLimit != null) {
            return cachedLimit;
        }
        
        long stackLimit = getStackLimitInternal(itemId);
        stackLimitCache.put(itemId, stackLimit);
        
        return stackLimit;
    }
    
    /**
     * Внутренний метод получения лимита стака
     */
    private long getStackLimitInternal(int itemId) {
        // Проверяем известные предметы
        ItemInfo knownItem = KNOWN_ITEMS.get(itemId);
        if (knownItem != null) {
            return knownItem.getStackLimit();
        }
        
        if (detectedBuild == BuildType.FALLBACK) {
            return ItemUtils.isCurrency(itemId) ? 999999999L : 999999L;
        }
        
        Object template = getTemplateInternal(itemId);
        if (template == null) {
            return 1;
        }
        
        try {
            String[] methodNames = getMethodNamesForBuild("getStackLimit");
            
            for (String methodName : methodNames) {
                try {
                    Object result = template.getClass().getMethod(methodName).invoke(template);
                    if (result instanceof Number) {
                        return ((Number) result).longValue();
                    }
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            
        } catch (Exception e) {
            LOG.debug("Error getting stack limit for item {}: {}", itemId, e.getMessage());
        }
        
        return 1;
    }
    
    /**
     * Получает тип предмета
     */
    public String getItemType(int itemId) {
        if (!isInitialized) {
            initialize();
        }
        
        // Проверяем кэш
        String cachedType = typeCache.get(itemId);
        if (cachedType != null) {
            return cachedType;
        }
        
        String itemType = getItemTypeInternal(itemId);
        typeCache.put(itemId, itemType);
        
        return itemType;
    }
    
    /**
     * Внутренний метод получения типа предмета
     */
    private String getItemTypeInternal(int itemId) {
        // Проверяем известные предметы
        ItemInfo knownItem = KNOWN_ITEMS.get(itemId);
        if (knownItem != null) {
            return knownItem.getType();
        }
        
        if (detectedBuild == BuildType.FALLBACK) {
            return ItemUtils.isCurrency(itemId) ? "CURRENCY" : "ITEM";
        }
        
        Object template = getTemplateInternal(itemId);
        if (template == null) {
            return "UNKNOWN";
        }
        
        try {
            String[] methodNames = getMethodNamesForBuild("getType");
            
            for (String methodName : methodNames) {
                try {
                    Object result = template.getClass().getMethod(methodName).invoke(template);
                    if (result != null) {
                        return result.toString();
                    }
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            
        } catch (Exception e) {
            LOG.debug("Error getting type for item {}: {}", itemId, e.getMessage());
        }
        
        return "ITEM";
    }
    
    /**
     * Получает список методов для текущей сборки
     */
    private String[] getMethodNamesForBuild(String operation) {
        return switch (operation) {
            case "getTemplate" -> switch (detectedBuild) {
                case L2J_SERVER -> new String[]{"getTemplate"};
                case ACIS, L2J_FROZEN, L2J_ARCHID -> new String[]{"getItem", "getTemplate"};
                case L2J_MOBIUS, L2J_MOBIUS_ALT -> new String[]{"getItemTemplate", "getTemplate"};
                case L2J_ETERNITY -> new String[]{"getTemplate", "getItem"};
                case LINEAGE2_RU -> new String[]{"getItemTemplate", "getTemplate"};
                case L2R -> new String[]{"getTemplate", "getItem"};
                default -> new String[]{"getTemplate", "getItem", "getItemTemplate", "getItemById"};
            };
            
            case "getName" -> new String[]{"getName", "getItemName", "toString"};
            
            case "getStackLimit" -> new String[]{"getStackLimit", "getMaxStackCount", "getStackable"};
            
            case "getType" -> new String[]{"getType", "getItemType", "getClass"};
            
            default -> new String[]{};
        };
    }
    
    /**
     * Создает fallback шаблон для предмета
     */
    private Object createFallbackTemplate(int itemId) {
        ItemInfo knownItem = KNOWN_ITEMS.get(itemId);
        return knownItem != null ? new FallbackItemTemplate(knownItem) : null;
    }
    
    /**
     * Добавляет известный предмет в систему
     */
    public void addKnownItem(int itemId, String name) {
        addKnownItem(itemId, name, "ITEM", 999999L, false, false);
    }
    
    /**
     * Добавляет известный предмет с полной информацией
     */
    public void addKnownItem(int itemId, String name, String type, long stackLimit, boolean isCurrency, boolean isRare) {
        ItemInfo itemInfo = new ItemInfo(itemId, name, type, stackLimit, isCurrency, isRare);
        KNOWN_ITEMS.put(itemId, itemInfo);
        
        // Обновляем кэши
        nameCache.put(itemId, name);
        typeCache.put(itemId, type);
        stackLimitCache.put(itemId, stackLimit);
        
        LOG.debug("Added known item: {} - {}", itemId, name);
    }
    
    /**
     * Очищает все кэши
     */
    public void clearCache() {
        nameCache.clear();
        typeCache.clear();
        stackLimitCache.clear();
        LOG.debug("All caches cleared");
    }
    
    /**
     * Получает подробную информацию о системе
     */
    public SystemInfo getSystemInfo() {
        return new SystemInfo(
            detectedBuild,
            itemDataClass != null ? itemDataClass.getName() : "NOT_FOUND",
            itemTemplateClass != null ? itemTemplateClass.getName() : "NOT_FOUND",
            itemDataInstance != null,
            KNOWN_ITEMS.size(),
            nameCache.size(),
            typeCache.size(),
            stackLimitCache.size(),
            cacheHits.get(),
            cacheMisses.get()
        );
    }
    
    /**
     * Информация о системе ItemData
     */
    public static class SystemInfo {
        private final BuildType buildType;
        private final String itemDataClassName;
        private final String itemTemplateClassName;
        private final boolean hasInstance;
        private final int knownItemsCount;
        private final int nameCacheSize;
        private final int typeCacheSize;
        private final int stackCacheSize;
        private final long cacheHits;
        private final long cacheMisses;
        
        public SystemInfo(BuildType buildType, String itemDataClassName, String itemTemplateClassName,
                         boolean hasInstance, int knownItemsCount, int nameCacheSize, int typeCacheSize,
                         int stackCacheSize, long cacheHits, long cacheMisses) {
            this.buildType = buildType;
            this.itemDataClassName = itemDataClassName;
            this.itemTemplateClassName = itemTemplateClassName;
            this.hasInstance = hasInstance;
            this.knownItemsCount = knownItemsCount;
            this.nameCacheSize = nameCacheSize;
            this.typeCacheSize = typeCacheSize;
            this.stackCacheSize = stackCacheSize;
            this.cacheHits = cacheHits;
            this.cacheMisses = cacheMisses;
        }
        
        // Геттеры
        public BuildType getBuildType() { return buildType; }
        public String getItemDataClassName() { return itemDataClassName; }
        public String getItemTemplateClassName() { return itemTemplateClassName; }
        public boolean hasInstance() { return hasInstance; }
        public int getKnownItemsCount() { return knownItemsCount; }
        public int getNameCacheSize() { return nameCacheSize; }
        public int getTypeCacheSize() { return typeCacheSize; }
        public int getStackCacheSize() { return stackCacheSize; }
        public long getCacheHits() { return cacheHits; }
        public long getCacheMisses() { return cacheMisses; }
        
        public boolean isUsingFallback() {
            return buildType == BuildType.FALLBACK;
        }
        
        public double getCacheHitRatio() {
            long total = cacheHits + cacheMisses;
            return total > 0 ? (double) cacheHits / total * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("ItemDataSystem{build=%s, fallback=%s, cache=%.1f%%, items=%d}", 
                buildType.getDisplayName(), isUsingFallback(), getCacheHitRatio(), knownItemsCount);
        }
    }
    
    /**
     * Fallback шаблон предмета
     */
    private static class FallbackItemTemplate {
        private final ItemInfo itemInfo;
        
        public FallbackItemTemplate(ItemInfo itemInfo) {
            this.itemInfo = itemInfo;
        }
        
        public String getName() { return itemInfo.getName(); }
        public int getItemId() { return itemInfo.getItemId(); }
        public String getType() { return itemInfo.getType(); }
        public long getStackLimit() { return itemInfo.getStackLimit(); }
    }
    
    /**
     * Инициализирует известные предметы
     */
    private static void initializeKnownItems() {
        // Основные валюты
        KNOWN_ITEMS.put(57, new ItemInfo(57, "Adena", "CURRENCY", 999999999L, true, false));
        KNOWN_ITEMS.put(5575, new ItemInfo(5575, "Ancient Adena", "CURRENCY", 999999999L, true, false));
        KNOWN_ITEMS.put(6673, new ItemInfo(6673, "Seal of Gnosis", "CURRENCY", 999999999L, true, false));
        KNOWN_ITEMS.put(6674, new ItemInfo(6674, "Forgotten Scroll", "CURRENCY", 999999999L, true, false));
        
        // Популярные предметы
        KNOWN_ITEMS.put(1538, new ItemInfo(1538, "Blessed Scroll of Escape", "CONSUMABLE", 999999L, false, false));
        KNOWN_ITEMS.put(1374, new ItemInfo(1374, "Greater Heal Potion", "CONSUMABLE", 999999L, false, false));
        KNOWN_ITEMS.put(1060, new ItemInfo(1060, "Lesser Heal Potion", "CONSUMABLE", 999999L, false, false));
        KNOWN_ITEMS.put(1147, new ItemInfo(1147, "Scroll of Resurrection", "CONSUMABLE", 999999L, false, false));
        KNOWN_ITEMS.put(3936, new ItemInfo(3936, "Blessed Scroll of Resurrection", "CONSUMABLE", 999999L, false, false));
        
        // Редкие предметы
        KNOWN_ITEMS.put(6577, new ItemInfo(6577, "Blessed Enchant Weapon S", "ENCHANT", 999999L, false, true));
        KNOWN_ITEMS.put(6578, new ItemInfo(6578, "Blessed Enchant Armor S", "ENCHANT", 999999L, false, true));
        KNOWN_ITEMS.put(4037, new ItemInfo(4037, "Coin of Luck", "CURRENCY", 999999L, true, false));
        KNOWN_ITEMS.put(8762, new ItemInfo(8762, "Top-grade Life Stone", "ENCHANT", 999999L, false, true));
        
        LOG.debug("Initialized {} known items for fallback mode", KNOWN_ITEMS.size());
    }
    
    /**
     * Отладочная информация о системе
     */
    public void printDebugInfo() {
        SystemInfo info = getSystemInfo();
        LOG.info("=== ItemDataWrapper v2.0 Debug Info ===");
        LOG.info("Build Type: {}", info.getBuildType().getDisplayName());
        LOG.info("ItemData class: {}", info.getItemDataClassName());
        LOG.info("ItemTemplate class: {}", info.getItemTemplateClassName());
        LOG.info("ItemData instance: {}", info.hasInstance() ? "OK" : "NULL");
        LOG.info("Using fallback: {}", info.isUsingFallback());
        LOG.info("Known items: {}", info.getKnownItemsCount());
        LOG.info("Cache stats: {:.1f}% hit ratio ({}/{} requests)", 
            info.getCacheHitRatio(), info.getCacheHits(), info.getCacheHits() + info.getCacheMisses());
        LOG.info("Cache sizes: names={}, types={}, stacks={}", 
            info.getNameCacheSize(), info.getTypeCacheSize(), info.getStackCacheSize());
        
        // Тестируем с Adena
        if (itemExists(57)) {
            LOG.info("Test successful - Adena: {}", getItemName(57));
        } else {
            LOG.warn("Test failed - Adena not found");
        }
        LOG.info("========================================");
    }
}