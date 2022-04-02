package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.CharacterCard;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.handler.Handler;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Class ActionPhase keep track of action for the player

public class ActionPhase {

    private Player actualPlayer;
    private List<Player> otherPlayers;
    private Map<String, Integer> callableMethod;

    private Model model;


    public ActionPhase(Player player, List<Player> otherPlayers, int numOfStudentToMove, Model model) {
        this.actualPlayer = player;
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

    public void playCharacterCard() {
        if(callableMethod.get("character_card") > 0) {

            callableMethod.put("character_card", callableMethod.get("character_card") - 1);
            callableMethod.put("extra_action", 1);

        }
    }

    public void moveStudentToDiningRoom(Student student) {
        if(callableMethod.get("student_movement") > 0) {

            callableMethod.put("student_movement", callableMethod.get("student_movement") - 1);
        }
    }

    public void moveStudentToIsland(Student student, Island island) throws IllegalActionException {
        if(callableMethod.get("student_movement") > 0) {

            callableMethod.put("student_movement", callableMethod.get("student_movement") - 1);
        } else throw new IllegalActionException();
    }

    public void moveMotherNature(MotherNature motherNature, int steps) {
        if(callableMethod.get("mothernature_movement") > 0) {

            callableMethod.put("mothernature_movement", callableMethod.get("mothernature_movement") - 1);
        }
    }

    private void resolveIsland(MotherNature motherNature) {

    }

    public void getStudentsFromCloud(Cloud cloud) {
        if(callableMethod.get("students_cloud") > 0) {

            callableMethod.put("students_cloud", callableMethod.get("students_cloud") - 1);
        }
    }

    public void extraAction(Object obj) {
        if(callableMethod.get("extra_action") > 0) {

            callableMethod.put("extra_action", callableMethod.get("extra_action") - 1 );
        }
    }

    public boolean isEnded() {
      for (String s : callableMethod.keySet()) {
          if (callableMethod.get(s) > 0) return false;
      }

      return true;
    }

    public Player getPlayer () {
        return actualPlayer;
    }
}
