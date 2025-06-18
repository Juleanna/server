/*
 * Copyright © 2024 Your Company
 *
 * This file is part of Your Project.
 *
 * Your Project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Your Project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Your Project. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.config;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.aeonbits.owner.Config.HotReloadType.ASYNC;
import static org.aeonbits.owner.Config.LoadType.MERGE;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;


/**
 * Rewards Configuration.
 * Represents the configuration for rewards based on time spent in-game.
 * @author Dafna
 * @version 1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/RewardsOnlineConfig.properties",
	"file:./config/RewardsOnlineConfig.properties",
	"classpath:config/RewardsOnlineConfig.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface RewardsOnlineConfig extends Reloadable {

    @Key("reward.items.count")
    int getRewardItemCount();

    @Key("reward.item.%d.id")
    int getItemId(int index);

    @Key("reward.item.%d.count")
    long getItemCount(int index);

    @Key("reward.item.%d.time")
    long getItemTime(int index);

    @Key("reward.item.%d.saveToDatabase")
    boolean isSaveToDatabase(int index);
	
	@Key("reward.item.%d.onceOnly")
    @DefaultValue("false")
    boolean isOnceOnly(int index);
}


