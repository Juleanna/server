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

import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.options.Options;
import com.l2jserver.gameserver.model.options.OptionsSkillHolder;
import com.l2jserver.gameserver.model.options.OptionsSkillType;
import com.l2jserver.gameserver.model.stats.Stats;
import com.l2jserver.gameserver.model.stats.functions.FuncTemplate;
import com.l2jserver.gameserver.util.IXmlReader;

/**
 * Item Option data.
 * @author UnAfraid
 */
public class OptionData implements IXmlReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(OptionData.class);
	
	private final Map<Integer, Options> _optionData = new HashMap<>();
	
	protected OptionData() {
		load();
	}
	
	@Override
	public synchronized void load() {
		_optionData.clear();
		parseDatapackDirectory("data/stats/options", false);
		LOG.info("Loaded {} item options.", _optionData.size());
	}
	
	@Override
	public void parseDocument(Document doc) {
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("list".equalsIgnoreCase(n.getNodeName())) {
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
					if ("option".equalsIgnoreCase(d.getNodeName())) {
						final int id = parseInteger(d.getAttributes(), "id");
						final Options op = new Options(id);
						
						for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
							switch (cd.getNodeName()) {
								case "for" -> {
									for (Node fd = cd.getFirstChild(); fd != null; fd = fd.getNextSibling()) {
										switch (fd.getNodeName()) {
											case "add", "sub", "mul", "div", "set", "share", "enchant", "enchanthp" -> parseFuncs(fd.getAttributes(), fd.getNodeName(), op);
										}
									}
								}
								case "active_skill" -> op.setActiveSkill(new SkillHolder(parseInteger(cd.getAttributes(), "id"), parseInteger(cd.getAttributes(), "level")));
								case "passive_skill" -> op.setPassiveSkill(new SkillHolder(parseInteger(cd.getAttributes(), "id"), parseInteger(cd.getAttributes(), "level")));
								case "attack_skill" -> op.addActivationSkill(new OptionsSkillHolder(parseInteger(cd.getAttributes(), "id"), parseInteger(cd.getAttributes(), "level"), parseDouble(cd.getAttributes(), "chance"), OptionsSkillType.ATTACK));
								case "magic_skill" -> op.addActivationSkill(new OptionsSkillHolder(parseInteger(cd.getAttributes(), "id"), parseInteger(cd.getAttributes(), "level"), parseDouble(cd.getAttributes(), "chance"), OptionsSkillType.MAGIC));
								case "critical_skill" -> op.addActivationSkill(new OptionsSkillHolder(parseInteger(cd.getAttributes(), "id"), parseInteger(cd.getAttributes(), "level"), parseDouble(cd.getAttributes(), "chance"), OptionsSkillType.CRITICAL));
							}
						}
						_optionData.put(op.getId(), op);
					}
				}
			}
		}
	}
	
	private void parseFuncs(NamedNodeMap attrs, String functionName, Options op) {
		Stats stat = Stats.valueOfXml(parseString(attrs, "stat"));
		double val = parseDouble(attrs, "val");
		int order = -1;
		final Node orderNode = attrs.getNamedItem("order");
		if (orderNode != null) {
			order = Integer.parseInt(orderNode.getNodeValue());
		}
		op.addFunc(new FuncTemplate(null, null, functionName, order, stat, val));
	}
	
	public Options getOptions(int id) {
		return _optionData.get(id);
	}
	
	public static OptionData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final OptionData INSTANCE = new OptionData();
	}
}
