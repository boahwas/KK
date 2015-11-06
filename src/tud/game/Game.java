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
		
		Modus modus;
		Map<String, Player> players;
		
		// Abfrage nach Feldgroesse
		int boardSize = setBoardSize(scan);
		Board board = new Board(boardSize);
		
		KI ki = new KI(board);
		MiniMax mini = new MiniMax();
		
		// oder Spiel mit KI Auswahl ob random oder minimax
		
		modus = setModus(scan);
		
		if(modus == Modus.KI){
			players = createKIGame(scan, board, true);
		}else{
			if(modus == Modus.KIHARD){
				players = createKIGame(scan, board, false);
			}
			else{
			// Variable Spieler 
			int playerCount = setPlayerCount(scan);
			players = createPlayers(scan, playerCount);
			}
		}
		Player currPlayer = players.get("p1");
		while (true) {
			board.printBoard();
			System.out.println("It is your turn " + currPlayer.getName() + "(" + currPlayer.getPlayerName() + "):");
			String turn = checkTurn(scan, board, currPlayer, ki, mini, players);
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
	private static String checkTurn(Scanner scan, Board board, Player currPlayer, KI ki, MiniMax mini, Map<String,Player> players) {
		
		String turn;
		if(currPlayer.getName().equals("kiGegner")){
			ki.updateBoard();
			turn = ki.randomMove().toString();
		}else if(currPlayer.getName().equals("HardKiGegner")){
			turn = String.valueOf(mini.findMove(currPlayer, nextPlayer(players, currPlayer), board));
		}
		else{
			if(containsCom(players))
			{
				System.out.println("Für einen Tipp (t) eingeben");
			}
			turn = scan.next();
			// Wenn Spieler dran ist, abfragen ob er hilfe betaetigt hat.
			while (!turn.matches("[1-9][0-9]*") || !board.checkRules(Integer.valueOf(turn))) {
				if(containsCom(players)){
					if(turn.equals("t")){
						System.out.println(mini.findMove(currPlayer, nextPlayer(players, currPlayer), board));
					}
				}else{
					System.out.println("Your turn is not valid! Please make another.");
				}
				turn = scan.next();
			}
		}
		return turn;
	}
	
	/**
	 * checks for com players
	 * @param players
	 * @return boolean if ki contains players
	 */
	static boolean containsCom(Map<String, Player> players){
		for(Player p: players.values()){
			if(p.getName().equals("kiGegner") || p.getName().equals("HardKiGegner")){
				return true;
			}
		}
		return false;
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
	 * Creates a KI-game
	 * @param scan - input scanner
	 * @param board - reference of the game board
	 * @return a map of the players
	 * @author Daniel Strippel, Philip Stauder
	 */
	static Map<String, Player> createKIGame(Scanner scan, Board board, boolean easy) {
		Map<String, Player> players = new HashMap<String, Player>();
		
			System.out.println("Player what is your name?");
			System.out.print("Name:");
			String pstr = scan.next();
			Player p = new Player(pstr);
			players.put(p.getPlayerName(), p);
			System.out.println("Welcome " + pstr);
			Player kiPlayer;
			if(easy){
				kiPlayer= new Player("kiGegner");
			}
			else{
				kiPlayer= new Player("HardKiGegner");
			}
			players.put(kiPlayer.getPlayerName(), kiPlayer);

		System.out.println("---------------------------------------------------------------");

		return players;
	}
	/**
	 * sets the number of players
	 * @param scan - input scanner
	 * @return number of players
	 * @author Philip Stauder
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
	 * @author Philip Stauder
	 */
	private static int setBoardSize(Scanner scan) {
		
		System.out.println("Wie große soll das Feld sein?\nGröße:\n");
		String size = scan.next();
		return Integer.valueOf(size);
	}
	
	/**
	 * Set the game mode (ki, kiHard, multiplayer)
	 * @param scan - input scanner
	 * @return mod game modus
	 * @author Daniel Strippel, Philip Stauder
	 */
	private static Modus setModus(Scanner scan){
		
		System.out.println("Wollen sie gegen den Computer spielen?(j/n)");
		String m = scan.next();
		while(!m.equals("j") && !m.equals("n")){
			System.out.println("Falsche Eingabe! Bitte mit (j) oder (n) antworten.");
			m = scan.next();
		}
		Modus mod = (m.equals("j"))?Modus.KI:Modus.MULTIPLAYER;
		if(mod.equals(Modus.KI)){
			System.out.println("Einfach oder schwer (e/s)");
			m = scan.next();
			while(!m.equals("e") && !m.equals("s")){
				System.out.println("Falsche Eingabe! Bitte mit (j) oder (n) antworten.");
				m = scan.next();
			}
			if(m.equals("s")){
				mod = Modus.KIHARD;
			}
		}
		return mod;
	}

}
