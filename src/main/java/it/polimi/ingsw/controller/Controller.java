package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


//Class Round keep players sorted

public class Controller {

    private List<Player> playersToPlay;
    private List<Player> playersHavePlayed;

    private Model model;

    private PlanningPhase planning;
    private ActionPhase action;

    private boolean isPlanningFinished;

    private final int numStudentToMove;

    public Controller(List<Player> players, List<Cloud> clouds, StudentBag bag, int numStudentToMove, Model model) {

        this.playersToPlay = players;
        this.playersHavePlayed = new ArrayList<>();
        this.numStudentToMove = numStudentToMove;
        this.model = model;

        this.isPlanningFinished = false;
        this.planning = null;
        this.action = null;

    }

    public PlanningPhase startPlanningPhase(int playerID) {
        Player player = this.playersToPlay.get(0);
        if (player.getId() == playerID) {
            planning = new PlanningPhase(player,this.model);
            return planning;
        }
        return null;
    }

    public PlanningPhase endPlanningPhase(int playerID) {
        Player player = this.playersToPlay.get(0);
        if (player.getId() == playerID) {
            planning = null;

        }
        return planning;
    }


    public ActionPhase startActionPhase(int playerID) {
        if (isPlanningFinished && action == null) {
            Player player = this.playersToPlay.get(0);
            if (player.getId() == playerID) {
                List<Player> otherPlayers = new ArrayList<>(playersToPlay);
                otherPlayers.remove(player);
                otherPlayers.addAll(playersHavePlayed);
                action = new ActionPhase(player, otherPlayers, numStudentToMove, model);
                return action;
            }
        }
        return null;
    }

    public ActionPhase endActionPhase(int playerID) {
        if (action != null) {
            if (this.action.getPlayer().getId() == playerID && action.isEnded()) {
                playersHavePlayed.add(playersToPlay.remove(0));
                action = null;
            }
        }
        return action;
    }

    protected List<Player> end() {
        orderPlayersForNextRound();
        return new ArrayList<>(this.playersToPlay);
    }

    private void orderPlayersForAction(){
        this.playersHavePlayed =
                this.playersHavePlayed.stream()
                .sorted(Comparator.comparingInt(x -> x.getLastAssistantCard().getValue()))
                .collect(Collectors.toList());
        playersToPlay = playersHavePlayed;
        playersHavePlayed = new ArrayList<>();
    }


    private void orderPlayersForNextRound(){
        List<Player> temp = new ArrayList<>();
        orderPlayersForAction();
        int firstPlayerId;
        temp.add(this.playersHavePlayed.remove(0));
        firstPlayerId = temp.get(0).getId();

        temp.addAll(this.playersHavePlayed
                .stream()
                .sorted(Comparator.comparingInt(x -> (x.getId() + firstPlayerId) % 3)).toList());

        this.playersToPlay = temp;
    }

    public List<Player> getPlayersHavePlayed() {
        return new ArrayList<>(playersHavePlayed);
    }
}
