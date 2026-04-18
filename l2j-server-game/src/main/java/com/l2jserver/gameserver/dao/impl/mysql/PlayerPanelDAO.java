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
package com.l2jserver.gameserver.dao.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * DAO класс для работы с базой данных панели игрока
 * @author YourName
 */
public class PlayerPanelDAO {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayerPanelDAO.class);
    
    // SQL запросы для статистики
    private static final String SELECT_PLAYER_STATS = 
        "SELECT * FROM player_panel_stats WHERE char_id = ?";
    
    private static final String INSERT_PLAYER_STATS = 
        "INSERT INTO player_panel_stats (char_id, first_used) VALUES (?, ?) " +
        "ON DUPLICATE KEY UPDATE last_used = ?";
    
    private static final String UPDATE_PLAYER_STATS = 
        "UPDATE player_panel_stats SET total_enchants = ?, successful_enchants = ?, " +
        "failed_enchants = ?, total_purchases = ?, total_spent = ?, buffs_received = ?, " +
        "last_used = ? WHERE char_id = ?";
    
    // SQL запросы для логирования
    private static final String INSERT_LOG = 
        "INSERT INTO player_panel_log (char_id, char_name, action, details, cost, success, timestamp, ip_address) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_ENCHANT_LOG = 
        "INSERT INTO player_panel_enchant_history (char_id, item_id, item_object_id, item_name, " +
        "enchant_before, enchant_after, success, scroll_id, scroll_name, cost, timestamp) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_PURCHASE_LOG = 
        "INSERT INTO player_panel_purchase_history (char_id, item_id, item_name, quantity, " +
        "price_per_item, total_price, currency_type, timestamp) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_BUFF_LOG = 
        "INSERT INTO player_panel_buff_history (char_id, skill_id, skill_name, skill_level, " +
        "cost, duration, timestamp) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    // SQL запросы для настроек
    private static final String SELECT_PLAYER_SETTINGS = 
        "SELECT * FROM player_panel_settings WHERE char_id = ?";
    
    private static final String INSERT_PLAYER_SETTINGS = 
        "INSERT INTO player_panel_settings (char_id) VALUES (?) " +
        "ON DUPLICATE KEY UPDATE char_id = char_id";
    
    private static final String UPDATE_PLAYER_SETTINGS = 
        "UPDATE player_panel_settings SET sound_enabled = ?, visual_effects_enabled = ?, " +
        "auto_buff_enabled = ?, notifications_enabled = ?, theme = ?, language = ?, " +
        "last_section = ? WHERE char_id = ?";
    
    /**
     * In-memory кэш кулдаунов: charId -> (actionType -> endTimeMs).
     * Кулдауны — эфемерное состояние (минуты); персистить их в БД не нужно,
     * а SELECT/INSERT на каждый клик BBS — недопустимая нагрузка в игровом потоке.
     */
    private static final Map<Integer, Map<String, Long>> COOLDOWNS = new ConcurrentHashMap<>();
    
    // SQL запросы для топов
    private static final String SELECT_TOP_ENCHANTERS = 
        "SELECT c.char_name, ps.successful_enchants, ps.total_enchants " +
        "FROM player_panel_stats ps " +
        "JOIN characters c ON ps.char_id = c.charId " +
        "WHERE ps.total_enchants > 0 " +
        "ORDER BY ps.successful_enchants DESC LIMIT ?";
    
    private static final String SELECT_TOP_SPENDERS = 
        "SELECT c.char_name, ps.total_spent, ps.total_purchases " +
        "FROM player_panel_stats ps " +
        "JOIN characters c ON ps.char_id = c.charId " +
        "WHERE ps.total_purchases > 0 " +
        "ORDER BY ps.total_spent DESC LIMIT ?";
    
    /**
     * Класс для хранения статистики игрока
     */
    public static class PlayerPanelStats {
        public int charId;
        public int totalEnchants;
        public int successfulEnchants;
        public int failedEnchants;
        public int totalPurchases;
        public long totalSpent;
        public int buffsReceived;
        public long lastUsed;
        public long firstUsed;
        
        public double getSuccessRate() {
            return totalEnchants > 0 ? (double) successfulEnchants / totalEnchants * 100 : 0;
        }
    }
    
    /**
     * Класс для хранения настроек игрока
     */
    public static class PlayerPanelSettings {
        public int charId;
        public boolean soundEnabled = true;
        public boolean visualEffectsEnabled = true;
        public boolean autoBuffEnabled = false;
        public boolean notificationsEnabled = true;
        public String theme = "dark";
        public String language = "ru";
        public String lastSection = "enchant";
    }
    
    /**
     * Получить статистику игрока
     */
    public static PlayerPanelStats getPlayerStats(int charId) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_PLAYER_STATS)) {
            
            ps.setInt(1, charId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PlayerPanelStats stats = new PlayerPanelStats();
                    stats.charId = rs.getInt("char_id");
                    stats.totalEnchants = rs.getInt("total_enchants");
                    stats.successfulEnchants = rs.getInt("successful_enchants");
                    stats.failedEnchants = rs.getInt("failed_enchants");
                    stats.totalPurchases = rs.getInt("total_purchases");
                    stats.totalSpent = rs.getLong("total_spent");
                    stats.buffsReceived = rs.getInt("buffs_received");
                    stats.lastUsed = rs.getLong("last_used");
                    stats.firstUsed = rs.getLong("first_used");
                    return stats;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error getting player panel stats for charId: {}", charId, e);
        }
        
        // Создать новую запись, если не найдена
        PlayerPanelStats stats = new PlayerPanelStats();
        stats.charId = charId;
        stats.firstUsed = System.currentTimeMillis();
        createPlayerStats(stats);
        return stats;
    }
    
    /**
     * Создать статистику игрока
     */
    public static boolean createPlayerStats(PlayerPanelStats stats) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_PLAYER_STATS)) {
            
            ps.setInt(1, stats.charId);
            ps.setLong(2, stats.firstUsed);
            ps.setLong(3, System.currentTimeMillis());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Error creating player panel stats for charId: {}", stats.charId, e);
            return false;
        }
    }
    
    /**
     * Обновить статистику игрока
     */
    public static boolean updatePlayerStats(PlayerPanelStats stats) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_PLAYER_STATS)) {
            
            ps.setInt(1, stats.totalEnchants);
            ps.setInt(2, stats.successfulEnchants);
            ps.setInt(3, stats.failedEnchants);
            ps.setInt(4, stats.totalPurchases);
            ps.setLong(5, stats.totalSpent);
            ps.setInt(6, stats.buffsReceived);
            ps.setLong(7, System.currentTimeMillis());
            ps.setInt(8, stats.charId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Error updating player panel stats for charId: {}", stats.charId, e);
            return false;
        }
    }
    
    // =========================================================================
    // Логирование.
    //
    // Все log* методы — fire-and-forget: снимаем данные с L2PcInstance на
    // caller-потоке (он — игровой поток), а I/O с БД отдаём в ThreadPoolManager.
    // Таким образом клик по BBS не ждёт сетевой round-trip к MySQL.
    // =========================================================================

    /**
     * Логировать действие в панели (асинхронно).
     */
    public static void logAction(L2PcInstance player, String action, String details, long cost, boolean success) {
        final int objectId = player.getObjectId();
        final String name = player.getName();
        final String ip = (player.getClient() != null && player.getClient().getConnectionAddress() != null)
            ? player.getClient().getConnectionAddress().getHostAddress() : "unknown";
        final long timestamp = System.currentTimeMillis();

        ThreadPoolManager.getInstance().executeGeneral(() -> {
            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(INSERT_LOG)) {
                ps.setInt(1, objectId);
                ps.setString(2, name);
                ps.setString(3, action);
                ps.setString(4, details);
                ps.setLong(5, cost);
                ps.setBoolean(6, success);
                ps.setLong(7, timestamp);
                ps.setString(8, ip);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Error logging panel action for player: {}", name, e);
            }
        });
    }

    /**
     * Логировать заточку (асинхронно).
     */
    public static void logEnchant(L2PcInstance player, int itemId, int itemObjectId, String itemName,
                                   int enchantBefore, int enchantAfter, boolean success,
                                   int scrollId, String scrollName, long cost) {
        final int objectId = player.getObjectId();
        final String playerName = player.getName();
        final long timestamp = System.currentTimeMillis();

        ThreadPoolManager.getInstance().executeGeneral(() -> {
            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(INSERT_ENCHANT_LOG)) {
                ps.setInt(1, objectId);
                ps.setInt(2, itemId);
                ps.setInt(3, itemObjectId);
                ps.setString(4, itemName);
                ps.setInt(5, enchantBefore);
                ps.setInt(6, enchantAfter);
                ps.setBoolean(7, success);
                ps.setInt(8, scrollId);
                ps.setString(9, scrollName);
                ps.setLong(10, cost);
                ps.setLong(11, timestamp);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Error logging enchant for player: {}", playerName, e);
            }
        });
    }

    /**
     * Логировать покупку (асинхронно).
     */
    public static void logPurchase(L2PcInstance player, int itemId, String itemName,
                                    int quantity, long pricePerItem, long totalPrice, String currencyType) {
        final int objectId = player.getObjectId();
        final String playerName = player.getName();
        final long timestamp = System.currentTimeMillis();

        ThreadPoolManager.getInstance().executeGeneral(() -> {
            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(INSERT_PURCHASE_LOG)) {
                ps.setInt(1, objectId);
                ps.setInt(2, itemId);
                ps.setString(3, itemName);
                ps.setInt(4, quantity);
                ps.setLong(5, pricePerItem);
                ps.setLong(6, totalPrice);
                ps.setString(7, currencyType);
                ps.setLong(8, timestamp);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Error logging purchase for player: {}", playerName, e);
            }
        });
    }

    /**
     * Логировать получение бафа (асинхронно).
     */
    public static void logBuff(L2PcInstance player, int skillId, String skillName,
                                int skillLevel, long cost, int duration) {
        final int objectId = player.getObjectId();
        final String playerName = player.getName();
        final long timestamp = System.currentTimeMillis();

        ThreadPoolManager.getInstance().executeGeneral(() -> {
            try (Connection con = ConnectionFactory.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(INSERT_BUFF_LOG)) {
                ps.setInt(1, objectId);
                ps.setInt(2, skillId);
                ps.setString(3, skillName);
                ps.setInt(4, skillLevel);
                ps.setLong(5, cost);
                ps.setInt(6, duration);
                ps.setLong(7, timestamp);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Error logging buff for player: {}", playerName, e);
            }
        });
    }
    
    /**
     * Получить настройки игрока
     */
    public static PlayerPanelSettings getPlayerSettings(int charId) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_PLAYER_SETTINGS)) {
            
            ps.setInt(1, charId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PlayerPanelSettings settings = new PlayerPanelSettings();
                    settings.charId = rs.getInt("char_id");
                    settings.soundEnabled = rs.getBoolean("sound_enabled");
                    settings.visualEffectsEnabled = rs.getBoolean("visual_effects_enabled");
                    settings.autoBuffEnabled = rs.getBoolean("auto_buff_enabled");
                    settings.notificationsEnabled = rs.getBoolean("notifications_enabled");
                    settings.theme = rs.getString("theme");
                    settings.language = rs.getString("language");
                    settings.lastSection = rs.getString("last_section");
                    return settings;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error getting player panel settings for charId: {}", charId, e);
        }
        
        // Создать настройки по умолчанию
        PlayerPanelSettings settings = new PlayerPanelSettings();
        settings.charId = charId;
        createPlayerSettings(settings);
        return settings;
    }
    
    /**
     * Создать настройки игрока
     */
    public static boolean createPlayerSettings(PlayerPanelSettings settings) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_PLAYER_SETTINGS)) {
            
            ps.setInt(1, settings.charId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Error creating player panel settings for charId: {}", settings.charId, e);
            return false;
        }
    }
    
    /**
     * Обновить настройки игрока
     */
    public static boolean updatePlayerSettings(PlayerPanelSettings settings) {
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_PLAYER_SETTINGS)) {
            
            ps.setBoolean(1, settings.soundEnabled);
            ps.setBoolean(2, settings.visualEffectsEnabled);
            ps.setBoolean(3, settings.autoBuffEnabled);
            ps.setBoolean(4, settings.notificationsEnabled);
            ps.setString(5, settings.theme);
            ps.setString(6, settings.language);
            ps.setString(7, settings.lastSection);
            ps.setInt(8, settings.charId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Error updating player panel settings for charId: {}", settings.charId, e);
            return false;
        }
    }
    
    /**
     * Проверить кулдаун (in-memory, lock-free).
     */
    public static boolean hasCooldown(int charId, String actionType) {
        Map<String, Long> perChar = COOLDOWNS.get(charId);
        if (perChar == null) {
            return false;
        }
        Long end = perChar.get(actionType);
        if (end == null) {
            return false;
        }
        if (System.currentTimeMillis() < end) {
            return true;
        }
        perChar.remove(actionType);
        return false;
    }

    /**
     * Установить кулдаун (in-memory).
     */
    public static boolean setCooldown(int charId, String actionType, long durationMs) {
        long end = System.currentTimeMillis() + durationMs;
        COOLDOWNS.computeIfAbsent(charId, k -> new ConcurrentHashMap<>()).put(actionType, end);
        return true;
    }

    /**
     * Очистка в памяти. Вызывается периодически / при logout.
     */
    public static void cleanExpiredCooldowns() {
        long now = System.currentTimeMillis();
        COOLDOWNS.values().forEach(perChar -> perChar.values().removeIf(end -> end <= now));
        COOLDOWNS.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    /**
     * Удалить все кулдауны игрока (например, при logout).
     */
    public static void clearPlayerCooldowns(int charId) {
        COOLDOWNS.remove(charId);
    }
    
    /**
     * Получить топ заточников
     */
    public static List<Map<String, Object>> getTopEnchanters(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_TOP_ENCHANTERS)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", rs.getString("char_name"));
                    row.put("successful", rs.getInt("successful_enchants"));
                    row.put("total", rs.getInt("total_enchants"));
                    row.put("rate", rs.getInt("total_enchants") > 0 ? 
                        (double) rs.getInt("successful_enchants") / rs.getInt("total_enchants") * 100 : 0);
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            LOG.error("Error getting top enchanters", e);
        }
        
        return result;
    }
    
    /**
     * Получить топ покупателей
     */
    public static List<Map<String, Object>> getTopSpenders(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_TOP_SPENDERS)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", rs.getString("char_name"));
                    row.put("spent", rs.getLong("total_spent"));
                    row.put("purchases", rs.getInt("total_purchases"));
                    row.put("average", rs.getInt("total_purchases") > 0 ? 
                        rs.getLong("total_spent") / rs.getInt("total_purchases") : 0);
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            LOG.error("Error getting top spenders", e);
        }
        
        return result;
    }
    
    /**
     * Получить статистику сервера одним запросом (было: 6 round-trip'ов к БД).
     */
    public static Map<String, Object> getServerStats() {
        Map<String, Object> stats = new HashMap<>();

        String query = "SELECT "
            + "COUNT(*) AS total_users, "
            + "SUM(total_enchants) AS total_enchants, "
            + "SUM(successful_enchants) AS successful_enchants, "
            + "SUM(total_purchases) AS total_purchases, "
            + "SUM(total_spent) AS total_spent, "
            + "SUM(buffs_received) AS total_buffs "
            + "FROM player_panel_stats";

        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("total_users", rs.getLong("total_users"));
                stats.put("total_enchants", rs.getLong("total_enchants"));
                stats.put("successful_enchants", rs.getLong("successful_enchants"));
                stats.put("total_purchases", rs.getLong("total_purchases"));
                stats.put("total_spent", rs.getLong("total_spent"));
                stats.put("total_buffs", rs.getLong("total_buffs"));
            }
        } catch (SQLException e) {
            LOG.error("Error getting server stats", e);
        }

        return stats;
    }
    
    /**
     * Получить статистику за период
     */
    public static Map<String, Object> getStatsForPeriod(long startTime, long endTime) {
        Map<String, Object> stats = new HashMap<>();
        
        String query = "SELECT COUNT(*) as count, SUM(cost) as total_cost FROM player_panel_log " +
                      "WHERE timestamp BETWEEN ? AND ?";
        
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setLong(1, startTime);
            ps.setLong(2, endTime);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("actions", rs.getInt("count"));
                    stats.put("total_cost", rs.getLong("total_cost"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error getting stats for period", e);
        }
        
        return stats;
    }
}