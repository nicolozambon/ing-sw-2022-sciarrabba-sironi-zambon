package it.polimi.ingsw.model;

public class Tower extends Pawn<TowerColor>{

    public final Player owner;

    /*public Tower (TowerColor color){
        super(color);
    }*/

    public Tower (Player owner, TowerColor color){
        super(color);
        this.owner = owner;
    }

}
