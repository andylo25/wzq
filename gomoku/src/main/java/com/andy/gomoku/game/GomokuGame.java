package com.andy.gomoku.game;

import java.io.Serializable;

import com.andy.gomoku.ai.WineAI;
import com.andy.gomoku.ai.WineAI.Mov;
import com.andy.gomoku.utils.GameUI;

public class GomokuGame implements Serializable{

	private static final long serialVersionUID = 1L;

	private int[][] chess;
	private int status;
	private int turnInd;
	private boolean haveRob;
	private int size;
	private WineAI wineAI;
	private GameUI ui;
	
	public GomokuGame(boolean haveRob,int firstInd) {
		this.turnInd = firstInd;
		this.setHaveRob(haveRob);
		size = 15;
		wineAI = new WineAI(size);
		chess = new int[size][size];
		ui = new GameUI();
	}

	public void end(){
		if(wineAI != null){
			wineAI.end();
		}
		status = -1;
	}
	
	public boolean isEnd(){
		return status == -1;
	}

	/**
	 * 落子，-1-失败，0-成功，1-结束
	 * @param turnIndx
	 * @param x
	 * @param y
	 * @return
	 */
	public int turnMove(int turnIndx,int x, int y) {
		if(x < 0 || x >= size || y < 0 ||y >= size)return -1;
		if(chess[x][y] > 0)return -1;
		if(turnInd != turnIndx)return -1;
		chess[x][y] = turnIndx+1;
		turnInd = 1-turnInd;
		ui.addChess(x, y);
		if(wineAI != null){
			if(wineAI.addChess(x, y)){
				end();
				return 1;
			}
		}
		return 0;
	}

	public boolean haveRob() {
		return haveRob;
	}

	public void setHaveRob(boolean haveRob) {
		this.haveRob = haveRob;
	}

	public int robotMove(Mov out) {
		if(wineAI != null){
			Mov mov = wineAI.getBestMove();
			out.x = mov.x;
			out.y = mov.y;
			return turnMove(turnInd, mov.x, mov.y);
		}
		return -1;
	}
	
}
