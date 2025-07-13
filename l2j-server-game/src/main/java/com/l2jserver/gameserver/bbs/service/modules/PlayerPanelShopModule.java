/*
 * Copyright © 2004-2023 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.bbs.service.modules;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * Модуль магазина для панели игрока
 * @author YourName
 */
public class PlayerPanelShopModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayerPanelShopModule.class);
    
    private final PlayerPanelConfig _config;
    private final PlayerPanelValidator _validator;
    private final PlayerPanelHtmlGenerator _htmlGenerator;
    
    public PlayerPanelShopModule(PlayerPanelConfig config) {
        _config = config;
        _validator = new PlayerPanelValidator(config);
        _htmlGenerator = new PlayerPanelHtmlGenerator(config);
    }
    
    /**
     * Обработка магазина
     */
    public void handleShop(L2PcInstance player, StringTokenizer st) {
        if (!_validator.canUseShop(player)) {
            showShopPanel(player);
            return;
        }
        
        if (!st.hasMoreTokens()) {
            showShopPanel(player);
            return;
        }
        
        String action = st.nextToken();
        if (!"buy".equals(action)) {
            showShopPanel(player);
            return;
        }
        
        if (!st.hasMoreTokens()) {
            showShopPanel(player);
            return;
        }
        
        String itemId = st.nextToken();
        if (!st.hasMoreTokens()) {
            showShopPanel(player);
            return;
        }
        
        long price = Long.parseLong(st.nextToken());
        
        // Проверка доступности предмета
        if (!_validator.isItemAvailableInShop(itemId)) {
            player.sendMessage("Этот предмет недоступен для покупки!");
            showShopPanel(player);
            return;
        }
        
        // Проверка денег
        if (player.getAdena() < price) {
            player.sendMessage("Недостаточно Adena для покупки!");
            showShopPanel(player);
            return;
        }
        
        // Получение реального ID предмета
        int realItemId = getItemIdByShopId(itemId);
        if (realItemId == 0) {
            player.sendMessage("Предмет не найден!");
            showShopPanel(player);
            return;
        }
        
        // Проверка кулдауна
        if (PlayerPanelDAO.hasCooldown(player.getObjectId(), "shop")) {
            player.sendMessage("Подождите перед следующей покупкой!");
            showShopPanel(player);
            return;
        }
        
        // Проверка места в инвентаре
        if (player.getInventory().getSize() >= player.getInventoryLimit()) {
            player.sendMessage("Инвентарь переполнен!");
            showShopPanel(player);
            return;
        }
        
        // Выполнение покупки
        performPurchase(player, realItemId, itemId, price);
    }
    
    /**
     * Выполнить покупку
     */
    private void performPurchase(L2PcInstance player, int realItemId, String shopItemId, long price) {
        // Снятие денег
        player.reduceAdena("Shop", price, null, true);
        
        // Определение количества предметов
        int quantity = getItemQuantity(shopItemId);
        
        // Добавление предмета
        L2ItemInstance newItem = player.addItem("Shop", realItemId, quantity, null, true);
        String itemName = newItem != null ? newItem.getName() : "Unknown Item";
        
        if (quantity > 1) {
            player.sendMessage("✅ Покупка успешна! " + itemName + " x" + quantity + " добавлен в инвентарь.");
        } else {
            player.sendMessage("✅ Покупка успешна! " + itemName + " добавлен в инвентарь.");
        }
        
        // Проверка на специальные предметы
        handleSpecialItems(player, realItemId, quantity);
        
        // Логирование покупки
        PlayerPanelDAO.logPurchase(player, realItemId, itemName, quantity, price / quantity, price, "adena");
        PlayerPanelDAO.logAction(player, "shop_buy", "Bought " + itemName + " x" + quantity + " for " + price + " adena", price, true);
        
        // Установка кулдауна
        if (_config.getAntiSpamInterval() > 0) {
            PlayerPanelDAO.setCooldown(player.getObjectId(), "shop", _config.getAntiSpamInterval() * 1000);
        }
        
        showShopPanel(player);
    }
    
    /**
     * Обработка специальных предметов
     */
    private void handleSpecialItems(L2PcInstance player, int itemId, int quantity) {
        switch (itemId) {
            case 1060: // Healing Potion
                if (quantity >= 10) {
                    player.sendMessage("🎁 Бонус! Получен дополнительный опыт за покупку зелий!");
                    player.addExpAndSp(1000, 100);
                }
                break;
            case 729: // Enchant Scroll
                if (quantity >= 5) {
                    player.sendMessage("🎁 Бонус! Получен благословенный свиток за покупку обычных!");
                    player.addItem("Bonus", 6569, 1, null, true); // Blessed Enchant Scroll
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * Получить количество предмета по ID магазина
     */
    private int getItemQuantity(String shopItemId) {
        switch (shopItemId) {
            case "3": // Enchant Scroll
            case "10": // Blessed Scroll
                return 1;
            case "4": // Healing Potion
                return 10;
            case "6": // Soul Crystal
                return 5;
            default:
                return 1;
        }
    }
    
    /**
     * Получить реальный ID предмета по ID магазина
     */
    private int getItemIdByShopId(String shopId) {
        switch (shopId) {
            case "1": return 2; // Sword
            case "2": return 23; // Leather Armor
            case "3": return 729; // Weapon Enchant Scroll
            case "4": return 1060; // Lesser Healing Potion
            case "5": return 881; // Ring
            case "6": return 1458; // Crystal
            case "7": return 4; // Bow
            case "8": return 224; // Dagger
            case "9": return 1403; // Staff
            case "10": return 6569; // Blessed Enchant Scroll
            default: return 0;
        }
    }
    
    /**
     * Получить название предмета по ID магазина
     */
    private String getItemNameByShopId(String shopId) {
        switch (shopId) {
            case "1": return "Demon Sword";
            case "2": return "Mithril Armor";
            case "3": return "Enchant Scroll";
            case "4": return "Healing Potion";
            case "5": return "Power Ring";
            case "6": return "Soul Crystal";
            case "7": return "Elven Bow";
            case "8": return "Dagger Set";
            case "9": return "Magic Staff";
            case "10": return "Blessed Scroll";
            default: return "Unknown Item";
        }
    }
    
    /**
     * Проверить наличие скидки для игрока
     */
    private double getPlayerDiscount(L2PcInstance player) {
        double discount = 0.0;
        
        // Скидка для премиум игроков
        double premiumDiscount = _config.getPremiumDiscount();
        if (premiumDiscount > 0 && player.hasPremiumStatus()) {
            discount += premiumDiscount / 100.0;
        }
        
        // Скидка во время событий
        if (_config.isEventsEnabled()) {
            double eventDiscount = _config.getEventDiscount();
            if (eventDiscount > 0 && isEventActive()) {
                discount += eventDiscount / 100.0;
            }
        }
        
        // Скидка для клановых участников
        if (player.getClan() != null && player.getClan().getLevel() >= 5) {
            discount += 0.05; // 5% скидка для кланов 5+ уровня
        }
        
        // Максимальная скидка 50%
        return Math.min(discount, 0.5);
    }
    
    /**
     * Проверить, активно ли событие
     */
    private boolean isEventActive() {
        // Здесь можно добавить логику проверки активных событий
        // Например, проверка определенных дат или флагов сервера
        return false;
    }
    
    /**
     * Рассчитать финальную цену с учетом скидок
     */
    private long calculateFinalPrice(L2PcInstance player, long basePrice) {
        double discount = getPlayerDiscount(player);
        
        if (discount > 0) {
            long discountedPrice = (long)(basePrice * (1.0 - discount));
            player.sendMessage("💰 Применена скидка " + String.format("%.0f", discount * 100) + "%!");
            return discountedPrice;
        }
        
        return basePrice;
    }
    
    /**
     * Проверить лимиты покупок
     */
    private boolean checkPurchaseLimits(L2PcInstance player, long price) {
        // Проверка дневного лимита покупок
        long dailyLimit = _config.getDailyPurchaseLimit();
        if (dailyLimit > 0) {
            // Здесь должна быть логика проверки потраченной суммы за день
            // Для упрощения пропускаем
        }
        
        // Проверка налога на покупки
        double taxRate = _config.getPurchaseTax();
        if (taxRate > 0) {
            long tax = (long)(price * taxRate / 100.0);
            if (player.getAdena() < price + tax) {
                player.sendMessage("Недостаточно Adena для покупки с налогом! Налог: " + tax + " Adena");
                return false;
            }
            
            // Снятие налога
            player.reduceAdena("Tax", tax, null, true);
            player.sendMessage("💰 Уплачен налог: " + tax + " Adena");
        }
        
        return true;
    }
    
    /**
     * Показать панель магазина
     */
    private void showShopPanel(L2PcInstance player) {
        String html = _htmlGenerator.generateMainPanel(player, "shop");
        CommunityBoardHandler.separateAndSend(html, player);
    }
}