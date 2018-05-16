package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 选择棋子
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_115)
public class Action115 implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		if(user == null)return;
		
		Integer cid = MapUtils.getInteger(data, "cid");
		if(cid != null){
			if(!user.checkCid(cid)){
				cid = null;
			}
		}
		
		if(cid != null){
			user.getGameInfo().setCid(cid);
		}
		
		SendUtil.send115(user.getRoom(),user);
		
		
	}

	
	
	
}
