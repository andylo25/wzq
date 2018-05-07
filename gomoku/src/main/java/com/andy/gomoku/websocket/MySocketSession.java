package com.andy.gomoku.websocket;

import com.andy.gomoku.game.GameUser;

public interface MySocketSession {

	public void setAttr(String name,Object value);
	
	public Object getAttr(String name);
	
	public Object removeAttr(String name);
	
	public GameUser getUser();
	
	public void sendMessage(String message);
	
	public void invalidate();
	
}
