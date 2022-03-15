package it.polimi.ingsw.model;

import java.util.List;

public interface Board {

    /**
     *
     * @return return the students on the board
     */
    List<Student> getStudentOnBoard();

    void addStudent(Student student);

}
