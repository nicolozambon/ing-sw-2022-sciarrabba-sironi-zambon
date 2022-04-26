package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.AssistantCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlanningPhase {

    private Model model;
    private Player currentPlayer;
    private int callable;

    public PlanningPhase (Player currentPlayer, Model model) {
        this.model = model;
        this.currentPlayer = currentPlayer;
        this.callable = 1;
    }

    //TODO check if assistant card is already played;
    public void playAssistantCard(int choice) {
        if (!getAlreadyPlayedAssistantCard().contains(choice)) {
            model.playAssistantCard(this.currentPlayer.getId(), choice);
            callable--;
        }
    }

    private List<Integer> getAlreadyPlayedAssistantCard() {
        List<Integer> temp = new ArrayList<>();
        for(Player player : this.model.getController().getPlayersHavePlayed()) {
            temp.add(player.getLastAssistantCard().getValue());
        }
        return temp;
    }

    public boolean isEnded() {
        return this.callable == 0;
    }

}
