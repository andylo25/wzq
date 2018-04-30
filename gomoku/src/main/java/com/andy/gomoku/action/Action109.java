package com.andy.gomoku.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 排行榜
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_109)
public class Action109 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		
		SendUtil.send109(user,Global.getRanks());
		
	}

	
	
	
}
