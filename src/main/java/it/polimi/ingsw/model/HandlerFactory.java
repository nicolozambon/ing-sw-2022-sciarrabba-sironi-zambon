package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.List;

/**
 * Factory to build different Handler based on the CharacterCard played
 */
public class HandlerFactory {

    /**
     * Build handler based on the specified card and list of players
     * @param players the list of players
     * @param card the CharacterCard
     * @return the handler based on the specified parameters
     */
    public Handler buildHandler(List<Player> players, CharacterCard card) {
        return switch (card.getCategory()) {
            case "influence" -> new InfluenceHandler(players, new InfluenceCharacterCard(card));
            case "mother_nature" -> new MotherNatureHandler(players, new MotherNatureCharacterCard(card));
            case "movement" -> new MovementHandler(players, new MovementCharacterCard(card));
            default -> new Handler(players);
        };
    }

    /**
     * Build default handler with the specified list of players
     * @param players the list of players
     * @return the default handler
     */
    public Handler buildHandler(List<Player> players) {
        return new Handler(players);
    }

}
