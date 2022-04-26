package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;
   // Player player2;
    AssistantCard assistantCard1;
    AssistantCard assistantCard2;
    List<Student> students;

    @BeforeEach
    void initialization() {

        List<AssistantCard> cards = new ArrayList<>();
        assistantCard1 = new AssistantCard(1, 5);
        assistantCard2 = new AssistantCard(2, 9);
        cards.add(assistantCard1);
        cards.add(assistantCard2);

        Deck<AssistantCard> deck = new Deck<>(cards);
        students = new ArrayList<>();
        List<Tower> towers = new ArrayList<>();

        for (Color c : Color.values()) {
            students.add(new Student(c));
        }
        for (int i = 0; i < 10; i++) {
            towers.add(new Tower(TowerColor.BLACK));
        }
        player = new Player(1, "player", students, towers, deck);

        /*deck = new Deck<>(cards);
        students = new ArrayList<>();
        towers = new ArrayList<>();

        for (Color c : Color.values()) {
            students.add(new Student(c));
        }
        for (int i = 0; i < 10; i++) {
            towers.add(new Tower(TowerColor.WHITE));
        }
        player2 = new Player(2, "player2",students, towers, deck);*/

    }

    @Test
    void getNickname() {
        assertEquals("player", player.getNickname());
    }

    @Test
    void AssistantCard() {
        assertThrows(IndexOutOfBoundsException.class, () -> player.getLastAssistantCard());
        player.playAssistantCard(1);
        assertEquals(player.getLastAssistantCard().getValue(), 1);
        player.playAssistantCard(2);
        assertEquals(player.getLastAssistantCard().getValue(), 2);
    }

    @Test
    void moveStudentDiningRoom() {
        int prevCoins = player.getCoins();
        for (Student student : students) {
            player.moveStudentDiningRoom(student,20);
            assertTrue(player.getSchool().getDiningRoomByColor(student.getColor()).getPawns().contains(student));
            assertFalse(player.getSchool().getEntrance().getPawns().contains(student));
        }

        player.moveStudentDiningRoom(new Student(Color.BLUE),20);
        player.moveStudentDiningRoom(new Student(Color.BLUE),20);
        player.moveStudentDiningRoom(new Student(Color.BLUE),20);
        player.moveStudentDiningRoom(new Student(Color.BLUE),20);
        player.moveStudentDiningRoom(new Student(Color.BLUE),20);
        player.moveStudentDiningRoom(new Student(Color.BLUE),20);
        assertEquals(prevCoins+2, player.getCoins());



    }

    @Test
    void moveStudentIsland() {
        Island island = new Island(1);
        for (Student student : students) {
            player.moveStudentIsland(student, island);
            assertTrue(island.getPawns().contains(student));
            assertFalse(player.getSchool().getEntrance().getPawns().contains(student));
        }

    }

    @Test
    void takeStudentsFromCloud() {
        students = new ArrayList<>();
        for (Color c : Color.values()) {
            students.add(new Student(c));
        }
        Cloud cloud = new Cloud(students);

        player.takeStudentsFromCloud(cloud);

        for (Student s : students) {
            assertTrue(player.getSchool().getEntrance().getPawns().contains(s));
            assertFalse(cloud.getPawns().contains(s));
        }

    }
}