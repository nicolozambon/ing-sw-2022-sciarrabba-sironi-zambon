package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a Player has won the game
 */
public class WinnerException extends Exception {

    /**
     * ID of the player who has won the game
     */
    private final int playerId;

    /**
     * Constructor with the player's id
     * @param playerId - the player's id
     */
    public WinnerException(int playerId) {
        super();
        this.playerId = playerId;
    }

    /**
     * Returns the ID of the player who has won the game
     * @return the ID of the player who has won the game
     */
    public int getPlayerId() {
        return playerId;
    }
}
