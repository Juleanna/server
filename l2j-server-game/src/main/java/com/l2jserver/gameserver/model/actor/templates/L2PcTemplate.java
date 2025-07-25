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
package com.l2jserver.gameserver.model.actor.templates;

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

/**
 * @author mkizub
 * @author Zoey76
 */
public class L2PcTemplate extends L2CharTemplate {
	private final ClassId _classId;
	
	private final float[] _baseHp;
	private final float[] _baseMp;
	private final float[] _baseCp;
	
	private final double[] _baseHpReg;
	private final double[] _baseMpReg;
	private final double[] _baseCpReg;
	
	private final double _fCollisionHeightFemale;
	private final double _fCollisionRadiusFemale;
	
	private final int _baseSafeFallHeight;
	
	private final Map<Integer, Integer> _baseSlotDef;
	
	public L2PcTemplate(StatsSet set) {
		super(set);
		_classId = ClassId.getClassId(set.getInt("classId"));
		setRace(_classId.getRace());
		_baseHp = new float[character().getMaxPlayerLevel() + 1];
		_baseMp = new float[character().getMaxPlayerLevel() + 1];
		_baseCp = new float[character().getMaxPlayerLevel() + 1];
		_baseHpReg = new double[character().getMaxPlayerLevel() + 1];
		_baseMpReg = new double[character().getMaxPlayerLevel() + 1];
		_baseCpReg = new double[character().getMaxPlayerLevel() + 1];
		
		_baseSlotDef = new HashMap<>(12);
		_baseSlotDef.put(Inventory.PAPERDOLL_CHEST, set.getInt("basePDefchest", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_LEGS, set.getInt("basePDeflegs", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_HEAD, set.getInt("basePDefhead", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_FEET, set.getInt("basePDeffeet", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_GLOVES, set.getInt("basePDefgloves", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_UNDER, set.getInt("basePDefunderwear", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_CLOAK, set.getInt("basePDefcloak", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_REAR, set.getInt("baseMDefrear", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_LEAR, set.getInt("baseMDeflear", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_RFINGER, set.getInt("baseMDefrfinger", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_LFINGER, set.getInt("baseMDefrfinger", 0));
		_baseSlotDef.put(Inventory.PAPERDOLL_NECK, set.getInt("baseMDefneck", 0));
		
		_fCollisionRadiusFemale = set.getDouble("collisionFemaleradius");
		_fCollisionHeightFemale = set.getDouble("collisionFemaleheight");
		
		_baseSafeFallHeight = set.getInt("baseSafeFall", 333);
	}
	
	/**
	 * @return the template class Id.
	 */
	public ClassId getClassId() {
		return _classId;
	}
	
	/**
	 * Sets the value of level upgain parameter.
	 * @param paramName name of parameter
	 * @param level corresponding character level
	 * @param val value of parameter
	 */
	public void setUpgainValue(String paramName, int level, double val) {
		switch (paramName) {
			case "hp" -> _baseHp[level] = (float) val;
			case "mp" -> _baseMp[level] = (float) val;
			case "cp" -> _baseCp[level] = (float) val;
			case "hpRegen" -> _baseHpReg[level] = val;
			case "mpRegen" -> _baseMpReg[level] = val;
			case "cpRegen" -> _baseCpReg[level] = val;
		}
	}
	
	/**
	 * @param level character level to return value
	 * @return the baseHpMax for given character level
	 */
	public float getBaseHpMax(int level) {
		return _baseHp[level];
	}
	
	/**
	 * @param level character level to return value
	 * @return the baseMpMax for given character level
	 */
	public float getBaseMpMax(int level) {
		return _baseMp[level];
	}
	
	/**
	 * @param level character level to return value
	 * @return the baseCpMax for given character level
	 */
	public float getBaseCpMax(int level) {
		return _baseCp[level];
	}
	
	/**
	 * @param level character level to return value
	 * @return the base HP Regeneration for given character level
	 */
	public double getBaseHpRegen(int level) {
		return _baseHpReg[level];
	}
	
	/**
	 * @param level character level to return value
	 * @return the base MP Regeneration for given character level
	 */
	public double getBaseMpRegen(int level) {
		return _baseMpReg[level];
	}
	
	/**
	 * @param level character level to return value
	 * @return the base HP Regeneration for given character level
	 */
	public double getBaseCpRegen(int level) {
		return _baseCpReg[level];
	}
	
	/**
	 * @param slotId id of inventory slot to return value
	 * @return defence value of character for EMPTY given slot
	 */
	public int getBaseDefBySlot(int slotId) {
		return _baseSlotDef.getOrDefault(slotId, 0);
	}
	
	/**
	 * @return the template collision height for female characters.
	 */
	public double getFCollisionHeightFemale() {
		return _fCollisionHeightFemale;
	}
	
	/**
	 * @return the template collision radius for female characters.
	 */
	public double getFCollisionRadiusFemale() {
		return _fCollisionRadiusFemale;
	}
	
	/**
	 * @return the safe fall height.
	 */
	public int getSafeFallHeight() {
		return _baseSafeFallHeight;
	}
}
