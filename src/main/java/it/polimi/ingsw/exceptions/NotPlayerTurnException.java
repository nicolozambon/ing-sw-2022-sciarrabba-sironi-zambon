package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a Player try to play, and it is not his turn
 */
public class NotPlayerTurnException extends Exception {

    /**
     * Constructor with default message
     */
    public NotPlayerTurnException() {
        super("It is not your turn!");
    }
}
