package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.andy.gomoku.ai.WineAI.Mov;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.GomokuGame;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MyWebSocket;

/**
 * 落子
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_106)
public class Action106 implements IWebAction{

	@Override
	public void doAction(MyWebSocket myWebSocket, Map<String, Object> data) {
		int x = MapUtils.getIntValue(data, "x");
		int y = MapUtils.getIntValue(data, "y");
		
		GameUser gameUser = myWebSocket.getUser();
		GomokuGame game = gameUser.getGame();
		Room room = gameUser.getRoom();
		if(game == null || game == null)return;
		
		Mov mov = new Mov(x,y);
		int resu = -1;
		GameUser movUser = gameUser;
		if(x >= 0 && y >= 0){
			resu = game.turnMove(room.getIndx(gameUser),x,y);
		}else{
			// 机器人
			if(game.haveRob()){
				movUser = room.getOther(gameUser);
				mov = new Mov();
				resu = game.robotMove(mov);
			}
		}
		
		if(resu >= 0){
			SendUtil.send106(room, movUser.getId(), mov.x, mov.y);
			
			if(resu == 1){
				CommonUtils.gameOver(room,game,gameUser);
			}
		}
		
		
	}

	
	
	
}
