import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Task 1: Create a Working Demonstrator. <br\>
 * Task 2: Adding a Score and two buttons. <br\>
 * This is my own version main class that uses the software provided to play the
 * basic game in a swing window
 *
 * @author kan billy (b.kan@lancaster.ac.uk)
 *
 */
public class GameDriver implements ActionListener {
	private JLabel scoreLabel;
	private JButton startButton, stopButton;
	private Racer r;

	/**
	 * Default constructor, used to initialize all the components
	 */
	public GameDriver() {
		JFrame window = new JFrame();
		BoxLayout boxLayout = new BoxLayout(window.getContentPane(),
				BoxLayout.Y_AXIS);
		window.setLayout(boxLayout);

		r = new Racer();
		window.setTitle("Racer");
		window.setSize(Racer.SCREEN_WIDTH, Racer.SCREEN_HEIGHT);
		window.add(topPanel());
		window.add(r.getPanel());
		window.add(bottomPanel());
		window.setVisible(true);
	}

	/**
	 * The panel contains the score record of the player
	 *
	 * @return the top panel
	 */
	private JPanel topPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		scoreLabel = new JLabel("Score: ");
		pane.add(scoreLabel, BorderLayout.WEST);
		return pane;
	}

	/**
	 * The panel contains start and stop buttons.
	 *
	 * @return The bottom panel
	 */
	private JPanel bottomPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 2));

		startButton = new JButton("PLAY");
		startButton.addActionListener(this);
		stopButton = new JButton("STOP");
		stopButton.addActionListener(this);

		pane.add(startButton);
		pane.add(stopButton);
		return pane;
	}

	/**
	 * The main entrance of the program
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		GameDriver game = new GameDriver();
		game.play();
	}

	/**
	 * The play function as a routine
	 */
	public void play() {
		r.start();
		while (true) {
			// make the racer able to render the graphical lock
			r.pause();
			while (r.isPlaying()) {
				r.update();
				// update the real time score
				scoreLabel.setText("Score:" + r.getScore());
			}
		}
	}

	/**
	 * Action Listeners for the start and stop button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == startButton) {
			r.start();
		} else if (o == stopButton) {
			r.stop();
		}
	}
}
