package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.exceptions.NotPlayerTurnException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ControllerTest {

    private Model model;
    private Controller controller;

    @BeforeEach
    void initialization() {
        List<String> names = new ArrayList<>();
        names.add("player0");
        names.add("player1");
        names.add("player2");
        model = new ModelBuilder().buildModel(names);
        controller = model.getController();
    }

    @Test
    void playAssistantCard() throws InvalidCardException, NotPlayerTurnException {

        Player current;

        current = controller.getActivePlayer();
        assertEquals(0, current.getId());
        controller.playAssistantCard(0, 6);
        assertEquals(6,current.getLastAssistantCard().getValue());

        current = controller.getActivePlayer();
        assertEquals(1, current.getId());
        assertThrows(InvalidCardException.class, () -> controller.playAssistantCard(1, 6));
        assertThrows(NotPlayerTurnException.class, () -> controller.playAssistantCard(2, 7));
        controller.playAssistantCard(1, 4);
        assertEquals(4, current.getLastAssistantCard().getValue());

        current = controller.getActivePlayer();
        assertThrows(InvalidCardException.class, () -> controller.playAssistantCard(2, 6));
        assertThrows(InvalidCardException.class, () -> controller.playAssistantCard(2, 4));
        controller.playAssistantCard(2, 5);
        assertEquals(5, current.getLastAssistantCard().getValue());
        assertEquals(9, current.getAssistantCards().size());

        assertEquals(1, controller.getPlayersToPlay().get(0).getId());
        assertEquals(2, controller.getPlayersToPlay().get(1).getId());
        assertEquals(0, controller.getPlayersToPlay().get(2).getId());
    }

    @Test
    void playCharacterCard() {
    }

    @Test
    void moveStudentToDiningRoom() {

        controller.moveStudentToDiningRoom(1, 4);
    }

    @Test
    void moveStudentToIsland() {
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void getStudentsFromCloud() {
    }

    @Test
    void extraAction() {
    }

    @Test
    void endAction() {
    }

    @Test
    void getOptions() {
    }

    @Test
    void getPlayersHavePlayed() {
    }
}