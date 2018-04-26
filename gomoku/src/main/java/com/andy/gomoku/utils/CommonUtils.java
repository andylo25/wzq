package com.andy.gomoku.utils;

import org.apache.commons.lang3.RandomUtils;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.BaseEntity;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.GomokuGame;
import com.andy.gomoku.game.Room;

public class CommonUtils {

	public static void outRoom(GameUser user) {
		Room room = user.getRoom();
		GomokuGame game = user.getGame();
		if(game != null){
			game.end();
			GameUser winner = room.getOther(user);
			gameOver(room, game,winner);
		}
		if(room != null){
			room.logout(user);
		}
		
		SendUtil.send108(room, user);
		
	}

	public static String genNickName() {
		String firstName = GameConf.firstNames[RandomUtils.nextInt(0, GameConf.firstNames.length)];
		String secondName = GameConf.secondNames[RandomUtils.nextInt(0, GameConf.secondNames.length)];
		return firstName+secondName;
	}

	public static void gameOver(Room room, GomokuGame game,GameUser winner) {
		GameUser other = room.getOther(winner);
		UsrGameInfo wingi = winner.getGameInfo();
		UsrGameInfo otgu = other.getGameInfo();
		// 结算
		wingi.setWinCount(wingi.getWinCount()+1);
		otgu.setWinCount(wingi.getWinCount()-1);
		if(wingi.getTitleSort() == otgu.getTitleSort()){
			wingi.addScore(10);
			otgu.addScore(-10);
		}else if(wingi.getTitleSort() < otgu.getTitleSort()){
			wingi.addScore(15);
			otgu.addScore(-10);
		}else{
			wingi.addScore(5);
			otgu.addScore(-10);
		}
		saveDb(wingi,otgu);
		for(GameUser us:room.getUsers()){
			us.gameOver();
		}
		
		SendUtil.send107(room, winner);
		
	}
	
	public static void saveDb(BaseEntity... entity){
		// todo 可改批量插入
		for(BaseEntity en:entity){
			DaoUtils.update(en);
		}
	}

}
