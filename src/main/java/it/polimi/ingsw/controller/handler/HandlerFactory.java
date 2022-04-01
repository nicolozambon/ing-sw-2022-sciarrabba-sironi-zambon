package it.polimi.ingsw.controller.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.card.CharacterCard;
import it.polimi.ingsw.model.component.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.component.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.component.card.MovementCharacterCard;

public class HandlerFactory {

    private CharacterCard card;

    public HandlerFactory(CharacterCard card) {
        this.card = card;
    }

    public Handler buildHandler(Player player) {
        Handler final_handler = null;
        switch (card.getCategory()) {
            case "influence":
                final_handler = new InfluenceModifier(player, (InfluenceCharacterCard) this.card);
                break;
            case "mother_nature":
                final_handler = new MotherNatureModifier(player, (MotherNatureCharacterCard) this.card);
                break;
            case "movement":
                final_handler = new MovementModifier(player, (MovementCharacterCard) this.card);
                break;
            default:
                final_handler = new Handler(player);
        }
        return final_handler;
    }

}
