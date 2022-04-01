package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.ActionPhase;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private List<Player> players;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final MotherNature motherNature;
    private final List<CharacterCard> characterCards;
    private int coins;
    private final StudentBag bag;
    private final int numStudentToMove;

    private Controller controller;
    private ActionPhase actionPhase;

    private Map<Integer, Integer> numStudentToMovePerPlayer = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        //this.put(4, 3);
    }};

    public Model(List<Player> players, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
                 List<CharacterCard> characterCards, int coins, StudentBag bag) {
        this.players = players;
        this.islands = islands;
        this.clouds = clouds;
        this.motherNature = motherNature;
        this.characterCards = characterCards;
        this.coins = coins;
        this.bag = bag;
        this.actionPhase = null;
        this.numStudentToMove = numStudentToMovePerPlayer.get(this.players.size());
    }

    public void playAssistantCard(int player_id, int choice) {

    }

    public void playCharacterCard(int player_id, int choice) {
        controller.playAssistantCard(player_id, choice);
    }


    public void moveStudentToDiningRoom(int player_id, int choice) {
        Player player = actionPhase.getPlayer();
        if (player.ID == player_id){
            Student student = player.getSchool().getEntrance().getPawns().get(choice);
            actionPhase.moveStudentToDiningRoom(student, coins);
        }
    }

    public void moveStudentToIsland(int player_id, int studentChoice, int islandChoice) throws IllegalActionException {
        Player player = actionPhase.getPlayer();
        if (player.ID == player_id){
            Island island = islands.get(islandChoice);
            Student student = player.getSchool().getEntrance().getPawns().get(studentChoice);
            actionPhase.moveStudentToIsland(student, island);
        }
    }

    public void moveMotherNature(int player_id, int stepsChoice) {
        Player player = actionPhase.getPlayer();
        if (player.ID == player_id){
            actionPhase.moveMotherNature(motherNature, stepsChoice);
        }
    }

    public void getStudentsFromCloud(int player_id, int choice) {
        Player player = actionPhase.getPlayer();
        if (player.ID == player_id){
            actionPhase.getStudentsFromCloud(clouds.get(choice));
        }
    }

}
