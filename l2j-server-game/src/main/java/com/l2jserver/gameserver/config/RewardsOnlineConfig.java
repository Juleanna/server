package com.l2jserver.gameserver.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

/**
 * Улучшенная конфигурация для системы онлайн наград с кешированием
 * @author Dafna
 */
@Sources("file:config/RewardsOnline.properties")
public interface RewardsOnlineConfig extends Config {
    
    RewardsOnlineConfig INSTANCE = ConfigFactory.create(RewardsOnlineConfig.class);
    
    static RewardsOnlineConfig getInstance() {
        return INSTANCE;
    }
    
    // ===============================
    // ОСНОВНЫЕ НАСТРОЙКИ СИСТЕМЫ
    // ===============================
    
    /**
     * Включена ли система наград
     */
    @DefaultValue("true")
    @Key("reward.system.enabled")
    boolean isRewardSystemEnabled();
    
    /**
     * Режим отладки
     */
    @DefaultValue("false")
    @Key("reward.debug.mode")
    boolean isDebugMode();
    
    /**
     * Версия конфигурации (для отслеживания изменений)
     */
    @DefaultValue("2.0.0")
    @Key("reward.config.version")
    String getConfigVersion();
    
    // ===============================
    // НАСТРОЙКИ КЕШИРОВАНИЯ
    // ===============================
    
    /**
     * Включено ли кеширование
     */
    @DefaultValue("true")
    @Key("reward.cache.enabled")
    boolean isCacheEnabled();
    
    /**
     * TTL для кеша групп наград в миллисекундах
     */
    @DefaultValue("300000")
    @Key("reward.cache.groups.ttl")
    long getGroupsCacheTtl();
    
    /**
     * TTL для кеша игроков в миллисекундах
     */
    @DefaultValue("1800000")
    @Key("reward.cache.players.ttl")
    long getPlayersCacheTtl();
    
    /**
     * TTL для кеша конфигурации в миллисекундах
     */
    @DefaultValue("600000")
    @Key("reward.cache.config.ttl")
    long getConfigCacheTtl();
    
    /**
     * Общий TTL по умолчанию
     */
    @DefaultValue("900000")
    @Key("reward.cache.default.ttl")
    long getCacheTtl();
    
    /**
     * Максимальный размер кеша
     */
    @DefaultValue("1000")
    @Key("reward.cache.max.size")
    int getCacheMaxSize();
    
    /**
     * Интервал очистки кеша в миллисекундах
     */
    @DefaultValue("300000")
    @Key("reward.cache.cleanup.interval")
    long getCacheCleanupInterval();
    
    // ===============================
    // НАСТРОЙКИ БАЗЫ ДАННЫХ
    // ===============================
    
    /**
     * Размер пула соединений с БД
     */
    @DefaultValue("10")
    @Key("reward.database.pool.size")
    int getDatabasePoolSize();
    
    /**
     * Таймаут соединения с БД в миллисекундах
     */
    @DefaultValue("30000")
    @Key("reward.database.connection.timeout")
    long getDatabaseConnectionTimeout();
    
    /**
     * Запрос для валидации соединения
     */
    @DefaultValue("SELECT 1")
    @Key("reward.database.validation.query")
    String getDatabaseValidationQuery();
    
    /**
     * Интервал проверки здоровья БД в миллисекундах
     */
    @DefaultValue("60000")
    @Key("reward.database.health.check.interval")
    long getDatabaseHealthCheckInterval();
    
    /**
     * Размер батча для сохранения статистики
     */
    @DefaultValue("100")
    @Key("reward.database.batch.size")
    int getDatabaseBatchSize();
    
    /**
     * Включена ли асинхронная обработка БД
     */
    @DefaultValue("true")
    @Key("reward.database.async.enabled")
    boolean isDatabaseAsyncEnabled();
    
    // ===============================
    // НАСТРОЙКИ ПРОИЗВОДИТЕЛЬНОСТИ
    // ===============================
    
    /**
     * Размер пула потоков для задач наград
     */
    @DefaultValue("5")
    @Key("reward.thread.pool.size")
    int getThreadPoolSize();
    
    /**
     * Размер пула потоков для асинхронной обработки
     */
    @DefaultValue("3")
    @Key("reward.async.thread.pool.size")
    int getAsyncThreadPoolSize();
    
    /**
     * Максимальное время выполнения задачи награды в миллисекундах
     */
    @DefaultValue("30000")
    @Key("reward.task.max.execution.time")
    long getTaskMaxExecutionTime();
    
    /**
     * Максимальное количество попыток повтора
     */
    @DefaultValue("3")
    @Key("reward.task.max.retry.attempts")
    int getTaskMaxRetryAttempts();
    
    /**
     * Задержка между попытками повтора в миллисекундах
     */
    @DefaultValue("5000")
    @Key("reward.task.retry.delay")
    long getTaskRetryDelay();
    
    /**
     * Включена ли асинхронная обработка наград
     */
    @DefaultValue("true")
    @Key("reward.async.processing")
    boolean isAsyncProcessingEnabled();
    
    // ===============================
    // ANTI-AFK СИСТЕМА
    // ===============================
    
    /**
     * Интервал проверки AFK в миллисекундах
     */
    @DefaultValue("120000")
    @Key("reward.afk.check.interval")
    long getAfkCheckInterval();
    
    /**
     * Таймаут AFK в миллисекундах
     */
    @DefaultValue("600000")
    @Key("reward.afk.timeout")
    long getAfkTimeout();
    
    /**
     * Минимальное расстояние для определения движения
     */
    @DefaultValue("100")
    @Key("reward.afk.movement.threshold")
    int getAfkMovementThreshold();
    
    /**
     * Включена ли система Anti-AFK
     */
    @DefaultValue("true")
    @Key("reward.afk.enabled")
    boolean isAfkSystemEnabled();
    
    /**
     * Дополнительные проверки для AFK (атаки, скилы)
     */
    @DefaultValue("true")
    @Key("reward.afk.advanced.checks")
    boolean isAfkAdvancedChecksEnabled();
    
    // ===============================
    // ПРОГРЕССИВНАЯ СИСТЕМА
    // ===============================
    
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
     * Интервал обновления прогрессивного множителя в миллисекундах
     */
    @DefaultValue("3600000")
    @Key("reward.progressive.update.interval")
    long getProgressiveUpdateInterval();
    
    /**
     * Включена ли прогрессивная система
     */
    @DefaultValue("true")
    @Key("reward.progressive.enabled")
    boolean isProgressiveSystemEnabled();
    
    // ===============================
    // КАЛЕНДАРНЫЕ СОБЫТИЯ
    // ===============================
    
    /**
     * Интервал проверки календарных событий в миллисекундах
     */
    @DefaultValue("300000")
    @Key("reward.calendar.check.interval")
    long getCalendarCheckInterval();
    
    /**
     * Включены ли календарные события
     */
    @DefaultValue("true")
    @Key("reward.calendar.enabled")
    boolean isCalendarEventsEnabled();
    
    /**
     * Автоматическое создание событий выходного дня
     */
    @DefaultValue("true")
    @Key("reward.calendar.auto.weekend.events")
    boolean isAutoWeekendEventsEnabled();
    
    // ===============================
    // СТАТИСТИКА И МОНИТОРИНГ
    // ===============================
    
    /**
     * Включена ли система статистики
     */
    @DefaultValue("true")
    @Key("reward.statistics.enabled")
    boolean isStatisticsEnabled();
    
    /**
     * Интервал сохранения статистики в миллисекундах
     */
    @DefaultValue("300000")
    @Key("reward.statistics.save.interval")
    long getStatisticsSaveInterval();
    
    /**
     * Дни хранения статистики
     */
    @DefaultValue("30")
    @Key("reward.statistics.retention.days")
    int getStatisticsRetentionDays();
    
    /**
     * Размер батча для статистики
     */
    @DefaultValue("50")
    @Key("reward.statistics.batch.size")
    int getStatisticsBatchSize();
    
    /**
     * Включено ли детальное логирование статистики
     */
    @DefaultValue("false")
    @Key("reward.statistics.detailed.logging")
    boolean isDetailedStatisticsLoggingEnabled();
    
    // ===============================
    // ВЕБ-ПАНЕЛЬ УПРАВЛЕНИЯ
    // ===============================
    
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
     * Хост для привязки веб-панели
     */
    @DefaultValue("localhost")
    @Key("reward.web.panel.host")
    String getWebPanelHost();
    
    /**
     * Включена ли аутентификация для веб-панели
     */
    @DefaultValue("true")
    @Key("reward.web.panel.auth.enabled")
    boolean isWebPanelAuthEnabled();
    
    /**
     * Пользователь для веб-панели
     */
    @DefaultValue("admin")
    @Key("reward.web.panel.username")
    String getWebPanelUsername();
    
    /**
     * Пароль для веб-панели
     */
    @DefaultValue("admin123")
    @Key("reward.web.panel.password")
    String getWebPanelPassword();
    
    // ===============================
    // АВТОМАТИЧЕСКАЯ ПЕРЕЗАГРУЗКА
    // ===============================
    
    /**
     * Интервал перезагрузки конфигурации в миллисекундах
     */
    @DefaultValue("300000")
    @Key("reward.config.reload.interval")
    long getConfigReloadInterval();
    
    /**
     * Включена ли автоматическая перезагрузка конфигурации
     */
    @DefaultValue("true")
    @Key("reward.config.auto.reload")
    boolean isConfigAutoReloadEnabled();
    
    /**
     * Интервал очистки в миллисекундах
     */
    @DefaultValue("1800000")
    @Key("reward.cleanup.interval")
    long getCleanupInterval();
    
    /**
     * Включена ли автоматическая очистка
     */
    @DefaultValue("true")
    @Key("reward.cleanup.enabled")
    boolean isCleanupEnabled();
    
    // ===============================
    // ЛОГИРОВАНИЕ
    // ===============================
    
    /**
     * Уровень логирования (TRACE, DEBUG, INFO, WARN, ERROR)
     */
    @DefaultValue("INFO")
    @Key("reward.log.level")
    String getLogLevel();
    
    /**
     * Включено ли логирование в файл
     */
    @DefaultValue("true")
    @Key("reward.log.file.enabled")
    boolean isFileLoggingEnabled();
    
    /**
     * Путь к файлу логов
     */
    @DefaultValue("logs/rewards/")
    @Key("reward.log.file.path")
    String getLogFilePath();
    
    /**
     * Максимальный размер файла лога в МБ
     */
    @DefaultValue("10")
    @Key("reward.log.file.max.size")
    int getLogFileMaxSize();
    
    /**
     * Количество файлов логов для хранения
     */
    @DefaultValue("7")
    @Key("reward.log.file.backup.count")
    int getLogFileBackupCount();
    
    /**
     * Включено ли логирование производительности
     */
    @DefaultValue("true")
    @Key("reward.log.performance")
    boolean isPerformanceLoggingEnabled();
    
    // ===============================
    // БЕЗОПАСНОСТЬ И ЗАЩИТА
    // ===============================
    
    /**
     * Максимальное количество наград для игрока в час
     */
    @DefaultValue("1000")
    @Key("reward.security.max.rewards.per.hour")
    int getMaxRewardsPerHour();
    
    /**
     * Включена ли защита от злоупотреблений
     */
    @DefaultValue("true")
    @Key("reward.security.abuse.protection")
    boolean isAbuseProtectionEnabled();
    
    /**
     * Минимальный интервал между наградами в миллисекундах
     */
    @DefaultValue("60000")
    @Key("reward.security.min.reward.interval")
    long getMinRewardInterval();
    
    /**
     * Включена ли IP-защита (один аккаунт с IP)
     */
    @DefaultValue("false")
    @Key("reward.security.ip.protection")
    boolean isIpProtectionEnabled();
    
    // ===============================
    // УВЕДОМЛЕНИЯ И СООБЩЕНИЯ
    // ===============================
    
    /**
     * Включены ли уведомления игрокам о получении наград
     */
    @DefaultValue("true")
    @Key("reward.notifications.enabled")
    boolean isNotificationsEnabled();
    
    /**
     * Тип уведомлений (SYSTEM, SCREEN, CHAT, ALL)
     */
    @DefaultValue("SYSTEM")
    @Key("reward.notifications.type")
    String getNotificationType();
    
    /**
     * Включены ли звуковые уведомления
     */
    @DefaultValue("true")
    @Key("reward.notifications.sound")
    boolean isSoundNotificationsEnabled();
    
    /**
     * Включены ли уведомления о событиях
     */
    @DefaultValue("true")
    @Key("reward.notifications.events")
    boolean isEventNotificationsEnabled();
    
    // ===============================
    // ЛОКАЛИЗАЦИЯ
    // ===============================
    
    /**
     * Язык по умолчанию для системы
     */
    @DefaultValue("en")
    @Key("reward.language.default")
    String getDefaultLanguage();
    
    /**
     * Включена ли поддержка множественных языков
     */
    @DefaultValue("true")
    @Key("reward.language.multilingual")
    boolean isMultilingualEnabled();
    
    // ===============================
    // ЭКСПЕРИМЕНТАЛЬНЫЕ ФУНКЦИИ
    // ===============================
    
    /**
     * Включена ли поддержка кастомных скриптов
     */
    @DefaultValue("false")
    @Key("reward.experimental.custom.scripts")
    boolean isCustomScriptsEnabled();
    
    /**
     * Включена ли интеграция с внешними API
     */
    @DefaultValue("false")
    @Key("reward.experimental.external.api")
    boolean isExternalApiEnabled();
    
    /**
     * Включен ли расширенный мониторинг
     */
    @DefaultValue("false")
    @Key("reward.experimental.advanced.monitoring")
    boolean isAdvancedMonitoringEnabled();
}