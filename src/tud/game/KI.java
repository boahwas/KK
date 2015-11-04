package tud.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class KI{
	
	/**
	 * Reference to the gameboard
	 */
	Board board;
	/**
	 * List of possible moves
	 */
	List<Integer> posMoves;
	
	public KI(Board board){
		this.board = board;
	}
	/**
	 * Updates the possible moves
	 */
	public void updateBoard(){
		posMoves = board.getPossibleMoves();
	}
	/**
	 * Returns a random integer which represents a position on the board
	 * @return a random Integer from posMoves
	 * @author Philip Stauder
	 */
	public Integer randomMove(){
		
		Random rand = new Random();
		
		return posMoves.get(rand.nextInt(posMoves.size()-1));
	}

}

