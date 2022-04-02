package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.card.MotherNatureCharacterCard;

import java.util.List;

public class MotherNatureHandler extends Handler {

    MotherNatureCharacterCard card;

    public MotherNatureHandler(List<Player> players, MotherNatureCharacterCard card) {
        super(players);
        this.card = card;
    }
}
