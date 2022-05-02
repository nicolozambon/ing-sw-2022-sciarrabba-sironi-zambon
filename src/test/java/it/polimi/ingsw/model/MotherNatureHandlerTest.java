package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.exceptions.InvalidMotherNatureStepsException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        model = modelBuilder.buildModel(playersNames);
        handler = new HandlerFactory().buildHandler(model.getPlayers());
    }

    @Test
    void motherNatureMovement() throws NotEnoughCoinsException, InvalidCardException, InvalidMotherNatureStepsException {
        Island island0 = model.getMotherNature().getPosition();
        Island island2 = island0.getNextIsland().getNextIsland();

        model.playAssistantCard(0, 1);
        model.playCharacterCard(0, 3);

        model.moveMotherNature(0, 2);

        assertTrue(model.getMotherNature().getPosition().equals(island2));
    }

    @Test
    void extraAction() throws NotEnoughCoinsException, InvalidCardException {
        //model.playCharacterCard(0, 2);

        //TODO
    }
}