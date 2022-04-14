package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;
import it.polimi.ingsw.model.card.MovementCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandlerFactoryTest {

    Model model;
    @BeforeEach
    void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        List<String> playersNames = new ArrayList<>();
        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames);
    }

    @Test
    void buildHandler() {
        HandlerFactory factory = new HandlerFactory();
        List<Player> players = model.getPlayers();
        assertTrue(factory.buildHandler(players) instanceof Handler);
        for (CharacterCard card : model.getCharacterCards()) {
            switch (card.getCategory()) {
                case "influence":
                    assertTrue(factory.buildHandler(players, card) instanceof InfluenceHandler);
                    break;
                case "mother_nature":
                    assertTrue(factory.buildHandler(players, card) instanceof MotherNatureHandler);
                    break;
                case "movement":
                    assertTrue(factory.buildHandler(players, card) instanceof MovementHandler);
                    break;
                default:
                    assertTrue(factory.buildHandler(players, card) instanceof Handler);
                    break;
            }

        }

    }
}