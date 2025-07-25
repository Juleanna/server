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
package com.l2jserver.gameserver.model.interfaces;

/**
 * Simple interface for parser, enforces of a fall back value.<br>
 * More suitable for developers not sure about their data.<br>
 * @author xban1x
 */
public interface IParserUtils {
	boolean getBoolean(String key, boolean defaultValue);
	
	byte getByte(String key, byte defaultValue);
	
	short getShort(String key, short defaultValue);
	
	int getInt(String key, int defaultValue);
	
	long getLong(String key, long defaultValue);
	
	float getFloat(String key, float defaultValue);
	
	double getDouble(String key, double defaultValue);
	
	String getString(String key, String defaultValue);
	
	<T extends Enum<T>> T getEnum(String key, Class<T> clazz, T defaultValue);
}
