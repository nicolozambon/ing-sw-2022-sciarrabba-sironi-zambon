package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.MotherNature;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
        island1.setNextIsland(island2);
        island2.setNextIsland(island3);
        MotherNature montherNature = new MotherNature(island1);
        montherNature.stepsToMove(2);
        assertEquals(montherNature.getPosition(), island3);
    }
}