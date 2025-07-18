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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.fishing.L2Fish;
import com.l2jserver.gameserver.util.IXmlReader;

/**
 * Fish data parser.
 * @author nonom
 */
public final class FishData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(FishData.class);
	
	private final Map<Integer, L2Fish> _fishNormal = new HashMap<>();
	
	private final Map<Integer, L2Fish> _fishEasy = new HashMap<>();
	
	private final Map<Integer, L2Fish> _fishHard = new HashMap<>();
	
	protected FishData() {
		load();
	}
	
	@Override
	public void load() {
		_fishEasy.clear();
		_fishNormal.clear();
		_fishHard.clear();
		parseDatapackFile("data/stats/fishing/fishes.xml");
		LOG.info("Loaded {} fish.", (_fishEasy.size() + _fishNormal.size() + _fishHard.size()));
	}
	
	@Override
	public void parseDocument(Document doc) {
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("list".equalsIgnoreCase(n.getNodeName())) {
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
					if ("fish".equalsIgnoreCase(d.getNodeName())) {
						final NamedNodeMap attrs = d.getAttributes();
						
						final StatsSet set = new StatsSet();
						for (int i = 0; i < attrs.getLength(); i++) {
							final Node att = attrs.item(i);
							set.set(att.getNodeName(), att.getNodeValue());
						}
						
						final L2Fish fish = new L2Fish(set);
						switch (fish.getFishGrade()) {
							case 0 -> _fishEasy.put(fish.getFishId(), fish);
							case 1 -> _fishNormal.put(fish.getFishId(), fish);
							case 2 -> _fishHard.put(fish.getFishId(), fish);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gets the fish.
	 * @param level the fish Level
	 * @param group the fish Group
	 * @param grade the fish Grade
	 * @return List of Fish that can be fished
	 */
	public List<L2Fish> getFish(int level, int group, int grade) {
		final ArrayList<L2Fish> result = new ArrayList<>();
		Map<Integer, L2Fish> fish;
		switch (grade) {
			case 0 -> fish = _fishEasy;
			case 1 -> fish = _fishNormal;
			case 2 -> fish = _fishHard;
			default -> {
				LOG.warn("Unmanaged fish grade!");
				return result;
			}
		}
		
		for (L2Fish f : fish.values()) {
			if ((f.getFishLevel() != level) || (f.getFishGroup() != group)) {
				continue;
			}
			result.add(f);
		}
		
		if (result.isEmpty()) {
			LOG.warn("Cannot find any fish for level: {} group: {} and grade: {}!", level, group, grade);
		}
		return result;
	}
	
	public static FishData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final FishData INSTANCE = new FishData();
	}
}
