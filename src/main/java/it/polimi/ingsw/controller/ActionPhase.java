package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.CharacterCardException;
import it.polimi.ingsw.exceptions.CloudException;
import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Class ActionPhase keep track of action for the player

public class ActionPhase {

    private final Model model;
    private final Player currentPlayer;
    private final Map<String, Integer> callableMethod;


    public ActionPhase(Player currentPlayer, int numOfStudentToMove, Model model) {
        this.currentPlayer = currentPlayer;
        this.model = model;

        callableMethod = new HashMap<>(){{
            this.put("moveStudentToDiningRoom", numOfStudentToMove);
            this.put("moveStudentToIsland", numOfStudentToMove);
            this.put("moveMotherNature", 0);
            this.put("playCharacterCard", 1);
            this.put("takeStudentsFromCloud", 0);
            this.put("extraAction", 0);
            this.put("endAction", 0);
        }};
    }

    public void playCharacterCard(int choice) throws NotEnoughCoinsException, CharacterCardException {
        if (callableMethod.get("playCharacterCard") > 0) {
            this.model.playCharacterCard(this.currentPlayer.getId(), choice);
            callableMethod.put("playCharacterCard", callableMethod.get("playCharacterCard") - 1);
            if (this.model.getCharacterCards().get(choice).getHasExtraAction()) {

                callableMethod.put("extraAction", 1);
            }

        }
    }

    public void moveStudentToDiningRoom(int choice) {
        if(callableMethod.get("moveStudentToDiningRoom") > 0) {
            this.model.moveStudentToDiningRoom(this.currentPlayer.getId(), choice);
            moveStudentCounter();
        }
    }

    public void moveStudentToIsland(int studentChoice, int islandChoice) {
        if(callableMethod.get("moveStudentToIsland") > 0) {
            this.model.moveStudentToIsland(this.currentPlayer.getId(), studentChoice, islandChoice);
            moveStudentCounter();
        }
    }

    public void moveMotherNature(int stepsChoice) throws MotherNatureStepsException {
        if(callableMethod.get("moveMotherNature") > 0) {
            this.model.moveMotherNature(this.currentPlayer.getId(), stepsChoice);
            callableMethod.put("moveMotherNature", callableMethod.get("moveMotherNature") - 1);
            if (callableMethod.get("moveMotherNature") < 1) {
                callableMethod.put("takeStudentsFromCloud", 1);
            }
        }
    }

    public void takeStudentsFromCloud(int choice) throws CloudException {
        if(callableMethod.get("takeStudentsFromCloud") > 0) {
            this.model.takeStudentsFromCloud(this.currentPlayer.getId(), choice);
            callableMethod.put("takeStudentsFromCloud", callableMethod.get("takeStudentsFromCloud") - 1);
            if (callableMethod.get("takeStudentsFromCloud") < 1 && (callableMethod.get("extraAction") < 1 || callableMethod.get("playCharacterCard") > 0)) {
                callableMethod.put("endAction", 1);
            }
        }
    }

    public void extraAction(int ... values) {
        if(callableMethod.get("extraAction") > 0) {
            this.model.extraAction(values);
            callableMethod.put("extraAction", callableMethod.get("extraAction") - 1 );
        }
    }

    public void endAction(){
        if(callableMethod.get("endAction") > 0) {
            callableMethod.replaceAll((s, v) -> 0);
        }
    }

    public boolean isEnded() {
      for (String s : callableMethod.keySet()) {
          if (callableMethod.get(s) > 0) return false;
      }
      this.model.resetHandler();
      return true;
    }

    private void moveStudentCounter() {
        callableMethod.put("moveStudentToIsland", callableMethod.get("moveStudentToIsland") - 1);
        callableMethod.put("moveStudentToDiningRoom", callableMethod.get("moveStudentToDiningRoom") - 1);
        if (callableMethod.get("moveStudentToIsland") < 1 && callableMethod.get("moveStudentToDiningRoom") < 1) {
            callableMethod.put("moveMotherNature", 1);
        }
    }

    public List<String> getOptions() {
        return new ArrayList<>(callableMethod.entrySet().stream()
                                                        .filter(x -> x.getValue() > 0)
                                                        .map(Map.Entry::getKey).toList());
    }

}
