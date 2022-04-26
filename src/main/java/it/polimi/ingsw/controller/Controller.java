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
    }

    protected void playAssistantCard(int playerId, int choice) {
        if (!isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                planning = new PlanningPhase(player, model);
                planning.playAssistantCard(choice);
                if (planning.isEnded()) {
                    endPlayerTurn(player);
                }
            }
            if (playersToPlay.size() < 0) {
                endPlanningPhase();
            }
        }
    }



    private void endPlanningPhase() {
        isPlanningFinished = true;
    }

    private void endPlayerTurn(Player player) {
        playersToPlay.remove(player);
        playersHavePlayed.add(player);
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
