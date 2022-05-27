package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
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

        CLI cli = new CLI(nicknamesCLI);
        buildIsland(cli, model);
        buildCloud(cli, model);
        buildCharacterCards(cli, model);
        buildAssistantCards(cli, model);
        buildSchool(cli, model);

        return cli;
    }

    private void buildIsland(CLI cli, ThinModel model) {
        for (int i = 0; i < model.getNumIslands(); i++) {
            Map<Color, Integer> studentsMap = model.getStudentOnIsland(i);
            for (Color color : Color.values()) {
                if (studentsMap.get(color) != 0) cli.addStudentsToIsland(i, color, studentsMap.get(color));
            }
            if (model.getTowerColorOnIsland(i) != null) cli.addTowerToIsland(i, model.getTowerColorOnIsland(i));
            if (model.isIslandLinkedNext(i)) cli.addLinkToNextIsland(i);
        }
        cli.addMotherNatureToIsland(model.getMNPosition());

    }

    private void buildCloud(CLI cli, ThinModel model) {
        for (int i = 0; i < model.getNumClouds(); i++) {
            List<Color> students = model.getStudentOnCloud(i);
            for (Color color : students) {
                cli.addStudentToCloud(i, color);
            }
        }
    }

    //TODO build character card
    private void buildCharacterCards(CLI cli, ThinModel model) {
        /*for (CharacterCard card : model.getCharacterCards()) {
            cli.addCharacterCard(card);
        }*/
    }

    //TODO: build assistant card
    private void buildAssistantCards(CLI cli, ThinModel model) {

    }

    private void buildSchool(CLI cli, ThinModel model) {
        for (Integer id : idMap.keySet()) {
            //Entrance
            for (Color color : model.getEntranceById(id)) {
                cli.addStudentToSchoolEntrance(idMap.get(id), color);
            }
            //DiningRoom
            Map<Color, Integer> diningRoom = model.getDiningRoomById(id);
            for (Color color : diningRoom.keySet()) {
                for (int i = 0; i < diningRoom.get(color); i++) {
                    cli.addStudentToSchoolDiningRoom(idMap.get(id), color);
                }
            }
            //TowerBoard
            TowerColor towerColor = model.getTowerColorByPlayer(id);
            for (int i = 0; i < model.getNumTowerByPlayer(id); i++) {
                cli.addTowerToSchool(idMap.get(id), towerColor);
            }
            //Professor
            for (Color color : model.getProfessorsByPlayer(id)) {
                cli.addProfessorToSchool(idMap.get(id), color);
            }
        }
    }





}
