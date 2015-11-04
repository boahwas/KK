package tud.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniMax {
	
	private Player maxPlayer;
	private Player minPlayer;
	private Board board;
	private static final int MAX_DEPTH = 4;
	
	private long moveCalculationStartedAt;
	
	private final int maxDurationForMoveInMilliSeconds;
	private final int maxDurationForAllMovesInMilliSeconds;
	
	public MiniMax(int m1, int m2) {
		maxDurationForMoveInMilliSeconds = m1;
		maxDurationForAllMovesInMilliSeconds = m2;
	}
	
	public int findMove(Player player, Player opponent, Board newboard, long moveCalculationStartedAt){
		int takeThisMove = 0;
		this.moveCalculationStartedAt = moveCalculationStartedAt;
		maxPlayer = player;
		minPlayer = opponent;
		this.board = newboard.getBoardCopy();
		double valueOfBestMove = Double.NEGATIVE_INFINITY;
		double v = 0;
		for (int m : board.getPossibleMoves()){
			long elapsedTime = System.nanoTime() - moveCalculationStartedAt;
			double millseconds = (double) elapsedTime / 1000000.0;
			if (millseconds > maxDurationForAllMovesInMilliSeconds) {
				break;
			}
			
			Board newBoard = board.getBoardCopy();
			updateBoard(m, newBoard, maxPlayer);
			
			v = MinValue(newBoard, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, MAX_DEPTH - 1);
			if (v >= valueOfBestMove) {
				valueOfBestMove = v;
				takeThisMove = m;
			}
		}
		return takeThisMove;
	}
	
	public void updateBoard(int m, Board b, Player p) {
		b.nextAction(m, p.getName());
	}
	
	private double MinValue(Board b, double alpha, double beta, int d) {
		if (d < 1) {
			return scoreHeuristically(b);
		}

		double v = Double.POSITIVE_INFINITY;
		Board board = b.getBoardCopy();

		for (int m : board.getPossibleMoves()) {
			long elapsedTime = System.nanoTime() - moveCalculationStartedAt;
			double millseconds = (double) elapsedTime / 1000000.0;
			if (millseconds > maxDurationForMoveInMilliSeconds) {
				break;
			}
			Board newBoard = board.getBoardCopy();
			updateBoard(m, newBoard, minPlayer);
			
			v = Math.min(v, MaxValue(newBoard, alpha, beta, d - 1));
			if (v <= alpha) {
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	private double MaxValue(Board b, double alpha, double beta, int d) {
		if (d < 1) {
			return scoreHeuristically(b);
		}

		double v = Double.NEGATIVE_INFINITY;
		Board board = b.getBoardCopy();

		for (int m : board.getPossibleMoves()) {
			long elapsedTime = System.nanoTime() - moveCalculationStartedAt;
			double millseconds = (double) elapsedTime / 1000000.0;
			if (millseconds > maxDurationForMoveInMilliSeconds) {
				break;
			}
			Board newBoard = board.getBoardCopy();
			updateBoard(m, newBoard, maxPlayer);
			v = Math.max(v, MinValue(newBoard, alpha, beta, d - 1));
			if (v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;
	}
	
	public int scoreHeuristically(Board b) {
		int score = 0;
		List<Integer> moves = b.getPossibleMoves();
		for(int m: moves){
			if(!b.nextAction(m, "1").equals(Action.Again)){
				score = score + 12;
			}
		}
		Random r = new Random();
		score = r.nextInt(32);
		return score; 
	}
	
}
