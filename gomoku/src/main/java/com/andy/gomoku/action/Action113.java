package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.GomokuGame;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 超时检测
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_113)
public class Action113 implements IWebAction{

	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		
		GameUser user = myWebSocket.getUser();
		int type = MapUtils.getIntValue(data, "type");
		Room room = user.getRoom();
		if(room == null)return;
		GameUser other = room.getOther(user);
		if(other == null)return;
		GomokuGame game = user.getGame();
		if(type == 1){ // 求和
			user.peace();
		}else if(type == 2){ // 同意求和
			if(other.isOutPeace()) return;
			// 和棋
			CommonUtils.gameOver(room, game, null);
			return;
		}else if(type == 3){ // 请求悔棋
			user.peace();
		}else if(type == 4){ // 同意悔棋
			if(other.isOutPeace()) return;
			other.rejuct(); // 恢复状态
			// 执行悔棋
			if(user.getGame().backMove()){
				user.move(other.getLastMov());
			}
		}else if(type == 5){ // 投降
			data.put("userId", user.getId());
			SendUtil.send113(room,data);
			CommonUtils.gameOver(room, game, other);
			return;
		}else if(type == 6){ // 取消
			other.rejuct();
		}
		
		data.put("userId", user.getId());
		SendUtil.send113(room,data);
		
		// 机器人处理,直接拒绝
		if(other.isRobo() && (type == 1 || type == 3)){ // 求和,请求悔棋
			user.rejuct();
			data.put("type", 6);
			data.put("userId", other.getId());
			SendUtil.send113(room,data);
		}
		
		
	}

	
	
	
}
