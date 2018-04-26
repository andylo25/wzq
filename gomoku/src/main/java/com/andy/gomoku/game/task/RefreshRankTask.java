package com.andy.gomoku.game.task;

import java.util.TimerTask;

import com.andy.gomoku.game.Global;

public class RefreshRankTask extends TimerTask {

	@Override
	public void run() {

		Global.refreshRanks();
		
	}

}
