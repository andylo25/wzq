package com.andy.gomoku.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 好友约战
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_102)
public class Action102 implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		GameUser user = myWebSocket.getUser();
		if(user.getRoom() != null) return;
//		if(user.getGameInfo().getCoin() <= 0)return ;
		
		Room room = Global.newRoom(user);
		SendUtil.send102(user, room);
		
		
	}

	
	
	
}
