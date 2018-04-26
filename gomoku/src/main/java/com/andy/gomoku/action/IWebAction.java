package com.andy.gomoku.action;

import java.util.Map;

import com.andy.gomoku.websocket.MyWebSocket;

public interface IWebAction {

	public void doAction(MyWebSocket myWebSocket, Map<String,Object> data);
	
}
