package tud.game;
/**
 * data class for column and row
 * @author Leonid
 *
 */
public class Position {

	/**
	 * row of position
	 */
	private int row;
	/**
	 * column of position
	 */
	private int col;

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

}
