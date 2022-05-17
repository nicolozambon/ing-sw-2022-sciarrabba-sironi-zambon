package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
                case "influence" -> assertTrue(factory.buildHandler(players, card) instanceof InfluenceHandler);
                case "mother_nature" -> assertTrue(factory.buildHandler(players, card) instanceof MotherNatureHandler);
                case "movement" -> assertTrue(factory.buildHandler(players, card) instanceof MovementHandler);
                default -> assertTrue(factory.buildHandler(players, card) instanceof Handler);
            }

        }

    }
}