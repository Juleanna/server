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
package com.l2jserver.gameserver.model;

/**
 * Action Key DTO.
 * @author mrTJO
 * @author Zoey76
 */
public class ActionKey {
	private final int _cat;
	private int _cmd = 0;
	private int _key = 0;
	private int _toggleKey1 = 0;
	private int _toggleKey2 = 0;
	private int _show = 1;
	
	public ActionKey(int cat) {
		_cat = cat;
	}
	
	/**
	 * L2ActionKey Initialization
	 * @param cat Category ID
	 * @param cmd Command ID
	 * @param key User Defined Primary Key
	 * @param tgKey1 1st Toggled Key (eg. Alt, Ctrl or Shift)
	 * @param tgKey2 2nd Toggled Key (eg. Alt, Ctrl or Shift)
	 * @param show Show Action in UI
	 */
	public ActionKey(int cat, int cmd, int key, int tgKey1, int tgKey2, int show) {
		_cat = cat;
		_cmd = cmd;
		_key = key;
		_toggleKey1 = tgKey1;
		_toggleKey2 = tgKey2;
		_show = show;
	}
	
	public int getCategory() {
		return _cat;
	}
	
	public int getCommandId() {
		return _cmd;
	}
	
	public void setCommandId(int cmd) {
		_cmd = cmd;
	}
	
	public int getKeyId() {
		return _key;
	}
	
	public void setKeyId(int key) {
		_key = key;
	}
	
	public int getToggleKey1() {
		return _toggleKey1;
	}
	
	public void setToggleKey1(int tKey1) {
		_toggleKey1 = tKey1;
	}
	
	public int getToggleKey2() {
		return _toggleKey2;
	}
	
	public void setToggleKey2(int tKey2) {
		_toggleKey2 = tKey2;
	}
	
	public int getShowStatus() {
		return _show;
	}
	
	public void setShowStatus(int show) {
		_show = show;
	}
	
	public String getSqlSaveString(int playerId, int order) {
		return "(" + playerId + ", " + _cat + ", " + order + ", " + _cmd + "," + _key + ", " + _toggleKey1 + ", " + _toggleKey2 + ", " + _show + ")";
	}
}
