package it.polimi.ingsw.model.manager;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.AssistantCard;

import java.util.ArrayList;


public class Manager {

    public Manager() {

    }

    public int calculatePlayerInfluenceOnIsland(ArrayList<Player> players, Player actual, Island island) {
        return 0;
    }

    public void professorControl() {

    }

    public Island motherNatureMovement(MotherNature motherNature, Island from, AssistantCard card, int steps_choice) {
        Island destination = null;
        if (steps_choice <= card.getSteps()) {
            while (steps_choice > 0) {
                destination = from.nextIsland();
                steps_choice--;
            }
        }
        motherNature.setPosition(destination);
        return destination;
    }



}