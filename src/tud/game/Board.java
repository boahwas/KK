package tud.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Board {

	/**
	 * count of fields in the board
	 */
	int fieldLen = 4;
	/**
	 * game board representation multiple array
	 */
	//String[][] board = new String[fieldLen * 2 + 1][];
	String[][] board;
	/**
	 * length of longest position number, e.g. position 123 length = 3
	 */
	int optionLen = 1;
	/**
	 * length of longest position number, e.g. position 123 length = 3
	 */
	int count = 0;
	/**
	 * list of positions in game
	 */
	List<Position> positions = new ArrayList<Position>();

	public Board() {
		init();
	}
	
	/**
	 * Constructor with arbitrary size
	 * @param size - Size of Board
	 */
	public Board(int size) {
		this.fieldLen = size;
		board = new String[fieldLen * 2 + 1][];
		init();
	}
	/**
	 * Initializes the board with numbers and helping stars
	 */
	private void init() {
		for (int i = 0; i < board.length; i++) {
			board[i] = new String[fieldLen * 2 + 1];

		}
		int count = 1;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						board[i][j] = "*";
					} else {
						board[i][j] = String.valueOf(count);
						positions.add(new Position(i, j));
						count++;
					}
				} else {
					if (j % 2 == 0) {
						board[i][j] = String.valueOf(count);
						positions.add(new Position(i, j));
						count++;
					} else {

						board[i][j] = " ";
					}

				}
			}
		}
		this.count = count - 1;
		optionLen = String.valueOf(count - 1).length();
	}
	
	/**
	 * returns allPossibleMoves of Board
	 */
	public List<Integer> getPossibleMoves(){
		List<Integer> moves = new ArrayList<Integer>();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if (i % 2 == 0) {
					if (j % 2 == 1) {
						if(!board[i][j].startsWith("-")){
							moves.add(Integer.valueOf(board[i][j]));
						}
					}
				}else
				{
					if (j % 2 == 0) {
						if(!board[i][j].startsWith("|")){
							moves.add(Integer.valueOf(board[i][j]));
						}
					} 
				}
			}
		}
		return moves;
	}
	/**
	 * prints the board onto the console
	 */
	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (i % 2 == 0) {
					System.out.printf(" %" + optionLen + "s ", board[i][j]);
				} else {
					if (j > 0 && j < fieldLen * 2) {
						if (board[i][j].equals(" "))
							System.out.printf("%" + (board[i][j].length() + 1) + "s", " ");
						if (board[i][j].startsWith("p")) {
							System.out.printf("%" + (board[i][j].length()) + "s", " ");
						}
					}
					System.out.printf(" %" + optionLen + "s", board[i][j]);
				}
			}
			System.out.printf("\n");
		}
		System.out.println();
	}
	/**
	 * checks if the wall number pos is not set
	 * @param pos - position, which should be checked
	 * @return true if wall is free, otherwise false
	 */
	public boolean checkRules(int pos) {
		if (pos < 1 || pos > count)
			return false;
		if (board[getRow(pos)][getCol(pos)].equals(String.valueOf(pos))) {
			return true;
		}
		return false;
	}

	/**
	 * sets the wall on position pos and checks if a box is conquered
	 * @param pos - position where to set the wall
	 * @param name - internal player name
	 * @return if a box is conquered then Again else NextTurn
	 */
	public Action nextAction(int pos, String name) {
		int row = getRow(pos);
		int col = getCol(pos);
		if (board[row][col].equals(String.valueOf(pos))) {
			if (row % 2 == 0) {
				board[row][col] = "-";
				return isConqueredByRow(name, row, col);

			} else {
				board[row][col] = "|";
				return isConqueredByCol(name, row, col);
			}
		}

		return Action.NextTurn;
	}

	/**
	 * returns column of position pos
	 * @param pos - position for which we want to have column
	 * @return column of position pos
	 */
	private int getCol(int pos) {
		return positions.get(pos - 1).getCol();
	}

	/**
	 * returns row of position pos
	 * @param pos - position for which we want to have row
	 * @return row of position pos
	 */
	private int getRow(int pos) {
		return positions.get(pos - 1).getRow();
	}
	/**
	 * checks if above or below box is conquered
	 * @param name - internal player name
	 * @param i - row of set position 
	 * @param j - column of set position
	 * @return Again if a box is conquered, NextTurn otherwise
	 */
	private Action isConqueredByRow(String name, int i, int j) {
		Action again = Action.NextTurn;
		again = checkBoxAbove(name, i, j, again);
		again = checkBoxBelow(name, i, j, again);
		return again;
	}

	/**
	 * checks if above or below box is conquered
	 * @param name - internal player name
	 * @param i - row of set position 
	 * @param j - column of set position
	 * @return Again if a box is conquered, NextTurn otherwise
	 */
	private Action checkBoxBelow(String name, int i, int j, Action again) {
		if (i + 2 < board.length) {
			if (board[i + 2][j].equals("-") && board[i + 1][j - 1].equals("|") && board[i + 1][j + 1].equals("|")) {
				board[i + 1][j] = name;
				again = Action.Again;
			}
		}
		return again;
	}

	/**
	 * checks if above box is conquered
	 * @param name - internal player name
	 * @param i - row of set position 
	 * @param j - column of set position
	 * @param again - result of the previous check
	 * @return Again if a box is conquered, NextTurn otherwise
	 */
	private Action checkBoxAbove(String name, int i, int j, Action again) {
		if (i - 2 >= 0) {
			if (board[i - 2][j].equals("-") && board[i - 1][j - 1].equals("|") && board[i - 1][j + 1].equals("|")) {
				board[i - 1][j] = name;
				again = Action.Again;
			}
		}
		return again;
	}
	/**
	 * checks if left or right box is conquered
	 * @param name - internal player name
	 * @param i - row of set position 
	 * @param j - column of set position
	 * @return Again if a box is conquered, NextTurn otherwise
	 */
	private Action isConqueredByCol(String name, int i, int j) {
		Action again = Action.NextTurn;
		again = checkBoxLeft(name, i, j, again);
		again = checkBoxRight(name, i, j, again);
		return again;
	}

	/**
	 * checks if right box is conquered
	 * @param name - internal player name
	 * @param i - row of set position 
	 * @param j - column of set position
	 * @param again - result of the previous check
	 * @return Again if a box is conquered, NextTurn otherwise
	 */
	private Action checkBoxRight(String name, int i, int j, Action again) {
		if (j + 2 < board[i].length) {
			if (board[i][j + 2].equals("|") && board[i - 1][j + 1].equals("-") && board[i + 1][j + 1].equals("-")) {
				board[i][j + 1] = name;
				again = Action.Again;
			}
		}
		return again;
	}
	/**
	 * checks if left box is conquered
	 * @param name - internal player name
	 * @param i - row of set position 
	 * @param j - column of set position
	 * @param again - result of the previous check
	 * @return Again if a box is conquered, NextTurn otherwise
	 */
	private Action checkBoxLeft(String name, int i, int j, Action again) {
		if (j - 2 >= 0) {
			if (board[i][j - 2].equals("|") && board[i - 1][j - 1].equals("-") && board[i + 1][j - 1].equals("-")) {
				board[i][j - 1] = name;
				again = Action.Again;
			}
		}
		return again;
	}

	/**
	 * checks if a position is not set and the game is over
	 * @return true if game is over, false otherwise
	 */
	public boolean isEnded() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].matches("[1-9][0-9]*"))
					return false;
			}
		}

		return true;
	}

	/**
	 * checks who has the most conquered boxes and prints the player statistics
	 * @param ps - map of internal player names to players
	 */
	public void showScore(Map<String, Player> ps) {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		int maxCount = 0;
		String player = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].startsWith("p")) {
					int count = 1;
					if (map.containsKey(board[i][j])) {
						count += map.get(board[i][j]);
					}
					if (count > maxCount) {
						maxCount = count;
						player = board[i][j];
					}
					map.put(board[i][j], count);
				}
			}
		}
		for (String p : ps.keySet()) {
			if (!map.containsKey(p)) {
				map.put(p, 0);
			}
		}
		List<String> players = determineWinners(ps, map, maxCount);
		printWinners(players, ps.get(player).getName());
		printPlayerStats(ps, map);

	}
	/**
	 * prints winners
	 * @param players - winners
	 * @param player - one player name of the winners
	 */
	private void printWinners(List<String> players, String player) {
		if (players.size() > 1) {
			System.out.println("Draw between players: " + Arrays.toString(players.toArray()));
		} else {
			System.out.println("The player " + player + " has won!!!");
		}
	}

	/**
	 * prints statistics of each player
	 * @param ps - map of internal player names to players
	 * @param map - map of internal player names to conquered box counts
	 */
	private void printPlayerStats(Map<String, Player> ps, Map<String, Integer> map) {
		System.out.println("The players have:");
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(ps.get(entry.getKey()).getName() + "(" + entry.getKey() + "): " + entry.getValue());
		}
	}
	/**
	 * determines the winners of the game
	 * @param ps -  map of internal player names to players
	 * @param map - map of internal player names to conquered box counts
	 * @param maxCount - highest number of conquered boxes
	 * @return - list of winners
	 */
	private List<String> determineWinners(Map<String, Player> ps, Map<String, Integer> map, int maxCount) {

		List<String> players = new ArrayList<String>();
		for (Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() == maxCount) {
				players.add(ps.get(entry.getKey()).getName());
			}
		}
		return players;
	}
}
