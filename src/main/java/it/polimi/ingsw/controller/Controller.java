package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.model.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


//Class Round keep players sorted

public class Controller implements RequestListener {

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
        this.model.addStudentsToClouds();

        action = null;
        this.isPlanningFinished = false;
    }

    public void playAssistantCard(int playerId, int choice) throws AssistantCardException, NotPlayerTurnException {
        if (!isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                planning.playAssistantCard(choice);
                if (planning.isEnded()) {
                    endPlayerPlanning(player);
                }
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void playCharacterCard(int playerId, int choice) throws NotPlayerTurnException, NotEnoughCoinsException, CharacterCardException {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
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
                action.moveStudentToDiningRoom(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void moveStudentToIsland(int playerId, int student, int island) throws NotPlayerTurnException, IslandException {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                if (island >= 0 && island < 12) action.moveStudentToIsland(student, island);
                else throw new IslandException();
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void moveMotherNature(int playerId, int choice) throws NotPlayerTurnException, MotherNatureStepsException {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                action.moveMotherNature(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
            } else {
                throw new NotPlayerTurnException();
            }
        }
    }

    public void takeStudentsFromCloud(int playerId, int choice) throws NotPlayerTurnException, Exception {
        if (isPlanningFinished && playersToPlay.size() > 0) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                action.takeStudentsFromCloud(choice);
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
        if (!isPlanningFinished) return planning.getOptions();
        return action.getOptions();
    }

    private boolean isRoundEnded(){
        return playersToPlay.size() == 0 && isPlanningFinished;
    }

    private void endPlayerPlanning(Player player) {
        playersToPlay.remove(player);
        playersHavePlayed.add(player);
        if (playersToPlay.size() > 0) {
            planning = new PlanningPhase(playersToPlay.get(0), model);
        }
        if (playersToPlay.size() == 0) {
            orderPlayersForAction();
        }
    }

    private void endPlayerAction(Player player) {
        playersToPlay.remove(player);
        playersHavePlayed.add(player);
        if (playersToPlay.size() > 0) {
            action = new ActionPhase(playersToPlay.get(0), numStudentToMove, model);
        }
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
        this.model.addStudentsToClouds();
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

    @Override
    public void onRequestEvent(RequestEvent requestEvent) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = requestEvent.getPropertyName();
        int[] values = requestEvent.getValues();
        switch(methodName) {

            case "moveStudentToIsland" -> {
                Method method = Controller.class.getDeclaredMethod(methodName, int.class, int.class, int.class);
                method.invoke(this, requestEvent.getPlayerId(), values[0], values[1]);
            }

            case "extraAction" -> {
                Method method = Controller.class.getDeclaredMethod(methodName, int[].class);
                method.invoke(this, (Object) values);
            }

            case "endAction" -> {
                Method method = Controller.class.getDeclaredMethod(methodName, int.class);
                method.invoke(this, requestEvent.getPlayerId());
            }

            default -> {
                Method method = Controller.class.getDeclaredMethod(methodName, int.class, int.class);
                method.invoke(this, requestEvent.getPlayerId(), values[0]);
            }
        }
    }
}
