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
package com.l2jserver.gameserver.idfactory;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.util.file.filter.PrimeFinder;

/**
 * BitSet ID Factory.
 * @version 2.6.1.0
 */
public class BitSetIDFactory extends IdFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(BitSetIDFactory.class);
	
	private BitSet _freeIds;
	
	private AtomicInteger _freeIdCount;
	
	private AtomicInteger _nextFreeId;
	
	protected class BitSetCapacityCheck implements Runnable {
		@Override
		public void run() {
			synchronized (BitSetIDFactory.this) {
				if (reachingBitSetCapacity()) {
					increaseBitSetCapacity();
				}
			}
		}
	}
	
	protected BitSetIDFactory() {
		synchronized (BitSetIDFactory.class) {
			ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new BitSetCapacityCheck(), 30000, 30000);
			initialize();
		}
		LOG.info("{} Ids available.", _freeIds.size());
	}
	
	public void initialize() {
		try {
			_freeIds = new BitSet(PrimeFinder.nextPrime(100000));
			_freeIds.clear();
			_freeIdCount = new AtomicInteger(FREE_OBJECT_ID_SIZE);
			
			for (int usedObjectId : extractUsedObjectIDTable()) {
				int objectID = usedObjectId - FIRST_OID;
				if (objectID < 0) {
					LOG.warn("Object Id {} in DB is less than minimum ID of {}!", usedObjectId, FIRST_OID);
					continue;
				}
				_freeIds.set(usedObjectId - FIRST_OID);
				_freeIdCount.decrementAndGet();
			}
			
			_nextFreeId = new AtomicInteger(_freeIds.nextClearBit(0));
			_initialized = true;
		} catch (Exception ex) {
			_initialized = false;
			LOG.error("Could not be initialized properly!", ex);
		}
	}
	
	@Override
	public synchronized void releaseId(int objectId) {
		if ((objectId - FIRST_OID) > -1) {
			_freeIds.clear(objectId - FIRST_OID);
			_freeIdCount.incrementAndGet();
		} else {
			LOG.warn("Release objectID {} failed (< {}).", objectId, FIRST_OID);
		}
	}
	
	@Override
	public synchronized int getNextId() {
		int newID = _nextFreeId.get();
		_freeIds.set(newID);
		_freeIdCount.decrementAndGet();
		
		int nextFree = _freeIds.nextClearBit(newID);
		
		if (nextFree < 0) {
			nextFree = _freeIds.nextClearBit(0);
		}
		if (nextFree < 0) {
			if (_freeIds.size() < FREE_OBJECT_ID_SIZE) {
				increaseBitSetCapacity();
			} else {
				throw new NullPointerException("Ran out of valid Id's.");
			}
		}
		
		_nextFreeId.set(nextFree);
		
		return newID + FIRST_OID;
	}
	
	@Override
	public synchronized int size() {
		return _freeIdCount.get();
	}
	
	/**
	 * @return
	 */
	protected synchronized int usedIdCount() {
		return (size() - FIRST_OID);
	}
	
	/**
	 * @return
	 */
	protected synchronized boolean reachingBitSetCapacity() {
		return PrimeFinder.nextPrime((usedIdCount() * 11) / 10) > _freeIds.size();
	}
	
	protected synchronized void increaseBitSetCapacity() {
		BitSet newBitSet = new BitSet(PrimeFinder.nextPrime((usedIdCount() * 11) / 10));
		newBitSet.or(_freeIds);
		_freeIds = newBitSet;
	}
}
