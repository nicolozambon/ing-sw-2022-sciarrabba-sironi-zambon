package it.polimi.ingsw.exceptions;

public class AssistantCardException extends Exception {
    public AssistantCardException() {
        super("Invalid assistant card played! Retry");
    }
}
