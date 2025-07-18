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
package com.l2jserver.gameserver.config.converter;

import java.io.File;
import java.lang.reflect.Method;

import org.aeonbits.owner.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File Converter.
 * @author Zoey76
 * @version 2.6.1.0
 */
public class FileConverter implements Converter<File> {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileConverter.class);
	
	@Override
	public File convert(Method method, String input) {
		try {
			return new File(input.replace('\\', '/')).getCanonicalFile();
		} catch (Exception ex) {
			LOG.warn("Error setting script root!", ex);
			return new File(".");
		}
	}
}
