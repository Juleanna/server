/*
 * Copyright © 2004-2026 L2J DataPack
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

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.config.Configuration.customs;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Auto Loot Extension voiced command.
 * @author Maneco2
 * @version 2.6.2.0
 */
public class AutoLoot implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"loot",
		"autoloot",
		"itemloot",
		"herbloot"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance player, String params) {
		if (!customs().autoLootVoiceCommand()) {
			return false;
		}
        switch (command) {
            case "loot" -> {
                player.sendMessage("Using Voices Methods:\n.autoloot: Loot all item(s).\n.itemloot: Loot all better item(s).\n.herbloot: Loot recovery(s) herb(s).");
                if (player.isAutoLoot()) {
                    player.sendMessage("Auto Loot: enabled.");
                }
                if (player.isAutoLootItem()) {
                    player.sendMessage("Auto Loot Item: enabled.");
                }
                if (player.isAutoLootHerb()) {
                    player.sendMessage("Auto Loot Herbs: enabled.");
                }
            }
            case "autoloot" -> {
                if (!character().autoLoot()) {
                    if (player.isAutoLoot()) {
                        player.setAutoLoot(false);
                        player.sendMessage("Auto Loot: disabled.");
                        if (customs().autoLootVoiceRestore()) {
                            player.getVariables().remove("AutoLoot");
                        }
                    } else {
                        player.setAutoLoot(true);
                        player.sendMessage("Auto Loot: enabled.");
                        if (customs().autoLootVoiceRestore()) {
                            player.getVariables().set("AutoLoot", true);
                        }
                    }
                } else {
                    player.sendMessage("Auto Loot already enable.");
                }
            }
            case "itemloot" -> {
                if (player.isAutoLootItem()) {
                    player.setAutoLootItem(false);
                    player.sendMessage("Auto Loot Item: disabled.");
                    if (customs().autoLootVoiceRestore()) {
                        player.getVariables().remove("AutoLootItems");
                    }
                } else {
                    player.setAutoLootItem(true);
                    player.sendMessage("Auto Loot Item: enabled.");
                    if (customs().autoLootVoiceRestore()) {
                        player.getVariables().set("AutoLootItems", true);
                    }

                    if (player.isAutoLoot()) {
                        player.setAutoLoot(false);
                        player.sendMessage("Auto Loot Item is now priority.");
                        if (customs().autoLootVoiceRestore()) {
                            player.getVariables().remove("AutoLoot");
                        }
                    }
                }
            }
            case "herbloot" -> {
                if (player.isAutoLootHerb()) {
                    player.setAutoLootHerbs(false);
                    player.sendMessage("Auto Loot Herbs: disabled.");
                    if (customs().autoLootVoiceRestore()) {
                        player.getVariables().remove("AutoLootHerbs");
                    }
                } else {
                    player.setAutoLootHerbs(true);
                    player.sendMessage("Auto Loot Herbs: enabled.");
                    if (customs().autoLootVoiceRestore()) {
                        player.getVariables().set("AutoLootHerbs", true);
                    }
                }
            }
        }
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}