package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.card.MovementCharacterCard;

import java.util.List;

public class MovementHandler extends Handler {

    MovementCharacterCard card;

    public MovementHandler(List<Player> players, MovementCharacterCard card) {
        super(players);
        this.card = card;
    }
}
