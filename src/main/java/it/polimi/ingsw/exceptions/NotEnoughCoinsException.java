package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a Player has not enough coins to play a CharacterCard
 */
public class NotEnoughCoinsException extends Exception {

    /**
     * Constructor with default message
     */
    public NotEnoughCoinsException() {
        super("Not enough coins to play the character card! Retry");
    }
}
