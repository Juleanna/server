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
package com.l2jserver.gameserver.taskmanager;

import static com.l2jserver.gameserver.taskmanager.TaskTypes.TYPE_NONE;
import static com.l2jserver.gameserver.taskmanager.TaskTypes.TYPE_SHEDULED;
import static com.l2jserver.gameserver.taskmanager.TaskTypes.TYPE_TIME;
import static java.util.concurrent.TimeUnit.DAYS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.taskmanager.tasks.TaskBirthday;
import com.l2jserver.gameserver.taskmanager.tasks.TaskClanLeaderApply;
import com.l2jserver.gameserver.taskmanager.tasks.TaskCleanUp;
import com.l2jserver.gameserver.taskmanager.tasks.TaskDailySkillReuseClean;
import com.l2jserver.gameserver.taskmanager.tasks.TaskGlobalVariablesSave;
import com.l2jserver.gameserver.taskmanager.tasks.TaskHuntingSystem;
import com.l2jserver.gameserver.taskmanager.tasks.TaskOlympiadSave;
import com.l2jserver.gameserver.taskmanager.tasks.TaskRaidPointsReset;
import com.l2jserver.gameserver.taskmanager.tasks.TaskRecom;
import com.l2jserver.gameserver.taskmanager.tasks.TaskRestart;
import com.l2jserver.gameserver.taskmanager.tasks.TaskScript;
import com.l2jserver.gameserver.taskmanager.tasks.TaskSevenSignsUpdate;
import com.l2jserver.gameserver.taskmanager.tasks.TaskShutdown;

/**
 * Task Manager.
 * @author Layane
 */
public final class TaskManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(TaskManager.class);
	
	private final Map<Integer, Task> _tasks = new ConcurrentHashMap<>();
	
	protected final List<ExecutedTask> _currentTasks = new CopyOnWriteArrayList<>();
	
	protected static final String[] SQL_STATEMENTS = {
		"SELECT id,task,type,last_activation,param1,param2,param3 FROM global_tasks",
		"UPDATE global_tasks SET last_activation=? WHERE id=?",
		"SELECT id FROM global_tasks WHERE task=?",
		"INSERT INTO global_tasks (task,type,last_activation,param1,param2,param3) VALUES(?,?,?,?,?,?)"
	};
	
	protected TaskManager() {
		initializate();
		startAllTasks();
		LOG.info("Loaded {} tasks.", _tasks.size());
	}
	
	public class ExecutedTask implements Runnable {
		int id;
		long lastActivation;
		Task task;
		TaskTypes type;
		String[] params;
		ScheduledFuture<?> scheduled;
		
		public ExecutedTask(Task ptask, TaskTypes ptype, ResultSet rset) throws SQLException {
			task = ptask;
			type = ptype;
			id = rset.getInt("id");
			lastActivation = rset.getLong("last_activation");
			params = new String[] {
				rset.getString("param1"),
				rset.getString("param2"),
				rset.getString("param3")
			};
		}
		
		@Override
		public void run() {
			task.onTimeElapsed(this);
			lastActivation = System.currentTimeMillis();
			try (var con = ConnectionFactory.getInstance().getConnection();
				var statement = con.prepareStatement(SQL_STATEMENTS[1])) {
				statement.setLong(1, lastActivation);
				statement.setInt(2, id);
				statement.executeUpdate();
			} catch (SQLException ex) {
				LOG.warn("Cannot updated global task Id {}!", id, ex);
			}
			
			if ((type == TYPE_SHEDULED) || (type == TYPE_TIME)) {
				stopTask();
			}
		}
		
		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (!(object instanceof ExecutedTask)) {
				return false;
			}
			return id == ((ExecutedTask) object).id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		public Task getTask() {
			return task;
		}
		
		public TaskTypes getType() {
			return type;
		}
		
		public int getId() {
			return id;
		}
		
		public String[] getParams() {
			return params;
		}
		
		public long getLastActivation() {
			return lastActivation;
		}
		
		public void stopTask() {
			task.onDestroy();
			
			if (scheduled != null) {
				scheduled.cancel(true);
			}
			
			_currentTasks.remove(this);
		}
	}
	
	private void initializate() {
		registerTask(new TaskBirthday());
		registerTask(new TaskClanLeaderApply());
		registerTask(new TaskCleanUp());
		registerTask(new TaskDailySkillReuseClean());
		registerTask(new TaskGlobalVariablesSave());
		registerTask(new TaskHuntingSystem());
		registerTask(new TaskOlympiadSave());
		registerTask(new TaskRaidPointsReset());
		registerTask(new TaskRecom());
		registerTask(new TaskRestart());
		registerTask(new TaskScript());
		registerTask(new TaskSevenSignsUpdate());
		registerTask(new TaskShutdown());
	}
	
	public void registerTask(Task task) {
		int key = task.getName().hashCode();
		_tasks.computeIfAbsent(key, k -> {
			task.initializate();
			return task;
		});
	}
	
	private void startAllTasks() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var statement = con.prepareStatement(SQL_STATEMENTS[0]);
			var rs = statement.executeQuery()) {
			while (rs.next()) {
				Task task = _tasks.get(rs.getString("task").trim().toLowerCase().hashCode());
				if (task == null) {
					continue;
				}
				
				final TaskTypes type = TaskTypes.valueOf(rs.getString("type"));
				if (type != TYPE_NONE) {
					ExecutedTask current = new ExecutedTask(task, type, rs);
					if (launchTask(current)) {
						_currentTasks.add(current);
					}
				}
			}
		} catch (Exception ex) {
			LOG.warn("There has been an error while loading global task table!", ex);
		}
	}
	
	private boolean launchTask(ExecutedTask task) {
		final ThreadPoolManager scheduler = ThreadPoolManager.getInstance();
		final TaskTypes type = task.getType();
		long delay, interval;
		switch (type) {
			case TYPE_STARTUP:
				task.run();
				return false;
			case TYPE_SHEDULED:
				delay = Long.parseLong(task.getParams()[0]);
				task.scheduled = scheduler.scheduleGeneral(task, delay);
				return true;
			case TYPE_FIXED_SHEDULED:
				delay = Long.parseLong(task.getParams()[0]);
				interval = Long.parseLong(task.getParams()[1]);
				task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, delay, interval);
				return true;
			case TYPE_TIME:
				try {
					Date desired = DateFormat.getInstance().parse(task.getParams()[0]);
					long diff = desired.getTime() - System.currentTimeMillis();
					if (diff >= 0) {
						task.scheduled = scheduler.scheduleGeneral(task, diff);
						return true;
					}
					LOG.info("Task {} is due.", task.getId());
				} catch (Exception e) {
				}
				break;
			case TYPE_SPECIAL:
				ScheduledFuture<?> result = task.getTask().launchSpecial(task);
				if (result != null) {
					task.scheduled = result;
					return true;
				}
				break;
			case TYPE_GLOBAL_TASK:
				interval = DAYS.toMillis(Long.parseLong(task.getParams()[0]));
				String[] hour = task.getParams()[1].split(":");
				
				if (hour.length != 3) {
					LOG.warn("Task {} has incorrect parameters!", task.getId());
					return false;
				}
				
				Calendar check = Calendar.getInstance();
				check.setTimeInMillis(task.getLastActivation() + interval);
				
				Calendar min = Calendar.getInstance();
				try {
					min.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
					min.set(Calendar.MINUTE, Integer.parseInt(hour[1]));
					min.set(Calendar.SECOND, Integer.parseInt(hour[2]));
				} catch (Exception ex) {
					LOG.warn("Bad parameter on task Id {}!", task.getId(), ex);
					return false;
				}
				
				delay = min.getTimeInMillis() - System.currentTimeMillis();
				
				if (check.after(min) || (delay < 0)) {
					delay += interval;
				}
				task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, delay, interval);
				return true;
			default:
				return false;
		}
		return false;
	}
	
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3) {
		return addUniqueTask(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps1 = con.prepareStatement(SQL_STATEMENTS[2])) {
			ps1.setString(1, task);
			try (var rs = ps1.executeQuery()) {
				if (!rs.next()) {
					try (var ps2 = con.prepareStatement(SQL_STATEMENTS[3])) {
						ps2.setString(1, task);
						ps2.setString(2, type.toString());
						ps2.setLong(3, lastActivation);
						ps2.setString(4, param1);
						ps2.setString(5, param2);
						ps2.setString(6, param3);
						ps2.execute();
					}
				}
			}
			return true;
		} catch (SQLException ex) {
			LOG.warn("Cannot add the unique task!", ex);
		}
		return false;
	}
	
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3) {
		return addTask(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var statement = con.prepareStatement(SQL_STATEMENTS[3])) {
			statement.setString(1, task);
			statement.setString(2, type.toString());
			statement.setLong(3, lastActivation);
			statement.setString(4, param1);
			statement.setString(5, param2);
			statement.setString(6, param3);
			statement.execute();
			return true;
		} catch (SQLException ex) {
			LOG.warn("Cannot add the task {}!", task, ex);
		}
		return false;
	}
	
	public static TaskManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final TaskManager INSTANCE = new TaskManager();
	}
}