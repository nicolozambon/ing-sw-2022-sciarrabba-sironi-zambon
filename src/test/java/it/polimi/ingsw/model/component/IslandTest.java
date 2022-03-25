package it.polimi.ingsw.model.component;

import it.polimi.ingsw.model.enums.TowerColor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {


    @Test
    @DisplayName("Test tower movement")
    public void checkTowerMovemnt() {
        Island island = new Island(1, null, null, null);

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

}
