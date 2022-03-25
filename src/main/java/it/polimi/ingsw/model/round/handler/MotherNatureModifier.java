package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.MotherNature;
import it.polimi.ingsw.model.component.card.MotherNatureCharacterCard;

public class MotherNatureModifier extends Handler {

    /* Card has this params:
    private int extraMovement;
    private boolean extraResolving;
    */

    public MotherNatureModifier(Player player, MotherNatureCharacterCard card) {
        super(player);
    }

    @Override
    public void motherNatureMovement(Player player, MotherNature motherNature, int steps_choice) {
        super.motherNatureMovement(player, motherNature, steps_choice);
    }
}
