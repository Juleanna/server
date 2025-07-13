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
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;

/**
 * Модуль системы бафов для панели игрока
 * @author YourName
 */
public class PlayerPanelBuffModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayerPanelBuffModule.class);
    
    private final PlayerPanelConfig _config;
    private final PlayerPanelValidator _validator;
    private final PlayerPanelHtmlGenerator _htmlGenerator;
    
    public PlayerPanelBuffModule(PlayerPanelConfig config) {
        _config = config;
        _validator = new PlayerPanelValidator(config);
        _htmlGenerator = new PlayerPanelHtmlGenerator(config);
    }
    
    /**
     * Обработка бафов
     */
    public void handleBuffs(L2PcInstance player, StringTokenizer st) {
        if (!_validator.canUseBuffs(player)) {
            showBuffsPanel(player);
            return;
        }
        
        if (!st.hasMoreTokens()) {
            showBuffsPanel(player);
            return;
        }
        
        String action = st.nextToken();
        
        switch (action) {
            case "add":
                if (st.hasMoreTokens()) {
                    int skillId = Integer.parseInt(st.nextToken());
                    addBuff(player, skillId);
                }
                break;
            case "remove":
                if (st.hasMoreTokens()) {
                    int skillId = Integer.parseInt(st.nextToken());
                    removeBuff(player, skillId);
                }
                break;
            case "refresh":
                refreshAllBuffs(player);
                break;
            case "scheme":
                if (st.hasMoreTokens()) {
                    String schemeName = st.nextToken();
                    applyBuffScheme(player, schemeName);
                }
                break;
            default:
                showBuffsPanel(player);
                return;
        }
        
        showBuffsPanel(player);
    }
    
    /**
     * Добавить баф
     */
    private void addBuff(L2PcInstance player, int skillId) {
        // Проверка доступности бафа
        if (!_validator.isBuffAvailable(skillId)) {
            player.sendMessage("Этот баф недоступен!");
            return;
        }
        
        long cost = calculateBuffCost(player, skillId);
        
        if (player.getAdena() < cost) {
            player.sendMessage("Недостаточно Adena для получения бафа!");
            return;
        }
        
        // Проверить, есть ли уже этот баф
        if (player.getFirstEffect(skillId) != null) {
            player.sendMessage("У вас уже есть этот баф!");
            return;
        }
        
        // Проверка кулдауна
        if (PlayerPanelDAO.hasCooldown(player.getObjectId(), "buff")) {
            player.sendMessage("Подождите перед получением следующего бафа!");
            return;
        }
        
        // Проверка максимального количества бафов
        if (getActiveBuffsCount(player) >= getMaxBuffsLimit(player)) {
            player.sendMessage("Достигнут лимит активных бафов!");
            return;
        }
        
        // Снять деньги
        player.reduceAdena("Buff", cost, null, true);
        
        // Добавить баф
        Skill skill = SkillData.getInstance().getSkill(skillId, getBuffLevel(skillId));
        if (skill != null) {
            int duration = calculateBuffDuration(player, skillId);
            skill.applyEffects(player, player, false, duration);
            
            player.sendMessage("✨ Баф " + skill.getName() + " активирован!");
            
            // Визуальные эффекты
            if (_config.isVisualEffectsEnabled()) {
                player.broadcastPacket(new MagicSkillUse(player, player, skillId, 1, 1, 0));
            }
            
            // Звуковые эффекты
            if (_config.isSoundEffectsEnabled()) {
                // Можно добавить звуковые эффекты
            }
            
            // Логирование
            PlayerPanelDAO.logBuff(player, skillId, skill.getName(), getBuffLevel(skillId), cost, duration);
            PlayerPanelDAO.logAction(player, "buff_add", "Added buff " + skill.getName(), cost, true);
        } else {
            player.sendMessage("Ошибка при активации бафа!");
            player.addAdena("BuffRefund", cost, null, true); // Возврат денег
        }
        
        // Установка кулдауна
        if (_config.getAntiSpamInterval() > 0) {
            PlayerPanelDAO.setCooldown(player.getObjectId(), "buff", _config.getAntiSpamInterval() * 1000);
        }
    }
    
    /**
     * Убрать баф
     */
    private void removeBuff(L2PcInstance player, int skillId) {
        if (player.getFirstEffect(skillId) == null) {
            player.sendMessage("У вас нет этого бафа!");
            return;
        }
        
        player.stopSkillEffects(skillId);
        player.sendMessage("❌ Баф убран!");
        
        PlayerPanelDAO.logAction(player, "buff_remove", "Removed buff " + skillId, 0, true);
    }
    
    /**
     * Обновить все бафы
     */
    private void refreshAllBuffs(L2PcInstance player) {
        long cost = calculateRefreshCost(player);
        
        if (player.getAdena() < cost) {
            player.sendMessage("Недостаточно Adena для обновления всех бафов!");
            return;
        }
        
        // Проверка кулдауна
        if (PlayerPanelDAO.hasCooldown(player.getObjectId(), "buff_refresh")) {
            player.sendMessage("Подождите перед следующим обновлением бафов!");
            return;
        }
        
        // Снять деньги
        player.reduceAdena("BuffRefresh", cost, null, true);
        
        // Добавить все доступные бафы
        String[] availableBuffs = _config.getAvailableBuffs().split(",");
        int buffCount = 0;
        int failedCount = 0;
        
        for (String buffIdStr : availableBuffs) {
            try {
                int skillId = Integer.parseInt(buffIdStr.trim());
                Skill skill = SkillData.getInstance().getSkill(skillId, getBuffLevel(skillId));
                if (skill != null) {
                    int duration = calculateBuffDuration(player, skillId);
                    skill.applyEffects(player, player, false, duration);
                    buffCount++;
                } else {
                    failedCount++;
                }
            } catch (NumberFormatException e) {
                LOG.warn("Invalid buff ID in config: {}", buffIdStr);
                failedCount++;
            }
        }
        
        if (buffCount > 0) {
            player.sendMessage("🔄 Все бафы обновлены! Получено бафов: " + buffCount);
            if (failedCount > 0) {
                player.sendMessage("⚠️ Не удалось применить " + failedCount + " бафов.");
            }
        } else {
            player.sendMessage("❌ Не удалось применить ни одного бафа!");
            player.addAdena("BuffRefreshRefund", cost, null, true); // Возврат денег
            return;
        }
        
        // Визуальные эффекты
        if (_config.isVisualEffectsEnabled()) {
            player.broadcastPacket(new MagicSkillUse(player, player, 2024, 1, 1, 0));
        }
        
        // Логирование
        PlayerPanelDAO.logAction(player, "buff_refresh", "Refreshed all buffs, count: " + buffCount, cost, true);
        
        // Установка кулдауна
        PlayerPanelDAO.setCooldown(player.getObjectId(), "buff_refresh", 5 * 60000); // 5 минут
    }
    
    /**
     * Применить схему бафов
     */
    private void applyBuffScheme(L2PcInstance player, String schemeName) {
        BuffScheme scheme = getBuffScheme(schemeName);
        if (scheme == null) {
            player.sendMessage("Схема бафов не найдена!");
            return;
        }
        
        long cost = calculateSchemeCost(player, scheme);
        if (player.getAdena() < cost) {
            player.sendMessage("Недостаточно Adena для применения схемы бафов!");
            return;
        }
        
        // Снять деньги
        player.reduceAdena("BuffScheme", cost, null, true);
        
        // Применить бафы схемы
        int appliedCount = 0;
        for (int skillId : scheme.getSkillIds()) {
            if (_validator.isBuffAvailable(skillId)) {
                Skill skill = SkillData.getInstance().getSkill(skillId, getBuffLevel(skillId));
                if (skill != null) {
                    int duration = calculateBuffDuration(player, skillId);
                    skill.applyEffects(player, player, false, duration);
                    appliedCount++;
                }
            }
        }
        
        player.sendMessage("🎯 Схема '" + scheme.getName() + "' применена! Бафов: " + appliedCount);
        
        // Логирование
        PlayerPanelDAO.logAction(player, "buff_scheme", "Applied scheme " + schemeName + ", buffs: " + appliedCount, cost, true);
    }
    
    /**
     * Получить схему бафов
     */
    private BuffScheme getBuffScheme(String schemeName) {
        // Предопределенные схемы бафов
        switch (schemeName.toLowerCase()) {
            case "warrior":
                return new BuffScheme("Warrior", new int[]{1040, 1068, 1077, 1086, 1204}); // Might, Focus, Haste, Wind Walk
            case "mage":
                return new BuffScheme("Mage", new int[]{1045, 1048, 1059, 1085, 1240}); // Shield, Blessed Body, Empower, Acumen, Guidance
            case "support":
                return new BuffScheme("Support", new int[]{1045, 1048, 1240, 1243, 1303}); // Shield, Blessed Body, Guidance, Bless Shield
            case "pvp":
                return new BuffScheme("PvP", new int[]{1040, 1068, 1077, 1086, 1242}); // Might, Focus, Haste, Death Whisper
            case "farm":
                return new BuffScheme("Farm", new int[]{1040, 1045, 1204, 1240, 1303}); // Might, Shield, Wind Walk, Guidance
            default:
                return null;
        }
    }
    
    /**
     * Рассчитать стоимость бафа
     */
    private long calculateBuffCost(L2PcInstance player, int skillId) {
        long baseCost = _config.getBuffCost();
        
        // Модификаторы стоимости в зависимости от типа бафа
        if (isHighTierBuff(skillId)) {
            baseCost *= 2; // Дорогие бафы
        }
        
        // Скидка для премиум игроков
        if (player.hasPremiumStatus()) {
            baseCost = (long)(baseCost * 0.8); // 20% скидка
        }
        
        // Скидка для клана
        if (player.getClan() != null && player.getClan().getLevel() >= 5) {
            baseCost = (long)(baseCost * 0.9); // 10% скидка
        }
        
        return baseCost;
    }
    
    /**
     * Рассчитать стоимость обновления всех бафов
     */
    private long calculateRefreshCost(L2PcInstance player) {
        long baseCost = _config.getBuffRefreshCost();
        
        // Скидка в зависимости от количества активных бафов
        int activeBuffs = getActiveBuffsCount(player);
        if (activeBuffs > 5) {
            baseCost = (long)(baseCost * 1.5); // Дороже, если много активных бафов
        }
        
        // Скидка для премиум игроков
        if (player.hasPremiumStatus()) {
            baseCost = (long)(baseCost * 0.7); // 30% скидка
        }
        
        return baseCost;
    }
    
    /**
     * Рассчитать стоимость схемы бафов
     */
    private long calculateSchemeCost(L2PcInstance player, BuffScheme scheme) {
        long totalCost = 0;
        
        for (int skillId : scheme.getSkillIds()) {
            totalCost += calculateBuffCost(player, skillId);
        }
        
        // Скидка за использование схемы
        totalCost = (long)(totalCost * 0.8); // 20% скидка за схему
        
        return totalCost;
    }
    
    /**
     * Рассчитать длительность бафа
     */
    private int calculateBuffDuration(L2PcInstance player, int skillId) {
        int baseDuration = _config.getBuffDuration();
        
        // Бонус длительности для премиум игроков
        if (player.hasPremiumStatus()) {
            baseDuration = (int)(baseDuration * 1.5); // +50% длительности
        }
        
        // Бонус для высокоуровневых игроков
        if (player.getLevel() >= 76) {
            baseDuration = (int)(baseDuration * 1.2); // +20% длительности
        }
        
        return baseDuration;
    }
    
    /**
     * Получить уровень бафа
     */
    private int getBuffLevel(int skillId) {
        // Для большинства бафов используем уровень 1
        // Можно настроить индивидуально для каждого бафа
        switch (skillId) {
            case 1040: // Shield
            case 1045: // Blessed Body
                return 6; // Высокий уровень для защитных бафов
            case 1068: // Might
            case 1077: // Focus
                return 3; // Средний уровень для боевых бафов
            default:
                return 1; // Базовый уровень
        }
    }
    
    /**
     * Проверить, является ли баф высокого уровня
     */
    private boolean isHighTierBuff(int skillId) {
        // Определяем дорогие бафы
        switch (skillId) {
            case 1240: // Guidance
            case 1242: // Death Whisper
            case 1243: // Bless Shield
            case 1303: // Wild Magic
            case 1304: // Advanced Block
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Получить количество активных бафов
     */
    private int getActiveBuffsCount(L2PcInstance player) {
        return player.getEffectList().getBuffCount();
    }
    
    /**
     * Получить максимальный лимит бафов для игрока
     */
    private int getMaxBuffsLimit(L2PcInstance player) {
        int baseLimit = 20; // Базовый лимит
        
        // Бонус для премиум игроков
        if (player.hasPremiumStatus()) {
            baseLimit += 4;
        }
        
        // Бонус для высокоуровневых игроков
        if (player.getLevel() >= 76) {
            baseLimit += 2;
        }
        
        return baseLimit;
    }
    
    /**
     * Показать панель бафов
     */
    private void showBuffsPanel(L2PcInstance player) {
        String html = _htmlGenerator.generateMainPanel(player, "buffs");
        CommunityBoardHandler.separateAndSend(html, player);
    }
    
    /**
     * Внутренний класс для схем бафов
     */
    private static class BuffScheme {
        private final String name;
        private final int[] skillIds;
        
        public BuffScheme(String name, int[] skillIds) {
            this.name = name;
            this.skillIds = skillIds;
        }
        
        public String getName() {
            return name;
        }
        
        public int[] getSkillIds() {
            return skillIds;
        }
    }
}