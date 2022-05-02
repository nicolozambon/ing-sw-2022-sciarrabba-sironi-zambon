package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovementHandlerTest {
    Model model;
    Handler handler;

    @BeforeEach
    void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        List<String> playersNames = new ArrayList<>();

        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames);
        handler = new HandlerFactory().buildHandler(model.getPlayers());
    }
    @Test
    void extraActionCard10() throws NotEnoughCoinsException, InvalidCardException {
        Player player1 = model.getPlayers().get(0);

        ArrayList<Student> cloud1List = new ArrayList<>();
        ArrayList<Student> cloud2List = new ArrayList<>();

        Student student1 = new Student(Color.YELLOW);
        Student student2 = new Student(Color.YELLOW);
        Student student3 = new Student(Color.YELLOW);

        Student student4 = new Student(Color.GREEN);
        Student student5 = new Student(Color.GREEN);
        Student student6 = new Student(Color.GREEN);

        cloud1List.add(student1);
        cloud1List.add(student2);
        cloud1List.add(student3);

        cloud2List.add(student4);
        cloud2List.add(student5);
        cloud2List.add(student6);

        Cloud cloud1 = new Cloud(cloud1List);
        Cloud cloud2 = new Cloud(cloud2List);

        player1.takeStudentsFromCloud(cloud1);
        player1.takeStudentsFromCloud(cloud2);

        player1.moveStudentDiningRoom(student4, 20);
        player1.moveStudentDiningRoom(student5, 20);
        player1.moveStudentDiningRoom(student6, 20);

        System.out.println(player1.getSchool().getEntrance().getNumPawns());

        model.playCharacterCard(0, 7);
        handler.extraAction(player1, model, 0, 8, 1, 8);

        int num = player1.getSchool().getDiningRoomByColor(Color.GREEN).getNumPawns();
        //assertEquals(2, num);
    }

    @Test
    void extraActionCard12() {

    }

    @Test
    void professorControl() {

    }
}
