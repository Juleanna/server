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

import static com.l2jserver.gameserver.config.Configuration.fortress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.entity.Fort;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

/**
 * Class managing periodical events with castle.
 * @author Vice
 * @since 2008
 */
public class FortUpdater implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(FortUpdater.class);
	private final L2Clan _clan;
	private final Fort _fort;
	private int _runCount;
	private final UpdaterType _updaterType;
	
	public enum UpdaterType {
		MAX_OWN_TIME, // gives fort back to NPC clan
		PERIODIC_UPDATE // raise blood oath/supply level
	}
	
	public FortUpdater(Fort fort, L2Clan clan, int runCount, UpdaterType ut) {
		_fort = fort;
		_clan = clan;
		_runCount = runCount;
		_updaterType = ut;
	}
	
	@Override
	public void run() {
		try {
			switch (_updaterType) {
				case PERIODIC_UPDATE -> {
					_runCount++;
					if ((_fort.getOwnerClan() == null) || (_fort.getOwnerClan() != _clan)) {
						return;
					}
					
					_fort.getOwnerClan().increaseBloodOathCount();
					
					if (_fort.getFortState() == 2) {
						if (_clan.getWarehouse().getAdena() >= fortress().getFeeForCastle()) {
							_clan.getWarehouse().destroyItemByItemId("FS_fee_for_Castle", Inventory.ADENA_ID, fortress().getFeeForCastle(), null, null);
							_fort.getContractedCastle().addToTreasuryNoTax(fortress().getFeeForCastle());
							_fort.raiseSupplyLvL();
						} else {
							_fort.setFortState(1, 0);
						}
					}
					_fort.saveFortVariables();
				}
				case MAX_OWN_TIME -> {
					if ((_fort.getOwnerClan() == null) || (_fort.getOwnerClan() != _clan)) {
						return;
					}
					if (_fort.getOwnedTime() > (fortress().getMaxKeepTime() * 3600)) {
						_fort.removeOwner(true);
						_fort.setFortState(0, 0);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("There has been a problem updating forts!", e);
		}
	}
	
	public int getRunCount() {
		return _runCount;
	}
}