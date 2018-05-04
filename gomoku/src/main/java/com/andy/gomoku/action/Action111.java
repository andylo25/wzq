package com.andy.gomoku.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 聊天
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_111)
public class Action111 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		Room room = user.getRoom();
		if(room != null){
			SendUtil.send111(room,data);
		}
		
	}

	
	
	
}
