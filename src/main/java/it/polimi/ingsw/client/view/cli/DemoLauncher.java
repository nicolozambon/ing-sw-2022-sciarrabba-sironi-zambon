package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.io.IOException;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {
        CLI cli = new CLI(3);

        // Get main school
        String[][] mainSchool = cli.getSchools().get(0);

        // Add some professors to the main school
        cli.addProfessorToBoard(mainSchool, Color.BLUE);
        cli.addProfessorToBoard(mainSchool, Color.RED);

        // Add some students to the main school
        cli.addStudentToBoard(mainSchool, Color.BLUE);
        cli.addStudentToBoard(mainSchool, Color.RED);
        cli.addStudentToBoard(mainSchool, Color.RED);
        cli.addStudentToBoard(mainSchool, Color.GREEN);
        cli.addStudentToBoard(mainSchool, Color.GREEN);
        cli.addStudentToBoard(mainSchool, Color.PINK);
        cli.addStudentToBoard(mainSchool, Color.YELLOW);

        // Add some pawns to the first island
        String[][] firstIsland = cli.getIslands().get(0); // Get the first island
        cli.addStudentToBoard(firstIsland, Color.YELLOW);
        cli.addStudentToBoard(firstIsland, Color.RED);
        cli.addStudentToBoard(firstIsland, Color.GREEN);
        cli.addStudentToBoard(firstIsland, Color.PINK);
        cli.addStudentToBoard(firstIsland, Color.BLUE);
        cli.addMotherNatureToBoard(firstIsland);

        // Add some pawns to the second island
        String[][] secondIsland = cli.getIslands().get(1); // Get the second island
        cli.addStudentToBoard(secondIsland, Color.BLUE);
        cli.addStudentToBoard(secondIsland, Color.PINK);
        cli.addStudentToBoard(secondIsland, Color.GREEN);
        cli.addMotherNatureToBoard(secondIsland);

        // Add link between first and second islands
        cli.addLinkToNextIsland(0);

        // Add link between fourth and fifth islands
        cli.addLinkToNextIsland(3);

        // Show updated game board
        cli.showGameBoard();
    }

}
