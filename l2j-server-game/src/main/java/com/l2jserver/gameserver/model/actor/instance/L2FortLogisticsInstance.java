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
package com.l2jserver.gameserver.model.actor.instance;

import java.util.StringTokenizer;

import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.enums.InstanceType;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Vice
 * @author Zoey76
 */
public class L2FortLogisticsInstance extends L2MerchantInstance {
	private static final int[] SUPPLY_BOX_IDS = {
		35665,
		35697,
		35734,
		35766,
		35803,
		35834,
		35866,
		35903,
		35935,
		35973,
		36010,
		36042,
		36080,
		36117,
		36148,
		36180,
		36218,
		36256,
		36293,
		36325,
		36363
	};
	
	public L2FortLogisticsInstance(L2NpcTemplate template) {
		super(template);
		setInstanceType(InstanceType.L2FortLogisticsInstance);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command) {
		if (player.getLastFolkNPC().getObjectId() != getObjectId()) {
			return;
		}
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		if (actualCommand.equalsIgnoreCase("rewards")) {
			if (isMyLord(player)) {
				html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-rewards.htm");
				html.replace("%bloodoath%", String.valueOf(player.getClan().getBloodOathCount()));
			} else {
				html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-noprivs.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		} else if (actualCommand.equalsIgnoreCase("blood")) {
			if (isMyLord(player)) {
				final int blood = player.getClan().getBloodOathCount();
				if (blood > 0) {
					player.addItem("Quest", 9910, blood, this, true);
					player.getClan().resetBloodOathCount();
					html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-blood.htm");
				} else {
					html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-noblood.htm");
				}
			} else {
				html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-noprivs.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		} else if (actualCommand.equalsIgnoreCase("supplylvl")) {
			if (getFort().getFortState() == 2) {
				if (player.isClanLeader()) {
					html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-supplylvl.htm");
					html.replace("%supplylvl%", String.valueOf(getFort().getSupplyLvL()));
				} else {
					html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-noprivs.htm");
				}
			} else {
				html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-1.htm"); // TODO: Missing HTML?
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		} else if (actualCommand.equalsIgnoreCase("supply")) {
			if (isMyLord(player)) {
				if (getFort().getSiege().isInProgress()) {
					html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-siege.htm");
				} else {
					final int level = getFort().getSupplyLvL();
					if (level > 0) {
						// spawn box
						L2NpcTemplate boxTemplate = NpcData.getInstance().getTemplate(SUPPLY_BOX_IDS[level - 1]);
						L2MonsterInstance box = new L2MonsterInstance(boxTemplate);
						box.setCurrentHp(box.getMaxHp());
						box.setCurrentMp(box.getMaxMp());
						box.setHeading(0);
						box.spawnMe(getX() - 23, getY() + 41, getZ());
						
						getFort().setSupplyLvL(0);
						getFort().saveFortVariables();
						
						html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-supply.htm");
					} else {
						html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-nosupply.htm");
					}
				}
			} else {
				html.setFile(player.getHtmlPrefix(), "data/html/fortress/logistics-noprivs.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		} else {
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(L2PcInstance player) {
		showMessageWindow(player, 0);
	}
	
	private void showMessageWindow(L2PcInstance player, int val) {
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		String filename;
		
		if (val == 0) {
			filename = "data/html/fortress/logistics.htm";
		} else {
			filename = "data/html/fortress/logistics-" + val + ".htm";
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(player.getHtmlPrefix(), filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getId()));
		if (getFort().getOwnerClan() != null) {
			html.replace("%clanname%", getFort().getOwnerClan().getName());
		} else {
			html.replace("%clanname%", "NPC");
		}
		player.sendPacket(html);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val) {
		String pom;
		if (val == 0) {
			pom = "logistics";
		} else {
			pom = "logistics-" + val;
		}
		return "data/html/fortress/" + pom + ".htm";
	}
	
	@Override
	public boolean hasRandomAnimation() {
		return false;
	}
}