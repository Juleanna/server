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

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemData;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.enchant.EnchantScroll;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;

/**
 * Модуль системы заточки для панели игрока
 * @author YourName
 */
public class PlayerPanelEnchantModule {

    private final PlayerPanelConfig _config;
    private final PlayerPanelValidator _validator;
    private final PlayerPanelHtmlGenerator _htmlGenerator;

    public PlayerPanelEnchantModule(PlayerPanelConfig config) {
        _config = config;
        _validator = new PlayerPanelValidator(config);
        _htmlGenerator = new PlayerPanelHtmlGenerator(config);
    }

    public void handleEnchant(L2PcInstance player, StringTokenizer st) {
        if (!_validator.canUseEnchant(player)) {
            showEnchantPanel(player);
            return;
        }

        if (!st.hasMoreTokens()) {
            showEnchantPanel(player);
            return;
        }

        String action = st.nextToken();
        if (!"item".equals(action)) {
            showEnchantPanel(player);
            return;
        }

        if (!st.hasMoreTokens()) {
            showEnchantPanel(player);
            return;
        }

        int itemObjId = PlayerPanelUtils.safeParseInt(st.nextToken(), -1);
        if (itemObjId <= 0) {
            player.sendMessage("Неверный идентификатор предмета!");
            showEnchantPanel(player);
            return;
        }

        L2ItemInstance item = player.getInventory().getItemByObjectId(itemObjId);
        // isEnchantable() возвращает int (grade), 0 = нельзя затачивать.
        if (item == null || item.isEnchantable() == 0) {
            player.sendMessage("Предмет не найден или не может быть заточен!");
            showEnchantPanel(player);
            return;
        }

        // Защита от заточки спец-предметов.
        if (item.isEquipped()) {
            player.sendMessage("Сначала снимите предмет!");
            showEnchantPanel(player);
            return;
        }
        if (item.isAugmented() || item.isShadowItem() || item.isCommonItem() || item.isHeroItem()) {
            player.sendMessage("Этот предмет нельзя затачивать через панель!");
            showEnchantPanel(player);
            return;
        }

        if (!_validator.canEnchantItem(player, item.getId())) {
            showEnchantPanel(player);
            return;
        }

        if (item.getEnchantLevel() >= _config.getMaxEnchantLevel()) {
            player.sendMessage("Достигнут максимальный уровень заточки!");
            showEnchantPanel(player);
            return;
        }

        if (PlayerPanelDAO.hasCooldown(player.getObjectId(), "enchant")) {
            player.sendMessage("Подождите перед следующей заточкой!");
            showEnchantPanel(player);
            return;
        }

        // Атомарно: поиск свитка + проверка денег + списание + результат.
        synchronized (player.getInventory()) {
            // Повторная проверка под lock — предмет мог быть выброшен/продан.
            L2ItemInstance verify = player.getInventory().getItemByObjectId(itemObjId);
            if (verify == null || verify != item) {
                player.sendMessage("Предмет больше недоступен!");
                showEnchantPanel(player);
                return;
            }

            L2ItemInstance scroll = findEnchantScroll(player, item);
            if (scroll == null) {
                player.sendMessage("У вас нет подходящих свитков заточки!");
                showEnchantPanel(player);
                return;
            }

            long cost = calculateEnchantCost(item);
            if (player.getAdena() < cost) {
                player.sendMessage("Недостаточно Adena для заточки! Требуется: " + formatNumber(cost));
                showEnchantPanel(player);
                return;
            }

            performEnchant(player, item, scroll, cost);
        }
    }

    /**
     * Поиск подходящего свитка через EnchantItemData (стандартный L2J-API).
     * Проверяет грейд и тип предмета — заменяет поиск по подстроке имени.
     */
    private L2ItemInstance findEnchantScroll(L2PcInstance player, L2ItemInstance item) {
        EnchantItemData data = EnchantItemData.getInstance();
        for (L2ItemInstance candidate : player.getInventory().getItems()) {
            if (candidate == null || candidate.getCount() <= 0) {
                continue;
            }
            EnchantScroll scrollDef = data.getEnchantScroll(candidate);
            if (scrollDef == null) {
                continue;
            }
            if (scrollDef.isValid(item, null)) {
                return candidate;
            }
        }
        return null;
    }

    private long calculateEnchantCost(L2ItemInstance item) {
        long baseCost = _config.getEnchantBaseCost();
        int enchantLevel = item.getEnchantLevel();

        double multiplier = 1.0 + (enchantLevel * 0.5);
        if (enchantLevel >= 10) {
            multiplier *= 2.0;
        }
        if (enchantLevel >= 15) {
            multiplier *= 3.0;
        }

        return (long) (baseCost * multiplier);
    }

    private void performEnchant(L2PcInstance player, L2ItemInstance item, L2ItemInstance scroll, long cost) {
        int currentEnchant = item.getEnchantLevel();
        int successRate = getEnchantSuccessRate(currentEnchant);

        String itemName = item.getName();
        int itemId = item.getId();
        int scrollId = scroll.getId();
        String scrollName = scroll.getName();

        // Списываем адену и свиток до броска — гарантированно, один раз.
        if (!player.reduceAdena("Enchant", cost, player, true)) {
            player.sendMessage("Не удалось снять Adena!");
            showEnchantPanel(player);
            return;
        }

        if (!player.destroyItem("Enchant", scroll, 1L, player, true)) {
            // Свиток пропал между проверкой и списанием — возвращаем адену.
            player.addAdena("EnchantRefund", cost, player, true);
            player.sendMessage("Свиток недоступен. Adena возвращена.");
            showEnchantPanel(player);
            return;
        }

        boolean success = Rnd.get(100) < successRate;

        if (success) {
            item.setEnchantLevel(currentEnchant + 1);
            item.updateDatabase();

            player.sendMessage("Заточка успешна! " + itemName + " теперь +" + item.getEnchantLevel());

            if (_config.isVisualEffectsEnabled()) {
                player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 1, 0));
            }

            PlayerPanelDAO.logEnchant(player, itemId, item.getObjectId(), itemName,
                                    currentEnchant, item.getEnchantLevel(), true,
                                    scrollId, scrollName, cost);
        } else {
            int newEnchant = currentEnchant;
            if (_config.canEnchantBreak() && currentEnchant > _config.getEnchantSafeLevel()) {
                int decrease = calculateEnchantDecrease(currentEnchant);
                newEnchant = Math.max(0, currentEnchant - decrease);
                item.setEnchantLevel(newEnchant);
                item.updateDatabase();

                if (newEnchant < currentEnchant) {
                    player.sendMessage("Заточка не удалась! " + itemName + " понижен до +" + newEnchant);
                } else {
                    player.sendMessage("Заточка не удалась, но предмет не пострадал.");
                }
            } else {
                player.sendMessage("Заточка не удалась, но предмет защищен от повреждений.");
            }

            PlayerPanelDAO.logEnchant(player, itemId, item.getObjectId(), itemName,
                                    currentEnchant, newEnchant, false,
                                    scrollId, scrollName, cost);
        }

        InventoryUpdate iu = new InventoryUpdate();
        iu.addModifiedItem(item);
        player.sendPacket(iu);
        player.sendPacket(new UserInfo(player));

        if (_config.getPanelCooldown() > 0) {
            PlayerPanelDAO.setCooldown(player.getObjectId(), "enchant", _config.getPanelCooldown() * 60000L);
        }

        showEnchantPanel(player);
    }

    private int calculateEnchantDecrease(int currentEnchant) {
        if (currentEnchant >= 15) {
            return Rnd.get(2, 4);
        } else if (currentEnchant >= 10) {
            return Rnd.get(1, 3);
        } else {
            return Rnd.get(1, 2);
        }
    }

    private int getEnchantSuccessRate(int currentEnchant) {
        if (currentEnchant < 4) {
            return _config.getEnchantSuccessRate1();
        } else if (currentEnchant < 10) {
            return _config.getEnchantSuccessRate5();
        } else if (currentEnchant < 15) {
            return _config.getEnchantSuccessRate10();
        } else {
            return _config.getEnchantSuccessRate15();
        }
    }

    private void showEnchantPanel(L2PcInstance player) {
        String html = _htmlGenerator.generateMainPanel(player, "enchant");
        CommunityBoardHandler.separateAndSend(html, player);
    }

    private String formatNumber(long number) {
        if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
}
