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
package com.l2jserver.gameserver.model.itemauction;

import static com.l2jserver.gameserver.config.Configuration.general;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.instancemanager.ItemAuctionManager;
import com.l2jserver.gameserver.model.ItemInfo;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Forsaiken
 */
public final class ItemAuction {
	
	private static final Logger _log = Logger.getLogger(ItemAuctionManager.class.getName());
	
	private static final long ENDING_TIME_EXTEND_5 = MILLISECONDS.convert(5, MINUTES);
	
	private static final long ENDING_TIME_EXTEND_3 = MILLISECONDS.convert(3, MINUTES);
	
	private final int _auctionId;
	private final int _instanceId;
	private final long _startingTime;
	private volatile long _endingTime;
	private final AuctionItem _auctionItem;
	private final List<ItemAuctionBid> _auctionBids;
	private final Object _auctionStateLock;
	
	private volatile ItemAuctionState _auctionState;
	private volatile ItemAuctionExtendState _scheduledAuctionEndingExtendState;
	private volatile ItemAuctionExtendState _auctionEndingExtendState;
	
	private final ItemInfo _itemInfo;
	
	private ItemAuctionBid _highestBid;
	private int _lastBidPlayerObjId;
	
	// SQL
	private static final String DELETE_ITEM_AUCTION_BID = "DELETE FROM item_auction_bid WHERE auctionId = ? AND playerObjId = ?";
	private static final String INSERT_ITEM_AUCTION_BID = "INSERT INTO item_auction_bid (auctionId, playerObjId, playerBid) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE playerBid = ?";
	
	public ItemAuction(final int auctionId, final int instanceId, final long startingTime, final long endingTime, final AuctionItem auctionItem) {
		this(auctionId, instanceId, startingTime, endingTime, auctionItem, new ArrayList<>(), ItemAuctionState.CREATED);
	}
	
	public ItemAuction(final int auctionId, final int instanceId, final long startingTime, final long endingTime, final AuctionItem auctionItem, final List<ItemAuctionBid> auctionBids, final ItemAuctionState auctionState) {
		_auctionId = auctionId;
		_instanceId = instanceId;
		_startingTime = startingTime;
		_endingTime = endingTime;
		_auctionItem = auctionItem;
		_auctionBids = auctionBids;
		_auctionState = auctionState;
		_auctionStateLock = new Object();
		_scheduledAuctionEndingExtendState = ItemAuctionExtendState.INITIAL;
		_auctionEndingExtendState = ItemAuctionExtendState.INITIAL;
		
		final L2ItemInstance item = _auctionItem.createNewItemInstance();
		_itemInfo = new ItemInfo(item);
		L2World.getInstance().removeObject(item);
		
		for (final ItemAuctionBid bid : _auctionBids) {
			if ((_highestBid == null) || (_highestBid.getLastBid() < bid.getLastBid())) {
				_highestBid = bid;
			}
		}
	}
	
	public ItemAuctionState getAuctionState() {
		final ItemAuctionState auctionState;
		
		synchronized (_auctionStateLock) {
			auctionState = _auctionState;
		}
		
		return auctionState;
	}
	
	public boolean setAuctionState(final ItemAuctionState expected, final ItemAuctionState wanted) {
		synchronized (_auctionStateLock) {
			if (_auctionState != expected) {
				return false;
			}
			
			_auctionState = wanted;
			storeMe();
			return true;
		}
	}
	
	public int getAuctionId() {
		return _auctionId;
	}
	
	public int getInstanceId() {
		return _instanceId;
	}
	
	public ItemInfo getItemInfo() {
		return _itemInfo;
	}
	
	public L2ItemInstance createNewItemInstance() {
		return _auctionItem.createNewItemInstance();
	}
	
	public long getAuctionInitBid() {
		return _auctionItem.getAuctionInitBid();
	}
	
	public ItemAuctionBid getHighestBid() {
		return _highestBid;
	}
	
	public ItemAuctionExtendState getAuctionEndingExtendState() {
		return _auctionEndingExtendState;
	}
	
	public ItemAuctionExtendState getScheduledAuctionEndingExtendState() {
		return _scheduledAuctionEndingExtendState;
	}
	
	public void setScheduledAuctionEndingExtendState(ItemAuctionExtendState state) {
		_scheduledAuctionEndingExtendState = state;
	}
	
	public long getStartingTime() {
		return _startingTime;
	}
	
	public long getEndingTime() {
		return _endingTime;
	}
	
	public long getStartingTimeRemaining() {
		return Math.max(getEndingTime() - System.currentTimeMillis(), 0L);
	}
	
	public long getFinishingTimeRemaining() {
		return Math.max(getEndingTime() - System.currentTimeMillis(), 0L);
	}
	
	public void storeMe() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("INSERT INTO item_auction (auctionId,instanceId,auctionItemId,startingTime,endingTime,auctionStateId) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE auctionStateId=?")) {
			ps.setInt(1, _auctionId);
			ps.setInt(2, _instanceId);
			ps.setInt(3, _auctionItem.getAuctionItemId());
			ps.setLong(4, _startingTime);
			ps.setLong(5, _endingTime);
			ps.setByte(6, _auctionState.getStateId());
			ps.setByte(7, _auctionState.getStateId());
			ps.execute();
		} catch (Exception e) {
			_log.log(Level.WARNING, "", e);
		}
	}
	
	public int getAndSetLastBidPlayerObjectId(final int playerObjId) {
		final int lastBid = _lastBidPlayerObjId;
		_lastBidPlayerObjId = playerObjId;
		return lastBid;
	}
	
	private void updatePlayerBid(final ItemAuctionBid bid, final boolean delete) {
		updatePlayerBidInternal(bid, delete);
	}
	
	void updatePlayerBidInternal(final ItemAuctionBid bid, final boolean delete) {
		final String query = delete ? DELETE_ITEM_AUCTION_BID : INSERT_ITEM_AUCTION_BID;
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(query)) {
			ps.setInt(1, _auctionId);
			ps.setInt(2, bid.getPlayerObjId());
			if (!delete) {
				ps.setLong(3, bid.getLastBid());
				ps.setLong(4, bid.getLastBid());
			}
			ps.execute();
		} catch (Exception e) {
			_log.log(Level.WARNING, "", e);
		}
	}
	
	public void registerBid(final L2PcInstance player, final long newBid) {
		if (player == null) {
			throw new NullPointerException();
		}
		
		if (newBid < getAuctionInitBid()) {
			player.sendPacket(SystemMessageId.BID_PRICE_MUST_BE_HIGHER);
			return;
		}
		
		if (newBid > 100000000000L) {
			player.sendPacket(SystemMessageId.BID_CANT_EXCEED_100_BILLION);
			return;
		}
		
		if (getAuctionState() != ItemAuctionState.STARTED) {
			return;
		}
		
		final int playerObjId = player.getObjectId();
		
		synchronized (_auctionBids) {
			if ((_highestBid != null) && (newBid < _highestBid.getLastBid())) {
				player.sendPacket(SystemMessageId.BID_MUST_BE_HIGHER_THAN_CURRENT_BID);
				return;
			}
			
			ItemAuctionBid bid = getBidFor(playerObjId);
			if (bid == null) {
				if (!reduceItemCount(player, newBid)) {
					player.sendPacket(SystemMessageId.NOT_ENOUGH_ADENA_FOR_THIS_BID);
					return;
				}
				
				bid = new ItemAuctionBid(playerObjId, newBid);
				_auctionBids.add(bid);
			} else {
				if (!bid.isCanceled()) {
					if (newBid < bid.getLastBid()) // just another check
					{
						player.sendPacket(SystemMessageId.BID_MUST_BE_HIGHER_THAN_CURRENT_BID);
						return;
					}
					
					if (!reduceItemCount(player, newBid - bid.getLastBid())) {
						player.sendPacket(SystemMessageId.NOT_ENOUGH_ADENA_FOR_THIS_BID);
						return;
					}
				} else if (!reduceItemCount(player, newBid)) {
					player.sendPacket(SystemMessageId.NOT_ENOUGH_ADENA_FOR_THIS_BID);
					return;
				}
				
				bid.setLastBid(newBid);
			}
			
			onPlayerBid(player, bid);
			updatePlayerBid(bid, false);
			
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SUBMITTED_A_BID_OF_S1);
			sm.addLong(newBid);
			player.sendPacket(sm);
		}
	}
	
	private void onPlayerBid(final L2PcInstance player, final ItemAuctionBid bid) {
		if (_highestBid == null) {
			_highestBid = bid;
		} else if (_highestBid.getLastBid() < bid.getLastBid()) {
			final L2PcInstance old = _highestBid.getPlayer();
			if (old != null) {
				old.sendPacket(SystemMessageId.YOU_HAVE_BEEN_OUTBID);
			}
			
			_highestBid = bid;
		}
		
		if ((getEndingTime() - System.currentTimeMillis()) <= (1000 * 60 * 10)) // 10 minutes
		{
			switch (_auctionEndingExtendState) {
				case INITIAL: {
					_auctionEndingExtendState = ItemAuctionExtendState.EXTEND_BY_5_MIN;
					_endingTime += ENDING_TIME_EXTEND_5;
					broadcastToAllBidders(SystemMessage.getSystemMessage(SystemMessageId.BIDDER_EXISTS_AUCTION_TIME_EXTENDED_BY_5_MINUTES));
					break;
				}
				case EXTEND_BY_5_MIN: {
					if (getAndSetLastBidPlayerObjectId(player.getObjectId()) != player.getObjectId()) {
						_auctionEndingExtendState = ItemAuctionExtendState.EXTEND_BY_3_MIN;
						_endingTime += ENDING_TIME_EXTEND_3;
						broadcastToAllBidders(SystemMessage.getSystemMessage(SystemMessageId.BIDDER_EXISTS_AUCTION_TIME_EXTENDED_BY_3_MINUTES));
					}
					break;
				}
				case EXTEND_BY_3_MIN:
					if (general().getItemAuctionTimeExtendsOnBid() > 0) {
						if (getAndSetLastBidPlayerObjectId(player.getObjectId()) != player.getObjectId()) {
							_auctionEndingExtendState = ItemAuctionExtendState.EXTEND_BY_CONFIG_PHASE_A;
							_endingTime += general().getItemAuctionTimeExtendsOnBid();
						}
					}
					break;
				case EXTEND_BY_CONFIG_PHASE_A: {
					if (getAndSetLastBidPlayerObjectId(player.getObjectId()) != player.getObjectId()) {
						if (_scheduledAuctionEndingExtendState == ItemAuctionExtendState.EXTEND_BY_CONFIG_PHASE_B) {
							_auctionEndingExtendState = ItemAuctionExtendState.EXTEND_BY_CONFIG_PHASE_B;
							_endingTime += general().getItemAuctionTimeExtendsOnBid();
						}
					}
					break;
				}
				case EXTEND_BY_CONFIG_PHASE_B: {
					if (getAndSetLastBidPlayerObjectId(player.getObjectId()) != player.getObjectId()) {
						if (_scheduledAuctionEndingExtendState == ItemAuctionExtendState.EXTEND_BY_CONFIG_PHASE_A) {
							_endingTime += general().getItemAuctionTimeExtendsOnBid();
							_auctionEndingExtendState = ItemAuctionExtendState.EXTEND_BY_CONFIG_PHASE_A;
						}
					}
				}
			}
		}
	}
	
	public void broadcastToAllBidders(final L2GameServerPacket packet) {
		ThreadPoolManager.getInstance().executeGeneral(() -> broadcastToAllBiddersInternal(packet));
	}
	
	public void broadcastToAllBiddersInternal(final L2GameServerPacket packet) {
		for (int i = _auctionBids.size(); i-- > 0;) {
			final ItemAuctionBid bid = _auctionBids.get(i);
			if (bid != null) {
				final L2PcInstance player = bid.getPlayer();
				if (player != null) {
					player.sendPacket(packet);
				}
			}
		}
	}
	
	public boolean cancelBid(final L2PcInstance player) {
		if (player == null) {
			throw new NullPointerException();
		}
		
		switch (getAuctionState()) {
			case CREATED -> {
				return false;
			}
			case FINISHED -> {
				if (_startingTime < (System.currentTimeMillis() - MILLISECONDS.convert(general().getItemAuctionExpiredAfter(), DAYS))) {
					return false;
				}
			}
		}
		
		final int playerObjId = player.getObjectId();
		
		synchronized (_auctionBids) {
			if (_highestBid == null) {
				return false;
			}
			
			final int bidIndex = getBidIndexFor(playerObjId);
			if (bidIndex == -1) {
				return false;
			}
			
			final ItemAuctionBid bid = _auctionBids.get(bidIndex);
			if (bid.getPlayerObjId() == _highestBid.getPlayerObjId()) {
				// can't return winning bid
				if (getAuctionState() == ItemAuctionState.FINISHED) {
					return false;
				}
				
				player.sendPacket(SystemMessageId.HIGHEST_BID_BUT_RESERVE_NOT_MET);
				return true;
			}
			
			if (bid.isCanceled()) {
				return false;
			}
			
			increaseItemCount(player, bid.getLastBid());
			bid.cancelBid();
			
			// delete bid from database if auction already finished
			updatePlayerBid(bid, getAuctionState() == ItemAuctionState.FINISHED);
			
			player.sendPacket(SystemMessageId.CANCELED_BID);
		}
		return true;
	}
	
	public void clearCanceledBids() {
		if (getAuctionState() != ItemAuctionState.FINISHED) {
			throw new IllegalStateException("Attempt to clear canceled bids for non-finished auction");
		}
		
		synchronized (_auctionBids) {
			for (ItemAuctionBid bid : _auctionBids) {
				if ((bid == null) || !bid.isCanceled()) {
					continue;
				}
				updatePlayerBid(bid, true);
			}
		}
	}
	
	private boolean reduceItemCount(final L2PcInstance player, final long count) {
		if (!player.reduceAdena("ItemAuction", count, player, true)) {
			player.sendPacket(SystemMessageId.NOT_ENOUGH_ADENA_FOR_THIS_BID);
			return false;
		}
		return true;
	}
	
	private void increaseItemCount(final L2PcInstance player, final long count) {
		player.addAdena("ItemAuction", count, player, true);
	}
	
	/**
	 * Returns the last bid for the given player or -1 if he did not made one yet.
	 * @param player The player that made the bid
	 * @return The last bid the player made or -1
	 */
	public long getLastBid(final L2PcInstance player) {
		final ItemAuctionBid bid = getBidFor(player.getObjectId());
		return bid != null ? bid.getLastBid() : -1L;
	}
	
	public ItemAuctionBid getBidFor(final int playerObjId) {
		final int index = getBidIndexFor(playerObjId);
		return index != -1 ? _auctionBids.get(index) : null;
	}
	
	private int getBidIndexFor(final int playerObjId) {
		for (int i = _auctionBids.size(); i-- > 0;) {
			final ItemAuctionBid bid = _auctionBids.get(i);
			if ((bid != null) && (bid.getPlayerObjId() == playerObjId)) {
				return i;
			}
		}
		return -1;
	}
}