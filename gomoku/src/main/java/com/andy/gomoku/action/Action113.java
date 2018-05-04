package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 超时检测
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_113)
public class Action113 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		int type = MapUtils.getIntValue(data, "type");
		Room room = user.getRoom();
		if(room == null)return;
		GameUser other = room.getOther(user);
		if(other == null)return;
		if(type == 1){ // 求和
			user.peace();
		}else if(type == 2){ // 同意求和
			if(other.isOutPeace()) return;
			// 和棋
			CommonUtils.gameOver(room, user.getGame(), null);
			return;
		}else if(type == 3){ // 请求悔棋
			user.peace();
		}else if(type == 4){ // 同意悔棋
			if(other.isOutPeace()) return;
			// 悔棋
			user.getGame().backMove();
			user.move(room.getOther(user).getLastMov());
		}else if(type == 5){ // 投降
			CommonUtils.gameOver(room, user.getGame(), other);
			return;
		}
		
		SendUtil.send113(user,data);
		
		// 机器人处理
		
	}

	
	
	
}
