package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.card.CharacterCard;
import it.polimi.ingsw.model.component.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.component.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.component.card.MovementCharacterCard;

public class HandlerFactory {

    private CharacterCard card;

    public void HandlerFactory(CharacterCard card) {
        this.card = card;
    }

    public Handler buildHandler(Player player) {
        Handler final_handler = null;
        switch (card.getCategory()) {
            case null:
                final_handler = new Handler(player);
            case "influence":
                final_handler = new InfluenceModifier(player, (InfluenceCharacterCard) this.card);
            case "mother_nature":
                final_handler = new MotherNatureModifier(player, (MotherNatureCharacterCard) this.card);
            case "movement":
                final_handler = new MovementModifier(player, (MovementCharacterCard) this.card);
        }
        return final_handler;
    }

}
