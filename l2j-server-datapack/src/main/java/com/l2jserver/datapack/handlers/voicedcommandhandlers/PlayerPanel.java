/**
     * Отправляет HTML сообщение игроку
     */
    private void sendHtmlMessage(L2PcInstance player, String html) {
        NpcHtmlMessage msg = new NpcHtmlMessage();
        msg.setHtml(html);
        player.sendPacket(msg);
    }
    
    /**
     * Обработка действий магазина
     */
    private void handleShopAction(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            showShopPanel(player);
            return;
        }
        
        String subAction = st.nextToken().toLowerCase();
        
        switch (subAction) {
            case "category":
                if (st.hasMoreTokens()) {
                    showShopCategory(player, st.nextToken());
                } else {
                    showShopPanel(player);
                }
                break;
            case "buy":
                handlePurchase(player, st);
                break;
            default:
                showShopCategory(player, subAction);
                break;
        }
    }
    
    /**
     * Показывает панель магазина
     */
    private void showShopPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Магазин игрока</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">🛒 МАГАЗИН ИГРОКА 🛒</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        // Категории товаров
        html.append("<table width=\"320\">");
        String[] categories = ShopConfig.getCategories();
        
        for (int i = 0; i < categories.length; i += 2) {
            html.append("<tr>");
            
            // Первая категория в строке
            String cat1 = categories[i];
            String displayName1 = ShopConfig.getCategoryDisplayName(cat1);
            html.append("<td><button value=\"").append(displayName1).append("\" action=\"bypass -h voice .panel shop ").append(cat1).append("\" width=\"150\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
            
            // Вторая категория в строке (если есть)
            if (i + 1 < categories.length) {
                String cat2 = categories[i + 1];
                String displayName2 = ShopConfig.getCategoryDisplayName(cat2);
                html.append("<td><button value=\"").append(displayName2).append("\" action=\"bypass -h voice .panel shop ").append(cat2).append("\" width=\"150\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
            } else {
                html.append("<td></td>");
            }
            
            html.append("</tr>");
        }
        html.append("</table><br>");
        
        // Информация о скидках
        html.append("<table width=\"320\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">💰 Действующие скидки</font></td></tr>");
        
        if (player.getLevel() <= 20) {
            html.append("<tr><td>Скидка новичка:</td><td align=\"right\"><font color=\"66FF66\">-20%</font></td></tr>");
        }
        
        if (player.isNoble()) {
            html.append("<tr><td>Скидка дворянина:</td><td align=\"right\"><font color=\"66FF66\">-10%</font></td></tr>");
        }
        
        if (player.getClan() != null && player.getClan().getLevel() >= 5) {
            html.append("<tr><td>Клановая скидка:</td><td align=\"right\"><font color=\"66FF66\">-5%</font></td></tr>");
        }
        
        html.append("</table><br>");
        
        // Кнопка назад
        html.append("<button value=\"⬅️ Назад в главное меню\" action=\"bypass -h voice .panel\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Обработка заточки украшений
     */
    private void handleJewelryEnchant(L2PcInstance player, StringTokenizer st) {
        L2ItemInstance[] jewelryItems = EnchantSystem.getEquippedItems(player, EnchantSystem.EnchantType.JEWELRY);
        
        if (jewelryItems.length == 0) {
            player.sendMessage("❌ Украшения не экипированы!");
            showEnchantPanel(player);
            return;
        }
        
        // Показываем панель выбора украшения
        showJewelrySelectionPanel(player, jewelryItems);
    }
    
    /**
     * Показывает панель выбора украшений для заточки
     */
    private void showJewelrySelectionPanel(L2PcInstance player, L2ItemInstance[] jewelryItems) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Заточка украшений</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">💍 ЗАТОЧКА УКРАШЕНИЙ 💍</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        html.append("Выберите украшение для заточки:<br><br>");
        
        for (int i = 0; i < jewelryItems.length; i++) {
            L2ItemInstance item = jewelryItems[i];
            long cost = EnchantSystem.calculateEnchantCost(item, 1, false);
            
            html.append("<table width=\"320\" bgcolor=\"111111\">");
            html.append("<tr>");
            html.append("<td width=\"200\">").append(item.getName());
            if (item.getEnchantLevel() > 0) {
                html.append(" <font color=\"LEVEL\">+").append(item.getEnchantLevel()).append("</font>");
            }
            html.append("</td>");
            html.append("<td width=\"120\">");
            html.append("<button value=\"Заточить\" action=\"bypass -h voice .panel enchant jewelry_item ").append(item.getObjectId()).append("\" width=\"80\" height=\"20\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
            html.append("</td>");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<td colspan=\"2\" align=\"center\"><font color=\"AAAAAA\">Стоимость: ").append(String.format("%,d", cost)).append(" адены</font></td>");
            html.append("</tr>");
            html.append("</table><br>");
        }
        
        html.append("<button value=\"⬅️ Назад к заточке\" action=\"bypass -h voice .panel enchant\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    @Override
    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
    
    /**
     * Статический метод для очистки кэша при перезагрузке
     */
    public static void clearCache() {
        LAST_USAGE.clear();
        USAGE_STATS.clear();
    }
    
    /**
     * Получение статистики использования панели
     */
    public static Map<Integer, Integer> getUsageStats() {
        return new HashMap<>(USAGE_STATS);
    }
}</body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Показывает категорию магазина
     */
    private void showShopCategory(L2PcInstance player, String category) {
        List<ShopConfig.ShopItem> items = ShopConfig.getCategoryItems(category);
        
        if (items.isEmpty()) {
            player.sendMessage("❌ Категория пуста или не найдена!");
            showShopPanel(player);
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Магазин - ").append(ShopConfig.getCategoryDisplayName(category)).append("</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">").append(ShopConfig.getCategoryDisplayName(category)).append("</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        // Товары
        html.append("<table width=\"320\">");
        html.append("<tr bgcolor=\"333333\">");
        html.append("<td width=\"180\" align=\"center\"><font color=\"LEVEL\">Товар</font></td>");
        html.append("<td width=\"70\" align=\"center\"><font color=\"LEVEL\">Цена</font></td>");
        html.append("<td width=\"70\" align=\"center\"><font color=\"LEVEL\">Действие</font></td>");
        html.append("</tr>");
        
        for (ShopConfig.ShopItem item : items) {
            if (!ShopConfig.isItemAvailable(item.getItemId(), player)) {
                continue; // Пропускаем недоступные товары
            }
            
            long modifiedPrice = ShopConfig.getModifiedPrice(item, player);
            
            html.append("<tr bgcolor=\"111111\">");
            html.append("<td>").append(item.getIcon()).append(" ").append(item.getName());
            
            if (item.getCount() > 1) {
                html.append(" (").append(item.getCount()).append(" шт)");
            }
            
            html.append("</td>");
            html.append("<td align=\"center\">");
            
            if (modifiedPrice != item.getPrice()) {
                html.append("<s>").append(String.format("%,d", item.getPrice())).append("</s><br>");
                html.append("<font color=\"66FF66\">").append(String.format("%,d", modifiedPrice)).append("</font>");
            } else {
                html.append(String.format("%,d", modifiedPrice));
            }
            
            html.append("</td>");
            html.append("<td align=\"center\">");
            html.append("<button value=\"Купить\" action=\"bypass -h voice .panel shop buy ").append(item.getItemId()).append(" ").append(item.getCount()).append("\" width=\"60\" height=\"20\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
            html.append("</td>");
            html.append("</tr>");
        }
        
        html.append("</table><br>");
        
        // Кнопки навигации
        html.append("<table width=\"320\">");
        html.append("<tr>");
        html.append("<td><button value=\"⬅️ К категориям\" action=\"bypass -h voice .panel shop\" width=\"150\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🏠 Главное меню\" action=\"bypass -h voice .panel\" width=\"150\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("</table>");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Обработка покупки
     */
    private void handlePurchase(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            player.sendMessage("❌ Не указан ID товара!");
            return;
        }
        
        try {
            int itemId = Integer.parseInt(st.nextToken());
            int count = 1;
            
            if (st.hasMoreTokens()) {
                count = Integer.parseInt(st.nextToken());
            }
            
            ShopConfig.ShopItem item = ShopConfig.findItem(itemId);
            if (item == null) {
                player.sendMessage("❌ Товар не найден!");
                return;
            }
            
            if (!ShopConfig.isItemAvailable(itemId, player)) {
                player.sendMessage("❌ Товар недоступен для вашего уровня!");
                return;
            }
            
            long totalCost = ShopConfig.getModifiedPrice(item, player) * (count / item.getCount());
            
            if (player.getAdena() < totalCost) {
                player.sendMessage("❌ Недостаточно адены! Необходимо: " + String.format("%,d", totalCost));
                return;
            }
            
            // Проверка места в инвентаре
            if (player.getInventory().getSize() >= player.getInventoryLimit() - 10) {
                player.sendMessage("❌ Недостаточно места в инвентаре!");
                return;
            }
            
            // Покупка
            player.reduceAdena("ShopPurchase", totalCost, null, true);
            player.addItem("ShopPurchase", itemId, count, null, true);
            
            player.sendMessage("✅ Покупка совершена: " + item.getName() + " x" + count + " за " + String.format("%,d", totalCost) + " адены!");
            
        } catch (NumberFormatException e) {
            player.sendMessage("❌ Неверный формат команды!");
        }
    }
    
    /**
     * Обработка действий телепортации
     */
    private void handleTeleportAction(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            showTeleportPanel(player);
            return;
        }
        
        try {
            int locationIndex = Integer.parseInt(st.nextToken());
            performTeleport(player, locationIndex);
        } catch (NumberFormatException e) {
            player.sendMessage("❌ Неверный индекс локации!");
            showTeleportPanel(player);
        }
    }
    
    /**
     * Показывает панель телепортации
     */
    private void showTeleportPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Система телепортации</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">🌀 ТЕЛЕПОРТАЦИЯ 🌀</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        // Локации
        int[][] locations = {
            {82698, 148638, -3473, 50000, 0}, // Giran
            {111409, 219364, -3545, 50000, 0}, // Heine  
            {-84318, 244579, -3730, 30000, 0}, // Talking Island
            {-44836, -112524, -235, 30000, 0}, // Elven Village
            {115113, -178212, -901, 30000, 0}, // Dark Elven Village
            {-80826, 149775, -3043, 30000, 0}, // Orc Village
            {-12672, 122776, -3116, 30000, 0}, // Dwarven Village
            {146331, 25762, -2018, 60000, 0}, // Aden
            {116819, 76994, -2714, 70000, 0}, // Hunters Village
            {-117251, 46890, 360, 80000, 0} // Goddard
        };
        
        String[] locationNames = {
            "🏰 Гиран", "🌊 Хейн", "🏝️ Остров Говорящих", "🌲 Эльфийская деревня",
            "🌙 Темная эльфийская деревня", "⚔️ Орочья деревня", "⛏️ Гномья деревня",
            "👑 Аден", "🏹 Деревня охотников", "🏔️ Годдард"
        };
        
        html.append("<table width=\"320\">");
        
        for (int i = 0; i < locations.length; i += 2) {
            html.append("<tr>");
            
            // Первая локация
            long cost1 = calculateTeleportCost(player, locations[i][3]);
            html.append("<td><button value=\"").append(locationNames[i]).append("\" action=\"bypass -h voice .panel teleport ").append(i).append("\" width=\"150\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
            
            // Вторая локация (если есть)
            if (i + 1 < locations.length) {
                long cost2 = calculateTeleportCost(player, locations[i + 1][3]);
                html.append("<td><button value=\"").append(locationNames[i + 1]).append("\" action=\"bypass -h voice .panel teleport ").append(i + 1).append("\" width=\"150\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
            } else {
                html.append("<td></td>");
            }
            
            html.append("</tr>");
        }
        
        html.append("</table><br>");
        
        // Информация о ценах
        html.append("<table width=\"320\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">💰 Стоимость телепортации</font></td></tr>");
        
        for (int i = 0; i < Math.min(5, locations.length); i++) {
            long cost = calculateTeleportCost(player, locations[i][3]);
            html.append("<tr>");
            html.append("<td>").append(locationNames[i]).append(":</td>");
            html.append("<td align=\"right\">").append(String.format("%,d", cost)).append(" адены</td>");
            html.append("</tr>");
        }
        
        html.append("</table><br>");
        
        // Информация о скидках
        if (player.getLevel() <= 20 || player.isNoble() || (player.getClan() != null && player.getClan().getLevel() >= 5)) {
            html.append("<table width=\"320\" bgcolor=\"333333\">");
            html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">🎁 Ваши скидки</font></td></tr>");
            
            if (player.getLevel() <= 20) {
                html.append("<tr><td>Скидка новичка:</td><td align=\"right\"><font color=\"66FF66\">-50%</font></td></tr>");
            }
            if (player.isNoble()) {
                html.append("<tr><td>Скидка дворянина:</td><td align=\"right\"><font color=\"66FF66\">-20%</font></td></tr>");
            }
            if (player.getClan() != null && player.getClan().getLevel() >= 5) {
                html.append("<tr><td>Клановая скидка:</td><td align=\"right\"><font color=\"66FF66\">-10%</font></td></tr>");
            }
            
            html.append("</table><br>");
        }
        
        // Предупреждения
        html.append("<font color=\"FF6666\" size=\"1\">⚠️ Телепортация недоступна в бою!</font><br>");
        html.append("<font color=\"66FF66\" size=\"1\">💡 Скидки суммируются</font><br><br>");
        
        // Кнопка назад
        html.append("<button value=\"⬅️ Назад в главное меню\" action=\"bypass -h voice .panel\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Вычисляет стоимость телепортации с учетом скидок
     */
    private long calculateTeleportCost(L2PcInstance player, long baseCost) {
        double multiplier = 1.0;
        
        // Скидка для новичков
        if (player.getLevel() <= 20) {
            multiplier *= 0.5; // 50% скидка
        }
        
        // Скидка для дворян
        if (player.isNoble()) {
            multiplier *= 0.8; // 20% скидка
        }
        
        // Клановая скидка
        if (player.getClan() != null && player.getClan().getLevel() >= 5) {
            multiplier *= 0.9; // 10% скидка
        }
        
        return (long)(baseCost * multiplier);
    }
    
    /**
     * Выполняет телепортацию
     */
    private void performTeleport(L2PcInstance player, int locationIndex) {
        int[][] locations = {
            {82698, 148638, -3473, 50000, 0}, // Giran
            {111409, 219364, -3545, 50000, 0}, // Heine  
            {-84318, 244579, -3730, 30000, 0}, // Talking Island
            {-44836, -112524, -235, 30000, 0}, // Elven Village
            {115113, -178212, -901, 30000, 0}, // Dark Elven Village
            {-80826, 149775, -3043, 30000, 0}, // Orc Village
            {-12672, 122776, -3116, 30000, 0}, // Dwarven Village
            {146331, 25762, -2018, 60000, 0}, // Aden
            {116819, 76994, -2714, 70000, 0}, // Hunters Village
            {-117251, 46890, 360, 80000, 0} // Goddard
        };
        
        String[] locationNames = {
            "Гиран", "Хейн", "Остров Говорящих", "Эльфийская деревня",
            "Темная эльфийская деревня", "Орочья деревня", "Гномья деревня",
            "Аден", "Деревня охотников", "Годдард"
        };
        
        if (locationIndex < 0 || locationIndex >= locations.length) {
            player.sendMessage("❌ Неверная локация!");
            return;
        }
        
        // Проверки
        if (player.isInCombat()) {
            player.sendMessage("❌ Нельзя телепортироваться в бою!");
            return;
        }
        
        if (player.isCastingNow() || player.isMovementDisabled() || player.isMuted() || 
            player.isAlikeDead() || player.inObserverMode() || player.isCombatFlagEquipped()) {
            player.sendMessage("❌ Телепортация недоступна в данный момент!");
            return;
        }
        
        int[] location = locations[locationIndex];
        long cost = calculateTeleportCost(player, location[3]);
        
        if (player.getAdena() < cost) {
            player.sendMessage("❌ Недостаточно адены! Необходимо: " + String.format("%,d", cost));
            return;
        }
        
        // Выполняем телепортацию
        player.reduceAdena("Teleport", cost, null, true);
        player.teleToLocation(location[0], location[1], location[2]);
        
        player.sendMessage("✅ Телепортация в " + locationNames[locationIndex] + " выполнена!");
    }
    
    /**
     * Показывает информационную панель
     */
    private void showInfoPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Информация о персонаже</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">📊 ИНФОРМАЦИЯ О ПЕРСОНАЖЕ 📊</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"350\" height=\"1\"><br>");
        
        // Основная информация
        html.append("<table width=\"350\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">👤 Основная информация</font></td></tr>");
        html.append("<tr><td width=\"175\">Имя:</td><td width=\"175\">").append(player.getName()).append("</td></tr>");
        html.append("<tr><td>Уровень:</td><td><font color=\"LEVEL\">").append(player.getLevel()).append("</font></td></tr>");
        html.append("<tr><td>Класс:</td><td>").append(player.getTemplate().getClassName()).append("</td></tr>");
        html.append("<tr><td>Раса:</td><td>").append(player.getRace().toString()).append("</td></tr>");
        html.append("<tr><td>Клан:</td><td>").append(player.getClan() != null ? player.getClan().getName() : "<font color=\"777777\">Нет</font>").append("</td></tr>");
        if (player.getClan() != null && player.getClan().getAllyName() != null) {
            html.append("<tr><td>Альянс:</td><td>").append(player.getClan().getAllyName()).append("</td></tr>");
        }
        html.append("</table><br>");
        
        // Характеристики
        html.append("<table width=\"350\" bgcolor=\"222222\">");
        html.append("<tr><td colspan=\"4\" align=\"center\"><font color=\"LEVEL\">⚔️ Характеристики</font></td></tr>");
        html.append("<tr>");
        html.append("<td width=\"87\">HP:</td><td width=\"88\"><font color=\"00FF00\">").append((int)player.getCurrentHp()).append("/").append(player.getMaxHp()).append("</font></td>");
        html.append("<td width=\"87\">MP:</td><td width=\"88\"><font color=\"0099FF\">").append((int)player.getCurrentMp()).append("/").append(player.getMaxMp()).append("</font></td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td>CP:</td><td><font color=\"FFFF00\">").append((int)player.getCurrentCp()).append("/").append(player.getMaxCp()).append("</font></td>");
        html.append("<td>Скорость:</td><td>").append(player.getRunSpeed()).append("</td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td>P.Atk:</td><td>").append(player.getPAtk(null)).append("</td>");
        html.append("<td>M.Atk:</td><td>").append(player.getMAtk(null, null)).append("</td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td>P.Def:</td><td>").append(player.getPDef(null)).append("</td>");
        html.append("<td>M.Def:</td><td>").append(player.getMDef(null, null)).append("</td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Экономика и PvP
        html.append("<table width=\"350\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">💰 Экономика и PvP</font></td></tr>");
        html.append("<tr><td width=\"175\">Адена:</td><td width=\"175\"><font color=\"FFFF00\">").append(String.format("%,d", player.getAdena())).append("</font></td></tr>");
        html.append("<tr><td>PvP убийства:</td><td><font color=\"FF6600\">").append(player.getPvpKills()).append("</font></td></tr>");
        html.append("<tr><td>PK убийства:</td><td><font color=\"FF0000\">").append(player.getPkKills()).append("</font></td></tr>");
        html.append("<tr><td>Карма:</td><td>").append(player.getKarma() > 0 ? "<font color=\"FF0000\">" + player.getKarma() + "</font>" : "<font color=\"66FF66\">0</font>").append("</td></tr>");
        html.append("</table><br>");
        
        // Время онлайна и статистика панели
        long onlineTime = (System.currentTimeMillis() - player.getOnlineTime()) / 1000;
        long hours = onlineTime / 3600;
        long minutes = (onlineTime % 3600) / 60;
        
        html.append("<table width=\"350\" bgcolor=\"222222\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">📈 Статистика сессии</font></td></tr>");
        html.append("<tr><td width=\"175\">Время онлайн:</td><td width=\"175\">").append(hours).append("ч ").append(minutes).append("м</td></tr>");
        html.append("<tr><td>Использований панели:</td><td><font color=\"LEVEL\">").append(USAGE_STATS.getOrDefault(player.getObjectId(), 0)).append("</font></td></tr>");
        html.append("</table><br>");
        
        // Кнопки действий
        html.append("<table width=\"350\">");
        html.append("<tr>");
        html.append("<td><button value=\"🔄 Обновить\" action=\"bypass -h voice .panel info\" width=\"115\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"⚙️ Настройки\" action=\"bypass -h voice .panel settings\" width=\"115\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🏠 Главная\" action=\"bypass -h voice .panel\" width=\"115\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("</table>");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Показывает панель настроек
     */
    private void showSettingsPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Настройки панели</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">⚙️ НАСТРОЙКИ ПАНЕЛИ ⚙️</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        html.append("<table width=\"320\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">📋 Информация о панели</font></td></tr>");
        html.append("<tr><td>Версия:</td><td><font color=\"LEVEL\">2.0.0</font></td></tr>");
        html.append("<tr><td>Команды:</td><td>.panel, .menu, .pp</td></tr>");
        html.append("<tr><td>Функций:</td><td>Заточка, Магазин, Бафы, Телепорт</td></tr>");
        html.append("<tr><td>Статус:</td><td><font color=\"66FF66\">Активна</font></td></tr>");
        html.append("</table><br>");
        
        html.append("<table width=\"320\" bgcolor=\"222222\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">💡 Советы по использованию</font></td></tr>");
        html.append("<tr><td colspan=\"2\">• Используйте безопасную заточку для ценных предметов</td></tr>");
        html.append("<tr><td colspan=\"2\">• Скидки суммируются (новичок + дворянин + клан)</td></tr>");
        html.append("<tr><td colspan=\"2\">• Бафы \"Все сразу\" дают 20% скидку</td></tr>");
        html.append("<tr><td colspan=\"2\">• VIP игроки получают доступ к особым бафам</td></tr>");
        html.append("</table><br>");
        
        html.append("<button value=\"⬅️ Назад в главное меню\" action=\"bypass -h voice .panel\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        
        html.append("</center>/*
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

import static com.l2jserver.gameserver.config.Configuration.customs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Полнофункциональная интегрированная панель игрока
 * Включает: заточку, магазин, бафы, телепортацию, информацию
 * @author YourName
 */
public class PlayerPanel implements IVoicedCommandHandler {
    
    private static final String[] VOICED_COMMANDS = {
        "panel",
        "menu", 
        "pp"
    };
    
    // Система защиты от спама
    private static final Map<Integer, Long> LAST_USAGE = new ConcurrentHashMap<>();
    private static final long COOLDOWN_TIME = 1000; // 1 секунда
    
    // Статистика использования панели
    private static final Map<Integer, Integer> USAGE_STATS = new ConcurrentHashMap<>();
    
    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
        if (activeChar == null) {
            return false;
        }
        
        // Проверка анти-спама
        if (!checkCooldown(activeChar)) {
            return false;
        }
        
        // Проверка разрешений
        if (!canUsePanel(activeChar)) {
            activeChar.sendMessage("❌ Панель игрока недоступна в данный момент!");
            return false;
        }
        
        // Обновляем статистику
        updateUsageStats(activeChar);
        
        // Обрабатываем команду
        if (params == null || params.isEmpty()) {
            showMainPanel(activeChar);
        } else {
            handlePanelAction(activeChar, params);
        }
        
        return true;
    }
    
    /**
     * Проверка кулдауна команд
     */
    private boolean checkCooldown(L2PcInstance player) {
        int playerId = player.getObjectId();
        long currentTime = System.currentTimeMillis();
        
        if (LAST_USAGE.containsKey(playerId)) {
            long lastUsage = LAST_USAGE.get(playerId);
            if (currentTime - lastUsage < COOLDOWN_TIME) {
                long remainingTime = COOLDOWN_TIME - (currentTime - lastUsage);
                player.sendMessage("⏱️ Подождите " + (remainingTime / 1000.0) + " сек. перед следующим использованием!");
                return false;
            }
        }
        
        LAST_USAGE.put(playerId, currentTime);
        return true;
    }
    
    /**
     * Проверка возможности использования панели
     */
    private boolean canUsePanel(L2PcInstance player) {
        // Проверка уровня
        if (player.getLevel() < 1) {
            return false;
        }
        
        // Проверка состояний
        if (player.isDead() || player.isInJail()) {
            return false;
        }
        
        // Проверка зон
        if (player.isInOlympiadMode() || player.isInSiege()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Обновление статистики использования
     */
    private void updateUsageStats(L2PcInstance player) {
        int playerId = player.getObjectId();
        USAGE_STATS.put(playerId, USAGE_STATS.getOrDefault(playerId, 0) + 1);
    }
    
    /**
     * Обработка действий панели
     */
    private void handlePanelAction(L2PcInstance player, String params) {
        StringTokenizer st = new StringTokenizer(params);
        if (!st.hasMoreTokens()) {
            showMainPanel(player);
            return;
        }
        
        String action = st.nextToken().toLowerCase();
        
        switch (action) {
            case "enchant":
                handleEnchantAction(player, st);
                break;
            case "shop":
                handleShopAction(player, st);
                break;
            case "buffs":
                handleBuffsAction(player, st);
                break;
            case "teleport":
                handleTeleportAction(player, st);
                break;
            case "info":
                showInfoPanel(player);
                break;
            case "settings":
                showSettingsPanel(player);
                break;
            default:
                showMainPanel(player);
                break;
        }
    }
    
    /**
     * Показывает главную панель
     */
    private void showMainPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        // Загружаем базовый шаблон или создаем встроенный
        String template = loadTemplate("main_panel.htm");
        if (template != null) {
            html.append(template);
        } else {
            html.append(createMainPanelHTML(player));
        }
        
        // Заменяем переменные
        String finalHtml = html.toString()
            .replace("%player_name%", player.getName())
            .replace("%player_level%", String.valueOf(player.getLevel()))
            .replace("%player_class%", player.getTemplate().getClassName())
            .replace("%current_hp%", String.valueOf((int)player.getCurrentHp()))
            .replace("%max_hp%", String.valueOf(player.getMaxHp()))
            .replace("%current_mp%", String.valueOf((int)player.getCurrentMp()))
            .replace("%max_mp%", String.valueOf(player.getMaxMp()))
            .replace("%player_adena%", String.valueOf(player.getAdena()))
            .replace("%pvp_kills%", String.valueOf(player.getPvpKills()))
            .replace("%usage_count%", String.valueOf(USAGE_STATS.getOrDefault(player.getObjectId(), 0)));
        
        sendHtmlMessage(player, finalHtml);
    }
    
    /**
     * Создает HTML главной панели
     */
    private String createMainPanelHTML(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Панель Игрока</title></head><body>");
        html.append("<center>");
        
        // Заголовок
        html.append("<table width=\"320\" bgcolor=\"000000\">");
        html.append("<tr><td align=\"center\">");
        html.append("<font color=\"LEVEL\" size=\"3\">🎮 ПАНЕЛЬ ИГРОКА 🎮</font><br>");
        html.append("<font color=\"AAAAAA\">Добро пожаловать, ").append(player.getName()).append("!</font>");
        html.append("</td></tr>");
        html.append("</table><br>");
        
        // Основные кнопки
        html.append("<table width=\"320\">");
        html.append("<tr>");
        html.append("<td><button value=\"⚔️ Заточка\" action=\"bypass -h voice .panel enchant\" width=\"150\" height=\"30\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🛒 Магазин\" action=\"bypass -h voice .panel shop\" width=\"150\" height=\"30\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td><button value=\"✨ Бафы\" action=\"bypass -h voice .panel buffs\" width=\"150\" height=\"30\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🌀 Телепорт\" action=\"bypass -h voice .panel teleport\" width=\"150\" height=\"30\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Информационная кнопка
        html.append("<button value=\"📊 Подробная информация\" action=\"bypass -h voice .panel info\" width=\"320\" height=\"30\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>");
        
        // Быстрая статистика
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        html.append("<table width=\"320\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"4\" align=\"center\"><font color=\"LEVEL\">📈 Быстрая статистика</font></td></tr>");
        html.append("<tr>");
        html.append("<td width=\"80\">Ур: <font color=\"LEVEL\">").append(player.getLevel()).append("</font></td>");
        html.append("<td width=\"80\">HP: <font color=\"00FF00\">").append((int)player.getCurrentHp()).append("</font></td>");
        html.append("<td width=\"80\">MP: <font color=\"0099FF\">").append((int)player.getCurrentMp()).append("</font></td>");
        html.append("<td width=\"80\">PvP: <font color=\"FF6600\">").append(player.getPvpKills()).append("</font></td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td colspan=\"2\">Адена: <font color=\"FFFF00\">").append(String.format("%,d", player.getAdena())).append("</font></td>");
        html.append("<td colspan=\"2\">Клан: <font color=\"LEVEL\">").append(player.getClan() != null ? player.getClan().getName() : "Нет").append("</font></td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Футер
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        html.append("<font color=\"777777\" size=\"1\">💡 Команды: .panel, .menu, .pp | Использований: ").append(USAGE_STATS.getOrDefault(player.getObjectId(), 0)).append("</font>");
        
        html.append("</center></body></html>");
        
        return html.toString();
    }
    
    /**
     * Обработка действий заточки
     */
    private void handleEnchantAction(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            showEnchantPanel(player);
            return;
        }
        
        String subAction = st.nextToken();
        
        switch (subAction.toLowerCase()) {
            case "weapon":
                handleWeaponEnchant(player, st);
                break;
            case "armor":
                handleArmorEnchant(player, st);
                break;
            case "jewelry":
                handleJewelryEnchant(player, st);
                break;
            case "safe":
                handleSafeEnchant(player, st);
                break;
            default:
                showEnchantPanel(player);
                break;
        }
    }
    
    /**
     * Показывает панель заточки
     */
    private void showEnchantPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Система заточки</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">⚔️ СИСТЕМА ЗАТОЧКИ ⚔️</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        // Информация о текущем оружии
        L2ItemInstance weapon = player.getActiveWeaponInstance();
        if (weapon != null) {
            html.append("<table width=\"320\" bgcolor=\"111111\">");
            html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">🗡️ Текущее оружие</font></td></tr>");
            html.append("<tr>");
            html.append("<td width=\"200\">").append(weapon.getName());
            if (weapon.getEnchantLevel() > 0) {
                html.append(" <font color=\"LEVEL\">+").append(weapon.getEnchantLevel()).append("</font>");
            }
            html.append("</td>");
            html.append("<td width=\"120\">").append(weapon.getItem().getCrystalType().toString()).append("</td>");
            html.append("</tr>");
            html.append("</table><br>");
        } else {
            html.append("<font color=\"FF6666\">❌ Оружие не экипировано!</font><br><br>");
        }
        
        // Кнопки заточки
        html.append("<table width=\"320\">");
        html.append("<tr>");
        html.append("<td><button value=\"⚔️ Оружие +1\" action=\"bypass -h voice .panel enchant weapon 1\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"⚔️ Оружие +3\" action=\"bypass -h voice .panel enchant weapon 3\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"⚔️ Оружие +5\" action=\"bypass -h voice .panel enchant weapon 5\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td><button value=\"🛡️ Броня +1\" action=\"bypass -h voice .panel enchant armor 1\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🛡️ Броня +3\" action=\"bypass -h voice .panel enchant armor 3\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🛡️ Броня +5\" action=\"bypass -h voice .panel enchant armor 5\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Безопасная заточка
        html.append("<table width=\"320\">");
        html.append("<tr>");
        html.append("<td><button value=\"🔒 Безопасная заточка\" action=\"bypass -h voice .panel enchant safe\" width=\"160\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"💎 Украшения\" action=\"bypass -h voice .panel enchant jewelry\" width=\"160\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Информация о ценах
        if (weapon != null) {
            long cost1 = EnchantSystem.calculateEnchantCost(weapon, 1, false);
            long cost3 = EnchantSystem.calculateEnchantCost(weapon, 3, false);
            long cost5 = EnchantSystem.calculateEnchantCost(weapon, 5, false);
            long costSafe = EnchantSystem.calculateEnchantCost(weapon, 1, true);
            
            html.append("<table width=\"320\" bgcolor=\"333333\">");
            html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">💰 Стоимость заточки</font></td></tr>");
            html.append("<tr><td>+1 уровень:</td><td align=\"right\">").append(String.format("%,d", cost1)).append(" адены</td></tr>");
            html.append("<tr><td>+3 уровня:</td><td align=\"right\">").append(String.format("%,d", cost3)).append(" адены</td></tr>");
            html.append("<tr><td>+5 уровней:</td><td align=\"right\">").append(String.format("%,d", cost5)).append(" адены</td></tr>");
            html.append("<tr><td>Безопасная +1:</td><td align=\"right\">").append(String.format("%,d", costSafe)).append(" адены</td></tr>");
            html.append("</table><br>");
        }
        
        // Предупреждения
        html.append("<font color=\"FF6666\" size=\"1\">⚠️ Заточка может привести к разрушению предмета!</font><br>");
        html.append("<font color=\"66FF66\" size=\"1\">🔒 Безопасная заточка не разрушает предмет, но стоит дороже</font><br><br>");
        
        // Кнопка назад
        html.append("<button value=\"⬅️ Назад в главное меню\" action=\"bypass -h voice .panel\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Обработка заточки оружия
     */
    private void handleWeaponEnchant(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            player.sendMessage("❌ Не указан уровень заточки!");
            return;
        }
        
        try {
            int levels = Integer.parseInt(st.nextToken());
            L2ItemInstance weapon = player.getActiveWeaponInstance();
            
            if (weapon == null) {
                player.sendMessage("❌ Оружие не экипировано!");
                showEnchantPanel(player);
                return;
            }
            
            EnchantSystem.EnchantResult result = EnchantSystem.enchantItem(player, weapon, levels, false);
            
            switch (result) {
                case SUCCESS:
                    player.sendMessage("✅ " + result.getMessage());
                    break;
                case FAILURE:
                    player.sendMessage("❌ " + result.getMessage());
                    break;
                case BREAK:
                    player.sendMessage("💥 " + result.getMessage());
                    break;
                case MAX_REACHED:
                    player.sendMessage("⭐ " + result.getMessage());
                    break;
            }
            
            showEnchantPanel(player);
            
        } catch (NumberFormatException e) {
            player.sendMessage("❌ Неверный формат числа!");
            showEnchantPanel(player);
        }
    }
    
    /**
     * Обработка заточки брони
     */
    private void handleArmorEnchant(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            player.sendMessage("❌ Не указан уровень заточки!");
            return;
        }
        
        try {
            int levels = Integer.parseInt(st.nextToken());
            L2ItemInstance[] armorItems = EnchantSystem.getEquippedItems(player, EnchantSystem.EnchantType.ARMOR);
            
            if (armorItems.length == 0) {
                player.sendMessage("❌ Броня не экипирована!");
                showEnchantPanel(player);
                return;
            }
            
            // Затачиваем первый найденный предмет брони
            L2ItemInstance armor = armorItems[0];
            EnchantSystem.EnchantResult result = EnchantSystem.enchantItem(player, armor, levels, false);
            
            switch (result) {
                case SUCCESS:
                    player.sendMessage("✅ " + result.getMessage() + " (" + armor.getName() + ")");
                    break;
                case FAILURE:
                    player.sendMessage("❌ " + result.getMessage() + " (" + armor.getName() + ")");
                    break;
                case BREAK:
                    player.sendMessage("💥 " + result.getMessage() + " (" + armor.getName() + ")");
                    break;
                case MAX_REACHED:
                    player.sendMessage("⭐ " + result.getMessage() + " (" + armor.getName() + ")");
                    break;
            }
            
            showEnchantPanel(player);
            
        } catch (NumberFormatException e) {
            player.sendMessage("❌ Неверный формат числа!");
            showEnchantPanel(player);
        }
    }
    
    /**
     * Обработка безопасной заточки
     */
    private void handleSafeEnchant(L2PcInstance player, StringTokenizer st) {
        L2ItemInstance weapon = player.getActiveWeaponInstance();
        
        if (weapon == null) {
            player.sendMessage("❌ Оружие не экипировано!");
            showEnchantPanel(player);
            return;
        }
        
        EnchantSystem.EnchantResult result = EnchantSystem.enchantItem(player, weapon, 1, true);
        
        switch (result) {
            case SUCCESS:
                player.sendMessage("✅ Безопасная заточка успешна! " + weapon.getName() + " +" + weapon.getEnchantLevel());
                break;
            case FAILURE:
                player.sendMessage("❌ Безопасная заточка не удалась, но предмет цел!");
                break;
            case MAX_REACHED:
                player.sendMessage("⭐ Достигнут максимальный уровень заточки!");
                break;
            default:
                player.sendMessage("❌ Ошибка при заточке!");
                break;
        }
        
        showEnchantPanel(player);
    }
    
    /**
     * Обработка действий с бафами
     */
    private void handleBuffsAction(L2PcInstance player, StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            showBuffsPanel(player);
            return;
        }
        
        String subAction = st.nextToken().toLowerCase();
        
        switch (subAction) {
            case "all":
                int totalBuffs = BuffSystem.applyAllBuffs(player);
                if (totalBuffs > 0) {
                    showBuffsPanel(player);
                }
                break;
            case "defense":
                BuffSystem.applyCategoryBuffs(player, BuffSystem.BuffCategory.DEFENSE);
                showBuffsPanel(player);
                break;
            case "attack":
                BuffSystem.applyCategoryBuffs(player, BuffSystem.BuffCategory.ATTACK);
                showBuffsPanel(player);
                break;
            case "speed":
                BuffSystem.applyCategoryBuffs(player, BuffSystem.BuffCategory.SPEED);
                showBuffsPanel(player);
                break;
            case "magic":
                BuffSystem.applyCategoryBuffs(player, BuffSystem.BuffCategory.MAGIC);
                showBuffsPanel(player);
                break;
            case "remove":
                BuffSystem.removeAllBuffs(player);
                showBuffsPanel(player);
                break;
            case "apply":
                if (st.hasMoreTokens()) {
                    try {
                        int skillId = Integer.parseInt(st.nextToken());
                        BuffSystem.BuffInfo buff = BuffSystem.findBuffBySkillId(skillId);
                        if (buff != null) {
                            BuffSystem.applyBuff(player, buff);
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("❌ Неверный ID скилла!");
                    }
                }
                showBuffsPanel(player);
                break;
            default:
                showBuffsPanel(player);
                break;
        }
    }
    
    /**
     * Показывает панель бафов
     */
    private void showBuffsPanel(L2PcInstance player) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><head><title>Система бафов</title></head><body>");
        html.append("<center>");
        html.append("<font color=\"LEVEL\" size=\"3\">✨ СИСТЕМА БАФОВ ✨</font><br>");
        html.append("<img src=\"L2UI.SquareWhite\" width=\"320\" height=\"1\"><br>");
        
        // Категории бафов
        html.append("<table width=\"320\">");
        html.append("<tr>");
        html.append("<td><button value=\"🛡️ Защитные\" action=\"bypass -h voice .panel buffs defense\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"⚔️ Боевые\" action=\"bypass -h voice .panel buffs attack\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🏃 Скорость\" action=\"bypass -h voice .panel buffs speed\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td><button value=\"🔮 Магические\" action=\"bypass -h voice .panel buffs magic\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"✨ ВСЕ БАФЫ\" action=\"bypass -h voice .panel buffs all\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("<td><button value=\"🗑️ Снять\" action=\"bypass -h voice .panel buffs remove\" width=\"100\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Информация о стоимости
        html.append("<table width=\"320\" bgcolor=\"111111\">");
        html.append("<tr><td colspan=\"2\" align=\"center\"><font color=\"LEVEL\">💰 Стоимость бафов</font></td></tr>");
        
        for (BuffSystem.BuffCategory category : BuffSystem.BuffCategory.values()) {
            long cost = BuffSystem.getCategoryCost(player, category);
            int count = BuffSystem.getAvailableBuffsCount(player, category);
            
            if (count > 0) {
                html.append("<tr>");
                html.append("<td>").append(category.getDisplayName()).append(" (").append(count).append(" шт):</td>");
                html.append("<td align=\"right\">").append(String.format("%,d", cost)).append(" a</td>");
                html.append("</tr>");
            }
        }
        
        // Стоимость всех бафов со скидкой
        long totalCost = 0;
        for (BuffSystem.BuffCategory category : BuffSystem.BuffCategory.values()) {
            totalCost += BuffSystem.getCategoryCost(player, category);
        }
        totalCost = (long)(totalCost * 0.8); // 20% скидка
        
        html.append("<tr bgcolor=\"333333\">");
        html.append("<td><font color=\"LEVEL\">ВСЕ БАФЫ (скидка 20%):</font></td>");
        html.append("<td align=\"right\"><font color=\"LEVEL\">").append(String.format("%,d", totalCost)).append(" a</font></td>");
        html.append("</tr>");
        html.append("</table><br>");
        
        // Кнопка назад
        html.append("<button value=\"⬅️ Назад в главное меню\" action=\"bypass -h voice .panel\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
        
        html.append("</center></body></html>");
        
        sendHtmlMessage(player, html.toString());
    }
    
    /**
     * Загружает HTML шаблон
     */
    private String loadTemplate(String filename) {
        try {
            return HtmCache.getInstance().getHtm(null, "data/html/panel/" + filename);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Отправляет HTML сообщение игроку
     */
    private voi/*
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

import static com.l2jserver.gameserver.config.Configuration.customs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.