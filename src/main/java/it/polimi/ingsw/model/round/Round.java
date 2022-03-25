package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


//Class Round keep players sorted and "mapping" integer to actual object

public class Round {

    private List<Player> playersToPlay;
    private List<Player> playersHavePlayed;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final MotherNature motherNature;
    private final List<CharacterCard> characterCards;
    private final List<Coin> coins;

    private boolean isPlanningFinished;

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
        this.coins = coins;
        this.numStudentToMove = numStudentToMove;

        this.planning = new PlanningPhase();
        this.isPlanningFinished = false;
        this.planning.addStudentsToCloud(this.clouds, bag);

    }

    public void playActionPhase(int player_id) {
        if (isPlanningFinished) {
            Player player = this.playersToPlay.get(player_id);
            if (player.equals(this.playersToPlay.get(0))) {
                List<Player> allPlayer = new ArrayList<>(playersToPlay);
                allPlayer.addAll(playersHavePlayed);
                this.action = new ActionPhase(player, allPlayer, numStudentToMove);
            }
        }
    }

    public void playAssistantCardForPlayer(int player_id, int choice) {
        if (!isPlanningFinished) {
            Player player = this.playersToPlay.get(player_id);
            if (player.equals(this.playersToPlay.get(0))) {
                this.planning.playAssistantCard(player, choice);
            }
            playersHavePlayed.add(playersToPlay.remove(0));
        }

        if (playersToPlay.size() == 0) {
            isPlanningFinished = true;
            orderPlayersForAction();
        }
    }





    private void orderPlayersForAction(){
        this.playersHavePlayed =
                this.playersHavePlayed.stream()
                .sorted(Comparator.comparingInt(x -> x.getLastAssistantCard().getValue()))
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
