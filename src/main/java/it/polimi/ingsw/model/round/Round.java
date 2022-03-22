package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.round.handler.Handler;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private PlanningPhase planning;
    private ActionPhase action;
    private MotherNature motherNature;
    private List<Player> players;
    private List<Island> islands;
    private Player actual;

    private Handler handler;

    public Round(List<Player> playerOrder, List<Island> islands, MotherNature motherNature, List<Cloud> clouds, StudentBag bag) {
        this.players = playerOrder;
        this.motherNature = motherNature;
        this.planning = new PlanningPhase();
        this.action = new ActionPhase();
        this.actual = players.get(0);

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
            this.action.moveStudent(player, isDiningRoom, from_index, island);
        }
    }

    public void playCharacterCard(int player_id, int card_id, int steps_choice) {
        Player player = this.players.get(player_id);
        if (player.equals(this.actual)) {
            player.playCharacterCard(card_id);
            AssistantCard card = null; // TODO to be fixed --> pass card situations to Round()
            this.handler.motherNatureMovement(this.motherNature, card, steps_choice);
        }
    }


}
