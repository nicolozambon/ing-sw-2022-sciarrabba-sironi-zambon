package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.List;

public class MovementHandler extends Handler {

    MovementCharacterCard card;

    protected MovementHandler(List<Player> players, MovementCharacterCard card) {
        super(players);
        this.card = card;
    }

}
