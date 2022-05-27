package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.model.ThinModel;

import java.util.ArrayList;

public class CLIBuilder {

    public CLI buildCLI(ThinModel model) {
        CLI cli = new CLI((ArrayList<String>) model.getNicknames());
        return cli;
    }

}
