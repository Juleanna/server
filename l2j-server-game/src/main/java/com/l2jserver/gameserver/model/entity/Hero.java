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
package com.l2jserver.gameserver.model.entity;

import static com.l2jserver.gameserver.config.Configuration.clan;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.data.sql.impl.CharNameTable;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.data.xml.impl.ClassListData;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.olympiad.Olympiad;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExBrExtraUserInfo;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;
import com.l2jserver.gameserver.util.StringUtil;

/**
 * Hero entity.
 * @author godson
 */
public class Hero {
	
	private static final Logger LOG = LoggerFactory.getLogger(Hero.class);
	
	private static final String GET_HEROES = "SELECT heroes.charId, characters.char_name, heroes.class_id, heroes.count, heroes.played, heroes.claimed FROM heroes, characters WHERE characters.charId = heroes.charId AND heroes.played = 1";
	private static final String GET_ALL_HEROES = "SELECT heroes.charId, characters.char_name, heroes.class_id, heroes.count, heroes.played, heroes.claimed FROM heroes, characters WHERE characters.charId = heroes.charId";
	private static final String UPDATE_ALL = "UPDATE heroes SET played = 0";
	private static final String INSERT_HERO = "INSERT INTO heroes (charId, class_id, count, played, claimed) VALUES (?,?,?,?,?)";
	private static final String UPDATE_HERO = "UPDATE heroes SET count = ?, played = ?, claimed = ? WHERE charId = ?";
	private static final String GET_CLAN_ALLY = "SELECT characters.clanid AS clanid, coalesce(clan_data.ally_Id, 0) AS allyId FROM characters LEFT JOIN clan_data ON clan_data.clan_id = characters.clanid WHERE characters.charId = ?";
	// delete hero items
	private static final String DELETE_ITEMS = "DELETE FROM items WHERE item_id IN (6842, 6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6619, 6620, 6621, 9388, 9389, 9390) AND owner_id NOT IN (SELECT charId FROM characters WHERE accesslevel > 0)";
	
	private static final Map<Integer, StatsSet> HEROES = new ConcurrentHashMap<>();
	private static final Map<Integer, StatsSet> COMPLETE_HEROES = new ConcurrentHashMap<>();
	
	private static final Map<Integer, StatsSet> HERO_COUNTS = new ConcurrentHashMap<>();
	private static final Map<Integer, List<StatsSet>> HERO_FIGHTS = new ConcurrentHashMap<>();
	
	private static final Map<Integer, List<StatsSet>> HERO_DIARY = new ConcurrentHashMap<>();
	private static final Map<Integer, String> HERO_MESSAGE = new ConcurrentHashMap<>();
	
	public static final String COUNT = "count";
	public static final String PLAYED = "played";
	public static final String CLAIMED = "claimed";
	public static final String CLAN_NAME = "clan_name";
	public static final String CLAN_CREST = "clan_crest";
	public static final String ALLY_NAME = "ally_name";
	public static final String ALLY_CREST = "ally_crest";
	
	public static final int ACTION_RAID_KILLED = 1;
	public static final int ACTION_HERO_GAINED = 2;
	public static final int ACTION_CASTLE_TAKEN = 3;
	
	protected Hero() {
		init();
	}
	
	private void init() {
		HEROES.clear();
		COMPLETE_HEROES.clear();
		HERO_COUNTS.clear();
		HERO_FIGHTS.clear();
		HERO_DIARY.clear();
		HERO_MESSAGE.clear();
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s1 = con.createStatement();
			var rs1 = s1.executeQuery(GET_HEROES);
			var ps = con.prepareStatement(GET_CLAN_ALLY);
			var s2 = con.createStatement();
			var rs2 = s2.executeQuery(GET_ALL_HEROES)) {
			while (rs1.next()) {
				StatsSet hero = new StatsSet();
				int charId = rs1.getInt(Olympiad.CHAR_ID);
				hero.set(Olympiad.CHAR_NAME, rs1.getString(Olympiad.CHAR_NAME));
				hero.set(Olympiad.CLASS_ID, rs1.getInt(Olympiad.CLASS_ID));
				hero.set(COUNT, rs1.getInt(COUNT));
				hero.set(PLAYED, rs1.getInt(PLAYED));
				hero.set(CLAIMED, rs1.getBoolean(CLAIMED));
				
				loadFights(charId);
				loadDiary(charId);
				loadMessage(charId);
				
				processHeroes(ps, charId, hero);
				
				HEROES.put(charId, hero);
			}
			
			while (rs2.next()) {
				StatsSet hero = new StatsSet();
				int charId = rs2.getInt(Olympiad.CHAR_ID);
				hero.set(Olympiad.CHAR_NAME, rs2.getString(Olympiad.CHAR_NAME));
				hero.set(Olympiad.CLASS_ID, rs2.getInt(Olympiad.CLASS_ID));
				hero.set(COUNT, rs2.getInt(COUNT));
				hero.set(PLAYED, rs2.getInt(PLAYED));
				hero.set(CLAIMED, rs2.getBoolean(CLAIMED));
				
				processHeroes(ps, charId, hero);
				
				COMPLETE_HEROES.put(charId, hero);
			}
		} catch (Exception ex) {
			LOG.warn("Couldn't load Heroes!", ex);
		}
		
		LOG.info("Loaded {} Heroes.", HEROES.size());
		LOG.info("Loaded {} all time Heroes.", COMPLETE_HEROES.size());
	}
	
	private void processHeroes(PreparedStatement ps, int charId, StatsSet hero) throws Exception {
		ps.setInt(1, charId);
		try (var rs = ps.executeQuery()) {
			if (rs.next()) {
				int clanId = rs.getInt("clanid");
				int allyId = rs.getInt("allyId");
				String clanName = "";
				String allyName = "";
				int clanCrest = 0;
				int allyCrest = 0;
				if (clanId > 0) {
					clanName = ClanTable.getInstance().getClan(clanId).getName();
					clanCrest = ClanTable.getInstance().getClan(clanId).getCrestId();
					if (allyId > 0) {
						allyName = ClanTable.getInstance().getClan(clanId).getAllyName();
						allyCrest = ClanTable.getInstance().getClan(clanId).getAllyCrestId();
					}
				}
				hero.set(CLAN_CREST, clanCrest);
				hero.set(CLAN_NAME, clanName);
				hero.set(ALLY_CREST, allyCrest);
				hero.set(ALLY_NAME, allyName);
			}
			ps.clearParameters();
		}
	}
	
	private String calcFightTime(long FightTime) {
		String format = String.format("%%0%dd", 2);
		FightTime = FightTime / 1000;
		String seconds = String.format(format, FightTime % 60);
		String minutes = String.format(format, (FightTime % 3600) / 60);
		return minutes + ":" + seconds;
	}
	
	/**
	 * Restore hero message from Db.
	 * @param charId
	 */
	public void loadMessage(int charId) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT message FROM heroes WHERE charId=?")) {
			ps.setInt(1, charId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					HERO_MESSAGE.put(charId, rs.getString("message"));
				}
			}
		} catch (Exception ex) {
			LOG.warn("Could not load Hero message for player Id {}!", charId, ex);
		}
	}
	
	public void loadDiary(int charId) {
		final List<StatsSet> diary = new ArrayList<>();
		int diaryEntries = 0;
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT * FROM  heroes_diary WHERE charId=? ORDER BY time")) {
			ps.setInt(1, charId);
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					StatsSet _diaryentry = new StatsSet();
					
					long time = rs.getLong("time");
					int action = rs.getInt("action");
					int param = rs.getInt("param");
					
					String date = (new SimpleDateFormat("yyyy-MM-dd HH")).format(new Date(time));
					_diaryentry.set("date", date);
					
					if (action == ACTION_RAID_KILLED) {
						L2NpcTemplate template = NpcData.getInstance().getTemplate(param);
						if (template != null) {
							_diaryentry.set("action", template.getName() + " was defeated");
						}
					} else if (action == ACTION_HERO_GAINED) {
						_diaryentry.set("action", "Gained Hero status");
					} else if (action == ACTION_CASTLE_TAKEN) {
						Castle castle = CastleManager.getInstance().getCastleById(param);
						if (castle != null) {
							_diaryentry.set("action", castle.getName() + " Castle was successfully taken");
						}
					}
					diary.add(_diaryentry);
					diaryEntries++;
				}
			}
			HERO_DIARY.put(charId, diary);
			
			LOG.info("Loaded {} diary entries for Hero {}.", diaryEntries, CharNameTable.getInstance().getNameById(charId));
		} catch (Exception ex) {
			LOG.warn("Could not load Hero Diary for player Id {}!", charId, ex);
		}
	}
	
	public void loadFights(int charId) {
		final List<StatsSet> fights = new ArrayList<>();
		StatsSet heroCountData = new StatsSet();
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, 1);
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.MILLISECOND, 0);
		
		long from = data.getTimeInMillis();
		int numberOfFights = 0;
		int _victorys = 0;
		int _losses = 0;
		int _draws = 0;
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("SELECT * FROM olympiad_fights WHERE (charOneId=? OR charTwoId=?) AND start<? ORDER BY start")) {
			ps.setInt(1, charId);
			ps.setInt(2, charId);
			ps.setLong(3, from);
			try (var rs = ps.executeQuery()) {
				int charOneId;
				int charOneClass;
				int charTwoId;
				int charTwoClass;
				int winner;
				long start;
				long time;
				int classed;
				while (rs.next()) {
					charOneId = rs.getInt("charOneId");
					charOneClass = rs.getInt("charOneClass");
					charTwoId = rs.getInt("charTwoId");
					charTwoClass = rs.getInt("charTwoClass");
					winner = rs.getInt("winner");
					start = rs.getLong("start");
					time = rs.getLong("time");
					classed = rs.getInt("classed");
					
					if (charId == charOneId) {
						String name = CharNameTable.getInstance().getNameById(charTwoId);
						String cls = ClassListData.getInstance().getClass(charTwoClass).getClientCode();
						if (name != null) {
							StatsSet fight = new StatsSet();
							fight.set("oponent", name);
							fight.set("oponentclass", cls);
							
							fight.set("time", calcFightTime(time));
							String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date(start));
							fight.set("start", date);
							
							fight.set("classed", classed);
							if (winner == 1) {
								fight.set("result", "<font color=\"00ff00\">victory</font>");
								_victorys++;
							} else if (winner == 2) {
								fight.set("result", "<font color=\"ff0000\">loss</font>");
								_losses++;
							} else if (winner == 0) {
								fight.set("result", "<font color=\"ffff00\">draw</font>");
								_draws++;
							}
							
							fights.add(fight);
							
							numberOfFights++;
						}
					} else if (charId == charTwoId) {
						String name = CharNameTable.getInstance().getNameById(charOneId);
						String cls = ClassListData.getInstance().getClass(charOneClass).getClientCode();
						if (name != null) {
							StatsSet fight = new StatsSet();
							fight.set("oponent", name);
							fight.set("oponentclass", cls);
							
							fight.set("time", calcFightTime(time));
							String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date(start));
							fight.set("start", date);
							
							fight.set("classed", classed);
							if (winner == 1) {
								fight.set("result", "<font color=\"ff0000\">loss</font>");
								_losses++;
							} else if (winner == 2) {
								fight.set("result", "<font color=\"00ff00\">victory</font>");
								_victorys++;
							} else if (winner == 0) {
								fight.set("result", "<font color=\"ffff00\">draw</font>");
								_draws++;
							}
							
							fights.add(fight);
							
							numberOfFights++;
						}
					}
				}
			}
			
			heroCountData.set("victory", _victorys);
			heroCountData.set("draw", _draws);
			heroCountData.set("loss", _losses);
			
			HERO_COUNTS.put(charId, heroCountData);
			HERO_FIGHTS.put(charId, fights);
			
			LOG.info("Loaded {} fights for Hero {}.", numberOfFights, CharNameTable.getInstance().getNameById(charId));
		} catch (Exception ex) {
			LOG.warn("Could not load Hero fights history for player Id {}!", charId, ex);
		}
	}
	
	public Map<Integer, StatsSet> getHeroes() {
		return HEROES;
	}
	
	public int getHeroByClass(int classId) {
		for (Entry<Integer, StatsSet> e : HEROES.entrySet()) {
			if (e.getValue().getInt(Olympiad.CLASS_ID) == classId) {
				return e.getKey();
			}
		}
		return 0;
	}
	
	public void resetData() {
		HERO_DIARY.clear();
		HERO_FIGHTS.clear();
		HERO_COUNTS.clear();
		HERO_MESSAGE.clear();
	}
	
	public void showHeroDiary(L2PcInstance activeChar, int heroclass, int charid, int page) {
		final int perpage = 10;
		
		final List<StatsSet> mainList = HERO_DIARY.get(charid);
		if (mainList != null) {
			final NpcHtmlMessage diaryReply = new NpcHtmlMessage();
			final String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/olympiad/herodiary.htm");
			final String heroMessage = HERO_MESSAGE.get(charid);
			if ((htmContent != null) && (heroMessage != null)) {
				diaryReply.setHtml(htmContent);
				diaryReply.replace("%heroname%", CharNameTable.getInstance().getNameById(charid));
				diaryReply.replace("%message%", heroMessage);
				diaryReply.disableValidation();
				
				if (!mainList.isEmpty()) {
					final List<StatsSet> list = new ArrayList<>(mainList);
					Collections.reverse(list);
					
					boolean color = true;
					final StringBuilder fList = new StringBuilder(500);
					int counter = 0;
					int breakat = 0;
					for (int i = ((page - 1) * perpage); i < list.size(); i++) {
						breakat = i;
						StatsSet diaryEntry = list.get(i);
						StringUtil.append(fList, "<tr><td>");
						if (color) {
							StringUtil.append(fList, "<table width=270 bgcolor=\"131210\">");
						} else {
							StringUtil.append(fList, "<table width=270>");
						}
						StringUtil.append(fList, "<tr><td width=270><font color=\"LEVEL\">" + diaryEntry.getString("date") + ":xx</font></td></tr>");
						StringUtil.append(fList, "<tr><td width=270>" + diaryEntry.getString("action") + "</td></tr>");
						StringUtil.append(fList, "<tr><td>&nbsp;</td></tr></table>");
						StringUtil.append(fList, "</td></tr>");
						color = !color;
						counter++;
						if (counter >= perpage) {
							break;
						}
					}
					
					if (breakat < (list.size() - 1)) {
						diaryReply.replace("%buttprev%", "<button value=\"Prev\" action=\"bypass _diary?class=" + heroclass + "&page=" + (page + 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					} else {
						diaryReply.replace("%buttprev%", "");
					}
					
					if (page > 1) {
						diaryReply.replace("%buttnext%", "<button value=\"Next\" action=\"bypass _diary?class=" + heroclass + "&page=" + (page - 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					} else {
						diaryReply.replace("%buttnext%", "");
					}
					
					diaryReply.replace("%list%", fList.toString());
				} else {
					diaryReply.replace("%list%", "");
					diaryReply.replace("%buttprev%", "");
					diaryReply.replace("%buttnext%", "");
				}
				
				activeChar.sendPacket(diaryReply);
			}
		}
	}
	
	public void showHeroFights(L2PcInstance activeChar, int heroclass, int charid, int page) {
		final int perpage = 20;
		int _win = 0;
		int _loss = 0;
		int _draw = 0;
		
		final List<StatsSet> heroFights = HERO_FIGHTS.get(charid);
		if (heroFights != null) {
			final NpcHtmlMessage FightReply = new NpcHtmlMessage();
			final String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/olympiad/herohistory.htm");
			if (htmContent != null) {
				FightReply.setHtml(htmContent);
				FightReply.replace("%heroname%", CharNameTable.getInstance().getNameById(charid));
				
				if (!heroFights.isEmpty()) {
					final StatsSet heroCount = HERO_COUNTS.get(charid);
					if (heroCount != null) {
						_win = heroCount.getInt("victory");
						_loss = heroCount.getInt("loss");
						_draw = heroCount.getInt("draw");
					}
					
					boolean color = true;
					final StringBuilder fList = new StringBuilder(500);
					int counter = 0;
					int breakat = 0;
					for (int i = ((page - 1) * perpage); i < heroFights.size(); i++) {
						breakat = i;
						StatsSet fight = heroFights.get(i);
						StringUtil.append(fList, "<tr><td>");
						if (color) {
							StringUtil.append(fList, "<table width=270 bgcolor=\"131210\">");
						} else {
							StringUtil.append(fList, "<table width=270>");
						}
						StringUtil.append(fList, "<tr><td width=220><font color=\"LEVEL\">" + fight.getString("start") + "</font>&nbsp;&nbsp;" + fight.getString("result") + //
							"</td><td width=50 align=right>" + (fight.getInt("classed") > 0 ? "<font color=\"FFFF99\">cls</font>" : "<font color=\"999999\">non-cls<font>") + "</td></tr>");
						StringUtil.append(fList, "<tr><td width=220>vs " + fight.getString("oponent") + " (" + fight.getString("oponentclass") + ")</td><td width=50 align=right>(" + fight.getString("time") + ")</td></tr>");
						StringUtil.append(fList, "<tr><td colspan=2>&nbsp;</td></tr></table>");
						StringUtil.append(fList, "</td></tr>");
						color = !color;
						counter++;
						if (counter >= perpage) {
							break;
						}
					}
					
					if (breakat < (heroFights.size() - 1)) {
						FightReply.replace("%buttprev%", "<button value=\"Prev\" action=\"bypass _match?class=" + heroclass + "&page=" + (page + 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					} else {
						FightReply.replace("%buttprev%", "");
					}
					
					if (page > 1) {
						FightReply.replace("%buttnext%", "<button value=\"Next\" action=\"bypass _match?class=" + heroclass + "&page=" + (page - 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					} else {
						FightReply.replace("%buttnext%", "");
					}
					
					FightReply.replace("%list%", fList.toString());
				} else {
					FightReply.replace("%list%", "");
					FightReply.replace("%buttprev%", "");
					FightReply.replace("%buttnext%", "");
				}
				
				FightReply.replace("%win%", String.valueOf(_win));
				FightReply.replace("%draw%", String.valueOf(_draw));
				FightReply.replace("%loos%", String.valueOf(_loss));
				
				activeChar.sendPacket(FightReply);
			}
		}
	}
	
	public synchronized void computeNewHeroes(List<StatsSet> newHeroes) {
		updateHeroes(true);
		
		for (Integer objectId : HEROES.keySet()) {
			final L2PcInstance player = L2World.getInstance().getPlayer(objectId);
			if (player == null) {
				continue;
			}
			
			player.setHero(false);
			
			for (int i = 0; i < Inventory.PAPERDOLL_TOTALSLOTS; i++) {
				L2ItemInstance equippedItem = player.getInventory().getPaperdollItem(i);
				if ((equippedItem != null) && equippedItem.isHeroItem()) {
					player.getInventory().unEquipItemInSlot(i);
				}
			}
			
			final InventoryUpdate iu = new InventoryUpdate();
			for (L2ItemInstance item : player.getInventory().getItems()) {
				if ((item != null) && item.isHeroItem()) {
					player.destroyItem("Hero", item, null, true);
					iu.addRemovedItem(item);
				}
			}
			
			if (!iu.getItems().isEmpty()) {
				player.sendPacket(iu);
			}
			
			player.broadcastUserInfo();
		}
		
		deleteItemsInDb();
		
		HEROES.clear();
		
		if (newHeroes.isEmpty()) {
			return;
		}
		
		for (StatsSet hero : newHeroes) {
			int charId = hero.getInt(Olympiad.CHAR_ID);
			
			if (COMPLETE_HEROES.containsKey(charId)) {
				StatsSet oldHero = COMPLETE_HEROES.get(charId);
				int count = oldHero.getInt(COUNT);
				oldHero.set(COUNT, count + 1);
				oldHero.set(PLAYED, 1);
				oldHero.set(CLAIMED, false);
				HEROES.put(charId, oldHero);
			} else {
				StatsSet newHero = new StatsSet();
				newHero.set(Olympiad.CHAR_NAME, hero.getString(Olympiad.CHAR_NAME));
				newHero.set(Olympiad.CLASS_ID, hero.getInt(Olympiad.CLASS_ID));
				newHero.set(COUNT, 1);
				newHero.set(PLAYED, 1);
				newHero.set(CLAIMED, false);
				HEROES.put(charId, newHero);
			}
		}
		
		updateHeroes(false);
	}
	
	public void updateHeroes(boolean setDefault) {
		try (var con = ConnectionFactory.getInstance().getConnection()) {
			if (setDefault) {
				try (var s = con.createStatement()) {
					s.executeUpdate(UPDATE_ALL);
				}
			} else {
				StatsSet hero;
				int heroId;
				for (Entry<Integer, StatsSet> entry : HEROES.entrySet()) {
					hero = entry.getValue();
					heroId = entry.getKey();
					if (!COMPLETE_HEROES.containsKey(heroId)) {
						try (var insert = con.prepareStatement(INSERT_HERO)) {
							insert.setInt(1, heroId);
							insert.setInt(2, hero.getInt(Olympiad.CLASS_ID));
							insert.setInt(3, hero.getInt(COUNT));
							insert.setInt(4, hero.getInt(PLAYED));
							insert.setBoolean(5, hero.getBoolean(CLAIMED));
							insert.execute();
						}
						
						try (var statement = con.prepareStatement(GET_CLAN_ALLY)) {
							statement.setInt(1, heroId);
							try (var rs = statement.executeQuery()) {
								if (rs.next()) {
									int clanId = rs.getInt("clanid");
									int allyId = rs.getInt("allyId");
									
									String clanName = "";
									String allyName = "";
									int clanCrest = 0;
									int allyCrest = 0;
									
									if (clanId > 0) {
										clanName = ClanTable.getInstance().getClan(clanId).getName();
										clanCrest = ClanTable.getInstance().getClan(clanId).getCrestId();
										
										if (allyId > 0) {
											allyName = ClanTable.getInstance().getClan(clanId).getAllyName();
											allyCrest = ClanTable.getInstance().getClan(clanId).getAllyCrestId();
										}
									}
									
									hero.set(CLAN_CREST, clanCrest);
									hero.set(CLAN_NAME, clanName);
									hero.set(ALLY_CREST, allyCrest);
									hero.set(ALLY_NAME, allyName);
								}
							}
						}
						HEROES.put(heroId, hero);
						
						COMPLETE_HEROES.put(heroId, hero);
					} else {
						try (var statement = con.prepareStatement(UPDATE_HERO)) {
							statement.setInt(1, hero.getInt(COUNT));
							statement.setInt(2, hero.getInt(PLAYED));
							statement.setBoolean(3, hero.getBoolean(CLAIMED));
							statement.setInt(4, heroId);
							statement.execute();
						}
					}
				}
			}
		} catch (Exception ex) {
			LOG.warn("Could not update Heroes!", ex);
		}
	}
	
	public void setHeroGained(int charId) {
		setDiaryData(charId, ACTION_HERO_GAINED, 0);
	}
	
	public void setRBkilled(int charId, int npcId) {
		setDiaryData(charId, ACTION_RAID_KILLED, npcId);
		
		final L2NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
		final List<StatsSet> list = HERO_DIARY.get(charId);
		if ((list != null) && (template != null)) {
			// Prepare new data
			final StatsSet diaryEntry = new StatsSet();
			final String date = (new SimpleDateFormat("yyyy-MM-dd HH")).format(new Date(System.currentTimeMillis()));
			diaryEntry.set("date", date);
			diaryEntry.set("action", template.getName() + " was defeated");
			// Add to old list
			list.add(diaryEntry);
		}
	}
	
	public void setCastleTaken(int charId, int castleId) {
		setDiaryData(charId, ACTION_CASTLE_TAKEN, castleId);
		
		final Castle castle = CastleManager.getInstance().getCastleById(castleId);
		final List<StatsSet> list = HERO_DIARY.get(charId);
		if ((list != null) && (castle != null)) {
			// Prepare new data
			final StatsSet diaryEntry = new StatsSet();
			final String date = (new SimpleDateFormat("yyyy-MM-dd HH")).format(new Date(System.currentTimeMillis()));
			diaryEntry.set("date", date);
			diaryEntry.set("action", castle.getName() + " Castle was successfully taken");
			// Add to old list
			list.add(diaryEntry);
		}
	}
	
	public void setDiaryData(int charId, int action, int param) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("INSERT INTO heroes_diary (charId, time, action, param) values(?,?,?,?)")) {
			ps.setInt(1, charId);
			ps.setLong(2, System.currentTimeMillis());
			ps.setInt(3, action);
			ps.setInt(4, param);
			ps.execute();
		} catch (Exception ex) {
			LOG.warn("There has been an error saving diary data!", ex);
		}
	}
	
	/**
	 * Set new hero message for hero
	 * @param player the player instance
	 * @param message String to set
	 */
	public void setHeroMessage(L2PcInstance player, String message) {
		HERO_MESSAGE.put(player.getObjectId(), message);
	}
	
	/**
	 * Update hero message in database
	 * @param charId the character Id
	 */
	public void saveHeroMessage(int charId) {
		if (HERO_MESSAGE.containsKey(charId)) {
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("UPDATE heroes SET message=? WHERE charId=?;")) {
			ps.setString(1, HERO_MESSAGE.get(charId));
			ps.setInt(2, charId);
			ps.execute();
		} catch (Exception ex) {
			LOG.warn("There has been an error updating Hero message!", ex);
		}
	}
	
	private void deleteItemsInDb() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var s = con.createStatement()) {
			s.executeUpdate(DELETE_ITEMS);
		} catch (Exception ex) {
			LOG.warn("There has been an error deleting Hero items!", ex);
		}
	}
	
	/**
	 * Saving task for {@link Hero}<BR>
	 * Save all hero messages to DB.
	 */
	public void shutdown() {
		HERO_MESSAGE.keySet().forEach(this::saveHeroMessage);
	}
	
	/**
	 * Verifies if the given object ID belongs to a claimed hero.
	 * @param objectId the player's object ID to verify
	 * @return {@code true} if there are heroes and the player is in the list, {@code false} otherwise
	 */
	public boolean isHero(int objectId) {
		return HEROES.containsKey(objectId) && HEROES.get(objectId).getBoolean(CLAIMED);
	}
	
	/**
	 * Verifies if the given object ID belongs to an unclaimed hero.
	 * @param objectId the player's object ID to verify
	 * @return {@code true} if player is unclaimed hero
	 */
	public boolean isUnclaimedHero(int objectId) {
		return HEROES.containsKey(objectId) && !HEROES.get(objectId).getBoolean(CLAIMED);
	}
	
	/**
	 * Claims the hero status for the given player.
	 * @param player the player to become hero
	 */
	public void claimHero(L2PcInstance player) {
		StatsSet hero = HEROES.get(player.getObjectId());
		if (hero == null) {
			hero = new StatsSet();
			HEROES.put(player.getObjectId(), hero);
		}
		
		hero.set(CLAIMED, true);
		
		final L2Clan clan = player.getClan();
		if ((clan != null) && (clan.getLevel() >= 5)) {
			clan.addReputationScore(clan().getHeroPoints(), true);
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_C1_BECAME_HERO_AND_GAINED_S2_REPUTATION_POINTS);
			sm.addString(CharNameTable.getInstance().getNameById(player.getObjectId()));
			sm.addInt(clan().getHeroPoints());
			clan.broadcastToOnlineMembers(sm);
		}
		
		player.setHero(true);
		player.broadcastPacket(new SocialAction(player.getObjectId(), 20016)); // Hero Animation
		player.sendPacket(new UserInfo(player));
		player.sendPacket(new ExBrExtraUserInfo(player));
		player.broadcastUserInfo();
		// Set Gained hero and reload data
		setHeroGained(player.getObjectId());
		loadFights(player.getObjectId());
		loadDiary(player.getObjectId());
		HERO_MESSAGE.put(player.getObjectId(), "");
		
		updateHeroes(false);
	}
	
	public static Hero getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final Hero INSTANCE = new Hero();
	}
}
