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
package com.l2jserver.gameserver.script;

/**
 * @author -Nemesiss-
 */
public class ShortList {
	public static short[] parse(String range) {
		if (range.contains("-")) {
			return getShortList(range.split("-"));
		} else if (range.contains(",")) {
			return getShortList(range.split(","));
		}
		
		return new short[] {
			getShort(range)
		};
	}
	
	private static short getShort(String number) {
		return Short.parseShort(number);
	}
	
	private static short[] getShortList(String[] numbers) {
		short[] list = new short[numbers.length];
		for (int i = 0; i < list.length; i++) {
			list[i] = getShort(numbers[i]);
		}
		return list;
	}
}
