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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.gameserver.util.IXmlReader;

/**
 * Karma data.
 * @author UnAfraid
 */
public class KarmaData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(KarmaData.class);
	
	private final Map<Integer, Double> _karmaTable = new HashMap<>();
	
	public KarmaData() {
		load();
	}
	
	@Override
	public synchronized void load() {
		_karmaTable.clear();
		parseDatapackFile("data/stats/chars/pcKarmaIncrease.xml");
		LOG.info("Loaded {} karma modifiers.", _karmaTable.size());
	}
	
	@Override
	public void parseDocument(Document doc) {
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("pcKarmaIncrease".equalsIgnoreCase(n.getNodeName())) {
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
					if ("increase".equalsIgnoreCase(d.getNodeName())) {
						final NamedNodeMap attrs = d.getAttributes();
						_karmaTable.put(parseInteger(attrs, "lvl"), parseDouble(attrs, "val"));
					}
				}
			}
		}
	}
	
	/**
	 * @param level
	 * @return {@code double} modifier used to calculate karma lost upon death.
	 */
	public double getMultiplier(int level) {
		return _karmaTable.get(level);
	}
	
	public static KarmaData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final KarmaData INSTANCE = new KarmaData();
	}
}
