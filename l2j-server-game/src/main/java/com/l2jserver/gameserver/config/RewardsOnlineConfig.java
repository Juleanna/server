package com.l2jserver.gameserver.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * Конфигурация для системы онлайн наград
 * @author Dafna
 */
@Sources("file:config/RewardsOnline.properties")
public interface RewardsOnlineConfig extends Config {
    
    /**
     * Включена ли система наград
     */
    @DefaultValue("true")
    @Key("reward.system.enabled")
    boolean isRewardSystemEnabled();
    
    /**
     * Интервал проверки AFK в минутах
     */
    @DefaultValue("2")
    @Key("reward.afk.check.interval")
    int getAfkCheckInterval();
    
    /**
     * Таймаут AFK в минутах
     */
    @DefaultValue("10")
    @Key("reward.afk.timeout")
    int getAfkTimeout();
    
    /**
     * Максимальный прогрессивный множитель
     */
    @DefaultValue("5.0")
    @Key("reward.progressive.max.multiplier")
    double getMaxProgressiveMultiplier();
    
    /**
     * Шаг увеличения прогрессивного множителя
     */
    @DefaultValue("0.1")
    @Key("reward.progressive.increment")
    double getProgressiveIncrement();
    
    /**
     * Включена ли веб-панель управления
     */
    @DefaultValue("true")
    @Key("reward.web.panel.enabled")
    boolean isWebPanelEnabled();
    
    /**
     * Порт для веб-панели
     */
    @DefaultValue("8080")
    @Key("reward.web.panel.port")
    int getWebPanelPort();
    
    /**
     * Интервал перезагрузки конфигурации в минутах
     */
    @DefaultValue("5")
    @Key("reward.config.reload.interval")
    int getConfigReloadInterval();
    
    /**
     * Интервал очистки в минутах
     */
    @DefaultValue("30")
    @Key("reward.cleanup.interval")
    int getCleanupInterval();
    
    /**
     * Уровень логирования (DEBUG, INFO, WARN, ERROR)
     */
    @DefaultValue("INFO")
    @Key("reward.log.level")
    String getLogLevel();
    
    /**
     * Включено ли сохранение статистики в БД
     */
    @DefaultValue("true")
    @Key("reward.statistics.save.enabled")
    boolean isStatisticsSaveEnabled();
    
    /**
     * Дни хранения статистики
     */
    @DefaultValue("30")
    @Key("reward.statistics.retention.days")
    int getStatisticsRetentionDays();
}