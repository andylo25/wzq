package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GameConf;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.GoConstant;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 游客登录
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_101)
public class Login101Action implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		UsrUser user = new UsrUser();
		user.setUserName(RandomStringUtils.randomAlphabetic(10));
		user.setNickName(CommonUtils.genNickName());
		user.setIcon(RandomUtils.nextInt(0, 10));
		DaoUtils.insert(user);
		
		UsrGameInfo gameInfo = new UsrGameInfo();
		gameInfo.setUid(user.getId());
		gameInfo.addCoin(GameConf.getConfInt("init_coin"));
		DaoUtils.insert(gameInfo);
		
		GameUser gameUser = new GameUser(user);
		gameUser.setGameInfo(gameInfo);
		
		Global.addUser(user.getId(), myWebSocket);
		myWebSocket.setAttr(GoConstant.USER_SESSION_KEY, gameUser);
		
		SendUtil.send101(myWebSocket, gameUser);
		
	}

	
	
	
}
