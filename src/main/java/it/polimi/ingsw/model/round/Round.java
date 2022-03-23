package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.AssistantCard;
import it.polimi.ingsw.model.component.card.CharacterCard;
import it.polimi.ingsw.model.round.component.*;
import it.polimi.ingsw.model.round.handler.Handler;

import java.util.List;

public class Round {

    private PlanningPhase planning;
    private ActionPhase action;
    private MotherNature motherNature;
    private List<Player> players;
    private List<Island> islands;
    private Player actual;
    private List<CharacterCard> characterCards;

    private Handler handler;

    public Round(List<Player> playerOrder, List<Island> islands, MotherNature motherNature, List<Cloud> clouds,
                 StudentBag bag, List<CharacterCard> characterCards) {
        this.players = playerOrder;
        this.motherNature = motherNature;
        this.planning = new PlanningPhase();
        this.action = new ActionPhase();
        this.actual = players.get(0);
        this.characterCards = characterCards;

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


}
