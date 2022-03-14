package it.polimi.ingsw.model;

public abstract class Pawn {
    private Board position;

    public Pawn (){

    }

    /** Get pawn's position
     *
     * @return Position of the Pawn object
     */
    public Board getPosition(){
        return position;
    }
}
