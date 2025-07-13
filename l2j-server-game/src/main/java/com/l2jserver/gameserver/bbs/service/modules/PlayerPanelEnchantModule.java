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

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;

/**
 * Модуль системы заточки для панели игрока
 * @author YourName
 */
public class PlayerPanelEnchantModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayerPanelEnchantModule.class);
    
    private final PlayerPanelConfig _config;
    private final PlayerPanelValidator _validator;
    private final PlayerPanelHtmlGenerator _htmlGenerator;
    
    public PlayerPanelEnchantModule(PlayerPanelConfig config) {
        _config = config;
        _validator = new PlayerPanelValidator(config);
        _htmlGenerator = new PlayerPanelHtmlGenerator(config);
    }
    
    /**
     * Обработка заточки
     */
    public void handleEnchant(L2PcInstance player, StringTokenizer st) {
        if (!_validator.canUseEnchant(player)) {
            showEnchantPanel(player);
            return;
        }
        
        if (!st.hasMoreTokens()) {
            showEnchantPanel(player);
            return;
        }
        
        String action = st.nextToken();
        if (!"item".equals(action)) {
            showEnchantPanel(player);
            return;
        }
        
        if (!st.hasMoreTokens()) {
            showEnchantPanel(player);
            return;
        }
        
        int itemObjId = Integer.parseInt(st.nextToken());
        L2ItemInstance item = player.getInventory().getItemByObjectId(itemObjId);
        
        if (item == null || !item.isEnchantable()) {
            player.sendMessage("Предмет не найден или не может быть заточен!");
            showEnchantPanel(player);
            return;
        }
        
        // Проверка доступности предмета для заточки
        if (!_validator.canEnchantItem(player, item.getId())) {
            showEnchantPanel(player);
            return;
        }
        
        // Проверка максимального уровня
        if (item.getEnchantLevel() >= _config.getMaxEnchantLevel()) {
            player.sendMessage("Достигнут максимальный уровень заточки!");
            showEnchantPanel(player);
            return;
        }
        
        // Поиск подходящих свитков
        L2ItemInstance scroll = findEnchantScroll(player, item);
        if (scroll == null) {
            player.sendMessage("У вас нет подходящих свитков заточки!");
            showEnchantPanel(player);
            return;
        }
        
        // Проверка стоимости
        long cost = calculateEnchantCost(item);
        if (player.getAdena() < cost) {
            player.sendMessage("Недостаточно Adena для заточки! Требуется: " + formatNumber(cost));
            showEnchantPanel(player);
            return;
        }
        
        // Проверка кулдауна
        if (PlayerPanelDAO.hasCooldown(player.getObjectId(), "enchant")) {
            player.sendMessage("Подождите перед следующей заточкой!");
            showEnchantPanel(player);
            return;
        }
        
        // Выполнение заточки
        performEnchant(player, item, scroll, cost);
    }
    
    /**
     * Найти подходящий свиток заточки
     */
    private L2ItemInstance findEnchantScroll(L2PcInstance player, L2ItemInstance item) {
        // Ищем свитки заточки в инвентаре
        for (L2ItemInstance scroll : player.getInventory().getItems()) {
            if (isEnchantScroll(scroll, item)) {
                return scroll;
            }
        }
        return null;
    }
    
    /**
     * Проверить, является ли предмет свитком заточки
     */
    private boolean isEnchantScroll(L2ItemInstance scroll, L2ItemInstance targetItem) {
        String scrollName = scroll.getName().toLowerCase();
        
        // Проверяем на наличие ключевых слов в названии
        if (!scrollName.contains("enchant") && !scrollName.contains("scroll")) {
            return false;
        }
        
        // Проверяем тип свитка (оружие/броня)
        if (targetItem.isWeapon()) {
            return scrollName.contains("weapon") || scrollName.contains("w_");
        } else if (targetItem.isArmor()) {
            return scrollName.contains("armor") || scrollName.contains("a_");
        }
        
        return true;
    }
    
    /**
     * Рассчитать стоимость заточки
     */
    private long calculateEnchantCost(L2ItemInstance item) {
        long baseCost = _config.getEnchantBaseCost();
        int enchantLevel = item.getEnchantLevel();
        
        // Увеличение стоимости с уровнем заточки
        double multiplier = 1.0 + (enchantLevel * 0.5);
        
        // Дополнительная стоимость для высоких уровней
        if (enchantLevel >= 10) {
            multiplier *= 2.0;
        }
        if (enchantLevel >= 15) {
            multiplier *= 3.0;
        }
        
        return (long)(baseCost * multiplier);
    }
    
    /**
     * Выполнить заточку
     */
    private void performEnchant(L2PcInstance player, L2ItemInstance item, L2ItemInstance scroll, long cost) {
        int currentEnchant = item.getEnchantLevel();
        int successRate = getEnchantSuccessRate(currentEnchant);
        
        // Сохранение данных для логирования
        String itemName = item.getName();
        int itemId = item.getId();
        int scrollId = scroll.getId();
        String scrollName = scroll.getName();
        
        // Симуляция заточки
        boolean success = Rnd.get(100) < successRate;
        
        // Снятие стоимости и свитка
        player.reduceAdena("Enchant", cost, null, true);
        player.destroyItem("Enchant", scroll, 1, null, false);
        
        if (success) {
            // Успешная заточка
            item.setEnchantLevel(currentEnchant + 1);
            item.updateDatabase();
            
            player.sendMessage("🎉 Заточка успешна! " + itemName + " теперь +" + item.getEnchantLevel());
            
            // Визуальные эффекты
            if (_config.isVisualEffectsEnabled()) {
                player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 1, 0));
            }
            
            // Логирование
            PlayerPanelDAO.logEnchant(player, itemId, item.getObjectId(), itemName, 
                                    currentEnchant, item.getEnchantLevel(), true, 
                                    scrollId, scrollName, cost);
        } else {
            // Неудачная заточка
            int newEnchant = currentEnchant;
            if (_config.canEnchantBreak() && currentEnchant > _config.getEnchantSafeLevel()) {
                // Понижение уровня заточки
                int decrease = calculateEnchantDecrease(currentEnchant);
                newEnchant = Math.max(0, currentEnchant - decrease);
                item.setEnchantLevel(newEnchant);
                item.updateDatabase();
                
                if (newEnchant < currentEnchant) {
                    player.sendMessage("💥 Заточка не удалась! " + itemName + " понижен до +" + newEnchant);
                } else {
                    player.sendMessage("😔 Заточка не удалась, но предмет не пострадал.");
                }
            } else {
                player.sendMessage("😔 Заточка не удалась, но предмет защищен от повреждений.");
            }
            
            // Логирование
            PlayerPanelDAO.logEnchant(player, itemId, item.getObjectId(), itemName, 
                                    currentEnchant, newEnchant, false, 
                                    scrollId, scrollName, cost);
        }
        
        // Обновление инвентаря
        InventoryUpdate iu = new InventoryUpdate();
        iu.addModifiedItem(item);
        player.sendPacket(iu);
        player.sendPacket(new UserInfo(player));
        
        // Установка кулдауна
        if (_config.getPanelCooldown() > 0) {
            PlayerPanelDAO.setCooldown(player.getObjectId(), "enchant", _config.getPanelCooldown() * 60000);
        }
        
        showEnchantPanel(player);
    }
    
    /**
     * Рассчитать понижение уровня заточки при неудаче
     */
    private int calculateEnchantDecrease(int currentEnchant) {
        if (currentEnchant >= 15) {
            return Rnd.get(2, 4); // Сильное понижение на высоких уровнях
        } else if (currentEnchant >= 10) {
            return Rnd.get(1, 3); // Среднее понижение
        } else {
            return Rnd.get(1, 2); // Минимальное понижение
        }
    }
    
    /**
     * Получить шанс успеха заточки
     */
    private int getEnchantSuccessRate(int currentEnchant) {
        if (currentEnchant < 4) {
            return _config.getEnchantSuccessRate1();
        } else if (currentEnchant < 10) {
            return _config.getEnchantSuccessRate5();
        } else if (currentEnchant < 15) {
            return _config.getEnchantSuccessRate10();
        } else {
            return _config.getEnchantSuccessRate15();
        }
    }
    
    /**
     * Показать панель заточки
     */
    private void showEnchantPanel(L2PcInstance player) {
        String html = _htmlGenerator.generateMainPanel(player, "enchant");
        CommunityBoardHandler.separateAndSend(html, player);
    }
    
    /**
     * Форматировать число
     */
    private String formatNumber(long number) {
        if (number >= 1000000000) {
            return String.format("%.1fB", number / 1000000000.0);
        } else if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000.0);
        } else {
            return String.valueOf(number);
        }
    }
}