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
package com.l2jserver.gameserver.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * Конфигурация для панели игрока
 * @author YourName
 */
@Sources("file:config/PlayerPanel.properties")
public interface PlayerPanelConfig extends Config {
    
    /**
     * Включена ли панель игрока
     */
    @DefaultValue("true")
    @Key("player.panel.enabled")
    boolean isPlayerPanelEnabled();
    
    /**
     * Включена ли система заточки в панели
     */
    @DefaultValue("true")
    @Key("player.panel.enchant.enabled")
    boolean isEnchantEnabled();
    
    /**
     * Максимальный уровень заточки в панели
     */
    @DefaultValue("20")
    @Key("player.panel.enchant.max.level")
    int getMaxEnchantLevel();
    
    /**
     * Базовая стоимость заточки
     */
    @DefaultValue("10000")
    @Key("player.panel.enchant.base.cost")
    long getEnchantBaseCost();
    
    /**
     * Включен ли магазин в панели
     */
    @DefaultValue("true")
    @Key("player.panel.shop.enabled")
    boolean isShopEnabled();
    
    /**
     * Множитель цен в магазине
     */
    @DefaultValue("1.0")
    @Key("player.panel.shop.price.multiplier")
    double getShopPriceMultiplier();
    
    /**
     * Включена ли система бафов в панели
     */
    @DefaultValue("true")
    @Key("player.panel.buffs.enabled")
    boolean isBuffsEnabled();
    
    /**
     * Стоимость одного бафа
     */
    @DefaultValue("5000")
    @Key("player.panel.buff.cost")
    long getBuffCost();
    
    /**
     * Стоимость обновления всех бафов
     */
    @DefaultValue("50000")
    @Key("player.panel.buff.refresh.cost")
    long getBuffRefreshCost();
    
    /**
     * Длительность бафов в секундах
     */
    @DefaultValue("1200")
    @Key("player.panel.buff.duration")
    int getBuffDuration();
    
    /**
     * Разрешены ли бафы в бою
     */
    @DefaultValue("false")
    @Key("player.panel.buff.in.combat")
    boolean isBuffInCombatAllowed();
    
    /**
     * Минимальный уровень для использования панели
     */
    @DefaultValue("20")
    @Key("player.panel.min.level")
    int getMinLevel();
    
    /**
     * Максимальный уровень для использования панели (0 = без ограничений)
     */
    @DefaultValue("0")
    @Key("player.panel.max.level")
    int getMaxLevel();
    
    /**
     * Стоимость входа в панель (0 = бесплатно)
     */
    @DefaultValue("0")
    @Key("player.panel.entry.cost")
    long getEntryCost();
    
    /**
     * Кулдаун между использованиями панели в минутах
     */
    @DefaultValue("0")
    @Key("player.panel.cooldown")
    int getPanelCooldown();
    
    /**
     * Разрешена ли панель в зонах PvP
     */
    @DefaultValue("false")
    @Key("player.panel.pvp.zones")
    boolean isPvpZonesAllowed();
    
    /**
     * Разрешена ли панель в осадных зонах
     */
    @DefaultValue("false")
    @Key("player.panel.siege.zones")
    boolean isSiegeZonesAllowed();
    
    /**
     * Разрешена ли панель в олимпиаде
     */
    @DefaultValue("false")
    @Key("player.panel.olympiad")
    boolean isOlympiadAllowed();
    
    /**
     * Включены ли уведомления для всего сервера
     */
    @DefaultValue("false")
    @Key("player.panel.global.announcements")
    boolean isGlobalAnnouncementsEnabled();
    
    /**
     * Включены ли звуковые эффекты
     */
    @DefaultValue("true")
    @Key("player.panel.sound.effects")
    boolean isSoundEffectsEnabled();
    
    /**
     * Включены ли визуальные эффекты
     */
    @DefaultValue("true")
    @Key("player.panel.visual.effects")
    boolean isVisualEffectsEnabled();
    
    /**
     * Список запрещенных предметов для заточки (через запятую)
     */
    @DefaultValue("")
    @Key("player.panel.enchant.forbidden.items")
    String getForbiddenEnchantItems();
    
    /**
     * Список доступных предметов в магазине (через запятую)
     */
    @DefaultValue("1,2,3,4,5,6,7,8,9,10")
    @Key("player.panel.shop.available.items")
    String getShopAvailableItems();
    
    /**
     * Список доступных бафов (через запятую)
     */
    @DefaultValue("1040,1045,1048,1059,1068,1077,1085,1086,1204,1240,1242,1243,1303,1304")
    @Key("player.panel.available.buffs")
    String getAvailableBuffs();
    
    /**
     * Включено ли логирование действий
     */
    @DefaultValue("true")
    @Key("player.panel.logging.enabled")
    boolean isLoggingEnabled();
    
    /**
     * Включена ли защита от спама
     */
    @DefaultValue("true")
    @Key("player.panel.anti.spam")
    boolean isAntiSpamEnabled();
    
    /**
     * Интервал защиты от спама в секундах
     */
    @DefaultValue("3")
    @Key("player.panel.anti.spam.interval")
    int getAntiSpamInterval();
    
    /**
     * Включена ли система рейтингов
     */
    @DefaultValue("true")
    @Key("player.panel.rating.system")
    boolean isRatingSystemEnabled();
    
    /**
     * Шанс успеха заточки на +1
     */
    @DefaultValue("85")
    @Key("player.panel.enchant.success.rate.1")
    int getEnchantSuccessRate1();
    
    /**
     * Шанс успеха заточки на +5
     */
    @DefaultValue("60")
    @Key("player.panel.enchant.success.rate.5")
    int getEnchantSuccessRate5();
    
    /**
     * Шанс успеха заточки на +10
     */
    @DefaultValue("30")
    @Key("player.panel.enchant.success.rate.10")
    int getEnchantSuccessRate10();
    
    /**
     * Шанс успеха заточки на +15
     */
    @DefaultValue("10")
    @Key("player.panel.enchant.success.rate.15")
    int getEnchantSuccessRate15();
    
    /**
     * Разрешено ли понижение уровня заточки при неудаче
     */
    @DefaultValue("true")
    @Key("player.panel.enchant.can.break")
    boolean canEnchantBreak();
    
    /**
     * Безопасный уровень заточки (до которого предмет не ломается)
     */
    @DefaultValue("3")
    @Key("player.panel.enchant.safe.level")
    int getEnchantSafeLevel();
}