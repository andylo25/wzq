package com.andy.gomoku.utils;

import java.util.List;
import java.util.Map;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.websocket.MySocketSession;
import com.google.common.collect.Maps;

public class SendUtil {

	public static void send100(MySocketSession session,GameUser gameUser) {
		if(gameUser == null){
			Map<String,Object> map = Maps.newHashMap();
			map.put("respCode", 1);
			doSend(session,GmAction.ACTION_100, map);
		}else{
			Map<String, Object> map = gameUser.getUserInfo();
			map.put("respCode", GoConstant.SUCC_CODE);
			
			doSend(session,GmAction.ACTION_100, gameUser.getUserInfo());
		}
	}
	public static void send101(MySocketSession session,GameUser gameUser) {
		if(gameUser == null)return;
		doSend(session,GmAction.ACTION_101, gameUser.getUserInfo());
	}
	
	public static void send102(GameUser session,Room room) {
		if(room == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("respCode", GoConstant.SUCC_CODE);
		map.put("roomNum", room.getId());
		doSend(session,GmAction.ACTION_102, map);
	}
	
	public static void send103(GameUser session,GameUser gameUser) {
		Map<String,Object> map = Maps.newHashMap();
		if(gameUser != null){
			map = gameUser.getUserInfo();
			map.put("roomNum", gameUser.getRoom().getId());
		}else{
			map.put("roomNum", 0);
		}
		doSend(session,GmAction.ACTION_103, map);
	}
	
	public static void send104(MySocketSession session,GameUser gameUser) {
		if(gameUser == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("respCode", GoConstant.SUCC_CODE);
		doSend(session,GmAction.ACTION_104, map);
	}
	
	public static void send105(Room room,GameUser readyUser,GameUser first) {
		if(room == null || readyUser == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", readyUser.getId());
		map.put("cid", readyUser.getCid());
		if(first != null){
			map.put("firstId", first.getId());
		}
		for(GameUser gameUser:room.getUsers()){
			doSend(gameUser,GmAction.ACTION_105, map);
		}
	}
	
	public static void send106(Room room,Long uid,int x,int y) {
		if(room == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", uid);
		map.put("x", x);
		map.put("y", y);
		for(GameUser gameUser:room.getUsers()){
			doSend(gameUser,GmAction.ACTION_106, map);
		}
	}
	
	public static void send107(Room room,GameUser winner) {
		if(room == null)return;
		Map<String,Object> map = Maps.newHashMap();
		if(winner != null){
			map.put("userId", winner.getId());
		} else {
			map.put("userId", -1);
		}
		for(GameUser gameUser:room.getUsers()){
			doSend(gameUser,GmAction.ACTION_107, map);
		}
	}
	
	public static void send108(Room room,GameUser user) {
		if(room == null || user == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", user.getId());
		for(GameUser gameUser:room.getUsers()){
			doSend(gameUser,GmAction.ACTION_108, map);
		}
		doSend(user,GmAction.ACTION_108, map);
	}
	
	public static void sendtoVar(Object sessionOrId,int action,Object... data) {
		if(data == null)return;
		Map<String,Object> map = Maps.newHashMap();
		for(int i=0;i<data.length;i+=2)
		map.put((String) data[i], data[i+1]);
		doSend(sessionOrId,action, map);
	}

	private static void doSend(Object sessionOrId,int action,Object data) {
		if(data == null)return;
		if(sessionOrId != null){
			MySocketSession sess = null;
			Map<String,Object> map = Maps.newHashMap();
			map.put("action", action);
			map.put("data", data);
			if(sessionOrId instanceof MySocketSession){
				sess = (MySocketSession) sessionOrId;
			}else if(sessionOrId instanceof GameUser){
				sess = Global.getSession(((GameUser) sessionOrId).getId());
			}else{
				sess = Global.getSession((Long) sessionOrId);
			}
			if(sess != null){
				sess.sendMessage(JsonUtils.object2JsonString(map));
			}
		}
	}
	
	public static void broadcast(int action,String data) {
		if(data == null)return;
		for(MySocketSession session:Global.getUserSessions()){
			doSend(session,action,data);
		}
	}

	public static void send109(GameUser user, List<Map<String, Object>> ranks) {
		if(user == null || ranks == null)return;
		doSend(user,GmAction.ACTION_109, ranks);
	}
	
	public static void send111(Room room, Map<String, Object> msg) {
		for(GameUser gameUser:room.getUsers()){
			doSend(gameUser,GmAction.ACTION_111, msg);
		}
	}
	
	public static void send112(GameUser user, Map<String, Object> chess) {
		doSend(user,GmAction.ACTION_112, chess);
	}
	
	public static void send113(Room room, Map<String, Object> type) {
		for(GameUser gameUser:room.getUsers()){
			doSend(gameUser,GmAction.ACTION_113, type);
		}
	}
	
	public static void send114(GameUser user) {
		Room room = user.getRoom();
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", user.getId());
		map.put("coin", user.getGameInfo().getCoin());
		map.put("title", user.getGameInfo().getTitle());
		if(room != null){
			for(GameUser gameUser:room.getUsers()){
				doSend(gameUser,GmAction.ACTION_114, map);
			}
		}else{
			doSend(user,GmAction.ACTION_114, map);
		}
	}
	

}
