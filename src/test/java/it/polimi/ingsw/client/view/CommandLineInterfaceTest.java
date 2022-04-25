package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.view.cli.CLI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandLineInterfaceTest {

    @Test
    void showGameBoard() {
        CLI cli = new CLI();
        cli.showGameBoard();
        assertTrue(true);
    }

}

