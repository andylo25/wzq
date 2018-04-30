package com.andy.gomoku.utils;

import java.util.List;
import java.util.Map;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.websocket.MyWebSocket;
import com.google.common.collect.Maps;

public class SendUtil {

	public static void send101(MyWebSocket session,GameUser gameUser) {
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
		if(gameUser == null)return;
		Map<String,Object> map = gameUser.getUserInfo();
		map.put("roomNum", gameUser.getRoom().getId());
		doSend(session,GmAction.ACTION_103, map);
	}
	
	public static void send104(MyWebSocket session,GameUser gameUser) {
		if(gameUser == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("respCode", GoConstant.SUCC_CODE);
		doSend(session,GmAction.ACTION_104, map);
	}
	
	public static void send105(Room room,GameUser readyUser,GameUser first) {
		if(room == null || readyUser == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", readyUser.getId());
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
		if(room == null || winner == null)return;
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", winner.getId());
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
			MyWebSocket sess = null;
			Map<String,Object> map = Maps.newHashMap();
			map.put("action", action);
			map.put("data", data);
			if(sessionOrId instanceof MyWebSocket){
				sess = (MyWebSocket) sessionOrId;
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
		for(MyWebSocket session:Global.getUserSessions()){
			doSend(session,action,data);
		}
	}

	public static void send109(GameUser user, List<Map<String, Object>> ranks) {
		if(user == null || ranks == null)return;
		doSend(user,GmAction.ACTION_109, ranks);
	}
	

}
