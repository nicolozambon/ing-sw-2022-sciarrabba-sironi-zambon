package it.polimi.ingsw.model.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentBag extends Board<Student> {

    public StudentBag(List<Student> students) {
        super(students);
    }

    /**
     * Extracts a student from the StudentBag and puts it in the destination board.
     * @param destination Board to which the student is to be moved.
     */
    public void extractStudentAndMove(Board<Student> destination) {
        Random rand = new Random();
        int value = rand.nextInt(130);
        try {
            destination.moveInPawn(this.getPawns().get(value), this);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Random integer " + value + " is Out Of Bounds.");
        }
    }

}
