package it.polimi.ingsw.model;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    private Student pawn;
    private Board<Student> dest;
    private Board<Student> src;

    @Test
    public void checkPawnMovement() {
        this.pawn = new Student(Color.BLUE);
        this.dest = new Board<>();
        this.src = new Board<>();
        dest.moveInPawn(pawn, src);
        assertTrue(dest.getPawns().contains(pawn));
        assertFalse(src.getPawns().contains(pawn));
    }

    @Test
    public void checkListSize() {
        List<Student> list = new ArrayList<>();
        for (Color c: Color.values()) {
            list.add(new Student(c));
        }

        this.src = new Board<>(list);

        assertEquals(src.getNumPawns(), list.size());

    }
}
