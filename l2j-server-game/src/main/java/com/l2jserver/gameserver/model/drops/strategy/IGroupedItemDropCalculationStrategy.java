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
package com.l2jserver.gameserver.model.drops.strategy;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.drops.GeneralDropItem;
import com.l2jserver.gameserver.model.drops.GroupedGeneralDropItem;
import com.l2jserver.gameserver.model.drops.IDropItem;
import com.l2jserver.gameserver.model.holders.ItemHolder;

/**
 * @author Battlecruiser
 */
public interface IGroupedItemDropCalculationStrategy {
	Logger LOG = LoggerFactory.getLogger(IGroupedItemDropCalculationStrategy.class);
	/**
	 * The default strategy used in L2J to calculate drops. When the group's chance raises over 100% and group has precise calculation, the dropped item's amount increases.
	 */
	IGroupedItemDropCalculationStrategy DEFAULT_STRATEGY = new IGroupedItemDropCalculationStrategy() {
		private final Map<GroupedGeneralDropItem, GeneralDropItem> singleItemCache = new ConcurrentHashMap<>();
		
		private GeneralDropItem getSingleItem(GroupedGeneralDropItem dropItem) {
			final GeneralDropItem item1 = dropItem.getItems().iterator().next();
			singleItemCache.putIfAbsent(dropItem, new GeneralDropItem(item1.getItemId(), item1.getMin(), item1.getMax(), (item1.getChance() * dropItem.getChance()) / 100, //
				item1.getAmountStrategy(), item1.getChanceStrategy(), dropItem.getPreciseStrategy(), dropItem.getKillerChanceModifierStrategy(), item1.getDropCalculationStrategy()));
			return singleItemCache.get(dropItem);
		}
		
		@Override
		public List<ItemHolder> calculateDrops(GroupedGeneralDropItem dropItem, L2Character victim, L2Character killer) {
			if (dropItem.getItems().size() == 1) {
				return getSingleItem(dropItem).calculateDrops(victim, killer);
			}
			
			GroupedGeneralDropItem normalized = dropItem.normalizeMe(victim, killer);
			if (normalized.getChance() > (Rnd.nextDouble() * 100)) {
				final double random = (Rnd.nextDouble() * 100);
				double totalChance = 0;
				for (GeneralDropItem item2 : normalized.getItems()) {
					// Grouped item chance rates should not be modified (the whole magic was already done by normalizing thus the items' chance sum is always 100%).
					totalChance += item2.getChance();
					if (totalChance > random) {
						int amountMultiply = 1;
						if (dropItem.isPreciseCalculated() && (normalized.getChance() >= 100)) {
							amountMultiply = (int) (normalized.getChance()) / 100;
							if ((normalized.getChance() % 100) > (Rnd.nextDouble() * 100)) {
								amountMultiply++;
							}
						}
						
						return Collections.singletonList(new ItemHolder(item2.getItemId(), Rnd.get(item2.getMin(victim), item2.getMax(victim)) * amountMultiply));
					}
				}
			}
			return null;
		}
	};
	
	/**
	 * This strategy calculates a group's drop by calculating drops of its individual items and merging its results.
	 */
	IGroupedItemDropCalculationStrategy DISBAND_GROUP = (item, victim, killer) -> {
		List<ItemHolder> dropped = new ArrayList<>();
		for (IDropItem dropItem : item.extractMe()) {
			dropped.addAll(dropItem.calculateDrops(victim, killer));
		}
		return dropped.isEmpty() ? null : dropped;
	};
	
	/**
	 * This strategy when group has precise calculation rolls multiple times over group to determine drops when group's chance raises over 100% instead of just multiplying the dropped item's amount. Thus it can produce different items from group at once.
	 */
	IGroupedItemDropCalculationStrategy PRECISE_MULTIPLE_GROUP_ROLLS = (item, victim, killer) -> {
		if (!item.isPreciseCalculated()) {
			// if item hasn't precise calculation there's no change from DEFAULT_STRATEGY
			return DEFAULT_STRATEGY.calculateDrops(item, victim, victim);
		}
		GroupedGeneralDropItem newItem = new GroupedGeneralDropItem(item.getChance(), DEFAULT_STRATEGY, item.getKillerChanceModifierStrategy(), IPreciseDeterminationStrategy.NEVER);
		newItem.setItems(item.getItems());
		GroupedGeneralDropItem normalized = newItem.normalizeMe(victim, killer);
		// Let's determine the number of rolls.
		int rolls = (int) (normalized.getChance() / 100);
		if ((Rnd.nextDouble() * 100) < (normalized.getChance() % 100)) {
			rolls++;
		}
		
		List<ItemHolder> dropped = new ArrayList<>(rolls);
		for (int i = 0; i < rolls; i++) {
			// As further normalizing on already normalized drop group does nothing, we can just pass the calculation to DEFAULT_STRATEGY with precise calculation disabled as we handle it.
			List<ItemHolder> drops = normalized.calculateDrops(victim, killer);
			if (drops != null) {
				dropped.addAll(drops);
			}
		}
		
		if (general().preciseDropMultipleRollsAggregateDrops()) {
			Map<Integer, Long> countByItemId = new HashMap<>();
			for (ItemHolder drop : dropped) {
				Long currentCount = countByItemId.getOrDefault(drop.getId(), 0L);
				countByItemId.put(drop.getId(), currentCount + drop.getCount());
			}
			
			dropped.clear();
			for (Map.Entry<Integer, Long> entry : countByItemId.entrySet()) {
				dropped.add(new ItemHolder(entry.getKey(), entry.getValue()));
			}
		}
		
		return dropped.isEmpty() ? null : dropped;
	};
	
	List<ItemHolder> calculateDrops(GroupedGeneralDropItem item, L2Character victim, L2Character killer);
}
