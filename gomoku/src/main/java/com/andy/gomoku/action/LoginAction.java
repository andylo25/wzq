package com.andy.gomoku.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 登录逻辑
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_100)
public class LoginAction implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		
	}

	
	
	
}
