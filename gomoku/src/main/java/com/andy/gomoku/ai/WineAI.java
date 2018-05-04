package com.andy.gomoku.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WineAI {

	static {
		try {
			System.loadLibrary("WineAI");
			System.out.println("WineAI加载成功..");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private long winePoint; // native 指针
	private boolean isEnd;
	
	public WineAI() {
		this(15,0,0);
	}
	public WineAI(int size) {
		try {
			winePoint = newPoint(size,0,0,0);
		} catch (Exception e) {
			logger.error("创建AI异常",e);
		}
	}
	public WineAI(int size,int toTurn,int toMatch) {
		winePoint = newPoint(size,0,toTurn,toMatch);
	}
	
	public boolean addChess(int x,int y){
		boolean isWin = addChess(winePoint, x, y);
		if(isWin){
			end();
		}
		return isWin;
	}
	
	public Mov getBestMove(){
		long pi = getBestMove(winePoint);
		return new Mov((int) (pi/10000), (int)(pi%10000));
	}
	
	public void end(){
		if(isEnd) return;
		isEnd = true;
		deletePoint(winePoint);
	}
	
	public void takeBack(){
		takeBack(winePoint);
	}
	
	public static class Mov{
		public int x,y;
		public Mov(){ }
		public Mov(int x,int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private static native long newPoint(int size,int depth,int turn,int match);
	private static native void deletePoint(long p);
	private static native boolean addChess(long p,int x,int y);
	private static native long getBestMove(long p);
	private static native void takeBack(long p);
	
}
