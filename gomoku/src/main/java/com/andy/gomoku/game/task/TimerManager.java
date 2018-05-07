package com.andy.gomoku.game.task;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerManager {
	
	private static Logger logger = LoggerFactory.getLogger(TimerManager.class);
	
	private static Timer timer;
	
	public static void init() {
		// 自动匹配
		timer = new Timer("TimerManager");
		TimerTask task = new AutoMatchTask();
		timer.schedule(task, 5000, 3*1000);
		
		// 机器人占用房间检测
		TimerTask checkTask = new RobotTask();
		timer.schedule(checkTask , 60*1000,60*1000);
		
		// 刷新排行榜
		timer.schedule(new RefreshRankTask() , 60*1000,60*1000);
	}
	
}
