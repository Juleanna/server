package com.l2jserver.datapack.custom.RewardForTimeOnline;

/**
 * Тестовый класс для проверки ItemDataWrapper
 */
public class ItemDataTest {
    
    public static void main(String[] args) {
        System.out.println("=== Тестирование ItemDataWrapper ===");
        
        ItemDataWrapper wrapper = ItemDataWrapper.getInstance();
        
        // Выводим отладочную информацию
        wrapper.printDebugInfo();
        
        // Тестируем популярные предметы
        int[] testItems = {
            57,     // Adena
            1538,   // Blessed Scroll of Escape
            1374,   // Greater Heal Potion
            6577,   // Blessed Enchant Weapon S
            6578,   // Blessed Enchant Armor S
            4037,   // Coin of Luck
            8762,   // Top-grade Life Stone
            1147    // Scroll of Resurrection
        };
        
        System.out.println("\n=== Тестирование предметов ===");
        for (int itemId : testItems) {
            testItem(itemId);
        }
        
        // Тестируем несуществующий предмет
        System.out.println("\n=== Тест несуществующего предмета ===");
        testItem(999999);
    }
    
    private static void testItem(int itemId) {
        ItemDataWrapper wrapper = ItemDataWrapper.getInstance();
        
        System.out.println("--- Item ID: " + itemId + " ---");
        System.out.println("Exists: " + wrapper.itemExists(itemId));
        System.out.println("Name: " + wrapper.getItemName(itemId));
        System.out.println("Type: " + wrapper.getItemType(itemId));
        System.out.println("Stack Limit: " + wrapper.getStackLimit(itemId));
        System.out.println("Formatted (x1000): " + ItemUtils.getFormattedItemName(itemId, 1000));
        System.out.println("Description: " + ItemUtils.getItemDescription(itemId));
        System.out.println("Is Currency: " + ItemUtils.isCurrency(itemId));
        System.out.println();
    }
    
    /**
     * Метод для интеграционного тестирования
     */
    public static boolean runCompatibilityTest() {
        try {
            ItemDataWrapper wrapper = ItemDataWrapper.getInstance();
            
            // Базовые тесты
            if (!wrapper.itemExists(57)) {
                System.err.println("FAIL: Adena not found!");
                return false;
            }
            
            String adenaName = wrapper.getItemName(57);
            if (adenaName == null || adenaName.trim().isEmpty()) {
                System.err.println("FAIL: Cannot get Adena name!");
                return false;
            }
            
            // Тест совместимости с основным кодом
            ItemData legacyWrapper = ItemData.getInstance();
            Object template = legacyWrapper.getTemplate(57);
            if (template == null) {
                System.err.println("FAIL: Legacy wrapper not working!");
                return false;
            }
            
            System.out.println("SUCCESS: All compatibility tests passed!");
            return true;
            
        } catch (Exception e) {
            System.err.println("FAIL: Exception during testing: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}