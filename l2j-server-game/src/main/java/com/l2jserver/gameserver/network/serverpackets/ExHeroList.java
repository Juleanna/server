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

import java.util.Map;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.entity.Hero;
import com.l2jserver.gameserver.model.olympiad.Olympiad;

/**
 * @author -Wooden-
 * @author KenM
 * @author godson
 */
public class ExHeroList extends L2GameServerPacket {
	private final Map<Integer, StatsSet> _heroList;
	
	public ExHeroList() {
		_heroList = Hero.getInstance().getHeroes();
	}
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0x79);
		writeD(_heroList.size());
		
		for (Integer heroId : _heroList.keySet()) {
			StatsSet hero = _heroList.get(heroId);
			writeS(hero.getString(Olympiad.CHAR_NAME));
			writeD(hero.getInt(Olympiad.CLASS_ID));
			writeS(hero.getString(Hero.CLAN_NAME, ""));
			writeD(hero.getInt(Hero.CLAN_CREST, 0));
			writeS(hero.getString(Hero.ALLY_NAME, ""));
			writeD(hero.getInt(Hero.ALLY_CREST, 0));
			writeD(hero.getInt(Hero.COUNT));
		}
	}
}