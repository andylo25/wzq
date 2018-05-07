package com.andy.gomoku;

import java.util.Scanner;

import com.andy.gomoku.ai.GameInfo;
import com.andy.gomoku.ai.GameState;
import com.andy.gomoku.ai.Move;
import com.andy.gomoku.ai.NegamaxPlayer;
import com.andy.gomoku.ai.State;
import com.andy.gomoku.ai.WineAI;
import com.andy.gomoku.ai.WineAI.Mov;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

//	/**
//	 * Rigourous Test :-)
//	 */
//	public void testApp() {
//		
//		WineAI fd = new WineAI(15);
//		fd.addChess(7, 7);
//		Mov mov = fd.getBestMove();
//		System.out.println(mov.x + ":"+ mov.y);
//	}
	
	public void testAi() {
		State state = new State(15);
		NegamaxPlayer player = new NegamaxPlayer(state);
		Move mov = player.getMove();
		state.makeMove(mov);
		System.out.println(mov.col + ":"+ mov.row);
		Scanner scan = new Scanner(System.in);
		while(state.checkWin() == 0){
			String fd = scan.nextLine();
			if(fd.equals("b")){
				state.undoMove();
			}else{
				String[] fds = fd.split(",");
				state.makeMove(new Move(Integer.parseInt(fds[1]), Integer.parseInt(fds[0])));
				
				mov = player.getMove();
				state.makeMove(mov);
				System.out.println(mov.col + ":"+ mov.row);
			}
		}
		scan.close();
	}

}
