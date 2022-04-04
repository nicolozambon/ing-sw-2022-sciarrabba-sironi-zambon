package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.InfluenceCharacterCard;

import java.util.List;

public class InfluenceHandler extends Handler {

    InfluenceCharacterCard card;

    protected InfluenceHandler(List<Player> players, InfluenceCharacterCard card) {
        super(players);
        this.card = card;
    }
}
