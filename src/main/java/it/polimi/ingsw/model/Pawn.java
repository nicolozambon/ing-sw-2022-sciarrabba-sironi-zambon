package it.polimi.ingsw.model;

public abstract class Pawn<T>{

    /**
     * Color og the pawn
     */
    private final T color;

    /**
     * Constructor of Pawn
     * @param color color of the pawn
     */
    protected Pawn(T color){
        this.color = color;
    }

    /**
     * Getter of the color
     * @return color
     */
    protected T getColor(){
        return color;
    }

}
