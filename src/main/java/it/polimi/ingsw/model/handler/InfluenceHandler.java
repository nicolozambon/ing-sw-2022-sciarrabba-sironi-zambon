package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.card.InfluenceCharacterCard;

import java.util.List;

public class InfluenceHandler extends Handler {

    InfluenceCharacterCard card;

    public InfluenceHandler(List<Player> players, InfluenceCharacterCard card) {
        super(players);
        this.card = card;
    }
}
