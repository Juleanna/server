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
package com.l2jserver.gameserver.bbs.service;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

import com.l2jserver.gameserver.bbs.model.Forum;
import com.l2jserver.gameserver.bbs.model.ForumType;
import com.l2jserver.gameserver.bbs.model.Post;
import com.l2jserver.gameserver.bbs.model.Topic;
import com.l2jserver.gameserver.bbs.model.TopicType;
import com.l2jserver.gameserver.dao.factory.impl.DAOFactory;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.StringUtil;

/**
 * Topic BBS Manager.
 * @author Zoey76
 * @version 2.6.2.0
 */
public class TopicBBSManager extends BaseBBSManager {
	
	private final List<Topic> table = new CopyOnWriteArrayList<>();
	
	private final Map<Forum, Integer> maxId = new HashMap<>();
	
	protected TopicBBSManager() {
		// Do nothing.
	}
	
	public void addTopic(Topic topic) {
		table.add(topic);
	}
	
	public void delTopic(Topic topic) {
		table.remove(topic);
	}
	
	public void setMaxId(int id, Forum forum) {
		maxId.put(forum, id);
	}
	
	public int getMaxId(Forum f) {
		var i = maxId.get(f);
		if (i == null) {
			return 0;
		}
		return i;
	}
	
	public Topic getTopicById(int id) {
		for (var topic : table) {
			if (topic.getId() == id) {
				return topic;
			}
		}
		return null;
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar) {
		if (ar1.equals("crea")) {
			final var forum = ForumsBBSManager.getInstance().getForumById(Integer.parseInt(ar2));
			if (forum == null) {
				CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + ar2 + " is not implemented yet</center><br><br></body></html>", activeChar);
				return;
			}
			
			final var id = TopicBBSManager.getInstance().getMaxId(forum) + 1;
			final var date = Calendar.getInstance().getTimeInMillis();
			final var topic = new Topic(id, Integer.parseInt(ar2), ar5, date, activeChar.getName(), activeChar.getObjectId(), TopicType.MEMO, 0);
			
			TopicBBSManager.getInstance().addTopic(topic);
			
			DAOFactory.getInstance().getTopicRepository().save(topic);
			
			forum.addTopic(topic);
			TopicBBSManager.getInstance().setMaxId(topic.getId(), forum);
			
			final var posts = new LinkedList<Post>();
			posts.add(new Post(0, activeChar.getName(), activeChar.getObjectId(), Calendar.getInstance().getTimeInMillis(), topic.getId(), forum.getId(), ar4));
			PostBBSManager.getInstance().addPostByTopic(topic, posts);
			parsecmd("_bbsmemo", activeChar);
		} else if (ar1.equals("del")) {
			final var forum = ForumsBBSManager.getInstance().getForumById(Integer.parseInt(ar2));
			if (forum == null) {
				CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + ar2 + " does not exist !</center><br><br></body></html>", activeChar);
				return;
			}
			
			final var topic = forum.getTopic(Integer.parseInt(ar3));
			if (topic == null) {
				CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the topic: " + ar3 + " does not exist !</center><br><br></body></html>", activeChar);
				return;
			}
			
			final var posts = PostBBSManager.getInstance().getGPostByTopic(topic);
			if (!posts.isEmpty()) {
				PostBBSManager.getInstance().delPostByTopic(topic);
				DAOFactory.getInstance().getPostRepository().delete(topic);
			}
			
			DAOFactory.getInstance().getTopicRepository().delete(topic, forum);
			
			parsecmd("_bbsmemo", activeChar);
		} else {
			CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the command: " + ar1 + " is not implemented yet</center><br><br></body></html>", activeChar);
		}
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar) {
		if (command.equals("_bbsmemo")) {
			showTopics(activeChar.getMemo(), activeChar, 1, activeChar.getMemo().getId());
		} else if (command.startsWith("_bbstopics;read")) {
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int idf = Integer.parseInt(st.nextToken());
			String index = null;
			if (st.hasMoreTokens()) {
				index = st.nextToken();
			}
			int ind;
			if (index == null) {
				ind = 1;
			} else {
				ind = Integer.parseInt(index);
			}
			showTopics(ForumsBBSManager.getInstance().getForumById(idf), activeChar, ind, idf);
		} else if (command.startsWith("_bbstopics;crea")) {
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int idf = Integer.parseInt(st.nextToken());
			showNewTopic(ForumsBBSManager.getInstance().getForumById(idf), activeChar, idf);
		} else if (command.startsWith("_bbstopics;del")) {
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int idf = Integer.parseInt(st.nextToken());
			int idt = Integer.parseInt(st.nextToken());
			Forum f = ForumsBBSManager.getInstance().getForumById(idf);
			if (f == null) {
				CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + idf + " does not exist !</center><br><br></body></html>", activeChar);
			} else {
				Topic t = f.getTopic(idt);
				if (t == null) {
					CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the topic: " + idt + " does not exist !</center><br><br></body></html>", activeChar);
				} else {
					final var p = PostBBSManager.getInstance().getGPostByTopic(t);
					if (p != null) {
						PostBBSManager.getInstance().delPostByTopic(t);
						DAOFactory.getInstance().getPostRepository().delete(t);
					}
					
					DAOFactory.getInstance().getTopicRepository().delete(t, f);
					
					parsecmd("_bbsmemo", activeChar);
				}
			}
		} else {
			CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", activeChar);
		}
	}
	
	private void showNewTopic(Forum forum, L2PcInstance activeChar, int idf) {
		if (forum == null) {
			CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + idf + " is not implemented yet</center><br><br></body></html>", activeChar);
		} else if (forum.getType() == ForumType.MEMO) {
			showMemoNewTopics(forum, activeChar);
		} else {
			CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + forum.getName() + " is not implemented yet</center><br><br></body></html>", activeChar);
		}
	}
	
	private void showMemoNewTopics(Forum forum, L2PcInstance activeChar) {
		final String html = StringUtil
			.concat("<html><body><br><br><table border=0 width=610><tr><td width=10></td><td width=600 align=left><a action=\"bypass _bbshome\">HOME</a>&nbsp;>&nbsp;<a action=\"bypass _bbsmemo\">Memo Form</a></td></tr></table><img src=\"L2UI.squareblank\" width=\"1\" height=\"10\"><center><table border=0 cellspacing=0 cellpadding=0><tr><td width=610><img src=\"sek.cbui355\" width=\"610\" height=\"1\"><br1><img src=\"sek.cbui355\" width=\"610\" height=\"1\"></td></tr></table><table fixwidth=610 border=0 cellspacing=0 cellpadding=0><tr><td><img src=\"l2ui.mini_logo\" width=5 height=20></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=1></td><td align=center FIXWIDTH=60 height=29>&$413;</td><td FIXWIDTH=540><edit var = \"Title\" width=540 height=13></td><td><img src=\"l2ui.mini_logo\" width=5 height=1></td></tr></table><table fixwidth=610 border=0 cellspacing=0 cellpadding=0><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=1></td><td align=center FIXWIDTH=60 height=29 valign=top>&$427;</td><td align=center FIXWIDTH=540><MultiEdit var =\"Content\" width=535 height=313></td><td><img src=\"l2ui.mini_logo\" width=5 height=1></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr></table><table fixwidth=610 border=0 cellspacing=0 cellpadding=0><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=1></td><td align=center FIXWIDTH=60 height=29>&nbsp;</td><td align=center FIXWIDTH=70><button value=\"&$140;\" action=\"Write Topic crea ", String
				.valueOf(forum
					.getId()), " Title Content Title\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\" ></td><td align=center FIXWIDTH=70><button value = \"&$141;\" action=\"bypass _bbsmemo\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"> </td><td align=center FIXWIDTH=400>&nbsp;</td><td><img src=\"l2ui.mini_logo\" width=5 height=1></td></tr></table></center></body></html>");
		send1001(html, activeChar);
		send1002(activeChar);
	}
	
	private void showTopics(Forum forum, L2PcInstance activeChar, int index, int idf) {
		if (forum == null) {
			CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + idf + " is not implemented yet</center><br><br></body></html>", activeChar);
		} else if (forum.getType() == ForumType.MEMO) {
			showMemoTopics(forum, activeChar, index);
		} else {
			CommunityBoardHandler.separateAndSend("<html><body><br><br><center>the forum: " + forum.getName() + " is not implemented yet</center><br><br></body></html>", activeChar);
		}
	}
	
	private void showMemoTopics(Forum forum, L2PcInstance activeChar, int index) {
		final StringBuilder html = StringUtil
			.startAppend(2000, "<html><body><br><br><table border=0 width=610><tr><td width=10></td><td width=600 align=left><a action=\"bypass _bbshome\">HOME</a>&nbsp;>&nbsp;<a action=\"bypass _bbsmemo\">Memo Form</a></td></tr></table><img src=\"L2UI.squareblank\" width=\"1\" height=\"10\"><center><table border=0 cellspacing=0 cellpadding=2 bgcolor=888888 width=610><tr><td FIXWIDTH=5></td><td FIXWIDTH=415 align=center>&$413;</td><td FIXWIDTH=120 align=center></td><td FIXWIDTH=70 align=center>&$418;</td></tr></table>");
		final DateFormat dateFormat = DateFormat.getInstance();
		
		for (int i = 0, j = getMaxId(forum) + 1; i < (12 * index); j--) {
			if (j < 0) {
				break;
			}
			Topic t = forum.getTopic(j);
			if (t != null) {
				if (i++ >= (12 * (index - 1))) {
					StringUtil.append(html, "<table border=0 cellspacing=0 cellpadding=5 WIDTH=610><tr><td FIXWIDTH=5></td><td FIXWIDTH=415><a action=\"bypass _bbsposts;read;", String.valueOf(forum.getId()), ";", String.valueOf(t.getId()), "\">", t
						.getName(), "</a></td><td FIXWIDTH=120 align=center></td><td FIXWIDTH=70 align=center>", dateFormat.format(new Date(t.getDate())), "</td></tr></table><img src=\"L2UI.Squaregray\" width=\"610\" height=\"1\">");
				}
			}
		}
		
		html.append("<br><table width=610 cellspace=0 cellpadding=0><tr><td width=50><button value=\"&$422;\" action=\"bypass _bbsmemo\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"></td><td width=510 align=center><table border=0><tr>");
		
		if (index == 1) {
			html.append("<td><button action=\"\" back=\"l2ui_ch3.prev1_down\" fore=\"l2ui_ch3.prev1\" width=16 height=16 ></td>");
		} else {
			StringUtil.append(html, "<td><button action=\"bypass _bbstopics;read;", String.valueOf(forum.getId()), ";", String.valueOf(index - 1), "\" back=\"l2ui_ch3.prev1_down\" fore=\"l2ui_ch3.prev1\" width=16 height=16 ></td>");
		}
		
		int nbp;
		nbp = forum.getTopicSize() / 8;
		if ((nbp * 8) != ClanTable.getInstance().getClanCount()) {
			nbp++;
		}
		for (int i = 1; i <= nbp; i++) {
			if (i == index) {
				StringUtil.append(html, "<td> ", String.valueOf(i), " </td>");
			} else {
				StringUtil.append(html, "<td><a action=\"bypass _bbstopics;read;", String.valueOf(forum.getId()), ";", String.valueOf(i), "\"> ", String.valueOf(i), " </a></td>");
			}
		}
		if (index == nbp) {
			html.append("<td><button action=\"\" back=\"l2ui_ch3.next1_down\" fore=\"l2ui_ch3.next1\" width=16 height=16 ></td>");
		} else {
			StringUtil.append(html, "<td><button action=\"bypass _bbstopics;read;", String.valueOf(forum.getId()), ";", String.valueOf(index + 1), "\" back=\"l2ui_ch3.next1_down\" fore=\"l2ui_ch3.next1\" width=16 height=16 ></td>");
		}
		
		StringUtil.append(html, "</tr></table> </td> <td align=right><button value = \"&$421;\" action=\"bypass _bbstopics;crea;", String.valueOf(forum.getId()), //
			"\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\" ></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr><tr> <td></td><td align=center><table border=0><tr><td></td><td><edit var = \"Search\" width=130 height=11></td><td><button value=\"&$420;\" action=\"Write 5 -2 0 Search _ _\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"> </td> </tr></table> </td></tr></table><br><br><br></center></body></html>");
		CommunityBoardHandler.separateAndSend(html.toString(), activeChar);
	}
	
	public static TopicBBSManager getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder {
		protected static final TopicBBSManager _instance = new TopicBBSManager();
	}
}