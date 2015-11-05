package tud.game;

/**
 * @author Daniel Strippel
 *
 */
public class MiniMax {

	private Board b;
	private static final int MAX_DEPTH = 3;

	public MiniMax() {

	}

	/**
	 * Returns the Best Move with MiniMax
	 * @param player - currentPlayer
	 * @param opponent - nextPlayer
	 * @param newboard - Board
	 * @return nextMove
	 */
	public int findMove(Player player, Player opponent, Board newboard) {
		int takeThisMove = 0;
		maxPlayer = player;
		minPlayer = opponent;
		this.b = newboard.getBoardCopy();
		double valueOfBestMove = Double.NEGATIVE_INFINITY;
		double v = 0;
		for (int m : b.getPossibleMoves()) {

			Board newBoard = b.getBoardCopy();
			b.nextAction(m, "");

			v = MinValue(newBoard, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, MAX_DEPTH - 1);
			if (v >= valueOfBestMove) {
				valueOfBestMove = v;
				takeThisMove = m;
			}
		}
		return takeThisMove;
	}

	/**
	 * min of MiniMax
	 * @param b
	 * @param alpha 
	 * @param beta
	 * @param d
	 * @return best score of minplayer
	 */
	private double MinValue(Board b, double alpha, double beta, int d) {
		if (d < 1) {
			return scoreHeuristically(b, d);
		}

		double v = Double.POSITIVE_INFINITY;
		Board board = b.getBoardCopy();

		for (int m : board.getPossibleMoves()) {

			Board newBoard = board.getBoardCopy();
			b.nextAction(m, "");

			v = Math.min(v, MaxValue(newBoard, alpha, beta, d - 1));
			if (v <= alpha) {
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}

	/**
	 * max of MiniMax
	 * @param b
	 * @param alpha
	 * @param beta
	 * @param d
	 * @return best score of maxplayer
	 */
	private double MaxValue(Board b, double alpha, double beta, int d) {
		if (d < 1) {
			return scoreHeuristically(b, d);
		}

		double v = Double.NEGATIVE_INFINITY;
		Board board = b.getBoardCopy();

		for (int m : board.getPossibleMoves()) {

			Board newBoard = board.getBoardCopy();
			b.nextAction(m, "");
			v = Math.max(v, MinValue(newBoard, alpha, beta, d - 1));
			if (v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	/**
	 * Scores the Board
	 * @param b
	 * @param d
	 * @return score of Board
	 */
	public int scoreHeuristically(Board b, int d) {
		int score = 0;
		for (int i = 0; i < b.board.length; i++) {
			for (int j = 0; j < b.board[i].length; j++) {
				if (i % 2 == 0) {
				} else {
					if (j > 0 && j < b.fieldLen * 2) {
						if (b.board[i][j].equals(" ")) {
							score = checktoclose(b, i, j) * d;
							break;
						}
					}
				}
			}
		}
		return score;
	}

	/**
	 * checks of board if 3 lines are set    
	 * @param b
	 * @param i
	 * @param j
	 * @return score
	 */
	private int checktoclose(Board b, int i, int j) {
		if (b.board[i - 1][j].equals("-") && b.board[i + 1][j].equals("-") && b.board[i][j + 1].equals("|")
				|| b.board[i][j + 1].equals("|") && b.board[i + 1][j].equals("-") && b.board[i][j - 1].equals("|")
				|| b.board[i + 1][j].equals("-") && b.board[i][j - 1].equals("|") && b.board[i - 1][j].equals("-")
				|| b.board[i][j - 1].equals("|") && b.board[i - 1][j].equals("-") && b.board[i][j + 1].equals("|")) {
			return 1000;
		}
		return 0;
	}

}
