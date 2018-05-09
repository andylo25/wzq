package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.GoConstant;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 登录逻辑
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_100)
public class LoginAction implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		String userName = MapUtils.getString(data, "userName");
		UsrUser user = null;
		UsrGameInfo gameInfo = null;
		if(StringUtils.isBlank(userName))return;
		
		user = DaoUtils.getOne(UsrUser.class, Where.eq("user_Name", userName));
		if(user == null){
			SendUtil.send100(myWebSocket, null);
			return;
		}else{
			GameUser gameUser = new GameUser(user);
			gameInfo = DaoUtils.getOne(UsrGameInfo.class, Where.eq("uid", user.getId()));
			gameInfo.addCoin(0); // 刷新称号
			gameUser.setGameInfo(gameInfo);
			
			Global.addUser(user.getId(), myWebSocket);
			myWebSocket.setAttr(GoConstant.USER_SESSION_KEY, gameUser);
			
			SendUtil.send100(myWebSocket, gameUser);
		}
	}

	
	
	
}
