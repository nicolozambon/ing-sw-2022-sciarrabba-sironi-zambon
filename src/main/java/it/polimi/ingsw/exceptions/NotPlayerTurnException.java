package it.polimi.ingsw.exceptions;

public class NotPlayerTurnException extends Exception {
    public NotPlayerTurnException() {
        super("It is not your turn!");
    }
}
