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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO.PlayerPanelStats;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Модуль генерации HTML для панели игрока
 * @author YourName
 */
public class PlayerPanelHtmlGenerator {
    
    private final PlayerPanelConfig _config;
    
    public PlayerPanelHtmlGenerator(PlayerPanelConfig config) {
        _config = config;
    }
    
    /**
     * Сгенерировать главную панель
     */
    public String generateMainPanel(L2PcInstance player, String section) {
        StringBuilder html = new StringBuilder();
        
        // HTML заголовок и стили
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Панель игрока</title>");
        html.append(getCSS());
        html.append("</head><body>");
        
        // Заголовок
        html.append("<div class=\"container\">");
        html.append("<div class=\"header\">");
        html.append("<h1>🗡️ Панель игрока L2J</h1>");
        html.append("<p>Добро пожаловать, ").append(player.getName()).append("!</p>");
        html.append("</div>");
        
        // Информация о персонаже
        html.append(getPlayerInfo(player));
        
        // Основной контент
        html.append("<div class=\"main-content\">");
        
        // Боковая панель
        html.append(getSidebar(section));
        
        // Область контента
        html.append("<div class=\"content-area\">");
        html.append(getCurrencyDisplay(player));
        
        switch (section) {
            case "enchant":
                html.append(getEnchantSection(player));
                break;
            case "shop":
                html.append(getShopSection(player));
                break;
            case "buffs":
                html.append(getBuffsSection(player));
                break;
            case "inventory":
                html.append(getInventorySection(player));
                break;
            case "skills":
                html.append(getSkillsSection(player));
                break;
            case "clan":
                html.append(getClanSection(player));
                break;
            case "pvp":
                html.append(getPvPSection(player));
                break;
            case "stats":
                html.append(getStatsSection(player));
                break;
            case "settings":
                html.append(getSettingsSection(player));
                break;
        }
        
        html.append("</div></div></div>");
        html.append(getJavaScript());
        html.append("</body></html>");
        
        return html.toString();
    }
    
    /**
     * Получить CSS стили
     */
    private String getCSS() {
        return """
            <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body { 
                font-family: Arial, sans-serif; 
                background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 50%, #16213e 100%);
                color: #ffffff; 
                min-height: 100vh;
            }
            .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
            .header { 
                text-align: center; 
                margin-bottom: 30px; 
                padding: 20px;
                background: rgba(255, 255, 255, 0.05);
                border-radius: 15px;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .header h1 { 
                font-size: 2.5rem; 
                background: linear-gradient(45deg, #ffd700, #ff6b35);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                margin-bottom: 10px;
                animation: glow 2s ease-in-out infinite alternate;
            }
            @keyframes glow {
                from { text-shadow: 0 0 10px rgba(255, 215, 0, 0.5); }
                to { text-shadow: 0 0 20px rgba(255, 215, 0, 0.8); }
            }
            .player-info { 
                display: grid; 
                grid-template-columns: 1fr 2fr 1fr; 
                gap: 20px; 
                margin-bottom: 30px;
            }
            .character-avatar { 
                background: linear-gradient(145deg, #2a2a4a, #1a1a2e);
                border-radius: 15px; 
                padding: 20px; 
                text-align: center;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .avatar-img { 
                width: 120px; 
                height: 120px; 
                border-radius: 50%;
                background: linear-gradient(45deg, #667eea, #764ba2);
                margin: 0 auto 15px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 3rem;
                border: 3px solid #ffd700;
                box-shadow: 0 0 20px rgba(255, 215, 0, 0.5);
            }
            .stats-grid { 
                display: grid; 
                grid-template-columns: repeat(2, 1fr); 
                gap: 15px;
            }
            .stat-bar { 
                background: rgba(255, 255, 255, 0.05);
                border-radius: 10px; 
                padding: 15px;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .stat-bar h4 { 
                margin-bottom: 10px; 
                color: #ffd700;
            }
            .progress-bar { 
                width: 100%; 
                height: 25px;
                background: rgba(0, 0, 0, 0.3);
                border-radius: 12px; 
                overflow: hidden;
                position: relative;
            }
            .progress-fill { 
                height: 100%; 
                border-radius: 12px;
                position: relative; 
                transition: width 0.5s ease;
            }
            .hp-bar { background: linear-gradient(90deg, #ff4757, #ff6b81); }
            .mp-bar { background: linear-gradient(90deg, #3742fa, #5352ed); }
            .exp-bar { background: linear-gradient(90deg, #ffa502, #ff6348); }
            .cp-bar { background: linear-gradient(90deg, #2ed573, #1e90ff); }
            .progress-text { 
                position: absolute; 
                top: 50%; 
                left: 50%;
                transform: translate(-50%, -50%); 
                font-weight: bold;
                text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8); 
                font-size: 0.9rem;
            }
            .character-details { 
                background: linear-gradient(145deg, #2a2a4a, #1a1a2e);
                border-radius: 15px; 
                padding: 20px;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .detail-row { 
                display: flex; 
                justify-content: space-between; 
                padding: 10px 0;
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            }
            .detail-row:last-child { border-bottom: none; }
            .detail-label { color: #b0b0b0; }
            .detail-value { color: #ffd700; font-weight: bold; }
            .main-content { 
                display: grid; 
                grid-template-columns: 250px 1fr; 
                gap: 30px;
            }
            .sidebar { 
                background: linear-gradient(145deg, #2a2a4a, #1a1a2e);
                border-radius: 15px; 
                padding: 20px; 
                height: fit-content;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .nav-button { 
                width: 100%; 
                padding: 15px; 
                margin-bottom: 10px;
                background: rgba(255, 255, 255, 0.05);
                border: 1px solid rgba(255, 255, 255, 0.1);
                border-radius: 10px; 
                color: #ffffff; 
                cursor: pointer;
                transition: all 0.3s ease;
                font-size: 1rem;
                text-decoration: none;
                display: block;
                text-align: center;
            }
            .nav-button:hover { 
                background: rgba(255, 215, 0, 0.2);
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(255, 215, 0, 0.3);
            }
            .nav-button.active { 
                background: linear-gradient(45deg, #ffd700, #ff6b35);
                color: #000000; 
                font-weight: bold;
            }
            .content-area { 
                background: linear-gradient(145deg, #2a2a4a, #1a1a2e);
                border-radius: 15px; 
                padding: 30px;
                border: 1px solid rgba(255, 255, 255, 0.1);
                min-height: 600px;
            }
            .section-title { 
                font-size: 2rem; 
                margin-bottom: 20px; 
                color: #ffd700;
                border-bottom: 2px solid #ffd700;
                padding-bottom: 10px;
            }
            .currency-display { 
                display: flex; 
                justify-content: space-around;
                background: rgba(255, 255, 255, 0.05);
                border-radius: 10px; 
                padding: 15px; 
                margin-bottom: 20px;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .currency-item { text-align: center; }
            .currency-icon { font-size: 2rem; margin-bottom: 5px; display: block; }
            .currency-amount { font-weight: bold; color: #ffd700; }
            .enchant-button { 
                padding: 15px 30px;
                background: linear-gradient(45deg, #667eea, #764ba2);
                border: none; 
                border-radius: 10px; 
                color: white;
                font-size: 1.1rem; 
                cursor: pointer;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-block;
            }
            .enchant-button:hover { 
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
            }
            .shop-grid { 
                display: grid; 
                grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                gap: 20px;
            }
            .shop-item { 
                background: rgba(255, 255, 255, 0.05);
                border-radius: 10px; 
                padding: 20px;
                border: 1px solid rgba(255, 255, 255, 0.1);
                transition: all 0.3s ease;
                text-align: center;
            }
            .shop-item:hover { 
                transform: translateY(-5px);
                box-shadow: 0 10px 25px rgba(255, 215, 0, 0.2);
                border-color: #ffd700;
            }
            .item-image { 
                width: 80px; 
                height: 80px;
                background: linear-gradient(45deg, #667eea, #764ba2);
                border-radius: 10px; 
                margin: 0 auto 15px;
                display: flex; 
                align-items: center; 
                justify-content: center;
                font-size: 2rem;
            }
            .item-name { 
                font-size: 1.2rem; 
                margin-bottom: 10px; 
                color: #ffd700;
            }
            .item-price { 
                font-size: 1.5rem; 
                color: #00ff00; 
                font-weight: bold;
            }
            .buffs-grid { 
                display: grid; 
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
                gap: 15px;
            }
            .buff-card { 
                background: rgba(255, 255, 255, 0.05);
                border-radius: 10px; 
                padding: 15px;
                border: 1px solid rgba(255, 255, 255, 0.1);
                text-align: center; 
                transition: all 0.3s ease;
                cursor: pointer;
            }
            .buff-card:hover { 
                border-color: #ffd700;
                transform: scale(1.05);
            }
            .buff-card.active { 
                border-color: #00ff00;
                background: rgba(0, 255, 0, 0.1);
            }
            .buff-icon { 
                font-size: 3rem; 
                margin-bottom: 10px; 
                display: block;
            }
            .buff-name { 
                font-size: 1.1rem; 
                margin-bottom: 5px; 
                color: #ffd700;
            }
            .buff-duration { 
                color: #b0b0b0; 
                font-size: 0.9rem;
            }
            .equipment-grid { 
                display: grid; 
                grid-template-columns: repeat(3, 1fr); 
                gap: 15px;
            }
            .equipment-slot { 
                aspect-ratio: 1;
                background: rgba(255, 255, 255, 0.05);
                border: 2px solid rgba(255, 255, 255, 0.2);
                border-radius: 10px; 
                display: flex; 
                flex-direction: column;
                align-items: center; 
                justify-content: center; 
                cursor: pointer;
                transition: all 0.3s ease; 
                position: relative;
            }
            .equipment-slot:hover { 
                border-color: #ffd700;
                background: rgba(255, 215, 0, 0.1); 
                transform: scale(1.05);
            }
            .equipment-slot.selected { 
                border-color: #ffd700;
                box-shadow: 0 0 20px rgba(255, 215, 0, 0.5);
            }
            .equipment-icon { font-size: 2rem; margin-bottom: 5px; }
            .equipment-name { font-size: 0.8rem; text-align: center; }
            .enchant-level { 
                position: absolute; 
                top: 5px; 
                right: 5px;
                background: #ffd700; 
                color: #000; 
                border-radius: 50%;
                width: 20px; 
                height: 20px; 
                display: flex;
                align-items: center; 
                justify-content: center; 
                font-size: 0.7rem;
                font-weight: bold;
            }
            .enchant-section { 
                display: grid; 
                grid-template-columns: 1fr 1fr; 
                gap: 30px;
            }
            .enchant-panel { 
                background: rgba(255, 255, 255, 0.03);
                border-radius: 10px; 
                padding: 20px;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            @media (max-width: 768px) {
                .player-info { grid-template-columns: 1fr; }
                .main-content { grid-template-columns: 1fr; }
                .enchant-section { grid-template-columns: 1fr; }
                .shop-grid { grid-template-columns: 1fr; }
            }
            </style>
            """;
    }
    
    /**
     * Получить информацию о персонаже
     */
    private String getPlayerInfo(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"player-info\">");
        
        // Аватар
        html.append("<div class=\"character-avatar\">");
        html.append("<div class=\"avatar-img\">⚔️</div>");
        html.append("<h3>").append(player.getName()).append("</h3>");
        html.append("<p>").append(player.getTemplate().getClassName()).append("</p>");
        html.append("<p>Уровень: ").append(player.getLevel()).append("</p>");
        html.append("</div>");
        
        // Статы с прогресс-барами
        html.append("<div class=\"stats-grid\">");
        
        // HP
        html.append("<div class=\"stat-bar\">");
        html.append("<h4>❤️ HP</h4>");
        html.append("<div class=\"progress-bar\">");
        double hpPercent = (player.getCurrentHp() / player.getMaxHp()) * 100;
        html.append("<div class=\"progress-fill hp-bar\" style=\"width: ").append(hpPercent).append("%\"></div>");
        html.append("<div class=\"progress-text\">").append((int)player.getCurrentHp()).append(" / ").append((int)player.getMaxHp()).append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        // MP
        html.append("<div class=\"stat-bar\">");
        html.append("<h4>💧 MP</h4>");
        html.append("<div class=\"progress-bar\">");
        double mpPercent = (player.getCurrentMp() / player.getMaxMp()) * 100;
        html.append("<div class=\"progress-fill mp-bar\" style=\"width: ").append(mpPercent).append("%\"></div>");
        html.append("<div class=\"progress-text\">").append((int)player.getCurrentMp()).append(" / ").append((int)player.getMaxMp()).append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        // EXP
        html.append("<div class=\"stat-bar\">");
        html.append("<h4>⭐ EXP</h4>");
        html.append("<div class=\"progress-bar\">");
        double expPercent = player.getExpForLevel(player.getLevel() + 1) > 0 ? 
            ((double)(player.getExp() - player.getExpForLevel(player.getLevel())) / 
             (player.getExpForLevel(player.getLevel() + 1) - player.getExpForLevel(player.getLevel()))) * 100 : 100;
        html.append("<div class=\"progress-fill exp-bar\" style=\"width: ").append(expPercent).append("%\"></div>");
        html.append("<div class=\"progress-text\">").append(String.format("%.1f", expPercent)).append("%</div>");
        html.append("</div>");
        html.append("</div>");
        
        // CP
        html.append("<div class=\"stat-bar\">");
        html.append("<h4>🛡️ CP</h4>");
        html.append("<div class=\"progress-bar\">");
        double cpPercent = (player.getCurrentCp() / player.getMaxCp()) * 100;
        html.append("<div class=\"progress-fill cp-bar\" style=\"width: ").append(cpPercent).append("%\"></div>");
        html.append("<div class=\"progress-text\">").append((int)player.getCurrentCp()).append(" / ").append((int)player.getMaxCp()).append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("</div>");
        
        // Характеристики
        html.append("<div class=\"character-details\">");
        html.append("<h3 style=\"color: #ffd700; margin-bottom: 15px;\">⚔️ Характеристики</h3>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">P.Atk:</span>");
        html.append("<span class=\"detail-value\">").append(player.getPAtk()).append("</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">M.Atk:</span>");
        html.append("<span class=\"detail-value\">").append(player.getMAtk()).append("</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">P.Def:</span>");
        html.append("<span class=\"detail-value\">").append(player.getPDef()).append("</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">M.Def:</span>");
        html.append("<span class=\"detail-value\">").append(player.getMDef()).append("</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">Скорость:</span>");
        html.append("<span class=\"detail-value\">").append(player.getRunSpeed()).append("</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">Точность:</span>");
        html.append("<span class=\"detail-value\">").append(player.getAccuracy()).append("</span>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("</div>");
        
        return html.toString();
    }
    
    /**
     * Получить боковую панель навигации
     */
    private String getSidebar(String activeSection) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"sidebar\">");
        html.append("<h3 style=\"color: #ffd700; margin-bottom: 20px;\">🎮 Меню</h3>");
        
        String[][] navItems = {
            {"enchant", "⚡ Заточка"},
            {"shop", "🛒 Магазин"},
            {"buffs", "✨ Бафы"},
            {"inventory", "🎒 Инвентарь"},
            {"skills", "📖 Умения"},
            {"clan", "👥 Клан"},
            {"pvp", "⚔️ PvP"},
            {"stats", "📊 Статистика"},
            {"settings", "⚙️ Настройки"}
        };
        
        for (String[] item : navItems) {
            String cssClass = item[0].equals(activeSection) ? "nav-button active" : "nav-button";
            html.append("<a href=\"bypass _bbsplayerpanel;").append(item[0]).append("\" class=\"").append(cssClass).append("\">");
            html.append(item[1]);
            html.append("</a>");
        }
        
        html.append("</div>");
        
        return html.toString();
    }
    
    /**
     * Получить отображение валюты
     */
    private String getCurrencyDisplay(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"currency-display\">");
        
        html.append("<div class=\"currency-item\">");
        html.append("<span class=\"currency-icon\">💰</span>");
        html.append("<div>Adena</div>");
        html.append("<div class=\"currency-amount\">").append(formatNumber(player.getAdena())).append("</div>");
        html.append("</div>");
        
        html.append("<div class=\"currency-item\">");
        html.append("<span class=\"currency-icon\">💎</span>");
        html.append("<div>L2-Coin</div>");
        html.append("<div class=\"currency-amount\">").append(formatNumber(player.getFame())).append("</div>");
        html.append("</div>");
        
        html.append("<div class=\"currency-item\">");
        html.append("<span class=\"currency-icon\">🏆</span>");
        html.append("<div>Honor</div>");
        html.append("<div class=\"currency-amount\">").append(formatNumber(player.getFame())).append("</div>");
        html.append("</div>");
        
        html.append("<div class=\"currency-item\">");
        html.append("<span class=\"currency-icon\">⭐</span>");
        html.append("<div>Fame</div>");
        html.append("<div class=\"currency-amount\">").append(formatNumber(player.getFame())).append("</div>");
        html.append("</div>");
        
        html.append("</div>");
        
        return html.toString();
    }
    
    /**
     * Получить секцию заточки
     */
    public String getEnchantSection(L2PcInstance player) {
        if (!_config.isEnchantEnabled()) {
            return "<div class=\"content-section\"><h2 class=\"section-title\">⚡ Система заточки</h2><p>Система заточки отключена.</p></div>";
        }
        
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"content-section\">");
        html.append("<h2 class=\"section-title\">⚡ Система заточки</h2>");
        html.append("<div class=\"enchant-section\">");
        
        // Левая панель - экипировка
        html.append("<div>");
        html.append("<h3 style=\"color: #ffd700; margin-bottom: 20px;\">Экипированные предметы</h3>");
        html.append("<div class=\"equipment-grid\">");
        
        // Оружие
        L2ItemInstance weapon = player.getActiveWeaponInstance();
        if (weapon != null && weapon.isEnchantable()) {
            html.append("<div class=\"equipment-slot\" onclick=\"location.href='bypass _bbsenchant;item;").append(weapon.getObjectId()).append("'\">");
            html.append("<div class=\"equipment-icon\">⚔️</div>");
            html.append("<div class=\"equipment-name\">").append(weapon.getName()).append("</div>");
            if (weapon.getEnchantLevel() > 0) {
                html.append("<div class=\"enchant-level\">+").append(weapon.getEnchantLevel()).append("</div>");
            }
            html.append("</div>");
        } else {
            html.append("<div class=\"equipment-slot\">");
            html.append("<div class=\"equipment-icon\">⚔️</div>");
            html.append("<div class=\"equipment-name\">Оружие</div>");
            html.append("</div>");
        }
        
        // Броня и аксессуары
        String[] slotNames = {"Шлем", "Броня", "Ботинки", "Перчатки", "Кольцо", "Серьга"};
        String[] slotIcons = {"⛑️", "🛡️", "👢", "🧤", "💍", "💎"};
        int[] slots = {L2Item.SLOT_HEAD, L2Item.SLOT_CHEST, L2Item.SLOT_FEET, L2Item.SLOT_GLOVES, L2Item.SLOT_R_FINGER, L2Item.SLOT_R_EAR};
        
        for (int i = 0; i < slots.length; i++) {
            L2ItemInstance item = player.getInventory().getPaperdollItem(slots[i]);
            html.append("<div class=\"equipment-slot\"");
            if (item != null && item.isEnchantable()) {
                html.append(" onclick=\"location.href='bypass _bbsenchant;item;").append(item.getObjectId()).append("'\"");
            }
            html.append(">");
            html.append("<div class=\"equipment-icon\">").append(slotIcons[i]).append("</div>");
            html.append("<div class=\"equipment-name\">");
            if (item != null) {
                html.append(item.getName());
            } else {
                html.append(slotNames[i]);
            }
            html.append("</div>");
            if (item != null && item.getEnchantLevel() > 0) {
                html.append("<div class=\"enchant-level\">+").append(item.getEnchantLevel()).append("</div>");
            }
            html.append("</div>");
        }
        
        html.append("</div>");
        html.append("</div>");
        
        // Правая панель - информация
        html.append("<div class=\"enchant-panel\">");
        html.append("<h3 style=\"color: #ffd700; margin-bottom: 20px;\">Информация о заточке</h3>");
        html.append("<div style=\"margin-bottom: 20px;\">");
        html.append("<h4>Шансы успеха:</h4>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">+1 до +3:</span>");
        html.append("<span class=\"detail-value\">").append(_config.getEnchantSuccessRate1()).append("%</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">+4 до +9:</span>");
        html.append("<span class=\"detail-value\">").append(_config.getEnchantSuccessRate5()).append("%</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">+10 до +14:</span>");
        html.append("<span class=\"detail-value\">").append(_config.getEnchantSuccessRate10()).append("%</span>");
        html.append("</div>");
        html.append("<div class=\"detail-row\">");
        html.append("<span class=\"detail-label\">+15+:</span>");
        html.append("<span class=\"detail-value\">").append(_config.getEnchantSuccessRate15()).append("%</span>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("<div style=\"margin-bottom: 20px;\">");
        html.append("<h4>Правила заточки:</h4>");
        html.append("<p>• Максимальный уровень: +").append(_config.getMaxEnchantLevel()).append("</p>");
        html.append("<p>• Безопасный уровень: +").append(_config.getEnchantSafeLevel()).append("</p>");
        if (_config.canEnchantBreak()) {
            html.append("<p>• При неудаче возможно понижение уровня</p>");
        } else {
            html.append("<p>• Предметы не ломаются при неудаче</p>");
        }
        html.append("</div>");
        
        html.append("<p style=\"color: #b0b0b0;\">Нажмите на предмет выше, чтобы начать заточку.</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</div>");
        
        return html.toString();
    }
    
    /**
     * Получить секцию магазина
     */
    public String getShopSection(L2PcInstance player) {
        if (!_config.isShopEnabled()) {
            return "<div class=\"content-section\"><h2 class=\"section-title\">🛒 Магазин</h2><p>Магазин отключен.</p></div>";
        }
        
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"content-section\">");
        html.append("<h2 class=\"section-title\">🛒 Магазин</h2>");
        html.append("<div class=\"shop-grid\">");
        
        // Получаем список доступных предметов из конфигурации
        String[] availableItems = _config.getShopAvailableItems().split(",");
        
        // Предопределенные предметы магазина
        String[][] shopItems = {
            {"1", "⚔️", "Demon Sword", "150000", "Мощный меч демонов"},
            {"2", "🛡️", "Mithril Armor", "200000", "Легкая, но прочная броня"},
            {"3", "📜", "Enchant Scroll", "5000", "Свиток для заточки оружия"},
            {"4", "🧪", "Healing Potion", "500", "Восстанавливает здоровье"},
            {"5", "💍", "Power Ring", "75000", "Кольцо увеличивающее силу"},
            {"6", "💎", "Soul Crystal", "25000", "Кристалл души для улучшений"},
            {"7", "🏹", "Elven Bow", "120000", "Эльфийский лук высокого качества"},
            {"8", "🗡️", "Dagger Set", "80000", "Набор кинжалов для ассасина"},
            {"9", "⚡", "Magic Staff", "180000", "Посох для заклинателей"},
            {"10", "🎭", "Blessed Scroll", "15000", "Благословенный свиток заточки"}
        };
        
        for (String[] item : shopItems) {
            String itemId = item[0];
            
            // Проверяем, доступен ли предмет
            if (!Arrays.asList(availableItems).contains(itemId)) {
                continue;
            }
            
            long basePrice = Long.parseLong(item[3]);
            long actualPrice = (long)(basePrice * _config.getShopPriceMultiplier());
            
            html.append("<div class=\"shop-item\">");
            html.append("<div class=\"item-image\">").append(item[1]).append("</div>");
            html.append("<div class=\"item-name\">").append(item[2]).append("</div>");
            html.append("<div style=\"color: #b0b0b0; margin: 10px 0; font-size: 0.9rem;\">").append(item[4]).append("</div>");
            html.append("<div class=\"item-price\">").append(formatNumber(actualPrice)).append(" Adena</div>");
            
            // Проверяем, может ли игрок купить
            if (player.getAdena() >= actualPrice) {
                html.append("<a href=\"bypass _bbsshop;buy;").append(itemId).append(";").append(actualPrice).append("\" class=\"enchant-button\">Купить</a>");
            } else {
                html.append("<div class=\"enchant-button\" style=\"background: #666; cursor: not-allowed;\">Недостаточно Adena</div>");
            }
            html.append("</div>");
        }
        
        html.append("</div>");
        html.append("</div>");
        
        return html.toString();
    }
    
    /**
     * Получить секцию бафов
     */
    public String getBuffsSection(L2PcInstance player) {
        if (!_config.isBuffsEnabled()) {
            return "<div class=\"content-section\"><h2 class=\"section-title\">✨ Система бафов</h2><p>Система бафов отключена.</p></div>";
        }
        
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"content-section\">");
        html.append("<h2 class=\"section-title\">✨ Система бафов</h2>");
        html.append("<div class=\"buffs-grid\">");
        
        // Получаем список доступных бафов
        String[] availableBuffs = _config.getAvailableBuffs().split(",");
        
        // Предопределенные бафы
        String[][] buffs = {
            {"1040", "💪", "Might", "Увеличивает физическую атаку"},
            {"1045", "🛡️", "Shield", "Повышает физическую защиту"},
            {"1048", "✨", "Blessed Body", "Увеличивает максимальное HP"},
            {"1059", "⚡", "Empower", "Повышает магическую атаку"},
            {"1068", "💥", "Might", "Дает дополнительную силу"},
            {"1077", "🎯", "Focus", "Увеличивает точность"},
            {"1085", "🔮", "Acumen", "Ускоряет каст заклинаний"},
            {"1086", "🌪️", "Haste", "Увеличивает скорость атаки"},
            {"1204", "🌬️", "Wind Walk", "Повышает скорость передвижения"},
            {"1240", "🌟", "Guidance", "Улучшает все характеристики"},
            {"1242", "💀", "Death Whisper", "Увеличивает критический урон"},
            {"1243", "🛡️", "Bless Shield", "Магическая защита"}
        };
        
        for (String[] buff : buffs) {
            String skillId = buff[0];
            
            // Проверяем, доступен ли баф
            if (!Arrays.asList(availableBuffs).contains(skillId)) {
                continue;
            }
            
            boolean isActive = player.getFirstEffect(Integer.parseInt(skillId)) != null;
            String cssClass = isActive ? "buff-card active" : "buff-card";
            
            html.append("<div class=\"").append(cssClass).append("\">");
            html.append("<div class=\"buff-icon\">").append(buff[1]).append("</div>");
            html.append("<div class=\"buff-name\">").append(buff[2]).append("</div>");
            html.append("<div style=\"color: #b0b0b0; margin: 5px 0; font-size: 0.8rem;\">").append(buff[3]).append("</div>");
            
            if (isActive) {
                html.append("<div class=\"buff-duration\" style=\"color: #00ff00;\">Активен</div>");
                html.append("<a href=\"bypass _bbsbuffs;remove;").append(skillId).append("\" class=\"enchant-button\" style=\"background: linear-gradient(45deg, #ff4757, #ff6b81);\">Убрать</a>");
            } else {
                html.append("<div class=\"buff-duration\">").append(formatNumber(_config.getBuffCost())).append(" Adena</div>");
                if (player.getAdena() >= _config.getBuffCost()) {
                    html.append("<a href=\"bypass _bbsbuffs;add;").append(skillId).append("\" class=\"enchant-button\">Получить</a>");
                } else {
                    html.append("<div class=\"enchant-button\" style=\"background: #666; cursor: not-allowed;\">Недостаточно Adena</div>");
                }
            }
            html.append("</div>");
        }
        
        html.append("</div>");
        
        html.append("<div style=\"margin-top: 30px; text-align: center;\">");
        if (player.getAdena() >= _config.getBuffRefreshCost()) {
            html.append("<a href=\"bypass _bbsbuffs;refresh\" class=\"enchant-button\" style=\"padding: 15px 30px;\">🔄 Обновить все бафы (").append(formatNumber(_config.getBuffRefreshCost())).append(" Adena)</a>");
        } else {
            html.append("<div class=\"enchant-button\" style=\"padding: 15px 30px; background: #666; cursor: not-allowed;\">🔄 Обновить все бафы (").append(formatNumber(_config.getBuffRefreshCost())).append(" Adena)</div>");
        }
        html.append("</div>");
        
        html.append("</div>");
        
        return html.toString();
    }
    
    /**
     * Остальные методы генерации секций...
     */
    private String getInventorySection(L2PcInstance player) {
        return generateBasicSection("🎒 Инвентарь", "Здесь отображается ваш инвентарь");
    }
    
    private String getSkillsSection(L2PcInstance player) {
        return generateBasicSection("📖 Система умений", "Здесь отображаются ваши умения");
    }
    
    private String getClanSection(L2PcInstance player) {
        return generateBasicSection("👥 Информация о клане", "Здесь отображается информация о клане");
    }
    
    private String getPvPSection(L2PcInstance player) {
        return generateBasicSection("⚔️ PvP Статистика", "Здесь отображается PvP статистика");
    }
    
    private String getStatsSection(L2PcInstance player) {
        return generateBasicSection("📊 Детальная статистика", "Здесь отображается детальная статистика");
    }
    
    private String getSettingsSection(L2PcInstance player) {
        return generateBasicSection("⚙️ Настройки", "Здесь настройки панели");
    }
    
    private String generateBasicSection(String title, String content) {
        return "<div class=\"content-section\"><h2 class=\"section-title\">" + title + "</h2><p>" + content + "</p></div>";
    }
    
    /**
     * Получить JavaScript код
     */
    private String getJavaScript() {
        return """
            <script>
            document.addEventListener('DOMContentLoaded', function() {
                console.log('Панель игрока загружена');
                
                // Анимация прогресс-баров
                const progressBars = document.querySelectorAll('.progress-fill');
                progressBars.forEach(bar => {
                    const width = bar.style.width;
                    bar.style.width = '0%';
                    setTimeout(() => {
                        bar.style.width = width;
                    }, 500);
                });
            });
            </script>
            """;
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