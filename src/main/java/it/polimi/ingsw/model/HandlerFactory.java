package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.List;

public class HandlerFactory {

    private CharacterCard card;

    protected HandlerFactory(CharacterCard card) {
        this.card = card;
    }

    protected Handler buildHandler(List<Player> players) {
        Handler final_handler = null;
        switch (card.getCategory()) {
            case "influence":
                final_handler = new InfluenceHandler(players, (InfluenceCharacterCard) this.card);
                break;
            case "mother_nature":
                final_handler = new MotherNatureHandler(players, (MotherNatureCharacterCard) this.card);
                break;
            case "movement":
                final_handler = new MovementHandler(players, (MovementCharacterCard) this.card);
                break;
            default:
                final_handler = new Handler(players);
        }
        return final_handler;
    }

}
