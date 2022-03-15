package it.polimi.ingsw.model;

public class Student implements Pawn{
    private final Color color;


    /** Student's constructor
     *
     * @param color Student's color
     */
    public Student (Color color){
        this.color = color;
    }

    /**
     *
     * @return the student's color
     */
    public Color getColor(){
        return this.color;
    }
}
