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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Утилиты для панели игрока
 * @author YourName
 */
public final class PlayerPanelUtils {
    
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    // Приватный конструктор для утилитного класса
    private PlayerPanelUtils() {
        // Утилитный класс
    }
    
    /**
     * Форматирование чисел для отображения
     */
    public static String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        } else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return NUMBER_FORMAT.format(number);
        }
    }
    
    /**
     * Форматирование времени
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds <= 0) {
            return "0с";
        }
        
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        
        if (days > 0) {
            return days + "д " + (hours % 24) + "ч";
        } else if (hours > 0) {
            return hours + "ч " + (minutes % 60) + "м";
        } else if (minutes > 0) {
            return minutes + "м " + (seconds % 60) + "с";
        } else {
            return seconds + "с";
        }
    }
    
    /**
     * Форматирование даты
     */
    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }
    
    /**
     * Форматирование процентов
     */
    public static String formatPercent(double value) {
        return String.format("%.1f%%", value);
    }
    
    /**
     * Получить иконку для предмета по типу
     */
    public static String getItemIcon(String itemName) {
        String name = itemName.toLowerCase();
        
        if (name.contains("sword") || name.contains("blade")) {
            return "⚔️";
        } else if (name.contains("bow") || name.contains("crossbow")) {
            return "🏹";
        } else if (name.contains("staff") || name.contains("wand")) {
            return "🪄";
        } else if (name.contains("dagger") || name.contains("knife")) {
            return "🗡️";
        } else if (name.contains("hammer") || name.contains("mace")) {
            return "🔨";
        } else if (name.contains("armor") || name.contains("plate")) {
            return "🛡️";
        } else if (name.contains("helmet") || name.contains("hat")) {
            return "⛑️";
        } else if (name.contains("boots") || name.contains("shoes")) {
            return "👢";
        } else if (name.contains("gloves") || name.contains("gauntlets")) {
            return "🧤";
        } else if (name.contains("ring")) {
            return "💍";
        } else if (name.contains("earring") || name.contains("ear")) {
            return "💎";
        } else if (name.contains("necklace") || name.contains("neck")) {
            return "📿";
        } else if (name.contains("scroll")) {
            return "📜";
        } else if (name.contains("potion") || name.contains("elixir")) {
            return "🧪";
        } else if (name.contains("crystal") || name.contains("gem")) {
            return "💎";
        } else if (name.contains("book") || name.contains("tome")) {
            return "📖";
        } else if (name.contains("key")) {
            return "🗝️";
        } else if (name.contains("food") || name.contains("bread")) {
            return "🍞";
        } else {
            return "📦";
        }
    }
    
    /**
     * Получить иконку для скилла по типу
     */
    public static String getSkillIcon(String skillName) {
        String name = skillName.toLowerCase();
        
        if (name.contains("heal") || name.contains("cure")) {
            return "❤️‍🩹";
        } else if (name.contains("shield") || name.contains("guard")) {
            return "🛡️";
        } else if (name.contains("might") || name.contains("power")) {
            return "💪";
        } else if (name.contains("magic") || name.contains("spell")) {
            return "🔮";
        } else if (name.contains("haste") || name.contains("speed")) {
            return "⚡";
        } else if (name.contains("wind") || name.contains("air")) {
            return "🌪️";
        } else if (name.contains("fire") || name.contains("flame")) {
            return "🔥";
        } else if (name.contains("ice") || name.contains("frost")) {
            return "❄️";
        } else if (name.contains("death") || name.contains("dark")) {
            return "💀";
        } else if (name.contains("light") || name.contains("divine")) {
            return "✨";
        } else if (name.contains("focus") || name.contains("concentration")) {
            return "🎯";
        } else if (name.contains("guidance") || name.contains("blessing")) {
            return "🌟";
        } else {
            return "📖";
        }
    }
    
    /**
     * Получить цвет для качества предмета
     */
    public static String getItemQualityColor(int enchantLevel) {
        if (enchantLevel >= 15) {
            return "#ff6b35"; // Оранжевый для высоких уровней
        } else if (enchantLevel >= 10) {
            return "#ffd700"; // Золотой
        } else if (enchantLevel >= 5) {
            return "#00ff00"; // Зеленый
        } else if (enchantLevel > 0) {
            return "#87ceeb"; // Светло-синий
        } else {
            return "#ffffff"; // Белый
        }
    }
    
    /**
     * Получить статус онлайн времени
     */
    public static String getOnlineTimeStatus(long onlineTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(onlineTime);
        
        if (hours >= 100) {
            return "🏆 Ветеран";
        } else if (hours >= 50) {
            return "⭐ Активный";
        } else if (hours >= 20) {
            return "👍 Регулярный";
        } else if (hours >= 5) {
            return "🌱 Новичок";
        } else {
            return "👶 Начинающий";
        }
    }
    
    /**
     * Получить ранг PvP
     */
    public static String getPvPRank(int pvpKills) {
        if (pvpKills >= 1000) {
            return "👑 Легенда";
        } else if (pvpKills >= 500) {
            return "🏆 Мастер";
        } else if (pvpKills >= 200) {
            return "⚔️ Воин";
        } else if (pvpKills >= 50) {
            return "🗡️ Боец";
        } else if (pvpKills >= 10) {
            return "🛡️ Защитник";
        } else {
            return "🌱 Мирный";
        }
    }
    
    /**
     * Получить статус кармы
     */
    public static String getKarmaStatus(int karma) {
        if (karma <= 0) {
            return "😇 Праведный";
        } else if (karma <= 100) {
            return "😐 Нейтральный";
        } else if (karma <= 500) {
            return "😠 Агрессивный";
        } else {
            return "👹 Убийца";
        }
    }
    
    /**
     * Рассчитать рейтинг игрока
     */
    public static int calculatePlayerRating(L2PcInstance player) {
        int rating = 0;
        
        // Уровень персонажа
        rating += player.getLevel() * 10;
        
        // PvP убийства
        rating += player.getPvpKills() * 5;
        
        // Время онлайн
        long hoursOnline = TimeUnit.MILLISECONDS.toHours(player.getOnlineTime());
        rating += (int)(hoursOnline * 2);
        
        // Штраф за карму
        if (player.getKarma() > 0) {
            rating -= player.getKarma() / 10;
        }
        
        // Бонус за клан
        if (player.getClan() != null) {
            rating += player.getClan().getLevel() * 20;
        }
        
        // Бонус за статус героя/нобла
        if (player.isHero()) {
            rating += 1000;
        }
        if (player.isNoble()) {
            rating += 500;
        }
        
        return Math.max(0, rating);
    }
    
    /**
     * Проверить, является ли игрок новичком
     */
    public static boolean isNewbie(L2PcInstance player) {
        return player.getLevel() < 40 || 
               TimeUnit.MILLISECONDS.toHours(player.getOnlineTime()) < 10;
    }
    
    /**
     * Проверить, является ли игрок ветераном
     */
    public static boolean isVeteran(L2PcInstance player) {
        return player.getLevel() >= 76 && 
               TimeUnit.MILLISECONDS.toHours(player.getOnlineTime()) >= 100;
    }
    
    /**
     * Получить случайное приветствие для игрока
     */
    public static String getRandomGreeting(L2PcInstance player) {
        String[] greetings = {
            "Добро пожаловать, " + player.getName() + "!",
            "Привет, " + player.getName() + "! Готовы к приключениям?",
            "Отличная игра, " + player.getName() + "!",
            "С возвращением, " + player.getName() + "!",
            "Удачной охоты, " + player.getName() + "!"
        };
        
        int index = (int)(System.currentTimeMillis() % greetings.length);
        return greetings[index];
    }
    
    /**
     * Получить совет дня для игрока
     */
    public static String getTipOfTheDay() {
        String[] tips = {
            "💡 Регулярно сохраняйте важные предметы в варехаусе!",
            "💡 Присоединитесь к клану для получения дополнительных бонусов!",
            "💡 Используйте свитки благословения для безопасной заточки!",
            "💡 Участвуйте в событиях сервера для получения наград!",
            "💡 Изучайте новые умения у НПЦ-учителей в городах!",
            "💡 Торгуйтесь с другими игроками для выгодных сделок!",
            "💡 Исследуйте новые локации для поиска редких предметов!",
            "💡 Объединяйтесь с друзьями для эффективной охоты!"
        };
        
        int dayOfYear = (int)(System.currentTimeMillis() / (24 * 60 * 60 * 1000)) % tips.length;
        return tips[dayOfYear];
    }
    
    /**
     * Безопасное парсинг числа
     */
    public static int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Безопасное парсинг long
     */
    public static long safeParseLong(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Проверка валидности email
     */
    public static boolean isValidEmail(String email) {
        return email != null && 
               email.contains("@") && 
               email.contains(".") && 
               email.length() > 5;
    }
    
    /**
     * Экранирование HTML символов
     */
    public static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .