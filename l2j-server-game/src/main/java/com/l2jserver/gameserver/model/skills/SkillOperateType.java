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
package com.l2jserver.gameserver.model.skills;

/**
 * This enum class holds the skill operative types:
 * <ul>
 * <li>A1</li>
 * <li>A2</li>
 * <li>A3</li>
 * <li>A4</li>
 * <li>CA1</li>
 * <li>CA5</li>
 * <li>DA1</li>
 * <li>DA2</li>
 * <li>P</li>
 * <li>T</li>
 * </ul>
 * @author Zoey76
 */
public enum SkillOperateType {
	/**
	 * Active Skill with "Instant Effect" (for example damage skills heal/pdam/mdam/cpdam skills).
	 */
	A1,
	
	/**
	 * Active Skill with "Continuous effect + Instant effect" (for example buff/debuff or damage/heal over time skills).
	 */
	A2,
	
	/**
	 * Active Skill with "Instant effect + Continuous effect"
	 */
	A3,
	
	/**
	 * Active Skill with "Instant effect + ?" used for special event herb.
	 */
	A4,
	
	/**
	 * Continuous Active Skill with "instant effect" (instant effect casted by ticks).
	 */
	CA1,
	
	/**
	 * Continuous Active Skill with "continuous effect" (continuous effect casted by ticks).
	 */
	CA5,
	
	/**
	 * Directional Active Skill with "Charge/Rush instant effect".
	 */
	DA1,
	
	/**
	 * Directional Active Skill with "Charge/Rush Continuous effect".
	 */
	DA2,
	
	/**
	 * Passive Skill.
	 */
	P,
	
	/**
	 * Toggle Skill.
	 */
	T;
	
	/**
	 * Verifies if the operative type correspond to an active skill.
	 * @return {@code true} if the operative skill type is active, {@code false} otherwise
	 */
	public boolean isActive() {
		return switch (this) {
			case A1, A2, A3, A4, CA1, CA5, DA1, DA2 -> true;
			default -> false;
		};
	}
	
	/**
	 * Verifies if the operative type correspond to a continuous skill.
	 * @return {@code true} if the operative skill type is continuous, {@code false} otherwise
	 */
	public boolean isContinuous() {
		return switch (this) {
			case A2, A4, DA2 -> true;
			default -> false;
		};
	}
	
	/**
	 * Verifies if the operative type correspond to a continuous skill.
	 * @return {@code true} if the operative skill type is continuous, {@code false} otherwise
	 */
	public boolean isSelfContinuous() {
		return (this == A3);
	}
	
	/**
	 * Verifies if the operative type correspond to a passive skill.
	 * @return {@code true} if the operative skill type is passive, {@code false} otherwise
	 */
	public boolean isPassive() {
		return (this == P);
	}
	
	/**
	 * Verifies if the operative type correspond to a toggle skill.
	 * @return {@code true} if the operative skill type is toggle, {@code false} otherwise
	 */
	public boolean isToggle() {
		return (this == T);
	}
	
	/**
	 * Verifies if the operative type correspond to a channeling skill.
	 * @return {@code true} if the operative skill type is channeling, {@code false} otherwise
	 */
	public boolean isChanneling() {
		return switch (this) {
			case CA1, CA5 -> true;
			default -> false;
		};
	}
	
	/**
	 * Verifies if the operative type correspond to a fly skill.
	 * @return {@code true} if the operative skill type is fly, {@code false} otherwise
	 */
	public boolean isFlyType() {
		return switch (this) {
			case DA1, DA2 -> true;
			default -> false;
		};
	}
}
