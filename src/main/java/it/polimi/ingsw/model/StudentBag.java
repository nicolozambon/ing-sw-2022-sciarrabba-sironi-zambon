package it.polimi.ingsw.model;

import java.util.List;
import java.util.Random;

public class StudentBag extends Board<Student> {

    /**
     * Constructor of StudentBag
     * @param students list of students
     */
    protected StudentBag(List<Student> students) {
        super(students);
    }

    /**
     * Extracts a student from the StudentBag and puts it in the destination board.
     * @param destination Board to which the student is to be moved.
     */
    protected void extractStudentAndMove(Board<Student> destination) {
        Random rand = new Random();
        if (this.getNumPawns() > 0) {
            int value = rand.nextInt(this.getNumPawns());
            destination.moveInPawn(this.getPawns().get(value), this);
        } else throw new IndexOutOfBoundsException();
    }

}
