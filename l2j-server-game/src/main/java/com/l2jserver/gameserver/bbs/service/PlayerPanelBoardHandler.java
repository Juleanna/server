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
package com.l2jserver.gameserver.bbs.service;

import static com.l2jserver.gameserver.config.Configuration.PlayerPanel;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.bbs.service.modules.PlayerPanelBuffModule;
import com.l2jserver.gameserver.bbs.service.modules.PlayerPanelEnchantModule;
import com.l2jserver.gameserver.bbs.service.modules.PlayerPanelHtmlGenerator;
import com.l2jserver.gameserver.bbs.service.modules.PlayerPanelShopModule;
import com.l2jserver.gameserver.bbs.service.modules.PlayerPanelValidator;
import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.handler.IParseBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Основной обработчик панели игрока для L2J Server
 * @author YourName
 */
public class PlayerPanelBoardHandler implements IParseBoardHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayerPanelBoardHandler.class);
    
    // Команды панели
    private static final String[] COMMANDS = {
        "_bbsplayerpanel",
        "_bbsenchant",
        "_bbsshop", 
        "_bbsbuffs",
        "_bbsinventory",
        "_bbsskills",
        "_bbsclan",
        "_bbspvp",
        "_bbsstats",
        "_bbssettings"
    };
    
    // Кэш для предотвращения спама
    private final Map<Integer, Long> _lastUsed = new ConcurrentHashMap<>();
    
    // Конфигурация и модули
    private final PlayerPanelConfig _config;
    private final PlayerPanelValidator _validator;
    private final PlayerPanelHtmlGenerator _htmlGenerator;
    private final PlayerPanelEnchantModule _enchantModule;
    private final PlayerPanelShopModule _shopModule;
    private final PlayerPanelBuffModule _buffModule;
    
    public PlayerPanelBoardHandler() {
        _config = PlayerPanel();
        _validator = new PlayerPanelValidator(_config);
        _htmlGenerator = new PlayerPanelHtmlGenerator(_config);
        _enchantModule = new PlayerPanelEnchantModule(_config);
        _shopModule = new PlayerPanelShopModule(_config);
        _buffModule = new PlayerPanelBuffModule(_config);
    }
    
    @Override
    public String[] getCommunityBoardCommands() {
        return COMMANDS;
    }
    
    @Override
    public boolean parseCommunityBoardCommand(String command, L2PcInstance player) {
        if (player == null) {
            return false;
        }
        
        // Проверка базовых условий
        if (!_validator.canUsePanel(player)) {
            return false;
        }
        
        // Антиспам проверка
        if (!checkAntiSpam(player)) {
            player.sendMessage("Пожалуйста, подождите перед следующим использованием панели.");
            return false;
        }
        
        StringTokenizer st = new StringTokenizer(command, ";");
        String cmd = st.nextToken();
        
        try {
            switch (cmd) {
                case "_bbsplayerpanel":
                    showMainPanel(player, st.hasMoreTokens() ? st.nextToken() : "enchant");
                    break;
                case "_bbsenchant":
                    _enchantModule.handleEnchant(player, st);
                    break;
                case "_bbsshop":
                    _shopModule.handleShop(player, st);
                    break;
                case "_bbsbuffs":
                    _buffModule.handleBuffs(player, st);
                    break;
                case "_bbsinventory":
                    showMainPanel(player, "inventory");
                    break;
                case "_bbsskills":
                    showMainPanel(player, "skills");
                    break;
                case "_bbsclan":
                    showMainPanel(player, "clan");
                    break;
                case "_bbspvp":
                    showMainPanel(player, "pvp");
                    break;
                case "_bbsstats":
                    showMainPanel(player, "stats");
                    break;
                case "_bbssettings":
                    showMainPanel(player, "settings");
                    break;
                default:
                    return false;
            }
            
            // Логирование использования
            PlayerPanelDAO.logAction(player, cmd, command, 0, true);
            
        } catch (Exception e) {
            LOG.error("Error processing panel command: {} for player: {}", command, player.getName(), e);
            player.sendMessage("Произошла ошибка при обработке команды.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Проверка антиспама
     */
    private boolean checkAntiSpam(L2PcInstance player) {
        if (!_config.isAntiSpamEnabled()) {
            return true;
        }
        
        long currentTime = System.currentTimeMillis();
        Long lastUsed = _lastUsed.get(player.getObjectId());
        
        if (lastUsed != null) {
            long timeDiff = currentTime - lastUsed;
            if (timeDiff < _config.getAntiSpamInterval() * 1000) {
                return false;
            }
        }
        
        _lastUsed.put(player.getObjectId(), currentTime);
        return true;
    }
    
    /**
     * Показать главную панель
     */
    private void showMainPanel(L2PcInstance player, String section) {
        String html = _htmlGenerator.generateMainPanel(player, section);
        CommunityBoardHandler.separateAndSend(html, player);
    }
}