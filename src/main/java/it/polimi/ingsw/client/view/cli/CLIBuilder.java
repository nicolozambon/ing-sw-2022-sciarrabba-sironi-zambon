package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.model.ThinModel;

public class CLIBuilder {

    public CLI buildCLI(ThinModel model) {
        CLI cli = new CLI(model.getNumPlayers(), model.getNicknames());
        return cli;
    }

}
