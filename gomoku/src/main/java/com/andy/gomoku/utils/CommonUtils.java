package com.andy.gomoku.utils;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.DbBatch;
import com.andy.gomoku.entity.BaseEntity;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrGameLog;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.GomokuGame;
import com.andy.gomoku.game.Room;

public class CommonUtils {

	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	public static void outRoom(GameUser user) {
		if(user == null)return;
		Room room = user.getRoom();
		GomokuGame game = user.getGame();
		if(game != null){
			GameUser winner = room.getOther(user);
			gameOver(room, game,winner);
		}
		if(room != null){
			room.logout(user);
			SendUtil.send108(room, user);
		}
		
		saveDb(user.getGameInfo());
		
	}

	public static String genNickName() {
		String firstName = GameConf.firstNames[RandomUtils.nextInt(0, GameConf.firstNames.length)];
		String secondName = GameConf.secondNames[RandomUtils.nextInt(0, GameConf.secondNames.length)];
		return firstName+secondName;
	}

	public static void gameOver(Room room, GomokuGame game,GameUser winner) {
		if(game != null){
			game.end();
		}else{
			return;
		}
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
//			if(wingi.getTitleSort() == otgu.getTitleSort()){
//				winLog.setAddCoin(wingi.addCoin(10));
//				otLog.setAddCoin(otgu.addCoin(-10));
//			}else if(wingi.getTitleSort() < otgu.getTitleSort()){
//				winLog.setAddCoin(wingi.addCoin(15));
//				otLog.setAddCoin(otgu.addCoin(-10));
//			}else{
//				winLog.setAddCoin(wingi.addCoin(5));
//				otLog.setAddCoin(otgu.addCoin(-10));
//			}
			int wc = GameConf.getConfInt("win_coin");
			int lc = GameConf.getConfInt("lost_coin");
			winLog.setAddCoin(wingi.addCoin(wc));
			otLog.setAddCoin(otgu.addCoin(lc));
			
			saveDb(wingi,otgu,winLog,otLog);
		}
		
		for(GameUser us:room.getUsers()){
			us.gameOver();
		}
		if(room != null){
			room.gameOver();
		}
		
		SendUtil.send107(room, winner);
		
		
	}
	
	public static void saveDb(BaseEntity... entity){
		try {
			for(BaseEntity en:entity){
				if(en.getId() != null){
					if(en instanceof UsrGameInfo){
						DbBatch.upUsrUser((UsrGameInfo) en);
					}else{
						DaoUtils.update(en);
					}
				}else{
					if(en instanceof UsrGameLog){
						DbBatch.svGameLog((UsrGameLog) en);
					}else{
						DaoUtils.insert(en);
					}
				}
			}
		} catch (Exception e) {
			logger.error("持久化异常",e);
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
