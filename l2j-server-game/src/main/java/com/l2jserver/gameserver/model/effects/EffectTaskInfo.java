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
package com.l2jserver.gameserver.model.effects;

import java.util.concurrent.ScheduledFuture;

/**
 * Effect Task Info DTO.
 * @author Zoey76
 */
public class EffectTaskInfo {
	private final EffectTickTask _effectTask;
	private final ScheduledFuture<?> _scheduledFuture;
	
	public EffectTaskInfo(EffectTickTask effectTask, ScheduledFuture<?> scheduledFuture) {
		_effectTask = effectTask;
		_scheduledFuture = scheduledFuture;
	}
	
	public EffectTickTask getEffectTask() {
		return _effectTask;
	}
	
	public ScheduledFuture<?> getScheduledFuture() {
		return _scheduledFuture;
	}
}
