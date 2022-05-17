package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {

        CLI cli = new CLI(3);

        cli.addStudentToSchoolDiningRoom(0, Color.RED);
        cli.addStudentToSchoolDiningRoom(0, Color.BLUE);
        cli.addProfessorToSchool(0, Color.BLUE);
        cli.addStudentToCloud(0, Color.BLUE);


        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(1, Color.BLUE);

        cli.addStudentToSchoolDiningRoom(1, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(1, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(1, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(1, Color.YELLOW);

        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);
        cli.addStudentToSchoolDiningRoom(1, Color.RED);

        cli.addStudentToSchoolDiningRoom(1, Color.PINK);
        cli.addStudentToSchoolDiningRoom(1, Color.PINK);
        cli.addStudentToSchoolDiningRoom(1, Color.PINK);
        cli.addStudentToSchoolDiningRoom(1, Color.PINK);
        cli.addStudentToSchoolDiningRoom(1, Color.PINK);

        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(1, Color.GREEN);




        cli.addLinkToNextIsland(0);
        cli.addLinkToNextIsland(1);
        cli.addLinkToNextIsland(2);
        cli.addLinkToNextIsland(3);
        cli.addLinkToNextIsland(4);
        cli.addLinkToNextIsland(5);
        cli.addLinkToNextIsland(6);
        cli.addLinkToNextIsland(7);
        cli.addLinkToNextIsland(8);
        cli.addLinkToNextIsland(9);
        cli.addLinkToNextIsland(10);
        cli.addLinkToNextIsland(11);

        cli.addMotherNatureToIsland(0);
        cli.addTowerToIsland(0, TowerColor.WHITE);
        cli.addStudentToIsland(0, Color.GREEN);

        // Show updated game board
        cli.showGameBoard();
    }

}
