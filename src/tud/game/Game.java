package tud.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Game {

	/**
	 * begin of game
	 * @param args - not needed parameter array
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		// Variable Spieler 
		int playerCount = setPlayerCount(scan);
		Map<String, Player> players = createPlayers(scan, playerCount);
		
		// Abfrage nach Feldgroesse
		int boardSize = setBoardSize(scan);
		Board board = new Board(boardSize);
				
		// oder Spiel mit KI Auswahl ob random oder minimax
		
		Player currPlayer = players.get("p1");
		while (true) {
			board.printBoard();
			System.out.println("It is your turn " + currPlayer.getName() + "(" + currPlayer.getPlayerName() + "):");
			String turn = checkTurn(scan, board, currPlayer);
			if (board.nextAction(Integer.valueOf(turn), currPlayer.getPlayerName()).equals(Action.NextTurn)) {
				currPlayer = nextPlayer(players, currPlayer);
			}
			if (board.isEnded()) {
				board.printBoard();
				board.showScore(players);
				break;
			}
			System.out.println("---------------------------------------------------------------");
		}
		scan.close();
	}
	/**
	 * returns the next player in the game
	 * @param players - map of internal player names to players
	 * @param currPlayer - current player
	 * @return next player in the row, if current player is the last it returns player 1
	 */
	private static Player nextPlayer(Map<String, Player> players, Player currPlayer) {
		return players.get("p" + ((currPlayer.getPlayerId() % players.size()) + 1));
	}

	/**
	 * checks if the input is a valid turn
	 * @param scan - input scanner
	 * @param board - game board
	 * @param currPlayer - current player
	 * @return valid turn
	 */
	private static String checkTurn(Scanner scan, Board board, Player currPlayer) {
		// Ist Spieler oder KI dran, wenn die KI spielt dann Zug berechnen.
		String turn = scan.next();
		// Wenn Spieler dran ist, abfragen ob er hilfe betaetigt hat.
		while (!turn.matches("[1-9][0-9]*") || !board.checkRules(Integer.valueOf(turn))) {
			System.out.println("Your turn is not valid! Please make another.");
			turn = scan.next();
		}
		return turn;
	}
	/**
	 * creates players and asks for their names
	 * @param scan - input scanner
	 * @param playerCount - count of how many players should be created
	 * @return - map of internal player names to players
	 */
	private static Map<String, Player> createPlayers(Scanner scan, int playerCount) {

		Map<String, Player> players = new HashMap<String, Player>();
		for (int i = 0; i < playerCount; i++) {
			System.out.println("Player " + (i + 1) + " what is your name?");
			System.out.print("Name:");
			String pstr = scan.next();
			Player p = new Player(pstr);
			players.put(p.getPlayerName(), p);
			System.out.println("Welcome " + pstr);
		}

		System.out.println("---------------------------------------------------------------");

		return players;
	}
	/**
	 * sets the number of players
	 * @param scan - input scanner
	 * @return number of players
	 */
	private static int setPlayerCount(Scanner scan) {
		
		System.out.println("Wieviele Spieler gibt es?\nAnzahl:\n");
		String count = scan.next();
		return Integer.valueOf(count);
	}
	/**
	 * sets the size of the board
	 * @param scan - input scanner
	 * @return size of the board
	 */
	private static int setBoardSize(Scanner scan) {
		
		System.out.println("Wie groß soll das Feld sein?\nGröße:\n");
		String size = scan.next();
		return Integer.valueOf(size);
	}

}
