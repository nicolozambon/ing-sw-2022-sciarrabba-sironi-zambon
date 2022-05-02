package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidMotherNatureStepsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.IllegalActionException;


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
            this.put("move_student_dining", numOfStudentToMove);
            this.put("move_student_island", numOfStudentToMove);
            this.put("move_mothernature", 0);
            this.put("character_card", 1);
            this.put("students_cloud", 0);
            this.put("extra_action", 0);
            this.put("end_action", 0);
        }};
    }

    public void playCharacterCard(int choice) {
        try {
            if (callableMethod.get("character_card") > 0) {
                this.model.playCharacterCard(this.currentPlayer.getId(), choice);
                callableMethod.put("character_card", callableMethod.get("character_card") - 1);
                if (this.model.getCharacterCards().get(choice).getHasExtraAction()) {
                    callableMethod.put("extra_action", 1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveStudentToDiningRoom(int choice) {
        if(callableMethod.get("move_student_dining") > 0) {
            this.model.moveStudentToDiningRoom(this.currentPlayer.getId(), choice);
            moveStudentCounter();
        }
    }

    public void moveStudentToIsland(int studentChoice, int islandChoice) {
        if(callableMethod.get("move_student_island") > 0) {
            this.model.moveStudentToIsland(this.currentPlayer.getId(), studentChoice, islandChoice);
            moveStudentCounter();
        }
    }

    public void moveMotherNature(int stepsChoice) throws InvalidMotherNatureStepsException {
        if(callableMethod.get("move_mothernature") > 0) {
            this.model.moveMotherNature(this.currentPlayer.getId(), stepsChoice);
            callableMethod.put("move_mothernature", callableMethod.get("move_mothernature") - 1);
            if (callableMethod.get("move_mothernature") < 1) {
                callableMethod.put("students_cloud", 1);
            }
        }
    }

    public void getStudentsFromCloud(int choice) {
        if(callableMethod.get("students_cloud") > 0) {
            this.model.takeStudentsFromCloud(this.currentPlayer.getId(), choice);
            callableMethod.put("students_cloud", callableMethod.get("students_cloud") - 1);
            if (callableMethod.get("students_cloud") < 1 && callableMethod.get("extra_action") < 0) {
                callableMethod.put("end_action", 1);
            }
        }
    }

    public void extraAction(int ... values) {
        if(callableMethod.get("extra_action") > 0) {
            this.model.extraAction(values);
            callableMethod.put("extra_action", callableMethod.get("extra_action") - 1 );
        }
    }

    public void endAction(){
        if(callableMethod.get("end_action") > 0) {
            for (String s : callableMethod.keySet()) {
                callableMethod.put(s, 0);
            }
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
        callableMethod.put("move_student_island", callableMethod.get("move_student_island") - 1);
        callableMethod.put("move_student_dining", callableMethod.get("move_student_dining") - 1);
        if (callableMethod.get("move_student_island") < 1 && callableMethod.get("move_student_dining") < 1) {
            callableMethod.put("move_mothernature", 1);
        }
    }

    public List<String> getOptions() {
        return new ArrayList<>(callableMethod.entrySet().stream()
                                                        .filter(x -> x.getValue() > 0)
                                                        .map(x -> x.getKey()).toList());
    }

}
