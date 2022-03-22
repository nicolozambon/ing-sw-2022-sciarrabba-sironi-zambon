package it.polimi.ingsw.model.round.handler.characters_effects;

import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.round.handler.Handler;
import it.polimi.ingsw.model.*;

public class Card04 extends Handler {

    /*
    CARD EFFECT:
    "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played."
    */

    @Override
    public Island motherNatureMovement(MotherNature motherNature, Island from, AssistantCard card, int steps_choice) {
        Island destination = null;
        if (steps_choice <= card.getSteps() + 2) {
            while (steps_choice > 0) {
                destination = from.getNextIsland();
                steps_choice--;
            }
        }
        motherNature.setPosition(destination);
        return destination;
    }

}
