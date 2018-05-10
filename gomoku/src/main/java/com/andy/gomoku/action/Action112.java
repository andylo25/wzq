package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 主题购买
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_112)
public class Action112 implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		
		String theme = user.getUser().getTheme();
		String chess = MapUtils.getString(data,"chess");
		if(theme != null && theme.indexOf(chess+",") >= 0){
			return;
		}
		theme += chess+",";
		user.getUser().setTheme(theme);
		
		CommonUtils.saveDb(user.getUser());
		
		SendUtil.send112(user,data);
		
	}

	
	
	
}
