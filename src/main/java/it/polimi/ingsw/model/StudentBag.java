package it.polimi.ingsw.model;

import java.util.Random;

public class StudentBag extends Board<Student> {

    public StudentBag() {
        for (int j = 0; j < 26; j++) {
            this.addPawn(new Student(Color.BLUE));
            this.addPawn(new Student(Color.GREEN));
            this.addPawn(new Student(Color.PINK));
            this.addPawn(new Student(Color.RED));
            this.addPawn(new Student(Color.YELLOW));
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
            this.movePawn(getPawn(value), this, destination);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Random integer " + value + " is Out Of Bounds.");
        }
    }
}
