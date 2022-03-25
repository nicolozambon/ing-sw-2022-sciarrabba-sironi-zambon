package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.CharacterCard;
import it.polimi.ingsw.model.exception.IllegalActionException;
import it.polimi.ingsw.model.round.handler.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO Use try and catch

//Class ActionPhase keep track of action for the player
public class ActionPhase {

    private Player actualPlayer;
    private List<Player> otherPlayers;
    private Map<String, Integer> callableMethod;
    Handler handler;

    public ActionPhase(Player player, List<Player> otherPlayers, int numOfStudentToMove) {
        this.actualPlayer = player;
        this.otherPlayers = otherPlayers;

        callableMethod = new HashMap<>(){{
            this.put("student_movement", numOfStudentToMove);
            this.put("mothernature_movement", 1);
            this.put("character_card", 1);
            this.put("resolve", 1);
            this.put("students_cloud", 1);
            this.put("extra_action", 1);
        }};

        handler = new Handler(player);
    }

    public void playCharacterCard(CharacterCard card) {
        if(callableMethod.get("character_card") > 0) {
            this.actualPlayer.playCharacterCard(card);
            callableMethod.put("character_card", callableMethod.get("character_card") - 1 );
        }
        //TODO HandlerFactory passing card create appropriate handler
    }

    public void moveStudentToDiningRoom(Student student, List<Coin> coinsSrc) {
        if(callableMethod.get("student_movement") > 0) {
            this.actualPlayer.moveStudentDiningRoom(student, coinsSrc);
            callableMethod.put("student_movement", callableMethod.get("student_movement") - 1 );
            professorControl();
        }
    }

    private void professorControl() {
        this.handler.professorControl(this.otherPlayers);
    }

    public void moveStudentToIsland(Student student, Island island) throws IllegalActionException {
        if(callableMethod.get("student_movement") > 0) {
            this.actualPlayer.moveStudentIsland(student, island);
            callableMethod.put("student_movement", callableMethod.get("student_movement") - 1 );
        } else throw new IllegalActionException();
    }

    public void moveMotherNature(MotherNature motherNature, int steps) {
        if(callableMethod.get("mothernature_movement") > 0) {
            this.handler.motherNatureMovement(this.actualPlayer, motherNature, steps);
            callableMethod.put("mothernature_movement", callableMethod.get("mothernature_movement") - 1 );
        }
    }

    public void resolveIsland(MotherNature motherNature) {
        if(callableMethod.get("resolve") > 0) {
            this.handler.resolveIsland(motherNature, otherPlayers);
            callableMethod.put("resolve", callableMethod.get("resolve") - 1 );
        }
    }

    public void getStudentsFromCloud(Cloud cloud) {
        if(callableMethod.get("students_cloud") > 0) {
            this.actualPlayer.getSchool().takeStudentsFromCloud(cloud);
            callableMethod.put("students_cloud", callableMethod.get("students_cloud") - 1 );
        }
    }

    public void extraAction() {
        if(callableMethod.get("students_cloud") > 0) {
            this.handler.extraAction();
            callableMethod.put("students_cloud", callableMethod.get("students_cloud") - 1 );
        }
    }

    public void endActionPhase() {
      //  return this.handler;
    }
}
