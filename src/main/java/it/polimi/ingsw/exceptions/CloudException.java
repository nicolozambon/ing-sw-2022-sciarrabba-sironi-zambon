package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when an invalid action involving Clouds is performed
 */
public class CloudException extends Exception {

    /**
     * Constructor with default message
     */
    public CloudException() {
        super("Invalid chosen cloud! Retry");
    }
}
