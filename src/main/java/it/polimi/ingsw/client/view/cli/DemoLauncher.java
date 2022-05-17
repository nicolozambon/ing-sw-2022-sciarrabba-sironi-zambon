package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {

        CLI cli = new CLI(3);

        cli.addCharacterCard(1, 5, "lorem ipsum first card");
        cli.addCharacterCard(1, 5, "lorem ipsum second card");
        cli.addCharacterCard(1, 5, "lorem ipsum third card");

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

        cli.addStudentToIsland(0, Color.BLUE);
        cli.addStudentToIsland(0, Color.YELLOW);
        cli.addStudentToIsland(0, Color.GREEN);
        cli.addStudentToIsland(0, Color.RED);
        cli.addStudentToIsland(0, Color.PINK);

        cli.addMotherNatureToIsland(0);
        cli.addTowerToIsland(0, TowerColor.WHITE);

        // Show updated game board
        cli.showGameBoard();
    }

}
