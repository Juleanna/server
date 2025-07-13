package com.l2jserver.datapack.custom.RewardForTimeOnline.systems;

import com.l2jserver.datapack.custom.RewardForTimeOnline.database.RewardDatabase;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.CalendarEvent;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.ItemReward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Менеджер календарных событий
 * Управляет специальными событиями с бонусными наградами
 * @author Dafna
 */
public class CalendarEventManager {
    private static final Logger LOG = LoggerFactory.getLogger(CalendarEventManager.class);
    
    // Константы
    private static final long EVENT_CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    private static final long EVENT_CLEANUP_INTERVAL = TimeUnit.HOURS.toMillis(1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    
    // Компоненты
    private final RewardDatabase database;
    private volatile List<CalendarEvent> allEvents;
    private volatile List<CalendarEvent> activeEvents;
    private volatile long lastEventCheck;
    private ScheduledFuture<?> eventCheckTask;
    private ScheduledFuture<?> cleanupTask;
    private boolean isInitialized = false;
    
    // Статистика
    private int eventActivations = 0;
    private int eventDeactivations = 0;
    private long totalEventTime = 0;
    
    public CalendarEventManager(RewardDatabase database) {
        this.database = database;
        this.allEvents = new ArrayList<>();
        this.activeEvents = new ArrayList<>();
        this.lastEventCheck = 0;
    }
    
    /**
     * Инициализация системы
     */
    public void initialize() {
        if (isInitialized) {
            LOG.warn("CalendarEventManager already initialized");
            return;
        }
        
        LOG.info("Initializing CalendarEventManager...");
        
        // Загружаем события из базы данных
        loadEvents();
        
        // Создаем события по умолчанию если их нет
        if (allEvents.isEmpty()) {
            createDefaultEvents();
        }
        
        // Обновляем активные события
        updateActiveEvents();
        
        // Запускаем периодические задачи
        startPeriodicTasks();
        
        isInitialized = true;
        LOG.info("CalendarEventManager initialized with {} total events, {} currently active", 
            allEvents.size(), activeEvents.size());
    }
    
    /**
     * Загружает события из базы данных
     */
    public void loadEvents() {
        try {
            List<CalendarEvent> loadedEvents = database.loadCalendarEvents();
            allEvents = new ArrayList<>(loadedEvents);
            
            LOG.info("Loaded {} calendar events from database", loadedEvents.size());
            
            // Логируем загруженные события
            for (CalendarEvent event : loadedEvents) {
                LOG.debug("Loaded event: {} ({} - {}) multiplier: {:.1f}", 
                    event.getName(), event.getFormattedStartDate(), 
                    event.getFormattedEndDate(), event.getRewardMultiplier());
            }
            
        } catch (Exception e) {
            LOG.error("Failed to load calendar events from database", e);
            allEvents = new ArrayList<>();
        }
    }
    
    /**
     * Создает события по умолчанию
     */
    private void createDefaultEvents() {
        LOG.info("Creating default calendar events");
        
        LocalDateTime now = LocalDateTime.now();
        List<CalendarEvent> defaultEvents = new ArrayList<>();
        
        // Выходные бонусы (каждые выходные)
        CalendarEvent weekendBonus = CalendarEvent.builder()
            .name("Weekend Bonus")
            .description("Double rewards during weekends")
            .startDate(getNextWeekend())
            .endDate(getNextWeekend().plusDays(2))
            .rewardMultiplier(2.0)
            .build();
        defaultEvents.add(weekendBonus);
        
        // Ночные бонусы (каждую ночь)
        CalendarEvent nightBonus = CalendarEvent.builder()
            .name("Night Bonus")
            .description("Increased rewards during night hours")
            .startDate(now.withHour(22).withMinute(0).withSecond(0))
            .endDate(now.plusDays(1).withHour(6).withMinute(0).withSecond(0))
            .rewardMultiplier(1.5)
            .build();
        defaultEvents.add(nightBonus);
        
        // Праздничное событие
        CalendarEvent holidayEvent = CalendarEvent.builder()
            .name("Holiday Celebration")
            .description("Special holiday rewards")
            .startDate(now.plusDays(7))
            .endDate(now.plusDays(10))
            .rewardMultiplier(3.0)
            .specialReward(57, 100000) // 100K Adena
            .build();
        defaultEvents.add(holidayEvent);
        
        // Месячное событие
        CalendarEvent monthlyEvent = CalendarEvent.builder()
            .name("Monthly Event")
            .description("Monthly special rewards")
            .startDate(now.withDayOfMonth(1).plusMonths(1))
            .endDate(now.withDayOfMonth(1).plusMonths(1).plusDays(7))
            .rewardMultiplier(2.5)
            .specialReward(6577, 1) // Blessed Enchant Weapon S
            .build();
        defaultEvents.add(monthlyEvent);
        
        allEvents.addAll(defaultEvents);
        
        // Сохраняем в базу данных
        try {
            database.saveCalendarEvents(defaultEvents);
            LOG.info("Created and saved {} default calendar events", defaultEvents.size());
        } catch (Exception e) {
            LOG.error("Failed to save default calendar events", e);
        }
    }
    
    /**
     * Получает дату следующих выходных
     */
    private LocalDateTime getNextWeekend() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currentDay = now.getDayOfWeek();
        
        int daysUntilSaturday = DayOfWeek.SATURDAY.getValue() - currentDay.getValue();
        if (daysUntilSaturday <= 0) {
            daysUntilSaturday += 7;
        }
        
        return now.plusDays(daysUntilSaturday).withHour(0).withMinute(0).withSecond(0);
    }
    
    /**
     * Обновляет список активных событий
     */
    private void updateActiveEvents() {
        List<CalendarEvent> newActiveEvents = allEvents.stream()
            .filter(CalendarEvent::isActiveNow)
            .sorted((a, b) -> Double.compare(b.getRewardMultiplier(), a.getRewardMultiplier()))
            .collect(Collectors.toList());
        
        // Проверяем изменения
        Set<String> previousActiveNames = activeEvents.stream()
            .map(CalendarEvent::getName)
            .collect(Collectors.toSet());
        
        Set<String> newActiveNames = newActiveEvents.stream()
            .map(CalendarEvent::getName)
            .collect(Collectors.toSet());
        
        // Логируем активации
        for (String newName : newActiveNames) {
            if (!previousActiveNames.contains(newName)) {
                CalendarEvent event = newActiveEvents.stream()
                    .filter(e -> e.getName().equals(newName))
                    .findFirst().orElse(null);
                if (event != null) {
                    eventActivations++;
                    LOG.info("Calendar event activated: {} (multiplier: {:.1f})", 
                        event.getName(), event.getRewardMultiplier());
                }
            }
        }
        
        // Логируем деактивации
        for (String previousName : previousActiveNames) {
            if (!newActiveNames.contains(previousName)) {
                eventDeactivations++;
                LOG.info("Calendar event deactivated: {}", previousName);
            }
        }
        
        activeEvents = newActiveEvents;
        lastEventCheck = System.currentTimeMillis();
    }
    
    /**
     * Запускает периодические задачи
     */
    private void startPeriodicTasks() {
        // Задача проверки событий
        eventCheckTask = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(this::updateActiveEvents, 
                EVENT_CHECK_INTERVAL, EVENT_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
        
        // Задача очистки устаревших событий
        cleanupTask = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(this::cleanupExpiredEvents, 
                EVENT_CLEANUP_INTERVAL, EVENT_CLEANUP_INTERVAL, TimeUnit.MILLISECONDS);
        
        LOG.debug("Calendar event periodic tasks started");
    }
    
    /**
     * Очищает устаревшие события
     */
/**
 * Очищает устаревшие события
 */
private void cleanupExpiredEvents() {
    try {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        AtomicInteger removedCount = new AtomicInteger(0);

        allEvents.removeIf(event -> {
            if (event.getEndDate().isBefore(cutoffDate)) {
                removedCount.incrementAndGet();
                return true;
            }
            return false;
        });

        if (removedCount.get() > 0) {
            LOG.info("Cleaned up {} expired calendar events", removedCount.get());
            database.cleanupExpiredEvents(cutoffDate);
        }

    } catch (Exception e) {
        LOG.error("Error during calendar event cleanup", e);
    }
}

    
    /**
     * Получает список активных событий
     */
    public List<CalendarEvent> getActiveEvents() {
        return new ArrayList<>(activeEvents);
    }
    
    /**
     * Получает имена активных событий
     */
    public List<String> getActiveEventNames() {
        return activeEvents.stream()
            .map(CalendarEvent::getName)
            .collect(Collectors.toList());
    }
    
    /**
     * Получает текущий множитель от событий
     */
    public double getCurrentEventMultiplier() {
        return activeEvents.stream()
            .mapToDouble(CalendarEvent::getRewardMultiplier)
            .max()
            .orElse(1.0);
    }
    
    /**
     * Получает специальные награды от событий
     */
    public List<ItemReward> getSpecialEventRewards() {
        List<ItemReward> specialRewards = new ArrayList<>();
        
        for (CalendarEvent event : activeEvents) {
            if (event.hasSpecialReward()) {
                ItemReward specialReward = event.createSpecialReward();
                if (specialReward != null) {
                    specialRewards.add(specialReward);
                }
            }
        }
        
        return specialRewards;
    }
    
    /**
     * Добавляет новое событие
     */
    public boolean addEvent(CalendarEvent event) {
        try {
            // Проверяем конфликты по именам
            boolean nameExists = allEvents.stream()
                .anyMatch(e -> e.getName().equals(event.getName()));
            
            if (nameExists) {
                LOG.warn("Calendar event with name '{}' already exists", event.getName());
                return false;
            }
            
            allEvents.add(event);
            database.saveCalendarEvent(event);
            
            // Обновляем активные события
            updateActiveEvents();
            
            LOG.info("Added new calendar event: {}", event.getName());
            return true;
            
        } catch (Exception e) {
            LOG.error("Failed to add calendar event: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Удаляет событие по имени
     */
    public boolean removeEvent(String eventName) {
        try {
            CalendarEvent removed = null;
            for (CalendarEvent event : allEvents) {
                if (event.getName().equals(eventName)) {
                    removed = event;
                    break;
                }
            }
            
            if (removed != null) {
                allEvents.remove(removed);
                database.removeCalendarEvent(eventName);
                updateActiveEvents();
                
                LOG.info("Removed calendar event: {}", eventName);
                return true;
            } else {
                LOG.warn("Calendar event not found: {}", eventName);
                return false;
            }
            
        } catch (Exception e) {
            LOG.error("Failed to remove calendar event '{}': {}", eventName, e.getMessage());
            return false;
        }
    }
    
    /**
     * Получает событие по имени
     */
    public CalendarEvent getEvent(String eventName) {
        return allEvents.stream()
            .filter(e -> e.getName().equals(eventName))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Получает все события
     */
    public List<CalendarEvent> getAllEvents() {
        return new ArrayList<>(allEvents);
    }
    
    /**
     * Получает предстоящие события
     */
    public List<CalendarEvent> getUpcomingEvents() {
        return allEvents.stream()
            .filter(CalendarEvent::isUpcoming)
            .sorted((a, b) -> a.getStartDate().compareTo(b.getStartDate()))
            .collect(Collectors.toList());
    }
    
    /**
     * Проверяет изменения в событиях
     */
    public boolean hasEventsChanged() {
        try {
            return database.hasCalendarEventsChanged(lastEventCheck);
        } catch (Exception e) {
            LOG.warn("Error checking calendar events changes: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Перезагружает события из базы данных
     */
    public void reloadEvents() {
        try {
            LOG.info("Reloading calendar events...");
            
            loadEvents();
            updateActiveEvents();
            
            LOG.info("Calendar events reloaded: {} total, {} active", 
                allEvents.size(), activeEvents.size());
                
        } catch (Exception e) {
            LOG.error("Failed to reload calendar events", e);
        }
    }
    
    /**
     * Принудительно активирует событие (для тестирования)
     */
    public void forceActivateEvent(String eventName) {
        CalendarEvent event = getEvent(eventName);
        if (event != null) {
            // Создаем временную активную версию события
            CalendarEvent tempEvent = CalendarEvent.builder()
                .name(event.getName() + " (Forced)")
                .description(event.getDescription() + " - Manually activated")
                .startDate(LocalDateTime.now().minusMinutes(1))
                .endDate(LocalDateTime.now().plusHours(1))
                .rewardMultiplier(event.getRewardMultiplier())
                .build();
            
            if (event.hasSpecialReward()) {
                tempEvent = CalendarEvent.builder()
                    .name(tempEvent.getName())
                    .description(tempEvent.getDescription())
                    .startDate(tempEvent.getStartDate())
                    .endDate(tempEvent.getEndDate())
                    .rewardMultiplier(tempEvent.getRewardMultiplier())
                    .specialReward(event.getSpecialItemId(), event.getSpecialItemCount())
                    .build();
            }
            
            allEvents.add(tempEvent);
            updateActiveEvents();
            
            LOG.info("Force activated calendar event: {}", eventName);
        } else {
            LOG.warn("Cannot force activate event - not found: {}", eventName);
        }
    }
    
    /**
     * Получает статистику событий
     */
    public CalendarEventStatistics getStatistics() {
        long totalEvents = allEvents.size();
        long activeEvents = this.activeEvents.size();
        long upcomingEvents = getUpcomingEvents().size();
        long expiredEvents = allEvents.stream()
            .filter(CalendarEvent::isExpired)
            .count();
        
        double avgMultiplier = allEvents.stream()
            .mapToDouble(CalendarEvent::getRewardMultiplier)
            .average()
            .orElse(1.0);
        
        double currentMaxMultiplier = getCurrentEventMultiplier();
        
        return new CalendarEventStatistics(
            totalEvents,
            activeEvents,
            upcomingEvents,
            expiredEvents,
            avgMultiplier,
            currentMaxMultiplier,
            eventActivations,
            eventDeactivations
        );
    }
    
    /**
     * Отключение системы
     */
    public void shutdown() {
        LOG.info("Shutting down CalendarEventManager...");
        
        // Останавливаем периодические задачи
        if (eventCheckTask != null) {
            eventCheckTask.cancel(false);
            eventCheckTask = null;
        }
        if (cleanupTask != null) {
            cleanupTask.cancel(false);
            cleanupTask = null;
        }
        
        // Очищаем данные
        allEvents.clear();
        activeEvents.clear();
        
        isInitialized = false;
        LOG.info("CalendarEventManager shutdown completed");
    }
    
    /**
     * Статистика календарных событий
     */
    public static class CalendarEventStatistics {
        private final long totalEvents;
        private final long activeEvents;
        private final long upcomingEvents;
        private final long expiredEvents;
        private final double averageMultiplier;
        private final double currentMaxMultiplier;
        private final int eventActivations;
        private final int eventDeactivations;
        
        public CalendarEventStatistics(long totalEvents, long activeEvents, long upcomingEvents,
                                     long expiredEvents, double averageMultiplier, double currentMaxMultiplier,
                                     int eventActivations, int eventDeactivations) {
            this.totalEvents = totalEvents;
            this.activeEvents = activeEvents;
            this.upcomingEvents = upcomingEvents;
            this.expiredEvents = expiredEvents;
            this.averageMultiplier = averageMultiplier;
            this.currentMaxMultiplier = currentMaxMultiplier;
            this.eventActivations = eventActivations;
            this.eventDeactivations = eventDeactivations;
        }
        
        public long getTotalEvents() { return totalEvents; }
        public long getActiveEvents() { return activeEvents; }
        public long getUpcomingEvents() { return upcomingEvents; }
        public long getExpiredEvents() { return expiredEvents; }
        public double getAverageMultiplier() { return averageMultiplier; }
        public double getCurrentMaxMultiplier() { return currentMaxMultiplier; }
        public int getEventActivations() { return eventActivations; }
        public int getEventDeactivations() { return eventDeactivations; }
        
        public boolean hasActiveEvents() {
            return activeEvents > 0;
        }
        
        public double getActivePercentage() {
            return totalEvents > 0 ? (double) activeEvents / totalEvents * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("CalendarStats{total=%d, active=%d (%.1f%%), upcoming=%d, currentMultiplier=%.1f}", 
                totalEvents, activeEvents, getActivePercentage(), upcomingEvents, currentMaxMultiplier);
        }
    }
}