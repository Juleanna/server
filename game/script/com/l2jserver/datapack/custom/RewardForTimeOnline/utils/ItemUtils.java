package com.l2jserver.datapack.custom.RewardForTimeOnline.utils;

import java.text.DecimalFormat;
import java.util.Set;

/**
 * Утилиты для работы с предметами
 * @author Dafna
 */
public class ItemUtils {
    
    // Известные валюты в Lineage 2
    private static final Set<Integer> CURRENCY_ITEMS = Set.of(
        57,     // Adena
        5575,   // Ancient Adena
        6673,   // Seal of Gnosis
        6674,   // Forgotten Scroll
        4037,   // Coin of Luck
        8762,   // Top-grade Life Stone (часто используется как валюта)
        6360,   // Clan Reputation Points (не предмет, но ID)
        -200,   // Honor Points (виртуальная валюта)
        -300    // Fame Points (виртуальная валюта)
    );
    
    // Редкие предметы (особое форматирование)
    private static final Set<Integer> RARE_ITEMS = Set.of(
        6577,   // Blessed Enchant Weapon S
        6578,   // Blessed Enchant Armor S
        9552,   // Fire Stone
        9553,   // Water Stone
        9554,   // Earth Stone
        9555,   // Wind Stone
        9556,   // Dark Stone
        9557    // Holy Stone
    );
    
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    
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
     * Форматирует большие числа в читаемый вид
     */
    public static String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return DECIMAL_FORMAT.format(number / 1_000_000_000_000.0) + "T";
        } else if (number >= 1_000_000_000L) {
            return DECIMAL_FORMAT.format(number / 1_000_000_000.0) + "B";
        } else if (number >= 1_000_000L) {
            return DECIMAL_FORMAT.format(number / 1_000_000.0) + "M";
        } else if (number >= 1_000L) {
            return DECIMAL_FORMAT.format(number / 1_000.0) + "K";
        } else {
            return String.valueOf(number);
        }
    }
    
    /**
     * Форматирует число с разделителями тысяч
     */
    public static String formatNumberWithSeparators(long number) {
        return String.format("%,d", number);
    }
    
    /**
     * Проверяет является ли предмет валютой
     */
    public static boolean isCurrency(int itemId) {
        return CURRENCY_ITEMS.contains(itemId);
    }
    
    /**
     * Проверяет является ли предмет редким
     */
    public static boolean isRare(int itemId) {
        return RARE_ITEMS.contains(itemId);
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
    
    /**
     * Получает краткое описание предмета с количеством
     */
    public static String getShortDescription(int itemId, long count) {
        String name = ItemDataWrapper.getInstance().getItemName(itemId);
        
        // Сокращаем длинные названия
        if (name.length() > 20) {
            name = name.substring(0, 17) + "...";
        }
        
        return String.format("%s (%d)", name, count);
    }
    
    /**
     * Создает HTML представление предмета для веб-интерфейса
     */
    public static String getHtmlRepresentation(int itemId, long count) {
        String itemName = ItemDataWrapper.getInstance().getItemName(itemId);
        String formattedCount = formatNumber(count);
        String cssClass = getCssClass(itemId);
        
        return String.format("<span class=\"item %s\" data-item-id=\"%d\">" +
                           "<span class=\"item-name\">%s</span>" +
                           "<span class=\"item-count\">x%s</span>" +
                           "</span>", 
                           cssClass, itemId, itemName, formattedCount);
    }
    
    /**
     * Получает CSS класс для предмета
     */
    private static String getCssClass(int itemId) {
        if (isCurrency(itemId)) {
            return "currency";
        } else if (isRare(itemId)) {
            return "rare";
        } else {
            return "common";
        }
    }
    
    /**
     * Проверяет валидность ID предмета
     */
    public static boolean isValidItemId(int itemId) {
        // Виртуальные валюты могут иметь отрицательные ID
        if (itemId < 0) {
            return CURRENCY_ITEMS.contains(itemId);
        }
        
        // Обычные предметы должны иметь положительный ID
        return itemId > 0 && itemId < 100000; // Разумный верхний лимит
    }
    
    /**
     * Получает рекомендуемый размер стака для предмета
     */
    public static long getRecommendedStackSize(int itemId) {
        if (isCurrency(itemId)) {
            return 999999999L; // Большой стак для валют
        } else if (isRare(itemId)) {
            return 100L; // Небольшой стак для редких предметов
        } else {
            return 99999L; // Стандартный размер
        }
    }
    
    /**
     * Проверяет может ли предмет быть сложен в стак
     */
    public static boolean isStackable(int itemId) {
        ItemDataWrapper wrapper = ItemDataWrapper.getInstance();
        return wrapper.getStackLimit(itemId) > 1;
    }
    
    /**
     * Получает иконку предмета (для будущего использования)
     */
    public static String getItemIcon(int itemId) {
        // Базовая логика для иконок
        if (isCurrency(itemId)) {
            return "icon_currency.png";
        } else if (isRare(itemId)) {
            return "icon_rare.png";
        } else {
            return "icon_common.png";
        }
    }
    
    /**
     * Создает JSON представление предмета
     */
    public static String toJson(int itemId, long count) {
        return String.format(
            "{\"itemId\":%d,\"name\":\"%s\",\"count\":%d,\"type\":\"%s\",\"isCurrency\":%s,\"isRare\":%s}",
            itemId,
            ItemDataWrapper.getInstance().getItemName(itemId).replace("\"", "\\\""),
            count,
            ItemDataWrapper.getInstance().getItemType(itemId),
            isCurrency(itemId),
            isRare(itemId)
        );
    }
    
    /**
     * Парсит количество из строки с поддержкой суффиксов (1K, 2M, etc.)
     */
    public static long parseCount(String countStr) {
        if (countStr == null || countStr.trim().isEmpty()) {
            return 0;
        }
        
        countStr = countStr.trim().toUpperCase();
        
        try {
            // Проверяем суффиксы
            if (countStr.endsWith("K")) {
                return (long) (Double.parseDouble(countStr.substring(0, countStr.length() - 1)) * 1_000);
            } else if (countStr.endsWith("M")) {
                return (long) (Double.parseDouble(countStr.substring(0, countStr.length() - 1)) * 1_000_000);
            } else if (countStr.endsWith("B")) {
                return (long) (Double.parseDouble(countStr.substring(0, countStr.length() - 1)) * 1_000_000_000);
            } else if (countStr.endsWith("T")) {
                return (long) (Double.parseDouble(countStr.substring(0, countStr.length() - 1)) * 1_000_000_000_000L);
            } else {
                return Long.parseLong(countStr);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid count format: " + countStr);
        }
    }
    
    /**
     * Валидирует количество предмета
     */
    public static boolean isValidCount(long count) {
        return count > 0 && count <= 999_999_999_999L;
    }
    
    /**
     * Получает цвет для предмета (в зависимости от редкости)
     */
    public static String getItemColor(int itemId) {
        if (isCurrency(itemId)) {
            return "#FFD700"; // Золотой для валют
        } else if (isRare(itemId)) {
            return "#FF6B6B"; // Красный для редких
        } else {
            return "#4ECDC4"; // Бирюзовый для обычных
        }
    }
    
    /**
     * Создает краткий отчет о предмете
     */
    public static String createItemReport(int itemId, long count) {
        ItemDataWrapper wrapper = ItemDataWrapper.getInstance();
        
        StringBuilder report = new StringBuilder();
        report.append("=== Item Report ===\n");
        report.append(String.format("ID: %d\n", itemId));
        report.append(String.format("Name: %s\n", wrapper.getItemName(itemId)));
        report.append(String.format("Count: %s (%d)\n", formatNumber(count), count));
        report.append(String.format("Type: %s\n", wrapper.getItemType(itemId)));
        report.append(String.format("Stack Limit: %d\n", wrapper.getStackLimit(itemId)));
        report.append(String.format("Is Currency: %s\n", isCurrency(itemId)));
        report.append(String.format("Is Rare: %s\n", isRare(itemId)));
        report.append(String.format("Is Stackable: %s\n", isStackable(itemId)));
        report.append("==================");
        
        return report.toString();
    }
    
    /**
     * Сравнивает два предмета по ценности (для сортировки)
     */
    public static int compareItemValue(int itemId1, int itemId2) {
        // Валюты имеют высший приоритет
        boolean isCurrency1 = isCurrency(itemId1);
        boolean isCurrency2 = isCurrency(itemId2);
        
        if (isCurrency1 && !isCurrency2) return -1;
        if (!isCurrency1 && isCurrency2) return 1;
        
        // Редкие предметы имеют приоритет над обычными
        boolean isRare1 = isRare(itemId1);
        boolean isRare2 = isRare(itemId2);
        
        if (isRare1 && !isRare2) return -1;
        if (!isRare1 && isRare2) return 1;
        
        // По умолчанию сравниваем по ID
        return Integer.compare(itemId1, itemId2);
    }
    
    /**
     * Добавляет кастомный предмет в систему
     */
    public static void addCustomItem(int itemId, String name, boolean isCurrency, boolean isRare) {
        ItemDataWrapper.getInstance().addKnownItem(itemId, name);
        
        // Это требует модификации констант, что не очень хорошо
        // В будущем можно сделать конфигурируемые списки
        if (isCurrency) {
            // CURRENCY_ITEMS.add(itemId); // Нельзя модифицировать immutable Set
        }
        
        if (isRare) {
            // RARE_ITEMS.add(itemId); // Нельзя модифицировать immutable Set
        }
    }
}