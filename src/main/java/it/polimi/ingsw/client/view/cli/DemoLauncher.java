package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {

        final AssetsLoader loader = new AssetsLoader();

        String[][] school = loader.getSchool(false);
        System.out.println(school.length);
        System.out.println(school[0].length);

        String[][] island = loader.getIsland();
        System.out.println(island.length);
        System.out.println(island[0].length);

        String[][] cloud = loader.getIsland();
        System.out.println(cloud.length);
        System.out.println(cloud[0].length);

        String[][] character_card = loader.getIsland();
        System.out.println(character_card.length);
        System.out.println(character_card[0].length);

        /*

        CLI cli = new CLI(3);



        // Show updated game board
        cli.showGameBoard();

         */
    }

}
