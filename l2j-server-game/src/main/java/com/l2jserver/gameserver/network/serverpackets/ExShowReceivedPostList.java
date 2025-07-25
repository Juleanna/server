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
package com.l2jserver.gameserver.network.serverpackets;

import java.util.List;

import com.l2jserver.gameserver.instancemanager.MailManager;
import com.l2jserver.gameserver.model.entity.Message;

/**
 * @author Migi
 * @author DS
 */
public class ExShowReceivedPostList extends L2GameServerPacket {
	private final List<Message> _inbox;
	
	public ExShowReceivedPostList(int objectId) {
		_inbox = MailManager.getInstance().getInbox(objectId);
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0xAA);
		writeD((int) (System.currentTimeMillis() / 1000));
		if ((_inbox != null) && (_inbox.size() > 0)) {
			writeD(_inbox.size());
			for (Message msg : _inbox) {
				writeD(msg.getId());
				writeS(msg.getSubject());
				writeS(msg.getSenderName());
				writeD(msg.isLocked() ? 0x01 : 0x00);
				writeD(msg.getExpirationSeconds());
				writeD(msg.isUnread() ? 0x01 : 0x00);
				writeD(0x01);
				writeD(msg.hasAttachments() ? 0x01 : 0x00);
				writeD(msg.isReturned() ? 0x01 : 0x00);
				writeD(msg.getSendBySystem());
				writeD(0x00);
			}
		} else {
			writeD(0x00);
		}
	}
}
