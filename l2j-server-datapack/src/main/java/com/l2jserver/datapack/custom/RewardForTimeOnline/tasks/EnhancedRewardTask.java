package com.l2jserver.datapack.custom.RewardForTimeOnline.tasks;

import com.l2jserver.datapack.custom.RewardForTimeOnline.AdvancedRewardSystem;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.ItemReward;
import com.l2jserver.datapack.custom.RewardForTimeOnline.models.PlayerHolder;
import com.l2jserver.datapack.custom.RewardForTimeOnline.utils.ItemUtils;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Улучшенная задача награждения игрока
 * Обеспечивает надежную выдачу наград с учетом всех систем
 * @author Dafna
 */
public class EnhancedRewardTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(EnhancedRewardTask.class);
    
    // Основные компоненты
    private final PlayerHolder playerHolder;
    private final ItemReward reward;
    private final String groupName;
    private final AdvancedRewardSystem system;
    
    // Управление выполнением
    private volatile ScheduledFuture<?> task;
    private volatile long lastExecutionTime;
    private volatile long nextExecutionTime;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicBoolean isValid = new AtomicBoolean(true);
    private final AtomicBoolean isPaused = new AtomicBoolean(false);
    
    // Статистика выполнения
    private final AtomicLong executionCount = new AtomicLong(0);
    private final AtomicLong successfulExecutions = new AtomicLong(0);
    private final AtomicLong failedExecutions = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final long creationTime;
    
    // Константы
    private static final long MAX_EXECUTION_TIME = TimeUnit.SECONDS.toMillis(30); // 30 секунд максимум
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY = TimeUnit.SECONDS.toMillis(5);
    
    public EnhancedRewardTask(PlayerHolder playerHolder, ItemReward reward, 
                             String groupName, AdvancedRewardSystem system) {
        this.playerHolder = playerHolder;
        this.reward = reward;
        this.groupName = groupName;
        this.system = system;
        this.lastExecutionTime = System.currentTimeMillis();
        this.creationTime = System.currentTimeMillis();
        
        // Вычисляем начальную задержку
        long initialDelay = calculateInitialDelay();
        
        if (initialDelay >= 0) {
            scheduleTask(initialDelay);
            LOG.debug("Created reward task for player {} (item: {}, group: {}, delay: {}ms)", 
                playerHolder.getPlayer().getName(), reward.getItemId(), groupName, initialDelay);
        } else {
            isValid.set(false);
            LOG.debug("Reward task not created for player {} - already received or invalid",
                playerHolder.getPlayer().getName());
        }
    }
    
    /**
     * Планирует выполнение задачи
     */
    private void scheduleTask(long initialDelay) {
        try {
            this.task = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(
                this, initialDelay, reward.getTimeInterval());
            this.nextExecutionTime = System.currentTimeMillis() + initialDelay;
            
        } catch (Exception e) {
            LOG.error("Failed to schedule reward task: {}", e.getMessage());
            isValid.set(false);
        }
    }
    
    /**
     * Вычисляет начальную задержку для награды
     */
    private long calculateInitialDelay() {
        L2PcInstance player = playerHolder.getPlayer();
        
        if (player == null) {
            LOG.warn("Player is null during task creation");
            return -1;
        }
        
        // Проверяем одноразовые награды
        if (reward.isOnceOnly() && player.getVariables().getBoolean(getGivenVar(), false)) {
            LOG.debug("Player {} already received once-only reward {}", 
                player.getName(), reward.getItemId());
            return -1;
        }
        
        // Для сохраняемых в БД наград проверяем сохраненное время
        if (reward.isSaveToDatabase()) {
            long savedTime = player.getVariables().getLong(getTimeVar(), reward.getTimeInterval());
            long calculatedDelay = Math.max(0, Math.min(savedTime, reward.getTimeInterval()));
            
            LOG.debug("Calculated initial delay for player {} item {}: {}ms (saved: {}ms)", 
                player.getName(), reward.getItemId(), calculatedDelay, savedTime);
            
            return calculatedDelay;
        }
        
        return reward.getTimeInterval();
    }
    
    @Override
    public void run() {
        if (system.isShuttingDown() || !isRunning.compareAndSet(false, true)) {
            return;
        }
        
        long startTime = System.currentTimeMillis();
        executionCount.incrementAndGet();
        
        try {
            // Проверяем паузу
            if (isPaused.get()) {
                LOG.trace("Task paused for player {}, skipping execution", 
                    playerHolder.getPlayer().getName());
                return;
            }
            
            // Основная логика выполнения
            boolean success = executeRewardWithRetry();
            
            if (success) {
                successfulExecutions.incrementAndGet();
            } else {
                failedExecutions.incrementAndGet();
            }
            
            // Обновляем время выполнения
            long executionTime = System.currentTimeMillis() - startTime;
            totalExecutionTime.addAndGet(executionTime);
            lastExecutionTime = System.currentTimeMillis();
            nextExecutionTime = lastExecutionTime + reward.getTimeInterval();
            
            // Предупреждение о долгом выполнении
            if (executionTime > MAX_EXECUTION_TIME) {
                LOG.warn("Reward task for player {} took unusually long: {}ms", 
                    playerHolder.getPlayer().getName(), executionTime);
            }
            
        } catch (Exception e) {
            failedExecutions.incrementAndGet();
            LOG.error("Critical error in reward task for player {} and item {}: {}", 
                playerHolder.getPlayer().getName(), reward.getItemId(), e.getMessage(), e);
        } finally {
            isRunning.set(false);
        }
    }
    
    /**
     * Выполняет награждение с повторными попытками
     */
    private boolean executeRewardWithRetry() {
        int attempts = 0;
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                if (executeReward()) {
                    return true;
                }
                attempts++;
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    LOG.debug("Reward execution failed for player {}, attempt {}/{}", 
                        playerHolder.getPlayer().getName(), attempts, MAX_RETRY_ATTEMPTS);
                    
                    // Небольшая пауза перед повтором
                    Thread.sleep(RETRY_DELAY);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.warn("Reward task interrupted for player {}", playerHolder.getPlayer().getName());
                return false;
            } catch (Exception e) {
                LOG.warn("Error during reward execution attempt {} for player {}: {}", 
                    attempts + 1, playerHolder.getPlayer().getName(), e.getMessage());
                attempts++;
            }
        }
        
        LOG.error("All retry attempts failed for player {} and item {}", 
            playerHolder.getPlayer().getName(), reward.getItemId());
        return false;
    }
    
    /**
     * Выполняет награждение игрока
     */
    private boolean executeReward() {
        L2PcInstance player = playerHolder.getPlayer();
        
        // Базовые проверки
        if (!isPlayerValid(player)) {
            LOG.debug("Player validation failed for {}", player != null ? player.getName() : "null");
            return false;
        }
        
        if (!playerHolder.areRewardsAvailable()) {
            LOG.trace("Rewards not available for player {}", player.getName());
            return false;
        }
        
        // Проверка AFK
        if (system.getAntiAFK().isPlayerAFK(player)) {
            LOG.trace("Player {} is AFK, skipping reward", player.getName());
            return false;
        }
        
        // Проверка одноразовых наград
        if (reward.isOnceOnly() && player.getVariables().getBoolean(getGivenVar(), false)) {
            LOG.debug("Player {} already received once-only reward {}, stopping task", 
                player.getName(), reward.getItemId());
            stopTask();
            return true; // Не ошибка, просто завершаем
        }
        
        // Проверка доступности награды по времени
        if (!reward.isAvailableNow()) {
            LOG.trace("Reward {} not available now for player {}", 
                reward.getItemId(), player.getName());
            return false;
        }
        
        // Проверка возможности получения награды игроком
        if (!reward.canPlayerReceive(player)) {
            LOG.debug("Player {} cannot receive reward {} (level/day restrictions)", 
                player.getName(), reward.getItemId());
            return false;
        }
        
        // Проверяем существование предмета
        if (!system.getItemDataWrapper().itemExists(reward.getItemId())) {
            LOG.error("Reward item {} does not exist, stopping task for player {}", 
                reward.getItemId(), player.getName());
            stopTask();
            return false;
        }
        
        // Выдаем награду
        return giveReward(player);
    }
    
    /**
     * Проверяет валидность игрока
     */
    private boolean isPlayerValid(L2PcInstance player) {
        return player != null && 
               player.getClient() != null && 
               !player.getClient().isDetached() && 
               player.isOnline() &&
               playerHolder.isActive();
    }
    
    /**
     * Выдает награду игроку
     */
    private boolean giveReward(L2PcInstance player) {
        try {
            long finalCount = calculateFinalRewardCount(player);
            
            // Проверяем место в инвентаре
            if (!player.getInventory().validateCapacityByItemId(reward.getItemId())) {
                sendNotification(player, "Inventory full! Reward will be given later.");
                return false; // Не критическая ошибка, попробуем позже
            }
            
            // Проверяем лимит стака
            long stackLimit = system.getItemDataWrapper().getStackLimit(reward.getItemId());
            if (finalCount > stackLimit) {
                LOG.warn("Reward count {} exceeds stack limit {} for item {}, adjusting", 
                    finalCount, stackLimit, reward.getItemId());
                finalCount = stackLimit;
            }
            
            // Добавляем предмет
            player.addItem("AdvancedRewardSystem", reward.getItemId(), finalCount, null, true);
            
            // Обновляем прогрессивные множители
            if (reward.isProgressive()) {
                system.getProgressiveManager().incrementPlayerMultiplier(player, reward);
            }
            
            // Отправляем уведомление
            sendRewardNotification(player, finalCount);
            
            // Обновляем статистику
            updateStatistics(player, finalCount);
            
            // Сохраняем в базу данных
            saveRewardToDatabase(player, finalCount);
            
            // Обновляем состояние
            updateRewardState(player);
            
            LOG.debug("Successfully gave reward to {}: {} (group: {}, multiplier: {:.2f})", 
                player.getName(), 
                ItemUtils.getFormattedItemName(reward.getItemId(), finalCount),
                groupName, 
                system.getProgressiveManager().getPlayerMultiplier(player, reward));
            
            return true;
            
        } catch (Exception e) {
            LOG.error("Failed to give reward to player {}: {}", player.getName(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Вычисляет итоговое количество награды с учетом множителей
     */
    private long calculateFinalRewardCount(L2PcInstance player) {
        long baseCount = reward.getBaseCount();
        
        double progressiveMultiplier = system.getProgressiveManager().getPlayerMultiplier(player, reward);
        double eventMultiplier = system.getCalendarManager().getCurrentEventMultiplier();
        
        double finalMultiplier = progressiveMultiplier * eventMultiplier;
        long finalCount = Math.max(1, Math.round(baseCount * finalMultiplier));
        
        LOG.trace("Calculated reward count for player {}: base={}, progressive={:.2f}, event={:.2f}, final={}", 
            player.getName(), baseCount, progressiveMultiplier, eventMultiplier, finalCount);
        
        return finalCount;
    }
    
    /**
     * Отправляет уведомление о награде
     */
    private void sendRewardNotification(L2PcInstance player, long finalCount) {
        try {
            double progressiveMultiplier = system.getProgressiveManager().getPlayerMultiplier(player, reward);
            double eventMultiplier = system.getCalendarManager().getCurrentEventMultiplier();
            
            StringBuilder message = new StringBuilder();
            message.append("Received ").append(ItemUtils.getFormattedItemName(reward.getItemId(), finalCount));
            
            // Добавляем информацию о множителях
            if (progressiveMultiplier > 1.0) {
                message.append(String.format(" (Progressive: %.1fx)", progressiveMultiplier));
            }
            
            if (eventMultiplier > 1.0) {
                String eventName = getActiveEventName();
                message.append(String.format(" [%s: %.1fx]", eventName, eventMultiplier));
            }
            
            // Добавляем информацию о группе
            if (!groupName.equals("EVENT")) {
                message.append(String.format(" [%s]", groupName));
            }
            
            // Отправляем системное сообщение
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1);
            sm.addString("[Online Reward] " + message.toString());
            player.sendPacket(sm);
            
        } catch (Exception e) {
            LOG.debug("Failed to send reward notification to player {}: {}", 
                player.getName(), e.getMessage());
        }
    }
    
    /**
     * Отправляет простое уведомление
     */
    private void sendNotification(L2PcInstance player, String message) {
        try {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1);
            sm.addString("[Online Reward] " + message);
            player.sendPacket(sm);
        } catch (Exception e) {
            LOG.debug("Failed to send notification to player {}: {}", 
                player.getName(), e.getMessage());
        }
    }
    
    /**
     * Обновляет статистику
     */
    private void updateStatistics(L2PcInstance player, long finalCount) {
        try {
            // Обновляем общую статистику системы
            system.getStatistics().incrementRewards(groupName, reward.getItemId(), finalCount, 
                totalExecutionTime.get() / Math.max(1, executionCount.get()));
            
            // Обновляем статистику игрока
            system.getStatistics().recordPlayerReward(player.getObjectId(), player.getName(), 
                reward.getItemId(), finalCount);
            
            // Обновляем счетчик игрока
            playerHolder.incrementRewardsReceived();
            
        } catch (Exception e) {
            LOG.warn("Failed to update statistics for player {}: {}", player.getName(), e.getMessage());
        }
    }
    
    /**
     * Сохраняет награду в базу данных
     */
    private void saveRewardToDatabase(L2PcInstance player, long finalCount) {
        try {
            double progressiveMultiplier = system.getProgressiveManager().getPlayerMultiplier(player, reward);
            String eventName = getActiveEventName();
            
            system.getDatabase().saveStatistics(player.getObjectId(), player.getName(), 
                groupName, reward.getItemId(), finalCount, progressiveMultiplier, eventName);
                
        } catch (Exception e) {
            LOG.debug("Failed to save reward statistics to database: {}", e.getMessage());
            // Не критическая ошибка, продолжаем работу
        }
    }
    
    /**
     * Обновляет состояние награды
     */
    private void updateRewardState(L2PcInstance player) {
        // Обновляем время последнего выполнения
        lastExecutionTime = System.currentTimeMillis();
        
        // Сохраняем состояние для наград, требующих сохранения в БД
        if (reward.isSaveToDatabase()) {
            player.getVariables().set(getTimeVar(), reward.getTimeInterval());
        }
        
        // Для одноразовых наград помечаем как выданные и останавливаем задачу
        if (reward.isOnceOnly()) {
            player.getVariables().set(getGivenVar(), true);
            stopTask();
            LOG.debug("Marked once-only reward as given for player {} item {}", 
                player.getName(), reward.getItemId());
        }
    }
    
    /**
     * Получает имя активного события
     */
    private String getActiveEventName() {
        return system.getCalendarManager().getActiveEventNames().stream()
            .findFirst()
            .orElse("None");
    }
    
    /**
     * Приостанавливает выполнение задачи
     */
    public void pause() {
        isPaused.set(true);
        LOG.debug("Paused reward task for player {}", playerHolder.getPlayer().getName());
    }
    
    /**
     * Возобновляет выполнение задачи
     */
    public void resume() {
        if (isPaused.compareAndSet(true, false)) {
            LOG.debug("Resumed reward task for player {}", playerHolder.getPlayer().getName());
        }
    }
    
    /**
     * Обработка выхода игрока
     */
    public void onPlayerLogout() {
        if (reward.isSaveToDatabase() && !reward.isOnceOnly()) {
            L2PcInstance player = playerHolder.getPlayer();
            if (player != null) {
                long elapsedTime = System.currentTimeMillis() - lastExecutionTime;
                long remainingTime = Math.max(0, reward.getTimeInterval() - elapsedTime);
                
                if (remainingTime > 0) {
                    player.getVariables().set(getTimeVar(), remainingTime);
                    LOG.debug("Saved remaining time {} ms for player {} and reward {}", 
                        remainingTime, player.getName(), reward.getItemId());
                }
            }
        }
        
        stopTask();
    }
    
    /**
     * Останавливает задачу
     */
    public void stopTask() {
        isValid.set(false);
        
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
            task = null;
            LOG.debug("Stopped reward task for player {} item {}", 
                playerHolder.getPlayer().getName(), reward.getItemId());
        }
    }
    
    /**
     * Принудительно выполняет награждение (для административных команд)
     */
    public boolean forceExecute() {
        if (isRunning.get() || !isValid.get()) {
            return false;
        }
        
        LOG.info("Force executing reward task for player {} item {}", 
            playerHolder.getPlayer().getName(), reward.getItemId());
        
        try {
            return executeReward();
        } catch (Exception e) {
            LOG.error("Failed to force execute reward task: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверяет валидность задачи
     */
    public boolean isValid() {
        return isValid.get() && task != null && !task.isCancelled();
    }
    
    /**
     * Проверяет активность задачи
     */
    public boolean isActive() {
        return isValid() && !isPaused.get();
    }
    
    /**
     * Получает переменную для времени
     */
    private String getTimeVar() {
        return "reward_time_" + groupName + "_" + reward.getItemId();
    }
    
    /**
     * Получает переменную для отметки о выдаче
     */
    private String getGivenVar() {
        return "reward_given_" + groupName + "_" + reward.getItemId();
    }
    
    /**
     * Получает информацию о задаче
     */
    public TaskInfo getTaskInfo() {
        long timeUntilNext = 0;
        if (isValid() && nextExecutionTime > 0) {
            timeUntilNext = Math.max(0, nextExecutionTime - System.currentTimeMillis());
        }
        
        double successRate = executionCount.get() > 0 ? 
            (double) successfulExecutions.get() / executionCount.get() * 100 : 100.0;
        
        double avgExecutionTime = executionCount.get() > 0 ? 
            (double) totalExecutionTime.get() / executionCount.get() : 0.0;
        
        return new TaskInfo(
            reward.getItemId(),
            groupName,
            reward.getFormattedInterval(),
            timeUntilNext,
            isValid.get(),
            isRunning.get(),
            isPaused.get(),
            executionCount.get(),
            successfulExecutions.get(),
            failedExecutions.get(),
            successRate,
            avgExecutionTime,
            System.currentTimeMillis() - creationTime
        );
    }
    
    /**
     * Информация о задаче награждения
     */
    public static class TaskInfo {
        private final int itemId;
        private final String groupName;
        private final String interval;
        private final long timeUntilNext;
        private final boolean isValid;
        private final boolean isRunning;
        private final boolean isPaused;
        private final long executionCount;
        private final long successfulExecutions;
        private final long failedExecutions;
        private final double successRate;
        private final double avgExecutionTime;
        private final long taskAge;
        
        public TaskInfo(int itemId, String groupName, String interval, 
                       long timeUntilNext, boolean isValid, boolean isRunning, boolean isPaused,
                       long executionCount, long successfulExecutions, long failedExecutions,
                       double successRate, double avgExecutionTime, long taskAge) {
            this.itemId = itemId;
            this.groupName = groupName;
            this.interval = interval;
            this.timeUntilNext = timeUntilNext;
            this.isValid = isValid;
            this.isRunning = isRunning;
            this.isPaused = isPaused;
            this.executionCount = executionCount;
            this.successfulExecutions = successfulExecutions;
            this.failedExecutions = failedExecutions;
            this.successRate = successRate;
            this.avgExecutionTime = avgExecutionTime;
            this.taskAge = taskAge;
        }
        
        // Геттеры
        public int getItemId() { return itemId; }
        public String getGroupName() { return groupName; }
        public String getInterval() { return interval; }
        public long getTimeUntilNext() { return timeUntilNext; }
        public boolean isValid() { return isValid; }
        public boolean isRunning() { return isRunning; }
        public boolean isPaused() { return isPaused; }
        public long getExecutionCount() { return executionCount; }
        public long getSuccessfulExecutions() { return successfulExecutions; }
        public long getFailedExecutions() { return failedExecutions; }
        public double getSuccessRate() { return successRate; }
        public double getAvgExecutionTime() { return avgExecutionTime; }
        public long getTaskAge() { return taskAge; }
        
        public String getStatus() {
            if (!isValid) return "STOPPED";
            if (isPaused) return "PAUSED";
            if (isRunning) return "RUNNING";
            return "SCHEDULED";
        }
        
        public String getFormattedTimeUntilNext() {
            if (timeUntilNext <= 0) return "Ready";
            
            long minutes = timeUntilNext / (1000 * 60);
            long seconds = (timeUntilNext / 1000) % 60;
            
            if (minutes > 0) {
                return String.format("%dm %ds", minutes, seconds);
            } else {
                return String.format("%ds", seconds);
            }
        }
        
        public String getFormattedTaskAge() {
            long hours = TimeUnit.MILLISECONDS.toHours(taskAge);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(taskAge) % 60;
            
            if (hours > 0) {
                return String.format("%dh %dm", hours, minutes);
            } else {
                return String.format("%dm", minutes);
            }
        }
        
        public boolean isHealthy() {
            return isValid && successRate > 80.0 && avgExecutionTime < 5000; // 5 секунд
        }
        
        @Override
        public String toString() {
            return String.format("TaskInfo{item=%d, group='%s', status='%s', next=%s, success=%.1f%%, healthy=%s}", 
                itemId, groupName, getStatus(), getFormattedTimeUntilNext(), successRate, isHealthy());
        }
    }
}