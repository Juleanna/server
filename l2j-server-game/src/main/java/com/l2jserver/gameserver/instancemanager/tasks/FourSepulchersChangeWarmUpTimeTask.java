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
package com.l2jserver.gameserver.instancemanager.tasks;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.instancemanager.FourSepulchersManager;

/**
 * Four Sepulchers change warm up time task.
 * @author xban1x
 */
public final class FourSepulchersChangeWarmUpTimeTask implements Runnable {
	@Override
	public void run() {
		final FourSepulchersManager manager = FourSepulchersManager.getInstance();
		manager.setIsEntryTime(true);
		manager.setIsWarmUpTime(false);
		manager.setIsAttackTime(false);
		manager.setIsCoolDownTime(false);
		
		long interval;
		// searching time when warmup time will be ended:
		// counting difference between time when warmup time ends and
		// current time and then launching change time task
		if (manager.isFirstTimeRun()) {
			interval = manager.getWarmUpTimeEnd() - Calendar.getInstance().getTimeInMillis();
		} else {
			interval = general().getTimeOfWarmUp();
		}
		
		manager.setChangeAttackTimeTask(ThreadPoolManager.getInstance().scheduleGeneral(new FourSepulchersChangeAttackTimeTask(), interval));
		final ScheduledFuture<?> changeWarmUpTimeTask = manager.getChangeWarmUpTimeTask();
		
		if (changeWarmUpTimeTask != null) {
			changeWarmUpTimeTask.cancel(true);
			manager.setChangeWarmUpTimeTask(null);
		}
	}
}
