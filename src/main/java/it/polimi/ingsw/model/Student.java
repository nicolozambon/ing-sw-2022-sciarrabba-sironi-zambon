package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;

public class Student extends Pawn<Color>{

    /** Student's constructor
     *
     * @param color Student's color
     */
    protected Student (Color color){
        super(color);
    }
}