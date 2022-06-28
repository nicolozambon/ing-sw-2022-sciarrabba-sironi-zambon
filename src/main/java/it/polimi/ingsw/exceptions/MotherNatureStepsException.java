package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when an invalid number of steps involving MotherNature is performed
 */
public class MotherNatureStepsException extends Exception {

    /**
     * Constructor with default message
     */
    public MotherNatureStepsException() {
        super("Invalid mother nature steps! Retry");
    }
}
