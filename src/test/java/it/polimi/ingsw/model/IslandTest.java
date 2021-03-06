package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {


    @Test
    void checkSetNextPrevIsland() {
        Island island1 = new Island(1);
        Island island2 = new Island(2);

        island1.setNextIsland(island2);
        island2.setPrevIsland(island1);

        assertEquals(island1.getNextIsland(), island2);
        assertEquals(island2.getPrevIsland(), island1);

        assertFalse(island1.isUnifyNext());
        assertFalse(island2.isUnifyPrev());
        island1.unifyToNext();
        island2.unifyToPrev();
        assertTrue(island1.isUnifyNext());
        assertTrue(island2.isUnifyPrev());
    }

    @Test
    void countStudentsByColorTest() {
        Island island1 = new Island(1);
        Island island2 = new Island(2);
        Student student1 = new Student(Color.GREEN);
        Student student2 = new Student(Color.GREEN);
        Student student3 = new Student(Color.BLUE);
        ArrayList<Student> arrayList = new ArrayList<>();
        arrayList.add(student1);
        arrayList.add(student2);
        arrayList.add(student3);

        Board<Student> board1 = new Board<>(arrayList);


        island1.moveInPawn(student1, board1);
        island1.moveInPawn(student2, board1);
        island1.moveInPawn(student3, board1);

        int numOfGreens = island1.countStudentsByColor(Color.GREEN);
        int numOfBlues = island1.countStudentsByColor(Color.BLUE);

        assertEquals(numOfGreens, 2);
        assertEquals(numOfBlues, 1);

        island2.moveInPawn(student1, island1);
        numOfGreens = island1.countStudentsByColor(Color.GREEN);
        assertEquals(numOfGreens, 1);
    }
}
