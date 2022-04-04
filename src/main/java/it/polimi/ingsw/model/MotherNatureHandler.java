package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.MotherNatureCharacterCard;

import java.util.List;

public class MotherNatureHandler extends Handler {

    MotherNatureCharacterCard card;

    protected MotherNatureHandler(List<Player> players, MotherNatureCharacterCard card) {
        super(players);
        this.card = card;
    }
}
