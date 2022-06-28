package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handling of Action Phase of the turn of the player
 */
public class ActionPhase {
    /**
     * Current Model, state of the game
     */
    private transient Model model;
    /**
     * Current Player's Action Phase handling
     */
    private transient Player currentPlayer;
    /**
     * Map containing methods that can be called and played
     */
    private final Map<String, Integer> callableMethod;

    /**
     * ActionPhase Constructor, initializes callableMethod Map
     * @param currentPlayer Player whose turn it is
     * @param numOfStudentToMove Number of Students to move
     * @param model Current Model
     */
    protected ActionPhase(Player currentPlayer, int numOfStudentToMove, Model model) {
        this.currentPlayer = currentPlayer;
        this.model = model;

        this.callableMethod = new HashMap<>(){{
            this.put("moveStudentToDiningRoom", numOfStudentToMove);
            this.put("moveStudentToIsland", numOfStudentToMove);
            this.put("moveMotherNature", 0);
            this.put("takeStudentsFromCloud", 0);
            this.put("extraAction", 0);
            this.put("endAction", 0);
        }};

        if (this.model.isCompleteRule()) this.callableMethod.put("playCharacterCard", 1);
        else this.callableMethod.put("playCharacterCard", 0);
    }

    /**
     * Method for playing a character card in this turn
     * @param choice CharacterCard to Play
     * @throws NotEnoughCoinsException if the player doesn't have enough coins
     * @throws CardException Exception caused by an invalid action upon playing the card
     */
    protected void playCharacterCard(int choice) throws NotEnoughCoinsException, CardException {
        if (callableMethod.get("playCharacterCard") > 0) {
            CharacterCard card = this.model.playCharacterCard(this.currentPlayer.getId(), choice);
            callableMethod.put("playCharacterCard", callableMethod.get("playCharacterCard") - 1);
            if (card.getHasExtraAction()) {
                callableMethod.put("extraAction", 1);
                if (this.model.getCharacterCardIdFromHandler() == 7) callableMethod.put("extraAction", 2);

            }
        }
    }

    /**
     * Moves a student from the entrance to the player's Dining Room
     * @param choice student to move
     * @throws InvalidActionException if action is not possible.
     */
    protected void moveStudentToDiningRoom(int choice) throws InvalidActionException {
        if(callableMethod.get("moveStudentToDiningRoom") > 0) {
            this.model.moveStudentToDiningRoom(this.currentPlayer.getId(), choice);
            moveStudentCounter();
        }
    }

    /**
     * Moves a student from the entrance to an island.
     * @param studentChoice student to move
     * @param islandChoice island destination of the pawn
     * @throws InvalidActionException if action is not possible
     */
    protected void moveStudentToIsland(int studentChoice, int islandChoice) throws InvalidActionException {
        if(callableMethod.get("moveStudentToIsland") > 0) {
            this.model.moveStudentToIsland(this.currentPlayer.getId(), studentChoice, islandChoice);
            moveStudentCounter();
        }
    }

    /**
     * Moves MotherNature in the islands
     * @param stepsChoice steps to move Mother Nature
     * @throws MotherNatureStepsException if Mother Nature cannot move as requested
     */
    protected void moveMotherNature(int stepsChoice) throws MotherNatureStepsException {
        if(callableMethod.get("moveMotherNature") > 0) {
            this.model.moveMotherNature(this.currentPlayer.getId(), stepsChoice);
            callableMethod.put("moveMotherNature", callableMethod.get("moveMotherNature") - 1);
            if (callableMethod.get("moveMotherNature") < 1) {
                if (model.isTakeCloud()) callableMethod.put("takeStudentsFromCloud", 1);
                else callableMethod.put("endAction", 1);
            }
        }
    }
    
    /**
     * Get all students from the selected cloud, and put the students in the player's entrance
     * @param choice cloud ID to take the student from
     * @throws CloudException
     */
    protected void takeStudentsFromCloud(int choice) throws CloudException {
        if(callableMethod.get("takeStudentsFromCloud") > 0) {
            this.model.takeStudentsFromCloud(this.currentPlayer.getId(), choice);
            callableMethod.put("takeStudentsFromCloud", callableMethod.get("takeStudentsFromCloud") - 1);
            if (callableMethod.get("takeStudentsFromCloud") < 1) {
                callableMethod.put("endAction", 1);
            }
        }
    }

    /**
     * Extra action helper method when playing a character card: if the played character card requires an additional move,
     * this method is invoked
     * @param values options varying based on the action type
     * @throws Exception if action is invalid
     */
    protected void extraAction(int ... values) throws Exception {
        if(callableMethod.get("extraAction") > 0) {
            this.model.extraAction(this.currentPlayer.getId(), values);
            callableMethod.put("extraAction", callableMethod.get("extraAction") - 1);
        }
    }

    /**
     * Ends this ActionPhase for the currentPlayer
     */
    protected void endAction(){
        if(callableMethod.get("endAction") > 0) {
            callableMethod.replaceAll((s, v) -> 0);
        }
    }

    /**
     * Answers if there are additional actions in this ActionPhase that can be played
     * @return true if no more actions are possible in this ActionPhase
     */
    protected boolean isEnded() {
      for (String s : callableMethod.keySet()) {
          if (callableMethod.get(s) > 0) return false;
      }
      this.model.resetHandler();
      return true;
    }

    /**
     * Keeps count of the possible moves of the students in this ActionPhase, depending on the number of players in the model
     */
    private void moveStudentCounter() {
        callableMethod.put("moveStudentToIsland", callableMethod.get("moveStudentToIsland") - 1);
        callableMethod.put("moveStudentToDiningRoom", callableMethod.get("moveStudentToDiningRoom") - 1);
        if (callableMethod.get("moveStudentToIsland") < 1 && callableMethod.get("moveStudentToDiningRoom") < 1) {
            callableMethod.put("moveMotherNature", 1);
        }
    }
    
    /**
     * @return Options and actions that can be still be played
     */
    protected List<String> getOptions() {
        List<String> options = new ArrayList<>(callableMethod.entrySet().stream()
                                                        .filter(x -> x.getValue() > 0)
                                                        .map(Map.Entry::getKey).toList());
        if (options.contains("extraAction")) {
            options.remove("extraAction");
            options.add("card" + model.getCharacterCardIdFromHandler());
        }
        return options;
    }

    /**
     * Setter method for model
     * @param model current Model object
     * @param currentPlayer current player object
     */
    protected void setModel(Model model, Player currentPlayer) {
        this.model = model;
        this.currentPlayer = currentPlayer;
    }

}
