package com.l2jserver.datapack.custom.RewardForTimeOnline;

/**
 * Универсальная обертка для работы с данными предметов
 * Поддерживает различные сборки L2J
 */
public class ItemDataWrapper {
    
    private static ItemDataWrapper instance;
    private Object itemDataInstance;
    private Class<?> itemDataClass;
    private Class<?> itemTemplateClass;
    
    // Возможные классы в разных сборках
    private static final String[] ITEM_DATA_CLASSES = {
        // L2J-Server официальный
        "com.l2jserver.gameserver.data.xml.impl.ItemData",
        
        // aCis сборка
        "net.sf.l2j.gameserver.data.ItemTable",
        
        // L2JFrozen
        "com.l2jfrozen.gameserver.datatables.ItemTable",
        
        // L2JMobius
        "org.l2jmobius.gameserver.data.ItemTable",
        "org.l2jmobius.gameserver.data.xml.ItemData",
        
        // L2JEternity
        "com.l2jeternity.gameserver.data.ItemTable",
        
        // L2JArchid  
        "net.sf.l2j.gameserver.datatables.ItemTable",
        
        // Другие популярные сборки
        "com.l2j.gameserver.data.ItemTable",
        "l2r.gameserver.data.xml.ItemData",
        "lineage2.gameserver.data.xml.holder.ItemHolder"
    };
    
    private static final String[] ITEM_TEMPLATE_CLASSES = {
        // L2J-Server официальный
        "com.l2jserver.gameserver.model.items.L2Item",
        
        // aCis
        "net.sf.l2j.gameserver.model.item.Item",
        
        // L2JFrozen
        "com.l2jfrozen.gameserver.model.L2Item",
        
        // L2JMobius
        "org.l2jmobius.gameserver.model.items.Item",
        
        // Другие
        "com.l2j.gameserver.model.L2Item",
        "lineage2.gameserver.templates.item.ItemTemplate"
    };
    
    private ItemDataWrapper() {
        initialize();
    }
    
    public static ItemDataWrapper getInstance() {
        if (instance == null) {
            instance = new ItemDataWrapper();
        }
        return instance;
    }
    
    private void initialize() {
        // Пробуем найти правильный класс для данной сборки
        for (String className : ITEM_DATA_CLASSES) {
            try {
                itemDataClass = Class.forName(className);
                
                // Пробуем получить инстанс через getInstance()
                try {
                    itemDataInstance = itemDataClass.getMethod("getInstance").invoke(null);
                    System.out.println("[ItemDataWrapper] Found ItemData class: " + className);
                    break;
                } catch (Exception e) {
                    // Пробуем как статический класс
                    itemDataInstance = itemDataClass;
                    System.out.println("[ItemDataWrapper] Found static ItemData class: " + className);
                    break;
                }
            } catch (ClassNotFoundException e) {
                // Класс не найден, пробуем следующий
                continue;
            }
        }
        
        // Находим класс шаблона предмета
        for (String className : ITEM_TEMPLATE_CLASSES) {
            try {
                itemTemplateClass = Class.forName(className);
                System.out.println("[ItemDataWrapper] Found ItemTemplate class: " + className);
                break;
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        
        if (itemDataClass == null) {
            System.err.println("[ItemDataWrapper] ERROR: Could not find ItemData class for this L2J build!");
        }
        if (itemTemplateClass == null) {
            System.err.println("[ItemDataWrapper] ERROR: Could not find ItemTemplate class for this L2J build!");
        }
    }
    
    /**
     * Получает шаблон предмета по ID
     */
    public Object getTemplate(int itemId) {
        if (itemDataInstance == null || itemDataClass == null) {
            return null;
        }
        
        try {
            // Список возможных методов в разных сборках
            String[] methodNames = {
                "getTemplate",      // L2J-Server
                "getItem",          // aCis, Frozen
                "getItemTemplate",  // Mobius
                "getItemById"       // Другие
            };
            
            for (String methodName : methodNames) {
                try {
                    return itemDataClass.getMethod(methodName, int.class).invoke(itemDataInstance, itemId);
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            
            System.err.println("[ItemDataWrapper] Could not find suitable method to get item template");
            return null;
            
        } catch (Exception e) {
            System.err.println("[ItemDataWrapper] Error getting item template: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Получает название предмета
     */
    public String getItemName(int itemId) {
        Object template = getTemplate(itemId);
        if (template == null) {
            return "Unknown Item (" + itemId + ")";
        }
        
        try {
            // Пробуем разные методы получения имени
            String[] methodNames = {"getName", "getItemName", "toString"};
            
            for (String methodName : methodNames) {
                try {
                    Object result = template.getClass().getMethod(methodName).invoke(template);
                    if (result instanceof String) {
                        return (String) result;
                    }
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            
            return "Item " + itemId;
            
        } catch (Exception e) {
            return "Item " + itemId;
        }
    }
    
    /**
     * Проверяет существование предмета
     */
    public boolean itemExists(int itemId) {
        return getTemplate(itemId) != null;
    }
    
    /**
     * Получает максимальное количество в стаке
     */
    public long getStackLimit(int itemId) {
        Object template = getTemplate(itemId);
        if (template == null) {
            return 1;
        }
        
        try {
            String[] methodNames = {"getStackLimit", "getMaxStackCount", "getStackable"};
            
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
            
            return 1;
            
        } catch (Exception e) {
            return 1;
        }
    }
    
    /**
     * Получает тип предмета
     */
    public String getItemType(int itemId) {
        Object template = getTemplate(itemId);
        if (template == null) {
            return "UNKNOWN";
        }
        
        try {
            String[] methodNames = {"getType", "getItemType", "getClass"};
            
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
            
            return "ITEM";
            
        } catch (Exception e) {
            return "ITEM";
        }
    }
    
    /**
     * Отладочная информация о найденных классах
     */
    public void printDebugInfo() {
        System.out.println("=== ItemDataWrapper Debug Info ===");
        System.out.println("ItemData class: " + (itemDataClass != null ? itemDataClass.getName() : "NOT FOUND"));
        System.out.println("ItemTemplate class: " + (itemTemplateClass != null ? itemTemplateClass.getName() : "NOT FOUND"));
        System.out.println("ItemData instance: " + (itemDataInstance != null ? "OK" : "NULL"));
        
        // Тестируем с известным предметом (Adena = 57)
        Object adenaTemplate = getTemplate(57);
        if (adenaTemplate != null) {
            System.out.println("Test item (Adena): " + getItemName(57));
            System.out.println("Stack limit: " + getStackLimit(57));
        } else {
            System.out.println("ERROR: Could not get Adena template");
        }
        System.out.println("===================================");
    }
}

/**
 * Wrapper для совместимости со старым кодом
 * Эмулирует ItemData.getInstance().getTemplate()
 */
class ItemData {
    private static ItemData instance = new ItemData();
    
    public static ItemData getInstance() {
        return instance;
    }
    
    public Object getTemplate(int itemId) {
        return ItemDataWrapper.getInstance().getTemplate(itemId);
    }
    
    public String getItemName(int itemId) {
        return ItemDataWrapper.getInstance().getItemName(itemId);
    }
    
    public boolean itemExists(int itemId) {
        return ItemDataWrapper.getInstance().itemExists(itemId);
    }
}

/**
 * Простой класс шаблона предмета для совместимости
 */
class ItemTemplate {
    private final int itemId;
    private final String name;
    
    public ItemTemplate(int itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public int getItemId() {
        return itemId;
    }
}

/**
 * Утилиты для работы с предметами
 */
class ItemUtils {
    
    /**
     * Получает красивое название предмета с форматированием
     */
    public static String getFormattedItemName(int itemId, long count) {
        String itemName = ItemDataWrapper.getInstance().getItemName(itemId);
        
        if (count == 1) {
            return itemName;
        } else {
            return String.format("%s x%s", itemName, formatNumber(count));
        }
    }
    
    /**
     * Форматирует большие числа
     */
    public static String formatNumber(long number) {
        if (number >= 1_000_000_000) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
    
    /**
     * Проверяет является ли предмет валютой
     */
    public static boolean isCurrency(int itemId) {
        // Список популярных валют в L2
        int[] currencies = {57, 5575, 6673, 6674}; // Adena, Ancient Adena, etc.
        
        for (int currency : currencies) {
            if (currency == itemId) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Получает описание предмета для логов
     */
    public static String getItemDescription(int itemId) {
        return String.format("Item{id=%d, name='%s', type='%s'}", 
            itemId, 
            ItemDataWrapper.getInstance().getItemName(itemId),
            ItemDataWrapper.getInstance().getItemType(itemId)
        );
    }
}