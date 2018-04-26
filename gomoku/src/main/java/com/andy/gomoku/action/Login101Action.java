package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.Where;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.GoConstant;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 游客登录
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_101)
public class Login101Action implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		String userName = MapUtils.getString(data, "userName");
		UsrUser user = null;
		UsrGameInfo gameInfo = null;
		if(StringUtils.isNotBlank(userName)){
			user = DaoUtils.getOne(UsrUser.class, Where.eq("userName", userName));
		}else {
			user = new UsrUser();
			user.setUserName(RandomStringUtils.randomAlphabetic(10));
			user.setNickName(CommonUtils.genNickName());
			DaoUtils.insert(user);
			
			gameInfo = new UsrGameInfo();
			gameInfo.setUid(user.getId());
			gameInfo.setTitleSort(0);
			DaoUtils.insert(gameInfo);
		}
		GameUser gameUser = new GameUser(user);
		gameInfo = DaoUtils.getOne(UsrGameInfo.class, Where.eq("uid", user.getId()));
		gameUser.setGameInfo(gameInfo);
		
		Global.addUser(user.getId(), myWebSocket);
		myWebSocket.setAttr(GoConstant.USER_SESSION_KEY, gameUser);
		
		SendUtil.send101(myWebSocket, gameUser);
		
	}

	
	
	
}
