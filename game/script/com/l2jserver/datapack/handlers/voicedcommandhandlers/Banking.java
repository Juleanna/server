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

import static com.l2jserver.gameserver.config.Configuration.customs;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class trades Gold Bars for Adena and vice versa.
 * @author Ahmed
 */
public class Banking implements IVoicedCommandHandler {
	private static final String[] _voicedCommands = {
		"bank",
		"withdraw",
		"deposit"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		if (command.equals("bank")) {
			activeChar.sendMessage(".deposit (" + customs().getBankingAdenaCount() + " Adena = " + customs().getBankingGoldbarCount() + " Goldbar) / .withdraw (" + customs().getBankingGoldbarCount() + " Goldbar = " + customs().getBankingAdenaCount() + " Adena)");
		} else if (command.equals("deposit")) {
			if (activeChar.getInventory().getInventoryItemCount(57, 0) >= customs().getBankingAdenaCount()) {
				if (!activeChar.reduceAdena("Goldbar", customs().getBankingAdenaCount(), activeChar, false)) {
					return false;
				}
				activeChar.getInventory().addItem("Goldbar", 3470, customs().getBankingGoldbarCount(), activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendMessage("Thank you, you now have " + customs().getBankingGoldbarCount() + " Goldbar(s), and " + customs().getBankingAdenaCount() + " less adena.");
			} else {
				activeChar.sendMessage("You do not have enough Adena to convert to Goldbar(s), you need " + customs().getBankingAdenaCount() + " Adena.");
			}
		} else if (command.equals("withdraw")) {
			if (activeChar.getInventory().getInventoryItemCount(3470, 0) >= customs().getBankingGoldbarCount()) {
				if (!activeChar.destroyItemByItemId("Adena", 3470, customs().getBankingGoldbarCount(), activeChar, false)) {
					return false;
				}
				activeChar.getInventory().addAdena("Adena", customs().getBankingAdenaCount(), activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendMessage("Thank you, you now have " + customs().getBankingAdenaCount() + " Adena, and " + customs().getBankingGoldbarCount() + " less Goldbar(s).");
			} else {
				activeChar.sendMessage("You do not have any Goldbars to turn into " + customs().getBankingAdenaCount() + " Adena.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return _voicedCommands;
	}
}