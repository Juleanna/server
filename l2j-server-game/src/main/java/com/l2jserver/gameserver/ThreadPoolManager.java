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
package com.l2jserver.gameserver;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.util.StringUtil;

/**
 * <p>
 * This class is made to handle all the ThreadPools used in L2J.
 * </p>
 * <p>
 * Scheduled Tasks can either be sent to a {@link #_generalScheduledThreadPool "general"} or {@link #_effectsScheduledThreadPool "effects"} {@link ScheduledThreadPoolExecutor ScheduledThreadPool}: The "effects" one is used for every effects (skills, hp/mp regen ...) while the "general" one is used
 * for everything else that needs to be scheduled.<br>
 * There also is an {@link #_aiScheduledThreadPool "ai"} {@link ScheduledThreadPoolExecutor ScheduledThreadPool} used for AI Tasks.
 * </p>
 * <p>
 * Tasks can be sent to {@link ScheduledThreadPoolExecutor ScheduledThreadPool} either with:
 * <ul>
 * <li>{@link #scheduleEffect(Runnable, long, TimeUnit)} and {@link #scheduleEffect(Runnable, long)} : for effects Tasks that needs to be executed only once.</li>
 * <li>{@link #scheduleGeneral(Runnable, long, TimeUnit)} and {@link #scheduleGeneral(Runnable, long)} : for scheduled Tasks that needs to be executed once.</li>
 * <li>{@link #scheduleAi(Runnable, long, TimeUnit)} and {@link #scheduleAi(Runnable, long)} : for AI Tasks that needs to be executed once</li>
 * </ul>
 * or
 * <ul>
 * <li>{@link #scheduleEffectAtFixedRate(Runnable, long, long, TimeUnit)} and {@link #scheduleEffectAtFixedRate(Runnable, long, long)} : for effects Tasks that needs to be executed periodically.</li>
 * <li>{@link #scheduleGeneralAtFixedRate(Runnable, long, long, TimeUnit)} and {@link #scheduleGeneralAtFixedRate(Runnable, long, long)} : for scheduled Tasks that needs to be executed periodically.</li>
 * <li>{@link #scheduleAiAtFixedRate(Runnable, long, long, TimeUnit)} and {@link #scheduleAiAtFixedRate(Runnable, long, long)} : for AI Tasks that needs to be executed periodically</li>
 * </ul>
 * </p>
 * <p>
 * For all Tasks that should be executed with no delay asynchronously in a ThreadPool there also are usual {@link ThreadPoolExecutor ThreadPools} that can grow/shrink according to their load.:
 * <ul>
 * <li>{@link #_generalPacketsThreadPool GeneralPackets} where most packets handler are executed.</li>
 * <li>{@link #_ioPacketsThreadPool I/O Packets} where all the i/o packets are executed.</li>
 * <li>There will be an AI ThreadPool where AI events should be executed</li>
 * <li>A general ThreadPool where everything else that needs to run asynchronously with no delay should be executed ({@link com.l2jserver.gameserver.model.actor.knownlist KnownList} updates, SQL updates/inserts...)?</li>
 * </ul>
 * </p>
 * @author -Wooden-
 */
public class ThreadPoolManager {
	protected static final Logger LOG = LoggerFactory.getLogger(ThreadPoolManager.class);
	
	private static final class RunnableWrapper implements Runnable {
		private final Runnable _r;
		
		public RunnableWrapper(final Runnable r) {
			_r = r;
		}
		
		@Override
		public void run() {
			try {
				_r.run();
			} catch (final Throwable e) {
				final Thread t = Thread.currentThread();
				final UncaughtExceptionHandler h = t.getUncaughtExceptionHandler();
				if (h != null) {
					h.uncaughtException(t, e);
				}
			}
		}
	}
	
	private final ScheduledThreadPoolExecutor _effectsScheduledThreadPool;
	private final ScheduledThreadPoolExecutor _generalScheduledThreadPool;
	private final ScheduledThreadPoolExecutor _aiScheduledThreadPool;
	private final ScheduledThreadPoolExecutor _eventScheduledThreadPool;
	private final ThreadPoolExecutor _generalPacketsThreadPool;
	private final ThreadPoolExecutor _ioPacketsThreadPool;
	private final ThreadPoolExecutor _generalThreadPool;
	private final ThreadPoolExecutor _eventThreadPool;
	
	private boolean _shutdown;
	
	public static ThreadPoolManager getInstance() {
		return SingletonHolder._instance;
	}
	
	protected ThreadPoolManager() {
		_effectsScheduledThreadPool = new ScheduledThreadPoolExecutor(general().getThreadPoolSizeEffects(), new PriorityThreadFactory("EffectsSTPool", Thread.NORM_PRIORITY));
		_generalScheduledThreadPool = new ScheduledThreadPoolExecutor(general().getThreadPoolSizeGeneral(), new PriorityThreadFactory("GeneralSTPool", Thread.NORM_PRIORITY));
		_eventScheduledThreadPool = new ScheduledThreadPoolExecutor(general().getThreadPoolSizeEvents(), new PriorityThreadFactory("EventSTPool", Thread.NORM_PRIORITY));
		_ioPacketsThreadPool = new ThreadPoolExecutor(general().getUrgentPacketThreadCoreSize(), Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("I/O Packet Pool", Thread.NORM_PRIORITY + 1));
		_generalPacketsThreadPool = new ThreadPoolExecutor(general().getGeneralPacketThreadCoreSize(), general().getGeneralPacketThreadCoreSize() + 2, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("Normal Packet Pool", Thread.NORM_PRIORITY + 1));
		_generalThreadPool = new ThreadPoolExecutor(general().getGeneralThreadCoreSize(), general().getGeneralThreadCoreSize() + 2, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("General Pool", Thread.NORM_PRIORITY));
		_aiScheduledThreadPool = new ScheduledThreadPoolExecutor(general().getAiMaxThread(), new PriorityThreadFactory("AISTPool", Thread.NORM_PRIORITY));
		_eventThreadPool = new ThreadPoolExecutor(general().getEventsMaxThread(), general().getEventsMaxThread() + 2, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("Event Pool", Thread.NORM_PRIORITY));
		
		scheduleGeneralAtFixedRate(new PurgeTask(_effectsScheduledThreadPool, _generalScheduledThreadPool, _aiScheduledThreadPool, _eventThreadPool), 10, 5, TimeUnit.MINUTES);
	}
	
	/**
	 * Schedules an effect task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in the given time unit
	 * @param unit the time unit of the delay parameter
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEffect(Runnable task, long delay, TimeUnit unit) {
		try {
			return _effectsScheduledThreadPool.schedule(new RunnableWrapper(task), delay, unit);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}
	
	/**
	 * Schedules an effect task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEffect(Runnable task, long delay) {
		return scheduleEffect(task, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules an effect task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in the given time unit
	 * @param period the period between executions in the given time unit
	 * @param unit the time unit of the initialDelay and period parameters
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEffectAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
		try {
			return _effectsScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(task), initialDelay, period, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules an effect task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in milliseconds
	 * @param period the period between executions in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEffectAtFixedRate(Runnable task, long initialDelay, long period) {
		return scheduleEffectAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules a general task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in the given time unit
	 * @param unit the time unit of the delay parameter
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleGeneral(Runnable task, long delay, TimeUnit unit) {
		try {
			return _generalScheduledThreadPool.schedule(new RunnableWrapper(task), delay, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a general task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleGeneral(Runnable task, long delay) {
		return scheduleGeneral(task, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules a general task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in the given time unit
	 * @param period the period between executions in the given time unit
	 * @param unit the time unit of the initialDelay and period parameters
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
		try {
			return _generalScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(task), initialDelay, period, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a event task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in the given time unit
	 * @param unit the time unit of the delay parameter
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEvent(Runnable task, long delay, TimeUnit unit) {
		try {
			return _eventScheduledThreadPool.schedule(new RunnableWrapper(task), delay, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a event task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEvent(Runnable task, long delay) {
		return scheduleEvent(task, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules a event task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in the given time unit
	 * @param period the period between executions in the given time unit
	 * @param unit the time unit of the initialDelay and period parameters
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleEventAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
		try {
			return _eventScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(task), initialDelay, period, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a general task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in milliseconds
	 * @param period the period between executions in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable task, long initialDelay, long period) {
		return scheduleGeneralAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules an AI task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in the given time unit
	 * @param unit the time unit of the delay parameter
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleAi(Runnable task, long delay, TimeUnit unit) {
		try {
			return _aiScheduledThreadPool.schedule(new RunnableWrapper(task), delay, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules an AI task to be executed after the given delay.
	 * @param task the task to execute
	 * @param delay the delay in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleAi(Runnable task, long delay) {
		return scheduleAi(task, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules a general task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in the given time unit
	 * @param period the period between executions in the given time unit
	 * @param unit the time unit of the initialDelay and period parameters
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleAiAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
		try {
			return _aiScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(task), initialDelay, period, unit);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a general task to be executed at fixed rate.
	 * @param task the task to execute
	 * @param initialDelay the initial delay in milliseconds
	 * @param period the period between executions in milliseconds
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
	 */
	public ScheduledFuture<?> scheduleAiAtFixedRate(Runnable task, long initialDelay, long period) {
		return scheduleAiAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Executes a packet task sometime in future in another thread.
	 * @param task the task to execute
	 */
	public void executePacket(Runnable task) {
		try {
			_generalPacketsThreadPool.execute(task);
		} catch (RejectedExecutionException e) {
			/* shutdown, ignore */
		}
	}
	
	/**
	 * Executes an IO packet task sometime in future in another thread.
	 * @param task the task to execute
	 */
	public void executeIOPacket(Runnable task) {
		try {
			_ioPacketsThreadPool.execute(task);
		} catch (RejectedExecutionException e) {
			/* shutdown, ignore */
		}
	}
	
	/**
	 * Executes a general task sometime in future in another thread.
	 * @param task the task to execute
	 */
	public void executeGeneral(Runnable task) {
		try {
			_generalThreadPool.execute(new RunnableWrapper(task));
		} catch (RejectedExecutionException e) {
			/* shutdown, ignore */
		}
	}
	
	/**
	 * Executes an AI task sometime in future in another thread.
	 * @param task the task to execute
	 */
	public void executeAi(Runnable task) {
		try {
			_aiScheduledThreadPool.execute(new RunnableWrapper(task));
		} catch (RejectedExecutionException e) {
			/* shutdown, ignore */
		}
	}
	
	/**
	 * Executes an Event task sometime in future in another thread.
	 * @param task the task to execute
	 */
	public void executeEvent(Runnable task) {
		try {
			_eventThreadPool.execute(new RunnableWrapper(task));
		} catch (RejectedExecutionException e) {
			/* shutdown, ignore */
		}
	}
	
	public String[] getStats() {
		return new String[] {
			"STP:",
			" + Effects:",
			" |- ActiveThreads:   " + _effectsScheduledThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _effectsScheduledThreadPool.getCorePoolSize(),
			" |- PoolSize:        " + _effectsScheduledThreadPool.getPoolSize(),
			" |- MaximumPoolSize: " + _effectsScheduledThreadPool.getMaximumPoolSize(),
			" |- CompletedTasks:  " + _effectsScheduledThreadPool.getCompletedTaskCount(),
			" |- ScheduledTasks:  " + _effectsScheduledThreadPool.getQueue().size(),
			" | -------",
			" + General:",
			" |- ActiveThreads:   " + _generalScheduledThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _generalScheduledThreadPool.getCorePoolSize(),
			" |- PoolSize:        " + _generalScheduledThreadPool.getPoolSize(),
			" |- MaximumPoolSize: " + _generalScheduledThreadPool.getMaximumPoolSize(),
			" |- CompletedTasks:  " + _generalScheduledThreadPool.getCompletedTaskCount(),
			" |- ScheduledTasks:  " + _generalScheduledThreadPool.getQueue().size(),
			" | -------",
			" + AI:",
			" |- ActiveThreads:   " + _aiScheduledThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _aiScheduledThreadPool.getCorePoolSize(),
			" |- PoolSize:        " + _aiScheduledThreadPool.getPoolSize(),
			" |- MaximumPoolSize: " + _aiScheduledThreadPool.getMaximumPoolSize(),
			" |- CompletedTasks:  " + _aiScheduledThreadPool.getCompletedTaskCount(),
			" |- ScheduledTasks:  " + _aiScheduledThreadPool.getQueue().size(),
			" | -------",
			" + Event:",
			" |- ActiveThreads:   " + _eventScheduledThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _eventScheduledThreadPool.getCorePoolSize(),
			" |- PoolSize:        " + _eventScheduledThreadPool.getPoolSize(),
			" |- MaximumPoolSize: " + _eventScheduledThreadPool.getMaximumPoolSize(),
			" |- CompletedTasks:  " + _eventScheduledThreadPool.getCompletedTaskCount(),
			" |- ScheduledTasks:  " + _eventScheduledThreadPool.getQueue().size(),
			"TP:",
			" + Packets:",
			" |- ActiveThreads:   " + _generalPacketsThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _generalPacketsThreadPool.getCorePoolSize(),
			" |- MaximumPoolSize: " + _generalPacketsThreadPool.getMaximumPoolSize(),
			" |- LargestPoolSize: " + _generalPacketsThreadPool.getLargestPoolSize(),
			" |- PoolSize:        " + _generalPacketsThreadPool.getPoolSize(),
			" |- CompletedTasks:  " + _generalPacketsThreadPool.getCompletedTaskCount(),
			" |- QueuedTasks:     " + _generalPacketsThreadPool.getQueue().size(),
			" | -------",
			" + I/O Packets:",
			" |- ActiveThreads:   " + _ioPacketsThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _ioPacketsThreadPool.getCorePoolSize(),
			" |- MaximumPoolSize: " + _ioPacketsThreadPool.getMaximumPoolSize(),
			" |- LargestPoolSize: " + _ioPacketsThreadPool.getLargestPoolSize(),
			" |- PoolSize:        " + _ioPacketsThreadPool.getPoolSize(),
			" |- CompletedTasks:  " + _ioPacketsThreadPool.getCompletedTaskCount(),
			" |- QueuedTasks:     " + _ioPacketsThreadPool.getQueue().size(),
			" | -------",
			" + General Tasks:",
			" |- ActiveThreads:   " + _generalThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _generalThreadPool.getCorePoolSize(),
			" |- MaximumPoolSize: " + _generalThreadPool.getMaximumPoolSize(),
			" |- LargestPoolSize: " + _generalThreadPool.getLargestPoolSize(),
			" |- PoolSize:        " + _generalThreadPool.getPoolSize(),
			" |- CompletedTasks:  " + _generalThreadPool.getCompletedTaskCount(),
			" |- QueuedTasks:     " + _generalThreadPool.getQueue().size(),
			" | -------",
			" + Event Tasks:",
			" |- ActiveThreads:   " + _eventThreadPool.getActiveCount(),
			" |- getCorePoolSize: " + _eventThreadPool.getCorePoolSize(),
			" |- MaximumPoolSize: " + _eventThreadPool.getMaximumPoolSize(),
			" |- LargestPoolSize: " + _eventThreadPool.getLargestPoolSize(),
			" |- PoolSize:        " + _eventThreadPool.getPoolSize(),
			" |- CompletedTasks:  " + _eventThreadPool.getCompletedTaskCount(),
			" |- QueuedTasks:     " + _eventThreadPool.getQueue().size(),
			" | -------"
		};
	}
	
	private static class PriorityThreadFactory implements ThreadFactory {
		private final int _priority;
		private final String _name;
		private final AtomicInteger _threadNumber = new AtomicInteger(1);
		private final ThreadGroup _group;
		
		public PriorityThreadFactory(String name, int priority) {
			_priority = priority;
			_name = name;
			_group = new ThreadGroup(_name);
		}
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r, _name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_priority);
			return t;
		}
		
		public ThreadGroup getGroup() {
			return _group;
		}
	}
	
	public void shutdown() {
		_shutdown = true;
		try {
			_effectsScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			_generalScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			_generalPacketsThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			_ioPacketsThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			_generalThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			_eventThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			_effectsScheduledThreadPool.shutdown();
			_generalScheduledThreadPool.shutdown();
			_generalPacketsThreadPool.shutdown();
			_ioPacketsThreadPool.shutdown();
			_generalThreadPool.shutdown();
			_eventThreadPool.shutdown();
			LOG.info("All ThreadPools are now stopped");
			
		} catch (InterruptedException e) {
			LOG.warn("There has been a problem shutting down the thread pool manager!", e);
		}
	}
	
	public boolean isShutdown() {
		return _shutdown;
	}
	
	public void purge() {
		_effectsScheduledThreadPool.purge();
		_generalScheduledThreadPool.purge();
		_aiScheduledThreadPool.purge();
		_eventScheduledThreadPool.purge();
		_ioPacketsThreadPool.purge();
		_generalPacketsThreadPool.purge();
		_generalThreadPool.purge();
		_eventThreadPool.purge();
	}
	
	public String getPacketStats() {
		final StringBuilder sb = new StringBuilder(1000);
		ThreadFactory tf = _generalPacketsThreadPool.getThreadFactory();
		if (tf instanceof PriorityThreadFactory ptf) {
			int count = ptf.getGroup().activeCount();
			Thread[] threads = new Thread[count + 2];
			ptf.getGroup().enumerate(threads);
			StringUtil.append(sb, "General Packet Thread Pool:" + Configuration.EOL + "Tasks in the queue: ", String.valueOf(_generalPacketsThreadPool.getQueue().size()), Configuration.EOL + "Showing threads stack trace:" + Configuration.EOL + "There should be ", String.valueOf(count), " Threads"
				+ Configuration.EOL);
			for (Thread t : threads) {
				if (t == null) {
					continue;
				}
				
				StringUtil.append(sb, t.getName(), Configuration.EOL);
				for (StackTraceElement ste : t.getStackTrace()) {
					StringUtil.append(sb, ste.toString(), Configuration.EOL);
				}
			}
		}
		
		sb.append("Packet Tp stack traces printed.");
		sb.append(Configuration.EOL);
		return sb.toString();
	}
	
	public String getIOPacketStats() {
		final StringBuilder sb = new StringBuilder(1000);
		ThreadFactory tf = _ioPacketsThreadPool.getThreadFactory();
		
		if (tf instanceof PriorityThreadFactory ptf) {
			int count = ptf.getGroup().activeCount();
			Thread[] threads = new Thread[count + 2];
			ptf.getGroup().enumerate(threads);
			StringUtil.append(sb, "I/O Packet Thread Pool:" + Configuration.EOL + "Tasks in the queue: ", String.valueOf(_ioPacketsThreadPool.getQueue().size()), Configuration.EOL + "Showing threads stack trace:" + Configuration.EOL + "There should be ", String.valueOf(count), " Threads"
				+ Configuration.EOL);
			
			for (Thread t : threads) {
				if (t == null) {
					continue;
				}
				
				StringUtil.append(sb, t.getName(), Configuration.EOL);
				
				for (StackTraceElement ste : t.getStackTrace()) {
					StringUtil.append(sb, ste.toString(), Configuration.EOL);
				}
			}
		}
		
		sb.append("Packet Tp stack traces printed.");
		sb.append(Configuration.EOL);
		return sb.toString();
	}
	
	public String getGeneralStats() {
		final StringBuilder sb = new StringBuilder(1000);
		ThreadFactory tf = _generalThreadPool.getThreadFactory();
		
		if (tf instanceof PriorityThreadFactory ptf) {
			int count = ptf.getGroup().activeCount();
			Thread[] threads = new Thread[count + 2];
			ptf.getGroup().enumerate(threads);
			StringUtil.append(sb, "General Thread Pool:" + Configuration.EOL + "Tasks in the queue: ", String.valueOf(_generalThreadPool.getQueue().size()), Configuration.EOL + "Showing threads stack trace:" + Configuration.EOL + "There should be ", String.valueOf(count), " Threads"
				+ Configuration.EOL);
			
			for (Thread t : threads) {
				if (t == null) {
					continue;
				}
				
				StringUtil.append(sb, t.getName(), Configuration.EOL);
				
				for (StackTraceElement ste : t.getStackTrace()) {
					StringUtil.append(sb, ste.toString(), Configuration.EOL);
				}
			}
		}
		
		sb.append("Packet Tp stack traces printed.");
		sb.append(Configuration.EOL);
		return sb.toString();
	}
	
	protected static class PurgeTask implements Runnable {
		private final ScheduledThreadPoolExecutor _effectsScheduled;
		
		private final ScheduledThreadPoolExecutor _generalScheduled;
		
		private final ScheduledThreadPoolExecutor _aiScheduled;
		
		private final ThreadPoolExecutor _eventScheduled;
		
		PurgeTask(ScheduledThreadPoolExecutor effectsScheduledThreadPool, ScheduledThreadPoolExecutor generalScheduledThreadPool, //
			ScheduledThreadPoolExecutor aiScheduledThreadPool, ThreadPoolExecutor eventScheduledThreadPool) {
			_effectsScheduled = effectsScheduledThreadPool;
			_generalScheduled = generalScheduledThreadPool;
			_aiScheduled = aiScheduledThreadPool;
			_eventScheduled = eventScheduledThreadPool;
		}
		
		@Override
		public void run() {
			_effectsScheduled.purge();
			_generalScheduled.purge();
			_aiScheduled.purge();
			_eventScheduled.purge();
		}
	}
	
	private static class SingletonHolder {
		protected static final ThreadPoolManager _instance = new ThreadPoolManager();
	}
}