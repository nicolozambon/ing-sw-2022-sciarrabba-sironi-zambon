package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;

public class Cloud implements Board {

    private List<Student> students;

    public Cloud(List<Student> students) {
        this.students = new ArrayList<Student>(students);

    }

    @Override
    public List<Student> getStudentOnBoard() {
        return new ArrayList<Student>(this.students);
    }

    @Override
    public void addStudent(Student student){

    }
}
