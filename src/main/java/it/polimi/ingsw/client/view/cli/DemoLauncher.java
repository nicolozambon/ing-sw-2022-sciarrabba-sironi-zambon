package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {

        System.out.println("\u001B[93m" + "ANSI escapes test "+ "\u001B[0m");
        System.out.println("╔═════════════════════════════════════════════════════════════════════════════════════╗\n" +
                "║      ╔═══╗   ░░   ╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗ ╔═══╗   ░░              ║\n" +
                "║      ║EA0║   ░░   ║S00║S01║S02║S03║S04║S05║S06║S07║S08║S09║ ║P00║   ░░   ╔═══╦═══╗  ║\n" +
                "║  ╔═══╬═══╣   ░░   ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣ ╠═══╣   ░░   ║T00║T00║  ║\n" +
                "║  ║EA1║EA2║   ░░   ║S10║S11║S12║S13║S14║S15║S16║S17║S18║S19║ ║P10║   ░░   ╠═══╬═══╣  ║\n" +
                "║  ╠═══╬═══╣   ░░   ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣ ╠═══╣   ░░   ║T00║T00║  ║\n" +
                "║  ║EA3║EA4║   ░░   ║S20║S21║S22║S23║S24║S25║S26║S27║S28║S29║ ║P20║   ░░   ╠═══╬═══╣  ║\n" +
                "║  ╠═══╬═══╣   ░░   ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣ ╠═══╣   ░░   ║T00║T00║  ║\n" +
                "║  ║EA5║EA6║   ░░   ║S30║S31║S32║S33║S34║S35║S36║S37║S38║S39║ ║P30║   ░░   ╠═══╬═══╣  ║\n" +
                "║  ╠═══╬═══╣   ░░   ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣ ╠═══╣   ░░   ║T00║T00║  ║\n" +
                "║  ║EA7║EA8║   ░░   ║S40║S41║S42║S43║S44║S45║S46║S47║S48║S49║ ║P40║   ░░   ╚═══╩═══╝  ║\n" +
                "║  ╚═══╩═══╝   ░░   ╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝ ╚═══╝   ░░              ║\n" +
                "╠═════════════════════════════════════════════════════════════════════════════════════╣\n" +
                "║                                                                                     ║\n" +
                "╚═════════════════════════════════════════════════════════════════════════════════════╝");


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

        // Show updated game board
        cli.showGameBoard();
    }

}
