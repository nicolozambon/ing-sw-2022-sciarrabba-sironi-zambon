package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;


import java.util.List;

public class ActionPhase {

    private Player player;

    public ActionPhase(Player player) {
        this.player = player;
    }

    public void moveStudentToDiningRoom(Student student, List<Coin> coinsSrc) {
        this.player.moveStudentDiningRoom(student, coinsSrc);
    }

}
