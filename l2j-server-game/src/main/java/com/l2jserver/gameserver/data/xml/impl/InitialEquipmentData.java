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

import static com.l2jserver.gameserver.config.Configuration.character;

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
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.items.PcItemTemplate;
import com.l2jserver.gameserver.util.IXmlReader;

/**
 * This class holds the Initial Equipment information.<br>
 * What items get each newly created character and if this item is equipped upon creation (<b>Requires the item to be equippable</b>).
 * @author Zoey76
 */
public final class InitialEquipmentData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(InitialEquipmentData.class);
	
	private static final String NORMAL = "data/stats/initialEquipment.xml";
	
	private static final String EVENT = "data/stats/initialEquipmentEvent.xml";
	
	private final Map<ClassId, List<PcItemTemplate>> _initialEquipmentList = new HashMap<>();
	
	protected InitialEquipmentData() {
		load();
	}
	
	@Override
	public void load() {
		_initialEquipmentList.clear();
		parseDatapackFile(character().initialEquipmentEvent() ? EVENT : NORMAL);
		LOG.info("Loaded {} initial equipment data.", _initialEquipmentList.size());
	}
	
	@Override
	public void parseDocument(Document doc) {
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("list".equalsIgnoreCase(n.getNodeName())) {
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
					if ("equipment".equalsIgnoreCase(d.getNodeName())) {
						parseEquipment(d);
					}
				}
			}
		}
	}
	
	/**
	 * Parses the equipment.
	 * @param d parse an initial equipment and add it to {@link #_initialEquipmentList}
	 */
	private void parseEquipment(Node d) {
		NamedNodeMap attrs = d.getAttributes();
		final ClassId classId = ClassId.getClassId(Integer.parseInt(attrs.getNamedItem("classId").getNodeValue()));
		final List<PcItemTemplate> equipList = new ArrayList<>();
		for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling()) {
			if ("item".equalsIgnoreCase(c.getNodeName())) {
				final StatsSet set = new StatsSet();
				attrs = c.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					set.set(attr.getNodeName(), attr.getNodeValue());
				}
				equipList.add(new PcItemTemplate(set));
			}
		}
		_initialEquipmentList.put(classId, equipList);
	}
	
	/**
	 * Gets the equipment list.
	 * @param cId the class Id for the required initial equipment.
	 * @return the initial equipment for the given class Id.
	 */
	public List<PcItemTemplate> getEquipmentList(ClassId cId) {
		return _initialEquipmentList.get(cId);
	}
	
	/**
	 * Gets the equipment list.
	 * @param cId the class Id for the required initial equipment.
	 * @return the initial equipment for the given class Id.
	 */
	public List<PcItemTemplate> getEquipmentList(int cId) {
		return _initialEquipmentList.get(ClassId.getClassId(cId));
	}
	
	public static InitialEquipmentData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final InitialEquipmentData INSTANCE = new InitialEquipmentData();
	}
}