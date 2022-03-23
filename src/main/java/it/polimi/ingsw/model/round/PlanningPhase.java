package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.component.Cloud;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.StudentBag;
import it.polimi.ingsw.model.component.card.AssistantCard;

import java.util.List;

public class PlanningPhase {

    public PlanningPhase() {

    }

    public void addStudentsToCloud(List<Cloud> clouds, StudentBag bag) {
        for (Cloud cloud : clouds) {
            bag.extractStudentAndMove(cloud);
        }
    }

    public AssistantCard playAssistantCard(Player player, int choice) {
        player.playAssistantCard(choice);
    }

}
