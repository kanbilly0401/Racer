import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * It represents a record in the score panel
 *
 * @author kanbilly (b.kan@lancaster.ac.uk)
 *
 */
public class ScorePanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel nameLabel, scoreLabel;

	/**
	 * Default constructor
	 */
	public ScorePanel() {
		this.setLayout(new GridLayout(1, 2));
		nameLabel = new JLabel("None");
		scoreLabel = new JLabel("0");
		this.add(nameLabel);
		this.add(scoreLabel);
	}

	/**
	 * Set the content of the record
	 *
	 * @param name
	 * @param score
	 */
	public void setRecord(String name, int score) {
		nameLabel.setText(name);
		scoreLabel.setText(String.valueOf(score));
	}

}
