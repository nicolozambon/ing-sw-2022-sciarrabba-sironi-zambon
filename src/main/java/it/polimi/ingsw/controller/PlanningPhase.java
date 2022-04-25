package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.List;

public class PlanningPhase {

    private Model model;

    public PlanningPhase (Model model) {
        this.model = model;
    }

    public void addStudentsToCloud(List<Cloud> clouds, StudentBag bag) {
        model.addStudentsToClouds();
    }

    //TODO check if assistant card is already taken;
    public void playAssistantCard(Player player, int choice) {
        model.playAssistantCard(player.getId(), choice);
    }

}
