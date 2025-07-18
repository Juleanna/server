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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.l2jserver.gameserver.model.Location;

/**
 * Location Converter test.
 * @author Zoey76
 * @version 2.6.3.0
 */
public class LocationConverterTest {
	
	private static final LocationConverter CONVERTER = new LocationConverter();
	
	@ParameterizedTest
	@MethodSource("provideLocations")
	public void convertTest(String input, Location expected) {
		assertEquals(CONVERTER.convert(null, input), expected);
	}
	
	public static Object[][] provideLocations() {
		return new Object[][] {
			{
				"83425,148585,-3406",
				new Location(83425, 148585, -3406)
			},
			{
				"148695,46725,-3414,200",
				new Location(148695, 46725, -3414, 200)
			},
			{
				"149999,46728,-3414,200,5000",
				new Location(149999, 46728, -3414, 200, 5000)
			}
		};
	}
}
