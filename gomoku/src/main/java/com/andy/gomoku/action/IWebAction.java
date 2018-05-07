package com.andy.gomoku.action;

import java.util.Map;

import com.andy.gomoku.websocket.MySocketSession;

public interface IWebAction {

	public void doAction(MySocketSession myWebSocket, Map<String,Object> data);
	
}
