package it.polimi.ingsw.model;

public class Professor extends Pawn{
    private final Color color;

    /** Professor's constructor
     *
     * @param color Professor's color
     */
    public Professor (Color color){
        this.color = color;
    }

    /**
     *
     * @return the professor's color
     */
    public Color getColor(){
        return this.color;
    }
}
