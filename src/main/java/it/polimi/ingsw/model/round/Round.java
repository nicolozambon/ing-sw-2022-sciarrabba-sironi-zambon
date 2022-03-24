package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Round {

    private List<Player> playersToPlay;
    private List<Player> playersHavePlayed;
    private List<Island> islands;
    private List<Cloud> clouds;
    private MotherNature motherNature;
    private List<CharacterCard> characterCards;
    private List<Coin> coins;

    private PlanningPhase planning;
    private ActionPhase action;

    private final int numStudentToMove;

    public Round(List<Player> playersToPlay, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
                 List<CharacterCard> characterCards, List<Coin> coins, StudentBag bag, int numStudentToMove) {

        this.playersToPlay = playersToPlay;
        this.playersHavePlayed = new ArrayList<>();
        this.islands = islands;
        this.clouds = clouds;
        this.motherNature = motherNature;
        this.characterCards = characterCards;
        this.numStudentToMove = numStudentToMove;

        this.planning = new PlanningPhase();
    }

    public void playActionPhase(int player_id) {
        Player player = this.playersToPlay.get(player_id);
        if (player.equals(this.playersToPlay.get(0))) {
            ActionPhase action = new ActionPhase(player);
        }
    }

    public void playAssistantCardForPlayer(int player_id, int choice) {
        Player player = this.playersToPlay.get(player_id);
        if (player.equals(this.playersToPlay.get(0))) {
            this.planning.playAssistantCard(player, choice);
        }
        playersHavePlayed.add(playersToPlay.remove(0));
    }



    private void orderPlayersForAction(){
        this.playersHavePlayed =
                this.playersHavePlayed.stream()
                .sorted(Comparator.comparingInt(x -> x.lastAssistantCard().getValue()))
                .collect(Collectors.toList());
        playersToPlay = playersHavePlayed;
        playersHavePlayed = new ArrayList<>();
    }

    private void orderPlayersForNextRound(){
        List<Player> temp = new ArrayList<>();
        orderPlayersForAction();

        temp.add(this.playersHavePlayed.remove(0));
        temp.addAll(this.playersHavePlayed
                    .stream()
                    .sorted(Comparator.comparingInt(x -> x.id))
                    .collect(Collectors.toList()));

        this.playersToPlay = temp;
    }

}
