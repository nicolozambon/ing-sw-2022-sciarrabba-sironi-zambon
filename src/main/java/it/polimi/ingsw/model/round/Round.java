package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import it.polimi.ingsw.model.round.handler.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Round {

    private List<Player> players;
    private List<Island> islands;
    private List<Cloud> clouds;
    private MotherNature motherNature;
    private List<CharacterCard> characterCards;
    private List<Coin> coins;

    private PlanningPhase planning;
    private ActionPhase action;

    private Player actual;

    private Handler handler;

    public Round(List<Player> players, List<Island> islands, MotherNature motherNature, List<Cloud> clouds,
                 StudentBag bag) {
        this.players = players;
        this.motherNature = motherNature;
        this.planning = new PlanningPhase();
        this.action = new ActionPhase();
        this.actual = players.get(0);
        this.clouds = clouds;
        this.handler = new Handler();
        this.planning.addStudentsToCloud(clouds, bag);
    }

    public void playAssistantCardForPlayer(int player_id, int choice) {
        Player player = this.players.get(player_id);
        if (player.equals(this.actual)) {
            this.planning.playAssistantCard(player, choice);
        }
    }

    public void moveStudent(int player_id, boolean isDiningRoom, int from_index, int dest_index) {
        Player player = this.players.get(player_id);
        if (player.equals(this.actual)) {
            Island island = null;
            if (!isDiningRoom) { // move to island
                island = this.islands.get(dest_index);
            }
            Student student = player.getSchool().getEntrance().getPawns().get(from_index);
            this.action.moveStudent(player, isDiningRoom, student, island);
        }
    }

    public void playCharacterCard(int player_id, int card_id, int steps_choice) {
        Player player = this.players.get(player_id);
        if (player.equals(this.actual)) {
            player.playCharacterCard(characterCards.get(card_id)); //TODO methods outside of round accept only specific object
            this.handler.calculateInfluence();
            AssistantCard card = null; // TODO to be fixed --> pass card situations to Round()
            this.handler.motherNatureMovement(this.motherNature, card, steps_choice);
        }
    }

    private void orderPlayersForAction(){
        this.players =
                this.players.stream()
                .sorted(Comparator.comparingInt(x -> x.lastAssistantCard().getValue()))
                .collect(Collectors.toList());
    }

    private void orderPlayersForNextRound(){
        List<Player> temp = new ArrayList<>();
        orderPlayersForAction();

        temp.add(this.players.remove(0));
        temp.addAll(this.players
                    .stream()
                    .sorted(Comparator.comparingInt(x -> x.id))
                    .collect(Collectors.toList()));

        this.players = temp;
    }

}
