package tud.game;

public class Player {

	/**
	 * name of the player
	 */
	private String name;

	/**
	 * internal representation of player
	 */
	private String playerName;
	/**
	 * id of player
	 */
	private int playerId;
	/**
	 * count of created player objects
	 */
	private static int count = 1;
	{
		playerName = "p" + count;
		playerId = count;
		count++;
	}

	// hier speichern, ob KI oder Spieler

	public Player(String name) {
		this.name = name;
	}

	/**
	 * returns player name
	 * 
	 * @return palyer name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns internal player name
	 * 
	 * @return internal player name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * returns player id
	 * @return player id
	 */
	public int getPlayerId() {
		return playerId;
	}
}
