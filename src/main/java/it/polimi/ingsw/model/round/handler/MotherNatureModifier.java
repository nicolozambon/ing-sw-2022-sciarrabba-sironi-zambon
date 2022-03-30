package it.polimi.ingsw.model.round.handler;

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
    public void motherNatureMovement(Player player, MotherNature motherNature, int stepsChoice) {
        Island destination = null;
        if (stepsChoice <= (player.getLastAssistantCard().getSteps() + this.card.getExtraMovement())) {
            motherNature.stepsToMove(stepsChoice);
        }
    }

    @Override
    public void extraAction(Player player, MotherNature motherNature, int stepsChoice) {
        if (this.card.isExtraResolving()) {
            // TODO: correct the extraResolving part.
            super.motherNatureMovement(player, motherNature, stepsChoice);
        }
    }
}
