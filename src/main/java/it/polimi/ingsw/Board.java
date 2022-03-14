package it.polimi.ingsw;

import java.util.ArrayList;

public abstract class Board {
    int ID;
    /**
     *
     * @return return the students on the board
     */
    public ArrayList<Student> getStudentOnBoard() {
        ArrayList<Student> student = null;

        return student;
    }

    /**
     *
     * @return the ID of the board.
     */
    public int getID() {
        return ID;
    }
}
