package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {

        List<String> nicknames = new ArrayList<>();
        nicknames.add("Nicolò");
        nicknames.add("Alessandro");
        nicknames.add("Jonathan");

        CLI cli = new CLI(nicknames);

        CharacterCard characterCard = new CharacterCard(1, 5, "lorem ipsum");
        cli.addCharacterCard(characterCard);
        cli.addCharacterCard(characterCard);
        cli.addCharacterCard(characterCard);

        AssistantCard assistantCard = new AssistantCard(1, 1);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        cli.addAssistantCard(assistantCard);
        //cli.addAssistantCard(assistantCard);

        cli.addLastPlayedAssistantCard(assistantCard);
        cli.addLastPlayedAssistantCard(assistantCard);

        cli.addStudentToSchoolDiningRoom(0, Color.RED);
        cli.addStudentToSchoolDiningRoom(0, Color.BLUE);
        cli.addProfessorToSchool(0, Color.BLUE);
        cli.addProfessorToSchool(1, Color.BLUE);

        cli.addStudentToCloud(0, Color.BLUE);
        cli.addStudentToCloud(1, Color.RED);
        cli.addStudentToCloud(2, Color.YELLOW);


        cli.addStudentsToIsland(5, Color.RED, 5);


        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);

        cli.addStudentToSchoolEntrance(0, Color.BLUE);
        cli.addStudentToSchoolEntrance(0, Color.BLUE);
        cli.addStudentToSchoolEntrance(0, Color.BLUE);
        cli.addStudentToSchoolEntrance(0, Color.RED);

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

        cli.addStudentsToIsland(5, Color.BLUE, 2);
        cli.addStudentsToIsland(5, Color.YELLOW, 3);
        cli.addStudentsToIsland(5, Color.RED, 4);
        cli.addStudentsToIsland(5, Color.PINK, 10);

        cli.addStudentsToIsland(9, Color.BLUE, 2);
        cli.addStudentsToIsland(9, Color.YELLOW, 3);
        cli.addStudentsToIsland(9, Color.GREEN, 4);
        cli.addStudentsToIsland(9, Color.PINK, 10);

        cli.addStudentsToIsland(0, Color.BLUE, 2);
        cli.addStudentsToIsland(0, Color.YELLOW, 3);
        cli.addStudentsToIsland(0, Color.GREEN, 4);
        cli.addStudentsToIsland(0, Color.RED, 4);
        cli.addStudentsToIsland(0, Color.PINK, 10);

        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.RED);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.YELLOW);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.GREEN);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.BLUE);
        cli.addStudentToSchoolDiningRoom(2, Color.PINK);
        cli.addStudentToSchoolDiningRoom(2, Color.PINK);
        cli.addStudentToSchoolDiningRoom(2, Color.PINK);
        cli.addStudentToSchoolDiningRoom(2, Color.PINK);
        cli.addStudentToSchoolDiningRoom(2, Color.PINK);
        cli.addStudentToSchoolDiningRoom(2, Color.PINK);

        cli.addMotherNatureToIsland(0);
        cli.addTowerToIsland(0, TowerColor.WHITE);

        cli.addMotherNatureToIsland(0);
        cli.addTowerToIsland(0, TowerColor.WHITE);
        cli.addStudentsToIsland(0, Color.GREEN, 5);

        // Show updated game board
        cli.showGameBoard();
    }

}
