package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ThinModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CLIBuilderTest {

    @Test
    void nicknamesOrderingTest() throws IOException {
        List<String> nicknames = new ArrayList<>(List.of("player0", "player1", "player2"));
        CLIBuilder cliBuilder = new CLIBuilder();
        Model model = new ModelBuilder().buildModel(nicknames, true, true);
        model.getController();
        ThinModel thinModel = new ThinModel(model);
        CLI cli;

        cli = cliBuilder.buildCLI(thinModel, "player0");
        cli.showGameBoard();

        cli = cliBuilder.buildCLI(thinModel, "player1");
        cli.showGameBoard();

        cli = cliBuilder.buildCLI(thinModel, "player2");
        cli.showGameBoard();
    }

}