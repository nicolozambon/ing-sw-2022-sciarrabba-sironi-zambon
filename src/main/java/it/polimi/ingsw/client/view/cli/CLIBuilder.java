package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.ThinModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIBuilder {

    private final Map<Integer, Integer> idMap;

    public CLIBuilder() {
        this.idMap = new HashMap<>();
    }

    public CLI buildCLI(ThinModel model, String nickname) {
        List<String> nicknames = model.getNicknames();
        List<String> nicknamesCLI = new ArrayList<>();
        int currentId = nicknames.indexOf(nickname);
        this.idMap.put(currentId, 0);
        nicknamesCLI.add(nickname);

        for (int i = 1; i < nicknames.size(); i++) {
            this.idMap.put((currentId + i) % nicknames.size(), i);
            nicknamesCLI.add(nicknames.get((currentId + i) % nicknames.size()));
        }

        CLI cli = new CLI(model.getNumPlayers(), nicknamesCLI);
        buildIsland(cli, model);
        buildCloud(cli, model);

        return cli;
    }

    private void buildIsland(CLI cli, ThinModel model) {
        for (int i = 0; i < model.getNumIslands(); i++) {
            Map<Color, Integer> studentsMap = model.getStudentOnIsland(i);
            for (Color color : Color.values()) {
                cli.addStudentsToIsland(i, color, studentsMap.get(color));
            }
        }
        cli.addMotherNatureToIsland(model.getMNPosition());

    }

    private void buildCloud(CLI cli, ThinModel model) {
        for (int i = 0; i < model.getNumClouds(); i++) {
            List<Color> students = model.getStudentOnCloud(i);
            System.out.println(students);
            for (Color color : students) {
                cli.addStudentToCloud(i, color);
            }
        }
    }

    private void buildCharacterCards(CLI cli, ThinModel model) {

    }



}
