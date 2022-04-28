package it.polimi.ingsw.controller;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.exceptions.InvalidIslandException;
import it.polimi.ingsw.exceptions.InvalidMotherNatureStepsException;
import it.polimi.ingsw.exceptions.NotPlayerTurnException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ModelSerializable;
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



    private void playPlanningPhase() throws  InvalidCardException, NotPlayerTurnException {
        controller.playAssistantCard(0, 8);
        controller.playAssistantCard(1, 10);
        controller.playAssistantCard(2, 9);
        assertEquals(0, controller.getPlayersToPlay().get(0).getId());
        assertEquals(2, controller.getPlayersToPlay().get(1).getId());
        assertEquals(1, controller.getPlayersToPlay().get(2).getId());
    }

    @Test
    void moveStudentToDiningRoom() throws InvalidCardException, NotPlayerTurnException {
        playPlanningPhase();
        assertEquals(0, controller.getPlayersToPlay().get(0).getId());
        assertEquals(2, controller.getPlayersToPlay().get(1).getId());
        assertEquals(1, controller.getPlayersToPlay().get(2).getId());

        assertThrows(NotPlayerTurnException.class, () -> controller.moveStudentToDiningRoom(1, 4));

        Player player = controller.getActivePlayer();
        assertEquals(0, player.getId());
        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int initialSize =  modelSerializable.getEntranceByPlayerId(player.getId()).size();

        controller.moveStudentToDiningRoom(0, 0);
        controller.moveStudentToDiningRoom(0, 0);
        controller.moveStudentToDiningRoom(0, 0);
        controller.moveStudentToDiningRoom(0, 0);

        modelSerializable = new ModelSerializable(this.model);
        assertEquals(initialSize-4, modelSerializable.getEntranceByPlayerId(player.getId()).size());

        int numStudentInDining = 0;
        for (Color color : Color.values()) {
            numStudentInDining += modelSerializable.getDRByPlayerAndColor(player.getId(), color);
        }
        assertEquals(4, numStudentInDining);
    }

    @Test
    void moveStudentToIsland() throws InvalidCardException, NotPlayerTurnException, InvalidIslandException {
        playPlanningPhase();

        assertThrows(NotPlayerTurnException.class, () -> controller.moveStudentToIsland(1, 4, 9));

        Player player = controller.getActivePlayer();
        assertEquals(0, player.getId());
        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int initialSize =  modelSerializable.getEntranceByPlayerId(player.getId()).size();

        assertThrows(InvalidIslandException.class, () -> controller.moveStudentToIsland(0, 0, 12));
        assertThrows(InvalidIslandException.class, () -> controller.moveStudentToIsland(0, 0, -1));

        modelSerializable = new ModelSerializable(this.model);
        int prevSize = modelSerializable.getStudentOnIslandByPlayer(0, 6);
        controller.moveStudentToIsland(0, 0, 6);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandByPlayer(0, 6));

        modelSerializable = new ModelSerializable(this.model);
        prevSize = modelSerializable.getStudentOnIslandByPlayer(0, 9);
        controller.moveStudentToIsland(0, 0, 9);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandByPlayer(0, 9));

        modelSerializable = new ModelSerializable(this.model);
        prevSize = modelSerializable.getStudentOnIslandByPlayer(0, 2);
        controller.moveStudentToIsland(0, 0, 2);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandByPlayer(0, 2));

        modelSerializable = new ModelSerializable(this.model);
        prevSize = modelSerializable.getStudentOnIslandByPlayer(0, 7);
        controller.moveStudentToIsland(0, 0, 7);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandByPlayer(0, 7));

        assertEquals(initialSize-4, modelSerializable.getEntranceByPlayerId(player.getId()).size());

    }

    private void playAC_MoveStudent() throws InvalidIslandException, InvalidCardException, NotPlayerTurnException {
        playPlanningPhase();
        controller.moveStudentToIsland(0, 0, 2);
        controller.moveStudentToIsland(0, 0, 11);

        controller.moveStudentToDiningRoom(0, 0);
        controller.moveStudentToDiningRoom(0, 2);
    }

    @Test
    void moveMotherNature() throws InvalidIslandException, InvalidCardException, NotPlayerTurnException, InvalidMotherNatureStepsException {
        playAC_MoveStudent();

        assertThrows(InvalidMotherNatureStepsException.class, () -> controller.moveMotherNature(0, 5));
        assertThrows(InvalidMotherNatureStepsException.class, () -> controller.moveMotherNature(0, 0));

        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int start = modelSerializable.getMNPosition();

        controller.moveMotherNature(0, 4);

        modelSerializable = new ModelSerializable(this.model);

        assertEquals((start + 4) % 12, modelSerializable.getMNPosition());
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

}