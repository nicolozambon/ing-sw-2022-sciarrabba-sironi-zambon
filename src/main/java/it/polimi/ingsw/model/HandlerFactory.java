package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.List;

public class HandlerFactory {

    public Handler buildHandler(List<Player> players, CharacterCard card) {
        return switch (card.getCategory()) {
            case "influence" -> new InfluenceHandler(players, new InfluenceCharacterCard(card));
            case "mother_nature" -> new MotherNatureHandler(players, new MotherNatureCharacterCard(card));
            case "movement" -> new MovementHandler(players, new MovementCharacterCard(card));
            default -> new Handler(players);
        };
    }

    public Handler buildHandler(List<Player> players) {
        return new Handler(players);
    }

}
