package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.CardException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.card.AssistantCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanningPhase {

    private transient final Model model;
    private final Player currentPlayer;
    private final Map<String, Integer> callableMethod;

    public PlanningPhase (Player currentPlayer, Model model) {
        this.model = model;
        this.currentPlayer = currentPlayer;
        callableMethod = new HashMap<>(){{
            this.put("playAssistantCard", 1);
        }};
    }

    public synchronized void playAssistantCard(int choice) throws CardException {
        if (callableMethod.get("playAssistantCard") > 0 && cardIsPlayable(choice)) {
            model.playAssistantCard(this.currentPlayer.getId(), choice);
            callableMethod.put("playAssistantCard", 0);
        } else {
            throw new CardException("Invalid assistant card played! Retry");
        }

    }

    private List<Integer> playedAssistantCard() {
        List<Integer> temp = new ArrayList<>();
        for(Player player : this.model.getController().getPlayersHavePlayed()) {
            temp.add(player.getLastAssistantCard().getValue());
        }
        return temp;
    }

    private boolean existsNotPlayedCard() {
        List<Integer> temp = currentPlayer.getAssistantCards().stream().map(AssistantCard::getValue).toList();
        for (Integer i : temp) {
            if (!playedAssistantCard().contains(i)) return true;
        }
        return false;
    }

    private boolean cardIsPlayable(int choice) {
        return !playedAssistantCard().contains(choice) || !existsNotPlayedCard();
    }

    public boolean isEnded() {
        for (String s : callableMethod.keySet()) {
            if (callableMethod.get(s) > 0) return false;
        }
        return true;
    }

    public List<String> getOptions() {
        return new ArrayList<>(callableMethod.entrySet().stream()
                .filter(x -> x.getValue() > 0)
                .map(Map.Entry::getKey).toList());
    }

}
