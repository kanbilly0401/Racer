import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * It represents the high score's view
 *
 * @author kanbilly (b.kan@lancaster.ac.uk)
 *
 */
public class HighScoreView extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final int TOP = 10;
	private ScorePanel panel[] = new ScorePanel[TOP];
	private JTextField nameField;

	/**
	 * Default constructor
	 */
	public HighScoreView() {
		this.setTitle("HIGH SCORES");
		this.setSize(new Dimension(300, 300));
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.initialise();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	/**
	 * Add key listener to the name field
	 */
	public void equipKeyListener(KeyListener listener) {
		nameField.addKeyListener(listener);
	}

	/**
	 * return the content of the text field
	 */
	public String name() {
		return nameField.getText();
	}

	/**
	 * Clear the content of the text field
	 */
	public void clearField() {
		nameField.setText("");
	}

	/**
	 * Set the enable option
	 *
	 * @param option
	 */
	public void enableOrNot(boolean option) {
		nameField.setEnabled(option);
	}

	/**
	 * Display the record at the right position
	 *
	 * @param i
	 *            position
	 * @param name
	 *            player's name
	 * @param score
	 *            player's score
	 */
	public void display(int i, String name, int score) {
		panel[i].setRecord(name, score);
	}

	/**
	 * Add initial content into the score panel
	 */
	private void initialise() {
		for (int i = 0; i < TOP; i++) {
			panel[i] = new ScorePanel();
			this.add(panel[i]);
		}
		this.add(inputPanel());
	}

	/**
	 * Input name panel
	 *
	 * @return name input panel
	 */
	private JPanel inputPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));

		JLabel nameLabel = new JLabel("YOUR NAME: ");
		panel.add(nameLabel);

		nameField = new JTextField();
		panel.add(nameField);

		return panel;
	}
}
