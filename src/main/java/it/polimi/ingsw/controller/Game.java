package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.ActionPhase;
import it.polimi.ingsw.controller.round.Round;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private List<Player> players;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final MotherNature motherNature;
    private final List<CharacterCard> characterCards;
    private final List<Coin> coins;
    private final StudentBag bag;
    private final int numStudentToMove;

    private Round round;
    private ActionPhase actionPhase;

    private Map<Integer, Integer> numStudentToMovePerPlayer = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        //this.put(4, 3);
    }};

    public Game(List<Player> players, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
                List<CharacterCard> characterCards, List<Coin> coins, StudentBag bag) {
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

    public void startRound() {
        round = new Round(players, clouds, bag, numStudentToMove);
    }

    public void playCharacterCard(int player_id, int choice) {
        round.playAssistantCard(player_id, choice);
    }

    public void startActionPhase(int player_id) {
        actionPhase = round.startActionPhase(player_id);
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

    public void extraAction(int player_id, Object o) {
        Player player = actionPhase.getPlayer();
        if (player.ID == player_id) {
            actionPhase.extraAction(o);
        }
    }

    public void endActionPhase(int player_id) {
        actionPhase = round.endActionPhase(player_id);
    }

    public void endRound() {
        players = round.end();
        round = null;
    }

}
