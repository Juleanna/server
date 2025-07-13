/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
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
import com.l2jserver.gameserver.taskmanager.AutoPotionTaskManager;

/**
 * @author Mobius, Gigi
 */
public class AutoPotion implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"apon",
		"apoff",
		"potionon",
		"potionoff"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
	
		if (!customs().AutoPotionsEnabled() || (activeChar == null))
		{
			return false;
		}
		if (activeChar.getLevel() < customs().getAutoPotionMinimumLevel())
		{
			activeChar.sendMessage("You need to be at least " + customs().getAutoPotionMinimumLevel() + " to use auto potions.");
			return false;
		}
		
		switch (command)
		{
			case "apon":
			case "potionon":
			{
				AutoPotionTaskManager.getInstance().add(activeChar);
				activeChar.sendMessage("Auto potions is enabled.");
				break;
			}
			case "apoff":
			case "potionoff":
			{
				AutoPotionTaskManager.getInstance().remove(activeChar);
				activeChar.sendMessage("Auto potions is disabled.");
				break;
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}