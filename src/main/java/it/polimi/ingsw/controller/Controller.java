package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalActionException;
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

    public Controller(List<Player> players, Model model, int numStudentToMove) {

        this.playersToPlay = players;
        this.playersHavePlayed = new ArrayList<>();
        this.numStudentToMove = numStudentToMove;
        this.model = model;

        this.isPlanningFinished = false;
    }

    public void playAssistantCard(int playerId, int choice) {
        if (!isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (planning == null || planning.isEnded()) {
                    planning = new PlanningPhase(player, model);
                }
                planning.playAssistantCard(choice);
                if (planning.isEnded()) {
                    endPlayerPlanning(player);
                }
            }
        }
    }

    public void playCharacterCard(int playerId, int choice) {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.playCharacterCard(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    public void moveStudentToDiningRoom(int playerId, int choice) {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.moveStudentToDiningRoom(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    public void moveStudentToIsland(int playerId, int student, int island) throws IllegalActionException {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.moveStudentToIsland(student, island);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    public void moveMotherNature(int playerId, int choice) {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.moveMotherNature(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    public void getStudentsFromCloud(int playerId, int choice) {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.getStudentsFromCloud(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    public void extraAction(int playerId, int ... values) {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.extraAction(values);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    public void endAction(int playerId) {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                action.endAction();
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            }
        }
    }

    private boolean isRoundEnded(){
        return playersToPlay.size() == 0 && isPlanningFinished;
    }

    private void endPlayerPlanning(Player player) {
        playersToPlay.remove(player);
        playersHavePlayed.add(player);
        if (playersToPlay.size() == 0) {
            orderPlayersForAction();
        }
    }

    private void endPlayerAction(Player player) {
        playersToPlay.remove(player);
        playersHavePlayed.add(player);
        if (isRoundEnded()) {
            orderPlayersForNextRound();
        }
    }

    private void orderPlayersForAction(){
        this.playersHavePlayed =
                this.playersHavePlayed.stream()
                .sorted(Comparator.comparingInt(x -> x.getLastAssistantCard().getValue()))
                .collect(Collectors.toList());
        playersToPlay = playersHavePlayed;
        playersHavePlayed = new ArrayList<>();
        isPlanningFinished = true;
    }

    private void orderPlayersForNextRound(){
        List<Player> temp = new ArrayList<>();
        orderPlayersForAction();
        int firstPlayerId;
        temp.add(this.playersToPlay.remove(0));
        firstPlayerId = temp.get(0).getId();

        temp.addAll(this.playersToPlay
                .stream()
                .sorted(Comparator.comparingInt(x -> (x.getId() + firstPlayerId) % 3)).toList());

        this.playersToPlay = temp;
        isPlanningFinished = false;
    }

    public List<Player> getPlayersHavePlayed() {
        return new ArrayList<>(playersHavePlayed);
    }
}
