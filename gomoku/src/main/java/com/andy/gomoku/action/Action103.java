package com.andy.gomoku.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 加入房间
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_103)
public class Action103 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		GameUser gameUser = myWebSocket.getUser();
		if(gameUser.getRoom() != null)return;
		
		Integer roomId = MapUtils.getInteger(data, "roomNum");
		Room room = Global.getRoom(roomId);
		if(room == null){
			SendUtil.send103(gameUser,null);
			return;
		}
		List<GameUser> users = room.getUsers();
		if(users.size() > 1) return;
		GameUser other = null;
		if(!users.isEmpty()){
			other = users.get(0);
		}
		room.addUser(gameUser);
		
		SendUtil.send102(gameUser, room);
		SendUtil.send103(gameUser,other);
		SendUtil.send103(other,gameUser);
	}

	
	
	
}
