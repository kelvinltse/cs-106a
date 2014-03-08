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

public class Breakout extends GraphicsProgram {
	
	public static void main(String[] args) {
		new Breakout().start(args);
	}

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
	
	private static final int PAUSETIME = 15;

	/* Method: init() */
	/** Sets up the Breakout program. */
	public void init() {
		/* You fill this in, along with any subsidiary methods */
		setSize(WIDTH, HEIGHT);
		createBricks();
		createPaddle();
		createBall();
		//initializes paddle variable so it can be used by mouselisteners
		paddle = getElementAt((WIDTH - PADDLE_WIDTH) / 2, HEIGHT - PADDLE_Y_OFFSET);
		//initialize lastX as middle of screen so it doesn't "jump" on first mouse movement
		lastX = WIDTH / 2;
		//initialize ball
		ball = getElementAt(WIDTH / 2, HEIGHT / 2);
		setRandomLaunchAngle();
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		int brickCount = NBRICKS_PER_ROW * NBRICK_ROWS;
		int lives = NTURNS;
		int paddleBounces = 0;
		while (true) {
			bounceBallOffWalls();
			if (isBottomWall()) {
				lives--;
				if (lives == 0) {
					gameOver();
					break;
				}
				resetBall();
				setRandomLaunchAngle();
				paddleBounces = 0;
			}
			ball.move(vx, vy);
			if (getCollidingObject() != null) {
				bounceClip.play();
				//if colliding object is a brick, remove it
				if (getCollidingObject() != paddle) {
					remove(getCollidingObject());
					brickCount--;
					if(brickCount == 0) {
						playerWins();
						break;
					}
					if (ballHitsSide()) {
					vx = -vx;
					} else {
						vy = -vy;
					}
				} else {
					paddleBounces++;
					if (ballHitsSide()) {
						vx = -vx;
						//corner control bounce
						ball.setLocation(ball.getX(), paddle.getY() - BALL_RADIUS);
						vy = -vy;
						} else {
							ball.setLocation(ball.getX(), paddle.getY() - BALL_RADIUS);
							vy = -vy;
						}
				}
			}
			//speed of ball, doubles after 7th bounce
			if (paddleBounces < 7) {
				pause(PAUSETIME);
			} else {
				pause(PAUSETIME / 2);
			}
		}
	}
	
	private void playerWins() {
		GLabel label = new GLabel("YOU WIN");
		add(label, (getWidth() - label.getWidth()) / 2, getHeight() / 2);
	}
	
	// side of ball is detected 1 px away from true side
	private boolean ballHitsSide() {
		return ((getElementAt(ball.getX() - 1, ball.getY() + (BALL_RADIUS / 2)) != null)
		|| (getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + (BALL_RADIUS / 2)) != null));
	}
	
	private void setRandomLaunchAngle() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = 3.0;
	}
	
	private void resetBall() {
		pause(1000);
		ball.setLocation((getWidth() - BALL_RADIUS) / 2, (getHeight() - BALL_RADIUS) / 2);
	}
	
	private boolean isBottomWall() {
		return (ball.getY() + BALL_RADIUS + vy) > HEIGHT;
	}
	
	private void gameOver() {
		GLabel label = new GLabel("GAME OVER");
		add(label, (getWidth() - label.getWidth()) / 2, getHeight() / 2);
	}
	
	private void bounceBallOffWalls() {
		if (((ball.getX() + vx) < 0) || ((ball.getX() + BALL_RADIUS + vx) > WIDTH)) {
			vx = -vx;
		}
		if ((ball.getY() + vy) < 0) {
			vy = -vy;
		}
	}
	
	private GObject getCollidingObject() {
		if (getElementAt(ball.getX(), ball.getY()) == null) {
			if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY()) == null) {
				if (getElementAt(ball.getX(), ball.getY() + BALL_RADIUS) == null) {
					if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS) == null) {
						return null;
					} else {
						return getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
					}
				} else {
					return getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
				}
			} else {
				return getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
			}
		} else {
			return getElementAt(ball.getX(), ball.getY());
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		//prevent paddle from moving past edge of screen
		if ((paddle.getX() + (e.getX() - lastX)) < 0) {
			paddle.setLocation(0, HEIGHT - PADDLE_Y_OFFSET);
		} else if ((paddle.getX() + PADDLE_WIDTH + (e.getX() - lastX)) > WIDTH) {
			paddle.setLocation(WIDTH - PADDLE_WIDTH, HEIGHT - PADDLE_Y_OFFSET);
		} else {
			paddle.move(e.getX() - lastX, 0);
		}
		lastX = e.getX();
	}
	
	private void createBall() {
		//centers ball in screen
		GOval ball = new GOval((WIDTH - BALL_RADIUS) / 2, (HEIGHT - BALL_RADIUS) / 2, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		add(ball);
	}
	
	private void createPaddle() {
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
	
	private GObject paddle;
	private double lastX;
	private GObject ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private AudioClip bounceClip  = MediaTools.loadAudioClip("bounce.au");
}
