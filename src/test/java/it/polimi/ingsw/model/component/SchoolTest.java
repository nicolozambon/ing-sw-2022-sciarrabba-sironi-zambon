package it.polimi.ingsw.model.component;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.card.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {

    School school;
    Player player;
    List<Student> students;
    List<Tower> towers;

    @BeforeEach
    public void initialization() {
        students = new ArrayList<>();
        for (Color c : Color.values()) {
            students.add(new Student(c));
        }
        towers = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            towers.add(new Tower(TowerColor.GREY));
        }
        player = new Player( 1,"player", students, towers, new Deck(), new Coin());
        school = player.getSchool();
    }

    @Test
    void getOwner() {
        assertEquals(school.getOwner(), player);
    }

    @Test
    void getEntrance() {
        Board<Student> entrance = school.getEntrance();
        for (Student student : students) {
            assertTrue(entrance.getPawns().contains(student));
        }
    }

    @Test
    void checkMovementToDiningRoom() {
        for (Student student : school.getEntrance().getPawns()) {
            school.moveStudentDiningRoom(student);
            assertTrue(school.getDiningRoomByColor(student.getColor()).getPawns().contains(student));
        }
    }

    @Test
    void checkProfessorMovement() {

    }

    @Test
    void getTowersBoard() {
        Board<Tower> towerBoard = school.getTowersBoard();
        for (Tower tower : towers) {
            assertTrue(towerBoard.getPawns().contains(tower));
        }
    }

    @Test
    void takeInTower() {
    }

    @Test
    void takeOutTower() {
    }



    @Test
    void moveStudentIsland() {
    }

    @Test
    void takeStudentsFromCloud() {
    }

}