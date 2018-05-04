package com.andy.gomoku.utils;

/**
 * 控制台的简单界面
 * 
 * @author cuiwm
 *
 */
public class GameUI {

	int[][] board = new int[15][15];
	int chessType = 1;
	char chessCount = 0;

	private void drawBoard() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				// 行首输出行号
				if (j == 0) {
					if (i + 1 < 10) {
						System.out.print(" ");
					}
					System.out.print(i + 1);
				}
				// 输出棋盘内容
				switch (board[i][j]) {
				case 0:
					if (i == 0) {
						if (j == 0) {
							System.out.print("┌ ");
						} else if (j == 14) {
							System.out.print("┐");
						} else {
							System.out.print("┬ ");
						}
					} else if (i == 14) {
						if (j == 0) {
							System.out.print("└ ");
						} else if (j == 14) {
							System.out.print("┘");
						} else {
							System.out.print("┴ ");
						}
					} else {
						if (j == 0) {
							System.out.print("├ ");
						} else if (j == 14) {
							System.out.print("┤");
						} else {
							System.out.print("┼ ");
						}
					}
					break;
				case 1:
					System.out.print("● ");
					break;
				case 2:
					System.out.print("○ ");
					break;

				}
			}
			// 行末换行
			System.out.println();
		}
		System.out.println("  A B C D E F G H I J K L M N O");
	}

	// 往控制台棋盘添加棋子
	public void addChess(int x, int y) {
		board[y][x] = chessType;
		chessType = 3 - chessType;
		chessCount++;
//		drawBoard();
	}

	public void removeChess(int x, int y) {
		board[y][x] = 0;
		chessType = 3 - chessType;
		chessCount--;
//		drawBoard();
	}

}
