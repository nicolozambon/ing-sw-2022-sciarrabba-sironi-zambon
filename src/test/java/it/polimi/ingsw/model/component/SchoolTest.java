package it.polimi.ingsw.model.component;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.Board;
import it.polimi.ingsw.model.component.School;
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
        player = new Player( 1,"player", students, towers, new Deck());
        school = player.getSchool();
    }

    @Test
    void getOwnerTest() {
        assertEquals(school.getOwner(), player);
    }

    @Test
    void getEntranceTest() {
        Board<Student> entrance = school.getEntrance();
        for (Student student : students) {
            assertTrue(entrance.getPawns().contains(student));
        }
    }

    @Test
    void checkMovementToDiningRoomTest() {
        for (Student student : school.getEntrance().getPawns()) {
            school.moveStudentDiningRoom(student);
            assertTrue(school.getDiningRoomByColor(student.getColor()).getPawns().contains(student));
        }
    }

    @Test
    void checkProfessorMovementTest() {
        Professor professor = new Professor(Color.GREEN);
        Island island = new Island(1, new ArrayList<>());
        Player player = new Player(1, "pippo", new ArrayList<>(), new ArrayList<>(), new Deck());
        School school = new School(player, new ArrayList<Student>(), new ArrayList<Tower>());

        ArrayList<Professor> arrayList = new ArrayList<Professor>();
        arrayList.add(professor);
        Board<Professor> board = new Board<Professor>(arrayList);

        school.controlProfessor(professor, board);

        Board<Professor> professorTable = school.getProfessorsTable();
        List<Professor> professorTableArrayList = professorTable.getPawns();
        assertTrue(professorTableArrayList.contains(professor));
    }

    @Test
    void getTowersBoardTest() {
        Board<Tower> towerBoard = school.getTowersBoard();
        for (Tower tower : towers) {
            assertTrue(towerBoard.getPawns().contains(tower));
        }
    }

    @Test
    void takeInTowerTest() {
        Player owner1 = new Player(1, "pluto", new ArrayList<Student>(), new ArrayList<Tower>(), new Deck());
        Tower tower1 = new Tower(TowerColor.BLACK);
        School school1 = new School(owner1, new ArrayList<Student>(), new ArrayList<Tower>());

        school1.takeInTower(tower1);

        Board towerBoard = school1.getTowersBoard();
        List<Tower> towerBoardList = towerBoard.getPawns();

        assertTrue(towerBoardList.contains(tower1));
    }

    @Test
    void takeOutTowerTest() {
        Player owner1 = new Player(1, "pluto", new ArrayList<Student>(), new ArrayList<Tower>(), new Deck());
        Tower tower1 = new Tower(TowerColor.BLACK);
        ArrayList<Tower> towerList = new ArrayList<Tower>();
        towerList.add(tower1);
        School school1 = new School(owner1, new ArrayList<Student>(), towerList);

        school1.takeOutTower();
        assertTrue(school1.getProfessorsTable().getPawns().isEmpty());
    }



    @Test
    void moveStudentIslandTest() {
        ArrayList<Student> studentArrayList = new ArrayList<Student>();
        Island island = new Island(1, studentArrayList);

        Player owner1 = new Player(1, "pluto", new ArrayList<Student>(), new ArrayList<Tower>(), new Deck());
        ArrayList<Student> studentArrayList2 = new ArrayList<Student>();
        Student student1 = new Student(Color.BLUE);
        studentArrayList2.add(student1);
        School school = new School(owner1, studentArrayList2, new ArrayList<Tower>());

        school.moveStudentIsland(student1,island);

        assertEquals(island.countStudentsByColor(Color.BLUE), 1);

    }

    @Test
    void takeStudentsFromCloudTest() {
        ArrayList<Student> studentArrayList = new ArrayList<Student>();
        Student student1 = new Student(Color.PINK);
        Student student2 = new Student(Color.PINK);
        Student student3 = new Student(Color.BLUE);

        studentArrayList.add(student1);
        studentArrayList.add(student2);
        studentArrayList.add(student3);

        Cloud cloud = new Cloud(studentArrayList);

        Player owner1 = new Player(1, "pluto", new ArrayList<Student>(), new ArrayList<Tower>(), new Deck());
        School school = new School(owner1, new ArrayList<Student>(), new ArrayList<Tower>());

        school.takeStudentsFromCloud(cloud);
        List<Student> studentArrayList2 = school.getEntrance().getPawns();

        assertTrue(studentArrayList2.contains(student1) && studentArrayList2.contains(student2) && studentArrayList2.contains(student3));
        assertTrue(cloud.getPawns().isEmpty());

    }

}