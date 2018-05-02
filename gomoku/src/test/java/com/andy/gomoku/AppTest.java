package com.andy.gomoku;

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

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		
		WineAI fd = new WineAI(15);
		fd.addChess(7, 7);
		Mov mov = fd.getBestMove();
		System.out.println(mov.x + ":"+ mov.y);
	}
}
