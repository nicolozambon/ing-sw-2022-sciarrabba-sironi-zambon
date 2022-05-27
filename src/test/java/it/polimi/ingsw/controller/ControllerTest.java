package it.polimi.ingsw.controller;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ThinModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Model model;
    private Controller controller;

    @BeforeEach
    void initialization() {
        List<String> names = new ArrayList<>();
        names.add("player0");
        names.add("player1");
        names.add("player2");
        model = new ModelBuilder().buildModel(names, true);
        controller = model.getController();
    }

    @Test
    void playAssistantCard() throws CardException, NotPlayerTurnException, InvalidActionException {

        Player current;

        current = controller.getActivePlayer();
        assertEquals(0, current.getId());
        controller.chooseWizard(0, 0);
        controller.chooseWizard(1, 1);
        controller.chooseWizard(2, 2);

        controller.playAssistantCard(0, 6);

        assertEquals(6,current.getLastAssistantCard().getValue());


        current = controller.getActivePlayer();
        assertEquals(1, current.getId());
        assertThrows(CardException.class, () -> controller.playAssistantCard(1, 6));
        assertThrows(NotPlayerTurnException.class, () -> controller.playAssistantCard(2, 7));

        controller.playAssistantCard(1, 4);

        assertEquals(4, current.getLastAssistantCard().getValue());

        current = controller.getActivePlayer();
        assertThrows(CardException.class, () -> controller.playAssistantCard(2, 6));
        assertThrows(CardException.class, () -> controller.playAssistantCard(2, 4));
        controller.playAssistantCard(2, 5);
        assertEquals(5, current.getLastAssistantCard().getValue());
        assertEquals(9, current.getAssistantCards().size());

        assertEquals(1, controller.getPlayersToPlay().get(0).getId());
        assertEquals(2, controller.getPlayersToPlay().get(1).getId());
        assertEquals(0, controller.getPlayersToPlay().get(2).getId());
    }



    private void playPlanningPhase() throws CardException, NotPlayerTurnException, InvalidActionException {
        controller.chooseWizard(0, 0);
        controller.chooseWizard(1, 1);
        controller.chooseWizard(2, 2);

        controller.playAssistantCard(0, 10);
        controller.playAssistantCard(1, 8);
        controller.playAssistantCard(2, 6);
        assertEquals(2, controller.getPlayersToPlay().get(0).getId());
        assertEquals(1, controller.getPlayersToPlay().get(1).getId());
        assertEquals(0, controller.getPlayersToPlay().get(2).getId());
    }

    @Test
    void moveStudentToDiningRoom() throws CardException, NotPlayerTurnException, InvalidActionException {
        playPlanningPhase();

        assertThrows(NotPlayerTurnException.class, () -> controller.moveStudentToDiningRoom(1, 4));

        Player player = controller.getActivePlayer();
        assertEquals(2, player.getId());
        ThinModel thinModel = new ThinModel(this.model);
        int initialSize =  thinModel.getEntranceByPlayerId(player.getId()).size();

        controller.moveStudentToDiningRoom(2, 1);
        controller.moveStudentToDiningRoom(2, 1);
        controller.moveStudentToDiningRoom(2, 1);
        controller.moveStudentToDiningRoom(2, 1);

        thinModel = new ThinModel(this.model);
        assertEquals(initialSize-4, thinModel.getEntranceByPlayerId(player.getId()).size());

        int numStudentInDining = 0;
        for (Color color : Color.values()) {
            numStudentInDining += thinModel.getDRByPlayerAndColor(player.getId(), color);
        }
        assertEquals(4, numStudentInDining);
    }

    @Test
    void moveStudentToIsland() throws CardException, NotPlayerTurnException, IslandException, InvalidActionException {
        playPlanningPhase();

        assertThrows(NotPlayerTurnException.class, () -> controller.moveStudentToIsland(1, 4, 9));

        Player player = controller.getActivePlayer();
        assertEquals(2, player.getId());
        ThinModel thinModel = new ThinModel(this.model);
        int initialSize =  thinModel.getEntranceByPlayerId(player.getId()).size();

        assertThrows(IslandException.class, () -> controller.moveStudentToIsland(2, 0, 13));
        assertThrows(IslandException.class, () -> controller.moveStudentToIsland(2, 0, -1));

        thinModel = new ThinModel(this.model);
        int prevSize = thinModel.getStudentOnIslandById(6);
        controller.moveStudentToIsland(2, 4, 7);
        thinModel = new ThinModel(this.model);
        assertEquals(prevSize + 1, thinModel.getStudentOnIslandById(6));

        thinModel = new ThinModel(this.model);
        prevSize = thinModel.getStudentOnIslandById(9);
        controller.moveStudentToIsland(2, 1, 10);
        thinModel = new ThinModel(this.model);
        assertEquals(prevSize + 1, thinModel.getStudentOnIslandById(9));

        thinModel = new ThinModel(this.model);
        prevSize = thinModel.getStudentOnIslandById(2);
        controller.moveStudentToIsland(2, 1, 3);
        thinModel = new ThinModel(this.model);
        assertEquals(prevSize + 1, thinModel.getStudentOnIslandById(2));

        thinModel = new ThinModel(this.model);
        prevSize = thinModel.getStudentOnIslandById(7);
        controller.moveStudentToIsland(2, 1, 8);
        thinModel = new ThinModel(this.model);
        assertEquals(prevSize + 1, thinModel.getStudentOnIslandById(7));

        assertEquals(initialSize-4, thinModel.getEntranceByPlayerId(player.getId()).size());

    }

    private void playAC_MoveStudent(int playerId) throws IslandException, CardException, NotPlayerTurnException, InvalidActionException {

        controller.moveStudentToIsland(playerId, 1, 2);
        controller.moveStudentToIsland(playerId, 1, 11);

        controller.moveStudentToDiningRoom(playerId, 1);
        controller.moveStudentToDiningRoom(playerId, 2);
    }

    @Test
    void moveMotherNature() throws IslandException, CardException, NotPlayerTurnException, MotherNatureStepsException, InvalidActionException {
        playPlanningPhase();
        playAC_MoveStudent(2);

        assertThrows(MotherNatureStepsException.class, () -> controller.moveMotherNature(2, 5));
        assertThrows(MotherNatureStepsException.class, () -> controller.moveMotherNature(2, 0));

        ThinModel thinModel = new ThinModel(this.model);
        int start = thinModel.getMNPosition();

        controller.moveMotherNature(2, 3);

        thinModel = new ThinModel(this.model);

        assertEquals((start + 3) % 12, thinModel.getMNPosition());
    }

    private void playUntilCloud(int playerId) throws NotPlayerTurnException, IslandException, CardException, MotherNatureStepsException, InvalidActionException {
        playAC_MoveStudent(playerId);
        controller.moveMotherNature(playerId, 1);
    }

    @Test
    void getStudentsFromCloud() throws Exception {
        playPlanningPhase();
        playUntilCloud(2);

        controller.takeStudentsFromCloud(2, 1);
        ThinModel thinModel = new ThinModel(this.model);
        assertEquals(0, thinModel.getNumStudentOnCloud(0));
        assertEquals(9, thinModel.getEntranceByPlayerId(0).size());

    }

    @Test
    void playCharacterCard() throws Exception {
        playPlanningPhase();
        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 1);

        ThinModel thinModel = new ThinModel(this.model);
        int prev = thinModel.getCharacterCards().stream().filter(x -> x.getId() == 3).findFirst().get().getCoins();
        controller.playCharacterCard(2, 3);
        thinModel = new ThinModel(this.model);
        assertEquals(prev + 1, thinModel.getCharacterCards().stream().filter(x -> x.getId() == 3).findFirst().get().getCoins());
    }

    @Test
    void extraAction() throws Exception {
        playPlanningPhase();
        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 1);
        assertThrows(NotEnoughCoinsException.class, () -> controller.playCharacterCard(2, 2));
        controller.extraAction(2, 1);
    }

    @Test
    void endAction() throws NotPlayerTurnException, IslandException, CardException, Exception {
        playPlanningPhase();
        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 1);
        //System.out.println(controller.getOptions());
        controller.endAction(2);
        assertEquals(1, controller.getActivePlayer().getId());
    }

    @Test
    void requestPlayAssistantCard() throws Exception{
        //playPlanningPhase();
        controller.chooseWizard(0, 0);
        controller.chooseWizard(1, 1);
        controller.chooseWizard(2, 2);

        Player currentPlayer = controller.getActivePlayer();
        assertEquals(0, currentPlayer.getId());
        controller.onRequestEvent(new RequestEvent("playAssistantCard", 0, 2));
        assertEquals(2, currentPlayer.getLastAssistantCard().getValue());
    }

    @Test
    void getOptions() {
        assertEquals(1, controller.getOptions().size());
        assertTrue(controller.getOptions().contains("chooseWizard"));
        assertEquals(0, controller.getActivePlayer().getId());
        assertEquals(3, controller.getPlayersToPlay().size());
        assertEquals(0, controller.getPlayersHavePlayed().size());
    }

    @Test
    void orderPlayerForNextRound() throws Exception{
        playPlanningPhase();

        playUntilCloud(2);
        controller.takeStudentsFromCloud(2, 2);
        controller.endAction(2);

        playUntilCloud(1);
        controller.takeStudentsFromCloud(1, 1);
        controller.endAction(1);

        playUntilCloud(0);
        controller.takeStudentsFromCloud(0, 3);
        controller.endAction(0);

        List<Player> players = controller.getPlayersToPlay();
        assertEquals(3, players.size());

        assertEquals(2, players.get(0).getId());
        assertEquals(1, players.get(1).getId());
        assertEquals(0, players.get(2).getId());
    }

    @Test
    void chooseWizardTest() throws InvalidActionException {
        controller.chooseWizard(0, 0);
        controller.chooseWizard(1, 1);
        assertThrows(InvalidActionException.class, () -> controller.chooseWizard(2, 0));
        controller.chooseWizard(2, 2);
        ThinModel thinModel = new ThinModel(this.model);
        assertEquals(thinModel.getWizards().get(0).ordinal(), 0);
        assertEquals(thinModel.getWizards().get(1).ordinal(), 1);
        assertEquals(thinModel.getWizards().get(2).ordinal(), 2);
    }

}