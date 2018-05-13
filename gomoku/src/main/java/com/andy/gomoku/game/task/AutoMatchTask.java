package com.andy.gomoku.game.task;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.GameConf;
import com.andy.gomoku.utils.SendUtil;

public class AutoMatchTask extends TimerTask {

	private static Logger logger = LoggerFactory.getLogger(AutoMatchTask.class);

	public AtomicInteger counter = new AtomicInteger();

	@Override
	public void run() {
		try {
			doRun();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void doRun() {
		GameUser[] gameUsers = Global.pollMatch();
		if (gameUsers != null) {
			for (GameUser gu : gameUsers) {
				if (gu == null)
					return;
			}
			// 匹配成功
			if(logger.isInfoEnabled()){
				logger.info("{}《=》{}人人匹配成功",gameUsers[0].getId(),gameUsers[1].getId());
			}
			done(gameUsers);
			return ;
		}

		// 匹配超时加入机器人
		int timeout = 1000 * GameConf.getConfInt("match_timeout");
		GameUser otuser = Global.outTimeUser(timeout);
		if (otuser != null) {
			// 加入机器人
			GameUser rob = Global.getRobot(counter.incrementAndGet());
			if(logger.isInfoEnabled()){
				logger.info("{}《=》{}机器人匹配成功",otuser.getId(),rob.getId());
			}
			done(new GameUser[]{otuser,rob});

//			otuser = Global.outTimeUser(timeout);
		}
	}

	private void done(GameUser[] gameUsers) {
		// 第一个人建立房间gameUsers[0]
		Room room = Global.newRoom(gameUsers[0]);
		SendUtil.send102(gameUsers[0], room);

		// 第二个加入房间gameUsers[1]
		room.addUser(gameUsers[1]);
		SendUtil.send103(gameUsers[1], gameUsers[0]);
		SendUtil.send103(gameUsers[0], gameUsers[1]);
		
		// 准备
//		gameUsers[0].toggleReady(1);
//		room.ready(gameUsers[0]);
//		SendUtil.send105(room, gameUsers[0], null);
//		gameUsers[1].toggleReady(2);
//		GameUser first = room.ready(gameUsers[1]);
//		SendUtil.send105(room, gameUsers[1], first);
		
	}

}
