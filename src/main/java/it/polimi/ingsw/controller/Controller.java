package it.polimi.ingsw.controller;

import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Controller implements RequestListener {

    private transient Model model;
    private final int numStudentToMove;
    private final List<Wizard> wizards;
    private List<Player> playersToPlay;
    private List<Player> playersHavePlayed;
    private PlanningPhase planning;
    private ActionPhase action;
    private boolean isPlanningFinished;

    public Controller(List<Player> players, Model model, int numStudentToMove) {

        this.playersToPlay = players;
        this.playersHavePlayed = new ArrayList<>();
        this.numStudentToMove = numStudentToMove;
        this.model = model;

        this.wizards = new ArrayList<>(Arrays.asList(Wizard.values()));

        this.planning = new PlanningPhase(playersToPlay.get(0), this.model);
        this.model.addStudentsToClouds();

        this.action = null;
        this.isPlanningFinished = false;
    }

    public void playAssistantCard(int playerId, int choice) throws CardException, NotPlayerTurnException, InvalidActionException {
        if (!isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                planning.playAssistantCard(choice);
                if (planning.isEnded()) {
                    endPlayerPlanning(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void playCharacterCard(int playerId, int choice) throws NotPlayerTurnException, NotEnoughCoinsException, CardException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                action.playCharacterCard(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void moveStudentToDiningRoom(int playerId, int choice) throws NotPlayerTurnException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                choice--;
                action.moveStudentToDiningRoom(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void moveStudentToIsland(int playerId, int student, int island) throws NotPlayerTurnException, IslandException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                island--;
                student--;
                if (island >= 0 && island < 12) action.moveStudentToIsland(student, island);
                else throw new IslandException();
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void moveMotherNature(int playerId, int choice) throws NotPlayerTurnException, MotherNatureStepsException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                action.moveMotherNature(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void takeStudentsFromCloud(int playerId, int choice) throws NotPlayerTurnException, CloudException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                choice--;
                action.takeStudentsFromCloud(choice);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void extraAction(int playerId, int... values) throws Exception {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                action.extraAction(values);
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void endAction(int playerId) throws NotPlayerTurnException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty()) {
            Player player = playersToPlay.get(0);
            if (player.getId() == playerId) {
                action.endAction();
                if (action.isEnded()) {
                    endPlayerAction(player);
                }
                fireOptionEvent();
            } else {
                throw new NotPlayerTurnException();
            }
        } else throw new InvalidActionException("Not valid action!");
    }

    public void chooseWizard(int playerId, int choice) throws InvalidActionException {
        Wizard wizard = Arrays.stream(Wizard.values()).filter(w -> w.ordinal() == choice).findFirst().get();
        if (wizards.contains(wizard)) {
            model.setWizard(playerId, wizard);
            wizards.remove(wizard);

            if ((playersToPlay.size() == 3 && wizards.size() == 1) || (playersToPlay.size() == 2 && wizards.size() == 2)) {
                wizards.clear();
                this.model.fireAnswer(new AnswerEvent("update", this.model));
                fireOptionEvent();
            } else this.model.fireAnswer(new AnswerEvent("wait", playerId));
        } else throw new InvalidActionException("Wizard already taken! Retry");
    }

    public List<String> getOptions() {
        if (wizards.isEmpty()) {
            if (!isPlanningFinished) return planning.getOptions();
            return action.getOptions();
        } else {
            return new ArrayList<>(List.of("chooseWizard"));
        }

    }

    private boolean isRoundEnded() {
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

    private void orderPlayersForAction() {
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

    private void orderPlayersForNextRound() {
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
        switch (methodName) {

            case "moveStudentToIsland" -> {
                Method method = Controller.class.getDeclaredMethod(methodName, int.class, int.class, int.class);
                method.invoke(this, requestEvent.getPlayerId(), values[0], values[1]);
            }

            case "extraAction" -> {
                Method method = Controller.class.getDeclaredMethod(methodName, int.class, int[].class);
                method.invoke(this, requestEvent.getPlayerId(), values);
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

    public void restoreAfterDeserialization(Model model) {
        this.model = model;
        rebuildPlayer(model.getPlayers());

        if (action != null) action.setModel(this.model, playersToPlay.get(0));
        if (planning != null) planning.setModel(this.model, playersToPlay.get(0));
    }

    private void rebuildPlayer(List<Player> players) {
        List<Player> temp = new ArrayList<>();
        for (int i = 0; i < playersToPlay.size(); i++) {
            temp.add(players.get(playersToPlay.get(i).getId()));
        }
        playersToPlay = temp;

        temp = new ArrayList<>();
        for (int i = 0; i < playersHavePlayed.size(); i++) {
            temp.add(players.get(playersHavePlayed.get(i).getId()));
        }
        playersHavePlayed = temp;
    }

    private void fireOptionEvent() {
        if (!this.model.isThereWinner()) this.model.fireAnswer(new AnswerEvent("options", getOptions()));
    }
}
