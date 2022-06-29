package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.TowerColor;

public class Tower extends Pawn<TowerColor> {

    /**
     * Owner of the tower
     */
    private transient Player owner;

    /**
     * Constructor of Tower
     * @param color color of tower
     */
    protected Tower(TowerColor color){
        super(color);
        this.owner = null;
    }

    /**
     * Setter for owner
     * @param owner player that owns the tower
     */
    protected void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Getter for owners
     * @return the owner of the tower
     */
    protected Player getOwner() {
        return owner;
    }

}
