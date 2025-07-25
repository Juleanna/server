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
package com.l2jserver.gameserver.network.clientpackets;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.Objects;

import com.l2jserver.gameserver.enums.ItemLocation;
import com.l2jserver.gameserver.enums.PrivateStoreType;
import com.l2jserver.gameserver.instancemanager.MailManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Message;
import com.l2jserver.gameserver.model.itemcontainer.ItemContainer;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExChangePostState;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.ItemList;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * @author Migi
 * @author DS
 */
public final class RequestCancelPostAttachment extends L2GameClientPacket {
	private static final String _C__D0_6F_REQUESTCANCELPOSTATTACHMENT = "[C] D0:6F RequestCancelPostAttachment";
	
	private int _msgId;
	
	@Override
	protected void readImpl() {
		_msgId = readD();
	}
	
	@Override
	public void runImpl() {
		final L2PcInstance activeChar = getClient().getActiveChar();
		if ((activeChar == null) || !general().allowMail() || !general().allowAttachments()) {
			return;
		}
		
		if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("cancelpost")) {
			return;
		}
		
		Message msg = MailManager.getInstance().getMessage(_msgId);
		if (msg == null) {
			return;
		}
		if (msg.getSenderId() != activeChar.getObjectId()) {
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to cancel not own post!");
			return;
		}
		
		if (!activeChar.isInsideZone(ZoneId.PEACE)) {
			activeChar.sendPacket(SystemMessageId.CANT_CANCEL_NOT_IN_PEACE_ZONE);
			return;
		}
		
		if (activeChar.getActiveTradeList() != null) {
			activeChar.sendPacket(SystemMessageId.CANT_CANCEL_DURING_EXCHANGE);
			return;
		}
		
		if (activeChar.isEnchanting()) {
			activeChar.sendPacket(SystemMessageId.CANT_CANCEL_DURING_ENCHANT);
			return;
		}
		
		if (activeChar.getPrivateStoreType() != PrivateStoreType.NONE) {
			activeChar.sendPacket(SystemMessageId.CANT_CANCEL_PRIVATE_STORE);
			return;
		}
		
		if (!msg.hasAttachments()) {
			activeChar.sendPacket(SystemMessageId.YOU_CANT_CANCEL_RECEIVED_MAIL);
			return;
		}
		
		final ItemContainer attachments = msg.getAttachments();
		if ((attachments == null) || (attachments.getSize() == 0)) {
			activeChar.sendPacket(SystemMessageId.YOU_CANT_CANCEL_RECEIVED_MAIL);
			return;
		}
		
		int weight = 0;
		int slots = 0;
		
		for (L2ItemInstance item : attachments.getItems()) {
			if (item == null) {
				continue;
			}
			
			if (item.getOwnerId() != activeChar.getObjectId()) {
				Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get not own item from cancelled attachment!");
				return;
			}
			
			if (item.getItemLocation() != ItemLocation.MAIL) {
				Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get items not from mail !");
				return;
			}
			
			if (item.getLocationSlot() != msg.getId()) {
				Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to get items from different attachment!");
				return;
			}
			
			weight += item.getCount() * item.getItem().getWeight();
			if (!item.isStackable()) {
				slots += item.getCount();
			} else if (activeChar.getInventory().getItemByItemId(item.getId()) == null) {
				slots++;
			}
		}
		
		if (!activeChar.getInventory().validateCapacity(slots)) {
			activeChar.sendPacket(SystemMessageId.CANT_CANCEL_INVENTORY_FULL);
			return;
		}
		
		if (!activeChar.getInventory().validateWeight(weight)) {
			activeChar.sendPacket(SystemMessageId.CANT_CANCEL_INVENTORY_FULL);
			return;
		}
		
		// Proceed to the transfer
		InventoryUpdate playerIU = general().forceInventoryUpdate() ? null : new InventoryUpdate();
		for (L2ItemInstance item : attachments.getItems()) {
			if (item == null) {
				continue;
			}
			
			long count = item.getCount();
			final L2ItemInstance newItem = attachments.transferItem(attachments.getName(), item.getObjectId(), count, activeChar.getInventory(), activeChar, null);
			if (newItem == null) {
				return;
			}
			
			if (playerIU != null) {
				if (newItem.getCount() > count) {
					playerIU.addModifiedItem(newItem);
				} else {
					playerIU.addNewItem(newItem);
				}
			}
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_ACQUIRED_S2_S1);
			sm.addItemName(item.getId());
			sm.addLong(count);
			activeChar.sendPacket(sm);
		}
		
		msg.removeAttachments();
		
		// Send updated item list to the player
		activeChar.sendPacket(Objects.requireNonNullElseGet(playerIU, () -> new ItemList(activeChar, false)));
		
		// Update current load status on player
		StatusUpdate su = new StatusUpdate(activeChar);
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
		
		final L2PcInstance receiver = L2World.getInstance().getPlayer(msg.getReceiverId());
		if (receiver != null) {
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANCELLED_MAIL);
			sm.addCharName(activeChar);
			receiver.sendPacket(sm);
			receiver.sendPacket(new ExChangePostState(true, _msgId, Message.DELETED));
		}
		
		MailManager.getInstance().deleteMessageInDb(_msgId);
		
		activeChar.sendPacket(new ExChangePostState(false, _msgId, Message.DELETED));
		activeChar.sendPacket(SystemMessageId.MAIL_SUCCESSFULLY_CANCELLED);
	}
	
	@Override
	public String getType() {
		return _C__D0_6F_REQUESTCANCELPOSTATTACHMENT;
	}
	
	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
