package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DemoLauncher {

    public static void main(String[] args) throws IOException {

        CLI cli = new CLI(3);



        // Show updated game board
        cli.showGameBoard();
    }

}
