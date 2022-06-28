package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when an invalid action involving Islands is performed
 */
public class IslandException extends Exception {

    /**
     * Constructor with default message
     */
    public IslandException() {
        super("Invalid chosen island! Retry");
    }
}
