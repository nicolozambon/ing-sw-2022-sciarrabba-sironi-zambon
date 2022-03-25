package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.card.MovementCharacterCard;

import java.util.List;

public class MovementModifier extends Handler {

    /* Card has this params:
    private int extraControl;
    private int possibleExchange;
    private int numOfStudentsToReturn;
    */

    public MovementModifier(Player player, MovementCharacterCard card) {
        super(player);
    }

    @Override
    public void professorControl(List<Player> otherPlayers) {
        super.professorControl(otherPlayers);
    }

    @Override
    public void extraAction() {
        super.extraAction();
    }
}
