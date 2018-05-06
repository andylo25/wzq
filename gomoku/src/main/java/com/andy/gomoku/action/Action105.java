package com.andy.gomoku.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 开始准备
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_105)
public class Action105 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		GameUser gameUser = myWebSocket.getUser();
		Room room = gameUser.getRoom();
		if(room == null)return;
		
		ready(gameUser, room);
		
		GameUser robot = room.getOther(gameUser);
		if(robot != null && robot.isRobo()){
			ready(robot, room);
		}
	}

	private void ready(GameUser gameUser, Room room) {
		GameUser first = room.ready(gameUser);
		if(first != null){
			SendUtil.send105(room, gameUser, first);
		}else{
			SendUtil.send105(room, gameUser, null);
		}
	}

	
	
	
}
