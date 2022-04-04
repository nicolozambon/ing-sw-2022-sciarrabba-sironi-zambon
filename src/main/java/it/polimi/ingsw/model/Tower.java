package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.TowerColor;

public class Tower extends Pawn<TowerColor>{

    private Player owner;

    protected Tower (TowerColor color){
        super(color);
        this.owner = null;
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
    }

    protected Player getOwner() {
        return owner;
    }

}
