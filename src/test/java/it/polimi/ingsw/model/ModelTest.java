package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.CardException;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.card.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    Model model;
    List<String> playersNames;
    List<Professor> professors;

    @BeforeEach
    void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        playersNames = new ArrayList<>();
        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames, true, true);
        professors = model.getProfessors();
    }

    @Test
    void extraActionTest() throws InvalidActionException {
        for (Color color : Color.values()) {
            model.returnStudentsToBag(color, 3);
        }
        Student student = model.getPlayers().get(0).getSchool().getEntrance().getPawns().get(0);
        Student student1 = model.getPlayers().get(0).getSchool().getEntrance().getPawns().get(1);
        model.moveStudentToDiningRoom(0, 0);
        model.exchangeStudentsDiningRoomEntrance(0, 0, student.getColor());
        assertEquals(1, model.getPlayers().get(0).getSchool().getDiningRoomByColor(student1.getColor()).getNumPawns());
        if (student.getColor() != student1.getColor()) assertEquals(0, model.getPlayers().get(0).getSchool().getDiningRoomByColor(student.getColor()).getNumPawns());
    }

    @Test
    void checkIslandsConnection() {
        List<Island> islands = model.getIslands();
        for (Island island : islands) {
            assertEquals((island.getId() + 1) % 12, island.getNextIsland().getId());
            assertEquals((island.getId() + 11) % 12, island.getPrevIsland().getId());
        }

    }

    @Test
    void checkNickname(){
        int i = 0;
        for (String s : playersNames) {
            assertEquals(s, model.getPlayers().get(i).getNickname());
            i++;
        }
    }

    @Test
    void playAssistantCard() throws CardException {
        int i = 0;
        for (Player player : model.getPlayers()) {
            assertEquals(i, player.getId());
            for (int j = 1 ; j < 11; j++) {
                model.playAssistantCard(player.getId(),j);
                assertEquals(j,player.getLastAssistantCard().getValue());
            }
            i++;
        }
    }

    @Test
    void playCharacterCard() throws NotEnoughCoinsException, CardException {
        for (Player player : model.getPlayers()) {
            for (CharacterCard card : model.getCharacterCards()) {
                int prevCoin = player.getCoins();

                if (prevCoin >= card.getCoins()){
                    model.playCharacterCard(player.getId(), card.getId());
                    assertEquals(prevCoin - card.getCoins() + 1, player.getCoins());
                }
                else {
                    assertThrows(NotEnoughCoinsException.class, () -> model.playCharacterCard(player.getId(), card.getId()));
                }
            }
        }
    }

    @Test
    void moveStudentToDiningRoom() throws InvalidActionException {
        for (Player player : model.getPlayers()) {
            int i = 1;
            for (int j = 0 ; j < player.getSchool().getEntrance().getNumPawns();) {
                Student student = player.getSchool().getEntrance().getPawns().get(j);
                model.moveStudentToDiningRoom(player.getId(),j);
                assertTrue(player.getSchool().getDiningRoomByColor(student.getColor()).getPawns().contains(student));
                assertFalse(player.getSchool().getEntrance().getPawns().contains(student));
                if (player.getSchool().getDiningRoomByColor(student.getColor()).getNumPawns() % 3 == 0) {
                    assertEquals( i + 1, player.getCoins());
                    i++;
                }
            }
        }
    }

    @Test
    void moveStudentToIsland() throws InvalidActionException {
        for (Player player : model.getPlayers()) {
            List<Student> students = player.getSchool().getEntrance().getPawns();
            for (Student student : students) {
                int islandId = new Random().nextInt(model.getIslands().size());
                model.moveStudentToIsland(player.getId(), player.getSchool().getEntrance().getPawns().indexOf(student), islandId);
                assertFalse(player.getSchool().getEntrance().getPawns().contains(student));
                assertTrue(model.getIslands().get(islandId).getPawns().contains(student));
            }
        }
    }

    @Test
    void moveMotherNature() throws MotherNatureStepsException, CardException, InvalidActionException {
        for (Player player : model.getPlayers()) {
            model.playAssistantCard(player.getId(), new Random().nextInt(1,11));
            for (int i = 0; i < player.getSchool().getEntrance().getNumPawns(); i++) {
                model.moveStudentToIsland(player.getId(), i, new Random().nextInt(12));
            }
            int movement = new Random().nextInt(1, player.getLastAssistantCard().getSteps() + 1);
            Island prev = model.getMotherNature().getPosition();
            model.moveMotherNature(player.getId(), movement);
            Island next = model.getMotherNature().getPosition();
            assertEquals((prev.getId() + movement) % 12, next.getId());
        }
    }

    @Test
    void cloudMethods() throws Exception{
        model.addStudentsToClouds();
        for (Cloud cloud : model.getClouds()) {
            if (model.getPlayers().size() == 2) assertEquals(3, cloud.getNumPawns());
            if (model.getPlayers().size() == 3) assertEquals(4, cloud.getNumPawns());
        }
        int i = 0;
        for (Player player : model.getPlayers()) {
            List<Student> students = model.getClouds().get(i).getPawns();
            model.takeStudentsFromCloud(player.getId(), i);
            for (Student student : students) {
                assertTrue(player.getSchool().getEntrance().getPawns().contains(student));
                assertFalse(model.getClouds().get(i).getPawns().contains(student));
            }
            i++;
        }
    }
}