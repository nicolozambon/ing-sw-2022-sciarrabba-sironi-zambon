package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfessorTest {

    @Test
    public void getColor() {
        Professor prof;
        for (Color c : Color.values()) {
            prof = new Professor(c);
            assertEquals(prof.getColor(), c);
        }
    }

}