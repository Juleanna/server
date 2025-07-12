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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.CrystalType;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.ItemList;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * Расширенная система заточки для панели игрока
 * @author YourName
 */
public class EnchantSystem {
    
    private static final Random RANDOM = new Random();
    
    /**
     * Типы заточки
     */
    public enum EnchantType {
        WEAPON("weapon", "Оружие", "⚔️"),
        ARMOR("armor", "Броня", "🛡️"),
        JEWELRY("jewelry", "Украшения", "💍");
        
        private final String key;
        private final String displayName;
        private final String icon;
        
        EnchantType(String key, String displayName, String icon) {
            this.key = key;
            this.displayName = displayName;
            this.icon = icon;
        }
        
        public String getKey() { return key; }
        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
    }
    
    /**
     * Настройки заточки по грейдам
     */
    private static final Map<CrystalType, EnchantConfig> ENCHANT_CONFIGS = new HashMap<>();
    
    static {
        // Настройки шансов заточки по грейдам
        ENCHANT_CONFIGS.put(CrystalType.NONE, new EnchantConfig(90, 80, 70, 100000));
        ENCHANT_CONFIGS.put(CrystalType.D, new EnchantConfig(80, 70, 60, 200000));
        ENCHANT_CONFIGS.put(CrystalType.C, new EnchantConfig(70, 60, 50, 500000));
        ENCHANT_CONFIGS.put(CrystalType.B, new EnchantConfig(60, 50, 40, 1000000));
        ENCHANT_CONFIGS.put(CrystalType.A, new EnchantConfig(50, 40, 30, 2000000));
        ENCHANT_CONFIGS.put(CrystalType.S, new EnchantConfig(40, 30, 20, 5000000));
        ENCHANT_CONFIGS.put(CrystalType.S80, new EnchantConfig(35, 25, 15, 10000000));
        ENCHANT_CONFIGS.put(CrystalType.S84, new EnchantConfig(30, 20, 10, 20000000));
    }
    
    /**
     * Класс конфигурации заточки
     */
    private static class EnchantConfig {
        private final int safeChance;    // Шанс до +3
        private final int normalChance;  // Шанс +4 до +9
        private final int dangerChance;  // Шанс +10 и выше
        private final long baseCost;     // Базовая стоимость
        
        public EnchantConfig(int safeChance, int normalChance, int dangerChance, long baseCost) {
            this.safeChance = safeChance;
            this.normalChance = normalChance;
            this.dangerChance = dangerChance;
            this.baseCost = baseCost;
        }
        
        public int getSafeChance() { return safeChance; }
        public int getNormalChance() { return normalChance; }
        public int getDangerChance() { return dangerChance; }
        public long getBaseCost() { return baseCost; }
    }
    
    /**
     * Результат заточки
     */
    public enum EnchantResult {
        SUCCESS("Успех! Предмет заточен!", "✅", 2061),
        FAILURE("Заточка не удалась!", "❌", 2062),
        BREAK("Предмет разрушен!", "💥", 2063),
        MAX_REACHED("Достигнут максимальный уровень!", "⭐", 2064);
        
        private final String message;
        private final String icon;
        private final int effectSkillId;
        
        EnchantResult(String message, String icon, int effectSkillId) {
            this.message = message;
            this.icon = icon;
            this.effectSkillId = effectSkillId;
        }
        
        public String getMessage() { return message; }
        public String getIcon() { return icon; }
        public int getEffectSkillId() { return effectSkillId; }
    }
    
    /**
     * Выполняет заточку предмета
     */
    public static EnchantResult enchantItem(L2PcInstance player, L2ItemInstance item, int targetLevel, boolean safeEnchant) {
        if (item == null || !item.isEquipped()) {
            return EnchantResult.FAILURE;
        }
        
        int currentEnchant = item.getEnchantLevel();
        int maxEnchant = getMaxEnchantLevel(item);
        
        // Проверка максимального уровня
        if (currentEnchant >= maxEnchant) {
            return EnchantResult.MAX_REACHED;
        }
        
        // Вычисляем стоимость
        long cost = calculateEnchantCost(item, targetLevel, safeEnchant);
        if (player.getAdena() < cost) {
            player.sendMessage("Недостаточно адены! Необходимо: " + cost);
            return EnchantResult.FAILURE;
        }
        
        // Снимаем адену
        player.reduceAdena("Enchant", cost, null, true);
        
        // Выполняем заточку
        boolean success = performEnchant(item, targetLevel, safeEnchant);
        
        if (success) {
            item.setEnchantLevel(Math.min(currentEnchant + targetLevel, maxEnchant));
            updatePlayerInventory(player, item);
            playEnchantEffect(player, EnchantResult.SUCCESS);
            return EnchantResult.SUCCESS;
        } else {
            // Проверяем, ломается ли предмет
            if (!safeEnchant && shouldItemBreak(item)) {
                // Разрушаем предмет
                player.getInventory().destroyItem("Enchant Break", item, player, null);
                updatePlayerInventory(player, null);
                playEnchantEffect(player, EnchantResult.BREAK);
                return EnchantResult.BREAK;
            } else {
                playEnchantEffect(player, EnchantResult.FAILURE);
                return EnchantResult.FAILURE;
            }
        }
    }
    
    /**
     * Вычисляет шанс успеха заточки
     */
    private static boolean performEnchant(L2ItemInstance item, int targetLevel, boolean safeEnchant) {
        CrystalType grade = item.getItem().getCrystalType();
        EnchantConfig config = ENCHANT_CONFIGS.get(grade);
        
        if (config == null) {
            config = ENCHANT_CONFIGS.get(CrystalType.NONE);
        }
        
        int currentEnchant = item.getEnchantLevel();
        int chance;
        
        // Определяем шанс в зависимости от текущего уровня заточки
        if (currentEnchant < 3) {
            chance = config.getSafeChance();
        } else if (currentEnchant < 9) {
            chance = config.getNormalChance();
        } else {
            chance = config.getDangerChance();
        }
        
        // Бонус для безопасной заточки
        if (safeEnchant) {
            chance += 20;
        }
        
        // Уменьшаем шанс для множественной заточки
        if (targetLevel > 1) {
            chance = (int)(chance * Math.pow(0.8, targetLevel - 1));
        }
        
        return RANDOM.nextInt(100) < chance;
    }
    
    /**
     * Проверяет, должен ли предмет сломаться
     */
    private static boolean shouldItemBreak(L2ItemInstance item) {
        int currentEnchant = item.getEnchantLevel();
        
        // Предметы до +3 не ломаются
        if (currentEnchant < 3) {
            return false;
        }
        
        // Шанс поломки увеличивается с уровнем заточки
        int breakChance = Math.max(5, (currentEnchant - 2) * 10);
        return RANDOM.nextInt(100) < breakChance;
    }
    
    /**
     * Вычисляет стоимость заточки
     */
    public static long calculateEnchantCost(L2ItemInstance item, int levels, boolean safeEnchant) {
        CrystalType grade = item.getItem().getCrystalType();
        EnchantConfig config = ENCHANT_CONFIGS.get(grade);
        
        if (config == null) {
            config = ENCHANT_CONFIGS.get(CrystalType.NONE);
        }
        
        long baseCost = config.getBaseCost();
        int currentEnchant = item.getEnchantLevel();
        
        // Базовая стоимость * уровень заточки * количество уровней
        long cost = baseCost * (currentEnchant + 1) * levels;
        
        // Множитель для безопасной заточки
        if (safeEnchant) {
            cost *= 3;
        }
        
        // Множитель для высоких уровней
        if (currentEnchant >= 10) {
            cost *= 2;
        }
        
        return cost;
    }
    
    /**
     * Получает максимальный уровень заточки для предмета
     */
    private static int getMaxEnchantLevel(L2ItemInstance item) {
        if (item.getItem().getType2() == L2Item.TYPE2_WEAPON) {
            return 25; // Максимум для оружия
        } else {
            return 20; // Максимум для брони и аксессуаров
        }
    }
    
    /**
     * Обновляет инвентарь игрока
     */
    private static void updatePlayerInventory(L2PcInstance player, L2ItemInstance item) {
        if (item != null) {
            InventoryUpdate iu = new InventoryUpdate();
            iu.addModifiedItem(item);
            player.sendPacket(iu);
        }
        player.sendPacket(new ItemList(player, false));
        player.broadcastUserInfo();
    }
    
    /**
     * Воспроизводит эффект заточки
     */
    private static void playEnchantEffect(L2PcInstance player, EnchantResult result) {
        MagicSkillUse msu = new MagicSkillUse(player, player, result.getEffectSkillId(), 1, 1, 0);
        Broadcast.toSelfAndKnownPlayersInRadius(player, msu, 600);
    }
    
    /**
     * Получает все экипированные предметы определенного типа
     */
    public static L2ItemInstance[] getEquippedItems(L2PcInstance player, EnchantType type) {
        switch (type) {
            case WEAPON:
                L2ItemInstance weapon = player.getActiveWeaponInstance();
                return weapon != null ? new L2ItemInstance[]{weapon} : new L2ItemInstance[0];
                
            case ARMOR:
                return player.getInventory().getItems().stream()
                    .filter(item -> item.isEquipped() && 
                           item.getItem().getBodyPart() != L2Item.SLOT_R_HAND &&
                           item.getItem().getBodyPart() != L2Item.SLOT_L_HAND &&
                           item.getItem().getBodyPart() != L2Item.SLOT_LR_HAND &&
                           item.getItem().getBodyPart() != L2Item.SLOT_R_EAR &&
                           item.getItem().getBodyPart() != L2Item.SLOT_L_EAR &&
                           item.getItem().getBodyPart() != L2Item.SLOT_NECK &&
                           item.getItem().getBodyPart() != L2Item.SLOT_R_FINGER &&
                           item.getItem().getBodyPart() != L2Item.SLOT_L_FINGER)
                    .toArray(L2ItemInstance[]::new);
                    
            case JEWELRY:
                return player.getInventory().getItems().stream()
                    .filter(item -> item.isEquipped() && 
                           (item.getItem().getBodyPart() == L2Item.SLOT_R_EAR ||
                            item.getItem().getBodyPart() == L2Item.SLOT_L_EAR ||
                            item.getItem().getBodyPart() == L2Item.SLOT_NECK ||
                            item.getItem().getBodyPart() == L2Item.SLOT_R_FINGER ||
                            item.getItem().getBodyPart() == L2Item.SLOT_L_FINGER))
                    .toArray(L2ItemInstance[]::new);
                    
            default:
                return new L2ItemInstance[0];
        }
    }
    
    /**
     * Генерирует HTML для отображения предмета
     */
    public static String generateItemDisplay(L2ItemInstance item) {
        if (item == null) {
            return "<font color=\"AAAAAA\">Предмет не экипирован</font>";
        }
        
        StringBuilder html = new StringBuilder();
        html.append("<table width=\"100%\" bgcolor=\"111111\">");
        html.append("<tr>");
        html.append("<td width=\"200\">").append(item.getName());
        
        if (item.getEnchantLevel() > 0) {
            html.append(" <font color=\"LEVEL\">+").append(item.getEnchantLevel()).append("</font>");
        }
        
        html.append("</td>");
        html.append("<td width=\"80\">").append(item.getItem().getCrystalType().toString()).append("</td>");
        html.append("</tr>");
        html.append("</table>");
        
        return html.toString();
    }
    
    /**
     * Получает информацию о стоимости заточки для отображения
     */
    public static String getEnchantCostInfo(L2ItemInstance item, int levels, boolean safeEnchant) {
        if (item == null) {
            return "Предмет не выбран";
        }
        
        long cost = calculateEnchantCost(item, levels, safeEnchant);
        String type = safeEnchant ? "Безопасная" : "Обычная";
        
        return String.format("%s заточка на +%d: %,d адены", type, levels, cost);
    }
}