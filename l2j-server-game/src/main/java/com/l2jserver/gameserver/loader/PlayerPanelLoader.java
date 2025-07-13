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
package com.l2jserver.gameserver.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.bbs.service.PlayerPanelBoardHandler;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;


/**
 * Загрузчик панели игрока
 * @author YourName
 */
public class PlayerPanelLoader {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayerPanelLoader.class);
    
    /**
     * Инициализация панели игрока
     */
    public static void load() {
        try {
            // Загрузка конфигурации
            PlayerPanelConfig config = Configuration.PlayerPanel();
            
            if (!config.isPlayerPanelEnabled()) {
                LOG.info("Player Panel is disabled in configuration");
                return;
            }
            
            // Регистрация обработчика Community Board
            CommunityBoardHandler.getInstance().registerHandler(new PlayerPanelBoardHandler());
            
            // Очистка истекших кулдаунов при запуске
            PlayerPanelDAO.cleanExpiredCooldowns();
            
            LOG.info("Player Panel system loaded successfully");
            LOG.info("Available features:");
            LOG.info("  - Enchant System: {}", config.isEnchantEnabled() ? "Enabled" : "Disabled");
            LOG.info("  - Shop System: {}", config.isShopEnabled() ? "Enabled" : "Disabled");
            LOG.info("  - Buff System: {}", config.isBuffsEnabled() ? "Enabled" : "Disabled");
            LOG.info("  - Min Level: {}", config.getMinLevel());
            LOG.info("  - Max Level: {}", config.getMaxLevel() > 0 ? config.getMaxLevel() : "No limit");
            LOG.info("  - PvP Zones: {}", config.isPvpZonesAllowed() ? "Allowed" : "Restricted");
            LOG.info("  - Siege Zones: {}", config.isSiegeZonesAllowed() ? "Allowed" : "Restricted");
            LOG.info("  - Olympiad: {}", config.isOlympiadAllowed() ? "Allowed" : "Restricted");
            
        } catch (Exception e) {
            LOG.error("Failed to load Player Panel system", e);
        }
    }
}