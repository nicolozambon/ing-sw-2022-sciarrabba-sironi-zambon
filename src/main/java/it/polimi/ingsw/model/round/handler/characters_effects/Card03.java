package it.polimi.ingsw.model.round.handler.characters_effects;

import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.round.handler.Handler;
import it.polimi.ingsw.model.*;


public class Card03 extends Handler {

    /*
    CARD EFFECT:
    "Choose an Island and resolve the Island as if Mother Nature had ended her movement there.
    Mother Nature will still move and the Island where she ends her movement will also be resolved."
    */

    @Override
    public Island motherNatureMovement(MotherNature motherNature, Island from, Island to, AssistantCard card, int steps_choice) {
        // TODO resolve Island here

        // Then callback superclass method's with "to" as "from" Island
        return super.motherNatureMovement(motherNature, to, card, steps_choice);
    }
}
