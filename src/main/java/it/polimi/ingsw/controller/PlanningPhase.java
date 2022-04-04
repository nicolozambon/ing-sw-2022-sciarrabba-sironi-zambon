package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

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
