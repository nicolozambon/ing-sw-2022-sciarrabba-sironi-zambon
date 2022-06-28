package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a generic invalid action is performed
 */
public class InvalidActionException extends Exception{

    /**
     * Constructor without message
     */
    public InvalidActionException(){
        super();
    }

    /**
     * Constructor with custom message
     * @param message the custom message
     */
    public InvalidActionException(String message){
        super(message);
    }

}
