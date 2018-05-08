package com.andy.gomoku;

import java.util.Scanner;

import com.andy.gomoku.ai.Move;
import com.andy.gomoku.ai.NegamaxAI;

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
		NegamaxAI player = new NegamaxAI(15);
		Move mov = player.getBestMove();
		int win = player.addChess(mov);
		System.out.println(mov.col + ":"+ mov.row);
		Scanner scan = new Scanner(System.in);
		while(win < 0){
			String fd = scan.nextLine();
			if(fd.equals("b")){
				player.takeBack();
			}else{
				String[] fds = fd.split(",");
				player.addChess(new Move(Integer.parseInt(fds[1]), Integer.parseInt(fds[0])));
				
				mov = player.getBestMove();
				win = player.addChess(mov);
				System.out.println(mov.col + ":"+ mov.row);
			}
		}
		scan.close();
	}

}
