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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.l2jserver.gameserver.config.PlayerPanelConfig;
import com.l2jserver.gameserver.dao.impl.mysql.PlayerPanelDAO;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * Модуль магазина для панели игрока
 * @author YourName
 */
public class PlayerPanelShopModule {

    /**
     * Серверный каталог магазина: shopId -> запись.
     * Цена берётся только отсюда — никогда из клиентского пакета.
     */
    private static final Map<String, ShopEntry> CATALOG;
    static {
        Map<String, ShopEntry> m = new HashMap<>();
        m.put("1",  new ShopEntry(2,    1,  50_000L,    "Demon Sword"));
        m.put("2",  new ShopEntry(23,   1,  30_000L,    "Mithril Armor"));
        m.put("3",  new ShopEntry(729,  1,  500_000L,   "Weapon Enchant Scroll"));
        m.put("4",  new ShopEntry(1060, 10, 5_000L,     "Healing Potion"));
        m.put("5",  new ShopEntry(881,  1,  80_000L,    "Power Ring"));
        m.put("6",  new ShopEntry(1458, 5,  20_000L,    "Soul Crystal"));
        m.put("7",  new ShopEntry(4,    1,  40_000L,    "Elven Bow"));
        m.put("8",  new ShopEntry(224,  1,  35_000L,    "Dagger"));
        m.put("9",  new ShopEntry(1403, 1,  45_000L,    "Magic Staff"));
        m.put("10", new ShopEntry(6569, 1,  2_000_000L, "Blessed Enchant Scroll"));
        CATALOG = Collections.unmodifiableMap(m);
    }

    private final PlayerPanelConfig _config;
    private final PlayerPanelValidator _validator;
    private final PlayerPanelHtmlGenerator _htmlGenerator;

    public PlayerPanelShopModule(PlayerPanelConfig config) {
        _config = config;
        _validator = new PlayerPanelValidator(config);
        _htmlGenerator = new PlayerPanelHtmlGenerator(config);
    }

    public void handleShop(L2PcInstance player, StringTokenizer st) {
        if (!_validator.canUseShop(player)) {
            showShopPanel(player);
            return;
        }

        if (!st.hasMoreTokens()) {
            showShopPanel(player);
            return;
        }

        String action = st.nextToken();
        if (!"buy".equals(action)) {
            showShopPanel(player);
            return;
        }

        if (!st.hasMoreTokens()) {
            showShopPanel(player);
            return;
        }

        String shopId = st.nextToken();

        // Цена клиента игнорируется намеренно — берём из серверного каталога.
        if (st.hasMoreTokens()) {
            st.nextToken();
        }

        if (!_validator.isItemAvailableInShop(shopId)) {
            player.sendMessage("Этот предмет недоступен для покупки!");
            showShopPanel(player);
            return;
        }

        ShopEntry entry = CATALOG.get(shopId);
        if (entry == null) {
            player.sendMessage("Предмет не найден!");
            showShopPanel(player);
            return;
        }

        long price = (long) Math.max(1, Math.round(entry.price * _config.getShopPriceMultiplier()));

        if (PlayerPanelDAO.hasCooldown(player.getObjectId(), "shop")) {
            player.sendMessage("Подождите перед следующей покупкой!");
            showShopPanel(player);
            return;
        }

        if (player.getInventory().getSize() >= player.getInventoryLimit()) {
            player.sendMessage("Инвентарь переполнен!");
            showShopPanel(player);
            return;
        }

        performPurchase(player, entry, price);
    }

    private void performPurchase(L2PcInstance player, ShopEntry entry, long price) {
        // Атомарно: проверка адены + списание + выдача под lock инвентаря.
        synchronized (player.getInventory()) {
            if (player.getAdena() < price) {
                player.sendMessage("Недостаточно Adena для покупки!");
                showShopPanel(player);
                return;
            }

            if (!player.reduceAdena("Shop", price, player, true)) {
                player.sendMessage("Не удалось снять Adena!");
                showShopPanel(player);
                return;
            }

            L2ItemInstance newItem = player.addItem("Shop", entry.itemId, entry.quantity, player, true);
            if (newItem == null) {
                // addItem не прошёл (overweight / slot limit) — возвращаем адену.
                player.addAdena("ShopRefund", price, player, true);
                player.sendMessage("Не удалось выдать предмет. Adena возвращена.");
                showShopPanel(player);
                return;
            }

            String itemName = newItem.getName();
            if (entry.quantity > 1) {
                player.sendMessage("Покупка успешна! " + itemName + " x" + entry.quantity + " добавлен в инвентарь.");
            } else {
                player.sendMessage("Покупка успешна! " + itemName + " добавлен в инвентарь.");
            }

            long pricePerItem = entry.quantity > 0 ? price / entry.quantity : price;
            PlayerPanelDAO.logPurchase(player, entry.itemId, itemName, entry.quantity, pricePerItem, price, "adena");
            PlayerPanelDAO.logAction(player, "shop_buy",
                "Bought " + itemName + " x" + entry.quantity + " for " + price + " adena", price, true);

            if (_config.getAntiSpamInterval() > 0) {
                PlayerPanelDAO.setCooldown(player.getObjectId(), "shop", _config.getAntiSpamInterval() * 1000L);
            }
        }

        showShopPanel(player);
    }

    private void showShopPanel(L2PcInstance player) {
        String html = _htmlGenerator.generateMainPanel(player, "shop");
        CommunityBoardHandler.separateAndSend(html, player);
    }

    /** Запись каталога магазина. Неизменяема. */
    private static final class ShopEntry {
        final int itemId;
        final int quantity;
        final long price;
        final String name;

        ShopEntry(int itemId, int quantity, long price, String name) {
            this.itemId = itemId;
            this.quantity = quantity;
            this.price = price;
            this.name = name;
        }
    }
}
