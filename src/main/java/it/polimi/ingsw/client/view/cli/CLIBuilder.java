package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.ThinModel;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIBuilder {

    //Map from model-id to cli-id
    private final Map<Integer, Integer> idMap;

    public CLIBuilder() {
        this.idMap = new HashMap<>();
    }

    public CLI buildCLI(ThinModel model, String nickname) {
        List<String> nicknames = model.getNicknames();
        List<String> nicknamesCLI = new ArrayList<>();
        List<Integer> coins = new ArrayList<>();
        List<AssistantCard> lastAssistantCards = new ArrayList<>();
        int currentId = nicknames.indexOf(nickname);
        this.idMap.put(currentId, 0);
        nicknamesCLI.add(nickname);
        coins.add(model.getCoinByPlayer(currentId));
        lastAssistantCards.add(model.getLastAssistantCardByPlayer(currentId));

        for (int i = 1; i < nicknames.size(); i++) {
            int nextPlayerId = (currentId + i) % nicknames.size();
            this.idMap.put(nextPlayerId, i);
            nicknamesCLI.add(nicknames.get(nextPlayerId));
            coins.add(model.getCoinByPlayer(nextPlayerId));
            lastAssistantCards.add(model.getLastAssistantCardByPlayer(nextPlayerId));
        }

        CLI cli = new CLI(nicknamesCLI, coins);
        lastAssistantCards.forEach(cli::addLastPlayedAssistantCard);
        cli.setBoardCoins(model.getCoinReserve());
        buildIsland(cli, model);
        buildCloud(cli, model);
        buildCharacterCards(cli, model);
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

    private void buildCharacterCards(CLI cli, ThinModel model) {
        if (model.isCompleteRule()) {
            List<CharacterCard> cards = model.getCharacterCards();
            for (int i = 0; i < 3; i++) {
                cli.addCharacterCard(cards.get(i));
            }
        }
    }

    private void buildSchool(CLI cli, ThinModel model) {
        for (Integer id : idMap.keySet()) {
            //AssistantCard
            if (idMap.get(id) == 0) {
                for (AssistantCard card : model.getAssistantCardsByPlayer(id)) {
                    cli.addAssistantCard(card);
                }
            }
            //Entrance
            for (Color color : model.getEntranceByPlayer(id)) {
                cli.addStudentToSchoolEntrance(idMap.get(id), color);
            }
            //DiningRoom
            Map<Color, Integer> diningRoom = model.getDiningRoomByPlayer(id);
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
