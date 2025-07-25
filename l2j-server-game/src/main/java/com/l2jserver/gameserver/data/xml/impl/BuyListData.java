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
package com.l2jserver.gameserver.data.xml.impl;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.buylist.Product;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.util.IXmlReader;
import com.l2jserver.gameserver.util.file.filter.NumericNameFilter;

/**
 * Loads buy lists for NPCs.
 * @author NosBit
 */
public final class BuyListData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(BuyListData.class);
	
	private static final FileFilter NUMERIC_FILTER = new NumericNameFilter();
	
	private final Map<Integer, L2BuyList> _buyLists = new HashMap<>();
	
	protected BuyListData() {
		load();
	}
	
	@Override
	public synchronized void load() {
		_buyLists.clear();
		parseDatapackDirectory("data/buylists", false);
		if (general().customBuyListLoad()) {
			parseDatapackDirectory("data/buylists/custom", false);
		}
		
		LOG.info("Loaded {} buy lists.", _buyLists.size());
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var statement = con.createStatement();
			var rs = statement.executeQuery("SELECT * FROM `buylists`")) {
			while (rs.next()) {
				int buyListId = rs.getInt("buylist_id");
				int itemId = rs.getInt("item_id");
				long count = rs.getLong("count");
				long nextRestockTime = rs.getLong("next_restock_time");
				final L2BuyList buyList = getBuyList(buyListId);
				if (buyList == null) {
					LOG.warn("Buy list {} found in database but not loaded from XML!", buyListId);
					continue;
				}
				final Product product = buyList.getProductByItemId(itemId);
				if (product == null) {
					LOG.warn("Item Id {} found in database but not loaded from XML {}!", itemId, buyListId);
					continue;
				}
				if (count < product.getMaxCount()) {
					product.setCount(count);
					product.restartRestockTask(nextRestockTime);
				}
			}
		} catch (Exception ex) {
			LOG.warn("Failed to load buy list data from database.", ex);
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f) {
		try {
			final int buyListId = Integer.parseInt(f.getName().replaceAll(".xml", ""));
			
			for (Node node = doc.getFirstChild(); node != null; node = node.getNextSibling()) {
				if ("list".equalsIgnoreCase(node.getNodeName())) {
					final L2BuyList buyList = new L2BuyList(buyListId);
					for (Node list_node = node.getFirstChild(); list_node != null; list_node = list_node.getNextSibling()) {
						if ("item".equalsIgnoreCase(list_node.getNodeName())) {
							long price = -1;
							long restockDelay = -1;
							long count = -1;
							NamedNodeMap attrs = list_node.getAttributes();
							Node attr = attrs.getNamedItem("id");
							int itemId = Integer.parseInt(attr.getNodeValue());
							attr = attrs.getNamedItem("price");
							if (attr != null) {
								price = Long.parseLong(attr.getNodeValue());
							}
							attr = attrs.getNamedItem("restock_delay");
							if (attr != null) {
								restockDelay = Long.parseLong(attr.getNodeValue());
							}
							attr = attrs.getNamedItem("count");
							if (attr != null) {
								count = Long.parseLong(attr.getNodeValue());
							}
							final L2Item item = ItemTable.getInstance().getTemplate(itemId);
							if (item != null) {
								buyList.addProduct(new Product(buyList.getListId(), item, price, restockDelay, count));
							} else {
								LOG.warn("Item not found. BuyList: {} Item ID: {} File: {}", buyList.getListId(), itemId, f.getName());
							}
						} else if ("npcs".equalsIgnoreCase(list_node.getNodeName())) {
							for (Node npcs_node = list_node.getFirstChild(); npcs_node != null; npcs_node = npcs_node.getNextSibling()) {
								if ("npc".equalsIgnoreCase(npcs_node.getNodeName())) {
									int npcId = Integer.parseInt(npcs_node.getTextContent());
									buyList.addAllowedNpc(npcId);
								}
							}
						}
					}
					_buyLists.put(buyList.getListId(), buyList);
				}
			}
		} catch (Exception ex) {
			LOG.warn("Failed to load buy list data from XML {}!", f.getName(), ex);
		}
	}
	
	@Override
	public FileFilter getCurrentFileFilter() {
		return NUMERIC_FILTER;
	}
	
	public L2BuyList getBuyList(int listId) {
		return _buyLists.get(listId);
	}
	
	public static BuyListData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final BuyListData INSTANCE = new BuyListData();
	}
}
