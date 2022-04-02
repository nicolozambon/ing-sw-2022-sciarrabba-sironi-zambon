package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.card.CharacterCard;
import it.polimi.ingsw.model.component.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.component.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.component.card.MovementCharacterCard;

import java.util.List;

public class HandlerFactory {

    private CharacterCard card;

    public HandlerFactory(CharacterCard card) {
        this.card = card;
    }

    public Handler buildHandler(List<Player> players) {
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
