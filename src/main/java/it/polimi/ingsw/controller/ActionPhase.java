package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.IllegalActionException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Class ActionPhase keep track of action for the player

public class ActionPhase {

    private Player currentPlayer;
    private List<Player> otherPlayers;
    private Map<String, Integer> callableMethod;

    private Model model;

    public ActionPhase(Player player, List<Player> otherPlayers, int numOfStudentToMove, Model model) {
        this.currentPlayer = player;
        this.otherPlayers = otherPlayers;
        this.model = model;

        callableMethod = new HashMap<>(){{
            this.put("student_movement", numOfStudentToMove);
            this.put("mothernature_movement", 1);
            this.put("character_card", 1);
            this.put("students_cloud", 1);
            this.put("extra_action", 0);
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
        if(callableMethod.get("student_movement") > 0) {
            this.model.moveStudentToDiningRoom(this.currentPlayer.getId(), choice);
            callableMethod.put("student_movement", callableMethod.get("student_movement") - 1);
        }
    }

    public void moveStudentToIsland(int studentChoice, int islandChoice) throws IllegalActionException {
        if(callableMethod.get("student_movement") > 0) {
            this.model.moveStudentToIsland(this.currentPlayer.getId(), studentChoice, islandChoice);
            callableMethod.put("student_movement", callableMethod.get("student_movement") - 1);
        } else throw new IllegalActionException();
    }

    public void moveMotherNature(int stepsChoice) {
        if(callableMethod.get("mothernature_movement") > 0) {
            this.model.moveMotherNature(this.currentPlayer.getId(), stepsChoice);
            callableMethod.put("mothernature_movement", callableMethod.get("mothernature_movement") - 1);
        }
    }

    private void resolveIsland(MotherNature motherNature) {

    }

    public void getStudentsFromCloud(int choice) {
        if(callableMethod.get("students_cloud") > 0) {
            this.model.takeStudentsFromCloud(this.currentPlayer.getId(), choice);
            callableMethod.put("students_cloud", callableMethod.get("students_cloud") - 1);
        }
    }

    public void extraAction(int ... values) {
        if(callableMethod.get("extra_action") > 0) {
            this.model.extraAction(values);
            callableMethod.put("extra_action", callableMethod.get("extra_action") - 1 );
        }
    }

    public boolean isEnded() {
      for (String s : callableMethod.keySet()) {
          if (callableMethod.get(s) > 0) return false;
      }
      this.model.resetHandler();
      return true;
    }

    public Player getPlayer () {
        return currentPlayer;
    }
}
