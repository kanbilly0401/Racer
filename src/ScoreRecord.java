/**
 * A concrete class represents a record
 *
 * @author kanbilly (b.kan@lancaster.ac.uk)
 *
 */
public class ScoreRecord implements Comparable<Object> {
	private String name;
	private int score;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	public ScoreRecord(String name, int score) {
		this.name = name;
		this.score = score;
	}

	@Override
	public int compareTo(Object o) throws ClassCastException {
		if (!(o instanceof ScoreRecord)) {
			throw new ClassCastException("A ScoreRecord object expected.");
		}

		ScoreRecord other = (ScoreRecord) o;
		return this.score - other.score;
	}
}
