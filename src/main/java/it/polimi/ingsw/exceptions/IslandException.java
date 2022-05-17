package it.polimi.ingsw.exceptions;

public class IslandException extends Exception {
    public IslandException() {
        super("Invalid chosen island! Retry");
    }
}
