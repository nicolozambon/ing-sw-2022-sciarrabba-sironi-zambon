package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.round.handler.Handler;

public class ActionPhase {

    public ActionPhase() {

    }

    public void moveStudent(Player player, boolean isDiningRoom, int from_index, Island island) {
        if (isDiningRoom) {
            player.school.moveStudentDiningRoom(from_index);
        } else {
            player.school.moveStudentIsland(from_index, island);
        }
    }

}
