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
package com.l2jserver.gameserver.instancemanager;

import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.model.itemauction.ItemAuctionInstance;

/**
 * Item Auction Manager.
 * @author Forsaiken
 */
public final class ItemAuctionManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(ItemAuctionManager.class);
	
	private final Map<Integer, ItemAuctionInstance> _managerInstances = new HashMap<>();
	private final AtomicInteger _auctionIds;
	
	protected ItemAuctionManager() {
		_auctionIds = new AtomicInteger(1);
		
		if (!general().itemAuctionEnabled()) {
			LOG.info("Auction Manager disabled by config.");
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s = con.createStatement();
			var rs = s.executeQuery("SELECT auctionId FROM item_auction ORDER BY auctionId DESC LIMIT 0, 1")) {
			if (rs.next()) {
				_auctionIds.set(rs.getInt(1) + 1);
			}
		} catch (Exception e) {
			LOG.error("Failed loading auctions!", e);
		}
		
		final File file = new File(server().getDatapackRoot(), "data/ItemAuctions.xml");
		if (!file.exists()) {
			LOG.warn("Missing ItemAuctions.xml!");
			return;
		}
		
		// TODO(Zoey76): Use IXmlReader.
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		
		try {
			final Document doc = factory.newDocumentBuilder().parse(file);
			for (Node na = doc.getFirstChild(); na != null; na = na.getNextSibling()) {
				if ("list".equalsIgnoreCase(na.getNodeName())) {
					for (Node nb = na.getFirstChild(); nb != null; nb = nb.getNextSibling()) {
						if ("instance".equalsIgnoreCase(nb.getNodeName())) {
							final NamedNodeMap nab = nb.getAttributes();
							final int instanceId = Integer.parseInt(nab.getNamedItem("id").getNodeValue());
							
							if (_managerInstances.containsKey(instanceId)) {
								throw new Exception("Dublicated instanceId " + instanceId);
							}
							
							final ItemAuctionInstance instance = new ItemAuctionInstance(instanceId, _auctionIds, nb);
							_managerInstances.put(instanceId, instance);
						}
					}
				}
			}
			LOG.info("Loaded " + _managerInstances.size() + " auction manager instance(s).");
		} catch (Exception e) {
			LOG.error("Failed loading auctions from xml!", e);
		}
	}
	
	public void shutdown() {
		for (ItemAuctionInstance instance : _managerInstances.values()) {
			instance.shutdown();
		}
	}
	
	public ItemAuctionInstance getManagerInstance(final int instanceId) {
		return _managerInstances.get(instanceId);
	}
	
	public int getNextAuctionId() {
		return _auctionIds.getAndIncrement();
	}
	
	public static void deleteAuction(final int auctionId) {
		try (var con = ConnectionFactory.getInstance().getConnection()) {
			try (var ps = con.prepareStatement("DELETE FROM item_auction WHERE auctionId=?")) {
				ps.setInt(1, auctionId);
				ps.execute();
			}
			
			try (var ps = con.prepareStatement("DELETE FROM item_auction_bid WHERE auctionId=?")) {
				ps.setInt(1, auctionId);
				ps.execute();
			}
		} catch (Exception e) {
			LOG.error("Failed deleting auction ID {}!", auctionId, e);
		}
	}
	
	/**
	 * Gets the single instance of {@code ItemAuctionManager}.
	 * @return single instance of {@code ItemAuctionManager}
	 */
	public static ItemAuctionManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final ItemAuctionManager INSTANCE = new ItemAuctionManager();
	}
}