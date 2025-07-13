/*
 * Copyright © 2004-2023 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.handlers.voicedcommandhandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.gameserver.data.xml.impl.SkillInfoData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * Расширенная система бафов для панели игрока
 * @author YourName
 */
public class BuffSystem {
    
    /**
     * Категории бафов
     */
    public enum BuffCategory {
        DEFENSE("defense", "🛡️ Защитные", "Бафы для защиты и выживания"),
        ATTACK("attack", "⚔️ Боевые", "Бафы для увеличения урона"),
        SPEED("speed", "🏃 Скорость", "Бафы скорости и подвижности"),
        MAGIC("magic", "🔮 Магические", "Бафы для магов"),
        SPECIAL("special", "✨ Особые", "Уникальные бафы"),
        CLAN("clan", "👥 Клановые", "Бафы для клана"),
        RESIST("resist", "🔰 Сопротивления", "Бафы сопротивления дебафам");
        
        private final String key;
        private final String displayName;
        private final String description;
        
        BuffCategory(String key, String displayName, String description) {
            this.key = key;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getKey() { return key; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Класс для представления бафа
     */
    public static class BuffInfo {
        private final SkillHolder skill;
        private final String name;
        private final String description;
        private final BuffCategory category;
        private final int cost;
        private final int requiredLevel;
        private final boolean isPremium;
        
        public BuffInfo(int skillId, int skillLevel, String name, String description, 
                       BuffCategory category, int cost, int requiredLevel, boolean isPremium) {
            this.skill = new SkillHolder(skillId, skillLevel);
            this.name = name;
            this.description = description;
            this.category = category;
            this.cost = cost;
            this.requiredLevel = requiredLevel;
            this.isPremium = isPremium;
        }
        
        public SkillHolder getSkill() { return skill; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BuffCategory getCategory() { return category; }
        public int getCost() { return cost; }
        public int getRequiredLevel() { return requiredLevel; }
        public boolean isPremium() { return isPremium; }
    }
    
    // Конфигурация всех доступных бафов
    private static final Map<BuffCategory, List<BuffInfo>> BUFF_CONFIG = new HashMap<>();
    
    static {
        initializeBuffs();
    }
    
    /**
     * Инициализация всех бафов
     */
    private static void initializeBuffs() {
        
        // === ЗАЩИТНЫЕ БАФЫ ===
        List<BuffInfo> defenseBuffs = new ArrayList<>();
        defenseBuffs.add(new BuffInfo(1040, 3, "Shield", "Увеличивает P.Def", BuffCategory.DEFENSE, 50000, 1, false));
        defenseBuffs.add(new BuffInfo(1045, 6, "Blessed Body", "Увеличивает максимум HP", BuffCategory.DEFENSE, 75000, 1, false));
        defenseBuffs.add(new BuffInfo(1048, 6, "Blessed Soul", "Увеличивает максимум MP", BuffCategory.DEFENSE, 75000, 1, false));
        defenseBuffs.add(new BuffInfo(1036, 2, "Magic Barrier", "Увеличивает M.Def", BuffCategory.DEFENSE, 60000, 1, false));
        defenseBuffs.add(new BuffInfo(1035, 4, "Mental Shield", "Защита от ментальных атак", BuffCategory.DEFENSE, 40000, 1, false));
        defenseBuffs.add(new BuffInfo(1259, 4, "Resist Shock", "Сопротивление шоку", BuffCategory.DEFENSE, 80000, 25, false));
        defenseBuffs.add(new BuffInfo(1352, 1, "Elemental Protection", "Защита от стихий", BuffCategory.DEFENSE, 100000, 40, true));
        BUFF_CONFIG.put(BuffCategory.DEFENSE, defenseBuffs);
        
        // === БОЕВЫЕ БАФЫ ===
        List<BuffInfo> attackBuffs = new ArrayList<>();
        attackBuffs.add(new BuffInfo(1068, 3, "Might", "Увеличивает P.Atk", BuffCategory.ATTACK, 50000, 1, false));
        attackBuffs.add(new BuffInfo(1062, 2, "Berserker Spirit", "Увеличивает P.Atk и скорость атаки", BuffCategory.ATTACK, 70000, 1, false));
        attackBuffs.add(new BuffInfo(1077, 3, "Focus", "Увеличивает критический шанс", BuffCategory.ATTACK, 60000, 1, false));
        attackBuffs.add(new BuffInfo(1242, 3, "Death Whisper", "Увеличивает критический урон", BuffCategory.ATTACK, 80000, 20, false));
        attackBuffs.add(new BuffInfo(1388, 3, "Greater Might", "Сильно увеличивает P.Atk", BuffCategory.ATTACK, 150000, 52, true));
        attackBuffs.add(new BuffInfo(1087, 2, "Agility", "Увеличивает точность", BuffCategory.ATTACK, 45000, 1, false));
        attackBuffs.add(new BuffInfo(1397, 3, "Clarity", "Увеличивает критический шанс магии", BuffCategory.ATTACK, 90000, 30, false));
        BUFF_CONFIG.put(BuffCategory.ATTACK, attackBuffs);
        
        // === БАФЫ СКОРОСТИ ===
        List<BuffInfo> speedBuffs = new ArrayList<>();
        speedBuffs.add(new BuffInfo(1086, 2, "Haste", "Увеличивает скорость атаки", BuffCategory.SPEED, 40000, 1, false));
        speedBuffs.add(new BuffInfo(1204, 2, "Wind Walk", "Увеличивает скорость передвижения", BuffCategory.SPEED, 60000, 1, false));
        speedBuffs.add(new BuffInfo(1007, 3, "Acrobatics", "Увеличивает скорость и ловкость", BuffCategory.SPEED, 80000, 20, false));
        speedBuffs.add(new BuffInfo(1389, 3, "Greater Shield", "Улучшенная защита", BuffCategory.SPEED, 120000, 52, true));
        BUFF_CONFIG.put(BuffCategory.SPEED, speedBuffs);
        
        // === МАГИЧЕСКИЕ БАФЫ ===
        List<BuffInfo> magicBuffs = new ArrayList<>();
        magicBuffs.add(new BuffInfo(1085, 3, "Acumen", "Увеличивает скорость каста", BuffCategory.MAGIC, 50000, 1, false));
        magicBuffs.add(new BuffInfo(1059, 3, "Empower", "Увеличивает M.Atk", BuffCategory.MAGIC, 55000, 1, false));
        magicBuffs.add(new BuffInfo(1078, 6, "Concentration", "Уменьшает время каста", BuffCategory.MAGIC, 70000, 1, false));
        magicBuffs.add(new BuffInfo(1397, 3, "Clarity", "Увеличивает критический шанс магии", BuffCategory.MAGIC, 90000, 30, false));
        magicBuffs.add(new BuffInfo(1413, 1, "Magnus' Chant", "Мощный магический баф", BuffCategory.MAGIC, 200000, 56, true));
        magicBuffs.add(new BuffInfo(1303, 2, "Wild Magic", "Дикая магия", BuffCategory.MAGIC, 150000, 44, true));
        BUFF_CONFIG.put(BuffCategory.MAGIC, magicBuffs);
        
        // === ОСОБЫЕ БАФЫ ===
        List<BuffInfo> specialBuffs = new ArrayList<>();
        specialBuffs.add(new BuffInfo(1354, 1, "Vampiric Rage", "Вампирская ярость", BuffCategory.SPECIAL, 250000, 62, true));
        specialBuffs.add(new BuffInfo(1355, 1, "Reflect Damage", "Отражение урона", BuffCategory.SPECIAL, 200000, 58, true));
        specialBuffs.add(new BuffInfo(1356, 1, "Blessing of Queen", "Благословение королевы", BuffCategory.SPECIAL, 300000, 65, true));
        specialBuffs.add(new BuffInfo(1357, 1, "Gift of Queen", "Дар королевы", BuffCategory.SPECIAL, 350000, 68, true));
        specialBuffs.add(new BuffInfo(1363, 1, "Chant of Victory", "Песнь победы", BuffCategory.SPECIAL, 400000, 70, true));
        specialBuffs.add(new BuffInfo(1499, 1, "Improved Combat", "Улучшенный бой", BuffCategory.SPECIAL, 180000, 50, true));
        BUFF_CONFIG.put(BuffCategory.SPECIAL, specialBuffs);
        
        // === КЛАНОВЫЕ БАФЫ ===
        List<BuffInfo> clanBuffs = new ArrayList<>();
        clanBuffs.add(new BuffInfo(1364, 1, "Chant of Eagle", "Песнь орла", BuffCategory.CLAN, 150000, 40, false));
        clanBuffs.add(new BuffInfo(1365, 1, "Chant of Predator", "Песнь хищника", BuffCategory.CLAN, 150000, 40, false));
        clanBuffs.add(new BuffInfo(1390, 3, "War Chant", "Боевая песнь", BuffCategory.CLAN, 200000, 55, true));
        clanBuffs.add(new BuffInfo(1391, 3, "Earth Chant", "Песнь земли", BuffCategory.CLAN, 200000, 55, true));
        clanBuffs.add(new BuffInfo(1392, 3, "Flame Chant", "Песнь пламени", BuffCategory.CLAN, 200000, 55, true));
        clanBuffs.add(new BuffInfo(1393, 3, "Storm Chant", "Песнь бури", BuffCategory.CLAN, 200000, 55, true));
        BUFF_CONFIG.put(BuffCategory.CLAN, clanBuffs);
        
        // === СОПРОТИВЛЕНИЯ ===
        List<BuffInfo> resistBuffs = new ArrayList<>();
        resistBuffs.add(new BuffInfo(1032, 3, "Invigor", "Сопротивление дебафам", BuffCategory.RESIST, 80000, 25, false));
        resistBuffs.add(new BuffInfo(1033, 3, "Flexibility", "Сопротивление замедлению", BuffCategory.RESIST, 70000, 20, false));
        resistBuffs.add(new BuffInfo(1259, 4, "Resist Shock", "Сопротивление шоку", BuffCategory.RESIST, 90000, 30, false));
        resistBuffs.add(new BuffInfo(1352, 1, "Elemental Protection", "Защита от стихий", BuffCategory.RESIST, 120000, 40, true));
        resistBuffs.add(new BuffInfo(1461, 1, "Resist Aqua", "Сопротивление воде", BuffCategory.RESIST, 60000, 25, false));
        resistBuffs.add(new BuffInfo(1462, 1, "Resist Wind", "Сопротивление ветру", BuffCategory.RESIST, 60000, 25, false));
        BUFF_CONFIG.put(BuffCategory.RESIST, resistBuffs);
    }
    
    /**
     * Получает бафы определенной категории
     */
    public static List<BuffInfo> getBuffsByCategory(BuffCategory category) {
        return BUFF_CONFIG.getOrDefault(category, new ArrayList<>());
    }
    
    /**
     * Получает все доступные категории
     */
    public static BuffCategory[] getCategories() {
        return BuffCategory.values();
    }
    
    /**
     * Применяет баф к игроку
     */
    public static boolean applyBuff(L2PcInstance player, BuffInfo buffInfo) {
        // Проверка уровня
        if (player.getLevel() < buffInfo.getRequiredLevel()) {
            player.sendMessage("Недостаточный уровень! Требуется: " + buffInfo.getRequiredLevel());
            return false;
        }
        
        // Проверка премиум статуса для премиум бафов
        if (buffInfo.isPremium() && !isPremiumPlayer(player)) {
            player.sendMessage("Этот баф доступен только VIP игрокам!");
            return false;
        }
        
        // Проверка адены
        if (player.getAdena() < buffInfo.getCost()) {
            player.sendMessage("Недостаточно адены! Необходимо: " + buffInfo.getCost());
            return false;
        }
        
        // Снимаем адену
        player.reduceAdena("Buff", buffInfo.getCost(), null, true);
        
        // Применяем баф
        Skill skill = SkillData.getInstance().getSkill(buffInfo.getSkill().getSkillId(), buffInfo.getSkill().getSkillLvl());
        if (skill != null) {
            skill.applyEffects(player, player);
            
            // Эффект получения бафа
            MagicSkillUse msu = new MagicSkillUse(player, player, buffInfo.getSkill().getSkillId(), 
                                                buffInfo.getSkill().getSkillLvl(), 1, 0);
            Broadcast.toSelfAndKnownPlayersInRadius(player, msu, 600);
            
            player.sendMessage("Получен баф: " + buffInfo.getName());
            return true;
        }
        
        return false;
    }
    
    /**
     * Применяет все бафы категории
     */
    public static int applyCategoryBuffs(L2PcInstance player, BuffCategory category) {
        List<BuffInfo> buffs = getBuffsByCategory(category);
        int appliedCount = 0;
        long totalCost = 0;
        
        // Вычисляем общую стоимость
        for (BuffInfo buff : buffs) {
            if (player.getLevel() >= buff.getRequiredLevel() && 
                (!buff.isPremium() || isPremiumPlayer(player))) {
                totalCost += buff.getCost();
            }
        }
        
        // Проверяем общую стоимость
        if (player.getAdena() < totalCost) {
            player.sendMessage("Недостаточно адены для всех бафов категории! Необходимо: " + totalCost);
            return 0;
        }
        
        // Применяем бафы
        for (BuffInfo buff : buffs) {
            if (player.getLevel() >= buff.getRequiredLevel() && 
                (!buff.isPremium() || isPremiumPlayer(player))) {
                
                player.reduceAdena("CategoryBuffs", buff.getCost(), null, true);
                
                Skill skill = SkillData.getInstance().getSkill(buff.getSkill().getSkillId(), buff.getSkill().getSkillLvl());
                if (skill != null) {
                    skill.applyEffects(player, player);
                    appliedCount++;
                }
            }
        }
        
        if (appliedCount > 0) {
            // Эффект получения множественных бафов
            MagicSkillUse msu = new MagicSkillUse(player, player, 2024, 1, 1, 0);
            Broadcast.toSelfAndKnownPlayersInRadius(player, msu, 600);
            
            player.sendMessage("Получено бафов: " + appliedCount + " (" + category.getDisplayName() + ")");
        }
        
        return appliedCount;
    }
    
    /**
     * Применяет все доступные бафы
     */
    public static int applyAllBuffs(L2PcInstance player) {
        int totalApplied = 0;
        long totalCost = 0;
        
        // Вычисляем общую стоимость всех доступных бафов
        for (BuffCategory category : BuffCategory.values()) {
            for (BuffInfo buff : getBuffsByCategory(category)) {
                if (player.getLevel() >= buff.getRequiredLevel() && 
                    (!buff.isPremium() || isPremiumPlayer(player))) {
                    totalCost += buff.getCost();
                }
            }
        }
        
        // Скидка за покупку всех бафов сразу
        totalCost = (long)(totalCost * 0.8); // 20% скидка
        
        if (player.getAdena() < totalCost) {
            player.sendMessage("Недостаточно адены для всех бафов! Необходимо: " + totalCost);
            return 0;
        }
        
        // Снимаем общую стоимость
        player.reduceAdena("AllBuffs", totalCost, null, true);
        
        // Применяем все доступные бафы
        for (BuffCategory category : BuffCategory.values()) {
            for (BuffInfo buff : getBuffsByCategory(category)) {
                if (player.getLevel() >= buff.getRequiredLevel() && 
                    (!buff.isPremium() || isPremiumPlayer(player))) {
                    
                    Skill skill = SkillData.getInstance().getSkill(buff.getSkill().getSkillId(), buff.getSkill().getSkillLvl());
                    if (skill != null) {
                        skill.applyEffects(player, player);
                        totalApplied++;
                    }
                }
            }
        }
        
        if (totalApplied > 0) {
            // Мощный эффект получения всех бафов
            MagicSkillUse msu = new MagicSkillUse(player, player, 2025, 1, 1, 0);
            Broadcast.toSelfAndKnownPlayersInRadius(player, msu, 600);
            
            player.sendMessage("✨ Получены ВСЕ доступные бафы: " + totalApplied + " шт! (скидка 20%)");
        }
        
        return totalApplied;
    }
    
    /**
     * Снимает все бафы с игрока
     */
    public static void removeAllBuffs(L2PcInstance player) {
        player.stopAllEffectsExceptThoseThatLastThroughDeath();
        
        // Эффект снятия бафов
        MagicSkillUse msu = new MagicSkillUse(player, player, 2026, 1, 1, 0);
        Broadcast.toSelfAndKnownPlayersInRadius(player, msu, 600);
        
        player.sendMessage("🗑️ Все бафы сняты!");
    }
    
    /**
     * Проверяет, является ли игрок премиум
     */
    private static boolean isPremiumPlayer(L2PcInstance player) {
        // Здесь должна быть проверка VIP статуса игрока
        // Пока используем проверку на дворянство как пример
        return player.isNoble() || (player.getClan() != null && player.getClan().getLevel() >= 8);
    }
    
    /**
     * Получает стоимость всех бафов категории
     */
    public static long getCategoryCost(L2PcInstance player, BuffCategory category) {
        long totalCost = 0;
        
        for (BuffInfo buff : getBuffsByCategory(category)) {
            if (player.getLevel() >= buff.getRequiredLevel() && 
                (!buff.isPremium() || isPremiumPlayer(player))) {
                totalCost += buff.getCost();
            }
        }
        
        return totalCost;
    }
    
    /**
     * Получает количество доступных бафов в категории
     */
    public static int getAvailableBuffsCount(L2PcInstance player, BuffCategory category) {
        int count = 0;
        
        for (BuffInfo buff : getBuffsByCategory(category)) {
            if (player.getLevel() >= buff.getRequiredLevel() && 
                (!buff.isPremium() || isPremiumPlayer(player))) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Генерирует HTML для отображения бафа
     */
    public static String generateBuffDisplay(BuffInfo buff, L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        boolean available = player.getLevel() >= buff.getRequiredLevel() && 
                          (!buff.isPremium() || isPremiumPlayer(player));
        
        html.append("<table width=\"100%\" bgcolor=\"").append(available ? "111111" : "333333").append("\">");
        html.append("<tr>");
        html.append("<td width=\"150\">").append(buff.getName());
        
        if (buff.isPremium()) {
            html.append(" <font color=\"LEVEL\">[VIP]</font>");
        }
        
        html.append("</td>");
        html.append("<td width=\"80\">").append(buff.getCost()).append(" a</td>");
        html.append("<td width=\"50\">");
        
        if (available) {
            html.append("<button value=\"Получить\" action=\"bypass -h voice .buffs apply ")
                .append(buff.getSkill().getSkillId()).append("\" width=\"50\" height=\"20\" ")
                .append("back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        } else {
            html.append("<font color=\"777777\">Ур.").append(buff.getRequiredLevel()).append("</font>");
        }
        
        html.append("</td>");
        html.append("</tr>");
        html.append("</table>");
        
        return html.toString();
    }
    
    /**
     * Находит баф по ID скилла
     */
    public static BuffInfo findBuffBySkillId(int skillId) {
        for (List<BuffInfo> buffs : BUFF_CONFIG.values()) {
            for (BuffInfo buff : buffs) {
                if (buff.getSkill().getSkillId() == skillId) {
                    return buff;
                }
            }
        }
        return null;
    }
}