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

/**
 * Controller class for the Model
 */
public class Controller implements RequestListener {

    /**
     * Model, state of the game
     */
    private transient Model model;

    /**
     * Number of students that can be moved in a round
     */
    private final int numStudentToMove;

    /**
     * Wizards List for player choice at the start of the game
     */
    private final List<Wizard> wizards;

    /**
     * Players that have yet to play the round
     */
    private List<Player> playersToPlay;

    /**
     * Players that have already played this round
     */
    private List<Player> playersHavePlayed;

    /**
     * Planning Phase for this round
     */
    private PlanningPhase planning;

    /**
     * Action Phase for this round
     */
    private ActionPhase action;

    /**
     * Has the player ended his actions
     */
    private boolean isPlanningFinished;

    /**
     * Controller constructor. Initializes parameters, adds students to the clouds
     * @param players Players to play this round
     * @param model Current model
     * @param numStudentToMove Number of students to move
     */
    public Controller(List<Player> players, Model model, int numStudentToMove) {

        this.playersToPlay = players;
        this.playersHavePlayed = new ArrayList<>();
        this.numStudentToMove = numStudentToMove;
        this.model = model;

        this.wizards = new ArrayList<>(Arrays.asList(Wizard.values()));

        this.planning = new PlanningPhase(playersToPlay.get(0), this.model);
        this.action = null;
        this.isPlanningFinished = false;

        this.model.addStudentsToClouds();
    }

    /**
     * Plays the chosen Assistant Card
     * @param playerId Player that has played the Assistant Card
     * @param choice Assistant Card chosen
     * @throws CardException if exception is thrown in PlanningPhase.playAssistantCard()
     * @see PlanningPhase
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws InvalidActionException if the requested action is invalid
     */
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

    /**
     * Play the chosen Character Card
     * @param playerId Player ID that chooses the Character Card
     * @param choice chosen Character Card's ID
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws NotEnoughCoinsException if the player doesn't have enough coins
     * @throws CardException if an exception is Thrown in ActionPhase.playCharacterCard()
     * @throws InvalidActionException if the requested action is invalid
     */
    public void playCharacterCard(int playerId, int choice) throws NotPlayerTurnException, NotEnoughCoinsException, CardException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty() && model.isCompleteRule()) {
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

    /**
     * Moves the student to the player's Dining Room
     * @param playerId player that requested the action
     * @param choice chosen pawn to move
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws InvalidActionException if the requested action is invalid
     */
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

    /**
     * Moves the student to the chosen island
     * @param playerId player that requested the action
     * @param student student to move
     * @param island destination to move the student to
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws IslandException if the island chosen is invalid
     * @throws InvalidActionException if requested action is invalid
     */
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

    /**
     * Moves MotherNature
     * @param playerId player that requested the action
     * @param choice number of steps to move MotherNature
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws MotherNatureStepsException invalid MotherNature steps requested
     * @throws InvalidActionException if the requested action is invalid
     */
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

    /**
     * Take all the students from a cloud
     * @param playerId player that requested the action
     * @param choice chosen cloud
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws CloudException if invalid cloud chosen
     * @throws InvalidActionException if the requested action is invalid
     */
    public void takeStudentsFromCloud(int playerId, int choice) throws NotPlayerTurnException, CloudException, InvalidActionException {
        if (isPlanningFinished && playersToPlay.size() > 0 && wizards.isEmpty() && model.isTakeCloud()) {
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

    /**
     * Extra Action method helper, if the played character card contemplates an additional action
     * @param playerId player that requested the action
     * @param values options varying based on the played card
     * @throws Exception if any exception is thrown by the different called methods
     */
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

    /**
     * Ends the playerId's Action
     * @param playerId player whose action is ended
     * @throws NotPlayerTurnException if it's not the playerId's turn
     * @throws InvalidActionException if requested action is invalid
     */
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

    /**
     * Choose the wizard at the start of the game
     * @param playerId playerId's choice
     * @param choice choice of the wizard
     * @throws InvalidActionException
     */
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

    /**
     * @return possible Options that can be played and called
     */
    public List<String> getOptions() {
        if (wizards.isEmpty()) {
            if (!isPlanningFinished) return planning.getOptions();
            return action.getOptions();
        } else {
            return new ArrayList<>(List.of("chooseWizard"));
        }
    }

    /**
     * @return true if no other player has to play this round
     */
    private boolean isRoundEnded() {
        return playersToPlay.size() == 0 && isPlanningFinished;
    }

    /**
     * Ends player's Planning Phase
     * @param player to end his planning phase
     */
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

    /**
     * Ends player's Action Phase
     * @param player to end his action phase
     */
    private void endPlayerAction(Player player) {
        playersToPlay.remove(player);
        playersHavePlayed.add(player);
        if (playersToPlay.size() > 0) {
            action = new ActionPhase(playersToPlay.get(0), numStudentToMove, model);
        }
        if (isRoundEnded()) {
            if (model.isLastRound()) model.findWinner();
            else {
                orderPlayersForNextRound();
            }
            model.resetLastAssistantCards();
        }
    }

    /**
     * Correctly sets the playing order of the players, based on the assistant card chosen
     */
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

    /**
     * Correctly sets the order for the next round's Planning Phase choice
     */
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

    /**
     * @return playersHavePlayed new List of Player
     */
    public List<Player> getPlayersHavePlayed() {
        return new ArrayList<>(playersHavePlayed);
    }

    /**
     * @return playersToPlay new List of Player
     */
    public List<Player> getPlayersToPlay() {
        return new ArrayList<>(playersToPlay);
    }

    /**
     * @return get the currently active player, if there are any
     */
    public Player getActivePlayer() {
        if (playersToPlay.size() > 0) return playersToPlay.get(0);
        return null;
    }

    /**
     * Given the requestedEvent, invoke the correct method
     * @param requestEvent event requested.
     * @throws NoSuchMethodException the requested method does not exist
     * @throws InvocationTargetException wrapped exception thrown by an invoked method
     * @throws IllegalAccessException if the method doesn't have the correct access authorization to the invoked class or field
     */
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

    /**
     * Rebuilds a model after having loaded a serialized model
     * @param model to restore to
     */
    public void restoreAfterDeserialization(Model model) {
        this.model = model;
        rebuildPlayer(model.getPlayers());

        if (action != null) action.setModel(this.model, playersToPlay.get(0));
        if (planning != null) planning.setModel(this.model, playersToPlay.get(0));
    }

    /**
     * Rebuilds the players after having loaded a serialized model
     * @param players to rebuild
     */
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

    /**
     * Launches the event containing the playable actions for the player, only if there is no winner
     */
    private void fireOptionEvent() {
        if (!this.model.isThereWinner()) this.model.fireAnswer(new AnswerEvent("options", getOptions()));
    }
}
