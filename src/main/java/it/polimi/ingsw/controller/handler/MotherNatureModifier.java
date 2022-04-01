package it.polimi.ingsw.controller.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.Island;
import it.polimi.ingsw.model.component.MotherNature;
import it.polimi.ingsw.model.component.card.MotherNatureCharacterCard;

public class MotherNatureModifier extends Handler {

    /* Card has this params:
    private int extraMovement;
    private boolean extraResolving;
    */

    private final MotherNatureCharacterCard card;

    public MotherNatureModifier(Player player, MotherNatureCharacterCard card) {
        super(player);
        this.card = card;
    }

    @Override
    public void motherNatureMovement(MotherNature motherNature, int stepsChoice) {
        Island destination = null;
        if (stepsChoice <= (this.getActualPlayer().getLastAssistantCard().getSteps() + this.card.getExtraMovement())) {
            motherNature.stepsToMove(stepsChoice);
        }
    }

    @Override
    public void extraAction(Object obj) {
        if (this.card.isExtraResolving()) {
            // TODO: correct the extraResolving part.
        }
    }
}
