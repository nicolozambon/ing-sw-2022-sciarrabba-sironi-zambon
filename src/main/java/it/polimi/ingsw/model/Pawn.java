package it.polimi.ingsw.model;

public abstract class Pawn<T>{

    private final T color;

    protected Pawn(T color){
        this.color = color;
    }

    protected T getColor(){
        return color;
    }

}
