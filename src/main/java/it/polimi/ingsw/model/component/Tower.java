package it.polimi.ingsw.model.component;

import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.Player;

public class Tower extends Pawn<TowerColor>{

    private Player owner;

    public Tower (TowerColor color){
        super(color);
        this.owner = null;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        Player owner = null;
        // TODO: create the method that returns the owner of the tower
        return owner;
    }

}
