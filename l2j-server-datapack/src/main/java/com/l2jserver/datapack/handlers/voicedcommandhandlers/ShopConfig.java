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

public class ShopConfig {
    
    /**
     * Класс для представления товара в магазине
     */
    public static class ShopItem {
        private final int itemId;
        private final String name;
        private final long price;
        private final int count;
        private final String description;
        private final String icon;
        
        public ShopItem(int itemId, String name, long price, int count, String description, String icon) {
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.count = count;
            this.description = description;
            this.icon = icon;
        }
        
        // Getters
        public int getItemId() { return itemId; }
        public String getName() { return name; }
        public long getPrice() { return price; }
        public int getCount() { return count; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
    }
    
    // Категории магазина
    public static final Map<String, List<ShopItem>> SHOP_CATEGORIES = new HashMap<>();
    
    static {
        initializeShop();
    }
    
    /**
     * Инициализация товаров магазина
     */
    private static void initializeShop() {
        
        // === ОРУЖИЕ ===
        List<ShopItem> weapons = new ArrayList<>();
        weapons.add(new ShopItem(4, "Wooden Sword", 100000, 1, "Базовый деревянный меч", "🗡️"));
        weapons.add(new ShopItem(7, "Bastard Sword", 500000, 1, "Двуручный меч", "⚔️"));
        weapons.add(new ShopItem(174, "Blade Dagger", 200000, 1, "Острый кинжал", "🗡️"));
        weapons.add(new ShopItem(2, "Wooden Bow", 150000, 1, "Деревянный лук", "🏹"));
        weapons.add(new ShopItem(127, "Apprentice's Staff", 120000, 1, "Посох ученика", "🪄"));
        SHOP_CATEGORIES.put("weapons", weapons);
        
        // === БРОНЯ ===
        List<ShopItem> armor = new ArrayList<>();
        armor.add(new ShopItem(23, "Leather Helmet", 50000, 1, "Кожаный шлем", "🪖"));
        armor.add(new ShopItem(1146, "Leather Armor", 150000, 1, "Кожаная броня", "🦺"));
        armor.add(new ShopItem(37, "Leather Pants", 80000, 1, "Кожаные штаны", "👖"));
        armor.add(new ShopItem(52, "Leather Gloves", 40000, 1, "Кожаные перчатки", "🧤"));
        armor.add(new ShopItem(1104, "Leather Boots", 60000, 1, "Кожаные ботинки", "👢"));
        armor.add(new ShopItem(627, "Wooden Shield", 70000, 1, "Деревянный щит", "🛡️"));
        SHOP_CATEGORIES.put("armor", armor);
        
        // === ЗЕЛЬЯ ===
        List<ShopItem> potions = new ArrayList<>();
        potions.add(new ShopItem(1060, "Greater Healing Potion", 10000, 100, "Восстанавливает много HP", "🧪"));
        potions.add(new ShopItem(728, "Greater Magic Haste Potion", 15000, 50, "Увеличивает скорость каста", "🍶"));
        potions.add(new ShopItem(1374, "Greater Haste Potion", 15000, 50, "Увеличивает скорость атаки", "🥤"));
        potions.add(new ShopItem(1540, "Quick Healing Potion", 5000, 100, "Быстро восстанавливает HP", "💊"));
        potions.add(new ShopItem(726, "Magic Haste Potion", 8000, 50, "Скорость каста", "🧴"));
        potions.add(new ShopItem(735, "Greater Swift Attack Potion", 12000, 30, "Критическая атака", "⚗️"));
        SHOP_CATEGORIES.put("potions", potions);
        
        // === SOUL/SPIRIT SHOTS ===
        List<ShopItem> shots = new ArrayList<>();
        shots.add(new ShopItem(1835, "Soulshot: No Grade", 30, 10000, "Снаряды для физ. атак", "💥"));
        shots.add(new ShopItem(1463, "Blessed Spiritshot: No Grade", 50, 10000, "Благословленные снаряды для магии", "✨"));
        shots.add(new ShopItem(1464, "Blessed Spiritshot: Grade D", 100, 5000, "Снаряды D грейда", "💫"));
        shots.add(new ShopItem(1465, "Blessed Spiritshot: Grade C", 200, 2500, "Снаряды C грейда", "🌟"));
        shots.add(new ShopItem(1466, "Blessed Spiritshot: Grade B", 400, 1000, "Снаряды B грейда", "⭐"));
        shots.add(new ShopItem(1467, "Blessed Spiritshot: Grade A", 800, 500, "Снаряды A грейда", "💎"));
        SHOP_CATEGORIES.put("shots", shots);
        
        // === СВИТКИ ===
        List<ShopItem> scrolls = new ArrayList<>();
        scrolls.add(new ShopItem(3936, "Blessed Scroll of Escape", 50000, 10, "Телепорт в город", "📜"));
        scrolls.add(new ShopItem(3959, "Blessed Scroll of Resurrection", 100000, 5, "Воскрешение без потери опыта", "📋"));
        scrolls.add(new ShopItem(955, "Scroll: Enchant Weapon (Grade D)", 200000, 1, "Заточка оружия D грейда", "📃"));
        scrolls.add(new ShopItem(951, "Scroll: Enchant Weapon (Grade C)", 500000, 1, "Заточка оружия C грейда", "📄"));
        scrolls.add(new ShopItem(947, "Scroll: Enchant Weapon (Grade B)", 1000000, 1, "Заточка оружия B грейда", "📑"));
        scrolls.add(new ShopItem(729, "Scroll: Enchant Weapon (Grade A)", 2000000, 1, "Заточка оружия A грейда", "📒"));
        SHOP_CATEGORIES.put("scrolls", scrolls);
        
        // === МАТЕРИАЛЫ ===
        List<ShopItem> materials = new ArrayList<>();
        materials.add(new ShopItem(1458, "Crystal: D-Grade", 1000, 1000, "Кристаллы D грейда", "💎"));
        materials.add(new ShopItem(1459, "Crystal: C-Grade", 2000, 500, "Кристаллы C грейда", "💠"));
        materials.add(new ShopItem(1460, "Crystal: B-Grade", 4000, 250, "Кристаллы B грейда", "🔷"));
        materials.add(new ShopItem(1461, "Crystal: A-Grade", 8000, 100, "Кристаллы A грейда", "🔹"));
        materials.add(new ShopItem(1462, "Crystal: S-Grade", 16000, 50, "Кристаллы S грейда", "💙"));
        materials.add(new ShopItem(1865, "Varnish", 500, 1000, "Лак для крафта", "🧪"));
        SHOP_CATEGORIES.put("materials", materials);
        
        // === АКСЕССУАРЫ ===
        List<ShopItem> accessories = new ArrayList<>();
        accessories.add(new ShopItem(112, "Necklace of Magic", 100000, 1, "Ожерелье магии", "📿"));
        accessories.add(new ShopItem(845, "Cat's Eye Earring", 150000, 1, "Серьги кошачий глаз", "💍"));
        accessories.add(new ShopItem(875, "Ring of Ages", 80000, 1, "Кольцо веков", "💎"));
        accessories.add(new ShopItem(876, "Ring of Wisdom", 120000, 1, "Кольцо мудрости", "🔮"));
        accessories.add(new ShopItem(877, "Blue Coral Ring", 90000, 1, "Кольцо синего коралла", "💙"));
        accessories.add(new ShopItem(878, "Ring of Devotion", 110000, 1, "Кольцо преданности", "❤️"));
        SHOP_CATEGORIES.put("accessories", accessories);
        
        // === ОСОБЫЕ ПРЕДМЕТЫ ===
        List<ShopItem> special = new ArrayList<>();
        special.add(new ShopItem(3470, "Gold Bar", 500000, 1, "Золотой слиток", "🏅"));
        special.add(new ShopItem(4037, "Coin of Luck", 1000000, 1, "Монета удачи", "🪙"));
        special.add(new ShopItem(5575, "Event - Mysterious Box", 750000, 1, "Таинственная коробка", "📦"));
        special.add(new ShopItem(6673, "Transformation Scroll", 2000000, 1, "Свиток трансформации", "🎭"));
        special.add(new ShopItem(9140, "Agathion Seal Bracelet", 5000000, 1, "Браслет агатиона", "💫"));
        SHOP_CATEGORIES.put("special", special);
    }
    
    /**
     * Получить товары категории
     */
    public static List<ShopItem> getCategoryItems(String category) {
        return SHOP_CATEGORIES.getOrDefault(category.toLowerCase(), new ArrayList<>());
    }
    
    /**
     * Получить все категории
     */
    public static String[] getCategories() {
        return SHOP_CATEGORIES.keySet().toArray(new String[0]);
    }
    
    /**
     * Найти товар по ID
     */
    public static ShopItem findItem(int itemId) {
        for (List<ShopItem> items : SHOP_CATEGORIES.values()) {
            for (ShopItem item : items) {
                if (item.getItemId() == itemId) {
                    return item;
                }
            }
        }
        return null;
    }
    
    /**
     * Получить отображаемое имя категории
     */
    public static String getCategoryDisplayName(String category) {
        switch (category.toLowerCase()) {
            case "weapons": return "🗡️ Оружие";
            case "armor": return "🛡️ Броня";
            case "potions": return "🧪 Зелья";
            case "shots": return "💥 Снаряды";
            case "scrolls": return "📜 Свитки";
            case "materials": return "💎 Материалы";
            case "accessories": return "💍 Аксессуары";
            case "special": return "⭐ Особые";
            default: return category.toUpperCase();
        }
    }
    
    /**
     * Проверить, доступен ли товар для покупки
     */
    public static boolean isItemAvailable(int itemId, L2PcInstance player) {
        ShopItem item = findItem(itemId);
        if (item == null) {
            return false;
        }
        
        // Проверка уровня игрока для некоторых предметов
        if (itemId >= 947 && itemId <= 959) { // Свитки заточки
            return player.getLevel() >= 20;
        }
        
        if (itemId >= 1466 && itemId <= 1467) { // Высокоуровневые снаряды
            return player.getLevel() >= 40;
        }
        
        return true;
    }
    
    /**
     * Получить модифицированную цену с учетом скидок
     */
    public static long getModifiedPrice(ShopItem item, L2PcInstance player) {
        long basePrice = item.getPrice();
        
        // Скидка для новобранцев
        if (player.getLevel() <= 20) {
            basePrice = (long)(basePrice * 0.8); // 20% скидка
        }
        
        // Скидка для VIP игроков (если есть система VIP)
        if (player.isNoble()) {
            basePrice = (long)(basePrice * 0.9); // 10% скидка для дворян
        }
        
        // Скидка для клановых игроков
        if (player.getClan() != null && player.getClan().getLevel() >= 5) {
            basePrice = (long)(basePrice * 0.95); // 5% скидка
        }
        
        return basePrice;
    }
}

// Импорт для L2PcInstance
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;