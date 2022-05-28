package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MotherNatureHandlerTest {
    Model model;
    Handler handler;

    @BeforeEach
    void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        List<String> playersNames = new ArrayList<>();

        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames, true);
        handler = new HandlerFactory().buildHandler(model.getPlayers());
    }

    @Test
    void motherNatureMovement() throws Exception {
        Island island0 = model.getMotherNature().getPosition();
        Island island2 = island0.getNextIsland().getNextIsland();

        model.playAssistantCard(0, 1);
        model.playCharacterCard(0, 3);

        model.moveMotherNature(0, 2);

        assertTrue(model.getMotherNature().getPosition().equals(island2));
    }

    @Test
    void card02Test() throws Exception {
        model.getPlayers().get(0).increaseCoinBy(10);
        model.playCharacterCard(0, 2);
        model.playAssistantCard(0, 6);
        model.extraAction(0,2);
        assertTrue(model.getHandler() instanceof MotherNatureHandler);
    }
}