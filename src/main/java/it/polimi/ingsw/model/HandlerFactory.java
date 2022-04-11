package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.List;

public class HandlerFactory {

    protected Handler buildHandler(List<Player> players, CharacterCard card) {
        Handler final_handler = null;
        switch (card.getCategory()) {
            case "influence":
                final_handler = new InfluenceHandler(players, new InfluenceCharacterCard(card));
                break;
            case "mother_nature":
                final_handler = new MotherNatureHandler(players, new MotherNatureCharacterCard(card));
                break;
            case "movement":
                final_handler = new MovementHandler(players, new MovementCharacterCard(card));
                break;
            default:
                final_handler = new Handler(players);
        }
        return final_handler;
    }

}
