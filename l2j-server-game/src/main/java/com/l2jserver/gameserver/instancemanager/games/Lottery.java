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
package com.l2jserver.gameserver.instancemanager.games;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Broadcast;

public class Lottery {
	
	public static final long SECOND = 1000;
	
	public static final long MINUTE = 60000;
	
	protected static final Logger _log = Logger.getLogger(Lottery.class.getName());
	
	private static final String INSERT_LOTTERY = "INSERT INTO games(id, idnr, enddate, prize, newprize) VALUES (?, ?, ?, ?, ?)";
	
	private static final String UPDATE_PRICE = "UPDATE games SET prize=?, newprize=? WHERE id = 1 AND idnr = ?";
	
	private static final String UPDATE_LOTTERY = "UPDATE games SET finished=1, prize=?, newprize=?, number1=?, number2=?, prize1=?, prize2=?, prize3=? WHERE id=1 AND idnr=?";
	
	private static final String SELECT_LAST_LOTTERY = "SELECT idnr, prize, newprize, enddate, finished FROM games WHERE id = 1 ORDER BY idnr DESC LIMIT 1";
	
	private static final String SELECT_LOTTERY_ITEM = "SELECT enchant_level, custom_type2 FROM items WHERE item_id = 4442 AND custom_type1 = ?";
	
	private static final String SELECT_LOTTERY_TICKET = "SELECT number1, number2, prize1, prize2, prize3 FROM games WHERE id = 1 and idnr = ?";
	
	protected int _number;
	protected long _prize;
	protected boolean _isSellingTickets;
	protected boolean _isStarted;
	protected long _enddate;
	
	protected Lottery() {
		_number = 1;
		_prize = general().getLotteryPrize();
		_isSellingTickets = false;
		_isStarted = false;
		_enddate = System.currentTimeMillis();
		
		if (general().allowLottery()) {
			(new startLottery()).run();
		}
	}
	
	public static Lottery getInstance() {
		return SingletonHolder._instance;
	}
	
	public int getId() {
		return _number;
	}
	
	public long getPrize() {
		return _prize;
	}
	
	public long getEndDate() {
		return _enddate;
	}
	
	public void increasePrize(long count) {
		_prize += count;
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(UPDATE_PRICE)) {
			ps.setLong(1, getPrize());
			ps.setLong(2, getPrize());
			ps.setInt(3, getId());
			ps.execute();
		} catch (Exception e) {
			_log.log(Level.WARNING, "Lottery: Could not increase current lottery prize: " + e.getMessage(), e);
		}
	}
	
	public boolean isSellableTickets() {
		return _isSellingTickets;
	}
	
	public boolean isStarted() {
		return _isStarted;
	}
	
	private class startLottery implements Runnable {
		protected startLottery() {
			// Do nothing
		}
		
		@Override
		public void run() {
			
			try (var con = ConnectionFactory.getInstance().getConnection();
				var s = con.createStatement();
				var rs = s.executeQuery(SELECT_LAST_LOTTERY)) {
				if (rs.next()) {
					_number = rs.getInt("idnr");
					
					if (rs.getInt("finished") == 1) {
						_number++;
						_prize = rs.getLong("newprize");
					} else {
						_prize = rs.getLong("prize");
						_enddate = rs.getLong("enddate");
						
						if (_enddate <= (System.currentTimeMillis() + (2 * MINUTE))) {
							(new finishLottery()).run();
							return;
						}
						
						if (_enddate > System.currentTimeMillis()) {
							_isStarted = true;
							ThreadPoolManager.getInstance().scheduleGeneral(new finishLottery(), _enddate - System.currentTimeMillis());
							
							if (_enddate > (System.currentTimeMillis() + (12 * MINUTE))) {
								_isSellingTickets = true;
								ThreadPoolManager.getInstance().scheduleGeneral(new stopSellingTickets(), _enddate - System.currentTimeMillis() - (10 * MINUTE));
							}
							return;
						}
					}
				}
			} catch (Exception e) {
				_log.log(Level.WARNING, "Lottery: Could not restore lottery data: " + e.getMessage(), e);
			}
			
			if (general().debug()) {
				_log.info("Lottery: Starting ticket sell for lottery #" + getId() + ".");
			}
			_isSellingTickets = true;
			_isStarted = true;
			
			Broadcast.toAllOnlinePlayers("Lottery tickets are now available for Lucky Lottery #" + getId() + ".");
			Calendar finishtime = Calendar.getInstance();
			finishtime.setTimeInMillis(_enddate);
			finishtime.set(Calendar.MINUTE, 0);
			finishtime.set(Calendar.SECOND, 0);
			
			if (finishtime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				finishtime.set(Calendar.HOUR_OF_DAY, 19);
				_enddate = finishtime.getTimeInMillis();
				_enddate += 604800000;
			} else {
				finishtime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				finishtime.set(Calendar.HOUR_OF_DAY, 19);
				_enddate = finishtime.getTimeInMillis();
			}
			
			ThreadPoolManager.getInstance().scheduleGeneral(new stopSellingTickets(), _enddate - System.currentTimeMillis() - (10 * MINUTE));
			ThreadPoolManager.getInstance().scheduleGeneral(new finishLottery(), _enddate - System.currentTimeMillis());
			
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(INSERT_LOTTERY)) {
				ps.setInt(1, 1);
				ps.setInt(2, getId());
				ps.setLong(3, getEndDate());
				ps.setLong(4, getPrize());
				ps.setLong(5, getPrize());
				ps.execute();
			} catch (Exception e) {
				_log.log(Level.WARNING, "Lottery: Could not store new lottery data: " + e.getMessage(), e);
			}
		}
	}
	
	private class stopSellingTickets implements Runnable {
		protected stopSellingTickets() {
			// Do nothing
		}
		
		@Override
		public void run() {
			if (general().debug()) {
				_log.info("Lottery: Stopping ticket sell for lottery #" + getId() + ".");
			}
			_isSellingTickets = false;
			
			Broadcast.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.LOTTERY_TICKET_SALES_TEMP_SUSPENDED));
		}
	}
	
	private class finishLottery implements Runnable {
		protected finishLottery() {
			// Do nothing
		}
		
		@Override
		public void run() {
			if (general().debug()) {
				_log.info("Lottery: Ending lottery #" + getId() + ".");
			}
			
			int[] luckynums = new int[5];
			int luckynum = 0;
			
			for (int i = 0; i < 5; i++) {
				boolean found = true;
				
				while (found) {
					luckynum = Rnd.get(20) + 1;
					found = false;
					
					for (int j = 0; j < i; j++) {
						if (luckynums[j] == luckynum) {
							found = true;
							break;
						}
					}
				}
				
				luckynums[i] = luckynum;
			}
			
			if (general().debug()) {
				_log.info("Lottery: The lucky numbers are " + luckynums[0] + ", " + luckynums[1] + ", " + luckynums[2] + ", " + luckynums[3] + ", " + luckynums[4] + ".");
			}
			
			int enchant = 0;
			int type2 = 0;
			
			for (int i = 0; i < 5; i++) {
				if (luckynums[i] < 17) {
					enchant += Math.pow(2, luckynums[i] - 1);
				} else {
					type2 += Math.pow(2, luckynums[i] - 17);
				}
			}
			
			if (general().debug()) {
				_log.info("Lottery: Encoded lucky numbers are " + enchant + ", " + type2);
			}
			
			int count1 = 0;
			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(SELECT_LOTTERY_ITEM)) {
				ps.setInt(1, getId());
				try (var rset = ps.executeQuery()) {
					while (rset.next()) {
						int curenchant = rset.getInt("enchant_level") & enchant;
						int curtype2 = rset.getInt("custom_type2") & type2;
						
						if ((curenchant == 0) && (curtype2 == 0)) {
							continue;
						}
						
						int count = 0;
						
						for (int i = 1; i <= 16; i++) {
							int val = curenchant / 2;
							
							if (val != Math.round((double) curenchant / 2)) {
								count++;
							}
							
							int val2 = curtype2 / 2;
							
							if (val2 != ((double) curtype2 / 2)) {
								count++;
							}
							
							curenchant = val;
							curtype2 = val2;
						}
						
						if (count == 5) {
							count1++;
						} else if (count == 4) {
							count2++;
						} else if (count == 3) {
							count3++;
						} else if (count > 0) {
							count4++;
						}
					}
				}
			} catch (Exception e) {
				_log.log(Level.WARNING, "Lottery: Could restore lottery data: " + e.getMessage(), e);
			}
			
			long prize4 = count4 * general().getLottery2and1NumberPrize();
			long prize1 = 0;
			long prize2 = 0;
			long prize3 = 0;
			
			if (count1 > 0) {
				prize1 = (long) (((getPrize() - prize4) * general().getLottery5NumberRate()) / count1);
			}
			
			if (count2 > 0) {
				prize2 = (long) (((getPrize() - prize4) * general().getLottery4NumberRate()) / count2);
			}
			
			if (count3 > 0) {
				prize3 = (long) (((getPrize() - prize4) * general().getLottery3NumberRate()) / count3);
			}
			
			if (general().debug()) {
				_log.info("Lottery: " + count1 + " players with all FIVE numbers each win " + prize1 + ".");
				_log.info("Lottery: " + count2 + " players with FOUR numbers each win " + prize2 + ".");
				_log.info("Lottery: " + count3 + " players with THREE numbers each win " + prize3 + ".");
				_log.info("Lottery: " + count4 + " players with ONE or TWO numbers each win " + prize4 + ".");
			}
			
			long newprize = getPrize() - (prize1 + prize2 + prize3 + prize4);
			if (general().debug()) {
				_log.info("Lottery: Jackpot for next lottery is " + newprize + ".");
			}
			
			SystemMessage sm;
			if (count1 > 0) {
				// There are winners.
				sm = SystemMessage.getSystemMessage(SystemMessageId.AMOUNT_FOR_WINNER_S1_IS_S2_ADENA_WE_HAVE_S3_PRIZE_WINNER);
				sm.addInt(getId());
				sm.addLong(getPrize());
				sm.addLong(count1);
				Broadcast.toAllOnlinePlayers(sm);
			} else {
				// There are no winners.
				sm = SystemMessage.getSystemMessage(SystemMessageId.AMOUNT_FOR_LOTTERY_S1_IS_S2_ADENA_NO_WINNER);
				sm.addInt(getId());
				sm.addLong(getPrize());
				Broadcast.toAllOnlinePlayers(sm);
			}
			
			try (var con = ConnectionFactory.getInstance().getConnection();
				var ps = con.prepareStatement(UPDATE_LOTTERY)) {
				ps.setLong(1, getPrize());
				ps.setLong(2, newprize);
				ps.setInt(3, enchant);
				ps.setInt(4, type2);
				ps.setLong(5, prize1);
				ps.setLong(6, prize2);
				ps.setLong(7, prize3);
				ps.setInt(8, getId());
				ps.execute();
			} catch (Exception e) {
				_log.log(Level.WARNING, "Lottery: Could not store finished lottery data: " + e.getMessage(), e);
			}
			
			ThreadPoolManager.getInstance().scheduleGeneral(new startLottery(), MINUTE);
			_number++;
			
			_isStarted = false;
		}
	}
	
	public int[] decodeNumbers(int enchant, int type2) {
		int[] res = new int[5];
		int id = 0;
		int nr = 1;
		
		while (enchant > 0) {
			int val = enchant / 2;
			if (val != Math.round((double) enchant / 2)) {
				res[id++] = nr;
			}
			enchant /= 2;
			nr++;
		}
		
		nr = 17;
		
		while (type2 > 0) {
			int val = type2 / 2;
			if (val != ((double) type2 / 2)) {
				res[id++] = nr;
			}
			type2 /= 2;
			nr++;
		}
		
		return res;
	}
	
	public long[] checkTicket(L2ItemInstance item) {
		return checkTicket(item.getCustomType1(), item.getEnchantLevel(), item.getCustomType2());
	}
	
	public long[] checkTicket(int id, int enchant, int type2) {
		long[] res = {
			0,
			0
		};
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(SELECT_LOTTERY_TICKET)) {
			ps.setInt(1, id);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					int curenchant = rs.getInt("number1") & enchant;
					int curtype2 = rs.getInt("number2") & type2;
					
					if ((curenchant == 0) && (curtype2 == 0)) {
						return res;
					}
					
					int count = 0;
					
					for (int i = 1; i <= 16; i++) {
						int val = curenchant / 2;
						if (val != Math.round((double) curenchant / 2)) {
							count++;
						}
						int val2 = curtype2 / 2;
						if (val2 != ((double) curtype2 / 2)) {
							count++;
						}
						curenchant = val;
						curtype2 = val2;
					}
					
					switch (count) {
						case 0:
							break;
						case 5:
							res[0] = 1;
							res[1] = rs.getLong("prize1");
							break;
						case 4:
							res[0] = 2;
							res[1] = rs.getLong("prize2");
							break;
						case 3:
							res[0] = 3;
							res[1] = rs.getLong("prize3");
							break;
						default:
							res[0] = 4;
							res[1] = general().getLottery2and1NumberPrize();
					}
					
					if (general().debug()) {
						_log.warning("count: " + count + ", id: " + id + ", enchant: " + enchant + ", type2: " + type2);
					}
				}
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, "Lottery: Could not check lottery ticket #" + id + ": " + e.getMessage(), e);
		}
		return res;
	}
	
	private static class SingletonHolder {
		protected static final Lottery _instance = new Lottery();
	}
}
