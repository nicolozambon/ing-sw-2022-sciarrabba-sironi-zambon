package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;

import java.util.List;

public class PlanningPhase {

    public void addStudentsToCloud(List<Cloud> clouds, StudentBag bag) {
        for (Cloud cloud : clouds) {
            bag.extractStudentAndMove(cloud);
        }
    }

    public void playAssistantCard(Player player, int choice) {
        player.playAssistantCard(choice);
    }

}
