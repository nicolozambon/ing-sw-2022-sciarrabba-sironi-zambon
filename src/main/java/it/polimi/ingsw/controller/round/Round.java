package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


//Class Round keep players sorted

public class Round {

    private List<Player> playersToPlay;
    private List<Player> playersHavePlayed;

    private boolean isPlanningFinished;

    private PlanningPhase planning;
    private ActionPhase action;

    private final int numStudentToMove;

    public Round(List<Player> players, List<Cloud> clouds, StudentBag bag, int numStudentToMove) {

        this.playersToPlay = players;
        this.playersHavePlayed = new ArrayList<>();
        this.numStudentToMove = numStudentToMove;

        this.isPlanningFinished = false;
        this.planning.addStudentsToCloud(clouds, bag);

        this.action = null;

    }


    public void playAssistantCard(int player_id, int choice) {
        if (!isPlanningFinished) {
            Player player = this.playersToPlay.get(0);
            if (player.ID == player_id) {
                this.planning.playAssistantCard(player, choice);
            }
            playersHavePlayed.add(playersToPlay.remove(0));
        }

        if (playersToPlay.size() == 0) {
            isPlanningFinished = true;
            orderPlayersForAction();
        }
    }

    public ActionPhase startActionPhase(int player_id) {
        if (isPlanningFinished && action == null) {
            Player player = this.playersToPlay.get(0);
            if (player.ID == player_id) {
                List<Player> otherPlayers = new ArrayList<>(playersToPlay);
                otherPlayers.remove(player);
                otherPlayers.addAll(playersHavePlayed);
                action = new ActionPhase(player, otherPlayers, numStudentToMove);
                return action;
            }
        }
        return null;
    }

    public ActionPhase endActionPhase(int player_id) {
        if (action != null) {
            if (this.action.getPlayer().ID == player_id && action.isEnded()) {
                playersHavePlayed.add(playersToPlay.remove(0));
                action = null;
                return action;
            }
        }
        return action;
    }


    public List<Player> end() {
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

        temp.add(this.playersHavePlayed.remove(0));
        temp.addAll(this.playersHavePlayed
                    .stream()
                    .sorted(Comparator.comparingInt(x -> x.ID))
                    .collect(Collectors.toList()));
        this.playersToPlay = temp;
    }

}
