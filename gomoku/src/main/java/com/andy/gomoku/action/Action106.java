package com.andy.gomoku.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.andy.gomoku.ai.Move;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.GomokuGame;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;

/**
 * 落子
 * @author cuiwm
 *
 */
@Component(GmAction.ACTION_PREFIX+GmAction.ACTION_106)
public class Action106 implements IWebAction{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doAction(MySocketSession myWebSocket, Map<String, Object> data) {
		int x = MapUtils.getIntValue(data, "x");
		int y = MapUtils.getIntValue(data, "y");
		
		GameUser gameUser = myWebSocket.getUser();
		GomokuGame game = gameUser.getGame();
		Room room = gameUser.getRoom();
		if(room == null || game == null)return;
		// 求和中，悔棋中不能下子
		if(!gameUser.isOutPeace()) return;
		if(!CommonUtils.checkMov(gameUser)) return;
		
		Move mov = new Move(y,x);
		int resu = -2;
		GameUser movUser = gameUser;
		if(x >= 0 && y >= 0){
			resu = game.turnMove(room.getIndx(gameUser),x,y);
		}else{
			// 机器人
			if(game.haveRob()){
				movUser = room.getOther(gameUser);
				int[] resu1 = game.robotMove(-1);
				resu = resu1[0];
				if(resu > -2){
					mov = new Move(resu1[1],resu1[2]);
				}
			}else{
				logger.error("落子异常："+data);
			}
		}
		if(resu > -2){
			movUser.move(room.getOther(movUser).getLastMov());
			SendUtil.send106(room, movUser.getId(), mov.col, mov.row);
			
			if(resu >= 0){
				CommonUtils.gameOver(room,game,resu==0?null:movUser);
			}
		}else{
			logger.error("落子失败："+data);
		}
		
		
	}

	
	
	
}
