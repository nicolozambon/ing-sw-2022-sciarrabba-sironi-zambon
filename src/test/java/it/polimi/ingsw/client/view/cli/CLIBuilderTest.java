package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ThinModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CLIBuilderTest {

    @Test
    void nicknamesOrderingTest() {
        List<String> nicknames = new ArrayList<>(List.of("player0", "player1", "player2"));
        CLIBuilder cliBuilder = new CLIBuilder();
        Model model = new ModelBuilder().buildModel(nicknames, false);
        model.getController();
        ThinModel thinModel = new ThinModel(model);
        CLI cli;

        cli = cliBuilder.buildCLI(thinModel, "player0");
        cli.addCharacterCard(1, 5, "lorem ipsum first card");
        cli.addCharacterCard(1, 5, "lorem ipsum second card");
        cli.addCharacterCard(1, 5, "lorem ipsum third card lorem ipsum lorem ipsum lorem ipsum");
        cli.showGameBoard();

        cli = cliBuilder.buildCLI(thinModel, "player1");
        cli.addCharacterCard(1, 5, "lorem ipsum first card");
        cli.addCharacterCard(1, 5, "lorem ipsum second card");
        cli.addCharacterCard(1, 5, "lorem ipsum third card lorem ipsum lorem ipsum lorem ipsum");
        cli.showGameBoard();

        cli = cliBuilder.buildCLI(thinModel, "player2");
        cli.addCharacterCard(1, 5, "lorem ipsum first card");
        cli.addCharacterCard(1, 5, "lorem ipsum second card");
        cli.addCharacterCard(1, 5, "lorem ipsum third card lorem ipsum lorem ipsum lorem ipsum");
        cli.showGameBoard();
    }

}