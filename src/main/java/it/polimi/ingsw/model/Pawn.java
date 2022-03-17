package it.polimi.ingsw.model;

public abstract class Pawn<T>{

    private final T color;

    public Pawn(T color){
        this.color = color;
    }

    public T getColor(){
        return color;
    }

}
