package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.CardException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.card.AssistantCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handling of the Planning Phase in the turn of the player
 */
public class PlanningPhase {
    /**
     * Current model, state of the game
     */
    private transient Model model;
    /**
     * Player currently playing.
     */
    private transient Player currentPlayer;
    /**
     * Map containing methods that can be called and played.
     */
    private final Map<String, Integer> callableMethod;

    /**
     * PlanningPhase constructor, initializes callableMethod to get the player to choose the Assistant Card
     * @param currentPlayer
     * @param model
     */
    public PlanningPhase (Player currentPlayer, Model model) {
        this.model = model;
        this.currentPlayer = currentPlayer;
        callableMethod = new HashMap<>(){{
            this.put("playAssistantCard", 1);
        }};
    }

    /**
     * Plays the chosen Assistant Card for the currentPlayer
     * @param choice chosen Assistant Card ID
     * @throws CardException if the card isn't playable or valid, or if the player cannot play any assistant card
     */
    public synchronized void playAssistantCard(int choice) throws CardException {
        if (callableMethod.get("playAssistantCard") > 0 && cardIsPlayable(choice)) {
            model.playAssistantCard(this.currentPlayer.getId(), choice);
            callableMethod.put("playAssistantCard", 0);
        } else {
            throw new CardException("Invalid assistant card played! Retry");
        }

    }

    /**
     * @return this player's played assistant card
     */
    private List<Integer> playedAssistantCard() {
        List<Integer> temp = new ArrayList<>();
        for(Player player : this.model.getController().getPlayersHavePlayed()) {
            temp.add(player.getLastAssistantCard().getValue());
        }
        return temp;
    }

    /**
     * @return true if exists a card that has not yet been played
     */
    private boolean existsNotPlayedCard() {
        List<Integer> temp = currentPlayer.getAssistantCards().stream().map(AssistantCard::getValue).toList();
        for (Integer i : temp) {
            if (!playedAssistantCard().contains(i)) return true;
        }
        return false;
    }

    /**
     * Answers if an Assistant Card is playable
     * @param choice AssistantCard to check
     * @return true if the card has not been yet played or if there are still playable cards available
     */
    private boolean cardIsPlayable(int choice) {
        return !playedAssistantCard().contains(choice) || !existsNotPlayedCard();
    }

    /**
     * @return true if the currentPlayer's Planning Phase is ended
     */
    public boolean isEnded() {
        for (String s : callableMethod.keySet()) {
            if (callableMethod.get(s) > 0) return false;
        }
        return true;
    }

    /**
     * @return list of options of playable actions
     */
    public List<String> getOptions() {
        return new ArrayList<>(callableMethod.entrySet().stream()
                .filter(x -> x.getValue() > 0)
                .map(Map.Entry::getKey).toList());
    }

    /**
     * Setter method for the model and currentPlayer
     * @param model model to set this.model to
     * @param currentPlayer currentPlayer to set this.currentPlayer to
     */
    protected void setModel(Model model, Player currentPlayer) {
        this.model = model;
        this.currentPlayer = currentPlayer;
    }

}
