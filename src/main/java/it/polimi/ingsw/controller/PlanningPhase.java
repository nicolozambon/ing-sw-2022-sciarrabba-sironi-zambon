package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanningPhase {

    private Model model;
    private Player currentPlayer;
    private Map<String, Integer> callableMethod;

    public PlanningPhase (Player currentPlayer, Model model) {
        this.model = model;
        this.currentPlayer = currentPlayer;
        callableMethod = new HashMap<>(){{
            this.put("assistant_card", 1);
        }};
    }

    public void playAssistantCard(int choice) throws InvalidCardException{
        if (callableMethod.get("assistant_card") > 0 && cardIsPlayable(choice)) {
            model.playAssistantCard(this.currentPlayer.getId(), choice);
            callableMethod.put("assistant_card", 0);
        } else {
            throw new InvalidCardException();
        }

    }

    private List<Integer> playedAssistantCard() {
        List<Integer> temp = new ArrayList<>();
        for(Player player : this.model.getController().getPlayersHavePlayed()) {
            temp.add(player.getLastAssistantCard().getValue());
        }
        return temp;
    }

    private boolean existsNotPlayedCard(int choice) {
        List<Integer> temp = currentPlayer.getAssistantCards().stream().map(x -> x.getValue()).toList();
        for (Integer i : temp) {
            if (!playedAssistantCard().contains(i)) return true;
        }
        return false;
    }

    private boolean cardIsPlayable(int choice) {
        if (!playedAssistantCard().contains(choice) || !existsNotPlayedCard(choice)) {
            return true;
        }
        return false;
    }

    public boolean isEnded() {
        for (String s : callableMethod.keySet()) {
            if (callableMethod.get(s) > 0) return false;
        }
        return true;
    }

    public Map<String, Integer> getOptions() {
        return new HashMap<>(callableMethod);
    }

}
