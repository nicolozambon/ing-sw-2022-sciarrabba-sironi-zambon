package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TowerColor;

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
