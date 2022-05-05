package it.polimi.ingsw.exceptions;

public class CloudException extends Exception {
    public CloudException() {
        super("Invalid chosen cloud! Retry");
    }
}
