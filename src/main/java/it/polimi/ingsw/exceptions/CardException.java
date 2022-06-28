package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when an invalid action involving Cards is performed
 */
public class CardException extends Exception {

    /**
     * Constructor without message
     */
    public CardException() {
        super();
    }

    /**
     * Constructor with custom message
     * @param message the custom message
     */
    public CardException (String message) {
        super(message);
    }
}
