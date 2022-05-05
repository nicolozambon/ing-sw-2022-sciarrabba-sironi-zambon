package it.polimi.ingsw.exceptions;

public class NotEnoughCoinsException extends Exception {
    public NotEnoughCoinsException() {
        super("Not enough coins to play th character card! Retry");
    }
}
