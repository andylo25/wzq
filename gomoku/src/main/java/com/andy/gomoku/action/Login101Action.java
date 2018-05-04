package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
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
		UsrUser user = new UsrUser();
		user.setUserName(RandomStringUtils.randomAlphabetic(10));
		user.setNickName(CommonUtils.genNickName());
		DaoUtils.insert(user);
		
		UsrGameInfo gameInfo = new UsrGameInfo();
		gameInfo.setUid(user.getId());
		gameInfo.setTitleSort(0);
		DaoUtils.insert(gameInfo);
		
		GameUser gameUser = new GameUser(user);
		gameUser.setGameInfo(gameInfo);
		
		Global.addUser(user.getId(), myWebSocket);
		myWebSocket.setAttr(GoConstant.USER_SESSION_KEY, gameUser);
		
		SendUtil.send101(myWebSocket, gameUser);
		
	}

	
	
	
}
