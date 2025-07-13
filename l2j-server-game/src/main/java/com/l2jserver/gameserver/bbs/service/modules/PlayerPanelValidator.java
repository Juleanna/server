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

import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Модуль валидации для панели игрока
 * @author YourName
 */
public class PlayerPanelValidator {
    
    private final PlayerPanelConfig _config;
    
    public PlayerPanelValidator(PlayerPanelConfig config) {
        _config = config;
    }
    
    /**
     * Проверка возможности использования панели
     */
    public boolean canUsePanel(L2PcInstance player) {
        // Проверка включения системы
        if (!_config.isPlayerPanelEnabled()) {
            player.sendMessage("Панель игрока отключена.");
            return false;
        }
        
        // Проверка уровня
        if (player.getLevel() < _config.getMinLevel()) {
            player.sendMessage("Минимальный уровень для использования панели: " + _config.getMinLevel());
            return false;
        }
        
        if (_config.getMaxLevel() > 0 && player.getLevel() > _config.getMaxLevel()) {
            player.sendMessage("Максимальный уровень для использования панели: " + _config.getMaxLevel());
            return false;
        }
        
        // Проверка зон
       /* if (!_config.isPvpZonesAllowed() && player.isInsideZone(L2Character.PVP)) {
            player.sendMessage("Панель игрока недоступна в PvP зоне.");
            return false;
        }
        
        if (!_config.isSiegeZonesAllowed() && player.isInsideZone(L2Character.ZONE_SIEGE)) {
            player.sendMessage("Панель игрока недоступна в осадной зоне.");
            return false;
        }*/
        
        if (!_config.isOlympiadAllowed() && player.isInOlympiadMode()) {
            player.sendMessage("Панель игрока недоступна в олимпиаде.");
            return false;
        }
        
        // Проверка стоимости входа
        if (_config.getEntryCost() > 0 && player.getAdena() < _config.getEntryCost()) {
            player.sendMessage("Недостаточно Adena для использования панели. Требуется: " + _config.getEntryCost());
            return false;
        }
        
        return true;
    }
    
    /**
     * Проверка возможности использования заточки
     */
    public boolean canUseEnchant(L2PcInstance player) {
        if (!_config.isEnchantEnabled()) {
            player.sendMessage("Система заточки отключена.");
            return false;
        }
        
        return canUsePanel(player);
    }
    
    /**
     * Проверка возможности использования магазина
     */
    public boolean canUseShop(L2PcInstance player) {
        if (!_config.isShopEnabled()) {
            player.sendMessage("Магазин отключен.");
            return false;
        }
        
        return canUsePanel(player);
    }
    
    /**
     * Проверка возможности использования бафов
     */
    public boolean canUseBuffs(L2PcInstance player) {
        if (!_config.isBuffsEnabled()) {
            player.sendMessage("Система бафов отключена.");
            return false;
        }
        
        // Проверка использования бафов в бою
        if (!_config.isBuffInCombatAllowed() && player.isInCombat()) {
            player.sendMessage("Нельзя использовать бафы в бою!");
            return false;
        }
        
        return canUsePanel(player);
    }
    
    /**
     * Проверка доступности предмета для заточки
     */
    public boolean canEnchantItem(L2PcInstance player, int itemId) {
        String forbiddenItems = _config.getForbiddenEnchantItems();
        if (!forbiddenItems.isEmpty()) {
            String[] forbidden = forbiddenItems.split(",");
            for (String id : forbidden) {
                if (String.valueOf(itemId).equals(id.trim())) {
                    player.sendMessage("Этот предмет нельзя затачивать через панель!");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Проверка доступности предмета в магазине
     */
    public boolean isItemAvailableInShop(String itemId) {
        String availableItems = _config.getShopAvailableItems();
        if (availableItems.isEmpty()) {
            return false;
        }
        
        String[] available = availableItems.split(",");
        for (String id : available) {
            if (itemId.equals(id.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Проверка доступности бафа
     */
    public boolean isBuffAvailable(int skillId) {
        String availableBuffs = _config.getAvailableBuffs();
        if (availableBuffs.isEmpty()) {
            return false;
        }
        
        String[] available = availableBuffs.split(",");
        for (String id : available) {
            try {
                if (skillId == Integer.parseInt(id.trim())) {
                    return true;
                }
            } catch (NumberFormatException e) {
                // Игнорируем некорректные ID
            }
        }
        
        return false;
    }
}