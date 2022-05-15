package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ThinModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CLIBuilder {

    public CLI buildCLI(ThinModel model) {
        CLI cli = new CLI(model.getNumPlayers());
        Map<Color, Integer> s;
        for (int i = 0; i < model.getNumIslands(); i++) {
            s = model.getStudentOnIsland(i);
            for (Color c : s.keySet()) {
                for (int j = 0; j < s.get(c); j++) {
                    cli.addStudentToBoard(cli.getIslands().get(i), c);
                }
            }
        }
        return cli;
    }

    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("player0");
        names.add("player1");
        Model model = new ModelBuilder().buildModel(names);
        ThinModel thinModel = new ThinModel(model);
        CLI cli = new CLIBuilder().buildCLI(thinModel);
        cli.showGameBoard();
    }


}
