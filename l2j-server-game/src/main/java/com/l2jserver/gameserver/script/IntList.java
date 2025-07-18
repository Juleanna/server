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
 * @author Luis Arias
 */
public class IntList {
	public static int[] parse(String range) {
		if (range.contains("-")) {
			return getIntegerRange(range.split("-"));
		} else if (range.contains(",")) {
			return getIntegerList(range.split(","));
		}
		
		return new int[] {
			getInt(range)
		};
	}
	
	private static int getInt(String number) {
		return Integer.parseInt(number);
	}
	
	private static int[] getIntegerList(String[] numbers) {
		int[] list = new int[numbers.length];
		for (int i = 0; i < list.length; i++) {
			list[i] = getInt(numbers[i]);
		}
		return list;
	}
	
	private static int[] getIntegerRange(String[] numbers) {
		int min = getInt(numbers[0]);
		int max = getInt(numbers[1]);
		int[] list = new int[(max - min) + 1];
		for (int i = 0; i < list.length; i++) {
			list[i] = min + i;
		}
		return list;
	}
}
