import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

/**
 * uses the GameArena APIs to implement a simple top down racing game.
 *
 * The graphical output of the game is provided as a Swing component, so that it
 * can be added into any Swing application, just like a JButton etc.
 *
 * To allow users to control the game as they see fit, start(), stop() and
 * update() methods are provided. start() should be used to create a new game,
 * stop() to terminate a running game, and update() should be called in a loop
 * to update game play and graphics
 *
 * Simple example of use:
 *
 * <pre>
 *
 * JFrame window = new JFrame();
 * Racer r = new Racer();
 * window.setTitle(&quot;Racer&quot;);
 * window.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
 * window.setContentPane(r.getPanel());
 * window.setVisible(true);
 * 
 * r.start();
 * 
 * while (r.isPlaying())
 * 	r.update();
 *
 * </pre>
 *
 * @author Joe Finney (joe@comp.lancs.ac.uk)
 */
public class Racer implements KeyListener {
	public static final double PLAYER_SPEED = 5;
	public static final int ROAD_SEGMENT_WIDTH = 160;
	public static final int ROAD_SEGMENT_HEIGHT = 10;
	public static final int ROAD_CURVE_SPEED = 5;
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;

	private GameArena arena;
	private Car player;
	private RoadSegment[] road = new RoadSegment[SCREEN_HEIGHT
			/ ROAD_SEGMENT_HEIGHT + 1];

	private double currentRoadX = SCREEN_WIDTH / 2;
	private double speed = 2.0;
	private boolean playing = false;
	private int score = 0;

	private HighScoreView scoreFrame;
	private static Set<ScoreRecord> record;

	/**
	 * Creates a new instance of the Racer racing game.
	 */
	public Racer() {
		arena = new GameArena(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		record = new HashSet<>();
		scoreFrame = new HighScoreView();
	}

	/**
	 * Provides a Swing component in which the Racer game runs. This component
	 * can be added to a Swing panel to display the game on screen.
	 *
	 * @return A Swing component for this game.
	 */
	public JComponent getPanel() {
		return arena.getPanel();
	}

	/**
	 * Provides the player's current score in the game.
	 *
	 * @return The player's current score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Starts a new game, if the game is not already running.
	 */
	public void start() {
		if (!playing) {
			// Create the player's car
			player = new Car(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 150, arena);

			// Create the initial road layout
			for (int s = road.length - 1; s >= 0; s--) {
				road[s] = nextRoadSegment();
				road[s].setYPosition(s * ROAD_SEGMENT_HEIGHT);
			}

			score = 0;
			playing = true;
		}
	}

	/**
	 * Stops a currently running game and display a high score table
	 */
	public void stop() {
		if (playing) {
			playing = false;
			arena.exit();
			arena.reset();
			player.reset();
			if (isTopTenScore()) {
				scoreFrame.setVisible(true);
				scoreFrame.enableOrNot(true);
				scoreFrame.equipKeyListener(this);
			}
		}
	}

	/**
	 * Pauses the game for the lock to be rendered
	 */
	public void pause() {
		arena.pause();
	}

	/**
	 * Determines if the game is currently being played.
	 *
	 * @return false if the game has not been started or on game over, true if
	 *         the game is actively running.
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Updates the game state to allow the road and player character to move on
	 * the screen.
	 *
	 * This method should be called in a loop (once per frame) to advance game
	 * play in response to time and user input. The method uses the GameArena
	 * pause() method to ensure the game runs at a constant speed.
	 */
	public void update() {
		if (playing) {
			score++;

			double xspeed = 0;
			double yspeed = 0;
			if (arena.leftPressed())
				xspeed -= PLAYER_SPEED;

			if (arena.rightPressed())
				xspeed += PLAYER_SPEED;

			if (arena.upPressed())
				yspeed -= PLAYER_SPEED;

			if (arena.downPressed())
				yspeed += PLAYER_SPEED;

			player.setXSpeed(xspeed);
			player.setYSpeed(yspeed);

			player.move();

			for (int i = 0; i < road.length; i++) {
				if (road[i] != null)
					road[i].move();
			}

			// Recycle any segments that have scrolled off screen...
			recycleRoadSegments();

			if (hasCrashed())
				stop();
		}

		arena.pause();
	}

	/**
	 * Provides a randomly generated, thin slice of road. This method is used
	 * periodically to create new road on the screen in front of the player's
	 * car.
	 *
	 * @return A new randomly generated RoadSegment
	 */
	private RoadSegment nextRoadSegment() {
		currentRoadX += Math.random() * 2 * ROAD_CURVE_SPEED - ROAD_CURVE_SPEED;
		RoadSegment s = new RoadSegment(currentRoadX, -ROAD_SEGMENT_HEIGHT,
				ROAD_SEGMENT_WIDTH, ROAD_SEGMENT_HEIGHT, arena);
		s.setYSpeed(speed);
		return s;
	}

	/**
	 * Removes any parts of road that have scrolled off the bottom of the
	 * screen.
	 */
	private void recycleRoadSegments() {
		for (int i = 0; i < road.length; i++) {
			if (road[i].getYPosition() > SCREEN_HEIGHT) {
				double y = road[i].getYPosition();
				road[i].remove();
				road[i] = nextRoadSegment();
				road[i].setYPosition(y - SCREEN_HEIGHT - ROAD_SEGMENT_HEIGHT);
			}
		}
	}

	/**
	 * Determines if the player has crashed (driven off road)
	 *
	 * @return true is the player is touching the kerb/grass, false otherwise.
	 */
	private boolean hasCrashed() {
		for (int i = 0; i < road.length; i++) {
			if (player.isTouching(road[i]))
				return true;
		}

		return false;
	}

	/**
	 * Check whether the score can go to top 10
	 *
	 * @return whether it is in top 10 score
	 */
	private boolean isTopTenScore() {
		ArrayList<ScoreRecord> list = new ArrayList<>(record);
		Collections.sort(list);

		// if there are less than 10 scores or it is larger
		// than the smallest one
		if (list.size() < 10 || list.get(0).getScore() < score)
			return true;

		return false;
	}

	/**
	 * Display the score board into the frame
	 */
	private void display() {
		ArrayList<ScoreRecord> list = new ArrayList<>(record);
		Collections.sort(list);

		int length = list.size();
		for (int i = length - 1; i >= 0; i--) {
			scoreFrame.display(length - i - 1, list.get(i).getName(),
					list.get(i).getScore());
		}
	}

	/**
	 * Press the enter key to enter the name
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String name = scoreFrame.name();
			if (name != null && name.length() != 0) {
				this.scoreFrame.clearField();
				this.scoreFrame.enableOrNot(false);
				record.add(new ScoreRecord(name, score));
				display();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
