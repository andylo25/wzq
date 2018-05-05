package com.andy.gomoku.utils;

import org.apache.commons.lang3.RandomUtils;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.BaseEntity;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrGameLog;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.GomokuGame;
import com.andy.gomoku.game.Room;

public class CommonUtils {

	public static void outRoom(GameUser user) {
		if(user == null)return;
		Room room = user.getRoom();
		GomokuGame game = user.getGame();
		if(game != null){
			game.end();
			GameUser winner = room.getOther(user);
			gameOver(room, game,winner);
		}
		if(room != null){
			room.logout(user);
			SendUtil.send108(room, user);
		}
		
		
	}

	public static String genNickName() {
		String firstName = GameConf.firstNames[RandomUtils.nextInt(0, GameConf.firstNames.length)];
		String secondName = GameConf.secondNames[RandomUtils.nextInt(0, GameConf.secondNames.length)];
		return firstName+secondName;
	}

	public static void gameOver(Room room, GomokuGame game,GameUser winner) {
		if(winner != null){
			GameUser other = room.getOther(winner);
			UsrGameInfo wingi = winner.getGameInfo();
			UsrGameInfo otgu = other.getGameInfo();
			UsrGameLog winLog = new UsrGameLog(wingi);
			UsrGameLog otLog = new UsrGameLog(otgu);
			// 结算
			wingi.setWinCount(wingi.getWinCount()+1);
			otgu.setWinCount(otgu.getWinCount()-1);
			winLog.setResult(1);
			otLog.setResult(-1);
			if(wingi.getTitleSort() == otgu.getTitleSort()){
				wingi.addScore(10);
				winLog.setAddCoin(10);
				otgu.addScore(-10);
				otLog.setAddCoin(-10);
			}else if(wingi.getTitleSort() < otgu.getTitleSort()){
				wingi.addScore(15);
				winLog.setAddCoin(15);
				otgu.addScore(-10);
				otLog.setAddCoin(-10);
			}else{
				wingi.addScore(5);
				winLog.setAddCoin(5);
				otgu.addScore(-10);
				otLog.setAddCoin(-10);
			}
			
			saveDb(wingi,otgu,winLog,otLog);
		}
		
		for(GameUser us:room.getUsers()){
			us.gameOver();
		}
		
		SendUtil.send107(room, winner);
		
		
	}
	
	public static void saveDb(BaseEntity... entity){
		// todo 可改批量插入
		for(BaseEntity en:entity){
			if(en.getId() != null){
				DaoUtils.update(en);
			}else{
				DaoUtils.insert(en);
			}
		}
	}

	public static boolean checkMov(GameUser user) {
		Room room = user.getRoom();
		Long lastMov = room.getOther(user).getLastMov();
		Long allMov = user.getAllMovTime();
		Long curTime = System.currentTimeMillis();
		if(curTime - lastMov >= GoConstant.STEP_TIMEOUT
				|| allMov >= GoConstant.QUANPAN_TIMEOUT) {
			// 超时,游戏结束
			CommonUtils.gameOver(room,user.getGame(),room.getOther(user));
			return false;
		}else {
			return true;
		}
	}

}
