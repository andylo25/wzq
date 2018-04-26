package com.andy.gomoku.ai;

public class WineAI {

	static {
		System.loadLibrary("lib/WineAI");
	}
	
	private long winePoint; // native 指针
	private boolean isEnd;
	
	public WineAI() {
		init(15);
	}
	public WineAI(int size) {
		init(size);
	}
	
	public void init(int size){
		winePoint = newPoint(size);
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
	
	public static class Mov{
		public int x,y;
		public Mov(){ }
		public Mov(int x,int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private static native long newPoint(int size);
	private static native void deletePoint(long p);
	private static native boolean addChess(long p,int x,int y);
	private static native long getBestMove(long p);
	
}
