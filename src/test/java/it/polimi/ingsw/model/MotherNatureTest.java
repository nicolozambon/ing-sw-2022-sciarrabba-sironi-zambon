package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MotherNatureTest {


    @Test
    void getPosition() {
        Island island1 = new Island(1);
        MotherNature montherNature = new MotherNature(island1);
        assertEquals(montherNature.getPosition(), island1);
    }

    @Test
    void stepsToMove() {
        Island island1 = new Island(1);
        Island island2 = new Island(2);
        Island island3 = new Island(3);
        assertFalse(island1.equals(island3));
        assertFalse(island1.equals(island2));
        assertFalse(island3.equals(island2));
        island1.setNextIsland(island2);
        island1.setPrevIsland(island3);
        island2.setPrevIsland(island1);
        island2.setNextIsland(island3);
        island3.setPrevIsland(island2);
        island3.setNextIsland(island1);
        MotherNature montherNature = new MotherNature(island1);
        montherNature.stepsToMove(2);
        assertEquals(montherNature.getPosition(), island3);
    }
}