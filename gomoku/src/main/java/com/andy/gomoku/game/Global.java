package com.andy.gomoku.game;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.controller.BaseController;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GoConstant;
import com.andy.gomoku.websocket.MySocketSession;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class Global implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(Global.class);
	
	/**
	 * 在线人数
	 */
	public static AtomicInteger onlineNumber = new AtomicInteger();

	private static Map<Long,MySocketSession> sessionMap = Maps.newConcurrentMap();
	private static List<MySocketSession> sessions = Lists.newCopyOnWriteArrayList();
	
	public static void addSession(MySocketSession session){
		onlineNumber.incrementAndGet();
		sessions.add(session);
		
		if(logger.isInfoEnabled()){
			logger.info("有新连接加入！ 当前在线人数：{}，s={}", onlineNumber,sessions.size());
		}
	}
	
	public static void removeSession(MySocketSession session){
		if(sessions.remove(session)){
			onlineNumber.decrementAndGet();
		}
		GameUser user = session.getUser();
		if(user != null){
			sessionMap.remove(user.getId());
			session.removeAttr(GoConstant.USER_SESSION_KEY);
			if(logger.isInfoEnabled()){
				logger.info("断开连接用户：{}-{}", user.getId(),session);
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("有连接关闭！ 当前在线人数：{},s={},u={}", onlineNumber,sessions.size(),sessionMap.size());
		}
	}
	
	public static void addUser(Long uid,MySocketSession session){
		if(logger.isInfoEnabled()){
			logger.info("登录用户：{}-{}", uid,session);
			logger.info("当前在线人数{},s={},u={}", onlineNumber,sessions.size(),sessionMap.size());
		}
		sessionMap.put(uid, session);
	}
	
	public static MySocketSession getSession(long id){
		return sessionMap.get(id);
	}
	
	public static Collection<MySocketSession> getUserSessions(){
		return sessionMap.values();
	}

	private static Map<Integer,Room> rooms = Maps.newConcurrentMap();
	
	public static Room newRoom(GameUser user) {
		Room room = new Room();
		room.addUser(user);
		rooms.put(room.getId(),room);
		return room;
	}

	public static Room getRoom(Integer roomId) {
		return rooms.get(roomId);
	}
	public static Collection<Room> getRooms() {
		return rooms.values();
	}
	
	/**
	 * 自动匹配队列
	 */
	private static Queue<GameUser> autoMatchs = Queues.newConcurrentLinkedQueue();
	private static List<GameUser> robots = Lists.newArrayList();
	private static List<GameUser> readyRobots = Lists.newArrayList();
	
	public static void addMatch(GameUser user){
		user.match(true);
		autoMatchs.add(user);
	}
	public static GameUser[] pollMatch(){
		if(autoMatchs.size() >= 2){
			GameUser[] result = new GameUser[2];
			result[0] = autoMatchs.poll();
			result[1] = autoMatchs.poll();
			return result;
		}
		return null;
	}
	public static GameUser outTimeUser(int timeOut){
		GameUser user = autoMatchs.peek();
		if(user != null && user.isMatchOutTime(timeOut)){
			removeMatch(user);
			return user;
		}
		return null;
	}
	
	public static void removeMatch(GameUser user){
		autoMatchs.remove(user);
		user.match(false);
	}
	
	public static GameUser getRobot(int id){
		GameUser rob = null;
		if(!readyRobots.isEmpty()){
			rob = readyRobots.get(0);
		}else{
			UsrUser robot = new UsrUser(GoConstant.ROBOT_ID_START + id);
			robot.setNickName(CommonUtils.genNickName());
			rob = new GameUser(robot);
			robots.add(rob);
		}
		return rob;
	}
	public static List<GameUser> getRobots(){
		return robots;
	}
	public static void toReadyRob(List<GameUser> robs){
		robots.removeAll(robs);
		readyRobots.addAll(robs);
	}

	private static List<Map<String, Object>> ranks = Lists.newArrayList();
	public static List<Map<String, Object>> getRanks() {
		return ranks;
	}

	public static void refreshRanks() {
		List<Map<String, Object>> ranksT = DaoUtils.getListSql( "select uid,win_count as winCount,title from usr_game_info order by win_count desc limit 0,10");
		BaseController.idToName(ranksT, UsrUser.table(), "uid#id:icon,nick_name as nickName");
		ranks = ranksT;
	}

	
}
