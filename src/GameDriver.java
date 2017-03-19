import javax.swing.JFrame;

/**
 * Task 1: Create a Working Demonstrator. <br\>
 * This is my own version main class that uses the software provided to play the
 * basic game in a swing window
 *
 * @author kan billy (b.kan@lancaster.ac.uk)
 *
 */
public class GameDriver {

	public static void main(String args[]) {
		JFrame window = new JFrame();
		Racer r = new Racer();
		window.setTitle("Racer");
		window.setSize(Racer.SCREEN_WIDTH, Racer.SCREEN_HEIGHT);
		window.setContentPane(r.getPanel());
		window.setVisible(true);

		r.start();

		while (r.isPlaying())
			r.update();
	}
}
