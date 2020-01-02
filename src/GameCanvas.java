import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;

public class GameCanvas extends JComponent implements KeyListener {
	protected Timer timer1;
	protected int rectX, rectY, velocityX = 40, velocityY = 30, ballSize = 20, rectWidth = 100, rectHeight = 30, lives = 3, score = 0, actualTime = 0,
			gameTime = 0, levelTime = 10, level = 1, highScore = HighScore.read(), adjustToMiddle = 100;
	protected String rectDirection;
	protected double ballX, ballY, dirX = -1, dirY = 1, theta = Math.toRadians(30), time = 0.3, increaseBallX, increaseBallY, timeX, 
			timeY;
	protected boolean gameOver = false;
	protected String messageToPrint;
	@Override
	public void paintComponent(Graphics g) {	
		rectY = (int) (getHeight()*0.95);

		if(!(timer1.isRunning())) { //initial position of ball and pad before game starts
			rectX = getWidth()/2 - rectWidth/2;
			ballX = getWidth()/2 - ballSize/2;
			ballY = getHeight()/4 - ballSize/2;
			
			
			System.out.println("Initial X: " + ballX + " dirX: " + dirX +  
					" Y: " + ballY + " dirY: " + dirY);
		} 
		if (gameOver) { //if game is over, a message is displayed
			g.setFont(new Font("Calibri", 1, 40));
			g.drawString("GAME OVER!", getWidth()/2 - 100, getHeight()/2);
			g.drawString(messageToPrint, getWidth()/2 - adjustToMiddle, getHeight()/2 + 50);
			g.drawString("Press SPACE to start again!", getWidth()/2 - 200, getHeight()/2 + 100);

		}
		g.setFont(new Font("Calibri", 0, 12));
		updateHighScoreLabel(g);
		updateLevelLabel(g, level);
		updateScoreLabel(g);
		g.setColor(Color.BLUE);
		updateLives(g);
		g.setColor(Color.GREEN);
		updateTimer(g);


		g.setColor(Color.RED);
		g.fillRect(rectX, rectY, rectWidth, rectHeight);
		g.setColor(Color.BLUE);
		g.fillOval((int) ballX, (int) ballY, ballSize, ballSize);
	}
	
	public GameCanvas() {
		
		timer1 = new Timer(5, new TimerCallback());
		addKeyListener(this);
		setFocusable(true);
	}
	protected class TimerCallback implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			collide();
			gameTime++;
			System.out.println(getWidth() + " " + getHeight());
			timeX += dirX * 0.04;
			timeY += dirY * 0.025;
			ballX = velocityX * Math.cos(theta) * timeX; 
			ballY = ((velocityY * Math.sin(theta) * timeY) + (0.5 * 9.81 * timeY * timeY));
			
			System.out.println("X: " + ballX + " dirX: " + dirX +  
					" Y: " + ballY + " dirY: " + dirY);
			repaint();
		}
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		System.out.println(e);
		if (timer1.isRunning()) { //key press only moves rectangle if timer is running
			if (keyCode == KeyEvent.VK_LEFT) {
				if (rectX > 0) {
					rectX -= 20;
				}
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				if (rectX + rectWidth < getWidth()) {
					rectX += 20;
				}
			}
		} else if (keyCode == KeyEvent.VK_SPACE) {
			gameOver = false; 
			timer1.start();
			System.out.println("timer started");
		}
		//repaint();	
	}
	public void collide() {
		if (ballInPlay()) { //if ball is in play
			if (ballX + ballSize >= getWidth()) { //if ball touches right wall
				dirX = -1;
				sound();
			} else if (ballY <= 0)  { //if ball touches top wall
				dirY = 1;
				sound();
			} else if (ballX <= 0) { //if ball touches left wall
				dirX = 1;
				sound();
			} else if(ballY + ballSize >= rectY) { // if ball touches pad
				sound();
				if ((ballX + ballSize >= rectX) && (ballX + ballSize <= rectX + rectWidth/4)) { //ball touches left corner of pad
					//ball goes left
					dirX = -1;
					dirY = -1;
					score++;

				} else if ((ballX + ballSize > rectX + rectWidth/4) && (ballX + ballSize <= rectX + 3*rectWidth/4))  { //if ball touches middle of pad
					//ball is simply reflected
					dirY = -1;
					score++;

				} else if ((ballX + ballSize > rectX + 3*rectWidth/4) && (ballX <= rectX + rectWidth)) { //if ball touches right corner of pad
					//ball goes right
					dirX = 1;
					dirY = -1;
					score++;

				}
			} 
		} else {
			//everything is initialized if there is no collision with pad
			lives--;
			timer1.stop();
			if (lives < 0) {
				endGame();
			} /*else {
				//timeX = 0;
				//timeY = 0;
			}*/
			timeX = 0;
			timeY = 0;
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {

		// TODO Auto-generated method stub
		
	}
	public boolean ballInPlay() { //checks if the ball is in play
		if (ballY + ballSize < getHeight()) {
			return true;
		} else {
			return false;
		}
	}
	public void updateLives(Graphics g) { //changes the amount of lives displayed on top left corner
		switch (lives) {
		case 3:
			g.fillOval(10, 10, 15, 15);
			g.fillOval(30, 10, 15, 15);
			g.fillOval(50, 10, 15, 15);
			break;
		case 2:
			g.fillOval(10, 10, 15, 15);
			g.fillOval(30, 10, 15, 15);
			break;
		case 1:
			g.fillOval(10, 10, 15, 15);
			break;
		} 
	}
	public void updateScoreLabel(Graphics g) { //draws a string on the top with the current score
		g.drawString("SCORE: " + score, getWidth()/2 - 5, 20);
	}
	public void calculateActualTime() { //calculates the actual amount of time passed
		actualTime = gameTime/200; //since timer is being run 200 times every second 
	}
	public void updateTimer(Graphics g) { //draws timer
		calculateActualTime();
		System.out.println(actualTime);
		if (actualTime < levelTime) { //timer is only drawn if it's lesser than time of level
			g.fillArc(getWidth()-50, 10, 40, 40, 0, 360 - 360/levelTime * actualTime); //draws arc which slowly reduces
		} else { //if levelTime is up, value of level is incremented and timer is stopped
			level++;
			//timer1.stop();
			if (rectWidth >= 10) { //the size of pad decreases after every level
				rectWidth -= 10; 
			}
			levelTime = 10*level; //levelTime increases by 10 after every level
			actualTime = 0; //initialises time
			gameTime = 0;
			repaint();
		} 
	}
	public void updateLevelLabel(Graphics g, int level) { //level label is updated to new level
		g.drawString("LEVEL: " + level, getWidth()/4 - 5, 20);

	}
	public void updateHighScoreLabel(Graphics g) {
		g.drawString("HIGHSCORE: " + highScore, getWidth()- 230, 20);
	}
	
	public void endGame() {
		//everything is initialized when lives end
		Graphics g = getGraphics();
		if (score > highScore) { //if current score is more than current highscore, then the highscore is set to current score
			highScore = score;
			HighScore.write(highScore);
			messageToPrint = "CONGRATULATIONS! NEW HIGHSCORE: " + highScore;
			adjustToMiddle += 250;
		} else {
			adjustToMiddle = 100;
			messageToPrint = "Your score: " + score;
		}
		gameOver = true;
		levelTime = 10;
		actualTime = 0;
		gameTime = 0;
		lives = 3; 
		score = 0;
		level = 1;
		rectWidth = 100;
	}
	public void sound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("../other-files/boing.wav"));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
}
