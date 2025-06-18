package com.l2jserver.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.handler.ItemHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.l2jserver.gameserver.config.Configuration.customs;

public class AutoPotionTaskManager implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(AutoPotionTaskManager.class);
    private static final Map<L2Character, L2PcInstance> PLAYERS = new ConcurrentHashMap<>();
    private static boolean _workingautopotion = false;

    private AutoPotionTaskManager() {
        // Расписание для задачи
        ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 0, 2000);
    }

    @Override
    public void run() {
        try {
            if (_workingautopotion) {
                return;
            }
            _workingautopotion = true;

            if (!PLAYERS.isEmpty()) {
                for (Map.Entry<L2Character, L2PcInstance> entry : PLAYERS.entrySet()) {
                    L2PcInstance player = entry.getValue();
                    if ((player == null) || player.isAlikeDead() || (player.isOnlineInt() != 1) || (!customs().AutoPotionsInOlympiad() && player.isInOlympiadMode())) {
                        remove(player);
                        continue;
                    }

                    boolean success = false;
                    if (customs().AutoHpEnabled()) {
                        final boolean restoreHP = ((player.getStatus().getCurrentHp() / player.getMaxHp()) * 100) < customs().getAutoHpPercentage();
                        HP: for (int itemId : customs().getAutoHpItemIdsList()) {
                            final L2ItemInstance hpPotion = player.getInventory().getItemByItemId(itemId);
                            if ((hpPotion != null) && (hpPotion.getCount() > 0)) {
                                success = true;
                                if (restoreHP) {
                                    ItemHandler.getInstance().getHandler(hpPotion.getEtcItem()).useItem(player, hpPotion, false);
                                    player.sendMessage("Auto potion: Restored HP.");
                                    break HP;
                                }
                            }
                        }
                        if (!success) {
                            player.sendMessage("Auto potion: You are out of HP potions!");
                        }
                    }
                    if (customs().AutoCpEnabled()) {
                        final boolean restoreCP = ((player.getStatus().getCurrentCp() / player.getMaxCp()) * 100) < customs().getAutoCpPercentage();
                        CP: for (int itemId : customs().getAutoCpItemIdsList()) {
                            final L2ItemInstance cpPotion = player.getInventory().getItemByItemId(itemId);
                            if ((cpPotion != null) && (cpPotion.getCount() > 0)) {
                                success = true;
                                if (restoreCP) {
                                    ItemHandler.getInstance().getHandler(cpPotion.getEtcItem()).useItem(player, cpPotion, false);
                                    player.sendMessage("Auto potion: Restored CP.");
                                    break CP;
                                }
                            }
                        }
                        if (!success) {
                            player.sendMessage("Auto potion: You are out of CP potions!");
                        }
                    }
                    if (customs().AutoMpEnabled()) {
                        final boolean restoreMP = ((player.getStatus().getCurrentMp() / player.getMaxMp()) * 100) < customs().getAutoMpPercentage();
                        MP: for (int itemId : customs().getAutoMpItemIdsList()) {
                            final L2ItemInstance mpPotion = player.getInventory().getItemByItemId(itemId);
                            if ((mpPotion != null) && (mpPotion.getCount() > 0)) {
                                success = true;
                                if (restoreMP) {
                                    ItemHandler.getInstance().getHandler(mpPotion.getEtcItem()).useItem(player, mpPotion, false);
                                    player.sendMessage("Auto potion: Restored MP.");
                                    break MP;
                                }
                            }
                        }
                        if (!success) {
                            player.sendMessage("Auto potion: You are out of MP potions!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in AutoPotionTaskManager: {}", e.getMessage(), e);
        } finally {
            _workingautopotion = false;
        }
    }

    public void add(L2PcInstance player) {
        if (!PLAYERS.containsValue(player)) {
            PLAYERS.put(player, player);
        }
    }

    public void remove(L2PcInstance player) {
        PLAYERS.remove(player);
    }

    public static AutoPotionTaskManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        protected static final AutoPotionTaskManager INSTANCE = new AutoPotionTaskManager();
    }


}
