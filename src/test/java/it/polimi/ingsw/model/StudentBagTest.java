package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


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
