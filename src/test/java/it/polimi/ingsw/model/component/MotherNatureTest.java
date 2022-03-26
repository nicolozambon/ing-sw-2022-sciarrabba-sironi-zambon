package it.polimi.ingsw.model.component;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureTest {


    @Test
    void getPosition() {
        Island island1 = new Island(1, new ArrayList<>());
        MotherNature montherNature = new MotherNature(island1);
        assertEquals(montherNature.getPosition(), island1);
    }

    @Test
    void stepsToMove() {
        Island island1 = new Island(1, new ArrayList<>());
        Island island2 = new Island(2, new ArrayList<>());
        Island island3 = new Island(3, new ArrayList<>());
        island1.setNextIsland(island2);
        island2.setNextIsland(island3);
        MotherNature montherNature = new MotherNature(island1);
        montherNature.stepsToMove(2);
        assertEquals(montherNature.getPosition(), island3);
    }
}