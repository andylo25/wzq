package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GameConf;
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
		
		int bcc = GameConf.getConfInt("buy_chess_coin");
		if(user.getGameInfo().getCoin() < bcc) return;
		
		String theme = user.getUser().getTheme();
		String chess = MapUtils.getString(data,"chess");
		if(theme != null && theme.indexOf(chess+",") >= 0){
			return;
		}else{
			theme = "";
		}
		theme += chess+",";
		user.getGameInfo().addCoin(-bcc);
		user.getUser().setTheme(theme);
		
		CommonUtils.saveDb(user.getUser());
		CommonUtils.saveDb(user.getGameInfo());
		
		SendUtil.send112(user,data);
		
	}

	
	
	
}
