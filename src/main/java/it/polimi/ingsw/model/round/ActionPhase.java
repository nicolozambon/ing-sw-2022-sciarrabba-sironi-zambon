package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.Island;
import it.polimi.ingsw.model.component.Student;

public class ActionPhase {

    public ActionPhase() {

    }

    public void moveStudent(Player player, boolean isDiningRoom, Student student, Island island) {
        if (isDiningRoom) {
            player.getSchool().moveStudentDiningRoom(student);
        } else {
            player.getSchool().moveStudentIsland(student, island);
        }
    }

}
