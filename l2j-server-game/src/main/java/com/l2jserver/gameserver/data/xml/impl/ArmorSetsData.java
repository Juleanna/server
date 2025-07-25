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

import com.l2jserver.gameserver.model.L2ArmorSet;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.util.IXmlReader;

/**
 * Loads armor set bonuses.
 * @author godson
 * @author Luno
 * @author UnAfraid
 */
public final class ArmorSetsData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArmorSetsData.class);
	
	private final Map<Integer, L2ArmorSet> _armorSets = new HashMap<>();
	
	protected ArmorSetsData() {
		load();
	}
	
	@Override
	public void load() {
		_armorSets.clear();
		parseDatapackDirectory("data/stats/armorsets", false);
		LOG.info("Loaded {} armor sets.", _armorSets.size());
	}
	
	@Override
	public void parseDocument(Document doc) {
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("list".equalsIgnoreCase(n.getNodeName())) {
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
					if ("set".equalsIgnoreCase(d.getNodeName())) {
						final L2ArmorSet set = new L2ArmorSet();
						for (Node a = d.getFirstChild(); a != null; a = a.getNextSibling()) {
							final NamedNodeMap attrs = a.getAttributes();
							switch (a.getNodeName()) {
								case "chest" -> set.addChest(parseInteger(attrs, "id"));
								case "feet" -> set.addFeet(parseInteger(attrs, "id"));
								case "gloves" -> set.addGloves(parseInteger(attrs, "id"));
								case "head" -> set.addHead(parseInteger(attrs, "id"));
								case "legs" -> set.addLegs(parseInteger(attrs, "id"));
								case "shield" -> set.addShield(parseInteger(attrs, "id"));
								case "skill" -> set.addSkill(new SkillHolder(parseInteger(attrs, "id"), parseInteger(attrs, "level")));
								case "shield_skill" -> set.addShieldSkill(new SkillHolder(parseInteger(attrs, "id"), parseInteger(attrs, "level")));
								case "enchant6skill" -> set.addEnchant6Skill(new SkillHolder(parseInteger(attrs, "id"), parseInteger(attrs, "level")));
								case "con" -> set.addCon(parseInteger(attrs, "val"));
								case "dex" -> set.addDex(parseInteger(attrs, "val"));
								case "str" -> set.addStr(parseInteger(attrs, "val"));
								case "men" -> set.addMen(parseInteger(attrs, "val"));
								case "wit" -> set.addWit(parseInteger(attrs, "val"));
								case "int" -> set.addInt(parseInteger(attrs, "val"));
							}
						}
						_armorSets.put(set.getChestId(), set);
					}
				}
			}
		}
	}
	
	/**
	 * Checks if is armor set.
	 * @param chestId the chest Id to verify.
	 * @return {@code true} if the chest Id belongs to a registered armor set, {@code false} otherwise.
	 */
	public boolean isArmorSet(int chestId) {
		return _armorSets.containsKey(chestId);
	}
	
	/**
	 * Gets the sets the.
	 * @param chestId the chest Id identifying the armor set.
	 * @return the armor set associated to the give chest Id.
	 */
	public L2ArmorSet getSet(int chestId) {
		return _armorSets.get(chestId);
	}
	
	public static ArmorSetsData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final ArmorSetsData INSTANCE = new ArmorSetsData();
	}
}
