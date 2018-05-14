package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 开始准备
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_105)
public class Action105 implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		GameUser gameUser = myWebSocket.getUser();
		Room room = gameUser.getRoom();
		if(room == null)return;
		Integer cid = MapUtils.getInteger(data, "cid");
		if(cid != null){
			if(!checkCid(gameUser,cid)){
				cid = null;
			}
		}
		
		ready(gameUser, room,cid);
		
		GameUser robot = room.getOther(gameUser);
		if(robot != null && robot.isRobo()){
			ready(robot, room,RandomUtils.nextInt(0, 4));
		}
	}

	private boolean checkCid(GameUser gameUser, Integer cid) {
		String theme = gameUser.getUser().getTheme();
		if(StringUtils.isNotBlank(theme) && theme.indexOf(cid+",") >= 0){
			return true;
		}
		return false;
	}

	private void ready(GameUser gameUser, Room room, Integer cid) {
		gameUser.toggleReady(cid);
		GameUser first = room.ready(gameUser);
		if(first != null){
			SendUtil.send105(room, gameUser, first);
		}else{
			SendUtil.send105(room, gameUser, null);
		}
	}

	
	
	
}
