package it.polimi.ingsw.model.manager.characters_effects;

import it.polimi.ingsw.model.manager.Manager;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;

public class Card02 extends Manager {

    /*
    CARD EFFECT:
    "During this turn, you take control of any number of Professors even if you have the same number
    of Students as the player who currently controls them."
    */

    @Override
    public int calculatePlayerInfluenceOnIsland(ArrayList<Player> players, Player actual, Island island) {
        Player influencer = null;
        int influence = 0;

        Tower tower = island.getTower();
        if (tower != null) influencer = tower.owner;

        if (actual.equals(influencer)) influence++;
        for (Player player : players) {
            for (Professor professor : player.school.professorsTable.getPawns()) {
                influence = influence + island.countByColor(professor.getColor());
            }
        }
        return influence;
    }

}
