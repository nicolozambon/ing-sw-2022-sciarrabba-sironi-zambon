package it.polimi.ingsw.model.component;

import java.util.*;
import it.polimi.ingsw.enums.TowerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {


    @Test
    public void checkTowerMovement() {
        Island island = new Island(1, new ArrayList<>());

        Tower towerBlack = new Tower(TowerColor.BLACK);
        Tower towerWhite = new Tower(TowerColor.WHITE);
        Tower towerGrey = new Tower(TowerColor.GREY);

        island.setTower(towerBlack);
        assertEquals(towerBlack, island.getTower());

        island.setTower(towerWhite);
        assertEquals(towerWhite, island.getTower());

        island.setTower(towerGrey);
        assertEquals(towerGrey, island.getTower());
    }

    @Test
    public void checkSetNextPrevIsland() {
        Island island1 = new Island(1, new ArrayList<>());
        Island island2 = new Island(2, new ArrayList<>());

        island1.setNextIsland(island2);
        island2.setPrevIsland(island1);

        assertEquals(island1.getNextIsland(), island2);
        assertEquals(island2.getPrevIsland(), island1);
    }



}
