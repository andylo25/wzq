package com.andy.gomoku.game;

import java.io.Serializable;

import com.andy.gomoku.ai.GomokuAI;
import com.andy.gomoku.ai.Move;
import com.andy.gomoku.ai.NegamaxAI;

public class GomokuGame implements Serializable{

	private static final long serialVersionUID = 1L;

	private int status;
	private boolean haveRob;
	private GomokuAI gomokuAI;
	private GameState gameState;
	
	public GomokuGame(boolean haveRob,int firstInd) {
		gameState = new GameState(15,firstInd);
		this.setHaveRob(haveRob);
		if(haveRob){
			gomokuAI = new NegamaxAI(gameState.getSize());
		}
	}

	public void end(){
		if(gomokuAI != null){
			gomokuAI.end();
			gomokuAI = null;
		}
		gameState = null;
		status = -1;
	}
	
	public boolean isEnd(){
		return status == -1;
	}

	/**
	 * 落子，-2-失败，-1-未结束，0-和棋，>0-赢方
	 * @param turnIndx 校验下棋方，-1不校验
	 * @param x
	 * @param y
	 * @return
	 */
	public int turnMove(int ind,int x, int y) {
		if(isEnd())return -2;
		Move move = new Move(y,x);
		if(!gameState.makeMove(ind,move)) return -2;
		if(gomokuAI != null){
			gomokuAI.addChess(move);
		}
		int resu = gameState.checkWin();
		if(resu >= 0){
			end();
		}
		return resu;
		
	}
	
	public boolean backMove(){
		if(isEnd())return false;
		if(gameState.undo() == null) return false;
		if(gomokuAI != null){
			gomokuAI.takeBack();
		}
		return true;
	}

	public boolean haveRob() {
		return haveRob;
	}

	public void setHaveRob(boolean haveRob) {
		this.haveRob = haveRob;
	}

	/**
	 * 落子，-2-执行失败，-1-未结束，0-和棋，>0-赢方
	 * @param turnIndx
	 * @return
	 */
	public int[] robotMove(int ind) {
		if(!isEnd() && gomokuAI != null){
			Move move = gomokuAI.getBestMove();
			int resu = turnMove(ind,move.col, move.row);
			return new int[]{resu,move.row,move.col};
		}
		return new int[]{-2};
	}
	
}
