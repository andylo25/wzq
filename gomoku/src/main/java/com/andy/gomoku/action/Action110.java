package com.andy.gomoku.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 退出房间
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_108)
public class Action110 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		CommonUtils.checkMov(user);
		
	}

	
	
	
}
