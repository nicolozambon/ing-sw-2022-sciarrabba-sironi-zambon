package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InfluenceHandlerTest {
    Model model;
    Handler handler;

    @BeforeEach
    private void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        List<String> playersNames = new ArrayList<>();

        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames);
        handler = new HandlerFactory().buildHandler(model.getPlayers());

    }

    @Test
    public void getMostInfluentialPlayerTest() throws Exception {

        ArrayList<Student> cloud1List = new ArrayList<>();
        ArrayList<Student> cloud2List = new ArrayList<>();
        ArrayList<Student> cloud3List = new ArrayList<>();
        ArrayList<Student> cloud4List = new ArrayList<>();

        Player player1 = model.getPlayers().get(0);
        Player player2 = model.getPlayers().get(1);
        Player player3 = model.getPlayers().get(2);

        Island island = model.getIslands().get(0);

        Student student1 = new Student(Color.BLUE);
        Student student2 = new Student(Color.BLUE);
        Student student3 = new Student(Color.BLUE);

        Student student4 = new Student(Color.BLUE);
        Student student5 = new Student(Color.BLUE);
        Student student6 = new Student(Color.RED);

        Student student7 = new Student(Color.YELLOW);
        Student student8 = new Student(Color.YELLOW);
        Student student9 = new Student(Color.YELLOW);

        Student student10 = new Student(Color.YELLOW);
        Student student11 = new Student(Color.YELLOW);
        Student student12 = new Student(Color.YELLOW);

        cloud1List.add(student1);
        cloud1List.add(student2);
        cloud1List.add(student3);

        cloud2List.add(student4);
        cloud2List.add(student5);
        cloud2List.add(student6);

        cloud3List.add(student7);
        cloud3List.add(student8);
        cloud3List.add(student9);

        cloud4List.add(student10);
        cloud4List.add(student11);
        cloud4List.add(student12);

        Cloud cloud1 = new Cloud(cloud1List);
        Cloud cloud2 = new Cloud(cloud2List);
        Cloud cloud3 = new Cloud(cloud3List);
        Cloud cloud4 = new Cloud(cloud4List);

        player1.takeStudentsFromCloud(cloud3);
        player1.takeStudentsFromCloud(cloud4);

        //To get coins
        model.moveStudentToDiningRoom(player1.getId(), player1.getSchool().getEntrance().getNumPawns()-1);
        model.moveStudentToDiningRoom(player1.getId(), player1.getSchool().getEntrance().getNumPawns()-1);
        model.moveStudentToDiningRoom(player1.getId(), player1.getSchool().getEntrance().getNumPawns()-1);

        /*
        player1.moveStudentDiningRoom(player1.getSchool().getEntrance().getPawns().get(0), 20);
        player1.moveStudentDiningRoom(player1.getSchool().getEntrance().getPawns().get(0), 20);
        player1.moveStudentDiningRoom(player1.getSchool().getEntrance().getPawns().get(0), 20);
        player1.moveStudentDiningRoom(player1.getSchool().getEntrance().getPawns().get(0), 20);
        player1.moveStudentDiningRoom(player1.getSchool().getEntrance().getPawns().get(0), 20);
        player1.moveStudentDiningRoom(player1.getSchool().getEntrance().getPawns().get(0), 20);
        */

        System.out.println("Player1 coin size: " + player1.getCoins());
        model.addStudentsToClouds();
        model.takeStudentsFromCloud(player1.getId(), 0);
        player1.takeStudentsFromCloud(cloud1);
        player2.takeStudentsFromCloud(cloud2);

        model.moveStudentToIsland(player1.getId(), player1.getSchool().getEntrance().getNumPawns()-1, island.getId());
        model.moveStudentToIsland(player1.getId(), player1.getSchool().getEntrance().getNumPawns()-1, island.getId());
        model.moveStudentToIsland(player1.getId(), player1.getSchool().getEntrance().getNumPawns()-1, island.getId());

        /*System.out.println(player1.getSchool().getProfessorsTable().getPawns().stream().map(Professor::getColor).toList());
        System.out.println(island.getPawns().stream().map(Student::getColor).toList());*/

        player2.moveStudentIsland(player2.getSchool().getEntrance().getPawns().get(0), island);
        player2.moveStudentIsland(player2.getSchool().getEntrance().getPawns().get(0), island);
        player2.moveStudentIsland(player2.getSchool().getEntrance().getPawns().get(0), island);

        model.playCharacterCard(0, 5);
        System.out.println("Player1 coin size after playing character: " + player1.getCoins());

        Player mostInfluentialPlayer = model.getHandler().getMostInfluentialPlayer(player1, island);
        System.out.println("mostInfluentialPlayer: " + mostInfluentialPlayer);

        assertEquals(player1, mostInfluentialPlayer);
    }

    @Test
    void card04Test() throws Exception {
        model.getPlayers().get(0).increaseCoinBy(10);
        model.playCharacterCard(0, 4);
        model.playAssistantCard(0, 6);
        model.moveMotherNature(0, 2);
        assertTrue(model.getHandler() instanceof InfluenceHandler);
    }

    @Test
    void card06Test() throws Exception {
        model.getPlayers().get(0).increaseCoinBy(10);
        model.playCharacterCard(0, 6);
        model.playAssistantCard(0, 6);
        model.extraAction(0,0);
        assertTrue(model.getHandler() instanceof InfluenceHandler);
    }

}