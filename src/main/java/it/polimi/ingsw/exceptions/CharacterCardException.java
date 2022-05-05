package it.polimi.ingsw.exceptions;

public class CharacterCardException extends Exception {
    public CharacterCardException() {
        super("Invalid character card played! Retry");
    }
}
