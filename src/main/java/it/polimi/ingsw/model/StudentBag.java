package it.polimi.ingsw.model;

import java.util.List;
import java.util.Random;

public class StudentBag extends Board<Student> {

    protected StudentBag(List<Student> students) {
        super(students);
    }

    /**
     * Extracts a student from the StudentBag and puts it in the destination board.
     * @param destination Board to which the student is to be moved.
     */
    protected void extractStudentAndMove(Board<Student> destination) {
        Random rand = new Random();
        int value = rand.nextInt(this.getNumPawns());
        try {
            destination.moveInPawn(this.getPawns().get(value), this);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Random integer " + value + " is Out Of Bounds.");
        }
    }

}
