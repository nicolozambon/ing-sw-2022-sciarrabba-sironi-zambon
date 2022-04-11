package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCardIdException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.card.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    Model model;
    List<String> playersNames;

    @BeforeEach
    void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        playersNames = new ArrayList<>();
        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames);
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
    void playAssistantCard() {
        int i = 0;
        for (Player player : model.getPlayers()) {
            assertEquals(i, player.getId());
            for (int j = 1 ; j < 11; j++) {
                model.playAssistantCard(player.getId(),j);
                assertEquals(j,player.getLastAssistantCard().getId());
            }
            i++;
        }
    }

    @Test
    void playCharacterCard() throws NotEnoughCoinsException, InvalidCardIdException {
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
    void moveStudentToDiningRoom() {
        for (Player player : model.getPlayers()) {
            int i = 1;
            for (int j = 0 ; j < player.getSchool().getEntrance().getNumPawns();) {
                Student student = player.getSchool().getEntrance().getPawns().get(j);
                model.moveStudentToDiningRoom(player.getId(),j);
                assertTrue(player.getSchool().getDiningRoomByColor(student.getColor()).getPawns().contains(student));
                assertFalse(player.getSchool().getEntrance().getPawns().contains(student));
                if (player.getSchool().getDiningRoomByColor(student.getColor()).getNumPawns() % 3 == 2) {
                    System.out.println("coins++");
                    assertEquals( i + 1, player.getCoins());
                    i++;
                }
            }
        }
    }

    @Test
    void moveStudentToIsland() {

    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void addStudentsToClouds() {
    }

    @Test
    void getStudentsFromCloud() {
    }

    @Test
    void extraAction() {
    }
}