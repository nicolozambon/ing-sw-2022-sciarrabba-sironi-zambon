package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class StudentBag extends Board<Student> {

    public StudentBag() {
        super();
        for (int j = 0; j < 26; j++) {
            this.pawns.add(new Student(Color.BLUE));
            this.pawns.add(new Student(Color.GREEN));
            this.pawns.add(new Student(Color.PINK));
            this.pawns.add(new Student(Color.RED));
            this.pawns.add(new Student(Color.YELLOW));
        }

    }

    /**
     * Extracts a student from the StudentBag and puts it in the destination board.
     * @param destination Board to which the student is to be moved.
     */
    void extractStudentAndMove(Board<Student> destination) {
        Random rand = new Random();
        int value = rand.nextInt(130);
        try {
            destination.moveInPawn(getPawn(value), this);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Random integer " + value + " is Out Of Bounds.");
        }
    }
}
