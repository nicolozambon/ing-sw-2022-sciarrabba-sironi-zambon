package it.polimi.ingsw.controller;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.*;
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
        controller.playAssistantCard(0, 10);
        controller.playAssistantCard(1, 8);
        controller.playAssistantCard(2, 6);
        assertEquals(2, controller.getPlayersToPlay().get(0).getId());
        assertEquals(1, controller.getPlayersToPlay().get(1).getId());
        assertEquals(0, controller.getPlayersToPlay().get(2).getId());
    }

    @Test
    void moveStudentToDiningRoom() throws InvalidCardException, NotPlayerTurnException {
        playPlanningPhase();

        assertThrows(NotPlayerTurnException.class, () -> controller.moveStudentToDiningRoom(1, 4));

        Player player = controller.getActivePlayer();
        assertEquals(2, player.getId());
        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int initialSize =  modelSerializable.getEntranceByPlayerId(player.getId()).size();

        controller.moveStudentToDiningRoom(2, 0);
        controller.moveStudentToDiningRoom(2, 0);
        controller.moveStudentToDiningRoom(2, 0);
        controller.moveStudentToDiningRoom(2, 0);

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
        assertEquals(2, player.getId());
        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int initialSize =  modelSerializable.getEntranceByPlayerId(player.getId()).size();

        assertThrows(InvalidIslandException.class, () -> controller.moveStudentToIsland(2, 0, 12));
        assertThrows(InvalidIslandException.class, () -> controller.moveStudentToIsland(2, 0, -1));

        modelSerializable = new ModelSerializable(this.model);
        int prevSize = modelSerializable.getStudentOnIslandById(6);
        controller.moveStudentToIsland(2, 0, 6);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandById(6));

        modelSerializable = new ModelSerializable(this.model);
        prevSize = modelSerializable.getStudentOnIslandById(9);
        controller.moveStudentToIsland(2, 0, 9);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandById(9));

        modelSerializable = new ModelSerializable(this.model);
        prevSize = modelSerializable.getStudentOnIslandById(2);
        controller.moveStudentToIsland(2, 0, 2);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandById(2));

        modelSerializable = new ModelSerializable(this.model);
        prevSize = modelSerializable.getStudentOnIslandById(7);
        controller.moveStudentToIsland(2, 0, 7);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prevSize + 1, modelSerializable.getStudentOnIslandById(7));

        assertEquals(initialSize-4, modelSerializable.getEntranceByPlayerId(player.getId()).size());

    }

    private void playAC_MoveStudent(int playerId) throws InvalidIslandException, InvalidCardException, NotPlayerTurnException {

        controller.moveStudentToIsland(playerId, 0, 2);
        controller.moveStudentToIsland(playerId, 0, 11);

        controller.moveStudentToDiningRoom(playerId, 0);
        controller.moveStudentToDiningRoom(playerId, 2);
    }

    @Test
    void moveMotherNature() throws InvalidIslandException, InvalidCardException, NotPlayerTurnException, InvalidMotherNatureStepsException {
        playPlanningPhase();
        playAC_MoveStudent(2);

        assertThrows(InvalidMotherNatureStepsException.class, () -> controller.moveMotherNature(2, 5));
        assertThrows(InvalidMotherNatureStepsException.class, () -> controller.moveMotherNature(2, 0));

        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int start = modelSerializable.getMNPosition();

        controller.moveMotherNature(2, 3);

        modelSerializable = new ModelSerializable(this.model);

        assertEquals((start + 3) % 12, modelSerializable.getMNPosition());
    }

    private void playUntilCloud(int playerId) throws NotPlayerTurnException, InvalidIslandException, InvalidCardException, InvalidMotherNatureStepsException {
        playAC_MoveStudent(playerId);
        controller.moveMotherNature(playerId, 1);
    }

    @Test
    void getStudentsFromCloud() throws NotPlayerTurnException, InvalidIslandException, InvalidCardException, InvalidMotherNatureStepsException {
        playPlanningPhase();
        playUntilCloud(2);

        controller.takeStudentsFromCloud(2, 0);
        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        assertEquals(0, modelSerializable.getStudentOnCloud(0));
        assertEquals(9, modelSerializable.getEntranceByPlayerId(0).size());

    }

    @Test
    void playCharacterCard() throws Exception {
        playPlanningPhase();
        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 0);

        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        int prev = modelSerializable.getCharacterCardCost(3);
        controller.playCharacterCard(2, 3);
        modelSerializable = new ModelSerializable(this.model);
        assertEquals(prev + 1, modelSerializable.getCharacterCardCost(3));
    }

    @Test
    void extraAction() throws Exception {
        playPlanningPhase();
        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 0);
        assertThrows(NotEnoughCoinsException.class, () -> controller.playCharacterCard(2, 2));
        controller.extraAction(2, 1);
    }

    @Test
    void endAction() throws NotPlayerTurnException, InvalidIslandException, InvalidCardException, InvalidMotherNatureStepsException {
        playPlanningPhase();
        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 0);
        //System.out.println(controller.getOptions());
        controller.endAction(2);
        assertEquals(1, controller.getActivePlayer().getId());
    }

    @Test
    void requestPlayAssistantCard() throws Exception{
        //playPlanningPhase();
        Player currentPlayer = controller.getActivePlayer();
        assertEquals(0, currentPlayer.getId());
        controller.requestPerformed(new RequestEvent("playAssistantCard", 0, 2));
        assertEquals(2, currentPlayer.getLastAssistantCard().getValue());
    }

    @Test
    void getOptions() {
        assertEquals(1, controller.getOptions().size());
        assertTrue(controller.getOptions().contains("playAssistantCard"));
        assertEquals(0, controller.getActivePlayer().getId());
        assertEquals(3, controller.getPlayersToPlay().size());
        assertEquals(0, controller.getPlayersHavePlayed().size());
    }

    @Test
    void orderPlayerForNextRound() throws Exception{
        playPlanningPhase();

        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 0);
        controller.endAction(2);

        playUntilCloud(1);
        controller.takeStudentsFromCloud(1, 2);
        controller.endAction(1);

        playUntilCloud(0);
        controller.takeStudentsFromCloud(0, 1);
        controller.endAction(0);

        List<Player> players = controller.getPlayersToPlay();
        assertEquals(3, players.size());

        assertEquals(2, players.get(0).getId());
        assertEquals(1, players.get(1).getId());
        assertEquals(0, players.get(2).getId());
    }

}