package it.polimi.ingsw.model;

import java.util.*;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.StudentBag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class StudentBagTest {

    @Test
    void extractStudentAndMoveTest() {
        Board<Student> destination = new Board<Student>();
        List<Student> studentBagList = new ArrayList<Student>();

        for (int i = 0; i < 26; i++) {
            studentBagList.add(new Student(Color.GREEN));
            studentBagList.add(new Student(Color.BLUE));
            studentBagList.add(new Student(Color.RED));
            studentBagList.add(new Student(Color.PINK));
            studentBagList.add(new Student(Color.YELLOW));
        }

        StudentBag studentBag = new StudentBag(studentBagList);

        studentBag.extractStudentAndMove(destination);

        assertTrue(destination.getNumPawns() == 1);
        assertTrue(studentBag.getNumPawns() == 129);


    }
}
