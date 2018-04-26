package com.andy.gomoku.game.task;

import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.Room;
import com.andy.gomoku.utils.CommonUtils;
import com.google.common.collect.Lists;

public class RobotTask extends TimerTask {

	private static Logger logger = LoggerFactory.getLogger(RobotTask.class);
	
	@Override
	public void run() {
		
		try {
			doRun();
		} catch (Exception e) {
			logger.error("",e);
		}

	}
	
	/**
	 * 检测占用空房间的机器人
	 */
	public void doRun(){
		
		List<GameUser> robs = Lists.newArrayList();
		for(GameUser robo : Global.getRobots()){
			Room room = robo.getRoom();
			if(room != null && room.getUsers().size() == 1){
				CommonUtils.outRoom(robo);
				robs.add(robo);
			}
		}
		
		Global.toReadyRob(robs);
	}

}
