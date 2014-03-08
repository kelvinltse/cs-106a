/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutTest extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/* Method: init() */
	/** Sets up the Breakout program. */
	public void init() {
		/* You fill this in, along with any subsidiary methods */
		setSize(WIDTH, HEIGHT);
		createBricks();
//		createPaddle();
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		GRect paddle = new GRect((WIDTH - PADDLE_WIDTH) / 2, HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}
	
	public void mouseMoved(MouseEvent e) {

		paddle.move(5, 0);
		//lastX = e.getX();
	}
	
	public void createPaddle() {
		GRect paddle = new GRect((WIDTH - PADDLE_WIDTH) / 2, HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	private void createBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			layRowOfBricks(i);
		}
	}
	
	private void layRowOfBricks(int rows) {
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			//each brick in row is placed with a bit of brick separation
			//the y coordinate of the row is increased by height of brick + brick sep for each row
			layBrick(brick_X_Offset() + (i * (BRICK_WIDTH + BRICK_SEP)), BRICK_Y_OFFSET + ((BRICK_HEIGHT + BRICK_SEP) * rows), rows);
		}
	}
	
	private void layBrick(double x, double y, int rows) {
		int w = BRICK_WIDTH;
		int h = BRICK_HEIGHT;
		GRect brick = new GRect(x, y, w, h);
		//sets color based on which row the brick is part of
		brick.setFilled(true);
		switch (rows) {
			case 0: case 1: brick.setColor(Color.RED); break;
			case 2: case 3: brick.setColor(Color.ORANGE); break;
			case 4: case 5: brick.setColor(Color.YELLOW); break;
			case 6: case 7: brick.setColor(Color.GREEN); break;
			case 8: case 9: brick.setColor(Color.CYAN); break;
		}
		add(brick);
	}
	
	//sets offset of bricks from left wall by centering whole brick stack in app window
	private double brick_X_Offset() {
		return (WIDTH - ((BRICK_WIDTH * NBRICKS_PER_ROW) + (BRICK_SEP * (NBRICKS_PER_ROW - 1))))/2;
	}
	
	private GRect paddle;
	//private double lastX;
}
