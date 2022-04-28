package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.exceptions.InvalidIslandException;
import it.polimi.ingsw.exceptions.InvalidMotherNatureStepsException;
import it.polimi.ingsw.exceptions.NotPlayerTurnException;
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

        planning = new PlanningPhase(playersToPlay.get(0), this.model);

        action = null;
        this.isPlanningFinished = false;
    }

    public void playAssistantCard(int playerId, int choice) throws InvalidCardException, NotPlayerTurnException {
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void playCharacterCard(int playerId, int choice) throws NotPlayerTurnException{
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void moveStudentToDiningRoom(int playerId, int choice) throws NotPlayerTurnException{
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void moveStudentToIsland(int playerId, int student, int island) throws NotPlayerTurnException, InvalidIslandException {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (action == null || action.isEnded()) {
                    action = new ActionPhase(player, numStudentToMove, model);
                }
                if (island >= 0 && island < 12) action.moveStudentToIsland(student, island);
                else throw new InvalidIslandException();
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void moveMotherNature(int playerId, int choice) throws NotPlayerTurnException, InvalidMotherNatureStepsException {
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void getStudentsFromCloud(int playerId, int choice) throws NotPlayerTurnException{
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void extraAction(int playerId, int ... values) throws NotPlayerTurnException{
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void endAction(int playerId) throws NotPlayerTurnException{
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
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public List<String> getOptions() {
        if (isPlanningFinished) return planning.getOptions();
        return action.getOptions();
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
        planning = null;
        action = new ActionPhase(playersToPlay.get(0), this.numStudentToMove, this.model);
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
        action = null;
        planning = new PlanningPhase(playersToPlay.get(0), this.model);
    }

    public List<Player> getPlayersHavePlayed() {
        return new ArrayList<>(playersHavePlayed);
    }

    public List<Player> getPlayersToPlay() {
        return new ArrayList<>(playersToPlay);
    }

    public Player getActivePlayer() {
        if (playersToPlay.size() > 0) return playersToPlay.get(0);
        return null;
    }

}
