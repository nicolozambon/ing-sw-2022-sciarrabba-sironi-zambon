package it.polimi.ingsw.exceptions;

public class InvalidActionException extends Exception{

    public InvalidActionException(){
        super();
    }

    public InvalidActionException(String message){
        super(message);
    }

}
