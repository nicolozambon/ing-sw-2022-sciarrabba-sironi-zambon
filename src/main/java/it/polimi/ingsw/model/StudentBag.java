package it.polimi.ingsw.model;

import java.util.Random;

public class StudentBag extends Board {

    public StudentBag() {
        for (int j = 0; j < 26; j++) {
            this.addPawn(new Student(Color.BLUE));
        }

        for (int j = 0; j < 26; j++) {
            this.addPawn(new Student(Color.GREEN));
        }

        for (int j = 0; j < 26; j++) {
            this.addPawn(new Student(Color.PINK));
        }

        for (int j = 0; j < 26; j++) {
            this.addPawn(new Student(Color.RED));
        }

        for (int j = 0; j < 26; j++) {
            this.addPawn(new Student(Color.YELLOW));
        }
    }


    /**
     * Extracts a student from the StudentBag and puts it in the destination board.
     * @param destination Board to which the student is to be moved.
     */
    void extractStudentAndMove(Board destination) {
        Random rand = new Random();
        int value = rand.nextInt(131);
        try {
            this.movePawn(getPawn(value), this, destination);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Random integer " + value + " is Out Of Bounds.")
        }
    }
}
