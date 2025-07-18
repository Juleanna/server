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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.gameserver.model.SiegeScheduleDate;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.util.IXmlReader;
import com.l2jserver.gameserver.util.Util;

/**
 * Siege Schedule data.
 * @author UnAfraid
 */
public class SiegeScheduleData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiegeScheduleData.class);
	
	private final List<SiegeScheduleDate> _scheduleData = new ArrayList<>();
	
	protected SiegeScheduleData() {
		load();
	}
	
	@Override
	public synchronized void load() {
		_scheduleData.clear();
		parseFile(new File("config/SiegeSchedule.xml"));
		LOG.info("Loaded {} siege schedulers.", _scheduleData.size());
		
		if (_scheduleData.isEmpty()) {
			_scheduleData.add(new SiegeScheduleDate(new StatsSet()));
			LOG.info("Emergency Loaded {} default siege schedulers.", _scheduleData.size());
		}
	}
	
	@Override
	public void parseDocument(Document doc) {
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("list".equalsIgnoreCase(n.getNodeName())) {
				for (Node cd = n.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
					if ("schedule".equals(cd.getNodeName())) {
						final StatsSet set = new StatsSet();
						final NamedNodeMap attrs = cd.getAttributes();
						for (int i = 0; i < attrs.getLength(); i++) {
							Node node = attrs.item(i);
							String key = node.getNodeName();
							String val = node.getNodeValue();
							if ("day".equals(key)) {
								if (!Util.isDigit(val)) {
									val = Integer.toString(getValueForField(val));
								}
							}
							set.set(key, val);
						}
						_scheduleData.add(new SiegeScheduleDate(set));
					}
				}
			}
		}
	}
	
	private int getValueForField(String field) {
		try {
			return Calendar.class.getField(field).getInt(Calendar.class);
		} catch (Exception ex) {
			LOG.warn("Unable to get value!", ex);
			return -1;
		}
	}
	
	public List<SiegeScheduleDate> getScheduleDates() {
		return _scheduleData;
	}
	
	public static SiegeScheduleData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final SiegeScheduleData INSTANCE = new SiegeScheduleData();
	}
}
