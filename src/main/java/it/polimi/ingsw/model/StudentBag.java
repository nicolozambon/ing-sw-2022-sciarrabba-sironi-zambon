package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.util.Random;

public class StudentBag extends Board<Student> {

    /**
     * Extracts a student from the StudentBag and puts it in the destination board.
     * @param destination Board to which the student is to be moved.
     */
    public void extractStudentAndMove(Board<Student> destination) {
        Random rand = new Random();
        int value = rand.nextInt(130);
        try {
            destination.moveInPawn(value, this);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Random integer " + value + " is Out Of Bounds.");
        }
    }

}
