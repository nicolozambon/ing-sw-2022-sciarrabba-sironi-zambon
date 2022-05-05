package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.model.Island;

public class IslandException extends Exception {
    public IslandException() {
        super("Invalid chosen island! Retry");
    }
}
