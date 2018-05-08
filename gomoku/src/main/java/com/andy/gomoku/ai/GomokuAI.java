package com.andy.gomoku.ai;

public interface GomokuAI {

	/**
	 * 落子
	 * @param x 从0开始
	 * @param y 从0开始
	 * @return -1-未结束，0-和棋，>0-赢方
	 */
	public int addChess(Move move);
	
	public Move getBestMove();
	
	/**
	 * 结束
	 * @return 返回赢方,-1-已经结束过了
	 */
	public int end();
	
	/**
	 * 悔棋
	 */
	public void takeBack();
	
}
