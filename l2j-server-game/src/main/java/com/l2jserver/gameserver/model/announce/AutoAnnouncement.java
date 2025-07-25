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
package com.l2jserver.gameserver.model.announce;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * Auto-announcements.
 * @author UnAfraid
 */
public final class AutoAnnouncement extends Announcement implements Runnable {
	
	private static final String INSERT_QUERY = "INSERT INTO announcements (`type`, `content`, `author`, `initial`, `delay`, `repeat`) VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_QUERY = "UPDATE announcements SET `type` = ?, `content` = ?, `author` = ?, `initial` = ?, `delay` = ?, `repeat` = ? WHERE id = ?";
	
	private long _initial;
	
	private long _delay;
	
	private int _repeat;
	
	private int _currentState;
	
	private ScheduledFuture<?> _task;
	
	public AutoAnnouncement(AnnouncementType type, String content, String author, long initial, long delay, int repeat) {
		super(type, content, author);
		_initial = initial;
		_delay = delay;
		_repeat = repeat;
		restartMe();
	}
	
	public long getInitial() {
		return _initial;
	}
	
	public void setInitial(long initial) {
		_initial = initial;
	}
	
	public long getDelay() {
		return _delay;
	}
	
	public void setDelay(long delay) {
		_delay = delay;
	}
	
	public int getRepeat() {
		return _repeat;
	}
	
	public void setRepeat(int repeat) {
		_repeat = repeat;
	}
	
	@Override
	public boolean storeMe() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_QUERY, RETURN_GENERATED_KEYS)) {
			ps.setInt(1, getType().ordinal());
			ps.setString(2, getContent());
			ps.setString(3, getAuthor());
			ps.setLong(4, getInitial());
			ps.setLong(5, getDelay());
			ps.setInt(6, getRepeat());
			ps.execute();
			try (var rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					_id = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't store announcement: ", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateMe() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(UPDATE_QUERY)) {
			ps.setInt(1, getType().ordinal());
			ps.setString(2, getContent());
			ps.setString(3, getAuthor());
			ps.setLong(4, getInitial());
			ps.setLong(5, getDelay());
			ps.setLong(6, getRepeat());
			ps.setLong(7, getId());
			ps.execute();
		} catch (Exception e) {
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't update announcement: ", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteMe() {
		if ((_task != null) && !_task.isCancelled()) {
			_task.cancel(false);
		}
		return super.deleteMe();
	}
	
	public void restartMe() {
		if ((_task != null) && !_task.isCancelled()) {
			_task.cancel(false);
		}
		_currentState = _repeat;
		_task = ThreadPoolManager.getInstance().scheduleGeneral(this, _initial);
	}
	
	@Override
	public void run() {
		if ((_currentState == -1) || (_currentState > 0)) {
			for (String content : getContent().split(Configuration.EOL)) {
				Broadcast.toAllOnlinePlayers(content, (getType() == AnnouncementType.AUTO_CRITICAL));
			}
			
			if (_currentState != -1) {
				_currentState--;
			}
			
			_task = ThreadPoolManager.getInstance().scheduleGeneral(this, _delay);
		}
	}
}
