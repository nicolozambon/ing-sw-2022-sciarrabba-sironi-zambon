package it.polimi.ingsw.model;

public class Tower{
    private final TowerColor color;

    /** Tower's constructor
     *
     * @param color Tower's color
     */
    public Tower (TowerColor color){
        this.color = color;
    }


    /**
     *
     * @return the tower's color
     */
    public TowerColor getColor(){
        return this.color;
    }
}
